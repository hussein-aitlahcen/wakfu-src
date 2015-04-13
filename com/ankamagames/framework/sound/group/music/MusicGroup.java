package com.ankamagames.framework.sound.group.music;

import com.ankamagames.framework.sound.group.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.openAL.*;
import org.lwjgl.openal.*;
import java.util.*;
import org.jetbrains.annotations.*;
import java.io.*;

public class MusicGroup extends AudioSourceGroup
{
    private static final boolean DEBUG = false;
    private static final Logger m_logger;
    public static final byte DEFAULT_MUSIC_VOICES_AVAILABLE = 4;
    public static final int DEFAULT_FADE_IN_DURATION = 8000;
    public static final int DEFAULT_FADE_OUT_DURATION = 8000;
    private AudioSource m_mainMusic;
    private AudioSource m_crossMusic;
    private AudioSource m_nextCrossMusic;
    private boolean m_paused;
    private AudioSource m_pausedMusic;
    private PlayListState m_playListState;
    private PlayListState m_prePausePlayListState;
    private ArrayList<MusicData> m_playListData;
    private boolean m_playListLoop;
    private int m_playListIndex;
    private int m_musicTotalDuration;
    private int m_musicCurrentDuration;
    private int m_silenceTotalDuration;
    private int m_silenceCurrentDuration;
    private long m_previousUpdateTime;
    private MusicData m_currentMusicData;
    private float m_pausedMusicTimeOffset;
    private boolean m_isAlternate;
    private boolean m_needsToSwitch;
    public boolean m_isInContinuousMode;
    private int m_fadeInDuration;
    private int m_fadeOutDuration;
    private final Object m_audioSourcesMutex;
    
    public MusicGroup(final String name) {
        this(name, (byte)(-1));
    }
    
    public MusicGroup(final String name, final byte parentId) {
        this(name, (byte)0, parentId);
    }
    
    public MusicGroup(final String name, final byte priority, final byte parentId) {
        super(name, priority, parentId);
        this.m_paused = false;
        this.m_playListState = PlayListState.NONE;
        this.m_prePausePlayListState = PlayListState.NONE;
        this.m_playListData = null;
        this.m_playListIndex = 0;
        this.m_musicTotalDuration = 0;
        this.m_musicCurrentDuration = 0;
        this.m_silenceTotalDuration = 0;
        this.m_silenceCurrentDuration = 0;
        this.m_previousUpdateTime = 0L;
        this.m_currentMusicData = null;
        this.m_pausedMusicTimeOffset = 0.0f;
        this.m_isAlternate = false;
        this.m_needsToSwitch = false;
        this.m_isInContinuousMode = false;
        this.m_fadeInDuration = 8000;
        this.m_fadeOutDuration = 8000;
        this.m_audioSourcesMutex = new Object();
        this.setCanPrepareSoundsMuted(true);
        this.m_mainMusic = null;
        this.m_crossMusic = null;
        this.m_nextCrossMusic = null;
    }
    
    public boolean isInContinuousMode() {
        return this.m_isInContinuousMode;
    }
    
    public void setInContinuousMode(final boolean inContinuousMode) {
        this.m_isInContinuousMode = inContinuousMode;
        this.m_musicCurrentDuration = 0;
        if (this.isInContinuousMode()) {
            this.m_playListState = PlayListState.LOADING;
        }
    }
    
    public final AudioSource getMainMusic() {
        return this.m_mainMusic;
    }
    
    public final void setFadeInDuration(final int fadeInDuration) {
        this.m_fadeInDuration = fadeInDuration;
    }
    
    public final void setFadeOutDuration(final int fadeOutDuration) {
        this.m_fadeOutDuration = fadeOutDuration;
    }
    
    public final synchronized AudioSource crossFade(final AudioStreamProvider asp, final float targetGain) {
        return this.crossFade(asp, targetGain, false);
    }
    
    public synchronized void switchToAlternatePlayList(final boolean alternate) {
        if (this.m_currentMusicData != null && this.m_isAlternate != alternate) {
            this.m_needsToSwitch = true;
        }
        this.m_isAlternate = alternate;
    }
    
    @Override
    public void addWaitingSources() {
    }
    
    public final synchronized AudioSource crossFade(final long id, final float targetGain) {
        return this.crossFade(id, targetGain, false);
    }
    
