package com.ankamagames.baseImpl.graphics.script.function.world;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetPictoColor extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetPictoColor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setPictoColor";
    }
    
    @Override
    public String getDescription() {
        return "Modifie la couleur d'un picto dans le monde selon ces coordonn?es (en faisant un fondu)";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("z", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("r", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("g", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("b", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("alpha", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("time", null, LuaScriptParameterType.INTEGER, true) };
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
        final int red = this.getParamInt(3);
        final int green = this.getParamInt(4);
        final int blue = this.getParamInt(5);
        final int alpha = this.getParamInt(6);
        if (paramCount == 8) {
            HiddenElementManager.getInstance().set(hash, red, green, blue, alpha, this.getParamInt(7));
        }
        else {
            HiddenElementManager.getInstance().set(hash, red, green, blue, alpha);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetPictoColor.class);
    }
}
