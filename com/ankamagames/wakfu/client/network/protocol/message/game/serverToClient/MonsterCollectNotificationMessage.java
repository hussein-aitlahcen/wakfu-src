package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MonsterCollectNotificationMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_actionId;
    private boolean m_collectAvailable;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_actionId = buffer.getInt();
        this.m_collectAvailable = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 4526;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public boolean isCollectAvailable() {
        return this.m_collectAvailable;
    }
}
