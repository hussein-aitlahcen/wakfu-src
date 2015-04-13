package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public final class CompanionListMessage extends InputOnlyProxyMessage
{
    private byte[] m_serializedCompanions;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_serializedCompanions = rawDatas;
        return true;
    }
    
    public byte[] getSerializedCompanions() {
        return this.m_serializedCompanions;
    }
    
    @Override
    public int getId() {
        return 2077;
    }
}
