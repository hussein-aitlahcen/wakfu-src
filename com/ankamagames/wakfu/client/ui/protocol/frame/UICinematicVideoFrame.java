package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.video.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.script.video.*;
import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class UICinematicVideoFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final UICinematicVideoFrame INSTANCE;
    private VideoWidget m_videoWidget;
    private String m_videoPath;
    private VideoView m_videoView;
    private boolean m_added;
    
    private UICinematicVideoFrame() {
        super();
        this.m_added = false;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (this.m_added) {
            return;
        }
        this.m_added = true;
        removeGameSceneAndSound();
        this.addVideoDialog();
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!this.m_added) {
            return;
        }
        this.m_added = false;
        addGameSceneAndSound();
        this.removeVideoDialog();
    }
    
    private void addVideoDialog() {
        this.m_videoView = new VideoView();
        PropertiesProvider.getInstance().setPropertyValue("video", this.m_videoView);
        final EventDispatcher eventDispatcher = Xulor.getInstance().load("videoCinematicDialog", Dialogs.getDialogPath("videoCinematicDialog"), 8448L, (short)19500);
        this.m_videoWidget = (VideoWidget)eventDispatcher.getElementMap().getElement("video");
        if (this.m_videoWidget == null) {
            UICinematicVideoFrame.m_logger.error((Object)"On n'a pas trouv\u00ef¿½ le video widget dans la dialog");
        }
        else {
            this.m_videoWidget.addVideoEventListener(this.m_videoView.getEventListener());
        }
        Xulor.getInstance().putActionClass("wakfu.videoCinematic", VideoCinematicDialogActions.class);
    }
    
    private void removeVideoDialog() {
        Xulor.getInstance().unload("videoCinematicDialog");
        Xulor.getInstance().removeActionClass("wakfu.videoCinematic");
        this.m_videoWidget = null;
    }
    
    private static void addGameSceneAndSound() {
        final Renderer renderer = WakfuClientInstance.getInstance().getRenderer();
        final AleaWorldScene worldScene = WakfuClientInstance.getInstance().getWorldScene();
        renderer.pushScene(worldScene, false);
        WakfuSoundManager.getInstance().fadeGUI(1.0f, 500);
        WakfuSoundManager.getInstance().fadeAmbiance(1.0f, 500);
        WakfuSoundManager.getInstance().fadeMusic(1.0f, 500);
    }
    
    protected static void removeGameSceneAndSound() {
        final Renderer renderer = WakfuClientInstance.getInstance().getRenderer();
        final AleaWorldScene worldScene = WakfuClientInstance.getInstance().getWorldScene();
        renderer.removeScene(worldScene);
        WakfuSoundManager.getInstance().fadeGUI(0.0f, 500);
        WakfuSoundManager.getInstance().fadeAmbiance(0.0f, 500);
        WakfuSoundManager.getInstance().fadeMusic(0.0f, 500);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19100: {
                this.playVideo();
                return false;
            }
            case 19101: {
                final UIMessage msg = (UIMessage)message;
                this.addVideoStopOrEndCallback(msg.getObjectValue());
                return false;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setVideoPath(final String videoPath) {
        this.m_videoPath = videoPath;
    }
    
    public void stopVideo() {
        this.m_videoWidget.applyStop();
    }
    
    public void playVideo() {
        if (!this.m_added) {
            UICinematicVideoFrame.m_logger.error((Object)"On veut jouer la video mais la frame n'est pas encore push\u00ef¿½");
        }
        if (this.m_videoWidget == null) {
            return;
        }
        this.m_videoWidget.setVideoPath(this.m_videoPath);
        this.m_videoWidget.setKeepRatio(true);
        this.m_videoWidget.play();
        this.m_videoWidget.setPaused(false);
        this.m_videoWidget.addVideoEventListener(new RemoveCinematicFrameListener());
        this.m_videoWidget.addVideoEventListener(new changeSpuListener(this.m_videoWidget));
    }
    
    public void addVideoStopOrEndCallback(final Runnable callback) {
        if (this.m_videoWidget != null && this.m_videoWidget.isPlaying()) {
            this.m_videoWidget.addVideoEventListener(new VideoStopOrEndCallback(callback));
        }
        else {
            callback.run();
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UICinematicVideoFrame.class);
        INSTANCE = new UICinematicVideoFrame();
    }
    
    private static class VideoView extends ImmutableFieldProvider
    {
        public static final String BUFFERING_DESC = "bufferingDesc";
        public static final String MODULATION_COLOR = "modulationColor";
        private float m_buffering;
        private String m_bufferingDesc;
        private String m_modulationColor;
        private EventListener m_eventListener;
        
        private VideoView() {
            super();
            this.m_buffering = -1.0f;
            this.m_eventListener = new EventListener();
        }
        
        @Override
        public String[] getFields() {
            return VideoView.NO_FIELDS;
        }
        
        @Nullable
        @Override
        public Object getFieldValue(final String fieldName) {
            if ("bufferingDesc".equals(fieldName)) {
                if (this.m_buffering < 0.0f || this.m_buffering == 100.0f) {
                    return null;
                }
                return this.m_bufferingDesc;
            }
            else {
                if ("modulationColor".equals(fieldName)) {
                    return this.m_modulationColor;
                }
                return null;
            }
        }
        
        public EventListener getEventListener() {
            return this.m_eventListener;
        }
        
        private class EventListener implements VideoEventListener
        {
            private Runnable m_runnable;
            
            private EventListener() {
                super();
                this.m_runnable = new Runnable() {
                    private float m_alpha = 0.0f;
                    private boolean m_ascending = true;
                    
                    @Override
                    public void run() {
                        final TextWidgetFormater sb = new TextWidgetFormater();
                        VideoView.this.m_modulationColor = "1.0,1.0,1.0," + this.m_alpha;
                        sb.append(WakfuTranslator.getInstance().getString("videoLoading"));
                        VideoView.this.m_bufferingDesc = sb.finishAndToString();
                        PropertiesProvider.getInstance().firePropertyValueChanged(VideoView.this, "bufferingDesc", "modulationColor");
                        if (this.m_ascending) {
                            this.m_alpha += 0.04f;
                            if (this.m_alpha >= 1.0f) {
                                this.m_alpha = 1.0f;
                                this.m_ascending = false;
                            }
                        }
                        else {
                            this.m_alpha -= 0.04f;
                            if (this.m_alpha <= 0.0f) {
                                this.m_alpha = 0.0f;
                                this.m_ascending = true;
                            }
                        }
                    }
                };
            }
            
            @Override
            public void onVideoEnd() {
            }
            
            @Override
            public void onVideoStopped() {
            }
            
            @Override
            public void onVideoOutput() {
            }
            
            @Override
            public void onBuffering(final float buffering) {
                if (VideoView.this.m_buffering == buffering) {
                    return;
                }
                VideoView.this.m_buffering = buffering;
                if (VideoView.this.m_buffering == 0.0f) {
                    ProcessScheduler.getInstance().schedule(this.m_runnable, 50L);
                }
                else if (VideoView.this.m_buffering == 100.0f) {
                    ProcessScheduler.getInstance().remove(this.m_runnable);
                    PropertiesProvider.getInstance().firePropertyValueChanged(VideoView.this, "bufferingDesc");
                }
            }
        }
    }
    
    private static class VideoStopOrEndCallback implements VideoEventListener
    {
        private final Runnable m_callback;
        
        VideoStopOrEndCallback(final Runnable callback) {
            super();
            this.m_callback = callback;
        }
        
        @Override
        public void onVideoOutput() {
        }
        
        @Override
        public void onVideoEnd() {
            this.m_callback.run();
        }
        
        @Override
        public void onVideoStopped() {
            this.m_callback.run();
        }
        
        @Override
        public void onBuffering(final float buffering) {
        }
        
        @Override
        public String toString() {
            return "VideoStopOrEndCallback{m_callback=" + this.m_callback + '}';
        }
    }
}
