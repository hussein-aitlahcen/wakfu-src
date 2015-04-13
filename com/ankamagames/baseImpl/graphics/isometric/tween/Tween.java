package com.ankamagames.baseImpl.graphics.isometric.tween;

import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;

public abstract class Tween
{
    private boolean m_removable;
    private List<TweenListener> m_listeners;
    protected final IsoWorldTarget m_target;
    
    public Tween(final IsoWorldTarget target) {
        super();
        this.m_removable = false;
        this.m_listeners = null;
        this.m_target = target;
    }
    
    public void addListener(final TweenListener listener) {
        if (this.m_listeners == null) {
            this.m_listeners = new ArrayList<TweenListener>();
        }
        this.m_listeners.add(listener);
    }
    
    public void removeListener(final TweenListener listener) {
        if (this.m_listeners == null) {
            return;
        }
        this.m_listeners.remove(listener);
    }
    
    public boolean isRemovable() {
        return this.m_removable;
    }
    
    public void endTween() {
        this.m_removable = true;
        if (this.m_listeners != null) {
            for (final TweenListener listener : this.m_listeners) {
                listener.onTweenEnd(this);
            }
        }
    }
    
    public abstract float getTweenDuration();
    
    public abstract void process(final int p0);
}
