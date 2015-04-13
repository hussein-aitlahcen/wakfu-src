package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class MultipleImageMesh
{
    private Color m_modulationColor;
    private ArrayList<ImageData> m_imageDataList;
    private ArrayList<TextData> m_textDataList;
    private int m_x;
    private int m_y;
    private int m_chunkWidth;
    private int m_chunkHeight;
    private boolean m_pixmapsInitialized;
    private Entity3D m_entity3D;
    private EntityGroup m_entity;
    private EntityGroup m_particleEntity;
    static int m_numImageMesh;
    
    public MultipleImageMesh() {
        super();
        this.m_modulationColor = null;
        this.m_imageDataList = new ArrayList<ImageData>();
        this.m_textDataList = new ArrayList<TextData>();
        this.m_x = 0;
        this.m_y = 0;
        this.m_chunkWidth = 0;
        this.m_chunkHeight = 0;
        this.m_pixmapsInitialized = false;
    }
    
    public final int getX() {
        return this.m_x;
    }
    
    public final void setX(final int x) {
        this.m_x = x;
    }
    
    public final int getY() {
        return this.m_y;
    }
    
    public final void setY(final int y) {
        this.m_y = y;
    }
    
    public final int getHeight() {
        return this.m_chunkHeight;
    }
    
    public final void setHeight(final int height) {
        this.m_chunkHeight = height;
    }
    
    public final int getWidth() {
        return this.m_chunkWidth;
    }
    
    public final void setWidth(final int width) {
        this.m_chunkWidth = width;
    }
    
    public void addImageData(final ImageData data) {
        this.m_imageDataList.add(data);
        this.addGeometry(data);
    }
    
    public void addTextData(final TextData data) {
        this.m_textDataList.add(data);
        this.addGeometry(data);
    }
    
    public void removeImageData(final ImageData data) {
        final int index = this.m_imageDataList.indexOf(data);
        this.m_imageDataList.remove(index);
        this.m_entity3D.removeTextureGeometry(this.m_entity3D.getGeometry(index));
    }
    
    public final boolean isPixmapInitialized() {
        return this.m_pixmapsInitialized;
    }
    
    public final void setModulationColor(final Color modulationColor) {
        if (modulationColor == this.m_modulationColor) {
            return;
        }
        if (modulationColor != null) {
            this.m_entity3D.setColor(modulationColor.getRed(), modulationColor.getGreen(), modulationColor.getBlue(), modulationColor.getAlpha());
        }
        else {
            this.m_entity3D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        this.m_modulationColor = modulationColor;
    }
    
    public final Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void clear() {
        for (int i = this.m_entity3D.getNumGeometries() - 1; i >= 0; --i) {
            this.m_entity3D.removeTextureGeometry(this.m_entity3D.getGeometry(0));
        }
        this.m_imageDataList.clear();
        this.m_textDataList.clear();
    }
    
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        this.m_entity.setVisible(true);
        this.m_entity3D.setVisible(true);
        this.m_particleEntity.setVisible(true);
        final int left = margin.left + border.left + padding.left;
        final int bottom = margin.bottom + border.bottom + padding.bottom;
        this.m_entity.getTransformer().setTranslation(0, this.m_x, this.m_y);
        if (this.m_modulationColor != null) {
            this.m_entity3D.setColor(this.m_modulationColor.getRed(), this.m_modulationColor.getGreen(), this.m_modulationColor.getBlue(), this.m_modulationColor.getAlpha());
            for (int i = 0, isize = this.m_textDataList.size(); i < isize; ++i) {
                this.m_textDataList.get(i).getParticle().setGlobalColor(this.m_modulationColor.getRed() * this.m_modulationColor.getAlpha(), this.m_modulationColor.getGreen() * this.m_modulationColor.getAlpha(), this.m_modulationColor.getBlue() * this.m_modulationColor.getAlpha(), this.m_modulationColor.getAlpha());
            }
        }
        else {
            this.m_entity3D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            for (int i = 0, isize = this.m_textDataList.size(); i < isize; ++i) {
                this.m_textDataList.get(i).getParticle().setGlobalColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        for (int i = 0, listSize = this.m_imageDataList.size(); i < listSize; ++i) {
            final ImageData imageData = this.m_imageDataList.get(i);
            final int x = imageData.getX() * this.m_chunkWidth + left;
            final int y = imageData.getY() * this.m_chunkHeight + bottom + size.height;
            final GeometrySprite geometry = (GeometrySprite)this.m_entity3D.getGeometry(i);
            geometry.setTopLeft(y, x);
        }
        for (int i = 0, listSize = this.m_textDataList.size(); i < listSize; ++i) {
            final TextData textData = this.m_textDataList.get(i);
            final GeometryMesh geom = (GeometryMesh)this.m_entity3D.getGeometry(i + this.m_imageDataList.size());
            this.setTextGeometryPosition(geom.getVertexBuffer(), textData);
        }
    }
    
    public final void onCheckIn() {
        this.m_imageDataList.clear();
        this.m_textDataList.clear();
        this.m_modulationColor = null;
        this.m_entity3D.removeReference();
        this.m_entity3D = null;
    }
    
    private void addGeometry(final ImageData data) {
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        final Pixmap pixmap = data.getPixmap();
        geom.setTextureCoordinates(pixmap.getTop(), pixmap.getLeft(), pixmap.getBottom(), pixmap.getRight());
        geom.setSize(pixmap.getWidth(), pixmap.getHeight());
        this.m_entity3D.addTexturedGeometry(geom, pixmap.getTexture(), null);
        geom.removeReference();
    }
    
    private void setTextGeometryPosition(final VertexBufferPCT vertexBuffer, final TextData data) {
        final float[] positionBuffer = { data.getX(), data.getY(), data.getX() + data.getWidth(), data.getY(), data.getX() + data.getWidth(), data.getY(), data.getX() + data.getWidth(), data.getY() + data.getHeight(), data.getX() + data.getWidth(), data.getY() + data.getHeight(), data.getX(), data.getY() + data.getHeight(), data.getX(), data.getY() + data.getHeight(), data.getX(), data.getY() };
        vertexBuffer.setPositionBuffer(positionBuffer);
    }
    
    private void addGeometry(final TextData data) {
        final GeometryMesh geom = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        final int numVertices = 16;
        final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(16);
        final IndexBuffer indexBuffer = IndexBuffer.INDICES;
        vertexBuffer.setNumVertices(16);
        for (int i = 0, size = vertexBuffer.getNumVertices(); i < size; ++i) {
            vertexBuffer.setVertexColor(i, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        geom.create(GeometryMesh.MeshType.Line, vertexBuffer, indexBuffer);
        this.m_entity3D.addGeometry(geom);
        geom.removeReference();
        vertexBuffer.removeReference();
    }
    
    public final void onCheckOut() {
        assert this.m_entity3D == null;
        this.m_entity = EntityGroup.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        this.m_entity.getTransformer().addTransformer(new TransformerSRT());
        this.m_entity3D = Entity3D.Factory.newPooledInstance();
        this.m_particleEntity = EntityGroup.Factory.newPooledInstance();
        this.m_particleEntity.m_owner = this;
        this.m_entity.addChild(this.m_entity3D);
        this.m_entity.addChild(this.m_particleEntity);
    }
    
    public final EntityGroup getEntity() {
        return this.m_entity;
    }
    
    public EntityGroup getParticleEntity() {
        return this.m_particleEntity;
    }
    
    static {
        MultipleImageMesh.m_numImageMesh = 0;
    }
}
