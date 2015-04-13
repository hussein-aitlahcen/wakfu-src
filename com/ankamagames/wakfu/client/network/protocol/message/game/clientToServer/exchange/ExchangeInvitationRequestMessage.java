package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeInvitationRequestMessage extends OutputOnlyProxyMessage
{
    private long m_otherUserId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_otherUserId);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 6001;
    }
    
    public void setOtherUserId(final long otherUserId) {
        this.m_otherUserId = otherUserId;
    }
}
