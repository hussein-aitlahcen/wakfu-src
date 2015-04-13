package com.ankamagames.wakfu.client.core.game.IsoWorldTarget;

import gnu.trove.*;

public class InteractiveIsoWorldTargetManager
{
    private static final InteractiveIsoWorldTargetManager m_instance;
    private final TLongObjectHashMap<InteractiveIsoWorldTarget> m_interactiveIsoWorldTargets;
    
    public static InteractiveIsoWorldTargetManager getInstance() {
        return InteractiveIsoWorldTargetManager.m_instance;
    }
    
    private InteractiveIsoWorldTargetManager() {
        super();
        this.m_interactiveIsoWorldTargets = new TLongObjectHashMap<InteractiveIsoWorldTarget>();
    }
    
    public void addInteractiveIsoWorldTarget(final long id, final InteractiveIsoWorldTarget target) {
        this.m_interactiveIsoWorldTargets.put(id, target);
    }
    
    public InteractiveIsoWorldTarget getInteractiveIsoWorldTarget(final long id) {
        return this.m_interactiveIsoWorldTargets.get(id);
    }
    
    public void removeInteractiveIsoWorldTarget(final long id) {
        this.m_interactiveIsoWorldTargets.remove(id);
    }
    
    public void removeAll() {
        this.m_interactiveIsoWorldTargets.clear();
    }
    
    static {
        m_instance = new InteractiveIsoWorldTargetManager();
    }
}
