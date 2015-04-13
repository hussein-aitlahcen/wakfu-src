package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class IsSpecificArea extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private final long m_effectAreaBaseId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsSpecificArea.signatures;
    }
    
    public IsSpecificArea(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_effectAreaBaseId = args.get(0).getLongValue(null, null, null, null);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            for (final BasicEffectArea area : ((BasicFight)criterionContext).getEffectAreaManager().getActiveEffectAreas()) {
                if (area.getBaseId() == this.m_effectAreaBaseId) {
                    return 0;
                }
            }
            return -1;
        }
        if (criterionTarget instanceof EffectAreaProvider) {
            final BasicEffectArea effectArea = ((EffectAreaProvider)criterionTarget).getEffectArea();
            if (effectArea == null) {
                return -1;
            }
            return (effectArea.getBaseId() == this.m_effectAreaBaseId) ? 0 : -1;
        }
        else {
            AbstractFight fight = null;
            if (criterionContext instanceof AbstractFight) {
                fight = (AbstractFight)criterionContext;
            }
            else if (criterionContext instanceof WakfuFightEffectContext) {
                fight = ((WakfuFightEffectContext)criterionContext).getFight();
            }
            if (criterionTarget instanceof Point3 && fight != null) {
                final Point3 point3 = (Point3)criterionTarget;
                for (final BasicEffectArea area2 : fight.getEffectAreaManager().getActiveEffectAreas()) {
                    if (area2.getBaseId() == this.m_effectAreaBaseId && area2.contains(point3.getX(), point3.getY(), point3.getZ())) {
                        return 0;
                    }
                }
                return -1;
            }
            return -2;
        }
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_SPECIFIC_AREA;
    }
    
    static {
        (IsSpecificArea.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_NUMBER_SIGNATURE);
    }
}
