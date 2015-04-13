package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import java.nio.*;

public class HavenWorldBidResultMessage extends InputOnlyProxyMessage
{
    private HavenWorldError m_error;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_error = HavenWorldError.valueOf(buffer.getInt());
        return true;
    }
    
    public HavenWorldError getError() {
        return this.m_error;
    }
    
    @Override
    public int getId() {
        return 20095;
    }
}
