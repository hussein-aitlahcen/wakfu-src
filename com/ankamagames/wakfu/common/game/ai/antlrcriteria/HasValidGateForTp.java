package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import gnu.trove.*;

public class HasValidGateForTp extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    private final boolean m_target;
    private final NumericalValue m_minRange;
    private final NumericalValue m_maxRange;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasValidGateForTp.signatures;
    }
    
    public HasValidGateForTp(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_minRange = args.get(1);
        this.m_maxRange = args.get(2);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContext == null) {
            return 0;
        }
        final boolean isFight = criterionContext instanceof AbstractFight;
        final boolean isFightContext = criterionContext instanceof WakfuFightEffectContext;
        if (!isFight && !isFightContext) {
            return 0;
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
            return 0;
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
            return 0;
        }
        long value = 0L;
        final long effectAreaTypeId = EffectAreaType.GATE.getTypeId();
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
                final Point3 areaPosition = effectArea.getPosition();
                final TByteHashSet obstaclesOnAreaPosition = fight.getFightMap().getObstaclesIdFromPos(areaPosition.getX(), areaPosition.getY());
                if (obstaclesOnAreaPosition != null && !obstaclesOnAreaPosition.isEmpty()) {
                    continue;
                }
                final int distanceBetweenUserAndEffectUser = DistanceUtils.getIntersectionDistance(effectArea, center);
                if (maxRange >= 0L && (distanceBetweenUserAndEffectUser > maxRange || distanceBetweenUserAndEffectUser < minRange)) {
                    continue;
                }
                ++value;
            }
        }
        return (value > 0L) ? 0 : -1;
    }
    
    protected boolean isConcernedArea(final long id, final BasicEffectArea effectArea) {
        return effectArea.getType() != id;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_VALID_GATE_FOR_TP;
    }
    
    static {
        (signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.NUMBER });
    }
}
