package com.ankamagames.framework.sound.openAL;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.framework.sound.openAL.soundLogger.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class AudioSourceManager
{
    protected static final Logger m_logger;
    private static final AudioSourceManager m_instance;
    private final LongObjectLightWeightMap<AudioSource> m_sourcesMap;
    private long m_soundUID;
    private static final ObjectPool m_sources;
    
    public AudioSourceManager() {
        super();
        this.m_sourcesMap = new LongObjectLightWeightMap<AudioSource>();
        this.m_soundUID = 0L;
    }
    
    public static AudioSourceManager getInstance() {
        return AudioSourceManager.m_instance;
    }
    
    public long generateUID() {
        if (this.m_soundUID == Long.MAX_VALUE) {
            this.m_soundUID = 0L;
        }
        return this.m_soundUID++;
    }
    
    public synchronized AudioSource checkOut(final long soundUID) {
        try {
            final AudioSource source = (AudioSource)AudioSourceManager.m_sources.borrowObject();
            source.setSoundManagerRelease(true);
            source.setSoundUID((soundUID == -1L) ? this.generateUID() : soundUID);
            this.m_sourcesMap.put(source.getSoundUID(), source);
            return source;
        }
        catch (Exception e) {
            AudioSourceManager.m_logger.error((Object)"Exception lev\u00e9e lors du checkOut d'une source audio : ", (Throwable)e);
            return null;
        }
    }
    
    public synchronized void release(final AudioSource source) {
        try {
            if (source != null) {
                final AudioSourceGroup group = source.getGroup();
                SoundLogger.log("Removing " + source.getFileId(), (byte)((group == null) ? -1 : group.getParentId()));
                source.setSoundManagerRelease(false);
                this.m_sourcesMap.remove(source.getSoundUID());
                AudioSourceManager.m_sources.returnObject(source);
            }
        }
        catch (Exception e) {
            AudioSourceManager.m_logger.error((Object)"Exception lev\u00e9e lors du release d'une source audio : ", (Throwable)e);
        }
    }
    
    public synchronized AudioSource getAudioSource(final long soundUID) {
        return this.m_sourcesMap.get(soundUID);
    }
    
    public AudioSource getSourceWithLesserPriority() {
        int priority = -1;
        AudioSource found = null;
        for (int i = this.m_sourcesMap.size() - 1; i >= 0; --i) {
            final AudioSource source = this.m_sourcesMap.getQuickValue(i);
            if (source.getPriority() > priority) {
                found = source;
                priority = source.getPriority();
            }
        }
        return found;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AudioSourceManager.class);
        m_instance = new AudioSourceManager();
        m_sources = new MonitoredPool(new ObjectFactory<AudioSource>() {
            @Override
            public AudioSource makeObject() {
                return new AudioSource();
            }
        });
    }
}
