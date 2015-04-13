package com.ankamagames.wakfu.common.game.companion;

import gnu.trove.*;

public final class UnlockedCompanionGroupLimitManager
{
    public static final UnlockedCompanionGroupLimitManager INSTANCE;
    private final TLongHashSet m_unlockedCompanionGroupLimit;
    
    private UnlockedCompanionGroupLimitManager() {
        super();
        this.m_unlockedCompanionGroupLimit = new TLongHashSet();
    }
    
    public void changeLockStateFor(final long clientId) {
        if (this.m_unlockedCompanionGroupLimit.contains(clientId)) {
            this.m_unlockedCompanionGroupLimit.remove(clientId);
        }
        else {
            this.m_unlockedCompanionGroupLimit.add(clientId);
        }
    }
    
    public boolean hasUnlockedCompanionGroupLimit(final long clientId) {
        return this.m_unlockedCompanionGroupLimit.contains(clientId);
    }
    
    static {
        INSTANCE = new UnlockedCompanionGroupLimitManager();
    }
}
