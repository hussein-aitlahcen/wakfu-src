package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class OnPathEnded extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final String NAME = "onPathEnded";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public OnPathEnded(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "onPathEnded";
    }
    
    @Override
    public String getDescription() {
        return "Apelle une fonction quand le mobile est arriv? a destination";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return OnPathEnded.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            if (mobile instanceof PathMobile) {
                final PathMobile pathMobile = (PathMobile)mobile;
                final LuaScript script = this.getScriptObject();
                final String func = this.getParamString(1);
                final LuaValue[] params = this.getParams(2, paramCount);
                final int taskId = script.registerWaitingTask(func, params);
                final MobileEndPathListener listener = new MobileEndPathListener() {
                    @Override
                    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                        mobile.removeEndPositionListener(this);
                        script.executeWaitingTask(taskId);
                    }
                };
                pathMobile.addEndPositionListener(listener);
            }
        }
        else {
            this.writeError(OnPathEnded.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)OnPathEnded.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("funcName", "Fonction ? appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Param?tres de la fonction ? appeler", LuaScriptParameterType.BLOOPS, true) };
    }
}
