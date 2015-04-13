package com.ankamagames.wakfu.common.game.antiAddiction;

import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class AntiAddictionDataHandler
{
    private boolean m_enabled;
    private boolean m_connected;
    private final GameDate m_lastConnectionDate;
    private final GameInterval m_currentUsedQuota;
    
    public AntiAddictionDataHandler() {
        super();
        this.m_lastConnectionDate = new GameDate(GameDate.NULL_DATE);
        this.m_currentUsedQuota = new GameInterval(GameIntervalConst.EMPTY_INTERVAL);
    }
    
    public void normalize() {
        if (!this.m_enabled) {
            return;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        final GameInterval duration = this.m_lastConnectionDate.timeTo(now);
        if (this.m_connected) {
            this.m_currentUsedQuota.add(duration);
        }
        if (!this.m_currentUsedQuota.isPositive()) {
            this.m_currentUsedQuota.set(GameIntervalConst.EMPTY_INTERVAL);
        }
        this.m_lastConnectionDate.set(now);
    }
    
    public void setConnected(final boolean connected) {
        this.m_connected = connected;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public void setCurrentUsedQuota(final GameIntervalConst currentUsedQuota) {
        this.m_currentUsedQuota.set(currentUsedQuota);
    }
    
    public void setLastConnectionDate(final GameDateConst lastConnectionDate) {
        this.m_lastConnectionDate.set(lastConnectionDate);
    }
    
    public GameIntervalConst getCurrentUsedQuota() {
        return this.m_currentUsedQuota;
    }
    
    public GameDateConst getLastConnectionDate() {
        return this.m_lastConnectionDate;
    }
    
    public void reset() {
        this.m_enabled = false;
        this.m_connected = false;
        this.m_lastConnectionDate.set(GameDate.NULL_DATE);
        this.m_currentUsedQuota.set(GameIntervalConst.EMPTY_INTERVAL);
    }
    
    @Override
    public String toString() {
        return "AntiAddictionDataHandler{m_currentUsedQuota=" + this.m_currentUsedQuota + ", m_enabled=" + this.m_enabled + ", m_lastConnectionDate=" + this.m_lastConnectionDate + '}';
    }
}
