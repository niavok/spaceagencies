package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.server.ServerBroadcaster;
import com.spaceagencies.server.Time.Timestamp;

public class LobbyActivity extends Activity {

    //private Triangle mobileLogoPart;
    private ServerBroadcaster mServerBroadcaster;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/mainmenu");
    }

    @Override
    public void onResume() {
        mServerBroadcaster = new ServerBroadcaster();
        mServerBroadcaster.start();
    }

    @Override
    public void onPause() {
        mServerBroadcaster.stop();
    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Timestamp time) {
    }

}
