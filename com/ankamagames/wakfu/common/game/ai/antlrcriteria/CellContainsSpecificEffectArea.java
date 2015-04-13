package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public final class CellContainsSpecificEffectArea extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private final NumericalValue m_areaId;
    private final ConstantBooleanCriterion m_ownAreaNeeded;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return CellContainsSpecificEffectArea.signatures;
    }
    
    public CellContainsSpecificEffectArea(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_areaId = args.get(0);
        if (args.size() >= 2) {
            this.m_ownAreaNeeded = args.get(1);
        }
        else {
            this.m_ownAreaNeeded = null;
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final Point3 targetCell = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCell == null) {
            return -1;
        }
        final long areaId = this.m_areaId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        AbstractFight fight = null;
        if (criterionContext instanceof AbstractFight) {
            fight = (AbstractFight)criterionContext;
        }
        else if (criterionContext instanceof WakfuFightEffectContext) {
            fight = ((WakfuFightEffectContext)criterionContext).getFight();
        }
        if (fight == null) {
            return -1;
        }
        final boolean onlyOwnAreas = this.m_ownAreaNeeded != null && this.m_ownAreaNeeded.isValid(criterionUser, criterionContent, criterionContent, criterionContext);
        EffectUser areaOwnerToCheck;
        if (onlyOwnAreas) {
            final CriterionUser caster = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
            if (caster == null) {
                return -1;
            }
            areaOwnerToCheck = CriteriaUtils.getOwner(caster);
        }
        else {
            areaOwnerToCheck = null;
        }
        final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (area.getBaseId() != areaId) {
                continue;
            }
            if (areaOwnerToCheck != null && CriteriaUtils.getOwner(area) != areaOwnerToCheck) {
                continue;
            }
            if (area.contains(targetCell)) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.CELL_CONTAINS_SPECIFIC_EFFECT_AREA;
    }
    
    static {
        CellContainsSpecificEffectArea.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBER };
        CellContainsSpecificEffectArea.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.BOOLEAN, null };
        CellContainsSpecificEffectArea.signatures.add(sig);
    }
}
