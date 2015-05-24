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
	String name;
	int health;
	Element element;
	int extraPower;
	int extraRange;
	int extraAttackSpeed;
	MovementPattern movementPattern;
	int spawnDistance;
	double speed;
	Element[] immunityArray;
	Element[] weaknessArray;
	
	//constructor
	public Enemy(String code, String name, int health)
	{
		super(code);
		this.name = name;
		this.health = health;
	}
// 	
// 	public Enemy(String code, String name, int health, Element el, Element immunArr)
// 	{
// 		
// 	}
// 	
// 	public Enemy(String code, String name, int health, int extraPow, int extraRng, int extraAtkSpd,
// 				 MovementPattern movPat, Element el, int spawnDis, double speed, 
// 				 Element[] immunArr, Element[] weaknessArr)
// 	{
// 		this(code, name, health);
// 		
// 	
// 	}
}