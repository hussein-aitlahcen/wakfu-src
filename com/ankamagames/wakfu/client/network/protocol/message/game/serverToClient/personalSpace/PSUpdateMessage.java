package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class PSUpdateMessage extends InputOnlyProxyMessage
{
    private RawDimensionalBagForClient m_serializedPersonalSpace;
    
    public RawDimensionalBagForClient getSerializedPersonalSpace() {
        return this.m_serializedPersonalSpace;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        (this.m_serializedPersonalSpace = new RawDimensionalBagForClient()).unserialize(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 10018;
    }
}
