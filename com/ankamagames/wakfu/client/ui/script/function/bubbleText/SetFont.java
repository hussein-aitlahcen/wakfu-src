package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import org.keplerproject.luajava.*;

final class SetFont extends JavaFunctionEx
{
    SetFont(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setFont";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final InteractiveBubble bubble = BubbleManager.getInstance().getInteractiveBubble(this.getParamInt(0));
        if (bubble != null) {
            bubble.setBubbleFontName(this.getParamString(1));
            bubble.setBubbleFontSize(this.getParamInt(2));
            bubble.setBubbleFontStyle(this.getParamInt(3));
        }
    }
}
