package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetCommonLastInstanceId extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return new ArrayList<ParserType[]>(GetCommonLastInstanceId.SIGNATURES);
    }
    
    public GetCommonLastInstanceId() {
        super();
    }
    
    public GetCommonLastInstanceId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser user = CriteriaUtils.getTargetCriterionUserFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (!(user instanceof BasicCharacterInfo)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le point de reSpawn sur un character inexistant ou qui n'est pas un joueur");
        }
        return this.getSign() * ((BasicCharacterInfo)user).getPreviousWorldId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_LAST_INSTANCE_ID;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(ParserType.EMPTY_ARRAY);
    }
}
