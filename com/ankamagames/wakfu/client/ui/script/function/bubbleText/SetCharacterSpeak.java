package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;

final class SetCharacterSpeak extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    SetCharacterSpeak(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setCharacterSpeak";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("translationKey", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("translation parameters", null, LuaScriptParameterType.TABLE, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final String translationKey = this.getParamString(1);
        LuaValue[] translationParams = null;
        boolean resizable = false;
        if (paramCount > 2) {
            if (this.isTable(2)) {
                translationParams = this.getParamTable(2);
            }
            else if (this.isBoolean(2)) {
                resizable = this.getParamBool(2);
            }
        }
        if (paramCount > 3) {
            resizable = this.getParamBool(3);
        }
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(SetCharacterSpeak.m_logger, "mobile inconnu " + mobileId);
            return;
        }
        if (!(mobile instanceof CharacterActor)) {
            this.writeError(SetCharacterSpeak.m_logger, "le mobile " + mobileId + " n'est pas valide");
            return;
        }
        final CharacterInfo character = ((CharacterActor)mobile).getCharacterInfo();
        String[] params;
        if (translationParams != null) {
            params = new String[translationParams.length];
            for (int i = 0; i < translationParams.length; ++i) {
                params[i] = BubbleTextUtils.getTranslatedText((String)translationParams[i].getValue(), new Object[0]);
            }
        }
        else {
            params = new String[0];
        }
        final String sentence = WakfuTranslator.getInstance().getString(translationKey, (Object[])params);
        final ChatMessage chatMessage = new ChatMessage(character.getName(), mobileId, sentence);
        chatMessage.setPipeDestination(1);
        chatMessage.setResizable(resizable);
        FightActionHelper.executeInFight(new Runnable() {
            @Override
            public void run() {
                ChatManager.getInstance().pushMessage(chatMessage);
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetCharacterSpeak.class);
    }
}
