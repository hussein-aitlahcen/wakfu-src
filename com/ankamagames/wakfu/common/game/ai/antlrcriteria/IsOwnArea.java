package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public class IsOwnArea extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private final ConstantBooleanCriterion m_onAreaCenter;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOwnArea.SIGNATURES;
    }
    
    public IsOwnArea(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.isEmpty()) {
            this.m_onAreaCenter = null;
        }
        else {
            this.m_onAreaCenter = args.get(0);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final EffectUser effectUser = CriteriaUtils.getOwner(criterionUser);
        final EffectUser effectTarget = CriteriaUtils.getOwner(criterionTarget);
        if (criterionUser == null) {
            return -1;
        }
        if (criterionTarget instanceof AreaOwnerProvider) {
            if ((this.getAreaType() == null || ((AreaOwnerProvider)criterionTarget).getType() == this.getAreaType().getTypeId()) && effectTarget == effectUser) {
                return 0;
            }
            return -1;
        }
        else {
            if (criterionTarget == null) {
                for (final BasicEffectArea area : ((AbstractFight)criterionContext).getEffectAreaManager().getActiveEffectAreas()) {
                    if ((this.getAreaType() == null || area.getType() == this.getAreaType().getTypeId()) && this.areaIsOwnedByUser(effectUser, area)) {
                        return 0;
                    }
                }
                return -1;
            }
            final boolean onAreaCenter = this.m_onAreaCenter != null && this.m_onAreaCenter.isValid(criterionUser, criterionContent, criterionContent, criterionContext);
            final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
            if (criterionTarget instanceof Point3 && fight != null) {
                final Point3 targetPos = (Point3)criterionTarget;
                for (final BasicEffectArea area2 : fight.getEffectAreaManager().getActiveEffectAreas()) {
                    if (onAreaCenter && !area2.getPosition().equalsIgnoringAltitude(targetPos)) {
                        continue;
                    }
                    if (this.areaContainsTargetPos(targetPos, area2) && this.areaIsOwnedByUser(effectUser, area2)) {
                        return 0;
                    }
                }
                return -1;
            }
            return -2;
        }
    }
    
    private boolean areaIsOwnedByUser(final EffectUser effectUser, final BasicEffectArea area) {
        return CriteriaUtils.getOwner(area) == effectUser;
    }
    
    private boolean areaContainsTargetPos(final Point3 targetPos, final BasicEffectArea area) {
        return (this.getAreaType() == null || area.getType() == this.getAreaType().getTypeId()) && area.contains(targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }
    
    EffectAreaType getAreaType() {
        return null;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_OWN_AREA;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsOwnArea.SIGNATURES.add(CriterionConstants.ONE_BOOLEAN_SIGNATURE);
    }
}
