package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetDistanceBetweenMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getDistanceBetweenMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetDistanceBetweenMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getDistanceBetweenMobile";
    }
    
    @Override
    public String getDescription() {
        return "Retourne la distance entre deux mobiles en nombre de cases";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetDistanceBetweenMobile.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetDistanceBetweenMobile.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long ida = this.getParamLong(0);
        final long idb = this.getParamLong(1);
        final Mobile mobile1 = MobileManager.getInstance().getMobile(ida);
        if (mobile1 == null) {
            this.writeError(GetDistanceBetweenMobile.m_logger, "le mobile " + ida + "n'existe pas");
            this.addReturnNilValue();
            return;
        }
        final Mobile mobile2 = MobileManager.getInstance().getMobile(idb);
        if (mobile2 == null) {
            this.writeError(GetDistanceBetweenMobile.m_logger, "le mobile " + idb + "n'existe pas");
            this.addReturnNilValue();
            return;
        }
        this.addReturnValue(DistanceUtils.getIntersectionDistance(mobile1, mobile2));
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetDistanceBetweenMobile.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileIdA", "L'Id du premier mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("mobileIdB", "L'id du second mobile", LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("distance", "La distance entre les deux mobiles (deltaX + deltaY)", LuaScriptParameterType.INTEGER, false) };
    }
}
