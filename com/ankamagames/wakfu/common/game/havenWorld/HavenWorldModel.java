package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;
import com.ankamagames.framework.kernel.core.maths.*;

class HavenWorldModel implements HavenWorld
{
    private final HavenWorldDefinition m_definition;
    private GuildInfo m_guildInfo;
    private int m_resources;
    private final TLongObjectHashMap<Building> m_buildings;
    private final TIntObjectHashMap<Partition> m_partitions;
    private final List<HavenWorldListener> m_listeners;
    
    HavenWorldModel(final HavenWorldDefinition definition) {
        super();
        this.m_buildings = new TLongObjectHashMap<Building>();
        this.m_partitions = new TIntObjectHashMap<Partition>();
        this.m_listeners = new ArrayList<HavenWorldListener>();
        this.m_definition = definition;
    }
    
    @Override
    public short getWorldInstanceId() {
        return this.m_definition.getWorldInstanceId();
    }
    
    @Override
    public HavenWorldDefinition getDefinition() {
        return this.m_definition;
    }
    
    @Override
    public GuildInfo getGuildInfo() {
        return this.m_guildInfo;
    }
    
    @Override
    public Building getBuilding(final long buildingUid) {
        return this.m_buildings.get(buildingUid);
    }
    
    @Override
    public boolean forEachBuilding(final TObjectProcedure<Building> procedure) {
        return this.m_buildings.forEachValue(procedure);
    }
    
    @Override
    public int getTotalWorkers() {
        final WorkerBuildingCount count = new WorkerBuildingCount(this);
        this.forEachBuilding(count);
        return count.getTotalWorker();
    }
    
    @Override
    public int getAvailableWorkers() {
        final WorkerBuildingCount count = new WorkerBuildingCount(this);
        this.forEachBuilding(count);
        return count.getRemainingWorker();
    }
    
    @Override
    public int getBuildingAndEvolutionsCount(final short buildingRefId) {
        final BuildingAndEvolutionCount count = new BuildingAndEvolutionCount(buildingRefId);
        this.forEachBuilding(count);
        return count.getCount();
    }
    
    @Override
    public int getBuildingOfTypeCount(final int buildingType) {
        final BuildingOfTypeCount count = new BuildingOfTypeCount(buildingType);
        this.forEachBuilding(count);
        return count.getCount();
    }
    
    @Override
    public int getResources() {
        return this.m_resources;
    }
    
    @Override
    public Partition getPartition(final short x, final short y) {
        return this.m_partitions.get(MathHelper.getIntFromTwoShort(x, y));
    }
    
    @Override
    public boolean forEachPartition(final TObjectProcedure<Partition> procedure) {
        return this.m_partitions.forEachValue(procedure);
    }
    
    @Override
    public int getPartitionCost() {
        return 250000;
    }
    
    void setGuildInfo(final GuildInfo guildInfo) {
        this.m_guildInfo = guildInfo;
        this.notifyOwnerGuildChanged();
    }
    
    void setResources(final int resources) {
        this.m_resources = resources;
        this.notifyResourcesChanged();
    }
    
    void addBuilding(final Building building) {
        this.m_buildings.put(building.getUid(), building);
        this.notifyBuildingAdded(building);
    }
    
    void removeBuilding(final long buildingUid) {
        final Building building = this.m_buildings.remove(buildingUid);
        this.notifyBuildingRemoved(building);
    }
    
    void addPartition(final Partition partition) {
        final int key = MathHelper.getIntFromTwoShort(partition.getX(), partition.getY());
        this.m_partitions.put(key, partition);
        this.notifyPartitionAdded(partition);
    }
    
    void onPartitionModified(final Partition partition) {
        this.notifyPartitionAdded(partition);
    }
    
    private void notifyOwnerGuildChanged() {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).guildChanged(this.m_guildInfo);
        }
    }
    
    private void notifyResourcesChanged() {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).resourcesChanged(this.m_resources);
        }
    }
    
    private void notifyBuildingAdded(final Building building) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).buildingAdded(building);
        }
    }
    
    private void notifyBuildingRemoved(final Building building) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).buildingRemoved(building);
        }
    }
    
    private void notifyPartitionAdded(final Partition partition) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).partitionAdded(partition);
        }
    }
    
    @Override
    public boolean addListener(final HavenWorldListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final HavenWorldListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "HavenWorldModel{m_definition=" + this.m_definition + ", m_guildInfo=" + this.m_guildInfo + ", m_buildings=" + this.m_buildings + ", m_partitions=" + this.m_partitions + ", m_listeners=" + this.m_listeners + '}';
    }
}
