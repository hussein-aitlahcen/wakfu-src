package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.entity.batch.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import gnu.trove.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class GraphMesh
{
    private static final short STIPPLE_PATTERN = 21845;
    private EntityGroup m_entity;
    private Entity3D m_bgEntity;
    private Entity3D m_rulesEntity;
    private Entity3D m_linesEntity;
    private Entity3DBatcher m_bgBatcher;
    private Entity3DBatcher m_rulesBatcher;
    private Entity3DBatcher m_linesBatcher;
    private Color m_modulationColor;
    private int m_cellWidth;
    private int m_width;
    private int m_height;
    private Graph.GraphData m_data;
    
    public GraphMesh() {
        super();
        this.m_modulationColor = null;
        this.m_cellWidth = 0;
        this.m_data = null;
    }
    
    public void clear() {
        this.m_data = null;
    }
    
    public void setData(final Graph.GraphData data) {
        this.m_data = data;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public void setCellWidth(final int cellWidth) {
        this.m_cellWidth = cellWidth;
    }
    
    public void setModulationColor(final Color c) {
        if (this.m_modulationColor == c) {
            return;
        }
        this.m_modulationColor = c;
    }
    
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    private static int getXLineCoord(final int cellWidth, final int index) {
        return cellWidth / 2 + getXCoord(cellWidth, index);
    }
    
    private static int getXCoord(final int cellWidth, final int index) {
        return cellWidth * index;
    }
    
    private float getHeightDelta() {
        return (this.m_data != null) ? (-this.m_data.getMinValue()) : 0.0f;
    }
    
    private float getHeightFactor() {
        if (this.m_data == null) {
            return 1.0f;
        }
        return this.m_height / (this.m_data.getMaxValue() - this.m_data.getMinValue());
    }
    
    public float[] getRulesVertices() {
        final int minGraphY = (this.m_data != null) ? ((int)this.m_data.getMinValue()) : 0;
        final int maxGraphY = (this.m_data != null) ? ((int)this.m_data.getMaxValue()) : 0;
        final int minY = minGraphY / 5 * 5;
        final int maxY = maxGraphY / 5 * 5;
        final int size = (maxY - minY) / 5 + 1;
        final TFloatArrayList list = new TFloatArrayList();
        for (int i = 0; i < size; ++i) {
            final float y = minY + 5 * i;
            list.add(0.0f);
            list.add(y);
            list.add(this.m_width);
            list.add(y);
        }
        if (this.m_data != null) {
            for (int i = 0, s = this.m_data.getGraphElements().size(); i < s; ++i) {
                final float x = this.m_cellWidth * i;
                list.add(x);
                list.add(minGraphY);
                list.add(x);
                list.add(maxGraphY);
            }
        }
        return list.toNativeArray();
    }
    
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        final int left = margin.left + border.left + padding.left;
        final int bottom = margin.bottom + border.bottom + padding.bottom;
        this.m_width = size.width - left;
        this.m_height = size.height - bottom;
        this.m_bgEntity.clear();
        this.m_linesEntity.clear();
        this.m_rulesEntity.clear();
        TransformerSRT t = (TransformerSRT)this.m_linesEntity.getTransformer().getTransformer(0);
        t.setScale(1.0f, this.getHeightFactor(), 1.0f);
        t.setTranslation(left, this.getHeightDelta() * this.getHeightFactor() + bottom, 0.0f);
        this.m_linesEntity.getTransformer().setTransformer(0, t);
        t = (TransformerSRT)this.m_rulesEntity.getTransformer().getTransformer(0);
        t.setScale(1.0f, this.getHeightFactor(), 1.0f);
        t.setTranslation(left, this.getHeightDelta() * this.getHeightFactor() + bottom, 0.0f);
        this.m_rulesEntity.getTransformer().setTransformer(0, t);
        final Color color = (this.m_modulationColor == null) ? Color.WHITE : this.m_modulationColor;
        if (this.m_data != null) {
            final ArrayList<GraphElement> graphElements = this.m_data.getGraphElements();
            for (int i = 0, s = graphElements.size(); i < s; ++i) {
                final GraphElement elem = graphElements.get(i);
                final Pixmap pixmap = elem.getBackgroundPixmap();
                final Color elemColor = (elem.getModulationColor() != null) ? Color.mult(elem.getModulationColor(), color) : color;
                if (pixmap != null) {
                    this.addPixmapGeometry(left + getXCoord(this.m_cellWidth, i), bottom + this.m_height, this.m_cellWidth, this.m_height, pixmap, elemColor);
                }
                else {
                    this.addBackgroundColorGeometry(left + getXCoord(this.m_cellWidth, i), bottom + this.m_height, this.m_cellWidth, this.m_height, elemColor);
                }
            }
        }
        final float[] rulesVertices = this.getRulesVertices();
        final int[] rulesColorIndexes = new int[rulesVertices.length / 2];
        this.addLineGeometry(rulesVertices, new Color[] { new Color(0.0f, 0.0f, 0.0f, 0.5f) }, rulesColorIndexes, 1.0f, GeometryMesh.MeshType.Line, this.m_rulesEntity);
        if (this.m_data != null) {
            final ArrayList<GraphZone> zones = this.m_data.getGraphZones();
            for (int j = 0, s2 = zones.size(); j < s2; ++j) {
                final GraphZone graphZone = zones.get(j);
                this.addLineGeometry(graphZone.getVertices(this.m_cellWidth), graphZone.getColors(), graphZone.getColorIndexes(), 1.0f, GeometryMesh.MeshType.TriangleStrip, this.m_linesEntity);
            }
            final ArrayList<GraphLine> lines = this.m_data.getGraphLines();
            for (int k = 0, s3 = lines.size(); k < s3; ++k) {
                final GraphLine line = lines.get(k);
                this.addLineGeometry(line.getVertices(this.m_cellWidth), line.getColors(), line.getColorIndexes(), 2.0f, GeometryMesh.MeshType.LineStrip, this.m_linesEntity);
                this.addLineGeometry(line.getVertices(this.m_cellWidth), line.getColors(), line.getColorIndexes(), 2.0f, GeometryMesh.MeshType.Point, this.m_linesEntity);
            }
        }
    }
    
    public void onCheckIn() {
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_bgEntity.removeReference();
        this.m_bgEntity = null;
        this.m_rulesEntity.removeReference();
        this.m_rulesEntity = null;
        this.m_linesEntity.removeReference();
        this.m_linesEntity = null;
        this.m_modulationColor = null;
        this.m_data = null;
    }
    
    public void onCheckOut() {
        assert this.m_entity == null;
        assert this.m_rulesEntity == null;
        assert this.m_linesEntity == null;
        this.m_entity = EntityGroup.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        this.m_bgEntity = Entity3D.Factory.newPooledInstance();
        this.m_rulesEntity = Entity3D.Factory.newPooledInstance();
        this.m_linesEntity = Entity3D.Factory.newPooledInstance();
        this.m_entity.addChild(this.m_bgEntity);
        this.m_entity.addChild(this.m_rulesEntity);
        this.m_entity.addChild(this.m_linesEntity);
        this.m_rulesEntity.setPreRenderStates(new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                RenderStateManager.getInstance().enableLineStipple(true);
                RenderStateManager.getInstance().setLineStippleFactor(1);
                RenderStateManager.getInstance().setLineStipplePattern((short)21845);
            }
        });
        this.m_rulesEntity.setPostRenderStates(new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                RenderStateManager.getInstance().enableLineStipple(false);
            }
        });
        this.m_linesEntity.setPreRenderStates(new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                RenderStateManager.getInstance().enablePointSmooth(true);
                RenderStateManager.getInstance().enableLineSmooth(true);
                RenderStateManager.getInstance().setPointSize(5.0f);
            }
        });
        this.m_linesEntity.setPostRenderStates(new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                RenderStateManager.getInstance().enablePointSmooth(false);
                RenderStateManager.getInstance().enableLineSmooth(false);
            }
        });
        this.m_linesEntity.getTransformer().addTransformer(new TransformerSRT());
        this.m_rulesEntity.getTransformer().addTransformer(new TransformerSRT());
    }
    
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    private int addLineGeometry(final float[] positionBuffer, final Color[] colors, final int[] colorIndex, final float lineWidth, final GeometryMesh.MeshType type, final Entity3D entity) {
        final GLGeometryMesh geom = GLGeometryMesh.Factory.newPooledInstance();
        final int numVertices = positionBuffer.length / 2;
        final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newInstance(numVertices);
        final IndexBuffer indexBuffer = IndexBuffer.INDICES;
        vertexBuffer.setNumVertices(numVertices);
        vertexBuffer.setPositionBuffer(positionBuffer);
        for (int i = 0, size = vertexBuffer.getNumVertices(); i < size; ++i) {
            final Color c = colors[colorIndex[i]];
            vertexBuffer.setVertexColor(i, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
        geom.create(type, vertexBuffer, indexBuffer);
        geom.setLineWidth(lineWidth);
        final int result = entity.addGeometry(geom);
        geom.removeReference();
        vertexBuffer.removeReference();
        return result;
    }
    
    private void addPixmapGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap, final Color color) {
        if (width == 0 || height == 0) {
            return;
        }
        final float centerZ = 0.0f;
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        geom.setBounds(top - height / 2.0f, left - width / 2, width, height);
        geom.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        Texture texture = null;
        if (pixmap != null) {
            geom.setTextureCoordinates(pixmap.getTop(), pixmap.getLeft(), pixmap.getBottom(), pixmap.getRight(), pixmap.getRotation());
            texture = pixmap.getTexture();
        }
        this.m_bgEntity.addTexturedGeometry(geom, texture, null);
        geom.removeReference();
    }
    
    private void addBackgroundColorGeometry(final int left, final int top, final int width, final int height, final Color color) {
        if (width == 0 || height == 0) {
            return;
        }
        final GeometryMesh geom = ((MemoryObject.ObjectFactory<GeometryMesh>)GLGeometryMesh.Factory).newPooledInstance();
        final float[] vertices = { left, top - height, left + width, top - height, left + width, top, left, top };
        final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(4);
        final IndexBuffer indexBuffer = IndexBuffer.QUAD_INDICES;
        vertexBuffer.setNumVertices(4);
        vertexBuffer.setPositionBuffer(vertices);
        for (int i = 0, size = vertexBuffer.getNumVertices(); i < size; ++i) {
            vertexBuffer.setVertexColor(i, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
        geom.create(GeometryMesh.MeshType.Quad, vertexBuffer, indexBuffer);
        vertexBuffer.removeReference();
        this.m_bgEntity.addGeometry(geom);
        geom.removeReference();
    }
    
    public static class GraphElement
    {
        private Color m_modulationColor;
        private Pixmap m_backgroundPixmap;
        
        public GraphElement() {
            super();
            this.m_modulationColor = null;
            this.m_backgroundPixmap = null;
        }
        
        public Color getModulationColor() {
            return this.m_modulationColor;
        }
        
        public void setModulationColor(final Color modulationColor) {
            this.m_modulationColor = modulationColor;
        }
        
        public Pixmap getBackgroundPixmap() {
            return this.m_backgroundPixmap;
        }
        
        public void setBackgroundPixmap(final Pixmap backgroundPixmap) {
            this.m_backgroundPixmap = backgroundPixmap;
        }
    }
    
    public static class GraphLine
    {
        private float[] m_values;
        private Color[] m_color;
        private int[] m_colorIndexes;
        
        public Color[] getColors() {
            return this.m_color;
        }
        
        public void setColors(final Color[] color, final int[] colorIndexes) {
            this.m_color = color;
            this.m_colorIndexes = colorIndexes;
        }
        
        public float[] getValues() {
            return this.m_values;
        }
        
        public int[] getColorIndexes() {
            return this.m_colorIndexes;
        }
        
        public void setValues(final float[] values) {
            this.m_values = values;
        }
        
        public float[] getVertices(final int cellWidth) {
            final int size = this.m_values.length;
            final TFloatArrayList list = new TFloatArrayList();
            for (int i = 0; i < size; ++i) {
                list.add(getXLineCoord(cellWidth, i));
                list.add(this.m_values[i]);
            }
            return list.toNativeArray();
        }
    }
    
    public static class GraphZone
    {
        private ArrayList<GraphLine> m_lines;
        private Color[] m_colors;
        private int[] m_colorIndexes;
        
        public GraphZone() {
            super();
            this.m_lines = new ArrayList<GraphLine>(2);
        }
        
        public void addGraphLine(final GraphLine line) {
            this.m_lines.add(line);
        }
        
        public ArrayList<GraphLine> getLines() {
            return this.m_lines;
        }
        
        public void clear() {
            this.m_lines.clear();
        }
        
        public void setColors(final Color[] colors, final int[] indices) {
            this.m_colors = colors;
            this.m_colorIndexes = indices;
        }
        
        public Color[] getColors() {
            return this.m_colors;
        }
        
        public int[] getColorIndexes() {
            return this.m_colorIndexes;
        }
        
        public float[] getVertices(final int cellWidth) {
            if (this.m_lines.size() == 0) {
                return new float[0];
            }
            final float[] line1 = this.m_lines.get(0).getValues();
            final float[] line2 = this.m_lines.get(1).getValues();
            final int size = line1.length;
            final TFloatArrayList list = new TFloatArrayList();
            for (int i = 0; i < size; ++i) {
                list.add(getXLineCoord(cellWidth, i));
                list.add(line1[i]);
                list.add(getXLineCoord(cellWidth, i));
                list.add(line2[i]);
            }
            return list.toNativeArray();
        }
    }
}
