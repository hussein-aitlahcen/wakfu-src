package com.ankamagames.wakfu.common.game.pvp;

import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public enum NationPvpState
{
    PVP_OFF(0, false, false, false), 
    PVP_STARTING(1, false, true, true), 
    PVP_ON_LOCKED(2, true, true, true), 
    PVP_ON(3, true, true, true);
    
    private final byte m_id;
    private final boolean m_canAttack;
    private final boolean m_canBeAttackedLegally;
    private final boolean m_isActive;
    
    private NationPvpState(final int id, final boolean canAttack, final boolean canBeAttackedLegally, final boolean isActive) {
        this.m_canAttack = canAttack;
        this.m_canBeAttackedLegally = canBeAttackedLegally;
        this.m_isActive = isActive;
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Nullable
    public static NationPvpState getFromId(final byte id) {
        for (final NationPvpState state : values()) {
            if (state.m_id == id) {
                return state;
            }
        }
        return null;
    }
    
    public boolean isActive() {
        return this.m_isActive;
    }
    
    public boolean canAttack() {
        return this.m_canAttack;
    }
    
    public boolean canBeAttackedLegally() {
        return this.m_canBeAttackedLegally;
    }
}
