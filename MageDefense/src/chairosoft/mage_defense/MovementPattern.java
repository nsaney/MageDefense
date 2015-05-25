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
public class MovementPattern
{
	//Instance Variables
	protected FloatPoint2D start;
	protected FloatPoint2D end;
	//protected MageDefense md;
	protected FloatPoint2D pathVector;
	protected FloatPoint2D unitNormalVector;
	protected FloatPoint2D linearPathPosition;
	protected QSprite sprite;
	//protected long timeStart; //removing time based functionality
	
	//constructor
	public MovementPattern(FloatPoint2D end, QSprite qs) //MageDefense md removed as parameter
	{
		this.start = qs.getPosition();
		this.linearPathPosition = this.start;
		this.end = end;
		this.sprite = qs;
		//this.md = md;
		//this.timeStart = this.md.getFramesElapsedTotal();
		
		this.pathVector = new FloatPoint2D(this.end.x - this.start.x, this.end.y - this.start.x);
		FloatPoint2D normalVec = new FloatPoint2D(-this.pathVector.y, this.pathVector.x);
		this.unitNormalVector = normalVec.getUnitVector();
		
		//update the QSprite to the new direction
		FloatPoint2D unitPathVector = this.pathVector.getUnitVector();
		double velocityMagnitude = qs.getVelocity().getVectorLength();
 		qs.setVelocity((float)(unitPathVector.x * velocityMagnitude), (float)(unitPathVector.y * velocityMagnitude));
	}
	
	//Methods
	
	//// Mutators
	
	public void setStart(FloatPoint2D newStartPoint)
	{
		this.start = newStartPoint;
	}
	
	public void setSprite(QSprite qs)
	{
		this.sprite = qs;
	}	
	//// Transformations
	
	public void moveAlongNonTransformedPath()
	{
		//move along linear path and record that position
		this.sprite.setPosition(this.linearPathPosition);
		this.sprite.moveOneFrame();
		this.linearPathPosition = this.sprite.getPosition();
	}
 
	/**
	 * The linear transformation that translates the sprite along the non transformed path 
	 * before going on to translate the sprite to its transformed path location.
	 *
	 *@param slope slope m
	 *@param intercept y-intercept
	 */
	 public void LinearTransform(double slope, double intercept)
	 {
		moveAlongNonTransformedPath();
		
		//obtain x in transform function graph
		double distanceAlongPath = this.linearPathPosition.distance(this.start);
		
	 	//y = mx + b Linear Transform Displacement
	 	double displacement = slope * distanceAlongPath + intercept;	 	
	 	FloatPoint2D translationVector = this.unitNormalVector.multipliedBy((float)displacement);
		FloatPoint2D transformedPosition = new FloatPoint2D(this.linearPathPosition.x + translationVector.x,
															this.linearPathPosition.y + translationVector.y);
	 	this.sprite.setPosition(transformedPosition);
	 }
}





