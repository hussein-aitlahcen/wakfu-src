package com.ankamagames.baseImpl.graphics.isometric.text;

import com.ankamagames.framework.graphics.engine.*;

public class TooltipBackground implements DrawedBackground
{
    private static final float[][] VERTICES_ADJUSTMENT;
    private static final float[][] VERTICES_WIDTH_AND_HEIGHT;
    private static final IndexBuffer VERTICES_INDEX;
    private static final IndexBuffer BORDER_VERTICES_INDEX;
    
    @Override
    public int getBottomMargin() {
        return 5;
    }
    
    @Override
    public int getLeftMargin() {
        return 5;
    }
    
    @Override
    public int getRightMargin() {
        return 5;
    }
    
    @Override
    public int getTopMargin() {
        return 5;
    }
    
    @Override
    public float[][] getVerticesAdjustement() {
        return TooltipBackground.VERTICES_ADJUSTMENT;
    }
    
    @Override
    public float[][] getVerticesWidthAndHeight() {
        return TooltipBackground.VERTICES_WIDTH_AND_HEIGHT;
    }
    
    @Override
    public IndexBuffer getVerticesIndex() {
        return TooltipBackground.VERTICES_INDEX;
    }
    
    @Override
    public IndexBuffer getBorderVerticesIndex() {
        return TooltipBackground.BORDER_VERTICES_INDEX;
    }
    
    static {
        VERTICES_ADJUSTMENT = new float[][] { { 0.0f, 2.0f }, { 0.0f, -2.0f }, { 2.0f, 0.0f }, { -2.0f, 0.0f }, { 0.0f, -2.0f }, { 0.0f, 2.0f }, { -2.0f, 0.0f }, { 2.0f, 0.0f } };
        VERTICES_WIDTH_AND_HEIGHT = new float[][] { { 0.0f, 0.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 0.0f, 0.0f } };
        VERTICES_INDEX = new IndexBuffer(new short[] { 0, 1, 2, 7, 7, 2, 3, 6, 3, 6, 5, 4 });
        BORDER_VERTICES_INDEX = new IndexBuffer(new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 0 });
    }
}
