package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public final class GetOwnTeamStateCountInRange extends GetStateCountInRange
{
    public GetOwnTeamStateCountInRange(final List<ParserObject> args) {
        super(args);
        this.m_ownTeam = true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_OWN_TEAM_STATE_COUNT_IN_RANGE;
    }
}
