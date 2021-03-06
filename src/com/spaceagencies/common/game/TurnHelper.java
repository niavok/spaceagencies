package com.spaceagencies.common.game;

import com.spaceagencies.common.game.Card.Type;

public class TurnHelper {

    public static boolean hasMoneyInHand(Turn turn) {
        
        for(Card card : turn.getHand().getCards()) {
            if(TurnHelper.isMoneyCard(card)) {
                return true;
            }
        }
            
        return false;
    }

    public static boolean isMoneyCard(Card card) {
        if((card.getType() & Type.RESSOURCES.getFlag()) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMissionCard(Card card) {
        if((card.getType() & Type.MISSIONS.getFlag()) == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isTechnoCard(Card card) {
        if((card.getType() & Type.TECHNOLOGIES.getFlag()) == 0) {
            return false;
        } else {
            return true;
        }
    }
    
}
