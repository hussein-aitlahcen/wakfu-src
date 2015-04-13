package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class LockFightRequestMessage extends OutputOnlyProxyMessage
{
    private boolean m_lock;
    
    public void setLock(final boolean lock) {
        this.m_lock = lock;
    }
    
    @Override
    public byte[] encode() {
        final byte[] content = { 0 };
        if (this.m_lock) {
            content[0] = 1;
        }
        else {
            content[0] = 0;
        }
        return this.addClientHeader((byte)3, content);
    }
    
    @Override
    public int getId() {
        return 8157;
    }
}
