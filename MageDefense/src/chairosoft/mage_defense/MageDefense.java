/* 
 * MageDefense.java 
 * MageDefense main and auxiliary methods
 * 
 */

package chairosoft.mage_defense;

import static chairosoft.quadrado.QCompassDirection.*;
import chairosoft.quadrado.*;
import chairosoft.ui.audio.*;
import chairosoft.ui.event.*;
import chairosoft.ui.geom.*;
import chairosoft.ui.graphics.*;

import java.io.*; 
import java.util.*;
import java.util.concurrent.*;

import chairosoft.desktop.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class MageDefense extends QApplication
{
	//
	// Main Method
	// 
	
	public static void main(String[] args)
	{
	    System.err.println("Starting game... ");
		MageDefense app = new MageDefense();
	    app.gameStart();
        
        DesktopDoubleBufferedUI dbui = (DesktopDoubleBufferedUI)app.getDbui();
        JPanel panel = dbui.getPanel();
        panel.addMouseListener(app.mouseAdapter);
        panel.addMouseMotionListener(app.mouseAdapter);
	}
    
    
    //
    // Constructor
    //
    
    public MageDefense() 
    {
        super("Mage Defense"); 
    }
    
    
    
    //
    // Constants
    //
    
    public static final String COORDINATE_FORMAT_STRING = "(%1$7.3f,%2$7.3f)";
    public static final int    TRANSPARENT              = Color.TRANSPARENT;
    
    @Override public int getPanelWidth() { return 576; }
    @Override public int getPanelHeight() { return 384; }
    
    public static final int X_SCALING = 1;
    public static final int Y_SCALING = 1;
    @Override public int getXScaling() { return X_SCALING; }
    @Override public int getYScaling() { return Y_SCALING; }
    
    public final int getClipWidth() { return (int)(this.getPanelWidth() / X_SCALING); }
    public final int getClipHeight() { return (int)(this.getPanelHeight() / Y_SCALING); }
    public final int getHalfClipWidth() { return this.getClipWidth() / 2; }
    public final int getHalfClipHeight() { return this.getClipHeight() / 2; }
    
    public static final String START_SCREEN_SPRITE_CODE = "Start_Screen_Sprite";
    public static final String BACKGROUND_SPRITE_CODE = "Background_Sprite";
    public static final String CROSSHAIR_SPRITE_CODE = "Crosshair_Sprite";
    public static final String MAGE_SPRITE_CODE = "RedMage_Sprite";
    public static final String GHOST_SPRITE_CODE = "Ghost_Enemy_Sprite";
    public static final String TREX_SPRITE_CODE = "TRex_Enemy_Sprite";
    
    public static final int LIFE_BAR_OUTLINE_COLOR = Color.create(0x007f00);
    public static final int LIFE_BAR_FILL_COLOR = Color.create(0x7fff7f);
    public static final double LIFE_FORCE_COUNT_AT_MAX = 100;
    public static final double LIFE_FORCE_BAR_MAX_WIDTH = 200;
    public static final double LIFE_FORCE_BAR_MAX_RATIO = LIFE_FORCE_BAR_MAX_WIDTH / LIFE_FORCE_COUNT_AT_MAX;
    public static final int LIFE_FORCE_BAR_HEIGHT = 8;
    
    
    //
    // Instance Variables
    //
    
    public enum GameState { GAME_NOT_LOADED, MAPROOM_LOADING, MAPROOM_LOADED, NORMAL_GAMEPLAY; }
    
    protected volatile GameState gameState = GameState.GAME_NOT_LOADED;
    
    protected QCompassKeypad keypad = new QCompassKeypad();
    
    protected volatile QMapRoom.MapLink currentMapLink = null;
    protected volatile QMapRoom nextQMapRoom = null;
    protected QMapRoom qmaproom = null;
    protected DrawingImage content = null;
    protected DrawingContext contentGraphics = null;
    protected volatile int clipX = 0;
    protected volatile int clipY = 0;
    
    protected volatile boolean isPaused = false;
    protected boolean show_boundaries = false;
    protected boolean show_bounding_box = false;
    protected boolean move_and_collide = true;
    
    protected QSprite startScreenSprite = new QSprite(START_SCREEN_SPRITE_CODE);
    protected QSprite backgroundSprite = new QSprite(BACKGROUND_SPRITE_CODE);
    
    protected QSprite mageSprite = new QSprite(MAGE_SPRITE_CODE);
    protected int spawnRow = 0;
    protected int spawnCol = 0;
    
    protected MageDefensePlayer player = new MageDefensePlayer();
    
    protected float CROSSHAIR_DISTANCE = 7.0f * QTileset.getTileWidth();
    protected QSprite crosshair = new QSprite(CROSSHAIR_SPRITE_CODE);
    
    protected Set<AttackSprite> attackSprites = new HashSet<>();
    
    protected String positionString = "";
    protected String velocityString = "";
    protected String accelerationString = "";
    protected String lastMoveString = "";
    protected boolean show_position = false;
    
    protected volatile ConcurrentLinkedQueue<MouseEvent> mouseEventQueue = new ConcurrentLinkedQueue<>();
    public final MouseAdapter mouseAdapter = new MouseAdapter()
    {
        @Override public void mouseMoved(MouseEvent e) { MageDefense.this.mouseEventQueue.offer(e); }        
        @Override public void mousePressed(MouseEvent e) { MageDefense.this.mouseEventQueue.offer(e); }
    };
    
    protected volatile ConcurrentLinkedQueue<KeyEvent> keyEventQueue = new ConcurrentLinkedQueue<>();
    public final KeyAdapter keyAdapter = new KeyAdapter()
    {
        @Override public void keyPressed(KeyEvent ke) { MageDefense.this.keyEventQueue.offer(ke); }
        @Override public void keyReleased(KeyEvent ke) { MageDefense.this.keyEventQueue.offer(ke); }
    };
    
    
    protected QSprite ghostSprite = new QSprite(GHOST_SPRITE_CODE);
    protected QSprite trexSprite1 = new QSprite(TREX_SPRITE_CODE);
    protected QSprite trexSprite2 = new QSprite(TREX_SPRITE_CODE);
    protected QSprite trexSprite3 = new QSprite(TREX_SPRITE_CODE);
    
    
    //
    // Instance Methods 
    //
    
    public void loadQMapRoomFromCurrentLink()
    {
        this.gameState = GameState.MAPROOM_LOADING;
        QMapRoomLoader loader = new QMapRoomLoader(
            currentMapLink, 
            new Functions.Consumer<QMapRoomLoader.Result>()
            {
                @Override public void accept(QMapRoomLoader.Result result)
                {
                    MageDefense thiz = MageDefense.this;
                    thiz.nextQMapRoom = result.qMapRoom;
                    thiz.spawnRow = result.spawnRow;
                    thiz.spawnCol = result.spawnCol;
                    thiz.content = result.contentImage;
                    thiz.contentGraphics = result.contentImageContext;
                    thiz.gameState = GameState.MAPROOM_LOADED;
                }
            }
        );
        loader.startLoading();
    }
    
    protected void loadInitialQMapRoom()
    {
        this.currentMapLink = new QMapRoom.MapLink(0, 0, "000", 11, 11);
        this.loadQMapRoomFromCurrentLink();
    }
    
    @Override
    protected void qGameInitialize() 
    {
        System.err.println("GAME INITIALIZED");
        player.chooseAbilityType(MageDefensePlayer.AbilityType.FIRE_ATTACK);
        //Loading.startVerbose();
    }
    
    @Override
    protected void qGameUpdateInit() { }
    
    @Override protected void qButtonPressed(ButtonEvent.Code buttonCode) { }
    @Override protected void qButtonHeld(ButtonEvent.Code buttonCode) { }
    @Override protected void qButtonReleased(ButtonEvent.Code buttonCode) { }
    @Override protected void qButtonNotHeld(ButtonEvent.Code buttonCode) { }
    
    
    protected void qKeyPressed(int keyCode)
    {
        // keypad needs to respond, even if loading
        switch (keyCode)
        {
            case KeyEvent.VK_LEFT: keypad.activateValue(WEST); break; // left
            case KeyEvent.VK_RIGHT: keypad.activateValue(EAST); break; // right
            case KeyEvent.VK_UP: keypad.activateValue(NORTH); break; // up
            case KeyEvent.VK_DOWN: keypad.activateValue(SOUTH); break; // down
        }
        
        switch (gameState)
        {
            case GAME_NOT_LOADED:
                switch (keyCode)
                {
                    case KeyEvent.VK_ENTER:
                        loadInitialQMapRoom();
                        break;
                }
                break;
            
            case NORMAL_GAMEPLAY:
                switch (keyCode)
                {
                    // normal gameplay
                    case KeyEvent.VK_Q: player.chooseAbilityType(MageDefensePlayer.AbilityType.FIRE_ATTACK); break;
                    case KeyEvent.VK_W: player.chooseAbilityType(MageDefensePlayer.AbilityType.WHIRLWIND_ATTACK); break;
                    case KeyEvent.VK_E: player.chooseAbilityType(MageDefensePlayer.AbilityType.BOLT_ATTACK); break;
                    case KeyEvent.VK_S: player.chooseAbilityType(MageDefensePlayer.AbilityType.SWORD_ATTACK); break;
                    
                    case KeyEvent.VK_2:
                    case KeyEvent.VK_NUMPAD2: player.addToLifeForceCurrent(-5); break;
                    
                    case KeyEvent.VK_8:
                    case KeyEvent.VK_NUMPAD8: player.addToLifeForceCurrent(+5); break;
                    
                    case KeyEvent.VK_4:
                    case KeyEvent.VK_NUMPAD4: player.addToLifeForceMax(-5); break;
                    
                    case KeyEvent.VK_6:
                    case KeyEvent.VK_NUMPAD6: player.addToLifeForceMax(+5); break;
                    
                    case KeyEvent.VK_ENTER:
                        this.isPaused = !this.isPaused;
                        break;
                    
                    // debug switches
                    case KeyEvent.VK_B: show_boundaries = !show_boundaries; break;
                    case KeyEvent.VK_X: show_bounding_box = !show_bounding_box; break;
                    case KeyEvent.VK_P: show_position = !show_position; break;
                    case KeyEvent.VK_M: move_and_collide = !move_and_collide; break;
                    
                    // debug actions
                    case KeyEvent.VK_SPACE: 
                        mageSprite.setPositionByQTile(this.spawnCol, this.spawnRow); 
                        ghostSprite.setPositionByQTile(-2, -2);
                        break;
                    default: break;
                }
                break;
        }
    }
    
    protected void qKeyReleased(int keyCode)
    {
        // keypad needs to respond, even if loading
        switch (keyCode)
        {
            case KeyEvent.VK_LEFT: keypad.deactivateValue(WEST); break; // left
            case KeyEvent.VK_RIGHT: keypad.deactivateValue(EAST); break; // right
            case KeyEvent.VK_UP: keypad.deactivateValue(NORTH); break; // up
            case KeyEvent.VK_DOWN: keypad.deactivateValue(SOUTH); break; // down
            default: break;
        }
        
        // switch (gameState)
        // {
            // case NORMAL_GAMEPLAY: break;
        // }
    }
    
    
    protected FloatPoint2D updateCrosshair(MouseEvent e)
    {
        java.awt.Point ep = e.getPoint();
        FloatPoint2D pp = mageSprite.getPosition();
        //Rectangle pb = mageSprite.getBounds();
        
        int mouseX = ep.x - clipX;
        int mouseY = ep.y - clipY;
        float mouseOffsetX = pp.x;//+ (pb.width / 2f);
        float mouseOffsetY = pp.y;// + (pb.height / 2f);
        FloatPoint2D mp = new FloatPoint2D(mouseX - mouseOffsetX, mouseY - mouseOffsetY);
        FloatPoint2D mp_unit = QPhysical2D.getUnitVector(mp);
        FloatPoint2D mp_ranged = new FloatPoint2D(mp_unit.x * CROSSHAIR_DISTANCE, mp_unit.y * CROSSHAIR_DISTANCE);
        //Rectangle cb = crosshair.getBounds();
        float dx = mouseOffsetX;// - (cb.width / 2f);
        float dy = mouseOffsetY;// - (cb.height / 2f);
        crosshair.setPosition(mp_ranged.x + dx, mp_ranged.y + dy);
        
        double t = Math.atan2(-mp_unit.y, mp_unit.x);
        
        String next_state = "left_basic";
        /*
        double increment = Math.PI / 2;
        double t1 = - Math.PI + (increment / 2);
        double t2 = t1 + increment;
        double t3 = t2 + increment;
        double t4 = t3 + increment;
        
        if (t1 <= t && t <= t2) { next_state = "down_basic"; }
        else if (t2 <= t && t <= t3) { next_state = "right_basic"; }
        else if (t3 <= t && t <= t4) { next_state = "up_basic"; }
        else { next_state = "left_basic"; }
        */
        
        if(mp.x > 0){ next_state = "right_basic"; }
        mageSprite.setCurrentState(next_state);
        
        return mp_unit;
    }
    
    protected void qMouseMoved(MouseEvent e)
    {
        switch (gameState)
        {
            case NORMAL_GAMEPLAY: this.updateCrosshair(e); break;
        }
    }
    
    protected void qMousePressed(MouseEvent e)
    {
        switch (gameState)
        {
            case GAME_NOT_LOADED:
                loadInitialQMapRoom();
                break;
            
            case NORMAL_GAMEPLAY:
                // get ability
                MageDefensePlayer.Ability ability = player.getChosenAbility();
                //System.err.println(ability);
                if (ability == null) { break; }
                
                // update crosshair
                FloatPoint2D mp_unit = this.updateCrosshair(e);
                if (0.0 == mp_unit.distance(0, 0)) { break; }
                
                // update life force
                if (player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT && ability.type != MageDefensePlayer.AbilityType.SWORD_ATTACK ) { break; }
                player.addToLifeForceCurrent(-1 * ability.cost);
                
                // add attack sprite
                AttackSprite nextAttackSprite = new AttackSprite(ability.type.spriteCode, ability.range, ability.clickLifeSpan);
                
                FloatPoint2D pp = mageSprite.getPosition();
                // Rectangle pb = mageSprite.getBounds();
                // Rectangle ab = nextAttackSprite.getBounds();
                // nextAttackSprite.setPosition(pp.x + ((pb.width - ab.width) / 2f), pp.y + ((pb.height - ab.height) / 2f));
                nextAttackSprite.setPosition(pp.x, pp.y);
                
                FloatPoint2D v = new FloatPoint2D(mp_unit.x * ability.velocity, mp_unit.y * ability.velocity);
                nextAttackSprite.setVelocity(v);
                
                if (ability.type == MageDefensePlayer.AbilityType.BOLT_ATTACK)
                {
                    double t = Math.atan2(-mp_unit.y, mp_unit.x);
                    
                    String next_state = "left_basic";
                    
                    double increment = Math.PI / 4;
                    double t1 = - Math.PI + (increment / 2);
                    double t2 = t1 + increment;
                    double t3 = t2 + increment;
                    double t4 = t3 + increment;
                    double t5 = t4 + increment;
                    double t6 = t5 + increment;
                    double t7 = t6 + increment;
                    double t8 = t7 + increment;
                    
                    if (t1 <= t && t <= t2) { next_state = "down_left_basic"; }
                    else if (t2 <= t && t <= t3) { next_state = "down_basic"; }
                    else if (t3 <= t && t <= t4) { next_state = "down_right_basic"; }
                    else if (t4 <= t && t <= t5) { next_state = "right_basic"; }
                    else if (t5 <= t && t <= t6) { next_state = "up_right_basic"; }
                    else if (t6 <= t && t <= t7) { next_state = "up_basic"; }
                    else if (t7 <= t && t <= t8) { next_state = "up_left_basic"; }
                    else { next_state = "left_basic"; }
                    
                    nextAttackSprite.setCurrentState(next_state);
                }
                else if (ability.type == MageDefensePlayer.AbilityType.SWORD_ATTACK)
                {
                    double t = Math.atan2(-mp_unit.y, mp_unit.x);
                    
                    String next_state = "left_basic";
                    
                    double increment = Math.PI / 2;
                    double t1 = - Math.PI + (increment / 2);
                    double t2 = t1 + increment;
                    double t3 = t2 + increment;
                    double t4 = t3 + increment;
                    
                    if (t1 <= t && t <= t2) { next_state = "down_basic"; }
                    else if (t2 <= t && t <= t3) { next_state = "right_basic"; }
                    else if (t3 <= t && t <= t4) { next_state = "up_basic"; }
                    else { next_state = "left_basic"; }
                    
                    nextAttackSprite.setCurrentState(next_state);
                    
                    FloatPoint2D ap = nextAttackSprite.getPosition();
                    nextAttackSprite.setPosition(ap.x + mp_unit.x * QTileset.getTileWidth() / 2, ap.y + mp_unit.y * QTileset.getTileWidth() / 2);
                }
                
                attackSprites.add(nextAttackSprite);
                
                break;
        }
    }
    
    @Override
    protected void qGameUpdate() 
    {
        // hook for mouse event handling
        int mouseEventCount = mouseEventQueue.size(); // only process mouse events in the queue as of this call
        for (int i = 0; i < mouseEventCount; ++i)
        {
            MouseEvent e = mouseEventQueue.poll();
            if (e != null) 
            {
                switch (e.getID())
                {
                    case MouseEvent.MOUSE_MOVED: this.qMouseMoved(e); break;
                    case MouseEvent.MOUSE_PRESSED: this.qMousePressed(e); break;
                }
            }
        }
        
        // hook for key event handling
        int keyEventCount = keyEventQueue.size(); // only process key events in the queue as of this call
        for (int i = 0; i < keyEventCount; ++i)
        {
            KeyEvent e = keyEventQueue.poll();
            if (e != null) 
            {
                switch (e.getID())
                {
                    case KeyEvent.KEY_PRESSED: this.qKeyPressed(e.getKeyCode()); break;
                    case KeyEvent.KEY_RELEASED: this.qKeyReleased(e.getKeyCode()); break;
                }
            }
        }
        
        
        
        // rest of qGameUpdate logic
        switch (gameState)
        {
            case GAME_NOT_LOADED: 
                break;
                
            case MAPROOM_LOADING: 
                break;
                
            case MAPROOM_LOADED: 
                {
                    qmaproom = nextQMapRoom;
                    gameState = GameState.NORMAL_GAMEPLAY;
                    mageSprite.setPositionByQTile(this.spawnCol, this.spawnRow);
                    
                    ghostSprite.setCurrentState("right_basic");
                    ghostSprite.setPositionByQTile(-2, -2);
                    FloatPoint2D pp = mageSprite.getPosition();
                    FloatPoint2D gp = ghostSprite.getPosition();
                    FloatPoint2D difference = new FloatPoint2D(pp.x - gp.x, pp.y - gp.y);
                    FloatPoint2D differenceUnit = QPhysical2D.getUnitVector(difference);
                    ghostSprite.setVelocity(0.8f * differenceUnit.x, 0.8f * differenceUnit.y);
                    
                    trexSprite1.setPositionByQTile(18, 4);
                    trexSprite1.setCurrentState("right_basic");
                }
                break;
                
            case NORMAL_GAMEPLAY: 
                // TODO: set direction based on keypad compass direction
                ////mageSprite.setDirection(keypad.getDirection()); //// TODO: this
                
                // collision
                if (move_and_collide)
                {
                    // update life force
                    player.addToLifeForceCurrent(player.regenAmount / player.framesBetweenRegen);
                    
                    
                    ////mageSprite.moveOneFrameIn(qmaproom); //// TODO: this
                    /*currentMapLink = mageSprite.getMapLinkOrNullFrom(qmaproom);
                    if (currentMapLink != null)
                    {
                        //System.err.println(currentMapLink);
                        this.loadQMapRoomFromCurrentLink();
                        break; // break from the switch statement
                    }*/
                    //mageSprite.resolveCollisionInQMapRoom(qmaproom, true, true);
                    
                    
                    // get mageSprite info
                    FloatPoint2D pp = mageSprite.getPosition();
                    
                    // update ghost sprite
                    ghostSprite.moveOneFrame();
                    
                    // update fire sprites
                    ArrayList<AttackSprite> attackSpritesToRemove = new ArrayList<>();
                    for (AttackSprite qs : attackSprites)
                    {
                        qs.moveOneFrame();
                        
                        Point2D qp = qs.getPosition();
                        double qpDistance = pp.distance(qp);
                        if ((qpDistance > qs.range) || qs.lifeSpanExceeded())
                        {
                            attackSpritesToRemove.add(qs);
                        }
                    }
                    
                    for (AttackSprite qs : attackSpritesToRemove)
                    {
                        attackSprites.remove(qs);
                    }
                    
                    attackSpritesToRemove.clear();
                }
                
                
                if (show_position)
                {
                    FloatPoint2D pp = mageSprite.getPosition();
                    FloatPoint2D pv = mageSprite.getVelocity();
                    FloatPoint2D pa = mageSprite.getAcceleration();
                    FloatPoint2D pl = mageSprite.getLastMove();
                    
                    positionString = String.format(COORDINATE_FORMAT_STRING, pp.x, pp.y);
                    velocityString = String.format(COORDINATE_FORMAT_STRING, pv.x, pv.y);
                    accelerationString = String.format(COORDINATE_FORMAT_STRING, pa.x, pa.y);
                    lastMoveString = String.format(COORDINATE_FORMAT_STRING, pl.x, pl.y);
                }
                break;
        }
    }
    
    @Override
    protected void qGameRender(DrawingContext ctx) 
    {
        // save current graphics settings
        int ctxColor = ctx.getColor();
        Font ctxFont = ctx.getFont();
        
        try
        {
            switch (gameState)
            {
                case GAME_NOT_LOADED: 
                {
                    // start screen sprite
                    startScreenSprite.drawToContext(ctx, 0, 0);
                    
                    // cycle bar
                    ctx.setColor(Color.BLACK);
                    IntPoint2D cb = new IntPoint2D(7, 20);
                    ctx.drawRect(cb.x, cb.y, 102, 8); // outline
                    ctx.fillRect(cb.x + 1 + (int)(framesElapsedTotal % 100), cb.y, 2, 8); // cycle
                    break;
                }
                case MAPROOM_LOADING: 
                {
                    // cycle bar
                    ctx.setColor(Color.RED);
                    IntPoint2D cb = new IntPoint2D(7, 12);
                    ctx.drawRect(cb.x, cb.y, 102, 8); // outline
                    ctx.fillRect(cb.x + 1 + (int)(framesElapsedTotal % 100), cb.y, 2, 8); // cycle
                    break;
                }
                case MAPROOM_LOADED: 
                    break;
                    
                case NORMAL_GAMEPLAY: 
                {
                    // background 
                    // this is taken care of in QPanel class (using Color.WHITE)
                    
                    // content graphics 
                    IntPoint2D p = mageSprite.getIntPosition();
                    clipX = this.getPanelHalfWidth() - (int)(p.x * X_SCALING);
                    clipY = this.getPanelHalfHeight() - (int)(p.y * Y_SCALING);
                    this.drawNormalGameplayContent(contentGraphics, p.x, p.y);
                    ctx.drawImage(content, clipX, clipY);
                    
                    // // cycle bar
                    // ctx.setColor(Color.BLUE);
                    // IntPoint2D cb = new IntPoint2D(7, 4);
                    // ctx.drawRect(cb.x, cb.y, 102, 8); // outline
                    // ctx.fillRect(cb.x + 1 + (int)(framesElapsedTotal % 100), cb.y, 2, 8); // cycle
                    
                    // life force bar
                    IntPoint2D lfb = new IntPoint2D(7, 7);
                    double lifeForceMax = player.getLifeForceMax();
                    double lifeForceCurrent = player.getLifeForceCurrent();
                    double lifeForcePercent = player.getLifeForcePercent();
                    int lifeForceBarOutlineWidth = (int)(lifeForceMax * LIFE_FORCE_BAR_MAX_RATIO);
                    int lifeForceBarFillWidth = (int)(lifeForceCurrent * LIFE_FORCE_BAR_MAX_RATIO);
                    ctx.setColor(player.getStatus().barOutlineColorCode);
                    ctx.drawRect(lfb.x, lfb.y, lifeForceBarOutlineWidth + 1, LIFE_FORCE_BAR_HEIGHT + 1);
                    ctx.setColor(player.getStatus().barFillColorCode);
                    ctx.fillRect(lfb.x + 1, lfb.y + 1, lifeForceBarFillWidth, LIFE_FORCE_BAR_HEIGHT);
                    
                    ctx.setColor(Color.BLACK);
                    String lifeForceString = String.format("%04d / %04d Life Force", (int)lifeForceCurrent, (int)lifeForceMax);
                    ctx.drawString(lifeForceString, lfb.x, lfb.y + LIFE_FORCE_BAR_HEIGHT + 22);
                    if (player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT)
                    {
                        ctx.drawString("BURNOUT", lfb.x, lfb.y + LIFE_FORCE_BAR_HEIGHT + 42);
                    }
                    
                    // // mouse position
                    // ctx.setColor(Color.BLACK);
                    // ctx.drawString(mousePositionString, cb.x, cb.y + 30);
                    // ctx.drawString(crosshairPositionString, cb.x, cb.y + 50);
                    break;
                }
            }
        }
        finally
        {
            // put back graphics settings
            ctx.setFont(ctxFont);
            ctx.setColor(ctxColor);
        }
    }
    
    private void drawNormalGameplayContent(DrawingContext ctx, int playerX, int playerY)
    {
		// save current graphics settings
        int ctxColor = ctx.getColor();
        Font ctxFont = ctx.getFont();
        
        try
        {
        	// background and qmaproom
        	ctx.setColor(qmaproom.getBackgroundColor());
        	ctx.fillRect(0, 0, qmaproom.getWidthPixels(), qmaproom.getHeightPixels());
			//qmaproom.drawToContext(ctx, 0, 0, null);
			backgroundSprite.drawToContext(ctx, 0, 0);
			
			// player sprite
			//mageSprite.advanceAnimationOneClick();
			mageSprite.drawToContextAtOwnPosition(ctx);
			
            // ghost sprite
            ghostSprite.advanceAnimationOneClick();
            ghostSprite.drawToContextAtOwnPosition(ctx);
            
            // trex sprite
            trexSprite1.advanceAnimationOneClick();
            trexSprite1.drawToContextAtOwnPosition(ctx);
            
            // attack sprites
            for (QSprite qs : attackSprites)
            {
                qs.advanceAnimationOneClick();
                qs.drawToContextAtOwnPosition(ctx);
            }
            
            // crosshair
            crosshair.advanceAnimationOneClick();
            crosshair.drawToContextAtOwnPosition(ctx);
			
			// boundaries
			if (show_bounding_box) 
            {
                ctx.setColor(Color.BLUE); 
                ctx.fillPolygon(mageSprite); 
                for (QSprite qs : attackSprites) { ctx.fillPolygon(qs); }
            }
            
            //// TODO: collisions between attacks and enemies ???
			// if (show_boundaries) 
            // {
                // mageSprite.drawCollidingBoundariesToGraphics(ctx, qmaproom, Color.RED); 
                // for (QSprite qs : fireAttackSprites) { qs.drawCollidingBoundariesToGraphics(ctx, qmaproom, Color.RED); }
            // }
			
			
			// // // position info
			// // if (show_position)
			// // {
				// // Point p = mageSprite.getIntPosition();
				// // p.translate(20, 36);
				
                // // ctx.setColor(new Color(0x7fffffff, true));
				// // ctx.fillRect(p.x, p.y - 20, 160, 160);
                
				// // ctx.setColor(Color.BLACK);
				// // ctx.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
				// // ctx.drawString(positionString, p.x, p.y);
				// // ctx.drawString(velocityString, p.x, p.y + 20);
				// // ctx.drawString(accelerationString, p.x, p.y + 40);
				// // ctx.drawString(lastMoveString, p.x, p.y + 60);
				// // //ctx.drawString(mageSprite.getCurrentDirection().toString(), p.x, p.y + 80);
				// // //ctx.drawString(mageSprite.getVelocityDirection().toString(), p.x, p.y + 100);
				// // //ctx.drawString(mageSprite.pointList(), p.x, p.y + 120);
				// // //ctx.drawString(mageSprite.vertexList().toString(), p.x, p.y + 140);
			// // }
		}
        catch (Exception ex)
        {
            System.err.println(ex);
        }
        finally
        {
			// put back graphics settings
			ctx.setFont(ctxFont);
			ctx.setColor(ctxColor);
        }
    }
    
    @Override
    protected void qGameFinish()
    {
        System.err.println("GAME FINISHED");
    }
    
}