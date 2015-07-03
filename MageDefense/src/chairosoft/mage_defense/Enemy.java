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
import chairosoft.ui.graphics.Color;
import chairosoft.ui.graphics.DrawingContext;


public class Enemy extends MovementPatternSprite
{

    //LifeBar Info
    public static final int LIFE_BAR_OUTLINE_COLOR = Color.create(0x007f00);
    public static final int LIFE_BAR_FILL_COLOR = Color.create(0x7fff7f);
    public static final double LIFE_FORCE_COUNT_AT_MAX = 64;
    public static final double LIFE_FORCE_BAR_MAX_WIDTH = 32;
    public static final double LIFE_FORCE_BAR_MAX_RATIO = LIFE_FORCE_BAR_MAX_WIDTH / LIFE_FORCE_COUNT_AT_MAX;
    public static final int LIFE_FORCE_BAR_HEIGHT = 4;

    //Sprite Info
    public static final String GHOST_SPRITE_CODE = "Ghost_Enemy_Sprite";
    public static final String TREX_SPRITE_CODE = "TRex_Enemy_Sprite";
    public static final String VIPERREX_SPRITE_CODE = "ViperRex_Sprite";
    
    //ID
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
    
    public void drawAuxContent(DrawingContext ctx)
    {
        int x = (int)this.getPosition().x - 4;
        int y = (int)this.getPosition().y - 4;
        
        int lifeForceBarOutlineWidth = (int)(this.maxHealth * Enemy.LIFE_FORCE_BAR_MAX_RATIO);
		int lifeForceBarFillWidth = (int)(this.health * Enemy.LIFE_FORCE_BAR_MAX_RATIO);
		//ctx.setColor(Enemy.LIFE_BAR_OUTLINE_CODE);
		ctx.setColor(Color.BLACK);
		ctx.drawRect(x, y, lifeForceBarOutlineWidth + 1, Enemy.LIFE_FORCE_BAR_HEIGHT + 1);
		float healthPercent = this.health / (float)this.maxHealth;
        if (healthPercent > .75) 
        { 
            ctx.setColor(Color.GREEN); 
        }
        else if (healthPercent > .4) {ctx.setColor(Color.YELLOW); }
        else {ctx.setColor(Color.RED); }		
        ctx.fillRect(x + 1, y + 1, lifeForceBarFillWidth, Enemy.LIFE_FORCE_BAR_HEIGHT);
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