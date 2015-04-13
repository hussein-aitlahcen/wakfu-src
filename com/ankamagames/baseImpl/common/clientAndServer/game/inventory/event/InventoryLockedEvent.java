package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class InventoryLockedEvent extends InventoryEvent
{
    private static final ObjectPool m_staticPool;
    
    public static InventoryLockedEvent checkOut(final Inventory inventory) {
        InventoryLockedEvent event;
        try {
            event = (InventoryLockedEvent)InventoryLockedEvent.m_staticPool.borrowObject();
            event.m_pool = InventoryLockedEvent.m_staticPool;
        }
        catch (Exception e) {
            InventoryLockedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type InventoryClearedEvent : " + e.getMessage()));
            event = new InventoryLockedEvent();
        }
        event.init(inventory, Action.LOCKED);
        return event;
    }
    
    @Override
    public String getLogRepresentation() {
        final Inventory inventory = this.getInventory();
        if (!(inventory instanceof LoggableEntity)) {
            InventoryLockedEvent.m_logger.error((Object)("Log de type  " + this.getClass().getName() + " sur un inventaire non-loggable de type " + inventory.getClass().getName()));
            return null;
        }
        final String inventoryRepr = ((LoggableEntity)inventory).getLogRepresentation();
        return "LockedInventory=" + inventory.isLocked() + " : " + inventoryRepr;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<InventoryLockedEvent>() {
            @Override
            public InventoryLockedEvent makeObject() {
                return new InventoryLockedEvent(null);
            }
        });
    }
}
