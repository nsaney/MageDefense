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
				EnemyWave.addEnemies(md, 1, md.GHOST_SPRITE_CODE);
				EnemyWave.addEnemies(md, 1, md.TREX_SPRITE_CODE);
				break;
			case 2:
				break;
			case 3:
				break;
		}
		
		md.wave++;
	}
	
	public static void addEnemies(MageDefense md, int number, String spriteCode)
	{
		FloatPoint2D pp = md.mageSprite.getPosition();
		for (int i = 0; i < number; ++i)
		{
			QSprite enemy = new QSprite(spriteCode);
			
			// initialize pos and vel
			FloatPoint2D gp = new FloatPoint2D((float)(Math.random() * md.getPanelWidth()),
											   (float)(Math.random() * md.getPanelHeight()));
			enemy.setPosition(gp.x, gp.y);
			FloatPoint2D difference = new FloatPoint2D(pp.x - gp.x, pp.y - gp.y);
			FloatPoint2D differenceUnit = QPhysical2D.getUnitVector(difference);
			enemy.setVelocity(0.8f * differenceUnit.x, 0.8f * differenceUnit.y);
			
			md.enemySprites.add(enemy);
		}
	}
}