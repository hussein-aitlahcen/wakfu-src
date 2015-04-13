package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class IsDay extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsDay.signatures;
    }
    
    public IsDay(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return WakfuGameCalendar.getInstance().isSunShining() ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IsDay;
    }
    
    static {
        IsDay.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        IsDay.signatures.add(sig);
    }
}
