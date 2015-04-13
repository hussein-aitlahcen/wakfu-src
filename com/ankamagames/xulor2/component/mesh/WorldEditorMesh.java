package com.ankamagames.xulor2.component.mesh;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class WorldEditorMesh
{
    private static final Logger m_logger;
    private static final Color GRID_COLOR;
    private static final int CELL_WIDTH = 86;
    private static final int CELL_HEIGHT = 43;
    private static final float MIN_ZOOM = 0.06f;
    private static final float MAX_ZOOM = 1.0f;
    private static final float TRANSLATION_FACTOR = 0.5f;
    private static final float PATCH_HEIGHT = 193.5f;
    private static final float PATCH_WIDTH = 387.0f;
    private static final Comparator<Entity> ENTITY_COMPARATOR;
    private float m_patchScale;
    private float m_buildingScale;
    private final TransformerSRT m_transformerTranslate;
    private final TransformerSRT m_transformerScale;
    private final EntityGroup m_root;
    private Entity3D m_gridEntity;
    private Entity3D m_partitionEntity;
    private final SpriteList m_groundLayer;
    private final SpriteList m_buildingLayer;
    private boolean m_drawGrid;
    private final Rect m_gridBounds;
    
    public WorldEditorMesh() {
        super();
        this.m_transformerTranslate = new TransformerSRT();
        this.m_transformerScale = new TransformerSRT();
        this.m_groundLayer = new SpriteList();
        this.m_buildingLayer = new SpriteList();
        this.m_drawGrid = false;
        this.m_gridBounds = new Rect();
        this.m_root = EntityGroup.Factory.newPooledInstance();
        this.m_root.m_owner = this;
        this.m_root.getTransformer().addTransformer(this.m_transformerTranslate);
        this.m_root.getTransformer().addTransformer(this.m_transformerScale);
    }
    
    public void setBuildingAlpha(final float alpha) {
        this.m_buildingLayer.setAlpha(alpha);
    }
    
    public void setGroundAlpha(final float alpha) {
        this.m_groundLayer.setAlpha(alpha);
    }
    
    public void setImageScale(final float patchScale, final float buildingScale) {
        this.m_patchScale = patchScale;
        this.m_buildingScale = buildingScale;
    }
    
    public void setSize(final int width, final int height) {
        this.m_transformerScale.setTranslation(width * 0.5f, height * 0.5f, 0.0f);
    }
    
    public void setGridBound(final Rect bounds) {
        this.m_gridBounds.set(bounds.m_xMin, bounds.m_xMax + 1, bounds.m_yMin, bounds.m_yMax + 1);
    }
    
    public EntityGroup getEntity() {
        this.getEntities(this.m_root);
        return this.m_root;
    }
    
    public void getEntities(final EntityGroup root) {
        root.removeAllChildren();
        Collections.sort((List<Object>)this.m_groundLayer, (Comparator<? super Object>)WorldEditorMesh.ENTITY_COMPARATOR);
        Collections.sort((List<Object>)this.m_buildingLayer, (Comparator<? super Object>)WorldEditorMesh.ENTITY_COMPARATOR);
        for (final EntitySprite entity : this.m_groundLayer) {
            root.addChild(entity);
        }
        for (final EntitySprite entity : this.m_buildingLayer) {
            root.addChild(entity);
        }
        if (this.m_partitionEntity != null) {
            root.addChild(this.m_partitionEntity);
        }
        if (this.m_drawGrid) {
            root.addChild(this.m_gridEntity);
        }
    }
    
    public EntitySprite addGroundEntity(final Point2i position, final Point2i center, final Texture texture, final Color color) {
        return this.addEntity(position, center, this.m_patchScale, texture, color, this.m_groundLayer);
    }
    
    public EntitySprite addBuildingEntity(final Point2i position, final Point2i center, final Texture texture, final Color color) {
        return this.addEntity(position, center, this.m_buildingScale, texture, color, this.m_buildingLayer);
    }
    
    private EntitySprite addEntity(final Point2i position, final Point2i center, final float scale, final Texture texture, final Color color, final ArrayList<EntitySprite> layer) {
        final EntitySprite entity = EntitySprite.Factory.newPooledInstance();
        entity.setEffect(EffectManager.getInstance().getBaseEffect(), FxConstants.TRANSFORM_TECHNIQUE, FxConstants.COLOR_SCALE_FOR_UI_PARAMS);
        entity.m_cellX = position.getX();
        entity.m_cellY = position.getY();
        entity.m_zOrder = position.getX() + position.getY();
        final int cellX = position.getX() + center.getX();
        final int cellY = position.getY() + center.getY();
        final int left = MathHelper.fastFloor((cellX - cellY) * 86 * 0.5f);
        final int top = MathHelper.fastFloor(-(cellX + cellY) * 43 * 0.5f);
        final Layer texLayer = texture.getLayer(0);
        final int offsetX = texLayer.getStartX();
        final int offsetY = texLayer.getStartY();
        EntityConstructor.addGeometry(left + offsetX, top + offsetY, texture, color, entity);
        final GeometrySprite geometry = entity.getGeometry();
        geometry.setSize(MathHelper.fastFloor(geometry.getWidth() / scale), MathHelper.fastFloor(geometry.getHeight() / scale));
        geometry.setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
        layer.add(entity);
        return entity;
    }
    
    public Entity3D addPartitionEntity(final Point2i position, final Color color) {
        this.removePartitionEntity();
        this.m_partitionEntity = Entity3D.Factory.newPooledInstance();
        final GLGeometryMesh geom = GLGeometryMesh.Factory.newPooledInstance();
        final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(4);
        vertexBuffer.begin();
        final int x = MathHelper.fastFloor(position.getX() / 2.0f) * 2;
        final int y = MathHelper.fastFloor(position.getY() / 2.0f) * 2;
        addLine(vertexBuffer, x, x + 2, y, y, color);
        addLine(vertexBuffer, x + 2, x, y + 2, y + 2, color);
        vertexBuffer.end();
        geom.create(GeometryMesh.MeshType.Quad, vertexBuffer, IndexBuffer.QUAD_INDICES);
        this.m_partitionEntity.addGeometry(geom);
        geom.removeReference();
        return this.m_partitionEntity;
    }
    
    public void removePartitionEntity() {
        if (this.m_partitionEntity != null) {
            this.m_partitionEntity.removeReference();
            this.m_partitionEntity = null;
        }
    }
    
    public EntitySprite removeGroundEntity(final int cellX, final int cellY) {
        return removeEntity(cellX, cellY, this.m_groundLayer);
    }
    
    public EntitySprite removeBuildingEntity(final int cellX, final int cellY) {
        return removeEntity(cellX, cellY, this.m_buildingLayer);
    }
    
    public void removeEntity(final Entity sprite) {
        if (this.m_buildingLayer.remove(sprite)) {
            sprite.removeReference();
            return;
        }
        if (this.m_groundLayer.remove(sprite)) {
            sprite.removeReference();
        }
    }
    
    private static EntitySprite removeEntity(final int cellX, final int cellY, final ArrayList<EntitySprite> layer) {
        final EntitySprite entity = findEntity(cellX, cellY, layer);
        if (entity != null) {
            layer.remove(entity);
            entity.removeReference();
        }
        return entity;
    }
    
    public EntitySprite findGroundEntity(final int cellX, final int cellY) {
        return findEntity(cellX, cellY, this.m_groundLayer);
    }
    
    public EntitySprite findBuildingEntity(final int cellX, final int cellY) {
        return findEntity(cellX, cellY, this.m_buildingLayer);
    }
    
    private static EntitySprite findEntity(final int cellX, final int cellY, final ArrayList<EntitySprite> groundLayer) {
        for (int i = 0; i < groundLayer.size(); ++i) {
            final EntitySprite entity = groundLayer.get(i);
            if (entity.m_cellX == cellX && entity.m_cellY == cellY) {
                return entity;
            }
        }
        return null;
    }
    
    public EntitySprite findBuildingEntityUnderMouse(final int screenX, final int screenY) {
        float x = screenX - this.m_transformerScale.getTranslationConst().m_x;
        float y = screenY - this.m_transformerScale.getTranslationConst().m_y;
        x /= this.getZoomFactor();
        y /= this.getZoomFactor();
        x -= this.m_transformerTranslate.getTranslationConst().m_x;
        y -= this.m_transformerTranslate.getTranslationConst().m_y;
        for (int i = this.m_buildingLayer.size() - 1; i >= 0; --i) {
            final EntitySprite entity = this.m_buildingLayer.get(i);
            final GeometrySprite geometry = entity.getGeometry();
            if (x > geometry.getLeft() && x < geometry.getRight() && geometry.getTop() > y) {
                if (geometry.getBottom() < y) {
                    final AlphaMask alphaMask = entity.getTexture().getLayer(0).getAlphaMask();
                    final float px = (x - geometry.getLeft()) * this.m_buildingScale;
                    final float py = (geometry.getHeight() - (y - geometry.getBottom())) * this.m_buildingScale;
                    if (alphaMask == null || alphaMask.getValue((int)px, (int)py)) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }
    
    public void clear() {
        this.m_groundLayer.clear();
        this.m_buildingLayer.clear();
        this.removePartitionEntity();
        this.m_root.removeAllChildren();
    }
    
    public void dispose() {
        this.clear();
        if (this.m_gridEntity != null) {
            this.m_gridEntity.removeReference();
            this.m_gridEntity = null;
        }
        this.m_drawGrid = false;
        this.m_root.removeReference();
    }
    
    public float getScreenX(final float cellX, final float cellY) {
        final float dx = cellX - cellY;
        return dx * 86.0f * 2.0f;
    }
    
    public float getScreenY(final float cellX, final float cellY) {
        final float dy = -(cellX + cellY);
        return dy * 43.0f * 2.0f;
    }
    
    public float getZoomFactor() {
        return this.m_transformerScale.getScaleConst().m_x;
    }
    
    public void setZoomFactor(final float zoomFactor) {
        final float scale = MathHelper.clamp(zoomFactor, 0.06f, 1.0f);
        this.m_transformerScale.setScale(scale, scale, 1.0f);
    }
    
    public void setDrawGrid(final boolean drawGrid) {
        this.m_drawGrid = drawGrid;
        if (this.m_gridEntity == null) {
            this.m_gridEntity = Entity3D.Factory.newPooledInstance();
            final GLGeometryMesh geom = GLGeometryMesh.Factory.newPooledInstance();
            final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(2 * (this.m_gridBounds.width() + this.m_gridBounds.height()));
            vertexBuffer.begin();
            for (int x = this.m_gridBounds.m_xMin; x <= this.m_gridBounds.m_xMax; ++x) {
                addLine(vertexBuffer, x, x, this.m_gridBounds.m_yMin, this.m_gridBounds.m_yMax, WorldEditorMesh.GRID_COLOR);
            }
            for (int y = this.m_gridBounds.m_yMin; y <= this.m_gridBounds.m_yMax; ++y) {
                addLine(vertexBuffer, this.m_gridBounds.m_xMin, this.m_gridBounds.m_xMax, y, y, WorldEditorMesh.GRID_COLOR);
            }
            vertexBuffer.end();
            geom.create(GeometryMesh.MeshType.Line, vertexBuffer, IndexBuffer.INDICES);
            this.m_gridEntity.addGeometry(geom);
            geom.removeReference();
        }
    }
    
    private static void addLine(final VertexBufferPCT vertexBuffer, final int x0, final int x1, final int y0, final int y1, final Color color) {
        final float startX = x0 - y0;
        final float endX = x1 - y1;
        final float startY = -(x0 + y0);
        final float endY = -(x1 + y1);
        vertexBuffer.pushVertex(387.0f * startX, 193.5f * startY, 0.0f, 0.0f, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        vertexBuffer.pushVertex(387.0f * endX, 193.5f * endY, 0.0f, 0.0f, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public Point2i getCellCoordFromMouse(final int screenX, final int screenY) {
        float x = screenX - this.m_transformerScale.getTranslationConst().m_x;
        float y = screenY - this.m_transformerScale.getTranslationConst().m_y;
        x /= this.getZoomFactor();
        y /= this.getZoomFactor();
        x -= this.m_transformerTranslate.getTranslationConst().m_x;
        y -= this.m_transformerTranslate.getTranslationConst().m_y;
        x /= 86.0f;
        y /= 43.0f;
        x += 0.5f;
        y += 0.5f;
        return new Point2i(MathHelper.fastFloor(x - y), -MathHelper.fastFloor(x + y));
    }
    
    public Vector2 getCellCoord(final float x, final float y) {
        final float dx = x / 86.0f;
        final float dy = y / 43.0f;
        return new Vector2(-dx + dy, dx + dy);
    }
    
    public Vector2 getCenterCell() {
        final Vector4 translation = this.m_transformerTranslate.getTranslationConst();
        return this.getCellCoord(translation.m_x, translation.m_y);
    }
    
    public Vector2 getTranslation(final float x, final float y) {
        final float dx = x - y;
        final float dy = -(x + y);
        return new Vector2(-dx * 86.0f * 0.5f, -dy * 43.0f * 0.5f);
    }
    
    public void centerOnCell(final float x, final float y) {
        final Vector2 pos = this.getTranslation(x, y);
        this.m_transformerTranslate.setTranslation(pos.getX(), pos.getY(), 0.0f);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldEditorMesh.class);
        GRID_COLOR = new Color(0.2f, 0.2f, 0.2f, 0.3f);
        ENTITY_COMPARATOR = new Comparator<Entity>() {
            @Override
            public int compare(final Entity o1, final Entity o2) {
                return (int)(o1.m_zOrder - o2.m_zOrder);
            }
        };
    }
    
    static class SpriteList extends ArrayList<EntitySprite>
    {
        @Override
        public void clear() {
            for (final EntitySprite entity : this) {
                entity.removeReference();
            }
            super.clear();
        }
        
        public void setAlpha(final float alpha) {
            for (final EntitySprite sprite : this) {
                sprite.setColor(alpha, alpha, alpha, alpha);
            }
        }
    }
}
