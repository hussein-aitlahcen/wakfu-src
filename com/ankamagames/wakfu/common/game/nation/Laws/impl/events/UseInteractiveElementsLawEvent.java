package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class UseInteractiveElementsLawEvent extends NationLawEvent
{
    private long m_elementId;
    
    public UseInteractiveElementsLawEvent(final Citizen citizen) {
        super(citizen);
    }
    
    public void setElementId(final long elementId) {
        this.m_elementId = elementId;
    }
    
    public long getElementId() {
        return this.m_elementId;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.USE_INTERACTIVE_ELEMENT;
    }
}
