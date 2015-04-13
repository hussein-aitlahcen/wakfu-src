package com.ankamagames.wakfu.client.core.game.protector;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.xulor2.component.map.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;

public class TerritoryParentMapZoneDescription extends WakfuParentMapZoneDescription
{
    private static final Logger m_logger;
    private static int GUID;
    private short m_instanceId;
    private boolean m_altitudeIsSet;
    
    public TerritoryParentMapZoneDescription() {
        super(TerritoryParentMapZoneDescription.GUID++, null, null, null);
        this.m_altitudeIsSet = false;
    }
    
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    @Override
    public short getAltitudeAt00() {
        if (!this.m_altitudeIsSet) {
            this.setAltitudeAt00(WorldInfoManager.getInstance().getInfo(this.m_instanceId).m_altitude);
            this.m_altitudeIsSet = true;
        }
        return super.getAltitudeAt00();
    }
    
    @Override
    public Color getZoneColor() {
        final List<PartitionListMapZoneDescription> children = this.getChildren();
        if (children.isEmpty()) {
            return TerritoryViewConstants.NONE;
        }
        final PartitionListMapZoneDescription mapZoneDesc = children.get(0);
        return mapZoneDesc.getZoneColor();
    }
    
    @Override
    public void onLoad(final AbstractMapManager mapManager) {
        throw new UnsupportedOperationException("On ne devrait pas charger depuis cette zone");
    }
    
    @Override
    public boolean canZoomIn() {
        return false;
    }
    
    @Override
    public void zoomIn(final AbstractMapManager mapManager) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)TerritoryParentMapZoneDescription.class);
        TerritoryParentMapZoneDescription.GUID = 0;
    }
}
