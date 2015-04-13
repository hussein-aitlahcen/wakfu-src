package com.ankamagames.baseImpl.graphics.script.function.world;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.keplerproject.luajava.*;

public class SetPictoLayerAlpha extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public SetPictoLayerAlpha(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setPictoLayerAlpha";
    }
    
    @Override
    public String getDescription() {
        return "Modifie l'alpha d'un layer de pictos dans le monde (en faisant un fondu)";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("layer", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("alpha", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("time", null, LuaScriptParameterType.INTEGER, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final short layer = (short)this.getParamInt(0);
        final int alpha = this.getParamInt(1);
        if (paramCount == 3) {
            HiddenElementManager.getInstance().setByLayer(layer, 1.0f, 1.0f, 1.0f, alpha, this.getParamInt(2));
        }
        else {
            HiddenElementManager.getInstance().setByLayer(layer, 1.0f, 1.0f, 1.0f, alpha);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetPictoLayerAlpha.class);
    }
}
