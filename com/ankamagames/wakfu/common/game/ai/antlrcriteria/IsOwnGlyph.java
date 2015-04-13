package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class IsOwnGlyph extends IsOwnArea
{
    public IsOwnGlyph(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    EffectAreaType getAreaType() {
        return EffectAreaType.GLYPH;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_OWN_GLYPH;
    }
}
