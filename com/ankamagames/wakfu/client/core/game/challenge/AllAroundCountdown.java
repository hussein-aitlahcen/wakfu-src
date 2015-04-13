package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.sound.*;

public class AllAroundCountdown extends Countdown
{
    private static final AllAroundCountdown m_instance;
    private boolean m_stopped;
    
    private AllAroundCountdown() {
        super(10, 5, "tutorialMessageDialog");
        this.m_stopped = true;
    }
    
    public static AllAroundCountdown getInstance() {
        return AllAroundCountdown.m_instance;
    }
    
    @Override
    public void start(final int duration) {
        this.start(duration, WakfuGameCalendar.getInstance().getDate());
    }
    
    public void start(final int duration, final GameDateConst startDate) {
        final GameInterval passedTime = startDate.timeTo(WakfuGameCalendar.getInstance().getDate());
        final GameInterval durationInterval = GameInterval.fromSeconds(duration);
        durationInterval.substract(passedTime);
        if (!durationInterval.isPositive()) {
            return;
        }
        super.start((int)durationInterval.toSeconds());
        this.m_stopped = false;
    }
    
    @Override
    public void stop() {
        this.m_stopped = true;
        super.stop();
        this.clean();
    }
    
    @Override
    protected void setDuration(final int duration) {
        if (this.getDuration() == duration) {
            return;
        }
        super.setDuration(duration);
        if (!this.m_stopped && duration >= 1 && duration <= 5) {
            WakfuSoundManager.getInstance().playGUISound(600128L);
        }
        if (duration == 0) {
            if (!this.m_stopped) {
                WakfuSoundManager.getInstance().playGUISound(600129L);
            }
            this.clean();
        }
    }
    
    private void clean() {
    }
    
    static {
        m_instance = new AllAroundCountdown();
    }
}
