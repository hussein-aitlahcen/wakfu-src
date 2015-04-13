package com.ankamagames.xulor2.decorator.mesh;

import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;

public class SwitchingPlainBackgroundMesh extends PlainBackgroundMesh
{
    private final ArrayList<Color> m_colors;
    private final float[] m_currentColor;
    private int m_elapsedTime;
    private int m_duration;
    private final TweenFunction m_function;
    
    public SwitchingPlainBackgroundMesh() {
        super();
        this.m_colors = new ArrayList<Color>();
        this.m_currentColor = new float[4];
        this.m_function = TweenFunction.PROGRESSIVE;
    }
    
    public void addColor(final Color c) {
        this.m_colors.add(c);
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public void process(final int deltaTime) {
        if (this.m_colors.size() < 2) {
            return;
        }
        this.m_elapsedTime += deltaTime;
        if (this.m_elapsedTime >= this.m_duration) {
            this.m_elapsedTime = 0;
            this.m_colors.add(this.m_colors.remove(0));
        }
        final Color c1 = this.m_colors.get(0);
        final Color c2 = this.m_colors.get(1);
        this.m_currentColor[0] = this.m_function.compute(c1.getRed(), c2.getRed(), this.m_elapsedTime, this.m_duration);
        this.m_currentColor[1] = this.m_function.compute(c1.getGreen(), c2.getGreen(), this.m_elapsedTime, this.m_duration);
        this.m_currentColor[2] = this.m_function.compute(c1.getBlue(), c2.getBlue(), this.m_elapsedTime, this.m_duration);
        this.m_currentColor[3] = this.m_function.compute(c1.getAlpha(), c2.getAlpha(), this.m_elapsedTime, this.m_duration);
        this.setColor(this.m_currentColor);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_colors.clear();
    }
}
