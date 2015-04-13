package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.nio.*;

public class SetEventStartDateMessage extends OutputOnlyProxyMessage
{
    private long m_eventUid;
    private GameDate m_date;
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    public void setDate(final GameDateConst date) {
        this.m_date = new GameDate(date);
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(this.m_eventUid);
        bb.putLong(this.m_date.toLong());
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 593;
    }
}
