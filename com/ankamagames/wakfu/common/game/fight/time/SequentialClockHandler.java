package com.ankamagames.wakfu.common.game.fight.time;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import org.jetbrains.annotations.*;

public class SequentialClockHandler implements MessageHandler
{
    private final TimeProvider m_timeProvider;
    private ClockUser m_handler;
    private final MessageScheduler m_scheduler;
    private Long m_clockId;
    private long m_clockTime;
    
    public SequentialClockHandler(final TimeProvider timeProvider, final MessageScheduler messageScheduler) {
        super();
        this.m_timeProvider = timeProvider;
        this.m_scheduler = messageScheduler;
    }
    
    public void startClock(final int duration, final int clockType) {
        this.removeClock();
        this.m_clockId = this.m_scheduler.addClock(this, duration, clockType, 1);
        this.m_clockTime = this.m_timeProvider.current() + duration;
    }
    
    private void removeClock() {
        if (this.m_clockId != null) {
            this.m_scheduler.removeClock(this.m_clockId);
        }
        this.m_clockId = null;
    }
    
    public boolean hasClock() {
        return this.m_clockId != null;
    }
    
    public void cancelClock() {
        this.removeClock();
    }
    
    public int getRemainingSeconds() {
        return Math.max(0, (int)(this.getRemainingMillis() / 1000L));
    }
    
    public long getRemainingMillis() {
        return this.m_clockTime - this.m_timeProvider.current();
    }
    
    public void setHandler(final ClockUser handler) {
        this.m_handler = handler;
    }
    
    @Override
    public boolean onMessage(@Nullable final Message message) {
        if (!(message instanceof ClockMessage)) {
            return true;
        }
        final ClockUser actualHandler = this.m_handler.getClockUser();
        if (actualHandler == null) {
            this.m_scheduler.removeAllClocks(this);
            return true;
        }
        return this.handleClockMessage((ClockMessage)message, actualHandler);
    }
    
    private boolean handleClockMessage(@NotNull final ClockMessage message, final ClockUser actualHandler) {
        if (this.m_clockId != null && this.m_clockId == message.getClockId()) {
            this.m_clockId = null;
            actualHandler.onClockEnd(message.getSubId());
        }
        return false;
    }
    
    @Override
    public final long getId() {
        return 1L;
    }
    
    @Override
    public final void setId(final long id) {
    }
}
