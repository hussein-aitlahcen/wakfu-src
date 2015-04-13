package com.ankamagames.baseImpl.graphics.sound;

import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.util.*;
import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.openAL.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.sound.ground.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;

public class LuaAnmActionRunScriptManager extends AnmActionRunScriptManager
{
    private static final LuaScriptParameterDescriptor[] PLAY_RANDOM_SOUND_PARAMS;
    private static final LuaScriptParameterDescriptor[] PLAY_SOUND_PARAMS;
    private static final LuaScriptParameterDescriptor[] PLAY_LOCAL_RANDOM_SOUND_PARAMS;
    private static final LuaScriptParameterDescriptor[] PLAY_LOCAL_SOUND_PARAMS;
    private static final LuaScriptParameterDescriptor[] PLAY_BARK_PARAMS;
    private static final LuaScriptParameterDescriptor[] PLAY_GROUND_SOUND_PARAMS;
    private static final String ANM_SCRIPTS = "anm/";
    
    private static String createParticleFileName(final long scriptId) {
        assert LuaManager.getInstance().getPath() != null;
        return String.format("%s%d%s", "anm/", scriptId, LuaManager.getInstance().getExtension());
    }
    
    @Override
    protected void doPlayAnmAction(final AnimatedObject elem, final long actionId) {
        final THashMap<String, Object> map = new THashMap<String, Object>(1);
        map.put("fightId", elem.getCurrentFightId());
        final InnerSoundFunctionsLibrary[] libs = { new InnerSoundFunctionsLibrary((AnimatedElement)elem) };
        LuaManager.getInstance().runScript(createParticleFileName(actionId), libs, map, null, false);
    }
    
