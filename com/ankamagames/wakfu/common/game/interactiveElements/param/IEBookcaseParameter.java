package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public final class IEBookcaseParameter extends IEParameter
{
    private final byte m_size;
    
    public IEBookcaseParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorId, final byte size) {
        super(paramId, visualId, chaosCategory, chaosCollectorId);
        this.m_size = size;
    }
    
    public byte getSize() {
        return this.m_size;
    }
}
