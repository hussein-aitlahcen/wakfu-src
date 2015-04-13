package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.engine.texture.*;
import java.awt.*;

public class PixmapBackgroundMesh extends AbstractPixmapBackgroundMesh
{
    private boolean m_scaled;
    
    public PixmapBackgroundMesh() {
        super();
        this.m_scaled = false;
        this.m_pixmaps = new Pixmap[9];
    }
    
    public boolean isScaled() {
        return this.m_scaled;
    }
    
    public void setScaled(final boolean scaled) {
        this.m_scaled = scaled;
    }
    
    public Pixmap getEast() {
        return this.m_pixmaps[5];
    }
    
    public void setEast(final Pixmap east) {
        this.m_pixmaps[5] = east;
    }
    
    public Pixmap getNorth() {
        return this.m_pixmaps[1];
    }
    
    public void setNorth(final Pixmap north) {
        this.m_pixmaps[1] = north;
    }
    
    public Pixmap getNorthEast() {
        return this.m_pixmaps[2];
    }
    
    public void setNorthEast(final Pixmap northEast) {
        this.m_pixmaps[2] = northEast;
    }
    
    public Pixmap getNorthWest() {
        return this.m_pixmaps[0];
    }
    
    public void setNorthWest(final Pixmap northWest) {
        this.m_pixmaps[0] = northWest;
    }
    
    public Pixmap getSouth() {
        return this.m_pixmaps[7];
    }
    
    public void setSouth(final Pixmap south) {
        this.m_pixmaps[7] = south;
    }
    
    public Pixmap getSouthEast() {
        return this.m_pixmaps[8];
    }
    
    public void setSouthEast(final Pixmap southEast) {
        this.m_pixmaps[8] = southEast;
    }
    
    public Pixmap getSouthWest() {
        return this.m_pixmaps[6];
    }
    
    public void setSouthWest(final Pixmap southWest) {
        this.m_pixmaps[6] = southWest;
    }
    
    public Pixmap getWest() {
        return this.m_pixmaps[3];
    }
    
    public void setWest(final Pixmap west) {
        this.m_pixmaps[3] = west;
    }
    
    public Pixmap getCenter() {
        return this.m_pixmaps[4];
    }
    
    public void setCenter(final Pixmap center) {
        this.m_pixmaps[4] = center;
    }
    
    public void setPixmaps(final Pixmap northWest, final Pixmap north, final Pixmap northEast, final Pixmap west, final Pixmap center, final Pixmap east, final Pixmap southWest, final Pixmap south, final Pixmap southEast) {
        this.m_pixmaps[0] = northWest;
        this.m_pixmaps[1] = north;
        this.m_pixmaps[2] = northEast;
        this.m_pixmaps[3] = west;
        this.m_pixmaps[4] = center;
        this.m_pixmaps[5] = east;
        this.m_pixmaps[6] = southWest;
        this.m_pixmaps[7] = south;
        this.m_pixmaps[8] = southEast;
    }
    
    public void setPixmaps(final Pixmap center) {
        this.m_pixmaps[4] = center;
    }
    
    public void setPixmaps(final Pixmap[] pixmap) {
        this.m_pixmaps[0] = pixmap[0];
        this.m_pixmaps[1] = pixmap[1];
        this.m_pixmaps[2] = pixmap[2];
        this.m_pixmaps[3] = pixmap[3];
        this.m_pixmaps[4] = pixmap[4];
        this.m_pixmaps[5] = pixmap[5];
        this.m_pixmaps[6] = pixmap[6];
        this.m_pixmaps[7] = pixmap[7];
        this.m_pixmaps[8] = pixmap[8];
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        final int left = margin.left + border.left;
        final int right = margin.right + border.right;
        final int top = margin.top + border.top;
        final int bottom = margin.bottom + border.bottom;
        this.m_entity.clear();
        if (this.m_pixmaps[0] == null) {
            this.updateCenterGeometry(size, left, right, top, bottom);
            return;
        }
        final int[] width = new int[3];
        final int[] height = new int[3];
        final int[] x = new int[3];
        final int[] y = new int[3];
        final int startX = margin.left;
        final int startY = size.height - margin.top;
        width[0] = this.m_pixmaps[0].getWidth();
        width[2] = this.m_pixmaps[2].getWidth();
        width[1] = size.width - (width[0] + width[2] + margin.left + margin.right);
        height[0] = this.m_pixmaps[0].getHeight();
        height[2] = this.m_pixmaps[6].getHeight();
        height[1] = size.height - (height[0] + height[2] + margin.top + margin.bottom);
        x[0] = startX;
        x[1] = x[0] + width[0];
        x[2] = x[1] + width[1];
        y[0] = startY;
        y[1] = y[0] - height[0];
        y[2] = y[1] - height[1];
        this.beginAddGeometry(9);
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 3; ++column) {
                if (row == 1 && column == 1 && !this.m_scaled) {
                    final Pixmap centerPixmap = this.m_pixmaps[row * 3 + column];
                    final int centerX = startX + (size.width - (margin.left + margin.right) - centerPixmap.getWidth()) / 2;
                    final int centerY = startY - (size.height - (margin.top + margin.bottom) + centerPixmap.getHeight()) / 2;
                    this.addGeometry(centerX, centerY, centerPixmap.getWidth(), centerPixmap.getHeight(), centerPixmap);
                }
                else {
                    this.addGeometry(x[column], y[row], width[column], height[row], this.m_pixmaps[row * 3 + column]);
                }
            }
        }
        this.endAddGeometry();
    }
    
    private void updateCenterGeometry(final Dimension size, final int left, final int right, final int top, final int bottom) {
        if (this.m_scaled) {
            final int width = size.width - right - left;
            final int height = size.height - top - bottom;
            this.beginAddGeometry(1);
            this.addGeometry(left, size.height - top, width, height, this.m_pixmaps[4]);
            this.endAddGeometry();
        }
        else if (this.m_pixmaps[4] != null) {
            final int width = this.m_pixmaps[4].getWidth();
            final int height = this.m_pixmaps[4].getHeight();
            final int halfHorizontalSpacing = (size.width - right - left - width) / 2;
            final int halfVerticalSpacing = (size.height - top - bottom - height) / 2;
            this.beginAddGeometry(1);
            this.addGeometry(left + halfHorizontalSpacing, size.height - top - halfVerticalSpacing, width, height, this.m_pixmaps[4]);
            this.endAddGeometry();
        }
    }
}
