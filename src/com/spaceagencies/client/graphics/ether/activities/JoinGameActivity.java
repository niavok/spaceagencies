package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.client.ServerDetector;
import com.spaceagencies.client.ServerDetector.OnServerDetectedListener;
import com.spaceagencies.common.engine.EngineManager;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.Intent;
import com.spaceagencies.i3d.Message;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.server.engine.game.ServerGameEngine;

public class JoinGameActivity extends Activity {

    private ServerGameEngine worldEngine = null;
    
    private static final int SERVER_FOUND_WHAT = 1;
    private EngineManager engineManager;

    private ServerDetector mServerDetector;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        setStackable(false);
    }

    @Override
    public void onResume() {
        mServerDetector = new ServerDetector();
        mServerDetector.setOnServerDetectedListener(new OnServerDetectedListener() {

            @Override
            public void onServerDetected() {
                mServerDetector.stop();
                getHandler().obtainMessage(SERVER_FOUND_WHAT).send();                
            }
        });
        mServerDetector.start();

    }

    @Override
    public void onPause() {
        mServerDetector.stop();
    }
    
    @Override
    public void onDestroy() {
        worldEngine.getWorldEnginObservable().unregister(this);
    }

    @Override
    protected void onMessage(Message message) {
        switch(message.what) {
            case SERVER_FOUND_WHAT:
                startActivity(new Intent(BoardActivity.class));
                break;
        }
    }
}
