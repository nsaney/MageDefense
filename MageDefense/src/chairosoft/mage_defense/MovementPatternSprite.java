package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QPhysical2D;
import chairosoft.ui.geom.FloatPoint2D;


public class MovementPatternSprite extends QSprite
{
	MovementPattern movementPattern;
	MovementPattern.Info mpi;
	
	public MovementPatternSprite(String code, MovementPattern mp)
	{
		super(code);
		this.movementPattern = mp;
	}
	
	@Override
	public void moveOneFrame()
	{
		//update non position components
		this.mpi.pathPhysical.setVelocity(this.velocity);
		this.mpi.pathPhysical.setAcceleration(this.acceleration);
		
		//move along linear path and record that position
		this.mpi.pathPhysical.moveOneFrame();
		this.setPosition(this.movementPattern.getNextTransformPoint(this.mpi));
	
	}
}