    static {
        PLAY_RANDOM_SOUND_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("stopOnAnimationChange", null, LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("soundId, gain", null, LuaScriptParameterType.BLOOPS, true) };
        PLAY_SOUND_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundFileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("stopOnAnimationChange", null, LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("gainModification", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("playCount", null, LuaScriptParameterType.NUMBER, true) };
        PLAY_LOCAL_RANDOM_SOUND_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("rollOffPresetId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("stopOnAnimationChange", null, LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("soundId, gain", null, LuaScriptParameterType.BLOOPS, true) };
        PLAY_LOCAL_SOUND_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("rollOffPresetId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("stopOnAnimationChange", null, LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("soundFileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("gainModification", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("playCount", null, LuaScriptParameterType.NUMBER, true) };
        PLAY_BARK_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("barkId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("gain", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("breedId", null, LuaScriptParameterType.INTEGER, true) };
        PLAY_GROUND_SOUND_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("walkType", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("gain", null, LuaScriptParameterType.INTEGER, true) };
    }
    
    private static class InnerSoundFunctionsLibrary extends JavaFunctionsLibrary
    {
        private final Logger m_soundLogger;
        private final AnimatedElement m_element;
        
        private InnerSoundFunctionsLibrary(final AnimatedElement element) {
            super();
            this.m_soundLogger = Logger.getLogger((Class)InnerSoundFunctionsLibrary.class);
            this.m_element = element;
        }
        
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
            return new JavaFunctionEx[] { new PlaySound(luaState), new PlayRandomSound(luaState), new PlayLocalSound(luaState), new PlayLocalRandomSound(luaState), new PlayBark(luaState), new PlayGroundSound(luaState) };
        }
        
        @Override
        public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
            return null;
        }
        
        class PlayRandomSound extends JavaFunctionEx
        {
            public PlayRandomSound(final LuaState luaState) {
                super(luaState);
            }
            
            @Override
            public final String getName() {
                return "playRandomSound";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return LuaAnmActionRunScriptManager.PLAY_RANDOM_SOUND_PARAMS;
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!InnerSoundFunctionsLibrary.this.m_element.canPlaySound()) {
                    return;
                }
                if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
                    return;
                }
                if (paramCount % 2 != 1) {
                    return;
                }
                final boolean stopOnAnimationChange = this.getParamBool(0);
                final int choice = MathHelper.random(0, (paramCount - 1) / 2);
                final long soundId = this.getParamLong(2 * choice + 1);
                final int gainMod = this.getParamInt(2 * choice + 2);
                if (!InnerSoundFunctionsLibrary.this.m_element.getSoundValidator().canPlaySound(soundId)) {
                    return;
                }
                final long date = System.currentTimeMillis();
                if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, soundId)) {
                    return;
                }
                AnmActionRunScriptManager.getInstance().registerSound(date, soundId);
                try {
                    if (soundId != 0L) {
                        final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundId, gainMod / 100.0f, 1, -1L, -1L, this.getScriptObject().getFightId());
                        if (stopOnAnimationChange && source != null) {
                            InnerSoundFunctionsLibrary.this.m_element.addSoundRef(soundId, source.getSoundUID());
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
        
        class PlaySound extends JavaFunctionEx
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
                return LuaAnmActionRunScriptManager.PLAY_SOUND_PARAMS;
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!InnerSoundFunctionsLibrary.this.m_element.canPlaySound()) {
                    return;
                }
                if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
                    return;
                }
                final long soundFileId = this.getParamLong(0);
                if (!InnerSoundFunctionsLibrary.this.m_element.getSoundValidator().canPlaySound(soundFileId)) {
                    return;
                }
                final long date = System.currentTimeMillis();
                if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, soundFileId)) {
                    return;
                }
                AnmActionRunScriptManager.getInstance().registerSound(date, soundFileId);
                final boolean stopOnAnimationChange = this.getParamBool(1);
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
                        final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gainMod / 100.0f, playCount, -1L, -1L, this.getScriptObject().getFightId());
                        if (stopOnAnimationChange && source != null) {
                            InnerSoundFunctionsLibrary.this.m_element.addSoundRef(soundFileId, source.getSoundUID());
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
        
        class PlayLocalRandomSound extends JavaFunctionEx
        {
            public PlayLocalRandomSound(final LuaState luaState) {
                super(luaState);
            }
            
            @Override
            public final String getName() {
                return "playLocalRandomSound";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return LuaAnmActionRunScriptManager.PLAY_LOCAL_RANDOM_SOUND_PARAMS;
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!InnerSoundFunctionsLibrary.this.m_element.canPlaySound()) {
                    return;
                }
                if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
                    return;
                }
                final int rollOffPresetId = this.getParamInt(0);
                final boolean stopOnAnimationChange = this.getParamBool(1);
                if (paramCount % 2 != 0) {
                    return;
                }
                final int choice = MathHelper.random(0, (paramCount - 2) / 2);
                final long soundId = this.getParamLong(2 * choice + 2);
                final int gainMod = this.getParamInt(2 * choice + 3);
                if (!InnerSoundFunctionsLibrary.this.m_element.getSoundValidator().canPlaySound(soundId)) {
                    return;
                }
                final long date = System.currentTimeMillis();
                if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, soundId)) {
                    return;
                }
                AnmActionRunScriptManager.getInstance().registerSound(date, soundId);
                try {
                    if (soundId != 0L) {
                        final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundId, gainMod / 100.0f, 1, -1L, -1L, this.getScriptObject().getFightId(), InnerSoundFunctionsLibrary.this.m_element, rollOffPresetId);
                        if (stopOnAnimationChange && source != null) {
                            InnerSoundFunctionsLibrary.this.m_element.addSoundRef(soundId, source.getSoundUID());
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
        
        class PlayLocalSound extends JavaFunctionEx
        {
            public PlayLocalSound(final LuaState luaState) {
                super(luaState);
            }
            
            @Override
            public final String getName() {
                return "playLocalSound";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return LuaAnmActionRunScriptManager.PLAY_LOCAL_SOUND_PARAMS;
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!InnerSoundFunctionsLibrary.this.m_element.canPlaySound()) {
                    return;
                }
                if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
                    return;
                }
                final int presetId = this.getParamInt(0);
                final boolean stopOnAnimationChange = this.getParamBool(1);
                final long soundFileId = this.getParamLong(2);
                if (!InnerSoundFunctionsLibrary.this.m_element.getSoundValidator().canPlaySound(soundFileId)) {
                    return;
                }
                final long date = System.currentTimeMillis();
                if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, soundFileId)) {
                    return;
                }
                AnmActionRunScriptManager.getInstance().registerSound(date, soundFileId);
                float gainMod;
                if (paramCount >= 4) {
                    gainMod = (float)this.getParamDouble(3);
                }
                else {
                    gainMod = 100.0f;
                }
                int playCount;
                if (paramCount >= 5) {
                    playCount = this.getParamInt(4);
                }
                else {
                    playCount = 1;
                }
                try {
                    if (soundFileId != 0L) {
                        final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gainMod / 100.0f, playCount, -1L, -1L, this.getScriptObject().getFightId(), InnerSoundFunctionsLibrary.this.m_element, presetId);
                        if (stopOnAnimationChange && source != null) {
                            InnerSoundFunctionsLibrary.this.m_element.addSoundRef(soundFileId, source.getSoundUID());
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
        
        class PlayBark extends JavaFunctionEx
        {
            public PlayBark(final LuaState luaState) {
                super(luaState);
            }
            
            @Override
            public final String getName() {
                return "playBark";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return LuaAnmActionRunScriptManager.PLAY_BARK_PARAMS;
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!InnerSoundFunctionsLibrary.this.m_element.canPlaySound()) {
                    return;
                }
                final int barkId = this.getParamInt(0);
                int gain = 100;
                int breedId = -1;
                if (paramCount > 1) {
                    gain = this.getParamInt(1);
                }
                if (paramCount > 2) {
                    breedId = this.getParamInt(2);
                }
                try {
                    if (barkId != 0) {
                        final SoundFunctionsLibrary.BarkData data = SoundFunctionsLibrary.getInstance().getBarkData(barkId, InnerSoundFunctionsLibrary.this.m_element, breedId);
                        if (data == null) {
                            InnerSoundFunctionsLibrary.m_logger.debug((Object)"Impossible de trouver de BarkData ad?quat");
                            return;
                        }
                        if (!InnerSoundFunctionsLibrary.this.m_element.getSoundValidator().canPlaySound(data.getSoundId())) {
                            return;
                        }
                        final long date = System.currentTimeMillis();
                        if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, data.getSoundId())) {
                            return;
                        }
                        AnmActionRunScriptManager.getInstance().registerSound(date, data.getSoundId());
                        SoundFunctionsLibrary.getInstance().playSound(data.getSoundId(), data.getGain() * gain / 100.0f, 1, -1L, -1L, this.getScriptObject().getFightId(), InnerSoundFunctionsLibrary.this.m_element, data.getRollOff());
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
        
        class PlayGroundSound extends JavaFunctionEx
        {
            public PlayGroundSound(final LuaState luaState) {
                super(luaState);
            }
            
            @Override
            public final String getName() {
                return "playGroundSound";
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
                return LuaAnmActionRunScriptManager.PLAY_GROUND_SOUND_PARAMS;
            }
            
            @Override
            public final LuaScriptParameterDescriptor[] getResultDescriptors() {
                return null;
            }
            
            public final void run(final int paramCount) throws LuaException {
                if (!InnerSoundFunctionsLibrary.this.m_element.canPlaySound()) {
                    return;
                }
                byte groundType = GroundSoundType.DEFAULT.getType();
                final byte walkType = (byte)this.getParamInt(0);
                int gain = 100;
                if (paramCount > 1) {
                    gain = this.getParamInt(1);
                }
                final short z = TopologyMapManager.getNearestWalkableZ(InnerSoundFunctionsLibrary.this.m_element.getWorldCellX(), InnerSoundFunctionsLibrary.this.m_element.getWorldCellY(), InnerSoundFunctionsLibrary.this.m_element.getWorldCellAltitude());
                final DisplayedScreenElement element = DisplayedScreenWorld.getInstance().getElementAtTop(InnerSoundFunctionsLibrary.this.m_element.getWorldCellX(), InnerSoundFunctionsLibrary.this.m_element.getWorldCellY(), z, ElementFilter.NOT_EMPTY);
                if (element != null) {
                    final ElementProperties properties = element.getElement().getCommonProperties();
                    if (properties != null) {
                        groundType = properties.getGroundSoundType();
                    }
                }
                try {
                    final SoundFunctionsLibrary.GroundSoundData data = SoundFunctionsLibrary.getInstance().getGroundSoundData(groundType, walkType);
                    if (data == null) {
                        InnerSoundFunctionsLibrary.m_logger.debug((Object)"Impossible de trouver de GroundSoundData ad?quat");
                        return;
                    }
                    if (!InnerSoundFunctionsLibrary.this.m_element.getSoundValidator().canPlaySound(data.getSoundId())) {
                        return;
                    }
                    final long date = System.currentTimeMillis();
                    if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, data.getSoundId())) {
                        return;
                    }
                    AnmActionRunScriptManager.getInstance().registerSound(date, data.getSoundId());
                    SoundFunctionsLibrary.getInstance().playSound(data.getSoundId(), data.getGain() * gain / 100.0f, 1, -1L, -1L, this.getScriptObject().getFightId(), InnerSoundFunctionsLibrary.this.m_element, data.getRollOff());
                }
                catch (Exception e) {
                    InnerSoundFunctionsLibrary.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
                }
            }
        }
    }
}
