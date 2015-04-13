package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased;

public enum TimelineState
{
    INITIAL(0), 
    ON_FIGHTER_TURN(1), 
    BETWEEN_FIGHTER_TURNS(2), 
    ENDING_FIGHTER_TURN(3), 
    BEGINNING_FIGHTER_TURN(4);
    
    private final byte m_id;
    
    private TimelineState(final int id) {
        this.m_id = (byte)id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static TimelineState byId(final byte id) {
        for (final TimelineState state : values()) {
            if (id == state.m_id) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid " + TimelineState.class.getName() + " id");
    }
}
