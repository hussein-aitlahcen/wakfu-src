package com.ankamagames.wakfu.client.core.game.characterInfo.zones;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.zone.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class LocalPlayerZoneBuffs
{
    protected static final Logger m_logger;
    private final ArrayList<ZoneBuffInstance> m_zoneBuffs;
    private final ArrayList<LocalPlayerZoneBuffsObserver> m_observers;
    private long m_clockId;
    
    public LocalPlayerZoneBuffs() {
        super();
        this.m_zoneBuffs = new ArrayList<ZoneBuffInstance>();
        this.m_observers = new ArrayList<LocalPlayerZoneBuffsObserver>(1);
    }
    
    public final void addObserver(final LocalPlayerZoneBuffsObserver observer) {
        this.m_observers.add(observer);
    }
    
    public final void removeObserver(final LocalPlayerZoneBuffsObserver observer) {
        this.m_observers.remove(observer);
    }
    
    public final void addBuff(final ZoneBuffInstance buffInstance) {
        this.m_zoneBuffs.add(buffInstance);
        if (!buffInstance.getBuff().isInfinite()) {
            this.enableClock();
        }
    }
    
    public final void removeBuff(final ZoneBuffInstance buffInstance) {
        this.m_zoneBuffs.remove(buffInstance);
        if (!this.hasTimedBuffs()) {
            this.disableClock();
        }
    }
    
    public final void clearBuffs() {
        this.m_zoneBuffs.clear();
        this.disableClock();
    }
    
    public final Collection<ZoneBuffInstance> getActiveBuffs() {
        return this.m_zoneBuffs;
    }
    
    private boolean hasTimedBuffs() {
        for (int i = 0, size = this.m_zoneBuffs.size(); i < size; ++i) {
            final ZoneBuffInstance buffInstance = this.m_zoneBuffs.get(i);
            if (!buffInstance.getBuff().isInfinite()) {
                return true;
            }
        }
        return false;
    }
    
    private void enableClock() {
        if (this.m_clockId == 0L) {
            this.m_clockId = MessageScheduler.getInstance().addClock(new MessageHandler() {
                @Override
                public boolean onMessage(final Message message) {
                    LocalPlayerZoneBuffs.this.notifyObservers();
                    return false;
                }
                
                @Override
                public long getId() {
                    return 1L;
                }
                
                @Override
                public void setId(final long id) {
                }
            }, 1000L, 1);
            this.notifyObservers();
        }
    }
    
    private void disableClock() {
        if (this.m_clockId != 0L) {
            MessageScheduler.getInstance().removeClock(this.m_clockId);
            this.m_clockId = 0L;
            this.notifyObservers();
        }
    }
    
    private void notifyObservers() {
        for (int i = 0, size = this.m_observers.size(); i < size; ++i) {
            final LocalPlayerZoneBuffsObserver observer = this.m_observers.get(i);
            observer.onTick();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LocalPlayerZoneBuffs.class);
    }
}
