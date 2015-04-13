package com.ankamagames.wakfu.client.core.game.almanach;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.almanach.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;

public class AlmanachView extends ImmutableFieldProvider
{
    public static final String CURRENT_DATE = "currentDate";
    public static final String CURRENT_DATE_TEXT = "currentDateText";
    public static final String CURRENT_MONTH_DAYS = "currentMonthDays";
    public static final String CURRENT_ENTRY = "currentEntry";
    public static final String CURRENT_EPHEMERIS = "currentEphemeris";
    public static final String CURRENT_MONTH = "currentMonth";
    public static final String CURRENT_ZODIAK = "currentZodiak";
    public static final String CAN_GO_BEFORE = "canGoBefore";
    public static final String CAN_GO_AFTER = "canGoAfter";
    public static final String CALENDAR = "calendar";
    public static final AlmanachView INSTANCE;
    private final GameDate m_displayedDate;
    private final GregorianCalendar m_calendar;
    private final ArrayList<AlmanachDateView> m_displayedMonth;
    
    private AlmanachView() {
        super();
        this.m_displayedDate = new GameDate(GameDate.NULL_DATE);
        this.m_calendar = new GregorianCalendar();
        this.m_displayedMonth = new ArrayList<AlmanachDateView>();
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_displayedDate.isNull()) {
            return null;
        }
        if (fieldName.equals("currentMonthDays")) {
            return this.m_displayedMonth;
        }
        if (fieldName.equals("currentEntry")) {
            return this.getCurrentEntry();
        }
        if (fieldName.equals("currentDate")) {
            return this.m_displayedDate;
        }
        if (fieldName.equals("currentDateText")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(this.m_displayedDate.getDay()).append(" ");
            sb.append(WakfuTranslator.getInstance().getString("calendar.month." + this.m_displayedDate.getMonth()));
            return sb.finishAndToString();
        }
        if (fieldName.equals("canGoAfter")) {
            return this.canGoAfter();
        }
        if (fieldName.equals("canGoBefore")) {
            return this.canGoBefore();
        }
        if (fieldName.equals("currentMonth")) {
            return AlmanachViewManager.INSTANCE.getAlmanachMonthEntry(this.m_displayedDate, (byte)this.m_displayedDate.getMonth());
        }
        if (fieldName.equals("currentZodiak")) {
            return AlmanachViewManager.INSTANCE.getAlmanachZodiacEntry(this.m_displayedDate);
        }
        if (fieldName.equals("currentEphemeris")) {
            return AlmanachViewManager.INSTANCE.getAlmanachEventEntry(this.m_displayedDate);
        }
        if (fieldName.equals("calendar")) {
            return this.m_calendar;
        }
        return null;
    }
    
    private boolean canGoAfter() {
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return now.getYear() > this.m_displayedDate.getYear() || now.getMonth() > this.m_displayedDate.getMonth();
    }
    
    private boolean canGoBefore() {
        if (ActivationConstants.ALMANAX_UNLOCK_DATE.getMonth() == this.m_displayedDate.getMonth() && ActivationConstants.ALMANAX_UNLOCK_DATE.getYear() == this.m_displayedDate.getYear()) {
            return false;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        if (now.getYear() == this.m_displayedDate.getYear()) {
            return now.getMonth() - this.m_displayedDate.getMonth() - 1 <= 12;
        }
        return this.m_displayedDate.getMonth() - now.getMonth() - 2 >= 0;
    }
    
    private AlmanachEntryView getCurrentEntry() {
        final AlmanachEntry entry = AlmanachEntryManager.INSTANCE.getEntryFor(this.m_displayedDate);
        return AlmanachViewManager.INSTANCE.getAlmanachEntry(entry.getId());
    }
    
    public void setDisplayedDate(final GameDateConst displayedDate) {
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        if (displayedDate.after(now)) {
            return;
        }
        final GameDateConst previousDate = new GameDate(this.m_displayedDate);
        this.m_displayedDate.set(0, 0, 0, displayedDate.getDay(), displayedDate.getMonth(), displayedDate.getYear());
        this.m_calendar.set(this.m_displayedDate.getYear(), this.m_displayedDate.getMonth() - 1, this.m_displayedDate.getDay());
        if (previousDate.getMonth() != this.m_displayedDate.getMonth() || previousDate.getYear() != this.m_displayedDate.getYear()) {
            this.recomputeMonth();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentEntry", "currentDate", "currentDateText", "currentEphemeris", "currentZodiak", "calendar");
        this.updateMonthViews();
        final AlmanachEntry entry = AlmanachEntryManager.INSTANCE.getEntryFor(this.m_displayedDate);
        final AchievementView achievement = entry.isNull() ? null : AchievementsViewManager.INSTANCE.getAchievement(WakfuGameEntity.getInstance().getLocalPlayer().getId(), entry.getAchievementId());
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", achievement, "almanachDialog");
    }
    
    public GameDate getDisplayedDate() {
        return this.m_displayedDate;
    }
    
    public void previousMonth() {
        int month;
        int year;
        if (this.m_displayedDate.getMonth() == 1) {
            month = 12;
            year = this.m_displayedDate.getYear() - 1;
        }
        else {
            month = this.m_displayedDate.getMonth() - 1;
            year = this.m_displayedDate.getYear();
        }
        this.setDisplayedDate(new GameDate(0, 0, 0, 1, month, year));
    }
    
    public void nextMonth() {
        int month;
        int year;
        if (this.m_displayedDate.getMonth() == 12) {
            month = 1;
            year = this.m_displayedDate.getYear() + 1;
        }
        else {
            month = this.m_displayedDate.getMonth() + 1;
            year = this.m_displayedDate.getYear();
        }
        this.setDisplayedDate(new GameDate(0, 0, 0, 1, month, year));
    }
    
    private void updateMonthViews() {
        for (int i = 0, size = this.m_displayedMonth.size(); i < size; ++i) {
            this.m_displayedMonth.get(i).updateMonthView();
        }
    }
    
    private void recomputeMonth() {
        this.m_displayedMonth.clear();
        final GameDate date = new GameDate(this.m_displayedDate);
        for (int i = 1, lastDayOfMonth = this.m_calendar.getActualMaximum(5); i <= lastDayOfMonth; ++i) {
            date.setDay(i);
            this.m_displayedMonth.add(AlmanachViewManager.INSTANCE.getAlmanachDate(date));
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentMonthDays", "currentMonth", "canGoBefore", "canGoAfter");
    }
    
    public void clear() {
        this.m_displayedDate.set(0L);
        this.m_displayedMonth.clear();
    }
    
    static {
        INSTANCE = new AlmanachView();
    }
}
