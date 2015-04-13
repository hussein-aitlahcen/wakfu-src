package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.openAL.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlaySound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RETURN_VALUES;
    
    public PlaySound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public final String getName() {
        return "playSound";
    }
    
    @Override
    public String getDescription() {
        return "Joue un son (?v?nement sonore relativement court) au sein du monde.";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlaySound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return PlaySound.RETURN_VALUES;
    }
    
    public final void run(final int paramCount) throws LuaException {
        if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
            return;
        }
        final long soundFileId = this.getParamLong(0);
        final boolean isStereo = paramCount >= 2 && this.getParamBool(1);
        float gainMod;
        if (paramCount >= 3) {
            gainMod = (float)this.getParamDouble(2);
        }
        else {
            gainMod = 100.0f;
        }
        int playCount;
        if (paramCount >= 4) {
            playCount = this.getParamInt(3);
        }
        else {
            playCount = 1;
        }
        try {
            if (soundFileId != 0L) {
                final AudioSourceDefinition def = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gainMod / 100.0f, playCount, -1L, -1L, this.getScriptObject().getFightId());
                this.addReturnValue((def == null) ? -1L : def.getSoundUID());
            }
            else {
                PlaySound.m_logger.error((Object)"Id du son nul");
            }
        }
        catch (Exception e) {
            this.writeError(PlaySound.m_logger, "soundExtension or soundPath not initialized " + e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlaySound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundFileId", "Id du fichier son", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("isStereo", "Activation de la st?r?o", LuaScriptParameterType.BOOLEAN, true), new LuaScriptParameterDescriptor("gainModification", "Entier compris entre 0 et 200 % sp?cifiant une modification du gain", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("playCount", "Nombre de fois ou le son doit ?tre jou?", LuaScriptParameterType.NUMBER, true) };
        RETURN_VALUES = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("fileId", "uid du son cr??", LuaScriptParameterType.LONG, false) };
    }
}
