package com.ankamagames.wakfu.common.game.pvp.fight;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.pvp.*;

public final class PvpFightReport
{
    private final long m_fightId;
    private final TLongObjectHashMap<PlayerReportInfo> m_playersReportsInfos;
    
    public PvpFightReport(final long fightId) {
        super();
        this.m_playersReportsInfos = new TLongObjectHashMap<PlayerReportInfo>();
        this.m_fightId = fightId;
    }
    
    private PlayerReportInfo getOrCreatePlayerInfo(final long playerId) {
        PlayerReportInfo playerReportInfo = this.m_playersReportsInfos.get(playerId);
        if (playerReportInfo == null) {
            playerReportInfo = new PlayerReportInfo();
            this.m_playersReportsInfos.put(playerId, playerReportInfo);
        }
        return playerReportInfo;
    }
    
    public void setPreviousStrength(final long playerId, final int strength) {
        final PlayerReportInfo playerReportInfo = this.getOrCreatePlayerInfo(playerId);
        playerReportInfo.setPreviousStrength(strength);
    }
    
    public void setCurrentStrength(final long playerId, final int strength) {
        final PlayerReportInfo playerInfo = this.getOrCreatePlayerInfo(playerId);
        playerInfo.setCurrentStrength(strength);
    }
    
    public void setCurrentRanking(final long playerId, final int ranking) {
        final PlayerReportInfo playerInfo = this.getOrCreatePlayerInfo(playerId);
        playerInfo.setCurrentRanking(ranking);
    }
    
    public void setCurrentRank(final long playerId, final NationPvpRanks rank) {
        final PlayerReportInfo playerInfo = this.getOrCreatePlayerInfo(playerId);
        playerInfo.setCurrentRank(rank);
    }
    
    public void setPreviousRanking(final long playerId, final int ranking) {
        final PlayerReportInfo playerInfo = this.getOrCreatePlayerInfo(playerId);
        playerInfo.setPreviousRanking(ranking);
    }
    
    public void setPreviousRank(final long playerId, final NationPvpRanks rank) {
        final PlayerReportInfo playerInfo = this.getOrCreatePlayerInfo(playerId);
        playerInfo.setPreviousRank(rank);
    }
    
    public void setStreak(final long playerId, final int streak) {
        final PlayerReportInfo playerInfo = this.getOrCreatePlayerInfo(playerId);
        playerInfo.setStreak(streak);
    }
    
    public long getFightId() {
        return this.m_fightId;
    }
    
    public TLongObjectHashMap<PlayerReportInfo> getPlayersReportsInfos() {
        return this.m_playersReportsInfos;
    }
    
    @Override
    public String toString() {
        return "PvpFightReport{m_fightId=" + this.m_fightId + ", m_playersReportsInfos=" + this.m_playersReportsInfos + '}';
    }
}
