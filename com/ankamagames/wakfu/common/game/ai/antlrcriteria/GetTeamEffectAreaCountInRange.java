package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class GetTeamEffectAreaCountInRange extends FunctionValue
{
    private static final ArrayList<ParserType[]> signatures;
    private final boolean m_target;
    private final NumericalValue m_effectAreaTypeId;
    private final NumericalValue m_minRange;
    private final NumericalValue m_maxRange;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTeamEffectAreaCountInRange.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetTeamEffectAreaCountInRange(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_effectAreaTypeId = args.get(1);
        this.m_minRange = args.get(2);
        this.m_maxRange = args.get(3);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContext == null) {
            return 0L;
        }
        final boolean isFight = criterionContext instanceof AbstractFight;
        final boolean isFightContext = criterionContext instanceof WakfuFightEffectContext;
        if (!isFight && !isFightContext) {
            return 0L;
        }
        AbstractFight<?> fight;
        if (isFight) {
            fight = (AbstractFight<?>)criterionContext;
        }
        else {
            fight = ((WakfuFightEffectContext)criterionContext).getFight();
        }
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            return 0L;
        }
        Point3 center = null;
        if (!this.m_target) {
            center = user.getPosition();
        }
        else if (criterionTarget instanceof Point3) {
            center = (Point3)criterionTarget;
        }
        else {
            final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
            if (targetCharacter != null) {
                center = targetCharacter.getPosition();
            }
        }
        if (center == null) {
            return 0L;
        }
        long value = 0L;
        final long effectAreaTypeId = this.m_effectAreaTypeId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final long minRange = this.m_minRange.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final long maxRange = this.m_maxRange.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        for (final BasicEffectArea effectArea : fight.getEffectAreaManager().getActiveEffectAreas()) {
            if (effectArea != user && effectArea != null) {
                if (this.isConcernedArea(effectAreaTypeId, effectArea)) {
                    continue;
                }
                if (effectArea.getTeamId() != user.getTeamId()) {
                    continue;
                }
                final int distanceBetweenUserAndEffectUser = DistanceUtils.getIntersectionDistance(effectArea, center);
                if (maxRange >= 0L && (distanceBetweenUserAndEffectUser > maxRange || distanceBetweenUserAndEffectUser < minRange)) {
                    continue;
                }
                ++value;
            }
        }
        return this.getSign() * value;
    }
    
    protected boolean isConcernedArea(final long id, final BasicEffectArea effectArea) {
        return effectArea.getType() != id;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE;
    }
    
    static {
        (signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.NUMBER, ParserType.NUMBER });
    }
}
