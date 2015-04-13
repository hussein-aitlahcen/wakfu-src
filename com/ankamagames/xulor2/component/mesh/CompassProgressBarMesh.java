package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.*;

public class CompassProgressBarMesh extends CirclePlainProgressBarMesh
{
    @Override
    protected void computeVertices(final VertexBufferPCT buf, final int vertices) {
        buf.setNumVertices(vertices);
        if (this.m_pixmap != null) {
            boolean outer = false;
            for (int i = 0; i < vertices; ++i) {
                final float alpha = 1.0f - Math.abs((vertices / 2.0f - i) / (vertices / 2.0f));
                final boolean mirror = i % 4 < 2;
                outer = !outer;
                final float u = mirror ? this.m_pixmap.getRight() : this.m_pixmap.getLeft();
                final float v = outer ? this.m_pixmap.getTop() : this.m_pixmap.getBottom();
                buf.setVertexTexCoord0(i, u, v);
                buf.setVertexColor(i, 1.0f, 1.0f, 1.0f, alpha);
            }
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setInnerRadiusFactor(0.95f);
    }
}
