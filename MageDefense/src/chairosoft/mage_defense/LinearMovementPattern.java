package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QPhysical2D;
import chairosoft.ui.geom.FloatPoint2D;


public class LinearMovementPattern implements MovementPattern
{

	double slope;
	double intercept;
	/**
	 * The linear transformation that translates the sprite along the non transformed path 
	 * before going on to translate the sprite to its transformed path location.
	 *
	 *@param slope slope m
	 *@param intercept y-intercept
	 */
	 @Override
	 public FloatPoint2D getNextTransformPoint(MovementPattern.Info mpi)
	 {		
		//obtain x in transform function graph
		double distanceAlongPath = mpi.pathPhysical.getPosition().distance(mpi.start);
		
	 	//y = mx + b Linear Transform Displacement
	 	double displacement = this.slope * distanceAlongPath + this.intercept;	 	
	 	FloatPoint2D translationVector = mpi.unitNormalVector.multipliedBy((float)displacement);
		FloatPoint2D transformedPosition = new FloatPoint2D(mpi.pathPhysical.getPosition().x + translationVector.x,
															mpi.pathPhysical.getPosition().y + translationVector.y);
	 	return transformedPosition;
	 }

}