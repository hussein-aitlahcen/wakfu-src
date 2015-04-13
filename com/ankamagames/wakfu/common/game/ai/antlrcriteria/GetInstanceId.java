package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetInstanceId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetInstanceId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetInstanceId(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        if (user != null) {
            return user.getInstanceId();
        }
        if (criterionUser instanceof Collectible) {
            return ((Collectible)criterionUser).getInstanceId();
        }
        throw new CriteriaExecutionException("Impossible d'evaluer le critere GetInstanceId sur user=" + criterionUser);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETINSTANCEID;
    }
    
    static {
        GetInstanceId.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        GetInstanceId.signatures.add(sig);
    }
}
