package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.handlers.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.impl.*;
import com.ankamagames.wakfu.common.game.nation.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;

public class CNationPoliticHandler extends NationPoliticHandler
{
    public static final String OVERHEAD_POLITIC_COLOR = "efcc0a";
    private int m_nbCandidates;
    private int m_nbBallots;
    
    public CNationPoliticHandler(final Nation nation) {
        super(nation);
        this.m_voteDatePart = new VoteDatePart() {
            @Override
            protected void onDataChanged() {
                CNationPoliticHandler.m_logger.info((Object)("Mise \u00e0 jour du vote pour la nation " + CNationPoliticHandler.this.getNation() + " : Running : " + this.m_partVoteRunning + " start : " + this.m_partStartDate));
                CNationPoliticHandler.this.m_bVoteRunning = this.m_partVoteRunning;
                CNationPoliticHandler.this.m_voteDuration.set(this.m_partDuration);
                CNationPoliticHandler.this.m_voteFrequency.set(this.m_partFrequency);
                CNationPoliticHandler.this.setVoteStartDate(this.m_partStartDate);
            }
        };
    }
    
    @Override
    protected NationElectionHistory createHistory() {
        return new CNationElectionHistory();
    }
    
    @Override
    public void requestCandidateRegistration(final long characterId, final String slogan) {
        CNationPoliticHandler.m_logger.info((Object)"Demande d'enregistrement d'un candidat \u00e0 la nation !!!");
        final NationCandidateRegistrationRequest request = new NationCandidateRegistrationRequest();
        request.setNationId(this.getNation().getNationId());
        request.setCharacterId(characterId);
        request.setSlogan(slogan);
        NationActionRequestDispatcher.INSTANCE.send(request);
    }
    
    @Override
    public void requestCandidateRegistration(final long citizenId, final CandidateInfo candidate) {
        throw new UnsupportedOperationException("[NATION] Impossible d'enregistrer des CandidateInfo d\u00e9j\u00e0 initialis\u00e9s \u00e0 partir d'ici");
    }
    
    @Override
    public void setCandidates(final ArrayList<CandidateInfo> candidates) {
        this.clearCandidates();
        NationDisplayer.getInstance().clearElections();
        for (int i = 0; i < candidates.size(); ++i) {
            final CandidateInfo candidateInfo = candidates.get(i);
            this.m_candidates.put(candidateInfo.getId(), candidateInfo);
        }
        NationDisplayer.getInstance().updateUI();
    }
    
    @Override
    public void setCandidatesStats(final int nbCandidates, final int nbBallots) {
        this.m_nbCandidates = nbCandidates;
        this.m_nbBallots = nbBallots;
    }
    
    @Override
    public void requestGovernmentNominationResult(final long characterId, final byte result, final long rankId) {
        final NationGovernmentNominationConfirmationResult request = new NationGovernmentNominationConfirmationResult();
        request.setNationId(this.getNation().getNationId());
        request.setCharacterId(characterId);
        request.setResult(result);
        request.setRankId(rankId);
        NationActionRequestDispatcher.INSTANCE.send(request);
    }
    
    @Override
    public void requestGovernmentRevoke(final NationRank requesterRank, final NationRank requestedRank, final RevokeReason reason) {
        final NationGovernmentRevokeRequest request = new NationGovernmentRevokeRequest();
        request.setNationId(this.getNation().getNationId());
        request.setRequesterRankId(requesterRank.getId());
        request.setRequestedRankId(requestedRank.getId());
        request.setReason(reason);
        NationActionRequestDispatcher.INSTANCE.send(request);
    }
    
    @Override
    public void registerVoteState(final GameDateConst voteStartDate, final GameIntervalConst voteDuration, final boolean voteRunning) {
        this.setVoteStartDate(voteStartDate);
        this.m_voteDuration.set(voteDuration);
        this.m_bVoteRunning = voteRunning;
    }
    
    @Override
    public boolean requestVoteEnd() {
        this.m_candidates.clear();
        return true;
    }
    
    @Override
    public void requestCitizenVote(final long citizenId, final long candidateId) {
        final NationCandidateVoteRequest request = new NationCandidateVoteRequest();
        request.setNationId(this.getNation().getNationId());
        request.setCandidateId(candidateId);
        request.setCitizenId(citizenId);
        NationActionRequestDispatcher.INSTANCE.send(request);
    }
    
    @Override
    protected void clearCandidates() {
        this.m_nbBallots = 0;
        this.m_nbCandidates = 0;
        final TLongObjectIterator<CandidateInfo> it = this.getNation().getCandidateIterator();
        while (it.hasNext()) {
            it.advance();
            final long id = it.key();
            final Citizen citizen = this.getNation().getCitizen(id);
            if (citizen == null) {
                continue;
            }
            ((ClientCitizenComportment)citizen.getCitizenComportment()).updateCandidateInfo();
        }
        super.clearCandidates();
    }
    
    @Override
    public int getNbCandidates() {
        return this.m_nbCandidates;
    }
    
    @Override
    public int getNbCandidateBallots() {
        return this.m_nbBallots;
    }
    
    public void setNbBallots(final int nbBallots) {
        this.m_nbBallots = nbBallots;
    }
    
    @Override
    public void onCitizenHasVoted(final long citizenId, final long candidateId) {
    }
    
    @Override
    public void onCandidateRegistered(final long citizenId) {
    }
    
    @Override
    public void onCandidateWithdraw(final long citizenId) {
    }
    
    @Override
    public void onCandidateRevalidate(final long citizenId) {
    }
    
    @Override
    public void onForbiddenCandidatesChanged() {
    }
}
