package com.ankamagames.wakfu.common.game.lock;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;

public class LockManager
{
    private static final Logger m_logger;
    public static final LockManager INSTANCE;
    private TIntObjectHashMap<LockDefinition> m_locks;
    
    public LockManager() {
        super();
        this.m_locks = new TIntObjectHashMap<LockDefinition>();
    }
    
    public void addLock(final LockDefinition lockDefinition) {
        this.m_locks.put(lockDefinition.getId(), lockDefinition);
    }
    
    public void initializeContext(final LockContext context, final boolean withDailyLock) {
        final TIntObjectIterator<LockDefinition> it = this.m_locks.iterator();
        while (it.hasNext()) {
            it.advance();
            context.addLock(new LockData(it.value()));
        }
        if (withDailyLock) {
            context.addLock(new LockData(LockConstants.DAILY_LOCK_DEFINITION));
        }
    }
    
    public void lock(final LockContextProvider provider, final int lockId) {
        provider.getLockContext().lock(lockId);
    }
    
    public void incrementLock(final LockContextProvider provider, final int lockId) {
        provider.getLockContext().incrementLock(lockId);
    }
    
    public boolean isLocked(final int lockId) {
        final LockDefinition lockDefinition = this.m_locks.get(lockId);
        if (lockDefinition == null) {
            LockManager.m_logger.warn((Object)("[LOCK] On essaye de r\u00e9cup\u00e9rer l'\u00e9tat d'un verrou inexistant id=" + lockId), (Throwable)new Exception());
            return false;
        }
        if (lockDefinition.getUnlockDate().isNull()) {
            return false;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return now.before(lockDefinition.getUnlockDate());
    }
    
    public boolean isAvailableOnlyForCitizens(final int lockId) {
        final LockDefinition lockDefinition = this.m_locks.get(lockId);
        if (lockDefinition == null) {
            LockManager.m_logger.warn((Object)("[LOCK] On essaye de r\u00e9cup\u00e9rer l'\u00e9tat d'un verrou inexistant id=" + lockId), (Throwable)new Exception());
            return false;
        }
        return lockDefinition.isAvailableToCitizensOnly();
    }
    
    public int getLockByLockedItemId(final int lockedItemId) {
        final TIntObjectIterator<LockDefinition> it = this.m_locks.iterator();
        while (it.hasNext()) {
            it.advance();
            final LockDefinition lock = it.value();
            if (lock.getLockedItem() == lockedItemId) {
                return lock.getId();
            }
        }
        return -1;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LockManager.class);
        INSTANCE = new LockManager();
    }
}
