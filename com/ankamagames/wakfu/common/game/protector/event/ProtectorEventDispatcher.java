package com.ankamagames.wakfu.common.game.protector.event;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.protector.*;

public final class ProtectorEventDispatcher
{
    public static final ProtectorEventDispatcher INSTANCE;
    private static final Logger m_logger;
    private final ArrayList<ProtectorEventListener> m_listeners;
    
    private ProtectorEventDispatcher() {
        super();
        this.m_listeners = new ArrayList<ProtectorEventListener>();
    }
    
    public void addListener(final ProtectorEventListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final ProtectorEventListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public void dispatch(final ProtectorEvent event) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            final ProtectorBase protector = event.getProtector();
            if (protector != null) {
                final ProtectorEventListener listener = this.m_listeners.get(i);
                try {
                    listener.onProtectorEvent(event);
                }
                catch (Exception e) {
                    ProtectorEventDispatcher.m_logger.error((Object)("Exception lev\u00e9e lors du traitement d'un evenement de protecteur " + protector.getId() + ", par un listener : " + listener.getClass().getSimpleName()), (Throwable)e);
                }
            }
        }
    }
    
    static {
        INSTANCE = new ProtectorEventDispatcher();
        m_logger = Logger.getLogger((Class)ProtectorEventDispatcher.class);
    }
}
