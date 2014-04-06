package com.spaceagencies.i3d.view;

import com.spaceagencies.common.tools.Log;
import com.spaceagencies.i3d.Measure;
import com.spaceagencies.i3d.MeasurePoint;
import com.spaceagencies.i3d.Measure.Axis;

public class LayoutParams {

    // Output
    boolean layouted;
    
    //float width;
    //float height;
    //public Point offset;
    
    public float mLeft;
    public float mRight;
    public float mTop;
    public float mBottom;

    
    public float mExtraLeft;
    public float mExtraRight;
    public float mExtraTop;
    public float mExtraBottom;

    public float mLeftPadding;
    public float mRightPadding;
    public float mTopPadding;
    public float mBottomPadding;
    
    public float mLeftMargin;
    public float mRightMargin;
    public float mTopMargin;
    public float mBottomMargin;
    
    public float mTotalWidth;
    public float mBorderWidth;
    public float mContentWidth;
    public float mTotalHeight;
    public float mBorderHeight;
    public float mContentHeight;
    
    
    public float mComputedLeft;
    public float mComputedRight;
    public float mComputedTop;
    public float mComputedBottom;
    public float mMeasuredContentWidth;
    public float mMeasuredContentHeight;

    // Input
    private LayoutMeasure layoutWidthMeasure;
    private LayoutMeasure layoutHeightMeasure;
    private LayoutGravity layoutGravityX;
    private LayoutGravity layoutGravityY;
    private MeasurePoint measurePoint;

    private Measure layoutMarginLeftMeasure;
    private Measure layoutMarginRightMeasure;
    private Measure layoutMarginTopMeasure;
    private Measure layoutMarginBottomMeasure;

    private Measure layoutPaddingLeftMeasure;
    private Measure layoutPaddingRightMeasure;
    private Measure layoutPaddingTopMeasure;
    private Measure layoutPaddingBottomMeasure;

    private float relativeHorizontalRatio;
    private float relativeVerticalRatio;

    
    public enum LayoutMeasure {
        MATCH_PARENT, WRAP_CONTENT, FIXED,
    }

    public enum LayoutGravity {
        CENTER, LEFT, TOP, BOTTOM, RIGHT
    }

    public LayoutParams() {

        measurePoint = new MeasurePoint();

        layoutWidthMeasure = LayoutMeasure.WRAP_CONTENT;
        layoutHeightMeasure = LayoutMeasure.WRAP_CONTENT;
        layoutGravityX = LayoutGravity.LEFT;
        layoutGravityY = LayoutGravity.TOP;

        // Output
//        this.width = -1;
//        this.height = -1;
//        widthDefined = false;
//        heightDefined = false;
//        offset = new Point();
        mLeft = 0;
        mTop = 0;
        mRight = 0;
        mBottom = 0;
        mComputedLeft = 0;
        mComputedTop = 0;
        mComputedRight = 0;
        mComputedBottom = 0;
        mMeasuredContentHeight = 0;
        mMeasuredContentWidth = 0;
        mLeftPadding = 0;
        mRightPadding = 0;
        mTopPadding = 0;
        mBottomPadding = 0;
        relativeHorizontalRatio = 1;
        relativeVerticalRatio = 1;
        layoutMarginTopMeasure = new Measure(0, false, Axis.VERTICAL);
        layoutMarginBottomMeasure = new Measure(0, false, Axis.VERTICAL);
        layoutMarginRightMeasure = new Measure(0, false, Axis.HORIZONTAL);
        layoutMarginLeftMeasure = new Measure(0, false, Axis.HORIZONTAL);
        
        layoutPaddingTopMeasure = new Measure(0, false, Axis.VERTICAL);
        layoutPaddingBottomMeasure = new Measure(0, false, Axis.VERTICAL);
        layoutPaddingRightMeasure = new Measure(0, false, Axis.HORIZONTAL);
        layoutPaddingLeftMeasure = new Measure(0, false, Axis.HORIZONTAL);
    }

//
//    public void setWidthDefined(boolean widthDefined) {
//        this.widthDefined = widthDefined;
//    }
//
    public boolean isLayouted() {
        return layouted;
    }
//
//    public void setHeightDefined(boolean heightDefined) {
//        this.heightDefined = heightDefined;
//    }

    
    protected boolean setFrame(float left, float top, float right, float bottom) {
        boolean changed = false;

        if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
            changed = true;
            mLeft = left;
            mTop = top;
            mRight = right;
            mBottom = bottom;
        }
        
        mTotalWidth = mRight - mLeft;
        mTotalHeight = mBottom - mTop;
        
        mTopMargin = computeMesure(layoutMarginTopMeasure);
        mBottomMargin = computeMesure(layoutMarginBottomMeasure);
        mLeftMargin = computeMesure(layoutMarginLeftMeasure);
        mRightMargin = computeMesure(layoutMarginRightMeasure);
        
