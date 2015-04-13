package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.process.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class ScrollTo extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public ScrollTo(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "scrollTo";
    }
    
    @Override
    public String getDescription() {
        return "Permet de d?finir la position de la cam?ra en effectuant un scrolling r?gulier";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", "Position x d?sir?e", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", "Position y d?sir?e", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", "Position z d?sir?e", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("function Name", "Fonction ? appeler lorsque la cam?ra est ariv? a destination", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("function Params", "Param?tres de la fonction ? appeler", LuaScriptParameterType.BLOOPS, true) };
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
            camera.setCameraMovementMode(InterpolationType.HARD_LINEAR);
            camera.setTrackingTarget(new DefaultIsoWorldTarget(x, y, z));
            camera.addCameraReachTargetListener(new ReachTargetListener() {
                @Override
                public void onTargetReached() {
                    camera.removeCameraReachTargetListener(this);
                    camera.setCameraMovementMode(InterpolationType.SMOOTH_LINEAR);
                }
            });
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
            this.writeError(ScrollTo.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScrollTo.class);
    }
}
