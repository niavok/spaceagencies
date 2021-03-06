package com.spaceagencies.client.graphics.ether.activities;

import java.util.List;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.TurnHelper;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.SelectionManager;
import com.spaceagencies.i3d.SelectionManager.OnSelectionChangeListener;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.DrawableView;
import com.spaceagencies.i3d.view.LayoutParams.LayoutMeasure;
import com.spaceagencies.i3d.view.ProxyView;
import com.spaceagencies.i3d.view.RelativeLayout;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.View;
import com.spaceagencies.i3d.view.drawable.InsetDrawable;

public class CardView extends ProxyView {

    private Card mCard;
    private OnClickListener localClickListener;

    public CardView(Card card, final SelectionManager<Card> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/card"));
        mCard = card;
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/card");
//        TextView descriptionTextView = (TextView) findViewById("descriptionTextView@layout/card");
        TextView costTextView = (TextView) findViewById("costTextView@layout/card");
        
        RelativeLayout cardTypeMissionPlaceholder = (RelativeLayout) findViewById("cardBorderPlaceholder@layout/card");
        
        
//        DrawableView drawableView = new DrawableView();
//        drawableView.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardTypeMission@card"));
        
        
        
        RelativeLayout cardTypePlaceholder = (RelativeLayout) findViewById("cardTypePlaceholder@layout/card");
        RelativeLayout cardBorderPlaceholder = (RelativeLayout) findViewById("cardBorderPlaceholder@layout/card");
        
        
        if(TurnHelper.isMoneyCard(card)) {
        	DrawableView drawableView2 = new DrawableView();
            drawableView2.setDrawable(I3dRessourceManager.getInstance().loadDrawable("cardResourcesBorder@card"));
            drawableView2.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView2.getLayoutParams().setWidthMeasure(new Measure(166, false, Axis.HORIZONTAL));
            drawableView2.getLayoutParams().setHeightMeasure(new Measure(145, false, Axis.VERTICAL));
            cardBorderPlaceholder.addViewInLayout(drawableView2);
            
            View cardPvFlagDrawable = findViewById("cardPvFlagDrawable@layout/card");
            TextView pvTextView = (TextView) findViewById("pvTextView@layout/card");
            cardPvFlagDrawable.setVisible(false);
            pvTextView.setVisible(false);
        } else if(TurnHelper.isTechnoCard(card)) {
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
            cardPvFlagDrawable.setVisible(false);
            pvTextView.setVisible(false);
            
        } else if(TurnHelper.isMissionCard(card)) {
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
            pvTextView.setText(""+card.getVictoryPoints());
            cardPvFlagDrawable.setVisible(true);
            pvTextView.setVisible(true);
        }
        
        costTextView.setText(""+card.getCost());
        
        titleTextView.setText(card.getTitle());
//        descriptionTextView.setText(card.getFullDescription());
        
        if (card.getFilename() != "") {
            InsetDrawable insetDrawable = new InsetDrawable();
            insetDrawable.setWidth(150);
            insetDrawable.setHeight(125);
            insetDrawable.setInsetTop(0);
            insetDrawable.setInsetLeft(0);
            insetDrawable.setDrawableName(card.getFilename() + "@cards");
            DrawableView drawableView = (DrawableView) findViewById("imageDrawable@layout/card");
            drawableView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setWidthMeasure(new Measure(150, false, Axis.HORIZONTAL));
            drawableView.getLayoutParams().setHeightMeasure(new Measure(125, false, Axis.VERTICAL));
            drawableView.setDrawable(insetDrawable);
        }
        
        super.setOnMouseListener(new OnMouseEventListener() {
			@Override
			public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
                if (!TurnHelper.isMoneyCard(mCard)){
                    selectionManager.select(mCard);
                }
				return false;
			}
		});
        
        super.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                if(localClickListener!= null) {
                    localClickListener.onClick(mouseEvent, view);
                }
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Card>() {
            
            public void onSelectionChange(List<Card> selection) {
                if(selection.contains(mCard)) {
                    setState(ViewState.SELECTED);
                } else {
                    setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(CardView.class));
            }
        });
        
        if(selectionManager.getSelection().contains(card)) {
            setState(ViewState.SELECTED);
        }
    }
    
    public CardView(Card card, final SelectionManager<Card> selectionManager, String layout) {
        super(I3dRessourceManager.loadView("main@layout/"+layout));
        mCard = card;
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/"+layout);
        TextView costTextView = (TextView) findViewById("costTextView@layout/"+layout);
        
        
        titleTextView.setText(card.getTitle());
        costTextView.setText(""+card.getCost());
        
        if(layout.equals("bigCard")) {
            TextView descriptionTextView = (TextView) findViewById("descriptionTextView@layout/"+layout);
            descriptionTextView.setText(card.getFullDescription());
        }
        
        if (card.getFilename() != "") {
            InsetDrawable insetDrawable = new InsetDrawable();
            insetDrawable.setWidth(150);
            insetDrawable.setHeight(125);
            insetDrawable.setInsetTop(0);
            insetDrawable.setInsetLeft(0);
            insetDrawable.setDrawableName(card.getFilename() + "@cards");
            DrawableView drawableView = (DrawableView) findViewById("imageDrawable@layout/"+layout);
            drawableView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setWidthMeasure(new Measure(150, false, Axis.HORIZONTAL));
            drawableView.getLayoutParams().setHeightMeasure(new Measure(125, false, Axis.VERTICAL));
            drawableView.setDrawable(insetDrawable);
        }
        
        
        View cardPvFlagDrawable = findViewById("cardPvFlagDrawable@layout/"+layout);
        TextView pvTextView = (TextView) findViewById("pvTextView@layout/"+layout);
        pvTextView.setText(""+card.getVictoryPoints());
        cardPvFlagDrawable.setVisible(true);
        pvTextView.setVisible(true);
        
        super.setOnMouseListener(new OnMouseEventListener() {
			@Override
			public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
			    if (!TurnHelper.isMoneyCard(mCard)){
			        selectionManager.select(mCard);
			    }
				return false;
			}
		});
        
        super.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                if(localClickListener!= null) {
                    localClickListener.onClick(mouseEvent, view);
                }
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Card>() {
            
            public void onSelectionChange(List<Card> selection) {
                if(selection.contains(mCard)) {
                    setState(ViewState.SELECTED);
                } else {
                    setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(CardView.class));
            }
        });
        
        if(selectionManager.getSelection().contains(card)) {
            setState(ViewState.SELECTED);
        }
    }
    
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        localClickListener = onClickListener;
    }
    

}
