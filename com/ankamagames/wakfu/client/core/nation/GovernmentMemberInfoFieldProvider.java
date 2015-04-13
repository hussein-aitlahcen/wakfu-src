package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.government.data.*;

public class GovernmentMemberInfoFieldProvider extends CandidateInfoFieldProvider
{
    public static final String SPEECH_FIELD = "speech";
    static final String[] LOCAL_ALL_FIELDS;
    static final String[] LOCAL_FIELDS;
    private String m_currentSpeech;
    
    public GovernmentMemberInfoFieldProvider(final GovernmentInfo governmentInfo) {
        super(governmentInfo);
        final SpeechData data = (SpeechData)governmentInfo.getData();
        this.m_currentSpeech = data.getSpeech();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("speech")) {
            final String temporarySpeech = NationDisplayer.getInstance().getTemporarySpeech();
            final String realSpeech = this.m_currentSpeech.isEmpty() ? null : this.m_currentSpeech;
            return (temporarySpeech != null) ? temporarySpeech : realSpeech;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public String[] getFields() {
        return GovernmentMemberInfoFieldProvider.LOCAL_ALL_FIELDS;
    }
    
    public void setCurrentSpeech(final String currentSpeech) {
        this.m_currentSpeech = currentSpeech;
    }
    
    static {
        LOCAL_FIELDS = new String[] { "speech" };
        LOCAL_ALL_FIELDS = new String[GovernmentMemberInfoFieldProvider.LOCAL_FIELDS.length + CandidateInfoFieldProvider.COMMON_FIELDS.length];
        System.arraycopy(GovernmentMemberInfoFieldProvider.LOCAL_FIELDS, 0, GovernmentMemberInfoFieldProvider.LOCAL_ALL_FIELDS, 0, GovernmentMemberInfoFieldProvider.LOCAL_FIELDS.length);
        System.arraycopy(CandidateInfoFieldProvider.COMMON_FIELDS, 0, GovernmentMemberInfoFieldProvider.LOCAL_ALL_FIELDS, GovernmentMemberInfoFieldProvider.LOCAL_FIELDS.length, CandidateInfoFieldProvider.COMMON_FIELDS.length);
    }
}
