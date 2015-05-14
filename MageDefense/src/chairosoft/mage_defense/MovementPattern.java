/** 
 * 
 * Created: May 13, 2015
 * 
 * MovementPattern.java
 * MovementPatter Abstract class definition
 * 
 *
 * @author Richard Saney
 */

package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;

import chairosoft.ui.geom.FloatPoint2D;


/**
 * The transformation of a movement based on a function output superimposed 
 * onto a linear path.
 *
 */
public abstract class MovementPattern
{
	//Constants
	protected FloatPoint2D start;
	protected FloatPoint2D end;
	protected MageDefense md;
	protected FloatPoint2D pathVector;
	protected FloatPoint2D normalUnitVector;
	protected QSprite sprite;
	
	
	//constructor
	MovementPattern(FloatPoint2D start, FloatPoint2D end, MageDefense md, QSprite qs){
		this.start = start;
		this.end = end;
		this.md = md;
		
		this.pathVector = FloatPoint2D(this.end.x - this.start.x, this.end.y - this.start.x);
		
	}	
	
	//methods

	/**
	 * The transformation of one point at a given time
	 *
	 */
	public FloatPoint2D transform() {}
}





