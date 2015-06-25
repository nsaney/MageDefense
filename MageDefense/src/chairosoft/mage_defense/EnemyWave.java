/* 
 * Richard Saney 
 * 
 * Created: May 10, 2015
 * 
 * EnemyWave.java
 * EnemyWave Abstract class definition
 * 
 */

package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;

import chairosoft.ui.geom.FloatPoint2D;


/**
 * The initialization of enemies. 
 *
 */
public class EnemyWave
{
	// no instantiations
	private EnemyWave() {}
		
	// methods
	public static void initialize(NormalGameState ngs)
	{
		switch (ngs.wave)
		{
			case 1:
				EnemyWave.addEnemies(ngs, 1, Enemy.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(ngs, 1, Enemy.TREX_SPRITE_CODE);
				break;
			case 2:
				EnemyWave.addEnemies(ngs, 2, Enemy.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(ngs, 1, Enemy.TREX_SPRITE_CODE);
				break;
			case 3:
				EnemyWave.addEnemies(ngs, 3, Enemy.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(ngs, 1, Enemy.TREX_SPRITE_CODE);
				break;
			case 4: EnemyWave.addEnemies(ngs, 1, Enemy.VIPERREX_SPRITE_CODE);
				break;
			default:
				ngs.wave = 0; //for repeating 
			
		}
		ngs.wave++;
	}
	
	public static void addEnemies(NormalGameState ngs, int number, String spriteCode)
	{
		FloatPoint2D magePos = ngs.mageSprite.getCenterPosition();
		for (int i = 0; i < number; ++i)
		{
			Enemy enemy;
			switch(spriteCode)
			{
				case Enemy.GHOST_SPRITE_CODE:
					enemy = Enemy.getGhost();
					break;
				case Enemy.TREX_SPRITE_CODE:
					enemy = Enemy.getTRex();
					break;
				case Enemy.VIPERREX_SPRITE_CODE:
					enemy = Enemy.getViperRex();
					break;
				default:
					System.out.println("EnemyWave addEnemies(): Requested spriteCode does not match implemented switch block.");
					return;
			}		
			// initialize pos and vel
			
			float enemPosX;
			float enemPosY;
			double posChance = Math.random();
            
			if(posChance > .5)
			{
			    enemPosX = (posChance > .75)? -1 : MageDefense.PANEL_WIDTH + 1;
			    enemPosY = (float)(Math.random() * MageDefense.PANEL_HEIGHT);
			}
			else
			{
			    enemPosX = (float)(Math.random() * MageDefense.PANEL_WIDTH);
			    enemPosY = (posChance > .25)? -1 : MageDefense.PANEL_HEIGHT + 1;
			}
			FloatPoint2D enemPos = new FloatPoint2D(enemPosX, enemPosY);
			FloatPoint2D difference = new FloatPoint2D(magePos.x - enemPos.x, magePos.y - enemPos.y);
			enemy.setVelocity(difference.getUnitVector().multipliedBy(0.8f));
			enemy.initializePosition(enemPos);
			ngs.enemySprites.add(enemy);
		}
	}
}