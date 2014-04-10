package com.spaceagencies.server.engine.game;

import com.spaceagencies.common.engine.Engine;
import com.spaceagencies.common.engine.Observable;
import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardPile;
import com.spaceagencies.common.game.Game;
import com.spaceagencies.common.game.Player;
import com.spaceagencies.common.game.Turn;
import com.spaceagencies.common.game.Card.Type;
import com.spaceagencies.common.game.Turn.TurnState;
import com.spaceagencies.common.game.features.FeatureMoreActions;
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
            deck.addBottom(Card.getTestCardVillage());
        }
        for(int i = 0; i < 7 ;i++) {
            deck.addBottom(Card.getTestCardCuivre());
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
        
        checkAutoSkip(turn);
    }


    private void checkAutoSkip(Turn turn) {
      //Check auto end turn
        switch (turn.getTurnState()) {
            case ACTION_PHASE:
            {
                boolean possibleAction = false;
                
                if(turn.getActionCount() > 0) {
                    for(Card handCard : turn.getHand().getCards()) {
                        if((handCard.getType() & Type.TECHNOLOGIES.getFlag()) != 0) {
                            possibleAction = true;
                            break;
                        }
                    }
                }
                
                if(!possibleAction) {
                    skipActions(turn);
                }
            }
                break;
            case BUY_PHASE:
            {
                if(turn.getBuyCount() == 0) {
                    endTurn(turn);
                }
            }
                break;

            default:
                break;
        }
    }

    public void playActionCard(Turn turn, Card card) {
        if(turn.getTurnState() != TurnState.ACTION_PHASE) {
            Log.warn("Try to play action card in "+ turn.getTurnState() + " state" );
            return;
        }
        
        if((card.getType() & Type.TECHNOLOGIES.getFlag()) == 0) {
            Log.warn("Try to play a not action card" );
            return;
        }
        
        turn.doPlayCard(card);
        
        checkAutoSkip(turn);
    }
    
    public void playRessourceCard(Turn turn, Card card) {
        if(turn.getTurnState() != TurnState.BUY_PHASE) {
            Log.warn("Try to play ressource card in "+ turn.getTurnState() + " state" );
            return;
        }
        
        if((card.getType() & Type.RESSOURCES.getFlag()) == 0) {
            Log.warn("Try to play a not ressource card" );
            return;
        }
        
        turn.doPlayCard(card);
        
        checkAutoSkip(turn);
    }

}
