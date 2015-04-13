package com.ankamagames.wakfu.common.game.time.calendar;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.time.calendar.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;

public class WakfuGameCalendar extends GameCalendar implements TimeProvider
{
    private static final WakfuGameCalendar m_instance;
    protected static final Logger m_logger;
    private boolean m_isSunStatusComputed;
    private ArrayList<DayChangeListener> m_dayChangeListeners;
    private ArrayList<SunStatutChangeListener> m_sunStatutChangeListeners;
    private boolean m_isSunShining;
    private float m_currentPhasePercentage;
    private float m_dayPercentage;
    
    public static WakfuGameCalendar getInstance() {
        return WakfuGameCalendar.m_instance;
    }
    
    @Override
    protected void loadEvents() {
        this.addEvent(new NewDayNightCycleEvent(this.getDate(), new EventPeriod(WakfuCalendarConstants.WAKFU_DAY_CYCLE_DURATION)));
        final EventPeriod period = new EventPeriod(WakfuCalendarConstants.WAKFU_DAY_CYCLE_UPDATE_FREQUENCY);
        WakfuGameCalendar.m_logger.info((Object)("WakfuGameCalendar : adding periodic DayNightCycleUpdateEvent event, period=" + period));
        this.addEvent(new DayNightCycleUpdateEvent(this.getDate(), period));
        final int thisYear = getInstance().getDate().getYear();
        for (final Season s : Season.values()) {
            final GameDateConst d = s.getStartingDate();
            final GameDate date = new GameDate(d.getSeconds(), d.getMinutes(), d.getHours(), d.getDay(), d.getMonth(), thisYear - 1);
            this.addEvent(new SeasonEvent(date, s, EventPeriod.YEARLY));
        }
        final GameDate date2 = new GameDate(this.getDate());
        date2.trimToHour();
        this.addEvent(new NewHourEvent(date2));
        date2.trimToDay();
        this.addEvent(new NewDayEvent(date2));
        super.loadEvents();
    }
    
    @Override
    public void synchronize(final long synchronizedCurrentTime) {
        super.synchronize(synchronizedCurrentTime);
        this.updateSunStatus();
    }
    
    @Override
    public void run() {
        super.run();
        if (this.m_synchronized) {
            this.updateSunStatus();
        }
    }
    
    public void addDayChangeListener(final DayChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (this.m_dayChangeListeners == null) {
            this.m_dayChangeListeners = new ArrayList<DayChangeListener>(2);
        }
        this.m_dayChangeListeners.add(listener);
    }
    
    public void removeDayChangeListener(final DayChangeListener listener) {
        if (listener == null || this.m_dayChangeListeners == null) {
            return;
        }
        this.m_dayChangeListeners.remove(listener);
    }
    
    public void addSunStatutChangeListener(final SunStatutChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (this.m_sunStatutChangeListeners == null) {
            this.m_sunStatutChangeListeners = new ArrayList<SunStatutChangeListener>(1);
        }
        this.m_sunStatutChangeListeners.add(listener);
    }
    
    public void removeSunStatutChangeListener(final SunStatutChangeListener listener) {
        if (listener == null || this.m_sunStatutChangeListeners == null) {
            return;
        }
        this.m_sunStatutChangeListeners.remove(listener);
    }
    
    private void updateSunStatus() {
        final boolean beforeIsShinning = this.m_isSunShining;
        final long numberOfSecondsSince1970 = this.getSynchronizationTime() / 1000L;
        final int cycleDuration = 86400;
        final long currentDaySeconds = numberOfSecondsSince1970 % 86400L;
        this.m_dayPercentage = currentDaySeconds / 86400.0f * 100.0f;
        final int nightEndPercentage = 17;
        final int nightStartPercentage = 83;
        this.m_isSunShining = (this.m_dayPercentage < 83.0f && this.m_dayPercentage >= 17.0f);
        this.m_currentPhasePercentage = computeCurrentPhasePercentage(this.m_dayPercentage, 83, 17);
        if (beforeIsShinning != this.m_isSunShining && this.m_dayChangeListeners != null) {
            for (int i = 0, size = this.m_dayChangeListeners.size(); i < size; ++i) {
                this.m_dayChangeListeners.get(i).onDayChange(this.m_isSunShining);
            }
        }
        if (this.m_sunStatutChangeListeners != null) {
            for (int i = 0, size = this.m_sunStatutChangeListeners.size(); i < size; ++i) {
                this.m_sunStatutChangeListeners.get(i).onSunStatutChange(this.m_currentPhasePercentage, this.m_isSunShining);
            }
        }
        this.m_isSunStatusComputed = true;
    }
    
    private static float computeCurrentPhasePercentage(final float currentDayPercentage, final int startPercentage, final int endPercentage) {
        final int step1 = Math.min(startPercentage, endPercentage);
        final int step2 = Math.max(startPercentage, endPercentage);
        final int duration1 = step2 - step1;
        final int duration2 = 100 - duration1;
        final int duration3 = (currentDayPercentage > step1 && currentDayPercentage <= step2) ? duration1 : duration2;
        float calcul;
        if (currentDayPercentage < step2) {
            if (currentDayPercentage < step1) {
                calcul = 1.0f + (currentDayPercentage - step1) / duration3;
            }
            else {
                calcul = (currentDayPercentage - step1) / duration3;
            }
        }
        else {
            calcul = (currentDayPercentage - step2) / duration3;
        }
        return calcul * 100.0f;
    }
    
    @Override
    public boolean isSunShining() {
        return this.m_isSunShining;
    }
    
    public float getCurrentPhasePercentage() {
        return this.m_currentPhasePercentage;
    }
    
    @Override
    public float getDayPercentage() {
        return this.m_dayPercentage;
    }
    
    public boolean isSunStatusComputed() {
        return this.m_isSunStatusComputed;
    }
    
    private WakfuGameCalendar() {
        super(1970, 1);
        this.m_isSunStatusComputed = false;
    }
    
    @Override
    public long current() {
        return this.getInternalTimeInMs();
    }
    
    static {
        (m_instance = new WakfuGameCalendar()).setFirstDayOfWeek(2);
        m_logger = Logger.getLogger((Class)WakfuGameCalendar.class);
    }
    
    public interface SunStatutChangeListener
    {
        void onSunStatutChange(float p0, boolean p1);
    }
    
    public interface DayChangeListener
    {
        void onDayChange(boolean p0);
    }
}
