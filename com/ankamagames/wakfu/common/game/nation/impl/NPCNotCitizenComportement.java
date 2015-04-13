package com.ankamagames.wakfu.common.game.nation.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import com.ankamagames.wakfu.common.game.pvp.*;

public class NPCNotCitizenComportement implements CitizenComportment
{
    private static final Logger m_logger;
    private final Citizen m_citizen;
    private static final EnumSet<NationRank> EMPTY_RANKS;
    
    public NPCNotCitizenComportement(final Citizen citizen) {
        super();
        this.m_citizen = citizen;
    }
    
    @Override
    public void copyFrom(final CitizenComportment comportment) {
    }
    
    @Override
    public void setNation(@NotNull final Nation nation) {
    }
    
    @NotNull
    @Override
    public Nation getNation() {
        return Nation.VOID_NATION;
    }
    
    @Override
    public int getNationId() {
        return 0;
    }
    
    @Override
    public long getCitizenId() {
        return this.m_citizen.getId();
    }
    
    @Override
    public GameDateConst getVoteDate() {
        return GameDate.NULL_DATE;
    }
    
    @Override
    public void setVoteDate(@NotNull final GameDateConst voteDate) {
    }
    
    @Override
    public boolean hasVoted() {
        return false;
    }
    
    @Override
    public boolean canVote() {
        return false;
    }
    
    @Override
    public boolean isCandidate() {
        return false;
    }
    
    @Override
    public CandidateInfo getCandidateInfo() {
        return null;
    }
    
    @Override
    public NationRank getRank() {
        return null;
    }
    
    @Override
    public void setRank(final NationRank rank) {
    }
    
    @Override
    public boolean hasRank(final NationRank rank) {
        return false;
    }
    
    @Override
    public EnumSet<NationJob> getJobs() {
        return null;
    }
    
    @Override
    public void setJobs(final Collection<NationJob> jobs) {
    }
    
    @Override
    public boolean hasJob(final NationJob job) {
        return false;
    }
    
    @Override
    public boolean addJob(final NationJob job) {
        return false;
    }
    
    @Override
    public boolean removeJob(final NationJob job) {
        return false;
    }
    
    @Override
    public void setGovernmentOpinion(final GovernmentOpinion governmentOpinion) {
    }
    
    @Override
    public GovernmentOpinion getGovernmentOpinion() {
        return GovernmentOpinion.UNKNOWN;
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public void resetToNation(final Nation nation) {
    }
    
    @Override
    public int getCitizenScoreForNation(final int nationId) {
        return 0;
    }
    
    public int adjustCitizenScore(final int nationId, final int toAdd) {
        return 0;
    }
    
    @Override
    public void startCrimePurgation(final int nationId) {
        throw new UnsupportedOperationException("Un NPC ne peut purger une peine");
    }
    
    @Override
    public boolean isPvpEnemy() {
        return false;
    }
    
    @Override
    public boolean isPvpEnemy(final int nationId) {
        return false;
    }
    
    @Override
    public boolean isNationEnemy() {
        return false;
    }
    
    @Override
    public boolean isNationEnemy(final int nationId) {
        return false;
    }
    
    @Override
    public boolean isOutlaw() {
        return false;
    }
    
    @Override
    public boolean isOutlaw(final int nationId) {
        return false;
    }
    
    @Override
    public boolean removeOffendedNation(final Nation nation) {
        return false;
    }
    
    @Override
    public void setPasseportActive(final boolean active) {
    }
    
    @Override
    public boolean isPasseportActive() {
        return false;
    }
    
    @Override
    public NationPvpState getPvpState() {
        return NationPvpState.PVP_OFF;
    }
    
    @Override
    public void setPvpState(final NationPvpState state) {
    }
    
    @Override
    public GameDateConst getPvpDate() {
        return GameDate.NULL_DATE;
    }
    
    @Override
    public void setPvpDate(final GameDateConst date) {
    }
    
    @Override
    public NationPvpRanks getPvpRank() {
        return NationPvpRanks.RANK_1;
    }
    
    @Override
    public void setPvpRank(final NationPvpRanks rank) {
    }
    
    @Override
    public void setPvpMoneyAmount(final long amount) {
    }
    
    @Override
    public void setDailyPvpMoneyAmount(final long amount) {
    }
    
    @Override
    public long getDailyPvpMoneyAmount() {
        return 0L;
    }
    
    @Override
    public long getPvpMoneyAmount() {
        return 0L;
    }
    
    @Override
    public GameDateConst getPvpMoneyStartDate() {
        return null;
    }
    
    @Override
    public void setPvpMoneyStartDate(final GameDateConst date) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NPCNotCitizenComportement.class);
        EMPTY_RANKS = EnumSet.noneOf(NationRank.class);
    }
}
