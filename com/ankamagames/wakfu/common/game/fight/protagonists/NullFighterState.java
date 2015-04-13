package com.ankamagames.wakfu.common.game.fight.protagonists;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.utils.*;

class NullFighterState extends FighterState
{
    private final Long m_fighterId;
    
    NullFighterState(final BasicCharacterInfo fighter) {
        super((byte)0, false, false, 0L);
        this.m_fighterId = ((fighter == null) ? null : fighter.getId());
    }
    
    @Override
    public boolean isInPlay() {
        return false;
    }
    
    @Override
    public boolean isOffPlay() {
        return false;
    }
    
    @Override
    public byte getTeamId() {
        return -1;
    }
    
    @Override
    public boolean isLocalToFight() {
        return false;
    }
    
    @Override
    public boolean isTeamLeader() {
        return false;
    }
    
    @Override
    public void setTeamLeader(final boolean teamLeader) {
        NullFighterState.m_logger.error((Object)String.format("[FIGHT] setTeamLeader sur un fighter absent %d - %s", this.m_fighterId, ExceptionFormatter.currentStackTrace(10)));
    }
    
    @Override
    public long getOriginalControllerId() {
        return 0L;
    }
    
    @Override
    public Long getCurrentControllerId() {
        return null;
    }
    
    @Override
    public void setCurrentControllerId(final Long currentControllerId, final byte teamId) {
        NullFighterState.m_logger.error((Object)String.format("[FIGHT] setCurrentControllerId sur un fighter absent %d - %s", this.m_fighterId, ExceptionFormatter.currentStackTrace(10)));
    }
    
    @Override
    boolean setPlayState(final FighterPlayState playState) {
        NullFighterState.m_logger.error((Object)String.format("[FIGHT] setPlayState sur un fighter absent %d - %s", this.m_fighterId, ExceptionFormatter.currentStackTrace(10)));
        return false;
    }
    
    @Override
    public boolean isCurrentlyControlledBy(final long id) {
        return false;
    }
    
    @Override
    public boolean isOriginallyControlledBy(final long id) {
        return false;
    }
}
