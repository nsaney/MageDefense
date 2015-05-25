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
	public static void initialize(MageDefense md)
	{
		switch (md.wave)
		{
			case 1:
				EnemyWave.addEnemies(md, 1, Enemy.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(md, 1, Enemy.TREX_SPRITE_CODE);
				break;
			case 2:
				EnemyWave.addEnemies(md, 2, Enemy.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(md, 1, Enemy.TREX_SPRITE_CODE);
				break;
			case 3:
				EnemyWave.addEnemies(md, 3, Enemy.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(md, 1, Enemy.TREX_SPRITE_CODE);
				break;
			case 4: EnemyWave.addEnemies(md, 1, Enemy.VIPERREX_SPRITE_CODE);
				break;
			default:
				md.wave = 0; //for repeating 
			
		}
		md.wave++;
	}
	
	public static void addEnemies(MageDefense md, int number, String spriteCode)
	{
		FloatPoint2D pp = md.mageSprite.getPosition();
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
			FloatPoint2D gp = new FloatPoint2D((float)(Math.random() * md.getPanelWidth()),
											   (float)(Math.random() * md.getPanelHeight()));
			FloatPoint2D difference = new FloatPoint2D(pp.x - gp.x, pp.y - gp.y);
			enemy.initializePosition(gp, pp);
			enemy.setVelocity(difference.getUnitVector().multipliedBy(0.8f));
			
			md.enemySprites.add(enemy);
		}
	}
}