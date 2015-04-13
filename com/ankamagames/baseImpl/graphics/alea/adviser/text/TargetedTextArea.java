package com.ankamagames.baseImpl.graphics.alea.adviser.text;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.engine.opengl.text.*;
import com.ankamagames.framework.kernel.core.common.*;

public class TargetedTextArea
{
    private WorldPositionable m_target;
    private int m_xOffset;
    private int m_yOffset;
    private EntityText m_entity;
    
    public TargetedTextArea(final Font font, final String text) {
        this(font, false, text);
    }
    
    public TargetedTextArea(final Font font, final boolean antiAliased, final String text) {
        super();
        this.m_target = null;
        this.m_entity = EntityText.Factory.newPooledInstance();
        final GeometryBackground background = ((MemoryObject.ObjectFactory<GeometryBackground>)GLGeometryBackground.Factory).newPooledInstance();
        final GeometryText textGeometry = ((MemoryObject.ObjectFactory<GeometryText>)GLGeometryText.Factory).newPooledInstance();
        this.m_entity.setBackgroundGeometry(background);
        this.m_entity.setTextGeometry(textGeometry);
        this.m_entity.setFont(font);
        this.m_entity.setText(text);
        background.removeReference();
        textGeometry.removeReference();
    }
    
    public WorldPositionable getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final WorldPositionable target) {
        this.m_target = target;
    }
    
    public int getXOffset() {
        return this.m_xOffset;
    }
    
    public void setXOffset(final int offset) {
        this.m_xOffset = offset;
    }
    
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    public void setYOffset(final int offset) {
        this.m_yOffset = offset;
    }
    
    public float getWorldX() {
        if (this.m_target != null) {
            return this.m_target.getWorldX();
        }
        return 0.0f;
    }
    
    public float getWorldY() {
        if (this.m_target != null) {
            return this.m_target.getWorldY();
        }
        return 0.0f;
    }
    
    public float getAltitude() {
        if (this.m_target != null) {
            return this.m_target.getAltitude();
        }
        return 0.0f;
    }
    
    public EntityText getEntity() {
        return this.m_entity;
    }
    
    public void setText(final String text) {
        this.m_entity.setText(text);
    }
    
    public void setVisible(final boolean visible) {
        this.m_entity.setVisible(visible);
    }
    
    public boolean isVisible() {
        return this.m_entity.isVisible();
    }
    
    public boolean needsToPrecomputeZoom() {
        return false;
    }
    
    public void release() {
        this.m_entity.removeReference();
        this.m_target = null;
    }
}
