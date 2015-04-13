package com.ankamagames.baseImpl.graphics.isometric.highlight;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.core.maths.*;

public enum HighLightTextureApplication
{
    ORTHO_COVER_CELL {
        @Override
        public void apply(final Entity3D entity3D, final byte slope, final float heightPx, final float size, final float halfWidth, final float halfHeight) {
            assert entity3D.getNumGeometries() == 1;
            final GeometryMesh geometryMesh = (GeometryMesh)entity3D.getGeometry(0);
            final VertexBufferPCT vertexBuffer = geometryMesh.getVertexBuffer();
            HighLightTextureApplication.POSITION_BUFFER[0] = -size;
            HighLightTextureApplication.POSITION_BUFFER[1] = (((slope & 0x1) == 0x1) ? heightPx : 0.0f);
            HighLightTextureApplication.POSITION_BUFFER[2] = 0.0f;
            HighLightTextureApplication.POSITION_BUFFER[3] = size * 0.5f + (((slope & 0x2) == 0x2) ? heightPx : 0.0f);
            HighLightTextureApplication.POSITION_BUFFER[4] = size;
            HighLightTextureApplication.POSITION_BUFFER[5] = (((slope & 0x4) == 0x4) ? heightPx : 0.0f);
            HighLightTextureApplication.POSITION_BUFFER[6] = 0.0f;
            HighLightTextureApplication.POSITION_BUFFER[7] = -size * 0.5f + (((slope & 0x8) == 0x8) ? heightPx : 0.0f);
            vertexBuffer.setPositionBuffer(HighLightTextureApplication.POSITION_BUFFER);
        }
    }, 
    ISO {
        @Override
        public void apply(final Entity3D entity3D, final byte slope, final float heightPx, final float size, final float halfWidth, final float halfHeight) {
            assert entity3D.getNumGeometries() == 1;
            final GeometryMesh geometryMesh = (GeometryMesh)entity3D.getGeometry(0);
            final VertexBufferPCT vertexBuffer = geometryMesh.getVertexBuffer();
            final float textureWidth = MathHelper.nearestGreatestPowOfTwo((int)(halfWidth * 2.0f));
            final float textureHeight = MathHelper.nearestGreatestPowOfTwo((int)(halfHeight * 2.0f));
            if (slope != 0) {
                final float fw = halfWidth * 2.0f / textureWidth;
                final float fh = halfHeight * 2.0f / textureHeight;
                final float fy = 1.0f;
                HighLightTextureApplication.POSITION_BUFFER[0] = -size;
                HighLightTextureApplication.POSITION_BUFFER[1] = -1.0f * halfHeight + (((slope & 0x1) == 0x1) ? heightPx : 0.0f);
                HighLightTextureApplication.POSITION_BUFFER[2] = 0.0f;
                HighLightTextureApplication.POSITION_BUFFER[3] = -1.0f * halfHeight + size * 0.5f + (((slope & 0x2) == 0x2) ? heightPx : 0.0f);
                HighLightTextureApplication.POSITION_BUFFER[4] = size;
                HighLightTextureApplication.POSITION_BUFFER[5] = -1.0f * halfHeight + (((slope & 0x4) == 0x4) ? heightPx : 0.0f);
                HighLightTextureApplication.POSITION_BUFFER[6] = 0.0f;
                HighLightTextureApplication.POSITION_BUFFER[7] = -1.0f * halfHeight + -size * 0.5f + (((slope & 0x8) == 0x8) ? heightPx : 0.0f);
                vertexBuffer.setPositionBuffer(HighLightTextureApplication.POSITION_BUFFER);
                vertexBuffer.setVertexTexCoord0(0, 0.5f * fw, 0.0f);
                vertexBuffer.setVertexTexCoord0(1, 1.0f * fw, 0.5f * fh);
                vertexBuffer.setVertexTexCoord0(2, 0.5f * fw, 1.0f * fh);
                vertexBuffer.setVertexTexCoord0(3, 0.0f, 0.5f * fh);
            }
            else {
                HighLightTextureApplication.POSITION_BUFFER[0] = -halfWidth;
                HighLightTextureApplication.POSITION_BUFFER[1] = halfHeight;
                HighLightTextureApplication.POSITION_BUFFER[2] = -halfWidth;
                HighLightTextureApplication.POSITION_BUFFER[3] = -halfHeight;
                HighLightTextureApplication.POSITION_BUFFER[4] = halfWidth;
                HighLightTextureApplication.POSITION_BUFFER[5] = -halfHeight;
                HighLightTextureApplication.POSITION_BUFFER[6] = halfWidth;
                HighLightTextureApplication.POSITION_BUFFER[7] = halfHeight;
                vertexBuffer.setPositionBuffer(HighLightTextureApplication.POSITION_BUFFER);
            }
        }
    }, 
    ISO_WITH_SLOPE {
        @Override
        public void apply(final Entity3D entity3D, final byte slope, final float heightPx, final float size, final float halfWidth, final float halfHeight) {
            assert entity3D.getNumGeometries() == 1;
            final GeometryMesh geometryMesh = (GeometryMesh)entity3D.getGeometry(0);
            final VertexBufferPCT vertexBuffer = geometryMesh.getVertexBuffer();
            HighLightTextureApplication.POSITION_BUFFER[0] = -halfWidth;
            HighLightTextureApplication.POSITION_BUFFER[1] = -halfHeight;
            HighLightTextureApplication.POSITION_BUFFER[2] = -halfWidth;
            HighLightTextureApplication.POSITION_BUFFER[3] = halfHeight;
            HighLightTextureApplication.POSITION_BUFFER[4] = halfWidth;
            HighLightTextureApplication.POSITION_BUFFER[5] = -halfHeight;
            HighLightTextureApplication.POSITION_BUFFER[6] = halfWidth;
            HighLightTextureApplication.POSITION_BUFFER[7] = halfHeight;
            vertexBuffer.setPositionBuffer(HighLightTextureApplication.POSITION_BUFFER);
        }
    };
    
    private static final float[] POSITION_BUFFER;
    
    public abstract void apply(final Entity3D p0, final byte p1, final float p2, final float p3, final float p4, final float p5);
    
    static {
        POSITION_BUFFER = new float[8];
    }
}
