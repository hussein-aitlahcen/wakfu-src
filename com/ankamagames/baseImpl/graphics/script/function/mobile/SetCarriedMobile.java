package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetCarriedMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setCarriedMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetCarriedMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setCarriedMobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetCarriedMobile.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long carrierId = this.getParamLong(0);
        final boolean carry = paramCount > 1;
        final Mobile carrier = MobileManager.getInstance().getMobile(carrierId);
        if (carrier == null) {
            this.writeError(SetCarriedMobile.m_logger, "Pas de carrier trouv? avec l'id " + carrierId);
            return;
        }
        if (carry) {
            if (carrier.isCarrier()) {
                this.writeError(SetCarriedMobile.m_logger, "Le mobile " + carrierId + " porte deja qq ");
                return;
            }
            final long carriedId = this.getParamLong(1);
            final Mobile carried = MobileManager.getInstance().getMobile(carriedId);
            if (carried != null && carried.isCarried()) {
                this.writeError(SetCarriedMobile.m_logger, "Le mobile " + carrierId + " est deja port? ou est null ");
                return;
            }
            carrier.carry(carried);
        }
        else {
            if (!carrier.isCarrier()) {
                this.writeError(SetCarriedMobile.m_logger, "Le mobile " + carrierId + " ne porte personne ");
                return;
            }
            carrier.uncarry();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetCarriedMobile.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("carrierId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("carriedId", null, LuaScriptParameterType.LONG, true) };
    }
}
