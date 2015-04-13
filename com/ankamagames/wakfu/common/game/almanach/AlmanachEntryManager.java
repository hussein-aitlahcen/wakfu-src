package com.ankamagames.wakfu.common.game.almanach;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class AlmanachEntryManager
{
    public static final AlmanachEntryManager INSTANCE;
    private final TIntObjectHashMap<AlmanachEntry> m_entries;
    private final TShortIntHashMap m_regularDateToEntry;
    
    private AlmanachEntryManager() {
        super();
        this.m_entries = new TIntObjectHashMap<AlmanachEntry>();
        this.m_regularDateToEntry = new TShortIntHashMap();
    }
    
    public void addEntry(final AlmanachEntry entry) {
        this.m_entries.put(entry.getId(), entry);
    }
    
    public void addRegularDate(final Date date, final int almanachEntry) {
        this.m_regularDateToEntry.put(AlmanachHelper.dateToHashcode(date), almanachEntry);
    }
    
    public AlmanachEntry getEntryFor(final GameDateConst date) {
        final int entryId = this.m_regularDateToEntry.get(AlmanachHelper.dateToHashcode(date));
        final AlmanachEntry entry = this.m_entries.get(entryId);
        return (entry != null) ? entry : AlmanachEntry.NO_ENTRY;
    }
    
    public AlmanachEntry getEntry(final int id) {
        final AlmanachEntry entry = this.m_entries.get(id);
        if (entry != null) {
            return entry;
        }
        return AlmanachEntry.NO_ENTRY;
    }
    
    static {
        INSTANCE = new AlmanachEntryManager();
    }
}
