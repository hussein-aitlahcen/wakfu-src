package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import java.nio.*;

public class CollectMonsterOccupationModifMessage extends AbstractOccupationModificationMessage
{
    private long m_collectTime;
    private long m_monsterID;
    private int m_actionID;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_concernedPlayerId = buffer.getLong();
        this.m_collectTime = buffer.getLong();
        this.m_monsterID = buffer.getLong();
        this.m_actionID = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4168;
    }
    
    public int getActionID() {
        return this.m_actionID;
    }
    
    public long getCollectTime() {
        return this.m_collectTime;
    }
    
    public long getMonsterID() {
        return this.m_monsterID;
    }
}
