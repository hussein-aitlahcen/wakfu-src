package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ActorPathRequestMessage extends OutputOnlyProxyMessage
{
    private Direction8Path m_path;
    
    @Override
    public byte[] encode() {
        if (this.m_path != null) {
            final byte[] encodedPath = this.m_path.encode();
            return this.addClientHeader((byte)3, encodedPath);
        }
        return null;
    }
    
    @Override
    public int getId() {
        return 4113;
    }
    
    public Direction8Path getPath() {
        return this.m_path;
    }
    
    public void setPath(final Direction8Path path) {
        this.m_path = path;
    }
}
