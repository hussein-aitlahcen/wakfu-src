package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MarketGetMoneyBackRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(0);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15273;
    }
}
