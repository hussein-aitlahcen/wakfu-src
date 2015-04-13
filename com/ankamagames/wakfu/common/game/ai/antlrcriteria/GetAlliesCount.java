package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetAlliesCount extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetAlliesCount.signatures;
    }
    
    GetAlliesCount() {
        super();
    }
    
    public GetAlliesCount(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        final boolean isFight = criterionContext instanceof AbstractFight;
        final boolean isFightContext = criterionContext instanceof WakfuFightEffectContext;
        if (!isFight && !isFightContext) {
            throw new CriteriaExecutionException("On essaie de compter les fighters en dehors d'un combat...");
        }
        if (!(criterionUser instanceof CriterionUser)) {
            throw new CriteriaExecutionException("On essaie de compter les fighters d'un caster qui n'est pas un perso");
        }
        AbstractFight<?> fight;
        if (isFight) {
            fight = (AbstractFight<?>)criterionContext;
        }
        else {
            fight = ((WakfuFightEffectContext)criterionContext).getFight();
        }
        if (fight == null) {
            return 0L;
        }
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        return fight.getFightersPresentInTimelineInPlayInTeam(user.getTeamId()).size();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_ALLIES_COUNT;
    }
    
    static {
        (GetAlliesCount.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        GetAlliesCount.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
