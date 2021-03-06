package com.spaceagencies.server.engine.game;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.spaceagencies.common.engine.Engine;
import com.spaceagencies.common.engine.Observable;
import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.Card.Type;
import com.spaceagencies.common.game.CardPile;
import com.spaceagencies.common.game.Game;
import com.spaceagencies.common.game.InfinitCardPile;
import com.spaceagencies.common.game.NormalCardPile;
import com.spaceagencies.common.game.InfinitCardPile.CardFactory;
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
        List<Card> cards = Card.getCards();
        Collections.shuffle(cards);
        cards.add(Card.getCuivre(mGame));
        cards.add(Card.getArgent(mGame));
        cards.add(Card.getOr(mGame));
        
        int count = 0;
        for (final Card card : cards) {
            if ((card.getType() & Card.Type.TECHNOLOGIES.getFlag()) != 0) {
                if (count <= 10) {
                    CardPile pile = new NormalCardPile(mGame, GameServer.pickNewId());
                    for(int i = 0; i < 10 ; i++) {
                        pile.addTop(card.duplicate());
                    }
                    mGame.getSupply().add(pile);
                    count += 1;
                }
            } else if (card.getType()  == Card.Type.RESSOURCES.getFlag()) {
                InfinitCardPile pile = new InfinitCardPile(mGame, GameServer.pickNewId(), new CardFactory() {
                    @Override
                    public Card createCard() {
                        return card.duplicate();
                    }
                });
                mGame.getResources().add(pile);
            } else {
                mGame.getMissions().addTop(card.duplicate());
            }
        }
        
        mGame.getMissions().shuffle();
        refillObjectives();
    }

    private void refillObjectives() {
        
        while(mGame.getMissions().getNbCards() != 0 && mGame.getVisibleMissions().getNbCards() < 6) {
            Card card = mGame.getMissions().takeTop();
            mGame.getVisibleMissions().addBottom(card);
        }
        
        mGame.getVisibleMissions().sort(new Comparator<Card>() {

            @Override
            public int compare(Card o1, Card o2) {
                
                return o2.getDate() - o1.getDate();
        }});
        
        
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
        deck.addBottom(Card.pullCardByName("flirtwspace"));
        deck.addBottom(Card.pullCardByName("soundbarrier"));
        deck.addBottom(Card.pullCardByName("NACA"));
        for(int i = 0; i < 7 ;i++) {
            deck.addBottom(Card.getCuivre(mGame));
        }
        
        deck.shuffle();

        // Ready to play
        newTurn(newPlayer);
        newPlayer.getTurn().doBeginTurn();
        checkAutoSkip(newPlayer.getTurn());
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
        checkAutoSkip(player.getTurn());
    }


    private void newTurn(Player player) {
        Turn turn = new Turn(mGame, GameServer.pickNewId(), player, player.getDeck());
        player.setTurn(turn);
        
        notifySomethingChanged();
    }


    public void skipActions(Turn turn) {
        if(turn.getTurnState() != TurnState.ACTION_PHASE) {
            Log.warn("Try to skip action in "+ turn.getTurnState() + " state" );
            return;
        }
        turn.doSkipActions();
        
        checkAutoSkip(turn);
        notifySomethingChanged();
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
        
        notifySomethingChanged();
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
        
        notifySomethingChanged();
    }


    public void buyCard(Turn turn, CardPile cardPile) {
        if(turn.getTurnState() != TurnState.BUY_PHASE) {
            Log.warn("Try to buy card in "+ turn.getTurnState() + " state" );
            return;
        }
        
        if(cardPile.getNbCards() == 0) {
            Log.warn("Try to buy card in an empty pile" );
            return;
        }
        
        if(cardPile.peekTop().getCost() > turn.getMoneyCount()) {
            Log.warn("Try to buy a too expensive card" );
            return;
        }
        
        turn.doBuyCard(cardPile);
        
        checkAutoSkip(turn);
        
        notifySomethingChanged();
    }
    
    
    public Game getGame() {
        return mGame;
    }


    public void buyMissionCard(Turn turn, Card card) {
        if(turn.getTurnState() != TurnState.BUY_PHASE) {
            Log.warn("Try to buy mission card in "+ turn.getTurnState() + " state" );
            return;
        }
        
        if(card.getCost() > turn.getMoneyCount()) {
            Log.warn("Try to buy a too expensive card" );
            return;
        }
        
        turn.doBuyMisionCard(card);
        
        //Remove higther mission
        Card bottom = mGame.getVisibleMissions().takeTop();
        if (bottom != null) {
            mGame.getMissions().addBottom(bottom);
        }
        
        refillObjectives();
        
        checkAutoSkip(turn);
        
        notifySomethingChanged();
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
    
    private void notifySomethingChanged() {
        for(GameEngineObserver observer : mWorldEngineObservable.getObservers()) {
            observer.onSomeThingChanged();
        }
    }
}
