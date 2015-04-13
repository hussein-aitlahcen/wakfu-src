package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import java.nio.*;

public class NationCandidateVoteRequest extends NationActionRequest
{
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private long m_citizenId;
    private long m_candidateId;
    
    public NationCandidateVoteRequest() {
        super(NationActionRequestType.CANDIDATE_VOTE_REQUEST);
    }
    
    public void setCitizenId(final long citizenId) {
        this.m_citizenId = citizenId;
    }
    
    public void setCandidateId(final long candidateId) {
        this.m_candidateId = candidateId;
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationCandidateVoteRequest.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        nation.requestCitizenVote(this.m_citizenId, this.m_candidateId);
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        final int requesterNationId = citizen.getCitizenComportment().getNationId();
        if (requesterNationId != this.m_nationId) {
            return false;
        }
        if (citizen.getLevel() < 1) {
            return false;
        }
        final int citizenScore = citizen.getCitizenComportment().getCitizenScoreForNation(requesterNationId);
        return CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScore).hasRule(CitizenRankRule.CAN_VOTE);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_citizenId);
        buffer.putLong(this.m_candidateId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_citizenId = buffer.getLong();
        this.m_candidateId = buffer.getLong();
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 16;
    }
    
    @Override
    public void clear() {
        this.m_nationId = -1;
        this.m_citizenId = -1L;
        this.m_candidateId = -1L;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationCandidateVoteRequest.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationCandidateVoteRequest();
            }
        };
    }
}
