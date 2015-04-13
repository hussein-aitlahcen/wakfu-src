package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class GetFightModel extends FunctionValue
{
    private static final List<ParserType[]> SIGNATURES;
    public static final ParserType[] EMPTY;
    
    public GetFightModel(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return Collections.unmodifiableList((List<? extends ParserType[]>)GetFightModel.SIGNATURES);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicFight<?> fight = (BasicFight<?>)CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null && criterionContent != null && criterionContent instanceof PlayerTriggered) {
            fight = (BasicFight<?>)((PlayerTriggered)criterionContent).getTriggerer().getCurrentFight();
        }
        if (fight == null) {
            return 0L;
        }
        return fight.getModel().getTypeId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_FIGHT_MODEL;
    }
    
    static {
        SIGNATURES = new ArrayList<ParserType[]>();
        EMPTY = new ParserType[0];
        GetFightModel.SIGNATURES.add(GetFightModel.EMPTY);
    }
}
