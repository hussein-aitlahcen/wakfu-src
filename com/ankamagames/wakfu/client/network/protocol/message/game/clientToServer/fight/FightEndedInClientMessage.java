package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class FightEndedInClientMessage extends OutputOnlyProxyMessage
{
    private final long m_fightId;
    
    public FightEndedInClientMessage(final long fightId) {
        super();
        this.m_fightId = fightId;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_fightId);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 8041;
    }
}
