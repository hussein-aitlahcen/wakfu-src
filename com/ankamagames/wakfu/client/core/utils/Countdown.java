package com.ankamagames.wakfu.client.core.utils;

import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class Countdown implements MessageHandler
{
    private int m_duration;
    private int m_startDuration;
    private long m_clockId;
    private final int m_state0Threshold;
    private final int m_state1Threshold;
    private final String m_dialogId;
    
    public Countdown(final int state0Threshold, final int state1Threshold, final String dialogId) {
        super();
        this.m_state0Threshold = state0Threshold;
        this.m_state1Threshold = state1Threshold;
        this.m_dialogId = dialogId;
    }
    
    public String getDialogId() {
        return this.m_dialogId;
    }
    
    public void start(final int duration) {
        this.stop();
        this.setDuration(this.m_startDuration = duration);
        this.m_clockId = MessageScheduler.getInstance().addClock(this, 1000L, 0, duration);
    }
    
    public void stop() {
        this.setDuration(this.m_startDuration = 0);
        MessageScheduler.getInstance().removeClock(this.m_clockId);
    }
    
    protected byte getStateFromDuration(final int duration) {
        if (duration > this.m_state0Threshold) {
            return 0;
        }
        if (duration > this.m_state1Threshold) {
            return 1;
        }
        return 2;
    }
    
    protected void setDuration(final int duration) {
        this.m_duration = Math.max(duration, 0);
        if (this.m_dialogId != null) {
            PropertiesProvider.getInstance().setLocalPropertyValue("countdown", this.m_duration, this.m_dialogId);
            PropertiesProvider.getInstance().setLocalPropertyValue("countdownPercentage", this.m_duration / this.m_startDuration, this.m_dialogId);
            PropertiesProvider.getInstance().setLocalPropertyValue("countdownState", this.getStateFromDuration(duration), this.m_dialogId);
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("countdown", this.m_duration);
            PropertiesProvider.getInstance().setPropertyValue("countdownPercentage", this.m_duration / this.m_startDuration);
            PropertiesProvider.getInstance().setPropertyValue("countdownState", this.getStateFromDuration(duration));
        }
    }
    
    protected final int getDuration() {
        return this.m_duration;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        this.setDuration(this.m_duration - 1);
        if (this.m_duration == 0) {
            this.stop();
        }
        return false;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
}
