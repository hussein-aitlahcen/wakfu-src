package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class LoopSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public LoopSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public final String getName() {
        return "loopSound";
    }
    
    @Override
    public String getDescription() {
        return "Joue un son (?v?nement sonore relativement court) en boucle pendant un temps fix?.";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return LoopSound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public final void run(final int paramCount) throws LuaException {
        final long soundFileId = this.getParamLong(0);
        int loopTime = 0;
        final boolean isStereo = paramCount >= 2 && this.getParamBool(1);
        float gainMod;
        if (paramCount >= 3) {
            gainMod = (float)this.getParamDouble(2);
        }
        else {
            gainMod = 100.0f;
        }
        if (paramCount >= 4) {
            loopTime = this.getParamInt(3);
        }
        long fadeOutTime;
        if (paramCount >= 5) {
            fadeOutTime = this.getParamInt(4);
        }
        else {
            fadeOutTime = 0L;
        }
        try {
            if (soundFileId != 0L) {
                final boolean b = loopTime > 0;
                final long endDate = b ? (System.currentTimeMillis() + loopTime) : -1L;
                SoundFunctionsLibrary.getInstance().playSound(soundFileId, gainMod / 100.0f, b ? 0 : 1, endDate, fadeOutTime, this.getScriptObject().getFightId());
            }
            else {
                LoopSound.m_logger.error((Object)"Id du son nul");
            }
        }
        catch (Exception e) {
            this.writeError(LoopSound.m_logger, "soundExtension or soundPath not initialized");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlaySound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundFileId", "id du fichier son", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("isStereo", "Activation de mode st?r?o", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("gainMod", "Entier compris entre 0 et 200 sp?cifiant un gain en %", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("loopingTime", "Temps (en ms) pendant lequel le son doit ?tre boucl?", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("fadeOut time", "Temps (en ms) ? partir duquel le volume du son foit ?tre diminu?", LuaScriptParameterType.NUMBER, true) };
    }
}
