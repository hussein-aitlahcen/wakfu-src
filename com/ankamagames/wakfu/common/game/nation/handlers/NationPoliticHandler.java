package com.ankamagames.wakfu.common.game.nation.handlers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.diplomacy.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.impl.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.survey.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.data.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public abstract class NationPoliticHandler extends NationHandler<NationPoliticEventHandler> implements NationProtectorEventHandler, NationPoliticEventHandler
{
    protected static final Logger m_logger;
    public static final TLongObjectIterator<CandidateInfo> EMPTY_CANDIDATE_ITERATOR;
    public static final NationElectionHistory EMPTY_ELECTION_HISTORY;
    private final BallotCounterProcedure BALLOT_COUNTER_PROCEDURE;
    protected boolean m_bVoteRunning;
    protected final GameDate m_voteStartDate;
    private final GameDate m_voteEndDate;
    protected final GameInterval m_voteDuration;
    protected final GameInterval m_voteFrequency;
    protected final TLongObjectHashMap<CandidateInfo> m_candidates;
    protected final NationElectionHistory m_electionHistory;
    protected final NationSurveyHandler m_surveyHandler;
    protected final NationGovernment m_nationGovernment;
    protected final NationProtectorInfoManager m_protectorManager;
    protected final NationDiplomacyManager m_diplomacyManager;
    protected final NationForbiddenCandidateHandler m_forbiddenCandidateHandler;
    private final List<NationPoliticEventHandler> m_politicEventHandlers;
    @Nullable
    protected VoteUpdatePart m_voteUpdatePart;
    private final CandidatesListPart m_candidatesListPart;
    private final ElectionHistoryPart m_electionHistoryPart;
    private final GovernorBookPart m_governorBookPart;
    private final GovernmentFullPart m_governmentFullPart;
    private final EconomyPart m_economyPart;
    private final EconomyPart m_economyBddPart;
    private final ProtectorInfoPart m_protectorInfoPart;
    private final SurveyPart m_surveyPart;
    private final DiplomacyPart m_diplomacyPart;
    private final ForbiddenCandidatesPart m_forbiddenCandidatesPart;
    protected VoteDatePart m_voteDatePart;
    
    protected NationPoliticHandler(final Nation nation) {
        super(nation);
        this.BALLOT_COUNTER_PROCEDURE = new BallotCounterProcedure();
        this.m_bVoteRunning = false;
        this.m_voteStartDate = GameDate.getNullDate();
        this.m_voteEndDate = GameDate.getNullDate();
        this.m_voteDuration = new GameInterval(GameInterval.EMPTY_INTERVAL);
        this.m_voteFrequency = new GameInterval(GameInterval.EMPTY_INTERVAL);
        this.m_candidates = new TLongObjectHashMap<CandidateInfo>();
        this.m_politicEventHandlers = new ArrayList<NationPoliticEventHandler>();
        this.m_voteUpdatePart = null;
        this.m_candidatesListPart = new CandidatesListPart();
        this.m_electionHistoryPart = new ElectionHistoryPart();
        this.m_governorBookPart = new GovernorBookPart();
        this.m_governmentFullPart = new GovernmentFullPart();
        this.m_economyPart = new EconomyPart(true);
        this.m_economyBddPart = new EconomyPart(false);
        this.m_protectorInfoPart = new ProtectorInfoPart();
        this.m_surveyPart = new SurveyPart();
        this.m_diplomacyPart = new DiplomacyPart();
        this.m_forbiddenCandidatesPart = new ForbiddenCandidatesPart();
        this.m_voteDatePart = null;
        this.m_electionHistory = this.createHistory();
        this.m_nationGovernment = this.createGovernment();
        this.m_protectorManager = new NationProtectorInfoManager(this.getNation());
        this.m_diplomacyManager = this.createDiplomacyManager();
        this.m_surveyHandler = new NationSurveyHandler(this.getNation());
        this.m_forbiddenCandidateHandler = new NationForbiddenCandidateHandler(this);
    }
    
    protected NationDiplomacyManager createDiplomacyManager() {
        return new NationDiplomacyManager(this.getNation());
    }
    
    protected NationGovernment createGovernment() {
        return new NationGovernment(this.getNation());
    }
    
    protected NationElectionHistory createHistory() {
        return new NationElectionHistory();
    }
    
    @Override
    public void finishInitialization() {
    }
    
    @Override
    public void registerEventHandler(@NotNull final NationPoliticEventHandler handler) {
        this.m_politicEventHandlers.add(handler);
    }
    
    @Override
    public void unregisterEventHandler(final NationPoliticEventHandler handler) {
        this.m_politicEventHandlers.remove(handler);
    }
    
    public int getCandidateRegistrationFee() {
        return 3000;
    }
    
    public NationProtectorInfoManager getProtectorInfoManager() {
        return this.m_protectorManager;
    }
    
    public NationDiplomacyManager getDiplomacyManager() {
        return this.m_diplomacyManager;
    }
    
    public NationSurveyHandler getSurveyHandler() {
        return this.m_surveyHandler;
    }
    
    public abstract void requestCandidateRegistration(final long p0, final String p1);
    
    public abstract void requestCandidateRegistration(final long p0, final CandidateInfo p1);
    
    public void requestCandidateRegistration(final NationCandidateRegistrationRequest request) {
        throw new UnsupportedOperationException();
    }
    
    public abstract void requestCitizenVote(final long p0, final long p1);
    
    public void registerVoteState(final GameDateConst voteStartDate, final GameIntervalConst voteDuration, final boolean voteRunning) {
        throw new UnsupportedOperationException("Commande non impl\u00e9ment\u00e9e sur ce serveur ou ce client");
    }
    
    public boolean requestVoteStart() {
        throw new UnsupportedOperationException("Operation non authoris\u00e9e sur ce serveur ou ce client");
    }
    
    public boolean requestVoteEnd() {
        throw new UnsupportedOperationException("Operation non authoris\u00e9e sur ce serveur ou ce client");
    }
    
    public boolean requestDesistCandidate(final long citizenId) {
        throw new UnsupportedOperationException("Operation non authoris\u00e9e sur ce serveur ou ce client");
    }
    
    public boolean requestRevalidateCandidate(final long citizenId) {
        throw new UnsupportedOperationException("Operation non authoris\u00e9e sur ce serveur ou ce client");
    }
    
    public void requestGovernmentNominationResult(final NationGovernmentNominationConfirmationResult request) {
        throw new UnsupportedOperationException();
    }
    
    public void requestGovernmentNominationResult(final long characterId, final byte result, final long rankId) {
        throw new UnsupportedOperationException();
    }
    
    public void requestGovernmentRevoke(final NationRank requesterRank, final NationRank requestedRank, final RevokeReason reason) {
        throw new UnsupportedOperationException();
    }
    
    public void requestGovernmentForceElection(final long characterId, final NationRank rank) {
        throw new UnsupportedOperationException();
    }
    
    public GameDateConst getVoteStartDate() {
        return this.m_voteStartDate;
    }
    
    public GameDateConst getVoteEndDate() {
        return this.m_voteEndDate;
    }
    
    public GameIntervalConst getVoteDuration() {
        return this.m_voteDuration;
    }
    
    public GameInterval getVoteFrequency() {
        return this.m_voteFrequency;
    }
    
    public void initializeVoteData(final GameIntervalConst voteDuration, final GameIntervalConst voteFrequency) {
        this.m_voteDuration.set(voteDuration);
        this.m_voteFrequency.set(voteFrequency);
    }
    
    public final boolean isVoteRunning() {
        return this.m_bVoteRunning;
    }
    
    public final boolean isCandidateForbidden(final long citizenId) {
        return this.m_forbiddenCandidateHandler.isCandidateForbidden(citizenId);
    }
    
    public boolean setVoteStartDate(@NotNull GameDateConst voteStartDate) {
        if (this.getNation() == Nation.VOID_NATION || this.getNation().getNationId() == 34) {
            this.m_voteStartDate.set(GameDate.NULL_DATE);
            return true;
        }
        if (voteStartDate.isNull()) {
            NationPoliticHandler.m_logger.info((Object)("[NATION] La date de d\u00e9but de vote pour la nation " + this.getNation() + " est d\u00e9finie \u00e0 null. Un nouveau vote va commencer d\u00e8s que possible."));
            voteStartDate = WakfuGameCalendar.getInstance().getDate();
        }
        this.m_voteStartDate.set(voteStartDate);
        this.m_voteEndDate.set(voteStartDate);
        this.m_voteEndDate.add(this.m_voteDuration);
        NationPoliticHandler.m_logger.debug((Object)("[NATION_DEBUG] Vote fix\u00e9e pour la nation " + this.getNation() + " du " + this.m_voteStartDate + " au " + this.m_voteEndDate));
        return !this.m_voteStartDate.isNull();
    }
    
    public final boolean registerCitizenVote(final long citizenId, final long candidateId) {
        final CandidateInfo candidateInfo = this.m_candidates.get(candidateId);
        if (candidateInfo == null) {
            return false;
        }
        final Citizen citizen = this.getNation().getCitizen(citizenId);
        if (citizen == null) {
            return false;
        }
        candidateInfo.setBallotCount((short)(candidateInfo.getBallotCount() + 1));
        citizen.getCitizenComportment().setVoteDate(WakfuGameCalendar.getInstance().getNewDate());
        citizen.getCitizenComportment().setGovernmentOpinion(GovernmentOpinion.UNKNOWN);
        for (int i = 0; i < this.m_politicEventHandlers.size(); ++i) {
            final NationPoliticEventHandler handler = this.m_politicEventHandlers.get(i);
            handler.onCitizenHasVoted(citizenId, candidateId);
        }
        return true;
    }
    
    public final void registerCandidate(final long characterId, final CandidateInfo infos) {
        NationPoliticHandler.m_logger.info((Object)("[NATION] Enregistrement du character " + characterId + " (" + infos + ") en tant que candidat pour la nation " + this.getNation()));
        if (infos == null) {
            NationPoliticHandler.m_logger.error((Object)("[NATION] Tentative d'enregistrement d'un candidat null en tant que candidat. CharacterId : " + characterId + " Nation : " + this.getNation()), (Throwable)new IllegalArgumentException());
            return;
        }
        this.m_candidates.put(characterId, infos);
        for (int i = 0; i < this.m_politicEventHandlers.size(); ++i) {
            final NationPoliticEventHandler handler = this.m_politicEventHandlers.get(i);
            handler.onCandidateRegistered(characterId);
        }
    }
    
    public final boolean desistCandidate(final long citizenId) {
        final CandidateInfo info = this.m_candidates.get(citizenId);
        if (info == null) {
            NationPoliticHandler.m_logger.info((Object)("[NATION] On demande le d\u00e9sistement du candidat " + citizenId + " mais il n'est pas enregistr\u00e9 dans la nation " + this.getNation()));
            return false;
        }
        info.setWitDraw(true);
        for (int i = 0; i < this.m_politicEventHandlers.size(); ++i) {
            final NationPoliticEventHandler handler = this.m_politicEventHandlers.get(i);
            handler.onCandidateWithdraw(citizenId);
        }
        return true;
    }
    
    public final boolean revalidateCandidate(final long citizenId) {
        final CandidateInfo info = this.m_candidates.get(citizenId);
        if (info == null) {
            NationPoliticHandler.m_logger.info((Object)("[NATION] On demande la r\u00e9\u00e9ligibilit\u00e9 du candidat " + citizenId + " mais il n'est pas enregistr\u00e9 dans la nation " + this.getNation()));
            return false;
        }
        info.setWitDraw(false);
        for (int i = 0; i < this.m_politicEventHandlers.size(); ++i) {
            final NationPoliticEventHandler handler = this.m_politicEventHandlers.get(i);
            handler.onCandidateRevalidate(citizenId);
        }
        return true;
    }
    
    public boolean isCandidate(final long characterId) {
        return this.m_candidates.containsKey(characterId);
    }
    
    public CandidateInfo getCandidateInfo(final long characterId) {
        return this.m_candidates.get(characterId);
    }
    
    protected boolean hasCandidates() {
        return !this.m_candidates.isEmpty();
    }
    
    protected void clearCandidates() {
        this.m_candidates.clear();
    }
    
    public TLongObjectIterator<CandidateInfo> getCandidatesIterator() {
        return this.m_candidates.iterator();
    }
    
    public int getNbCandidates() {
        return this.m_candidates.size();
    }
    
    public int getNbCandidateBallots() {
        this.BALLOT_COUNTER_PROCEDURE.size = 0;
        this.m_candidates.forEachValue(this.BALLOT_COUNTER_PROCEDURE);
        return this.BALLOT_COUNTER_PROCEDURE.size;
    }
    
    public void getCandidates(final int offset, final int needed, final ArrayList<CandidateInfo> listToFill) {
        final int max = offset + Math.min(needed, this.m_candidates.size() - offset);
        final Object[] c = this.m_candidates.getValues();
        for (int i = offset; i < max; ++i) {
            listToFill.add((CandidateInfo)c[i]);
        }
    }
    
    public void setCandidates(final ArrayList<CandidateInfo> candidates) {
        throw new UnsupportedOperationException();
    }
    
    public void setCandidatesStats(final int nbCandidates, final int nbBallots) {
        throw new UnsupportedOperationException();
    }
    
    public void buildElectionHistory(final Nation nation) {
        this.m_electionHistory.clear();
        this.m_electionHistory.buildHistory(nation);
    }
    
    public NationElectionHistory getElectionHistory() {
        return this.m_electionHistory;
    }
    
    public NationGovernment getGovernment() {
        return this.m_nationGovernment;
    }
    
    @Override
    public void onProtectorAcquired(final int protectorId) {
    }
    
    public NationPart getVoteDatePart() {
        return this.m_voteDatePart;
    }
    
    public NationPart getCandidatesListPart() {
        return this.m_candidatesListPart;
    }
    
    public NationPart getElectionHistoryPart() {
        return this.m_electionHistoryPart;
    }
    
    @NotNull
    public NationPart getVoteUpdatePart() {
        if (this.m_voteUpdatePart == null) {
            this.m_voteUpdatePart = new VoteUpdatePart();
        }
        return this.m_voteUpdatePart;
    }
    
    public NationPart getGovernorBookPart() {
        return this.m_governorBookPart;
    }
    
    public NationPart getGovernmentFullPart() {
        return this.m_governmentFullPart;
    }
    
    public NationPart getEconomyPart() {
        return this.m_economyPart;
    }
    
    public NationPart getEconomyBddPart() {
        return this.m_economyBddPart;
    }
    
    public NationPart getProtectorInfoPart() {
        return this.m_protectorInfoPart;
    }
    
    public NationPart getSurveyPart() {
        return this.m_surveyPart;
    }
    
    public DiplomacyPart getDiplomacyPart() {
        return this.m_diplomacyPart;
    }
    
    public NationPart getForbiddenCandidatesPart() {
        return this.m_forbiddenCandidatesPart;
    }
    
    public void clear() {
        this.clearCandidates();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationPoliticHandler.class);
        EMPTY_CANDIDATE_ITERATOR = new TLongObjectHashMap<CandidateInfo>().iterator();
        EMPTY_ELECTION_HISTORY = new NationElectionHistory();
    }
    
    private static class BallotCounterProcedure implements TObjectProcedure<CandidateInfo>
    {
        public int size;
        
        @Override
        public boolean execute(final CandidateInfo object) {
            this.size += object.getBallotCount();
            return true;
        }
    }
    
    public abstract class VoteDatePart extends NationPart
    {
        protected GameDateConst m_partStartDate;
        protected GameIntervalConst m_partDuration;
        protected GameIntervalConst m_partFrequency;
        protected boolean m_partVoteRunning;
        
        protected abstract void onDataChanged();
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            buffer.putLong((NationPoliticHandler.this.m_voteDuration != null) ? NationPoliticHandler.this.m_voteDuration.toSeconds() : GameInterval.EMPTY_INTERVAL.toSeconds());
            buffer.putLong((NationPoliticHandler.this.m_voteFrequency != null) ? NationPoliticHandler.this.m_voteFrequency.toSeconds() : GameInterval.EMPTY_INTERVAL.toSeconds());
            buffer.putLong((NationPoliticHandler.this.m_voteStartDate != null) ? NationPoliticHandler.this.m_voteStartDate.toLong() : 0L);
            buffer.put((byte)(NationPoliticHandler.this.m_bVoteRunning ? 1 : 0));
        }
        
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            try {
                this.m_partDuration = GameInterval.fromSeconds(buffer.getLong());
                this.m_partFrequency = GameInterval.fromSeconds(buffer.getLong());
                this.m_partStartDate = GameDate.fromLong(buffer.getLong());
                this.m_partVoteRunning = (buffer.get() == 1);
                this.onDataChanged();
            }
            catch (Exception e) {
                NationPoliticHandler.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        
        @Override
        public int serializedSize() {
            return 25;
        }
    }
    
    public class VoteUpdatePart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            for (int i = 0, size = buffer.getInt(); i < size; ++i) {
                final long citizenId = buffer.getLong();
                final long candidateId = buffer.getLong();
                NationPoliticHandler.this.registerCitizenVote(citizenId, candidateId);
            }
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            throw new UnsupportedOperationException("[NATION] Pas de s\u00e9rialization de la part VoteUpdatePart de base, cel\u00e0 ne se fait que dans le global");
        }
        
        @Override
        public int serializedSize() {
            return 0;
        }
    }
    
    public class CandidatesListPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationPoliticHandler.this.m_candidates.clear();
            int size = buffer.getInt();
            NationPoliticHandler.m_logger.info((Object)("S\u00e9rialisation de " + NationPoliticHandler.this.m_candidates.size() + " candidats (avec le nombre de votes) pour la nation " + NationPoliticHandler.this.getNation()));
            while (size-- > 0) {
                final CandidateInfo info = CandidateInfo.fromBuild(buffer);
                NationPoliticHandler.this.m_candidates.put(info.getId(), info);
            }
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            NationPoliticHandler.m_logger.info((Object)("S\u00e9rialisation des " + NationPoliticHandler.this.m_candidates.size() + " candidats (avec le nombre de votes) de la nation " + NationPoliticHandler.this.getNation()));
            buffer.putInt(NationPoliticHandler.this.m_candidates.size());
            final TLongObjectIterator<CandidateInfo> it = NationPoliticHandler.this.m_candidates.iterator();
            while (it.hasNext()) {
                it.advance();
                final CandidateInfo candidateInfo = it.value();
                candidateInfo.serialize(buffer, true);
            }
        }
        
        @Override
        public int serializedSize() {
            int size = 4;
            final TLongObjectIterator<CandidateInfo> it = NationPoliticHandler.this.m_candidates.iterator();
            while (it.hasNext()) {
                it.advance();
                size += it.value().serializedSize();
            }
            return size;
        }
    }
    
    public class ElectionHistoryPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationPoliticHandler.this.m_electionHistory.clear();
            NationPoliticHandler.this.m_electionHistory.unserialize(buffer);
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            NationPoliticHandler.this.m_electionHistory.serialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return NationPoliticHandler.this.m_electionHistory.serializedSize();
        }
    }
    
    public class GovernorBookPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationPoliticHandler.this.m_nationGovernment.removeMember(NationRank.GOVERNOR);
            if (buffer.get() == 0) {
                return;
            }
            final GovernmentInfo governor = GovernmentInfo.fromBuild(buffer);
            NationPoliticHandler.this.m_nationGovernment.putMember(governor);
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            final GovernmentInfo governor = NationPoliticHandler.this.m_nationGovernment.getGovernor();
            if (governor == null) {
                buffer.put((byte)0);
                return;
            }
            buffer.put((byte)1);
            governor.serialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            final GovernmentInfo governor = NationPoliticHandler.this.m_nationGovernment.getGovernor();
            return 1 + ((governor != null) ? governor.serializedSize() : 0);
        }
    }
    
    public class GovernmentFullPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationPoliticHandler.this.m_nationGovernment.clear();
            NationPoliticHandler.this.m_nationGovernment.unserialize(buffer);
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            NationPoliticHandler.this.m_nationGovernment.serialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return NationPoliticHandler.this.m_nationGovernment.serializedSize();
        }
    }
    
    public class ForbiddenCandidatesPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationPoliticHandler.this.m_forbiddenCandidateHandler.unserialize(buffer);
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            NationPoliticHandler.this.m_forbiddenCandidateHandler.serialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return NationPoliticHandler.this.m_forbiddenCandidateHandler.serializedSize();
        }
    }
    
    public class EconomyPart extends RawNationPart<RawNationEconomy>
    {
        private final Boolean m_serializeTotalCash;
        
        private EconomyPart(final Boolean serializeTotalCash) {
            super();
            this.m_serializeTotalCash = serializeTotalCash;
        }
        
        @Override
        protected RawNationEconomy createNewRaw() {
            return new RawNationEconomy();
        }
        
        @Override
        protected void toRaw(final RawNationEconomy raw) {
            NationPoliticHandler.this.m_protectorManager.getEconomyHandler().toRaw(raw, this.m_serializeTotalCash);
        }
        
        @Override
        protected void fromRaw(final RawNationEconomy raw) {
            final NationEconomyHandler handler = NationPoliticHandler.this.m_protectorManager.getEconomyHandler();
            handler.clear();
            handler.fromRaw(raw);
        }
    }
    
    public class ProtectorInfoPart extends NationPart
    {
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
            NationPoliticHandler.this.m_protectorManager.clearProtectors();
            NationPoliticHandler.this.m_protectorManager.unserializeProtectors(buffer);
        }
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            NationPoliticHandler.this.m_protectorManager.serializeProtectors(buffer);
        }
        
        @Override
        public int serializedSize() {
            return NationPoliticHandler.this.m_protectorManager.serializedProtectorSize();
        }
    }
    
    public class SurveyPart extends RawNationPart<RawNationSurvey>
    {
        @Override
        protected RawNationSurvey createNewRaw() {
            return new RawNationSurvey();
        }
        
        @Override
        protected void toRaw(final RawNationSurvey raw) {
            NationPoliticHandler.this.m_surveyHandler.toRaw(raw);
        }
        
        @Override
        protected void fromRaw(final RawNationSurvey raw) {
            NationPoliticHandler.this.m_surveyHandler.clear();
            NationPoliticHandler.this.m_surveyHandler.fromRaw(raw);
        }
    }
    
    public class DiplomacyPart extends RawNationPart<RawNationDiplomacy>
    {
        @Override
        protected RawNationDiplomacy createNewRaw() {
            return new RawNationDiplomacy();
        }
        
        @Override
        protected void toRaw(final RawNationDiplomacy raw) {
            NationPoliticHandler.this.m_diplomacyManager.toRaw(raw);
        }
        
        @Override
        protected void fromRaw(final RawNationDiplomacy raw) {
            NationPoliticHandler.this.m_diplomacyManager.clear();
            NationPoliticHandler.this.m_diplomacyManager.fromRaw(raw);
        }
    }
}
