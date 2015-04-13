package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class NbBeacons extends NbAreas
{
    public NbBeacons(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected int getAreaType() {
        return EffectAreaType.BEACON.getTypeId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETBEACONAMOUNT;
    }
}
