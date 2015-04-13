package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class FighterReadyRequestMessage extends OutputOnlyProxyMessage
{
    private boolean m_ready;
    
    public void setReady(final boolean ready) {
        this.m_ready = ready;
    }
    
    @Override
    public byte[] encode() {
        final byte[] content = { 0 };
        if (this.m_ready) {
            content[0] = 1;
        }
        else {
            content[0] = 0;
        }
        return this.addClientHeader((byte)3, content);
    }
    
    @Override
    public int getId() {
        return 8149;
    }
}
