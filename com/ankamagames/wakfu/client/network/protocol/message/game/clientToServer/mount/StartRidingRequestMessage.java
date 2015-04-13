package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.mount;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class StartRidingRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(0);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15993;
    }
    
    static {
        m_logger = Logger.getLogger((Class)StartRidingRequestMessage.class);
    }
}
