package com.ankamagames.wakfu.common.game.havenWorld.definition;

import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class HavenWorldDefinitionManager
{
    public static final HavenWorldDefinitionManager INSTANCE;
    private final TIntObjectHashMap<HavenWorldDefinition> m_worlds;
    private final TShortIntHashMap m_worldByInstance;
    private final TShortObjectHashMap<AbstractBuildingDefinition> m_buildings;
    private final TShortObjectHashMap<BuildingEvolution> m_evolutions;
    private final TShortObjectHashMap<BuildingCatalogEntry> m_buildingCatalog;
    private final TShortObjectHashMap<PatchCatalogEntry> m_patchCatalog;
    
    private HavenWorldDefinitionManager() {
        super();
        this.m_worlds = new TIntObjectHashMap<HavenWorldDefinition>();
        this.m_worldByInstance = new TShortIntHashMap();
        this.m_buildings = new TShortObjectHashMap<AbstractBuildingDefinition>();
        this.m_evolutions = new TShortObjectHashMap<BuildingEvolution>();
        this.m_buildingCatalog = new TShortObjectHashMap<BuildingCatalogEntry>();
        this.m_patchCatalog = new TShortObjectHashMap<PatchCatalogEntry>();
    }
    
    public void registerWorld(final HavenWorldDefinition definition) {
        this.m_worlds.put(definition.getId(), definition);
        this.m_worldByInstance.put(definition.getWorldInstanceId(), definition.getId());
    }
    
    public HavenWorldDefinition getWorld(final int worldId) {
        return this.m_worlds.get(worldId);
    }
    
    public HavenWorldDefinition getWorldFromInstance(final short worldInstanceId) {
        return this.getWorld(this.m_worldByInstance.get(worldInstanceId));
    }
    
    public void registerBuilding(final AbstractBuildingDefinition definition) {
        BuildingDefinitionHelper.refreshCache();
        this.m_buildings.put(definition.getId(), definition);
    }
    
    public AbstractBuildingDefinition getBuilding(final short buildingId) {
        return this.m_buildings.get(buildingId);
    }
    
    public void registerEvolution(final BuildingEvolution evolution) {
        BuildingDefinitionHelper.refreshCache();
        this.m_evolutions.put(evolution.getBuildingFromId(), evolution);
    }
    
    public BuildingEvolution getEvolutionFromBuilding(final short buildingFromId) {
        return this.m_evolutions.get(buildingFromId);
    }
    
    public void registerCatalogEntry(final BuildingCatalogEntry entry) {
        BuildingDefinitionHelper.refreshCache();
        this.m_buildingCatalog.put(entry.getId(), entry);
    }
    
    public BuildingCatalogEntry getBuildingCatalogEntry(final short catalogEntryId) {
        return this.m_buildingCatalog.get(catalogEntryId);
    }
    
    public void registerCatalogEntry(final PatchCatalogEntry entry) {
        BuildingDefinitionHelper.refreshCache();
        this.m_patchCatalog.put(entry.getPatchId(), entry);
    }
    
    public PatchCatalogEntry getPatchCatalogEntry(final short patchId) {
        return this.m_patchCatalog.get(patchId);
    }
    
    public BuildingCatalogEntry[] getSortedBuildingEntry() {
        final BuildingCatalogEntry[] entries = new BuildingCatalogEntry[this.m_buildingCatalog.size()];
        this.m_buildingCatalog.getValues(entries);
        Arrays.sort(entries, new Comparator<BuildingCatalogEntry>() {
            @Override
            public int compare(final BuildingCatalogEntry o1, final BuildingCatalogEntry o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
        return entries;
    }
    
    public void foreachSortedPatchCatalogEntry(final TObjectProcedure<PatchCatalogEntry> procedure) {
        final PatchCatalogEntry[] entries = new PatchCatalogEntry[this.m_patchCatalog.size()];
        this.m_patchCatalog.getValues(entries);
        Arrays.sort(entries, new PatchComparator());
        for (int i = 0, length = entries.length; i < length; ++i) {
            if (!procedure.execute(entries[i])) {
                return;
            }
        }
    }
    
    public void foreachWorld(final TObjectProcedure<HavenWorldDefinition> procedure) {
        this.m_worlds.forEachValue(procedure);
    }
    
    public void foreachBuilding(final TObjectProcedure<AbstractBuildingDefinition> procedure) {
        this.m_buildings.forEachValue(procedure);
    }
    
    public void foreachEvolutions(final TObjectProcedure<BuildingEvolution> procedure) {
        this.m_evolutions.forEachValue(procedure);
    }
    
    public void foreachEntry(final TObjectProcedure<BuildingCatalogEntry> procedure) {
        this.m_buildingCatalog.forEachValue(procedure);
    }
    
    public ArrayList<BuildingEvolution> getSortedEvolutionForEntry(final BuildingCatalogEntry entry) {
        final ArrayList<BuildingEvolution> evolutions = new ArrayList<BuildingEvolution>(3);
        final TShortObjectIterator<BuildingEvolution> it = this.m_evolutions.iterator();
        while (it.hasNext()) {
            it.advance();
            final BuildingEvolution evolution = it.value();
            if (evolution.getCatalogEntryId() == entry.getId()) {
                evolutions.add(it.value());
            }
        }
        Collections.sort(evolutions);
        return evolutions;
    }
    
    static {
        INSTANCE = new HavenWorldDefinitionManager();
    }
    
    private static class PatchComparator implements Comparator<PatchCatalogEntry>
    {
        @Override
        public int compare(final PatchCatalogEntry o1, final PatchCatalogEntry o2) {
            final Point2i c1 = PartitionPatch.getMapCoordFromPatchId(o1.getPatchId());
            final Point2i c2 = PartitionPatch.getMapCoordFromPatchId(o2.getPatchId());
            if (c1.getX() == 0) {
                return 1;
            }
            if (c2.getX() == 0) {
                return -1;
            }
            if (c1.getY() == c2.getY()) {
                return c1.getX() - c2.getX();
            }
            return c1.getY() - c2.getY();
        }
    }
}
