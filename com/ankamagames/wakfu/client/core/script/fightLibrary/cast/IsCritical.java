package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class IsCritical extends CastFunction
{
    private static final String NAME = "isCritical";
    private static final String DESC = "Permet de r?cup?rer savoir si l'action a d?clench? un coup critique";
    private static final LuaScriptParameterDescriptor[] IS_CRITICAL_RESULTS;
    
    IsCritical(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "isCritical";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return IsCritical.IS_CRITICAL_RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_castAction.isCriticalHit());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer savoir si l'action a d?clench? un coup critique";
    }
    
    static {
        IS_CRITICAL_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("criticalHit", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
