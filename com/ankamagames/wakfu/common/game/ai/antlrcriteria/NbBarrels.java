package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class NbBarrels extends NbAreas
{
    public NbBarrels(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected int getAreaType() {
        return EffectAreaType.BARREL.getTypeId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETBARRELAMOUNT;
    }
}
