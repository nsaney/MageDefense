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
import chairosoft.ui.graphics.DrawingImage;

import chairosoft.util.Loading;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameNotLoaded extends GameState
{
    public final DrawingImage startScreenImage = Loading.getImage("/img/bg/MageDefenseStartScreen_blurs.png");
    public final int startScreenImageX = (this.md.getPanelWidth() - this.startScreenImage.getWidth()) / 2;
    public final int startScreenImageY = (this.md.getPanelHeight() - this.startScreenImage.getHeight()) / 2;
    
	//constructor
	public GameNotLoaded(MageDefense md)
	{
		super(md);
	}

	//Overloaded methods
	@Override
	public void keyPressed(int keyCode)
	{
        if (keyCode == KeyEvent.VK_ENTER)
		{
            this.md.loadInitialQMapRoom();
		}
	}
	
	@Override
	public void keyReleased(int keyCode) { }
	
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		this.md.loadInitialQMapRoom();
	}
	
	@Override
	public void update() { }
	
	@Override
	public void render(DrawingContext ctx)
	{
        // background color
        ctx.setColor(Color.BLACK);
        ctx.fillRect(0, 0, this.md.getPanelWidth(), this.md.getPanelHeight());
        
		// start screen image
        ctx.drawImage(this.startScreenImage, this.startScreenImageX, this.startScreenImageY);
		
		// cycle bar
		ctx.setColor(Color.WHITE);
		IntPoint2D cb = new IntPoint2D(7, 20);
		ctx.drawRect(cb.x, cb.y, 102, 8); // outline
		ctx.fillRect(cb.x + 1 + (int)(this.md.getFramesElapsedTotal() % 100), cb.y, 2, 8); // cycle
	}
}