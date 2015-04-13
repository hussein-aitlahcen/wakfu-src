package com.ankamagames.baseImpl.graphics.sound.binary;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.baseImpl.graphics.sound.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.sound.openAL.*;

class SoundDataHelper
{
    private static final Logger m_logger;
    
    public static boolean canPlaySound(final AnimatedObject ae, final long soundId) {
        return canPlaySound(ae) && ae.getSoundValidator().canPlaySound(soundId);
    }
    
    public static boolean canPlaySound(final AnimatedObject ae) {
        return ae.canPlaySound() && SoundFunctionsLibrary.getInstance().canPlaySound();
    }
    
    public static boolean tryRegisterSound(final long soundId) {
        final long date = System.currentTimeMillis();
        if (!AnmActionRunScriptManager.getInstance().canPlaySoundById(date, soundId)) {
            return false;
        }
        AnmActionRunScriptManager.getInstance().registerSound(date, soundId);
        return true;
    }
    
    public static void playLocalSound(final AnimatedObject ae, final long soundId, final byte gain, final short playCount, final int rollOffPreset, final boolean stopOnAnimationChange) {
        if (soundId == 0L) {
            SoundDataHelper.m_logger.debug((Object)"Id du son nul");
            return;
        }
        try {
            final AnimatedElement elem = (AnimatedElement)ae;
            final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundId, gain / 100.0f, playCount, -1L, -1L, ae.getCurrentFightId(), elem, rollOffPreset);
            if (stopOnAnimationChange && source != null) {
                elem.addSoundRef(soundId, source.getSoundUID());
            }
        }
        catch (Exception e) {
            SoundDataHelper.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
        }
    }
    
    public static void playSound(final AnimatedObject ae, final long soundId, final byte gain, final short playCount, final boolean stopOnAnimationChange) {
        if (soundId == 0L) {
            SoundDataHelper.m_logger.debug((Object)"Id du son nul");
            return;
        }
        try {
            final AudioSourceDefinition source = SoundFunctionsLibrary.getInstance().playSound(soundId, gain / 100.0f, playCount, -1L, -1L, ae.getCurrentFightId());
            if (stopOnAnimationChange && source != null) {
                ((AnimatedElement)ae).addSoundRef(soundId, source.getSoundUID());
            }
        }
        catch (Exception e) {
            SoundDataHelper.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundDataHelper.class);
    }
}
