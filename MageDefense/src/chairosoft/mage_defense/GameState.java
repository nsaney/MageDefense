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
	 * A GameState specific handling of pressed pointers, modifying the MageDefense object
	 *
	 * @param x the x-location of the pressed pointer
     * @param y the y-location of the pressed pointer
	 */
	public void pointerPressed(float x, float y) {}
	
	/**
	 * A GameState specific handling of moved pointers, modifying the MageDefense object
	 *
	 * @param x the updated x-location of the moved pointer
     * @param y the updated y-location of the moved pointer
	 */
	public void pointerMoved(float x, float y) {}
	
	/**
	 * A GameState specific handling of released pointers, modifying the MageDefense object
	 *
	 * @param x the last x-location of the released pointer
     * @param y the last y-location of the released pointer
	 */
	public void pointerReleased(float x, float y) {}
}