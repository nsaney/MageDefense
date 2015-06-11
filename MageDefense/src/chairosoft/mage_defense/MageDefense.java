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

import chairosoft.desktop.DesktopDoubleBufferedUI;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

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
	    app.gameStart();
        
        DesktopDoubleBufferedUI dbui = (DesktopDoubleBufferedUI)app.getDbui();
        JPanel panel = dbui.getPanel();
        panel.addKeyListener(app.keyAdapter);
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
    
    protected final QCompassKeypad keypad = new QCompassKeypad();
    
    protected volatile ConcurrentLinkedQueue<KeyEvent> keyEventQueue = new ConcurrentLinkedQueue<>();
    public final KeyAdapter keyAdapter = new KeyAdapter()
    {
        @Override public void keyPressed(KeyEvent ke) { MageDefense.this.keyEventQueue.offer(ke); }
        @Override public void keyReleased(KeyEvent ke) { MageDefense.this.keyEventQueue.offer(ke); }
    };
    
    
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
    
    
    protected void qKeyPressed(int keyCode)
    {
        // keypad needs to respond, even if loading
        switch (keyCode)
        {
            case KeyEvent.VK_LEFT: keypad.activateValue(WEST); break; // left
            case KeyEvent.VK_RIGHT: keypad.activateValue(EAST); break; // right
            case KeyEvent.VK_UP: keypad.activateValue(NORTH); break; // up
            case KeyEvent.VK_DOWN: keypad.activateValue(SOUTH); break; // down
        }
        
        this.gameState.keyPressed(keyCode);
    }
    
    protected void qKeyReleased(int keyCode)
    {
        // keypad needs to respond, even if loading
        switch (keyCode)
        {
            case KeyEvent.VK_LEFT: keypad.deactivateValue(WEST); break; // left
            case KeyEvent.VK_RIGHT: keypad.deactivateValue(EAST); break; // right
            case KeyEvent.VK_UP: keypad.deactivateValue(NORTH); break; // up
            case KeyEvent.VK_DOWN: keypad.deactivateValue(SOUTH); break; // down
            default: break;
        }
        
        this.gameState.keyReleased(keyCode);
    }
    
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
        // hook for key event handling
        int keyEventCount = keyEventQueue.size(); // only process key events in the queue as of this call
        for (int i = 0; i < keyEventCount; ++i)
        {
            KeyEvent e = keyEventQueue.poll();
            if (e != null) 
            {
                switch (e.getID())
                {
                    case KeyEvent.KEY_PRESSED: this.qKeyPressed(e.getKeyCode()); break;
                    case KeyEvent.KEY_RELEASED: this.qKeyReleased(e.getKeyCode()); break;
                }
            }
        }
        
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