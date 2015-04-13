package com.ankamagames.baseImpl.client.proxyclient.base.console;

import java.util.*;

public abstract class AbstractInputHistoryManager
{
    private static final int HISTORY_LINES_MAX_COUNT = 100;
    private Stack<String> m_history;
    private ListIterator<String> m_historyIterator;
    
    public AbstractInputHistoryManager() {
        super();
        this.m_history = new Stack<String>();
    }
    
    public void clear() {
        this.m_history.clear();
    }
    
    public String getHistoryUp() {
        if (this.m_historyIterator != null && this.m_historyIterator.hasPrevious()) {
            return this.m_historyIterator.previous();
        }
        return "";
    }
    
    public String getHistoryDown() {
        if (this.m_historyIterator != null && this.m_historyIterator.hasNext()) {
            return this.m_historyIterator.next();
        }
        return "";
    }
    
    protected void pushToHistory(final String input) {
        if (!this.m_history.isEmpty()) {
            final String lastInput = this.m_history.lastElement();
            if (lastInput == null || !lastInput.equals(input)) {
                if (this.m_history.size() >= 100) {
                    this.m_history.remove(this.m_history.lastElement());
                }
                this.m_history.push(input);
            }
        }
        else {
            this.m_history.push(input);
        }
        this.m_historyIterator = this.m_history.listIterator(this.m_history.size());
    }
}
