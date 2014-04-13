package com.spaceagencies.common.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.spaceagencies.common.game.features.FeatureMoreActions;
import com.spaceagencies.common.game.features.FeatureMoreMoney;
import com.spaceagencies.server.GameServer;

@XmlRootElement
@XmlSeeAlso({ CardFeature.class, FeatureMoreActions.class })
public class Card extends GameEntity {
    private static final long serialVersionUID = -1131206989953598577L;

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

    @XmlElement()
    protected int victoryPoints;

    @XmlElement()
    private List<CardFeature> features = new ArrayList<CardFeature>();

    @XmlAttribute
    private int date;

    public Card() {
        super(null, 0);
    }

    public Card(Game game,
                long id,
                String title,
                String shortDescription,
                String longDescription,
                String filename,
                String action,
                long type,
                int cost,
                int victoryPoints) {
        super(game, id);
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

    public Card duplicate() {
        Card c = new Card(this.getWorld(),
                          GameServer.pickNewId(),
                          title,
                          shortDescription,
                          longDescription,
                          filename,
                          featureDescription,
                          type,
                          cost,
                          victoryPoints);
        c.features = features;
        return c;
    }

    private static Map<String, Card> namedCards = new HashMap<String, Card>();
    private static List<Card> cards = loadCards();
    private static List<Card> loadCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        File directory = new File("res/cards/");
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                try {
                    if (file.getAbsolutePath().endsWith(".xml")) {
                        Card card = unmarshal(Card.class, new FileInputStream(file));
                        cards.add(card);
                        namedCards.put(card.getFilename(), card);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        }
        return cards;
    }
    
    public static Card getCardByName(String name) {
        Card c;
        if ((c = namedCards.get(name)) != null) {
            return c.duplicate();
        }
        return null;
    }
    public static Card pullCardByName(String name) {
        Card c;
        if ((c = namedCards.get(name)) != null) {
            namedCards.remove(name);
            return c.duplicate();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static public <T> T unmarshal(Class<T> docClass, InputStream inputStream) throws JAXBException {
        String packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(inputStream);
    }

    public static Card getCuivre(Game game) {
        Card c = new Card(game,
                          GameServer.pickNewId(),
                          "Crew",
                          "",
                          "",
                          "resource1",
                          "",
                          Type.RESSOURCES.getFlag(),
                          0,
                          0);
        c.features.add(new FeatureMoreMoney(1));
        return c;
    }

    public static Card getArgent(Game game) {
        Card c = new Card(game,
                          GameServer.pickNewId(),
                          "Launcher",
                          "",
                          "",
                          "resource2",
                          "",
                          Type.RESSOURCES.getFlag(),
                          3,
                          0);
        c.features.add(new FeatureMoreMoney(2));
        return c;
    }

    public static Card getOr(Game game) {
        Card c = new Card(game,
                          GameServer.pickNewId(),
                          "Spacecraft",
                          "",
                          "",
                          "resource3",
                          "",
                          Type.RESSOURCES.getFlag(),
                          6,
                          0);
        c.features.add(new FeatureMoreMoney(3));
        return c;
    }

    private static Random random = new Random(42);

    public static Card getTestRandomMissionCard(int i) {

        int victory = random.nextInt(6) + 1;
        int cost = (int) (victory * 1.5 + ((double) i * (random.nextDouble() + 0.5) * 5 / 50));

        Card c = new Card(null,
                          GameServer.pickNewId(),
                          "Mission " + i,
                          "shortDescription2",
                          "longdescription2",
                          "filename2",
                          "action2",
                          Type.MISSIONS.getFlag(),
                          cost,
                          victory);
        c.date = i;
        return c;
    }

    public String getFullDescription() {
        StringBuilder fullDescription = new StringBuilder();

        for (CardFeature feature : features) {
            fullDescription.append(feature.getDescription());
            fullDescription.append("\n");
        }
        fullDescription.append("\n");

        fullDescription.append(shortDescription);


//        for (Type typeValue : Type.values()) {
//            if ((type & typeValue.getFlag()) != 0) {
//                fullDescription.append("\n");
//                fullDescription.append(typeValue.toString());
//            }
//        }

        return fullDescription.toString();
    }
    public String getFeaturesDescription() {
        StringBuilder fullDescription = new StringBuilder();
        
        for(CardFeature feature: features) {
            fullDescription.append(feature.getDescription());
            fullDescription.append("\n");
        }
        
        return fullDescription.toString();
    }

    public static List<Card> getCards() {
        return cards;
    }

    public List<CardFeature> getFeaturesList() {
        return features;
    }
}
