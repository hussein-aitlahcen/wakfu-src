package com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;

public class DelayedColor implements ColorHelper
{
    private static final Logger m_logger;
    private int m_startColorIndex;
    private VertexBufferPCT m_vb;
    private float[] m_particleGeomColor;
    
    @Override
    public void applyImmediate(final VertexBufferPCT vb, final float[] particleGeomColor, final int length) {
        this.m_vb = vb;
        this.m_startColorIndex = vb.getNumVertices() * 4;
        if (this.m_particleGeomColor == null || this.m_particleGeomColor.length != length) {
            this.m_particleGeomColor = new float[length];
        }
        System.arraycopy(particleGeomColor, 0, this.m_particleGeomColor, 0, length);
    }
    
    @Override
    public void applyDelayed(final float[] colors) {
        if (this.m_particleGeomColor == null) {
            return;
        }
        final float red = colors[0];
        final float green = colors[1];
        final float blue = colors[2];
        final float alpha = 1.0f;
        int colorIndex = 0;
        for (int verticesCount = this.m_particleGeomColor.length / 4, i = 0; i < verticesCount; ++i) {
            final float[] particleGeomColor = this.m_particleGeomColor;
            final int n = colorIndex++;
            particleGeomColor[n] *= red;
            final float[] particleGeomColor2 = this.m_particleGeomColor;
            final int n2 = colorIndex++;
            particleGeomColor2[n2] *= green;
            final float[] particleGeomColor3 = this.m_particleGeomColor;
            final int n3 = colorIndex++;
            particleGeomColor3[n3] *= blue;
            final float[] particleGeomColor4 = this.m_particleGeomColor;
            final int n4 = colorIndex++;
            particleGeomColor4[n4] *= 1.0f;
        }
        final FloatBuffer colorBuffer = this.m_vb.getColorBuffer();
        colorBuffer.position(this.m_startColorIndex);
        colorBuffer.put(this.m_particleGeomColor);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DelayedColor.class);
    }
}
