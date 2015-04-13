package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetZoomSpeedFactor extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetZoomSpeedFactor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setZoomSpeedFactor";
    }
    
    @Override
    public String getDescription() {
        return "D?finit la vitesse ? laquelle une modification de zoom s'applique";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("speed", "Float d?finissant la vitesse ? laquelle le zoom se modifie par rapport ? la vitesse de base", LuaScriptParameterType.NUMBER, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            scene.getIsoCamera().setZoomSpeedFactor((float)this.getParamDouble(0));
        }
        else {
            this.writeError(SetZoomSpeedFactor.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetZoomSpeedFactor.class);
    }
}
