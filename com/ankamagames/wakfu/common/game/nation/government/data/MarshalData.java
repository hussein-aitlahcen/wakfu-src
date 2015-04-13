package com.ankamagames.wakfu.common.game.nation.government.data;

import com.ankamagames.wakfu.common.rawData.*;

public class MarshalData extends GovernmentData implements SpeechData
{
    private String m_speech;
    
    MarshalData() {
        super();
        this.m_speech = "";
    }
    
    @Override
    public String getSpeech() {
        return this.m_speech;
    }
    
    @Override
    public void setSpeech(final String speech) {
        this.m_speech = speech;
    }
    
    @Override
    public void toRaw(final RawNationGovernmentData raw) {
        raw.speech = this.m_speech;
    }
    
    @Override
    public void fromRaw(final RawNationGovernmentData raw) {
        this.m_speech = raw.speech;
    }
}
