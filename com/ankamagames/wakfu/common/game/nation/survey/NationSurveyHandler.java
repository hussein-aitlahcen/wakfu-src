package com.ankamagames.wakfu.common.game.nation.survey;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.rawData.*;

public class NationSurveyHandler
{
    private static final Logger m_logger;
    public static final float LOW_POPULARITY = -0.5f;
    public static final float HIGH_POPULARITY = 0.5f;
    private final Nation m_nation;
    private final TByteIntHashMap m_governmentOpinions;
    private final RatingProcedure m_ratingProcedure;
    private final ArrayList<GovernorPopularityListener> m_listeners;
    public static final int OPINION_REASON_NONE_ID = 0;
    public static final int OPINION_REASON_VOTE_RUNNING_ID = 1;
    public static final int OPINION_REASON_DIDNT_VOTE_ID = 2;
    public static final int OPINION_REASON_NO_GOVERNOR_ID = 3;
    
    public NationSurveyHandler(final Nation nation) {
        super();
        this.m_governmentOpinions = new TByteIntHashMap();
        this.m_ratingProcedure = new RatingProcedure();
        this.m_listeners = new ArrayList<GovernorPopularityListener>();
        this.m_nation = nation;
    }
    
    public boolean addListener(final GovernorPopularityListener o) {
        return !this.m_listeners.contains(o) && this.m_listeners.add(o);
    }
    
    public boolean citizenGiveGovernmentOpinion(final Citizen citizen, final GovernmentOpinion opinion) {
        if (citizen == null || opinion == null) {
            return false;
        }
        final CitizenComportment comp = citizen.getCitizenComportment();
        final GovernmentOpinion currentOpinion = comp.getGovernmentOpinion();
        final int resultCode = this.canGiveOpinion(citizen);
        if (resultCode != 0) {
            switch (resultCode) {
                case 2: {
                    NationSurveyHandler.m_logger.error((Object)("[Nation] Impossible de prendre en compte l'opinion du joueur " + citizen + " car il n'a pas vot\u00e9"));
                    break;
                }
                case 1: {
                    NationSurveyHandler.m_logger.error((Object)("[Nation] Impossible de prendre en compte l'opinion du joueur " + citizen + " car un vote est en cours"));
                    break;
                }
                case 3: {
                    NationSurveyHandler.m_logger.error((Object)("[Nation] Impossible de prendre en compte l'opinion du joueur " + citizen + " car il n'y a pas de gouverneur"));
                    break;
                }
            }
            return false;
        }
        this.m_governmentOpinions.adjustValue(currentOpinion.idx, -1);
        this.m_governmentOpinions.adjustOrPutValue(opinion.idx, 1, 1);
        comp.setGovernmentOpinion(opinion);
        this.computeRating();
        return true;
    }
    
    public void buildFromLastElection() {
        this.m_governmentOpinions.clear();
        this.m_governmentOpinions.put(GovernmentOpinion.UNKNOWN.idx, this.m_nation.getElectionHistory().getNbCandidateBallots());
        this.computeRating();
    }
    
    public void clear() {
        this.m_governmentOpinions.clear();
    }
    
    public int get(final GovernmentOpinion governmentOpinion) {
        return this.m_governmentOpinions.get(governmentOpinion.idx);
    }
    
    public int getTotalOpinions() {
        return this.m_ratingProcedure.getTotalVote();
    }
    
    public float getPopularityRate() {
        return this.m_ratingProcedure.getPopularityRate();
    }
    
    public boolean isLowPopularity() {
        return isLowPopularity(this.m_ratingProcedure.getPopularityRate());
    }
    
    public boolean isHighPopularity() {
        return isHighPopularity(this.m_ratingProcedure.getPopularityRate());
    }
    
    public static boolean isLowPopularity(final float popularity) {
        return popularity <= -0.5f;
    }
    
    public static boolean isHighPopularity(final float popularity) {
        return popularity >= 0.5f;
    }
    
    private void computeRating() {
        final float oldRate = this.m_ratingProcedure.getPopularityRate();
        this.m_ratingProcedure.clear();
        this.m_governmentOpinions.forEachEntry(this.m_ratingProcedure);
        final float newRate = this.m_ratingProcedure.getPopularityRate();
        if (newRate != oldRate) {
            for (int i = 0; i < this.m_listeners.size(); ++i) {
                this.m_listeners.get(i).onPopularityChanged(this.m_nation, oldRate, newRate);
            }
        }
    }
    
    public int canGiveOpinion(final Citizen citizen) {
        final CitizenComportment comp = citizen.getCitizenComportment();
        final GovernmentOpinion currentOpinion = comp.getGovernmentOpinion();
        if (this.m_nation.isVoteRunning()) {
            return 1;
        }
        final GameDateConst voteDate = comp.getVoteDate();
        final GameDate lastVoteDate = this.m_nation.getElectionHistory().getElectionStartDate();
        if (voteDate.before(lastVoteDate)) {
            return 2;
        }
        if (this.m_governmentOpinions.get(currentOpinion.idx) <= 0) {
            return 2;
        }
        if (comp.getNation().getGovernment().getGovernor() == null) {
            return 3;
        }
        return 0;
    }
    
    public void toRaw(final RawNationSurvey raw) {
        this.m_governmentOpinions.forEachEntry(new SetOpinion(raw));
    }
    
    public void fromRaw(final RawNationSurvey raw) {
        for (int i = 0, size = raw.governmentOpinions.size(); i < size; ++i) {
            final RawNationSurvey.Opinion rawOpinion = raw.governmentOpinions.get(i);
            this.m_governmentOpinions.put(rawOpinion.opinionId, rawOpinion.nbBallots);
        }
        this.computeRating();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationSurveyHandler.class);
    }
    
    private static class SetOpinion implements TByteIntProcedure
    {
        private final RawNationSurvey m_raw;
        
        SetOpinion(final RawNationSurvey raw) {
            super();
            this.m_raw = raw;
        }
        
        @Override
        public boolean execute(final byte a, final int b) {
            final RawNationSurvey.Opinion raw = new RawNationSurvey.Opinion();
            raw.opinionId = a;
            raw.nbBallots = b;
            this.m_raw.governmentOpinions.add(raw);
            return true;
        }
        
        @Override
        public String toString() {
            return "SetOpinion{m_raw=" + this.m_raw + '}';
        }
    }
}
