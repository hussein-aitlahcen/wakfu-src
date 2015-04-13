package com.ankamagames.wakfu.common.game.fight.protagonists;

import java.util.*;

public enum FighterPlayState
{
    NONE((byte)0), 
    IN_PLAY((byte)1), 
    OFF_PLAY((byte)2), 
    OUT_OF_PLAY((byte)3);
    
    private static final EnumMap<FighterPlayState, FighterPlayState> RequiredStatesForTransition;
    private final byte m_id;
    
    private FighterPlayState(final byte id) {
        this.m_id = id;
    }
    
    private static EnumMap<FighterPlayState, FighterPlayState> requiredStatesForTransition() {
        final EnumMap<FighterPlayState, FighterPlayState> map = new EnumMap<FighterPlayState, FighterPlayState>(FighterPlayState.class);
        map.put(FighterPlayState.IN_PLAY, FighterPlayState.OFF_PLAY);
        map.put(FighterPlayState.OFF_PLAY, FighterPlayState.IN_PLAY);
        map.put(FighterPlayState.OUT_OF_PLAY, FighterPlayState.OFF_PLAY);
        return map;
    }
    
    public FighterPlayState required() {
        return FighterPlayState.RequiredStatesForTransition.get(this);
    }
    
    public static FighterPlayState fromId(final byte id) {
        final FighterPlayState[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final FighterPlayState value = values[i];
            if (value.m_id == id) {
                return value;
            }
        }
        return FighterPlayState.NONE;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    static {
        RequiredStatesForTransition = requiredStatesForTransition();
    }
}
