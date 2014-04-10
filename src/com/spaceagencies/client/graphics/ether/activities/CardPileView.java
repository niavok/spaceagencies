package com.spaceagencies.client.graphics.ether.activities;

import java.util.List;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardPile;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.SelectionManager;
import com.spaceagencies.i3d.SelectionManager.OnSelectionChangeListener;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.ProxyView;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.View;

public class CardPileView extends ProxyView {

    private CardPile mCardPile;
    private OnClickListener localClickListener;

    public CardPileView(CardPile cardPile, final SelectionManager<Card> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/card"));
        mCardPile = cardPile;
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/card");
        TextView descriptionTextView = (TextView) findViewById("descriptionTextView@layout/card");
        
        titleTextView.setText(cardPile.peekTop().getTitle());
        descriptionTextView.setText(cardPile.peekTop().getFullDescription());
        
        
        super.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                selectionManager.select(mCardPile.peekTop());
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
