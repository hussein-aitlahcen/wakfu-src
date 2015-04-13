package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AchievementObjectiveCompleteMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_objectiveId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_objectiveId = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15600;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getObjectiveId() {
        return this.m_objectiveId;
    }
}
