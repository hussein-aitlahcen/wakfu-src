package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MonsterEvolutionMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_evolutionStepId;
    private short m_newLevel;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_evolutionStepId = buffer.getInt();
        this.m_characterId = buffer.getLong();
        this.m_newLevel = buffer.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 4110;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getEvolutionStepId() {
        return this.m_evolutionStepId;
    }
    
    public short getNewLevel() {
        return this.m_newLevel;
    }
}
