package com.ankamagames.wakfu.client.core.game.synchronizing;

import com.ankamagames.framework.kernel.core.common.message.synchronizing.*;

public enum SynchroBarriers implements BarrierHandle
{
    NATION_LOADING_BARRIER(1), 
    END_FIGHT_LEVEL_UP_BARRIER(2), 
    LOCAL_PLAYER_LOADED(3);
    
    private final int m_id;
    
    private SynchroBarriers(final int id) {
        this.m_id = id;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
}
