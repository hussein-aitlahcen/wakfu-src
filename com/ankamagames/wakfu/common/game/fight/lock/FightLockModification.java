package com.ankamagames.wakfu.common.game.fight.lock;

import java.nio.*;

public class FightLockModification
{
    private final State m_state;
    private final long m_requestorId;
    private final byte m_teamId;
    
    public FightLockModification(final State state, final long requestorId, final byte teamId) {
        super();
        this.m_state = state;
        this.m_requestorId = requestorId;
        this.m_teamId = teamId;
    }
    
    public int serializedSize() {
        return 10;
    }
    
    public void serialize(final ByteBuffer buff) {
        buff.put(this.m_state.getId());
        buff.putLong(this.m_requestorId);
        buff.put(this.m_teamId);
    }
    
    public static FightLockModification deserialize(final ByteBuffer buff) {
        final State state = byId(buff.get());
        final long requestorId = buff.getLong();
        final byte teamId = buff.get();
        return new FightLockModification(state, requestorId, teamId);
    }
    
    public boolean isClosed() {
        return this.m_state != State.UNLOCKED;
    }
    
    public boolean isAutoLock() {
        return this.m_state == State.AUTO_LOCKED;
    }
    
    public long getRequestorId() {
        return this.m_requestorId;
    }
    
    public byte getTeamId() {
        return this.m_teamId;
    }
    
    public enum State
    {
        UNLOCKED(0), 
        LOCKED(1), 
        AUTO_LOCKED(2);
        
        private final byte m_id;
        
        private State(final int id) {
            this.m_id = (byte)id;
        }
        
        private byte getId() {
            return this.m_id;
        }
        
        private static State byId(final byte id) {
            for (final State state : values()) {
                if (state.m_id == id) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Invalid State Id: " + id);
        }
    }
}
