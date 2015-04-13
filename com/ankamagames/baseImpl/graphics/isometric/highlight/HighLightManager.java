package com.ankamagames.baseImpl.graphics.isometric.highlight;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public final class HighLightManager implements RenderProcessHandler
{
    private static final Logger m_logger;
    private static final HighLightManager m_instance;
    private final HashMap<String, HighLightLayer> m_layers;
    private Texture m_texture;
    private final ArrayList<HighLightLayer> m_layersToProcess;
    private static final boolean NEED_SORTING = true;
    private static EntityProcedure REMOVE_ENTITY_FROM_SCENE;
    
    private HighLightManager() {
        super();
        this.m_layersToProcess = new ArrayList<HighLightLayer>();
        this.m_layers = new HashMap<String, HighLightLayer>(18);
    }
    
    public static HighLightManager getInstance() {
        return HighLightManager.m_instance;
    }
    
    public void setTexture(final String textureFilePath) {
        if (this.m_texture != null) {
            this.m_texture.removeReference();
        }
        final String textureFileName = FileHelper.getNameWithoutExt(textureFilePath);
        final long textureName = Engine.getTextureName(textureFileName);
        (this.m_texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), textureName, textureFilePath, false)).addReference();
    }
    
    public HighLightLayer createLayer(final String name, final HighLightTextureApplication textureApplication) {
        return this.createLayer(name, this.m_texture, textureApplication);
    }
    
    public HighLightLayer createLayer(final String name) throws Exception {
        assert this.m_texture != null : "Aucune texture par d\u00e9faut n'est d\u00e9finie !";
        return this.createLayer(name, this.m_texture);
    }
    
    public HighLightLayer createLayer(final String name, final Texture texture) {
        return this.createLayer(name, texture, HighLightTextureApplication.ISO);
    }
    
    public HighLightLayer createLayer(final String name, final Texture texture, final HighLightTextureApplication textureApplication) {
        this.removeLayer(name);
        final HighLightLayer layer = new HighLightLayer(name, texture, textureApplication);
        this.m_layers.put(name, layer);
        return layer;
    }
    
    public void removeLayer(final String name) {
        final HighLightLayer layer = this.m_layers.remove(name);
        if (layer != null) {
            layer.release();
        }
    }
    
    public HighLightLayer getLayer(final String name) {
        return this.m_layers.get(name);
    }
    
    private static void clearLayer(final HighLightLayer layer) {
        assert layer != null;
        layer.clear();
    }
    
    public void clearLayer(final String layerName) {
        final HighLightLayer layerSearched = this.getLayer(layerName);
        if (layerSearched != null) {
            clearLayer(layerSearched);
        }
    }
    
    public void clear() {
        for (final HighLightLayer layer : this.m_layers.values()) {
            clearLayer(layer);
        }
    }
    
    public final void prepareElementBeforeRendering(final IsoWorldScene scene, final HighLightedElement displayedElement, final float alpha) {
        final int layerCount = this.m_layersToProcess.size();
        if (layerCount == 0) {
            return;
        }
        final int sceneElementUnit = (int)Math.floor(scene.getElevationUnit());
        final float size = 43.0f;
        final long handle = displayedElement.getLayerReference();
        for (int i = 0; i < layerCount; ++i) {
            final HighLightLayer layer = this.m_layersToProcess.get(i);
            final HighLightEntity entity = layer.getEntity(handle);
            if (entity != null) {
                if (!layer.m_addedToScene.add(handle)) {
                    displayedElement.setZOrder(entity);
                }
                else {
                    if (entity.getNumGeometries() == 0) {
                        HighLightManager.m_logger.error((Object)("probl\u00e8me d'hightlight " + displayedElement.toString()));
                        entity.m_transformed = true;
                        return;
                    }
                    final float[] color = layer.getColor();
                    final GeometryMesh geometryMesh = (GeometryMesh)entity.getGeometry(0);
                    geometryMesh.setColor(color[0] * 0.5f, color[1] * 0.5f, color[2] * 0.5f, color[3] * alpha);
                    displayedElement.transformHighLightEntity(scene, entity, 43.0f, layer.getInitialTextureSize(), sceneElementUnit, layer.getTextureApplication(), 0);
                    entity.m_transformed = true;
                    assert entity.getNumReferences() >= 0;
                    scene.addEntity(entity, true);
                }
            }
        }
    }
    
    @Override
    public void process(final IsoWorldScene scene, final int deltaTime) {
        HighLightManager.REMOVE_ENTITY_FROM_SCENE.setScene(scene);
        this.m_layersToProcess.clear();
        for (final HighLightLayer layer : this.m_layers.values()) {
            layer.forEachEntity(HighLightManager.REMOVE_ENTITY_FROM_SCENE);
            if (layer.isVisible() && !layer.isEmpty()) {
                layer.m_addedToScene.clear();
                this.insertLayerToProcess(layer);
            }
        }
    }
    
    private void insertLayerToProcess(final HighLightLayer layer) {
        final int size = this.m_layersToProcess.size();
        if (size == 0) {
            this.m_layersToProcess.add(layer);
            return;
        }
        if (layer.m_priority >= this.m_layersToProcess.get(size - 1).m_priority) {
            this.m_layersToProcess.add(layer);
            return;
        }
        for (int i = 0; i < size; ++i) {
            if (layer.m_priority < this.m_layersToProcess.get(i).m_priority) {
                this.m_layersToProcess.add(i, layer);
                break;
            }
        }
    }
    
    @Override
    public void prepareBeforeRendering(final IsoWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
    }
    
    public boolean add(final long handle, final String name) {
        final HighLightLayer layer = this.getLayer(name);
        if (layer == null) {
            HighLightManager.m_logger.error((Object)("le layer " + name + " n'exsite pas"));
            return false;
        }
        layer.add(handle);
        return true;
    }
    
    public boolean contains(final Point3 pt) {
        final long hash = getHandle(pt);
        for (final HighLightLayer layer : this.m_layers.values()) {
            if (layer.contains(hash)) {
                return true;
            }
        }
        return false;
    }
    
    public void remove(final long handle, final String name) {
        final HighLightLayer layer = this.getLayer(name);
        if (layer == null) {
            HighLightManager.m_logger.error((Object)("le layer " + name + " n'exsite pas"));
            return;
        }
        layer.remove(handle);
    }
    
    public static long getHandle(final int x, final int y, final int z) {
        return PositionValue.toLong(x, y, (short)z);
    }
    
    public static long getHandle(final Point3 coord) {
        return getHandle(coord.getX(), coord.getY(), coord.getZ());
    }
    
    public static Point3 getCoord(final long handle) {
        return PositionValue.fromLong(handle);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HighLightManager.class);
        m_instance = new HighLightManager();
        HighLightManager.REMOVE_ENTITY_FROM_SCENE = new EntityProcedure() {
            @Override
            public boolean execute(final HighLightEntity entity) {
                assert this.m_scene != null;
                this.m_scene.removeEntity(entity, true);
                return true;
            }
        };
    }
    
    private abstract static class EntityProcedure implements TObjectProcedure<HighLightEntity>
    {
        IsoWorldScene m_scene;
        
        public void setScene(final IsoWorldScene scene) {
            this.m_scene = scene;
        }
    }
}
