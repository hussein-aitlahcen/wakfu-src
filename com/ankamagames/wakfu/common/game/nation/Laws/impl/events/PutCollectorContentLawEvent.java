package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class PutCollectorContentLawEvent extends NationLawEvent
{
    private long m_elementId;
    private int m_moneyAmount;
    
    public PutCollectorContentLawEvent(final Citizen citizen) {
        super(citizen);
    }
    
    public void setElementId(final long elementId) {
        this.m_elementId = elementId;
    }
    
    public long getElementId() {
        return this.m_elementId;
    }
    
    public void setMoneyAmount(final int moneyAmount) {
        this.m_moneyAmount = moneyAmount;
    }
    
    public int getMoneyAmount() {
        return this.m_moneyAmount;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.PUT_COLLECTOR_CONTENT;
    }
}
