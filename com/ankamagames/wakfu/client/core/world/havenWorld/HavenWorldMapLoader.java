package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.alea.topology.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.shortKey.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class HavenWorldMapLoader implements MapLoader
{
    private static final Logger m_logger;
    private static final int HAVEN_WORLD_ID_FOR_AMBIANCE = 375;
    private EnvironmentMapManager m_environmentMapMgr;
    private DisplayedScreenWorld m_displayedScreenWorld;
    private LightningMapManager m_lightningMapManager;
    private final HavenWorldTopology m_havenWorld;
    
    public HavenWorldMapLoader(final HavenWorldTopology havenWorld) {
        super();
        this.m_havenWorld = havenWorld;
    }
    
    void resetScreenWorld(final ScreenWorldPatch world) {
        this.m_displayedScreenWorld.setWorld(world, world.getValidCoordinates());
        this.prepareGroups();
    }
    
    @Override
    public void initialize(final EnvironmentMapManager environmentMapMgr, final DisplayedScreenWorld screenWorld, final LightningMapManager lightningMapManager) {
        this.m_environmentMapMgr = environmentMapMgr;
        this.m_displayedScreenWorld = screenWorld;
        this.m_lightningMapManager = lightningMapManager;
    }
    
    @Override
    public void loadMaps(final short mapCoordX, final short mapCoordY) {
        final TopologyMapPatch topologyMap = this.m_havenWorld.createTopologyMap(mapCoordX, mapCoordY);
        TopologyMapManager.insertTopologyMapPatch(this.m_havenWorld.getWorldId(), mapCoordX, mapCoordY, (short)0, topologyMap);
        final LightningMapPatch lightMap = this.m_havenWorld.createLightningMap(mapCoordX, mapCoordY);
        this.m_lightningMapManager.insertMapPatch(mapCoordX, mapCoordY, lightMap);
        final WakfuClientEnvironmentMap envMap = this.m_havenWorld.createEnvironmentMap(mapCoordX, mapCoordY);
        HavenWorldSound.INSTANCE.addAllSoundsToMap(envMap);
        this.m_environmentMapMgr.insertMapPatch(mapCoordX, mapCoordY, envMap);
        if (LocalPartitionManager.getInstance().isInit()) {
            final LocalPartition localPartition = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getLocalPartitionAt(mapCoordX, mapCoordY);
            if (localPartition != null) {
                localPartition.foreachInteractiveElement(new TObjectProcedure<ClientMapInteractiveElement>() {
                    @Override
                    public boolean execute(final ClientMapInteractiveElement object) {
                        final WakfuClientMapInteractiveElement ie = (WakfuClientMapInteractiveElement)object;
                        ie.addToTopologyMap();
                        return true;
                    }
                });
            }
        }
    }
    
    @Override
    public void prepare(final short worldId) {
        try {
            this.resetScreenWorld(this.m_havenWorld.createScreenWorld());
        }
        catch (Exception e) {
            HavenWorldMapLoader.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    private void prepareGroups() {
        final int[] maxLayer = { 1 };
        final THashMap<AbstractBuildingStruct, LayerVisibility> transformed = new THashMap<AbstractBuildingStruct, LayerVisibility>();
        final TShortObjectHashMap<short[]> layerByGroups = new TShortObjectHashMap<short[]>();
        this.m_havenWorld.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct building) {
                final EditorGroupMap model = building.getModel();
                final RenameLayers renameLayers = new RenameLayers(maxLayer[0], layerByGroups, model.getLayerCount());
                model.foreach(renameLayers);
                final int[] val$maxLayer = maxLayer;
                final int n = 0;
                val$maxLayer[n] += model.getLayerCount();
                transformed.put(building, renameLayers.getTransformed());
                return true;
            }
        });
        this.mergeVisibility(transformed, layerByGroups);
        WorldGroupManager.getInstance().insertLayersVisibility(maxLayer[0], layerByGroups);
    }
    
    private void mergeVisibility(final THashMap<AbstractBuildingStruct, LayerVisibility> transformed, final TShortObjectHashMap<short[]> layerByGroups) {
        final ArrayList<AbstractBuildingStruct> buildings = new ArrayList<AbstractBuildingStruct>();
        this.m_havenWorld.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct building) {
                buildings.add(building);
                return true;
            }
        });
        for (final AbstractBuildingStruct b1 : buildings) {
            for (final AbstractBuildingStruct b2 : buildings) {
                if (b1 == b2) {
                    continue;
                }
                final LayerVisibility visibility1 = transformed.get(b1);
                final LayerVisibility visibility2 = transformed.get(b2);
                LayerVisibility.merge(visibility1, visibility2, layerByGroups);
            }
        }
    }
    
    @Override
    public boolean acceptMap(final short mapCoordX, final short mapCoordY) {
        return this.m_havenWorld.mapExists(mapCoordX, mapCoordY);
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public void prepareAmbianceZone(final short worldId) {
        AmbienceZoneBank.getInstance().setWorldId(375);
        AmbienceZoneBank.getInstance().load();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldMapLoader.class);
    }
    
    private static class RenameLayers implements TByteObjectProcedure<byte[]>
    {
        private final int m_startIndex;
        private final TShortObjectHashMap<short[]> m_layerByGroups;
        private final LayerVisibility m_transformed;
        
        RenameLayers(final int startIndex, final TShortObjectHashMap<short[]> layerByGroups, final int layerCount) {
            super();
            this.m_startIndex = startIndex;
            this.m_layerByGroups = layerByGroups;
            this.m_transformed = new LayerVisibility(layerCount);
        }
        
        public LayerVisibility getTransformed() {
            return this.m_transformed;
        }
        
        @Override
        public boolean execute(final byte fromLayer, final byte[] layersVisible) {
            if (fromLayer == 0) {
                return true;
            }
            final short from = this.getMaskKey(fromLayer);
            final short[] visibles = new short[layersVisible.length];
            for (int i = 0; i < layersVisible.length; ++i) {
                visibles[i] = this.getMaskKey(layersVisible[i]);
            }
            assert !this.m_layerByGroups.contains(from);
            this.m_layerByGroups.put(from, visibles);
            this.m_transformed.put(from, visibles);
            return true;
        }
        
        private short getMaskKey(final short layerId) {
            if (layerId == 0) {
                return 0;
            }
            return (layerId > 0) ? ((short)(layerId + this.m_startIndex)) : ((short)(layerId - this.m_startIndex));
        }
    }
    
    private static class LayerVisibility extends ShortObjectLightWeightMap<short[]>
    {
        private short[] m_visibleFromOutdoor;
        
        LayerVisibility(final int layerCount) {
            super(layerCount);
        }
        
        public static void merge(final LayerVisibility v1, final LayerVisibility v2, final TShortObjectHashMap<short[]> layerByGroups) {
            for (int i = 0; i < v1.size(); ++i) {
                final short[] layers = v1.getQuickValue(i);
                if (Arrays.binarySearch(layers, (short)0) >= 0) {
                    final short layer = v1.getQuickKey(i);
                    final TShortHashSet set = new TShortHashSet(layerByGroups.get(layer));
                    set.addAll(v2.getVisibleFromOutdoor());
                    final short[] added = set.toArray();
                    Arrays.sort(added);
                    layerByGroups.put(layer, added);
                }
            }
        }
        
        private short[] getVisibleFromOutdoor() {
            if (this.m_visibleFromOutdoor == null) {
                final int size = this.size();
                final TShortArrayList list = new TShortArrayList(size);
                for (int i = 0; i < size; ++i) {
                    final short layer = this.getQuickKey(i);
                    if (layer <= 0) {
                        list.add(layer);
                    }
                }
                this.m_visibleFromOutdoor = list.toNativeArray();
            }
            return this.m_visibleFromOutdoor;
        }
    }
}
