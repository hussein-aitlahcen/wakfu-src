package com.ankamagames.wakfu.client.alea;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.baseImpl.graphics.alea.display.worldTransition.*;

public class WakfuSceneFader extends FadeSceneHelper
{
    private static final Logger m_logger;
    public static final String ANIMATED_ELEMENT_VIEWER_COMPONENT_NAME = "animatedElementViewer";
    public static final String STYLE = "WorldLoading";
    private static final WakfuSceneFader m_instance;
    private String m_anmName;
    private String m_videoName;
    private int m_soundId;
    
    public static WakfuSceneFader getInstance() {
        return WakfuSceneFader.m_instance;
    }
    
    public final void fade(final int fadeInDuration, final int fadeOutDuration, final int mintransitionTime, final String anmTransition, final String videoName, final int soundId) {
        this.setFadeDuration(fadeInDuration, fadeOutDuration);
        this.setMinTransitionDuration(mintransitionTime);
        this.m_anmName = anmTransition;
        this.m_videoName = videoName;
        this.m_soundId = soundId;
        this.fadeInMainAndParallax(anmTransition != null || videoName != null);
    }
    
    public void fastFade() {
        this.fade(1000, 1000, 0, null, null, 0);
    }
    
    @Override
    protected AbstractGameClientInstance getClientInstance() {
        return WakfuClientInstance.getInstance();
    }
    
    private void startAnmTransition() {
        final EventDispatcher eventDispatcher = Xulor.getInstance().load("progressDialog", Dialogs.getDialogPath("progressDialog"), 8448L, (short)19500);
        final AnimatedElementViewer viewer = (AnimatedElementViewer)eventDispatcher.getElementMap().getElement("animatedElementViewer");
        viewer.setStyle("WorldLoading");
        viewer.setAnimName(this.m_anmName);
    }
    
    private void startVideoTransition() {
        try {
            final String videoPath = String.format(WakfuConfiguration.getInstance().getString("videosPath"), this.m_videoName);
            UIVideoLoadingFrame.getInstance().setVideoPath(ContentFileHelper.transformFileNameForVlc(videoPath));
            UIVideoLoadingFrame.getInstance().setScenesToLoad(this.m_scene);
            WakfuGameEntity.getInstance().pushFrame(UIVideoLoadingFrame.getInstance());
            final FadeManager fadeManager = FadeManager.getInstance();
            WakfuSceneFader.m_logger.info((Object)"darkscreen- Condition startVideoTransition");
            fadeManager.addFadeOutCondition(new FadeOutCondition() {
                @Override
                public boolean isValid() {
                    if (!WakfuGameEntity.getInstance().hasFrame(UIVideoLoadingFrame.getInstance())) {
                        fadeManager.removeFadeOutCondition(this);
                        return true;
                    }
                    return false;
                }
            });
        }
        catch (PropertyException e) {
            WakfuSceneFader.m_logger.error((Object)"Impossible de r\u00e9cup\u00e9rer le chemin vers les videos");
        }
    }
    
    @Override
    protected final void startTransition() {
        try {
            if (this.m_anmName != null) {
                this.startAnmTransition();
            }
            else if (this.m_videoName != null) {
                this.startVideoTransition();
            }
            if (this.m_soundId != 0) {
                WakfuSoundManager.getInstance().startLoading(this.m_soundId);
            }
        }
        catch (Exception ex) {
            WakfuSceneFader.m_logger.error((Object)"", (Throwable)ex);
        }
    }
    
    private void stopAnmTransition() {
        Xulor.getInstance().unload("progressDialog");
    }
    
    private void stopVideoTransition() {
    }
    
    @Override
    protected final void stopTransition() {
        try {
            this.stopAnmTransition();
            this.stopVideoTransition();
            WakfuSoundManager.getInstance().stopLoading();
        }
        catch (Exception ex) {
            WakfuSceneFader.m_logger.error((Object)"", (Throwable)ex);
        }
    }
    
    public void sceneLoadingTransition(final int lastWorld) {
        final LoadingTransitionManager loading = LoadingTransitionManager.getInstance();
        if (lastWorld == -32768) {
            final int fadeInDuration = 1000;
            final int fadeOutDuration = 1000;
            final int transitionTime = 0;
            final String anmTransition = null;
            final String videoTransition = null;
            final int soundId = 0;
        }
        else {
            final int fadeInDuration = loading.getFadeInDuration();
            final int fadeOutDuration = loading.getFadeOutDuration();
            final int transitionTime = loading.getMinTransitionDuration();
            final String anmTransition = loading.getAnmName();
            final String videoTransition = loading.getVideoName();
            final int soundId = loading.getSoundId();
        }
        this.fastFade();
        loading.reset();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuSceneFader.class);
        m_instance = new WakfuSceneFader();
    }
}
