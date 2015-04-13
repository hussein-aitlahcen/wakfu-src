package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.government.*;

public class GetNationRank extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetNationRank.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetNationRank(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user == null) {
            throw new CriteriaExecutionException("on cherche le rang gouvernemental d'un user null");
        }
        if (!(criterionUser instanceof Citizen)) {
            throw new CriteriaExecutionException("on cherche le gouvernemental d'un user qui n'est pas citoyen");
        }
        final NationRank rank = user.getCitizenComportment().getRank();
        if (rank == null) {
            return -1L;
        }
        return super.getSign() * rank.getId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_NATION_RANK;
    }
    
    static {
        (GetNationRank.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
    }
}
