package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class HavenWorldCatalogView extends ImmutableFieldProvider implements HavenWorldListener
{
    private static final Logger m_logger;
    public static final String CATEGORIES_FIELD = "categories";
    public static final String SELECTED_CATEGORY_FIELD = "selectedCategory";
    public static final String SIDOA_TEXT_FIELD = "sidoaText";
    public static final String IS_DIRTY_FIELD = "isDirty";
    public static final String QUOTATION_FIELD = "quotation";
    public static final String TOTAL_PRICE_FIELD = "totalPrice";
    public static final String RESOURCES_FIELD = "resources";
    public static final String RESOURCES_COST_FIELD = "resourcesCost";
    public static final String USED_SIDOAS_FIELD = "usedSidoas";
    public static final String[] FIELDS;
    private final TIntObjectHashMap<HavenWorldCatalogCategoryView> m_havenWorldCategoryEntryViews;
    private final ArrayList<HavenWorldQuotation> m_orderedQuotationEntries;
    private byte m_selectedCategoryId;
    private boolean m_dirty;
    private final HavenWorldDataProvider m_dataProvider;
    
    @Override
    public String[] getFields() {
        return HavenWorldCatalogView.FIELDS;
    }
    
    public HavenWorldCatalogView(final HavenWorldDataProvider dataProvider) {
        super();
        this.m_havenWorldCategoryEntryViews = new TIntObjectHashMap<HavenWorldCatalogCategoryView>();
        this.m_orderedQuotationEntries = new ArrayList<HavenWorldQuotation>();
        this.m_dataProvider = dataProvider;
        this.createBuildingEntries();
        this.createPatchEntries();
        this.m_selectedCategoryId = (byte)CatalogCategory.BUILDING.getId();
    }
    
    private HavenWorldCatalogCategoryView getOrCreateCategoryEntries(final CatalogCategory catalogCategory) {
        final byte category = (byte)catalogCategory.getId();
        HavenWorldCatalogCategoryView catalogCategoryView = this.m_havenWorldCategoryEntryViews.get(category);
        if (catalogCategoryView == null) {
            catalogCategoryView = new HavenWorldCatalogCategoryView(catalogCategory);
            this.m_havenWorldCategoryEntryViews.put(category, catalogCategoryView);
        }
        return catalogCategoryView;
    }
    
    private void createPatchEntries() {
        final HavenWorldCatalogCategoryView catalogCategoryView = this.getOrCreateCategoryEntries(CatalogCategory.PATCH);
        HavenWorldDefinitionManager.INSTANCE.foreachSortedPatchCatalogEntry(new TObjectProcedure<PatchCatalogEntry>() {
            @Override
            public boolean execute(final PatchCatalogEntry catalogEntry) {
                if (PartitionPatch.isEditable(catalogEntry.getPatchId())) {
                    addEntry(catalogEntry, CatalogCategory.PATCH, catalogCategoryView);
                }
                return true;
            }
        });
    }
    
    private void createBuildingEntries() {
        final HavenWorldCatalogCategoryView mainBuildingCategory = this.getOrCreateCategoryEntries(CatalogCategory.BUILDING);
        final HavenWorldCatalogCategoryView buildingDecoCategory = this.getOrCreateCategoryEntries(CatalogCategory.BUILDING_DECO);
        final HavenWorldCatalogCategoryView dungeonCategory = this.getOrCreateCategoryEntries(CatalogCategory.DUNGEON);
        for (final BuildingCatalogEntry catalogEntry : HavenWorldDefinitionManager.INSTANCE.getSortedBuildingEntry()) {
            if (catalogEntry.isBuyable()) {
                switch (HavenWorldConstants.getCategoryFromCatalogEntry(catalogEntry)) {
                    case 1: {
                        addEntry(catalogEntry, CatalogCategory.BUILDING, mainBuildingCategory);
                        break;
                    }
                    case 2: {
                        addEntry(catalogEntry, CatalogCategory.BUILDING_DECO, buildingDecoCategory);
                        break;
                    }
                    case 3: {
                        addEntry(catalogEntry, CatalogCategory.DUNGEON, dungeonCategory);
                        break;
                    }
                }
            }
        }
    }
    
    private static void addEntry(final HavenWorldCatalogEntry catalogEntry, final CatalogCategory type, final HavenWorldCatalogCategoryView rootCategory) {
        final HavenWorldCatalogEntryView entryView = HavenWorldViewManager.INSTANCE.getCatalogEntryView(catalogEntry);
        final int categoryId = catalogEntry.getCategoryId();
        HavenWorldCatalogSubCategoryView subCategoryView = rootCategory.getCategory(categoryId);
        if (subCategoryView == null) {
            subCategoryView = new HavenWorldCatalogSubCategoryView(type, categoryId);
            rootCategory.putCategory(subCategoryView);
        }
        subCategoryView.addEntry(entryView);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("categories")) {
            final ArrayList<HavenWorldCatalogCategoryView> havenWorldCatalogCategoryViews = new ArrayList<HavenWorldCatalogCategoryView>();
            final Object[] values = this.m_havenWorldCategoryEntryViews.getValues();
            for (int i = 0; i < values.length; ++i) {
                havenWorldCatalogCategoryViews.add((HavenWorldCatalogCategoryView)values[i]);
            }
            Collections.sort(havenWorldCatalogCategoryViews, new Comparator<HavenWorldCatalogCategoryView>() {
                @Override
                public int compare(final HavenWorldCatalogCategoryView o1, final HavenWorldCatalogCategoryView o2) {
                    return o1.order() - o2.order();
                }
            });
            return havenWorldCatalogCategoryViews;
        }
        if (fieldName.equals("selectedCategory")) {
            return this.m_havenWorldCategoryEntryViews.get(this.m_selectedCategoryId);
        }
        if (fieldName.equals("sidoaText")) {
            return this.m_dataProvider.getRemainingWorkers() + "/" + this.m_dataProvider.getTotalWorkers();
        }
        if (fieldName.equals("isDirty")) {
            return this.m_dirty;
        }
        if (fieldName.equals("quotation")) {
            return this.getOrderedQuotations();
        }
        if (fieldName.equals("totalPrice")) {
            return WakfuTranslator.getInstance().formatNumber(this.m_dataProvider.getKamaCost());
        }
        if (fieldName.equals("usedSidoas")) {
            return this.m_dataProvider.getCurrentUsedWorkers();
        }
        if (fieldName.equals("resources")) {
            return WakfuTranslator.getInstance().formatNumber(this.m_dataProvider.getResources());
        }
        if (fieldName.equals("resourcesCost")) {
            return WakfuTranslator.getInstance().formatNumber(this.m_dataProvider.getResourcesCost());
        }
        return null;
    }
    
    public int getQuotationsSize() {
        return this.m_orderedQuotationEntries.size();
    }
    
    public ArrayList<HavenWorldQuotation> getOrderedQuotations() {
        return new ArrayList<HavenWorldQuotation>(this.m_orderedQuotationEntries);
    }
    
    public void addEntryToQuotation(final HavenWorldQuotation quotation) {
        if (quotation.getModification() instanceof AddPatchModification) {
            this.removePreviousPatchQuotationFromModification((AddPatchModification)quotation.getModification());
        }
        this.m_orderedQuotationEntries.add(quotation);
        this.m_dirty = true;
    }
    
    private void removePreviousPatchQuotationFromModification(final AddPatchModification modification) {
        final Iterator<HavenWorldQuotation> iterator = this.m_orderedQuotationEntries.iterator();
        while (iterator.hasNext()) {
            final HavenWorldQuotation orderedQuotationEntry = iterator.next();
            final Modification mod = orderedQuotationEntry.getModification();
            if (!(mod instanceof AddPatchModification)) {
                continue;
            }
            final PatchItem item = ((AddPatchModification)mod).getItem();
            final PatchItem modificationItem = modification.getItem();
            if (item.getPatchX() != modificationItem.getPatchX() || item.getPatchY() != modificationItem.getPatchY()) {
                continue;
            }
            iterator.remove();
        }
    }
    
    public void removeEntryFromQuotation(final HavenWorldQuotation quotation) {
        this.m_orderedQuotationEntries.remove(quotation);
        this.m_dirty = !this.m_orderedQuotationEntries.isEmpty();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, HavenWorldCatalogView.FIELDS);
    }
    
    public HavenWorldQuotation getQuotationEntry(final Modification modification) {
        final ModificationItem item = modification.getItem();
        return this.getQuotationEntry(item.getUid(), item.getLayer());
    }
    
    public HavenWorldQuotation getQuotationEntry(final long uid, final ItemLayer layer) {
        for (int i = 0, size = this.m_orderedQuotationEntries.size(); i < size; ++i) {
            final HavenWorldQuotation entry = this.m_orderedQuotationEntries.get(i);
            final ModificationItem item = entry.getModification().getItem();
            if (item.getUid() == uid && item.getLayer() == layer) {
                return entry;
            }
        }
        return null;
    }
    
    public int countQuotationEntriesOfType(final short id) {
        int count = 0;
        for (final HavenWorldQuotation havenWorldQuotation : this.m_orderedQuotationEntries) {
            final HavenWorldCatalogEntryView catalogEntryView = havenWorldQuotation.getCatalogEntryView();
            if (!(catalogEntryView instanceof HavenWorldCatalogBuildingEntryView)) {
                continue;
            }
            if (((HavenWorldCatalogBuildingEntryView)catalogEntryView).getBuildingDefinition().getId() != id) {
                continue;
            }
            ++count;
        }
        return count;
    }
    
    public ItemLayer getSelectedCategoryLayer() {
        for (final CatalogCategory category : CatalogCategory.values()) {
            if (category.getId() == this.m_selectedCategoryId) {
                return category.getLayer();
            }
        }
        return null;
    }
    
    public byte getSelectedCategoryId() {
        return this.m_selectedCategoryId;
    }
    
    public void setSelectedCategoryId(final byte categoryId) {
        this.m_selectedCategoryId = categoryId;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedCategory");
    }
    
    public void refreshFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, HavenWorldCatalogView.FIELDS);
    }
    
    public HavenWorldDataProvider getDataProvider() {
        return this.m_dataProvider;
    }
    
    public boolean isDirty() {
        return this.m_dirty;
    }
    
    public void clear() {
        this.clearQuotations();
        this.m_selectedCategoryId = -1;
        this.m_havenWorldCategoryEntryViews.clear();
    }
    
    public void clearQuotations() {
        this.m_orderedQuotationEntries.clear();
        this.m_dirty = false;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, HavenWorldCatalogView.FIELDS);
    }
    
    public boolean isAvailable(final HavenWorldCatalogEntryView entryView) {
        final HavenWorldCatalogCategoryView categoryView = this.m_havenWorldCategoryEntryViews.get(entryView.getCategory().getId());
        if (categoryView == null) {
            return true;
        }
        final HavenWorldCatalogSubCategoryView subCategoryView = categoryView.m_entries.get(entryView.getCatalogEntry().getCategoryId());
        if (subCategoryView == null) {
            return true;
        }
        final ArrayList<FieldProvider> catalogEntryViews = subCategoryView.m_entries;
        if (catalogEntryViews == null) {
            return true;
        }
        for (final FieldProvider catalogEntryView : catalogEntryViews) {
            if (entryView instanceof HavenWorldCatalogPatchEntryView && catalogEntryView instanceof HavenWorldCatalogPatchEntryView) {
                final PatchCatalogEntry catalogEntry = ((HavenWorldCatalogPatchEntryView)catalogEntryView).getCatalogEntry();
                final PatchCatalogEntry entry = ((HavenWorldCatalogPatchEntryView)entryView).getCatalogEntry();
                if (catalogEntry.getPatchId() == entry.getPatchId()) {
                    return ((HavenWorldCatalogPatchEntryView)catalogEntryView).isAvailable();
                }
                continue;
            }
        }
        return true;
    }
    
    public void updateAvailables(final HavenWorldDataProvider dataProvider) {
        this.m_havenWorldCategoryEntryViews.forEachValue(new TObjectProcedure<HavenWorldCatalogCategoryView>() {
            @Override
            public boolean execute(final HavenWorldCatalogCategoryView category) {
                category.m_entries.forEachValue(new TObjectProcedure<HavenWorldCatalogSubCategoryView>() {
                    @Override
                    public boolean execute(final HavenWorldCatalogSubCategoryView object) {
                        final ArrayList<FieldProvider> entries = object.m_entries;
                        for (final FieldProvider entry : entries) {
                            if (entry instanceof HavenWorldCatalogBuildingEntryView) {
                                final BaseBuildingConditionValidator validator = new BuildingConditionValidator(dataProvider);
                                final HavenWorldCatalogBuildingEntryView entryView = (HavenWorldCatalogBuildingEntryView)entry;
                                final int definitionId = entryView.getFirstBuildingDefinitionId();
                                entryView.setAvailable(validator.validate(new BuildingStruct(0L, definitionId, 0, (short)(-32768), (short)(-32768))));
                                entryView.setValidator(validator);
                            }
                            else {
                                if (entry instanceof HavenWorldCatalogPartitionEntryView) {
                                    continue;
                                }
                                if (!(entry instanceof HavenWorldCatalogPatchEntryView)) {
                                    continue;
                                }
                                final HavenWorldCatalogPatchEntryView entryView2 = (HavenWorldCatalogPatchEntryView)entry;
                                final PatchCatalogEntry catalogEntry = entryView2.getCatalogEntry();
                                final HavenWorld world = UIWorldEditorFrame.getInstance().getWorld();
                                final int mineralCount = this.getMineralCount(world);
                                final boolean tooMuchMineral = HavenWorldCatalogView.this.isMineralPatch(catalogEntry.getPatchId()) && mineralCount >= 20;
                                final boolean hasMoney = dataProvider.hasMoney(catalogEntry.getKamaCost());
                                entryView2.setAvailable(hasMoney && !tooMuchMineral);
                            }
                        }
                        return true;
                    }
                    
                    private int getMineralCount(final HavenWorld world) {
                        if (world == null) {
                            return 0;
                        }
                        final Accumulator mineralPatchCount = new Accumulator();
                        world.forEachPartition(new TObjectProcedure<Partition>() {
                            @Override
                            public boolean execute(final Partition partition) {
                                if (HavenWorldCatalogView.this.isMineralPatch(partition.getBottomLeftPatch())) {
                                    mineralPatchCount.inc();
                                }
                                if (HavenWorldCatalogView.this.isMineralPatch(partition.getTopLeftPatch())) {
                                    mineralPatchCount.inc();
                                }
                                if (HavenWorldCatalogView.this.isMineralPatch(partition.getBottomRightPatch())) {
                                    mineralPatchCount.inc();
                                }
                                if (HavenWorldCatalogView.this.isMineralPatch(partition.getTopRightPatch())) {
                                    mineralPatchCount.inc();
                                }
                                return true;
                            }
                        });
                        for (final HavenWorldQuotation quotation : HavenWorldCatalogView.this.m_orderedQuotationEntries) {
                            this.modifyMineralCountFromQuotation(mineralPatchCount, quotation, world);
                        }
                        return mineralPatchCount.getValue();
                    }
                    
                    private void modifyMineralCountFromQuotation(final Accumulator mineralPatchCount, final HavenWorldQuotation quotation, final HavenWorld world) {
                        final Modification modification = quotation.getModification();
                        if (modification instanceof AddPatchModification) {
                            final AddPatchModification addPatchModification = (AddPatchModification)modification;
                            final PatchItem item = addPatchModification.getItem();
                            final short oldPatchId = addPatchModification.getOldPatchId();
                            if (HavenWorldCatalogView.this.isMineralPatch(item.getPatchId()) && !HavenWorldCatalogView.this.isMineralPatch(oldPatchId)) {
                                mineralPatchCount.inc();
                            }
                            else if (!HavenWorldCatalogView.this.isMineralPatch(item.getPatchId()) && HavenWorldCatalogView.this.isMineralPatch(oldPatchId)) {
                                mineralPatchCount.accumulate(-1);
                            }
                        }
                        else if (modification instanceof DeletePatchModification) {
                            final DeletePatchModification deletePatchModification = (DeletePatchModification)modification;
                            final PatchItem item = deletePatchModification.getItem();
                            final short patchId = item.getPatchId();
                            if (HavenWorldCatalogView.this.isMineralPatch(patchId)) {
                                mineralPatchCount.accumulate(-1);
                            }
                        }
                    }
                });
                return true;
            }
        });
    }
    
    private boolean isMineralPatch(final short bottomLeftPatch) {
        final PatchCatalogEntry patchDefinition = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(bottomLeftPatch);
        return patchDefinition != null && HavenWorldConstants.MINERAL_CATEGORY.contains(patchDefinition.getCategoryId());
    }
    
    @Override
    public void buildingAdded(final Building building) {
    }
    
    @Override
    public void guildChanged(final GuildInfo guildInfo) {
    }
    
    @Override
    public void buildingRemoved(final Building building) {
    }
    
    @Override
    public void partitionAdded(final Partition partition) {
    }
    
    @Override
    public void partitionChanged(final Partition partition) {
    }
    
    @Override
    public void resourcesChanged(final int resources) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "resources");
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldCatalogView.class);
        FIELDS = new String[] { "categories", "selectedCategory", "sidoaText", "isDirty", "quotation", "totalPrice", "resources", "resourcesCost", "usedSidoas" };
    }
    
    public enum CatalogCategory
    {
        PATCH("patchCatalog", -1, ItemLayer.GROUND), 
        BUILDING("buildingCatalog", -2, ItemLayer.BUILDING), 
        BUILDING_DECO("HavenWorldBuildingDeco", -3, ItemLayer.BUILDING), 
        DUNGEON("HavenWorldDungeon", 18, ItemLayer.BUILDING), 
        PARTITION("partitionCatalog", -1, ItemLayer.PARTITION);
        
        private final String m_tag;
        private final String m_style;
        private final int m_id;
        private final ItemLayer m_layer;
        
        private CatalogCategory(final String tag, final int id, final ItemLayer layer) {
            this(tag, id, tag, layer);
        }
        
        private CatalogCategory(final String tag, final int id, final String style, final ItemLayer layer) {
            this.m_tag = tag;
            this.m_style = style;
            this.m_id = id;
            this.m_layer = layer;
        }
        
        public String getTag() {
            return this.m_tag;
        }
        
        public String getStyle() {
            return this.m_style;
        }
        
        public int getId() {
            return this.m_id;
        }
        
        public ItemLayer getLayer() {
            return this.m_layer;
        }
    }
}
