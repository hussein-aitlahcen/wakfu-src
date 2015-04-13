package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import org.keplerproject.luajava.*;

final class RemoveOnClose extends JavaFunctionEx
{
    RemoveOnClose(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "removeOnClose";
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
        final int bubbleId = this.getParamInt(0);
        if (bubbleId < 0) {
            return;
        }
        final InteractiveBubble b = BubbleManager.getInstance().getInteractiveBubble(bubbleId);
        if (b != null) {
            final int dialogIdHash = ("interactiveBubbleDialog" + bubbleId).hashCode();
            BubbleManager.getInstance().removeFromEndFunctionRunners(dialogIdHash);
        }
        else {
            BubbleManager.getInstance().removeFromAdviserEventObservers(bubbleId);
        }
    }
}
