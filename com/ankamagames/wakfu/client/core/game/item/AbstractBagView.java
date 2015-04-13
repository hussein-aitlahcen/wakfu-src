package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;

public class AbstractBagView extends ImmutableFieldProvider
{
    public static final String BAG_ICON_FIELD = "bagIconUrl";
    public static final String BAG_NAME_FIELD = "bagName";
    public static final String BAG_ID_FIELD = "bagId";
    public static final String BAG_INVENTORY_FIELD = "bagInventory";
    public static final String BAG_SIZE_FIELD = "bagSize";
    public static final String BAG_NAME_SIZE_FIELD = "bagNameSize";
    public static final String BAG_POSITION_FIELD = "bagPosition";
    public static final String CAN_BE_SORTED = "canBeSorted";
    public static final String[] FIELDS;
    
    @Override
    public String[] getFields() {
        return AbstractBagView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return null;
    }
    
    static {
        FIELDS = new String[] { "bagName", "bagId", "bagInventory", "bagSize", "bagNameSize", "bagPosition", "canBeSorted" };
    }
}
