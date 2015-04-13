package com.ankamagames.baseImpl.graphics.isometric.interpolation;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.shape.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.process.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Interpolator2D
{
    protected static Logger m_logger;
    private int m_elapsedTime;
    private float m_speed;
    private float m_delta;
    private float m_startX;
    private float m_startY;
    private float m_endX;
    private float m_endY;
    private float m_valueX;
    private float m_valueY;
    private InterpolationShape m_lazyShape;
    private InterpolationProcess m_processType;
    
    public Interpolator2D() {
        super();
        this.m_lazyShape = new RectInterpolationShape(100, 50);
        this.m_processType = InterpolationType.SINUS;
    }
    
    public void setProcessType(final InterpolationType processType) {
        this.m_processType = processType;
    }
    
    public void setDelta(final float delta) {
        this.m_delta = delta;
    }
    
    protected float getDelta() {
        return this.m_delta;
    }
    
    public void setSpeed(final float speed) {
        this.m_speed = speed;
    }
    
    public void set(final float x, final float y) {
        this.m_valueX = x;
        this.m_endX = x;
        this.m_startX = x;
        this.m_valueY = y;
        this.m_endY = y;
        this.m_startY = y;
    }
    
    public void setX(final int x) {
        final float startX = x;
        this.m_valueX = startX;
        this.m_endX = startX;
        this.m_startX = startX;
    }
    
    public void setY(final int y) {
        final float startY = y;
        this.m_valueY = startY;
        this.m_endY = startY;
        this.m_startY = startY;
    }
    
    public void setStart(final float startX, final float startY) {
        this.m_startX = startX;
        this.m_startY = startY;
        this.m_elapsedTime = 0;
    }
    
    public void setEnd(final float endX, final float endY) {
        this.m_endX = endX;
        this.m_endY = endY;
        this.m_elapsedTime = 0;
    }
    
    public float getEndX() {
        return this.m_endX;
    }
    
    public float getEndY() {
        return this.m_endY;
    }
    
    public float getValueX() {
        return this.m_valueX;
    }
    
    public float getValueY() {
        return this.m_valueY;
    }
    
    public boolean process(final int deltaTime, final float zoomFactor) {
        this.m_lazyShape.setScale(zoomFactor);
        return this.computeValue(deltaTime);
    }
    
    protected boolean computeValue(final int deltaTime) {
        final float dX = this.m_endX - this.m_valueX;
        final float dY = this.m_endY - this.m_valueY;
        final Point2 point = this.m_lazyShape.intersectLine(dX, dY);
        if (point == null) {
            this.m_startX = this.m_valueX;
            this.m_startY = this.m_valueY;
            return false;
        }
        final float oldValueX = this.m_valueX;
        final float oldValueY = this.m_valueY;
        this.m_elapsedTime += deltaTime;
        final float f = this.m_elapsedTime * this.m_speed / 1000.0f;
        if (f > 1.0f) {
            this.m_valueX = this.m_endX;
            this.m_valueY = this.m_endY;
        }
        else {
            final float endX = this.m_endX - point.getX();
            final float endY = this.m_endY - point.getY();
            this.invoke(endX, endY, f);
        }
        return !MathHelper.isEqual(oldValueX, this.m_valueX, 1.0E-4f) || !MathHelper.isEqual(oldValueY, this.m_valueY, 1.0E-4f);
    }
    
    private void invoke(final float endX, final float endY, final float f) {
        this.m_valueX = this.m_processType.invoke(this.m_startX, this.m_valueX, endX, f);
        this.m_valueY = this.m_processType.invoke(this.m_startY, this.m_valueY, endY, f);
    }
    
    static {
        Interpolator2D.m_logger = Logger.getLogger((Class)Interpolator2D.class);
    }
}
