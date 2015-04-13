package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class IsFecaGlyphCenter extends IsArea
{
    public IsFecaGlyphCenter(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    EffectAreaType getAreaType() {
        return EffectAreaType.FECA_GLYPH;
    }
    
    @Override
    protected boolean isConcernedArea(final BasicEffectArea area) {
        return area.getType() == this.getAreaType().getTypeId();
    }
    
    @Override
    protected boolean isConcernedArea(final BasicEffectArea area, final Point3 pos) {
        return this.isConcernedArea(area) && area.getPosition().equals(pos);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_FECA_GLYPH_CENTER;
    }
}
