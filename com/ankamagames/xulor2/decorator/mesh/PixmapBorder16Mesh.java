package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.awt.*;

public class PixmapBorder16Mesh extends AbstractPixmapBorderMesh
{
    private Pixmap[] m_pixmaps;
    private boolean m_pixmapsInitialized;
    
    public PixmapBorder16Mesh() {
        super();
        this.m_pixmaps = new Pixmap[16];
    }
    
    public void setPixmap(final Pixmap p, final Alignment17 align) {
        switch (align) {
            case NORTH_WEST: {
                this.m_pixmaps[0] = p;
                break;
            }
            case NORTH_NORTH_WEST: {
                this.m_pixmaps[1] = p;
                break;
            }
            case NORTH: {
                this.m_pixmaps[2] = p;
                break;
            }
            case NORTH_NORTH_EAST: {
                this.m_pixmaps[3] = p;
                break;
            }
            case NORTH_EAST: {
                this.m_pixmaps[4] = p;
                break;
            }
            case WEST_NORTH_WEST: {
                this.m_pixmaps[5] = p;
                break;
            }
            case EAST_NORTH_EAST: {
                this.m_pixmaps[6] = p;
                break;
            }
            case WEST: {
                this.m_pixmaps[7] = p;
                break;
            }
            case EAST: {
                this.m_pixmaps[8] = p;
                break;
            }
            case WEST_SOUTH_WEST: {
                this.m_pixmaps[9] = p;
                break;
            }
            case EAST_SOUTH_EAST: {
                this.m_pixmaps[10] = p;
                break;
            }
            case SOUTH_WEST: {
                this.m_pixmaps[11] = p;
                break;
            }
            case SOUTH_SOUTH_WEST: {
                this.m_pixmaps[12] = p;
                break;
            }
            case SOUTH: {
                this.m_pixmaps[13] = p;
                break;
            }
            case SOUTH_SOUTH_EAST: {
                this.m_pixmaps[14] = p;
                break;
            }
            case SOUTH_EAST: {
                this.m_pixmaps[15] = p;
                break;
            }
        }
        this.updatePixmaps();
    }
    
    public void setPixmaps(final Pixmap northWest, final Pixmap northNorthWest, final Pixmap north, final Pixmap northNorthEast, final Pixmap northEast, final Pixmap northWestWest, final Pixmap northEastEast, final Pixmap west, final Pixmap east, final Pixmap southWestWest, final Pixmap southEastEast, final Pixmap southWest, final Pixmap southSouthWest, final Pixmap south, final Pixmap southSouthEast, final Pixmap southEast) {
        this.m_pixmaps[0] = northWest;
        this.m_pixmaps[1] = northNorthWest;
        this.m_pixmaps[2] = north;
        this.m_pixmaps[3] = northNorthEast;
        this.m_pixmaps[4] = northEast;
        this.m_pixmaps[5] = northWestWest;
        this.m_pixmaps[6] = northEastEast;
        this.m_pixmaps[7] = west;
        this.m_pixmaps[8] = east;
        this.m_pixmaps[9] = southWestWest;
        this.m_pixmaps[10] = southEastEast;
        this.m_pixmaps[11] = southWest;
        this.m_pixmaps[12] = southSouthWest;
        this.m_pixmaps[13] = south;
        this.m_pixmaps[14] = southSouthEast;
        this.m_pixmaps[15] = southEast;
        this.updatePixmaps();
    }
    
    public void setPixmaps(final Pixmap[] pixmaps) {
        this.m_pixmaps[0] = pixmaps[0];
        this.m_pixmaps[1] = pixmaps[1];
        this.m_pixmaps[2] = pixmaps[2];
        this.m_pixmaps[3] = pixmaps[3];
        this.m_pixmaps[4] = pixmaps[4];
        this.m_pixmaps[5] = pixmaps[5];
        this.m_pixmaps[6] = pixmaps[6];
        this.m_pixmaps[7] = pixmaps[7];
        this.m_pixmaps[8] = pixmaps[8];
        this.m_pixmaps[9] = pixmaps[9];
        this.m_pixmaps[10] = pixmaps[10];
        this.m_pixmaps[11] = pixmaps[11];
        this.m_pixmaps[12] = pixmaps[12];
        this.m_pixmaps[13] = pixmaps[13];
        this.m_pixmaps[14] = pixmaps[14];
        this.m_pixmaps[15] = pixmaps[15];
        this.updatePixmaps();
    }
    
    public final boolean isPixmapInitialized() {
        return this.m_pixmapsInitialized;
    }
    
