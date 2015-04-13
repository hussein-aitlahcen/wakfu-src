package com.ankamagames.baseImpl.graphics.script.function.camera;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetMoveSpeedFactor extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetMoveSpeedFactor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMoveSpeedFactor";
    }
    
    @Override
    public String getDescription() {
        return "D?finit la vitesse ? laquelle la camera se d?place par rapport ? la vitesse de base";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("speedFactor", "Vitesse de d?placement", LuaScriptParameterType.NUMBER, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final AleaWorldScene scene = CameraFunctionsLibrary.getInstance().getWorldScene();
        if (scene != null) {
            scene.getIsoCamera().setMoveSpeedFactor((float)this.getParamDouble(0));
        }
        else {
            this.writeError(SetMoveSpeedFactor.m_logger, "pas de camera associ?e ? CameraFunctionsLibrary");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMoveSpeedFactor.class);
    }
}
