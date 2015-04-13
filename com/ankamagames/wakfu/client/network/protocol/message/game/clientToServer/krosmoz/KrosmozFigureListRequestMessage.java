package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.krosmoz;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class KrosmozFigureListRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)3, new byte[0]);
    }
    
    @Override
    public int getId() {
        return 14010;
    }
}
