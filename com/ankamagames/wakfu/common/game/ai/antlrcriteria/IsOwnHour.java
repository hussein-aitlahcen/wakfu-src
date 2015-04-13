package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class IsOwnHour extends IsOwnArea
{
    public IsOwnHour(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    EffectAreaType getAreaType() {
        return EffectAreaType.HOUR;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_HOUR;
    }
}
