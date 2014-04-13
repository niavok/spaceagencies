package com.spaceagencies.i3d.view;

import org.lwjgl.opengl.GL11;

import com.spaceagencies.common.tools.Log;
import com.spaceagencies.common.tools.RessourceLoadingException;
import com.spaceagencies.i3d.Color;
import com.spaceagencies.i3d.Graphics;
import com.spaceagencies.i3d.I3dContext;
import com.spaceagencies.i3d.Style;
import com.spaceagencies.i3d.input.I3dMouseEvent;
import com.spaceagencies.i3d.input.I3dMouseEvent.Action;
import com.spaceagencies.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;

public abstract class View {

    /**
     * The parent this view is attached to. {@hide}
     * 
     * @see #getParent()
     */
    protected ViewParent mParent;
    protected LayoutParams mLayoutParams;
    private BorderParams borderParams;

    private String id = "";
    private OnClickListener onClickListener = null;
    private String help;
    private OnMouseEventListener onMouseEventListener;
    private OnKeyEventListener onKeyEventListener;
    private OnControllerEventListener onControllerEventListener;
    private boolean mVisible = true;
    private StyleRenderer styleRenderer;
//    private Style selectedStyle = new Style();
    private Style idleStyle = new Style();
    private ViewState state = ViewState.IDLE;
    private boolean mMouseOver;
    private boolean mEnabled = true;

    public enum ViewState {
        DISABLED, // The view is disabled, no interaction possible
        IDLE, // Idle state
        OVER, // The mouse is over the view
        FOCUSED, // The view has the focus
        FOCUSED_OVER,  // The view has the focus and the mouse is over
        SELECTED, // The view is selected
        SELECTED_OVER, // The view is selected and the mouse is over
        ACTIVE, // The button is pressing the view
    }
    
    public View() {
        mLayoutParams = new LayoutParams();
        borderParams = new BorderParams();
        styleRenderer = new StyleRenderer(this);
    }

    public void draw(Graphics g) {
        if (!mVisible) {
            return;
        }
        mMouseOver = isMouseOver(g.getUiTranslation());
        

        GL11.glPushMatrix();
        // GL11.glTranslatef(layout.offset.x, layout.offset.y, 0);
//        
        
        
        if(isMouseOver(g.getUiTranslation())) {
     //       g.setColor(Color.randomLightOpaqueColor());
//            g.drawFilledRectangle(mLayoutParams.mLeft, mLayoutParams.mTop, mLayoutParams.getTotalWidth(), mLayoutParams.getTotalHeight());
        }
        
        // Margin
        float tranlationXIncludingMargin = mLayoutParams.mLeft+ mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        float tranlationYIncludingMargin = mLayoutParams.mTop + mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());
        GL11.glTranslatef(tranlationXIncludingMargin, tranlationYIncludingMargin, 0);
        g.pushUiTranslation(new Point(tranlationXIncludingMargin, tranlationYIncludingMargin));
//        g.setColor(Color.randomDarkOpaqueColor());
//        g.drawFilledRectangle(0, 0, mLayoutParams.getBorderWidth(), mLayoutParams.getBorderHeight());
        styleRenderer.draw(g);
        
        
        
        //Padding
        float tranlationXPadding = mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
        float tranlationYPadding= mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());
        
        GL11.glTranslatef(tranlationXPadding, tranlationYPadding, 0);
        g.pushUiTranslation(new Point(tranlationXPadding, tranlationYPadding));
        
        
