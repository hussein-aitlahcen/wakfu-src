package com.ankamagames.wakfu.client.alea.graphics.tacticalView;

import com.ankamagames.framework.graphics.image.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.alea.graphics.tacticalView.map.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.geometry.*;

public class TacticalView
{
    private static final float LEFT_INC_COLOR = -0.09f;
    private static final float RIGHT_INC_COLOR = -0.18f;
    private static final float Z_INC_COLOR = 0.3f;
    private static final Color COLOR;
    private static final Color COLOR_LINE;
    private static final Color COLOR_LINE_DOTTED;
    private static final Color COLOR_NO_WALKABLE;
    private static final Logger m_logger;
    private final TacticalMap m_map;
    private final TLongObjectHashMap<Entity> m_entities;
    private final THashSet<Entity> m_usedEntity;
    private final TLongHashSet m_keyUsed;
    private final float[] _color;
    private static final boolean DISPLAY_DOTTED = false;
    
    public TacticalView(final FightMap fightMap) {
        super();
        this.m_entities = new TLongObjectHashMap<Entity>();
        this.m_usedEntity = new THashSet<Entity>();
        this.m_keyUsed = new TLongHashSet();
        this._color = new float[4];
        this.m_map = new TacticalMap(fightMap);
    }
    
    public void clear() {
        this.m_usedEntity.clear();
        this.m_entities.forEachValue(new TObjectProcedure<Entity>() {
            @Override
            public boolean execute(final Entity object) {
                if (object != null) {
                    object.removeReference();
                }
                return true;
            }
        });
    }
    
    public void prepare(final ArrayList<DisplayedScreenElement> elements) {
        this.m_usedEntity.clear();
        this.m_keyUsed.clear();
        for (int i = 0; i < elements.size(); ++i) {
            final DisplayedScreenElement element = elements.get(i);
            final long key = getKey(element);
            if (!this.m_keyUsed.contains(key)) {
                Entity entity = this.m_entities.get(key);
                if (entity == null) {
                    entity = this.createEntity(element);
                    this.m_entities.put(key, entity);
                }
                if (entity != null) {
                    this.m_keyUsed.add(key);
                    if (!this.m_usedEntity.contains(entity)) {
                        this.m_usedEntity.add(entity);
                    }
                }
            }
        }
    }
    
    private static long getKey(final LitSceneObject element) {
        final long x = element.getWorldCellX();
        final long y = element.getWorldCellY();
        final int z = element.getWorldCellAltitude();
        return (y & 0x3FFFFL) << 32 | (x & 0x3FFFFL) << 14 | (z & 0x3FFF);
    }
    
    public Collection<Entity> getEntities() {
        return this.m_usedEntity;
    }
    
    private Entity createEntity(final DisplayedScreenElement screenElement) {
        if (screenElement.isHollow()) {
            return null;
        }
        final DisplayBlock block = this.m_map.getDisplayBlockAt(screenElement.getWorldCellX(), screenElement.getWorldCellY(), screenElement.getWorldCellAltitude());
        if (block == null) {
            return null;
        }
        final Entity3D entity = this.createEntity3D(screenElement);
        this.attachGeometry(entity, GeometryMesh.MeshType.Quad, this.createCubeVB(block));
        this.attachGeometry(entity, GeometryMesh.MeshType.LineStrip, this.createCellLineVB(block), 1.0f, false);
        if (block.leftHeight != 0.0f || block.rightHeight != 0.0f) {
            this.attachGeometry(entity, GeometryMesh.MeshType.Line, this.createLineVB(block), 1.0f, false);
        }
        return entity;
    }
    
    private Entity3D createEntity3D(final DisplayedScreenElement screenElement) {
        final Entity3D entity = Entity3D.Factory.newPooledInstance();
        final Entity baseEntity = screenElement.getEntitySprite();
        entity.m_cellX = baseEntity.m_cellX;
        entity.m_cellY = baseEntity.m_cellY;
        entity.m_cellZ = baseEntity.m_cellZ;
        entity.m_renderRadius = baseEntity.m_renderRadius;
        entity.m_zOrder = baseEntity.m_zOrder;
        entity.m_height = baseEntity.m_height;
        entity.m_userFlag1 = baseEntity.m_userFlag1;
        return entity;
    }
    
    private void createDottedLines(final Entity3D entity, final int dx, final int dy) {
        final float zRatio = 4.3f;
        final int originX = (int)entity.m_cellX;
        final int originY = (int)entity.m_cellY;
        final int z = (int)entity.m_cellZ;
        int i = 1;
        while (true) {
            final int cellX = originX - i + dx;
            final int cellY = originY - i + dy;
            final Cell cell = this.m_map.getCell(cellX, cellY);
            if (cell == null) {
                break;
            }
            final Block block = cell.getBlockUnder(MathHelper.fastCeil(z - (i - 1) * 4.3f) - 1);
            if (block != null && block.maxZ != z) {
                final DisplayBlock b = new DisplayBlock(true, block.maxZ, 0, 0, cellX, cellY, block.blockLos);
                this.attachGeometry(entity, GeometryMesh.MeshType.Line, this.createCellLineLeftDottedVB(b), 1.0f, true);
            }
            ++i;
        }
    }
    
