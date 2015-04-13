package com.ankamagames.wakfu.common.game.group.partySearch;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class PartyOccupation
{
    private final long m_id;
    private final int m_referenceId;
    private final PartyOccupationType m_type;
    
    protected PartyOccupation(final int id, final int referenceId, final PartyOccupationType type) {
        super();
        this.m_id = MathHelper.getLongFromTwoInt(type.getId(), id);
        this.m_referenceId = referenceId;
        this.m_type = type;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    public PartyOccupationType getOccupationType() {
        return this.m_type;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final PartyOccupation that = (PartyOccupation)obj;
        return this.m_id == that.m_id;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.m_id ^ this.m_id >>> 32);
    }
}
