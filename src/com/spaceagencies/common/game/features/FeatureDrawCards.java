package com.spaceagencies.common.game.features;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardFeature;
import com.spaceagencies.common.game.Turn;

@XmlRootElement(name="moreCards")
public class FeatureDrawCards extends CardFeature {
    
    @XmlAttribute
    private int count;

    public FeatureDrawCards() {
        super();
    }
    
    public FeatureDrawCards(int count) {
        super();
        this.count = count;
    }


    @Override
    public void resolve(Turn t) {
        Card card = t.draw();
        if(card !=null) {
            t.getHand().addBottom(card);
        }
    }
    
    @Override
    public String getDescription() {
        return "+ "+count+" Card"+(count > 1?"s":"");
    }

}
