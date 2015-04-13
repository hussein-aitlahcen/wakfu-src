package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class TriggerServerEvent extends OutputOnlyProxyMessage
{
    private int m_eventId;
    
    @Override
    public byte[] encode() {
        final byte[] temp = new byte[4];
        final ByteBuffer buff = ByteBuffer.wrap(temp);
        buff.putInt(this.m_eventId);
        return this.addClientHeader((byte)3, temp);
    }
    
    @Override
    public int getId() {
        return 11101;
    }
    
    public void setEventId(final int eventId) {
        this.m_eventId = eventId;
    }
}
