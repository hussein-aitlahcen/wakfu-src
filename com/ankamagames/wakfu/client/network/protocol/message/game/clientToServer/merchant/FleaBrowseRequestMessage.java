package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class FleaBrowseRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_fleaOwnerId;
    private long m_merchantInventoryUidFilter;
    private boolean m_startBrowsing;
    
    public void setFleaOwnerId(final long fleaOwnerId) {
        this.m_fleaOwnerId = fleaOwnerId;
    }
    
    public void setMerchantInventoryUidFilter(final long merchantInventoryUidFilter) {
        this.m_merchantInventoryUidFilter = merchantInventoryUidFilter;
    }
    
    public void setStartBrowsing(final boolean startBrowsing) {
        this.m_startBrowsing = startBrowsing;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(17);
        buffer.putLong(this.m_fleaOwnerId);
        buffer.putLong(this.m_merchantInventoryUidFilter);
        buffer.put((byte)(this.m_startBrowsing ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5247;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FleaBrowseRequestMessage.class);
    }
}
