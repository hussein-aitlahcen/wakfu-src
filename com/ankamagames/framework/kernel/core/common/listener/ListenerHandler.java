package com.ankamagames.framework.kernel.core.common.listener;

import java.util.*;

public class ListenerHandler<L>
{
    private final ArrayList<L> m_listeners;
    private final ArrayList<L> m_listenerToRemove;
    private final ListenerNotifier<L> m_notifier;
    
    public ListenerHandler(final ListenerNotifier<L> notifier) {
        super();
        this.m_listeners = new ArrayList<L>();
        this.m_listenerToRemove = new ArrayList<L>();
        this.m_notifier = notifier;
    }
    
    public void notifyListeners() {
        this.m_listeners.removeAll(this.m_listenerToRemove);
        this.m_listenerToRemove.clear();
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_notifier.notify(this.m_listeners.get(i));
        }
    }
    
    public boolean addListener(final L l) {
        this.m_listenerToRemove.remove(l);
        return !this.m_listeners.contains(l) && this.m_listeners.add(l);
    }
    
    public boolean removeListener(final L l) {
        return !this.m_listenerToRemove.contains(l) && this.m_listenerToRemove.add(l);
    }
    
    public boolean contains(final L l) {
        return this.m_listeners.contains(l) && !this.m_listenerToRemove.contains(l);
    }
    
    public void clear() {
        this.m_listeners.clear();
        this.m_listenerToRemove.clear();
    }
    
    @Override
    public String toString() {
        return "ListenerHandler{m_listeners=" + this.m_listeners.size() + ", m_listenerToRemove=" + this.m_listenerToRemove.size() + ", m_notifier=" + this.m_notifier + '}';
    }
}
