package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class ApplyEquipment extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "applyEquipment";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public ApplyEquipment(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "applyEquipment";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ApplyEquipment.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final int equipmentFileId = this.getParamInt(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.applyAllPartsFrom(equipmentFileId);
        }
        else {
            this.writeError(ApplyEquipment.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ApplyEquipment.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("equipmentFileId", null, LuaScriptParameterType.NUMBER, false) };
    }
}
