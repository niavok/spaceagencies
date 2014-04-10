package com.spaceagencies.client.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.spaceagencies.client.input.ControllerManager.ControllerEventObserver;
import com.spaceagencies.common.engine.Engine;
import com.spaceagencies.common.engine.Observable;
import com.spaceagencies.common.tools.Log;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.input.I3dMouseEvent.Action;
import com.spaceagencies.i3d.view.Point;
import com.spaceagencies.server.Time;
import com.spaceagencies.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent.KeyAction;

public class InputEngine implements Engine {

    private static final int CLICK_TIME = 500000000;
    private static final int DOUBLE_CLICK_TIME = 500000000;
    
    private boolean dragging;
    private long[] pressTime;
    private long[] clickTime;
    private int[] clickCount;
    private Point[] pressLocation;
    private ControllerManager[] controllers;
    // private String cheatString = "";
    
    public InputEngine() {
        dragging = false;
        pressTime = new long[10];
        clickTime = new long[10];
        pressLocation = new Point[10];
        clickCount = new int[10];
    }

    @Override
    public void init() {
        Mouse.setGrabbed(false);
    }

    @Override
    public void start() {
        try {
            Controllers.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
        Controllers.poll();
        
        controllers = new ControllerManager[Controllers.getControllerCount()];
        
        ControllerEventObserver observer = new ControllerEventObserver() {
            
            @Override
            public void onControllerEvent(V3DControllerEvent event) {
                notifyControllerEvent(event);
            }
        };
        
        for(int i = 0; i < Controllers.getControllerCount(); i++) {
            controllers[i] = new ControllerManager(Controllers.getController(i), observer);
            controllers[i].dump();
        }
        
    }

    @Override
    public void stop() {
    }
    
    @Override
    public void destroy() {
    }

    @Override
    public void tick(Timestamp time) {
        
        try { 

        while (Mouse.next()) {

            int dWheel = Mouse.getDWheel();
            
            if(dWheel > 0) {
                
                I3dMouseEvent mouseEvent = new I3dMouseEvent(Action.MOUSE_WHEEL_UP, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, 0);
                notifyMouseEvent(mouseEvent);
            } else if(dWheel < 0) {
                I3dMouseEvent mouseEvent = new I3dMouseEvent(Action.MOUSE_WHEEL_DOWN, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, 0);
                notifyMouseEvent(mouseEvent);
            } else if (Mouse.getEventButton() == -1) {
                if (dragging) {
                    // Drag
                    I3dMouseEvent mouseEvent = new I3dMouseEvent(Action.MOUSE_DRAGGED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, 0);
                    notifyMouseEvent(mouseEvent);
                } else {
                    // Move
                    I3dMouseEvent mouseEvent = new I3dMouseEvent(Action.MOUSE_MOVED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, 0);
                    notifyMouseEvent(mouseEvent);
                }

            } else {
                if (Mouse.getEventButtonState()) {
                    // Pressed
                    dragging = true;
                    pressTime[Mouse.getEventButton()] = Mouse.getEventNanoseconds();
                    pressLocation[Mouse.getEventButton()] = new Point(Mouse.getX(), Mouse.getY());
                    I3dMouseEvent mouseEvent = new I3dMouseEvent(Action.MOUSE_PRESSED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, 0);
                    notifyMouseEvent(mouseEvent);
                } else {
                    // Released
                    dragging = false;
                    I3dMouseEvent mouseEvent = new I3dMouseEvent(Action.MOUSE_RELEASED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, 0);
                    notifyMouseEvent(mouseEvent);
                    if( Mouse.getEventNanoseconds()  - pressTime[Mouse.getEventButton()] < CLICK_TIME && new Point(Mouse.getX(), Mouse.getY()).distanceTo(pressLocation[Mouse.getEventButton()])  < 30) {
                        
                        if( Mouse.getEventNanoseconds()  - clickTime[Mouse.getEventButton()] > DOUBLE_CLICK_TIME ) {
                            clickCount[Mouse.getEventButton()] = 0;
                            Log.trace("Too long click");
                        }
                        clickCount[Mouse.getEventButton()]++;
                        clickTime[Mouse.getEventButton()] = Mouse.getEventNanoseconds();
//                        Log.trace("click count set to "+clickCount[Mouse.getEventButton()]);
                        mouseEvent = new I3dMouseEvent(Action.MOUSE_CLICKED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1, clickCount[Mouse.getEventButton()]);
                        notifyMouseEvent(mouseEvent);
                    }
                }
            }

        }
        
        for(ControllerManager controller : controllers) {
            controller.update(Mouse.getEventX(), Mouse.getEventY());
        }
        
        while (Keyboard.next()) {
            Log.log("Keyboard.getEventKeyState() "+ Keyboard.getEventKeyState());
            if(interceptSpecialKeys(time)) {
                continue;
            }
            
            Log.log("Keyboard.getEventKeyState() 2"+ Keyboard.getEventKeyState());
            
            
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventCharacter() == 0) {
                    V3DKeyEvent keyEvent = new  V3DKeyEvent(KeyAction.KEY_PRESSED, Keyboard.getEventKey(), "", Mouse.getEventX(), Mouse.getEventY());
                    notifyKeyEvent(keyEvent);
                } else {
                    V3DKeyEvent keyEvent = new  V3DKeyEvent(KeyAction.KEY_PRESSED,Keyboard.getEventKey(), Character.toString(Keyboard.getEventCharacter()), Mouse.getEventX(), Mouse.getEventY());
                    notifyKeyEvent(keyEvent);
                }

            } else {
                if (Keyboard.getEventCharacter() == 0) {
                    V3DKeyEvent keyEvent = new  V3DKeyEvent(KeyAction.KEY_RELEASED, Keyboard.getEventKey(), "", Mouse.getEventX(), Mouse.getEventY());
                    notifyKeyEvent(keyEvent);
                } else {
                    V3DKeyEvent keyEvent = new  V3DKeyEvent(KeyAction.KEY_RELEASED,Keyboard.getEventKey(), Character.toString(Keyboard.getEventCharacter()), Mouse.getEventX(), Mouse.getEventY());
                    notifyKeyEvent(keyEvent);
                }
            }
        }
        
        } catch(IllegalStateException e) {
            System.err.println("IllegalStateException");
        }

    }

    private boolean interceptSpecialKeys(Timestamp time) {
        if (Keyboard.getEventKeyState()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_F4 && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                notifyQuitEvent();
                return true;
            }
            
//            if(Keyboard.getEventKey() == Keyboard.KEY_TAB && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
//                Game.getInstance().sendToAll(new MinimizeWindowEvent());
//                return true;
//            }
            
        }
        return false;
    }
