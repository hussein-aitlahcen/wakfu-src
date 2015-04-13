package com.ankamagames.xulor2.component;

import uk.co.caprica.vlcj.player.direct.format.*;
import com.ankamagames.framework.graphics.engine.video.*;
import uk.co.caprica.vlcj.player.direct.*;
import com.ankamagames.framework.video.*;
import com.ankamagames.xulor2.core.*;
import uk.co.caprica.vlcj.player.events.*;
import com.ankamagames.framework.kernel.utils.*;
import java.io.*;
import uk.co.caprica.vlcj.runtime.*;
import com.sun.jna.*;
import uk.co.caprica.vlcj.player.*;
import java.util.*;

public class VlcVideoWidget extends VideoWidget implements BufferFormatCallback
{
    private static final int ENABLED_EVENTS;
    private DirectMediaPlayer m_mediaPlayer;
    private MediaPlayerFactory m_mediaPlayerFactory;
    private VideoTextureProducerFromNative m_videoTextureProducer;
    private EventListenerProxy m_eventListenerProxy;
    
    public BufferFormat getBufferFormat(final int sourceWidth, final int sourceHeight) {
        return (BufferFormat)new RV32BufferFormat(sourceWidth, sourceHeight);
    }
    
    @Override
    protected VideoTextureProducer getVideoTextureProducer() {
        return this.m_videoTextureProducer;
    }
    
    public boolean applyPlay() {
        final String path = this.getVideoPath();
        if (path == null) {
            VlcVideoWidget.m_logger.error((Object)("Unable to get path for video " + this));
            return false;
        }
        if (this.m_mediaPlayer == null) {
            if (this.m_mediaPlayerFactory == null) {
                VlcVideoWidget.m_logger.error((Object)("Unable to play media " + this.getVideoPath()));
                return false;
            }
            this.m_mediaPlayer = this.m_mediaPlayerFactory.newDirectMediaPlayer((BufferFormatCallback)this, (RenderCallback)this.m_videoTextureProducer);
            this.registerEventListeners();
            if (!this.m_mediaPlayer.playMedia(this.getVideoPath(), new String[0])) {
                VlcVideoWidget.m_logger.error((Object)("Unable to play media " + this.getVideoPath()));
                return false;
            }
        }
        this.m_mediaPlayer.play();
        return true;
    }
    
    @Override
    public void setSpu(final int spu) {
        if (this.m_mediaPlayer == null) {
            VlcVideoWidget.m_logger.error((Object)"Unable to set SPU if no media player is started");
            return;
        }
        this.m_mediaPlayer.setSpu(spu);
    }
    
    public boolean applyPaused(final boolean paused) {
        if (this.m_mediaPlayer == null) {
            return false;
        }
        this.m_mediaPlayer.setPause(paused);
        return true;
    }
    
    @Override
    public boolean applyStop() {
        if (this.m_mediaPlayer == null) {
            return false;
        }
        this.m_mediaPlayer.stop();
        return true;
    }
    
    @Override
    public long getVideoDuration() {
        return (this.m_mediaPlayer != null) ? this.m_mediaPlayer.getLength() : 0L;
    }
    
    @Override
    public void seek(final float positionPercent) {
        if (this.m_mediaPlayer == null) {
            return;
        }
        if (!this.m_mediaPlayer.isSeekable()) {
            return;
        }
        this.m_mediaPlayer.setPosition(positionPercent);
    }
    
    @Override
    protected void applyVolume(final float volume) {
        if (this.m_mediaPlayer == null) {
            return;
        }
        this.m_mediaPlayer.setVolume(Math.round(volume * 100.0f));
    }
    
    @Override
    protected long getVideoPosition() {
        if (this.m_mediaPlayer == null) {
            return 0L;
        }
        return this.m_mediaPlayer.getTime();
    }
    
    @Override
    public void addVideoEventListener(final VideoEventListener listener) {
        if (this.m_eventListenerProxy == null) {
            this.m_eventListenerProxy = new EventListenerProxy();
        }
        this.m_eventListenerProxy.m_listeners.add(listener);
        this.registerEventListeners();
    }
    
    private void registerEventListeners() {
        if (this.m_mediaPlayer == null || this.m_eventListenerProxy == null) {
            return;
        }
        this.m_mediaPlayer.removeMediaPlayerEventListener((MediaPlayerEventListener)this.m_eventListenerProxy);
        this.m_mediaPlayer.addMediaPlayerEventListener((MediaPlayerEventListener)this.m_eventListenerProxy);
        this.m_mediaPlayer.enableEvents(VlcVideoWidget.ENABLED_EVENTS);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        try {
            this.m_mediaPlayerFactory = new MediaPlayerFactory(new String[] { "--no-video-title-show" });
            this.m_videoTextureProducer = new VideoTextureProducerFromNative();
        }
        catch (RuntimeException e) {
            VlcVideoWidget.m_logger.info((Object)"Probl\u00e8me au chargement de la MediaPlayerFactory : ", (Throwable)e);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_mediaPlayer != null) {
            this.m_mediaPlayer.release();
            this.m_mediaPlayer = null;
        }
        if (this.m_mediaPlayerFactory != null) {
            this.m_mediaPlayerFactory = null;
        }
        if (this.m_eventListenerProxy != null) {
            this.m_eventListenerProxy.m_listeners.clear();
            this.m_eventListenerProxy = null;
        }
    }
    
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final VideoWidget videoWidget = (VideoWidget)source;
        videoWidget.setVideoPath(this.getVideoPath());
    }
    
    static {
        ENABLED_EVENTS = MediaPlayerEventType.events(new MediaPlayerEventType[] { MediaPlayerEventType.ALL });
        try {
            final File vlcLibPath = new File(System.getProperty("java.library.path") + "/vlc-" + OS.getCurrentOS().getName());
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibPath.getAbsolutePath());
            System.load(vlcLibPath.getAbsolutePath() + '/' + RuntimeUtil.getLibVlcCoreName());
        }
        catch (Throwable t) {
            VlcVideoWidget.m_logger.error((Object)"Throwable pendant le chargement des natives de VLC : ", t);
        }
    }
    
    private class EventListenerProxy extends MediaPlayerEventAdapter
    {
        final ArrayList<VideoEventListener> m_listeners;
        
        EventListenerProxy() {
            super();
            this.m_listeners = new ArrayList<VideoEventListener>();
        }
        
        public void buffering(final MediaPlayer mediaPlayer, final float newCache) {
            for (final VideoEventListener listener : this.m_listeners) {
                listener.onBuffering(newCache);
            }
        }
        
        public void videoOutput(final MediaPlayer mediaPlayer, final int newCount) {
            for (final VideoEventListener listener : this.m_listeners) {
                listener.onVideoOutput();
            }
        }
        
        public void finished(final MediaPlayer mediaPlayer) {
            for (final VideoEventListener listener : this.m_listeners) {
                listener.onVideoEnd();
            }
        }
        
        public void stopped(final MediaPlayer mediaPlayer) {
            for (final VideoEventListener listener : this.m_listeners) {
                listener.onVideoStopped();
            }
        }
        
        public String toString() {
            return "EventListenerProxy{m_listeners=" + this.m_listeners.size() + '}';
        }
    }
}
