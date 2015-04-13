package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DungeonWaveMessage extends InputOnlyProxyMessage
{
    private int m_bonusId;
    private int m_eventId;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_bonusId = bb.getInt();
        this.m_eventId = bb.getInt();
        return true;
    }
    
    public int getBonusId() {
        return this.m_bonusId;
    }
    
    public int getEventId() {
        return this.m_eventId;
    }
    
    @Override
    public int getId() {
        return 15954;
    }
}
