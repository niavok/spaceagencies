package com.spaceagencies.common.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.spaceagencies.common.game.features.FeatureMoreActions;
import com.spaceagencies.common.game.features.FeatureMoreBuy;
import com.spaceagencies.common.game.features.FeatureMoreMoney;
import com.spaceagencies.common.game.features.FeatureDrawCards;

@XmlRootElement
@XmlSeeAlso({CardFeature.class, FeatureMoreActions.class})
public class Card {
    public enum Type {
        RESSOURCES(1 << 0), TECHNOLOGIES(1 << 1), MISSIONS(1 << 2);
        private final long flag;

        Type(long flag) {
            this.flag = flag;
        }

        public long getFlag() {
            return flag;
        }
    }
    
    @XmlElement
    protected String title;
    
    @XmlElement
    protected String shortDescription;
    @XmlElement
    protected String longDescription;
    @XmlElement
    protected String filename;
    @XmlElement
    protected String featureDescription;
    @XmlAttribute
    protected long type;
    @XmlAttribute
    protected int cost;
    
    @XmlElement
    protected int victoryPoints;

    @XmlAnyElement
    protected List<CardFeature> features = new ArrayList<CardFeature>();

    @XmlAttribute
    private int date;
    
    public Card() {
        super();
    }
    public Card(String title,
                String shortDescription,
                String longDescription,
                String filename,
                String action,
                long type,
                int cost,
                int victoryPoints) {
        super();
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.filename = filename;
        this.featureDescription = action;
        this.type = type;
        this.cost = cost;
        this.victoryPoints = victoryPoints;
        this.date = 0;
    }

    public final int getVictoryPoints() {
        return victoryPoints;
    }

    public final String getTitle() {
        return title;
    }

    public final String getShortDescription() {
        return shortDescription;
    }

    public final String getLongDescription() {
        return longDescription;
    }

    public final String getFilename() {
        return filename;
    }

    public final String getAction() {
        return featureDescription;
    }

    public final long getType() {
        return type;
    }

    public final int getCost() {
        return cost;
    }
    
    public int getDate() {
        return date;
    }
    
    @SuppressWarnings("unchecked")
    static public <T> T unmarshal( Class<T> docClass, InputStream inputStream )
        throws JAXBException {
        String packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance( packageName );
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(inputStream);
    }

    public static void main(String[] args) throws Exception {
        try {
            {
                JAXBContext context = JAXBContext.newInstance(Card.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                Card c = new Card("title", "shortDescription", "longdescription", "filename", "action", 2, 12, 0);
                c.features.add(new FeatureMoreActions(12));
                c.features.add(new FeatureMoreActions(13));
                File f = new File("card.xml");
                m.marshal(c, f);
            }
            {
                File f = new File("card.xml");
                Card card = unmarshal(Card.class, new FileInputStream(f));
                System.out.println(card);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
    
    public static Card getTestCardCuivre() {
        Card c = new Card("Cuivre", "shortDescription", "longdescription1", "filename1", "action1", Type.RESSOURCES.getFlag(), 0, 0);
        c.features.add(new FeatureMoreMoney(1));
        return c;
    }
    
    public static Card getTestCardArgent() {
        Card c = new Card("Argent", "shortDescription", "longdescription1", "filename1", "action1", Type.RESSOURCES.getFlag(), 3, 0);
        c.features.add(new FeatureMoreMoney(2));
        return c;
    }
    
    public static Card getTestCardOr() {
        Card c = new Card("Or", "shortDescription", "longdescription1", "filename1", "action1", Type.RESSOURCES.getFlag(), 6, 0);
        c.features.add(new FeatureMoreMoney(3));
        return c;
    }
    
    public static Card getTestCardVillage() {
        Card c = new Card("Village", "shortDescription2", "longdescription2", "filename2", "action2", Type.TECHNOLOGIES.getFlag(), 3, 0);
        c.features.add(new FeatureDrawCards(1));
        c.features.add(new FeatureMoreActions(2));
        return c;
    }
    
    public static Card getTestCardBucheron() {
        Card c = new Card("Bucheron", "shortDescription2", "longdescription2", "filename2", "action2", Type.TECHNOLOGIES.getFlag(), 3, 0);
        c.features.add(new FeatureMoreBuy(1));
        c.features.add(new FeatureMoreMoney(2));
        return c;
    }
    
    public static Card getTestCardForgeron() {
        Card c = new Card("Forgeron", "shortDescription2", "longdescription2", "filename2", "action2", Type.TECHNOLOGIES.getFlag(), 4, 0);
        c.features.add(new FeatureDrawCards(3));
        return c;
    }
    
    public static Card getTestCardDomaine() {
        Card c = new Card("Domaine", "shortDescription2", "longdescription2", "filename2", "action2", Type.MISSIONS.getFlag(), 2, 1);
        return c;
    }
    
    private static Random random = new Random(42);
    
    public static Card getTestRandomMissionCard(int i) {
        
        int victory = random.nextInt(6)+1;
        int cost =  (int) (victory * 1.5 + ((double) i  * (random.nextDouble() + 0.5) * 5 / 50 ));
        
        Card c = new Card("Mission "+i, "shortDescription2", "longdescription2", "filename2", "action2", Type.MISSIONS.getFlag(), cost, victory);
        c.date = i;
        return c;
    }
    
    
    public String getFullDescription() {
        StringBuilder fullDescription = new StringBuilder();
        
        for(CardFeature feature: features) {
            fullDescription.append(feature.getDescription());
            fullDescription.append("\n");
        }
        fullDescription.append("\n");
        
        fullDescription.append(shortDescription);
        
       
        
        fullDescription.append("\n");
        fullDescription.append("Cost: "+cost);
        
        if(victoryPoints > 0) {
            fullDescription.append("\n");
            fullDescription.append("Victory: "+victoryPoints);    
        }
        
        for(Type typeValue : Type.values()) {
            if((type & typeValue.getFlag()) != 0) {
                fullDescription.append("\n");
                fullDescription.append(typeValue.toString());
            }
        }
        
        return fullDescription.toString();
    }
}
