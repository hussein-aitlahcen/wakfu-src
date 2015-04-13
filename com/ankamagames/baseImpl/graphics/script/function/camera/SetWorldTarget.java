package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetWorldTarget extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetWorldTarget(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setWorldTarget";
    }
    
    @Override
    public String getDescription() {
        return "permet de d?finir la position de la cam?ra";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("coordX", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("coordY", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("altitude", "Position z", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("funcName", "Fonction a appeler lorsque la cam?ra est centr?", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("funcParams", "Param?tres de la fonction a appeler", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final float x = this.getParamFloat(0);
        final float y = this.getParamFloat(1);
        final float z = this.getParamFloat(2);
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            final AleaIsoCamera camera = scene.getIsoCamera();
            camera.setTrackingTarget(new DefaultIsoWorldTarget(x, y, z));
            if (paramCount > 3) {
                final LuaScript script = this.getScriptObject();
                final String func = this.getParamString(3);
                final LuaValue[] params = this.getParams(4, paramCount);
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
            this.writeError(SetWorldTarget.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetWorldTarget.class);
    }
}
