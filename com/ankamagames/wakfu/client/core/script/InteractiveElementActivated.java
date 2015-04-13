package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.events.*;
import com.ankamagames.framework.script.*;

public class InteractiveElementActivated extends FilterableEvent
{
    private final long m_elementId;
    private final short m_actionId;
    
    public InteractiveElementActivated(final long elementId, final short actionId) {
        super();
        this.m_elementId = elementId;
        this.m_actionId = actionId;
    }
    
    @Override
    public short getId() {
        return 1;
    }
    
    @Override
    public LuaTable getInfoForLUA() {
        return null;
    }
    
    @Override
    public int hashCode() {
        return new Long(this.m_elementId * this.m_actionId).hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof InteractiveElementActivated) {
            final InteractiveElementActivated test = (InteractiveElementActivated)obj;
            return this.m_elementId == test.m_elementId && this.m_actionId == test.m_actionId;
        }
        return false;
    }
}
