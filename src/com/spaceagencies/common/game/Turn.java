package com.spaceagencies.common.game;

import com.spaceagencies.server.GameServer;

public class Turn extends GameEntity {
    private static final long serialVersionUID = 4334603045240900926L;
    private Player player;
    private int actionCount = 1;
    private int buyCount = 1;
    private int moneyCount = 0;
    private CardPile mHand;
    private CardPile mPlayedCards;
    private TurnState mTurnState;
    private Game mGame; 
    
    public enum TurnState {
        WAITING_BEGIN,
        ACTION_PHASE,
        PROCESSING_ACTION,
        BUY_PHASE,
        END_PHASE,
    }
 
    public Turn(Game game, long id, Player player, CardPile deck) {
        super(game, id);
        mGame = game;
        mHand = new NormalCardPile(game, GameServer.pickNewId());
        mPlayedCards = new NormalCardPile(game, GameServer.pickNewId());
        this.player = player;
        mTurnState = TurnState.WAITING_BEGIN;
        
        for (int i = 0; i < 5; i++) {
            Card card = draw();
            if(card !=null) {
                mHand.addTop(card);
            }
        }
    }

    public Card draw() {
        
        if(player.getDeck().getNbCards() == 0) {
            // Discard pile as deck
            while(player.getDiscardPile().getNbCards() > 0) {
                player.getDeck().addTop(player.getDiscardPile().takeTop());    
            }
            player.getDeck().shuffle();
        }
        
        if(player.getDeck().getNbCards() == 0) {
            return null;
        } else {
            return player.getDeck().takeTop();    
        }
    }

    public final void addActions(int nb) {
        actionCount += nb;
    }
    
    public final void addMoney(int nb) {
        moneyCount += nb;
    }
    
    public void addBuy(int nb) {
        buyCount += nb;
    }
    
    public final Player getPlayer() {
        return player;
    }

    public final int getActionCount() {
        return actionCount;
    }

    public final int getBuyCount() {
        return buyCount;
    }

    public int getMoneyCount() {
        return moneyCount;
    }
    
    public final CardPile getHand() {
        return mHand;
    }
    
    public CardPile getPlayedCards() {
        return mPlayedCards;
    }
    
    public void doEnd() {
        
        // Discard hand
        while(mHand.getNbCards() > 0) {
            player.getDiscardPile().addTop(mHand.takeTop());    
        }
        // Discard played cards
        while(mPlayedCards.getNbCards() > 0) {
            player.getDiscardPile().addTop(mPlayedCards.takeTop());    
        }
        
        mTurnState = TurnState.WAITING_BEGIN;
    }
    
    public void doBeginTurn() {
        mTurnState = TurnState.ACTION_PHASE;
    }
    
    public TurnState getTurnState() {
        return mTurnState;
    }

    public void doSkipActions() {
        actionCount = 0;
        mTurnState = TurnState.BUY_PHASE;
    }

    public void doPlayCard(Card card) {
        mTurnState = TurnState.PROCESSING_ACTION;
        
        mHand.remove(card);
        actionCount -= 1;
        for(CardFeature feature : card.getFeaturesList()) {
            feature.resolve(this);
        }
        
        mPlayedCards.addBottom(card);
        
        mTurnState = TurnState.ACTION_PHASE;
    }

    public void doBuyCard(CardPile cardPile) {
        
        Card card = cardPile.takeTop();
        moneyCount -= card.getCost();
        buyCount -= 1;
        player.getDiscardPile().addTop(card);
    }

    public void doBuyMisionCard(Card card) {
        mGame.getVisibleMissions().remove(card);
        moneyCount -= card.getCost();
        buyCount -= 1;
        player.getDiscardPile().addTop(card);
    }

}
