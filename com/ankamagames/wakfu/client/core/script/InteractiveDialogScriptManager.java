package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.openAL.*;
import org.keplerproject.luajava.*;

public final class InteractiveDialogScriptManager
{
    private static Logger m_scriptsLogger;
    public static final InteractiveDialogScriptManager INSTANCE;
    private static final String INTERACTIVE_DIALOGS_PATH = "interactiveDialogs/";
    private final TIntIntHashMap m_scripts;
    private JavaFunctionsLibrary[] m_libs;
    private static long m_soundUID;
    
    private InteractiveDialogScriptManager() {
        super();
        this.m_scripts = new TIntIntHashMap();
        this.m_libs = new JavaFunctionsLibrary[] { new InnerSoundFunctionsLibrary() };
    }
    
    public void stopSound() {
        final AudioSource source = AudioSourceManager.getInstance().getAudioSource(InteractiveDialogScriptManager.m_soundUID);
        if (source != null) {
            source.setStopOnNullGain(true);
            source.fade(0.0f, 0.2f);
        }
        InteractiveDialogScriptManager.m_soundUID = -1L;
    }
    
    public String createDialogFileName(final String fullId) {
        assert LuaManager.getInstance().getPath() != null;
        return String.format("%s%s%s", "interactiveDialogs/", fullId, LuaManager.getInstance().getExtension());
    }
    
    public void playDialogSound(final String fullId) {
        this.runScript(fullId);
    }
    
    private void runScript(final String fullId) {
        final String fileName = this.createDialogFileName(fullId);
        final boolean exists = FileHelper.isExistingFile(LuaManager.getInstance().getPath() + fileName);
        if (exists) {
            LuaManager.getInstance().runScript(fileName, this.m_libs);
        }
    }
    
    static {
        InteractiveDialogScriptManager.m_scriptsLogger = Logger.getLogger("InteractiveDialogScripts");
        INSTANCE = new InteractiveDialogScriptManager();
        InteractiveDialogScriptManager.m_soundUID = -1L;
    }
    
    public static class InnerSoundFunctionsLibrary extends JavaFunctionsLibrary
    {
        @Override
        public final String getName() {
            return "Sound";
        }
        
        @Override
        public String getDescription() {
            return "NO Description<br/>Please Dev... implement me!";
        }
        
        @Override
        public JavaFunctionEx[] createFunctions(final LuaState luaState) {
            return new JavaFunctionEx[] { new PlaySound(luaState), new PlayRandomSound(luaState) };
        }
        
        @Override
        public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
            return null;
        }
        
        public static class PlayRandomSound extends JavaFunctionEx
        {
            private static int[] m_shuffleIndex;
            private static int m_offset;
            
            public PlayRandomSound(final LuaState luaState) {
                super(luaState);
            }
            
            private void shuffle(final int size) {
                final TIntArrayList list = new TIntArrayList(size);
                for (int i = 0; i < size; ++i) {
                    list.add(i);
                }
                list.shuffle(MathHelper.RANDOM);
                PlayRandomSound.m_shuffleIndex = list.toNativeArray();
                PlayRandomSound.m_offset = 0;
            }
            
            @Override
            public final String getName() {
                return "playRandomSound";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundId, gain", null, LuaScriptParameterType.BLOOPS, true) };
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
                    return;
                }
                if (paramCount % 2 != 0) {
                    return;
                }
                final int numSounds = paramCount / 2;
                if (PlayRandomSound.m_shuffleIndex == null || numSounds != PlayRandomSound.m_shuffleIndex.length) {
                    this.shuffle(numSounds);
                }
                final int choice = PlayRandomSound.m_shuffleIndex[PlayRandomSound.m_offset];
                if (PlayRandomSound.m_offset < PlayRandomSound.m_shuffleIndex.length - 1) {
                    ++PlayRandomSound.m_offset;
                }
                else {
                    this.shuffle(numSounds);
                }
                final long soundId = this.getParamLong(2 * choice);
                final int gainMod = this.getParamInt(2 * choice + 1);
                try {
                    if (soundId != 0L) {
                        final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundId, gainMod / 100.0f, 1, -1L, -1L, -1);
                        if (source != null) {
                            InteractiveDialogScriptManager.m_soundUID = source.getSoundUID();
                        }
                    }
                    else {
                        InnerSoundFunctionsLibrary.m_logger.debug((Object)"Id du son nul");
                    }
                }
                catch (Exception e) {
                    InnerSoundFunctionsLibrary.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
                }
            }
            
            static {
                PlayRandomSound.m_shuffleIndex = null;
            }
        }
        
        public static class PlaySound extends JavaFunctionEx
        {
            public PlaySound(final LuaState luaState) {
                super(luaState);
            }
            
            @Override
            public final String getName() {
                return "playSound";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundFileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("gainModification", null, LuaScriptParameterType.NUMBER, true) };
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                final long soundFileId = this.getParamLong(0);
                float gainMod;
                if (paramCount >= 2) {
                    gainMod = (float)this.getParamDouble(1);
                }
                else {
                    gainMod = 100.0f;
                }
                try {
                    if (soundFileId != 0L) {
                        final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gainMod / 100.0f, 1, -1L, -1L, -1);
                        if (source != null) {
                            InteractiveDialogScriptManager.m_soundUID = source.getSoundUID();
                        }
                    }
                    else {
                        InnerSoundFunctionsLibrary.m_logger.debug((Object)"Id du son nul");
                    }
                }
                catch (Exception e) {
                    InnerSoundFunctionsLibrary.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
                }
            }
        }
    }
}
