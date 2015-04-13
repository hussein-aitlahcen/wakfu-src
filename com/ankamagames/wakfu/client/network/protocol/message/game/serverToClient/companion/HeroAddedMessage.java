package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public final class HeroAddedMessage extends InputOnlyProxyMessage
{
    private byte[] m_serializedData;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_serializedData = rawDatas;
        return true;
    }
    
    public byte[] getSerializedData() {
        return this.m_serializedData;
    }
    
    @Override
    public int getId() {
        return 5565;
    }
}
