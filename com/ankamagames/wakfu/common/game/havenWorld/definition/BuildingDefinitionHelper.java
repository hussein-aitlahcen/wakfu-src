package com.ankamagames.wakfu.common.game.havenWorld.definition;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;

public class BuildingDefinitionHelper
{
    private static final Logger m_logger;
    private static final TShortObjectHashMap<ArrayList<AbstractBuildingDefinition>> m_buildingsByEntry;
    private static final TShortObjectHashMap<BuildingCatalogEntry> m_entryByBuilding;
    private static boolean m_refreshCache;
    
    public static void refreshCache() {
        BuildingDefinitionHelper.m_refreshCache = true;
    }
    
    public static void buildCache() {
        if (!BuildingDefinitionHelper.m_refreshCache) {
            return;
        }
        HavenWorldDefinitionManager.INSTANCE.foreachBuilding(new TObjectProcedure<AbstractBuildingDefinition>() {
            @Override
            public boolean execute(final AbstractBuildingDefinition object) {
                final BuildingCatalogEntry entry = HavenWorldDefinitionManager.INSTANCE.getBuildingCatalogEntry(object.getCatalogEntryId());
                BuildingDefinitionHelper.m_entryByBuilding.put(object.getId(), entry);
                return true;
            }
        });
        HavenWorldDefinitionManager.INSTANCE.foreachEntry(new TObjectProcedure<BuildingCatalogEntry>() {
            @Override
            public boolean execute(final BuildingCatalogEntry object) {
                BuildingDefinitionHelper.m_buildingsByEntry.put(object.getId(), computeSortedBuildingForEntry(object));
                return true;
            }
        });
        BuildingDefinitionHelper.m_refreshCache = false;
    }
    
    private static BuildingCatalogEntry getEntryForBuilding(final AbstractBuildingDefinition b) {
        return getEntryForBuilding(b.getId());
    }
    
    public static BuildingCatalogEntry getEntryForBuilding(final short buildingId) {
        buildCache();
        return BuildingDefinitionHelper.m_entryByBuilding.get(buildingId);
    }
    
    private static ArrayList<AbstractBuildingDefinition> computeSortedBuildingForEntry(final BuildingCatalogEntry entry) {
        final ArrayList<BuildingEvolution> evolutions = HavenWorldDefinitionManager.INSTANCE.getSortedEvolutionForEntry(entry);
        final ArrayList<AbstractBuildingDefinition> buildings = new ArrayList<AbstractBuildingDefinition>(evolutions.size());
        if (!evolutions.isEmpty()) {
            buildings.add(HavenWorldDefinitionManager.INSTANCE.getBuilding(evolutions.get(0).getBuildingFromId()));
            for (final BuildingEvolution evolution : evolutions) {
                final AbstractBuildingDefinition building = HavenWorldDefinitionManager.INSTANCE.getBuilding(evolution.getBuildingToId());
                if (building != null && building.getCatalogEntryId() == entry.getId()) {
                    buildings.add(building);
                }
            }
        }
        else {
            HavenWorldDefinitionManager.INSTANCE.foreachBuilding(new TObjectProcedure<AbstractBuildingDefinition>() {
                @Override
                public boolean execute(final AbstractBuildingDefinition object) {
                    if (object.getCatalogEntryId() == entry.getId()) {
                        buildings.add(object);
                    }
                    return true;
                }
            });
        }
        return buildings;
    }
    
    public static GameIntervalConst getDelayForBuilding(final AbstractBuildingDefinition building) {
        final GameInterval delay = GameInterval.fromSeconds(0L);
        final BuildingCatalogEntry entry = getEntryForBuilding(building);
        final ArrayList<BuildingEvolution> evolutions = HavenWorldDefinitionManager.INSTANCE.getSortedEvolutionForEntry(entry);
        if (!evolutions.isEmpty()) {
            for (final BuildingEvolution evolution : evolutions) {
                if (!evolution.waitForResource()) {
                    delay.add(evolution.getDelay());
                }
            }
        }
        return delay;
    }
    
