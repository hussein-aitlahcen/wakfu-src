package com.ankamagames.xulor2.util;

import com.ankamagames.framework.graphics.engine.texture.*;

public class ImageData
{
    private Pixmap m_pixmap;
    private int m_x;
    private int m_y;
    
    public ImageData(final Pixmap pixmap, final int x, final int y) {
        super();
        this.m_pixmap = pixmap;
        this.m_x = x;
        this.m_y = y;
    }
    
    public Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public int getWidth() {
        return this.m_pixmap.getWidth();
    }
    
    public int getHeight() {
        return this.m_pixmap.getHeight();
    }
}
