package com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public abstract class HavenWorldCatalogEntryView<T extends HavenWorldCatalogEntry> extends ImmutableFieldProvider
{
    private static final String NAME_FIELD = "name";
    private static final String ENABLED_FIELD = "enabled";
    private static final String KAMAS_COST_FIELD = "kamasCost";
    private static final String CURRENT_QUANTITY_FIELD = "currentQuantity";
    private static final String MAX_QUANTITY_FIELD = "maxQuantity";
    private static final String QUANTITY_TEXT_FIELD = "quantityText";
    protected static final String[] COMMON_FIELDS;
    protected BaseBuildingConditionValidator m_validator;
    protected final T m_catalogEntry;
    private boolean m_available;
    
    protected static String[] concatFields(final String... fields) {
        final String[] result = new String[HavenWorldCatalogEntryView.COMMON_FIELDS.length + fields.length];
        System.arraycopy(HavenWorldCatalogEntryView.COMMON_FIELDS, 0, result, 0, HavenWorldCatalogEntryView.COMMON_FIELDS.length);
        System.arraycopy(fields, 0, result, HavenWorldCatalogEntryView.COMMON_FIELDS.length, fields.length);
        return result;
    }
    
    protected HavenWorldCatalogEntryView(final T catalogEntry) {
        super();
        this.m_catalogEntry = catalogEntry;
    }
    
    public abstract HavenWorldCatalogView.CatalogCategory getCategory();
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("kamasCost")) {
            int cost;
            if (this.m_catalogEntry instanceof BuildingCatalogEntry) {
                final BuildingCatalogEntry catalogEntry = (BuildingCatalogEntry)this.m_catalogEntry;
                final AbstractBuildingDefinition buildingDefinition = BuildingDefinitionHelper.getFirstBuildingFor(catalogEntry);
                cost = BuildingValidationHelper.getAdjustedKamaCost(buildingDefinition, HavenWorldViewManager.INSTANCE.getCurrentWorldId());
            }
            else {
                cost = this.m_catalogEntry.getKamaCost();
            }
            final String text = WakfuTranslator.getInstance().formatNumber(cost);
            final TextWidgetFormater twf = new TextWidgetFormater();
            if (!this.hasEnoughKamas()) {
                twf.addColor(Color.RED.getRGBtoHex());
            }
            twf.append(text);
            return twf.finishAndToString();
        }
        if (fieldName.equals("enabled")) {
            return this.isAvailable();
        }
        if (fieldName.equals("currentQuantity")) {
            return this.getCurrentQuantity();
        }
        if (fieldName.equals("maxQuantity")) {
            return this.m_catalogEntry.getMaxQuantity();
        }
        if (!fieldName.equals("quantityText")) {
            return null;
        }
        if (this.m_catalogEntry.getMaxQuantity() <= 0) {
            return null;
        }
        return this.getCurrentQuantity() + "/" + this.m_catalogEntry.getMaxQuantity();
    }
    
    public abstract boolean hasEnoughKamas();
    
    public abstract int getCurrentQuantity();
    
    public T getCatalogEntry() {
        return this.m_catalogEntry;
    }
    
    public String getName() {
        return HavenWorldViewHelper.getCatalogEntryName(this.getCatalogEntry());
    }
    
    public void setValidator(final BaseBuildingConditionValidator validator) {
        this.m_validator = validator;
    }
    
    public void setAvailable(final boolean available) {
        this.m_available = available;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "enabled", "maxQuantity", "quantityText");
    }
    
    public boolean isAvailable() {
        return this.m_available;
    }
    
    public abstract HavenWorldCatalogEntryView getCopy();
    
    static {
        COMMON_FIELDS = new String[] { "name", "kamasCost", "enabled", "quantityText" };
    }
}
