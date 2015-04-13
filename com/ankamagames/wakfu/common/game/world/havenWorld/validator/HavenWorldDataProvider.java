package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class HavenWorldDataProvider
{
    private static final Logger m_logger;
    private final BuildingStructFactory m_factory;
    private long m_totalKama;
    private int m_totalWorkers;
    private long m_kamaCost;
    private long m_resourcesCost;
    private int m_usedWorkers;
    private int m_currentUsedWorkers;
    private int m_addedWorkers;
    private short m_worldId;
    private final TLongObjectHashMap<AbstractBuildingStruct> m_buildings;
    private int m_resources;
    
    public HavenWorldDataProvider(final BuildingStructFactory factory) {
        super();
        this.m_buildings = new TLongObjectHashMap<AbstractBuildingStruct>();
        this.m_factory = factory;
    }
    
    public HavenWorldDataProvider(final HavenWorldDataProvider dataProvider) {
        super();
        this.m_buildings = new TLongObjectHashMap<AbstractBuildingStruct>();
        this.m_factory = dataProvider.m_factory;
        this.m_totalKama = dataProvider.m_totalKama;
        this.m_totalWorkers = dataProvider.m_totalWorkers;
        this.m_kamaCost = dataProvider.m_kamaCost;
        this.m_resourcesCost = dataProvider.m_resourcesCost;
        this.m_usedWorkers = dataProvider.m_usedWorkers;
        this.m_currentUsedWorkers = dataProvider.m_currentUsedWorkers;
        this.m_addedWorkers = dataProvider.m_addedWorkers;
        this.m_resources = dataProvider.m_resources;
        this.m_buildings.putAll(dataProvider.m_buildings);
    }
    
    public long getTotalKama() {
        return this.m_totalKama;
    }
    
    public long getKamaCost() {
        return this.m_kamaCost;
    }
    
    public long getRemainingKama() {
        return this.getTotalKama() - this.getKamaCost();
    }
    
    public int getTotalWorkers() {
        return this.m_totalWorkers + this.m_addedWorkers;
    }
    
    public int getUsedWorkers() {
        return this.m_usedWorkers;
    }
    
    public int getCurrentUsedWorkers() {
        return this.m_currentUsedWorkers;
    }
    
    public int getCurrentRemainingWorkers() {
        return this.getTotalWorkers() - this.getUsedWorkers() - this.getCurrentUsedWorkers();
    }
    
    public int getRemainingWorkers() {
        return this.getTotalWorkers() - this.getUsedWorkers();
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public boolean forEachBuilding(final TObjectProcedure<AbstractBuildingStruct> procedure) {
        return this.m_buildings.forEachValue(procedure);
    }
    
    public void refresh(final HavenWorld world) {
        this.refresh(this.m_totalKama, world);
    }
    
    public void refresh(final long totalKama, final HavenWorld world) {
        this.m_buildings.clear();
        this.m_usedWorkers = 0;
        this.m_addedWorkers = 0;
        this.m_kamaCost = 0L;
        this.m_resourcesCost = 0L;
        this.m_totalKama = totalKama;
        this.m_totalWorkers = world.getTotalWorkers();
        this.m_resources = world.getResources();
        this.m_kamaCost = 0L;
        this.m_resourcesCost = 0L;
        this.m_currentUsedWorkers = 0;
        world.forEachBuilding(new TObjectProcedure<Building>() {
            @Override
            public boolean execute(final Building object) {
                final AbstractBuildingStruct info = HavenWorldDataProvider.this.m_factory.createBuildingStruct(object);
                HavenWorldDataProvider.this.m_buildings.put(info.getBuildingUid(), info);
                HavenWorldDataProvider.this.m_usedWorkers += info.getDefinition().getNeededWorkers();
                return true;
            }
        });
        this.m_worldId = world.getWorldInstanceId();
    }
    
    public void createBuildingEntry(final AbstractBuildingStruct item) {
        final BuildingCatalogEntry entry = item.getCatalogEntry();
        this.m_currentUsedWorkers += entry.getNeededWorkers();
        this.m_addedWorkers += entry.getGrantedWorkers();
        final AbstractBuildingDefinition definition = item.getDefinition();
        final AbstractBuildingDefinition firstBuildingFor = BuildingDefinitionHelper.getFirstBuildingFor(definition);
        this.m_kamaCost += BuildingValidationHelper.getAdjustedKamaCost(firstBuildingFor, this.m_worldId);
        this.m_resourcesCost += BuildingValidationHelper.getAdjustedResourceCost(firstBuildingFor, this.m_worldId);
        this.m_buildings.put(item.getBuildingUid(), item);
    }
    
    public boolean removeBuilding(final AbstractBuildingStruct info) {
        final long uid = info.getBuildingUid();
        final AbstractBuildingDefinition definition = info.getDefinition();
        if (info.isTemporary()) {
            final BuildingCatalogEntry entry = info.getCatalogEntry();
            this.m_currentUsedWorkers -= entry.getNeededWorkers();
            this.m_addedWorkers -= entry.getGrantedWorkers();
            final AbstractBuildingDefinition firstBuildingFor = BuildingDefinitionHelper.getFirstBuildingFor(definition);
            this.m_kamaCost -= BuildingValidationHelper.getAdjustedKamaCost(firstBuildingFor, this.m_worldId);
            this.m_resourcesCost -= BuildingValidationHelper.getAdjustedResourceCost(firstBuildingFor, this.m_worldId);
        }
        else if (BuildingDefinitionHelper.getState(info.getDefinition()) != BuildingDefinitionHelper.ConstructionState.DONE) {
            this.m_usedWorkers -= definition.getNeededWorkers();
            this.m_addedWorkers -= definition.getGrantedWorkers();
        }
        return this.m_buildings.remove(uid) != null;
    }
    
    public void setPatch(final PatchCatalogEntry patch) {
        this.m_kamaCost += patch.getKamaCost();
    }
    
    public void addPartition(final PartitionCatalogEntry partition) {
        this.m_kamaCost += partition.getKamaCost();
    }
    
    public long getResourcesCost() {
        return this.m_resourcesCost;
    }
    
    public int getResources() {
        return this.m_resources;
    }
    
    public void setResources(final int resources) {
        this.m_resources = resources;
    }
    
    public boolean hasMoney(final int buyingCost) {
        return buyingCost <= this.getRemainingKama();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldDataProvider.class);
    }
}
