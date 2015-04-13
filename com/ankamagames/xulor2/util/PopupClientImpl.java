package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.core.*;

public class PopupClientImpl implements PopupClient
{
    public int m_displayX;
    public int m_displayY;
    public int m_width;
    public int m_height;
    public ElementMap m_map;
    
    @Override
    public int getDisplayX() {
        return this.m_displayX;
    }
    
    @Override
    public int getDisplayY() {
        return this.m_displayY;
    }
    
    @Override
    public int getWidth() {
        return this.m_width;
    }
    
    @Override
    public int getHeight() {
        return this.m_height;
    }
    
    @Override
    public ElementMap getElementMap() {
        return this.m_map;
    }
    
    public void setDisplayX(final int displayX) {
        this.m_displayX = displayX;
    }
    
    public void setDisplayY(final int displayY) {
        this.m_displayY = displayY;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
    }
    
    public void setMap(final ElementMap map) {
        this.m_map = map;
    }
}
