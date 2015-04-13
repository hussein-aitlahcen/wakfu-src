package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class GetTriggererEffectCaster extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTriggererEffectCaster.SIGNATURES;
    }
    
    public GetTriggererEffectCaster(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null) {
            return 0L;
        }
        if (!(criterionUser instanceof EffectUser)) {
            throw new CriteriaExecutionException("Le crit\u00e8re d'effet est employ\u00e9 pour autre chose qu'un effet");
        }
        return ((EffectUser)criterionUser).getId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETEFFECTTARGET;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
