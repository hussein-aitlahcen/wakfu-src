package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

abstract class IsArea extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsArea.signatures;
    }
    
    IsArea(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            if (this.isNegated()) {
                return -1;
            }
            for (final BasicEffectArea area : ((BasicFight)criterionContext).getEffectAreaManager().getActiveEffectAreas()) {
                if (this.isConcernedArea(area)) {
                    return 0;
                }
            }
            return -1;
        }
        else {
            if (criterionTarget instanceof EffectAreaProvider) {
                return this.isConcernedArea(((EffectAreaProvider)criterionTarget).getEffectArea()) ? 0 : -1;
            }
            AbstractFight fight = null;
            if (criterionContext instanceof AbstractFight) {
                fight = (AbstractFight)criterionContext;
            }
            else if (criterionContext instanceof WakfuFightEffectContext) {
                fight = ((WakfuFightEffectContext)criterionContext).getFight();
            }
            if (criterionTarget instanceof Point3 && fight != null) {
                for (final BasicEffectArea area2 : fight.getEffectAreaManager().getActiveEffectAreas()) {
                    final Point3 point3 = (Point3)criterionTarget;
                    if (this.isConcernedArea(area2, point3)) {
                        return 0;
                    }
                }
                return -1;
            }
            return -2;
        }
    }
    
    protected boolean isConcernedArea(final BasicEffectArea area, final Point3 point3) {
        return area != null && this.isConcernedArea(area) && area.contains(point3.getX(), point3.getY(), point3.getZ());
    }
    
    protected boolean isConcernedArea(final BasicEffectArea area) {
        return area.getType() == this.getAreaType().getTypeId();
    }
    
    abstract EffectAreaType getAreaType();
    
    static {
        (IsArea.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
