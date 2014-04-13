package com.spaceagencies.client.graphics.ether.activities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.spaceagencies.client.LoginManager;
import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.Card.Type;
import com.spaceagencies.common.game.CardPile;
import com.spaceagencies.common.game.Player;
import com.spaceagencies.common.game.Turn;
import com.spaceagencies.common.game.Turn.TurnState;
import com.spaceagencies.common.game.TurnHelper;
import com.spaceagencies.common.tools.Log;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.Message;
import com.spaceagencies.i3d.SelectionManager;
import com.spaceagencies.i3d.SelectionManager.OnSelectionChangeListener;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.Button;
import com.spaceagencies.i3d.view.LinearLayout;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.View;
import com.spaceagencies.i3d.view.View.OnClickListener;
import com.spaceagencies.i3d.view.View.OnKeyEventListener;
import com.spaceagencies.server.GameServer;
import com.spaceagencies.server.Time.Timestamp;
import com.spaceagencies.server.engine.game.GameEngine;
import com.spaceagencies.server.engine.game.GameEngineObserver;

import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent.KeyAction;

public class BoardActivity extends Activity {

    private Player mPlayer;
    private Turn mTurn;
    private TextView deckDescription;
    private TextView handDescriptionTextView;
    private TextView discardPileTextView;
    private TextView turnMoneyCounterTextView;
    private TextView turnActionCounterTextView;
    private TextView turnBuyCounterTextView;
    private SelectionManager<Card> cardSelectionManager;
    private LinearLayout detailZone;
    private Button turnPhaseButton;
    private GameEngine mGameEngine;
    private TextView todoTextView;
    private LinearLayout playedCardsLinearLayout;
    private LinearLayout dynamicButtonsZone;
    private TextView missionsTextView;
    private LinearLayout firstLineLinearLayout;
    private LinearLayout secondLineLinearLayout;
	private LinearLayout firstColumnLinearLayout;
	private LinearLayout secondColumnLinearLayout;
    private LinearLayout handLinearLayout;
	private LinearLayout resourcesLinearLayout;
	

    private static final int UPDATE_UI_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        mGameEngine = GameServer.getGameEngine();
        
        mPlayer = LoginManager.getLocalPlayer();
        
        deckDescription = (TextView) findViewById("deckDescription@layout/deck_zone");
//        handDescriptionTextView = (TextView) findViewById("handDescriptionTextView@layout/hand_zone");
        discardPileTextView = (TextView) findViewById("discardPileTextView@layout/discard_pile_zone");
        
        turnMoneyCounterTextView = (TextView) findViewById("turnMoneyCounterTextView@layout/turn_zone");
        turnActionCounterTextView = (TextView) findViewById("turnActionCounterTextView@layout/turn_zone");
        turnBuyCounterTextView = (TextView) findViewById("turnBuyCounterTextView@layout/turn_zone");
        
        todoTextView = (TextView) findViewById("todoTextView@layout/turn_zone");
        
        handLinearLayout = (LinearLayout) findViewById("handLinearLayout@layout/hand_zone");
        
        playedCardsLinearLayout = (LinearLayout) findViewById("playedCardsLinearLayout@layout/played_cards_zone");
        firstLineLinearLayout = (LinearLayout) findViewById("firstLineLinearLayout@layout/supply_zone");
        secondLineLinearLayout = (LinearLayout) findViewById("secondLineLinearLayout@layout/supply_zone");
        
        resourcesLinearLayout = (LinearLayout) findViewById("resourcesLinearLayout@layout/board");
        
        
        firstColumnLinearLayout = (LinearLayout) findViewById("firstColumnLinearLayout@layout/objective_zone");
        secondColumnLinearLayout = (LinearLayout) findViewById("secondColumnLinearLayout@layout/objective_zone");
//        missionsTextView = (TextView) findViewById("missionsTextView@layout/objective_zone");
        
        detailZone = (LinearLayout) findViewById("detailZone@layout/board");
        
        turnPhaseButton = (Button) findViewById("turnPhaseButton@layout/turn_zone");
        dynamicButtonsZone = (LinearLayout) findViewById("dynamicButtonsZone@layout/turn_zone");
        
        turnPhaseButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                switch (mTurn.getTurnState()) {
                    case ACTION_PHASE:
                        mGameEngine.skipActions(mTurn);
                        break;
                    case BUY_PHASE:
                        mGameEngine.endTurn(mTurn);                        
                        break;
                    case WAITING_BEGIN:
                    case END_PHASE:
                    case PROCESSING_ACTION:
                    default:
                        break;
                }
            }
        });
        
        turnPhaseButton.setOnKeyListener(new OnKeyEventListener() {
            
            @Override
            public boolean onKeyEvent(V3DKeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyAction.KEY_PRESSED && keyEvent.getKeyCode() == V3DKeyEvent.KEY_SPACE) {
                    switch (mTurn.getTurnState()) {
                        case ACTION_PHASE:
                            mGameEngine.skipActions(mTurn);
                            break;
                        case BUY_PHASE:
                            mGameEngine.endTurn(mTurn);                        
                            break;
                        case WAITING_BEGIN:
                        case END_PHASE:
                        case PROCESSING_ACTION:
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        
        cardSelectionManager = new SelectionManager<Card>();
        
        
        cardSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Card>() {

            @Override
            public void onSelectionChange(List<Card> selection) {
                if(selection.size() > 0) {
                    Card card = selection.get(0);
                    detailZone.removeAllView();
                    detailZone.addViewInLayout(new DetailedCardView(card));
                    
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return false;
            }});
        
            mGameEngine.getWorldEnginObservable().register(this, new GameEngineObserver() {
            
            @Override
            public void onPlayerConnected(Player player) {
            }
            
            @Override
            public void onSomeThingChanged() {
                getHandler().removeMessages(UPDATE_UI_WHAT);
                getHandler().obtainMessage(UPDATE_UI_WHAT).send();
            }
        });
    }

    @Override
    public void onResume() {
        mTurn = mPlayer.getTurn();
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/details_zone");
        titleTextView.setText("\n\n\nLear about a race ...\n\n\nLear about History ...\n\n\nLear about conquest ...");
        updateUi();
    }

    @Override
    public void onPause() {

    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Timestamp time) {
        if(mTurn != mPlayer.getTurn()) {
            mTurn = mPlayer.getTurn();
            updateUi();    
        }
    }
    
    @Override
    protected void onMessage(Message message) {
        switch(message.what) {
            case UPDATE_UI_WHAT:
                updateUi();
                break;
        }
    }
    
    private void updateUi() {
        
//        cardSelectionManager.getSelection();
        
        int deckSize = mTurn.getPlayer().getDeck().getNbCards();
        deckDescription.setText("Deck: \n"+deckSize+ " card"+(deckSize > 1 ? "s" : ""));
        
//        int handSize = mTurn.getHand().getNbCards();
//        handDescriptionTextView.setText("Hand: "+handSize+ " card"+(handSize > 1 ? "s" : ""));
        
        int discardPileSize = mTurn.getPlayer().getDiscardPile().getNbCards();
        discardPileTextView.setText("Discard: \n"+discardPileSize+ " card"+(discardPileSize > 1 ? "s" : ""));
        
        turnMoneyCounterTextView.setText("Money count: "+mTurn.getMoneyCount());
        turnActionCounterTextView.setText("Action count: "+mTurn.getActionCount());
        turnBuyCounterTextView.setText("Buy count: "+mTurn.getBuyCount());
        
        //Display hand
        handLinearLayout.removeAllView();
        int i = 0;
        for(final Card card : mTurn.getHand().getCards()) {
            CardView cardView = new CardView(card, cardSelectionManager);
            cardView.getLayoutParams().setMarginLeftMeasure(new Measure(5, false, Axis.HORIZONTAL));
            cardView.getLayoutParams().setMarginRightMeasure(new Measure(5, false, Axis.HORIZONTAL));
            
                handLinearLayout.addViewInLayout(cardView);
            cardView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(I3dMouseEvent mouseEvent, View view) {
                    Log.log("Click count "+ mouseEvent.getClickCount());
                    if(mTurn.getTurnState().equals(TurnState.ACTION_PHASE) && (card.getType() & Type.TECHNOLOGIES.getFlag()) != 0) {
                        mGameEngine.playActionCard(mTurn, card);
                    } else if(mTurn.getTurnState().equals(TurnState.BUY_PHASE) && (card.getType() & Type.RESSOURCES.getFlag()) != 0) {
                        mGameEngine.playRessourceCard(mTurn, card);
                        }
                }
            });
            i++;
        }
        
        //Display played cards
        playedCardsLinearLayout.removeAllView();
        
        for(final Card card : mTurn.getPlayedCards().getCards()) {
            CardView cardView = new CardView(card, cardSelectionManager);
            cardView.getLayoutParams().setMarginLeftMeasure(new Measure(5, false, Axis.HORIZONTAL));
            cardView.getLayoutParams().setMarginRightMeasure(new Measure(-100, false, Axis.HORIZONTAL));
            playedCardsLinearLayout.addViewInLayout(cardView);
        }
        
        // Resources
        resourcesLinearLayout.removeAllView();
        for(final CardPile cardPile : mPlayer.getWorld().getResources()) {
            if(cardPile.getNbCards() > 0) {
                CardPileView cardPileView = generateCardPileView(cardPile);
                
            	resourcesLinearLayout.addViewInLayout(cardPileView);
            }
            i++;
            
        }
        
        
        // Supply
        firstLineLinearLayout.removeAllView();
        secondLineLinearLayout.removeAllView();
        
        i = 0;
        for(final CardPile cardPile : mPlayer.getWorld().getSupply()) {
            if(cardPile.getNbCards() > 0) {
                CardPileView cardPileView = generateCardPileView(cardPile);
                
                if(i %2 == 0) {
                    firstLineLinearLayout.addViewInLayout(cardPileView);
                } else {
                    secondLineLinearLayout.addViewInLayout(cardPileView);
                }
                
                
            }
            i++;
            
        }
        
        dynamicButtonsZone.removeAllView();
        turnPhaseButton.setVisible(false);
        
        TurnState turnState = mTurn.getTurnState();
        switch (turnState) {
            case WAITING_BEGIN:
                todoTextView.setText("Wait for turn begining");
                break;
            case ACTION_PHASE:
                todoTextView.setText("Make actions");
                turnPhaseButton.setText("Skip actions");
                turnPhaseButton.setVisible(true);
                break;
            case BUY_PHASE:
                {
                    todoTextView.setText("Buy some card");
                    turnPhaseButton.setText("End turn");
                    turnPhaseButton.setVisible(true);
                    Button button = new Button();
                    button.setIdleStyle(I3dRessourceManager.getInstance().loadStyle("bigButton@styles"));
                    
                    button.setText("Play resources");
                    dynamicButtonsZone.addViewInLayout(button);
                    if(TurnHelper.hasMoneyInHand(mTurn)) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                    
                    button.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(I3dMouseEvent mouseEvent, View view) {
                            playAllRessourceCard();
                        }
                    });
                    
                    button.setOnKeyListener(new OnKeyEventListener() {
                        
                        @Override
                        public boolean onKeyEvent(V3DKeyEvent keyEvent) {
                            if(keyEvent.getAction() == KeyAction.KEY_PRESSED && keyEvent.getKeyCode() == Keyboard.KEY_A) {
                                playAllRessourceCard();
                            }
                            return false;
                        }
                    });
                    
                    
                }
                break;
            case END_PHASE:
                todoTextView.setText("Ending turn ...");
                break;
            case PROCESSING_ACTION:
                todoTextView.setText("!!TODO !!");
                break;
                
            default:
                break;
        }
        
        //Mission
        int missionPileSize = mGameEngine.getGame().getMissions().getNbCards();
