package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.seedSpreader;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class OpenSeedSpreaderResultMessage extends InputOnlyProxyMessage
{
    private int m_referenceId;
    private short m_quantity;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_referenceId = buffer.getInt();
        this.m_quantity = buffer.getShort();
        return true;
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public int getId() {
        return 15942;
    }
}
