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
    protected static int nextId = 0;

    //instance-variables
    public final int id = nextId++;
    public final int maxHealth;
    private Element element = Element.NEUTRAL;
    protected int health;
    protected Attack attack;
    protected Condition condition = Condition.NEUTRAL;
    protected int extraPower = 0;
    protected int extraRange = 0;
    protected int extraAttackSpeed = 0;
    protected Element[] immunityArray = Element.noElementsArray;
    protected Element[] weaknessArray = Element.noElementsArray;
    protected int flinchCount = 0;
    
    
    //constructors
    public Enemy(String code, int health, MovementPattern mp)
    {
        super(code, mp);
        this.maxHealth = health;
        this.health = health;
    }
    
    public Enemy(String code, int health, MovementPattern mp,
                 Element el, Element[] immunArr, Element[] weakArr)
    {
        this(code, health, mp);
        this.element = el;
        this.immunityArray = immunArr;
        this.weaknessArray = weakArr;
    }

    public Enemy(String code, int health, MovementPattern mp,
                 Element el, Element[] immunArr, Element[] weakArr,
                 int extraPow, int extraRng, int extraAtkSpd)
    {
        this(code, health, mp, el, immunArr, weakArr);
        this.extraPower = extraPow;
        this.extraRange = extraRange;
        this.extraAttackSpeed = extraAtkSpd;
    }
    
    public int getID()
    {    
        return this.id;
    }
        
    public void damage(int damage)
    { 
        this.health -= damage;
        this.condition = (this.health <= 0)? Condition.DEAD : this.condition;
    }
    
    public int getHealth(){ return this.health; }
    
    protected void setFlinchCount(int count){ this.flinchCount = count;}
    
    public int getFlinchCount(){ return this.flinchCount; }
    
    public Condition getCondition(){ return this.condition; }
    
    public Element getElement(){ return this.element; }
    
    public void setCondition(Condition condition){ this.condition = condition; }
    
    public boolean isOutOfRectangle(float widthMin, float widthMax, float heightMin, float heightMax)
    {
        FloatPoint2D pos = this.getPosition();
        return pos.x < widthMin || pos.x > widthMax || pos.y < heightMin || pos.y > heightMax;
    }
    public boolean isOutOfRectangle(FloatPoint2D point1, FloatPoint2D point2)
    {
        float widthMin; 
        float widthMax;
        if(point1.x < point2.x)
        {
            widthMin = point1.x;
            widthMax = point2.x;
        } 
        else
        {
            widthMin = point2.x;
            widthMax = point1.x;
        }
        float heightMin;
        float heightMax;
        if(point1.y < point2.y)
        {
            heightMin = point1.y;
            heightMax = point2.y;
        } 
        else
        {
            heightMin = point2.y;
            heightMax = point1.y;
        }
        return isOutOfRectangle(widthMin, widthMax, heightMin, heightMax);
    }
    
    //// Default Enemies
    public static Enemy getGhost()
    {
        return new Enemy(Enemy.GHOST_SPRITE_CODE, 20, new SineMovementPattern(16,.01,0,0));
        //return new Enemy(Enemy.GHOST_SPRITE_CODE, 20, new LinearMovementPattern(.2 ,0));
    }
    public static Enemy getTRex(){
        return new Enemy(Enemy.TREX_SPRITE_CODE, 30, new LinearMovementPattern(.2 ,0));
    }
    public static Enemy getViperRex(){
        return new Enemy(Enemy.VIPERREX_SPRITE_CODE, 45, new SineMovementPattern(15,.001,0,0));
    }
}