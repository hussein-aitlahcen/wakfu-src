package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;
import com.ankamagames.xulor2.component.map.*;

public class InstanceParentMapZoneDescription extends WakfuParentMapZoneDescription
{
    private int m_firstSubTerritoryId;
    private boolean m_hasLoadedSubTerritories;
    
    public InstanceParentMapZoneDescription(final int id, final boolean loadSubTerritories) {
        super(id, null, null, null);
        this.m_firstSubTerritoryId = -1;
        if (loadSubTerritories) {
            this.loadSubMaps();
        }
    }
    
    @Override
    public String getMapUrl() {
        try {
            final String path = WakfuConfiguration.getContentPath("fullMapPath", this.getId());
            try {
                return WorldMapFileHelper.getURL(path).toString();
            }
            catch (MalformedURLException e) {
                return null;
            }
        }
        catch (PropertyException e2) {
            return null;
        }
    }
    
    public short getInstanceId() {
        return (short)this.getId();
    }
    
    public boolean isHasLoadedSubTerritories() {
        return this.m_hasLoadedSubTerritories;
    }
    
    public void loadSubMaps() {
        final int[] ids = TerritoryManager.INSTANCE.getTerritoriesId();
        final short instanceId = this.getInstanceId();
        for (final int id : ids) {
            final Territory territory = (Territory)TerritoryManager.INSTANCE.getTerritory(id);
            if (territory.getInstanceId() == instanceId) {
                final WakfuParentMapZoneDescription subZone = MapManagerHelper.checkMapZoneSubInstanceCreation(territory.getId(), instanceId);
                if (subZone != null) {
                    ((DefaultParentMapZoneDescription<InstanceParentMapZoneDescription>)subZone).setParent(this);
                }
                this.m_firstSubTerritoryId = territory.getId();
            }
        }
        this.m_hasLoadedSubTerritories = true;
    }
    
    @Override
    public List<PartitionListMapZoneDescription> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public Collection<? extends ParentMapZoneDescription> getDisplayedZones() {
        final TIntObjectHashMap<WakfuParentMapZoneDescription> zones = MapZoneManager.getInstance().getZones(MapZoneManager.MapZoneType.TERRITORY);
        if (zones == null) {
            return (Collection<? extends ParentMapZoneDescription>)Collections.emptyList();
        }
        final Collection<ParentMapZoneDescription> list = new ArrayList<ParentMapZoneDescription>();
        final TIntObjectIterator<WakfuParentMapZoneDescription> it = zones.iterator();
        while (it.hasNext()) {
            it.advance();
            final TerritoryParentMapZoneDescription zone = it.value();
            if (zone.getInstanceId() == this.getId()) {
                list.add(zone);
            }
        }
        return list;
    }
    
    @Override
    public void onLoad(final AbstractMapManager mapManager) {
        final short instanceId = this.getInstanceId();
        TerritoriesView.INSTANCE.updateTerritoriesMapZoneDescriptions(instanceId);
        final TShortArrayList list = new TShortArrayList();
        list.add(instanceId);
        mapManager.getLandMarkHandler().loadMaps(list);
    }
    
    @Override
    public boolean canZoomIn() {
        final WakfuParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.SUB_INSTANCE, this.getTerritoryId());
        return zone != null;
    }
    
    @Override
    public void zoomIn(final AbstractMapManager mapManager) {
        final WakfuParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.SUB_INSTANCE, this.getTerritoryId());
        if (zone != null) {
            mapManager.setMap(zone);
        }
    }
    
    private int getTerritoryId() {
        int territoryId = this.m_firstSubTerritoryId;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getInstanceId() == this.getInstanceId()) {
            final Territory territory = TerritoriesView.INSTANCE.getFromWorldPosition(localPlayer.getWorldCellX(), localPlayer.getWorldCellY());
            if (territory != null) {
                territoryId = territory.getId();
            }
        }
        return territoryId;
    }
}
