package com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry;

import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import java.util.*;

public class HavenWorldCatalogBuildingEntryView extends HavenWorldCatalogEntryView<BuildingCatalogEntry>
{
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String SURFACE_FIELD = "surface";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SIDOA_NEED_FIELD = "sidoaNeed";
    public static final String BUILD_DELAY_FIELD = "buildDelay";
    public static final String CONDITIONS_FIELD = "conditions";
    public static final String EFFECTS_FIELD = "effects";
    public static final String RESSOURCES_COST_FIELD = "ressourcesCost";
    public static final String EVOLUTION_NAME_FIELD = "evolutionName";
    public static final String[] FIELDS;
    private final BuildingDefinitionView m_buildingDefinition;
    
    @Override
    public String[] getFields() {
        return HavenWorldCatalogBuildingEntryView.FIELDS;
    }
    
    public HavenWorldCatalogBuildingEntryView(final BuildingCatalogEntry buildingCatalogEntry) {
        super(buildingCatalogEntry);
        this.m_buildingDefinition = HavenWorldViewManager.INSTANCE.getBuildingDefinition(BuildingDefinitionHelper.getLastBuildingFor(buildingCatalogEntry).getId());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final Object result = super.getFieldValue(fieldName);
        if (result != null) {
            return result;
        }
        if (fieldName.equals("description")) {
            return WakfuTranslator.getInstance().getString(123, ((BuildingCatalogEntry)this.m_catalogEntry).getId(), new Object[0]);
        }
        if (fieldName.equals("conditions")) {
            final TextWidgetFormater twf = new TextWidgetFormater();
            final HavenWorldCatalogView havenWorldCatalogView = UIWorldEditorFrame.getInstance().getHavenWorldCatalogView();
            ((BuildingCatalogEntry)this.m_catalogEntry).forEachCondition(new TObjectProcedure<BuildingCondition>() {
                @Override
                public boolean execute(final BuildingCondition condition) {
                    if (twf.length() > 0) {
                        twf.newLine();
                    }
                    final MissingBuilding missingBuilding = HavenWorldCatalogBuildingEntryView.this.getConditionError(condition.getBuildingTypeNeeded());
                    if (missingBuilding != null) {
                        twf.addColor(Color.RED.getRGBtoHex());
                    }
                    final int quantity = condition.getQuantity();
                    if (quantity > 1) {
                        twf.append(quantity).append("x");
                    }
                    twf.append(WakfuTranslator.getInstance().getString(134, condition.getBuildingTypeNeeded(), new Object[0]));
                    if (missingBuilding != null) {
                        twf.closeText();
                    }
                    return true;
                }
            });
            return (twf.length() > 0) ? twf.finishAndToString() : null;
        }
        if (fieldName.equals("sidoaNeed")) {
            if (this.m_buildingDefinition == null) {
                return 0;
            }
            final AbstractBuildingDefinition building = this.m_buildingDefinition.getBuilding();
            if (building == null) {
                return 0;
            }
            final AbstractBuildingDefinition firstBuildingFor = BuildingDefinitionHelper.getFirstBuildingFor(building);
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            if (this.m_validator != null && this.m_validator.hasError(ConstructionError.Type.MissingWorker)) {
                twf2.addColor(Color.RED.getRGBtoHex());
            }
            twf2.append(firstBuildingFor.getNeededWorkers());
            return twf2.finishAndToString();
        }
        else if (fieldName.equals("ressourcesCost")) {
            final int resourceCost = ((BuildingCatalogEntry)this.m_catalogEntry).getResourceCost();
            if (resourceCost == 0) {
                return null;
            }
            final AbstractBuildingDefinition buildingDefinition = BuildingDefinitionHelper.getFirstBuildingFor((BuildingCatalogEntry)this.m_catalogEntry);
            final int cost = BuildingValidationHelper.getAdjustedResourceCost(buildingDefinition, HavenWorldViewManager.INSTANCE.getCurrentWorldId());
            final String string = WakfuTranslator.getInstance().formatNumber(cost);
            final TextWidgetFormater twf3 = new TextWidgetFormater();
            if (this.m_validator != null && this.m_validator.hasError(ConstructionError.Type.MissingResources)) {
                twf3.addColor(Color.RED.getRGBtoHex());
            }
            twf3.append(string);
            return twf3.finishAndToString();
        }
        else {
            if (!fieldName.equals("evolutionName")) {
                return this.m_buildingDefinition.getFieldValue(fieldName);
            }
            final AbstractBuildingDefinition building = this.getBuildingDefinition();
            if (building == null) {
                return null;
            }
            final BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(building.getId());
            if (evolution == null) {
                return null;
            }
            final AbstractBuildingDefinition buildingTo = HavenWorldDefinitionManager.INSTANCE.getBuilding(evolution.getBuildingToId());
            if (buildingTo == null) {
                return null;
            }
            return WakfuTranslator.getInstance().getString(126, buildingTo.getId(), new Object[0]);
        }
    }
    
    @Override
    public boolean hasEnoughKamas() {
        return this.m_validator == null || !this.m_validator.hasError(ConstructionError.Type.MissingKama);
    }
    
    @Override
    public int getCurrentQuantity() {
        return UIWorldEditorFrame.getInstance().getBuildingQuantityOfType(this.m_buildingDefinition.getBuilding().getId());
    }
    
    @Override
    public String getName() {
        return this.m_buildingDefinition.getName();
    }
    
    @Override
    public HavenWorldCatalogEntryView getCopy() {
        final HavenWorldCatalogBuildingEntryView havenWorldCatalogBuildingEntryView = new HavenWorldCatalogBuildingEntryView((BuildingCatalogEntry)this.m_catalogEntry);
        return havenWorldCatalogBuildingEntryView;
    }
    
    public AbstractBuildingDefinition getBuildingDefinition() {
        return BuildingDefinitionHelper.getLastBuildingFor((BuildingCatalogEntry)this.m_catalogEntry);
    }
    
    @Override
    public HavenWorldCatalogView.CatalogCategory getCategory() {
        return HavenWorldCatalogView.CatalogCategory.BUILDING;
    }
    
    public int getFirstBuildingDefinitionId() {
        return BuildingDefinitionHelper.getFirstBuildingFor((BuildingCatalogEntry)this.m_catalogEntry).getId();
    }
    
    private MissingBuilding getConditionError(final int buildingType) {
        if (this.m_validator == null) {
            return null;
        }
        final ArrayList<ConstructionError> errors = this.m_validator.getErrors();
        for (int i = 0, size = errors.size(); i < size; ++i) {
            final ConstructionError error = errors.get(i);
            if (error.getType() == ConstructionError.Type.MissingBuilding) {
                final MissingBuilding missingBuilding = (MissingBuilding)error;
                if (missingBuilding.getBuildingType() == buildingType) {
                    return missingBuilding;
                }
            }
        }
        return null;
    }
    
    static {
        FIELDS = HavenWorldCatalogEntryView.concatFields("iconUrl", "surface", "description", "sidoaNeed", "buildDelay", "conditions", "effects", "ressourcesCost", "evolutionName");
    }
}
