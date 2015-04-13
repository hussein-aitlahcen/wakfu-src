package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;

public final class GetCharacteristicValue extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public GetCharacteristicValue(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getCharacteristicValue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("charac", null, LuaScriptParameterType.STRING, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characValue", null, LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long characterId = this.getParamLong(0);
        final String characteristicName = this.getParamString(1);
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
        if (character == null) {
            GetCharacteristicValue.m_logger.error((Object)("On veut recup la valeur d'une charac sur un perso qui n'existe pas  :" + characterId));
            this.addReturnNilValue();
            return;
        }
        if (!character.hasCharacteristic(FighterCharacteristicType.valueOf(characteristicName))) {
            GetCharacteristicValue.m_logger.error((Object)("On veut recup la valeur d'une charac qu'un perso ne possede pas : " + characteristicName));
            this.addReturnNilValue();
            return;
        }
        this.addReturnValue(character.getCharacteristicValue(FighterCharacteristicType.valueOf(characteristicName)));
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetCharacteristicValue.class);
    }
}
