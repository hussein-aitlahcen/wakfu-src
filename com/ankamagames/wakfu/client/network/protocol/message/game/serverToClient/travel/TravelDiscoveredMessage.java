package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.travel;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.travel.*;
import java.nio.*;

public final class TravelDiscoveredMessage extends InputOnlyProxyMessage
{
    private TravelType m_travelType;
    private int m_machineId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_machineId = bb.getInt();
        this.m_travelType = TravelType.getFromId(bb.get());
        return false;
    }
    
    public TravelType getTravelType() {
        return this.m_travelType;
    }
    
    public int getMachineId() {
        return this.m_machineId;
    }
    
    @Override
    public int getId() {
        return 15727;
    }
}
