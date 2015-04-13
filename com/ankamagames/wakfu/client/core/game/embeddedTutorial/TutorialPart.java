package com.ankamagames.wakfu.client.core.game.embeddedTutorial;

class TutorialPart
{
    private final short m_id;
    private boolean m_activated;
    private final String m_name;
    private final byte m_eventId;
    
    TutorialPart(final short id, final String name, final boolean activated, final byte eventId) {
        super();
        this.m_id = id;
        this.m_activated = activated;
        this.m_name = name;
        this.m_eventId = eventId;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isActivated() {
        return this.m_activated;
    }
    
    public void setActivated(final boolean activated) {
        this.m_activated = activated;
    }
    
    public byte getEventId() {
        return this.m_eventId;
    }
    
    @Override
    public String toString() {
        return "TutorialPart{m_id=" + this.m_id + ", m_activated=" + this.m_activated + ", m_name='" + this.m_name + '\'' + ", m_eventId=" + this.m_eventId + '}';
    }
}
