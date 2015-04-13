package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.xulor2.component.*;
import org.keplerproject.luajava.*;

final class SetText extends JavaFunctionEx
{
    SetText(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setText";
    }
    
    @Override
    public String getDescription() {
        return "Sp?cifie le texte d'une bulle de dialogue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("text", "Texte ? afficher", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("parameters", "Param?tres du texte", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final InteractiveBubble bubble = BubbleManager.getInstance().getInteractiveBubble(this.getParamInt(0));
        String[] vars = null;
        if (this.textIsParametrized(paramCount)) {
            vars = new String[paramCount - 2];
            this.getTextParamsAsStrings(vars);
        }
        if (bubble != null) {
            this.setInteractiveBubbleText(bubble, vars);
            return;
        }
        final WakfuBubbleWidget wakfuBubble = BubbleManager.getInstance().getWakfuBubble(this.getParamInt(0));
        if (wakfuBubble != null) {
            this.setWakfuBubbleText(wakfuBubble, vars);
        }
    }
    
    private void setWakfuBubbleText(final WakfuBubbleWidget bubble, final String[] vars) throws LuaException {
        if (vars != null) {
            bubble.setText(BubbleTextUtils.getTranslatedText(this.getParamString(1), (Object[])vars));
        }
        else {
            bubble.setText(BubbleTextUtils.getTranslatedText(this.getParamString(1), new Object[0]));
        }
    }
    
    private void setInteractiveBubbleText(final InteractiveBubble bubble, final String[] vars) throws LuaException {
        if (vars != null) {
            bubble.setBubbleText(BubbleTextUtils.getTranslatedText(this.getParamString(1), (Object[])vars));
        }
        else {
            bubble.setBubbleText(BubbleTextUtils.getTranslatedText(this.getParamString(1), new Object[0]));
        }
    }
    
    private void getTextParamsAsStrings(final String[] vars) throws LuaException {
        for (int i = 0; i < vars.length; ++i) {
            final String param = this.getParamForcedAsString(i + 2);
            vars[i] = param;
        }
    }
    
    private boolean textIsParametrized(final int paramCount) {
        return paramCount > 2;
    }
}
