package com.ankamagames.wakfu.client.core.game.protector.event;

abstract class ProtectorEventFactory
{
    protected final int m_eventId;
    
    protected ProtectorEventFactory(final int eventId) {
        super();
        this.m_eventId = eventId;
    }
    
    protected int getEventId() {
        return this.m_eventId;
    }
    
    protected abstract ProtectorEvent createProtectorEvent();
    
    protected ProtectorEvent getProtectorEvent() {
        final ProtectorEvent event = this.createProtectorEvent();
        event.setId(this.m_eventId);
        return event;
    }
}
