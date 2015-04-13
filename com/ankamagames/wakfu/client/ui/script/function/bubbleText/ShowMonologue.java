package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;

final class ShowMonologue extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    ShowMonologue(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "showMonologue";
    }
    
    @Override
    public String getDescription() {
        return "Affiche une bulle de texte au dessus d'un personnage avec un bouton suivant qui permet de faire d?filer un texte. LE bouton devient 'FIN' pour le dernier texte et fermera la bulle ";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du personnage", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("texts", "Textes ? afficher (sous forme de table)", LuaScriptParameterType.TABLE, false), new LuaScriptParameterDescriptor("funcOnTerminate", "Fonction ? appeler ? la fin", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("funcParams", "Param?tres de la fonction de fin", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final Mobile mobile = MobileManager.getInstance().getMobile(this.getParamLong(0));
        if (mobile == null) {
            this.writeError(ShowMonologue.m_logger, "Le mobile n'existe pas " + this.getParamLong(0));
            this.addReturnValue(0);
            return;
        }
        final LuaValue[] tableText = this.getParamTable(1);
        if (tableText.length == 0) {
            this.writeError(ShowMonologue.m_logger, "La table de texte est vide");
        }
        final String[] texts = new String[tableText.length];
        for (int i = 0; i < tableText.length; ++i) {
            texts[i] = BubbleTextUtils.getTranslatedText((String)tableText[i].getValue(), new Object[0]);
        }
        final LuaScript script = this.getScriptObject();
        final int id = AdviserManager.getInstance().getNewUniqueId();
        final InteractiveBubble bubble = (InteractiveBubble)Xulor.getInstance().load("interactiveBubbleDialog" + id, Dialogs.getDialogPath("interactiveBubbleDialog"), 8256L, (short)30000);
        MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer(bubble, 25000);
        bubble.setVisible(false);
        bubble.setTarget(mobile);
        BubbleManager.getInstance().putInInteractiveBubbles(id, bubble);
        final int[] index = { 0 };
        bubble.setBubbleText(texts[0]);
        bubble.setActAsButton(true);
        final String funcName = (paramCount > 2) ? this.getParamString(2) : null;
        final LuaValue[] params = this.getParams(3, paramCount);
        bubble.addButton(BubbleTextUtils.getTranslatedText("dialog.next", new Object[0]), new EventListener() {
            @Override
            public boolean run(final Event event) {
                final int[] val$index = index;
                final int n = 0;
                ++val$index[n];
                final int i = index[0];
                bubble.setBubbleText(texts[i]);
                if (i == texts.length - 1) {
                    bubble.changeButtonEvent(0, this, new EventListener() {
                        @Override
                        public boolean run(final Event ev) {
                            if (funcName != null) {
                                script.runFunction(funcName, params, new LuaTable[0]);
                            }
                            Xulor.getInstance().unload("interactiveBubbleDialog" + id);
                            return false;
                        }
                    });
                }
                return false;
            }
        }, true);
        bubble.setCloseOnClick(false);
        bubble.show();
        this.addReturnValue(id);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShowMonologue.class);
    }
}
