package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.component.map.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import gnu.trove.*;

public class SubInstanceParentMapZoneDescription extends WakfuParentMapZoneDescription
{
    private short m_instanceId;
    
    public SubInstanceParentMapZoneDescription(final int id, final short instanceId) {
        super(id, null, null, null);
        this.m_instanceId = instanceId;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public int getTerritoryId() {
        return this.getId();
    }
    
    @Override
    public String getMapUrl() {
        try {
            final String path = String.format(WakfuConfiguration.getContentPath("fullSubMapPath"), this.getInstanceId(), this.getTerritoryId() / 100);
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
            if (zone.getInstanceId() == this.m_instanceId) {
                list.add(zone);
            }
        }
        return list;
    }
    
    @Override
    public void onLoad(final AbstractMapManager mapManager) {
        final TShortArrayList list = new TShortArrayList();
        list.add(this.m_instanceId);
        mapManager.getLandMarkHandler().loadMaps(list);
    }
    
    @Override
    public boolean canZoomIn() {
        return false;
    }
    
    @Override
    public void zoomIn(final AbstractMapManager mapManager) {
    }
}
