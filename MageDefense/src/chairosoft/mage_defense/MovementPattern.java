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
		protected FloatPoint2D end;
		//protected MageDefense md;
		protected FloatPoint2D pathVector;
		protected FloatPoint2D unitNormalVector;
		//protected FloatPoint2D linearPathPosition;
		protected QPhysical2D pathPhysical = new QPhysical2D();
		// protected QSprite sprite;
	
		public Info(QSprite sprite, FloatPoint2D end)
		{
			this.start = sprite.getPosition();
			this.pathPhysical.setPosition(this.start);
			this.pathPhysical.setVelocity(sprite.getVelocity());
			this.pathPhysical.setAcceleration(sprite.getAcceleration());
			this.end = end;
			
			this.pathVector = new FloatPoint2D(this.end.x - this.start.x, this.end.y - this.start.x);
			FloatPoint2D normalVec = new FloatPoint2D(-this.pathVector.y, this.pathVector.x);
			this.unitNormalVector = normalVec.getUnitVector();
		
			//update the QSprite to the new direction
			// FloatPoint2D unitPathVector = this.pathVector.getUnitVector();
// 			double velocityMagnitude = qs.getVelocity().getVectorLength();
// 			qs.setVelocity((float)(unitPathVector.x * velocityMagnitude), (float)(unitPathVector.y * velocityMagnitude));
		}
		
		//Methods

		//// Mutators

		public void setStart(FloatPoint2D newStartPoint){ this.start = newStartPoint; }
		//constructor

 		
	}
		
	public abstract FloatPoint2D getNextTransformPoint(MovementPattern.Info mpi);
}





