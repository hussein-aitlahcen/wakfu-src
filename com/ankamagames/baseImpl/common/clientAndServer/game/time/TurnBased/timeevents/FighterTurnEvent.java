package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import org.apache.commons.pool.*;

public abstract class FighterTurnEvent implements TimeEvent
{
    protected long m_fighterId;
    protected ObjectPool m_pool;
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    protected void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
    
    public void onCheckOut() {
        this.m_fighterId = 0L;
    }
    
    public void onCheckIn() {
    }
}