        mBorderWidth = mTotalWidth - mLeftMargin - mRightMargin;
        mBorderHeight = mTotalHeight - mTopMargin - mBottomMargin;
        
        mTopPadding  = computeMesure(layoutPaddingTopMeasure);
        mBottomPadding  = computeMesure(layoutPaddingBottomMeasure);
        mLeftPadding  = computeMesure(layoutPaddingLeftMeasure);
        mRightPadding  = computeMesure(layoutPaddingRightMeasure);
        
        mContentWidth = mBorderWidth - mLeftPadding - mRightPadding;
        mContentHeight = mBorderHeight - mTopPadding - mBottomPadding;
        
        layouted = true;
        return changed;
    }
    
    
    protected boolean setExtrasFrame(float left, float top, float right, float bottom) {
        boolean changed = false;

        if (mExtraLeft != left || mExtraRight != right || mExtraTop != top || mExtraBottom != bottom) {
            changed = true;
            mExtraLeft = left;
            mExtraTop = top;
            mExtraRight = right;
            mExtraBottom = bottom;
        }
        
        return changed;
    }
    
    
    public float getTotalWidth() {
        return mTotalWidth;
    }
    
    public float getBorderWidth() {
        return mBorderWidth;
    }
    
    public float getContentWidth() {
        return mContentWidth;
    }
    
//    public void setWidth(int width) {
//        this.width = width;
//    }

    public float getTotalHeight() {
        return mTotalHeight;
    }
    
    public float getBorderHeight() {
        return mBorderHeight;
    }
    
    public float getContentHeight() {
        return mContentHeight;
    }
    

//    public void setHeight(float height) {
//        this.height = height;
//    }

    public Point getOffset() {
        return new Point(mLeft, mTop);
    }

