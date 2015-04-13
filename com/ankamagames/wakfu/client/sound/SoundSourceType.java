package com.ankamagames.wakfu.client.sound;

import com.ankamagames.framework.sound.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.sound.helper.*;

enum SoundSourceType implements SoundInitializer
{
    PAK("pak") {
        @Override
        protected AudioResourceHelper createHelper(final String sndFilePath) {
            try {
                if (sndFilePath.charAt(0) == '@') {
                    final MultiPakAudioResourceHelper helper = new MultiPakAudioResourceHelper(new String[] { "snd", "ogg" });
                    helper.prepare(ContentFileHelper.getBaseContentPath() + sndFilePath.substring(1));
                    return helper;
                }
                final PakAudioResourceHelper helper2 = new PakAudioResourceHelper(new String[] { "snd", "ogg" });
                helper2.setPakFile(new PakFile(sndFilePath));
                return helper2;
            }
            catch (IOException e) {
                SoundSourceType.m_logger.error((Object)"Impossible de charger le fichier de pak de son", (Throwable)e);
                return null;
            }
        }
    }, 
    OGG_JAR("jar") {
        @Override
        protected UrlAudioResourceHelper createHelper(final String path) {
            return new UrlAudioResourceHelper(path, new String[] { "ogg", "snd" });
        }
    }, 
    OGG_FILES("file") {
        @Override
        protected FileAudioResourceHelper createHelper(final String path) {
            return new FileAudioResourceHelper(path, new String[] { "ogg", "snd" });
        }
    };
    
    private static final Logger m_logger;
    private final String m_key;
    
    private SoundSourceType(final String key) {
        this.m_key = key;
    }
    
    @Override
    public final void initSound() {
        this.createAndSetHelper("MUSIC", "musicPath", GameSoundGroup.MUSIC.getMusicGroup(), GameSoundGroup.MUSIC.getFieldSourceGroup());
        this.createAndSetHelper("GUI_SFX", "guiSoundPath", GameSoundGroup.GUI.getDefaultGroup());
        this.createAndSetHelper("AMB2D", "amb2DPath", GameSoundGroup.SOUND_AMB_2D.getDefaultGroup(), GameSoundGroup.SOUND_AMB_2D.getMusicGroup(), GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup());
        this.createAndSetHelper("FIGHT_SOUND", "fightSoundPath", GameSoundGroup.SOUND_FIGHT.getDefaultGroup(), GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup());
        this.createAndSetHelper("SFX_SOUND", "sfxSoundPath", GameSoundGroup.SFX.getDefaultGroup(), GameSoundGroup.SFX.getFieldSourceGroup());
        this.createAndSetHelper("VOICES_SOUND", "voicesPath", GameSoundGroup.VOICES.getDefaultGroup(), GameSoundGroup.VOICES.getFieldSourceGroup());
        this.createAndSetHelper("FOLEYS_SOUND", "foleysSoundPath", GameSoundGroup.FOLEYS.getDefaultGroup(), GameSoundGroup.FOLEYS.getFieldSourceGroup());
        this.createAndSetHelper("PARTICLES_SOUND", "particlesSoundPath", GameSoundGroup.PARTICLES.getDefaultGroup(), GameSoundGroup.PARTICLES.getFieldSourceGroup());
    }
    
    private void createAndSetHelper(final String id, final String configKey, final AudioSourceGroup... groups) {
        try {
            final String sndPath = WakfuConfiguration.getInstance().getString(configKey);
            final AudioResourceHelper helper = this.createHelper(sndPath);
            if (helper == null) {
                return;
            }
            for (final AudioSourceGroup group : groups) {
                group.setHelper(helper);
            }
        }
        catch (Exception ex) {
            SoundSourceType.m_logger.error((Object)("impossible d'initialiser " + id), (Throwable)ex);
        }
    }
    
    protected abstract AudioResourceHelper createHelper(final String p0);
    
    public static SoundSourceType getFromKey(final String key) {
        for (final SoundSourceType type : values()) {
            if (type.m_key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundSourceType.class);
    }
}
