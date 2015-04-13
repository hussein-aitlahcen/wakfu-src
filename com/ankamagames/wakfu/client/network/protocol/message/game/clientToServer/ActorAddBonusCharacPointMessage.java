package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class ActorAddBonusCharacPointMessage extends OutputOnlyProxyMessage
{
    private static final Logger m_logger;
    private byte m_characId;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 1;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.put(this.m_characId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8405;
    }
    
    public void setCharacId(final byte characId) {
        this.m_characId = characId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorAddBonusCharacPointMessage.class);
    }
}
