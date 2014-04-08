package com.spaceagencies.i3d.view;

import com.spaceagencies.i3d.Color;
import com.spaceagencies.i3d.Graphics;
import com.spaceagencies.i3d.I3dVec2;
import com.spaceagencies.i3d.input.I3dMouseEvent;

public class Rect extends View {

    private I3dVec2 size = new I3dVec2(10,10);
    private Color backgroundColor;

    public Rect() {
        backgroundColor = Color.pink;
    }

    @Override
    public void onDraw(Graphics g) {
        g.setColor(backgroundColor);
        g.drawFilledRectangle(0, 0, size.getWidth(), size.getHeight());
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    public void setSize(I3dVec2 size) {
        this.size = size;
    }
    
    @Override
    public View duplicate() {
        Rect view = new Rect();
        view.setBackgroundColor(backgroundColor);
        view.setSize(size);
        view.setLayout(getLayoutParams());
        view.setBorder(getBorderParams().duplicate());
        return view;
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
    }

    @Override
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
    }
}
