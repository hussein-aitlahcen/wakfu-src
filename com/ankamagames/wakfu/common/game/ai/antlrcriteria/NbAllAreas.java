package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class NbAllAreas extends NbAreas
{
    public NbAllAreas(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected int getAreaType() {
        return -1;
    }
    
    @Override
    protected boolean isConcernedArea(final BasicEffectArea area) {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.NB_ALL_AREAS;
    }
}
