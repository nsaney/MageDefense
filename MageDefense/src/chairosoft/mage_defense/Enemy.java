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

	public static final String GHOST_SPRITE_CODE = "Ghost_Enemy_Sprite";
    public static final String TREX_SPRITE_CODE = "TRex_Enemy_Sprite";
	public static final String VIPERREX_SPRITE_CODE = "ViperRex_Sprite";

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
		return new Enemy(Enemy.GHOST_SPRITE_CODE, 20, 32*12, new SineMovementPattern(3,.003,0,0));
	}
	public static Enemy getTRex(){
		return new Enemy(Enemy.TREX_SPRITE_CODE, 30, 32*12, new SineMovementPattern(1,.05,0,0));
	}
	public static Enemy getViperRex(){
		return new Enemy(Enemy.VIPERREX_SPRITE_CODE, 45, 32*12, new SineMovementPattern(2,.01,0,0));
	}
}