package com.ankamagames.wakfu.common.game.fight.protagonists;

import org.apache.log4j.*;

public class FighterState
{
    static final Logger m_logger;
    private FighterPlayState m_playState;
    private byte m_teamId;
    private final byte m_originalTeamId;
    private final boolean m_localToFight;
    private final long m_originalControllerId;
    private boolean m_isTeamLeader;
    private Long m_currentControllerId;
    
    FighterState(final byte teamId, final boolean isLeader, final boolean localToFight, final long controllerId) {
        super();
        this.m_playState = FighterPlayState.IN_PLAY;
        this.m_localToFight = localToFight;
        this.m_originalTeamId = teamId;
        this.m_teamId = teamId;
        this.m_isTeamLeader = isLeader;
        this.m_originalControllerId = controllerId;
        this.m_currentControllerId = controllerId;
    }
    
    public boolean isInPlay() {
        return this.m_playState == FighterPlayState.IN_PLAY;
    }
    
    public boolean isOffPlay() {
        return this.m_playState == FighterPlayState.OFF_PLAY;
    }
    
    public boolean isOutOfPlay() {
        return this.m_playState == FighterPlayState.OUT_OF_PLAY;
    }
    
    public FighterPlayState getPlayState() {
        return this.m_playState;
    }
    
    public byte getTeamId() {
        return this.m_teamId;
    }
    
    public void setTeamId(final byte teamId) {
        this.m_teamId = teamId;
    }
    
    public byte getOriginalTeamId() {
        return this.m_originalTeamId;
    }
    
    boolean setPlayState(final FighterPlayState playState) {
        if (this.m_playState != playState.required()) {
            FighterState.m_logger.error((Object)String.format("[FIGHT_STATE] \u00c9tat incorrect pour une transition vers l'\u00e9tat %s, actuel: %s, requis: %s", playState, this.m_playState, playState.required()));
            return false;
        }
        this.m_playState = playState;
        return true;
    }
    
    void forcePlayState(final FighterPlayState playState) {
        this.m_playState = playState;
    }
    
    public boolean isLocalToFight() {
        return this.m_localToFight;
    }
    
    public boolean isTeamLeader() {
        return this.m_isTeamLeader;
    }
    
    public void setTeamLeader(final boolean teamLeader) {
        this.m_isTeamLeader = teamLeader;
    }
    
    public long getOriginalControllerId() {
        return this.m_originalControllerId;
    }
    
    public Long getCurrentControllerId() {
        return this.m_currentControllerId;
    }
    
    public void setCurrentControllerId(final Long currentControllerId, final byte teamId) {
        this.m_currentControllerId = currentControllerId;
        this.m_teamId = teamId;
    }
    
    public boolean isCurrentlyControlledBy(final long id) {
        return this.m_currentControllerId != null && this.m_currentControllerId == id;
    }
    
    public boolean isOriginallyControlledBy(final long id) {
        return this.m_originalControllerId == id;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FighterState.class);
    }
}
