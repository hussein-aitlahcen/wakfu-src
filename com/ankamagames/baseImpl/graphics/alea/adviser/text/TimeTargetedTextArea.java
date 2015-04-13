package com.ankamagames.baseImpl.graphics.alea.adviser.text;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.text.*;
import com.ankamagames.framework.graphics.engine.text.*;

public class TimeTargetedTextArea extends TargetedTextArea
{
    public static final int INFINITE_DURATION = -1;
    private int m_elapsedLifeTime;
    private int m_duration;
    private int m_waitingTime;
    
    public TimeTargetedTextArea(final Font font, final String text) {
        super(font, text);
        this.m_elapsedLifeTime = 0;
        this.setDuration(this.m_duration = -1);
    }
    
    public TimeTargetedTextArea(final Font font, final String text, final int duration) {
        super(font, text);
        this.m_elapsedLifeTime = 0;
        this.m_duration = -1;
        this.setDuration(duration);
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public void setDuration(final int duration) {
        assert duration >= -1;
        this.m_duration = duration;
    }
    
    public boolean isAlive() {
        return this.m_duration == -1 || this.m_elapsedLifeTime <= this.m_duration + this.m_waitingTime;
    }
    
    public void resetElapsedLifeTime() {
        this.m_elapsedLifeTime = 0;
    }
    
    public void process(final int deltaTime) {
        if (this.m_waitingTime > 0) {
            this.m_waitingTime -= deltaTime;
            if (this.m_waitingTime <= 0) {
                this.getEntity().setVisible(true);
            }
        }
        else {
            this.m_elapsedLifeTime += deltaTime;
        }
    }
    
    public void setWaitingTime(final int waitingTime) {
        assert waitingTime >= 0;
        this.m_waitingTime = waitingTime;
        if (this.m_waitingTime > 0) {
            this.getEntity().setVisible(false);
        }
    }
    
    protected int getElapsedLifeTime() {
        return this.m_elapsedLifeTime;
    }
    
    public final String getText() {
        return this.getEntity().getText();
    }
    
    public final void setColor(final float red, final float green, final float blue, final float alpha) {
        this.getEntity().setColor(red, green, blue, alpha);
    }
    
    public final void setFont(final Font font) {
        this.getEntity().setFont(font);
    }
    
    public final void setPosition(final float x, final float y) {
        this.getEntity().setPosition(new Vector4(x, y, 0.0f));
    }
    
    public final void setBorderWidth(final float width) {
        this.getEntity().getBackgroundGeometry().setBorderWidth(width);
    }
    
    public final void setMaxWidth(final int maxWidth) {
        this.getEntity().setMaxWidth(maxWidth);
    }
    
    public final int getMaxWidth() {
        return this.getEntity().getMaxWidth();
    }
    
    public final int getMinWidth() {
        return this.getEntity().getMinWidth();
    }
    
    public final void setBackground(final DrawedBackground background) {
        final GeometryBackground geometryBackground = this.getEntity().getBackgroundGeometry();
        geometryBackground.setShape(background.getVerticesAdjustement(), background.getVerticesWidthAndHeight());
        geometryBackground.setBackgroundIndices(background.getVerticesIndex());
        geometryBackground.setBorderIndices(background.getBorderVerticesIndex());
        geometryBackground.setMargins(background.getLeftMargin(), background.getRightMargin(), background.getTopMargin(), background.getBottomMargin());
    }
    
    public final void setMinWidth(final int minWidth) {
        this.getEntity().setMinWidth(minWidth);
    }
    
    @Override
    public final void setVisible(final boolean visible) {
        this.getEntity().setVisible(visible);
    }
    
    public final void setMinHeight(final int minHeight) {
        this.getEntity().setMinHeight(minHeight);
    }
    
    public final int getRealTextWidth() {
        return this.getEntity().getTextWidth();
    }
    
    public final int getRealTextHeight() {
        return this.getEntity().getTextHeight();
    }
}
