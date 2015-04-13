package com.ankamagames.xulor2.core.messagebox;

import com.ankamagames.xulor2.*;
import java.util.*;

public class MessageBoxControler
{
    private final String m_messageBoxId;
    private final MessageBoxData m_data;
    private final ArrayList<MessageBoxEventListener> m_listeners;
    
    public MessageBoxControler(final String id, final MessageBoxData data) {
        super();
        this.m_messageBoxId = id;
        this.m_data = data;
        this.m_listeners = new ArrayList<MessageBoxEventListener>();
    }
    
    public String getMessageBoxId() {
        return this.m_messageBoxId;
    }
    
    public MessageBoxData getData() {
        return this.m_data;
    }
    
    public int getCategory() {
        return this.m_data.getCategory();
    }
    
    public int getLevel() {
        return this.m_data.getLevel();
    }
    
    public boolean canBeIgnored() {
        return this.m_data.canBeIgnored();
    }
    
    public void cleanUpAndRemove() {
        this.messageBoxClosed(this.m_data.getFirstButton(), null);
    }
    
    public void cleanUpAndRemoveQuick() {
        MessageBoxManager.getInstance().removeControler(this);
        Xulor.getInstance().unregisterMessageBoxControler(this);
        Xulor.getInstance().unload(this.m_messageBoxId, false);
    }
    
    public void messageBoxClosed(final int type, final String text) {
        this.cleanUpAndRemoveQuick();
        for (final MessageBoxEventListener listener : this.m_listeners) {
            listener.messageBoxClosed(type, text);
        }
    }
    
    public void addEventListener(final MessageBoxEventListener listener) {
        this.m_listeners.add(listener);
    }
    
    public void removeEventListener(final MessageBoxEventListener listener) {
        this.m_listeners.remove(listener);
    }
}
