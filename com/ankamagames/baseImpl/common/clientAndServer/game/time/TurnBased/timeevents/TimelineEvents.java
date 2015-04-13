package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import java.util.*;
import gnu.trove.*;

public final class TimelineEvents implements TimeEventObsolescenceListener<DelayableTimeEvent>
{
    public static final int DONT_CHECK_FIGHTER_ID = 0;
    private final TShortObjectHashMap<List<DelayableTimeEvent>> m_atStartDelayableTimeEvents;
    private final TShortObjectHashMap<List<DelayableTimeEvent>> m_atEndDelayableTimeEvents;
    private final TLongHashSet m_inactiveFighters;
    
    public TimelineEvents() {
        super();
        this.m_atStartDelayableTimeEvents = new TShortObjectHashMap<List<DelayableTimeEvent>>();
        this.m_atEndDelayableTimeEvents = new TShortObjectHashMap<List<DelayableTimeEvent>>();
        this.m_inactiveFighters = new TLongHashSet();
    }
    
    public RelativeFightTime addTimeEvent(final DelayableTimeEvent event, final RelativeFightTimeInterval timeToEvent, final boolean isTargetActive, final boolean targetHasNotPlayThisTurn, final short currentTableTurn) {
        final boolean targetHasPlayThisTurn = !targetHasNotPlayThisTurn && !isTargetActive;
        short turnToAddEventTo = (short)(currentTableTurn + (targetHasPlayThisTurn ? 1 : 0) + timeToEvent.getTableTurnsFromNow());
        final short minimumTurnToAddEvent = (short)(currentTableTurn + (targetHasPlayThisTurn ? 1 : 0));
        if (timeToEvent.isAtEndOfTurn()) {
            --turnToAddEventTo;
        }
        if (isTargetActive && timeToEvent.isDurationFullTurns()) {
            ++turnToAddEventTo;
        }
        turnToAddEventTo = (short)Math.max(minimumTurnToAddEvent, turnToAddEventTo);
        if (timeToEvent.isAtEndOfTurn()) {
            this.addToEvents(event, turnToAddEventTo, this.m_atEndDelayableTimeEvents);
        }
        else {
            this.addToEvents(event, turnToAddEventTo, this.m_atStartDelayableTimeEvents);
        }
        event.setObsolescenceListener(this);
        return RelativeFightTime.forFighter(event.getFighterToAttachToById()).atTableTurn(turnToAddEventTo).atEndOfTurn(timeToEvent.isAtEndOfTurn());
    }
    
    void addToEvents(final DelayableTimeEvent event, final short turnToAddEventTo, final TShortObjectHashMap<List<DelayableTimeEvent>> events) {
        List<DelayableTimeEvent> delayableTimeEvents = events.get(turnToAddEventTo);
        if (delayableTimeEvents == null) {
            delayableTimeEvents = new ArrayList<DelayableTimeEvent>();
            events.put(turnToAddEventTo, delayableTimeEvents);
        }
        delayableTimeEvents.add(event);
    }
    
    public void processStartTurnEvents(final long fighterId, final short tableTurn, final TurnBasedTimeline timeline) {
        final List<DelayableTimeEvent> events = this.m_atStartDelayableTimeEvents.get(tableTurn);
        if (events == null) {
            return;
        }
        this.processEvents(fighterId, timeline, events);
    }
    
    public void processEndTurnEvents(final long fighterId, final short tableTurn, final TurnBasedTimeline timeline) {
        final List<DelayableTimeEvent> events = this.m_atEndDelayableTimeEvents.get(tableTurn);
        if (events == null) {
            return;
        }
        this.processEvents(fighterId, timeline, events);
    }
    
    private void processEvents(final long fighterId, final TurnBasedTimeline timeline, final List<DelayableTimeEvent> events) {
        final ArrayList<DelayableTimeEvent> eventsToProcess = new ArrayList<DelayableTimeEvent>(events);
        final Iterator<DelayableTimeEvent> it = eventsToProcess.iterator();
        while (timeline.isRunning() && it.hasNext()) {
            final DelayableTimeEvent event = it.next();
            if (fighterId != 0L && event.getFighterToAttachToById() != fighterId) {
                continue;
            }
            if (!events.contains(event)) {
                continue;
            }
            it.remove();
            timeline.fireTimeEvent(event);
            this.removeEvent(event);
        }
    }
    
