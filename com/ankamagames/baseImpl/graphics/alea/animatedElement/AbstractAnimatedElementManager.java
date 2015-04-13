package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;

public abstract class AbstractAnimatedElementManager<T extends AnimatedElement> implements RenderProcessHandler<AleaWorldScene>, LitScene
{
    protected final ArrayList<T> m_hitElements;
    protected final ArrayList<T> m_elementsAtCoord;
    protected final ArrayList<T> m_displayedElements;
    
    public AbstractAnimatedElementManager() {
        super();
        this.m_hitElements = new ArrayList<T>();
        this.m_elementsAtCoord = new ArrayList<T>();
        this.m_displayedElements = new ArrayList<T>();
    }
    
    public ArrayList<T> getElementUnderMousePoint(final float x, final float y) {
        this.m_hitElements.clear();
        final ArrayList<T> elements = this.m_displayedElements;
        for (int i = 0, size = elements.size(); i < size; ++i) {
            final T element = elements.get(i);
            if (element.hitTest(x, y)) {
                this.m_hitElements.add(element);
            }
        }
        return this.m_hitElements;
    }
    
    public T getNearestElement(final float x, final float y) {
        final ArrayList<T> hitElements = this.getElementUnderMousePoint(x, y);
        T nearestElement = null;
        for (int i = 0, size = hitElements.size(); i < size; ++i) {
            final T elem = hitElements.get(i);
            if (nearestElement == null || elem.getZOrder() > nearestElement.getZOrder()) {
                nearestElement = elem;
            }
        }
        return nearestElement;
    }
    
    public ArrayList<T> getElementsAtCoordinates(final int worldCellX, final int worldCellY) {
        this.m_elementsAtCoord.clear();
        for (final T element : this.m_displayedElements) {
            if (element.getWorldCellX() == worldCellX && element.getWorldCellY() == worldCellY) {
                this.m_elementsAtCoord.add(element);
            }
        }
        return this.m_elementsAtCoord;
    }
    
    public T getNearestElementAtCoordinates(final int worldCellX, final int worldCellY) {
        final ArrayList<T> elements = this.getElementsAtCoordinates(worldCellX, worldCellY);
        T nearestElement = null;
        for (int i = 0, size = elements.size(); i < size; ++i) {
            final T elem = elements.get(i);
            if (nearestElement == null || elem.getZOrder() > nearestElement.getZOrder()) {
                nearestElement = elem;
            }
        }
        return nearestElement;
    }
    
    public final void queryDisplayed(final ArrayList<? super T> elements) {
        for (int count = this.m_displayedElements.size(), i = 0; i < count; ++i) {
            elements.add((Object)this.m_displayedElements.get(i));
        }
    }
    
    @Override
    public final void queryObjects(final AbstractCamera camera, final ArrayList<LitSceneObject> objects) {
        for (int i = 0, count = this.m_displayedElements.size(); i < count; ++i) {
            objects.add(this.m_displayedElements.get(i));
        }
    }
    
    protected void fireElementAdded(final T element, final int x, final int y, final int z) {
        MaskableHelper.setUndefined(element);
    }
    
    public void forceReload() {
        for (int i = 0, size = this.m_displayedElements.size(); i < size; ++i) {
            final T element = this.m_displayedElements.get(i);
            element.forceEnableAlphaMask(element.isAlphaMaskEnabled());
            element.resetColor();
            element.forceReloadAnimation();
        }
    }
    
    public void unselectAllElementExceptThis(final AnimatedInteractiveElement elem) {
        final ArrayList<T> elements = this.m_displayedElements;
        for (int i = 0, size = elements.size(); i < size; ++i) {
            final T element = elements.get(i);
            if (element != elem) {
                element.setSelected(false);
            }
        }
    }
    
    public final ArrayList<T> getDisplayedElements() {
        return this.m_displayedElements;
    }
    
    public abstract void setUndefinedMaskLayer();
}
