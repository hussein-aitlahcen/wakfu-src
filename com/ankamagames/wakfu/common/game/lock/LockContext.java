package com.ankamagames.wakfu.common.game.lock;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.configuration.*;

public class LockContext
{
    private static final Logger m_logger;
    private final TIntObjectHashMap<LockData> m_locks;
    private final List<LockContextHandler> m_listeners;
    private final GameDate m_TIME;
    private static TIntHashSet m_bypassedDailyLocksIds;
    
    public LockContext() {
        super();
        this.m_locks = new TIntObjectHashMap<LockData>();
        this.m_listeners = new ArrayList<LockContextHandler>();
        this.m_TIME = new GameDate(0, 0, 0, 0, 0, 0);
    }
    
    public static void reloadBypassedDailyLocks() {
        LockContext.m_bypassedDailyLocksIds = SystemConfiguration.INSTANCE.getIntHashSet(SystemConfigurationType.DUNGEON_DAILY_LOCK_BYPASS);
    }
    
    public static boolean isDailyLockBypassed(final int lockId) {
        return LockContext.m_bypassedDailyLocksIds != null && LockContext.m_bypassedDailyLocksIds.contains(lockId);
    }
    
    public void addLock(final LockData data) {
        this.m_locks.put(data.getId(), data);
    }
    
    public void removeLock(final int lockId) {
        this.m_locks.remove(lockId);
    }
    
    public boolean containsLock(final int lockId) {
        return this.m_locks.containsKey(lockId);
    }
    
    public void addListener(final LockContextHandler l) {
        if (!this.m_listeners.contains(l)) {
            this.m_listeners.add(l);
        }
    }
    
    public void removeListener(final LockContextHandler l) {
        this.m_listeners.remove(l);
    }
    
    public LockInstance getLock(final int lockId) {
        return this.m_locks.get(lockId);
    }
    
    public void setCurrentLockValue(final int lockId, final int currentLockValue) {
        final LockData lockData = this.m_locks.get(lockId);
        if (lockData == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de modifier un Lock inconnu id=" + lockId));
            return;
        }
        lockData.setCurrentLockValue(currentLockValue);
    }
    
    public void setCurrentLockValueDate(final int lockId, final GameDateConst currentLockValueDate) {
        final LockData lockData = this.m_locks.get(lockId);
        if (lockData == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de modifier un Lock inconnu id=" + lockId));
            return;
        }
        lockData.setCurrentLockValueDate(currentLockValueDate);
    }
    
    public void setLockDate(final int lockId, final GameDateConst lockDate) {
        final LockData lockData = this.m_locks.get(lockId);
        if (lockData == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de modifier un Lock inconnu id=" + lockId));
            return;
        }
        lockData.setLockDate(lockDate);
    }
    
    public void setLockDate(final int lockId, final GameDateConst lockDate, final GameDateConst unlockDate) {
        final LockData lockData = this.m_locks.get(lockId);
        if (lockData == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de modifier un Lock inconnu id=" + lockId));
            return;
        }
        lockData.setLockDate(lockDate);
        lockData.setUnlockDate(unlockDate);
    }
    
    public boolean isLocked(final int lockId) {
        final LockInstance lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de v\u00e9rifier qu'un Lock inconnu est actif id=" + lockId));
            return false;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return isLockedGlobally(lock, now) || this.isLockedDaily(lockId) || this.isLockedPersonnaly(lock, now);
    }
    
    public boolean isLockedPersonnaly(final int lockId) {
        final LockInstance lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de v\u00e9rifier qu'un Lock inconnu est actif id=" + lockId));
            return false;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return this.isLockedPersonnaly(lock, now);
    }
    
    public boolean isLockedDaily(final int lockId) {
        return this.isLockedDaily() && !isDailyLockBypassed(lockId);
    }
    
