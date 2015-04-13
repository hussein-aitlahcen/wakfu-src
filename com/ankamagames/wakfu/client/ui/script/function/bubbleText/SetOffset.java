package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import org.keplerproject.luajava.*;

final class SetOffset extends JavaFunctionEx
{
    SetOffset(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setOffset";
    }
    
    @Override
    public String getDescription() {
        return "D?cale une bulle de dialogue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("x", "D?calage en pixel vers la droite", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", "D?calage en pixel vers le haut", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final InteractiveBubble bubble = BubbleManager.getInstance().getInteractiveBubble(this.getParamInt(0));
        if (bubble != null) {
            bubble.setOffset(this.getParamInt(1), this.getParamInt(2));
        }
    }
}
