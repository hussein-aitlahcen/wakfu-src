package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class IsOwnBeacon extends IsOwnArea
{
    public IsOwnBeacon(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    EffectAreaType getAreaType() {
        return EffectAreaType.BEACON;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_OWN_BEACON;
    }
}
