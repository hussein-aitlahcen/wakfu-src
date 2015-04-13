package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.engine.texture.*;
import java.awt.*;

public class PixmapBorderMesh extends AbstractPixmapBorderMesh
{
    public PixmapBorderMesh() {
        super();
        this.m_pixmaps = new Pixmap[8];
    }
    
    @Override
    public void setInsets(final Insets insets) {
    }
    
    public Pixmap getEast() {
        return this.m_pixmaps[4];
    }
    
    public void setEast(final Pixmap east) {
        this.m_pixmaps[4] = east;
        this.updatePixmaps();
    }
    
    public Pixmap getNorth() {
        return this.m_pixmaps[1];
    }
    
    public void setNorth(final Pixmap north) {
        this.m_pixmaps[1] = north;
        this.updatePixmaps();
    }
    
    public Pixmap getNorthEast() {
        return this.m_pixmaps[2];
    }
    
    public void setNorthEast(final Pixmap northEast) {
        this.m_pixmaps[2] = northEast;
        this.updatePixmaps();
    }
    
    public Pixmap getNorthWest() {
        return this.m_pixmaps[0];
    }
    
    public void setNorthWest(final Pixmap northWest) {
        this.m_pixmaps[0] = northWest;
        this.updatePixmaps();
    }
    
    public Pixmap getSouth() {
        return this.m_pixmaps[6];
    }
    
    public void setSouth(final Pixmap south) {
        this.m_pixmaps[6] = south;
        this.updatePixmaps();
    }
    
    public Pixmap getSouthEast() {
        return this.m_pixmaps[7];
    }
    
    public void setSouthEast(final Pixmap southEast) {
        this.m_pixmaps[7] = southEast;
        this.updatePixmaps();
    }
    
    public Pixmap getSouthWest() {
        return this.m_pixmaps[5];
    }
    
    public void setSouthWest(final Pixmap southWest) {
        this.m_pixmaps[5] = southWest;
        this.updatePixmaps();
    }
    
    public Pixmap getWest() {
        return this.m_pixmaps[3];
    }
    
    public void setWest(final Pixmap west) {
        this.m_pixmaps[3] = west;
        this.updatePixmaps();
    }
    
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
        this.m_pixmaps[0] = northWest;
        this.m_pixmaps[1] = north;
        this.m_pixmaps[2] = northEast;
        this.m_pixmaps[3] = west;
        this.m_pixmaps[4] = east;
        this.m_pixmaps[5] = southWest;
        this.m_pixmaps[6] = south;
        this.m_pixmaps[7] = southEast;
        this.updatePixmaps();
    }
    
    public boolean isPixmapInitialized() {
        return this.m_pixmapsInitialized;
    }
    
    public void updateInsets(final Insets border) {
        if (border != null) {
            border.top = Math.max(this.m_pixmaps[0].getHeight(), Math.max(this.m_pixmaps[1].getHeight(), this.m_pixmaps[2].getHeight()));
            border.bottom = Math.max(this.m_pixmaps[5].getHeight(), Math.max(this.m_pixmaps[6].getHeight(), this.m_pixmaps[7].getHeight()));
            border.left = Math.max(this.m_pixmaps[0].getWidth(), Math.max(this.m_pixmaps[3].getWidth(), this.m_pixmaps[5].getWidth()));
            border.right = Math.max(this.m_pixmaps[2].getWidth(), Math.max(this.m_pixmaps[4].getWidth(), this.m_pixmaps[7].getWidth()));
        }
    }
    
    private void updatePixmaps() {
        for (int i = this.m_pixmaps.length - 1; i >= 0; --i) {
            if (this.m_pixmaps[i] == null) {
                this.m_pixmapsInitialized = false;
                return;
            }
        }
        this.m_pixmapsInitialized = true;
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        final int left = margin.left + border.left;
        final int right = margin.right + border.right;
        final int top = margin.top + border.top;
        final int bottom = margin.bottom + border.bottom;
        this.m_entity.clear();
        final int[] width = new int[3];
        final int[] height = new int[3];
        final int startX = margin.left;
        final int startY = size.height - margin.top;
        width[0] = border.left;
        width[1] = size.width - left - right;
        width[2] = border.right;
        height[0] = border.top;
        height[1] = size.height - top - bottom;
        height[2] = border.bottom;
        int y = startY;
        int pixmapIndex = 0;
        this.beginAddGeometry(9);
        for (int row = 0; row < 3; ++row) {
            int x = startX;
            for (int column = 0; column < 3; ++column) {
                if (column != 1 || row != 1) {
                    this.addGeometry(x, y, width[column], height[row], this.m_pixmaps[pixmapIndex]);
                    ++pixmapIndex;
                }
                x += width[column];
            }
            y -= height[row];
        }
        this.endAddGeometry();
    }
}
