package com.ankamagames.wakfu.client.core.havenWorld.view;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.buff.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class HavenWorldViewManager
{
    private static final Logger m_logger;
    private static final TObjectProcedure<HavenWorldElementView> CLEAR_ELEMENT_PROCEDURE;
    public static final HavenWorldViewManager INSTANCE;
    private final TIntObjectHashMap<BuildingDefinitionView> m_buildingDefinitions;
    private final TIntObjectHashMap<HavenWorldCatalogBuildingEntryView> m_catalogBuildingEntries;
    private final TIntObjectHashMap<HavenWorldCatalogPatchEntryView> m_catalogPatchEntries;
    private final TLongObjectHashMap<HavenWorldElementView> m_elements;
    private final HavenWorldImagesLibrary m_imageLibrary;
    private short m_currentWorldId;
    
    public HavenWorldViewManager() {
        super();
        this.m_buildingDefinitions = new TIntObjectHashMap<BuildingDefinitionView>();
        this.m_catalogBuildingEntries = new TIntObjectHashMap<HavenWorldCatalogBuildingEntryView>();
        this.m_catalogPatchEntries = new TIntObjectHashMap<HavenWorldCatalogPatchEntryView>();
        this.m_elements = new TLongObjectHashMap<HavenWorldElementView>();
        this.m_imageLibrary = HavenWorldImagesLibrary.INSTANCE;
    }
    
    public void refreshElements(final HavenWorld havenWorld) {
        final ArrayList<Building> buildings = new ArrayList<Building>();
        this.m_currentWorldId = havenWorld.getWorldInstanceId();
        HavenWorldZoneBuffs buffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(this.m_currentWorldId);
        final int adminModificator = buffs.getAdminBuildDurationPercentModificator();
        buffs = HavenWorldZoneBuffsManager.INSTANCE.resetBuffs(this.m_currentWorldId);
        buffs.setAdminBuildDurationPercentModificator(adminModificator);
        havenWorld.forEachBuilding(new TObjectProcedure<Building>() {
            @Override
            public boolean execute(final Building object) {
                buildings.add(object);
                if (object.getDefinition().isDecoOnly()) {
                    return true;
                }
                final BuildingDefinition definition = (BuildingDefinition)object.getDefinition();
                definition.forEachWorldEffect(new TIntProcedure() {
                    @Override
                    public boolean execute(final int value) {
                        final HavenWorldBuff buff = HavenWorldBuffFactory.INSTANCE.get(value);
                        if (buff == null) {
                            return true;
                        }
                        buff.apply(HavenWorldViewManager.this.m_currentWorldId, object.getUid());
                        return true;
                    }
                });
                return true;
            }
        });
        this.refreshElements(buildings);
    }
    
    public void refreshElements(final ArrayList<Building> buildings) {
        this.clearElements();
        for (int i = 0, size = buildings.size(); i < size; ++i) {
            this.createAndRegisterNewElement(buildings.get(i));
        }
    }
    
    public short getCurrentWorldId() {
        return this.m_currentWorldId;
    }
    
    private void clearElements() {
        this.m_elements.forEachValue(HavenWorldViewManager.CLEAR_ELEMENT_PROCEDURE);
        this.m_elements.clear();
    }
    
    public void clear() {
        this.m_buildingDefinitions.clear();
        this.m_imageLibrary.clearTextures();
        this.clearElements();
    }
    
    public BuildingDefinitionView getBuildingDefinition(final AbstractBuildingDefinition definition) {
        return this.getBuildingDefinition(definition.getId());
    }
    
    public BuildingDefinitionView getBuildingDefinition(final short id) {
        BuildingDefinitionView view = this.m_buildingDefinitions.get(id);
        if (view == null) {
            this.m_buildingDefinitions.put(id, view = new BuildingDefinitionView(HavenWorldDefinitionManager.INSTANCE.getBuilding(id), this.m_imageLibrary));
        }
        return view;
    }
    
    private HavenWorldCatalogBuildingEntryView getCatalogBuildingEntry(final BuildingCatalogEntry entry) {
        HavenWorldCatalogBuildingEntryView view = this.m_catalogBuildingEntries.get(entry.getId());
        if (view == null) {
            this.m_catalogBuildingEntries.put(entry.getId(), view = new HavenWorldCatalogBuildingEntryView(entry));
        }
        return view;
    }
    
    public HavenWorldCatalogBuildingEntryView getCatalogBuildingEntry(final short id) {
        return this.getCatalogBuildingEntry(HavenWorldDefinitionManager.INSTANCE.getBuildingCatalogEntry(id));
    }
    
    public HavenWorldCatalogPatchEntryView getCatalogPatchEntry(final PatchCatalogEntry entry) {
        final short patchId = entry.getPatchId();
        HavenWorldCatalogPatchEntryView view = this.m_catalogPatchEntries.get(patchId);
        if (view == null) {
            this.m_catalogPatchEntries.put(patchId, view = new HavenWorldCatalogPatchEntryView(HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(patchId), this.m_imageLibrary));
        }
        return view;
    }
    
    private HavenWorldCatalogPartitionEntryView getCatalogPartitionEntry(final PartitionCatalogEntry entry) {
        return new HavenWorldCatalogPartitionEntryView(entry);
    }
    
    public HavenWorldElementView getElement(final long uid) {
        return this.m_elements.get(uid);
    }
    
    public HavenWorldCatalogEntryView getCatalogEntryView(final HavenWorldCatalogEntry entry) {
        if (entry instanceof BuildingCatalogEntry) {
            return this.getCatalogBuildingEntry((BuildingCatalogEntry)entry);
        }
        if (entry instanceof PatchCatalogEntry) {
            return this.getCatalogPatchEntry((PatchCatalogEntry)entry);
        }
        if (entry instanceof PartitionCatalogEntry) {
            return this.getCatalogPartitionEntry((PartitionCatalogEntry)entry);
        }
        return null;
    }
    
    public HavenWorldElementView createAndRegisterNewElement(final Building building) {
        final long uid = building.getUid();
        final HavenWorldElementView elementView = HavenWorldElementView.fromNewEntry(this.getBuildingDefinition(building.getDefinition()), uid, building.getEquippedItemId(), GameDate.fromLong(building.getCreationDate()));
        if (elementView != null) {
            this.m_elements.put(uid, elementView);
            elementView.addToTimeManager();
        }
        return elementView;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldViewManager.class);
        CLEAR_ELEMENT_PROCEDURE = new TObjectProcedure<HavenWorldElementView>() {
            @Override
            public boolean execute(final HavenWorldElementView view) {
                view.removeFromTimeManager();
                return true;
            }
        };
        INSTANCE = new HavenWorldViewManager();
    }
}
