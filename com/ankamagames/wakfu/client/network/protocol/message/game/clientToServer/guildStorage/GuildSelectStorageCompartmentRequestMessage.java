package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.guildStorage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class GuildSelectStorageCompartmentRequestMessage extends OutputOnlyProxyMessage
{
    private static final Logger m_logger;
    private int m_compartmentId;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 4;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putInt(this.m_compartmentId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15621;
    }
    
    public void setCompartmentId(final int compartmentId) {
        this.m_compartmentId = compartmentId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildSelectStorageCompartmentRequestMessage.class);
    }
}
