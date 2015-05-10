/* 
 * Richard Saney 
 * 
 * Created: May, 2015
 * 
 * MapRoomLoading.java
 * MapRoomLoading class definition
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

public class MapRoomLoading extends GameState{

	//constructor
	public MapRoomLoading(MageDefense md){
		super(md);
	}
	
	//Overloaded methods
	@Override
	public void update()
	{
	
	}
	
	@Override
	public void render(DrawingContext ctx)
	{
		// cycle bar
		ctx.setColor(Color.RED);
		IntPoint2D cb = new IntPoint2D(7, 12);
		ctx.drawRect(cb.x, cb.y, 102, 8); // outline
		ctx.fillRect(cb.x + 1 + (int)(this.md.getFramesElapsedTotal() % 100), cb.y, 2, 8); // cycle
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