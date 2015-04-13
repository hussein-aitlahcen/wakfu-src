package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class GetMonth extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetMonth.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetMonth(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final Calendar date = Calendar.getInstance();
        return date.get(2) + 1L;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETMONTH;
    }
    
    static {
        GetMonth.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        GetMonth.signatures.add(sig);
    }
}
