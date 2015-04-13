package com.ankamagames.framework.script.action.group;

import com.ankamagames.framework.script.action.*;

public final class ExecutionForcedActionGroupFactory implements ActionGroupFactory
{
    public static ExecutionForcedActionGroupFactory INSTANCE;
    
    @Override
    public ActionGroup createActionGroup() {
        return new ExecutionForcedActionGroup();
    }
    
    static {
        ExecutionForcedActionGroupFactory.INSTANCE = new ExecutionForcedActionGroupFactory();
    }
}
