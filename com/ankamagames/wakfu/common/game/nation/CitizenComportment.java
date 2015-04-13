package com.ankamagames.wakfu.common.game.nation;

import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.pvp.*;

public interface CitizenComportment
{
    void copyFrom(CitizenComportment p0);
    
    void setNation(@NotNull Nation p0);
    
    @NotNull
    Nation getNation();
    
    int getNationId();
    
    long getCitizenId();
    
    GameDateConst getVoteDate();
    
    void setVoteDate(@NotNull GameDateConst p0);
    
    boolean hasVoted();
    
    boolean canVote();
    
    boolean isCandidate();
    
    CandidateInfo getCandidateInfo();
    
    void reset();
    
    void resetToNation(Nation p0);
    
    int getCitizenScoreForNation(int p0);
    
    void startCrimePurgation(int p0);
    
    boolean isPvpEnemy();
    
    boolean isPvpEnemy(int p0);
    
    boolean isNationEnemy();
    
    boolean isNationEnemy(int p0);
    
    boolean isOutlaw();
    
    boolean isOutlaw(int p0);
    
    boolean removeOffendedNation(Nation p0);
    
    void setPasseportActive(boolean p0);
    
    boolean isPasseportActive();
    
    void setGovernmentOpinion(GovernmentOpinion p0);
    
    GovernmentOpinion getGovernmentOpinion();
    
    NationRank getRank();
    
    void setRank(NationRank p0);
    
    boolean hasRank(NationRank p0);
    
    EnumSet<NationJob> getJobs();
    
    void setJobs(Collection<NationJob> p0);
    
    boolean hasJob(NationJob p0);
    
    boolean addJob(NationJob p0);
    
    boolean removeJob(NationJob p0);
    
    NationPvpState getPvpState();
    
    void setPvpState(NationPvpState p0);
    
    GameDateConst getPvpDate();
    
    void setPvpDate(GameDateConst p0);
    
    NationPvpRanks getPvpRank();
    
    void setPvpRank(NationPvpRanks p0);
    
    void setPvpMoneyAmount(long p0);
    
    void setDailyPvpMoneyAmount(long p0);
    
    long getDailyPvpMoneyAmount();
    
    long getPvpMoneyAmount();
    
    GameDateConst getPvpMoneyStartDate();
    
    void setPvpMoneyStartDate(GameDateConst p0);
}
