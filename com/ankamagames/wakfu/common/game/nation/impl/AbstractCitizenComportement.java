package com.ankamagames.wakfu.common.game.nation.impl;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.util.*;

public abstract class AbstractCitizenComportement implements CitizenComportment
{
    protected static final Logger m_logger;
    @NotNull
    private Nation m_nation;
    private final EnumSet<NationJob> m_jobs;
    private final GameDate m_voteDate;
    private GovernmentOpinion m_governmentOpinion;
    private NationPvpState m_pvpState;
    private GameDateConst m_pvpDate;
    private NationPvpRanks m_pvpRanks;
    private long m_pvpMoneyAmount;
    private long m_dailyPvpMoneyAmount;
    private GameDateConst m_dailyPvpMoneyDate;
    protected final Citizen m_citizen;
    
    protected AbstractCitizenComportement(final Citizen citizen) {
        super();
        this.m_jobs = EnumSet.noneOf(NationJob.class);
        this.m_voteDate = GameDate.getNullDate();
        this.m_governmentOpinion = GovernmentOpinion.UNKNOWN;
        this.m_pvpState = NationPvpState.PVP_OFF;
        this.m_pvpDate = GameDate.NULL_DATE;
        this.m_pvpRanks = NationPvpRanks.RANK_1;
        this.m_dailyPvpMoneyDate = GameDate.NULL_DATE;
        this.m_citizen = citizen;
    }
    
    @Override
    public long getCitizenId() {
        return this.m_citizen.getId();
    }
    
    @Override
    public void copyFrom(final CitizenComportment comportment) {
        this.setNation(comportment.getNation());
        this.setRank(comportment.getRank());
        this.setJobs(comportment.getJobs());
        this.setVoteDate(comportment.getVoteDate());
        this.m_governmentOpinion = comportment.getGovernmentOpinion();
        this.setPvpState(comportment.getPvpState());
        this.setPvpDate(comportment.getPvpDate());
        this.m_pvpRanks = comportment.getPvpRank();
        this.m_pvpMoneyAmount = comportment.getPvpMoneyAmount();
        this.m_dailyPvpMoneyAmount = comportment.getDailyPvpMoneyAmount();
        this.m_dailyPvpMoneyDate = comportment.getPvpMoneyStartDate();
    }
    
    @Override
    public void setNation(@NotNull final Nation nation) {
        this.m_nation = nation;
    }
    
    @NotNull
    @Override
    public Nation getNation() {
        return this.m_nation;
    }
    
    @Override
    public int getNationId() {
        return this.m_nation.getNationId();
    }
    
    @Override
    public GameDateConst getVoteDate() {
        if (this.m_nation == Nation.VOID_NATION) {
            return GameDate.NULL_DATE;
        }
        if (this.m_voteDate != null) {
            return this.m_voteDate;
        }
        return GameDate.NULL_DATE;
    }
    
    @Override
    public void setVoteDate(@NotNull final GameDateConst voteDate) {
        if (this.m_nation == Nation.VOID_NATION) {
            return;
        }
        this.m_voteDate.set(voteDate);
    }
    
    @Override
    public boolean hasVoted() {
        return this.m_nation != Nation.VOID_NATION && this.m_voteDate != null && !this.m_voteDate.isNull() && this.m_nation.getVoteStartDate().before(this.m_voteDate);
    }
    
    @Override
    public boolean canVote() {
        return this.m_nation.isVoteRunning() && !this.hasVoted();
    }
    
    @Override
    public boolean isCandidate() {
        return this.m_nation.isCandidate(this.getCitizenId());
    }
    
    @Override
    public CandidateInfo getCandidateInfo() {
        return this.m_nation.getCandidateInfo(this.getCitizenId());
    }
    
    @Override
    public boolean addJob(final NationJob job) {
        return this.m_jobs.add(job);
    }
    
    @Override
    public boolean removeJob(final NationJob job) {
        return this.m_jobs.remove(job);
    }
    
    @Override
    public boolean hasJob(final NationJob job) {
        return this.m_jobs.contains(job);
    }
    
    @Override
    public EnumSet<NationJob> getJobs() {
        return this.m_jobs;
    }
    
    @Override
    public void setJobs(final Collection<NationJob> jobs) {
        this.m_jobs.clear();
        this.m_jobs.addAll((Collection<?>)jobs);
    }
    