    private void processEvents(final TurnBasedTimeline timeline, final List<DelayableTimeEvent> events) {
        this.processEvents(0L, timeline, events);
    }
    
    @Override
    public void onBeingObsolete(final DelayableTimeEvent event) {
        this.removeEvent(event);
    }
    
    private void removeEvent(final DelayableTimeEvent event) {
        this.m_atStartDelayableTimeEvents.forEachValue(new TObjectProcedure<List<DelayableTimeEvent>>() {
            @Override
            public boolean execute(final List<DelayableTimeEvent> object) {
                object.remove(event);
                return true;
            }
        });
        this.m_atEndDelayableTimeEvents.forEachValue(new TObjectProcedure<List<DelayableTimeEvent>>() {
            @Override
            public boolean execute(final List<DelayableTimeEvent> object) {
                object.remove(event);
                return true;
            }
        });
    }
    
    private void removeEmptyEventsList() {
        this.removeEmptyEventsList(this.m_atStartDelayableTimeEvents);
        this.removeEmptyEventsList(this.m_atEndDelayableTimeEvents);
    }
    
    public void clearTimeEvents() {
        this.m_atEndDelayableTimeEvents.clear();
        this.m_atStartDelayableTimeEvents.clear();
    }
    
    public void fighterIsShelved(final long fighterId) {
        this.m_inactiveFighters.add(fighterId);
    }
    
    public void fighterUnshelved(final long fighterId) {
        this.m_inactiveFighters.remove(fighterId);
    }
    
    public void processNewTableTurnEvents(final short tableTurn, final TurnBasedTimeline timeline) {
        this.processEventsOfInactiveFighters(tableTurn, timeline);
        this.processPreviousTurnEvents(tableTurn, timeline);
        this.removeEmptyEventsList();
    }
    
    private void processEventsOfInactiveFighters(final short tableTurn, final TurnBasedTimeline timeline) {
        this.m_inactiveFighters.forEach(new TLongProcedure() {
            @Override
            public boolean execute(final long fighterId) {
                final List<DelayableTimeEvent> startEvents = TimelineEvents.this.m_atStartDelayableTimeEvents.get(tableTurn);
                if (startEvents != null) {
                    TimelineEvents.this.processEvents(fighterId, timeline, startEvents);
                }
                final List<DelayableTimeEvent> endEvents = TimelineEvents.this.m_atEndDelayableTimeEvents.get(tableTurn);
                if (endEvents != null) {
                    TimelineEvents.this.processEvents(fighterId, timeline, endEvents);
                }
                return true;
            }
        });
    }
    
    private void processPreviousTurnEvents(final short tableTurn, final TurnBasedTimeline timeline) {
        final int previousTurn = tableTurn - 1;
        final List<DelayableTimeEvent> startEvents = this.m_atStartDelayableTimeEvents.get((short)previousTurn);
        if (startEvents != null) {
            this.processEvents(timeline, startEvents);
        }
        final List<DelayableTimeEvent> endEvents = this.m_atEndDelayableTimeEvents.get((short)previousTurn);
        if (endEvents != null) {
            this.processEvents(timeline, endEvents);
        }
    }
    
    private void removeEmptyEventsList(final TShortObjectHashMap<List<DelayableTimeEvent>> timeEvents) {
        final TShortObjectIterator<List<DelayableTimeEvent>> iterator = timeEvents.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final List<DelayableTimeEvent> next = iterator.value();
            if (next.isEmpty()) {
                iterator.remove();
            }
        }
    }
    
    public TLongHashSet getInactiveFighters() {
        return this.m_inactiveFighters;
    }
    
    TShortObjectHashMap<List<DelayableTimeEvent>> getAtStartDelayableTimeEvents() {
        return this.m_atStartDelayableTimeEvents;
    }
    
    TShortObjectHashMap<List<DelayableTimeEvent>> getAtEndDelayableTimeEvents() {
        return this.m_atEndDelayableTimeEvents;
    }
}
