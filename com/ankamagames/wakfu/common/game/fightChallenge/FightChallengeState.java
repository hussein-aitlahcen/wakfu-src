package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public enum FightChallengeState
{
    RUNNING(0), 
    SUCCESS(1), 
    FAILURE(2);
    
    private final byte m_id;
    
    private FightChallengeState(final int id) {
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Nullable
    public static FightChallengeState getFromId(final byte id) {
        for (final FightChallengeState s : values()) {
            if (s.m_id == id) {
                return s;
            }
        }
        return null;
    }
}
