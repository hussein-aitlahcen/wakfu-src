package com.ankamagames.wakfu.common.game.eventsCalendar;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import gnu.trove.*;

public class WakfuCalendarEvent implements CalendarEvent, Poolable
{
    protected static Logger m_logger;
    private long m_uid;
    private GameDate m_startDate;
    private GameDate m_endDate;
    private String m_title;
    private String m_desc;
    private long m_ownerId;
    private boolean m_userDefinedTexts;
    private byte m_type;
    private byte m_subType;
    private byte m_maxParticipants;
    private TLongObjectHashMap<String> m_participants;
    private byte m_maxRegistrations;
    private TLongObjectHashMap<String> m_registered;
    private static long UID_TEMP_GEN;
    private boolean m_isCheckedOut;
    private static final ObjectPool POOL;
    private static int CHECKED_OUT_COUNT;
    
    public static WakfuCalendarEvent checkOut() {
        WakfuCalendarEvent obj;
        try {
            obj = (WakfuCalendarEvent)WakfuCalendarEvent.POOL.borrowObject();
            obj.m_isCheckedOut = true;
            ++WakfuCalendarEvent.CHECKED_OUT_COUNT;
        }
        catch (Exception e) {
            obj = new WakfuCalendarEvent();
            WakfuCalendarEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type CreateGuildRequestMessage : " + e.getMessage()));
        }
        return obj;
    }
    
    @Override
    public void onCheckOut() {
        this.m_maxParticipants = 2;
        this.m_maxRegistrations = 2;
    }
    
    @Override
    public void onCheckIn() {
        this.m_uid = -1L;
        this.m_startDate = null;
        this.m_endDate = null;
        this.m_desc = null;
        this.m_title = null;
        this.m_maxParticipants = 0;
        this.m_maxRegistrations = 0;
        this.m_ownerId = -1L;
        this.m_subType = 0;
        this.m_participants.clear();
        this.m_registered.clear();
    }
    
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                WakfuCalendarEvent.POOL.returnObject(this);
                --WakfuCalendarEvent.CHECKED_OUT_COUNT;
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                WakfuCalendarEvent.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    public WakfuCalendarEvent() {
        super();
        this.m_userDefinedTexts = true;
        this.m_subType = 0;
        this.m_maxParticipants = 2;
        this.m_participants = new TLongObjectHashMap<String>();
        this.m_maxRegistrations = 2;
        this.m_registered = new TLongObjectHashMap<String>();
    }
    
    public WakfuCalendarEvent(final String title, final String desc, final long ownerId, final byte type, final byte subType, final GameDateConst startDate) {
        this(title, desc, ownerId, type, subType, startDate, true);
    }
    
    public WakfuCalendarEvent(final String title, final String desc, final long ownerId, final byte type, final byte subType, final GameDateConst startDate, final boolean bUserDefinedTexts) {
        super();
        this.m_userDefinedTexts = true;
        this.m_subType = 0;
        this.m_maxParticipants = 2;
        this.m_participants = new TLongObjectHashMap<String>();
        this.m_maxRegistrations = 2;
        this.m_registered = new TLongObjectHashMap<String>();
        this.common(title, desc, ownerId, type, subType, bUserDefinedTexts);
        this.setStartDate(startDate);
    }
    
    public WakfuCalendarEvent(final String title, final String desc, final long ownerId, final byte type, final byte subType, final int day, final int month, final int year) {
        this(title, desc, ownerId, type, subType, day, month, year, true);
    }
    
    public WakfuCalendarEvent(final String title, final String desc, final long ownerId, final byte type, final byte subType, final int day, final int month, final int year, final boolean bUserDefinedTexts) {
        super();
        this.m_userDefinedTexts = true;
        this.m_subType = 0;
        this.m_maxParticipants = 2;
        this.m_participants = new TLongObjectHashMap<String>();
        this.m_maxRegistrations = 2;
        this.m_registered = new TLongObjectHashMap<String>();
        this.common(title, desc, ownerId, type, subType, bUserDefinedTexts);
        this.setStartDate(0, 0, day, month, year);
    }
    
    public WakfuCalendarEvent(final String title, final String desc, final long ownerId, final byte type, final byte subType, final byte day, final byte month, final short year, final byte hour, final byte minute) {
        this(title, desc, ownerId, type, subType, day, month, year, hour, minute, true);
    }
    
    public WakfuCalendarEvent(final String title, final String desc, final long ownerId, final byte type, final byte subType, final byte day, final byte month, final short year, final byte hour, final byte minute, final boolean bUserDefinedTexts) {
        super();
        this.m_userDefinedTexts = true;
        this.m_subType = 0;
        this.m_maxParticipants = 2;
        this.m_participants = new TLongObjectHashMap<String>();
        this.m_maxRegistrations = 2;
        this.m_registered = new TLongObjectHashMap<String>();
        this.common(title, desc, ownerId, type, subType, bUserDefinedTexts);
        this.setStartDate(minute, hour, day, month, year);
    }
    
