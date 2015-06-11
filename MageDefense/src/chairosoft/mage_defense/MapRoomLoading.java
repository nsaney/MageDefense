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

import chairosoft.ui.geom.IntPoint2D;
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;

public class MapRoomLoading extends GameState
{

	//constructor
	public MapRoomLoading(MageDefense md)
	{
		super(md);
	}
	
	//Overridden methods
	@Override
	public void keyPressed(int keyCode) { }
	
	@Override
	public void keyReleased(int keyCode) { }
	
	@Override
	public void pointerPressed(float x, float y) { }
	
	@Override
	public void pointerMoved(float x, float y) { }
	
	@Override
	public void pointerReleased(float x, float y) { }
	
	@Override
	public void update() { }
	
	@Override
	public void render(DrawingContext ctx)
	{
		// cycle bar
		ctx.setColor(Color.RED);
		IntPoint2D cb = new IntPoint2D(7, 12);
		ctx.drawRect(cb.x, cb.y, 102, 8); // outline
		ctx.fillRect(cb.x + 1 + (int)(this.md.getFramesElapsedTotal() % 100), cb.y, 2, 8); // cycle
	}
}