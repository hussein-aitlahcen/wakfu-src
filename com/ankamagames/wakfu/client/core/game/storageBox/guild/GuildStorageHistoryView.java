package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import java.util.*;

public class GuildStorageHistoryView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    private GuildStorageHistory m_history;
    public static final String ENTRIES_FIELD = "entries";
    public static final String[] FIELDS;
    private ArrayList<GuildStorageHistoryEntryView> m_entries;
    
    public GuildStorageHistoryView(final GuildStorageHistory history) {
        super();
        this.m_history = history;
        this.m_entries = new ArrayList<GuildStorageHistoryEntryView>();
        this.m_history.forEachItem(new FillItems());
        this.m_history.forEachMoney(new FillMoney());
        Collections.sort(this.m_entries);
    }
    
    @Override
    public String[] getFields() {
        return GuildStorageHistoryView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("entries")) {
            return this.m_entries;
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildStorageHistoryView.class);
        FIELDS = new String[] { "entries" };
    }
    
    private class FillItems implements TObjectProcedure<GuildStorageHistoryItemEntry>
    {
        @Override
        public boolean execute(final GuildStorageHistoryItemEntry object) {
            try {
                GuildStorageHistoryView.this.m_entries.add(new GuildStorageHistoryItemEntryView(object));
            }
            catch (Exception e) {
                GuildStorageHistoryView.m_logger.error((Object)e.getMessage(), (Throwable)e);
            }
            return true;
        }
    }
    
    private class FillMoney implements TObjectProcedure<GuildStorageHistoryMoneyEntry>
    {
        @Override
        public boolean execute(final GuildStorageHistoryMoneyEntry object) {
            GuildStorageHistoryView.this.m_entries.add(new GuildStorageHistoryMoneyEntryView(object));
            return true;
        }
    }
}
