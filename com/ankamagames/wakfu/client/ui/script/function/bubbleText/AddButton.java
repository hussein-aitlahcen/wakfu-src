package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.wakfu.client.ui.script.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import org.keplerproject.luajava.*;

final class AddButton extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    AddButton(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addButton";
    }
    
    @Override
    public String getDescription() {
        return "Ajoute un bouton ? une bulle interactive";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("text", "Texte du bouton ? afficher", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("function", "Nom de la fonction ? appeler lors du clic sur le boutton", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("parameters", "Param?tres ? passer ? la fonction", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int bubbleId = this.getParamInt(0);
        final String text = BubbleTextUtils.getTranslatedText(this.getParamString(1), new Object[0]);
        final String funcName = this.getParamString(2);
        final LuaValue[] params = this.getParams(3, paramCount);
        final LuaScript script = this.getScriptObject();
        EventListener el = (EventListener)ScriptUIEventManager.getInstance().eventListenerAlreadyExist(script, "interactiveBubbleDialog" + bubbleId, text, "MOUSE_CLICKED", funcName);
        if (el == null) {
            el = new LuaCallback(script, funcName, params);
            ScriptUIEventManager.getInstance().putEventListener(script, "interactiveBubbleDialog" + bubbleId, text, "MOUSE_CLICKED", funcName, el);
            final InteractiveBubble interactiveBubble = BubbleManager.getInstance().getInteractiveBubble(bubbleId);
            if (interactiveBubble != null) {
                interactiveBubble.addButton(text, el, true);
            }
            else {
                this.writeError(AddButton.m_logger, "id de bulle correspondant ? rien");
            }
        }
        else {
            ((LuaCallback)el).setParams(params);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddButton.class);
    }
}
