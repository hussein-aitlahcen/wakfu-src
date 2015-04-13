package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class ActorResurrectPlayerRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private final long m_targetCharacterId;
    
    public ActorResurrectPlayerRequestMessage(final long targetCharacterId) {
        super();
        this.m_targetCharacterId = targetCharacterId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_targetCharacterId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4195;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorResurrectPlayerRequestMessage.class);
    }
}
