package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class GetMobileAtCoordinates extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    public GetMobileAtCoordinates(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobileAtCoordinates";
    }
    
    @Override
    public String getDescription() {
        return "Permet de r\u00e9cup\u00e9rer l'id d'un mobile \u00e0 des coordonn\u00e9es donn\u00e9es";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobileAtCoordinates.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobileAtCoordinates.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int x = this.getParamInt(0);
        final int y = this.getParamInt(1);
        final Mobile mobile = MobileManager.getInstance().getNearestElementAtCoordinates(x, y);
        if (mobile == null) {
            this.writeError(GetMobileAtCoordinates.m_logger, "Pas de mobile trouv\u00e9 aux coordonn\u00e9es " + x + ", " + y);
            this.addReturnValue(1L);
            return;
        }
        this.addReturnValue(mobile.getId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobileAtCoordinates.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", "x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", "y", LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "l'id du mobile", LuaScriptParameterType.LONG, false) };
    }
}
