package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.lock.*;

public class IsLocked extends FunctionCriterion
{
    private int m_lockId;
    private boolean m_static;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsLocked.signatures;
    }
    
    public IsLocked() {
        super();
    }
    
    public IsLocked(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_lockId = (int)args.get(0).getLongValue(null, null, null, null);
            this.m_static = false;
        }
        else if (paramType == 1) {
            this.m_lockId = (int)args.get(0).getLongValue(null, null, null, null);
            this.m_static = args.get(1).isValid(null, null, null, null);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        boolean locked;
        if (this.m_static) {
            locked = LockManager.INSTANCE.isLocked(this.m_lockId);
        }
        else {
            if (!(criterionUser instanceof LockContextProvider)) {
                return -1;
            }
            final LockContextProvider provider = (LockContextProvider)criterionUser;
            final LockContext context = provider.getLockContext();
            locked = context.isLocked(this.m_lockId);
        }
        return locked ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_LOCKED;
    }
    
    static {
        IsLocked.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBER };
        IsLocked.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER, ParserType.BOOLEAN };
        IsLocked.signatures.add(sig);
    }
}
