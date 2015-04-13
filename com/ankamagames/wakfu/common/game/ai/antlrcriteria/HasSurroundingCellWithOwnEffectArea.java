package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class HasSurroundingCellWithOwnEffectArea extends HasSurroundingCellWithEffectArea
{
    public HasSurroundingCellWithOwnEffectArea(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected boolean isConcernedArea(final int effectAreaTypeId, final int effectAreaSpecificId, final BasicEffectArea area, final EffectUser user) {
        return (area.getType() == effectAreaTypeId || area.getBaseId() == effectAreaSpecificId) && area.getOwner() == user;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA;
    }
}
