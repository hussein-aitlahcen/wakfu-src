package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class NbGlyphs extends NbAreas
{
    public NbGlyphs(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected int getAreaType() {
        return EffectAreaType.GLYPH.getTypeId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.NB_ALL_AREAS;
    }
}
