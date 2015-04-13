package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ItemExchangerModifiedEvent extends ItemExchangerEvent implements Poolable
{
    protected static final Logger m_logger;
    protected Modification m_modification;
    protected InventoryContent m_content;
    protected short m_contentQuantity;
    private static final ObjectPool m_staticPool;
    
    public static ItemExchangerModifiedEvent checkOut(final ItemExchanger itemExchanger, final Modification modification, final long userId, final InventoryContent content, final short contentQuantity) {
        ItemExchangerModifiedEvent event;
        try {
            event = (ItemExchangerModifiedEvent)ItemExchangerModifiedEvent.m_staticPool.borrowObject();
            event.m_pool = ItemExchangerModifiedEvent.m_staticPool;
        }
        catch (Exception e) {
            ItemExchangerModifiedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type ItemExchangerEndEvent : " + e.getMessage()));
            event = new ItemExchangerModifiedEvent();
        }
        event.init(itemExchanger, modification, userId, content, contentQuantity);
        return event;
    }
    
    private void init(final ItemExchanger itemExchanger, final Modification modification, final long userId, final InventoryContent content, final short contentQuantity) {
        this.init(itemExchanger, Action.EXCHANGE_CONTENT_MODIFIED);
        this.m_modification = modification;
        this.m_userId = userId;
        this.m_content = content;
        this.m_contentQuantity = contentQuantity;
    }
    
    public Modification getModification() {
        return this.m_modification;
    }
    
    public InventoryContent getContent() {
        return this.m_content;
    }
    
    public short getContentQuantity() {
        return this.m_contentQuantity;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemExchangerModifiedEvent.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<ItemExchangerModifiedEvent>() {
            @Override
            public ItemExchangerModifiedEvent makeObject() {
                return new ItemExchangerModifiedEvent();
            }
        });
    }
    
    public enum Modification
    {
        CONTENT_ADDED, 
        CONTENT_REMOVED, 
        CASH_MODIFIED;
    }
}
