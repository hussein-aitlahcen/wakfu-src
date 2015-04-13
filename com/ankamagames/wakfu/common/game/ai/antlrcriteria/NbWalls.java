package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public class NbWalls extends NbAreas
{
    public NbWalls(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_WALL_AMOUNT;
    }
    
    @Override
    protected int getAreaType() {
        return EffectAreaType.WALL.getTypeId();
    }
}
