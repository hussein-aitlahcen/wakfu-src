package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.engine.texture.*;
import java.awt.*;

public class TiledPixmapBackgroundMesh extends PixmapBackgroundMesh
{
    boolean m_horizontal;
    boolean m_vertical;
    
    public TiledPixmapBackgroundMesh() {
        super();
        this.m_horizontal = true;
        this.m_vertical = true;
        this.m_pixmaps = new Pixmap[1];
    }
    
    public Pixmap getPximap() {
        return this.m_pixmaps[0];
    }
    
    public void setPixmap(final Pixmap pixmap) {
        this.m_pixmaps[0] = pixmap;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
    }
    
    public void setVertical(final boolean vertical) {
        this.m_vertical = vertical;
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        final int left = margin.left + border.left;
        final int right = margin.right + border.right;
        final int top = margin.top + border.top;
        final int bottom = margin.bottom + border.bottom;
        final int width = size.width - left - right;
        final int height = size.height - top - bottom;
        this.m_entity.clear();
        if (this.m_pixmaps[0] != null) {
            final int pixmapWidth = this.m_horizontal ? this.m_pixmaps[0].getWidth() : width;
            final int pixmapHeight = this.m_vertical ? this.m_pixmaps[0].getHeight() : height;
            final float hRepeat = width / pixmapWidth;
            final float vRepeat = height / pixmapHeight;
            final int columns = this.m_horizontal ? (width / pixmapWidth + ((width % pixmapWidth > 0) ? 1 : 0)) : 1;
            final int rows = this.m_vertical ? (height / pixmapHeight + ((height % pixmapHeight > 0) ? 1 : 0)) : 1;
            final int startX = left;
            int y;
            final int startY = y = size.height - top;
            this.beginAddGeometry(rows * columns);
            for (int row = 0; row < rows; ++row) {
                int x = startX;
                final float vRatio = (row == rows - 1) ? (vRepeat - row) : 1.0f;
                for (int column = 0; column < columns; ++column) {
                    final float hRatio = (column == columns - 1) ? (hRepeat - column) : 1.0f;
                    this.addGeometry(x, y, (int)(pixmapWidth * hRatio), (int)(pixmapHeight * vRatio), hRatio, vRatio, this.m_pixmaps[0]);
                    x += pixmapWidth;
                }
                y -= pixmapHeight;
            }
            this.endAddGeometry();
        }
    }
    
    protected void addGeometry(final int left, final int top, final int width, final int height, final float hratio, final float vratio, final Pixmap pixmap) {
        if (width == 0 || height == 0 || pixmap == null || pixmap.getTexture() == null) {
            return;
        }
        final float pWidth = pixmap.getRight() - pixmap.getLeft();
        final float pHeight = pixmap.getBottom() - pixmap.getTop();
        final float right = pixmap.getLeft() + pWidth * (pixmap.getRotation().isAffectWidthAndHeight() ? vratio : hratio);
        final float bottom = pixmap.getTop() + pHeight * (pixmap.getRotation().isAffectWidthAndHeight() ? hratio : vratio);
        this.addGeometry(left, top, width, height, pixmap, pixmap.getTop(), pixmap.getLeft(), bottom, right);
    }
}
