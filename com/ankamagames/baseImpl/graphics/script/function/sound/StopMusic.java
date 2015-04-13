package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

public class StopMusic extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public StopMusic(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "stopMusic";
    }
    
    @Override
    public String getDescription() {
        return "Stop une musique en cours de lecture";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.writeError(StopMusic.m_logger, "stop music not yet implemented");
    }
    
    static {
        m_logger = Logger.getLogger((Class)StopMusic.class);
    }
}
