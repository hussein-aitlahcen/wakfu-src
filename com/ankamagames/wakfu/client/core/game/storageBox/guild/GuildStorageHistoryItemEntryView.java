package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import com.ankamagames.wakfu.common.game.guild.storage.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class GuildStorageHistoryItemEntryView extends GuildStorageHistoryEntryView<GuildStorageHistoryItemEntry>
{
    public static final String QUANTITY_FIELD = "quantity";
    @Nullable
    private final Item m_item;
    
    public GuildStorageHistoryItemEntryView(final GuildStorageHistoryItemEntry entry) {
        super(entry);
        final ReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(entry.getItem().refId);
        if (refItem != null) {
            (this.m_item = new Item()).fromRaw(entry.getItem());
        }
        else {
            this.m_item = null;
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final Object value = super.getFieldValue(fieldName);
        if (value != null) {
            return value;
        }
        if (fieldName.equals("quantity")) {
            return ((GuildStorageHistoryItemEntry)this.m_entry).getQty();
        }
        return (this.m_item != null) ? this.m_item.getFieldValue(fieldName) : null;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    @Override
    protected int getType() {
        return 0;
    }
}
