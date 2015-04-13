package com.ankamagames.wakfu.client.core.script.fightLibrary.fightActionGroupFunctionLibrary;

import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class FightActionFunctionLibrary extends JavaFunctionsLibrary
{
    public static final FightActionFunctionLibrary INSTANCE;
    private static final String NAME = "FightAction";
    private static final String DESC = "Librairie qui permet de manipuler les groupes d'actions";
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new AddActionToPendingGroup(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public String getName() {
        return "FightAction";
    }
    
    @Override
    public String getDescription() {
        return "Librairie qui permet de manipuler les groupes d'actions";
    }
    
    static {
        INSTANCE = new FightActionFunctionLibrary();
    }
}
