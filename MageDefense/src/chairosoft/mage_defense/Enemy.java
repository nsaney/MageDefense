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

public class Enemy extends MovementPatternSprite
{
	//instance-variables
	protected int health;
	protected Element element = Element.NEUTRAL;
	protected Attack attack;
	protected int spawnDistance;
	protected int extraPower = 0;
	protected int extraRange = 0;
	protected int extraAttackSpeed = 0;
	protected Element[] immunityArray = Element.noElementsArray;
	protected Element[] weaknessArray = Element.noElementsArray;
	
	//constructors
	public Enemy(String code, int health, int spawnDis, MovementPattern mp)
	{
		super(code, mp);
		this.health = health;
		this.spawnDistance = spawnDis;
	}
	
	public Enemy(String code, int health, int spawnDis, MovementPattern mp,
				 Element el, Element[] immunArr, Element[] weakArr)
	{
		this(code, health, spawnDis, mp);
		this.element = el;
		this.immunityArray = immunArr;
		this.weaknessArray = weakArr;
	}

	public Enemy(String code, int health, int spawnDis, MovementPattern mp,
				 Element el, Element[] immunArr, Element[] weakArr,
				 int extraPow, int extraRng, int extraAtkSpd)
	{
		this(code, health, spawnDis, mp, el, immunArr, weakArr);
		this.extraPower = extraPow;
		this.extraRange = extraRange;
		this.extraAttackSpeed = extraAtkSpd;
	}
	
	//// Default Enemies
	public static Enemy getGhost()
	{
		return new Enemy("Ghost_Enemy_Sprite", 20, 60, new SineMovementPattern(32,1,0,0));
	}
	//public static Enemy getTRex(){}
	//public static Enemy getViperRex(){}
}