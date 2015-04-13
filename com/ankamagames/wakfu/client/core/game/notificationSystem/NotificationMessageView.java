package com.ankamagames.wakfu.client.core.game.notificationSystem;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.reflect.*;

public class NotificationMessageView extends ImmutableFieldProvider
{
    public static final String ICON_STYLE_FIELD = "iconStyle";
    public static final String TITLE_FIELD = "title";
    public static final String TEXT_FIELD = "text";
    public static final String INDEX_FIELD = "index";
    public static final String TYPE = "type";
    public final String[] FIELDS;
    private final NotificationMessageType m_notificationMessageType;
    private final String m_title;
    private final String m_text;
    private final int m_soundId;
    private int m_index;
    private FieldProvider m_value;
    
    public NotificationMessageView(final NotificationMessageType type, final String title, final String text, final int soundId, final FieldProvider value) {
        super();
        this.FIELDS = new String[] { "iconStyle", "title", "text", "index", "type" };
        this.m_notificationMessageType = type;
        this.m_title = title;
        this.m_text = text;
        this.m_soundId = soundId;
        this.m_value = value;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconStyle")) {
            final String style = this.m_notificationMessageType.getIconStyle();
            return (style == null) ? "none" : style;
        }
        if (fieldName.equals("title")) {
            return this.m_title;
        }
        if (fieldName.equals("index")) {
            return this.m_index;
        }
        if (fieldName.equals("text")) {
            return this.m_text;
        }
        if (fieldName.equals("type")) {
            return this.m_notificationMessageType.getType();
        }
        if (this.m_value != null) {
            return this.m_value.getFieldValue(fieldName);
        }
        return null;
    }
    
    public NotificationMessageType getNotificationMessageType() {
        return this.m_notificationMessageType;
    }
    
    public int getIndex() {
        return this.m_index;
    }
    
    public void setIndex(final int index) {
        this.m_index = index;
    }
    
    public int getSoundId() {
        return this.m_soundId;
    }
    
    @Override
    public String toString() {
        return this.m_title + " index=" + this.m_index;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
