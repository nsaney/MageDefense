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
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;
import chairosoft.ui.graphics.Font;

import java.util.ArrayList;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class NormalGameState extends GameState
{
	
	//constructor
	public NormalGameState(MageDefense md)
	{
		super(md);
	}

	//Overloaded methods
	@Override
	public void keyPressed(int keyCode)
	{
		switch (keyCode)
		{
			// normal gameplay
			//Changing attack types
			case KeyEvent.VK_Q: this.md.player.chooseAbilityType(MageDefensePlayer.AbilityType.FIRE_ATTACK); break;
			case KeyEvent.VK_W: this.md.player.chooseAbilityType(MageDefensePlayer.AbilityType.WHIRLWIND_ATTACK); break;
			case KeyEvent.VK_E: this.md.player.chooseAbilityType(MageDefensePlayer.AbilityType.BOLT_ATTACK); break;
			case KeyEvent.VK_S: this.md.player.chooseAbilityType(MageDefensePlayer.AbilityType.SWORD_ATTACK); break;
			
			case KeyEvent.VK_2:
			case KeyEvent.VK_NUMPAD2: this.md.player.addToLifeForceCurrent(-5); break;
			
			case KeyEvent.VK_8:
			case KeyEvent.VK_NUMPAD8: this.md.player.addToLifeForceCurrent(+5); break;
			
			case KeyEvent.VK_4:
			case KeyEvent.VK_NUMPAD4: this.md.player.addToLifeForceMax(-5); break;
			
			case KeyEvent.VK_6:
			case KeyEvent.VK_NUMPAD6: this.md.player.addToLifeForceMax(+5); break;
			
			case KeyEvent.VK_ENTER:
				this.md.isPaused = !this.md.isPaused;
				break;
			
			// debug switches
			case KeyEvent.VK_B: this.md.show_boundaries = !this.md.show_boundaries; break;
			case KeyEvent.VK_X: this.md.show_bounding_box = !this.md.show_bounding_box; break;
			case KeyEvent.VK_M: this.md.move_and_collide = !this.md.move_and_collide; break;
			
			default: break;
		}
	}
	
	@Override
	public void keyReleased(int keyCode)
	{
	
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		this.md.updateCrosshair(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		// get ability
		MageDefensePlayer.Ability ability = this.md.player.getChosenAbility();
		//System.err.println(ability);
		if (ability == null) { return; }
		
		// update crosshair
		FloatPoint2D mp_unit = this.md.updateCrosshair(e);
		if (0.0 == mp_unit.distance(0, 0)) { return; }
		
		// update life force
		if (this.md.player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT 
			&& ability.type != MageDefensePlayer.AbilityType.SWORD_ATTACK ) { return; }
		this.md.player.addToLifeForceCurrent(-1 * ability.cost);
		
		// add attack sprite
		AttackSprite nextAttackSprite = new AttackSprite(ability.type.spriteCode, ability.range, ability.clickLifeSpan);
		
		FloatPoint2D pp = this.md.mageSprite.getPosition();
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
			
			nextAttackSprite.setCurrentStateCode(next_state);
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
			
			nextAttackSprite.setCurrentStateCode(next_state);
			
			FloatPoint2D ap = nextAttackSprite.getPosition();
			nextAttackSprite.setPosition(ap.x + mp_unit.x * QTileset.getTileWidth() / 2, ap.y + mp_unit.y * QTileset.getTileWidth() / 2);
		}
		
		this.md.attackSprites.add(nextAttackSprite);
	}
	
	@Override
	public void update()
	{
		if (this.md.enemySprites.size() <= 1)
		{
			EnemyWave.initialize(this.md);
		}
		// TODO: set direction based on keypad compass direction
		////mageSprite.setDirection(keypad.getDirection()); //// TODO: this
		
		// collision
		if (this.md.move_and_collide && this.md.player.getStatus() != MageDefensePlayer.PlayerStatus.DEAD)
		{
			// update life force
			this.md.player.addToLifeForceCurrent(this.md.player.regenAmount / this.md.player.framesBetweenRegen);
			
			
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
			FloatPoint2D pp = this.md.mageSprite.getPosition();
			
			for (QSprite enemy : this.md.enemySprites)
			{
				FloatPoint2D enemyPos = enemy.getPosition();
				double distance = pp.distance(enemyPos);
				
				// update enemy sprite only when at least 25 units away (not colliding effectively)
				if (distance >= 25)
				{
//TODO RNS-->      //TODO: Incorporate MovementPattern of Enemy class in update
					enemy.moveOneFrame();
				}
				else if (this.md.getFramesElapsedTotal() % 10 == 0)
				{
					if (this.md.player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT)
					{
						this.md.player.addToLifeForceMax(-1);             		
					}
					else
					{
						this.md.player.addToLifeForceCurrent(-1);
					}
				}			
			}
			
			// update attack sprites
			ArrayList<AttackSprite> attackSpritesToRemove = new ArrayList<>();
			for (AttackSprite qs : this.md.attackSprites)
			{
//TODO RNS-->  //TODO: Incorporate MovementPattern of Attack class in update
				qs.moveOneFrame();
				
				Point2D qp = qs.getPosition();
				double qpDistance = pp.distance(qp);
				if ((qpDistance > qs.range) || qs.lifeSpanExceeded())
				{
					attackSpritesToRemove.add(qs);
				}
			}
			
			//actually remove
			for (AttackSprite qs : attackSpritesToRemove)
			{
				this.md.attackSprites.remove(qs);
			}
			attackSpritesToRemove.clear();
			
			ArrayList<QSprite> enemySpritesToRemove = new ArrayList<>();
			for (AttackSprite qs : this.md.attackSprites)
			{
				for (QSprite enemy : this.md.enemySprites)
				{
					if (enemy.collidesWith(qs))
					{
						enemySpritesToRemove.add(enemy);
						this.md.killScore++;
						if (this.md.killScore % 5 == 0)
						{
							this.md.player.addToLifeForceMax(+4);
						}
					}
				}
			}
			//actually remove
			for (QSprite enemy : enemySpritesToRemove)
			{
				this.md.enemySprites.remove(enemy);
			}
			enemySpritesToRemove.clear();
		}
	}
	
	@Override
	public void render(DrawingContext ctx)
	{
		// background 
		// this is taken care of in QPanel class (using Color.WHITE)
		
		// content graphics 
		IntPoint2D p = this.md.mageSprite.getIntPosition();
		this.md.clipX = this.md.getPanelHalfWidth() - (int)(p.x * this.md.X_SCALING);
		this.md.clipY = this.md.getPanelHalfHeight() - (int)(p.y * this.md.Y_SCALING);
		this.drawNormalGameplayContent(this.md.contentGraphics, p.x, p.y);
		ctx.drawImage(this.md.content, this.md.clipX, this.md.clipY);
		
		// // cycle bar
		// ctx.setColor(Color.BLUE);
		// IntPoint2D cb = new IntPoint2D(7, 4);
		// ctx.drawRect(cb.x, cb.y, 102, 8); // outline
		// ctx.fillRect(cb.x + 1 + (int)(framesElapsedTotal % 100), cb.y, 2, 8); // cycle
		
		// life force bar
		IntPoint2D lfb = new IntPoint2D(7, 7);
		double lifeForceMax = this.md.player.getLifeForceMax();
		double lifeForceCurrent = this.md.player.getLifeForceCurrent();
		double lifeForcePercent = this.md.player.getLifeForcePercent();
		int lifeForceBarOutlineWidth = (int)(lifeForceMax * this.md.LIFE_FORCE_BAR_MAX_RATIO);
		int lifeForceBarFillWidth = (int)(lifeForceCurrent * this.md.LIFE_FORCE_BAR_MAX_RATIO);
		ctx.setColor(this.md.player.getStatus().barOutlineColorCode);
		ctx.drawRect(lfb.x, lfb.y, lifeForceBarOutlineWidth + 1, this.md.LIFE_FORCE_BAR_HEIGHT + 1);
		ctx.setColor(this.md.player.getStatus().barFillColorCode);
		ctx.fillRect(lfb.x + 1, lfb.y + 1, lifeForceBarFillWidth, this.md.LIFE_FORCE_BAR_HEIGHT);
		
		ctx.setColor(Color.BLACK);
		String killScoreString = String.format("Kill Score: %04d", this.md.killScore);
		String lifeForceString = String.format("%04d / %04d Life Force", (int)lifeForceCurrent, (int)lifeForceMax);
		ctx.drawString(lifeForceString, lfb.x, lfb.y + this.md.LIFE_FORCE_BAR_HEIGHT + 22);
		
		if (this.md.player.getStatus() == MageDefensePlayer.PlayerStatus.BURNOUT)
		{
			ctx.drawString("BURNOUT", lfb.x, lfb.y + this.md.LIFE_FORCE_BAR_HEIGHT + 42);
		}
		ctx.drawString(killScoreString, lfb.x, lfb.y + this.md.LIFE_FORCE_BAR_HEIGHT + 42 * 2);
		// // mouse position
		// ctx.setColor(Color.BLACK);
		// ctx.drawString(mousePositionString, cb.x, cb.y + 30);
		// ctx.drawString(crosshairPositionString, cb.x, cb.y + 50);
		
		//Game Over
		if (this.md.player.getStatus() == MageDefensePlayer.PlayerStatus.DEAD)
		{
			ctx.setFont(this.md.gameOverFont);
			ctx.setColor(Color.RED);
			ctx.drawString("DEATH TO MAGE. AND YOU.", (int)((1/8.0) * this.md.getPanelWidth()),
													  (int)((1/2.0) * this.md.getPanelHeight()));
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
        	ctx.setColor(this.md.qmaproom.getBackgroundColor());
        	ctx.fillRect(0, 0, this.md.qmaproom.getWidthPixels(), this.md.qmaproom.getHeightPixels());
			//qmaproom.drawToContext(ctx, 0, 0, null);
			this.md.backgroundSprite.drawToContext(ctx, 0, 0);
			
			// player sprite
			//mageSprite.advanceAnimationOneClick();
			this.md.mageSprite.drawToContextAtOwnPosition(ctx);
			
			// enemy sprites
			for (QSprite qs : this.md.enemySprites)
			{
				qs.advanceAnimationOneClick();
            	qs.drawToContextAtOwnPosition(ctx);
			}

            // attack sprites
            for (QSprite qs : this.md.attackSprites)
            {
                qs.advanceAnimationOneClick();
                qs.drawToContextAtOwnPosition(ctx);
            }
            
            // crosshair
        	this.md.crosshair.advanceAnimationOneClick();
            this.md.crosshair.drawToContextAtOwnPosition(ctx);
			
			// boundaries
			if (this.md.show_bounding_box) 
            {
                ctx.setColor(Color.BLUE); 
                ctx.fillPolygon(this.md.mageSprite); 
                for (QSprite qs : this.md.attackSprites) { ctx.fillPolygon(qs); }
            }
            
            //// TODO: collisions between attacks and enemies ???
			// if (show_boundaries) 
            // {
                // mageSprite.drawCollidingBoundariesToGraphics(ctx, qmaproom, Color.RED); 
                // for (QSprite qs : fireAttackSprites) { qs.drawCollidingBoundariesToGraphics(ctx, qmaproom, Color.RED); }
            // }
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
}	