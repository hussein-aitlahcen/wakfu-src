package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DungeonLadderInformationMessage extends InputOnlyProxyMessage
{
    private short m_instanceId;
    private byte[] m_serializedLadderResults;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_instanceId = buffer.getShort();
        buffer.get(this.m_serializedLadderResults = new byte[buffer.remaining()]);
        return true;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public byte[] getSerializedLadderResults() {
        return this.m_serializedLadderResults;
    }
    
    @Override
    public int getId() {
        return 15950;
    }
}
