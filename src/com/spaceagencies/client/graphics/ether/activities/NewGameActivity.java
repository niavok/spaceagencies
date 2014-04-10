package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.client.GameClient;
import com.spaceagencies.client.LoginManager;
import com.spaceagencies.common.engine.EngineManager;
import com.spaceagencies.common.game.Player;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.Intent;
import com.spaceagencies.i3d.Message;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.server.engine.game.GameEngine;
import com.spaceagencies.server.engine.game.GameEngineObserver;
import com.spaceagencies.server.game.MetaGame;

public class NewGameActivity extends Activity {

    private GameEngine worldEngine = null;
    
    private static final int NEW_GAME_CREATED_WHAT = 1;
    private EngineManager engineManager;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        setStackable(false);
        
        engineManager = GameClient.getInstance().getEngineManager();
        
        MetaGame game = GameClient.getInstance().getGameManager().createGame();
        
        GameClient.getInstance().getGameManager().setActiveGame(game);
        
        game.load();
        
        worldEngine = new GameEngine(game.getGame());
        engineManager.add(worldEngine);
        worldEngine.start();
        
        
        worldEngine.getWorldEnginObservable().register(this, new GameEngineObserver() {
            
            @Override
            public void onPlayerConnected(Player player) {
                LoginManager.localPlayer = player;
                getHandler().obtainMessage(NEW_GAME_CREATED_WHAT).send();
            }
            
            @Override
            public void onSomeThingChanged() {
            }
            
        });
        
        worldEngine.connectPlayerAction("fredb219", true);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }
    
    @Override
    public void onDestroy() {
        worldEngine.getWorldEnginObservable().unregister(this);
    }

    @Override
    protected void onMessage(Message message) {
        switch(message.what) {
            case NEW_GAME_CREATED_WHAT:
                startActivity(new Intent(BoardActivity.class));
                break;
        }
    }
}
