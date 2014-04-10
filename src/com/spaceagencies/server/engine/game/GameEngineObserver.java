package com.spaceagencies.server.engine.game;

import com.spaceagencies.common.game.Player;


public interface GameEngineObserver {
    void onPlayerConnected(Player player);

    void onSomeThingChanged();
}
