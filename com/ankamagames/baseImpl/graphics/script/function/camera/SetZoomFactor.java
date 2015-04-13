package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetZoomFactor extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetZoomFactor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setZoomFactor";
    }
    
    @Override
    public String getDescription() {
        return "D?fini le niveau de zoom de la cam?ra";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("zoomValue", "Float compris entre 0.9 et 1.9 indiquant le nouveau niveau de zoom de la cam?ra", LuaScriptParameterType.NUMBER, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final float value = this.getParamFloat(0);
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            scene.setDesiredZoomFactor(value);
        }
        else {
            this.writeError(SetZoomFactor.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetZoomFactor.class);
    }
}
