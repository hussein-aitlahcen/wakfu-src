package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class DimensionalBagFleaContentMessage extends InputOnlyProxyMessage
{
    private final RawMerchantItemInventory m_serializedFlea;
    
    public DimensionalBagFleaContentMessage() {
        super();
        this.m_serializedFlea = new RawMerchantItemInventory();
    }
    
    public RawMerchantItemInventory getSerializedFlea() {
        return this.m_serializedFlea;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_serializedFlea.unserialize(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 10116;
    }
}