    @Override
    public GovernmentOpinion getGovernmentOpinion() {
        return this.m_governmentOpinion;
    }
    
    @Override
    public void setGovernmentOpinion(final GovernmentOpinion governmentOpinion) {
        this.m_governmentOpinion = governmentOpinion;
    }
    
    @Override
    public void reset() {
        this.resetToNation(Nation.VOID_NATION);
    }
    
    @Override
    public void resetToNation(@NotNull final Nation nation) {
        this.setNation(nation);
        this.setRank(null);
        this.setJobs((Collection<NationJob>)Collections.emptySet());
        this.setVoteDate(GameDate.NULL_DATE);
        this.m_governmentOpinion = GovernmentOpinion.UNKNOWN;
    }
    
    @Override
    public int getCitizenScoreForNation(final int nationId) {
        return 0;
    }
    
    @Override
    public void startCrimePurgation(final int nationId) {
        throw new UnsupportedOperationException("Impossible de lancer une purgation de peine : \u00e0 impl\u00e9menter autre part");
    }
    
    public void stopCrimePurgation() {
        throw new UnsupportedOperationException("Impossible de stopper une purgation de peine : \u00e0 impl\u00e9menter autre part");
    }
    
    public int getCrimePurgationScore() {
        throw new UnsupportedOperationException("Pas de purgation ici : \u00e0 impl\u00e9menter autre part");
    }
    
    public int getCrimePurgationNationId() {
        throw new UnsupportedOperationException("Pas de purgation ici : \u00e0 impl\u00e9menter autre part");
    }
    
    public int addPurgationCrimePoint(final int points) {
        throw new UnsupportedOperationException("Pas de purgation ici : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isPvpEnemy() {
        throw new UnsupportedOperationException("Impossible de Savoir si on est pvpEnemy de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isPvpEnemy(final int nationId) {
        throw new UnsupportedOperationException("Impossible de Savoir si on est pvpEnemy de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isNationEnemy() {
        throw new UnsupportedOperationException("Impossible de Savoir si on est enemy de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isNationEnemy(final int nationId) {
        throw new UnsupportedOperationException("Impossible de Savoir si on est enemy de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isOutlaw() {
        throw new UnsupportedOperationException("Impossible de savoir si on est hors la loi de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isOutlaw(final int nationId) {
        throw new UnsupportedOperationException("Impossible de savoir si on est hors la loi de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean removeOffendedNation(final Nation nation) {
        throw new UnsupportedOperationException("Impossible de retirer une nation aux nations offens\u00e9es : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public void setPasseportActive(final boolean active) {
        throw new UnsupportedOperationException("Impossible de Savoir si on est enemy de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public boolean isPasseportActive() {
        throw new UnsupportedOperationException("Impossible de Savoir si on est enemy de la nation : \u00e0 impl\u00e9menter autre part");
    }
    
    @Override
    public NationPvpState getPvpState() {
        return this.m_pvpState;
    }
    
    @Override
    public void setPvpState(final NationPvpState state) {
        this.m_pvpState = state;
    }
    
    @Override
    public GameDateConst getPvpDate() {
        return this.m_pvpDate;
    }
    
    @Override
    public void setPvpDate(final GameDateConst date) {
        this.m_pvpDate = new GameDate(date);
    }
    
    @Override
    public NationPvpRanks getPvpRank() {
        return this.m_pvpRanks;
    }
    
    @Override
    public void setPvpRank(final NationPvpRanks rank) {
        this.m_pvpRanks = rank;
    }
    
    @Override
    public void setPvpMoneyAmount(final long amount) {
        this.m_pvpMoneyAmount = amount;
    }
    
    @Override
    public void setDailyPvpMoneyAmount(final long amount) {
        this.m_dailyPvpMoneyAmount = amount;
    }
    
    @Override
    public GameDateConst getPvpMoneyStartDate() {
        return this.m_dailyPvpMoneyDate;
    }
    
    @Override
    public void setPvpMoneyStartDate(final GameDateConst date) {
        this.m_dailyPvpMoneyDate = date;
    }
    
    @Override
    public long getDailyPvpMoneyAmount() {
        return this.m_dailyPvpMoneyAmount;
    }
    
    @Override
    public long getPvpMoneyAmount() {
        return this.m_pvpMoneyAmount;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractCitizenComportement.class);
    }
}
