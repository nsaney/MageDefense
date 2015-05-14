/* 
 * Nicholas Saney 
 * 
 * Created: August 03, 2014
 * 
 * AttackSprite.java
 * AttackSprite class definition
 * 
 */

package chairosoft.mage_defense;

import chairosoft.quadrado.*;


/**
 * A class representing an attack sprite in Mage Defense.
 * 
 * @author Nicholas Saney
 * @see QSprite
 */
public class AttackSprite extends QSprite implements Comparable<AttackSprite>
{
    protected static int nextId = 0;
    
    public final Integer id = nextId++;
    public final double range;
    public final int clickLifeSpan;
    
    protected int currentClick = 0;
    
    /**
     * Obtains the time elapsed in clicks from initial instantiation.
     *
     * @return clicks elapsed
     */
    public int getCurrentClick() { return this.currentClick; }
    
    /**
     * Same effect as QSprite method, but advances an internal click counter.  
     *
     */
    @Override
    public void advanceAnimationOneClick() { 
    	super.advanceAnimationOneClick(); 
    	this.currentClick++;
    }
    
    /**
     * Compares how long the attack sprite has existed against its given life span.
     *
     * @return true when attack sprite has existed as many clicks as its given life span. 
     */
    public boolean lifeSpanExceeded() { return (this.clickLifeSpan != 0) && (this.clickLifeSpan <= this.currentClick); }
    
    /**
     * Constructor to simply establish distance and life span. A unique id is also
     * assigned in rising count order, starting with 0. 
     * 
     * @param _code identifier which corresponds to the sprite sheet 
     * @param _range the constant distance from original location in which the 
	 * AttackSprite is permitted to exist.
     * @param _clickLifeSpan the constant amount time (in clicks) the AttackSprite may
     * exist during.
     */
    public AttackSprite(String _code, double _range, int _clickLifeSpan)
    {
        super(_code);
        this.range = _range;
        this.clickLifeSpan = _clickLifeSpan;
    }
    
    /**
     * Compares based on id number. Each instance is given a distinct id in rising order.
     *
     * @param qs the AttackSprite to which the comparison is made.
     */
    @Override
    public int compareTo(AttackSprite qs)
    {
        return this.id.compareTo(qs.id);
    }
}
