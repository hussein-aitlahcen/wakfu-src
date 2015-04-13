package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class GetKamasCount extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetKamasCount.SIGNATURES;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetKamasCount(final ArrayList<ParserObject> args) {
        super();
    }
    
    public GetKamasCount() {
        super();
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicCharacterInfo user;
        if (criterionUser == null) {
            if (!(criterionContent instanceof PlayerTriggered)) {
                throw new CriteriaExecutionException("on cherche le nombre de kamas d'un user null");
            }
            user = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        else {
            if (!(criterionUser instanceof BasicCharacterInfo)) {
                throw new CriteriaExecutionException("on cherche le nombre de kamas d'autre chose qu'un character");
            }
            user = (BasicCharacterInfo)criterionUser;
        }
        return user.getKamasCount();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETKAMASCOUNT;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
