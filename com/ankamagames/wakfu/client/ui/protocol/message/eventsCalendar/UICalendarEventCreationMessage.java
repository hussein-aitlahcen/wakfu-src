package com.ankamagames.wakfu.client.ui.protocol.message.eventsCalendar;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UICalendarEventCreationMessage extends UIMessage
{
    private String m_title;
    private String m_description;
    private byte m_maxParticipants;
    private byte m_maxRegistrations;
    
    public UICalendarEventCreationMessage(final String title, final String desc, final byte maxParticipants, final byte maxRegistrations) {
        super();
        this.m_title = title;
        this.m_description = desc;
        this.m_maxParticipants = maxParticipants;
        this.m_maxRegistrations = maxRegistrations;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public byte getMaxParticipants() {
        return this.m_maxParticipants;
    }
    
    public byte getMaxRegistrations() {
        return this.m_maxRegistrations;
    }
}
