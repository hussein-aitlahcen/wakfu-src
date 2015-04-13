package com.ankamagames.wakfu.common.game.nation.crime;

import com.ankamagames.framework.kernel.core.common.progression.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class CrimePurgationCooldown implements CoolDown, Runnable
{
    private static Logger m_logger;
    protected final AbstractCitizenComportement m_comportment;
    private final long m_startTime;
    private long m_lastUpdateTime;
    private int m_totalCrimePoint;
    
    public CrimePurgationCooldown(final AbstractCitizenComportement comportment) {
        super();
        this.m_startTime = WakfuGameCalendar.getInstance().getInternalTimeInMs();
        this.m_lastUpdateTime = WakfuGameCalendar.getInstance().getInternalTimeInMs();
        this.m_comportment = comportment;
        this.m_totalCrimePoint = comportment.getCrimePurgationScore();
    }
    
    @Override
    public long getStartTime() {
        return this.m_startTime;
    }
    
    @Override
    public long getCoolDown() {
        this.run();
        final long cooldown = CrimeUtil.getJailDurationFromCrimePoints(this.m_totalCrimePoint);
        final int nationId = this.m_comportment.getCrimePurgationNationId();
        if (cooldown > 0L && nationId == -1) {
            CrimePurgationCooldown.m_logger.error((Object)"Attention un cooldown de purgation tourne sur une nation vide");
        }
        return cooldown;
    }
    
    protected long getRemainingTime(final int crimeScore) {
        return CrimeUtil.getJailDurationFromCrimePoints(crimeScore) - (WakfuGameCalendar.getInstance().getInternalTimeInMs() - this.m_lastUpdateTime);
    }
    
    public long getRemainingTime() {
        return this.getRemainingTime(this.m_comportment.getCrimePurgationScore());
    }
    
    public void onCoolDownLaunched() {
    }
    
    @Override
    public boolean onCoolDownExpired() {
        this.m_comportment.stopCrimePurgation();
        return false;
    }
    
    public void stopCooldown() {
    }
    
    @Override
    public void setStartTime(final long startTime) {
    }
    
    @Override
    public void run() {
        final int value = this.m_comportment.getCrimePurgationScore();
        final int newValue = CrimeUtil.getDecrementedCrimeScore(this.m_totalCrimePoint, WakfuGameCalendar.getInstance().getInternalTimeInMs() - this.m_startTime);
        if (value != newValue) {
            this.m_lastUpdateTime = WakfuGameCalendar.getInstance().getInternalTimeInMs();
        }
        this.m_comportment.addPurgationCrimePoint(newValue - value);
    }
    
    public void addPoints(final int toAdd, final int previousScore) {
        this.m_totalCrimePoint += toAdd;
    }
    
    public void reset() {
        this.m_totalCrimePoint = 0;
        this.m_lastUpdateTime = 0L;
    }
    
    static {
        CrimePurgationCooldown.m_logger = Logger.getLogger((Class)CrimePurgationCooldown.class);
    }
}
