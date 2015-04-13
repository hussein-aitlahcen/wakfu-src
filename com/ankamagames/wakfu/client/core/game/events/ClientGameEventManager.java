package com.ankamagames.wakfu.client.core.game.events;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.events.listeners.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import gnu.trove.*;

public class ClientGameEventManager
{
    private static final Logger m_logger;
    public static final ClientGameEventManager INSTANCE;
    private final TIntObjectHashMap<ClientGameEventListener> m_listenersById;
    private final TIntObjectHashMap<ArrayList<ClientGameEventListener>> m_listenersByEventId;
    
    public ClientGameEventManager() {
        super();
        this.m_listenersById = new TIntObjectHashMap<ClientGameEventListener>();
        this.m_listenersByEventId = new TIntObjectHashMap<ArrayList<ClientGameEventListener>>();
    }
    
    public void registerEventListener(final ClientGameEventListener eventListener) {
        final int listenedEventId = eventListener.getListenedEventId();
        ArrayList<ClientGameEventListener> eventListeners = this.m_listenersByEventId.get(listenedEventId);
        if (eventListeners == null) {
            eventListeners = new ArrayList<ClientGameEventListener>();
            this.m_listenersByEventId.put(listenedEventId, eventListeners);
        }
        eventListeners.add(eventListener);
        this.m_listenersById.put(eventListener.getId(), eventListener);
    }
    
    public void fireEvent(final ClientGameEvent event) {
        final int eventId = event.getId();
        final String[] eventProperties = event.getProperties();
        final ArrayList<ClientGameEventListener> eventListeners = this.m_listenersByEventId.get(eventId);
        if (eventListeners == null || eventListeners.isEmpty()) {
            return;
        }
        for (int i = 0, size = eventListeners.size(); i < size; ++i) {
            final ClientGameEventListener eventListener = eventListeners.get(i);
            final String[] eventFilters = eventListener.getListenedEventFilters();
            try {
                if (eventListener.isActive() && this.match(eventProperties, eventFilters)) {
                    eventListener.processEvent(event);
                }
            }
            catch (Exception e) {
                ClientGameEventManager.m_logger.error((Object)("Probl\u00e8me \u00e0 l'execution d'un event Client id=" + eventListener.getId()), (Throwable)e);
            }
        }
    }
    
    public ClientGameEventListener getListener(final int id) {
        return this.m_listenersById.get(id);
    }
    
    private boolean match(final String[] eventProperties, final String[] eventFilters) {
        if (eventProperties == null || eventFilters == null || eventProperties.length == 0 || eventFilters.length == 0) {
            return true;
        }
        for (int i = 0; i < eventFilters.length; ++i) {
            if (eventProperties[i] != null && eventFilters[i] != null && !eventProperties[i].equals(eventFilters[i])) {
                return false;
            }
        }
        return true;
    }
    
    public void initialize() {
        Xulor.getInstance().addDialogLoadListener(ClientEventXulorListener.INSTANCE);
        Xulor.getInstance().addDialogUnloadListener(ClientEventXulorListener.INSTANCE);
    }
    
    public void setToDefaultActiveValue() {
        this.m_listenersById.forEachValue(new TObjectProcedure<ClientGameEventListener>() {
            @Override
            public boolean execute(final ClientGameEventListener object) {
                object.setToDefaultActiveValue();
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientGameEventManager.class);
        INSTANCE = new ClientGameEventManager();
    }
}
