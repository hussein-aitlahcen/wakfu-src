package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.video.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class VideoMesh
{
    private Color m_modulationColor;
    private int m_x;
    private int m_y;
    private int m_componentWidth;
    private int m_componentHeight;
    private boolean m_flipVerticaly;
    private boolean m_keepRatio;
    private EntitySprite m_entity;
    
    public VideoMesh() {
        super();
        this.m_keepRatio = false;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public int getHeight() {
        return this.m_componentHeight;
    }
    
    public int getWidth() {
        return this.m_componentWidth;
    }
    
    public void setSize(final int width, final int height) {
        this.m_componentWidth = width;
        this.m_componentHeight = height;
    }
    
    public void setModulationColor(final Color modulationColor) {
        if (modulationColor == this.m_modulationColor) {
            return;
        }
        if (modulationColor != null) {
            this.m_entity.setColor(modulationColor.getRed(), modulationColor.getGreen(), modulationColor.getBlue(), modulationColor.getAlpha());
        }
        else {
            this.m_entity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        this.m_modulationColor = modulationColor;
    }
    
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void setFlipVerticaly(final boolean flipVerticaly) {
        this.m_flipVerticaly = flipVerticaly;
    }
    
    public void updateVertex(final VideoTextureProducer textureProducer, final Insets margin, final Insets border, final Insets padding) {
        if (this.m_entity == null) {
            return;
        }
        if (textureProducer == null || !textureProducer.hasTexture()) {
            this.m_entity.setVisible(false);
            return;
        }
        final int right = border.right + padding.left + padding.right;
        final int left = margin.left + border.left + padding.left;
        final int top = margin.top + margin.bottom + border.top;
        final int bottom = margin.bottom + border.bottom + padding.bottom;
        final int componentWidth = this.m_componentWidth - (left + right);
        final int componentHeight = this.m_componentHeight - (top + bottom);
        final int videoWidth = textureProducer.getSourceWidth();
        final int videoHeight = textureProducer.getSourceHeight();
        if (componentHeight == 0 || videoHeight == 0) {
            this.m_entity.setVisible(false);
            return;
        }
        final float componentRatio = componentWidth / componentHeight;
        final float videoRatio = videoWidth / videoHeight;
        if (!this.m_keepRatio || MathHelper.isEqual(componentRatio, videoRatio, 0.01f)) {
            this.m_entity.setBounds(this.m_componentHeight - top, left, componentWidth, componentHeight);
        }
        else if (componentRatio > videoRatio) {
            final float newWidth = componentHeight * videoRatio;
            final float lineWidth = (componentWidth - newWidth) / 2.0f;
            this.m_entity.setBounds(this.m_componentHeight - top, left + lineWidth, Math.round(newWidth), componentHeight);
        }
        else {
            final float newHeight = componentWidth / videoRatio;
            final float lineHeight = (componentHeight - newHeight) / 2.0f;
            this.m_entity.setBounds(this.m_componentHeight - top - lineHeight, left, componentWidth, Math.round(newHeight));
        }
        this.m_entity.setVisible(true);
        if (this.m_modulationColor != null) {
            this.m_entity.setColor(this.m_modulationColor);
        }
        else {
            this.m_entity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        this.m_entity.setTexture(textureProducer.getTexture());
        this.m_entity.getTransformer().setTranslation(0, -this.m_componentWidth / 2, -this.m_componentHeight / 2);
        this.m_entity.getTransformer().setTranslation(1, this.m_componentWidth / 2, this.m_componentHeight / 2);
        this.setTextureCoordinates(textureProducer);
    }
    
    private void setTextureCoordinates(final VideoTextureProducer textureProducer) {
        final float top = this.m_flipVerticaly ? textureProducer.getBottom() : textureProducer.getTop();
        final float bottom = this.m_flipVerticaly ? textureProducer.getTop() : textureProducer.getBottom();
        final float left = textureProducer.getLeft();
        final float right = textureProducer.getRight();
        this.m_entity.getGeometry().setTextureCoordinates(top, left, bottom, right, GeometrySprite.SpriteRotation.NONE);
    }
    
    public void onCheckIn() {
        this.m_modulationColor = null;
        this.m_entity.removeReference();
        this.m_entity = null;
    }
    
    public void onCheckOut() {
        assert this.m_entity == null;
        this.m_entity = EntitySprite.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        this.m_entity.setGeometry(geom);
        geom.removeReference();
        final BatchTransformer transformer = this.m_entity.getTransformer();
        transformer.addTransformer(new TransformerSRT());
        transformer.addTransformer(new TransformerSRT());
    }
    
    public Entity getEntity() {
        return this.m_entity;
    }
    
    public void setKeepRatio(final boolean keepRatio) {
        this.m_keepRatio = keepRatio;
    }
}
