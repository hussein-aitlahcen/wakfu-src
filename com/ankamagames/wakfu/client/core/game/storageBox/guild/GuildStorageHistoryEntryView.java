package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class GuildStorageHistoryEntryView<T extends GuildStorageHistoryEntry> extends ImmutableFieldProvider implements Comparable<GuildStorageHistoryEntryView>
{
    protected static final int ITEM_TYPE = 0;
    protected static final int MONEY_TYPE = 1;
    public static final String TYPE = "type";
    public static final String MEMBER_NAME_FIELD = "memberName";
    public static final String DATE_FIELD = "date";
    private static final String[] FIELDS;
    protected final T m_entry;
    
    public GuildStorageHistoryEntryView(final T entry) {
        super();
        this.m_entry = entry;
    }
    
    @Override
    public String[] getFields() {
        return GuildStorageHistoryEntryView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("memberName")) {
            return this.m_entry.getMemberName();
        }
        if (fieldName.equals("date")) {
            final GameDate gameDate = GameDate.fromLong(this.m_entry.getDate());
            return WakfuTranslator.getInstance().formatDateShort(gameDate);
        }
        if (fieldName.equals("type")) {
            return this.getType();
        }
        return null;
    }
    
    protected abstract int getType();
    
    @Override
    public int compareTo(final GuildStorageHistoryEntryView o) {
        return MathHelper.ensureInt(o.m_entry.getDate() - this.m_entry.getDate());
    }
    
    static {
        FIELDS = new String[0];
    }
}
