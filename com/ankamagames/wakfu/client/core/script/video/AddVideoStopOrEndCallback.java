package com.ankamagames.wakfu.client.core.script.video;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class AddVideoStopOrEndCallback extends JavaFunctionEx
{
    private static final String NAME = "addVideoStopOrEndCallback";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    AddVideoStopOrEndCallback(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addVideoStopOrEndCallback";
    }
    
    @Nullable
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddVideoStopOrEndCallback.PARAMS;
    }
    
    @Nullable
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[0];
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final String functionName = this.getParamString(0);
        final LuaValue[] parameters = this.getParams(1, paramCount);
        final LuaScript script = this.getScriptObject();
        final int taskId = script.registerWaitingTask(functionName, parameters);
        final AbstractUIMessage msg = new UIMessage((short)19101);
        msg.setObjectValue(new Runnable() {
            @Override
            public void run() {
                script.executeWaitingTask(taskId);
            }
        });
        Worker.getInstance().pushMessage(msg);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
