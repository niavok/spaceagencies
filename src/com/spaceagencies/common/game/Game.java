package com.spaceagencies.common.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spaceagencies.server.GameServer;

public class Game {
    private final List<Player> players = new ArrayList<Player>();
    
    private final List<CardPile> supply = new ArrayList<CardPile>();
    
    private final CardPile missions = new NormalCardPile(this, GameServer.pickNewId());
    
    private final CardPile visibleMissions = new NormalCardPile(this, GameServer.pickNewId());
    
    private final Map<Long, Player> playerIdMap = new HashMap<Long, Player>();
    
    private State state = State.WAITING_FOR_PLAYERS;
    
    public enum State {
        WAITING_FOR_PLAYERS,
        STARTED,
        FINISHED      
    }
    
    
    public void addPlayer(Player player) {
        players.add(player);
        playerIdMap.put(player.getId(), player);
    }
    
    public List<CardPile> getSupply() {
        return supply;
    }
    
    public CardPile getVisibleMissions() {
        return visibleMissions;
    }
    
    public CardPile getMissions() {
        return missions;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
}
