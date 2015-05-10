/* 
 * Richard Saney 
 * 
 * Created: May, 2015
 * 
 * MapRoomLoaded.java
 * MapRoomLoaded class definition
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

public class MapRoomLoaded extends GameState{

	//constructor
	public MapRoomLoaded(MageDefense md){
		super(md);
	}
	
	//Overloaded methods
	@Override
	public void update()
	{
		this.md.qmaproom = this.md.nextQMapRoom;
		this.md.gameState = this.md.NORMAL_GAMEPLAY;
		this.md.mageSprite.setPositionByQTile(this.md.spawnCol, this.md.spawnRow);
		
		this.md.ghostSprite.setCurrentState("right_basic");
		this.md.ghostSprite.setPositionByQTile(-2, -2);
		FloatPoint2D pp = this.md.mageSprite.getPosition();
		FloatPoint2D gp = this.md.ghostSprite.getPosition();
		FloatPoint2D difference = new FloatPoint2D(pp.x - gp.x, pp.y - gp.y);
		FloatPoint2D differenceUnit = QPhysical2D.getUnitVector(difference);
		this.md.ghostSprite.setVelocity(0.8f * differenceUnit.x, 0.8f * differenceUnit.y);
		
		this.md.trexSprite1.setPositionByQTile(18, 4);
		this.md.trexSprite1.setCurrentState("right_basic");
	}
	
	@Override
	public void render(DrawingContext ctx)
	{
	
	}
	
	@Override
	public void keyPressed(int keyCode)
	{
	
	}
	
	@Override
	public void keyReleased(int keyCode)
	{
	
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
	
	}
	
	@Override
	public void mousePressed(MouseEvent e){
	
	}
}