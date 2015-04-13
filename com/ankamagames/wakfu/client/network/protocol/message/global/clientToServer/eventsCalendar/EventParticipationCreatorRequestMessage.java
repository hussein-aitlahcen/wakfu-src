package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class EventParticipationCreatorRequestMessage extends OutputOnlyProxyMessage
{
    private long m_eventUid;
    private String m_characterToInviteName;
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    public void setCharacterToInviteId(final String characterToInviteName) {
        this.m_characterToInviteName = characterToInviteName;
    }
    
    @Override
    public byte[] encode() {
        final byte[] data = StringUtils.toUTF8(this.m_characterToInviteName);
        final ByteBuffer bb = ByteBuffer.allocate(9 + data.length);
        bb.putLong(this.m_eventUid);
        bb.put((byte)data.length);
        bb.put(data);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 579;
    }
}
