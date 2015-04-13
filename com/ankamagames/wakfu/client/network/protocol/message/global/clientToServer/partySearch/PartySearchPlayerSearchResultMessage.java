package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import java.nio.*;
import java.util.*;

public class PartySearchPlayerSearchResultMessage extends InputOnlyProxyMessage
{
    private List<PartyRequester> m_result;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final int size = bb.getInt();
        this.m_result = new ArrayList<PartyRequester>(size);
        for (int i = 0; i < size; ++i) {
            this.m_result.add(new PartyRequester(bb));
        }
        return true;
    }
    
    public List<PartyRequester> getResult() {
        return this.m_result;
    }
    
    @Override
    public int getId() {
        return 20415;
    }
}
