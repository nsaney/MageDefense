/* 
 * MageDefense.java 
 * MageDefense main and auxiliary methods
 * 
 */

package chairosoft.mage_defense;

import static chairosoft.quadrado.QCompassDirection.*;
import chairosoft.quadrado.*;
import chairosoft.quadrado.ui.event.*;

import chairosoft.ui.audio.*;
import chairosoft.ui.geom.*;
import chairosoft.ui.graphics.*;
import chairosoft.util.function.*;

import java.io.*; 
import java.util.*;
import java.util.concurrent.*;

import chairosoft.quadrado.desktop.DesktopDoubleBufferedUI;
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
        panel.addKeyListener(app.keyAdapter);
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
    public static final Font gameOverFont = new Font(Font.Family.MONOSPACED, Font.Style.BOLD, 48);
    public static final String COORDINATE_FORMAT_STRING = "(%1$7.3f,%2$7.3f)";
    public static final int    TRANSPARENT              = Color.TRANSPARENT;
    
    
    @Override public int getPanelWidth() { return 768; }
    @Override public int getPanelHeight() { return 432; }
    
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
    int killScore = 0;
    int wave = 1;
    
    protected final GameState GAME_NOT_LOADED = new GameNotLoaded(this);
    protected final GameState MAPROOM_LOADING = new MapRoomLoading(this);
    protected final GameState MAPROOM_LOADED = new MapRoomLoaded(this);
    protected final GameState NORMAL_GAMEPLAY = new NormalGameState(this);
    
    protected volatile GameState gameState = this.GAME_NOT_LOADED;
    
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
    protected Set<QSprite> enemySprites = new HashSet<>();
    
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
    
    //
    // Instance Methods 
    //
    
    
    public long getFramesElapsedTotal() { return this.framesElapsedTotal; }
    
    public void loadQMapRoomFromCurrentLink()
    {
        this.gameState = this.MAPROOM_LOADING;
        QMapRoomLoader loader = new QMapRoomLoader(
            currentMapLink, 
            new Consumer<QMapRoomLoader.Result>()
            {
                @Override public void accept(QMapRoomLoader.Result result)
                {
                    MageDefense thiz = MageDefense.this;
                    thiz.nextQMapRoom = result.qMapRoom;
                    thiz.spawnRow = result.spawnRow;
                    thiz.spawnCol = result.spawnCol;
                    thiz.content = result.contentImage;
                    thiz.contentGraphics = result.contentImageContext;
                    thiz.gameState = thiz.MAPROOM_LOADED;
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
        
        gameState.keyPressed(keyCode);
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
        gameState.keyReleased(keyCode);
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
        FloatPoint2D mp_unit = mp.getUnitVector();
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
        gameState.mouseMoved(e);
    }
    
    protected void qMousePressed(MouseEvent e)
    {
        gameState.mousePressed(e);
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
        gameState.update();
    }
    
    @Override
    protected void qGameRender(DrawingContext ctx) 
    {
        // save current graphics settings
        int ctxColor = ctx.getColor();
        Font ctxFont = ctx.getFont();
        
        try
        {
			gameState.render(ctx);
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