    public static GameIntervalConst getIndividualDelay(final AbstractBuildingDefinition building) {
        final BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(building.getId());
        return (evolution != null) ? evolution.getDelay() : GameInterval.EMPTY_INTERVAL;
    }
    
    @Nullable
    public static AbstractBuildingDefinition getLastBuildingFor(final AbstractBuildingDefinition building) {
        if (building == null) {
            return null;
        }
        if (building.isDecoOnly()) {
            return building;
        }
        final BuildingCatalogEntry entry = getEntryForBuilding(building);
        if (entry == null) {
            return null;
        }
        return getLastBuildingFor(entry);
    }
    
    @Nullable
    public static AbstractBuildingDefinition getLastBuildingFor(final BuildingCatalogEntry entry) {
        buildCache();
        final ArrayList<AbstractBuildingDefinition> definitions = BuildingDefinitionHelper.m_buildingsByEntry.get(entry.getId());
        if (definitions == null || definitions.isEmpty()) {
            return null;
        }
        return definitions.get(definitions.size() - 1);
    }
    
    @Nullable
    public static AbstractBuildingDefinition getFirstBuildingFor(final BuildingCatalogEntry entry) {
        buildCache();
        final ArrayList<AbstractBuildingDefinition> definitions = BuildingDefinitionHelper.m_buildingsByEntry.get(entry.getId());
        if (definitions == null) {
            return null;
        }
        return definitions.get(0);
    }
    
    @Nullable
    public static AbstractBuildingDefinition getFirstBuildingFor(final AbstractBuildingDefinition building) {
        if (building == null) {
            return null;
        }
        final BuildingCatalogEntry entry = getEntryForBuilding(building);
        if (entry == null) {
            return null;
        }
        return getFirstBuildingFor(entry);
    }
    
    public static ConstructionState getState(final AbstractBuildingDefinition building) {
        if (building.isDecoOnly()) {
            return ConstructionState.DONE;
        }
        final AbstractBuildingDefinition first = getFirstBuildingFor(building);
        if (first == building) {
            return ConstructionState.STARTED;
        }
        final AbstractBuildingDefinition last = getLastBuildingFor(building);
        if (last == building) {
            return ConstructionState.DONE;
        }
        return ConstructionState.WIP;
    }
    
    public static boolean isBuildingFrom(final short buildingRefId, final short buildingToId) {
        BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(buildingRefId);
        while (evolution != null) {
            evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(evolution.getBuildingToId());
            if (evolution != null && evolution.getBuildingToId() == buildingToId) {
                return true;
            }
        }
        return false;
    }
    
    public static ArrayList<String> getEffectsDescription(final AbstractBuildingDefinition buildingDefinition) {
        if (buildingDefinition == null) {
            return null;
        }
        if (!buildingDefinition.hasEffect()) {
            return null;
        }
        final ArrayList<WakfuEffect> effects = new ArrayList<WakfuEffect>();
        ((BuildingDefinition)buildingDefinition).forEachEffect(new TObjectProcedure<WakfuEffect>() {
            @Override
            public boolean execute(final WakfuEffect effect) {
                if (!effect.hasProperty(RunningEffectPropertyType.HAVEN_WORLD_VISITOR_BUFF)) {
                    effects.add(effect);
                }
                return true;
            }
        });
        assert !effects.isEmpty();
        return CastableDescriptionGenerator.generateDescription(new DummyEffectContainerWriter(effects, 0, (short)0, true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, 0));
    }
    
    static {
        m_logger = Logger.getLogger((Class)BuildingDefinitionHelper.class);
        m_buildingsByEntry = new TShortObjectHashMap<ArrayList<AbstractBuildingDefinition>>();
        m_entryByBuilding = new TShortObjectHashMap<BuildingCatalogEntry>();
    }
    
    public enum ConstructionState
    {
        STARTED, 
        WIP, 
        DONE;
    }
}
