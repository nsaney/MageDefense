/* 
 * Richard Saney 
 * 
 * Created: May, 2015
 * 
 * GameNotLoaded.java
 * GameNotLoaded class definition
 * 
 */

package chairosoft.mage_defense;

import chairosoft.ui.geom.IntPoint2D;
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameNotLoaded extends GameState
{

	//constructor
	public GameNotLoaded(MageDefense md)
	{
		super(md);
	}

	//Overloaded methods
	@Override
	public void keyPressed(int keyCode)
	{
		switch (keyCode)
		{
			case KeyEvent.VK_ENTER:
				this.md.loadInitialQMapRoom();
				break;
		}
	}
	
	@Override
	public void keyReleased(int keyCode)
	{
	
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
	
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		this.md.loadInitialQMapRoom();
	}
	
	@Override
	public void update()
	{
	
	}
	
	@Override
	public void render(DrawingContext ctx)
	{
		// start screen sprite
		this.md.startScreenSprite.drawToContext(ctx, 0, 0);
		
		// cycle bar
		ctx.setColor(Color.BLACK);
		IntPoint2D cb = new IntPoint2D(7, 20);
		ctx.drawRect(cb.x, cb.y, 102, 8); // outline
		ctx.fillRect(cb.x + 1 + (int)(this.md.getFramesElapsedTotal() % 100), cb.y, 2, 8); // cycle
	}
}