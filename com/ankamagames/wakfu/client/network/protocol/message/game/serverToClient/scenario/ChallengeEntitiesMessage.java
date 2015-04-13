package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChallengeEntitiesMessage extends InputOnlyProxyMessage
{
    private int m_entityCount;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_entityCount = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11226;
    }
    
    public int getEntityCount() {
        return this.m_entityCount;
    }
}
