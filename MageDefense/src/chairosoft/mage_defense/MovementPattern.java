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
	protected MageDefense md;
	protected FloatPoint2D pathVector;
	protected FloatPoint2D unitNormalVector;
	protected FloatPoint2D linearPathPosition;
	protected QSprite sprite;
	protected long timeStart;
	
	//constructor
	MovementPattern(FloatPoint2D end, MageDefense md, QSprite qs){
		this.start = qs.getPosition();
		this.linearPathPosition = this.start;
		this.end = end;
		this.md = md;
		this.sprite = qs;
		this.timeStart = this.md.getFramesElapsedTotal();
		
		this.pathVector = new FloatPoint2D(this.end.x - this.start.x, this.end.y - this.start.x);
		FloatPoint2D normalVec = new FloatPoint2D(-this.pathVector.y, this.pathVector.x);
		this.unitNormalVector = normalVec.getUnitVector();
		
		//update the QSprite to the new direction
		FloatPoint2D unitPathVector = this.pathVector.getUnitVector();
		double velocityMagnitude = qs.getVelocity().getVectorLength();
// 		qs.setVelocity((float)(unitPathVector.x * velocityMagnitude), (float)(unitPathVector.y * velocityMagnitude));
	}
	
	//Methods
	
	//// Mutators
	
	public void setStart(FloatPoint2D newStartPoint)
	{
		this.start = newStartPoint;
	}
	
	
	//// Transformations
	
	public void moveAlongNonTransformedPath(){
		//move along linear path and record that position
		this.sprite.setPosition(this.linearPathPosition);
		this.sprite.moveOneFrame();
		this.linearPathPosition = this.sprite.getPosition();
	}
	
	/**
	 * The linear transformation of the sprite at a given time along the path.
	 * 
	 *
	 */
	 public void LinearTransformOld(double slope, double intercept){
	 	
	 	long timeT = this.md.getFramesElapsedTotal() - this.timeStart;
	 	FloatPoint2D nonTransformedPosition = new FloatPoint2D(timeT * this.sprite.getVelocity().x,
	 														   timeT * this.sprite.getVelocity().y);
	 	double distanceAlongPath = nonTransformedPosition.distance(this.start);
	 	//y = mx + b
	 	double displacement = slope * distanceAlongPath + intercept;
	 	FloatPoint2D transformedPosition = new FloatPoint2D(nonTransformedPosition.x + (float)(displacement * this.unitNormalVector.x),
	 											 			nonTransformedPosition.y + (float)(displacement * this.unitNormalVector.y));
	 	this.sprite.setPosition(transformedPosition);
	 }
	 
	/**
	 * The linear transformation that translates the sprite along the non transformed path 
	 * before going on to translate the sprite to its transformed path location.
	 *
	 */
	 public void LinearTransform(double slope, double intercept){
		
		moveAlongNonTransformedPath();
		
		//obtain x in transform function graph
		double distanceAlongPath = this.linearPathPosition.distance(this.start);
	 	//y = mx + b Linear Transform Displacement
	 	double displacement = slope * distanceAlongPath + intercept;
	 	FloatPoint2D transformedPosition = new FloatPoint2D(this.linearPathPosition.x + (float)(displacement * this.unitNormalVector.x),
	 											 			this.linearPathPosition.y + (float)(displacement * this.unitNormalVector.y));
	 	this.sprite.setPosition(transformedPosition);
	 }
}





