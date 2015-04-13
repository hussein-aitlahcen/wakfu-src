package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.video.*;
import com.ankamagames.xulor2.core.*;

public class UIVideoLoadingFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIVideoLoadingFrame m_instance;
    private String m_videoPath;
    private AleaWorldScene m_scene;
    private final Runnable m_sceneLoadingChecker;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIVideoLoadingFrame() {
        super();
        this.m_sceneLoadingChecker = new Runnable() {
            @Override
            public void run() {
                if (UIVideoLoadingFrame.this.m_scene != null && !UIVideoLoadingFrame.this.m_scene.isWorldReady()) {
                    return;
                }
                ProcessScheduler.getInstance().remove(UIVideoLoadingFrame.this.m_sceneLoadingChecker);
                PropertiesProvider.getInstance().setPropertyValue("isNewWorldReady", true);
            }
        };
    }
    
    public static UIVideoLoadingFrame getInstance() {
        return UIVideoLoadingFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (this.m_videoPath == null) {
                UIVideoLoadingFrame.m_logger.error((Object)"Impossible de charger la frame de lecture video sans d\u00e9finir la video \u00e0 jouer");
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("videoLoadingDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIVideoLoadingFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final Renderer renderer = WakfuClientInstance.getInstance().getRenderer();
            final AleaWorldScene worldScene = WakfuClientInstance.getInstance().getWorldScene();
            renderer.removeScene(worldScene);
            WakfuSoundManager.getInstance().setRunning(false);
            if (!this.initializeVideo()) {
                WakfuGameEntity.getInstance().removeFrame(getInstance());
            }
            Xulor.getInstance().putActionClass("wakfu.videoLoading", VideoLoadingDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("isNewWorldReady", false);
            ProcessScheduler.getInstance().schedule(this.m_sceneLoadingChecker, 500L);
        }
    }
    
    public void setScenesToLoad(final AleaWorldScene scene) {
        this.m_scene = scene;
    }
    
    private boolean initializeVideo() {
        try {
            final EventDispatcher eventDispatcher = Xulor.getInstance().load("videoLoadingDialog", Dialogs.getDialogPath("videoLoadingDialog"), 8448L, (short)19500);
            final VideoWidget videoWidget = (VideoWidget)eventDispatcher.getElementMap().getElement("video");
            videoWidget.setVideoPath(this.m_videoPath);
            videoWidget.play();
            videoWidget.setPaused(false);
            videoWidget.addVideoEventListener(new VideoEventListener() {
                @Override
                public void onVideoEnd() {
                    ProcessScheduler.getInstance().remove(UIVideoLoadingFrame.this.m_sceneLoadingChecker);
                    WakfuGameEntity.getInstance().removeFrame(UIVideoLoadingFrame.getInstance());
                }
                
                @Override
                public void onVideoStopped() {
                }
                
                @Override
                public void onVideoOutput() {
                }
                
                @Override
                public void onBuffering(final float buffering) {
                }
            });
            return true;
        }
        catch (Exception e) {
            UIVideoLoadingFrame.m_logger.error((Object)("Erreur durant la tentative de lecture de la video " + this.m_videoPath), (Throwable)e);
            Xulor.getInstance().unload("videoLoadingDialog");
            return false;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final Renderer renderer = WakfuClientInstance.getInstance().getRenderer();
            final AleaWorldScene worldScene = WakfuClientInstance.getInstance().getWorldScene();
            renderer.pushScene(worldScene, false);
            WakfuSoundManager.getInstance().setRunning(true);
            this.m_videoPath = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("videoLoadingDialog");
            this.m_scene = null;
            PropertiesProvider.getInstance().removeProperty("isNewWorldReady");
            Xulor.getInstance().removeActionClass("wakfu.videoLoading");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIVideoLoadingFrame.class);
        m_instance = new UIVideoLoadingFrame();
    }
}
