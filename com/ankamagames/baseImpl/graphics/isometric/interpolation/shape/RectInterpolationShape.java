package com.ankamagames.baseImpl.graphics.isometric.interpolation.shape;

import com.ankamagames.framework.kernel.core.maths.*;

public class RectInterpolationShape implements InterpolationShape
{
    public int m_xMin;
    public int m_xMax;
    public int m_yMin;
    public int m_yMax;
    final Point2 m_point;
    float m_scale;
    
    public RectInterpolationShape(final int w, final int h) {
        super();
        this.m_point = new Point2();
        this.m_scale = 1.0f;
        this.m_xMin = -w;
        this.m_xMax = w;
        this.m_yMin = -h;
        this.m_yMax = h;
    }
    
    @Override
    public void setScale(final float scaleFactor) {
        this.m_scale = scaleFactor;
    }
    
    @Override
    public Point2 intersectLine(final float dX, final float dY) {
        if (Math.abs(dX) / this.m_xMax >= Math.abs(dY) / this.m_yMax) {
            final float minX = this.m_xMin * this.m_scale;
            if (dX <= minX - 0.01f) {
                final float x = minX;
                final float y = dY * x / dX;
                this.m_point.set(x, y);
                return this.m_point;
            }
            final float maxX = this.m_xMax * this.m_scale;
            if (dX >= maxX + 0.01f) {
                final float x = maxX;
                final float y = dY * x / dX;
                this.m_point.set(x, y);
                return this.m_point;
            }
        }
        else {
            final float minY = this.m_yMin * this.m_scale;
            if (dY <= minY - 0.01f) {
                final float y = minY;
                final float x = dX * y / dY;
                this.m_point.set(x, y);
                return this.m_point;
            }
            final float maxY = this.m_yMax * this.m_scale;
            if (dY >= maxY + 0.01f) {
                final float y = maxY;
                final float x = dX * y / dY;
                this.m_point.set(x, y);
                return this.m_point;
            }
        }
        return null;
    }
}
