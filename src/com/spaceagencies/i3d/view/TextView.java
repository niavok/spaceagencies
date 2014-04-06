package com.spaceagencies.i3d.view;

import org.lwjgl.opengl.GL11;

import com.spaceagencies.common.tools.Log;
import com.spaceagencies.i3d.Color;
import com.spaceagencies.i3d.Graphics;
import com.spaceagencies.i3d.I3dContext;
import com.spaceagencies.i3d.I3dRessourceManager;
import com.spaceagencies.i3d.RessourceFileCache;
import com.spaceagencies.i3d.StyleSelector;
import com.spaceagencies.i3d.Texture;
import com.spaceagencies.i3d.fonts.CharacterPixmap;
import com.spaceagencies.i3d.fonts.Font;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.view.LayoutParams.LayoutMeasure;
import com.spaceagencies.i3d.view.drawable.Drawable;

public class TextView extends View {

    protected String text = "";
    protected Font font;
    private String[] wrappedText;
    protected StyleSelector<Color> textColor = new StyleSelector<>(Color.black);
    protected Gravity gravity = Gravity.TOP_LEFT;
    private float offsetX;
    private float offsetY;
    private float innerWidth;
    private float innerHeight;

    public enum Gravity {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT,
    }

    public TextView() {
        font = I3dContext.getInstance().getDefaultFont();
        wrappedText = new String[0];

        // wrappedText[0] = "Bonjour ceci est un test de text assez long.";
        // wrappedText[1] = "plop";
    }

