package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class GetCarriedMobileId extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    public GetCarriedMobileId(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getCarriedMobileId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetCarriedMobileId.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCarriedMobileId.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile carrier = MobileManager.getInstance().getMobile(mobileId);
        if (carrier == null) {
            this.writeError(GetCarriedMobileId.m_logger, "Pas de carrier trouv? avec l'id " + mobileId);
            this.addReturnValue(1L);
            return;
        }
        final Mobile carriedMobile = carrier.getCarriedMobile();
        if (carriedMobile == null) {
            this.writeError(GetCarriedMobileId.m_logger, "Pas de carrier port? trouv? avec l'id " + mobileId);
            this.addReturnValue(1L);
            return;
        }
        this.addReturnValue(carriedMobile.getId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetCarriedMobileId.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("carrierMobileId", null, LuaScriptParameterType.LONG, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("carriedMobileId", null, LuaScriptParameterType.LONG, false) };
    }
}
