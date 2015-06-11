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

public class MapRoomLoaded extends GameState
{
	//constructor
	public MapRoomLoaded(MageDefense md)
	{
		super(md);
	}
	
	//Overridden methods
	@Override
	public void keyPressed(int keyCode)	{ }
	
	@Override
	public void keyReleased(int keyCode) { }
	
	@Override
	public void pointerPressed(float x, float y) { }
	
	@Override
	public void pointerMoved(float x, float y) { }
	
	@Override
	public void pointerReleased(float x, float y) { }
	
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