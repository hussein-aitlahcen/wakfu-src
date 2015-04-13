package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.keplerproject.luajava.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.framework.script.*;

final class Close extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    Close(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "close";
    }
    
    @Override
    public String getDescription() {
        return "Ferme une bulle de dialogue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return Close.PARAMS;
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
        closeBubble(bubbleId);
    }
    
    static void closeBubble(final int bubbleId) {
        if (BubbleManager.getInstance().removeFromInteractiveBubbles(bubbleId) != null) {
            Xulor.getInstance().unload("interactiveBubbleDialog" + bubbleId);
        }
        else {
            AdviserManager.getInstance().removeAdviser(bubbleId);
            BubbleManager.getInstance().removeWakfuBubble(bubbleId);
        }
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle ? fermer", LuaScriptParameterType.INTEGER, false) };
    }
}
