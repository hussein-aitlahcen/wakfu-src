package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class EffectAreaParameters extends BasicEffectAreaParameters
{
    private final short m_level;
    private final Direction8 m_direction;
    
    public EffectAreaParameters(final long id, final int x, final int y, final short z, final EffectContext context, final EffectUser owner, final short level, final Direction8 direction) {
        super(id, x, y, z, context, owner);
        this.m_level = level;
        this.m_direction = direction;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
}
