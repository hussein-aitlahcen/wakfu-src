package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class InventoryItemModifiedEvent extends InventoryEvent
{
    public static final int[] POUET_ITEMS;
    protected InventoryContent m_concernedItem;
    protected short m_position;
    protected short m_quantity;
    private static final ObjectPool m_staticPool;
    
    public static InventoryItemModifiedEvent checkOutAddEvent(final Inventory inventory, final InventoryContent itemAdded) {
        return checkOut(inventory, Action.ITEM_ADDED, itemAdded, (short)0);
    }
    
    public static InventoryItemModifiedEvent checkOutAddEvent(final Inventory inventory, final InventoryContent itemAdded, final short position) {
        return checkOut(inventory, Action.ITEM_ADDED_AT, itemAdded, position);
    }
    
    public static InventoryItemModifiedEvent checkOutQuantityEvent(final Inventory inventory, final InventoryContent itemModified, final short quantity) {
        return checkOut(inventory, Action.ITEM_QUANTITY_MODIFIED, itemModified, (short)0, quantity);
    }
    
    public static InventoryItemModifiedEvent checkOutQuantityEvent(final Inventory inventory, final InventoryContent itemModified, final short position, final short quantity) {
        return checkOut(inventory, Action.ITEM_QUANTITY_MODIFIED, itemModified, position, quantity);
    }
    
    public static InventoryItemModifiedEvent checkOutPriceEvent(final Inventory inventory, final InventoryContent itemModified, final short position) {
        return checkOut(inventory, Action.ITEM_PRICE_MODIFIED, itemModified, position);
    }
    
    public static InventoryItemModifiedEvent checkOutPackSizeEvent(final Inventory inventory, final InventoryContent itemModified, final short position) {
        return checkOut(inventory, Action.ITEM_PACK_SIZE_MODIFIED, itemModified, position);
    }
    
    public static InventoryItemModifiedEvent checkOutRemoveEvent(final Inventory inventory, final InventoryContent itemRemoved) {
        return checkOut(inventory, Action.ITEM_REMOVED, itemRemoved, (short)0);
    }
    
    public static InventoryItemModifiedEvent checkOutRemoveEvent(final Inventory inventory, final InventoryContent itemRemoved, final short position) {
        return checkOut(inventory, Action.ITEM_REMOVED_AT, itemRemoved, position);
    }
    
    static InventoryItemModifiedEvent checkOut(final Inventory inventory, final Action action, final InventoryContent itemModified, final short position) {
        return checkOut(inventory, action, itemModified, position, (short)(-1));
    }
    
    static InventoryItemModifiedEvent checkOut(final Inventory inventory, final Action action, final InventoryContent itemModified, final short position, final short quantity) {
        InventoryItemModifiedEvent event;
        try {
            event = (InventoryItemModifiedEvent)InventoryItemModifiedEvent.m_staticPool.borrowObject();
            event.m_pool = InventoryItemModifiedEvent.m_staticPool;
        }
        catch (Exception e) {
            InventoryItemModifiedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type InventoryItemModifiedEvent : " + e.getMessage()));
            event = new InventoryItemModifiedEvent();
        }
        event.init(inventory, action);
        event.m_concernedItem = itemModified;
        event.m_position = position;
        event.m_quantity = quantity;
        return event;
    }
    
    public InventoryContent getConcernedItem() {
        return this.m_concernedItem;
    }
    
    public short getPosition() {
        return this.m_position;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public String getLogRepresentation() {
        final InventoryContent item = this.getConcernedItem();
        if (!(item instanceof LoggableEntity)) {
            InventoryItemModifiedEvent.m_logger.error((Object)("Log de type " + this.getClass().getName() + " sur un InventoryItemModifiedEvent d'un item de type non-loggable : " + item.getClass().getName()));
            return null;
        }
        final String itemRepr = ((LoggableEntity)item).getLogRepresentation();
        switch (this.getAction()) {
            case ITEM_ADDED:
            case ITEM_ADDED_AT: {
                try {
                    if (ArrayUtils.contains(InventoryItemModifiedEvent.POUET_ITEMS, item.getReferenceId())) {
                        return "itemAcquired=" + itemRepr + " Stack : " + ExceptionFormatter.currentStackTrace();
                    }
                }
                catch (Throwable e) {
                    InventoryItemModifiedEvent.m_logger.error((Object)"Exception : ", e);
                }
                return "itemAcquired=" + itemRepr;
            }
            case ITEM_PRICE_MODIFIED: {
                return "itemPriceChange=" + itemRepr;
            }
            case ITEM_PACK_SIZE_MODIFIED: {
                return "itemPackSizeChange=" + itemRepr;
            }
            case ITEM_QUANTITY_MODIFIED: {
                try {
                    if (ArrayUtils.contains(InventoryItemModifiedEvent.POUET_ITEMS, item.getReferenceId())) {
                        return "itemQuantityModified=" + itemRepr + " quantity=" + this.getQuantity() + " Stack : " + ExceptionFormatter.currentStackTrace();
                    }
                }
                catch (Throwable e) {
                    InventoryItemModifiedEvent.m_logger.error((Object)"Exception : ", e);
                }
                return "itemQuantityModified=" + itemRepr + " quantity=" + this.getQuantity();
            }
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT: {
                return "itemLost=" + itemRepr;
            }
            default: {
                InventoryItemModifiedEvent.m_logger.error((Object)("Log de type " + this.getClass().getName() + " sur un InventoryItemModifiedEvent d'action " + this.getAction() + " inconnue"));
                return null;
            }
        }
    }
    
    static {
        POUET_ITEMS = new int[] { 14107, 18283, 18431, 18376, 18537 };
        m_staticPool = new MonitoredPool(new ObjectFactory<InventoryItemModifiedEvent>() {
            @Override
            public InventoryItemModifiedEvent makeObject() {
                return new InventoryItemModifiedEvent(null);
            }
        });
    }
}