    private VertexBufferPCT createCubeVB(final DisplayBlock b) {
        final VertexBufferPCT vb = VertexBufferPCT.Factory.newPooledInstance(12);
        vb.begin();
        final float inc = 0.3f * this.m_map.getZIndexRatio(b.top);
        final float[] color = transformColor(getColor(b, this._color), inc);
        vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getRight(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterTop(), 0.0f, 0.0f, color);
        if (b.leftHeight != 0.0f) {
            final float[] leftColor = transformColor(getColor(b, this._color), inc - 0.09f);
            vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, leftColor);
            vb.pushVertex(b.getLeft(), b.getCenterY() - b.leftHeight, 0.0f, 0.0f, leftColor);
            vb.pushVertex(b.getCenterX(), b.getCenterBottom() - b.leftHeight, 0.0f, 0.0f, leftColor);
            vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, leftColor);
        }
        if (b.rightHeight != 0.0f) {
            final float[] rightColor = transformColor(getColor(b, this._color), inc - 0.18f);
            vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, rightColor);
            vb.pushVertex(b.getCenterX(), b.getCenterBottom() - b.rightHeight, 0.0f, 0.0f, rightColor);
            vb.pushVertex(b.getRight(), b.getCenterY() - b.rightHeight, 0.0f, 0.0f, rightColor);
            vb.pushVertex(b.getRight(), b.getCenterY(), 0.0f, 0.0f, rightColor);
        }
        vb.end();
        return vb;
    }
    
    private static float[] getColor(final DisplayBlock b, final float[] color) {
        final Color c = b.walkable ? TacticalView.COLOR : TacticalView.COLOR_NO_WALKABLE;
        color[0] = c.getRed();
        color[1] = c.getGreen();
        color[2] = c.getBlue();
        color[3] = c.getAlpha();
        return color;
    }
    
    private static float[] transformColor(final float[] color, final float modif) {
        final int n = 0;
        color[n] += modif;
        final int n2 = 1;
        color[n2] += modif;
        final int n3 = 2;
        color[n3] += modif;
        return color;
    }
    
    private VertexBufferPCT createCellLineVB(final DisplayBlock b) {
        final VertexBufferPCT vb = VertexBufferPCT.Factory.newPooledInstance(5);
        vb.begin();
        final float[] color = getColor(b, this._color);
        color[0] = TacticalView.COLOR_LINE.getRed();
        color[1] = TacticalView.COLOR_LINE.getGreen();
        color[2] = TacticalView.COLOR_LINE.getBlue();
        color[3] = TacticalView.COLOR_LINE.getAlpha();
        vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getRight(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterTop(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.end();
        return vb;
    }
    
    private VertexBufferPCT createLineVB(final DisplayBlock b) {
        final VertexBufferPCT vb = VertexBufferPCT.Factory.newPooledInstance(6);
        vb.begin();
        final float[] color = getColor(b, this._color);
        color[0] = TacticalView.COLOR_LINE.getRed();
        color[1] = TacticalView.COLOR_LINE.getGreen();
        color[2] = TacticalView.COLOR_LINE.getBlue();
        color[3] = TacticalView.COLOR_LINE.getAlpha();
        if (b.leftHeight != 0.0f) {
            vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, color);
            vb.pushVertex(b.getLeft(), b.getCenterY() - b.leftHeight, 0.0f, 0.0f, color);
        }
        if (b.rightHeight != 0.0f) {
            vb.pushVertex(b.getRight(), b.getCenterY() - b.rightHeight, 0.0f, 0.0f, color);
            vb.pushVertex(b.getRight(), b.getCenterY(), 0.0f, 0.0f, color);
        }
        final float centerHeight = Math.max(b.leftHeight, b.rightHeight);
        if (centerHeight != 0.0f) {
            vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, color);
            vb.pushVertex(b.getCenterX(), b.getCenterBottom() - centerHeight, 0.0f, 0.0f, color);
        }
        vb.end();
        return vb;
    }
    
    private VertexBufferPCT createCellLineLeftDottedVB(final DisplayBlock b) {
        final VertexBufferPCT vb = VertexBufferPCT.Factory.newPooledInstance(8);
        vb.begin();
        final float[] color = getColor(b, this._color);
        color[0] = TacticalView.COLOR_LINE_DOTTED.getRed();
        color[1] = TacticalView.COLOR_LINE_DOTTED.getGreen();
        color[2] = 0.0f;
        color[3] = TacticalView.COLOR_LINE_DOTTED.getAlpha();
        vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterBottom(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getRight(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getRight(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterTop(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getCenterX(), b.getCenterTop(), 0.0f, 0.0f, color);
        vb.pushVertex(b.getLeft(), b.getCenterY(), 0.0f, 0.0f, color);
        vb.end();
        return vb;
    }
    
    private void attachGeometry(final Entity3D entity, final GeometryMesh.MeshType meshType, final VertexBufferPCT vertexBuffer) {
        this.attachGeometry(entity, meshType, vertexBuffer, 0.0f, false);
    }
    
    private void attachGeometry(final Entity3D entity, final GeometryMesh.MeshType meshType, final VertexBufferPCT vertexBuffer, final float lineWidth, final boolean enableStipple) {
        final GLGeometryMesh geometry = GLGeometryMesh.Factory.newPooledInstance();
        geometry.create(meshType, vertexBuffer, IndexBuffer.INDICES);
        geometry.setLineWidth(lineWidth);
        geometry.setEnableLineStipple(enableStipple);
        entity.addGeometry(geometry);
        geometry.removeReference();
    }
    
    boolean isInside(final int x, final int y) {
        return this.m_map.getCell(x, y) != null;
    }
    
    static {
        COLOR = new Color(0.5f, 0.5f, 0.3f, 0.85f);
        COLOR_LINE = new Color(0.8f, 0.8f, 0.5f, 1.0f);
        COLOR_LINE_DOTTED = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        COLOR_NO_WALKABLE = new Color(0.4f, 0.0f, 0.0f, 0.85f);
        m_logger = Logger.getLogger((Class)TacticalView.class);
    }
}
