package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QPhysical2D;
import chairosoft.ui.geom.FloatPoint2D;

public class SineMovementPattern implements MovementPattern
{
	
	double amplitude;
	double angularFrequency;
	double phase;
	double offset;
	
	public SineMovementPattern(double amplitude, double frequency, double phase, double offset)
	{
		this.amplitude = amplitude;
		this.angularFrequency = frequency * 2 * Math.PI;
		this.phase = phase;
		this.offset = offset;
	}
	
	@Override
	public FloatPoint2D getNextTransformPoint(MovementPattern.Info mpi)
	{		
		//obtain x in transform function graph
		double distanceAlongPath = mpi.pathPhysical.getPosition().distance(mpi.start);
		
		//A * sin ( (2 * PI * f * t) + ph) + offset   Sine transform displacement
		double displacement = this.amplitude * Math.sin((this.angularFrequency * distanceAlongPath) + this.phase) + this.offset;
		
		FloatPoint2D translationVector = mpi.unitNormalVector.multipliedBy((float)displacement);
		FloatPoint2D transformedPosition = new FloatPoint2D(mpi.pathPhysical.getPosition().x + translationVector.x,
															mpi.pathPhysical.getPosition().y + translationVector.y);
	 	return transformedPosition;
		
	}
}