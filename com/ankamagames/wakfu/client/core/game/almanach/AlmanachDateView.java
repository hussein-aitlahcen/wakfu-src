package com.ankamagames.wakfu.client.core.game.almanach;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.almanach.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class AlmanachDateView extends ImmutableFieldProvider implements DateComponent
{
    public static final String IS_TODAY = "isToday";
    public static final String IS_ENABLED = "isEnabled";
    public static final String DATE_TITLE = "dateTitle";
    public static final String ENTRY = "entry";
    private final GameDateConst m_date;
    private final int m_entryId;
    
    public AlmanachDateView(final GameDateConst date) {
        super();
        this.m_date = new GameDate(date);
        final AlmanachEntry entry = AlmanachEntryManager.INSTANCE.getEntryFor(date);
        this.m_entryId = entry.getId();
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    @Override
    public int getDayInMonth() {
        return this.m_date.getDay();
    }
    
    @Override
    public Object getContent() {
        return this;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("dateTitle")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(this.m_date.getDay()).append(' ');
            sb.append(WakfuTranslator.getInstance().getString("calendar.month.", this.m_date.getMonth()));
            return sb.finishAndToString();
        }
        if (fieldName.equals("isToday")) {
            final GameDate displayedDate = AlmanachView.INSTANCE.getDisplayedDate();
            return displayedDate.getDay() == this.m_date.getDay() && displayedDate.getMonth() == this.m_date.getMonth();
        }
        if (fieldName.equals("entry")) {
            return AlmanachViewManager.INSTANCE.getAlmanachEntry(this.m_entryId);
        }
        if (fieldName.equals("isEnabled")) {
            return !this.isDisabled();
        }
        return null;
    }
    
    public boolean isDisabled() {
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return this.m_date.after(now) || this.m_date.before(ActivationConstants.ALMANAX_UNLOCK_DATE);
    }
    
    void updateMonthView() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isToday");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AlmanachDateView");
        sb.append("{m_date=").append(this.m_date);
        sb.append(", m_entryId=").append(this.m_entryId);
        sb.append('}');
        return sb.toString();
    }
}
