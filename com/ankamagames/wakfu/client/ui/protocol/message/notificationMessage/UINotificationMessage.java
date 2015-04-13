package com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.framework.reflect.*;

public class UINotificationMessage extends UIMessage
{
    private final String m_title;
    private final String m_text;
    private final int m_soundId;
    private final NotificationMessageType m_notificationMessageType;
    private final FieldProvider m_value;
    
    public UINotificationMessage(final String title, final String text, final NotificationMessageType notificationMessageType) {
        this(title, text, notificationMessageType, -1);
    }
    
    public UINotificationMessage(final String title, final String text, final NotificationMessageType notificationMessageType, final int soundId) {
        this(title, text, notificationMessageType, soundId, null);
    }
    
    public UINotificationMessage(final String title, final String text, final NotificationMessageType notificationMessageType, final int soundId, final FieldProvider value) {
        super();
        this.m_title = title;
        this.m_text = text;
        this.m_notificationMessageType = notificationMessageType;
        this.m_soundId = soundId;
        this.m_value = value;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public NotificationMessageType getNotificationMessageType() {
        return this.m_notificationMessageType;
    }
    
    public int getSoundId() {
        return this.m_soundId;
    }
    
    public FieldProvider getValue() {
        return this.m_value;
    }
    
    @Override
    public int getId() {
        return 16163;
    }
}
