package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class IncrementMobileDeltaZ extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "incrementMobileDeltaZ";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public IncrementMobileDeltaZ(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "incrementMobileDeltaZ";
    }
    
    @Override
    public String getDescription() {
        return "Place le mobile sur un autre plan. Deux personnage plac?s ? la m?me coordonn?e seront tri? selon leur deltaZ, il suffit d'incr?menter le deltaZ d'un perso pour que celui-ci soit affich? devant.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return IncrementMobileDeltaZ.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final int deltaZ = this.getParamInt(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setDeltaZ(mobile.getDeltaZ() + deltaZ);
        }
        else {
            this.writeError(IncrementMobileDeltaZ.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileNext4Direction.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("deltaZ", "La valeur ? ajouter (peut ?tre n?gatif)", LuaScriptParameterType.INTEGER, false) };
    }
}
