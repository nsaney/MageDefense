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
import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QPhysical2D;
import chairosoft.ui.geom.FloatPoint2D;

/**
 * The transformation of a movement based on a function output superimposed 
 * onto a linear path.
 *
 */
public interface MovementPattern
{
	public static class Info
	{
		//Instance Variables
		protected FloatPoint2D start;
		protected FloatPoint2D pathDirection;
		protected FloatPoint2D pathVector;
		protected FloatPoint2D unitNormalVector;
		protected QPhysical2D pathPhysical = new QPhysical2D();
		
		//constructor	
		public Info(QSprite sprite, FloatPoint2D pathDirection)
		{
			this.start = sprite.getPosition();
			this.pathPhysical.setPosition(this.start);
			this.pathPhysical.setVelocity(sprite.getVelocity());
			this.pathPhysical.setAcceleration(sprite.getAcceleration());
			this.pathDirection = pathDirection;
			
			this.pathVector = new FloatPoint2D(this.pathDirection.x - this.start.x, this.pathDirection.y - this.start.x);
			FloatPoint2D normalVec = new FloatPoint2D(-this.pathVector.y, this.pathVector.x);
			this.unitNormalVector = normalVec.getUnitVector();
		
			//update the QSprite to the new direction
			// FloatPoint2D unitPathVector = this.pathVector.getUnitVector();
// 			double velocityMagnitude = qs.getVelocity().getVectorLength();
// 			qs.setVelocity((float)(unitPathVector.x * velocityMagnitude), (float)(unitPathVector.y * velocityMagnitude));
		}
		
		//Methods

		//// Mutators

		public void setStart(FloatPoint2D newStartPoint) { this.start = newStartPoint; }
		public void setDirectionTowards(FloatPoint2D newDirectionPoint) { this.pathDirection = newDirectionPoint; }

		//constructor
	}
		
	public FloatPoint2D getNextTransformPoint(MovementPattern.Info mpi);
}





