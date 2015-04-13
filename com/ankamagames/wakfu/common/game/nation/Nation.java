package com.ankamagames.wakfu.common.game.nation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.data.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.impl.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.wakfu.common.game.nation.diplomacy.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.nation.handlers.*;
import java.nio.*;

public final class Nation
{
    protected static final Logger m_logger;
    public static final boolean DEBUG_MODE = true;
    public static final int VOID_NATION_ID = 0;
    public static final int AMAKNA = 30;
    public static final int BONTA = 31;
    public static final int BRAKMAR = 32;
    public static final int SUFOKIA = 33;
    public static final int BANDIT = 34;
    public static final Nation VOID_NATION;
    private static NationHandlersFactory m_handlersFactory;
    @Nullable
    private NationMembersHandler m_membersHandler;
    @Nullable
    private NationPoliticHandler m_politicHandler;
    @Nullable
    private final NationBuffsHandler m_buffsHandler;
    @Nullable
    private NationJusticeHandler m_justiceHandler;
    private int m_nationId;
    private final NationSerializer m_serializer;
    private final IdentificationPart m_identificationPart;
    
    public static void setHandlersFactory(final NationHandlersFactory handlersFactory) {
        Nation.m_handlersFactory = handlersFactory;
        Nation.VOID_NATION.m_membersHandler = handlersFactory.createMembersHandler(Nation.VOID_NATION);
        Nation.VOID_NATION.m_politicHandler = handlersFactory.createPoliticHandler(Nation.VOID_NATION);
        Nation.VOID_NATION.m_justiceHandler = handlersFactory.createJusticeHandler(Nation.VOID_NATION);
    }
    
    public static Nation createNation() {
        return createNation(-1);
    }
    
    public static Nation createNation(final int nationId) {
        if (Nation.m_handlersFactory == null) {
            throw new UnsupportedOperationException("La factory de handlers de nation (NationHandlersFactory) doit \u00eatre d\u00e9finie");
        }
        return new Nation(nationId, Nation.m_handlersFactory);
    }
    
    private Nation(final int nationId, final NationHandlersFactory handlersFactory) {
        super();
        this.m_serializer = new NationSerializer(this);
        this.m_identificationPart = new IdentificationPart();
        this.m_membersHandler = handlersFactory.createMembersHandler(this);
        this.m_politicHandler = handlersFactory.createPoliticHandler(this);
        this.m_buffsHandler = handlersFactory.createBuffsHandler(this);
        this.m_justiceHandler = handlersFactory.createJusticeHandler(this);
        this.m_nationId = nationId;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    public void setNationId(final int id) {
        this.m_nationId = id;
    }
    
    public void finishInitialization() {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.finishInitialization();
        }
        if (this.m_politicHandler != null) {
            this.m_politicHandler.finishInitialization();
        }
        if (this.m_buffsHandler != null) {
            this.m_buffsHandler.finishInitialization();
        }
        if (this.m_justiceHandler != null) {
            this.m_justiceHandler.finishInitialization();
        }
    }
    
    public byte[] serialize(final NationSerializationType type) {
        return this.m_serializer.build(type);
    }
    
    public void unserialize(final byte[] data, final int version) {
        this.m_serializer.fromBuild(data, version);
    }
    
    public byte[] serializeForWorldToGlobal() {
        return this.m_serializer.build(NationSerializationType.FOR_WORLD_TO_GLOBAL);
    }
    
    public byte[] serializeForGameToClient() {
        return this.m_serializer.build(NationSerializationType.GAME_TO_LOCAL_FOR_CHARACTER_NATION_INITIALIZATION);
    }
    
    public NationPart getPart(final NationSerializationType.Part part) {
        switch (part) {
            case ID: {
                return this.getIdentificationPart();
            }
            case VOTE_DATE: {
                return this.getVoteDatePart();
            }
            case CANDIDATES_LIST_FULL: {
                return this.getCandidatesListPart();
            }
            case ELECTION_HISTORY: {
                return this.getElectionHistoryPart();
            }
            case FORBIDDEN_CANDIDATES: {
                return this.getForbiddenCandidatePart();
            }
            case PROTECTOR_BUFFS: {
                return this.getProtectorBuffsPart();
            }
            case VOTE_UPDATE: {
                return this.getVoteUpdatePart();
            }
            case JAIL: {
                return this.getJailPart();
            }
            case LAWS: {
                return this.getLawsPart();
            }
            case GOVERNOR_BOOK: {
                return this.getGovernorBookPart();
            }
            case GOVERNMENT_FULL: {
                return this.getGovernmentFullPart();
            }
            case DIPLOMACY: {
                return this.getDiplomacyPart();
            }
            case ECONOMY: {
                return this.getEconomyPart();
            }
            case PROTECTORS: {
                return this.getProtectorInfoPart();
            }
            case SURVEY: {
                return this.getSurveyPart();
            }
            case ECONOMY_BDD: {
                return this.getEconomyBddPart();
            }
            default: {
                return NationPart.EMPTY_PART;
            }
        }
    }
    
