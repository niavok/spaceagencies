package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.client.GameClient;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.Intent;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.Button;
import com.spaceagencies.i3d.view.View;
import com.spaceagencies.i3d.view.View.OnClickListener;
import com.spaceagencies.server.Duration;
import com.spaceagencies.server.GameServer;
import com.spaceagencies.server.Time;
import com.spaceagencies.server.Time.Timestamp;
import com.spaceagencies.server.game.MetaGame;

public class MainMenuActivity extends Activity {

    //private Triangle mobileLogoPart;
    private Measure animationMesure;
    private Time startTime;
    private Button newGameMenu;
    private Button continueGameButton;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/mainmenu");

      //  mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/logo");
        animationMesure = new Measure(0, true, Axis.VERTICAL);

        newGameMenu = (Button) findViewById("newGameButton@layout/mainmenu");
//        continueGameButton = (Button) findViewById("continueGameButton@layout/mainmenu");
        
        newGameMenu.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                startActivity(new Intent(NewGameActivity.class));
            }
        });
        
//        continueGameButton.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(I3dMouseEvent mouseEvent, View view) {
//                //startActivity(new Intent(ContinueGameActivity.class));
//            }
//        });
        
    }

    @Override
    public void onResume() {
        startTime = Time.now(false);

        // Pause the game in the main menu
        Time.pauseGame(startTime);
    }

    @Override
    public void onPause() {

    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Timestamp time) {
        
//        MetaGame activeGame = GameClient.getInstance().getGameManager().getPreviousGame();
//        if(activeGame == null) {
//            continueGameButton.setEnabled(false);
//        } else {
//            continueGameButton.setEnabled(true);
//        }
    }

}
