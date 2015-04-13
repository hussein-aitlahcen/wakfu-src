package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasStateFromLevel extends HasState
{
    public HasStateFromLevel(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        CriterionUser targetCharacter = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null && criterionContent instanceof PlayerTriggered) {
            targetCharacter = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        if (targetCharacter == null) {
            return -1;
        }
        boolean hasState;
        if (this.m_stateLevel == null) {
            hasState = targetCharacter.hasStateFromLevel(this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext), 0L);
        }
        else {
            hasState = targetCharacter.hasStateFromLevel(this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext), this.m_stateLevel.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        }
        if (hasState) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_STATE_FROM_LEVEL;
    }
}
