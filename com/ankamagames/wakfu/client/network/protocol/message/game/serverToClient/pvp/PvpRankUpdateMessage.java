package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import java.nio.*;

public class PvpRankUpdateMessage extends InputOnlyProxyMessage
{
    private int m_score;
    private int m_ranking;
    private NationPvpRanks m_rank;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_score = bb.getInt();
        this.m_ranking = bb.getInt();
        this.m_rank = NationPvpRanks.getById(bb.get());
        return true;
    }
    
    @Override
    public int getId() {
        return 20400;
    }
    
    public int getScore() {
        return this.m_score;
    }
    
    public int getRanking() {
        return this.m_ranking;
    }
    
    public NationPvpRanks getRank() {
        return this.m_rank;
    }
}