    @Override
    public void onDraw(Graphics g) {

        // int localX = x + g.getTranslation().getX();
        // int localXbase = localX;
        // int localY = y + g.getTranslation().getY() - getLineHeight();

        float localX = offsetX;
        float localXbase = offsetX;
        float localY = offsetY;

        g.setColor(textColor.get(getState()));
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        CharacterPixmap pixmap;

        boolean init = true;

        for (String text : wrappedText) {
            for (int i = 0; i < text.length(); i++) {
                final char c = text.charAt(i);
                if (c == '\r' || c == '\f' || c == '\t') {
                    continue;
                } else if (c == ' ') {
                    localX += font.getWidth(' ');
                    continue;
                } else if (c == '[') {
                    // Tag begin
                    int tagEndIndex = text.indexOf(']');
                    if(tagEndIndex == -1) {
                        Log.warn("Non terminated tag at index "+i+" in TextView '"+text+"'");
                    } else {
                          String tagName = text.substring(i+1, tagEndIndex);
                          Drawable drawable = I3dRessourceManager.getInstance().loadDrawable(tagName);
                          int intrinsicWidth = drawable.getIntrinsicWidth();
                          int intrinsicHeight = drawable.getIntrinsicHeight();
                          float height = font.getHeight();
                          float width = height;
                        
                        if(intrinsicHeight != -1 && intrinsicWidth != -1) {
                            width = (float) intrinsicWidth * height / (float) intrinsicHeight;
                        }
                        
                        drawable.setGraphics(g);
                        drawable.setBounds(localX, localY, localX+width, localY + height);
                      
                        if(!init) {
                            GL11.glEnd();
                        }
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        drawable.draw();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        if(!init) {
                            GL11.glBegin(GL11.GL_QUADS);
                        }
                        localX += width;
                        // Skip all tag
                        i = tagEndIndex;
                    }
                    continue;
                }
                pixmap = font.getCharPixMap(c);

                if (init) {
                    Texture tex = pixmap.getTexture();

                    if (tex != null) {
                        if (tex.hasAlpha()) {
                            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
                        }

                        tex.bind();
                    }
                    GL11.glBegin(GL11.GL_QUADS);
                    init = false;
                }

                final int imgWidth = pixmap.getWidth();
                final int imgHeight = pixmap.getHeight();

                final float endY = pixmap.getEndY();
                final float endX = pixmap.getEndX();

                final float startX = pixmap.getStartX();
                final float startY = pixmap.getStartY();

                GL11.glTexCoord2f(startX, startY);
                GL11.glVertex2f(localX, localY);

                GL11.glTexCoord2f(startX, endY);
                GL11.glVertex2f(localX, imgHeight + localY);

                GL11.glTexCoord2f(endX, endY);
                GL11.glVertex2f(imgWidth + localX, imgHeight + localY);

                GL11.glTexCoord2f(endX, startY);
                GL11.glVertex2f(imgWidth + localX, localY);

                localX += pixmap.getCharWidth();
            }
            // move to start of next line
            localY += font.getHeight();
            localX = localXbase;
        }
        if (!init)
            GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public void setTextColor(StyleSelector<Color> textColor) {
        this.textColor = textColor;
    }

    public StyleSelector<Color> getTextColor() {
        return textColor;
    }
    
    /**
     * Text property Example i3d:gravity="top|center" Possible values : -
     * top|left - top|center - top|right - center|left - center - center|right -
     * bottom|left - bottom|center - bottom|right
     * 
     * @param gravity
     */
    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    @Override
    public View duplicate() {
        TextView view = new TextView();
        duplicateTo(view);
        return view;
    }

    @Override
    protected void duplicateTo(View view) {
        super.duplicateTo(view);
        TextView myView = (TextView) view;
        myView.setText(text);
    }

    public void setText(String text) {
        if(!this.text.equals(text)) {
            this.text = text;
            wrappedText = new String[1];
            wrappedText[0] = text;
            if(mParent != null) {
                mParent.requestLayout();
            }
        }
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        float height = mLayoutParams.getContentHeight();
        float width = mLayoutParams.getContentWidth();

        // Horizontal gravity
        if (gravity == Gravity.TOP_LEFT || gravity == Gravity.CENTER_LEFT || gravity == Gravity.TOP_LEFT) {
            offsetX = 0;
        } else if (gravity == Gravity.TOP_RIGHT || gravity == Gravity.CENTER_RIGHT || gravity == Gravity.TOP_RIGHT) {
            offsetX = width - innerWidth;
        } else { // Center
            offsetX = (width - innerWidth) / 2;
        }

        // Vertical gravity
        if (gravity == Gravity.TOP_LEFT || gravity == Gravity.TOP_CENTER || gravity == Gravity.TOP_RIGHT) {
            offsetY = 0;
        } else if (gravity == Gravity.BOTTOM_LEFT || gravity == Gravity.BOTTOM_CENTER || gravity == Gravity.BOTTOM_RIGHT) {
            offsetY = height - innerHeight;
        } else { // Center
            offsetY = (height - innerHeight) / 2;
        }
    }

    public void setFont(Font font) {
        if (font != null) {
            this.font = font;
        }
    }

    @Override
    public void onMeasure() {
        float measuredWidth = 0;
        float measuredHeight = font.getHeight();

        CharacterPixmap pixmap;

        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c == '\r' || c == '\f' || c == '\t') {
                continue;
            } else if (c == ' ') {
                measuredWidth += font.getWidth(' ');
                continue;
            } else if (c == '[') {
                // Tag begin
                int tagEndIndex = text.indexOf(']');
                if(tagEndIndex == -1) {
                    Log.warn("Non terminated tag at index "+i+" in TextView '"+text+"'");
                } else {
                    String tagName = text.substring(i+1, tagEndIndex);
                    Drawable drawable = I3dRessourceManager.getInstance().loadDrawable(tagName);
                    int intrinsicWidth = drawable.getIntrinsicWidth();
                    int intrinsicHeight = drawable.getIntrinsicHeight();
                    float width = measuredHeight;
                    
                    if(intrinsicHeight != -1 && intrinsicWidth != -1) {
                        width = (float) intrinsicWidth * measuredHeight / (float) intrinsicHeight;
                    }
                    measuredWidth += width;
                    // Skip all tag
                    i = tagEndIndex;
                }
                continue;
            }
            pixmap = font.getCharPixMap(c);

            measuredWidth += pixmap.getCharWidth();
        }

        innerHeight = measuredHeight;
        innerWidth = measuredWidth;
        // Log.trace("TextView onMeasure "+ text);
        // Log.trace("measuredWidth "+measuredWidth);
        // Log.trace("measuredHeight "+measuredHeight);

        if (!mLayoutParams.getLayoutMarginTop().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());
        }
        if (!mLayoutParams.getLayoutMarginBottom().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginBottom());
        }
        if (!mLayoutParams.getLayoutMarginLeft().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        }
        if (!mLayoutParams.getLayoutMarginRight().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginRight());
        }

        if (!mLayoutParams.getLayoutPaddingTop().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());
        }
        if (!mLayoutParams.getLayoutPaddingBottom().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingBottom());
        }
        if (!mLayoutParams.getLayoutPaddingLeft().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
        }
        if (!mLayoutParams.getLayoutPaddingRight().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingRight());
        }

        if (mLayoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || mLayoutParams.getMeasurePoint().getX().isRelative()) {
            mLayoutParams.mMeasuredContentWidth = measuredWidth;
        }
        if (mLayoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || mLayoutParams.getMeasurePoint().getY().isRelative()) {
            mLayoutParams.mMeasuredContentHeight = measuredHeight;
        }
    }
}
