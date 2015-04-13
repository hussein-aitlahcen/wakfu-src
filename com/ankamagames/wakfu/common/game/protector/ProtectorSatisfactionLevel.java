package com.ankamagames.wakfu.common.game.protector;

import gnu.trove.*;

public enum ProtectorSatisfactionLevel
{
    UNDEFINED((byte)(-1)), 
    UNSATISFIED((byte)1), 
    HALF_SATISFIED((byte)2), 
    SATISFIED((byte)3);
    
    private static final TByteObjectHashMap<ProtectorSatisfactionLevel> m_satisfactionsById;
    private byte m_id;
    
    private ProtectorSatisfactionLevel(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static ProtectorSatisfactionLevel fromTargets(final int satisfiedTargets, final int targetsCount) {
        if (targetsCount == 0) {
            return ProtectorSatisfactionLevel.UNDEFINED;
        }
        if (satisfiedTargets == 0) {
            return ProtectorSatisfactionLevel.UNSATISFIED;
        }
        if (satisfiedTargets == targetsCount) {
            return ProtectorSatisfactionLevel.SATISFIED;
        }
        return ProtectorSatisfactionLevel.HALF_SATISFIED;
    }
    
    public static ProtectorSatisfactionLevel fromId(final byte id) {
        return ProtectorSatisfactionLevel.m_satisfactionsById.get(id);
    }
    
    static {
        m_satisfactionsById = new TByteObjectHashMap<ProtectorSatisfactionLevel>();
        for (final ProtectorSatisfactionLevel satisfaction : values()) {
            ProtectorSatisfactionLevel.m_satisfactionsById.put(satisfaction.getId(), satisfaction);
        }
    }
}
