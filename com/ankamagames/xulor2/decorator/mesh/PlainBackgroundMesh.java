package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;

public class PlainBackgroundMesh extends AbstractBackgroundMesh
{
    protected EntitySprite m_entity;
    protected Color m_color;
    protected Color m_modulationColor;
    
    public void setColor(final float[] colorComponents) {
        assert colorComponents != null : "Color components array can't be null";
        assert colorComponents.length == 4 : "Color components size must be equal to 4";
        this.m_color = new Color(colorComponents[0], colorComponents[1], colorComponents[2], colorComponents[3]);
        this.m_entity.setColor(this.m_color);
    }
    
    public void setColor(final Color color) {
        assert color != null : "Color can't be null";
        if (this.m_color != null && this.m_color.get() == color.get()) {
            return;
        }
        this.m_color = color;
        this.updateColor();
    }
    
    private void updateColor() {
        float r = (this.m_color != null) ? this.m_color.getRed() : 1.0f;
        float g = (this.m_color != null) ? this.m_color.getGreen() : 1.0f;
        float b = (this.m_color != null) ? this.m_color.getBlue() : 1.0f;
        float a = (this.m_color != null) ? this.m_color.getAlpha() : 1.0f;
        r *= ((this.m_modulationColor != null) ? this.m_modulationColor.getRed() : 1.0f);
        g *= ((this.m_modulationColor != null) ? this.m_modulationColor.getGreen() : 1.0f);
        b *= ((this.m_modulationColor != null) ? this.m_modulationColor.getBlue() : 1.0f);
        a *= ((this.m_modulationColor != null) ? this.m_modulationColor.getAlpha() : 1.0f);
        this.m_entity.setColor(r, g, b, a);
    }
    
    public final Color getColor() {
        return this.m_color;
    }
    
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void setModulationColor(final Color modulationColor) {
        if (this.m_modulationColor == modulationColor) {
            return;
        }
        this.m_modulationColor = modulationColor;
        this.updateColor();
    }
    
    @Override
    public final EntitySprite getEntity() {
        return this.m_entity;
    }
    
    @Override
    public void onCheckIn() {
        this.m_modulationColor = null;
        this.m_color = null;
        this.m_entity.removeReference();
        this.m_entity = null;
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null : "Object is already checked out";
        this.m_entity = EntitySprite.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        this.m_entity.setGeometry(geom);
        geom.removeReference();
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        final int width = size.width - margin.right - border.right - margin.left - border.left;
        final int height = size.height - margin.top - border.top - margin.bottom - border.bottom;
        final int left = margin.left + border.left;
        final int top = margin.bottom + border.bottom + height;
        this.m_entity.setBounds(top, left, width, height);
    }
}
