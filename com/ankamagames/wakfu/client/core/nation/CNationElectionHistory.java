package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.election.*;
import java.util.*;

public class CNationElectionHistory extends NationElectionHistory
{
    private int m_nbCandidates;
    
    public CNationElectionHistory() {
        super();
        this.m_nbCandidates = 0;
    }
    
    @Override
    public int getNbCandidates() {
        return this.m_nbCandidates;
    }
    
    public void setCandidates(final ArrayList<CandidateInfo> candidates, final int nbCandidates, final int nbBallots) {
        this.m_candidates.addAll(candidates);
        this.m_ballotCount = nbBallots;
        this.m_nbCandidates = nbCandidates;
    }
}
