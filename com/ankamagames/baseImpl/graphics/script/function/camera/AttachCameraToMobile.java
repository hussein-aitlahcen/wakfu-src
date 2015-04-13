package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class AttachCameraToMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    
    public AttachCameraToMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "attachCameraToMobile";
    }
    
    @Override
    public String getDescription() {
        return "Attache la cam?ra ? un mobile. Quand le mobile se d?place, la cam?ra continue de le suivre.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "id du mobile ? suivre", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("funcName", "Fonction ? appeler lorsque la cam?ra est centr?e sur le mobile", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("funcParams", "Param?tres de la fonction appel?e", LuaScriptParameterType.BLOOPS, true) };
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
            final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
            if (scene != null) {
                final IsoCamera camera = scene.getIsoCamera();
                camera.setTrackingTarget(mobile);
                if (paramCount > 1) {
                    final LuaScript script = this.getScriptObject();
                    final String func = this.getParamString(1);
                    final LuaValue[] params = this.getParams(2, paramCount);
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
                this.writeError(AttachCameraToMobile.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
            }
        }
        else {
            this.writeError(AttachCameraToMobile.m_logger, "mobile inconnu " + mobileId);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AttachCameraToMobile.class);
    }
}
