package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.video.*;
import com.ankamagames.framework.graphics.engine.video.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.converter.*;

public abstract class VideoWidget extends Widget
{
    private static final int MIN_OUTPUT_WIDTH = 32;
    private static final int MIN_OUTPUT_HEIGHT = 32;
    private static final int MAX_OUTPUT_WIDTH = 4096;
    private static final int MAX_OUTPUT_HEIGHT = 4096;
    protected static final Logger m_logger;
    public static final String TAG = "Video";
    protected VideoMesh m_videoMesh;
    private String m_path;
    private State m_state;
    private boolean m_muted;
    private float m_volume;
    private long m_lastPosition;
    private boolean m_keepRatio;
    private final XulorSceneDisplayListener m_videoTextureUpdater;
    public static final int PATH_HASH;
    public static final int KEEP_RATIO_HASH;
    public static final int ON_TIME_CHANGE_HASH;
    
    protected VideoWidget() {
        super();
        this.m_videoTextureUpdater = new XulorSceneDisplayListener() {
            @Override
            public void display(final Renderer renderer, final Graphics graphics) {
                if (!VideoWidget.this.isInitialized()) {
                    return;
                }
                VideoWidget.this.getVideoTextureProducer().updateTexture(renderer);
            }
        };
    }
    
    public static VideoWidget checkOut() {
        final VideoWidget widget = new VlcVideoWidget();
        widget.onCheckOut();
        return widget;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_videoMesh.onCheckIn();
        this.m_videoMesh = null;
        this.m_lastPosition = -1L;
        this.m_keepRatio = false;
        this.m_state = State.STOPPED;
        Xulor.getInstance().getScene().removeDisplayListener(this.m_videoTextureUpdater);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_keepRatio = false;
        this.m_volume = 1.0f;
        this.m_state = State.STOPPED;
        this.m_muted = false;
        this.m_path = null;
        this.m_lastPosition = 0L;
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.setNeedsToPreProcess();
        if (Xulor.getInstance().getScene() != null) {
            Xulor.getInstance().getScene().addDisplayListener(this.m_videoTextureUpdater);
        }
        (this.m_videoMesh = new VideoMesh()).onCheckOut();
        this.m_videoMesh.setFlipVerticaly(true);
        this.setMinSize(new Dimension(32, 32));
        this.setMaxSize(new Dimension(4096, 4096));
        this.setSize(this.getMinSize());
    }
    
    protected void setState(final State state) {
        this.m_state = state;
    }
    
    public String getVideoPath() {
        return this.m_path;
    }
    
    public void setVideoPath(final String path) {
        this.m_path = path;
    }
    
    public void play() {
        if (this.isPlaying()) {
            return;
        }
        if (this.isPaused()) {
            this.setPaused(false);
            return;
        }
        if (this.applyPlay()) {
            this.m_state = State.PLAYING;
            this.setNeedsToPreProcess();
        }
        else {
            VideoWidget.m_logger.info((Object)"Unable to set video as 'PLAYING'");
        }
    }
    
    protected abstract boolean applyPlay();
    
    public void setPaused(final boolean isPaused) {
        if (!this.isInitialized()) {
            return;
        }
        if (this.applyPaused(isPaused)) {
            this.m_state = (isPaused ? State.PAUSED : State.PLAYING);
        }
        this.setNeedsToPreProcess();
    }
    
    public abstract void setSpu(final int p0);
    
    protected abstract boolean applyPaused(final boolean p0);
    
    public abstract boolean applyStop();
    
    public boolean isPaused() {
        return this.m_state == State.PAUSED;
    }
    
    public boolean isPlaying() {
        return this.m_state == State.PLAYING;
    }
    
    public abstract void seek(final float p0);
    
    public abstract void addVideoEventListener(final VideoEventListener p0);
    
    public boolean isInitialized() {
        return this.m_state != State.STOPPED;
    }
    
    public abstract long getVideoDuration();
    
    protected abstract long getVideoPosition();
    
    protected abstract VideoTextureProducer getVideoTextureProducer();
    
    public void setVolume(final float volume) {
        if (volume > this.m_volume) {
            this.m_muted = false;
        }
        this.m_volume = volume;
        this.applyVolume(this.m_muted ? 0.0f : volume);
    }
    
    protected abstract void applyVolume(final float p0);
    
    public float getVolume() {
        return this.m_volume;
    }
    
    public void mute(final boolean isMuted) {
        if (isMuted == this.m_muted) {
            return;
        }
        this.m_muted = isMuted;
        this.applyVolume(this.m_muted ? 0.0f : this.m_volume);
    }
    
    public boolean isMuted() {
        return this.m_muted;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (!this.isInitialized()) {
            return ret;
        }
        final long position = this.getVideoPosition();
        if (this.m_lastPosition != position) {
            this.m_lastPosition = position;
            final ValueChangedEvent event = new ValueChangedEvent(this);
            event.setOldValue(this.m_lastPosition);
            event.setValue(position);
            this.dispatchEvent(event);
        }
        return this.isPlaying() || ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        if (this.getVideoTextureProducer().hasTexture() && this.m_appearance != null) {
            this.m_videoMesh.updateVertex(this.getVideoTextureProducer(), this.m_appearance.getMargin(), this.m_appearance.getBorder(), this.m_appearance.getPadding());
        }
        return true;
    }
    
    @Override
    public String getTag() {
        return "Video";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        if (this.m_entity != null && this.m_videoMesh != null && this.m_videoMesh.getEntity() != null) {
            this.m_entity.addChild(this.m_videoMesh.getEntity());
        }
    }
    
    @Override
    public void setSize(final int width, final int height) {
        super.setSize(width, height);
        this.m_videoMesh.setSize(width, height);
        this.setNeedsToPreProcess();
    }
    
    public void setOnTimeChange(final ValueChangedListener l) {
        this.addEventListener(Events.VALUE_CHANGED, l, false);
    }
    
    public void setKeepRatio(final boolean keepRatio) {
        this.m_keepRatio = keepRatio;
        this.m_videoMesh.setKeepRatio(this.m_keepRatio);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == VideoWidget.PATH_HASH) {
            this.setVideoPath(value);
        }
        else if (hash == VideoWidget.ON_TIME_CHANGE_HASH) {
            this.addEventListener(Events.VALUE_CHANGED, cl.convert(ValueChangedListener.class, value), false);
        }
        else {
            if (hash != VideoWidget.KEEP_RATIO_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setKeepRatio(cl.convert(Boolean.class, value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == VideoWidget.PATH_HASH) {
            this.setVideoPath((String)value);
        }
        else {
            if (hash != VideoWidget.KEEP_RATIO_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setKeepRatio((boolean)value);
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)VideoWidget.class);
        PATH_HASH = "videoPath".hashCode();
        KEEP_RATIO_HASH = "keepRatio".hashCode();
        ON_TIME_CHANGE_HASH = "onTimeChange".hashCode();
    }
    
    private enum State
    {
        STOPPED, 
        PLAYING, 
        PAUSED;
    }
}
