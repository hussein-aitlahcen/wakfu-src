package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.kernel.core.common.*;

public class EntityConstructor
{
    private static int addGeometry(final float[] positionBuffer, final Color c, final float alpha, final float lineWidth, final GeometryMesh.MeshType type, final boolean stipple, final Entity3D entity) {
        final GLGeometryMesh geom = GLGeometryMesh.Factory.newPooledInstance();
        final int numVertices = positionBuffer.length / 2;
        final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(numVertices);
        final IndexBuffer indexBuffer = IndexBuffer.INDICES;
        vertexBuffer.setNumVertices(numVertices);
        vertexBuffer.setPositionBuffer(positionBuffer);
        for (int i = 0, size = vertexBuffer.getNumVertices(); i < size; ++i) {
            vertexBuffer.setVertexColor(i, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
        geom.create(type, vertexBuffer, indexBuffer);
        geom.setLineWidth(lineWidth);
        geom.setEnableLineStipple(stipple);
        final int result = entity.addGeometry(geom);
        vertexBuffer.removeReference();
        geom.removeReference();
        return result;
    }
    
    public static void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap, final Color color, final Entity3D entity) {
        if (width == 0 || height == 0) {
            return;
        }
        final GeometrySprite geom = createGeom(left, top, width, height, color);
        final Texture texture = attachTextureToGeom(pixmap, geom);
        entity.addTexturedGeometry(geom, texture, null);
        geom.removeReference();
    }
    
    public static void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap, final Color color, final EntitySprite entity) {
        if (width == 0 || height == 0) {
            return;
        }
        final GeometrySprite geom = createGeom(left, top, width, height, color);
        final Texture texture = attachTextureToGeom(pixmap, geom);
        entity.setTexture(texture);
        entity.setGeometry(geom);
        geom.removeReference();
    }
    
    public static void addGeometry(final int left, final int top, final Texture texture, final Color color, final EntitySprite entity) {
        final Pixmap pixmap = new Pixmap(texture);
        final GeometrySprite geom = createGeom(left, top, pixmap.getWidth(), pixmap.getHeight(), color);
        attachTextureToGeom(pixmap, geom);
        entity.setTexture(texture);
        entity.setGeometry(geom);
        geom.removeReference();
    }
    
    private static Texture attachTextureToGeom(final Pixmap pixmap, final GeometrySprite geom) {
        if (pixmap == null) {
            return null;
        }
        geom.setTextureCoordinates(pixmap.getTop(), pixmap.getLeft(), pixmap.getBottom(), pixmap.getRight(), pixmap.getRotation());
        return pixmap.getTexture();
    }
    
    public static GeometrySprite createGeom(final int left, final int top, final int width, final int height, final Color color) {
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        geom.setBounds(top, left, width, height);
        geom.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        return geom;
    }
}
