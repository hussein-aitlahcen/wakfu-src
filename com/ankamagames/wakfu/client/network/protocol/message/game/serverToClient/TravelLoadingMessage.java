package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.travel.*;
import java.nio.*;

public class TravelLoadingMessage extends InputOnlyProxyMessage
{
    private TravelType m_travelType;
    private long m_machineId;
    private long m_linkId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_travelType = TravelType.getFromId(bb.get());
        this.m_machineId = bb.getLong();
        this.m_linkId = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15802;
    }
    
    public TravelType getTravelType() {
        return this.m_travelType;
    }
    
    public long getMachineId() {
        return this.m_machineId;
    }
    
    public long getLinkId() {
        return this.m_linkId;
    }
}
