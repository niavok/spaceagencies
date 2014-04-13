package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.view.DrawableView;
import com.spaceagencies.i3d.view.LayoutParams.LayoutMeasure;
import com.spaceagencies.i3d.view.ProxyView;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.drawable.InsetDrawable;

public class DetailedCardView extends ProxyView {

    private Card mCard;

    public DetailedCardView(Card card) {
        super(I3dRessourceManager.loadView("main@layout/detailed_card"));
        mCard = card;
        
        TextView titleTextView = (TextView) findViewById("titleTextView@layout/detailed_card");
        TextView descriptionTextView = (TextView) findViewById("descriptionTextView@layout/detailed_card");
        
        titleTextView.setText(card.getTitle());
        descriptionTextView.setText(card.getFullDescription());
        
        if (card.getFilename() != "") {
            InsetDrawable insetDrawable = new InsetDrawable();
            insetDrawable.setHeight(350);
            insetDrawable.setWidth(420);
            insetDrawable.setInsetTop(0);
            insetDrawable.setInsetLeft(0);
            insetDrawable.setDrawableName(card.getFilename() + "_large@cards");
            DrawableView drawableView = (DrawableView) findViewById("imageDrawable@layout/detailed_card");
            drawableView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
            drawableView.getLayoutParams().setWidthMeasure(new Measure(420, false, Axis.HORIZONTAL));
            drawableView.getLayoutParams().setHeightMeasure(new Measure(350, false, Axis.VERTICAL));
            drawableView.setDrawable(insetDrawable);
        }
    }
    
    
    @Override
    public void onLayout(float l, float t, float r, float b) {
        super.onLayout(l, t, r, b);
    }

}
