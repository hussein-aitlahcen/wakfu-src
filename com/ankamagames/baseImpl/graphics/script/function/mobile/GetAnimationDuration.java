package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetAnimationDuration extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getAnimationDuration";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetAnimationDuration(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getAnimationDuration";
    }
    
    @Override
    public String getDescription() {
        return "Renvoi la dur?e de l'animation en ms";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetAnimationDuration.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetAnimationDuration.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(GetAnimationDuration.m_logger, "le mobile " + mobileId + "n'existe pas");
            this.addReturnNilValue();
            return;
        }
        final String anim = this.getParamString(1);
        int time = mobile.getAnimationDuration(anim);
        if (time == -1) {
            GetAnimationDuration.m_logger.warn((Object)("animation (" + anim + ") qui boucle"));
            time = 0;
        }
        this.addReturnValue(time);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetAnimationDuration.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationName", "Nom de l'animation", LuaScriptParameterType.STRING, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("time", "Dur?e de l'animation en ms (0 dans le cas d'une anim en boucle)", LuaScriptParameterType.INTEGER, false) };
    }
}
