package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetMobilePosition extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getMobilePosition";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetMobilePosition(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobilePosition";
    }
    
    @Override
    public String getDescription() {
        return "R?cup?re la position d'un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobilePosition.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobilePosition.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        AnimatedElement mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            mobile = SimpleAnimatedElementManager.getInstance().getAnimatedElement(mobileId);
        }
        if (mobile != null) {
            this.addReturnValue(mobile.getWorldCellX());
            this.addReturnValue(mobile.getWorldCellY());
            this.addReturnValue((int)mobile.getAltitude());
        }
        else {
            this.writeError(GetMobilePosition.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
            this.addReturnNilValue();
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobilePosition.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'Id du mobile", LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", "Position z", LuaScriptParameterType.INTEGER, false) };
    }
}
