package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class HavenWorldElementView extends ImmutableFieldProvider implements TimeTickListener
{
    public static final String REMAINING_TIME = "remainingTime";
    public static final String CONSTRUCTION_DATE = "constructionDate";
    public static final String CUSTOM_ITEM = "customItem";
    public static final String IS_DECO = "isDeco";
    public static final String CAN_BE_EVOLVED_FIELD = "canBeEvolved";
    private final BuildingDefinitionView m_buildingDefinitionView;
    private final long m_uniqueId;
    private final GameDateConst m_constructionDate;
    private Item m_customItem;
    
    private HavenWorldElementView(final BuildingDefinitionView buildingDefinitionView, final long uniqueId, final Item customItem, final GameDateConst constructionDate) {
        super();
        this.m_buildingDefinitionView = buildingDefinitionView;
        this.m_uniqueId = uniqueId;
        this.m_constructionDate = constructionDate;
        this.setCustomItem(customItem);
    }
    
    public static HavenWorldElementView fromNewEntry(final BuildingDefinitionView catalogEntryView, final long uniqueId, final int customItem, final GameDateConst constructionDate) {
        Item item = null;
        if (customItem != 0) {
            item = ReferenceItemManager.getInstance().getDefaultItem(customItem);
        }
        return new HavenWorldElementView(catalogEntryView, uniqueId, item, constructionDate);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("remainingTime")) {
            return this.getRemainingTime(true);
        }
        if (fieldName.equals("constructionDate")) {
            return WakfuTranslator.getInstance().formatDateShort(this.m_constructionDate);
        }
        if (fieldName.equals("customItem")) {
            return this.m_customItem;
        }
        if (fieldName.equals("isDeco")) {
            return this.m_buildingDefinitionView.getBuilding().isDecoOnly();
        }
        if (!fieldName.equals("canBeEvolved")) {
            return this.m_buildingDefinitionView.getFieldValue(fieldName);
        }
        final AbstractBuildingDefinition building = this.m_buildingDefinitionView.getBuilding();
        if (building == null) {
            return false;
        }
        final BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(building.getId());
        if (evolution == null) {
            return false;
        }
        final AbstractBuildingDefinition buildingTo = HavenWorldDefinitionManager.INSTANCE.getBuilding(evolution.getBuildingToId());
        if (buildingTo == null) {
            return false;
        }
        return building.getCatalogEntryId() != buildingTo.getCatalogEntryId();
    }
    
    private boolean isReal() {
        return this.m_uniqueId > 0L;
    }
    
    public GameDateConst getBuildingEvolutionEndDate() {
        final BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(this.m_buildingDefinitionView.getBuilding().getId());
        if (evolution == null) {
            return null;
        }
        return BuildingValidationHelper.getEvolutionEndDate(HavenWorldViewManager.INSTANCE.getCurrentWorldId(), evolution, this.m_constructionDate.toLong());
    }
    
    public String getRemainingTime(final boolean longVersion) {
        if (!this.isReal()) {
            return null;
        }
        final GameDateConst endDate = this.getBuildingEvolutionEndDate();
        if (endDate == null) {
            return null;
        }
        final GameIntervalConst remainingTime = WakfuGameCalendar.getInstance().getDate().timeTo(endDate);
        if (!remainingTime.isPositive()) {
            return null;
        }
        if (remainingTime.lowerThan(GameIntervalConst.MINUTE_INTERVAL)) {
            return longVersion ? TimeUtils.getLongDescription(GameIntervalConst.MINUTE_INTERVAL) : TimeUtils.getShortDescription(GameIntervalConst.MINUTE_INTERVAL);
        }
        return longVersion ? TimeUtils.getLongDescription(remainingTime) : TimeUtils.getShortDescription(remainingTime);
    }
    
    public String getName() {
        return this.m_buildingDefinitionView.getName();
    }
    
    public HavenWorldCatalogEntryView getCatalogEntryView() {
        return HavenWorldViewManager.INSTANCE.getCatalogBuildingEntry(this.m_buildingDefinitionView.getBuilding().getCatalogEntryId());
    }
    
    public long getUniqueId() {
        return this.m_uniqueId;
    }
    
    public void addToTimeManager() {
        final GameIntervalConst buildingDelay = this.m_buildingDefinitionView.getIndividualBuildingDelay();
        if (!buildingDelay.isPositive()) {
            return;
        }
        TimeManager.INSTANCE.addListener(this);
    }
    
    public void removeFromTimeManager() {
        TimeManager.INSTANCE.removeListener(this);
    }
    
    @Override
    public void tick() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingTime");
    }
    
    public BuildingDefinitionView getBuildingDefinitionView() {
        return this.m_buildingDefinitionView;
    }
    
    public void setCustomItem(final Item customItem) {
        this.m_customItem = customItem;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "customItem");
    }
}
