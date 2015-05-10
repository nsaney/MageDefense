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
 */
public class AttackSprite extends QSprite implements Comparable<AttackSprite>
{
    protected static int nextId = 0;
    
    public final Integer id = nextId++;
    public final double range;
    public final int clickLifeSpan;
    
    protected int currentClick = 0;
    public int getCurrentClick() { return this.currentClick; }
    @Override
    public void advanceAnimationOneClick() { super.advanceAnimationOneClick(); this.currentClick++; }
    public boolean lifeSpanExceeded() { return (this.clickLifeSpan != 0) && (this.clickLifeSpan <= this.currentClick); }
    
    public AttackSprite(String _code, double _range, int _clickLifeSpan)
    {
        super(_code);
        this.range = _range;
        this.clickLifeSpan = _clickLifeSpan;
    }
    
    @Override
    public int compareTo(AttackSprite qs)
    {
        return this.id.compareTo(qs.id);
    }
}
