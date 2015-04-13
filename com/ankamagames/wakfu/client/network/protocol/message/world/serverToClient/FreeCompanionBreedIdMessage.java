package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class FreeCompanionBreedIdMessage extends InputOnlyProxyMessage
{
    private short m_freeCompanionBreedId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_freeCompanionBreedId = bb.getShort();
        return false;
    }
    
    public short getFreeCompanionBreedId() {
        return this.m_freeCompanionBreedId;
    }
    
    @Override
    public int getId() {
        return 2078;
    }
    
    @Override
    public String toString() {
        return "FreeCompanionBreedIdMessage{m_freeCompanionBreedId=" + this.m_freeCompanionBreedId + '}';
    }
}
