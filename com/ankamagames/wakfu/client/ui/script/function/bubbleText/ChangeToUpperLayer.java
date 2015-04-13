package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import org.keplerproject.luajava.*;

final class ChangeToUpperLayer extends JavaFunctionEx
{
    ChangeToUpperLayer(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "changeToUpperLayer";
    }
    
    @Override
    public String getDescription() {
        return "Ajoute la bulle ? la couche sup?rieure d'affichage";
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
            MasterRootContainer.getInstance().getLayeredContainer().removeWidget(bubble);
            bubble.setModalLevel((short)32767);
            MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer(bubble, 26000);
        }
    }
}
