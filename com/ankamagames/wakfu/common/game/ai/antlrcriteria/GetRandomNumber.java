package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class GetRandomNumber extends FunctionValue
{
    private NumericalValue m_randomLimit;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetRandomNumber.signatures;
    }
    
    public GetRandomNumber(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_randomLimit = args.get(0);
    }
    
    public int getRandomLimit() {
        if (this.m_randomLimit.isConstant() && this.m_randomLimit.isInteger()) {
            return (int)this.m_randomLimit.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!this.m_randomLimit.isInteger()) {
            throw new CriteriaExecutionException("La limite du random n'est pas un entier");
        }
        return MathHelper.random(this.getRandomLimit());
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETRANDOMNUMBER;
    }
    
    static {
        GetRandomNumber.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        GetRandomNumber.signatures.add(sig);
    }
}
