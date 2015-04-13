package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class LinkMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "linkMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public LinkMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "linkMobile";
    }
    
    @Override
    public String getDescription() {
        return "Attache un mobile ? un mobile source. Il sera attach? au point d'ancrage du mobile source et subira donc ses transformations.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return LinkMobile.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final long toLinkId = this.getParamLong(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(LinkMobile.m_logger, "le mobile source " + mobileId + " n'existe pas ");
            return;
        }
        final Mobile toLink = MobileManager.getInstance().getMobile(toLinkId);
        if (toLink == null) {
            this.writeError(LinkMobile.m_logger, "le mobile cible " + toLinkId + " n'existe pas ");
            return;
        }
        mobile.linkMobile(toLink);
    }
    
    static {
        m_logger = Logger.getLogger((Class)LinkMobile.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'id du mobile source", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("toLink", "L'Id du mobile ? attacher", LuaScriptParameterType.LONG, false) };
    }
}
