package com.spaceagencies.common.game.features;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.spaceagencies.common.game.CardFeature;
import com.spaceagencies.common.game.Turn;

@XmlRootElement(name="moreMoney")
public class FeatureMoreMoney extends CardFeature {
    
    @XmlAttribute
    private int count;

    public FeatureMoreMoney() {
        super();
    }
    
    public FeatureMoreMoney(int count) {
        super();
        this.count = count;
    }


    @Override
    public void resolve(Turn t) {
        t.addMoney(count);
    }
    
    @Override
    public String getDescription() {
        return "+ "+count+" Money";
    }

}
