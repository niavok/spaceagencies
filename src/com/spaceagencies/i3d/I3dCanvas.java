package com.spaceagencies.i3d;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class I3dCanvas {

    private I3dContext context;
    private int width;
    private int height;
    private final String title;
    private JFrame frame;
    private boolean pauseDisplay = false;
    
    private boolean polygonOffset = false;
    private String mIconPath;

    public I3dCanvas(final I3dContext context,String title, int width, int height, String iconPath) {
        this.context = context;
        this.title = title;
        this.width = width;
        this.height = height;
        mIconPath = iconPath;
    }
    
    public void init() {
        try{
            
            frame = new JFrame(title);
            Image image = new ImageIcon(mIconPath).getImage();
            frame.setIconImage(image);
            frame.setTitle(title);
            //frame.setSize(width,height);
            //frame.setUndecorated(true);  //here
            //frame.setAlwaysOnTop(true);
            
            frame.setLocation(0, 0);
            Canvas canvas = new Canvas();
            canvas.setMinimumSize(new Dimension(width, height));
            canvas.setPreferredSize(new Dimension(width, height));
            frame.add(canvas);
            frame.pack();
            frame.addWindowListener(generateWindowListener());
            
            frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
                
                @Override
                public void ancestorMoved(HierarchyEvent e) {
                }
                @Override
                public void ancestorResized(HierarchyEvent e) {
                    width = frame.getContentPane().getWidth();
                    height = frame.getContentPane().getHeight();
                    reshape(0,0,width, height);
                }          
            });
            
            Display.setDisplayMode(new DisplayMode(width, height));
            //Display.setFullscreen(true);
            Display.setVSyncEnabled(false);
            Display.setTitle(title);
            Display.setParent(canvas);
            Display.create(new PixelFormat(8, 24, 0, 4));
            canvas.requestFocus();
        }catch(Exception e){
            System.out.println("Error setting up display" + e);
            System.exit(0);
        }

        // Enable z- (depth) buffer for hidden surface removal.
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);

        if (polygonOffset) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, 1.0f);
        }
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glClearDepth(1.0); // Enables Clearing Of The Depth Buffer
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);

        // Enable smooth shading.
        GL11.glShadeModel(GL11.GL_SMOOTH);

        // Define "clear" color.
        GL11.glClearColor(0f, 0f, 0.4f, 0f);

        // We want a nice perspective.
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glFlush();

        TextureManager.clearCache();
        //initied = true;
        
//        new LWJGLBinding();
        
    }
    
    private void reshape(int x, int y, int width, int height) {
        for (Surface surface : context.getSurfaceList()) {
            surface.configure(width, height);
        }
    }
    
    
    private WindowListener generateWindowListener() {
        return new WindowListener() {
            
            @Override
            public void windowOpened(WindowEvent e) {
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
               pauseDisplay = true;
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
                pauseDisplay = false;
            }
            
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO put this out of here
                context.notifyQuit();
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
                
            }
            
            @Override
            public void windowActivated(WindowEvent e) {
            }
        };
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public boolean draw(Graphics g) {
        Color.resetSeed();
        
        if(pauseDisplay || Display.isCloseRequested()) {
            return false;
        }
        
        display(g);
        Display.update();
        return true;
    }

    public void display(Graphics g) {
        GL11.glClear(GL11.GL_ACCUM_BUFFER_BIT);

        for (Surface surface: context.getSurfaceList()) {
            surface.draw(g);
        }
        GL11.glFlush();
    }

    public void show() {
        frame.setVisible(true);
    }

    public void destroy() {
        Display.destroy();
        frame.dispose();
    }
    
    public void maximise() {
        frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }
    
}
