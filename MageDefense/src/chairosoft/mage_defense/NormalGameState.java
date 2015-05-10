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

public class NormalGameState extends GameState
{
	
	//constructor
	public NormalGameState(MageDefense md){
		super(md);
	}

	//Overloaded methods
	@Override
	public void update()
	{
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
			
			
			// get mageSprite and ghostSprite info
			FloatPoint2D pp = this.md.mageSprite.getPosition();
			FloatPoint2D ghostPos = this.md.ghostSprite.getPosition();
			double distance = pp.distance(ghostPos);
			// update ghost sprite conditionally
			
			if (distance >= 25)
			{
				this.md.ghostSprite.moveOneFrame();
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
			// update attack sprites
			ArrayList<AttackSprite> attackSpritesToRemove = new ArrayList<>();
			for (AttackSprite qs : this.md.attackSprites)
			{
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
			
			for (AttackSprite qs : this.md.attackSprites)
			{
				if (this.md.ghostSprite.collidesWith(qs))
				{
					this.md.ghostSprite.setPosition((float)(Math.random() * this.md.getPanelWidth()),
											(float)(Math.random() * this.md.getPanelHeight()));
					ghostPos = this.md.ghostSprite.getPosition();
					FloatPoint2D difference = new FloatPoint2D(pp.x - ghostPos.x, pp.y - ghostPos.y);
					FloatPoint2D differenceUnit = QPhysical2D.getUnitVector(difference);
					this.md.ghostSprite.setVelocity(0.8f * differenceUnit.x, 0.8f * differenceUnit.y);
					this.md.killScore++;
					if (this.md.killScore % 5 == 0)
					{
						this.md.player.addToLifeForceMax(+4);
					}
				}
			}
		}
		
		
		if (this.md.show_position)
		{
			FloatPoint2D pp = this.md.mageSprite.getPosition();
			FloatPoint2D pv = this.md.mageSprite.getVelocity();
			FloatPoint2D pa = this.md.mageSprite.getAcceleration();
			FloatPoint2D pl = this.md.mageSprite.getLastMove();
			
			this.md.positionString = String.format(this.md.COORDINATE_FORMAT_STRING, pp.x, pp.y);
			this.md.velocityString = String.format(this.md.COORDINATE_FORMAT_STRING, pv.x, pv.y);
			this.md.accelerationString = String.format(this.md.COORDINATE_FORMAT_STRING, pa.x, pa.y);
			this.md.lastMoveString = String.format(this.md.COORDINATE_FORMAT_STRING, pl.x, pl.y);
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
			
            // ghost sprite
            this.md.ghostSprite.advanceAnimationOneClick();
            this.md.ghostSprite.drawToContextAtOwnPosition(ctx);
            
            // trex sprite
            this.md.trexSprite1.advanceAnimationOneClick();
        	this.md.trexSprite1.drawToContextAtOwnPosition(ctx);
            
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
			case KeyEvent.VK_P: this.md.show_position = !this.md.show_position; break;
			case KeyEvent.VK_M: this.md.move_and_collide = !this.md.move_and_collide; break;
			
			// debug actions
			case KeyEvent.VK_SPACE: 
				this.md.mageSprite.setPositionByQTile(this.md.spawnCol, this.md.spawnRow); 
				this.md.ghostSprite.setPositionByQTile(-2, -2);
				FloatPoint2D ghostPos = this.md.ghostSprite.getPosition();
				FloatPoint2D pp = this.md.mageSprite.getPosition();
				FloatPoint2D difference = new FloatPoint2D(pp.x - ghostPos.x, pp.y - ghostPos.y);
				FloatPoint2D differenceUnit = QPhysical2D.getUnitVector(difference);
				this.md.ghostSprite.setVelocity(0.8f * differenceUnit.x, 0.8f * differenceUnit.y);
				break;
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
		
		this.md.attackSprites.add(nextAttackSprite);
	}
}	