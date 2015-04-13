package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class IsUnderControl extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    
    public IsUnderControl(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsUnderControl.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null || criterionTarget == null || !(criterionUser instanceof BasicCharacterInfo) || !(criterionTarget instanceof EffectUser)) {
            throw new CriteriaExecutionException("On cherche les summons d'un user null");
        }
        if (((BasicCharacterInfo)criterionUser).getControlled(((EffectUser)criterionTarget).getId()) != null) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISUNDERCONTROL;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        IsUnderControl.signatures.add(sig);
    }
}
