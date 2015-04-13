package com.ankamagames.baseImpl.client.proxyclient.base.chat;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.userGroup.*;
import java.util.*;

public abstract class ChatPipe
{
    public static final String CHAT_SUB_PIPE_INTERNAL_NAME = "subPipe";
    public static final int CHAT_SUB_PIPE_ID = -1;
    private static int MAX_HISTORY_PER_PIPE;
    private final int m_id;
    private String m_internalName;
    private boolean m_filterable;
    private String m_name;
    private UserGroup m_users;
    private LinkedList<ChatMessage> m_messages;
    protected List<ChatPipeListener> m_listeners;
    protected float[] m_color;
    protected HashMap<String, ChatPipe> m_subPipes;
    
    public ChatPipe(final int id, final String internalName, final float[] color, final String name, final boolean filterable) {
        super();
        this.m_internalName = null;
        this.m_filterable = false;
        this.m_name = null;
        this.m_listeners = new ArrayList<ChatPipeListener>();
        this.m_subPipes = new HashMap<String, ChatPipe>();
        this.m_id = id;
        this.m_internalName = internalName;
        this.m_color = color;
        this.m_name = name;
        this.m_filterable = filterable;
    }
    
    public String getInternalName() {
        return this.m_internalName;
    }
    
    public LinkedList<ChatMessage> getMessages() {
        return this.m_messages;
    }
    
    public UserGroup getUsers() {
        return this.m_users;
    }
    
    public void addSubPipe(final String key, final ChatPipe pipe) {
        if (this.m_subPipes == null) {
            this.m_subPipes = new HashMap<String, ChatPipe>();
        }
        if (!this.m_subPipes.containsKey(key)) {
            this.m_subPipes.put(key, pipe);
        }
        for (final ChatPipeListener listener : this.getListeners()) {
            listener.onSubPipeCreated(pipe, ChannelMode.INPUT);
        }
    }
    
    public void removeSubPipe(final ChatPipe pipe) {
        if (pipe == null) {
            return;
        }
        if (this.m_subPipes == null) {
            return;
        }
        this.m_subPipes.remove(pipe);
        if (pipe != null) {
            pipe.clean();
        }
    }
    
    public void pushMessage(final ChatMessage message) {
        if (this.m_messages == null) {
            this.m_messages = new LinkedList<ChatMessage>();
        }
        if (this.m_messages.size() > ChatPipe.MAX_HISTORY_PER_PIPE) {
            this.m_messages.removeLast();
        }
        this.m_messages.addFirst(message);
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onMessage(message);
        }
    }
    
    public void pushMessage(final ChatMessage message, final String subPipeKey) {
        final ChatPipe subPipe = this.m_subPipes.get(subPipeKey);
        if (subPipe != null) {
            subPipe.pushMessage(message);
        }
        else {
            this.pushMessage(message);
        }
    }
    
    public void onSubPipeInexistant(final String subPipeKey) {
    }
    
    public void addListener(final ChatPipeListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final ChatPipeListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public List<ChatPipeListener> getListeners() {
        return this.m_listeners;
    }
    
    public ChatPipe getSubPipe(final String name) {
        return this.m_subPipes.get(name);
    }
    
    public HashMap<String, ChatPipe> getSubPipes() {
        return this.m_subPipes;
    }
    
    public float[] getColor() {
        return this.m_color;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isFilterable() {
        return this.m_filterable;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setColor(final float r, final float v, final float b) {
        this.m_color = new float[] { r, v, b };
    }
    
    public void clean() {
        if (this.m_messages != null) {
            this.m_messages.clear();
        }
        for (final ChatPipe chatPipe : this.m_subPipes.values()) {
            chatPipe.clean();
        }
        this.m_subPipes.clear();
        this.m_listeners.clear();
    }
    
    public boolean isColorEditable() {
        return this.m_filterable;
    }
    
    public boolean isListenersEmpty() {
        return this.m_listeners.isEmpty();
    }
    
    static {
        ChatPipe.MAX_HISTORY_PER_PIPE = 100;
    }
}
