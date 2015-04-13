package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public final class IsSpecificAreaWithSpecificState extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private final boolean m_target;
    private final NumericalValue m_areaId;
    private final NumericalValue m_stateId;
    private final ConstantBooleanCriterion m_ownAreaNeeded;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsSpecificAreaWithSpecificState.signatures;
    }
    
    public IsSpecificAreaWithSpecificState(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_areaId = args.get(1);
        this.m_ownAreaNeeded = args.get(2);
        this.m_stateId = args.get(3);
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
        final boolean ownAreaNeeded = this.m_ownAreaNeeded.isValid(criterionUser, criterionTarget, criterionContent, criterionContext);
        final long areaId = this.m_areaId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final long stateId = this.m_stateId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (potentialArea instanceof EffectAreaProvider) {
            final BasicEffectArea effectArea = ((EffectAreaProvider)potentialArea).getEffectArea();
            if (effectArea == null) {
                return -1;
            }
            if (effectArea.getBaseId() != areaId) {
                return -1;
            }
            if (ownAreaNeeded && effectTarget != effectUser) {
                return -1;
            }
            if (!(effectArea instanceof CriterionUser)) {
                return -1;
            }
            if (!((CriterionUser)effectArea).hasState(stateId)) {
                return -1;
            }
            return 0;
        }
        else {
            if (potentialArea == null) {
                for (final BasicEffectArea area : ((AbstractFight)criterionContext).getEffectAreaManager().getActiveEffectAreas()) {
                    if (area.getBaseId() != areaId) {
                        continue;
                    }
                    if (ownAreaNeeded && CriteriaUtils.getOwner(area) != effectUser) {
                        continue;
                    }
                    if (!(area instanceof CriterionUser)) {
                        continue;
                    }
                    if (((CriterionUser)area).hasState(stateId)) {
                        return 0;
                    }
                }
                return -1;
            }
            final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
            if (criterionTarget instanceof Point3 && fight != null) {
                for (final BasicEffectArea area2 : fight.getEffectAreaManager().getActiveEffectAreas()) {
                    final Point3 point3 = (Point3)criterionTarget;
                    if (area2.getBaseId() != areaId) {
                        continue;
                    }
                    if (ownAreaNeeded && CriteriaUtils.getOwner(area2) != effectUser) {
                        continue;
                    }
                    if (!area2.contains(point3)) {
                        continue;
                    }
                    if (!(area2 instanceof CriterionUser)) {
                        continue;
                    }
                    if (((CriterionUser)area2).hasState(stateId)) {
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
        return WakfuCriterionIds.IS_SPECIFIC_AREA_WITH_SPECIFIC_STATE;
    }
    
    static {
        IsSpecificAreaWithSpecificState.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING, ParserType.NUMBER, ParserType.BOOLEAN, ParserType.NUMBER };
        IsSpecificAreaWithSpecificState.signatures.add(sig);
    }
}
