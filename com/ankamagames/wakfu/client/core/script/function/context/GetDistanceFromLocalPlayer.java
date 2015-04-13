package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;

public class GetDistanceFromLocalPlayer extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public GetDistanceFromLocalPlayer(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getDistanceFromLocalPlayer";
    }
    
    @Override
    public String getDescription() {
        return "Retourne la distance entre le joueur et le personnage d'id donn? en param?tre";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", "Id du character", LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("distance", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long characterId = this.getParamLong(0);
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
        if (character == null) {
            GetDistanceFromLocalPlayer.m_logger.error((Object)("On veut recup la distance s?parant le joueur d'un personnage inexistant :" + characterId));
            this.addReturnNilValue();
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.addReturnValue(character.getPosition().getDistance(localPlayer.getPosition()));
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetDistanceFromLocalPlayer.class);
    }
}
