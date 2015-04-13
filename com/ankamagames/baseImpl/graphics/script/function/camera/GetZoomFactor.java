package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class GetZoomFactor extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public GetZoomFactor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getZoomFactor";
    }
    
    @Override
    public String getDescription() {
        return "R?cup?re le niveau de zoom actuel de la cam?ra";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("zoomValue", "le niveau de zoom actuel de la cam?ra, entre 0.9 et 1.9", LuaScriptParameterType.NUMBER, false) };
    }
    
    public void run(final int paramCount) throws LuaException {
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            final float zoomValue = scene.getDesiredZoomFactor();
            this.addReturnValue(zoomValue);
        }
        else {
            this.writeError(GetZoomFactor.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetZoomFactor.class);
    }
}
