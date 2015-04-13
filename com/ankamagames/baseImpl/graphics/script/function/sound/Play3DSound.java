package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class Play3DSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public Play3DSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "play3DSound";
    }
    
    @Override
    public String getDescription() {
        return "Joue un son (?v?nement sonore relativement court) plac? ? une position sp?cifique du monde.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return Play3DSound.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long soundFileId = this.getParamLong(0);
        final int x = this.getParamInt(1);
        final int y = this.getParamInt(2);
        final int z = this.getParamInt(3);
        final boolean isLoop = paramCount >= 5 && this.getParamBool(4);
        final boolean isStereo = paramCount >= 6 && this.getParamBool(5);
        try {
            if (soundFileId > 0L) {
                SoundFunctionsLibrary.getInstance().playSound(soundFileId, 1.0f, isLoop ? 0 : 1, -1L, -1L, this.getScriptObject().getFightId());
            }
            else {
                Play3DSound.m_logger.warn((Object)"Pas de son sp?cifi? (ID<=0)");
            }
        }
        catch (Exception e) {
            this.writeError(Play3DSound.m_logger, "soundExtension or soundPath not initialized");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Play3DSound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundFileId", "Id du fichier son", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("posX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", "Position z", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("isLoop", "Si le son boucle", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("isStereo", "Si on active le mode st?r?o", LuaScriptParameterType.BOOLEAN, true) };
    }
}
