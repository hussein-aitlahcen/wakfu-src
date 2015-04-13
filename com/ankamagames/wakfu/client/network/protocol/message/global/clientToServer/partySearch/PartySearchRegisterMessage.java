package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class PartySearchRegisterMessage extends OutputOnlyProxyMessage
{
    private final PartyRequester m_request;
    
    public PartySearchRegisterMessage(final PartyRequester request) {
        super();
        this.m_request = request;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.put(this.m_request.serialize(true));
        return this.addClientHeader((byte)6, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 20413;
    }
    
    @Override
    public String toString() {
        return "PartySearchRegisterMessage{m_request=" + this.m_request + '}';
    }
}
