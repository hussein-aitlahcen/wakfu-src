package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.*;

public class PartySearchUnregisterPlayerMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 20416;
    }
}
