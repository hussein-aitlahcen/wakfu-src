package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetCamera extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetCamera(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getDescription() {
        return "Positionne la cam?ra sur un point";
    }
    
    @Override
    public String getName() {
        return "setCamera";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("zoomFactor", "Niveau de zoom d?sir? (entre 0.9 et 1.9)", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("x", "Position x d?sir?e", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", "Position y d?sir?e", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", "Position z d?sir?e", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("funcName", "Fonction a appeler lorsque la cam?ra est centr? sur le point", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("funcParams", "Param?tres de la fonction a appeler", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final float value = (float)this.getParamDouble(0);
        final float x = (float)this.getParamDouble(1);
        final float y = (float)this.getParamDouble(2);
        final float z = (float)this.getParamDouble(3);
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            final IsoCamera camera = scene.getIsoCamera();
            camera.setTrackingTarget(new DefaultIsoWorldTarget(x, y, z));
            camera.setDesiredZoomFactor(value);
            if (paramCount > 4) {
                final LuaScript script = this.getScriptObject();
                final String func = this.getParamString(4);
                final LuaValue[] params = this.getParams(5, paramCount);
                camera.addCameraReachTargetListener(new ReachTargetListener() {
                    @Override
                    public void onTargetReached() {
                        camera.removeCameraReachTargetListener(this);
                        script.runFunction(func, params, new LuaTable[0]);
                    }
                });
            }
        }
        else {
            this.writeError(SetCamera.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetCamera.class);
    }
}
