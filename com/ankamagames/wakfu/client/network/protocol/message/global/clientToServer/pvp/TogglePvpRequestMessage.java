package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class TogglePvpRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(0);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20411;
    }
}
