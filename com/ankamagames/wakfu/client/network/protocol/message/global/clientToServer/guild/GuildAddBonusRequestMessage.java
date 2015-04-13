package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GuildAddBonusRequestMessage extends OutputOnlyProxyMessage
{
    private int m_bonusId;
    
    public GuildAddBonusRequestMessage(final int bonusId) {
        super();
        this.m_bonusId = bonusId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_bonusId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20091;
    }
}
