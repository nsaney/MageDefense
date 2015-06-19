/* 
 * MageDefense.java 
 * MageDefense main and auxiliary methods
 * 
 */

package chairosoft.mage_defense;

import static chairosoft.quadrado.QCompassDirection.*;
import chairosoft.quadrado.QApplication;
import chairosoft.quadrado.QCompassDirection;
import chairosoft.quadrado.QCompassKeypad;

// import chairosoft.ui.audio.*;
// import chairosoft.ui.geom.*;
import chairosoft.ui.event.ButtonEvent;
import chairosoft.ui.event.PointerEvent;
import chairosoft.ui.graphics.DrawingContext;
import chairosoft.ui.graphics.Font;
// import chairosoft.util.function.*;

// import java.io.*; 
// import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MageDefense extends QApplication
{
	//
	// Main Method
	// 
	
	public static void main(String[] args)
	{
	    System.err.println("Starting game... ");
		MageDefense app = new MageDefense();
        app.setFrameRateInHz(100);
        //app.setUseButtonListener(true); // not going to use this for MageDefense
        app.setUsePointerListener(true);
	    app.gameStart();
	}
    
    
    //
    // Constructor
    //
    
    public MageDefense() 
    {
        super("Mage Defense"); 
    }
    
    
    //
    // Constants
    //
    
    public static final int PANEL_WIDTH = 768;
    public static final int PANEL_HEIGHT = 432;
    @Override public int getPanelWidth() { return PANEL_WIDTH; }
    @Override public int getPanelHeight() { return PANEL_HEIGHT; }
    
    public static final int X_SCALING = 1;
    public static final int Y_SCALING = 1;
    @Override public int getXScaling() { return X_SCALING; }
    @Override public int getYScaling() { return Y_SCALING; }

    
    
    //
    // Instance Variables
    //
    
    protected final GameState GAME_NOT_LOADED = new GameNotLoaded(this);
    protected final GameState MAPROOM_LOADING = new MapRoomLoading(this);
    protected final GameState MAPROOM_LOADED = new MapRoomLoaded(this);
    protected final GameState NORMAL_GAMEPLAY = new NormalGameState(this);
    
    protected volatile GameState gameState = this.GAME_NOT_LOADED;
    
    
    //
    // Instance Methods 
    //
    
    public long getFramesElapsedTotal() { return this.framesElapsedTotal; }
    
    public void loadNormalGameplay()
    {
        // If we need to do this asynchronously, we would make use of MAPROOM_LOADING.
        // That would be for something like loading background music files.
        // But otherwise, we can just go straight to MAPROOM_LOADED.
        this.gameState = this.MAPROOM_LOADED;
    }
    
    
    @Override
    protected void qGameInitialize() 
    {
        System.err.println("GAME INITIALIZED");
        //Loading.startVerbose();
    }
    
    @Override
    protected void qGameUpdateInit() { }
    
    @Override protected void qButtonPressed(ButtonEvent.Code buttonCode) { }
    @Override protected void qButtonHeld(ButtonEvent.Code buttonCode) { }
    @Override protected void qButtonReleased(ButtonEvent.Code buttonCode) { }
    @Override protected void qButtonNotHeld(ButtonEvent.Code buttonCode) { }
    
    
    @Override
    protected void qPointerPressed(float x, float y)
    {
        this.gameState.pointerPressed(x, y);
    }
    
    @Override
    protected void qPointerMoved(float x, float y)
    {
        this.gameState.pointerMoved(x, y);
    }
    
    @Override
    protected void qPointerReleased(float x, float y)
    {
        this.gameState.pointerReleased(x, y);
    }
    
    @Override
    protected void qGameUpdate() 
    {
        // do state-specific update
        this.gameState.update();
    }
    
    @Override
    protected void qGameRender(DrawingContext ctx) 
    {
        this.gameState.render(ctx);
    }
    
    @Override
    protected void qGameFinish()
    {
        System.err.println("GAME FINISHED");
    }
    
}