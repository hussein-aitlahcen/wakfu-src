package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorCollectMonsterRequestMessage extends OutputOnlyProxyMessage
{
    private int m_actionId;
    private long m_monsterID;
    
    public ActorCollectMonsterRequestMessage(final int actionId, final long monsterID) {
        super();
        this.m_actionId = actionId;
        this.m_monsterID = monsterID;
    }
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 12;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putInt(this.m_actionId);
        buffer.putLong(this.m_monsterID);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4167;
    }
}