//        g.setColor(Color.randomLightOpaqueColor());
//        g.drawFilledRectangle(0, 0, mLayoutParams.getContentWidth(), mLayoutParams.getContentHeight());
        onDraw(g);
        
        // Pop padding
        g.popUiTranslation();

        // Pop margin
        g.popUiTranslation();
        
        
        GL11.glPopMatrix();
    }

    private boolean isMouseOver(Point point) {
        I3dMouseEvent lastMouseEvent = I3dContext.getInstance().getLastMouseEvent();
        if(lastMouseEvent == null) {
            return false;
        }
        
        float y = I3dContext.getInstance().getCanvas().getHeight() - lastMouseEvent.getY();
         float py = I3dContext.getInstance().getCanvas().getHeight() + point.y;
        
        if(lastMouseEvent.getX() > point.x + mLayoutParams.mLeft + mLayoutParams.mLeftMargin 
                && lastMouseEvent.getX() < point.x + mLayoutParams.mRight - mLayoutParams.mRightMargin
                && y  >  py + mLayoutParams.mTop  + mLayoutParams.mTopMargin
                && y < py + mLayoutParams.mBottom -  + mLayoutParams.mBottomMargin
                ) {
            return true;
        }
        return false;
    }

    public abstract void onDraw(Graphics g);

    public void measure(float widthMeasureSpec, float heightMeasureSpec) {
        
        mLayoutParams.mMeasuredContentWidth = 0;
        mLayoutParams.mMeasuredContentHeight = 0;
        
        if (mLayoutParams.getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
            if (!mLayoutParams.getMeasurePoint().getX().isRelative()) {
                mLayoutParams.mMeasuredContentWidth = mLayoutParams.computeMesure(mLayoutParams.getMeasurePoint().getX());
            }
        }

        if (mLayoutParams.getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
            if (!mLayoutParams.getMeasurePoint().getY().isRelative()) {
                mLayoutParams.mMeasuredContentHeight = mLayoutParams.computeMesure(mLayoutParams.getMeasurePoint().getY());
            }
        }

        // Set margin
        if (!mLayoutParams.getLayoutMarginTop().isRelative()) {
            mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());
        }
        if (!mLayoutParams.getLayoutMarginBottom().isRelative()) {
            mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginBottom());
        }
        if (!mLayoutParams.getLayoutMarginLeft().isRelative()) {
            mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        }
        if (!mLayoutParams.getLayoutMarginRight().isRelative()) {
            mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginRight());
        }

        // Set padding
        if (mLayoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED) {
            if (!mLayoutParams.getLayoutPaddingTop().isRelative()) {
                mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());
            }
            if (!mLayoutParams.getLayoutPaddingBottom().isRelative()) {
                mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingBottom());
            }
        }
        
        if (mLayoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED) {
            if (!mLayoutParams.getLayoutPaddingLeft().isRelative()) {
                mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
            }
            if (!mLayoutParams.getLayoutPaddingRight().isRelative()) {
                mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingRight());
            }
        }

        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void layout(float l, float t, float r, float b) {
        boolean changed = mLayoutParams.setFrame(l, t, r, b);
        mLayoutParams.setExtrasFrame(l, t, r, b);
        // if (changed) {
        onLayout(l, t, r, b);
        // }
    }

    public abstract void onLayout(float l, float t, float r, float b);

    public abstract void onMeasure(float widthMeasureSpec, float heightMeasureSpec);

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract View duplicate();

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }
    
    public void setState(ViewState state) {
        if(this.state != state) {
            this.state = state;
            switch (state) {
                case SELECTED:
//                    if(selectedStyle != null) {
//                        selectedStyle.apply(this);
//                    } else {
//                        idleStyle.apply(this);
//                    }
                break;
                case IDLE:
                default:
                    idleStyle.apply(this);
                break;    
            }
        }
    }

    public ViewState getState() {
        if(!mEnabled) {
            return ViewState.DISABLED;
        }
        if(mMouseOver) {
            switch (state) {
                case IDLE:
                    return ViewState.OVER;
                case FOCUSED:
                    return ViewState.FOCUSED_OVER;
                case SELECTED:
                    return ViewState.SELECTED_OVER;
                default:    
                    return state;
            }
        } else {
            return state;
        }
        
    }
    
    void assignParent(ViewParent parent) {
        if (mParent == null) {
            mParent = parent;
        } else if (parent == null) {
            mParent = null;
        } else {
            throw new RuntimeException("view " + this + " being added, but" + " it already has a parent");
        }
    }

    /**
     * Gets the parent of this view. Note that the parent is a ViewParent and
     * not necessarily a View.
     * 
     * @return Parent of this view.
     */
    public ViewParent getParent() {
        return mParent;
    }

    protected void setLayout(LayoutParams layout) {
        mLayoutParams = layout;
    }

    protected void setBorder(BorderParams border) {
        this.borderParams = border;
    }

    public final View findViewById(String id) {
        View view = doFindViewById(id);
        if(view != null) {
            return view;
        }
        throw new RessourceLoadingException("Fail to find view '"+id+"' in '"+this.id+"'");
    }

    public View doFindViewById(String id) {
        if (id.equals(this.id)) {
            return this;
        }
        return null;
    }

    public BorderParams getBorderParams() {
        return borderParams;
    }

    protected void duplicateTo(View view) {
        view.setId(getId());
        view.setIdleStyle(idleStyle.duplicate());
        view.setLayout(getLayoutParams().duplicate());
        view.setBorder(getBorderParams().duplicate());
        view.getIdleStyle().apply(view);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnMouseListener(OnMouseEventListener onMouseEventListener) {
        this.onMouseEventListener = onMouseEventListener;
    }
    
    public void setOnKeyListener(OnKeyEventListener onKeyEventListener) {
        this.onKeyEventListener = onKeyEventListener;
    }
    
    public void setOnControllerListener(OnControllerEventListener onControllerEventListener) {
        this.onControllerEventListener = onControllerEventListener;
    }

    public boolean performClick(I3dMouseEvent mouseEvent) {
        if (onClickListener != null) {
            onClickListener.onClick(mouseEvent, this);
            return true;
        }
        return false;
    }

    public static interface OnClickListener {
        void onClick(I3dMouseEvent mouseEvent, View view);
    }

    public static interface OnMouseEventListener {
        boolean onMouseEvent(I3dMouseEvent mouseEvent);
    }
    
    public static interface OnKeyEventListener {
        boolean onKeyEvent(V3DKeyEvent keyEvent);
    }
    
    public static interface OnControllerEventListener {
        boolean onControllerEvent(V3DControllerEvent controllerEvent);
    }

    public void setHelp(String help) {
        this.help = help;
    }

    // public abstract boolean onMouseEvent(V3DMouseEvent mouseEvent);

    public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
        if (!mVisible || ! mEnabled) {
            return false;
        }

        if (mouseEvent.getAction() == Action.MOUSE_CLICKED) {
            if (performClick(mouseEvent)) {
                return true;
            }
        }

        if (onMouseEventListener != null) {
            return onMouseEventListener.onMouseEvent(mouseEvent);
        }

        return false;
    }
    
    public boolean onKeyEvent(V3DKeyEvent keyEvent) {
        if (!mVisible || ! mEnabled) {
            return false;
        }

        if (onKeyEventListener != null) {
            return onKeyEventListener.onKeyEvent(keyEvent);
        }

        return false;
    }
    
    public boolean onControllerEvent(V3DControllerEvent controllerEvent) {
        if (!mVisible || ! mEnabled) {
            return false;
        }

        if (onControllerEventListener != null) {
            return onControllerEventListener.onControllerEvent(controllerEvent);
        }

        return false;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }

//    public void setSelectedStyle(Style style) {
//        selectedStyle = style;
//    }
    
    public void setIdleStyle(Style style) {
        idleStyle = style;
        idleStyle.apply(this);
    }
    
    public Style getIdleStyle() {
        return idleStyle;
    }
    
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
    
//    public Style getSelectedStyle() {
//        return selectedStyle;
//    }

}
