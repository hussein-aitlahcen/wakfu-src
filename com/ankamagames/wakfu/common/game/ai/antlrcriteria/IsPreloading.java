package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public final class IsPreloading extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    public static boolean m_isPreloading;
    
    public IsPreloading(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsPreloading.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return IsPreloading.m_isPreloading ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_PLAYER;
    }
    
    static {
        IsPreloading.signatures = new ArrayList<ParserType[]>();
        IsPreloading.m_isPreloading = false;
        IsPreloading.signatures.add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
