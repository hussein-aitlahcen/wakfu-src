package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.nio.*;

public class PartySearchPlayerInviteTransferMessage extends InputOnlyProxyMessage
{
    private PartyRequester m_requester;
    private long m_occupationId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_requester = new PartyRequester(bb);
        this.m_occupationId = bb.getLong();
        return true;
    }
    
    public PartyRequester getResult() {
        return this.m_requester;
    }
    
    public long getOccupationId() {
        return this.m_occupationId;
    }
    
    @Override
    public int getId() {
        return 20417;
    }
}
