package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public abstract class UsingFightMessageHandler<M extends Message, F extends FightInfo> implements FightMessageHandler<M, F>
{
    protected F m_concernedFight;
    private int m_handledMessageId;
    
    public UsingFightMessageHandler() {
        super();
        this.m_handledMessageId = 0;
    }
    
    @Override
    public void setConcernedFight(final F fight) {
        this.m_concernedFight = fight;
    }
    
    @Override
    public abstract boolean onMessage(final M p0);
    
    @Override
    public final int getHandledMessageId() {
        return this.m_handledMessageId;
    }
    
    @Override
    public final void setHandledMessageId(final int handledMessageId) {
        this.m_handledMessageId = handledMessageId;
    }
}
