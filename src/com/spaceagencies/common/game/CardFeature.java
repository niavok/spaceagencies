package com.spaceagencies.common.game;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType
public abstract class CardFeature {
    public abstract void resolve(Turn t);

    public abstract String getDescription();
}
