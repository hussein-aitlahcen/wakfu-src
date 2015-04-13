package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorSatisfactionChangedEvent extends ProtectorEvent
{
    private ProtectorSatisfactionLevel m_protectorSatisfactionLevel;
    
    @Override
    public ProtectorMood getProtectorMood() {
        if (this.m_protectorSatisfactionLevel == ProtectorSatisfactionLevel.SATISFIED) {
            return ProtectorMood.HAPPY;
        }
        if (this.m_protectorSatisfactionLevel == ProtectorSatisfactionLevel.UNSATISFIED) {
            return ProtectorMood.HANGRY;
        }
        return ProtectorMood.NEUTRAL;
    }
    
    public void setProtectorSatisfactionLevel(final ProtectorSatisfactionLevel protectorSatisfactionLevel) {
        this.m_protectorSatisfactionLevel = protectorSatisfactionLevel;
    }
    
    public ProtectorSatisfactionLevel getProtectorSatisfactionLevel() {
        return this.m_protectorSatisfactionLevel;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ProtectorSatisfactionChangedEvent)) {
            return false;
        }
        final ProtectorSatisfactionChangedEvent protectorSatisfactionChangedEvent = (ProtectorSatisfactionChangedEvent)obj;
        return protectorSatisfactionChangedEvent.getProtectorSatisfactionLevel().equals(this.getProtectorSatisfactionLevel());
    }
    
    @Override
    public String[] getParams() {
        final String[] params = { String.valueOf(this.m_protectorSatisfactionLevel.getId()) };
        return params;
    }
}
