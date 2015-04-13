package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;

public class InventoryDisplayModeView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String ICON = "icon";
    public static final String MODE = "mode";
    public final String[] FIELDS;
    private final InventoryDisplayMode m_inventoryDisplayMode;
    
    public InventoryDisplayModeView(final InventoryDisplayMode inventoryDisplayMode) {
        super();
        this.FIELDS = new String[] { "name", "icon", "mode" };
        this.m_inventoryDisplayMode = inventoryDisplayMode;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(this.m_inventoryDisplayMode.name());
        }
        if (fieldName.equals("mode")) {
            return this.m_inventoryDisplayMode;
        }
        if (fieldName.equals("icon")) {
            return WakfuConfiguration.getInstance().getItemTypeIconUrl(this.m_inventoryDisplayMode.m_iconId);
        }
        return null;
    }
    
    public InventoryDisplayMode getInventoryDisplayMode() {
        return this.m_inventoryDisplayMode;
    }
}
