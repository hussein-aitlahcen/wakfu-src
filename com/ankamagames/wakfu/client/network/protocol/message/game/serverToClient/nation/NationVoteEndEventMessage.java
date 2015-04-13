package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class NationVoteEndEventMessage extends InputOnlyProxyMessage
{
    private static final Logger m_logger;
    private GameDate m_voteStartDate;
    private String m_newGovernorName;
    private String m_oldGovernorName;
    private GameDate m_nextVoteStartDate;
    private GameInterval m_nextVoteDuration;
    private boolean m_governorElected;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_voteStartDate = GameDate.fromLong(buff.getLong());
        this.m_nextVoteStartDate = GameDate.fromLong(buff.getLong());
        this.m_nextVoteDuration = GameInterval.fromSeconds(buff.getLong());
        this.m_governorElected = (buff.get() == 1);
        final boolean bMayor = buff.get() == 1;
        if (bMayor) {
            final byte[] mayorNameData = new byte[buff.get()];
            buff.get(mayorNameData);
            this.m_newGovernorName = StringUtils.fromUTF8(mayorNameData);
        }
        final boolean bOldMayor = buff.get() == 1;
        if (bOldMayor) {
            final byte[] mayorNameData2 = new byte[buff.get()];
            buff.get(mayorNameData2);
            this.m_oldGovernorName = StringUtils.fromUTF8(mayorNameData2);
        }
        return true;
    }
    
    public GameDateConst getVoteStartDate() {
        return this.m_voteStartDate;
    }
    
    public GameDate getNextVoteStartDate() {
        return this.m_nextVoteStartDate;
    }
    
    public GameInterval getNextVoteDuration() {
        return this.m_nextVoteDuration;
    }
    
    public boolean isGovernorElected() {
        return this.m_governorElected;
    }
    
    public String getNewGovernorName() {
        return this.m_newGovernorName;
    }
    
    public String getOldGovernorName() {
        return this.m_oldGovernorName;
    }
    
    @Override
    public int getId() {
        return 20006;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationVoteEndEventMessage.class);
    }
}
