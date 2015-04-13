package com.ankamagames.wakfu.client.core.game.protector;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.xulor2.component.map.*;
import gnu.trove.*;

public final class TerritoriesView implements ProtectorNationChangedListener
{
    public static final TerritoriesView INSTANCE;
    private static final Logger m_logger;
    private short m_currentInstanceId;
    private final TIntObjectHashMap<Territory> m_territories;
    private short m_currentMapTerritoriesInstanceId;
    private final TIntObjectHashMap<Territory> m_mapTerritories;
    
    private TerritoriesView() {
        super();
        this.m_territories = new TIntObjectHashMap<Territory>();
        this.m_currentMapTerritoriesInstanceId = -1;
        this.m_mapTerritories = new TIntObjectHashMap<Territory>();
    }
    
    public TIntObjectIterator<Territory> getMapIterator() {
        return this.m_mapTerritories.iterator();
    }
    
    private void updateTerritoryList() {
        this.m_territories.clear();
        final int[] arr$;
        final int[] ids = arr$ = TerritoryManager.INSTANCE.getTerritoriesId();
        for (final int id : arr$) {
            final Territory territory = (Territory)TerritoryManager.INSTANCE.getTerritory(id);
            if (territory.getInstanceId() == this.m_currentInstanceId) {
                this.m_territories.put(territory.getId(), territory);
            }
        }
    }
    
    public Territory getById(final int id) {
        return this.m_territories.get(id);
    }
    
    public Territory getFromWorldPosition(final int x, final int y) {
        final TIntObjectIterator<Territory> it = this.m_territories.iterator();
        while (it.hasNext()) {
            it.advance();
            final Territory territory = it.value();
            if (territory.containsWorldPosition(x, y)) {
                return territory;
            }
        }
        return null;
    }
    
    public Territory getFromPartition(final int x, final int y) {
        final TIntObjectIterator<Territory> it = this.m_territories.iterator();
        while (it.hasNext()) {
            it.advance();
            final Territory territory = it.value();
            if (territory.getPartitions().contains(x, y)) {
                return territory;
            }
        }
        return null;
    }
    
    public void onInstanceChanged(final short instanceId) {
        if (instanceId == this.m_currentInstanceId) {
            return;
        }
        this.m_currentInstanceId = instanceId;
        this.updateTerritoryList();
        this.updateTerritoriesMapZoneDescriptions(this.m_currentInstanceId, true);
    }
    
    @Override
    public void onNationChanged(final ProtectorBase protector, final Nation nation) {
        final AbstractTerritory t = protector.getTerritory();
        if (t == null) {
            return;
        }
        final Territory territory = this.m_territories.get(t.getId());
        if (territory == null) {
            return;
        }
        this.updateTerritoriesMapZoneDescriptions(this.m_currentMapTerritoriesInstanceId, true);
    }
    
    public void updateTerritoriesMapZoneDescriptions(final short instanceId) {
        this.updateTerritoriesMapZoneDescriptions(instanceId, false);
    }
    
    private void updateTerritoriesMapZoneDescriptions(final short instanceId, final boolean force) {
        if (this.m_currentMapTerritoriesInstanceId == instanceId && !force) {
            return;
        }
        this.m_currentMapTerritoriesInstanceId = instanceId;
        MapZoneManager.getInstance().removeMapZone(MapZoneManager.MapZoneType.TERRITORY);
        this.m_mapTerritories.clear();
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo(instanceId);
        if (instanceId <= 0 || info == null || !info.m_isDisplayTerritory) {
            return;
        }
        final TIntObjectHashMap<TerritoryParentMapZoneDescription> pmzds = new TIntObjectHashMap<TerritoryParentMapZoneDescription>();
        final int[] arr$;
        final int[] ids = arr$ = TerritoryManager.INSTANCE.getTerritoriesId();
        for (final int id : arr$) {
            final Territory territory = (Territory)TerritoryManager.INSTANCE.getTerritory(id);
            if (territory.getInstanceId() == instanceId) {
                if (territory.getProtector() == null) {
                    territory.setProtector(ProtectorManager.INSTANCE.getStaticProtectorByTerritoryId(territory.getId()));
                }
                this.m_mapTerritories.put(id, territory);
                final DefaultMapZoneDescription mapZone = new TerritoryMapZoneDescription(territory);
                final ProtectorBase protector = territory.getProtector();
                final int nationId = (protector != null) ? protector.getCurrentNationId() : -1;
                TerritoryParentMapZoneDescription parentMapZoneDescription = pmzds.get(nationId);
                if (parentMapZoneDescription == null) {
                    parentMapZoneDescription = new TerritoryParentMapZoneDescription();
                    pmzds.put(nationId, parentMapZoneDescription);
                    parentMapZoneDescription.setInstanceId(instanceId);
                }
                parentMapZoneDescription.addChild(mapZone);
            }
        }
        final TIntObjectIterator<TerritoryParentMapZoneDescription> it2 = pmzds.iterator();
        while (it2.hasNext()) {
            it2.advance();
            MapZoneManager.getInstance().addMapZone(MapZoneManager.MapZoneType.TERRITORY, it2.value().getId(), it2.value());
        }
        this.m_currentMapTerritoriesInstanceId = instanceId;
    }
    
    static {
        INSTANCE = new TerritoriesView();
        m_logger = Logger.getLogger((Class)TerritoriesView.class);
    }
}
