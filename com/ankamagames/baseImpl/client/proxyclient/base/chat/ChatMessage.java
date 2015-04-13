package com.ankamagames.baseImpl.client.proxyclient.base.chat;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class ChatMessage implements Comparable<ChatMessage>
{
    private int m_windowId;
    private int m_pipeDestination;
    private long m_sourceId;
    private String m_sourceName;
    private String m_message;
    private String m_color;
    private final GameDateConst m_time;
    private final long m_messageId;
    private boolean m_resizable;
    private static long m_uid;
    
    public ChatMessage(final String sourceName, final String message) {
        super();
        this.m_windowId = -1;
        this.m_sourceId = -1L;
        this.m_sourceName = sourceName;
        this.m_message = message;
        this.m_time = BaseGameDateProvider.INSTANCE.getDate();
        if (ChatMessage.m_uid == Long.MAX_VALUE) {
            ChatMessage.m_uid = Long.MIN_VALUE;
        }
        this.m_messageId = ChatMessage.m_uid++;
    }
    
    public ChatMessage(final String message) {
        this(null, message);
    }
    
    public ChatMessage(final long sourceId, final String message) {
        this(message);
        this.m_sourceId = sourceId;
    }
    
    public ChatMessage(final String sourceName, final long sourceId, final String message) {
        this(sourceName, message);
        this.m_sourceId = sourceId;
    }
    
    public int getPipeDestination() {
        return this.m_pipeDestination;
    }
    
    public void setPipeDestination(final int pipeDestination) {
        this.m_pipeDestination = pipeDestination;
    }
    
    public void setWindowId(final int windowId) {
        this.m_windowId = windowId;
    }
    
    public int getWindowId() {
        return this.m_windowId;
    }
    
    public long getSourceId() {
        return this.m_sourceId;
    }
    
    public String getSourceName() {
        return this.m_sourceName;
    }
    
    public void setSourceName(final String sourceName) {
        this.m_sourceName = sourceName;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public void setMessage(final String message) {
        this.m_message = message;
    }
    
    public GameDateConst getTime() {
        return this.m_time;
    }
    
    public void setColor(final String color) {
        this.m_color = color;
    }
    
    public String getColor() {
        return this.m_color;
    }
    
    public boolean isResizable() {
        return this.m_resizable;
    }
    
    public void setResizable(final boolean resizable) {
        this.m_resizable = resizable;
    }
    
    @Override
    public int compareTo(final ChatMessage chatMessage) {
        final int diff = this.m_time.compareTo(chatMessage.m_time);
        if (diff == 0) {
            return (int)(this.m_messageId - chatMessage.m_messageId);
        }
        return diff;
    }
    
    static {
        ChatMessage.m_uid = Long.MIN_VALUE;
    }
}
