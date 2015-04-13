package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

public class ZoomOn extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public ZoomOn(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getDescription() {
        return "Zoom sur un point pendant un certain temps puis revient ? sa position d'origine";
    }
    
    @Override
    public String getName() {
        return "zoomOn";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("zoomFactor", "Niveau de zoom de la cam?ra (compris entre 0.9 et 1.9)", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("time", "Temps au bout duquel la cam?ra devra revenir ? sa position d'origine", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("x", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", "Position z", LuaScriptParameterType.NUMBER, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final float value = this.getParamFloat(0);
        final int time = this.getParamInt(1);
        final float x = this.getParamFloat(2);
        final float y = this.getParamFloat(3);
        final float z = this.getParamFloat(4);
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            final IsoCamera camera = scene.getIsoCamera();
            final IsoWorldTarget oldTarget = camera.getTrackingTarget();
            final float oldX = oldTarget.getWorldX();
            final float oldY = oldTarget.getWorldY();
            final float oldZ = oldTarget.getAltitude();
            final float oldZoomFactor = camera.getZoomFactor();
            camera.setTrackingTarget(new DefaultIsoWorldTarget(x, y, z));
            scene.setDesiredZoomFactor(value);
            final LuaScript scriptObject = this.getScriptObject();
            if (oldTarget instanceof Mobile) {
                final long mobileId = ((Mobile)oldTarget).getId();
                final LuaValue[] args1 = { new LuaValue(mobileId) };
                scriptObject.registerTimedTask(time, 1, "attachCameraToMobile", args1);
                final LuaValue[] args2 = { new LuaValue(oldZoomFactor) };
                scriptObject.registerTimedTask(time, 1, "setZoomFactor", args2);
            }
            else {
                final LuaValue[] params = { new LuaValue(oldZoomFactor), new LuaValue(oldX), new LuaValue(oldY), new LuaValue(oldZ) };
                scriptObject.registerTimedTask(time, 1, "SetCamera", params);
            }
        }
        else {
            this.writeError(ZoomOn.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZoomOn.class);
    }
}
