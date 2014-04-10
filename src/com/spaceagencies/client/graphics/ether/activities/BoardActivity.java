package com.spaceagencies.client.graphics.ether.activities;

import java.util.List;

import com.spaceagencies.client.LoginManager;
import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.Card.Type;
import com.spaceagencies.common.game.Player;
import com.spaceagencies.common.game.Turn;
import com.spaceagencies.common.game.Turn.TurnState;
import com.spaceagencies.i3d.Bundle;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.SelectionManager;
import com.spaceagencies.i3d.SelectionManager.OnSelectionChangeListener;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.Activity;
import com.spaceagencies.i3d.view.Button;
import com.spaceagencies.i3d.view.LinearLayout;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.View;
import com.spaceagencies.i3d.view.View.OnClickListener;
import com.spaceagencies.server.GameServer;
import com.spaceagencies.server.Time.Timestamp;
import com.spaceagencies.server.engine.game.GameEngine;

public class BoardActivity extends Activity {

    private Player mPlayer;
    private Turn mTurn;
    private TextView deckDescription;
    private TextView handDescriptionTextView;
    private TextView discardPileTextView;
    private TextView turnMoneyCounterTextView;
    private TextView turnActionCounterTextView;
    private TextView turnBuyCounterTextView;
    private LinearLayout handLinearLayout;
    private SelectionManager<Card> cardSelectionManager;
    private LinearLayout detailZone;
    private Button turnPhaseButton;
    private GameEngine mGameEngine;
    private TextView todoTextView;
    private LinearLayout playedCardsLinearLayout;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        mGameEngine = GameServer.getGameEngine();
        
        mPlayer = LoginManager.getLocalPlayer();
        
        deckDescription = (TextView) findViewById("deckDescription@layout/deck_zone");
        handDescriptionTextView = (TextView) findViewById("handDescriptionTextView@layout/hand_zone");
        discardPileTextView = (TextView) findViewById("discardPileTextView@layout/discard_pile_zone");
        
        turnMoneyCounterTextView = (TextView) findViewById("turnMoneyCounterTextView@layout/turn_zone");
        turnActionCounterTextView = (TextView) findViewById("turnActionCounterTextView@layout/turn_zone");
        turnBuyCounterTextView = (TextView) findViewById("turnBuyCounterTextView@layout/turn_zone");
        
        todoTextView = (TextView) findViewById("todoTextView@layout/turn_zone");
        
        handLinearLayout = (LinearLayout) findViewById("handLinearLayout@layout/hand_zone");
        playedCardsLinearLayout = (LinearLayout) findViewById("playedCardsLinearLayout@layout/played_cards_zone");
        detailZone = (LinearLayout) findViewById("detailZone@layout/board");
        
        turnPhaseButton = (Button) findViewById("turnPhaseButton@layout/turn_zone");
        
        
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
    }

    @Override
    public void onResume() {
        mTurn = mPlayer.getTurn();
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
//        if(mTurn != mPlayer.getTurn()) {
            mTurn = mPlayer.getTurn();
            updateUi();    
//        }
    }
    
    private void updateUi() {
        
//        cardSelectionManager.getSelection();
        
        int deckSize = mTurn.getPlayer().getDeck().getNbCards();
        deckDescription.setText("Deck: "+deckSize+ " card"+(deckSize > 1 ? "s" : ""));
        
        int handSize = mTurn.getHand().getNbCards();
        handDescriptionTextView.setText("Hand: "+handSize+ " card"+(handSize > 1 ? "s" : ""));
        
        int discardPileSize = mTurn.getPlayer().getDiscardPile().getNbCards();
        discardPileTextView.setText("Discard pile: "+discardPileSize+ " card"+(discardPileSize > 1 ? "s" : ""));
        
        turnMoneyCounterTextView.setText("Money count: "+mTurn.getMoneyCount());
        turnActionCounterTextView.setText("Action count: "+mTurn.getActionCount());
        turnBuyCounterTextView.setText("Buy count: "+mTurn.getBuyCount());
        
        //Display hand
        handLinearLayout.removeAllView();
        
        for(final Card card : mTurn.getHand().getCards()) {
            CardView cardView = new CardView(card, cardSelectionManager);
            cardView.getLayoutParams().setMarginLeftMeasure(new Measure(5, false, Axis.HORIZONTAL));
            cardView.getLayoutParams().setMarginRightMeasure(new Measure(5, false, Axis.HORIZONTAL));
            handLinearLayout.addViewInLayout(cardView);
            
            cardView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(I3dMouseEvent mouseEvent, View view) {
                    if(mTurn.getTurnState().equals(TurnState.ACTION_PHASE) && (card.getType() & Type.TECHNOLOGIES.getFlag()) != 0) {
                        mGameEngine.playActionCard(mTurn, card);
                    } else if(mTurn.getTurnState().equals(TurnState.BUY_PHASE) && (card.getType() & Type.RESSOURCES.getFlag()) != 0) {
                        mGameEngine.playRessourceCard(mTurn, card);
                    }
                }
            });
            
        }
        
        //Display played cards
        playedCardsLinearLayout.removeAllView();
        
        for(final Card card : mTurn.getPlayedCards().getCards()) {
            CardView cardView = new CardView(card, cardSelectionManager);
            cardView.getLayoutParams().setMarginLeftMeasure(new Measure(5, false, Axis.HORIZONTAL));
            cardView.getLayoutParams().setMarginRightMeasure(new Measure(-100, false, Axis.HORIZONTAL));
            playedCardsLinearLayout.addViewInLayout(cardView);
        }
        
        TurnState turnState = mTurn.getTurnState();
        switch (turnState) {
            case WAITING_BEGIN:
                todoTextView.setText("Wait for turn begining");
                turnPhaseButton.setVisible(false);
                break;
            case ACTION_PHASE:
                todoTextView.setText("Make actions");
                turnPhaseButton.setText("Skip actions");
                turnPhaseButton.setVisible(true);
                break;
            case BUY_PHASE:
                todoTextView.setText("Buy some card");
                turnPhaseButton.setText("End turn");
                turnPhaseButton.setVisible(true);
                break;
            case END_PHASE:
                todoTextView.setText("Ending turn ...");
                turnPhaseButton.setVisible(false);
                break;
            case PROCESSING_ACTION:
                todoTextView.setText("!!TODO !!");
                turnPhaseButton.setVisible(false);
                break;
                
            default:
                break;
        }
        
        
        
        
//        <View
//        i3d:id="card"
//        i3d:layout_marginLeft="5px"
//        i3d:layout_marginRight="5px"
//        i3d:ref="main@layout/card" />
//       
        
    }

    
}
