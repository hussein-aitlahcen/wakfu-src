package com.ankamagames.xulor2.component.mesh;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.batch.*;
import com.ankamagames.xulor2.util.alignment.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public final class CircleDesatProgressBarMesh extends AbstractProgressBarMesh
{
    private static final Logger m_logger;
    private Pixmap[] m_pixmaps;
    private boolean m_pixmapsInitialized;
    private Entity3D m_entity;
    private boolean m_horizontal;
    private Color m_color;
    private AngleDesatShader m_shader;
    private float m_deltaAngle;
    private Entity3DBatcher m_batcher;
    
    public CircleDesatProgressBarMesh() {
        super();
        this.m_pixmaps = new Pixmap[9];
        this.m_pixmapsInitialized = false;
        this.m_horizontal = true;
        this.m_deltaAngle = 1.5707964f;
    }
    
    @Override
    public float getDeltaAngle() {
        return 0.0f;
    }
    
    @Override
    public void setDeltaAngle(final float deltaAngle) {
    }
    
    @Override
    public void setPosition(final Alignment9 position) {
    }
    
    @Override
    public Alignment9 getPosition() {
        return null;
    }
    
    @Override
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
    }
    
    @Override
    public void setBorderColor(final Color c) {
    }
    
    @Override
    public Color getBorderColor() {
        return null;
    }
    
    @Override
    public void setBorder(final Insets border) {
    }
    
    @Override
    public Insets getBorder() {
        return null;
    }
    
    @Override
    public void setColor(final Color c) {
        if (this.m_color == c) {
            return;
        }
        this.m_color = c;
        this.updateColor();
    }
    
    @Override
    public Color getColor() {
        return this.m_color;
    }
    
    @Override
    public void setModulationColor(final Color c) {
        super.setModulationColor(c);
        this.updateColor();
    }
    
    private void updateColor() {
        float a;
        float b;
        float r;
        float g;
        if (this.m_color == null) {
            g = (r = (b = (a = 1.0f)));
        }
        else {
            r = this.m_color.getRed();
            g = this.m_color.getGreen();
            b = this.m_color.getBlue();
            a = this.m_color.getAlpha();
        }
        if (this.m_modulationColor != null) {
            r *= this.m_modulationColor.getRed();
            g *= this.m_modulationColor.getGreen();
            b *= this.m_modulationColor.getBlue();
            a *= this.m_modulationColor.getAlpha();
        }
        this.m_entity.setColor(r, g, b, a);
    }
    
    @Override
    public void setFullCirclePercentage(final float percentage) {
    }
    
    @Override
    public float getFullCirclePercentage() {
        return 0.0f;
    }
    
    public Pixmap getEast() {
        return this.m_pixmaps[5];
    }
    
    public void setEast(final Pixmap east) {
        this.m_pixmaps[5] = east;
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
        return this.m_pixmaps[7];
    }
    
    public void setSouth(final Pixmap south) {
        this.m_pixmaps[7] = south;
        this.updatePixmaps();
    }
    
    public Pixmap getSouthEast() {
        return this.m_pixmaps[8];
    }
    
    public void setSouthEast(final Pixmap southEast) {
        this.m_pixmaps[8] = southEast;
        this.updatePixmaps();
    }
    
    public Pixmap getSouthWest() {
        return this.m_pixmaps[6];
    }
    
    public void setSouthWest(final Pixmap southWest) {
        this.m_pixmaps[6] = southWest;
        this.updatePixmaps();
    }
    
    public Pixmap getWest() {
        return this.m_pixmaps[3];
    }
    
    public void setWest(final Pixmap west) {
        this.m_pixmaps[3] = west;
        this.updatePixmaps();
    }
    
    public Pixmap getCenter() {
        return this.m_pixmaps[4];
    }
    
    public void setCenter(final Pixmap center) {
        this.m_pixmaps[4] = center;
        this.updatePixmaps();
    }
    
    @Override
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
        this.updatePixmaps();
    }
    
    public void setPixmaps(final Pixmap center) {
        this.m_pixmaps[5] = center;
        this.updatePixmaps();
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
        this.updatePixmaps();
    }
    
    private void updatePixmaps() {
        if (this.m_pixmaps[4] == null) {
            this.m_pixmapsInitialized = false;
            return;
        }
        int nbPixmap = 0;
        for (int i = this.m_pixmaps.length - 1; i >= 0; --i) {
            if (this.m_pixmaps[i] != null) {
                ++nbPixmap;
            }
        }
        if (nbPixmap != 1 && nbPixmap != 9) {
            this.m_pixmapsInitialized = false;
            return;
        }
        this.m_pixmapsInitialized = true;
        this.m_shader.setPixmap(this.m_pixmaps[4]);
    }
    
    @Override
    public void setGeometry(final int x, final int y, final int width, final int height, final float value) {
        if (!this.m_pixmapsInitialized) {
            return;
        }
        this.m_entity.clear();
        final int[] widths = new int[3];
        final int[] heights = new int[3];
        widths[0] = this.m_pixmaps[0].getWidth();
        widths[2] = this.m_pixmaps[2].getWidth();
        widths[1] = Math.max(0, width - (widths[0] + widths[2]));
        heights[0] = this.m_pixmaps[0].getHeight();
        heights[2] = this.m_pixmaps[6].getHeight();
        heights[1] = Math.max(0, height - (heights[0] + heights[2]));
        this.beginAddGeometry(9);
        int sy = height + y;
        for (int row = 0; row < 3; ++row) {
            int sx = x;
            for (int column = 0; column < 3; ++column) {
                this.addGeometry(sx, sy, widths[column], heights[row], this.m_pixmaps[row * 3 + column]);
                sx += widths[column];
            }
            sy -= heights[row];
        }
        this.endAddGeometry();
        this.setShaderValues(value);
    }
    
    private void setShaderValues(final float percentage) {
        final float minAngle = 6.2831855f * percentage;
        final float maxAngle = 6.2831855f;
        this.m_shader.setMinAlpha(minAngle);
        this.m_shader.setMaxAlpha(6.2831855f);
    }
    
    @Override
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    @Override
    public final void onCheckOut() {
        assert this.m_entity == null;
        this.m_entity = Entity3D.Factory.newPooledInstance();
        try {
            this.m_shader = new AngleDesatShader();
            this.m_entity.setEffect(this.m_shader.getEffect(), this.m_shader.getTechniqueCRC(), this.m_shader.getParams());
        }
        catch (Exception ex) {}
    }
    
    @Override
    public final void onCheckIn() {
        super.onCheckIn();
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_color = null;
        if (this.m_batcher != null) {
            this.m_batcher.release();
            this.m_batcher = null;
        }
    }
    
    private void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap) {
        if (width == 0 || height == 0) {
            return;
        }
        this.m_batcher.fillBuffer(left, top, width, height, pixmap, null);
    }
    
    private void beginAddGeometry(final int numGeom) {
        if (this.m_batcher == null) {
            this.m_batcher = new Entity3DBatcher();
        }
        this.m_batcher.beginAddGeometry(this.m_entity, numGeom);
    }
    
    private void endAddGeometry() {
        this.m_batcher.endAddGeometry();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CircleDesatProgressBarMesh.class);
    }
}
