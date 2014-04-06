package com.spaceagencies.i3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.spaceagencies.common.tools.Log;
import com.spaceagencies.common.tools.RessourceLoadingException;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.Point;
import com.spaceagencies.server.Time;
import com.spaceagencies.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;

public class Surface {

    private Activity currentActivity;
    private Stack<Intent> intentStack = new Stack<Intent>();
    private Map<Intent, Activity> activityMap = new HashMap<Intent, Activity>();
    private Color backgroundColor;

    public Surface(I3dContext context) {
        this.context = context;
        backgroundColor = Color.black;
    }

    public void startActivity(Intent intent) {
        Activity activity = null;

        try{
        if (!intentStack.contains(intent)) {
            // New Activity
            try {
                activity = intent.getActivityClass().newInstance();
                activityMap.put(intent, activity);
                activity.setContext(context);
                activity.assignSurface(this);
                activity.setIntent(intent);
                activity.create();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            // Set existing activity on top
            activity = activityMap.get(intent);
            intentStack.remove(intent);
        }

        // Pause current activity
        if (currentActivity != null) {
            currentActivity.pause();
        }

        currentActivity = activity;

        if (currentActivity != null) {
            if(currentActivity.isStackable()) {
                intentStack.push(intent);
            }
            currentActivity.setPreferredPosition(mPopupPreferredPosition);
            currentActivity.forceLayout();
            currentActivity.resume();
        }
        } catch (RessourceLoadingException e) {
            Log.warn("Fail to start Activity: "+e);
            currentActivity = null;
        }
    }
    
    public boolean unstackActivity() {
        
        boolean unstack = false;
        
        // Drop current intent
        if(currentActivity != null) {
            Intent dropedIntent = currentActivity.getIntent();
            intentStack.remove(dropedIntent);
            activityMap.remove(dropedIntent);
            unstack  = true;
        }
        
        if(intentStack.size() == 0) {
            if(currentActivity != null) {
                if(currentActivity.isStackable()) {
                    // No activity yo unstack, restart the current one
                    startActivity(currentActivity.getIntent());
                } else {
                    currentActivity.pause();
                    currentActivity.destroy();
                    currentActivity = null;
                }
            }
        } else {
            // Restart new top intent 
            startActivity(intentStack.peek());
        }
        
        return unstack;
    }

    public void update(Timestamp time) {
        Activity lastActivity = null;
        while (currentActivity != lastActivity && currentActivity != null) {
            lastActivity = currentActivity;
            currentActivity.update(time);
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void draw(Graphics g) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glViewport(x, y, width, height);
        GL11.glScissor(x, y, width, height);
        g.initUiTranslation(new Point(x, -y-height));
        
        
        
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        if(!backgroundColor.isSame(Color.transparent)) {
            GL11.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        }

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        // 2D
        GLU.gluOrtho2D(0, width, height, 0);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if (currentActivity != null) {
            currentActivity.draw(g);
        }
        
        GL11.glPopAttrib();
    }

    public enum LocationMode {

        ABSOLUTE, RELATIVE, ABSOLUTE_INVERT, RELATIVE_INVERT,
    }

    //
    // ------------------
    // Private stuff
    // ------------------
    //

    public int preferredX;
    public int preferredY;
    public int preferredWidth;
    public int preferredHeight;
    public int marginTop;
    public int marginBottom;
    public int marginLeft;
    public int marginRight;
    public LocationMode preferredXMode;
    public LocationMode preferredYMode;
    public LocationMode preferredWidthMode;
    public LocationMode preferredHeightMode;
    public int x;
    public int y;
    public int width;
    public int height;
    public int parentWidth;
    public int parentHeight;
//    public int mouseX = 0;
//    public int mouseY = 0;
    //private Point lastMousePosition = new Point();
    private I3dContext context;
    private I3dVec2 mPopupPreferredPosition;

    /**
     * Internal private method
     * 
     * @return
     */
    /*
     * public boolean isFocused() { }
     */

    /**
     * Internal private method
     * 
     * @param parentWidth
     * @param parentHeight
     */
    public void configure(int parentWidth, int parentHeight) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        configure();
    }

    /**
     * Internal private method
     */
    public void configure() {
        int tempX = 0;
        int tempY = 0;
        int tempWidth = 0;
        int tempHeight = 0;

        if (preferredXMode == LocationMode.ABSOLUTE) {
            tempX = preferredX;
        } else if (preferredXMode == LocationMode.RELATIVE) {
            tempX = (preferredX * parentWidth) / 100;
        }

        if (preferredYMode == LocationMode.ABSOLUTE) {
            tempY = preferredY;
        } else if (preferredYMode == LocationMode.RELATIVE) {
            tempY = (preferredY * parentHeight) / 100;
        }

        if (preferredWidthMode == LocationMode.ABSOLUTE) {
            tempWidth = preferredWidth;
        } else if (preferredWidthMode == LocationMode.RELATIVE) {
            tempWidth = (preferredWidth * parentWidth) / 100;
        }

        if (preferredHeightMode == LocationMode.ABSOLUTE) {
            tempHeight = preferredHeight;
        } else if (preferredHeightMode == LocationMode.RELATIVE) {
            tempHeight = (preferredHeight * parentHeight) / 100;
        }

//        mouseX = tempX;
//        mouseY = tempY;

        x = tempX + marginLeft;
        y = parentHeight - tempY - tempHeight - marginBottom;
        width = tempWidth - marginLeft - marginRight;
        height = tempHeight - marginTop - marginBottom;

        if (currentActivity != null) {
            currentActivity.forceLayout();
        }

        // gui.repack();
        // camera.setSize(width, height);
        // gui.generate();

    }

    /**
     * Internal private method
     * 
     * @param mouseX
     * @param mouseY
     * @return
     */
    public boolean contains(int mouseX, int mouseY) {

        if (mouseX < x || mouseY < y) {
            return false;
        }

        if (mouseX > x + width || mouseY > y + height) {
            return false;
        }

        return true;
    }

    /**
     * Internal private method
     * 
     * @param testMouseX
     * @param testMouseY
     * @return
     */
//    public boolean containsMouse(int testMouseX, int testMouseY) {
//
//        if (testMouseX < mouseX || testMouseY < mouseY) {
//            return false;
//        }
//
//        if (testMouseX > mouseX + width || testMouseY > mouseY + height) {
//            return false;
//        }
//
//        return true;
//    }

    /**
     * Internal private method
     * 
     * @return
     */
//    public Point getLastMousePosition() {
//        return lastMousePosition;
//    }

    /**
     * Internal private method
     * 
     * @param lastMousePosition
     */
//    public void setLastMousePosition(Point lastMousePosition) {
//        this.lastMousePosition = lastMousePosition;
//    }

    public void setContext(I3dContext context) {
        this.context = context;
    }
    
    public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
        if (currentActivity != null) {
            return currentActivity.onMouseEvent(mouseEvent);
        }
        return false;
    }
    
    public boolean onKeyEvent(V3DKeyEvent keyEvent) {
        if(keyEvent.getAction() == V3DKeyEvent.KeyAction.KEY_PRESSED &&  keyEvent.getKeyCode() == V3DKeyEvent.KEY_ESCAPE) {
            // TODO : make loose focus if focused item
            return unstackActivity();
        } else {
            if(currentActivity != null) {
                return currentActivity.onKeyEvent(keyEvent);
            }
        }
        return false;
    }
    
    public void onControllerEvent(V3DControllerEvent controllerEvent) {
        if (currentActivity != null) {
            currentActivity.onControllerEvent(controllerEvent);
        }
    }

    public void reloadUi() {
        for(Activity activity : activityMap.values()) {
            activity.destroy();
        }
        
        if(currentActivity != null) {
            startActivity(currentActivity.getIntent());
        } else if(!intentStack.empty()) {
            startActivity(intentStack.peek());
        }
    }

    public void setPopupPreferredPosition(I3dVec2 popupPreferredPosition) {
        mPopupPreferredPosition = popupPreferredPosition;
    }
}
