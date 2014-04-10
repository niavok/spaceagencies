package com.spaceagencies.server.engine.game;

import com.spaceagencies.common.engine.Engine;
import com.spaceagencies.common.engine.Observable;
import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardPile;
import com.spaceagencies.common.game.FeatureMoreActions;
import com.spaceagencies.common.game.Game;
import com.spaceagencies.common.game.Player;
import com.spaceagencies.common.game.Turn;
import com.spaceagencies.common.game.Turn.TurnState;
import com.spaceagencies.common.tools.Log;
import com.spaceagencies.server.GameServer;
import com.spaceagencies.server.Time.Timestamp;

public class GameEngine implements Engine {

    private Game mGame;

    public GameEngine(Game game) {
        this.mGame = game;
        GameServer.setWorldEngine(this);
    }

    
    @Override
    public void init() {
    }

    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
    }
    
    @Override
    public void destroy() 
    {
    }
    
    @Override
    public void tick(Timestamp time) {
    }

    public void connectPlayerAction(String playerLogin, boolean isLocal) {
        Player newPlayer = new Player(mGame, GameServer.pickNewId(), playerLogin);
        newPlayer.setHuman(true);
        newPlayer.setLocal(isLocal);
        
        mGame.addPlayer(newPlayer);
        notifyPlayerConnected(newPlayer);
        
        
        
        
        CardPile deck = newPlayer.getDeck();
        for(int i = 0; i < 3 ;i++) {
            deck.addBottom(Card.getTestCard1());
        }
        for(int i = 0; i < 7 ;i++) {
            deck.addBottom(Card.getTestCard2());
        }
        
        deck.shuffle();

        // Ready to play
        newTurn(newPlayer);
        newPlayer.getTurn().doBeginTurn();
    }
    
    
	// Observers
    private Observable<GameEngineObserver> mWorldEngineObservable = new Observable<GameEngineObserver>();
    
    public Observable<GameEngineObserver> getWorldEnginObservable() {
        return mWorldEngineObservable;
    }
    
    private void notifyPlayerConnected(Player player) {
        for(GameEngineObserver observer : mWorldEngineObservable.getObservers()) {
            observer.onPlayerConnected(player);
        }
    }


    public void endTurn(Turn turn) {
        if(turn.getTurnState() != TurnState.BUY_PHASE) {
            Log.warn("Try to end turn in "+ turn.getTurnState() + " state" );
            return;
        }
        turn.doEnd();
        Player player = turn.getPlayer();
        newTurn(player);
        
        // One player : begin new turn
        player.getTurn().doBeginTurn();
    }


    private void newTurn(Player player) {
        Turn turn = new Turn(player, player.getDeck());
        player.setTurn(turn);
    }


    public void skipActions(Turn turn) {
        if(turn.getTurnState() != TurnState.ACTION_PHASE) {
            Log.warn("Try to skip action in "+ turn.getTurnState() + " state" );
            return;
        }
        turn.doSkipActions();
    }

}
