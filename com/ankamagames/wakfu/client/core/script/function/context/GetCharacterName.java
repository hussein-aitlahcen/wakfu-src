package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;

public final class GetCharacterName extends JavaFunctionEx
{
    public GetCharacterName(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getCharacterName";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterName", null, LuaScriptParameterType.STRING, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(this.getParamLong(0));
        this.addReturnValue((character != null) ? character.getName() : "");
    }
}
