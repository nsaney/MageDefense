/* 
 * Nicholas Saney 
 * 
 * Created: August 03, 2014
 * 
 * MageDefensePlayer.java
 * MageDefensePlayer class definition
 * 
 */

package chairosoft.mage_defense;

import chairosoft.quadrado.*;
import chairosoft.ui.graphics.Color;


/**
 * A class representing player status in Mage Defense.
 * 
 * @author Nicholas Saney
 */
public class MageDefensePlayer
{
    //
    // Inner Enums/Classes
    //
    
    public static enum AbilityType
    {
        NONE(""),
        FIRE_ATTACK("Flame_Attack_Sprite"), 
        WHIRLWIND_ATTACK("Whirlwind_Attack_Sprite"), 
        BOLT_ATTACK("Bolt_Attack_Sprite"), 
        EARTHQUAKE_ATTACK("Earthquake_Attack_Sprite"),
        SWORD_ATTACK("Sword_Attack_Sprite"); 
        
        public final String spriteCode;
        AbilityType(String s) { this.spriteCode = s; }
    }
    
    public static enum AbilityLevel
    {
        _1,
        _2,
        _3;
    }
    
    public static class Ability
    {
        public final AbilityType type;
        public final AbilityLevel level;
        public final int cost;
        public final double range;
        public final float velocity;
        public final int clickLifeSpan;
        
        public Ability(AbilityType t, AbilityLevel l, int c, double r, float v, int s)
        {
            this.type = t; this.level = l; this.cost = c; this.range = r; this.velocity = v; this.clickLifeSpan = s;
        }
        
        @Override
        public String toString()
        {
            return String.format("[type: %s, level: %s, cost: %s, range: %s, velocity: %s]", type, level, cost, range, velocity);
        }
    }
    
    public static enum PlayerStatus
    {
        NORMAL(0x007f00, 0x7fff7f),
        BURNOUT(0x7f0000, 0xff7f7f), 
        DEAD(0x000000, 0x7f7f7f);
        
        public final int barOutlineColorCode;
        public final int barFillColorCode;
        
        PlayerStatus(int o, int f)
        {
            this.barOutlineColorCode = Color.create(o);
            this.barFillColorCode = Color.create(f);
        }
    }
    
    
    //
    // Static Fields
    //
    
    public static final Ability[] availableAbilities = 
    {
        //          AbilityType,                  AbilityLevel,    cost,                       range, velocity, clickLifeSpan
        new Ability(AbilityType.NONE,             AbilityLevel._1,   00, 0 * QTileset.getTileWidth(),        0,             0),
        new Ability(AbilityType.FIRE_ATTACK,      AbilityLevel._1,   01, 4 * QTileset.getTileWidth(),        1,             0),
        new Ability(AbilityType.WHIRLWIND_ATTACK, AbilityLevel._1,   02, 6 * QTileset.getTileWidth(),        1,             0),
        new Ability(AbilityType.BOLT_ATTACK,      AbilityLevel._1,   03, 8 * QTileset.getTileWidth(),        3,             0),
        new Ability(AbilityType.SWORD_ATTACK,     AbilityLevel._1,   00, 3 * QTileset.getTileWidth(),        0,            40)
    };
    
    public static final Ability getAbilityIfAvailable(AbilityType t, AbilityLevel l)
    {
        Ability result = null;
        
        for (Ability ability : availableAbilities)
        {
            if (ability.type == t && ability.level == l)
            {
                result = ability;
                break;
            }
        }
        
        return result;
    }
    
    
    //
    // Instance Fields
    //
    
    protected PlayerStatus status = PlayerStatus.NORMAL;
    public PlayerStatus getStatus() { return this.status; }
    
    public double regenAmount = 5.0;
    public int framesBetweenRegen = 100;
    
    protected double lifeForceMax = 10.0;
    protected double lifeForceCurrent = 10.0;
    public double getLifeForceMax() { return this.lifeForceMax; }
    public double getLifeForceCurrent() { return this.lifeForceCurrent; }
    public double getLifeForcePercent() { return this.lifeForceCurrent / this.lifeForceMax; }
    public void addToLifeForceMax(double amt) 
    {
        this.lifeForceMax += amt; 
        if (this.lifeForceMax < 0.0) { this.lifeForceMax = 0.0; }
        this.addToLifeForceCurrent(0); 
    }
    public void addToLifeForceCurrent(double amt) 
    { 
        this.lifeForceCurrent += amt; 
        if (this.lifeForceCurrent < 0.0) { this.lifeForceCurrent = 0.0; }
        if (this.lifeForceCurrent > this.lifeForceMax) { this.lifeForceCurrent = this.lifeForceMax; }
        
        if (this.lifeForceCurrent == 0.0) { this.status = PlayerStatus.BURNOUT; }
        else if (this.lifeForceCurrent == this.lifeForceMax) { this.status = PlayerStatus.NORMAL; }
    }
    
    protected Ability chosenAbility = null;
    public Ability getChosenAbility() { return this.chosenAbility; }
    protected void updateChosenAbility() { this.chosenAbility = getAbilityIfAvailable(this.chosenAbilityType, this.chosenAbilityLevel); }
    
    protected AbilityType chosenAbilityType = AbilityType.NONE;
    public void chooseAbilityType(AbilityType t) { this.chosenAbilityType = t; this.updateChosenAbility(); }
    
    protected AbilityLevel chosenAbilityLevel = AbilityLevel._1;
    public void chooseAbilityLevel(AbilityLevel l) { this.chosenAbilityLevel = l; this.updateChosenAbility(); }
}