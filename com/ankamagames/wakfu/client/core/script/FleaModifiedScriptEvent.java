package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.script.*;

public class FleaModifiedScriptEvent extends FilterableEvent
{
    protected static final Logger m_logger;
    private final Integer m_itemReferenceId;
    private final Integer m_quantity;
    private final int m_price;
    private final FleaAction m_action;
    
    public FleaModifiedScriptEvent() {
        super();
        this.m_itemReferenceId = 0;
        this.m_quantity = 0;
        this.m_price = 0;
        this.m_action = FleaAction.Added;
    }
    
    public FleaModifiedScriptEvent(final FleaAction action, final int itemReferenceId, final int quantity, final int price) {
        super();
        this.m_action = action;
        this.m_itemReferenceId = itemReferenceId;
        this.m_quantity = quantity;
        this.m_price = price;
    }
    
    @Override
    public short getId() {
        return 4;
    }
    
    @Override
    public LuaTable getInfoForLUA() {
        final LuaTable table = new LuaTable("event");
        table.addField("action", this.m_action.getAction());
        table.addField("referenceId", this.m_itemReferenceId);
        table.addField("quantity", this.m_quantity);
        table.addField("price", this.m_price);
        return table;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object object) {
        return object instanceof FleaModifiedScriptEvent;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FleaModifiedScriptEvent.class);
    }
    
    public enum FleaAction
    {
        Added("added"), 
        Removed("removed"), 
        ChangePrice("changeprice");
        
        private final String m_action;
        
        private FleaAction(final String action) {
            this.m_action = action;
        }
        
        public String getAction() {
            return this.m_action;
        }
    }
}
