package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class DimensionalBagAllFleasBrowseRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_bagIeId;
    private boolean m_startBrowsing;
    
    public void setBagIeId(final long bagIeId) {
        this.m_bagIeId = bagIeId;
    }
    
    public void setStartBrowsing(final boolean startBrowsing) {
        this.m_startBrowsing = startBrowsing;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putLong(this.m_bagIeId);
        buffer.put((byte)(this.m_startBrowsing ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10113;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagAllFleasBrowseRequestMessage.class);
    }
}
