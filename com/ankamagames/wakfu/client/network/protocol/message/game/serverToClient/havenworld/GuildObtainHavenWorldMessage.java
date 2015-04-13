package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GuildObtainHavenWorldMessage extends InputOnlyProxyMessage
{
    private short m_havenWorldInstanceId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_havenWorldInstanceId = bb.getShort();
        return true;
    }
    
    public short getHavenWorldInstanceId() {
        return this.m_havenWorldInstanceId;
    }
    
    @Override
    public int getId() {
        return 20099;
    }
}