    private void common(final String title, final String desc, final long ownerId, final byte type, final byte subType, final boolean bUserDefinedTexts) {
        this.setUid();
        this.m_title = ((title != null) ? title : "");
        this.m_desc = ((desc != null) ? desc : "");
        this.m_ownerId = ownerId;
        this.m_type = type;
        this.m_subType = subType;
        this.m_userDefinedTexts = bUserDefinedTexts;
    }
    
    public void copyFrom(final WakfuCalendarEvent source) {
        this.m_uid = source.m_uid;
        this.m_title = source.m_title;
        this.m_desc = source.m_desc;
        this.m_ownerId = source.m_ownerId;
        this.m_type = source.m_type;
        this.m_subType = source.m_subType;
        this.m_userDefinedTexts = source.m_userDefinedTexts;
        if (source.m_startDate != null) {
            this.m_startDate = new GameDate(source.m_startDate);
        }
        if (source.m_endDate != null) {
            this.m_endDate = new GameDate(source.m_endDate);
        }
        this.m_maxParticipants = source.m_maxParticipants;
        this.m_maxRegistrations = source.m_maxRegistrations;
        this.m_participants.clear();
        if (!source.m_participants.isEmpty()) {
            final TLongObjectIterator<String> it = source.m_participants.iterator();
            while (it.hasNext()) {
                it.advance();
                this.m_participants.put(it.key(), it.value());
            }
        }
        this.m_registered.clear();
        if (!source.m_registered.isEmpty()) {
            final TLongObjectIterator<String> it = source.m_registered.iterator();
            while (it.hasNext()) {
                it.advance();
                this.m_registered.put(it.key(), it.value());
            }
        }
    }
    
    public void setStartDate(final int minute, final int hour, final int day, final int month, final int year) {
        if (this.m_startDate == null) {
            this.m_startDate = new GameDate(0, minute, hour, day, month, year);
        }
        else {
            this.m_startDate.set(0, minute, hour, day, month, year);
        }
    }
    
    public void setEndDate(final int minute, final int hour, final int day, final int month, final int year) {
        if (this.m_endDate == null) {
            this.m_endDate = new GameDate(0, minute, hour, day, month, year);
        }
        else {
            this.m_endDate.set(0, minute, hour, day, month, year);
        }
    }
    
    public void setStartDate(final GameDateConst startDate) {
        this.m_startDate = new GameDate(startDate);
    }
    
    public void setEndDate(final GameDateConst endDate) {
        this.m_endDate = new GameDate(endDate);
    }
    
    public void setUid(final long uid) {
        this.m_uid = uid;
    }
    
    public void setUid() {
        this.m_uid = WakfuCalendarEvent.UID_TEMP_GEN--;
    }
    
    public long getUid() {
        return this.m_uid;
    }
    
    public void setTitle(String title) {
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        this.m_title = title;
    }
    
    public void setDesc(String desc) {
        if (desc.length() > 127) {
            desc = desc.substring(0, 127);
        }
        this.m_desc = desc;
    }
    
    public void setOwnerId(final long ownerId) {
        this.m_ownerId = ownerId;
    }
    
    public void setType(final byte type) {
        this.m_type = type;
    }
    
    public void setSubType(final byte subType) {
        this.m_subType = subType;
    }
    
    public void setTextsUserDefined(final boolean textsUserDefined) {
        this.m_userDefinedTexts = textsUserDefined;
    }
    
    public boolean areTextsUserDefined() {
        return this.m_userDefinedTexts;
    }
    
    public void setUserDefinedTexts(final boolean userDefinedTexts) {
        this.m_userDefinedTexts = userDefinedTexts;
    }
    
    public GameDate getModifiableStartDate() {
        return this.m_startDate;
    }
    
    @Override
    public GameDateConst getStartDate() {
        return this.m_startDate;
    }
    
    public GameDate getModifiableEndDate() {
        return this.m_endDate;
    }
    
    public GameDateConst getEndDate() {
        return this.m_endDate;
    }
    
    @Override
    public String getTitle() {
        return this.m_title;
    }
    
    @Override
    public String getDescription() {
        return this.m_desc;
    }
    
    @Override
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    @Override
    public byte getType() {
        return this.m_type;
    }
    
    public byte getSubType() {
        return this.m_subType;
    }
    
    public byte getMaxParticipants() {
        return this.m_maxParticipants;
    }
    
    public void setMaxParticipants(final byte maxParticipants) {
        this.m_maxParticipants = maxParticipants;
    }
    
