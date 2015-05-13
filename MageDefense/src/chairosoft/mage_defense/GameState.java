/* 
 * Richard Saney 
 * 
 * Created: May 10, 2015
 * 
 * GameState.java
 * GameState Abstract class definition
 * 
 */

package chairosoft.mage_defense;

import chairosoft.ui.graphics.DrawingContext;

import java.awt.event.MouseEvent;


/**
* The segment of the game dependent on an overarching situation. Any portion of the 
* game which necessitates unique handling should be implemented through a unique 
* GameState.
*
*/
public abstract class GameState
{
	//instance variables
	protected MageDefense md;
	
	//constructor
	public GameState(MageDefense md)
	{
		this.md = md;
	}
	
	//methods
	
	/**
	 * A GameState specific logical segment, modifying the MageDefense object
	 *
	 */
	public void update() {}
	
	/**
	 * A GameState specific visual display, modifying the MageDefense object 
	 *
	 * @param ctx the context to which visual components are drawn
	 */
	public void render(DrawingContext ctx) {}
	
	/**
	 * A GameState specific handling of pressed keys, modifying the MageDefense object
	 *
	 * @param keyCode the int code corresponding to the key pressed
	 */
	public void keyPressed(int keyCode) {}
	
	/**
	 * A GameState specific handling of released keys, modifying the MageDefense object
	 *
	 * @param keyCode the int code corresponding to the key released
	 */
	public void keyReleased(int keyCode) {}
	
	/**
	 * A GameState specific handling of released keys, modifying the MageDefense object
	 *
	 * @param e the event of a mouse moved action
	 */
	public void mouseMoved(MouseEvent e) {}
	
	/**
	 * A GameState specific handling of released keys, modifying the MageDefense object
	 *
	 * @param e the event of a mouse clicked action
	 */
	public void mousePressed(MouseEvent e) {}
}