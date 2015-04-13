package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;

public abstract class GameCalendar extends GregorianCalendar implements GameDateProvider, Runnable
{
    private static final Logger m_logger;
    private static final boolean DEBUG = false;
    protected final CalendarEventManager m_eventManager;
    private final List<GameCalendarEventListener> m_eventListeners;
    private final List<GameCalendarEventListener> m_eventListenersToRemove;
    private final List<CalendarEventExectutionListener> m_eventExecutionListeners;
    private final int m_referenceGameYear;
    private final int m_coefSpeedTime;
    private GameDate m_date;
    private Season m_season;
    private long m_synchronizedCurrentTimeMillis;
    private long m_synchronizationTime;
    protected boolean m_synchronized;
    
    protected GameCalendar(final int referenceGameYear, final int coefSpeedTime) {
        super(GameDate.DEFAULT_TZ);
        this.m_eventManager = new CalendarEventManager();
        this.m_eventListeners = new ArrayList<GameCalendarEventListener>();
        this.m_eventListenersToRemove = new ArrayList<GameCalendarEventListener>();
        this.m_eventExecutionListeners = new ArrayList<CalendarEventExectutionListener>();
        this.m_referenceGameYear = referenceGameYear;
        this.m_coefSpeedTime = coefSpeedTime;
    }
    
    public abstract float getDayPercentage();
    
