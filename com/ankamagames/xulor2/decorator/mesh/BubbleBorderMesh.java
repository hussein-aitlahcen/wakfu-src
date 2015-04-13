package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.baseImpl.graphics.alea.adviser.text.backgroundedText.bubble.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.backgroundedText.overHeadInfos.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.text.*;
import com.ankamagames.framework.graphics.engine.text.*;

public class BubbleBorderMesh extends AbstractBorderMesh
{
    private StyledBubbleTesselBackground m_background;
    private DefaultOverHeadInfosBackground m_defaultOverHeadInfosBackground;
    private EntityText m_entity;
    private boolean m_displaySpark;
    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    
    public BubbleBorderMesh() {
        super();
        this.m_background = new StyledBubbleTesselBackground();
        this.m_defaultOverHeadInfosBackground = new DefaultOverHeadInfosBackground();
        this.m_displaySpark = true;
        super.setInsets(new Insets(this.m_background.getTopMargin(), this.m_background.getLeftMargin(), this.m_background.getBottomMargin(), this.m_background.getRightMargin()));
    }
    
    public void setSparkAngle(final float angle) {
        this.m_background.setSparkAngle(angle);
        this.updateBackground();
    }
    
    public void setDisplaySpark(final boolean displaySpark) {
        this.m_background.drawSpark(displaySpark);
        this.updateBackground();
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        this.m_x = margin.left;
        this.m_y = margin.bottom;
        this.m_width = size.width - margin.left - margin.right;
        this.m_height = size.height - margin.bottom - margin.top;
        this.m_entity.setPosition(new Vector4(this.m_x, this.m_y, 0.0f));
        this.m_entity.setMinWidth(this.m_width - (border.left + border.right));
        this.m_entity.setMinHeight(this.m_height - (border.bottom + border.top));
    }
    
    private void updateBackground() {
        this.m_entity.getBackgroundGeometry().setShape(this.m_background.getVerticesAdjustement(), this.m_background.getVerticesWidthAndHeight());
    }
    
    @Override
    public Entity getEntity() {
        return this.m_entity;
    }
    
    @Override
    public void onCheckIn() {
        this.m_entity.removeReference();
        this.m_entity = null;
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null;
        this.m_entity = EntityText.Factory.newPooledInstance();
        final GLGeometryText geometryText = GLGeometryText.Factory.newPooledInstance();
        final GLGeometryBackground background = GLGeometryBackground.Factory.newPooledInstance();
        this.m_entity.setTextGeometry(geometryText);
        this.m_entity.setBackgroundGeometry(background);
        this.m_entity.setAlign(TextAlignment.SOUTH_WEST);
        background.setShape(this.m_background.getVerticesAdjustement(), this.m_background.getVerticesWidthAndHeight());
        background.setBackgroundIndices(this.m_background.getVerticesIndex());
        background.setBorderIndices(this.m_background.getBorderVerticesIndex());
        background.setMargins(this.m_background.getLeftMargin(), this.m_background.getRightMargin(), this.m_background.getTopMargin(), this.m_background.getBottomMargin());
        geometryText.removeReference();
        background.removeReference();
    }
    
    @Override
    public void setInsets(final Insets insets) {
    }
}
