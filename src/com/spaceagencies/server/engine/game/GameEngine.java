package com.spaceagencies.server.engine.game;

import java.util.Comparator;

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
        
        // Init mission
        for(int i = 1; i < 51 ; i++) {
            mGame.getMissions().addTop(Card.getTestRandomMissionCard(i));
        }
        mGame.getMissions().shuffle();
        
        refillObjectives();
        
        
        
        
        // Add Cuivre pile
        InfinitCardPile cuivrePile = new InfinitCardPile(new CardFactory() {
            
            @Override
            public Card createCard() {
                return Card.getTestCardCuivre();
            }
        });
        mGame.getSupply().add(cuivrePile);

        // Add Argent pile
        InfinitCardPile argentPile = new InfinitCardPile(new CardFactory() {
            
            @Override
            public Card createCard() {
                return Card.getTestCardArgent();
            }
        });
        mGame.getSupply().add(argentPile);
        
        // Add Or pile
        InfinitCardPile orPile = new InfinitCardPile(new CardFactory() {
            
            @Override
            public Card createCard() {
                return Card.getTestCardOr();
            }
        });
        mGame.getSupply().add(orPile);
        
        // Add village pile
        CardPile villagePile = new NormalCardPile();
        for(int i = 0; i < 12 ; i++) {
            villagePile.addTop(Card.getTestCardVillage());
        }
        mGame.getSupply().add(villagePile);
        
        // Add bucheron pile
        CardPile bucheronPile = new NormalCardPile();
        for(int i = 0; i < 12 ; i++) {
            bucheronPile.addTop(Card.getTestCardBucheron());
        }
        mGame.getSupply().add(bucheronPile);
        
        // Add forgeron pile
        CardPile forgeronPile = new NormalCardPile();
        for(int i = 0; i < 12 ; i++) {
            forgeronPile.addTop(Card.getTestCardForgeron());
        }
        mGame.getSupply().add(forgeronPile);
        
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
        for(int i = 0; i < 3 ;i++) {
            deck.addBottom(Card.getTestCardDomaine());
        }
        for(int i = 0; i < 7 ;i++) {
            deck.addBottom(Card.getTestCardCuivre());
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
        Turn turn = new Turn(mGame, player, player.getDeck());
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
        mGame.getMissions().addBottom(bottom);
        
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
