package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ItemExchangerEvent implements Poolable
{
    protected static final Logger m_logger;
    protected Action m_action;
    protected ItemExchanger m_itemExchanger;
    protected long m_userId;
    protected ObjectPool m_pool;
    private static final ObjectPool m_staticPool;
    
    public static ItemExchangerEvent checkOut(final ItemExchanger itemExchanger, final Action action) {
        ItemExchangerEvent event;
        try {
            event = (ItemExchangerEvent)ItemExchangerEvent.m_staticPool.borrowObject();
            event.m_pool = ItemExchangerEvent.m_staticPool;
        }
        catch (Exception e) {
            ItemExchangerEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type ItemExchangerEvent : " + e.getMessage()));
            event = new ItemExchangerEvent();
        }
        event.init(itemExchanger, action);
        return event;
    }
    
    protected ItemExchangerEvent() {
        super();
        this.m_userId = -1L;
        this.m_action = null;
        this.m_itemExchanger = null;
    }
    
    protected void init(final ItemExchanger itemExchanger, final Action action) {
        this.m_itemExchanger = itemExchanger;
        this.m_action = action;
    }
    
    public void release() throws Exception {
        if (this.m_pool != null) {
            this.m_pool.returnObject(this);
            this.m_pool = null;
        }
        else {
            ItemExchangerEvent.m_logger.error((Object)("Double release de " + this.getClass().toString()));
            this.onCheckIn();
        }
    }
    
    public Action getAction() {
        return this.m_action;
    }
    
    public ItemExchanger getItemExchanger() {
        return this.m_itemExchanger;
    }
    
    public void setUserId(final long userId) {
        this.m_userId = userId;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_itemExchanger = null;
        this.m_action = null;
        this.m_userId = -1L;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemExchangerEvent.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<ItemExchangerEvent>() {
            @Override
            public ItemExchangerEvent makeObject() {
                return new ItemExchangerEvent();
            }
        });
    }
    
    public enum Action
    {
        EXCHANGE_REQUESTED, 
        EXCHANGE_PROPOSED, 
        EXCHANGE_STARTED, 
        EXCHANGE_CONTENT_MODIFIED, 
        EXCHANGE_END;
    }
}
