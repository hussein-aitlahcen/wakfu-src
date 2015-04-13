package com.ankamagames.baseImpl.graphics.alea.adviser.text.backgroundedText.bubble;

import com.ankamagames.baseImpl.graphics.isometric.text.*;
import com.ankamagames.framework.graphics.engine.*;

public class StyledBubbleTesselBackground implements DrawedBackground
{
    public static final int SPARK_TAB_OFFSET = 72;
    public static final int SPARK_RADIUS = 20;
    private static final float[][] VERTICES_ADJUSTMENT;
    private static final float[][] VERTICES_WIDTH_AND_HEIGHT;
    private static final IndexBuffer VERTICES_INDEX;
    private static final IndexBuffer BORDER_VERTICES_INDEX;
    private float[][] m_verticesAdjustments;
    private float m_moveX;
    private float m_moveY;
    private boolean m_drawSpark;
    
    @Override
    public int getBottomMargin() {
        return 32;
    }
    
    @Override
    public int getLeftMargin() {
        return 12;
    }
    
    @Override
    public int getRightMargin() {
        return 12;
    }
    
    @Override
    public int getTopMargin() {
        return 12;
    }
    
    @Override
    public float[][] getVerticesAdjustement() {
        return this.m_verticesAdjustments;
    }
    
    @Override
    public float[][] getVerticesWidthAndHeight() {
        return StyledBubbleTesselBackground.VERTICES_WIDTH_AND_HEIGHT;
    }
    
    @Override
    public IndexBuffer getVerticesIndex() {
        return StyledBubbleTesselBackground.VERTICES_INDEX;
    }
    
    @Override
    public IndexBuffer getBorderVerticesIndex() {
        return StyledBubbleTesselBackground.BORDER_VERTICES_INDEX;
    }
    
    public StyledBubbleTesselBackground() {
        super();
        this.m_verticesAdjustments = null;
        this.m_verticesAdjustments = new float[StyledBubbleTesselBackground.VERTICES_ADJUSTMENT.length][2];
        System.arraycopy(StyledBubbleTesselBackground.VERTICES_ADJUSTMENT, 0, this.m_verticesAdjustments, 0, StyledBubbleTesselBackground.VERTICES_ADJUSTMENT.length);
        this.m_moveX = 0.0f;
        this.m_moveY = 0.0f;
    }
    
    public final float getMoveX() {
        return this.m_moveX;
    }
    
    public final void setMoveX(final float moveX) {
        this.m_moveX = moveX;
    }
    
    public final float getMoveY() {
        return this.m_moveY;
    }
    
    public final void setMoveY(final float moveY) {
        this.m_moveY = moveY;
    }
    
    public final void drawSpark(final boolean drawSpark) {
        final int radius = drawSpark ? 20 : 0;
        final float x = this.m_verticesAdjustments[73][0] + (this.m_verticesAdjustments[71][0] - this.m_verticesAdjustments[73][0]) / 2.0f;
        final float y = this.m_verticesAdjustments[73][1] + (this.m_verticesAdjustments[71][1] - this.m_verticesAdjustments[73][1]) / 2.0f;
        final float[] coords = { (float)(Math.cos(-2.0943951023931953) * radius + x), (float)(Math.sin(-2.0943951023931953) * radius + y) };
        this.m_verticesAdjustments[72] = coords;
    }
    
    public void setSparkAngle(float radians) {
        radians %= 3.1415927f;
        if (radians > 0.0f) {
            radians -= 3.141592653589793;
        }
        final float x = this.m_verticesAdjustments[73][0] + (this.m_verticesAdjustments[71][0] - this.m_verticesAdjustments[73][0]) / 2.0f;
        final float y = this.m_verticesAdjustments[73][1] + (this.m_verticesAdjustments[71][1] - this.m_verticesAdjustments[73][1]) / 2.0f;
        final float[] coords = { (float)(Math.cos(radians) * 20.0 + x), (float)(Math.sin(radians) * 20.0 + y) };
        this.m_verticesAdjustments[72] = coords;
    }
    
