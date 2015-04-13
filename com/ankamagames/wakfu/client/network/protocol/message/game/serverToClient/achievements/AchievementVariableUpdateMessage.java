package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementVariableUpdateMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_variableId;
    private long m_variableValue;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_variableId = buffer.getInt();
        this.m_variableValue = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15604;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getVariableId() {
        return this.m_variableId;
    }
    
    public long getVariableValue() {
        return this.m_variableValue;
    }
}