    public byte getMaxRegistrations() {
        return this.m_maxRegistrations;
    }
    
    public void setMaxRegistrations(final byte maxRegistrations) {
        this.m_maxRegistrations = maxRegistrations;
    }
    
    public void addParticipant(final long participantId, final String participantName) {
        this.m_participants.put(participantId, participantName);
    }
    
    public String removeParticipant(final long participantId) {
        return this.m_participants.remove(participantId);
    }
    
    public TLongObjectHashMap<String> getParticipants() {
        return this.m_participants;
    }
    
    public void addRegistered(final long registeredId, final String registeredName) {
        this.m_registered.put(registeredId, registeredName);
    }
    
    public String removeRegistered(final long registeredId) {
        return this.m_registered.remove(registeredId);
    }
    
    public TLongObjectHashMap<String> getRegistered() {
        return this.m_registered;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WakfuCalendarEvent)) {
            return false;
        }
        final WakfuCalendarEvent that = (WakfuCalendarEvent)o;
        if (this.m_startDate != null) {
            if (this.m_startDate.getDay() != that.m_startDate.getDay()) {
                return false;
            }
            if (this.m_startDate.getMonth() != this.m_startDate.getMonth()) {
                return false;
            }
            if (this.m_startDate.getYear() != that.m_startDate.getYear()) {
                return false;
            }
        }
        if (this.m_ownerId != that.m_ownerId) {
            return false;
        }
        if (this.m_type != that.m_type) {
            return false;
        }
        if (this.m_subType != that.m_subType) {
            return false;
        }
        if (this.m_title != null) {
            if (this.m_title.toLowerCase().equals(that.m_title.toLowerCase())) {
                return true;
            }
        }
        else if (that.m_title == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.m_uid ^ this.m_uid >>> 32);
    }
    
    public int alternativeHashCode() {
        int result = 31 * HashFunctions.hash(this.m_startDate.toLong());
        result = 31 * result + ((this.m_title != null) ? this.m_title.hashCode() : 0);
        result = 31 * result + (int)(this.m_ownerId ^ this.m_ownerId >>> 32);
        result = 31 * result + this.m_type;
        result = 31 * result + this.m_subType;
        return result;
    }
    
    @Override
    public String toString() {
        return "WakfuCalendarEvent{" + "\n" + "m_uid=" + this.m_uid + ", " + "m_startDate=" + this.m_startDate + ", " + "m_endDate=" + this.m_endDate + ", " + "m_title=\"" + this.m_title + "\", " + "m_desc=\"" + this.m_desc + "\"\n" + "m_ownerId=" + this.m_ownerId + ", " + "m_type=" + this.m_type + "\n" + "m_evolution=" + this.m_subType + "\n" + "m_participants=" + this.setToString(this.m_participants) + ", " + "m_registered=" + this.setToString(this.m_registered) + "\n" + '}';
    }
    
    private String setToString(final TLongObjectHashMap<String> set) {
        final StringBuilder res = new StringBuilder();
        res.append("{");
        final TLongObjectIterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final String name = iterator.value();
            final long id = iterator.key();
            res.append("(").append(id).append(",").append(name).append(")").append(";");
        }
        res.append("}");
        return res.toString();
    }
    
    public boolean isOutOfDate(final GameDateConst currentDate) {
        return this.m_startDate.getYear() <= currentDate.getYear() && this.m_startDate.getMonth() < currentDate.getMonth();
    }
    
    public WakfuCalendarEvent getCopy(final boolean pooled) {
        WakfuCalendarEvent res;
        if (pooled) {
            res = checkOut();
        }
        else {
            res = new WakfuCalendarEvent();
        }
        res.m_title = this.m_title;
        res.m_desc = this.m_desc;
        res.m_startDate = this.m_startDate;
        res.m_endDate = this.m_endDate;
        res.m_maxParticipants = this.m_maxParticipants;
        res.m_maxRegistrations = this.m_maxRegistrations;
        res.m_ownerId = this.m_ownerId;
        res.m_participants = this.m_participants.clone();
        res.m_registered = this.m_registered.clone();
        res.m_type = this.m_type;
        res.m_subType = this.m_subType;
        res.m_uid = this.m_uid;
        return res;
    }
    
    static {
        WakfuCalendarEvent.m_logger = Logger.getLogger((Class)WakfuCalendarEvent.class);
        WakfuCalendarEvent.UID_TEMP_GEN = -1L;
        POOL = new MonitoredPool(new ObjectFactory<WakfuCalendarEvent>() {
            @Override
            public WakfuCalendarEvent makeObject() {
                return new WakfuCalendarEvent();
            }
        });
        WakfuCalendarEvent.CHECKED_OUT_COUNT = 0;
    }
}
