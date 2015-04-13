package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;

public class NationActionRequestMessage extends OutputOnlyProxyMessage
{
    private NationActionRequest m_request;
    
    public NationActionRequestMessage() {
        super();
        this.m_request = null;
    }
    
    public void setRequest(final NationActionRequest request) {
        this.m_request = request;
    }
    
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)3, NationActionRequestFactory.serializeRequest(this.m_request));
    }
    
    @Override
    public int getId() {
        return 15115;
    }
}
