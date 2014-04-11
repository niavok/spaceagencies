package com.spaceagencies.common.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final List<Player> players = new ArrayList<Player>();
    
    private final List<CardPile> supply = new ArrayList<CardPile>();
    
    private final CardPile missions = new NormalCardPile();
    
    private final CardPile visibleMissions = new NormalCardPile();
    
    private final Map<Long, Player> playerIdMap = new HashMap<Long, Player>();
    
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
}
