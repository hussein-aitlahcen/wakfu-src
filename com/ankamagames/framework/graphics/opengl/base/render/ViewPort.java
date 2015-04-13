package com.ankamagames.framework.graphics.opengl.base.render;

public class ViewPort
{
    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    
    public ViewPort(final int x, final int y, final int width, final int height) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
    }
    
    public final int getX() {
        return this.m_x;
    }
    
    public final void setX(final int x) {
        this.m_x = x;
    }
    
    public final int getY() {
        return this.m_y;
    }
    
    public final void setY(final int y) {
        this.m_y = y;
    }
    
    public final int getWidth() {
        return this.m_width;
    }
    
    public final void setWidth(final int width) {
        this.m_width = width;
    }
    
    public final int getHeight() {
        return this.m_height;
    }
    
    public final void setHeight(final int height) {
        this.m_height = height;
    }
    
    public final float getHalfResX() {
        return (this.getWidth() - this.getX()) * 0.5f;
    }
    
    public final float getHalfResY() {
        return (this.getHeight() - this.getY()) * 0.5f;
    }
    
    public final ViewPort copy() {
        return new ViewPort(this.m_x, this.m_y, this.m_width, this.m_height);
    }
    
    public final void set(final int x, final int y, final int width, final int height) {
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
    }
    
    public final boolean equals(final int x, final int y, final int width, final int height) {
        return this.m_x == x && this.m_y == y && this.m_width == width && this.m_height == height;
    }
}
