package com.spaceagencies.client.graphics.ether.activities;

import java.util.List;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardPile;
import com.spaceagencies.common.game.TurnHelper;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.SelectionManager;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.SelectionManager.OnSelectionChangeListener;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.DrawableView;
import com.spaceagencies.i3d.view.LayoutParams.LayoutMeasure;
import com.spaceagencies.i3d.view.ProxyView;
import com.spaceagencies.i3d.view.RelativeLayout;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.View;

public class CardPileView extends ProxyView {

    private CardPile mCardPile;
    private OnClickListener localClickListener;

    public CardPileView(CardPile cardPile, final SelectionManager<Card> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/card"));
        mCardPile = cardPile;
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/card");
//        TextView descriptionTextView = (TextView) findViewById("descriptionTextView@layout/card");
        TextView costTextView = (TextView) findViewById("costTextView@layout/card");
        Card peekTop = cardPile.peekTop();
        costTextView.setText(""+peekTop.getCost());
        
        titleTextView.setText(peekTop.getTitle());
//        descriptionTextView.setText(cardPile.peekTop().getFullDescription());
        
        RelativeLayout cardTypePlaceholder = (RelativeLayout) findViewById("cardTypePlaceholder@layout/card");
        RelativeLayout cardBorderPlaceholder = (RelativeLayout) findViewById("cardBorderPlaceholder@layout/card");
        
        
        if(TurnHelper.isMoneyCard(peekTop)) {

            DrawableView drawableView2 = new DrawableView();
            drawableView2.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardResourcesBorder@card"));
            drawableView2.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setWidthMeasure(new Measure(166, false, Axis.HORIZONTAL));
            drawableView2.getLayoutParams().setHeightMeasure(new Measure(145, false, Axis.VERTICAL));
            cardBorderPlaceholder.addViewInLayout(drawableView2);
            
            View cardPvFlagDrawable = findViewById("cardPvFlagDrawable@layout/card");
            TextView pvTextView = (TextView) findViewById("pvTextView@layout/card");
            pvTextView.setText(""+peekTop.getVictoryPoints());
            cardPvFlagDrawable.setVisible(false);
            pvTextView.setVisible(false);
        } else if(TurnHelper.isTechnoCard(peekTop)) {
            DrawableView drawableView = new DrawableView();
            drawableView.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardTypeTechno@card"));
            drawableView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setWidthMeasure(new Measure(30, false, Axis.HORIZONTAL));
            drawableView.getLayoutParams().setHeightMeasure(new Measure(30, false, Axis.VERTICAL));
            cardTypePlaceholder.addViewInLayout(drawableView);
            
            
            DrawableView drawableView2 = new DrawableView();
            drawableView2.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardTechnoBorder@card"));
            drawableView2.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setWidthMeasure(new Measure(166, false, Axis.HORIZONTAL));
            drawableView2.getLayoutParams().setHeightMeasure(new Measure(145, false, Axis.VERTICAL));
            cardBorderPlaceholder.addViewInLayout(drawableView2);
            
            View cardPvFlagDrawable = findViewById("cardPvFlagDrawable@layout/card");
            TextView pvTextView = (TextView) findViewById("pvTextView@layout/card");
            pvTextView.setText(""+peekTop.getVictoryPoints());
            cardPvFlagDrawable.setVisible(false);
            pvTextView.setVisible(false);
            
        } else if(TurnHelper.isMissionCard(peekTop)) {
            DrawableView drawableView = new DrawableView();
            drawableView.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardTypeMission@card"));
            drawableView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setWidthMeasure(new Measure(30, false, Axis.HORIZONTAL));
            drawableView.getLayoutParams().setHeightMeasure(new Measure(30, false, Axis.VERTICAL));
            cardTypePlaceholder.addViewInLayout(drawableView);
            
            DrawableView drawableView2 = new DrawableView();
            drawableView2.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardMissionBorder@card"));
            drawableView2.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setWidthMeasure(new Measure(166, false, Axis.HORIZONTAL));
            drawableView2.getLayoutParams().setHeightMeasure(new Measure(145, false, Axis.VERTICAL));
            cardBorderPlaceholder.addViewInLayout(drawableView2);
            
            View cardPvFlagDrawable = findViewById("cardPvFlagDrawable@layout/card");
            TextView pvTextView = (TextView) findViewById("pvTextView@layout/card");
            pvTextView.setText(""+peekTop.getVictoryPoints());
            cardPvFlagDrawable.setVisible(true);
            pvTextView.setVisible(true);
        }
        
        
        
        
        
        
        super.setOnMouseListener(new OnMouseEventListener() {
			
			@Override
			public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
                if (!TurnHelper.isMoneyCard(mCardPile.peekTop())){
                    selectionManager.select(mCardPile.peekTop());
                }

				return false;
			}
		});
        
        super.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
            	if(localClickListener != null) {
                    localClickListener.onClick(mouseEvent, view);
                }
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Card>() {
            
            public void onSelectionChange(List<Card> selection) {
                if(selection.contains(mCardPile.peekTop())) {
                    setState(ViewState.SELECTED);
                } else {
                    setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(CardPileView.class));
            }
        });
        
        if(selectionManager.getSelection().contains(mCardPile.peekTop())) {
            setState(ViewState.SELECTED);
        }
    }
    
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        localClickListener = onClickListener;
    }
    

}
