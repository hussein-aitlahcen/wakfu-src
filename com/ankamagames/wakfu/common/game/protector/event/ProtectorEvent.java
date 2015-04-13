package com.ankamagames.wakfu.common.game.protector.event;

import com.ankamagames.wakfu.common.game.protector.*;

public abstract class ProtectorEvent<P extends ProtectorBase>
{
    protected P m_protector;
    
    public void setProtector(final P protector) {
        this.m_protector = protector;
    }
    
    public P getProtector() {
        return this.m_protector;
    }
}
