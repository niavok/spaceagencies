package com.spaceagencies.client.graphics.ether.activities;

import com.spaceagencies.common.game.Card;
import com.spaceagencies.common.game.CardFeature;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.Measure.Axis;
import com.spaceagencies.i3d.view.DrawableView;
import com.spaceagencies.i3d.view.LinearLayout;
import com.spaceagencies.i3d.view.ProxyView;
import com.spaceagencies.i3d.view.RelativeLayout;
import com.spaceagencies.i3d.view.TextView;
import com.spaceagencies.i3d.view.drawable.InsetDrawable;

public class DetailedCardView extends ProxyView {

    private Card mCard;

    public DetailedCardView(Card card) {
        super(I3dRessourceManager.loadView("main@layout/details_zone"));
        mCard = card;

        TextView titleTextView = (TextView) findViewById("titleTextView@layout/details_zone");
        TextView descriptionTextView = (TextView) findViewById("handDescriptionTextView@layout/details_zone");

        titleTextView.setText(card.getTitle());
        descriptionTextView.setText(card.getLongDescription());

        LinearLayout featuresLayout = (LinearLayout) findViewById("featuresLayout@layout/details_zone");
        RelativeLayout relativeLayout = new RelativeLayout();
        featuresLayout.addViewInLayout(relativeLayout);
        
        if (card.getCost() >= 0) {
            TextView textView = new TextView();
            textView.setText("Cost: " + String.valueOf(card.getCost()));
            textView.getLayoutParams().setWidthMeasure(new Measure(150, false, Axis.HORIZONTAL));
            textView.setFont(I3dRessourceManager.getInstance().loadFont("verylargebold@fonts"));
            textView.getLayoutParams().setMarginLeftMeasure(new Measure(30, false, Axis.HORIZONTAL));
            featuresLayout.addViewInLayout(textView);
        }
        if (card.getVictoryPoints() > 0) {
            TextView textView = new TextView();
            textView.setText("Mission points: " + String.valueOf(card.getCost()));
            textView.getLayoutParams().setWidthMeasure(new Measure(150, false, Axis.HORIZONTAL));
            textView.setFont(I3dRessourceManager.getInstance().loadFont("verylargebold@fonts"));
            textView.getLayoutParams().setMarginLeftMeasure(new Measure(30, false, Axis.HORIZONTAL));
            featuresLayout.addViewInLayout(textView);
        }
        for (CardFeature feat : card.getFeaturesList()) {
            TextView textView = new TextView();
            textView.setText(feat.getDescription());
            textView.getLayoutParams().setWidthMeasure(new Measure(150, false, Axis.HORIZONTAL));
            textView.setFont(I3dRessourceManager.getInstance().loadFont("verylargebold@fonts"));
            textView.getLayoutParams().setMarginLeftMeasure(new Measure(30, false, Axis.HORIZONTAL));

            featuresLayout.addViewInLayout(textView);
        }

        if (card.getFilename() != "") {
            InsetDrawable insetDrawable = new InsetDrawable();
            insetDrawable.setHeight(350);
            insetDrawable.setWidth(420);
            insetDrawable.setInsetTop(0);
            insetDrawable.setInsetLeft(0);
            insetDrawable.setDrawableName(card.getFilename() + "_large@cards");
            DrawableView drawableView = (DrawableView) findViewById("imageDrawable@layout/details_zone");
            drawableView.setDrawable(insetDrawable);
        }
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        super.onLayout(l, t, r, b);
    }

}
