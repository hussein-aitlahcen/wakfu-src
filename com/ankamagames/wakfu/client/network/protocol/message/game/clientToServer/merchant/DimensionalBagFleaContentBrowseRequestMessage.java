package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class DimensionalBagFleaContentBrowseRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_merchantInventoryUid;
    
    public void setMerchantInventoryUid(final long merchantInventoryUid) {
        this.m_merchantInventoryUid = merchantInventoryUid;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_merchantInventoryUid);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10115;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagFleaContentBrowseRequestMessage.class);
    }
}
