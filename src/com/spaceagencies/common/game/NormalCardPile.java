package com.spaceagencies.common.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NormalCardPile implements CardPile {
    private LinkedList<Card> cards = new LinkedList<Card>();

    public NormalCardPile() {
        super();
    }

    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#addTop(com.spaceagencies.common.game.Card)
     */
    @Override
    public void addTop(Card c) {
        cards.addFirst(c);
    }

    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#addBottom(com.spaceagencies.common.game.Card)
     */
    @Override
    public void addBottom(Card c) {
        cards.addLast(c);
    }

    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#shuffle()
     */
    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#peekTop()
     */
    @Override
    public Card peekTop() {
        return cards.peekFirst();
    }
    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#peekBottom()
     */
    @Override
    public Card peekBottom() {
        return cards.peekLast();
    }
    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#takeTop()
     */
    @Override
    public Card takeTop() {
        return cards.pollFirst();
    }
    /* (non-Javadoc)
     * @see com.spaceagencies.common.game.CardPile#takeBottom()
     */
    @Override
    public Card takeBottom() {
        return cards.pollLast();
    }

    @Override
    public int getNbCards() {
        return cards.size();
    }

    @Override
    public boolean isInfinite() {
        return false;
    }
    
    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void remove(Card card) {
        cards.remove(card);
    }

}
