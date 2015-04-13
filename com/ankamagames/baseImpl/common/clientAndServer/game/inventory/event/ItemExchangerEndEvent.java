package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ItemExchangerEndEvent extends ItemExchangerEvent implements Poolable
{
    protected static final Logger LOGGER;
    private Reason m_reason;
    private static final ObjectPool m_staticPool;
    
    public static ItemExchangerEndEvent checkOut(final ItemExchanger itemExchanger, final Reason reason) {
        ItemExchangerEndEvent event;
        try {
            event = (ItemExchangerEndEvent)ItemExchangerEndEvent.m_staticPool.borrowObject();
            event.m_pool = ItemExchangerEndEvent.m_staticPool;
        }
        catch (Exception e) {
            ItemExchangerEndEvent.LOGGER.error((Object)("Erreur lors d'un checkOut sur un message de type ItemExchangerEndEvent : " + e.getMessage()));
            event = new ItemExchangerEndEvent();
        }
        event.init(itemExchanger, reason);
        return event;
    }
    
    protected ItemExchangerEndEvent() {
        super();
        this.m_action = null;
        this.m_itemExchanger = null;
    }
    
    protected void init(final ItemExchanger itemExchanger, final Reason reason) {
        this.init(itemExchanger, Action.EXCHANGE_END);
        this.m_reason = reason;
    }
    
    @Override
    public void release() throws Exception {
        if (this.m_pool != null) {
            this.m_pool.returnObject(this);
            this.m_pool = null;
        }
        else {
            ItemExchangerEndEvent.LOGGER.error((Object)("Double release de " + this.getClass().toString()));
            this.onCheckIn();
        }
    }
    
    public Reason getReason() {
        return this.m_reason;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_reason = null;
    }
    
    static {
        LOGGER = Logger.getLogger((Class)ItemExchangerEndEvent.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<ItemExchangerEndEvent>() {
            @Override
            public ItemExchangerEndEvent makeObject() {
                return new ItemExchangerEndEvent();
            }
        });
    }
    
    public enum Reason
    {
        INVITATION_IMPOSSIBLE_USER_BUSY, 
        INVITATION_LOCALLY_CANCELED, 
        INVITATION_REMOTELY_CANCELED, 
        REMOTELY_CANCELED, 
        LOCALLY_CANCELED, 
        EXCHANGE_DONE, 
        USER_IGNORED, 
        EXCHANGE_FAILED;
    }
}