    public void updateInsets(final Insets border) {
        if (border != null) {
            border.top = Math.max(this.m_pixmaps[0].getHeight(), Math.max(this.m_pixmaps[2].getHeight(), Math.max(this.m_pixmaps[4].getHeight(), Math.max(this.m_pixmaps[1].getHeight(), this.m_pixmaps[3].getHeight()))));
            border.bottom = Math.max(this.m_pixmaps[11].getHeight(), Math.max(this.m_pixmaps[13].getHeight(), Math.max(this.m_pixmaps[15].getHeight(), Math.max(this.m_pixmaps[12].getHeight(), this.m_pixmaps[14].getHeight()))));
            border.left = Math.max(this.m_pixmaps[0].getWidth(), Math.max(this.m_pixmaps[7].getWidth(), Math.max(this.m_pixmaps[11].getWidth(), Math.max(this.m_pixmaps[5].getWidth(), this.m_pixmaps[9].getWidth()))));
            border.right = Math.max(this.m_pixmaps[4].getWidth(), Math.max(this.m_pixmaps[8].getWidth(), Math.max(this.m_pixmaps[15].getWidth(), Math.max(this.m_pixmaps[6].getWidth(), this.m_pixmaps[10].getWidth()))));
        }
    }
    
    @Override
    public final void onCheckIn() {
        this.m_entity.removeReference();
        this.m_entity = null;
    }
    
    @Override
    public final void onCheckOut() {
        this.updatePixmaps();
        assert this.m_entity == null;
        this.m_entity = Entity3D.Factory.newPooledInstance();
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        if (!this.m_pixmapsInitialized) {
            return;
        }
        this.m_entity.clear();
        this.beginAddGeometry(this.m_pixmaps.length);
        for (int i = 0; i < this.m_pixmaps.length; ++i) {
            final int x = this.getLeft(size, margin, border, i);
            final int y = this.getTop(size, margin, border, i);
            final int w = this.getWidth(size, margin, border, i);
            final int h = this.getHeight(size, margin, border, i);
            this.addGeometry(x, y, w, h, this.m_pixmaps[i]);
        }
        this.endAddGeometry();
    }
    
    private int getLeft(final Dimension size, final Insets margin, final Insets border, final int index) {
        if (index == 0 || index == 5 || index == 7 || index == 9 || index == 11) {
            return margin.left;
        }
        if (index == 1 || index == 12) {
            return margin.left + border.left;
        }
        if (index == 2 || index == 13) {
            return margin.left + border.left + this.m_pixmaps[index - 1].getWidth();
        }
        if (index == 3 || index == 14) {
            return size.width - margin.left - margin.right - border.right - this.m_pixmaps[index].getWidth();
        }
        if (index == 4 || index == 6 || index == 8 || index == 10 || index == 15) {
            return size.width - margin.left - margin.right - border.right;
        }
        assert false : "We should never end here";
        return 0;
    }
    
    private int getTop(final Dimension size, final Insets margin, final Insets border, final int index) {
        if (index == 0 || index == 1 || index == 2 || index == 3 || index == 4) {
            return size.height - margin.top;
        }
        if (index == 5 || index == 6) {
            return size.height - (margin.top + border.top);
        }
        if (index == 7 || index == 8) {
            return size.height - (margin.top + border.top + this.m_pixmaps[index - 2].getHeight());
        }
        if (index == 9 || index == 10) {
            return margin.bottom + border.bottom + this.m_pixmaps[index].getHeight();
        }
        if (index == 11 || index == 12 || index == 13 || index == 14 || index == 15) {
            return margin.bottom + border.bottom;
        }
        assert false : "We should never end here";
        return 0;
    }
    
    private int getWidth(final Dimension size, final Insets margin, final Insets border, final int index) {
        if (index == 0 || index == 5 || index == 7 || index == 9 || index == 11) {
            return border.left;
        }
        if (index == 1 || index == 12) {
            return this.m_pixmaps[index].getWidth();
        }
        if (index == 2) {
            return size.width - margin.left - margin.right - border.left - border.right - this.m_pixmaps[1].getWidth() - this.m_pixmaps[3].getWidth();
        }
        if (index == 13) {
            return size.width - margin.left - margin.right - border.left - border.right - this.m_pixmaps[12].getWidth() - this.m_pixmaps[14].getWidth();
        }
        if (index == 3 || index == 14) {
            return this.m_pixmaps[index].getWidth();
        }
        if (index == 4 || index == 6 || index == 8 || index == 10 || index == 15) {
            return border.right;
        }
        assert false : "We should never end here";
        return 0;
    }
    
    private int getHeight(final Dimension size, final Insets margin, final Insets border, final int index) {
        if (index == 0 || index == 1 || index == 2 || index == 3 || index == 4) {
            return border.top;
        }
        if (index == 5 || index == 6) {
            return this.m_pixmaps[index].getHeight();
        }
        if (index == 7) {
            return size.height - margin.top - margin.bottom - border.top - border.bottom - this.m_pixmaps[5].getHeight() - this.m_pixmaps[9].getHeight();
        }
        if (index == 8) {
            return size.height - margin.top - margin.bottom - border.top - border.bottom - this.m_pixmaps[6].getHeight() - this.m_pixmaps[10].getHeight();
        }
        if (index == 9 || index == 10) {
            return this.m_pixmaps[index].getHeight();
        }
        if (index == 11 || index == 12 || index == 13 || index == 14 || index == 15) {
            return border.bottom;
        }
        assert false : "We should never end here";
        return 0;
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
}
