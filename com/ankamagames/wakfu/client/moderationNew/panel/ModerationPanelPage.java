package com.ankamagames.wakfu.client.moderationNew.panel;

public enum ModerationPanelPage
{
    MAIN(0), 
    PLAYER(1);
    
    private final int m_id;
    
    private ModerationPanelPage(final int id) {
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
}
