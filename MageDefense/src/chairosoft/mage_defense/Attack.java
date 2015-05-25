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

public class Attack 
{
	//Instance-Variables
	protected String name;
	protected AttackSprite attackSprite;
	public final Element element;
	protected MovementPattern movementPattern;
	public int cost;
	public double range;
	public int clickLifeSpan;
	
	//Constructors
	public Attack(AttackSprite aS)
	{
		this.name = "Unnamed Attack";
		this.attackSprite = aS;
		this.element = Element.NEUTRAL;
	}
	
	public Attack(String name, AttackSprite aS, Element el)
	{
		this.name = name;
		this.attackSprite = aS;
		this.element = el;
	}
	
	//Instance-Methods
	
	//// Mutators
	
	protected void setName(String name){ this.name = name; }
	protected void setAttackSprite(AttackSprite aS){ this.attackSprite = aS; }	
	
	//// Accessors
	
	public String getName(){ return this.name; }
	public AttackSprite getAttackSprite(){ return this.attackSprite; }
	public Element getElement(){ return this.element; }
	
	//// Predefined Attacks
	//getFlameAttack
}