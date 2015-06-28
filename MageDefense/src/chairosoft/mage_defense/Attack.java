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

public class Attack extends MovementPatternSprite
{
	//Instance-Variables
	public final Element element;
	protected int strength;
	//public int cost;
	public double range;
	public int clickLifeSpan;
	
	//Constructors
	public Attack(String code, MovementPattern mp)
	{
		this(code, mp, Element.NEUTRAL);
	}
    
	public Attack(String code, MovementPattern mp, Element el)
	{
		super(code, mp);
		this.element = el;
	}
	
	//Instance-Methods
	
	//// Mutators
	
	protected void strengthen(int amount){ this.strength += amount;}
	
	public int getStrength(){ return this.strength; }
	
	public void setRange(int range){ this.range = range; }
	
	//// Accessors	
	//// Predefined Attacks
	//public static getFlameAttack()
}