package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.script.*;

public final class SetCharacteristicListenerOn extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetCharacteristicListenerOn(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setCharacteristicListenerOn";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("charac", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("functionOnUpdate", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("d?charger a la premiere utilisation", null, LuaScriptParameterType.BOOLEAN, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long characterId = this.getParamLong(0);
        final String characteristicName = this.getParamString(1);
        final String functionOnUpdate = this.getParamString(2);
        final boolean removeListenerAfterUse = this.getParamBool(3);
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
        if (character == null) {
            SetCharacteristicListenerOn.m_logger.error((Object)("On veut ajouter un listener sur un perso qui n'existe pas  :" + characterId));
            return;
        }
        if (!character.hasCharacteristic(FighterCharacteristicType.valueOf(characteristicName))) {
            SetCharacteristicListenerOn.m_logger.error((Object)("On veut ajouter un listener a une charac qu'un perso ne possede pas : " + characteristicName));
            return;
        }
        final FighterCharacteristic characteristic = character.getCharacteristic((CharacteristicType)FighterCharacteristicType.valueOf(characteristicName));
        characteristic.addListener(new CharacteristicUpdateListener() {
            @Override
            public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
                try {
                    SetCharacteristicListenerOn.this.getScriptObject().runFunction(functionOnUpdate);
                    if (removeListenerAfterUse) {
                        characteristic.removeListener(this);
                    }
                }
                catch (LuaException e) {
                    SetCharacteristicListenerOn.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetCharacteristicListenerOn.class);
    }
}
