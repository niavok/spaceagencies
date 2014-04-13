package com.spaceagencies.common.game.features;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.spaceagencies.common.game.CardFeature;
import com.spaceagencies.common.game.Turn;

@XmlRootElement(name="moreBuys")
public class FeatureMoreBuy extends CardFeature {
    
    @XmlAttribute
    private int count;

    public FeatureMoreBuy() {
        super();
    }
    
    public FeatureMoreBuy(int count) {
        super();
        this.count = count;
    }


    @Override
    public void resolve(Turn t) {
        t.addBuy(count);
    }
    
    @Override
    public String getDescription() {
        return "+ "+count+" Buy";
    }

}
