package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public final class HasLineOfSightFromEnemy extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    public HasLineOfSightFromEnemy(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasLineOfSightFromEnemy.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        final Collection enemies = fight.getFightersInPlayNotInTeam(target.getTeamId());
        final Point3 targetPos = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        for (final Object enemy : enemies) {
            final BasicCharacterInfo source = (BasicCharacterInfo)enemy;
            if (FightFunctions.hasLineOfSight(source, fight.getFightMap(), targetPos.getX(), targetPos.getY(), targetPos.getZ(), target)) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_LINE_OF_SIGHT_FROM_ENEMY;
    }
    
    static {
        HasLineOfSightFromEnemy.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRING };
        HasLineOfSightFromEnemy.signatures.add(sig);
    }
}
