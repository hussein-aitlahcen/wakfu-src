package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gems;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GemMergedResultMessage extends InputOnlyProxyMessage
{
    private boolean m_success;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_success = (bb.get() != 0);
        return true;
    }
    
    public boolean isSuccess() {
        return this.m_success;
    }
    
    @Override
    public int getId() {
        return 13106;
    }
    
    @Override
    public String toString() {
        return "GemMergedResultMessage{m_success=" + this.m_success + '}';
    }
}
