/* 
 * Richard Saney 
 * 
 * Created: May 23, 2015
 * 
 * Element.java
 * Element class definition
 * 
 */
package chairosoft.mage_defense;

import chairosoft.quadrado.QSprite;
import chairosoft.ui.geom.FloatPoint2D;

public enum Element
{
    NEUTRAL("Neutral"),
    DARK("Dark"),
    LIGHT("Light"),
    EARTH("Earth"),
    WIND("Wind"),
    WATER("Water"),
    FIRE("Fire"),
    LIGHTNING("Lightning");
    
    public final String name;
        
    public static final Element[] noElementsArray = {};
    
    Element(String name)
    {
        this.name = name;
    }
    
    public Element getOppositeElement(Element el)
    {
        switch(el)
        {
            case NEUTRAL:
                return null;
            case DARK:
                return Element.LIGHT;
            case LIGHT:
                return Element.DARK;
            case EARTH:
                return Element.WIND;
            case WIND:
                return Element.EARTH;
            case WATER:
                return Element.FIRE;
            case FIRE:
                return Element.WATER;
            case LIGHTNING:
                return null;
            default:
                System.out.println("Could not identify element passed.");
                return null;
        }
    }
    
}