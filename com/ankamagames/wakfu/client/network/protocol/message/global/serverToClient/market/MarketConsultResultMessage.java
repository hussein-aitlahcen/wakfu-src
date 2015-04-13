package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.market.*;
import java.nio.*;

public class MarketConsultResultMessage extends InputOnlyProxyMessage
{
    private MarketConstant.ConsultReturnSerialType m_serialType;
    private byte[] m_raw;
    private int m_totalCount;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_serialType = MarketConstant.ConsultReturnSerialType.values()[buffer.get()];
        buffer.get(this.m_raw = new byte[buffer.getInt()]);
        this.m_totalCount = buffer.getInt();
        return true;
    }
    
    public byte[] getRaw() {
        return this.m_raw;
    }
    
    public int getTotalCount() {
        return this.m_totalCount;
    }
    
    public MarketConstant.ConsultReturnSerialType getSerialType() {
        return this.m_serialType;
    }
    
    @Override
    public int getId() {
        return 20100;
    }
}
