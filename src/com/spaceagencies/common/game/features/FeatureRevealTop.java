package com.spaceagencies.common.game.features;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardFeature;
import com.spaceagencies.common.game.Turn;

@XmlRootElement(name="revealTop")
public class FeatureRevealTop extends CardFeature {
    
    @XmlAttribute
    private int count;

    public FeatureRevealTop() {
        super();
    }
    
    public FeatureRevealTop(int count) {
        super();
        this.count = count;
    }


    @Override
    public void resolve(Turn t) {
    }
    
    @Override
    public String getDescription() {
        return "";
    }

}
