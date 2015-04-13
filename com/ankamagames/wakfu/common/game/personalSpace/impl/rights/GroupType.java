package com.ankamagames.wakfu.common.game.personalSpace.impl.rights;

import com.ankamagames.framework.kernel.core.maths.*;

public enum GroupType
{
    ALL(0), 
    GUILD(1);
    
    public final byte idx;
    
    private GroupType(final int id) {
        this.idx = MathHelper.ensureByte(id);
    }
    
    public static GroupType valueOf(final byte id) {
        final GroupType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final GroupType value = values[i];
            if (value.idx == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("Impossible de r\u00e9cup\u00e9rer le GroupType d'Id " + id);
    }
}
