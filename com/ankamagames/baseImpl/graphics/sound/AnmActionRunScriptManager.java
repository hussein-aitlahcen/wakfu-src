package com.ankamagames.baseImpl.graphics.sound;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.sound.util.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public abstract class AnmActionRunScriptManager
{
    private static AnmActionRunScriptManager m_instance;
    private final LongLongLightWeightMap m_lastPlayedSoundTime;
    private static final int TIME_BEFORE_NEXT_PLAY = 1000;
    private final long[] PREFIXES_TO_CHECK;
    
    public AnmActionRunScriptManager() {
        super();
        this.m_lastPlayedSoundTime = new LongLongLightWeightMap();
        this.PREFIXES_TO_CHECK = PrimitiveArrays.EMPTY_LONG_ARRAY;
    }
    
    public static AnmActionRunScriptManager getInstance() {
        return AnmActionRunScriptManager.m_instance;
    }
    
    public static void setInstance(final AnmActionRunScriptManager instance) {
        AnmActionRunScriptManager.m_instance = instance;
    }
    
    public boolean canPlaySoundById(final long currentTime, final long soundId) {
        this.cleanUpOldEntries(currentTime);
        final long prefix = SoundGroupUtils.getSoundPrefix(soundId);
        if (!ArrayUtils.contains(this.PREFIXES_TO_CHECK, prefix)) {
            return true;
        }
        final long lastTime = this.m_lastPlayedSoundTime.get(soundId);
        return 1000L <= Math.abs(currentTime - lastTime);
    }
    
    private void cleanUpOldEntries(final long currentTime) {
        if (this.m_lastPlayedSoundTime.size() > 10) {
            for (int i = this.m_lastPlayedSoundTime.size() - 1; i >= 0; --i) {
                final long lastTime = this.m_lastPlayedSoundTime.getQuickValue(i);
                if (1000L <= Math.abs(currentTime - lastTime)) {
                    this.m_lastPlayedSoundTime.removeQuick(i);
                }
            }
        }
    }
    
    public void registerSound(final long time, final long fileId) {
        this.m_lastPlayedSoundTime.put(fileId, time);
    }
    
    public void playAnmAction(final AnimatedObject elem, final long actionId) {
        this.doPlayAnmAction(elem, actionId);
    }
    
    protected abstract void doPlayAnmAction(final AnimatedObject p0, final long p1);
}
