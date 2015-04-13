package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class IsTunnel extends IsArea
{
    public IsTunnel(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    EffectAreaType getAreaType() {
        return EffectAreaType.SPELL_TUNNEL;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_TUNNEL;
    }
}
