package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public class GetSpecificEffectAreaCountInRange extends GetEffectAreaCountInRange
{
    public GetSpecificEffectAreaCountInRange(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected boolean isConcernedArea(final long id, final BasicEffectArea effectArea) {
        return effectArea.getBaseId() != id;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE;
    }
}
