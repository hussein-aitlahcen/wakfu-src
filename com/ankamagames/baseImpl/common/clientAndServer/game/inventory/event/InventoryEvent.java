package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import org.apache.commons.pool.*;

public abstract class InventoryEvent implements Poolable, LoggableEntity
{
    protected static final Logger m_logger;
    protected Action m_action;
    protected Inventory m_inventory;
    protected ObjectPool m_pool;
    
    protected InventoryEvent() {
        super();
        this.m_action = null;
        this.m_inventory = null;
    }
    
    protected void init(final Inventory inventory, final Action action) {
        this.m_inventory = inventory;
        this.m_action = action;
    }
    
    public void release() throws Exception {
        if (this.m_pool != null) {
            this.m_pool.returnObject(this);
            this.m_pool = null;
        }
        else {
            InventoryEvent.m_logger.error((Object)("Double release de " + this.getClass().toString()));
            this.onCheckIn();
        }
    }
    
    public Action getAction() {
        return this.m_action;
    }
    
    public Inventory getInventory() {
        return this.m_inventory;
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_inventory = null;
        this.m_action = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InventoryEvent.class);
    }
    
    public enum Action
    {
        ITEM_ADDED, 
        ITEM_ADDED_AT, 
        ITEM_REMOVED, 
        ITEM_REMOVED_AT, 
        ITEM_QUANTITY_MODIFIED, 
        ITEM_PRICE_MODIFIED, 
        ITEM_PACK_SIZE_MODIFIED, 
        CLEARED, 
        LOCKED;
    }
}