//        missionsTextView.setText("Missions: "+missionPileSize+ " card"+(missionPileSize > 1 ? "s" : ""));
        
        firstColumnLinearLayout.removeAllView();
        secondColumnLinearLayout.removeAllView();
        
        int index = mPlayer.getWorld().getVisibleMissions().getNbCards();
        
        List<Card> cards = mPlayer.getWorld().getVisibleMissions().getCards();
        List<Card> inverseCards = new ArrayList<Card>();
        for(int j =cards.size()-1 ; j >=0 ; j--) {
        	inverseCards.add(cards.get(j));
        }
        
        //first Column
        if (cards.size() > 1) {
        	placeObjectiveCard( firstColumnLinearLayout, inverseCards.get(1), true);
        } else {
        	firstColumnLinearLayout.addViewInLayout(I3dRessourceManager.loadView("main@layout/bigCardPlaceHolder"));
        }
        if (cards.size() > 0) {
        	placeObjectiveCard( firstColumnLinearLayout, inverseCards.get(0), true);
        } else {
        	firstColumnLinearLayout.addViewInLayout(I3dRessourceManager.loadView("main@layout/bigCardPlaceHolder"));
        }
        if (cards.size() > 5) {
        	placeObjectiveCard( firstColumnLinearLayout, inverseCards.get(5), false);
        } else {
        	firstColumnLinearLayout.addViewInLayout(I3dRessourceManager.loadView("main@layout/cardPlaceHolder"));
        }
        
        //second Column
        if (cards.size() > 2) {
        	placeObjectiveCard( secondColumnLinearLayout, inverseCards.get(2), true);
        } else {
        	secondColumnLinearLayout.addViewInLayout(I3dRessourceManager.loadView("main@layout/bigCardPlaceHolder"));
        }
        if (cards.size() > 3) {
        	placeObjectiveCard( secondColumnLinearLayout, inverseCards.get(3), false);
        } else {
        	secondColumnLinearLayout.addViewInLayout(I3dRessourceManager.loadView("main@layout/cardPlaceHolder"));
        }
        if (cards.size() > 4) {
        	placeObjectiveCard( secondColumnLinearLayout, inverseCards.get(4), false);
        } else {
        	secondColumnLinearLayout.addViewInLayout(I3dRessourceManager.loadView("main@layout/cardPlaceHolder"));
        }
    }

	private CardPileView generateCardPileView(final CardPile cardPile) {
		CardPileView cardPileView = new CardPileView(cardPile, cardSelectionManager);
		
		cardPileView.getLayoutParams().setMarginTopMeasure(new Measure(10, false, Axis.HORIZONTAL));
		cardPileView.getLayoutParams().setMarginBottomMeasure(new Measure(10, false, Axis.HORIZONTAL));
		cardPileView.getLayoutParams().setMarginLeftMeasure(new Measure(10, false, Axis.HORIZONTAL));
		cardPileView.getLayoutParams().setMarginRightMeasure(new Measure(10, false, Axis.HORIZONTAL));
		
		cardPileView.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(I3dMouseEvent mouseEvent, View view) {
		        Log.log("Click count "+ mouseEvent.getClickCount());
		        if(mTurn.getTurnState().equals(TurnState.BUY_PHASE)) {
		            mGameEngine.buyCard(mTurn, cardPile);
		        }
		    }
		});
		return cardPileView;
	}
    
    private void placeObjectiveCard(LinearLayout columnLinearLayout, final Card card, boolean bigCard) {
		// TODO Auto-generated method stub
    	CardView cardView = addObjectiveCard(card, bigCard);
    	columnLinearLayout.addViewInLayout(cardView);
    	if (bigCard) { // add Listener for buyable objective
    		cardView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(I3dMouseEvent mouseEvent, View view) {
                    Log.log("Click count "+ mouseEvent.getClickCount());
                    if(mTurn.getTurnState().equals(TurnState.BUY_PHASE)) {
                        mGameEngine.buyMissionCard(mTurn, card);
                    }
                }
            });
    	}
	}

	private CardView addObjectiveCard(Card card,boolean BigCard) {
		CardView cardView = null;
		if (BigCard) { 
	    	cardView = new CardView(card, cardSelectionManager,"bigCard");
	        cardView.getLayoutParams().setMarginTopMeasure(new Measure(10, false, Axis.HORIZONTAL));
	        cardView.getLayoutParams().setMarginBottomMeasure(new Measure(10, false, Axis.HORIZONTAL));
	        cardView.getLayoutParams().setMarginLeftMeasure(new Measure(10, false, Axis.HORIZONTAL));
            cardView.getLayoutParams().setMarginRightMeasure(new Measure(10, false, Axis.HORIZONTAL));
		} else {
	    	cardView = new CardView(card, cardSelectionManager);
	        cardView.getLayoutParams().setMarginTopMeasure(new Measure(10, false, Axis.HORIZONTAL));
	        cardView.getLayoutParams().setMarginBottomMeasure(new Measure(10, false, Axis.HORIZONTAL));
	        cardView.getLayoutParams().setMarginLeftMeasure(new Measure(10, false, Axis.HORIZONTAL));
            cardView.getLayoutParams().setMarginRightMeasure(new Measure(10, false, Axis.HORIZONTAL));
		}
        return cardView;
    }
    
    private void playAllRessourceCard() {
        
        while(TurnHelper.hasMoneyInHand(mTurn)) {
            for(Card card : mTurn.getHand().getCards()) {
                if(TurnHelper.isMoneyCard(card)) {
                    mGameEngine.playRessourceCard(mTurn, card);
                    break;
                }
            }
        }
    }

    
}
