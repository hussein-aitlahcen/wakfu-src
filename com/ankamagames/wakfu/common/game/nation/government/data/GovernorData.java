package com.ankamagames.wakfu.common.game.nation.government.data;

import com.ankamagames.wakfu.common.rawData.*;

public class GovernorData extends GovernmentData implements SpeechData
{
    private String m_speech;
    private short m_titleId;
    private int m_nbMandate;
    
    GovernorData() {
        super();
        this.m_speech = "";
        this.m_titleId = -1;
        this.m_nbMandate = 0;
    }
    
    @Override
    public String getSpeech() {
        return this.m_speech;
    }
    
    @Override
    public void setSpeech(final String speech) {
        this.m_speech = speech;
    }
    
    public short getTitleId() {
        return this.m_titleId;
    }
    
    public void setTitleId(final short titleId) {
        this.m_titleId = titleId;
    }
    
    public int getNbMandate() {
        return this.m_nbMandate;
    }
    
    public void incMandates() {
        ++this.m_nbMandate;
    }
    
    @Override
    public void toRaw(final RawNationGovernmentData raw) {
        raw.speech = this.m_speech;
        raw.governor = new RawNationGovernmentData.Governor();
        raw.governor.titleId = this.m_titleId;
        raw.governor.nbMandate = this.m_nbMandate;
    }
    
    @Override
    public void fromRaw(final RawNationGovernmentData raw) {
        this.m_speech = raw.speech;
        this.m_titleId = raw.governor.titleId;
        this.m_nbMandate = raw.governor.nbMandate;
    }
}
