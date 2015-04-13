package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.material.*;

public class SmileyWidget extends WatcherContainer implements VisibleChangedListener
{
    private static final Logger m_logger;
    public static final String TAG = "smiley";
    public static final String ANIMATED_ELEMENT_VIEWER_COMPONENT_NAME = "animatedElementViewer";
    private String m_widgetId;
    private boolean m_unloadOnClean;
    private boolean m_smileyIsVisible;
    public static int UID;
    private AnimatedElementViewer m_animatedElementViewer;
    private float m_scale;
    
    public SmileyWidget() {
        super();
        this.m_smileyIsVisible = true;
    }
    
    public void initialize(final String widgetId, final String animation) throws Exception {
        this.initialize(widgetId, animation, true);
    }
    
    public void initialize(final String widgetId, final String animation, final boolean unloadOnClean) throws Exception {
        this.m_widgetId = widgetId;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.m_widgetId);
        assert map != null : "Impossible de charger un smiley";
        this.m_animatedElementViewer = (AnimatedElementViewer)map.getElement("animatedElementViewer");
        if (this.m_animatedElementViewer == null) {
            throw new Exception("On a charg\u00e9 un Widget de Smiley, mais il n'a pas de widget d'animatedElementViewer...");
        }
        this.m_unloadOnClean = unloadOnClean;
        this.setAnimation(animation);
        this.m_scale = this.m_animatedElementViewer.getAnimatedElement().getScale();
    }
    
    public void setAnimation(final String animation) {
        this.m_animatedElementViewer.setAnimName(animation);
    }
    
    public void setTarget(final AnimatedElementWithDirection target) {
        this.setTarget((AnimatedElement)target);
    }
    
    public void setTarget(final AnimatedElement target) {
        if (target == this.m_target) {
            return;
        }
        if (this.m_target != null && this.m_target instanceof AnimatedElement) {
            ((AnimatedElement)this.m_target).removeVisibleChangedListener(this);
        }
        super.setTarget(target);
        if (target != null) {
            target.addVisibleChangedListener(this);
            this.setTargetIsVisible(target.isVisible());
        }
    }
    
    @Override
    public void onAdviserCleanUp() {
        if (this.m_unloadOnClean) {
            this.unloadWidget();
        }
        else {
            this.setSmileyIsVisible(false);
        }
    }
    
    public void setSmileyIsVisible(final boolean smileyIsVisible) {
        if (this.m_smileyIsVisible == smileyIsVisible) {
            return;
        }
        this.m_smileyIsVisible = smileyIsVisible;
        this.updateVisibility();
    }
    
    @Override
    protected void updateVisibility() {
        this.setVisible(this.m_targetIsVisible && this.m_smileyIsVisible);
    }
    
    @Override
    public final void invalidate() {
        super.invalidate();
    }
    
    @Override
    public Point2i getComputedPosition(final int x, final int y, final int height) {
        final Point2i position = super.getComputedPosition(x, y, height);
        final int newX = position.getX();
        final int newY = position.getY();
        position.set(newX, newY);
        return position;
    }
    
    @Override
    public void screenTargetMoved(final ScreenTarget target, final int x, final int y, final int height) {
        this.updateVisibility();
        super.screenTargetMoved(target, x, y, 0);
    }
    
    @Override
    protected void process(final int deltaTime, final float zoomFactor) {
        super.process(deltaTime, zoomFactor);
        final float scale = zoomFactor - 0.5f + this.m_scale;
        this.m_animatedElementViewer.setScale(scale);
        float alpha = 1.0f;
        final long elapsedTime = this.getWatcherContainerAdviser().getElapsedLifeTime();
        final int duration = this.getDuration();
        if (elapsedTime < duration / 4) {
            alpha = elapsedTime / (duration / 4.0f);
        }
        else if (elapsedTime > duration * 3 / 4) {
            alpha = 1.0f - (elapsedTime - 3 * duration / 4.0f) / (duration - 3 * duration / 4.0f);
        }
        final Material m = Material.Factory.newPooledInstance();
        m.setDiffuseColor(alpha, alpha, alpha, alpha);
        this.m_animatedElementViewer.setMaterial(m);
        m.removeReference();
    }
    
    private static float easeOut(float t, final float b, final float c, final float d) {
        return -c * ((t = t / d - 1.0f) * t * t * t - 1.0f) + b;
    }
    
    public void setDuration(final int duration) {
        this.m_watcherContainerAdviser.setDuration(duration);
    }
    
    public void resetElapsedLifeTime() {
        this.m_watcherContainerAdviser.setElapsedLifeTime(0);
        this.updateVisibility();
    }
    
    public AnimatedElementViewer getAnimatedElementViewer() {
        return this.m_animatedElementViewer;
    }
    
    public int getAdviserId() {
        return this.m_watcherContainerAdviser.getId();
    }
    
    public String getWidgetId() {
        return this.m_widgetId;
    }
    
    public void unloadWidget() {
        Xulor.getInstance().unload(this.m_widgetId);
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
        this.setTargetIsVisible(visible);
    }
    
    @Override
    public float getWorldX() {
        if (this.m_target != null) {
            return this.m_target.getWorldX();
        }
        return 0.0f;
    }
    
    @Override
    public float getWorldY() {
        if (this.m_target != null) {
            return this.m_target.getWorldY();
        }
        return 0.0f;
    }
    
    @Override
    public float getAltitude() {
        if (this.m_target != null) {
            return this.m_target.getAltitude();
        }
        return 0.0f;
    }
    
    public int getDuration() {
        return this.m_watcherContainerAdviser.getDuration();
    }
    
    @Override
    public void removedFromWidgetTree() {
        super.removedFromWidgetTree();
        if (this.m_target != null && this.m_target instanceof AnimatedElement) {
            ((AnimatedElement)this.m_target).removeVisibleChangedListener(this);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SmileyWidget.class);
        SmileyWidget.UID = Integer.MIN_VALUE;
    }
}
