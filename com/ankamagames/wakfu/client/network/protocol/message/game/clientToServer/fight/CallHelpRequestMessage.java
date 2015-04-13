package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class CallHelpRequestMessage extends OutputOnlyProxyMessage
{
    private boolean m_callHelp;
    
    public void setCallHelp(final boolean callHelp) {
        this.m_callHelp = callHelp;
    }
    
    @Override
    public byte[] encode() {
        final byte[] content = { 0 };
        if (this.m_callHelp) {
            content[0] = 1;
        }
        else {
            content[0] = 0;
        }
        return this.addClientHeader((byte)3, content);
    }
    
    @Override
    public int getId() {
        return 8153;
    }
}
