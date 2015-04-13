package com.ankamagames.wakfu.client.core.game.almanach;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.almanach.*;
import com.ankamagames.wakfu.client.core.game.almanach.soap.*;
import com.ankamagames.wakfu.client.core.game.almanach.zodiac.*;

public class AlmanachViewManager
{
    public static final AlmanachViewManager INSTANCE;
    private static final AlmanachEntryView NO_ENTRY_VIEW;
    private final TIntObjectHashMap<AlmanachEntryView> m_entryViews;
    private final TShortObjectHashMap<AlmanachDateView> m_dateViews;
    private final TShortObjectHashMap<AlmanachEventEntryView> m_eventEntryViews;
    private final TByteObjectHashMap<AlmanachMonthEntryView> m_monthEntryViews;
    private final ArrayList<AlmanachZodiacEntryView> m_zodiakEntryViews;
    
    private AlmanachViewManager() {
        super();
        this.m_entryViews = new TIntObjectHashMap<AlmanachEntryView>();
        this.m_dateViews = new TShortObjectHashMap<AlmanachDateView>();
        this.m_eventEntryViews = new TShortObjectHashMap<AlmanachEventEntryView>();
        this.m_monthEntryViews = new TByteObjectHashMap<AlmanachMonthEntryView>();
        this.m_zodiakEntryViews = new ArrayList<AlmanachZodiacEntryView>();
    }
    
    public AlmanachEntryView getAlmanachEntry(final int entryId) {
        synchronized (this.m_entryViews) {
            final AlmanachEntryView almanachEntryView = this.m_entryViews.get(entryId);
            if (almanachEntryView != null) {
                return almanachEntryView;
            }
            final AlmanachEntry entry = AlmanachEntryManager.INSTANCE.getEntry(entryId);
            if (entry == AlmanachEntry.NO_ENTRY) {
                return AlmanachViewManager.NO_ENTRY_VIEW;
            }
            final AlmanachEntryView addedEntryView = new AlmanachEntryView(entry.getId());
            this.m_entryViews.put(entryId, addedEntryView);
            return addedEntryView;
        }
    }
    
    public AlmanachDateView getAlmanachDate(final GameDateConst date) {
        synchronized (this.m_dateViews) {
            final short dateCode = AlmanachHelper.dateToHashcode(date);
            final AlmanachDateView dateView = this.m_dateViews.get(dateCode);
            if (dateView != null) {
                return dateView;
            }
            final AlmanachDateView newDateView = new AlmanachDateView(date);
            this.m_dateViews.put(dateCode, newDateView);
            return newDateView;
        }
    }
    
    public AlmanachEventEntryView getAlmanachEventEntry(final GameDateConst date) {
        synchronized (this.m_eventEntryViews) {
            final short dateCode = AlmanachHelper.dateToHashcode(date);
            final AlmanachEventEntryView entryView = this.m_eventEntryViews.get(dateCode);
            if (entryView != null) {
                return entryView;
            }
            final AlmanachEventEntryView newEntryView = new AlmanachEventEntryView();
            this.m_eventEntryViews.put(dateCode, newEntryView);
            AlmanachGetEvent.INSTANCE.loadEntryFor(date);
            return newEntryView;
        }
    }
    
    public AlmanachMonthEntryView getAlmanachMonthEntry(final GameDateConst date, final byte month) {
        synchronized (this.m_monthEntryViews) {
            final AlmanachMonthEntryView entryView = this.m_monthEntryViews.get(month);
            if (entryView != null) {
                return entryView;
            }
            final AlmanachMonthEntryView newEntryView = new AlmanachMonthEntryView();
            this.m_monthEntryViews.put(month, newEntryView);
            AlmanachGetEvent.INSTANCE.loadEntryFor(date);
            return newEntryView;
        }
    }
    
    public AlmanachZodiacEntryView getAlmanachZodiacEntry(final GameDateConst date) {
        synchronized (this.m_zodiakEntryViews) {
            for (int i = 0, size = this.m_zodiakEntryViews.size(); i < size; ++i) {
                final AlmanachZodiacEntryView zodiakEntryView = this.m_zodiakEntryViews.get(i);
                if (zodiakEntryView.getDate().compareTo(date) == 0) {
                    return zodiakEntryView;
                }
                final AlmanachZodiacEntry entry = zodiakEntryView.getEntry();
                if (entry != null) {
                    if (entry.isDateValid(date)) {
                        return zodiakEntryView;
                    }
                }
            }
            final AlmanachZodiacEntryView entryView = new AlmanachZodiacEntryView(date);
            this.m_zodiakEntryViews.add(entryView);
            return entryView;
        }
    }
    
    public void setAlmanachZodiakEntryData(final AlmanachZodiacEntry entry, final GameDateConst date) {
        synchronized (this.m_zodiakEntryViews) {
            boolean found = false;
            for (int i = 0, size = this.m_zodiakEntryViews.size(); i < size; ++i) {
                final AlmanachZodiacEntryView zodiakEntryView = this.m_zodiakEntryViews.get(i);
                if (entry.isDateValid(zodiakEntryView.getDate())) {
                    zodiakEntryView.setEntry(entry);
                    found = true;
                }
            }
            if (!found) {
                final AlmanachZodiacEntryView zodiacEntryView = new AlmanachZodiacEntryView(date);
                zodiacEntryView.setEntry(entry);
                this.m_zodiakEntryViews.add(zodiacEntryView);
            }
        }
    }
    
    public void setAlmanachMonthEntryData(final AlmanachMonthEntry entry, final GameDateConst date) {
        synchronized (this.m_monthEntryViews) {
            final byte month = (byte)date.getMonth();
            AlmanachMonthEntryView entryView = this.m_monthEntryViews.get(month);
            if (entryView == null) {
                entryView = new AlmanachMonthEntryView();
                this.m_monthEntryViews.put(month, entryView);
            }
            entryView.setEntry(entry);
        }
    }
    
    public void setAlmanachEventEntryData(final AlmanachEventEntry entry, final GameDateConst date) {
        synchronized (this.m_eventEntryViews) {
            final short dateCode = AlmanachHelper.dateToHashcode(date);
            AlmanachEventEntryView entryView = this.m_eventEntryViews.get(dateCode);
            if (entryView == null) {
                entryView = new AlmanachEventEntryView();
                this.m_eventEntryViews.put(dateCode, entryView);
            }
            entryView.setEntry(entry);
        }
    }
    
    static {
        INSTANCE = new AlmanachViewManager();
        NO_ENTRY_VIEW = new AlmanachEntryView(AlmanachEntry.NO_ENTRY.getId());
    }
}
