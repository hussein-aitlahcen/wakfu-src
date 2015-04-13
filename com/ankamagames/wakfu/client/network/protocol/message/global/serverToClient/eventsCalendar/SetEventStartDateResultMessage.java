package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class SetEventStartDateResultMessage extends InputOnlyProxyMessage
{
    private byte m_errorCode;
    private long m_eventId;
    private GameDate m_date;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.get();
        this.m_eventId = bb.getLong();
        this.m_date = GameDate.fromLong(bb.getLong());
        return true;
    }
    
    public byte getErrorCode() {
        return this.m_errorCode;
    }
    
    public long getEventId() {
        return this.m_eventId;
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    @Override
    public int getId() {
        return 594;
    }
}
