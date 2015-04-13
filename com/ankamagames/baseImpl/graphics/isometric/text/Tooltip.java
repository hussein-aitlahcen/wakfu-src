package com.ankamagames.baseImpl.graphics.isometric.text;

import com.ankamagames.framework.graphics.engine.opengl.text.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;

public class Tooltip
{
    public static final int INFINITE_DURATION = Integer.MAX_VALUE;
    public static final int DEFAULT_DURATION = 3000;
    public static int DURATION;
    private int m_elapsedTime;
    private int m_duration;
    private boolean m_shown;
    private int m_xOffset;
    private int m_yOffset;
    private EntityText m_entity;
    
    public static void setDefaultDuration(final int defaultDuration) {
        Tooltip.DURATION = defaultDuration;
    }
    
    public static int getDefaultDuration() {
        return Tooltip.DURATION;
    }
    
    public Tooltip(final Font font) {
        super();
        this.m_duration = Tooltip.DURATION;
        this.m_shown = false;
        this.m_xOffset = 0;
        this.m_yOffset = 0;
        this.m_entity = EntityText.Factory.newPooledInstance();
        final GLGeometryText geometryText = GLGeometryText.Factory.newPooledInstance();
        final GeometryBackground geometryBackground = ((MemoryObject.ObjectFactory<GeometryBackground>)GLGeometryBackground.Factory).newPooledInstance();
        this.m_entity.setAlign(TextAlignment.SOUTH_WEST);
        this.m_entity.setTextGeometry(geometryText);
        this.m_entity.setBackgroundGeometry(geometryBackground);
        this.m_entity.setFont(font);
        final GeometryBackground background = this.m_entity.getBackgroundGeometry();
        final TooltipBackground tooltipBackground = new TooltipBackground();
        background.setShape(tooltipBackground.getVerticesAdjustement(), tooltipBackground.getVerticesWidthAndHeight());
        background.setBackgroundIndices(tooltipBackground.getVerticesIndex());
        background.setBorderIndices(tooltipBackground.getBorderVerticesIndex());
        background.setMargins(tooltipBackground.getLeftMargin(), tooltipBackground.getRightMargin(), tooltipBackground.getTopMargin(), tooltipBackground.getBottomMargin());
        geometryText.removeReference();
        background.removeReference();
    }
    
    public void setBackgroundColor(final float r, final float g, final float b, final float a) {
        this.m_entity.getBackgroundGeometry().setColor(r, g, b, a);
    }
    
    public Color getBackgroundColor() {
        return this.m_entity.getBackgroundGeometry().getColor();
    }
    
    public void setBorderColor(final float r, final float g, final float b, final float a) {
        this.m_entity.getBackgroundGeometry().setBorderColor(r, g, b, a);
    }
    
    public Color getBorderColor() {
        return this.m_entity.getBackgroundGeometry().getBorderColor();
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
    
    public void setOffset(final int xOffset, final int yOffset) {
        this.m_xOffset = xOffset;
        this.m_yOffset = yOffset;
    }
    
    public int getXOffset() {
        return this.m_xOffset;
    }
    
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    protected float getOriginalX() {
        return this.m_xOffset;
    }
    
    protected float getOriginalY() {
        return this.m_yOffset;
    }
    
    public void setVisible(final boolean visible) {
        if (!visible && this.m_entity.getText() != null && !this.m_entity.getText().isEmpty()) {
            this.m_shown = false;
            this.m_elapsedTime = 0;
        }
        this.m_entity.setVisible(visible);
    }
    
    public void process(final int deltaTime) {
        if (this.m_entity.getText() == null) {
            return;
        }
        if (!this.m_entity.isVisible()) {
            this.m_shown = false;
            return;
        }
        if (this.m_duration == Integer.MAX_VALUE) {
            return;
        }
        this.m_elapsedTime += deltaTime;
        if (!this.m_shown) {
            this.m_elapsedTime = 0;
            this.m_shown = true;
        }
        if (this.m_elapsedTime >= this.m_duration) {
            this.setVisible(false);
            this.m_shown = false;
        }
    }
    
    public final EntityText getEntity() {
        return this.m_entity;
    }
    
    public final String getText() {
        return this.m_entity.getText();
    }
    
    public final void setText(final String text) {
        this.m_entity.setText(text);
    }
    
    public final void setColor(final float red, final float green, final float blue, final float alpha) {
        this.m_entity.setColor(red, green, blue, alpha);
    }
    
    public final void setFont(final Font font) {
        this.m_entity.setFont(font);
    }
    
    public final void setPosition(final float x, final float y) {
        this.m_entity.setPosition(new Vector4(x, y, 0.0f));
    }
    
    public final void setBorderWidth(final float width) {
        this.m_entity.getBackgroundGeometry().setBorderWidth(width);
    }
    
    public final void setMaxWidth(final int maxWidth) {
        this.m_entity.setMaxWidth(maxWidth);
    }
    
    static {
        Tooltip.DURATION = 3000;
    }
}
