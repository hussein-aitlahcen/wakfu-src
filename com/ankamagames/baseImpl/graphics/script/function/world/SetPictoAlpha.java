package com.ankamagames.baseImpl.graphics.script.function.world;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetPictoAlpha extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetPictoAlpha(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setPictoAlpha";
    }
    
    @Override
    public String getDescription() {
        return "Modifie l'alpha d'un picto dans le monde selon ces coordonn?es (en faisant un fondu)";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("z", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("alpha", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("time", null, LuaScriptParameterType.INTEGER, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int x = this.getParamInt(0);
        final int y = this.getParamInt(1);
        final int z = this.getParamInt(2);
        final long hash = HighLightManager.getHandle(x, y, z);
        final int alpha = this.getParamInt(3);
        if (paramCount == 5) {
            HiddenElementManager.getInstance().set(hash, 1.0f, 1.0f, 1.0f, alpha, this.getParamInt(4));
        }
        else {
            HiddenElementManager.getInstance().set(hash, 1.0f, 1.0f, 1.0f, alpha);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetPictoAlpha.class);
    }
}
