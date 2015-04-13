package com.ankamagames.wakfu.client.ui.protocol.message.tutorial;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UITutorialMessage extends UIMessage
{
    private int m_eventActionId;
    private String m_title;
    private String m_desc;
    private String m_icon;
    private int m_type;
    
    public UITutorialMessage(final String title, final String desc, final String icon, final int type, final int eventActionId) {
        this(title, desc, icon, type);
        this.m_eventActionId = eventActionId;
    }
    
    public UITutorialMessage(final String title, final String desc, final String icon, final int type) {
        super();
        this.m_title = title;
        this.m_desc = desc;
        this.m_icon = icon;
        this.m_type = type;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    public String getDesc() {
        return this.m_desc;
    }
    
    public void setDesc(final String desc) {
        this.m_desc = desc;
    }
    
    public String getIcon() {
        return this.m_icon;
    }
    
    public void setIcon(final String icon) {
        this.m_icon = icon;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public void setType(final int type) {
        this.m_type = type;
    }
    
    public int getEventActionId() {
        return this.m_eventActionId;
    }
    
    @Override
    public int getId() {
        return 19148;
    }
}
