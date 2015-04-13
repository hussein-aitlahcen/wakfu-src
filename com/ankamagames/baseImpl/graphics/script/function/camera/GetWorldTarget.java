package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class GetWorldTarget extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public GetWorldTarget(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getWorldTarget";
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer la position actuelle de la cam?ra";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("posY", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("posZ", "Position z", LuaScriptParameterType.NUMBER, false) };
    }
    
    public void run(final int paramCount) throws LuaException {
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            final AleaIsoCamera camera = scene.getIsoCamera();
            this.addReturnValue(camera.getCameraExactIsoWorldX());
            this.addReturnValue(camera.getCameraExactIsoWorldY());
            this.addReturnValue(camera.getAltitude());
        }
        else {
            this.writeError(GetWorldTarget.m_logger, "Pas de scene associ? ? CameraFunctionsLibrary");
            this.addReturnNilValue();
            this.addReturnNilValue();
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetWorldTarget.class);
    }
}
