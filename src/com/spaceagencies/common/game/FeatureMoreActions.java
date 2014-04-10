package com.spaceagencies.common.game;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="moreActions")
public class FeatureMoreActions extends CardFeature {
    
    @XmlAttribute
    private int count;

    public FeatureMoreActions() {
        super();
    }
    
    public FeatureMoreActions(int count) {
        super();
        this.count = count;
    }


    @Override
    public void resolve(Turn t) {
        t.addActions(count);
    }
    
    @Override
    public String getDescription() {
        return "+ "+count+" Action"+(count > 1?"s":"");
    }

}
