package com.ankamagames.wakfu.client.ui.protocol.message.nation;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.nation.*;

public class UICandidateInfoMessage extends UIMessage
{
    private CandidateInfoFieldProvider m_candidateInfo;
    
    public CandidateInfoFieldProvider getCandidateInfo() {
        return this.m_candidateInfo;
    }
    
    public void setCandidateInfo(final CandidateInfoFieldProvider candidateInfo) {
        this.m_candidateInfo = candidateInfo;
    }
}
