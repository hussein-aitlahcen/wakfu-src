package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.nio.*;

public class ChallengeStatusMessage extends InputOnlyProxyMessage
{
    private byte m_status;
    private int m_timeBeforeCurrent;
    private int m_timeBeforeNext;
    private GameDate m_startDate;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_status = buff.get();
        this.m_timeBeforeCurrent = buff.getInt();
        this.m_timeBeforeNext = buff.getInt();
        this.m_startDate = GameDate.fromLong(buff.getLong());
        return true;
    }
    
    @Override
    public int getId() {
        return 11220;
    }
    
    public byte getStatus() {
        return this.m_status;
    }
    
    public int getTimeBeforeCurrent() {
        return this.m_timeBeforeCurrent;
    }
    
    public int getTimeBeforeNext() {
        return this.m_timeBeforeNext;
    }
    
    public GameDate getStartDate() {
        return this.m_startDate;
    }
}
