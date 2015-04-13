package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import gnu.trove.*;

public class SplitMapParentMapZoneDescription extends WakfuParentMapZoneDescription
{
    private final TShortArrayList m_childIds;
    private boolean m_altitudeIsSet;
    
    public SplitMapParentMapZoneDescription(final int instanceId, final TShortArrayList childInstanceId) {
        super(instanceId, null, null, null);
        this.m_altitudeIsSet = false;
        this.m_childIds = childInstanceId;
        this.loadChildren();
    }
    
    @Override
    public short getAltitudeAt00() {
        if (!this.m_altitudeIsSet) {
            final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo((short)this.getId());
            this.setAltitudeAt00((short)((worldInfo != null) ? worldInfo.m_altitude : 0));
            this.m_altitudeIsSet = true;
        }
        return super.getAltitudeAt00();
    }
    
    private void loadChildren() {
        for (int i = 0, size = this.m_childIds.size(); i < size; ++i) {
            final short id = this.m_childIds.get(i);
            final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(id);
            final PartitionList list = createPartitionList(worldInfo);
            this.addChild(new SplitMapMapzoneDescription(list, id));
        }
    }
    
    @Override
    public Color getZoneColor() {
        return Color.WHITE;
    }
    
    private static PartitionList createPartitionList(final WorldInfoManager.WorldInfo worldInfo) {
        final TIntObjectIterator<Territory> it = worldInfo.getTerritoriesIterator();
        final PartitionList list = new PartitionList();
        while (it.hasNext()) {
            it.advance();
            final Territory territory = it.value();
            list.add(territory.getPartitions());
        }
        return list;
    }
    
    @Override
    public void onLoad(final AbstractMapManager mapManager) {
    }
    
    @Override
    public boolean canZoomIn() {
        return false;
    }
    
    @Override
    public void zoomIn(final AbstractMapManager mapManager) {
    }
}
