package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RandomItemResultMessage extends InputOnlyProxyMessage
{
    private int m_referenceId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_referenceId = buffer.getInt();
        return true;
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    @Override
    public int getId() {
        return 4188;
    }
}
