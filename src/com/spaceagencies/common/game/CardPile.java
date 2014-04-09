package com.spaceagencies.common.game;

import java.util.List;

public interface CardPile {

    public abstract void addTop(Card c);

    public abstract void addBottom(Card c);

    public abstract void shuffle();

    public abstract Card peekTop();

    public abstract Card peekBottom();

    public abstract Card takeTop();

    public abstract Card takeBottom();

    public abstract int getNbCards();

    public abstract boolean isInfinite();

    public abstract List<Card> getCards();

}
