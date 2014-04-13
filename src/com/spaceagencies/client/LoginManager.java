package com.spaceagencies.client;

import java.util.ArrayList;
import java.util.List;

import com.spaceagencies.common.game.Player;


public class LoginManager {
    
	public static Player localPlayer = null;
    public static List<Player> distantPlayers = new ArrayList<Player>();
    
    public static boolean  isLogged() {
        return localPlayer != null;
    }
    
    public static Player getLocalPlayer() {
        return localPlayer;
    }
}