    private boolean isLockedDaily() {
        final LockData lock = this.m_locks.get(-1);
        if (lock == null) {
            return false;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return this.isLockedPersonnaly(lock, now);
    }
    
    private boolean isLockedPersonnaly(final LockInstance lock, final GameDateConst now) {
        if (!lock.getPeriodDuration().isPositive()) {
            return false;
        }
        if (lock.getLockDate().isNull()) {
            return false;
        }
        final GameDateConst closestStartTime = now.closestDatePeriod(lock.getPeriodStartTime(), lock.getPeriodDuration());
        this.m_TIME.set(lock.getLockDate());
        return closestStartTime.beforeOrEquals(this.m_TIME);
    }
    
    public boolean isLockedGlobally(final int lockId) {
        final LockInstance lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de v\u00e9rifier qu'un Lock inconnu est actif id=" + lockId));
            return false;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return isLockedGlobally(lock, now);
    }
    
    private static boolean isLockedGlobally(final LockDefinition lock, final GameDateConst now) {
        return !lock.getUnlockDate().isNull() && now.before(lock.getUnlockDate());
    }
    
    public GameDateConst getNextStartTime(final int lockId) {
        final LockInstance lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de v\u00e9rifier qu'un Lock inconnu est actif id=" + lockId));
            return GameDate.getNullDate();
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return now.closestDatePeriod(lock.getPeriodStartTime(), lock.getPeriodDuration(), true);
    }
    
    public int getActualCurrentLockValue(final int lockId) {
        final LockInstance lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de v\u00e9rifier qu'un Lock inconnu est actif id=" + lockId));
            return 0;
        }
        if (lock.getLockValue() == 0) {
            return 0;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getNewDate();
        if (lock.getPeriodDuration().isPositive() && !lock.getCurrentLockValueLastChange().isNull()) {
            final GameDateConst closestStartTime = now.closestDatePeriod(lock.getPeriodStartTime(), lock.getPeriodDuration());
            this.m_TIME.set(lock.getCurrentLockValueLastChange());
            if (!closestStartTime.beforeOrEquals(this.m_TIME)) {
                return 0;
            }
        }
        return lock.getCurrentLockValue();
    }
    
    public void lock(final int lockId) {
        final LockData lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de verrouiller un Lock inconnu id=" + lockId));
            return;
        }
        final GameDateConst date = WakfuGameCalendar.getInstance().getNewDate();
        lock.setLockDate(date);
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onLock(lockId, date);
        }
    }
    
    public void incrementLock(final int lockId) {
        final LockData lock = this.m_locks.get(lockId);
        if (lock == null) {
            LockContext.m_logger.warn((Object)("[LOCK] On essaye de verrouiller un Lock inconnu id=" + lockId));
            return;
        }
        this.incrementLock(lock);
    }
    
    private void incrementLock(final LockData lock) {
        final int lockId = lock.getId();
        final GameDateConst now = WakfuGameCalendar.getInstance().getNewDate();
        if (lock.getPeriodDuration().isPositive() && !lock.getCurrentLockValueLastChange().isNull()) {
            final GameDateConst closestStartTime = now.closestDatePeriod(lock.getPeriodStartTime(), lock.getPeriodDuration());
            this.m_TIME.set(lock.getCurrentLockValueLastChange());
            if (!closestStartTime.beforeOrEquals(this.m_TIME)) {
                lock.resetCurrentLockValue();
            }
        }
        lock.incrementCurrentLockValue();
        lock.setCurrentLockValueDate(now);
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onLockIncrement(lockId, lock.getCurrentLockValue(), lock.getCurrentLockValueLastChange());
        }
        if (lock.getCurrentLockValue() >= lock.getLockValue()) {
            this.lock(lockId);
        }
    }
    
    public TIntObjectIterator<LockData> iterator() {
        return this.m_locks.iterator();
    }
    
    public void cleanUp() {
        this.m_locks.clear();
        this.m_listeners.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)LockContext.class);
        reloadBypassedDailyLocks();
        SystemConfiguration.INSTANCE.addListener(new SystemConfigurationListener() {
            @Override
            public void onLoad() {
                LockContext.reloadBypassedDailyLocks();
            }
        });
    }
}
