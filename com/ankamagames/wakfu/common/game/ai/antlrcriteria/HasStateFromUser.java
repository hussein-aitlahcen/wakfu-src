package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasStateFromUser extends HasState
{
    public HasStateFromUser(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        CriterionUser targetUser = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        final CriterionUser casterUser = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetUser == null && criterionContent instanceof PlayerTriggered) {
            targetUser = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        if (targetUser == null) {
            return -1;
        }
        boolean hasState;
        if (this.m_stateLevel == null) {
            hasState = targetUser.hasStateFromUser(this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext), casterUser);
        }
        else {
            hasState = targetUser.hasStateFromUser(this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext), this.m_stateLevel.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext), casterUser);
        }
        if (hasState) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_STATE_FROM_USER;
    }
}
