package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MonsterBehaviourMessage extends InputOnlyProxyMessage
{
    private long m_CharacterId;
    private int m_behaviourId;
    private long m_targetId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_behaviourId = buffer.getInt();
        this.m_CharacterId = buffer.getLong();
        this.m_targetId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 4108;
    }
    
    public int getBehaviourId() {
        return this.m_behaviourId;
    }
    
    public long getCharacterId() {
        return this.m_CharacterId;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
}
