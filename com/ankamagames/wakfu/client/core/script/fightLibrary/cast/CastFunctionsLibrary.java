package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class CastFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final String NAME = "Cast";
    private static final String DESC = "Permet de r?cup?rer des informations li?es aux actions de Cast en combat (utilisation de sort ou d'arme)";
    private final AbstractFightCastAction m_castAction;
    
    public CastFunctionsLibrary(final AbstractFightCastAction action) {
        super();
        this.m_castAction = action;
    }
    
    @Override
    public final String getName() {
        return "Cast";
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer des informations li?es aux actions de Cast en combat (utilisation de sort ou d'arme)";
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetCaster(luaState, this.m_castAction), new GetCasterBreed(luaState, this.m_castAction), new GetCasterInformation(luaState, this.m_castAction), new GetCasterPosition(luaState, this.m_castAction), new GetCasterSex(luaState, this.m_castAction), new GetLevel(luaState, this.m_castAction), new GetPosition(luaState, this.m_castAction), new GetValidInputGatePosition(luaState, this.m_castAction), new GetValidOutputGatePosition(luaState, this.m_castAction), new HasEffect(luaState, this.m_castAction), new HasState(luaState, this.m_castAction), new IsCritical(luaState, this.m_castAction), new IsTargetCellInRange(luaState, this.m_castAction) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
}
