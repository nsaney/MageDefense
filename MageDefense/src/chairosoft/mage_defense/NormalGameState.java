/* 
 * Richard Saney 
 * 
 * Created: May, 2015
 * 
 * NormalGameState.java
 * NormalGameState class definition
 * 
 */


package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QTileset;

import chairosoft.ui.geom.Point2D;
import chairosoft.ui.geom.FloatPoint2D;
import chairosoft.ui.geom.IntPoint2D;
import chairosoft.ui.geom.Polygon;
import chairosoft.ui.geom.Rectangle;
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;
import chairosoft.ui.graphics.DrawingImage;
import chairosoft.ui.graphics.Font;

import chairosoft.util.Loading;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NormalGameState extends GameState
{
    // constants
    public static final String CROSSHAIR_SPRITE_CODE = "Crosshair_Sprite";
    public static final String MAGE_SPRITE_CODE = "GreenMage_Sprite";
    
    public static final int LIFE_BAR_OUTLINE_COLOR = Color.create(0x007f00);
    public static final int LIFE_BAR_FILL_COLOR = Color.create(0x7fff7f);
    public static final double LIFE_FORCE_COUNT_AT_MAX = 100;
    public static final double LIFE_FORCE_BAR_MAX_WIDTH = 200;
    public static final double LIFE_FORCE_BAR_MAX_RATIO = LIFE_FORCE_BAR_MAX_WIDTH / LIFE_FORCE_COUNT_AT_MAX;
    public static final int LIFE_FORCE_BAR_HEIGHT = 8;
    
    public static final Font GAME_OVER_FONT = Font.create(Font.Family.MONOSPACED, Font.Style.BOLD, 48);
    
    //public static final Font FF_FONT = Font.createFromEmbeddedFont("finalf.ttf");
    public static final Font FF_FONT = Font.createFromEmbeddedFont("uq_0.ttf");

    public static final Font FF_FONT_BOLD = FF_FONT.derive(Font.Style.BOLD | Font.Style.ITALIC, 48);
    
    // fields
    public final DrawingImage backgroundImage = Loading.getImage("/img/bg/BackgroundMageDefense.png");
    public final int backgroundImageX = (this.md.getPanelWidth() - this.backgroundImage.getWidth()) / 2;
    public final int backgroundImageY = (this.md.getPanelHeight() - this.backgroundImage.getHeight()) / 2;
    
    public int killScore = 0;
    public int wave = 1;
    public int round = 0;
    public int showNextRound = 0;
    
    protected volatile boolean isPaused = false;
    protected boolean show_boundaries = false;
    protected boolean show_bounding_box = false;
    protected boolean move_and_collide = true;
    
    protected Rectangle debugButton = new Rectangle();
    protected Rectangle resetButton = new Rectangle();
    
    
    protected MageDefensePlayer player = new MageDefensePlayer();
    {
        this.player.chooseAbilityType(MageDefensePlayer.AbilityType.FIRE_ATTACK);
    }
    protected QSprite mageSprite = new QSprite(MAGE_SPRITE_CODE);
    {
        float x = (this.md.getPanelWidth() - this.mageSprite.getImage().getWidth()/2) / 2;
        float y = (this.md.getPanelHeight() - this.mageSprite.getImage().getHeight()/2) / 2;
        this.mageSprite.setPosition(x, y);
    }
    
    protected float CROSSHAIR_DISTANCE = 7.0f * QTileset.getTileWidth();
    protected QSprite crosshair = new QSprite(CROSSHAIR_SPRITE_CODE);
    {
        this.crosshair.setPosition(-CROSSHAIR_DISTANCE, -CROSSHAIR_DISTANCE);
    }
    
    protected Set<AttackSprite> attackSprites = new HashSet<>();
    protected Set<Enemy> enemySprites = new HashSet<>();
    
    //ElementIcons
    protected final int ICON_WIDTH = 54;
    protected final int ICON_HEIGHT = 54;
    public Rectangle attackChoice = new Rectangle(ICON_WIDTH + 2, ICON_HEIGHT + 2);
	protected final QSprite[] swordIcons = {new QSprite("Sword_Icon"), new QSprite("Sword_Icon")};
	protected final QSprite[] fireIcons = {new QSprite("Fire_Icon"), new QSprite("Fire_Icon")};
	protected final QSprite[] windIcons = {new QSprite("Wind_Icon"), new QSprite("Wind_Icon")};
	protected final QSprite[] lightningIcons = {new QSprite("Lightning_Icon"), new QSprite("Lightning_Icon"), new QSprite("Lightning_Icon")};
    protected final QSprite[][] elementIcons = {swordIcons, fireIcons, windIcons, lightningIcons};
    {
		for(int row = 0; row < elementIcons.length; row++)
		{
			for(int col = 0; col < elementIcons[row].length; col++)
			{
				//System.err.println(row+", "+col+" "+elementIcons[row][col].code);
				elementIcons[row][col].setPosition(((this.md.getPanelWidth() + this.backgroundImage.getWidth()) / 2) + (ICON_WIDTH * col) + 3,
												   ((this.md.getPanelHeight() - this.backgroundImage.getHeight()) / 2) + (ICON_HEIGHT * row));
				if(col == 1)
				{
					elementIcons[row][col].setCurrentStateCode("level2");
				}
				else if (col == 2)
				{
					elementIcons[row][col].setCurrentStateCode("level3");
				}
			}
		}
    }
	// constructor
	public NormalGameState(MageDefense md)
	{
		super(md);
	}

	// overridden methods
    
    
    // TODO
    // TODO: move this into pointerPressed code
	// TODO
    
    
    // // @Override
	// // public void keyPressed(int keyCode)
	// // {
		// // switch (keyCode)
		// // {
			// // // normal gameplay
			// // //Changing attack types
			// // case KeyEvent.VK_Q: this.player.chooseAbilityType(MageDefensePlayer.AbilityType.FIRE_ATTACK); break;
			// // case KeyEvent.VK_W: this.player.chooseAbilityType(MageDefensePlayer.AbilityType.WHIRLWIND_ATTACK); break;
			// // case KeyEvent.VK_E: this.player.chooseAbilityType(MageDefensePlayer.AbilityType.BOLT_ATTACK); break;
			// // case KeyEvent.VK_S: this.player.chooseAbilityType(MageDefensePlayer.AbilityType.SWORD_ATTACK); break;
			
			// // case KeyEvent.VK_2:
			// // case KeyEvent.VK_NUMPAD2: this.player.addToLifeForceCurrent(-5); break;
			
			// // case KeyEvent.VK_8:
			// // case KeyEvent.VK_NUMPAD8: this.player.addToLifeForceCurrent(+5); break;
			
			// // case KeyEvent.VK_4:
			// // case KeyEvent.VK_NUMPAD4: this.player.addToLifeForceMax(-5); break;
			
			// // case KeyEvent.VK_6:
			// // case KeyEvent.VK_NUMPAD6: this.player.addToLifeForceMax(+5); break;
			
			// // case KeyEvent.VK_ENTER:
				// // this.isPaused = !this.isPaused;
				// // break;
			
			// // // debug switches
			// // case KeyEvent.VK_B: this.show_bounding_box = !this.show_bounding_box; break;
			// // case KeyEvent.VK_M: this.move_and_collide = !this.move_and_collide; break;
			
			// // default: break;
		// // }
	// // }
	
	@Override
	public void pointerMoved(float x, float y) 
    {
        this.updateCrosshair(x, y);
    }
    
    protected FloatPoint2D updateCrosshair(float mouseX, float mouseY)
    {
        FloatPoint2D pp = this.mageSprite.getPosition();
        //Rectangle pb = mageSprite.getBounds();
        
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
        
        String next_state = "Idle_Left";
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
        
        if(mp.x > 0){ next_state = "Idle_Right"; }
        mageSprite.setCurrentStateCode(next_state);
        
        return mp_unit;
    }
	
	@Override
	public void pointerPressed(float x, float y)
	{
        // buttons
        if (this.debugButton.containsPoint(x, y))
        {
            this.show_bounding_box = !this.show_bounding_box;
            return;
        }
        
        if (this.player.getStatus() == MageDefensePlayer.PlayerStatus.DEAD || this.player.roundsCompleted)
        {
            if (this.resetButton.containsPoint(x, y))
            {
                this.md.gameState = new NormalGameState(this.md);
                return;
            }
        }
        else
        {
            for (QSprite qs : this.swordIcons)
            {
                if (qs.containsPoint(x, y)) 
                {
                    this.player.chooseAbilityType(MageDefensePlayer.AbilityType.SWORD_ATTACK); 
                    return; 
                } 
            }
            for (QSprite qs : this.fireIcons) 
            {
                if (qs.containsPoint(x, y)) 
                {
                    this.player.chooseAbilityType(MageDefensePlayer.AbilityType.FIRE_ATTACK); 
                    return; 
                } 
            }
            for (QSprite qs : this.windIcons) 
            {
                if (qs.containsPoint(x, y)) 
                {
                    this.player.chooseAbilityType(MageDefensePlayer.AbilityType.WHIRLWIND_ATTACK); 
                    return; 
                } 
            }
            for (QSprite qs : this.lightningIcons) 
            {
                if (qs.containsPoint(x, y)) 
                {
                    this.player.chooseAbilityType(MageDefensePlayer.AbilityType.BOLT_ATTACK); 
                    return; 
                } 
            }
        }
        
		if (this.move_and_collide && this.player.getStatus() != MageDefensePlayer.PlayerStatus.DEAD)
		{
            // get ability
            MageDefensePlayer.Ability ability = this.player.getChosenAbility();
            //System.err.println(ability);
            if (ability == null) { return; }
            
            // update crosshair
            FloatPoint2D mp_unit = this.updateCrosshair(x, y);
            if (0.0 == mp_unit.distance(0, 0)) { return; }
            
            // update life force
            if (this.player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT 
                && ability.type != MageDefensePlayer.AbilityType.SWORD_ATTACK ) { return; }
            this.player.addToLifeForceCurrent(-1 * ability.cost);
            
    
            // add attack sprite
            AttackSprite nextAttackSprite = new AttackSprite(ability.type.spriteCode, ability.range, ability.clickLifeSpan, ability.flinchFrames);
        
            FloatPoint2D pp = this.mageSprite.getPosition();
            Polygon.Bounds pb = this.mageSprite.getBounds();
            Polygon.Bounds ab = nextAttackSprite.getBounds();
            nextAttackSprite.setPosition(pp.x + ((pb.width - ab.width) / 2f), pp.y + ((pb.height - ab.height) / 2f));
        
            FloatPoint2D v = new FloatPoint2D(mp_unit.x * ability.velocity, mp_unit.y * ability.velocity);
            nextAttackSprite.setVelocity(v);
            
            double increment = Math.PI / 2;
            double t1 = - Math.PI /*+ (increment / 2)*/ ;
            double t2 = t1 + increment;
            double t3 = t2 + increment;
            double t4 = t3 + increment;
            double t = Math.atan2(-mp_unit.y, mp_unit.x);      
            
            String mage_state = "left_basic";
            
            if (ability.type == MageDefensePlayer.AbilityType.SWORD_ATTACK)
            {           
                mage_state = (t2 <= t && t <= t4)? "Sword_Right": "Sword_Left";
            
                /*
                if (t1 <= t && t <= t2) { next_state = "down_basic"; }
                else if (t2 <= t && t <= t3) { next_state = "right_basic"; }
                else if (t3 <= t && t <= t4) { next_state = "up_basic"; }
                else { next_state = "left_basic"; }
                */                
            
                this.mageSprite.setCurrentStateCode(mage_state);
            
                FloatPoint2D ap = nextAttackSprite.getPosition();

                nextAttackSprite.setPosition(ap.x + mp_unit.x * QTileset.getTileWidth(), ap.y + mp_unit.y * QTileset.getTileWidth());
                
            }
            else
            {
                mage_state = (t2 <= t && t <= t4)? "Hand_Right": "Hand_Left";
                
                //reuse t regions
                increment = Math.PI / 4;
                t1 = - Math.PI + (increment / 2);
                t2 = t1 + increment;
                t3 = t2 + increment;
                t4 = t3 + increment;
                double t5 = t4 + increment;
                double t6 = t5 + increment;
                double t7 = t6 + increment;
                double t8 = t7 + increment;
                if (ability.type == MageDefensePlayer.AbilityType.BOLT_ATTACK)
                {              
                    String next_state = "left_basic";                  
                    if (t1 <= t && t <= t2) { next_state = "down_left_basic"; }
                    else if (t2 <= t && t <= t3) { next_state = "down_basic"; }
                    else if (t3 <= t && t <= t4) { next_state = "down_right_basic"; }
                    else if (t4 <= t && t <= t5) { next_state = "right_basic"; }
                    else if (t5 <= t && t <= t6) { next_state = "up_right_basic"; }
                    else if (t6 <= t && t <= t7) { next_state = "up_basic"; }
                    else if (t7 <= t && t <= t8) { next_state = "up_left_basic"; }
                    else { next_state = "left_basic"; }
                            
                    nextAttackSprite.setCurrentStateCode(next_state);
                    
                }
            this.mageSprite.setCurrentStateCode(mage_state);
            }
            nextAttackSprite.setOrigin(nextAttackSprite.getPosition());
            this.attackSprites.add(nextAttackSprite);
        }
	}
    
    @Override
    public void pointerReleased(float x, float y)
    {
        // nothing here
    }
	
	@Override
	public void update()
	{
		if (this.enemySprites.size() <= 1)
		{
			EnemyWave.initialize(this);
		}
		
		// collision
		if (this.move_and_collide && this.player.getStatus() != MageDefensePlayer.PlayerStatus.DEAD)
		{
			// update life force
			this.player.addToLifeForceCurrent(this.player.regenAmount / this.player.framesBetweenRegen);
            
			
			// get mageSprite info
			FloatPoint2D pp = this.mageSprite.getPosition();
			
			for (Enemy enemy : this.enemySprites)
			{
				if(enemy.getVelocity().x >= 0) 
				{ 
					enemy.setCurrentStateCode("right_basic"); 
				}
				else
				{
					enemy.setCurrentStateCode("left_basic");
				}
				
				//Spiral here
				if(enemy.isOutOfRectangle(this.backgroundImageX - 32, 
				                          this.backgroundImageX + this.backgroundImage.getWidth() + 32,
				                          this.backgroundImageY - 32,
				                          this.backgroundImageY + this.backgroundImage.getHeight() + 32))
				{
				    FloatPoint2D enemPos = enemy.getPosition();
				    FloatPoint2D difference = new FloatPoint2D(pp.x - enemPos.x, pp.y - enemPos.y);
			        enemy.setVelocity(difference.getUnitVector().multipliedBy(0.8f));
			        enemy.initializePosition(enemPos);
				}
				
				FloatPoint2D enemyPos = enemy.getPosition();
				double distance = pp.distance(enemyPos);
				//update if enemy not 
                if(enemy.getFlinchCount() == 0)
                {			
                    // update enemy sprite only when at least 25 units away (not colliding effectively)
                    if (distance >= 25)
                    {
                        enemy.moveOneFrame();
                    }
                    else if (this.md.getFramesElapsedTotal() % 15 == 0)
                    {
                        if (this.player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT)
                        {
                            this.player.addToLifeForceMax(-1);
                        }
                        else
                        {
                            this.player.addToLifeForceCurrent(-1);
                        }
                    }	
                }
                else
                {
                    enemy.setFlinchCount(enemy.getFlinchCount() - 1);
                }	
			}
			
			// update attack sprites
			ArrayList<AttackSprite> attackSpritesToRemove = new ArrayList<>();
			for (AttackSprite qs : this.attackSprites)
			{
				qs.moveOneFrame();
				
				Point2D qp = qs.getPosition();
				//double qpDistance = pp.distance(qp);
				double qpDistance = qs.origin.distance(qp);
				if ((qpDistance > qs.range) || qs.lifeSpanExceeded())
				{
					attackSpritesToRemove.add(qs);
				}
			}
			
			//actually remove
			for (AttackSprite qs : attackSpritesToRemove)
			{
				this.attackSprites.remove(qs);
			}
			attackSpritesToRemove.clear();
			
			//enemy sprites to remove
			ArrayList<Enemy> enemySpritesToRemove = new ArrayList<>();
			for (AttackSprite qs : this.attackSprites)
			{
				for (Enemy enemy : this.enemySprites)
				{
					if (enemy.collidesWith(qs))
					{
					    enemy.setFlinchCount(qs.flinchFrames);
					    if(this.md.getFramesElapsedTotal() % 15 == 0)
					    { 
					        enemy.damage(5); 
                            if(enemy.condition == Condition.DEAD)
                            {
                                enemySpritesToRemove.add(enemy);
                                this.killScore++;
                                if (this.killScore % 5 == 0)
                                {
                                    this.player.addToLifeForceMax(+4);
                                }
                            }
                        
                            if(qs.code.equals("Bolt_Attack_Sprite")) //later generalize to all lighting type attacks
                            {
                                FloatPoint2D qp = qs.getPosition();
                                qs.setOrigin(qp);
                                Enemy enemyToBolt = null;
                                for(Enemy e : this.enemySprites)
                                {   
                                    if(e != enemy){
                                        if(enemyToBolt == null)
                                        {
                                            if(e.getPosition().distance(qp) < qs.range)
                                            { 
                                                enemyToBolt = e; 
                                            }
                                        }
                                        else if(qp.distance(e.getPosition()) < qp.distance(enemyToBolt.getPosition()))
                                        {
                                            enemyToBolt = e;
                                        }
                                    }
                                }   
                                if(enemyToBolt != null && qp.distance(enemyToBolt.getPosition()) < qs.range)
                                {
                                    FloatPoint2D unit_toEnemy = (new FloatPoint2D(enemyToBolt.getPosition().x - qp.x,
                                                                                enemyToBolt.getPosition().y - qp.y)).getUnitVector();
                                    float vecLength = (float)(qs.getVelocity().getVectorLength());
                                    qs.setVelocity(unit_toEnemy.multipliedBy(vecLength));
                                    qs.range = qs.range * (.9);
                                }
                            }
					    }   
					}
				}
			}
			//actually remove
			for (Enemy enemy : enemySpritesToRemove)
			{
				this.enemySprites.remove(enemy);
			}
			enemySpritesToRemove.clear();
		}
	}
	
	@Override
	public void render(DrawingContext ctx)
	{
		//////////////////////
        // Gameplay Content //
        //////////////////////
        
        // background image
        ctx.drawImage(this.backgroundImage, this.backgroundImageX, this.backgroundImageY);
        
        // player sprite
        this.mageSprite.advanceAnimationOneClick();
        this.mageSprite.drawToContextAtOwnPosition(ctx);
        
        // enemy sprites
        for (Enemy e : this.enemySprites)
        {
            e.advanceAnimationOneClick();
            e.drawToContextAtOwnPosition(ctx);
            e.drawAuxContent(ctx);
        }

        // attack sprites
        for (QSprite qs : this.attackSprites)
        {
            qs.advanceAnimationOneClick();
            qs.drawToContextAtOwnPosition(ctx);
        }
        
        for (QSprite[] a : this.elementIcons)
        {
        	for(QSprite qs : a)
        	{
        		qs.drawToContextAtOwnPosition(ctx);
        	}
        }
        
        //attackChoice
        MageDefensePlayer.Ability ability = this.player.getChosenAbility();
        FloatPoint2D atkIconPt = null;
        switch(ability.type)
        {
            case  NONE:
            break;
            case  FIRE_ATTACK:
                atkIconPt = this.fireIcons[0].getPosition();
            break;
            case  WHIRLWIND_ATTACK:
                atkIconPt = this.windIcons[0].getPosition();
            break;
            case  BOLT_ATTACK:
                atkIconPt = this.lightningIcons[0].getPosition();
            break;
            case  EARTHQUAKE_ATTACK:
            break;
            case  SWORD_ATTACK:
                atkIconPt = this.swordIcons[0].getPosition();
            break;
            default:
            break;
        }
        if(atkIconPt != null)
        {
            ctx.setColor(Color.MAGENTA);
            ctx.drawRect((int)atkIconPt.x - 1, (int)atkIconPt.y - 1, this.ICON_WIDTH + 2, this.ICON_HEIGHT + 2);
        }
        // crosshair
        //this.crosshair.advanceAnimationOneClick();
        this.crosshair.drawToContextAtOwnPosition(ctx);
        
        // boundaries
        if (this.show_bounding_box) 
        {
            ctx.setColor(Color.BLUE); 
            ctx.fillPolygon(this.mageSprite); 
            
            ctx.setColor(Color.RED);
            for (Enemy e : this.enemySprites) { ctx.fillPolygon(e); }
            
            ctx.setColor(Color.CC.ORANGE); 
            for (QSprite qs : this.attackSprites) { ctx.fillPolygon(qs); }
        }
        
        
        //////////////////////
        // Heads up display //
        //////////////////////
        
		// life force bar
		IntPoint2D lfb = new IntPoint2D(7, 7);
		double lifeForceMax = this.player.getLifeForceMax();
		double lifeForceCurrent = this.player.getLifeForceCurrent();
		double lifeForcePercent = this.player.getLifeForcePercent();
		int lifeForceBarOutlineWidth = (int)(lifeForceMax * NormalGameState.LIFE_FORCE_BAR_MAX_RATIO);
		int lifeForceBarFillWidth = (int)(lifeForceCurrent * NormalGameState.LIFE_FORCE_BAR_MAX_RATIO);
		ctx.setColor(this.player.getStatus().barOutlineColorCode);
		ctx.drawRect(lfb.x, lfb.y, lifeForceBarOutlineWidth + 1, NormalGameState.LIFE_FORCE_BAR_HEIGHT + 1);
		ctx.setColor(this.player.getStatus().barFillColorCode);
		ctx.fillRect(lfb.x + 1, lfb.y + 1, lifeForceBarFillWidth, NormalGameState.LIFE_FORCE_BAR_HEIGHT);
		
        // kill score and life force number
		ctx.setColor(Color.BLACK);
		String killScoreString = String.format("Kill Score: %04d", this.killScore);
		String lifeForceString = String.format("%04d / %04d Life Force", (int)lifeForceCurrent, (int)lifeForceMax);
		ctx.drawString(lifeForceString, lfb.x, lfb.y + NormalGameState.LIFE_FORCE_BAR_HEIGHT + 22);
		
        // burnout message
		if (this.player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT)
		{
			ctx.drawString("BURNOUT", lfb.x, lfb.y + NormalGameState.LIFE_FORCE_BAR_HEIGHT + 42);
		}
		ctx.drawString(killScoreString, lfb.x, lfb.y + NormalGameState.LIFE_FORCE_BAR_HEIGHT + 42 * 2);
        
        // debug button
        ctx.setColor(this.show_bounding_box ? Color.RED : Color.BLUE);
        this.debugButton.x = lfb.x;
        this.debugButton.y = lfb.y + NormalGameState.LIFE_FORCE_BAR_HEIGHT + 42 * 3;
        this.debugButton.width = 80;
        this.debugButton.height = 35;
        ctx.fillRect(this.debugButton);
        ctx.setColor(Color.BLACK);
        ctx.drawString(this.show_bounding_box ? "DEBUG OFF" : "DEBUG ON", this.debugButton.x + 5, this.debugButton.y + this.debugButton.height - 5);
        
		// game over message and reset button
		if (this.player.getStatus() == MageDefensePlayer.PlayerStatus.DEAD)
		{
            Font originalFont = ctx.getFont();
            try
            {
                ctx.setFont(NormalGameState.GAME_OVER_FONT);
                ctx.setColor(Color.RED);
                ctx.drawString("GAME OVER!!!", (int)((1/8.0) * this.md.getPanelWidth()),
                                                          (int)((1/2.0) * this.md.getPanelHeight()));
            }
            finally
            {
                ctx.setFont(originalFont);
            }
            
            ctx.setColor(Color.CC.GREEN);
            this.resetButton.x = lfb.x;
            this.resetButton.y = lfb.y + NormalGameState.LIFE_FORCE_BAR_HEIGHT + 42 * 4;
            this.resetButton.width = 80;
            this.resetButton.height = 35;
            ctx.fillRect(this.resetButton);
            ctx.setColor(Color.BLACK);
            ctx.drawString("RESET", this.resetButton.x + 5, this.resetButton.y + this.resetButton.height - 5);
		}
		
		// game won message and reset button
		if (this.player.roundsCompleted && this.enemySprites.size() == 0)
		{
            Font originalFont = ctx.getFont();
            try
            {
                ctx.setFont(NormalGameState.GAME_OVER_FONT);
                ctx.setColor(Color.GREEN);
                ctx.drawString("YOU HAVE WON!!!", (int)((1/8.0) * this.md.getPanelWidth()),
                                                          (int)((1/2.0) * this.md.getPanelHeight()));
            }
            finally
            {
                ctx.setFont(originalFont);
            }
            
            ctx.setColor(Color.CC.GREEN);
            this.resetButton.x = lfb.x;
            this.resetButton.y = lfb.y + NormalGameState.LIFE_FORCE_BAR_HEIGHT + 42 * 4;
            this.resetButton.width = 80;
            this.resetButton.height = 35;
            ctx.fillRect(this.resetButton);
            ctx.setColor(Color.BLACK);
            ctx.drawString("RESET", this.resetButton.x + 5, this.resetButton.y + this.resetButton.height - 5);
		}
		
		//Next Round
		if(showNextRound > 0)
		{
		    showNextRound--;
		    Font originalFont = ctx.getFont();
            try
            {
                ctx.setFont(NormalGameState.FF_FONT_BOLD);
                ctx.setColor(Color.BLUE);
                String roundString = "ROUND "+this.round;
                ctx.drawString(roundString, -50 + 3 * showNextRound, (int)((2/5.0) * this.md.getPanelHeight()));
            }
            finally
            {
                ctx.setFont(originalFont);
            }
		    
		}
		
        //Debug Log
        ctx.setColor(Color.BLACK);
		ArrayList<String> debugLog = new ArrayList<>();
        for (Enemy e : this.enemySprites)
        {   
			//float x = e.getPosition().x;
			//float y = e.getPosition().y;
			int h = e.getHealth();
			int flinch = e.getFlinchCount();
			boolean b = e.isOutOfRectangle(this.backgroundImageX, this.backgroundImage.getWidth(),
				                          this.backgroundImageY, this.backgroundImage.getHeight());
			debugLog.add(String.format("# %02d: %d / %d  flinch: %d  box: %s", e.getID(), h, e.maxHealth, flinch, b));
        }
        //ctx.drawLinesOfText(debugLog, md.getPanelWidth() - 150, md.getPanelHeight() - 50);
        ctx.drawLinesOfText(debugLog, 0, 250);
        
        //Enemy Life
        // life force bar
        
        // // mouse position
		// ctx.setColor(Color.BLACK);
		// ctx.drawString(mousePositionString, cb.x, cb.y + 30);
		// ctx.drawString(crosshairPositionString, cb.x, cb.y + 50);
		// // cycle bar
		// ctx.setColor(Color.BLUE);
		// IntPoint2D cb = new IntPoint2D(7, 4);
		// ctx.drawRect(cb.x, cb.y, 102, 8); // outline
		// ctx.fillRect(cb.x + 1 + (int)(framesElapsedTotal % 100), cb.y, 2, 8); // cycle
	}
}	