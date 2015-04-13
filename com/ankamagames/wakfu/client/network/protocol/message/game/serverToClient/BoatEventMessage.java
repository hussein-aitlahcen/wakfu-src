package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import java.nio.*;

public final class BoatEventMessage extends InputOnlyProxyMessage
{
    private BoatEventType m_type;
    private long m_collectorId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_type = BoatEventType.getById(buff.get());
        this.m_collectorId = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15206;
    }
    
    public BoatEventType getType() {
        return this.m_type;
    }
    
    public long getCollectorId() {
        return this.m_collectorId;
    }
}
