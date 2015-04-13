package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.guildStorage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class GuildMoveItemBetweenCompartmentsRequestMessage extends OutputOnlyProxyMessage
{
    private static final Logger m_logger;
    private int m_srcCompartment;
    private int m_destCompartment;
    private byte m_srcPosition;
    private byte m_destPosition;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(this.m_srcCompartment);
        buffer.put(this.m_srcPosition);
        buffer.putInt(this.m_destCompartment);
        buffer.put(this.m_destPosition);
        return this.addClientHeader((byte)6, buffer.array());
    }
    
    @Override
    public int getId() {
        return 20201;
    }
    
    public void setSrcCompartment(final int srcCompartment) {
        this.m_srcCompartment = srcCompartment;
    }
    
    public void setDestCompartment(final int destCompartment) {
        this.m_destCompartment = destCompartment;
    }
    
    public void setDestPosition(final byte destPosition) {
        this.m_destPosition = destPosition;
    }
    
    public void setSrcPosition(final byte srcPosition) {
        this.m_srcPosition = srcPosition;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildMoveItemBetweenCompartmentsRequestMessage.class);
    }
}
