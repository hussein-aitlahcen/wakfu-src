package com.ankamagames.baseImpl.graphics.alea.display.worldTransition;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import com.ankamagames.framework.graphics.opengl.base.*;
import com.ankamagames.framework.graphics.opengl.*;

public abstract class FadeSceneHelper
{
    private static final Logger m_logger;
    protected AleaWorldScene m_scene;
    private int m_fadeInDuration;
    private int m_fadeOutDuration;
    private int m_minTransitionDuration;
    private FadeOutCondition m_waitForWorld;
    
    protected FadeSceneHelper() {
        super();
        this.m_minTransitionDuration = 0;
        this.setDefaultFadeDuration();
    }
    
    public final void setFadeDuration(final int fadeInDuration, final int fadeOutDuration) {
        this.m_fadeInDuration = fadeInDuration;
        this.m_fadeOutDuration = fadeOutDuration;
    }
    
    public final void setDefaultFadeDuration() {
        this.setFadeDuration(1000, 1000);
    }
    
    public void setMinTransitionDuration(final int minTransitionDuration) {
        this.m_minTransitionDuration = minTransitionDuration;
    }
    
    private void getSceneToFade() {
        final AbstractGameClientInstance clientInstance = this.getClientInstance();
        this.m_scene = clientInstance.getWorldScene();
    }
    
    protected abstract AbstractGameClientInstance getClientInstance();
    
    protected abstract void stopTransition();
    
    protected abstract void startTransition();
    
    public final void fadeInMainAndParallax(final boolean useTransition) {
        if (useTransition) {
            this.prepareFadeTransition(this.createTransition(), this.m_minTransitionDuration);
        }
        this.getSceneToFade();
        this.fadeIn(this.m_fadeInDuration, this.m_fadeOutDuration, this.m_scene);
    }
    
    private FadeListener createTransition() {
        return new FadeListener() {
            @Override
            public void onFadeInStart() {
            }
            
            @Override
            public void onFadeOutStart() {
                FadeSceneHelper.this.stopTransition();
            }
            
            @Override
            public void onFadeInEnd() {
                FadeSceneHelper.this.startTransition();
            }
            
            @Override
            public void onFadeOutEnd() {
                FadeManager.getInstance().removeListener(this);
            }
        };
    }
    
    private void prepareFadeTransition(final FadeListener transition, final int minTransitionTime) {
        final FadeManager fadeManager = FadeManager.getInstance();
        fadeManager.addListener(transition);
        if (minTransitionTime > 0) {
            final long time = System.currentTimeMillis();
            fadeManager.addFadeOutCondition(new FadeOutCondition() {
                @Override
                public boolean isValid() {
                    if (System.currentTimeMillis() - time < minTransitionTime) {
                        return false;
                    }
                    fadeManager.removeFadeOutCondition(this);
                    return true;
                }
            });
        }
    }
    
    public final void prepareFadeIn() {
        this.getSceneToFade();
        FadeManager.getInstance().reset();
        this.addWorldReadyCondition(FadeManager.getInstance(), this.m_fadeInDuration, this.m_scene);
    }
    
    public void addWorldReadyCondition(final FadeManager fadeManager, final int fadeInDuration, final AleaWorldScene scene) {
        if (this.m_waitForWorld != null) {
            FadeSceneHelper.m_logger.warn((Object)"on \u00e9tait d\u00e9j\u00e0 en attente d'un monde", (Throwable)new Exception());
            this.m_waitForWorld = null;
        }
        fadeManager.addFadeOutCondition(this.m_waitForWorld = new FadeOutCondition() {
            private final long time = System.currentTimeMillis();
            private boolean stopLog = false;
            
            @Override
            public boolean isValid() {
                if (!scene.isWorldReady()) {
                    if (!this.stopLog && System.currentTimeMillis() - this.time < fadeInDuration) {
                        this.stopLog = true;
                    }
                    return false;
                }
                fadeManager.removeFadeOutCondition(this);
                FadeSceneHelper.this.m_waitForWorld = null;
                return true;
            }
        });
    }
    
    private void fadeIn(final int fadeInDuration, final int fadeOutDuration, final AleaWorldScene scene) {
        final FadeManager fadeManager = FadeManager.getInstance();
        final Renderer renderer = this.getClientInstance().getRenderer();
        renderer.removeMouseController(scene);
        renderer.removeKeyboardController(scene);
        final AleaWorldScene duplicate = scene.duplicate();
        fadeManager.fadeIn(fadeInDuration, duplicate);
        scene.resetRenderTree();
        fadeManager.setFadeOutDuration(fadeOutDuration);
        this.addWorldReadyCondition(fadeManager, fadeInDuration, scene);
        fadeManager.addListener(new FadeListener() {
            @Override
            public void onFadeInEnd() {
                fadeManager.removeListener(this);
                duplicate.cleanAfterFade();
                renderer.pushMouseController(scene, false);
                renderer.pushKeyboardController(scene, false);
            }
            
            @Override
            public void onFadeOutEnd() {
            }
            
            @Override
            public void onFadeInStart() {
            }
            
            @Override
            public void onFadeOutStart() {
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)FadeSceneHelper.class);
    }
}
