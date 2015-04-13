package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public final class IsOwnSpecificArea extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private final long m_areaId;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOwnSpecificArea.signatures;
    }
    
    public IsOwnSpecificArea(final ArrayList<ParserObject> args) {
        super();
        this.m_target = true;
        this.checkType(args);
        this.m_areaId = args.get(0).getLongValue(null, null, null, null);
        if (args.size() == 2) {
            final String isTarget = args.get(1).getValue();
            if (isTarget.equalsIgnoreCase("caster")) {
                this.m_target = false;
            }
        }
        else {
            this.m_target = true;
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final EffectUser effectUser = CriteriaUtils.getOwner(criterionUser);
        final EffectUser effectTarget = CriteriaUtils.getOwner(criterionTarget);
        if (criterionUser == null) {
            return -1;
        }
        Object potentialArea;
        if (this.m_target) {
            potentialArea = criterionTarget;
        }
        else {
            potentialArea = criterionUser;
        }
        if (potentialArea instanceof EffectAreaProvider) {
            final BasicEffectArea effectArea = ((EffectAreaProvider)potentialArea).getEffectArea();
            if (effectArea == null) {
                return -1;
            }
            if (effectArea.getBaseId() == this.m_areaId && effectTarget == effectUser) {
                return 0;
            }
            return -1;
        }
        else {
            if (potentialArea == null) {
                for (final BasicEffectArea area : ((AbstractFight)criterionContext).getEffectAreaManager().getActiveEffectAreas()) {
                    if (area.getBaseId() == this.m_areaId && CriteriaUtils.getOwner(area) == effectUser) {
                        return 0;
                    }
                }
                return -1;
            }
            final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
            if (criterionTarget instanceof Point3 && fight != null) {
                for (final BasicEffectArea area2 : fight.getEffectAreaManager().getActiveEffectAreas()) {
                    final Point3 point3 = (Point3)criterionTarget;
                    if (area2.getBaseId() == this.m_areaId && area2.contains(point3.getX(), point3.getY(), point3.getZ()) && CriteriaUtils.getOwner(area2) == effectUser) {
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
        return WakfuCriterionIds.IS_OWN_SPECIFIC_AREA;
    }
    
    static {
        (IsOwnSpecificArea.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER });
        IsOwnSpecificArea.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING });
    }
}