    public final synchronized void pauseMainMusic(final boolean pause) {
        if (pause == this.m_paused) {
            return;
        }
        this.m_paused = pause;
        if (this.m_paused) {
            this.m_pausedMusic = this.m_mainMusic;
            if (this.m_pausedMusic != null) {
                this.m_pausedMusic.fade(0.0f, this.m_fadeOutDuration);
                this.m_pausedMusic.setPauseOnNullGain(true);
            }
            this.m_prePausePlayListState = this.m_playListState;
            this.m_playListState = PlayListState.PAUSE;
            this.m_mainMusic = null;
        }
        else {
            if (this.m_pausedMusic != null) {
                this.m_pausedMusic.unPause();
                if (this.m_prePausePlayListState == PlayListState.NONE) {
                    this.m_pausedMusic.fade(0.0f, this.m_fadeOutDuration);
                    this.m_pausedMusic.setStopOnNullGain(true);
                }
                else {
                    this.crossFade(this.m_pausedMusic);
                }
            }
            else if (this.m_mainMusic != null) {
                this.m_mainMusic.fade(0.0f, this.m_fadeOutDuration);
                this.m_mainMusic.setStopOnNullGain(true);
            }
            this.m_playListState = this.m_prePausePlayListState;
            this.m_pausedMusic = null;
        }
    }
    
