package com.spaceagencies.common.game;

import com.spaceagencies.server.GameServer;


public class Player extends GameEntity {

    private static final long serialVersionUID = -2593919021284765702L;

    private String login;

    private boolean human;

    private boolean local;

    private CardPile mDeck;
    
    private CardPile mDiscardPile;

    private Turn mTurn;

//	private Address address;
    
	public Player(Game game, long id, /*Address addr, */String login) {
	    super(game, id);
//		this.address = addr;
        this.login = login;
        
        mDeck = new NormalCardPile(game, GameServer.pickNewId());
        mDiscardPile = new NormalCardPile(game, GameServer.pickNewId());
        
        human = false;
	}
	
    public String getLogin() {
        return login;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public boolean isHuman() {
        return human;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
    
    public boolean isLocal() {
        return local;
    }

    public CardPile getDeck() {
        return mDeck;
    }
    
    public CardPile getDiscardPile() {
        return mDiscardPile;
    }

    public void setTurn(Turn turn) {
        mTurn = turn;
    }
    
    public Turn getTurn() {
        return mTurn;
    }

    public void setLogin(String name) {
        this.login = name;
    }
}
