package com.spaceagencies.common.game;

import java.util.LinkedList;
import java.util.List;

public class InfinitCardPile implements CardPile {
    
    private Card card;

    public InfinitCardPile(Card card) {
        this.card = card;
    }

    @Override
    public void addTop(Card c) {
        this.card = c;
    }

    @Override
    public void addBottom(Card c) {
        this.card = c;
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
        // TODO Duplicate ?
        return this.card;
    }

    @Override
    public Card takeBottom() {
        return this.card;
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

}