//
//        @Override
//        public void visit(StartEngineEvent event) {
//            pause(false);
//        }
//
//        @Override
//        public void visit(PauseEngineEvent event) {
//            pause(true);
//        }
//        
//        
//        @Override
//        public void visit(KeyPressedEvent event) {
//            // Cheats
//            if(event.getKeyCode() == Keyboard.KEY_RETURN) {
//                if(cheatString.toLowerCase().equals("gold")) {
//                    LoginManager.localPlayer.giveMoney(10000);
//                    Log.log("Cheat - 10000 $");
//                }
//                if(cheatString.toLowerCase().equals("glittering prizes")) {
//                    LoginManager.localPlayer.giveMoney(500000);
//                    Log.log("Cheat - 500000 $");
//                }
////                if(cheatString.toLowerCase().equals("armor")) {
////                    Monolith monolith = null;
////                    for (CelestialObject object : Game.getInstance().getWorld().getCelestialsObjects()) {
////                        if (object instanceof Monolith) {
////                            monolith = (Monolith) object;
////                            break;
////                        }
////                    }
////                    if (monolith != null) {
////                        if(monolith.getPhysicalResistance() < 1) {
////                            Log.log("Cheat - Monolith invicibility activated");
////                            monolith.setPhysicalResistance(100);
////                            monolith.setHeatResistance(100);
////                        } else {
////                            Log.log("Cheat - Monolith invicibility desactivated");
////                            monolith.setPhysicalResistance(0.5);
////                            monolith.setHeatResistance(0);
////                        }
////                        
////                    }
////                    
////                }
//                if(cheatString.toLowerCase().equals("repair")) {
//                    Ship ship = LoginManager.getLocalPlayer().getShipList().get(0);
//                    for(Component component : ship.getComponents()) {
//                        component.setDurability(component.getDurabilityMax()) ; 
//                    }
//                }
//                if(cheatString.toLowerCase().equals("power+")) {
//                    Ship ship = LoginManager.getLocalPlayer().getShipList().get(0);
//                    for(Component component : ship.getComponents()) {
//                        
//                        List<LinearEngineCapacity> engines = component.getCapacitiesByClass(LinearEngineCapacity.class);
//                        for (LinearEngineCapacity engineCapacity : engines) {
//                            engineCapacity.theoricalMaxThrust *=2;
//                            engineCapacity.theoricalMinThrust *=2;
//                        }
//                    }
//                }
//                if(cheatString.toLowerCase().equals("looser")) {
//                    Game.getInstance().sendToAll(new GameOverEvent("Cheat"));
//                }
//                cheatString = "";
//            } else if(event.getCharacter() != null) {
//                cheatString += event.getCharacter();
//            }
//            
//        }
    
    // Observers
    private Observable<InputEngineObserver> mInputEngineObservable = new Observable<InputEngineObserver>();
    
    public Observable<InputEngineObserver> getInputEnginObservable() {
        return mInputEngineObservable;
    }
    
    private void notifyMouseEvent(I3dMouseEvent event) {
        for(InputEngineObserver observer : mInputEngineObservable.getObservers()) {
            observer.onMouseEvent(event);
        }
    }
    
    private void notifyKeyEvent(V3DKeyEvent event) {
        for(InputEngineObserver observer : mInputEngineObservable.getObservers()) {
            observer.onKeyEvent(event);
        }
    }
    
    private void notifyControllerEvent(V3DControllerEvent event) {
        for(InputEngineObserver observer : mInputEngineObservable.getObservers()) {
            observer.onControllerEvent(event);
        }
    }
    
    private void notifyQuitEvent() {
        for(InputEngineObserver observer : mInputEngineObservable.getObservers()) {
            observer.onQuitEvent();
        }
    }
}
