package com.ankamagames.baseImpl.graphics.ui;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.script.*;
import java.util.*;

public final class ScriptUIEventManager
{
    protected static final Logger m_logger;
    private static final ScriptUIEventManager m_instance;
    private UIManager m_uiManager;
    private final HashMap<EventDatas, EventListener> m_eventListener;
    private final HashMap<String, DialogLoadListener> m_dialogLoadListeners;
    private final HashMap<String, DialogUnloadListener> m_dialogUnloadListeners;
    
    private ScriptUIEventManager() {
        super();
        this.m_eventListener = new HashMap<EventDatas, EventListener>();
        this.m_dialogLoadListeners = new HashMap<String, DialogLoadListener>();
        this.m_dialogUnloadListeners = new HashMap<String, DialogUnloadListener>();
    }
    
    public static ScriptUIEventManager getInstance() {
        return ScriptUIEventManager.m_instance;
    }
    
    public void setUiManager(final UIManager uiManager) {
        this.m_uiManager = uiManager;
    }
    
    public final void putEventListener(final LuaScript script, final String dialogName, final String controlId, final String eventType, final String funcName, final EventListener el) {
        final EventDatas datas = new EventDatas(script, dialogName, controlId, eventType, funcName);
        this.m_eventListener.put(datas, el);
    }
    
    public final EventListener eventListenerAlreadyExist(final LuaScript script, final String dialogName, final String controlId, final String eventType, final String funcName) {
        final EventDatas datas = new EventDatas(script, dialogName, controlId, eventType, funcName);
        return this.m_eventListener.get(datas);
    }
    
    public final EventListener removeEventListener(final LuaScript script, final String dialogName, final String controlId, final String eventType, final String funcName) {
        final EventDatas datas = new EventDatas(script, dialogName, controlId, eventType, funcName);
        return this.m_eventListener.remove(datas);
    }
    
    public final void addDialogUnloadListener(final String dialog, final DialogUnloadListener listener) {
        this.m_dialogUnloadListeners.put(dialog, listener);
        this.m_uiManager.addDialogUnloadListener(listener);
    }
    
    public final void removeDialogUnloadListener(final String dialog) {
        final DialogUnloadListener listener = this.m_dialogUnloadListeners.remove(dialog);
        if (listener != null) {
            this.m_uiManager.removeDialogUnloadListener(listener);
        }
        else {
            ScriptUIEventManager.m_logger.error((Object)("Aucun DialogUnloadListener n'est enregistr\u00e9 pour " + dialog));
        }
    }
    
    public final void addDialogLoadListener(final String dialog, final DialogLoadListener listener) {
        this.m_dialogLoadListeners.put(dialog, listener);
        this.m_uiManager.addDialogLoadListener(listener);
    }
    
    public final void removeDialogLoadListener(final String dialog) {
        final DialogLoadListener listener = this.m_dialogLoadListeners.remove(dialog);
        if (listener != null) {
            this.m_uiManager.removeDialogLoadListener(listener);
        }
        else {
            ScriptUIEventManager.m_logger.error((Object)("Aucun DialogLoadListener n'est enregistr\u00e9 pour " + dialog));
        }
    }
    
    public void removeAllEventListener(final LuaScript script) {
        final Set<Map.Entry<EventDatas, EventListener>> entries = this.m_eventListener.entrySet();
        final ArrayList<EventDatas> toRemove = new ArrayList<EventDatas>();
        for (final Map.Entry<EventDatas, EventListener> entry : entries) {
            if (entry.getKey().getScript() == script) {
                toRemove.add(entry.getKey());
            }
        }
        for (int i = toRemove.size() - 1; i >= 0; --i) {
            this.m_eventListener.remove(toRemove.get(i));
        }
    }
    
    public void removeAllEventListener(final String dialogName) {
        final Set<Map.Entry<EventDatas, EventListener>> entries = this.m_eventListener.entrySet();
        final ArrayList<EventDatas> toRemove = new ArrayList<EventDatas>();
        for (final Map.Entry<EventDatas, EventListener> entry : entries) {
            if (entry.getKey().getDialogName().equals(dialogName)) {
                toRemove.add(entry.getKey());
            }
        }
        for (int i = toRemove.size() - 1; i >= 0; --i) {
            this.m_eventListener.remove(toRemove.get(i));
        }
    }
    
    public void clean() {
        this.m_eventListener.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScriptUIEventManager.class);
        m_instance = new ScriptUIEventManager();
    }
    
    private class EventDatas
    {
        private final LuaScript m_script;
        private final String m_dialogName;
        private final String m_controlName;
        private final String m_eventType;
        private final String m_functionName;
        private final String m_key;
        
        private EventDatas(final LuaScript script, final String controlName, final String dialogName, final String eventType, final String functionName) {
            super();
            this.m_controlName = controlName;
            this.m_dialogName = dialogName;
            this.m_eventType = eventType;
            this.m_functionName = functionName;
            this.m_script = script;
            this.m_key = new StringBuilder(script.getId()).append("|").append(dialogName).append("|").append(controlName).append("|").append(eventType).append("|").append(functionName).toString();
        }
        
        public String getControlName() {
            return this.m_controlName;
        }
        
        public String getDialogName() {
            return this.m_dialogName;
        }
        
        public String getEventType() {
            return this.m_eventType;
        }
        
        public String getFunctionName() {
            return this.m_functionName;
        }
        
        public LuaScript getScript() {
            return this.m_script;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            final EventDatas ed = (EventDatas)obj;
            return this.m_key.equals(ed.m_key);
        }
        
        @Override
        public int hashCode() {
            return this.m_key.hashCode();
        }
    }
}
