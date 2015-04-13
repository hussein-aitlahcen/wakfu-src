package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ClientAdditionalCharacterSlotsUpdateMessage extends InputOnlyProxyMessage
{
    private byte m_additionalSlots;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_additionalSlots = bb.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 2069;
    }
    
    public byte getAdditionalSlots() {
        return this.m_additionalSlots;
    }
}
