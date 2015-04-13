package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class PartySearchPlayerSearchMessage extends OutputOnlyProxyMessage
{
    private final SearchParameters m_searchParameters;
    
    public PartySearchPlayerSearchMessage(final SearchParameters searchParameters) {
        super();
        this.m_searchParameters = searchParameters;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.put(this.m_searchParameters.serialize());
        return this.addClientHeader((byte)6, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 20414;
    }
}
