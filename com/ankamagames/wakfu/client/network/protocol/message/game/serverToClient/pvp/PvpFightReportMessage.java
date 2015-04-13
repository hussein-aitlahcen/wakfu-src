package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.pvp.fight.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pvp.*;

public final class PvpFightReportMessage extends InputOnlyProxyMessage
{
    private long m_fightId;
    private PvpFightReport m_report;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_fightId = bb.getLong();
        this.m_report = new PvpFightReport(this.m_fightId);
        final short playersCount = bb.getShort();
        for (int i = 0; i < playersCount; ++i) {
            final long playerId = bb.getLong();
            this.m_report.setCurrentRank(playerId, NationPvpRanks.getById(bb.get()));
            this.m_report.setPreviousRank(playerId, NationPvpRanks.getById(bb.get()));
            this.m_report.setCurrentStrength(playerId, bb.getInt());
            this.m_report.setPreviousStrength(playerId, bb.getInt());
            this.m_report.setCurrentRanking(playerId, bb.getInt());
            this.m_report.setPreviousRanking(playerId, bb.getInt());
            this.m_report.setStreak(playerId, bb.getInt());
        }
        return false;
    }
    
    public long getFightId() {
        return this.m_fightId;
    }
    
    public PvpFightReport getReport() {
        return this.m_report;
    }
    
    @Override
    public int getId() {
        return 20402;
    }
}
