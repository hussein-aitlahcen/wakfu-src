package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class IsPvp extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    public static final ParserType[] EMPTY;
    
    public IsPvp(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return Collections.unmodifiableList((List<? extends ParserType[]>)IsPvp.SIGNATURES);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null && criterionContent != null && criterionContent instanceof PlayerTriggered) {
            fight = (BasicFight<?>)((PlayerTriggered)criterionContent).getTriggerer().getCurrentFight();
        }
        if (fight == null) {
            return -1;
        }
        if (fight.getModel().getPvpModelType().isPvp()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISPVP;
    }
    
    static {
        SIGNATURES = new ArrayList<ParserType[]>();
        EMPTY = new ParserType[0];
        IsPvp.SIGNATURES.add(IsPvp.EMPTY);
    }
}
