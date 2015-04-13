package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;

public class CharacterEventsCalendar implements EventsCalendar<WakfuCalendarEvent>
{
    protected static Logger m_logger;
    private static CharacterEventsCalendar m_instance;
    private final LinkedList<WakfuCalendarEvent> m_addedEventsStack;
    private final TByteObjectHashMap<TLongHashSet> m_eventIdByEventType;
    private final HashMap<Long, WakfuCalendarEvent> m_eventById;
    private TLongObjectHashMap<TLongHashSet> m_eventsByDate;
    
    public CharacterEventsCalendar() {
        super();
        this.m_addedEventsStack = new LinkedList<WakfuCalendarEvent>();
        this.m_eventIdByEventType = new TByteObjectHashMap<TLongHashSet>();
        this.m_eventById = new HashMap<Long, WakfuCalendarEvent>();
        this.m_eventsByDate = new TLongObjectHashMap<TLongHashSet>();
    }
    
    public static CharacterEventsCalendar getInstance() {
        return CharacterEventsCalendar.m_instance;
    }
    
    @Override
    public void addCalendarEvent(final WakfuCalendarEvent event) {
        this.addEventAtDate(event);
        this.m_addedEventsStack.addFirst(event);
        this.m_eventById.put(event.getUid(), event);
        this.putEventByType(event);
    }
    
    public void modifyEvent(final WakfuCalendarEvent event) {
        if (event == null) {
            return;
        }
        final WakfuCalendarEvent oldEvent = this.m_eventById.get(event.getUid());
        this.removeEventByType(oldEvent);
        this.removeEventAtDate(oldEvent);
        this.putEventByType(event);
        this.addEventAtDate(event);
        this.m_eventById.put(event.getUid(), event);
    }
    
    private void addEventAtDate(final WakfuCalendarEvent event) {
        final long dayLong = event.getStartDate().dayToLong();
        TLongHashSet eventsAtThisDate = this.m_eventsByDate.get(dayLong);
        if (eventsAtThisDate == null) {
            eventsAtThisDate = new TLongHashSet();
            this.m_eventsByDate.put(dayLong, eventsAtThisDate);
        }
        eventsAtThisDate.add(event.getUid());
    }
    
    @Override
    public void removeCalendarEvent(final WakfuCalendarEvent event) {
        this.removeEventAtDate(event);
        this.m_addedEventsStack.remove(event);
        this.removeEventByType(event);
        this.m_eventById.remove(event.getUid());
    }
    
    private boolean removeEventAtDate(final WakfuCalendarEvent event) {
        final long dayLong = event.getStartDate().dayToLong();
        final TLongHashSet atSameDate = this.m_eventsByDate.get(dayLong);
        if (atSameDate == null) {
            return true;
        }
        atSameDate.remove(event.getUid());
        if (atSameDate.isEmpty()) {
            this.m_eventsByDate.remove(dayLong);
        }
        return false;
    }
    
    @Override
    public Set<WakfuCalendarEvent> getEvents() {
        return new HashSet<WakfuCalendarEvent>(this.m_eventById.values());
    }
    
    @Override
    public boolean isEmpty() {
        return this.m_eventsByDate.isEmpty() && this.m_eventById.isEmpty() && this.m_eventIdByEventType.isEmpty() && this.m_addedEventsStack.isEmpty();
    }
    
