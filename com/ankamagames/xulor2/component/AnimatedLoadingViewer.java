package com.ankamagames.xulor2.component;

import com.ankamagames.framework.graphics.engine.transformer.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class AnimatedLoadingViewer extends AnimatedElementViewer
{
    public static final String TAG = "animatedLoadingViewer";
    
    @Override
    protected void updateEntity() {
        final Entity entity = this.m_animatedElement.getEntity();
        final TransformerSRT transformer = (TransformerSRT)entity.getTransformer().getTransformer(0);
        transformer.setTranslation((float)this.m_size.getWidth() / 2.0f + this.m_offsetX, (float)this.m_size.getHeight() / 2.0f + this.m_offsetY, 0.0f);
        float scale = 1.0f;
        if (this.m_animatedElement.getAnmInstance() != null) {
            scale = this.m_animatedElement.getAnmInstance().getScale();
            final Dimension size = this.getSize();
            final double width = size.getWidth();
            final double height = size.getHeight();
            final double widgetRatio = width / height;
            final Rect boundingRect = this.m_animatedElement.getAnmInstance().getAvgBoundingRect();
            final int boundingWidth = boundingRect.getXMax() - boundingRect.getXMin();
            final int boundingHeight = boundingRect.getYMax() - boundingRect.getYMin();
            final float anmRatio = boundingWidth / boundingHeight;
            if (widgetRatio < anmRatio) {
                this.m_scale = (float)(width / boundingWidth);
            }
            else {
                this.m_scale = (float)(height / boundingHeight);
            }
            final float xScissorFactor = 0.8f;
            final float yScissorFactor = 0.74f;
            final float realWidth = boundingWidth * this.m_scale * 0.8f;
            final float realHeight = boundingHeight * this.m_scale * 0.74f;
            this.setScissor(new Rectangle((int)((width - realWidth) / 2.0), (int)((height - realHeight) / 2.0), (int)realWidth, (int)realHeight));
        }
        transformer.setScale(this.m_scale / scale, this.m_scale / scale, 0.0f);
        entity.getTransformer().setToUpdate();
        this.m_entityNeedUpdate = false;
    }
    
    @Override
    public void setSize(final int width, final int height) {
        super.setSize(width, height);
        this.setNeedsToPostProcess();
    }
    
    @Override
    public String getTag() {
        return "animatedLoadingViewer";
    }
}
