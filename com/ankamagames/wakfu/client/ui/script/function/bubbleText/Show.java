package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.xulor2.component.*;
import org.keplerproject.luajava.*;

final class Show extends JavaFunctionEx
{
    Show(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "show";
    }
    
    @Override
    public String getDescription() {
        return "Affiche une bulle de dialogue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final InteractiveBubble bubble = BubbleManager.getInstance().getInteractiveBubble(this.getParamInt(0));
        if (bubble != null) {
            bubble.show();
            return;
        }
        final WakfuBubbleWidget wakfuBubble = BubbleManager.getInstance().getWakfuBubble(this.getParamInt(0));
        wakfuBubble.setBubbleIsVisible(true);
    }
}
