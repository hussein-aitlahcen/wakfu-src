package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.script.*;

public class ItemEquippedScriptEvent extends FilterableEvent
{
    protected static final Logger m_logger;
    private Integer m_itemReferenceId;
    
    public ItemEquippedScriptEvent(final int itemReferenceId) {
        super();
        this.m_itemReferenceId = itemReferenceId;
    }
    
    @Override
    public short getId() {
        return 2;
    }
    
    @Override
    public LuaTable getInfoForLUA() {
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.m_itemReferenceId.hashCode();
    }
    
    @Override
    public boolean equals(final Object object) {
        return object instanceof ItemEquippedScriptEvent && ((ItemEquippedScriptEvent)object).m_itemReferenceId.equals(this.m_itemReferenceId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemEquippedScriptEvent.class);
    }
}
