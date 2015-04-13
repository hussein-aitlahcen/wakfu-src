package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class OnCellTransition extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final String NAME = "onCellTransition";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public OnCellTransition(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "onCellTransition";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return OnCellTransition.PARAMS;
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
                final int cellX = this.getParamInt(1);
                final int cellY = this.getParamInt(2);
                final String func = this.getParamString(3);
                final LuaValue[] params = this.getParams(4, paramCount);
                final int taskId = script.registerWaitingTask(func, params);
                final TargetPositionListener listener = new TargetPositionListener<PathMobile>() {
                    @Override
                    public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
                        if (worldX == cellX && worldY == cellY) {
                            mobile.removePositionListener(this);
                            script.executeWaitingTask(taskId);
                        }
                    }
                };
                pathMobile.addPositionListener(listener);
            }
        }
        else {
            this.writeError(OnCellTransition.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)OnCellTransition.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
