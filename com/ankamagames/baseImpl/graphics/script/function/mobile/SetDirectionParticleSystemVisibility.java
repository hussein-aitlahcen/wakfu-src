package com.ankamagames.baseImpl.graphics.script.function.mobile;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class SetDirectionParticleSystemVisibility extends JavaFunctionEx
{
    private static final String NAME = "setDirectionParticleSystemVisibility";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetDirectionParticleSystemVisibility(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setDirectionParticleSystemVisibility";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetDirectionParticleSystemVisibility.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final boolean visible = this.getParamBool(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            return;
        }
        if (visible) {
            mobile.showDirectionParticleSystem();
        }
        else {
            mobile.hideDirectionParticleSystem();
        }
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("visibility", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