    @Override
    public boolean asIdenticalEvent(final WakfuCalendarEvent event) {
        if (this.isEmpty()) {
            return false;
        }
        final Collection<WakfuCalendarEvent> events = this.m_eventById.values();
        final int eventAltarnativeHashCode = event.alternativeHashCode();
        for (final WakfuCalendarEvent wakfuCalendarEvent : events) {
            if (wakfuCalendarEvent.alternativeHashCode() == eventAltarnativeHashCode) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void clear() {
        this.m_eventById.clear();
        this.m_addedEventsStack.clear();
        this.m_eventIdByEventType.clear();
        this.m_eventsByDate.clear();
    }
    
    private void removeEventByType(final WakfuCalendarEvent event) {
        final TLongHashSet sameType = this.m_eventIdByEventType.get(event.getType());
        if (sameType == null) {
            return;
        }
        sameType.remove(event.getUid());
        if (sameType.isEmpty()) {
            this.m_eventIdByEventType.remove(event.getType());
        }
    }
    
    private void putEventByType(final WakfuCalendarEvent event) {
        TLongHashSet sameType = this.m_eventIdByEventType.get(event.getType());
        if (sameType == null) {
            sameType = new TLongHashSet();
            this.m_eventIdByEventType.put(event.getType(), sameType);
        }
        sameType.add(event.getUid());
    }
    
    public void removeLastEventsAdd() {
        final WakfuCalendarEvent lastAdded = this.m_addedEventsStack.removeFirst();
        if (lastAdded != null) {
            final TLongHashSet sameType = this.m_eventIdByEventType.get(lastAdded.getType());
            if (sameType != null) {
                sameType.remove(lastAdded.getUid());
            }
        }
        this.m_eventById.remove(lastAdded.getUid());
        this.removeCalendarEvent(lastAdded);
    }
    
    public void clearLastAddedStack() {
        this.m_addedEventsStack.clear();
    }
    
    public WakfuCalendarEvent getEventById(final long eventId) {
        return this.m_eventById.get(eventId);
    }
    
    public TLongHashSet getEventIdsByType(final byte type) {
        return this.m_eventIdByEventType.get(type);
    }
    
    @Override
    public Set<WakfuCalendarEvent> getEventsByDate(final GameDateConst date) {
        final long dayLong = date.dayToLong();
        final TLongHashSet atSameDate = this.m_eventsByDate.get(dayLong);
        if (atSameDate == null || atSameDate.isEmpty()) {
            return (Set<WakfuCalendarEvent>)Collections.EMPTY_SET;
        }
        final Set<WakfuCalendarEvent> res = new HashSet<WakfuCalendarEvent>();
        for (final long evenId : atSameDate) {
            assert this.m_eventById.get(evenId) != null : "Liste des eventsById foireuse";
            res.add(this.m_eventById.get(evenId));
        }
        return res;
    }
    
    public void validateEvent(final long eventAlternativeHashCode) {
        for (final WakfuCalendarEvent event : this.m_addedEventsStack) {
            if (event.alternativeHashCode() == eventAlternativeHashCode) {
                this.m_addedEventsStack.remove(event);
            }
        }
    }
    
    public void validateEvent(final WakfuCalendarEvent event) {
        this.m_addedEventsStack.remove(event);
    }
    
    public WakfuCalendarEvent getEventByAlternativeHashCode(final long eventAlternativeHashCode) {
        final Set<WakfuCalendarEvent> events = this.getEvents();
        for (final WakfuCalendarEvent event : events) {
            if (event.alternativeHashCode() == eventAlternativeHashCode) {
                return event;
            }
        }
        return null;
    }
    
    public void display() {
        CharacterEventsCalendar.m_logger.info((Object)this.toString());
    }
    
    public void removeCalendarEventType(final byte type) {
        final TLongHashSet eventsToRemove = this.getEventIdsByType(type);
        final long[] tEventsToRemove = eventsToRemove.toArray();
        for (int i = 0; i < tEventsToRemove.length; ++i) {
            final long id = tEventsToRemove[i];
            this.removeCalendarEvent(this.getEventById(id));
        }
    }
    
    public void changeEventUid(final WakfuCalendarEvent event, final long newUid) {
        final long dayLong = event.getStartDate().dayToLong();
        TLongHashSet atSameDate = this.m_eventsByDate.get(dayLong);
        if (atSameDate == null) {
            atSameDate = new TLongHashSet();
            this.m_eventsByDate.put(dayLong, atSameDate);
        }
        else {
            atSameDate.remove(event.getUid());
        }
        TLongHashSet sameType = this.m_eventIdByEventType.get(event.getType());
        if (sameType == null) {
            sameType = new TLongHashSet();
            this.m_eventIdByEventType.put(event.getType(), sameType);
        }
        else {
            sameType.remove(event.getUid());
        }
        this.m_eventById.remove(event.getUid());
        event.setUid(newUid);
        atSameDate.add(newUid);
        sameType.add(newUid);
        this.m_eventById.put(newUid, event);
    }
    
    @Override
    public int size() {
        return this.m_eventById.size();
    }
    
    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder();
        res.append("CharacterEventsCalendar{\n");
        final long[] keys = this.m_eventsByDate.keys();
        Arrays.sort(keys);
        for (int i = 0; i < keys.length; ++i) {
            final long key = keys[i];
            final TLongHashSet events = this.m_eventsByDate.get(key);
            for (final long eventUid : events) {
                res.append(this.m_eventById.get(eventUid));
                res.append("\n");
            }
        }
        res.append("}");
        return res.toString();
    }
    
    public void changeEventStartDate(final long eventId, final GameDateConst date) {
        final WakfuCalendarEvent event = this.m_eventById.get(eventId);
        this.removeEventAtDate(event);
        event.setStartDate(date);
        this.addEventAtDate(event);
    }
    
    static {
        CharacterEventsCalendar.m_logger = Logger.getLogger((Class)CharacterEventsCalendar.class);
        CharacterEventsCalendar.m_instance = new CharacterEventsCalendar();
    }
}
