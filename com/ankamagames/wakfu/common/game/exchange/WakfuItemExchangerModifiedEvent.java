package com.ankamagames.wakfu.common.game.exchange;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class WakfuItemExchangerModifiedEvent extends ItemExchangerModifiedEvent
{
    private Validity m_validity;
    protected int m_amountOfCash;
    private static final ObjectPool m_staticPool;
    
    public Validity getValidity() {
        return this.m_validity;
    }
    
    public int getAmountOfCash() {
        if (this.m_modification != Modification.CASH_MODIFIED) {
            throw new UnsupportedOperationException("Amount of cash can be retrieved for Cash Modification events only");
        }
        return this.m_amountOfCash;
    }
    
    public static WakfuItemExchangerModifiedEvent checkOut(final ItemExchanger itemExchanger, final Validity validity) {
        WakfuItemExchangerModifiedEvent event;
        try {
            event = (WakfuItemExchangerModifiedEvent)WakfuItemExchangerModifiedEvent.m_staticPool.borrowObject();
            event.m_pool = WakfuItemExchangerModifiedEvent.m_staticPool;
        }
        catch (Exception e) {
            WakfuItemExchangerModifiedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type WakfuItemExchangerModifiedEvent : " + e.getMessage()));
            event = new WakfuItemExchangerModifiedEvent();
        }
        event.init(itemExchanger, validity);
        return event;
    }
    
    public static WakfuItemExchangerModifiedEvent checkOut(final ItemExchanger itemExchanger, final Modification modification, final long userId, final InventoryContent content, final short contentQuantity, final Validity validity) {
        WakfuItemExchangerModifiedEvent event;
        try {
            event = (WakfuItemExchangerModifiedEvent)WakfuItemExchangerModifiedEvent.m_staticPool.borrowObject();
            event.m_pool = WakfuItemExchangerModifiedEvent.m_staticPool;
        }
        catch (Exception e) {
            WakfuItemExchangerModifiedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type ItemExchangerEndEvent : " + e.getMessage()));
            event = new WakfuItemExchangerModifiedEvent();
        }
        event.init(itemExchanger, modification, userId, content, contentQuantity, validity);
        return event;
    }
    
    public static WakfuItemExchangerModifiedEvent checkOut(final ItemExchanger itemExchanger, final Modification modification, final long userId, final int amountOfCash, final Validity validity) {
        if (modification != Modification.CASH_MODIFIED) {
            throw new UnsupportedOperationException("checkout with this parameters should only be use for a cash modification");
        }
        WakfuItemExchangerModifiedEvent event;
        try {
            event = (WakfuItemExchangerModifiedEvent)WakfuItemExchangerModifiedEvent.m_staticPool.borrowObject();
            event.m_pool = WakfuItemExchangerModifiedEvent.m_staticPool;
        }
        catch (Exception e) {
            WakfuItemExchangerModifiedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type ItemExchangerEndEvent : " + e.getMessage()));
            event = new WakfuItemExchangerModifiedEvent();
        }
        event.init(itemExchanger, modification, userId, amountOfCash, validity);
        return event;
    }
    
    public static WakfuItemExchangerModifiedEvent checkOut(final ItemExchanger itemExchanger, final Modification modification, final long userId, final int modifiedAmount) {
        if (modification != Modification.CASH_MODIFIED) {
            throw new UnsupportedOperationException("checkout with this parameters should only be use for a cash modification");
        }
        WakfuItemExchangerModifiedEvent event;
        try {
            event = (WakfuItemExchangerModifiedEvent)WakfuItemExchangerModifiedEvent.m_staticPool.borrowObject();
            event.m_pool = WakfuItemExchangerModifiedEvent.m_staticPool;
        }
        catch (Exception e) {
            WakfuItemExchangerModifiedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type ItemExchangerEndEvent : " + e.getMessage()));
            event = new WakfuItemExchangerModifiedEvent();
        }
        event.init(itemExchanger, modification, userId, modifiedAmount, null);
        return event;
    }
    
    private void init(final ItemExchanger itemExchanger, final Modification modification, final long userId, final InventoryContent content, final short contentQuantity, final Validity validity) {
        this.init(itemExchanger, Action.EXCHANGE_CONTENT_MODIFIED);
        this.m_modification = modification;
        this.m_userId = userId;
        this.m_content = content;
        this.m_contentQuantity = contentQuantity;
        this.m_validity = validity;
    }
    
    private void init(final ItemExchanger itemExchanger, final Modification modification, final long userId, final int amountOfCash, final Validity validity) {
        this.init(itemExchanger, Action.EXCHANGE_CONTENT_MODIFIED);
        this.m_modification = modification;
        this.m_userId = userId;
        this.m_amountOfCash = amountOfCash;
        this.m_validity = validity;
    }
    
    protected WakfuItemExchangerModifiedEvent() {
        super();
        this.m_action = null;
        this.m_itemExchanger = null;
        this.m_validity = null;
    }
    
    protected void init(final ItemExchanger itemExchanger, final Validity validity) {
        this.init(itemExchanger, Action.EXCHANGE_CONTENT_MODIFIED);
        this.m_validity = validity;
    }
    
    @Override
    public void release() throws Exception {
        if (this.m_pool != null) {
            this.m_pool.returnObject(this);
            this.m_pool = null;
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_validity = null;
        this.m_amountOfCash = -1;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_validity = null;
        this.m_amountOfCash = -1;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<WakfuItemExchangerModifiedEvent>() {
            @Override
            public WakfuItemExchangerModifiedEvent makeObject() {
                return new WakfuItemExchangerModifiedEvent();
            }
        });
    }
    
    public enum Validity
    {
        VALID, 
        INVENTORY_0_FULL, 
        INVENTORY_1_FULL, 
        USER1_ITEM_DOES_NOT_EXIST, 
        USER0_ITEM_DOES_NOT_EXIST;
    }
}
