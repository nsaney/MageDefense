package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.quadrado.QPhysical2D;
import chairosoft.ui.geom.FloatPoint2D;


public class MovementPatternSprite extends QSprite
{
	protected MovementPattern movementPattern;
	protected MovementPattern.Info mpi;
	
	public MovementPatternSprite(String code) { super(code); }
	public MovementPatternSprite(String code, MovementPattern mp)
	{
		this(code);
		this.setMovementPattern(mp);
	}
	
	public void setMovementPattern(MovementPattern mp)
	{
		this.movementPattern = mp;
	}
	
	public void initializePosition(float x, float y, float x2, float y2)
	{
		this.initializePosition(new FloatPoint2D(x, y), new FloatPoint2D(x2, y2));
	}
	public void initializePosition(FloatPoint2D p1, FloatPoint2D p2)
	{
		this.setPosition(p1.x , p1.y);
		this.mpi = new MovementPattern.Info(this, p2);
	}
	
	@Override
	public void moveOneFrame()
	{
		if(movementPattern == null) { super.moveOneFrame(); }
		else
		{
			//update non position components
			this.mpi.pathPhysical.setVelocity(this.velocity);
			this.mpi.pathPhysical.setAcceleration(this.acceleration);
		
			//move along linear path and record that position
			this.mpi.pathPhysical.moveOneFrame();
			this.setPosition(this.movementPattern.getNextTransformPoint(this.mpi));
		}
	}
}