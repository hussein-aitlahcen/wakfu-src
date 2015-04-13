package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.group.field.*;
import com.ankamagames.framework.sound.group.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlayLocalSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public PlayLocalSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "playLocalSound";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayLocalSound.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int rollOff = this.getParamInt(0);
        final long soundFileId = this.getParamLong(1);
        final int gain = this.getParamInt(2);
        final int x = this.getParamInt(3);
        final int y = this.getParamInt(4);
        final int z = this.getParamInt(5);
        try {
            if (soundFileId > 0L) {
                SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain / 100.0f, 1, -1L, -1L, -1, new StaticPositionProvider(x, y, z, false, 0), rollOff);
            }
            else {
                PlayLocalSound.m_logger.warn((Object)"Pas de son sp?cifi? (ID<=0)");
            }
        }
        catch (Exception e) {
            this.writeError(PlayLocalSound.m_logger, "soundExtension or soundPath not initialized");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayLocalSound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("rollOff", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("soundFileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("gain", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
