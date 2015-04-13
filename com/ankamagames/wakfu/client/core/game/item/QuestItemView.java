package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.property.*;

public class QuestItemView extends ImmutableFieldProvider
{
    public static final String IS_REF_ITEM_FIELD = "isRefItem";
    public static final String BACKGROUND_STYLE_FIELD = "backgroundStyle";
    private static final String[] FIELDS;
    private Item m_defaultItem;
    
    public QuestItemView(final Item defaultItem) {
        super();
        this.m_defaultItem = defaultItem;
    }
    
    @Override
    public String[] getFields() {
        return QuestItemView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isRefItem")) {
            return true;
        }
        if (!fieldName.equals("backgroundStyle")) {
            return this.m_defaultItem.getFieldValue(fieldName);
        }
        final Item itemDetail = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", "equipmentDialog");
        if (itemDetail != null && this.m_defaultItem.getReferenceId() == itemDetail.getReferenceId()) {
            return ItemDisplayerImpl.BackgroundStyle.SELECTED.getStyle();
        }
        return ItemDisplayerImpl.BackgroundStyle.DEFAULT.getStyle();
    }
    
    public Item getDefaultItem() {
        return this.m_defaultItem;
    }
    
    static {
        FIELDS = new String[] { "isRefItem", "backgroundStyle" };
    }
}
