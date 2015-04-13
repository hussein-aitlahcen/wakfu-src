package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;

final class AttachToMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    AttachToMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "attachToMobile";
    }
    
    @Override
    public String getDescription() {
        return "Applique une bulle ? un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("bubbleId", "Id de la bulle", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("mobileId", "Id du mobile auquel lier la bulle", LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int bubbleId = this.getParamInt(0);
        final InteractiveBubble bubble = BubbleManager.getInstance().getInteractiveBubble(bubbleId);
        if (bubble == null) {
            this.writeError(AttachToMobile.m_logger, "pas de bulle de texte " + bubbleId);
            return;
        }
        final long mobileId = this.getParamLong(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            bubble.setTarget(mobile);
            bubble.screenTargetMoved(mobile, mobile.getScreenX(), mobile.getScreenY(), mobile.getScreenTargetHeight());
        }
        else {
            this.writeError(AttachToMobile.m_logger, "mobile inconnu " + mobileId);
        }
        AttachToMobile.m_logger.info((Object)("attachtoMobile " + bubble + ' ' + mobileId));
    }
    
    static {
        m_logger = Logger.getLogger((Class)AttachToMobile.class);
    }
}
