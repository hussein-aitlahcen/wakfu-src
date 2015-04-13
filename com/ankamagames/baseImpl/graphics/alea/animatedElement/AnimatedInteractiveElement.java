package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import com.ankamagames.baseImpl.graphics.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;

public class AnimatedInteractiveElement extends AnimatedElementWithDirection implements OverHeadTarget
{
    protected static final Logger m_logger;
    public static final short DEFAULT_MOBILE_HEIGHT = 6;
    private boolean m_selected;
    protected boolean m_selectedChanged;
    protected ArrayList<InteractiveElementSelectionChangeListener> m_selectionChangedListeners;
    
    public AnimatedInteractiveElement() {
        super();
        this.m_selected = false;
        this.m_selectedChanged = false;
        this.m_selectionChangedListeners = new ArrayList<InteractiveElementSelectionChangeListener>();
    }
    
    public AnimatedInteractiveElement(final long id) {
        super(id);
        this.m_selected = false;
        this.m_selectedChanged = false;
        this.m_selectionChangedListeners = new ArrayList<InteractiveElementSelectionChangeListener>();
    }
    
    public AnimatedInteractiveElement(final long id, final float worldX, final float worldY) {
        super(id, worldX, worldY);
        this.m_selected = false;
        this.m_selectedChanged = false;
        this.m_selectionChangedListeners = new ArrayList<InteractiveElementSelectionChangeListener>();
    }
    
    public AnimatedInteractiveElement(final long id, final float worldX, final float worldY, final float altitude) {
        super(id, worldX, worldY, altitude);
        this.m_selected = false;
        this.m_selectedChanged = false;
        this.m_selectionChangedListeners = new ArrayList<InteractiveElementSelectionChangeListener>();
    }
    
    @Override
    public void setVisualHeight(final short visualHeight) {
        this.m_visualHeight = visualHeight;
    }
    
    public boolean isSelected() {
        return this.m_selected;
    }
    
    @Override
    public void setSelected(final boolean selected) {
        if (selected != this.m_selected) {
            this.m_selected = selected;
            this.m_selectedChanged = true;
        }
    }
    
    public boolean isHighlightable() {
        return true;
    }
    
    public void addSelectionChangedListener(final InteractiveElementSelectionChangeListener listener) {
        if (!this.m_selectionChangedListeners.contains(listener)) {
            this.m_selectionChangedListeners.add(listener);
        }
    }
    
    public void removeSelectionChangedListener(final InteractiveElementSelectionChangeListener listener) {
        this.m_selectionChangedListeners.remove(listener);
    }
    
    public void removeAllSelectionChangedListener() {
        this.m_selectionChangedListeners.clear();
    }
    
    @Override
    public boolean update(final IsoWorldScene scene, final int deltaTime) {
        final boolean result = super.update(scene, deltaTime);
        if (this.m_selectedChanged) {
            this.notifySelectionListener();
        }
        return result;
    }
    
    @Override
    public void process(final AleaWorldScene scene, final int deltaTime) {
        super.process(scene, deltaTime);
        if (this.m_selectedChanged) {
            this.notifySelectionListener();
        }
    }
    
    private void notifySelectionListener() {
        for (final InteractiveElementSelectionChangeListener listener : this.m_selectionChangedListeners) {
            listener.selectionChanged(this, this.isSelected());
        }
        this.m_selectedChanged = false;
    }
    
    @Override
    public int getIconId() {
        return -1;
    }
    
    @Override
    public Color getOverHeadborderColor() {
        return Color.WHITE;
    }
    
    @Override
    public String getFormatedOverheadText() {
        return "";
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnimatedInteractiveElement.class);
    }
}
