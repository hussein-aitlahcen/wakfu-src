package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.constants.*;
import java.nio.*;

public class ClientVersionMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(Version.INTERNAL_VERSION.length);
        bb.put(Version.INTERNAL_VERSION);
        return this.addClientHeader((byte)0, bb.array());
    }
    
    @Override
    public int getId() {
        return 7;
    }
}
