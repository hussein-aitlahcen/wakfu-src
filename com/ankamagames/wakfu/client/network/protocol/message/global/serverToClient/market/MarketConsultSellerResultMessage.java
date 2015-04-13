package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MarketConsultSellerResultMessage extends InputOnlyProxyMessage
{
    private byte[] m_raw;
    private int m_totalCount;
    private int m_outdatedCount;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        buffer.get(this.m_raw = new byte[buffer.getInt()]);
        this.m_totalCount = buffer.getInt();
        this.m_outdatedCount = buffer.getInt();
        return true;
    }
    
    public byte[] getRaw() {
        return this.m_raw;
    }
    
    public int getTotalCount() {
        return this.m_totalCount;
    }
    
    public int getOutdatedCount() {
        return this.m_outdatedCount;
    }
    
    @Override
    public int getId() {
        return 20102;
    }
}
