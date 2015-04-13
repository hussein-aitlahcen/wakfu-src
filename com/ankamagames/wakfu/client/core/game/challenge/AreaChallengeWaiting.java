package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;

public class AreaChallengeWaiting extends ImmutableFieldProvider
{
    public static final AreaChallengeWaiting INSTANCE;
    public static final String TITLE = "title";
    public static final String IS_EMPTY = "isEmpty";
    public static final String TIME_STATE_TEXT = "timeStateText";
    public static final String REMAINING_TIME = "remainingTime";
    private Date m_startDate;
    
    public AreaChallengeWaiting() {
        super();
        this.m_startDate = new Date();
    }
    
    public void setStartDate(final long timeBeforeNext, final boolean andActivate) {
        if (timeBeforeNext > 0L) {
            this.m_startDate.setTime(System.currentTimeMillis() + timeBeforeNext);
            if (andActivate) {
                this.activateClock();
            }
        }
    }
    
    public void activateClock() {
        MessageScheduler.getInstance().removeAllClocks(UIChallengeFrame.getInstance());
        MessageScheduler.getInstance().addClock(UIChallengeFrame.getInstance(), 1000L, -1);
        this.updateProperty();
    }
    
    private short getRemainingTime() {
        final long currentTime = System.currentTimeMillis();
        return (short)Math.max(0L, (this.m_startDate.getTime() - currentTime) / 1000L);
    }
    
    public void updateTime() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "timeStateText", "remainingTime");
    }
    
    public void updateProperty() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "title", "isEmpty", "timeStateText", "remainingTime");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return WakfuTranslator.getInstance().getString("challenge.state.waitingNext");
        }
        if (fieldName.equals("isEmpty")) {
            return true;
        }
        if (fieldName.equals("timeStateText")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.openText().append(WakfuTranslator.getInstance().getString("challenge.remainingTime", ChallengeDataView.formatTime(this.getRemainingTime()))).closeText();
            return sb.finishAndToString();
        }
        if (fieldName.equals("remainingTime")) {
            return ChallengeDataView.formatTime(this.getRemainingTime());
        }
        if (fieldName.equals("isChaos")) {
            return false;
        }
        return null;
    }
    
    static {
        INSTANCE = new AreaChallengeWaiting();
    }
}
