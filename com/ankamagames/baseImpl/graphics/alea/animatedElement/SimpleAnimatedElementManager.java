package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import java.util.concurrent.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;

public class SimpleAnimatedElementManager extends AbstractAnimatedElementManager<AnimatedElement>
{
    private static final SimpleAnimatedElementManager m_instance;
    protected final ConcurrentHashMap<Long, AnimatedElement> m_animatedElements;
    protected final List<AnimatedElement> m_elementsToInvalidate;
    
    public SimpleAnimatedElementManager() {
        super();
        this.m_animatedElements = new ConcurrentHashMap<Long, AnimatedElement>();
        this.m_elementsToInvalidate = new ArrayList<AnimatedElement>();
    }
    
    public static SimpleAnimatedElementManager getInstance() {
        return SimpleAnimatedElementManager.m_instance;
    }
    
    public void addAnimatedElement(final AnimatedElement element) {
        this.m_animatedElements.put(element.getId(), element);
        this.fireElementAdded(element, element.getWorldCellX(), element.getWorldCellY(), (int)element.getAltitude());
    }
    
    public AnimatedElement removeAnimatedElement(final long elementId) {
        final AnimatedElement removed = this.m_animatedElements.remove(elementId);
        if (removed != null) {
            this.m_elementsToInvalidate.add(removed);
        }
        return removed;
    }
    
    public AnimatedElement removeAnimatedElement(final AnimatedElement element) {
        return this.removeAnimatedElement(element.getId());
    }
    
    public void removeAllAnimatedElements() {
        this.m_elementsToInvalidate.addAll(this.m_animatedElements.values());
        this.m_animatedElements.clear();
    }
    
    public AnimatedElement getAnimatedElement(final long elementId) {
        return this.m_animatedElements.get(elementId);
    }
    
    @Override
    public void process(final AleaWorldScene scene, final int deltaTime) {
        this.disposeInvalidated();
        for (final AnimatedElement element : this.m_animatedElements.values()) {
            element.update(scene, deltaTime);
        }
    }
    
    private void disposeInvalidated() {
        for (int i = 0, size = this.m_elementsToInvalidate.size(); i < size; ++i) {
            final AnimatedElement elementToInvalidate = this.m_elementsToInvalidate.get(i);
            elementToInvalidate.setSelected(false);
            this.m_displayedElements.remove(elementToInvalidate);
            elementToInvalidate.dispose();
        }
        this.m_elementsToInvalidate.clear();
    }
    
    @Override
    public void prepareBeforeRendering(final AleaWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        this.m_displayedElements.clear();
        for (final AnimatedElement element : this.m_animatedElements.values()) {
            if (element.addToScene(scene)) {
                element.setWatchersClipped(false);
                updateElementScreenPosition(element, scene);
                this.m_displayedElements.add((T)element);
            }
            else {
                element.setWatchersClipped(true);
            }
        }
    }
    
    private static void updateElementScreenPosition(final AnimatedElement element, final AleaWorldScene scene) {
        if (!element.hasWatchers()) {
            return;
        }
        final int newScreenHeight = IsoCameraFunc.getScreenHeight(scene, element.getVisualHeight());
        final Point2 p = IsoCameraFunc.getScreenPositionFromCenter(scene, element);
        final int newScreenX = MathHelper.fastRound(p.m_x);
        final int newScreenY = MathHelper.fastRound(p.m_y);
        if (newScreenX != element.getScreenX() || newScreenY != element.getScreenY() || newScreenHeight != element.getScreenTargetHeight()) {
            element.setScreenX(newScreenX);
            element.setScreenY(newScreenY);
            element.setScreenTargetHeight(newScreenHeight);
            element.fireScreenPositionChanged();
        }
    }
    
    public void clear() {
        for (final AnimatedElement animatedElement : this.m_animatedElements.values()) {
            animatedElement.dispose();
        }
        this.m_animatedElements.clear();
        this.m_displayedElements.clear();
    }
    
    @Override
    public void setUndefinedMaskLayer() {
        for (final AnimatedElement ae : this.m_animatedElements.values()) {
            MaskableHelper.setUndefined(ae);
        }
    }
    
    static {
        m_instance = new SimpleAnimatedElementManager();
    }
}