//    public void setOffsetX(float x) {
//        offset.x = x;
//    }
//
//    public void setOffsetY(float y) {
//        offset.y = y;
//    }

    public LayoutMeasure getLayoutWidthMeasure() {
        return layoutWidthMeasure;
    }

    public void setLayoutWidthMeasure(LayoutMeasure layoutWidthMeasure) {
        this.layoutWidthMeasure = layoutWidthMeasure;
    }

    public LayoutMeasure getLayoutHeightMeasure() {
        return layoutHeightMeasure;
    }

    public void setLayoutHeightMeasure(LayoutMeasure layoutHeightMeasure) {
        this.layoutHeightMeasure = layoutHeightMeasure;
    }

    public LayoutGravity getLayoutGravityX() {
        return layoutGravityX;
    }

    public void setLayoutGravityX(LayoutGravity layoutAlignX) {
        this.layoutGravityX = layoutAlignX;
    }

    public LayoutGravity getLayoutGravityY() {
        return layoutGravityY;
    }

    public void setLayoutGravityY(LayoutGravity layoutAlignY) {
        this.layoutGravityY = layoutAlignY;
    }

    public void setWidthMeasure(Measure measure) {
        this.measurePoint.setX(measure);
    }

    public void setHeightMeasure(Measure measure) {
        this.measurePoint.setY(measure);
    }
    
    public void setMarginLeftMeasure(Measure measure) {
        this.layoutMarginLeftMeasure = measure;
    }
    
    public void setMarginRightMeasure(Measure measure) {
        this.layoutMarginRightMeasure = measure;
    }
    
    
    public void setMarginTopMeasure(Measure measure) {
        this.layoutMarginTopMeasure = measure;
    }
    
    public void setMarginBottomMeasure(Measure measure) {
        this.layoutMarginBottomMeasure = measure;
    }
    
    public void setPaddingLeftMeasure(Measure measure) {
        this.layoutPaddingLeftMeasure = measure;
    }
    
    public void setPaddingRightMeasure(Measure measure) {
        this.layoutPaddingRightMeasure = measure;
    }
    
    public void setPaddingTopMeasure(Measure measure) {
        this.layoutPaddingTopMeasure = measure;
    }
    
    public void setPaddingBottomMeasure(Measure measure) {
        this.layoutPaddingBottomMeasure = measure;
    }

    public LayoutParams duplicate() {
        LayoutParams layout = new LayoutParams();
        layout.layoutWidthMeasure = layoutWidthMeasure;
        layout.layoutHeightMeasure = layoutHeightMeasure;
        layout.layoutGravityX = layoutGravityX;
        layout.layoutGravityY = layoutGravityY;
        layout.measurePoint = new MeasurePoint(measurePoint);
        layout.layoutMarginTopMeasure = new Measure(layoutMarginTopMeasure);
        layout.layoutMarginBottomMeasure= new Measure(layoutMarginBottomMeasure);
        layout.layoutMarginLeftMeasure = new Measure(layoutMarginLeftMeasure);
        layout.layoutMarginRightMeasure = new Measure(layoutMarginRightMeasure);
        layout.layoutPaddingLeftMeasure = new Measure(layoutPaddingLeftMeasure);
        layout.layoutPaddingRightMeasure = new Measure(layoutPaddingRightMeasure);
        layout.layoutPaddingTopMeasure = new Measure(layoutPaddingTopMeasure);
        layout.layoutPaddingBottomMeasure = new Measure(layoutPaddingBottomMeasure);
        layout.relativeHorizontalRatio = relativeHorizontalRatio;
        layout.relativeVerticalRatio = relativeVerticalRatio;
        return layout;
    }
    
    public MeasurePoint getMeasurePoint() {
        return measurePoint;
    }
    
    
    public float computeMesure(Measure mesure) {
    	float value = 0;
        if(mesure.isRelative()) {
            if(isLayouted()) {
                if(mesure.getAxis() == Axis.HORIZONTAL) {
                    value = relativeHorizontalRatio * getTotalWidth() * mesure.getValue() / 100;
                } else {
                    value = relativeVerticalRatio * getTotalWidth() * mesure.getValue() / 100;
                }
            } else {
                Log.error("Relative width in undefined width parent");
                value =  0;
            }
        } else {
            value = mesure.getValue();
        }
        
        return value;
    }

    public void computeFrame(LayoutParams parentLayout) {
        
        float width = 0;
        float height = 0;
        
        if(getLayoutWidthMeasure() == LayoutMeasure.MATCH_PARENT) {
              width = parentLayout.getContentWidth();
          } else if (getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
              width = parentLayout.computeMesure(getMeasurePoint().getX());
              width += parentLayout.computeMesure(getLayoutMarginLeft());
              width += parentLayout.computeMesure(getLayoutMarginRight());
              
//              width += parentLayout.computeMesure(getLayoutPaddingLeft());
//              width += parentLayout.computeMesure(getLayoutPaddingRight());
          } else if (getLayoutWidthMeasure() == LayoutMeasure.WRAP_CONTENT) {
              width = mMeasuredContentWidth;
          } else {
              throw new RuntimeException("Not implemented");
          }
          
          if(getLayoutHeightMeasure() == LayoutMeasure.MATCH_PARENT) {
              height = parentLayout.getContentHeight();
          } else if (getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
              height = parentLayout.computeMesure(getMeasurePoint().getY());
              height += parentLayout.computeMesure(getLayoutMarginTop());
              height += parentLayout.computeMesure(getLayoutMarginBottom());
              
//              height += parentLayout.computeMesure(getLayoutPaddingTop());
//              height += parentLayout.computeMesure(getLayoutPaddingBottom());
          } else if (getLayoutHeightMeasure() == LayoutMeasure.WRAP_CONTENT) {
              height = mMeasuredContentHeight;
          } else {
              throw new RuntimeException("Not implemented");
          }
          
          if(getLayoutGravityX() == LayoutGravity.CENTER) {
              mComputedLeft = parentLayout.getContentWidth() /2 - width /2;
          } else if (getLayoutGravityX() == LayoutGravity.LEFT) {
              mComputedLeft = 0;
          } else if (getLayoutGravityX() == LayoutGravity.RIGHT) {
              mComputedLeft = parentLayout.getContentWidth() - width;
          }
    
          if(getLayoutGravityY() == LayoutGravity.CENTER) {
              mComputedTop = parentLayout.getContentHeight()/2 - height /2;
          }else if (getLayoutGravityY() == LayoutGravity.TOP) {
              mComputedTop = 0;
          } else if (getLayoutGravityY() == LayoutGravity.BOTTOM) {
              mComputedTop = parentLayout.getContentHeight() - height;
          }
          
          mComputedRight = mComputedLeft + width;
          mComputedBottom = mComputedTop + height;
    }

    
    public Measure getLayoutMarginBottom() {
        return layoutMarginBottomMeasure;
    }
    
    public Measure getLayoutMarginLeft() {
        return layoutMarginLeftMeasure;
    }
    
    public Measure getLayoutMarginRight() {
        return layoutMarginRightMeasure;
    }
    
    public Measure getLayoutMarginTop() {
        return layoutMarginTopMeasure;
    }
    
    public Measure getLayoutPaddingBottom() {
        return layoutPaddingBottomMeasure;
    }
    
    public Measure getLayoutPaddingLeft() {
        return layoutPaddingLeftMeasure;
    }
    
    public Measure getLayoutPaddingRight() {
        return layoutPaddingRightMeasure;
    }
    
    public Measure getLayoutPaddingTop() {
        return layoutPaddingTopMeasure;
    }

    public void setRelativeHorizontalRatio(float relativeHorizontalRatio) {
        this.relativeHorizontalRatio = relativeHorizontalRatio;
    }
    
    public void setRelativeVerticalRatio(float relativeVerticalRatio) {
        this.relativeVerticalRatio = relativeVerticalRatio;
    }
    
}
