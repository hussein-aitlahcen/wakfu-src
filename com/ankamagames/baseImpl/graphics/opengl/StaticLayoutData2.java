package com.ankamagames.baseImpl.graphics.opengl;

public class StaticLayoutData2 extends StaticLayoutData
{
    private int m_x;
    private int m_y;
    private int m_height;
    private int m_width;
    private boolean m_relative;
    
    public StaticLayoutData2(final float widthPercentage, final float heightPercentage, final byte align, final int x, final int y, final int width, final int height) {
        super(widthPercentage, heightPercentage, align);
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
        this.m_relative = true;
    }
    
    public StaticLayoutData2(final int x, final int y, final int width, final int height) {
        super(0.0f, 0.0f, (byte)1);
        this.m_height = height;
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    @Override
    public int getHeight(final int parentHeight, final int prefHeight) {
        if (this.m_relative) {
            return super.getHeight(parentHeight, prefHeight);
        }
        return this.m_height;
    }
    
    @Override
    public int getPositionX(final int width, final int parentWidth) {
        if (this.m_relative) {
            return super.getPositionX(width, parentWidth);
        }
        return this.m_x;
    }
    
    @Override
    public int getPositionY(final int height, final int parentHeight) {
        if (this.m_relative) {
            return super.getPositionY(height, parentHeight);
        }
        return this.m_y;
    }
    
    @Override
    public int getWidth(final int parentWidth, final int prefWidth) {
        if (this.m_relative) {
            return super.getWidth(parentWidth, prefWidth);
        }
        return this.m_width;
    }
    
    public void setRelative(final boolean relative) {
        this.m_relative = relative;
    }
}
