package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.graphics.engine.texture.*;

public class BuildingDefinitionView extends ImmutableFieldProvider
{
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String NAME_FIELD = "name";
    public static final String SURFACE_FIELD = "surface";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String EFFECTS_FIELD = "effects";
    public static final String SIDOA_NEED_FIELD = "sidoaNeed";
    public static final String BUILD_DELAY_FIELD = "buildDelay";
    public static final String CONDITIONS_FIELD = "conditions";
    public static final String RESSOURCES_COST_FIELD = "ressourcesCost";
    public static final String KAMAS_COST_FIELD = "kamasCost";
    public static final String CAN_BE_DESTROYED = "canBeDestroyed";
    public static final String[] FIELDS;
    private final AbstractBuildingDefinition m_building;
    private final HavenWorldImagesLibrary m_havenWorldImagesLibrary;
    
    public BuildingDefinitionView(final AbstractBuildingDefinition building, final HavenWorldImagesLibrary havenWorldImagesLibrary) {
        super();
        this.m_building = building;
        this.m_havenWorldImagesLibrary = havenWorldImagesLibrary;
    }
    
    @Override
    public String[] getFields() {
        return BuildingDefinitionView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            return this.getIcon();
        }
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("kamasCost")) {
            final AbstractBuildingDefinition buildingDefinition = this.getBuildingInfo();
            if (buildingDefinition == null) {
                return null;
            }
            final int kamaCost = BuildingValidationHelper.getAdjustedKamaCost(buildingDefinition, HavenWorldViewManager.INSTANCE.getCurrentWorldId());
            return WakfuTranslator.getInstance().formatNumber(kamaCost);
        }
        else if (fieldName.equals("surface")) {
            final EditorGroupMap model = EditorGroupMapLibrary.INSTANCE.getEditorGroup(this.m_building.getEditorGroupId());
            if (model == null) {
                return "Mod\u00e8le de batiment inconnu";
            }
            return model.getWidth() + "x" + model.getHeight();
        }
        else {
            if (fieldName.equals("description")) {
                final BuildingCatalogEntry entryForBuilding = BuildingDefinitionHelper.getEntryForBuilding(this.m_building.getId());
                return WakfuTranslator.getInstance().getString(123, entryForBuilding.getId(), new Object[0]);
            }
            if (fieldName.equals("effects")) {
                return this.getEffectsDescription();
            }
            if (fieldName.equals("sidoaNeed")) {
                if (this.m_building == null) {
                    return 0;
                }
                return this.m_building.getNeededWorkers();
            }
            else if (fieldName.equals("buildDelay")) {
                final GameIntervalConst delayForBuilding = this.getBuildingDelay();
                if (delayForBuilding.isEmpty()) {
                    return null;
                }
                final int days = delayForBuilding.getDays();
                final int hours = delayForBuilding.getHours();
                final int minutes = delayForBuilding.getMinutes();
                return WakfuTranslator.getInstance().getString("remainingDurationShort", days, hours, minutes, delayForBuilding);
            }
            else if (fieldName.equals("ressourcesCost")) {
                if (this.m_building.isDecoOnly()) {
                    return null;
                }
                final int resourceCost = BuildingValidationHelper.getAdjustedResourceCost(this.m_building, HavenWorldViewManager.INSTANCE.getCurrentWorldId());
                return WakfuTranslator.getInstance().formatNumber(resourceCost);
            }
            else {
                if (fieldName.equals("canBeDestroyed")) {
                    return this.m_building.canBeDestroyed();
                }
                return null;
            }
        }
    }
    
    public ArrayList<String> getEffectsDescription() {
        if (this.m_building.isDecoOnly()) {
            return null;
        }
        final ArrayList<String> effectsDescription = new ArrayList<String>();
        final ArrayList<String> strings = BuildingDefinitionHelper.getEffectsDescription(this.m_building);
        if (strings != null) {
            effectsDescription.addAll(strings);
        }
        return effectsDescription.isEmpty() ? null : effectsDescription;
    }
    
    private GameIntervalConst getBuildingDelay() {
        if (this.m_building.isDecoOnly()) {
            return GameInterval.EMPTY_INTERVAL;
        }
        return BuildingDefinitionHelper.getDelayForBuilding(this.m_building);
    }
    
    public GameIntervalConst getIndividualBuildingDelay() {
        if (this.m_building.isDecoOnly()) {
            return GameInterval.EMPTY_INTERVAL;
        }
        return BuildingDefinitionHelper.getIndividualDelay(this.m_building);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(126, this.m_building.getId(), new Object[0]);
    }
    
    public int getEditorGroupId() {
        final AbstractBuildingDefinition buildingDefinition = this.getBuildingInfo();
        if (buildingDefinition == null) {
            return -1;
        }
        return buildingDefinition.getEditorGroupId();
    }
    
    public Texture getIcon() {
        final AbstractBuildingDefinition buildingDefinition = this.getBuildingInfo();
        if (buildingDefinition == null) {
            return null;
        }
        return this.m_havenWorldImagesLibrary.getBuildingTexture(buildingDefinition.getEditorGroupId());
    }
    
    private AbstractBuildingDefinition getBuildingInfo() {
        return BuildingDefinitionHelper.getLastBuildingFor(this.m_building);
    }
    
    public AbstractBuildingDefinition getBuilding() {
        return this.m_building;
    }
    
    static {
        FIELDS = new String[] { "iconUrl", "name", "surface", "description", "effects", "sidoaNeed", "buildDelay", "conditions", "ressourcesCost", "kamasCost", "canBeDestroyed" };
    }
}
