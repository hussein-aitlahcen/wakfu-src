package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;

public class ChatWindow
{
    private final Window m_window;
    private final EventListener m_resizeEventListener;
    
    public ChatWindow(final Window window, final ChatViewManager chatViewManager) {
        super();
        this.m_window = window;
        this.m_resizeEventListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final ElementMap map = event.getTarget().getElementMap();
                if (map != null && map.getId() != null && map.getId().equals(window.getId())) {
                    chatViewManager.getCurrentView().reflowScrollNeed();
                    return false;
                }
                return true;
            }
        };
        this.m_window.setEnableResizeEvents(true);
        this.m_window.addEventListener(Events.RESIZED, this.m_resizeEventListener, true);
    }
    
    public Window getWindow() {
        return this.m_window;
    }
    
    public void removeListeners() {
        this.m_window.removeEventListener(Events.RESIZED, this.m_resizeEventListener, true);
    }
}
