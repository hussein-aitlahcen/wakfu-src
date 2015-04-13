package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class UnvalidateParticipationResultMessage extends InputOnlyProxyMessage
{
    private byte m_errorCode;
    private long m_eventId;
    private long m_characterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.get();
        this.m_eventId = bb.getLong();
        this.m_characterId = bb.getLong();
        return true;
    }
    
    public byte getErrorCode() {
        return this.m_errorCode;
    }
    
    public long getEventId() {
        return this.m_eventId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    @Override
    public int getId() {
        return 576;
    }
}
