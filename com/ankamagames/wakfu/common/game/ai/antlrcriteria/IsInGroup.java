package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class IsInGroup extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsInGroup.SIGNATURES;
    }
    
    public IsInGroup() {
        super();
    }
    
    public IsInGroup(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo character = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        return character.hasAGroup(GroupType.PARTY) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_IN_GROUP;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
