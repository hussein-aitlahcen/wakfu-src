package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;

public class IsUndead extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsUndead.SIGNATURES;
    }
    
    public IsUndead(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget instanceof EffectUser && ((EffectUser)criterionTarget).isActiveProperty(FightPropertyType.UNDEAD)) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISUNDEAD;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
