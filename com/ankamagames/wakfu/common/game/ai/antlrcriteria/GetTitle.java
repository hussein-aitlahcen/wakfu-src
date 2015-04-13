package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.title.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetTitle extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTitle.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetTitle(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            throw new CriteriaExecutionException("on cherche le nombre de kamas d'un user null");
        }
        if (!(criterionUser instanceof TitleHolder)) {
            throw new CriteriaExecutionException("on cherche le titre d'un character qui ne peut en avoir");
        }
        return super.getSign() * ((TitleHolder)user).getCurrentTitle();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TITLE;
    }
    
    static {
        (GetTitle.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
