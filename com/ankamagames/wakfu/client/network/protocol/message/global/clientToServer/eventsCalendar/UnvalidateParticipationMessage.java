package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class UnvalidateParticipationMessage extends OutputOnlyProxyMessage
{
    long m_eventUid;
    long m_characterId;
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(this.m_eventUid);
        bb.putLong(this.m_characterId);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 575;
    }
}
