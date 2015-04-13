package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.government.*;

public class GovernorHistoryFieldProvider extends CandidateInfoFieldProvider
{
    public GovernorHistoryFieldProvider(final AbstractGovernmentInfo candidateInfo) {
        super(candidateInfo);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isGovernor")) {
            return true;
        }
        return super.getFieldValue(fieldName);
    }
}