    @Override
    public synchronized void update(final long currentTime) throws Exception {
        super.update(currentTime);
        synchronized (this.m_audioSourcesMutex) {
            final Vector3 pos = Vector3.ZERO;
            try {
                if (this.m_mainMusic != null) {
                    this.m_mainMusic.setPosition(pos);
                    try {
                        switch (this.m_mainMusic.update(currentTime)) {
                            case 1:
                            case 3: {
                                final SoundManager manager = this.m_manager;
                                SoundManager.stopAndReleaseSound(this.m_mainMusic);
                                this.m_mainMusic = null;
                                if (this.m_playListState == PlayListState.PLAYING || this.m_playListState == PlayListState.FADING) {
                                    this.m_playListState = PlayListState.MUSIC_END;
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    catch (OpenALException e) {
                        MusicGroup.m_logger.error((Object)"Exception", (Throwable)e);
                        final SoundManager manager2 = this.m_manager;
                        SoundManager.stopAndReleaseSound(this.m_mainMusic);
                        this.m_mainMusic = null;
                    }
                }
                if (this.m_crossMusic != null) {
                    this.m_crossMusic.setPosition(pos);
                    try {
                        switch (this.m_crossMusic.update(currentTime)) {
                            case 1:
                            case 3: {
                                final SoundManager manager3 = this.m_manager;
                                SoundManager.stopAndReleaseSound(this.m_crossMusic);
                                this.m_crossMusic = null;
                                break;
                            }
                        }
                    }
                    catch (OpenALException e) {
                        MusicGroup.m_logger.error((Object)"Exception", (Throwable)e);
                        final SoundManager manager4 = this.m_manager;
                        SoundManager.stopAndReleaseSound(this.m_crossMusic);
                        this.m_crossMusic = null;
                    }
                }
                if (this.m_pausedMusic != null) {
                    this.m_pausedMusic.setPosition(pos);
                    try {
                        switch (this.m_pausedMusic.update(currentTime)) {
                            case 1:
                            case 3: {
                                final SoundManager manager5 = this.m_manager;
                                SoundManager.stopAndReleaseSound(this.m_pausedMusic);
                                this.m_pausedMusic = null;
                                break;
                            }
                        }
                    }
                    catch (OpenALException e) {
                        MusicGroup.m_logger.error((Object)"Exception", (Throwable)e);
                        final SoundManager manager6 = this.m_manager;
                        SoundManager.stopAndReleaseSound(this.m_pausedMusic);
                        this.m_pausedMusic = null;
                    }
                }
            }
            catch (Exception e2) {
                MusicGroup.m_logger.error((Object)"Exception", (Throwable)e2);
            }
            if (this.m_crossMusic != null && this.m_mainMusic == null) {
                this.m_mainMusic = this.m_crossMusic;
                this.m_crossMusic = null;
            }
            if (this.m_crossMusic == null && this.m_nextCrossMusic != null) {
                final AudioSource nextCross = this.m_nextCrossMusic;
                this.m_nextCrossMusic = null;
                this.crossFade(nextCross);
            }
            switch (this.m_playListState) {
                case LOADING: {
                    this.m_playListIndex = 0;
                    if (this.m_playListData == null) {
                        break;
                    }
                    this.m_currentMusicData = this.m_playListData.get(this.m_playListIndex);
                    final long id = this.m_isAlternate ? this.m_currentMusicData.getAlternativeMusicId() : this.m_currentMusicData.getMusicId();
                    if (this.m_pausedMusic != null && this.m_pausedMusic.getFileIdValue() != id) {
                        final SoundManager manager7 = this.m_manager;
                        SoundManager.stopAndReleaseSound(this.m_pausedMusic);
                        this.m_pausedMusic = null;
                        this.m_paused = false;
                        this.m_prePausePlayListState = PlayListState.PLAYING;
                    }
                    final AudioSource music = this.crossFade(id, this.m_currentMusicData.getVolume() / 100.0f, true);
                    if (this.m_currentMusicData.getNumLoops() == -1 || music == null) {
                        this.m_musicTotalDuration = -1;
                    }
                    else {
                        this.m_musicTotalDuration = this.m_currentMusicData.getNumLoops() * music.getDurationInMs();
                    }
                    this.m_musicCurrentDuration = 0;
                    this.m_playListState = PlayListState.PLAYING;
                    break;
                }
                case PLAYING: {
                    if (!this.m_isInContinuousMode) {
                        this.m_musicCurrentDuration += (int)(currentTime - this.m_previousUpdateTime);
                    }
                    if (this.m_musicTotalDuration != -1 && this.m_musicCurrentDuration + this.m_fadeOutDuration > this.m_musicTotalDuration) {
                        if (this.m_mainMusic != null) {
                            this.m_mainMusic.fade(0.0f, this.m_fadeOutDuration);
                            this.m_mainMusic.setStopOnNullGain(true);
                        }
                        this.m_playListState = PlayListState.FADING;
                        break;
                    }
                    if (!this.m_needsToSwitch) {
                        break;
                    }
                    long offset = 0L;
                    if (!this.m_mainMusic.isJustRefilled()) {
                        break;
                    }
                    if (this.m_mainMusic != null) {
                        offset = this.m_mainMusic.pcmTell();
                    }
                    final long id2 = this.m_isAlternate ? this.m_currentMusicData.getAlternativeMusicId() : this.m_currentMusicData.getMusicId();
                    final int fadeOut = this.m_fadeOutDuration;
                    final int fadeIn = this.m_fadeInDuration;
                    this.m_fadeInDuration = 200;
                    this.m_fadeOutDuration = 250;
                    final AudioSource source = this.crossFade(id2, this.m_currentMusicData.getVolume() / 100.0f, true);
                    this.m_fadeInDuration = fadeIn;
                    this.m_fadeOutDuration = fadeOut;
                    if (source != null) {
                        source.pcmSeek(offset);
                    }
                    this.m_needsToSwitch = false;
                    break;
                }
                case MUSIC_END: {
                    if (this.m_playListData == null) {
                        break;
                    }
                    final short silenceDuration = this.m_playListData.get(this.m_playListIndex).getSilenceDuration();
                    this.m_currentMusicData = null;
                    this.m_playListState = PlayListState.SILENCE;
                    this.m_silenceTotalDuration = silenceDuration * 1000;
                    this.m_silenceCurrentDuration = 0;
                    break;
                }
                case SILENCE: {
                    if (this.m_playListData == null) {
                        break;
                    }
                    this.m_silenceCurrentDuration += (int)(currentTime - this.m_previousUpdateTime);
                    if (this.m_silenceCurrentDuration > this.m_silenceTotalDuration) {
                        final boolean b = false;
                        this.m_silenceCurrentDuration = (b ? 1 : 0);
                        this.m_silenceTotalDuration = (b ? 1 : 0);
                        ++this.m_playListIndex;
                        if (this.m_playListIndex == this.m_playListData.size()) {
                            if (!this.m_playListLoop) {
                                this.m_playListData = null;
                                this.m_playListState = PlayListState.NONE;
                                break;
                            }
                            this.m_playListIndex = 0;
                        }
                        this.m_currentMusicData = this.m_playListData.get(this.m_playListIndex);
                        final long id = this.m_isAlternate ? this.m_currentMusicData.getAlternativeMusicId() : this.m_currentMusicData.getMusicId();
                        final AudioSource music = this.crossFade(id, this.m_currentMusicData.getVolume() / 100.0f, true);
                        if (this.m_currentMusicData.getNumLoops() == -1 || music == null) {
                            this.m_musicTotalDuration = -1;
                        }
                        else {
                            this.m_musicTotalDuration = this.m_currentMusicData.getNumLoops() * music.getDurationInMs();
                        }
                        this.m_musicCurrentDuration = 0;
                        this.m_playListState = PlayListState.PLAYING;
                        break;
                    }
                    break;
                }
            }
            this.m_previousUpdateTime = currentTime;
        }
    }
    
    @Override
    public final synchronized void stop() throws Exception {
        synchronized (this.m_audioSourcesMutex) {
            if (this.m_mainMusic != null) {
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_mainMusic);
                this.m_mainMusic = null;
            }
            if (this.m_crossMusic != null) {
                final SoundManager manager2 = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_crossMusic);
                this.m_crossMusic = null;
            }
            if (this.m_nextCrossMusic != null) {
                final SoundManager manager3 = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_nextCrossMusic);
                this.m_nextCrossMusic = null;
            }
        }
    }
    
    @Override
    public final synchronized void stopSource(final AudioSource source) {
        synchronized (this.m_audioSourcesMutex) {
            if (this.m_mainMusic == source) {
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_mainMusic);
                this.m_mainMusic = null;
            }
            if (this.m_crossMusic == source) {
                final SoundManager manager2 = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_crossMusic);
                this.m_crossMusic = null;
            }
            if (this.m_nextCrossMusic == source) {
                final SoundManager manager3 = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_nextCrossMusic);
                this.m_nextCrossMusic = null;
            }
        }
    }
    
    @Override
    public synchronized void pause() {
        synchronized (this.m_audioSourcesMutex) {
            if (this.m_mainMusic != null) {
                this.m_mainMusic.stop();
            }
            if (this.m_crossMusic != null) {
                this.m_crossMusic.stop();
            }
        }
    }
    
    @Override
    public synchronized void restart() {
        synchronized (this.m_audioSourcesMutex) {
            if (this.m_mainMusic != null) {
                try {
                    this.m_mainMusic.play();
                }
                catch (Exception e) {
                    MusicGroup.m_logger.warn((Object)"Impossible de red\u00e9marrer la musique");
                }
            }
            if (this.m_crossMusic != null) {
                try {
                    this.m_crossMusic.play();
                }
                catch (Exception e) {
                    MusicGroup.m_logger.warn((Object)"Impossible de red\u00e9marrer la musique");
                }
            }
        }
    }
    
    @Override
    public boolean applyReverb(final int effectSlot) {
        assert false : "On peut pas appliquer de reverb sur de la musique";
        return true;
    }
    
    @Override
    public Collection<AudioSource> getSources() {
        assert false : "Ne doit pas etre appel\u00e9";
        return null;
    }
    
    @Override
    public AudioSource addSource(final AudioStreamProvider asp, final boolean bStreaming, final boolean bStereo, final boolean bLoop, final long soundUID) {
        assert false : "Ne doit pas \u00eatre appel\u00e9";
        return null;
    }
    
    @Override
    public void addSource(final AudioSource source) {
        assert false : "Ne doit pas \u00eatre appel\u00e9";
    }
    
    @Override
    public final synchronized void onGainModChanged(final float newGainMod) {
        final float gain = this.getGain();
        if (this.m_mainMusic != null) {
            this.m_mainMusic.setGainMod(gain);
        }
        if (this.m_crossMusic != null) {
            this.m_crossMusic.setGainMod(gain);
        }
        if (this.m_nextCrossMusic != null) {
            this.m_nextCrossMusic.setGainMod(gain);
        }
    }
    
    @Override
    public final void onGainChanged(final float previousGain, final float newGain) {
    }
    
    @Override
    public final void onMaxGainChanged(final float previousMaxGain, final float newMaxGain) {
    }
    
    @Override
    public final synchronized void onMuteChanged(final boolean previousMute, final boolean newMute) {
        if (this.m_mainMusic != null) {
            this.m_mainMusic.setMute(newMute);
        }
        if (this.m_crossMusic != null) {
            this.m_crossMusic.setMute(newMute);
        }
        if (this.m_nextCrossMusic != null) {
            this.m_nextCrossMusic.setMute(newMute);
        }
    }
    
    public final PlayListState getPlayListState() {
        return this.m_playListState;
    }
    
    public final synchronized void loadPlayListData(@Nullable final ArrayList<MusicData> playList) {
        this.loadPlayListData(playList, false);
    }
    
    public final synchronized void loadPlayListData(@Nullable final ArrayList<MusicData> playList, final boolean loop) {
        if (this.m_playListData == playList) {
            return;
        }
        this.m_playListLoop = loop;
        PlayListState newState;
        if (playList == null) {
            newState = PlayListState.NONE;
        }
        else {
            newState = PlayListState.LOADING;
        }
        if (this.m_playListState == PlayListState.PAUSE) {
            this.m_prePausePlayListState = newState;
        }
        else {
            this.m_playListState = newState;
        }
        if (this.m_playListState == PlayListState.NONE && this.m_mainMusic != null) {
            this.m_mainMusic.fade(0.0f, this.m_fadeOutDuration);
            this.m_mainMusic.setStopOnNullGain(true);
        }
        this.m_playListData = playList;
    }
    
    public final synchronized void fadeAndStop(final float fadeOutDuration) {
        if (this.m_mainMusic != null) {
            this.m_mainMusic.fade(0.0f, fadeOutDuration);
            this.m_mainMusic.setStopOnNullGain(true);
        }
        if (this.m_crossMusic != null) {
            this.m_crossMusic.fade(0.0f, fadeOutDuration);
            this.m_crossMusic.setStopOnNullGain(true);
        }
        if (this.m_pausedMusic != null) {
            this.m_pausedMusic.fade(0.0f, fadeOutDuration);
            this.m_pausedMusic.setStopOnNullGain(true);
        }
        if (this.m_nextCrossMusic != null) {
            final SoundManager manager = this.m_manager;
            SoundManager.stopAndReleaseSound(this.m_nextCrossMusic);
            this.m_nextCrossMusic = null;
        }
    }
    
    public final synchronized void fadeAndStop() {
        this.fadeAndStop(this.m_fadeOutDuration);
    }
    
    private AudioSource crossFade(final long id, final float targetGain, final boolean keepPlayListState) {
        if (this.m_helper != null) {
            AudioStreamProvider asp;
            try {
                asp = this.m_helper.fromId(id);
            }
            catch (IOException e) {
                MusicGroup.m_logger.error((Object)("Impossible de charger le son d'id " + id));
                return null;
            }
            if (asp != null) {
                return this.crossFade(asp, targetGain, keepPlayListState);
            }
        }
        else {
            MusicGroup.m_logger.error((Object)"AudioResourceHelper non sp\u00e9cifi\u00e9.");
        }
        return null;
    }
    
    private AudioSource crossFade(final AudioStreamProvider asp, final float targetGain, final boolean playList) {
        synchronized (this.m_audioSourcesMutex) {
            if (this.m_mainMusic != null && this.m_mainMusic.isPlaying() && this.m_mainMusic.getGain() > 0.0f) {
                if (this.m_mainMusic.getStream().getDescription().equals(asp.getDescription())) {
                    return this.m_mainMusic;
                }
            }
            else if (this.m_crossMusic != null && this.m_crossMusic.isPlaying() && this.m_crossMusic.getGain() > 0.0f && this.m_crossMusic.getStream().getDescription().equals(asp.getDescription())) {
                return this.m_crossMusic;
            }
            final AudioSource audioSource = this.prepareSound(asp, -1L);
            if (audioSource != null) {
                audioSource.setLoop(true);
                audioSource.setMaxGain(targetGain);
                audioSource.setGainMod(this.getGain());
                this.crossFade(audioSource);
                if (!playList) {
                    this.cleanUpPlayListState();
                }
            }
            return audioSource;
        }
    }
    
    private void crossFade(final AudioSource crossMusic) {
        if (crossMusic == null) {
            return;
        }
        if (this.m_crossMusic == null) {
            if (this.m_mainMusic != null) {
                this.m_mainMusic.fade(0.0f, this.m_fadeOutDuration);
                this.m_mainMusic.setStopOnNullGain(true);
            }
            this.m_crossMusic = this.m_mainMusic;
            (this.m_mainMusic = crossMusic).setMute(this.isMute());
            try {
                crossMusic.setGain(0.0f);
                crossMusic.play();
                crossMusic.fade(crossMusic.getMaxGain(), this.m_fadeInDuration);
            }
            catch (Exception e) {
                MusicGroup.m_logger.error((Object)"Exception lev\u00e9e durant le crossfading :", (Throwable)e);
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(crossMusic);
                this.m_mainMusic = null;
                this.m_nextCrossMusic = null;
                this.m_crossMusic = null;
            }
            return;
        }
        if (this.m_nextCrossMusic != null) {
            final SoundManager manager2 = this.m_manager;
            SoundManager.stopAndReleaseSound(this.m_nextCrossMusic);
        }
        this.m_nextCrossMusic = crossMusic;
    }
    
    private void cleanUpPlayListState() {
        this.m_playListState = PlayListState.NONE;
        this.m_playListData = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MusicGroup.class);
    }
    
    public enum PlayListState
    {
        NONE, 
        LOADING, 
        PLAYING, 
        FADING, 
        MUSIC_END, 
        SILENCE, 
        PAUSE;
    }
}
