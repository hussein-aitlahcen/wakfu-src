package com.ankamagames.xulor2.tween;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;

public class UniversalRadialMenuPositionTween2 extends AbstractWidgetTween
{
    private static Logger m_logger;
    private int m_radiusA;
    private int m_radiusB;
    private int m_radiusX;
    private int m_radiusY;
    private float m_angleA;
    private float m_angleB;
    
    public UniversalRadialMenuPositionTween2(final float angleA, final float angleB, final int radiusA, final int radiusB, final int radiusX, final int radiusY, final Widget w, final int delay, final int duration, final TweenFunction function) {
        super(null, null, w, delay, duration, function);
        this.m_radiusA = Integer.MIN_VALUE;
        this.m_radiusB = Integer.MAX_VALUE;
        this.m_angleA = Float.MIN_VALUE;
        this.m_angleB = Float.MIN_VALUE;
        this.m_radiusA = radiusA;
        this.m_radiusB = radiusB;
        this.m_radiusX = radiusX;
        this.m_radiusY = radiusY;
        this.m_angleA = angleA;
        this.m_angleB = angleB;
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function != null) {
            int radius;
            if (this.m_radiusA != Integer.MIN_VALUE) {
                radius = (int)this.m_function.compute(this.m_radiusA, this.m_radiusB, Math.min(this.m_elapsedTime, this.m_duration / 2), this.m_duration / 2);
            }
            else {
                radius = this.m_radiusB;
            }
            double angle;
            if (this.m_elapsedTime < this.m_duration / 2 && this.m_angleA != Float.MIN_VALUE) {
                angle = this.m_angleA;
            }
            else if (this.m_angleA != Float.MIN_VALUE && this.m_elapsedTime >= this.m_duration / 2) {
                angle = this.m_function.compute(this.m_angleA, this.m_angleB, 2 * (this.m_elapsedTime - this.m_duration / 2), this.m_duration);
            }
            else {
                angle = this.m_angleB;
            }
            final int x = (int)(radius * Math.cos(angle)) + this.m_radiusX;
            final int y = (int)(radius * Math.sin(angle)) + this.m_radiusY;
            this.getWidget().setPosition(x, y, true);
        }
        return true;
    }
    
    @Override
    public void onEnd() {
        final int x = (int)(this.m_radiusB * Math.cos(this.m_angleB)) + this.m_radiusX;
        final int y = (int)(this.m_radiusB * Math.sin(this.m_angleB)) + this.m_radiusY;
        this.getWidget().setPosition(x, y, true);
        super.onEnd();
    }
    
    static {
        UniversalRadialMenuPositionTween2.m_logger = Logger.getLogger((Class)UniversalRadialMenuPositionTween2.class);
    }
}
