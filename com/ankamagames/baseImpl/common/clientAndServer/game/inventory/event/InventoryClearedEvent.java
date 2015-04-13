package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class InventoryClearedEvent extends InventoryEvent
{
    private static final ObjectPool m_staticPool;
    
    public static InventoryClearedEvent checkOut(final Inventory inventory) {
        InventoryClearedEvent event;
        try {
            event = (InventoryClearedEvent)InventoryClearedEvent.m_staticPool.borrowObject();
            event.m_pool = InventoryClearedEvent.m_staticPool;
        }
        catch (Exception e) {
            InventoryClearedEvent.m_logger.error((Object)("Erreur lors d'un checkOut sur un message de type InventoryClearedEvent : " + e.getMessage()));
            event = new InventoryClearedEvent();
        }
        event.init(inventory, Action.CLEARED);
        return event;
    }
    
    @Override
    public String getLogRepresentation() {
        final Inventory inventory = this.getInventory();
        if (!(inventory instanceof LoggableEntity)) {
            InventoryClearedEvent.m_logger.error((Object)("Log de type  " + this.getClass().getName() + " sur un inventaire non-loggable de type " + inventory.getClass().getName()));
            return null;
        }
        final String inventoryRepr = ((LoggableEntity)inventory).getLogRepresentation();
        return "clearedInventory=" + inventoryRepr;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<InventoryClearedEvent>() {
            @Override
            public InventoryClearedEvent makeObject() {
                return new InventoryClearedEvent(null);
            }
        });
    }
}