    static {
        VERTICES_ADJUSTMENT = new float[][] { { 1.0f, -11.0f }, { 2.0f, -11.0f }, { 0.0f, -11.0f }, { 0.0f, -8.0f }, { 2.0f, -8.0f }, { 2.0f, -7.0f }, { 1.0f, -7.0f }, { 1.0f, -4.0f }, { 4.0f, -4.0f }, { 4.0f, -6.0f }, { 6.0f, -6.0f }, { 6.0f, -4.0f }, { 4.0f, -4.0f }, { 4.0f, -1.0f }, { 7.0f, -1.0f }, { 7.0f, -2.0f }, { 8.0f, -2.0f }, { 8.0f, -0.0f }, { 11.0f, -0.0f }, { 11.0f, -2.0f }, { 11.0f, -1.0f }, { -11.0f, -1.0f }, { -11.0f, -2.0f }, { -11.0f, -0.0f }, { -8.0f, -0.0f }, { -8.0f, -2.0f }, { -7.0f, -2.0f }, { -7.0f, -1.0f }, { -4.0f, -1.0f }, { -4.0f, -4.0f }, { -6.0f, -4.0f }, { -6.0f, -6.0f }, { -4.0f, -6.0f }, { -4.0f, -4.0f }, { -1.0f, -4.0f }, { -1.0f, -7.0f }, { -2.0f, -7.0f }, { -2.0f, -8.0f }, { 0.0f, -8.0f }, { 0.0f, -11.0f }, { -2.0f, -11.0f }, { -1.0f, -11.0f }, { -1.0f, 31.0f }, { -2.0f, 31.0f }, { -0.0f, 31.0f }, { -0.0f, 28.0f }, { -2.0f, 28.0f }, { -2.0f, 27.0f }, { -1.0f, 27.0f }, { -1.0f, 24.0f }, { -4.0f, 24.0f }, { -4.0f, 26.0f }, { -6.0f, 26.0f }, { -6.0f, 24.0f }, { -4.0f, 24.0f }, { -4.0f, 21.0f }, { -7.0f, 21.0f }, { -7.0f, 22.0f }, { -8.0f, 22.0f }, { -8.0f, 20.0f }, { -11.0f, 20.0f }, { -11.0f, 22.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { -11.0f, 21.0f }, { 27.0f, 21.0f }, { 17.0f, 0.0f }, { 19.0f, 21.0f }, { 11.0f, 21.0f }, { 11.0f, 22.0f }, { 11.0f, 20.0f }, { 8.0f, 20.0f }, { 8.0f, 22.0f }, { 7.0f, 22.0f }, { 7.0f, 21.0f }, { 4.0f, 21.0f }, { 4.0f, 24.0f }, { 6.0f, 24.0f }, { 6.0f, 26.0f }, { 4.0f, 26.0f }, { 4.0f, 24.0f }, { 1.0f, 24.0f }, { 1.0f, 27.0f }, { 2.0f, 27.0f }, { 2.0f, 28.0f }, { 0.0f, 28.0f }, { 0.0f, 31.0f }, { 2.0f, 31.0f }, { 1.0f, 31.0f } };
        VERTICES_WIDTH_AND_HEIGHT = new float[][] { { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 0.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 1.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 1.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f } };
        VERTICES_INDEX = new IndexBuffer(new short[] { 0, 41, 42, 94, 2, 3, 38, 39, 92, 91, 45, 44, 4, 5, 36, 37, 90, 89, 47, 46, 6, 7, 8, 9, 35, 34, 33, 32, 88, 87, 86, 85, 48, 49, 50, 51, 6, 9, 32, 35, 88, 85, 51, 48, 10, 11, 30, 31, 84, 83, 53, 52, 11, 12, 13, 14, 30, 29, 28, 27, 83, 82, 81, 80, 53, 54, 55, 56, 11, 15, 26, 30, 83, 79, 57, 53, 11, 14, 15, 11, 30, 27, 26, 30, 83, 80, 79, 83, 53, 56, 57, 53, 16, 17, 18, 19, 25, 24, 23, 22, 78, 77, 76, 75, 58, 59, 60, 61, 19, 20, 21, 22, 75, 74, 62, 61, 71, 72, 73, 71 });
        BORDER_VERTICES_INDEX = new IndexBuffer(new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 0 });
    }
}
