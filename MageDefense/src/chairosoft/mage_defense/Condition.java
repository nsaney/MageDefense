/* 
 * Richard Saney 
 * 
 * Created: June 28, 2015
 * 
 * Condition.java
 * Condition class definition
 * 
 */
package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.ui.geom.FloatPoint2D;

public enum Condition
{
    BURNED("Burned"),
    CONFUSED("Confused"),
    HEALER("Healer"),
    STATUE("Statue"),
    DEAD("Dead"),
    NEUTRAL("Neutral");
    
    public final String name;
            
    Condition(String name)
    {
        this.name = name;
    }
    
}