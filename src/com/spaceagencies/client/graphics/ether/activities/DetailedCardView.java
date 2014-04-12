package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.view.ProxyView;
import com.spaceagencies.i3d.view.TextView;

public class DetailedCardView extends ProxyView {

    private Card mCard;

    public DetailedCardView(Card card) {
        super(I3dRessourceManager.loadView("main@layout/detailed_card"));
        mCard = card;
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/detailed_card");
        TextView descriptionTextView = (TextView) findViewById("descriptionTextView@layout/detailed_card");
        
        titleTextView.setText(card.getTitle());
        descriptionTextView.setText(card.getFullDescription());
    }
    
    
    @Override
    public void onLayout(float l, float t, float r, float b) {
        super.onLayout(l, t, r, b);
    }

}
