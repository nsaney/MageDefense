package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QPhysical2D;
import chairosoft.ui.geom.FloatPoint2D;


public class LinearMovementPattern implements MovementPattern
{

	double slope;
	double intercept;
	
	public LinearMovementPattern(double slope, double intercept)
	{
		this.slope = slope;
		this.intercept = intercept;
	}

	/**
	 * The linear transformation that translates the sprite along the non transformed path 
	 * before going on to translate the sprite to its transformed path location.
	 *
	 * @param mpi information used in calculating untransformed path
	 * 
	 * @return the transformed point along graph
	 */
	 @Override
	 public FloatPoint2D getNextTransformPoint(MovementPattern.Info mpi)
	 {		
		//obtain x in transform function graph
		double distanceAlongPath = mpi.pathPhysical.getPosition().distance(mpi.start);
		
	 	//y = mx + b Linear Transform Displacement
	 	double displacement = this.slope * distanceAlongPath + this.intercept;
	 	//System.out.println("Displacement: "+displacement);
		FloatPoint2D translationVector = new FloatPoint2D(mpi.unitNormalVector.x * (float)displacement,
 	 													  mpi.unitNormalVector.y * (float)displacement);
	 	//FloatPoint2D translationVector = mpi.unitNormalVector.multipliedBy((float)displacement);
	 	System.out.println("Translation Vector"+translationVector.x+", "+translationVector.y);
		FloatPoint2D transformedPosition = new FloatPoint2D(mpi.pathPhysical.getPosition().x + translationVector.x,
															mpi.pathPhysical.getPosition().y + translationVector.y);
	 	return transformedPosition;
	 }

}