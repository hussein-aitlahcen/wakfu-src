package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.xulor2.component.*;
import org.keplerproject.luajava.*;

final class SetShakingMode extends JavaFunctionEx
{
    SetShakingMode(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setShakingMode";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("enable", null, LuaScriptParameterType.BOOLEAN, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int bubbleId = this.getParamInt(0);
        if (bubbleId < 0) {
            return;
        }
        final WakfuBubbleWidget b = BubbleManager.getInstance().getWakfuBubble(bubbleId);
        if (b != null) {
            boolean shaking = true;
            if (paramCount > 1) {
                shaking = this.getParamBool(1);
            }
            b.setShakingBubble(shaking);
        }
    }
}
