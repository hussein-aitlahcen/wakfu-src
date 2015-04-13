package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class ActorEquipmentPositionCleared extends InputOnlyProxyMessage
{
    private TShortArrayList m_positions;
    private long m_playerId;
    
    public ActorEquipmentPositionCleared() {
        super();
        this.m_positions = new TShortArrayList();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_playerId = buff.getLong();
        final byte size = buff.get();
        for (int i = 0; i < size; ++i) {
            this.m_positions.add(buff.getShort());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 11106;
    }
    
    public TShortArrayList getPositions() {
        return this.m_positions;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
}
