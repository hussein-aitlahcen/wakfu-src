package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.client.core.*;

public class GuildStorageHistoryMoneyEntryView extends GuildStorageHistoryEntryView<GuildStorageHistoryMoneyEntry>
{
    public static final String MONEY_AMOUNT = "amount";
    
    public GuildStorageHistoryMoneyEntryView(final GuildStorageHistoryMoneyEntry entry) {
        super(entry);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("amount")) {
            return WakfuTranslator.getInstance().formatNumber(Math.abs(((GuildStorageHistoryMoneyEntry)this.m_entry).getAmount())) + '§';
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    protected int getType() {
        return 1;
    }
}
