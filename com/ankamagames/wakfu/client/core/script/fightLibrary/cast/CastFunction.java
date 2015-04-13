package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;

abstract class CastFunction extends JavaFunctionEx
{
    protected final AbstractFightCastAction m_castAction;
    
    CastFunction(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState);
        this.m_castAction = castAction;
    }
}
