package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.lock.*;

public class GetLockIncrement extends FunctionValue
{
    private int m_lockId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetLockIncrement.signatures;
    }
    
    public GetLockIncrement() {
        super();
    }
    
    public GetLockIncrement(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_lockId = (int)args.get(0).getLongValue(null, null, null, null);
        }
        else if (paramType == 1) {
            this.m_lockId = (int)args.get(0).getLongValue(null, null, null, null);
        }
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof LockContextProvider)) {
            return 0L;
        }
        final LockContextProvider provider = (LockContextProvider)criterionUser;
        final LockContext context = provider.getLockContext();
        return context.getActualCurrentLockValue(this.m_lockId);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_LOCKED;
    }
    
    static {
        GetLockIncrement.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        GetLockIncrement.signatures.add(sig);
    }
}
