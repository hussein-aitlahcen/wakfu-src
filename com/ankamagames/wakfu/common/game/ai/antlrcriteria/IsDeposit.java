package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class IsDeposit extends IsArea
{
    public IsDeposit(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    EffectAreaType getAreaType() {
        return EffectAreaType.ENUTROF_DEPOSIT;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_DEPOSIT;
    }
}
