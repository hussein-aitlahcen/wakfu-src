package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class IsMoving extends JavaFunctionEx
{
    private static final String NAME = "isMoving";
    private static final String DESC = "Renvoie true sur le mobile pass\u00e9 en param est en train de se d\u00e9placer";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public IsMoving(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "isMoving";
    }
    
    @Nullable
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return IsMoving.PARAMS;
    }
    
    @Nullable
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return IsMoving.RESULT;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (!(mobile instanceof PathMobile)) {
            return;
        }
        final PathMobile pathMobile = (PathMobile)mobile;
        this.addReturnValue(mobile != null && pathMobile.isMoving());
    }
    
    @Override
    public String getDescription() {
        return "Renvoie true sur le mobile pass\u00e9 en param est en train de se d\u00e9placer";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'Id du mobile", LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("En mouvement", "Le mobile est en mouvement", LuaScriptParameterType.BOOLEAN, false) };
    }
}
