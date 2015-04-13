package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public final class NbXelorDials extends NbAreas
{
    public NbXelorDials(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected int getAreaType() {
        return EffectAreaType.FAKE_FIGHTER.getTypeId();
    }
    
    @Override
    protected boolean isConcernedArea(final BasicEffectArea area) {
        return area.getType() == this.getAreaType() && ((AbstractFakeFighterEffectArea)area).getUserDefinedId() == 1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_XELOR_DIALS_COUNT;
    }
}