    NationPart getVoteDatePart() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getVoteDatePart();
        }
        return NationPart.EMPTY_PART;
    }
    
    NationPart getCandidatesListPart() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getCandidatesListPart();
        }
        return NationPart.EMPTY_PART;
    }
    
    NationPart getElectionHistoryPart() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getElectionHistoryPart();
        }
        return NationPart.EMPTY_PART;
    }
    
    NationPart getProtectorBuffsPart() {
        if (this.m_buffsHandler != null) {
            return this.m_buffsHandler.getProtectorBuffsPart();
        }
        return NationPart.EMPTY_PART;
    }
    
    NationPart getVoteUpdatePart() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getVoteUpdatePart();
        }
        return NationPart.EMPTY_PART;
    }
    
    IdentificationPart getIdentificationPart() {
        return this.m_identificationPart;
    }
    
    @Override
    public String toString() {
        return "{Nation id=" + this.m_nationId + "}";
    }
    
    public void initializeJustice(final short jailInstanceId, final short excludedRespawnInstanceId, final int excludedRespawnX, final int excludedRespawnY) {
        if (this.m_justiceHandler != null) {
            this.m_justiceHandler.initialize(jailInstanceId, excludedRespawnInstanceId, excludedRespawnX, excludedRespawnY);
        }
    }
    
    public NationLawsManager getLawManager() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getLawManager();
        }
        throw new UnsupportedOperationException();
    }
    
    public short getJailInstanceId() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getJailInstanceId();
        }
        throw new UnsupportedOperationException();
    }
    
    public short getExcludedRespawnInstanceId() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getExcludedRespawnInstanceId();
        }
        throw new UnsupportedOperationException();
    }
    
    public int getExcludedRespawnX() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getExcludedRespawnX();
        }
        throw new UnsupportedOperationException();
    }
    
    public int getExcludedRespawnY() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getExcludedRespawnY();
        }
        throw new UnsupportedOperationException();
    }
    
    public void onCriminalCandidate(final long citizenId) {
        if (this.m_justiceHandler != null) {
            this.m_justiceHandler.onCriminalCandidate(citizenId);
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    public void onRevalidateCandidate(final long citizenId) {
        if (this.m_justiceHandler != null) {
            this.m_justiceHandler.onRevalidateCandidate(citizenId);
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    NationPart getJailPart() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getJailPart();
        }
        return NationPart.EMPTY_PART;
    }
    
    NationPart getLawsPart() {
        if (this.m_justiceHandler != null) {
            return this.m_justiceHandler.getLawsPart();
        }
        return NationPart.EMPTY_PART;
    }
    
    public void registerJusticeEventHandler(final NationJusticeEventHandler handler) {
        if (this.m_justiceHandler == null) {
            return;
        }
        this.m_justiceHandler.registerEventHandler(handler);
    }
    
    public void unregisterJusticeEventHandler(final NationJusticeEventHandler handler) {
        if (this.m_justiceHandler == null) {
            return;
        }
        this.m_justiceHandler.unregisterEventHandler(handler);
    }
    
    public void registerPoliticEventHandler(final NationPoliticEventHandler handler) {
        if (this.m_politicHandler == null) {
            return;
        }
        this.m_politicHandler.registerEventHandler(handler);
    }
    
    public void unregisterPoliticEventHandler(final NationPoliticEventHandler handler) {
        if (this.m_politicHandler == null) {
            return;
        }
        this.m_politicHandler.unregisterEventHandler(handler);
    }
    
    public void initializeVoteData(final GameIntervalConst voteDuration, final GameIntervalConst voteFrequency) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.initializeVoteData(voteDuration, voteFrequency);
        }
    }
    
    public void registerVoteState(final GameDateConst voteStartDate, final GameIntervalConst voteDuration, final boolean voteRunning) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.registerVoteState(voteStartDate, voteDuration, voteRunning);
        }
    }
    
    public boolean askForVoteStart() {
        return this.m_politicHandler != null && this.m_politicHandler.requestVoteStart();
    }
    
    public boolean askForVoteEnd() {
        return this.m_politicHandler != null && this.m_politicHandler.requestVoteEnd();
    }
    
    public void initVoteStartDate(final GameDateConst voteStartDate) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.setVoteStartDate(voteStartDate);
        }
    }
    
    public boolean isVoteRunning() {
        return this.m_politicHandler != null && this.m_politicHandler.isVoteRunning();
    }
    
    public boolean isCandidateForbidden(final long citizenId) {
        return this.m_politicHandler != null && this.m_politicHandler.isCandidateForbidden(citizenId);
    }
    
    public GameDateConst getVoteStartDate() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getVoteStartDate();
        }
        return null;
    }
    
    public GameIntervalConst getVoteDuration() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getVoteDuration();
        }
        return null;
    }
    
    public GameIntervalConst getVoteFrequency() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getVoteFrequency();
        }
        return null;
    }
    
    public boolean isCandidate(final long citizenId) {
        return this.m_politicHandler != null && this.m_politicHandler.isCandidate(citizenId);
    }
    
    public CandidateInfo getCandidateInfo(final long characterId) {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getCandidateInfo(characterId);
        }
        return null;
    }
    
    public TLongObjectIterator<CandidateInfo> getCandidateIterator() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getCandidatesIterator();
        }
        return NationPoliticHandler.EMPTY_CANDIDATE_ITERATOR;
    }
    
    public int getNbCandidates() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getNbCandidates();
        }
        return 0;
    }
    
    public int getNbCandidateBallots() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getNbCandidateBallots();
        }
        return 0;
    }
    
    public void getCandidates(final int offset, final int needed, final ArrayList<CandidateInfo> listToFill) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.getCandidates(offset, needed, listToFill);
        }
    }
    
    public void setCandidates(final ArrayList<CandidateInfo> candidates) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.setCandidates(candidates);
        }
    }
    
    public void setCandidatesStats(final int nbCandidates, final int nbBallots) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.setCandidatesStats(nbCandidates, nbBallots);
        }
    }
    
    public void requestRegisterCandidate(final NationCandidateRegistrationRequest request) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestCandidateRegistration(request);
        }
    }
    
    public void requestRegisterCandidate(final long characterId, final String slogan) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestCandidateRegistration(characterId, slogan);
        }
    }
    
    public void requestRegisterCandidate(final long citizenId, final CandidateInfo candidate) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestCandidateRegistration(citizenId, candidate);
        }
    }
    
    public void registerCandidate(final CandidateInfo info) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.registerCandidate(info.getId(), info);
        }
    }
    
    public void requestGovernmentNominationResult(final NationGovernmentNominationConfirmationResult request) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestGovernmentNominationResult(request);
        }
    }
    
    public void requestGovernmentNominationResult(final long characterId, final byte result, final long rankId) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestGovernmentNominationResult(characterId, result, rankId);
        }
    }
    
    public void requestGovernmentRevoke(final NationRank requesterRank, final NationRank requestedRank, final RevokeReason reason) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestGovernmentRevoke(requesterRank, requestedRank, reason);
        }
    }
    
    public void requestGovernmentForceElection(final long characterId, final NationRank rank) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestGovernmentForceElection(characterId, rank);
        }
    }
    
    public void buildElectionHistory(final Nation nation) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.buildElectionHistory(nation);
        }
    }
    
    public NationElectionHistory getElectionHistory() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getElectionHistory() : NationPoliticHandler.EMPTY_ELECTION_HISTORY;
    }
    
    public NationGovernment getGovernment() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getGovernment() : null;
    }
    
    public NationProtectorInfoManager getProtectorInfoManager() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getProtectorInfoManager() : null;
    }
    
    public NationDiplomacyManager getDiplomacyManager() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getDiplomacyManager() : null;
    }
    
    public NationSurveyHandler getSurveyHandler() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getSurveyHandler() : null;
    }
    
    public void askDesistCandidate(final long citizenId) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestDesistCandidate(citizenId);
        }
    }
    
    public void askRevalidateCandidate(final long citizenId) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestRevalidateCandidate(citizenId);
        }
    }
    
    public void requestCitizenVote(final long citizenId, final long candidateId) {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.requestCitizenVote(citizenId, candidateId);
        }
    }
    
    public int getCandidateRegistrationFee() {
        if (this.m_politicHandler != null) {
            return this.m_politicHandler.getCandidateRegistrationFee();
        }
        return Integer.MAX_VALUE;
    }
    
    public void clearCandidates() {
        if (this.m_politicHandler != null) {
            this.m_politicHandler.clear();
        }
    }
    
    NationPart getGovernorBookPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getGovernorBookPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getGovernmentFullPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getGovernmentFullPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getEconomyPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getEconomyPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getEconomyBddPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getEconomyBddPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getProtectorInfoPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getProtectorInfoPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getSurveyPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getSurveyPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getDiplomacyPart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getDiplomacyPart() : NationPart.EMPTY_PART;
    }
    
    NationPart getForbiddenCandidatePart() {
        return (this.m_politicHandler != null) ? this.m_politicHandler.getForbiddenCandidatesPart() : NationPart.EMPTY_PART;
    }
    
    public int getCitizenCount() {
        return this.m_membersHandler.getCitizenCount();
    }
    
    public boolean hasCitizen(final Citizen citizen) {
        return this.m_membersHandler != null && this.m_membersHandler.hasCitizen(citizen);
    }
    
    public void registerMembersEventHandler(final NationMembersEventHandler handler) {
        if (this.m_membersHandler == null) {
            return;
        }
        this.m_membersHandler.registerEventHandler(handler);
    }
    
    public void unregisterMembersEventHandler(final NationMembersEventHandler handler) {
        if (this.m_membersHandler == null) {
            return;
        }
        this.m_membersHandler.unregisterEventHandler(handler);
    }
    
    public void onMemberDisconnection(final long characterId) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.onDisconnection(characterId);
        }
    }
    
    @Nullable
    public Citizen getCitizen(final long citizenId) {
        if (this.m_membersHandler == null) {
            return null;
        }
        return this.m_membersHandler.getCitizen(citizenId);
    }
    
    public void registerCitizen(@NotNull final Citizen citizen) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.registerCitizen(citizen);
        }
    }
    
    public void requestAddCitizen(@NotNull final Citizen character) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.requestAddCitizen(character);
        }
    }
    
    public void requestAddCitizen(final long citizenId) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.requestAddCitizen(citizenId);
        }
    }
    
    public void requestAddNationJob(final long citizenId, final NationJob job) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.requestAddNationJob(citizenId, job);
        }
    }
    
    public void requestRemoveNationJob(final long citizenId, final NationJob job) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.requestRemoveNationJob(citizenId, job);
        }
    }
    
    public boolean unregisterCitizen(@NotNull final Citizen citizen) {
        if (this.m_membersHandler != null) {
            return this.m_membersHandler.unregisterCitizen(citizen);
        }
        throw new UnsupportedOperationException();
    }
    
    public void addCitizenFromSerializedPart(@NotNull final Citizen character) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.addCitizen(character);
        }
    }
    
    public void executeForEachCitizen(final TObjectProcedure<Citizen> procedure) {
        if (this.m_membersHandler != null) {
            this.m_membersHandler.executeForEachCitizen(procedure);
        }
    }
    
    public void registerBuffEventHandler(final NationBuffEventHandler handler) {
        if (this.m_buffsHandler == null) {
            return;
        }
        this.m_buffsHandler.registerEventHandler(handler);
    }
    
    public void unregisterBuffEventHandler(final NationBuffEventHandler handler) {
        if (this.m_buffsHandler == null) {
            return;
        }
        this.m_buffsHandler.unregisterEventHandler(handler);
    }
    
    public void requestAddProtectorBuffs(final int protectorId, final int[] buffsId) {
        if (this.m_buffsHandler == null) {
            return;
        }
        this.m_buffsHandler.requestAddProtectorBuffs(protectorId, buffsId);
    }
    
    public void requestRemoveProtectorBuffs(final int protectorId, final int[] buffsId) {
        if (this.m_buffsHandler == null) {
            return;
        }
        this.m_buffsHandler.requestRemoveProtectorBuffs(protectorId, buffsId);
    }
    
    public void requestClearProtectorBuffs(final int protectorId) {
        if (this.m_buffsHandler == null) {
            return;
        }
        this.m_buffsHandler.requestClearProtectorBuffs(protectorId);
    }
    
    public void requestReplaceProtectorBuffs(final int protectorId, final int[] buffsId) {
        if (this.m_buffsHandler == null) {
            return;
        }
        this.m_buffsHandler.requestReplaceProtectorBuffs(protectorId, buffsId);
    }
    
    @Nullable
    public IntArray getProtectorBuffs(final int protectorId) {
        if (this.m_buffsHandler == null) {
            return null;
        }
        return this.m_buffsHandler.getProtectorBuffs(protectorId);
    }
    
    @Nullable
    public IntArray getAllProtectorsBuffs() {
        if (this.m_buffsHandler == null) {
            return null;
        }
        return this.m_buffsHandler.getAllProtectorsBuffs();
    }
    
    public void onProtectorAcquired(final int protectorId) {
        if (this.m_politicHandler == null) {
            return;
        }
        this.m_politicHandler.onProtectorAcquired(protectorId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)Nation.class);
        VOID_NATION = new Nation(0, NullHandlersFactory.getInstance());
        NationManager.INSTANCE.registerNation(Nation.VOID_NATION);
        Nation.m_handlersFactory = null;
    }
    
    private class IdentificationPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            Nation.this.setNationId(buffer.getInt());
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            buffer.putInt(Nation.this.getNationId());
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
    }
}
