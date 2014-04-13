package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.client.graphics.ether.activities.StatusActivity.StatusActivityController;
import com.spaceagencies.common.tools.Log;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.I3dContext;
import com.spaceagencies.i3d.Intent;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.LayoutParams;
import com.spaceagencies.i3d.view.Triangle;
import com.spaceagencies.server.Duration;
import com.spaceagencies.server.Time;
import com.spaceagencies.server.Time.Timestamp;

public class MainActivity extends Activity {

    private Triangle mobileLogoPart;
    private Measure animationMesure;
    private Time startTime;
    private boolean axisX;
    private float lastOffset;
    private float lastLastOffset;
    private MainActivityListener mListener;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        setStackable(false);
        //Log.trace("onCreate");
        mListener = (MainActivityListener) bundle.getObject();
        
//        mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/logo");
        animationMesure = new Measure(0, true, Axis.VERTICAL);
        I3dContext.getInstance().preload();
    }

    @Override
    public void onResume() {
        startTime = Time.now(false);
        //Log.trace("onResume "+startTime.getSeconds());
        axisX = true;
        lastOffset = 0;
        lastLastOffset = 0;
    }

    @Override
    public void onPause() {
        //Log.trace("onPause");
        
    }
    
    @Override
    public void onDestroy() {
        Log.trace("onDestroy");
    }

    @Override
    protected void onUpdate(Timestamp time) {
        //Log.trace("onUpdate "+absTime.getSeconds()+ " "+ Time.now(false).getSeconds()+ " "+gameTime.getSeconds());
        Duration duration = startTime.durationTo(time.getTime());
        //Log.trace("onUpdate duration "  +duration.getSeconds());
        // Log.trace("Update animatation after "+duration.getMilliseconds()+" ms ("+(1f/duration.getSeconds())+"fps)");
        //startTime = absTime;

//        LayoutParams layout = mobileLogoPart.getLayoutParams();
        animationMesure.setValue((float) (25f / 2.2f * (1 + Math.sin(time.getTime().getSeconds() * 4))));

//        float offset = layout.computeMesure(animationMesure);

        //Log.trace("Offset: "+offset);
        if (axisX) {
            //layout.setOffsetX(offset);
        } else {
            //layout.setOffsetY(offset);
        }

//        if ((lastOffset - lastLastOffset) < 0 && (offset - lastOffset > 0)) {
            // Cross a minimal
//            axisX = !axisX;
//        }

        lastLastOffset = lastOffset;
//        lastOffset = offset;

        // layout.setOffsetY(offset);
        if(duration.getSeconds() > 1) {
            //startActivity(new Intent(MainMenuActivity.class));
        }
        if(getContext().isPreloaded())  {
            startActivity(new Intent(MainMenuActivity.class));
            mListener.onDone();
            getContext().getCanvas().maximise();
        }
    }
    
    public interface MainActivityListener {
        void onDone();
    }

}
