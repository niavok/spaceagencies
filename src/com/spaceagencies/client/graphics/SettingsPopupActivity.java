package com.spaceagencies.client.graphics;

import com.spaceagencies.client.GameClient;
import com.spaceagencies.client.graphics.ether.activities.SettingsActivity;
import com.spaceagencies.client.graphics.ether.activities.StatusActivity.StatusActivityController;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.Intent;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.input.I3dMouseEvent.Action;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.Button;
import com.spaceagencies.i3d.view.View;
import com.spaceagencies.i3d.view.View.OnClickListener;
import com.spaceagencies.i3d.view.View.OnMouseEventListener;

public class SettingsPopupActivity extends Activity {


    private StatusActivityController mListener;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/status_settings_popup");
        setStackable(false);
        
        mListener = (StatusActivityController) bundle.getObject();
        
        Button quitButton = (Button) findViewById("quitButton@layout/status_settings_popup");
        Button settingsButton = (Button) findViewById("settingsButton@layout/status_settings_popup");
        Button saveButton = (Button) findViewById("saveButton@layout/status_settings_popup");
        
        
        quitButton.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
                if(mouseEvent.getAction() == Action.MOUSE_PRESSED && mouseEvent.getButton() == 1) {
                mListener.onQuit();
                return true;
                }
                return false;
            }
        });
        
        settingsButton.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
                if(mouseEvent.getAction() == Action.MOUSE_PRESSED && mouseEvent.getButton() == 1) {
                    mListener.getControlledSurface().startActivity(new Intent(SettingsActivity.class));
                    // Do not return true to exit the popup
                }
                return false;
            }
        });
        
        saveButton.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
                if(mouseEvent.getAction() == Action.MOUSE_PRESSED && mouseEvent.getButton() == 1) {
                    GameClient.getInstance().getGameManager().save();
                    // Do not return true to exit the popup
                }
                return false;
            }
        });
        
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        
    }

}
