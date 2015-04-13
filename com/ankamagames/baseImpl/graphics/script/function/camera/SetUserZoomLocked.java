package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetUserZoomLocked extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetUserZoomLocked(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setUserZoomLocked";
    }
    
    @Override
    public String getDescription() {
        return "Emp?che l'utilisation de la molette pour zoomer";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("zoomLocked", "True pour bloquer, false sinon", LuaScriptParameterType.BOOLEAN, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene == null) {
            this.writeError(SetUserZoomLocked.m_logger, "pas de scene associ? ? CameraFunctionsLibrary");
            return;
        }
        final AleaIsoCamera camera = scene.getIsoCamera();
        if (camera == null) {
            this.writeError(SetUserZoomLocked.m_logger, "pas de camera associ? ? la scene");
            return;
        }
        camera.setUserZoomLocked(this.getParamBool(0));
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetUserZoomLocked.class);
    }
}
