package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;

public class CandidateInfoFieldProvider extends ImmutableFieldProvider
{
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String SLOGAN_FIELD = "slogan";
    public static final String BALLOT_COUNT_FIELD = "ballotCount";
    public static final String GUILD_NAME_FIELD = "guildName";
    public static final String RANK_DESCRIPTION_FIELD = "rankDescription";
    public static final String WAKFU_VALUE_FIELD = "wakfuValue";
    public static final String WAKFU_STASIS_DESCRIPTION_FIELD = "wakfuStasisDescription";
    public static final String WAKFU_STASIS_COLOR_FIELD = "wakfuStasisColor";
    public static final String IS_ENABLED_FIELD = "isEnabled";
    public static final String ACTOR_DESCRIPTION_LIBRARY_FIELD = "actorDescriptorLibrary";
    public static final String IS_GOVERNOR_FIELD = "isGovernor";
    static final String[] COMMON_FIELDS;
    static final String[] CANDIDATE_FIELDS;
    private static final String[] LOCAL_ALL_FIELDS;
    protected static final Logger m_logger;
    private AbstractGovernmentInfo m_candidateInfo;
    private CharacterActor m_characterActor;
    private static final int WAKFU_TEST_VALUE = 10;
    
    public CandidateInfoFieldProvider(final AbstractGovernmentInfo candidateInfo) {
        super();
        this.m_candidateInfo = candidateInfo;
    }
    
    public AbstractGovernmentInfo getCandidateInfo() {
        return this.m_candidateInfo;
    }
    
    public void clear() {
        this.m_characterActor.getCharacterInfo().release();
        this.m_characterActor = null;
    }
    
    @Override
    public String[] getFields() {
        return CandidateInfoFieldProvider.LOCAL_ALL_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_candidateInfo.getName();
        }
        if (fieldName.equals("id")) {
            return this.m_candidateInfo.getId();
        }
        if (fieldName.equals("slogan")) {
            return ((CandidateInfo)this.m_candidateInfo).getSlogan();
        }
        if (fieldName.equals("ballotCount")) {
            final Nation nation = NationDisplayer.getInstance().getNation();
            final int totalBallotsCount = nation.getElectionHistory().getNbCandidateBallots();
            final short count = ((CandidateInfo)this.m_candidateInfo).getBallotCount();
            return (totalBallotsCount == 0) ? null : WakfuTranslator.getInstance().getString("election.ballotDescription", count * 100 / totalBallotsCount, count);
        }
        if (fieldName.equals("wakfuStasisDescription")) {
            final float v = Math.abs(this.m_candidateInfo.getWakfuGauge());
            return String.format(" : %d ", (int)(v * 100.0f)) + "%";
        }
        if (fieldName.equals("wakfuStasisColor")) {
            return (this.m_candidateInfo.getWakfuGauge() < 0.0f) ? WakfuClientConstants.STASIS_COLOR : WakfuClientConstants.WAKFU_COLOR;
        }
        if (fieldName.equals("wakfuValue")) {
            return this.m_candidateInfo.getWakfuGauge();
        }
        if (fieldName.equals("rankDescription")) {
            return WakfuTranslator.getInstance().getString(CitizenRankManager.getInstance().getRankFromCitizenScore(this.m_candidateInfo.getCitizenScore()).getTranslationKey());
        }
        if (fieldName.equals("guildName")) {
            return (this.m_candidateInfo.getGuildId() == 0L) ? null : this.m_candidateInfo.getGuildName();
        }
        if (fieldName.equals("actorDescriptorLibrary")) {
            if (this.m_characterActor == null) {
                this.m_characterActor = ActorUtils.getActorFromCharacterData(this.m_candidateInfo);
            }
            return this.m_characterActor;
        }
        if (fieldName.equals("isGovernor")) {
            return false;
        }
        if (fieldName.equals("isEnabled")) {
            return this.m_candidateInfo != null && !((CandidateInfo)this.m_candidateInfo).isWithDraw();
        }
        return null;
    }
    
    static {
        COMMON_FIELDS = new String[] { "id", "name", "guildName", "rankDescription", "wakfuValue", "wakfuStasisDescription", "wakfuStasisColor", "actorDescriptorLibrary", "isGovernor" };
        CANDIDATE_FIELDS = new String[] { "slogan", "ballotCount", "isEnabled" };
        LOCAL_ALL_FIELDS = new String[CandidateInfoFieldProvider.CANDIDATE_FIELDS.length + CandidateInfoFieldProvider.COMMON_FIELDS.length];
        System.arraycopy(CandidateInfoFieldProvider.CANDIDATE_FIELDS, 0, CandidateInfoFieldProvider.LOCAL_ALL_FIELDS, 0, CandidateInfoFieldProvider.CANDIDATE_FIELDS.length);
        System.arraycopy(CandidateInfoFieldProvider.COMMON_FIELDS, 0, CandidateInfoFieldProvider.LOCAL_ALL_FIELDS, CandidateInfoFieldProvider.CANDIDATE_FIELDS.length, CandidateInfoFieldProvider.COMMON_FIELDS.length);
        m_logger = Logger.getLogger((Class)CandidateInfoFieldProvider.class);
    }
}
