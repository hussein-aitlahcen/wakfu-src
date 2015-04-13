package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.nio.*;

public class PartyRequesterUpdateMessage extends InputOnlyProxyMessage
{
    private PartyRequester m_partyRequester;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_partyRequester = new PartyRequester(bb);
        return true;
    }
    
    public PartyRequester getPartyRequester() {
        return this.m_partyRequester;
    }
    
    @Override
    public int getId() {
        return 20420;
    }
}
