/* 
 * Richard Saney 
 * 
 * Created: May 2015
 * 
 * MapRoomLoaded.java
 * MapRoomLoaded class definition
 * 
 */

package chairosoft.mage_defense;

import chairosoft.ui.graphics.DrawingContext;

import java.awt.event.MouseEvent;

public class MapRoomLoaded extends GameState
{
	//constructor
	public MapRoomLoaded(MageDefense md)
	{
		super(md);
	}
	
	//Overloaded methods
	@Override
	public void keyPressed(int keyCode)	{ }
	
	@Override
	public void keyReleased(int keyCode) { }
	
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	@Override
	public void mousePressed(MouseEvent e) { }
	
	@Override
	public void update()
	{
		//this.md.qmaproom = this.md.nextQMapRoom;
		this.md.gameState = this.md.NORMAL_GAMEPLAY;
		//this.md.mageSprite.setPositionByQTile(this.md.spawnCol, this.md.spawnRow);
	}
	
	@Override
	public void render(DrawingContext ctx) { }
}