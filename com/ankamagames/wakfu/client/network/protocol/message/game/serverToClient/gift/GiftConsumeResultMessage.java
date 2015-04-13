package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gift;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GiftConsumeResultMessage extends InputOnlyProxyMessage
{
    private boolean m_consumeResult;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_consumeResult = (buffer.get() == 1);
        return true;
    }
    
    public boolean isConsumeResult() {
        return this.m_consumeResult;
    }
    
    @Override
    public int getId() {
        return 13004;
    }
}
