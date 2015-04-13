package com.ankamagames.wakfu.common.game.eventsCalendar.serialisation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class WakfuEventsCalendarSerializer extends BinarSerial implements Poolable
{
    private static final Logger m_logger;
    EventsCalendar<WakfuCalendarEvent> m_eventsCalendar;
    private boolean m_isCheckedOut;
    private static final ObjectPool POOL;
    public final BinarSerialPart EVENTS;
    
    private WakfuEventsCalendarSerializer() {
        super();
        this.EVENTS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final Set<WakfuCalendarEvent> events = WakfuEventsCalendarSerializer.this.m_eventsCalendar.getEvents();
                buffer.putInt(events.size());
                final WakfuCalendarEventSerializer eventSerializable = WakfuCalendarEventSerializer.checkOut();
                for (final WakfuCalendarEvent calendarEvent : events) {
                    eventSerializable.setEventForSerialisation(calendarEvent);
                    eventSerializable.serialize(buffer);
                }
                eventSerializable.release();
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuEventsCalendarSerializer.this.m_eventsCalendar.clear();
                final int size = buffer.getInt();
                final WakfuCalendarEventSerializer eventSerializable = WakfuCalendarEventSerializer.checkOut();
                for (int i = 0; i < size; ++i) {
                    eventSerializable.unserialize(buffer);
                    WakfuEventsCalendarSerializer.this.m_eventsCalendar.addCalendarEvent(eventSerializable.getEvent());
                    eventSerializable.setEventForSerialisation(null);
                }
                eventSerializable.release();
            }
            
            @Override
            public int expectedSize() {
                if (WakfuEventsCalendarSerializer.this.m_eventsCalendar == null) {
                    return 0;
                }
                final Set<WakfuCalendarEvent> events = WakfuEventsCalendarSerializer.this.m_eventsCalendar.getEvents();
                int eventsTotalSize = 0;
                final WakfuCalendarEventSerializer eventSerializable = WakfuCalendarEventSerializer.checkOut();
                for (final WakfuCalendarEvent calendarEvent : events) {
                    eventSerializable.setEventForSerialisation(calendarEvent);
                    eventsTotalSize += eventSerializable.expectedSize();
                }
                eventSerializable.release();
                return 4 + eventsTotalSize;
            }
        };
    }
    
    public static WakfuEventsCalendarSerializer checkOut(final EventsCalendar<WakfuCalendarEvent> eventsCalendar) {
        WakfuEventsCalendarSerializer obj;
        try {
            obj = (WakfuEventsCalendarSerializer)WakfuEventsCalendarSerializer.POOL.borrowObject();
            obj.setCheckedOut(true);
            obj.setEventsCalendar(eventsCalendar);
        }
        catch (Exception e) {
            obj = new WakfuEventsCalendarSerializer();
            WakfuEventsCalendarSerializer.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type WakfuEventsCalendarSerializable : " + e.getMessage()));
        }
        return obj;
    }
    
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                WakfuEventsCalendarSerializer.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                WakfuEventsCalendarSerializer.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_eventsCalendar = null;
    }
    
    @Override
    public void onCheckIn() {
        this.m_eventsCalendar = null;
    }
    
    private void setCheckedOut(final boolean checkedOut) {
        this.m_isCheckedOut = checkedOut;
    }
    
    public WakfuEventsCalendarSerializer(final EventsCalendar<WakfuCalendarEvent> eventsCalendar) {
        super();
        this.EVENTS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                final Set<WakfuCalendarEvent> events = WakfuEventsCalendarSerializer.this.m_eventsCalendar.getEvents();
                buffer.putInt(events.size());
                final WakfuCalendarEventSerializer eventSerializable = WakfuCalendarEventSerializer.checkOut();
                for (final WakfuCalendarEvent calendarEvent : events) {
                    eventSerializable.setEventForSerialisation(calendarEvent);
                    eventSerializable.serialize(buffer);
                }
                eventSerializable.release();
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuEventsCalendarSerializer.this.m_eventsCalendar.clear();
                final int size = buffer.getInt();
                final WakfuCalendarEventSerializer eventSerializable = WakfuCalendarEventSerializer.checkOut();
                for (int i = 0; i < size; ++i) {
                    eventSerializable.unserialize(buffer);
                    WakfuEventsCalendarSerializer.this.m_eventsCalendar.addCalendarEvent(eventSerializable.getEvent());
                    eventSerializable.setEventForSerialisation(null);
                }
                eventSerializable.release();
            }
            
            @Override
            public int expectedSize() {
                if (WakfuEventsCalendarSerializer.this.m_eventsCalendar == null) {
                    return 0;
                }
                final Set<WakfuCalendarEvent> events = WakfuEventsCalendarSerializer.this.m_eventsCalendar.getEvents();
                int eventsTotalSize = 0;
                final WakfuCalendarEventSerializer eventSerializable = WakfuCalendarEventSerializer.checkOut();
                for (final WakfuCalendarEvent calendarEvent : events) {
                    eventSerializable.setEventForSerialisation(calendarEvent);
                    eventsTotalSize += eventSerializable.expectedSize();
                }
                eventSerializable.release();
                return 4 + eventsTotalSize;
            }
        };
        this.m_eventsCalendar = eventsCalendar;
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.EVENTS };
    }
    
    public EventsCalendar<WakfuCalendarEvent> getEventsCalendar() {
        return this.m_eventsCalendar;
    }
    
    public void setEventsCalendar(final EventsCalendar<WakfuCalendarEvent> eventsCalendar) {
        this.m_eventsCalendar = eventsCalendar;
    }
    
    public byte[] buildForAllInformations() {
        return this.build(this.partsEnumeration());
    }
    
    public int expectedSize() {
        return this.EVENTS.expectedSize();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuEventsCalendarSerializer.class);
        POOL = new MonitoredPool(new ObjectFactory<WakfuEventsCalendarSerializer>() {
            @Override
            public WakfuEventsCalendarSerializer makeObject() {
                return new WakfuEventsCalendarSerializer((WakfuEventsCalendarSerializer$1)null);
            }
        });
    }
}
