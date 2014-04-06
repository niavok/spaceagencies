package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.client.graphics.SettingsPopupActivity;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.I3dVec2;
import com.spaceagencies.i3d.Intent;
import com.spaceagencies.i3d.Surface;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.Button;
import com.spaceagencies.i3d.view.View;
import com.spaceagencies.i3d.view.View.OnClickListener;

public class StatusActivity extends Activity {


    private Button settingsButton;
    private StatusActivityController mListener;

    @Override
    public void onCreate(final Bundle bundle) {
        setContentView("main@layout/status");
        setStackable(false);
        
        mListener = (StatusActivityController) bundle.getObject();
        
        settingsButton = (Button) findViewById("settingsButton@layout/status");
       
        settingsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                getContext().setPopUpActivity(new Intent(SettingsPopupActivity.class, bundle), new I3dVec2(mouseEvent.getRootEvent().getX(), 25));
            }
        });
        
    }
    
    public interface StatusActivityController {
        
        Surface getControlledSurface();
        
        void onQuit();
    }

    @Override
    protected void onResume() {
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onDestroy() {
    }

}
