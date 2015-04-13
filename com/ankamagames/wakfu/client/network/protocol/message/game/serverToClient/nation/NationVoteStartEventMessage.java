package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class NationVoteStartEventMessage extends InputOnlyProxyMessage
{
    private static final Logger m_logger;
    private GameInterval m_voteDuration;
    private GameDate m_voteStart;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_voteStart = GameDate.fromLong(buffer.getLong());
        this.m_voteDuration = GameInterval.fromSeconds(buffer.getLong());
        return true;
    }
    
    public GameIntervalConst getVoteDuration() {
        return this.m_voteDuration;
    }
    
    public GameDate getVoteStart() {
        return this.m_voteStart;
    }
    
    @Override
    public int getId() {
        return 20004;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationVoteStartEventMessage.class);
    }
}
