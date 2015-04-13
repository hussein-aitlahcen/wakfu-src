package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class FleaContentMessage extends InputOnlyProxyMessage
{
    private long m_bagOwnerId;
    private float m_baseBuyDuty;
    private float m_nationBuyDuty;
    private final RawMerchantItemInventory m_serializedFlea;
    
    public FleaContentMessage() {
        super();
        this.m_serializedFlea = new RawMerchantItemInventory();
    }
    
    public long getBagOwnerId() {
        return this.m_bagOwnerId;
    }
    
    public RawMerchantItemInventory getSerializedFlea() {
        return this.m_serializedFlea;
    }
    
    public float getBaseBuyDuty() {
        return this.m_baseBuyDuty;
    }
    
    public float getNationBuyDuty() {
        return this.m_nationBuyDuty;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_bagOwnerId = buffer.getLong();
        this.m_baseBuyDuty = buffer.getFloat();
        this.m_nationBuyDuty = buffer.getFloat();
        this.m_serializedFlea.unserialize(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 10106;
    }
}
