/* 
 * Richard Saney 
 * 
 * Created: May 23, 2015
 * 
 * Enemy.java
 * Enemy class definition
 * 
 */
package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;

import chairosoft.ui.geom.FloatPoint2D;

public class Enemy extends QSprite
{
	//instance-variables
	protected int health;
	protected Element element = Element.NEUTRAL;
	protected Attack attack;
	protected int spawnDistance;
	protected int extraPower = 0;
	protected int extraRange = 0;
	protected int extraAttackSpeed = 0;
	protected MovementPattern movementPattern;
	protected Element[] immunityArray = Element.noElementsArray;
	protected Element[] weaknessArray = Element.noElementsArray;
	
	//constructors
	public Enemy(String code, int health, int spawnDis, MovementPattern mP)
	{
		super(code);
		this.health = health;
		this.spawnDistance = spawnDis;
		mP.setSprite(this);
		this.movementPattern = mP;
	}
	
	public Enemy(String code, int health, int spawnDis, MovementPattern mP,
				 Element el, Element[] immunArr, Element[] weakArr)
	{
		this(code, health, spawnDis, mP);
		this.element = el;
		this.immunityArray = immunArr;
		this.weaknessArray = weakArr;
	}

	public Enemy(String code, int health, int spawnDis, MovementPattern mP,
				 Element el, Element[] immunArr, Element[] weakArr,
				 int extraPow, int extraRng, int extraAtkSpd)
	{
		this(code, health, spawnDis, mP, el, immunArr, weakArr);
		this.extraPower = extraPow;
		this.extraRange = extraRange;
		this.extraAttackSpeed = extraAtkSpd;
	}
	
	/*
	->setPosition() is final and cannot be overridden<-
	@Override
	protected void setPosition(FloatPoint2D pos)
	{
		super.setPosition(pos);
		this.movementPatter.setStart(pos);
	}
	
	@Override
	protected void setPosition(float xPos, float yPos)
	{
		super.setPosition(xPos, yPos);
		this.movementPattern.setStart(new FloatPoint2D(xPos, yPos));
	}
	*/
	
	//// Default Enemies
	// public static Enemy getGhost(){
// 		return new Enemy(String "Ghost_Enemy_Sprite", int 20, int 60, MovementPattern mP);
// 	}
	//public static Enemy getTRex(){}
	//public static Enemy getViperRex(){}
}