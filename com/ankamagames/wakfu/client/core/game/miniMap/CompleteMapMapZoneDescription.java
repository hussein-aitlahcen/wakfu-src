package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.map.*;

public class CompleteMapMapZoneDescription extends WakfuMapZoneDescription
{
    private final MapZoneManager.MapZoneType m_mapZoneType;
    private final byte m_scrollDecorator;
    
    public CompleteMapMapZoneDescription(final PartitionList list, final int id, final MapZoneManager.MapZoneType type, final String description, final byte scrollDecorator, final String animStatic, final String animHighlight, final long highlightSoundId) {
        super(list, id, null, description, null, (byte)0, animStatic, animHighlight, highlightSoundId, type.isInteractive());
        this.m_mapZoneType = type;
        this.m_scrollDecorator = scrollDecorator;
    }
    
    public MapZoneManager.MapZoneType getMapZoneType() {
        return this.m_mapZoneType;
    }
    
    @Override
    public byte getScrollDecorator() {
        return this.m_scrollDecorator;
    }
    
    @Override
    public boolean canZoomIn() {
        final ParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(this.m_mapZoneType, this.getId());
        return zone != null;
    }
    
    @Override
    public void sendZoneToMap(final MapManager mapManager) {
        final WakfuParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(this.m_mapZoneType, this.getId());
        if (zone != null && this.m_mapZoneType == MapZoneManager.MapZoneType.INSTANCE) {
            final InstanceParentMapZoneDescription instanceZone = (InstanceParentMapZoneDescription)zone;
            if (!instanceZone.isHasLoadedSubTerritories()) {
                instanceZone.loadSubMaps();
            }
        }
        mapManager.setMap(zone);
    }
}
