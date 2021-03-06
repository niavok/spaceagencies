package com.spaceagencies.common.game;

import java.util.Comparator;
import java.util.List;

public class InfinitCardPile extends GameEntity implements CardPile {
    
    private static final long serialVersionUID = 5305119804635272517L;
    private Card card;
    private CardFactory mCardFactory;

    public InfinitCardPile(Game game, long id, CardFactory cardFactory) {
    	super(game, id);
        mCardFactory = cardFactory;
        this.card = cardFactory.createCard();
    }

    @Override
    public void addTop(Card c) {
    }

    @Override
    public void addBottom(Card c) {
    }

    @Override
    public void shuffle() {
    }

    @Override
    public Card peekTop() {
        return this.card;
    }

    @Override
    public Card peekBottom() {
        return this.card;
    }

    @Override
    public Card takeTop() {
        Card returnCard = card;
        card = mCardFactory.createCard();
        return returnCard;
    }

    @Override
    public Card takeBottom() {
        Card returnCard = card;
        card = mCardFactory.createCard();
        return returnCard;
    }

    @Override
    public int getNbCards() {
        return 42;
    }

    @Override
    public boolean isInfinite() {
        return true;
    }

    @Override
    public List<Card> getCards() {
        throw new RuntimeException("Don't call getCards on infinite card pile ! It's dangerous !");
    }

    @Override
    public void remove(Card card) {
        throw new RuntimeException("Don't call remove on infinite card pile !");
    }

    public interface CardFactory {
        Card createCard();
    }

    @Override
    public void sort(Comparator<Card> comparator) {
       throw new RuntimeException("Sort on infinite card pile  will be quick ... or long !");
    }

    
}
