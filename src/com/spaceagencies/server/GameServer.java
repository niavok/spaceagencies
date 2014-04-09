package com.spaceagencies.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spaceagencies.common.engine.Engine;
import com.spaceagencies.common.game.Player;
import com.spaceagencies.server.engine.game.GameEngine;

public class GameServer {
    /*
     * private ServerGameEngine gameEngine; private ServerNetworkEngine
     * networkEngine; private PhysicEngine physicEngine;
     */
    // private ParameterAnalyser parameterAnalyser;

    List<Engine> engineList = new ArrayList<Engine>();

    // private boolean stillRunning;
    // private CommandManager commandManager;
    // private DebugGraphicEngine debugGraphicEngine;
//    private World world;

    private List<Player> playerList = new ArrayList<Player>();
    private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();
    

    public static GameServer instance = null;

    private static long nextId = 0;

//    private PhysicEngine physicEngine;

    private static GameEngine worldEngine;

    public static synchronized long pickNewId() {
        return nextId++;
    }

    public static GameServer getInstance() {
        return instance;
    }

    public GameServer(ParameterAnalyser parameterAnalyser) {
        // this.parameterAnalyser = parameterAnalyser;
        instance = this;
        // stillRunning = true;

//        world = new World();

//        engineList.add(new ServerSystemGameEngine());
//        physicEngine = new PhysicEngine();
//        engineList.add(physicEngine);
//        engineList.add(new ServerNetworkEngine());
        //engineList.add(new DebugGraphicEngine());

        /*
         * gameEngine = new ServerGameEngine(); physicEngine = new
         * PhysicEngine(); networkEngine = new ServerNetworkEngine();
         */
        // debugGraphicEngine = new DebugGraphicEngine();

        // commandManager = new CommandManager();

    }

    public void run() {
        // boolean currentStillRunning = stillRunning;

        // gameEngine.start();
        // physicEngine.start();
        // networkEngine.start();
        // debugGraphicEngine.start();
        for (Engine engine : engineList) {
            engine.start();
        }

        // currentStillRunning = stillRunning;
        // std::string i;
        // std::string o;

        // Wait engines started
//        boolean waitStart = true;
//        while (waitStart) {
//            waitStart = false;
//            for (Engine engine : engineList) {
//                if (!engine.isRunning()) {
//                    waitStart = true;
//                    break;
//                }
//            }
//            Duration.HUNDRED_MILLISECONDE.sleep();
//        }

        // while ((Engine.getRunningEngineCount()) < 3) {
        // new Duration(100000000).sleep();
        // }

//        sendToAll(new StartEngineEvent());
        /*
         * AddShipEvent addShipEvent = new AddShipEvent();
         * addShipEvent.setType(AddShipEvent.Type.SIMPLE);
         * sendToAll(addShipEvent);
         */

        System.out.println("Space agencies Server - v0.1a");

        /*
         * Reader reader = new InputStreamReader(System.in); BufferedReader
         * input = new BufferedReader(reader); try { while (true) {
         * System.out.print("> "); String command = input.readLine(); String
         * output = commandManager.execute(command); if (output != null) { if
         * (!output.isEmpty()) { System.out.println(output); } } else {
         * System.out.println("Game : Exiting..."); break; } } } catch
         * (IOException e) { // Todo handle exception }
         */

        boolean waitStop = true;

//        while (waitStop) {
//            waitStop = false;
//            for (Engine<GameEvent> engine : engineList) {
//                if (!engine.isStopped()) {
//                    waitStop = true;
//                    break;
//                }
//            }
//            Duration.HUNDRED_MILLISECONDE.sleep();
//        }
        System.out.println("Game Server: Stopped");

    }

    public void stop() {
//        sendToAll(new QuitGameEvent());
    }

//    public void sendToAll(GameEvent e) {
//        for (Engine<GameEvent> engine : engineList) {
//            engine.pushEvent(e);
//        }
//        /*
//         * gameEngine.pushEvent(e); physicEngine.pushEvent(e);
//         * networkEngine.pushEvent(e);
//         */
//        // debugGraphicEngine.pushEvent(e);
//    }

    /*
     * public ServerGameEngine getGameEngine() { return gameEngine; } public
     * PhysicEngine getPhysicEngine() { return physicEngine; } public
     * ServerNetworkEngine GetNetworkEngine() { return networkEngine; }
     */

    public List<Player> getPlayerList() {
        return playerList;
    }

//    public World getWorld() {
//        return world;
//    }

    
//    public PhysicEngine getPhysicEngine() {
//        return physicEngine;
//    }

    public Map<Integer, Player> getPlayerMap() {
        return playerMap;
    }
//
//    @Override
//    public void gameOver() {
//        // TODO Auto-generated method stub
//        
//    }

    public static void setWorldEngine(GameEngine worldEngine) {
        GameServer.worldEngine = worldEngine;
    }

    public static GameEngine getGameEngine() {
        return worldEngine;
    }
    
}
