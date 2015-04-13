package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class IsOnOriginalControllerSpecificArea extends IsOnSpecificEffectArea
{
    public IsOnOriginalControllerSpecificArea(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected boolean isValidForSpecificCriteria(final BasicEffectArea area, final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (area == null) {
            return false;
        }
        if (!(area instanceof CriterionUser)) {
            return false;
        }
        final EffectUser owner = area.getOwner();
        if (owner == null) {
            return false;
        }
        final CriterionUser character = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        return character.getOriginalControllerId() == owner.getId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA;
    }
}
