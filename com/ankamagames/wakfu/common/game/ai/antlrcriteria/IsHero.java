package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsHero extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsHero.SIGNATURES;
    }
    
    public IsHero(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_HERO;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        return (HeroesLeaderManager.INSTANCE.getLeader(user.getOwnerId()) == user.getId()) ? -1 : 0;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
