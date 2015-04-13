package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;

public class GetEffectAreaCountInRunningEffectAOE extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_effectAreaUniqueId;
    private EffectAreaOwnerCriteria m_ownerCriteria;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetEffectAreaCountInRunningEffectAOE.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetEffectAreaCountInRunningEffectAOE(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        final String ownerCriteria = args.get(0).getValue().toLowerCase();
        if (ownerCriteria.equals("mine")) {
            this.m_ownerCriteria = EffectAreaOwnerCriteria.MYSELF;
        }
        else if (ownerCriteria.equals("allies")) {
            this.m_ownerCriteria = EffectAreaOwnerCriteria.MY_TEAM;
        }
        else if (ownerCriteria.equals("ennemies")) {
            this.m_ownerCriteria = EffectAreaOwnerCriteria.ENNEMY_TEAM;
        }
        else {
            if (!ownerCriteria.equals("all")) {
                throw new CriteriaExecutionException("param\u00e8tre invalude dans une " + this.getClass().getSimpleName() + " : '" + ownerCriteria + "'");
            }
            this.m_ownerCriteria = EffectAreaOwnerCriteria.ANYBODY;
        }
        this.m_effectAreaUniqueId = args.get(1);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        if (criterionContent == null) {
            return 0L;
        }
        if (!(criterionContent instanceof RunningEffect)) {
            GetEffectAreaCountInRunningEffectAOE.m_logger.error((Object)"CriterionContent attendu, mais pas de type RunningEffect. Ce crit\u00e8re ne peut s'utiliser que comme crit\u00e8re sur un effet.");
            return 0L;
        }
        final Point3 targetPos = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetPos == null) {
            GetEffectAreaCountInRunningEffectAOE.m_logger.error((Object)"Unable to compute target position");
            return 0L;
        }
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null && this.m_ownerCriteria != EffectAreaOwnerCriteria.ANYBODY) {
            return 0L;
        }
        final Point3 casterPos = CriteriaUtils.getTargetPosition(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (casterPos == null) {
            GetEffectAreaCountInRunningEffectAOE.m_logger.error((Object)"Unable to compute caster position");
            return 0L;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            throw new CriteriaExecutionException("On essaie de compter les zones d'effet en dehors d'un combat...");
        }
        final Effect genericEffect = ((RunningEffect)criterionContent).getGenericEffect();
        if (genericEffect == null) {
            return 0L;
        }
        final AreaOfEffect areaOfEffect = genericEffect.getAreaOfEffect();
        if (areaOfEffect == null) {
            GetEffectAreaCountInRunningEffectAOE.m_logger.error((Object)("Unable to compute effect areas in effet AOE : effect doesn't have one. EffectIf : " + genericEffect.getEffectId()));
            return 0L;
        }
        final long effectAreaUniqueId = this.m_effectAreaUniqueId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        int count = 0;
        for (final BasicEffectArea effectArea : fight.getEffectAreaManager().getActiveEffectAreas()) {
            if (effectArea == null) {
                continue;
            }
            if (effectArea.getBaseId() != effectAreaUniqueId) {
                continue;
            }
            boolean criteriaOnOwnerOk = false;
            switch (this.m_ownerCriteria) {
                case ENNEMY_TEAM: {
                    criteriaOnOwnerOk = (effectArea.getTeamId() != user.getTeamId());
                    break;
                }
                case MY_TEAM: {
                    criteriaOnOwnerOk = (effectArea.getTeamId() == user.getTeamId());
                    break;
                }
                case MYSELF: {
                    criteriaOnOwnerOk = (effectArea.getOwner() == user);
                    break;
                }
                case ANYBODY: {
                    criteriaOnOwnerOk = true;
                    break;
                }
            }
            if (!criteriaOnOwnerOk) {
                continue;
            }
            final Point3 position = effectArea.getPosition();
            final boolean pointInside = areaOfEffect.isPointInside(targetPos.getX(), targetPos.getY(), targetPos.getZ(), casterPos.getX(), casterPos.getY(), casterPos.getZ(), position.getX(), position.getY(), position.getZ());
            if (!pointInside) {
                continue;
            }
            ++count;
        }
        return super.getSign() * count;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE;
    }
    
    static {
        GetEffectAreaCountInRunningEffectAOE.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING, ParserType.NUMBER };
        GetEffectAreaCountInRunningEffectAOE.signatures.add(sig);
    }
    
    private enum EffectAreaOwnerCriteria
    {
        MYSELF, 
        MY_TEAM, 
        ENNEMY_TEAM, 
        ANYBODY;
    }
}