    @Override
    public void run() {
        if (this.m_synchronized) {
            this.setSynchronizedTimeInMillis(this.getInternalTimeInMs());
            this.runEvents();
            this.m_eventListeners.removeAll(this.m_eventListenersToRemove);
            this.m_eventListenersToRemove.clear();
            for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
                this.m_eventListeners.get(i).onCalendarEvent(GameCalendarEventListener.CalendarEventType.CALENDAR_UPDATED, this);
            }
        }
    }
    
    public void addEventListener(final GameCalendarEventListener listener) {
        if (!this.m_eventListenersToRemove.remove(listener) || !this.m_eventListeners.contains(listener)) {
            this.m_eventListeners.add(listener);
        }
    }
    
    public void addEventExecutionListener(final CalendarEventExectutionListener listener) {
        if (!this.m_eventExecutionListeners.contains(listener)) {
            this.m_eventExecutionListeners.add(listener);
        }
    }
    
    public SortedList<CalendarEvent> getEvents() {
        return this.m_eventManager.getEvents();
    }
    
    public CalendarEvent getFirstEvent() {
        return this.m_eventManager.getFirstEvent();
    }
    
    public long getInternalTimeInMs() {
        return (this.getElapsedTimeSinceSynchronization() + this.m_synchronizedCurrentTimeMillis) * this.m_coefSpeedTime;
    }
    
    long getElapsedTimeSinceSynchronization() {
        return (System.nanoTime() - this.m_synchronizationTime) / 1000000L;
    }
    
    public byte getNextSeason() {
        for (int i = 0, size = this.m_eventManager.getEvents().size(); i < size; ++i) {
            final CalendarEvent calendarEvent = this.m_eventManager.getEvents().get(i);
            if (calendarEvent instanceof SeasonEvent) {
                return ((SeasonEvent)calendarEvent).getSeason().getIndex();
            }
        }
        return -1;
    }
    
    public long getSynchronizationTime() {
        return this.getElapsedTimeSinceSynchronization() + this.m_synchronizedCurrentTimeMillis;
    }
    
    public void removeEvent(final CalendarEvent event) {
        this.m_eventManager.removeEvent(event);
        for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
            this.m_eventListeners.get(i).onCalendarEvent(GameCalendarEventListener.CalendarEventType.EVENT_REMOVED, this);
        }
    }
    
    public void removeEventListener(final GameCalendarEventListener listener) {
        if (!this.m_eventListenersToRemove.contains(listener)) {
            this.m_eventListenersToRemove.add(listener);
        }
    }
    
    public void synchronize(final long synchronizedCurrentTime) {
        this.m_synchronizationTime = System.nanoTime();
        this.setSynchronizedTimeInMillis(this.m_synchronizedCurrentTimeMillis = synchronizedCurrentTime);
        this.m_synchronized = true;
        for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
            this.m_eventListeners.get(i).onCalendarEvent(GameCalendarEventListener.CalendarEventType.CALENDAR_SYNCHRONIZED, this);
        }
    }
    
    private void setSynchronizedTimeInMillis(final long synchronizedCurrentTime) {
        this.setTimeInMillis(synchronizedCurrentTime);
        final int year = this.m_referenceGameYear + this.get(1) - 1970;
        this.m_date = new GameDate(this.get(13), this.get(12), this.get(11), this.get(5), this.get(2) + 1, year);
    }
    
    public void start(final long updateFrequency) {
        this.loadEvents();
        ProcessScheduler.getInstance().remove(this);
        ProcessScheduler.getInstance().schedule(this, updateFrequency);
    }
    
    protected void loadEvents() {
        this.runEvents();
    }
    
    public void addEvent(final CalendarEvent event) {
        this.m_eventManager.addEvent(event);
        for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
            this.m_eventListeners.get(i).onCalendarEvent(GameCalendarEventListener.CalendarEventType.EVENT_ADDED, this);
        }
    }
    
    protected void runEvents() {
        if (!this.m_synchronized) {
            return;
        }
        for (CalendarEvent event = this.m_eventManager.getFirstEvent(); event != null && event.getDate().compareTo((GameDateConst)this.m_date) < 0; event = this.m_eventManager.getFirstEvent()) {
            this.tryToRunEvent(event);
            this.notifyToExecutionListeners(event);
            this.removeEventAndReaddIfCyclic(event);
            this.notiyToEventListeners();
        }
    }
    
    private void tryToRunEvent(final CalendarEvent event) {
        try {
            event.runEvent(this);
        }
        catch (Exception e) {
            GameCalendar.m_logger.error((Object)"Exception levee lors de l'execution d'un evenement", (Throwable)e);
        }
    }
    
    private void notifyToExecutionListeners(final CalendarEvent event) {
        for (int i = 0, size = this.m_eventExecutionListeners.size(); i < size; ++i) {
            try {
                this.m_eventExecutionListeners.get(i).onCalendarEventExecution(event);
            }
            catch (Exception e) {
                GameCalendar.m_logger.error((Object)"Exception levee lors de la notification d'un evenement aux observers", (Throwable)e);
            }
        }
    }
    
    private void removeEventAndReaddIfCyclic(final CalendarEvent event) {
        this.m_eventManager.removeEvent(event);
        if (event instanceof CyclicCalendarEvent) {
            final CyclicCalendarEvent e = (CyclicCalendarEvent)event;
            if (e.getPeriodicity() != null && (e.getEndDate().isNull() || e.getEndDate().compareTo((GameDateConst)this.m_date) > 0)) {
                this.m_eventManager.addEvent(event.addToDate(e.getPeriodicity()));
            }
        }
    }
    
    private void notiyToEventListeners() {
        for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
            try {
                this.m_eventListeners.get(i).onCalendarEvent(GameCalendarEventListener.CalendarEventType.EVENT_RUNNED, this);
            }
            catch (Exception e) {
                GameCalendar.m_logger.error((Object)"Exception levee lors de la notification d'un evenement aux observers", (Throwable)e);
            }
        }
    }
    
    @Override
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    public GameDate getNewDate() {
        return new GameDate(this.m_date);
    }
    
    public boolean isNow(final GameDateConst someDate) {
        return this.m_date.equals(someDate);
    }
    
    public boolean isInFuture(final GameDateConst someDate) {
        return this.m_date.before(someDate);
    }
    
    public boolean isInFutureOrNow(final GameDateConst someDate) {
        return this.m_date.beforeOrEquals(someDate);
    }
    
    public boolean isInPast(final GameDateConst someDate) {
        return this.m_date.after(someDate);
    }
    
    public boolean isInPastOrNow(final GameDateConst someDate) {
        return this.m_date.afterOrEquals(someDate);
    }
    
    public Season getSeason() {
        return this.m_season;
    }
    
    public void setSeason(final Season season) {
        this.m_season = season;
    }
    
    public boolean isSynchronized() {
        return this.m_synchronized;
    }
    
    public boolean isSunShining() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GameCalendar.class);
    }
}
