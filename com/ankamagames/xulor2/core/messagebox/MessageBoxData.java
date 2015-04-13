package com.ankamagames.xulor2.core.messagebox;

import org.jetbrains.annotations.*;
import java.util.*;

public class MessageBoxData
{
    private static String m_defaultIconUrl;
    private int m_category;
    private int m_level;
    private String m_message;
    private String m_title;
    private String m_iconUrl;
    private long m_options;
    private TextEditorParameters m_textEditorParameters;
    private int m_type;
    private ArrayList<String> m_customMessages;
    private final ArrayList<MessageBoxDataListener> m_listeners;
    
    public static void setDefaultIconUrl(final String defaultIconUrl) {
        MessageBoxData.m_defaultIconUrl = defaultIconUrl;
    }
    
    public MessageBoxData(final int category, final int level, final String message) {
        this(category, level, message, 2L);
    }
    
    public MessageBoxData(final int category, final int level, final String message, final long options) {
        this(category, level, message, " ", null, options);
    }
    
    public MessageBoxData(final int category, final int level, final String message, final String title, @Nullable final String iconUrl, final long options) {
        this(category, level, message, title, iconUrl, options, null, 0);
    }
    
    public MessageBoxData(final int category, final int level, final String message, final String title, final String iconUrl, final long options, @Nullable final ArrayList<String> customMessages, final int type) {
        super();
        this.m_listeners = new ArrayList<MessageBoxDataListener>();
        this.m_category = category;
        this.m_level = level;
        this.m_message = message;
        this.m_title = title;
        this.m_options = options;
        this.m_iconUrl = ((iconUrl != null) ? iconUrl : MessageBoxData.m_defaultIconUrl);
        this.m_customMessages = ((customMessages != null) ? new ArrayList<String>(customMessages) : new ArrayList<String>());
        this.m_type = type;
    }
    
    public boolean addListener(final MessageBoxDataListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    public int getCategory() {
        return this.m_category;
    }
    
    public MessageBoxData setCategory(final int category) {
        this.m_category = category;
        return this;
    }
    
    public int getLevel() {
        return this.m_level;
    }
    
    public MessageBoxData setLevel(final int level) {
        this.m_level = level;
        return this;
    }
    
    public TextEditorParameters getTextEditorParameters() {
        return this.m_textEditorParameters;
    }
    
    public void setTextEditorParameters(final TextEditorParameters textEditorParameters) {
        this.m_textEditorParameters = textEditorParameters;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public MessageBoxData setMessage(final String message) {
        this.m_message = message;
        for (final MessageBoxDataListener listener : this.m_listeners) {
            listener.messageChanged();
        }
        return this;
    }
    
    public long getOptions() {
        return this.m_options;
    }
    
    public MessageBoxData setOptions(final int options) {
        this.m_options = options;
        return this;
    }
    
    public ArrayList<String> getCustomMessages() {
        return this.m_customMessages;
    }
    
    public MessageBoxData setCustomMessages(final ArrayList<String> customMessages) {
        this.m_customMessages = customMessages;
        return this;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public MessageBoxData setTitle(final String title) {
        this.m_title = title;
        return this;
    }
    
    public MessageBoxData setIconUrl(final String iconUrl) {
        this.m_iconUrl = ((iconUrl != null) ? iconUrl : MessageBoxData.m_defaultIconUrl);
        return this;
    }
    
    public String getIconUrl() {
        return this.m_iconUrl;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public MessageBoxData setType(final int type) {
        this.m_type = type;
        return this;
    }
    
    public boolean canBeIgnored() {
        if ((this.m_options & 0x4000L) == 0x4000L) {
            return false;
        }
        int numButtons = 0;
        if ((this.m_options & 0x2L) == 0x2L) {
            ++numButtons;
        }
        if ((this.m_options & 0x4L) == 0x4L) {
            ++numButtons;
        }
        if ((this.m_options & 0x8L) == 0x8L) {
            ++numButtons;
        }
        if ((this.m_options & 0x10L) == 0x10L) {
            ++numButtons;
        }
        if ((this.m_options & 0x20L) == 0x20L) {
            ++numButtons;
        }
        if ((this.m_options & 0x40L) == 0x40L) {
            ++numButtons;
        }
        if ((this.m_options & 0x80L) == 0x80L) {
            ++numButtons;
        }
        if ((this.m_options & 0x100L) == 0x100L) {
            ++numButtons;
        }
        return numButtons < 2;
    }
    
    public int getFirstButton() {
        if ((this.m_options & 0x2L) == 0x2L) {
            return 2;
        }
        if ((this.m_options & 0x4L) == 0x4L) {
            return 4;
        }
        if ((this.m_options & 0x8L) == 0x8L) {
            return 8;
        }
        if ((this.m_options & 0x10L) == 0x10L) {
            return 16;
        }
        return 0;
    }
}
