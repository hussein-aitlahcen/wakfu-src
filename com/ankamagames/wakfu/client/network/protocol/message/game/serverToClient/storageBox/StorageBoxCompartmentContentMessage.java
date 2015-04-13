package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.storageBox;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class StorageBoxCompartmentContentMessage extends InputOnlyProxyMessage
{
    private RawStorageBoxCompartment m_rawCompartment;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        (this.m_rawCompartment = new RawStorageBoxCompartment()).unserialize(bb);
        return true;
    }
    
    @Override
    public int getId() {
        return 15972;
    }
    
    public RawStorageBoxCompartment getRawCompartment() {
        return this.m_rawCompartment;
    }
}
