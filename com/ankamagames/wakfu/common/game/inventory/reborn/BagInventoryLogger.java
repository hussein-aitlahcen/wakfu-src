package com.ankamagames.wakfu.common.game.inventory.reborn;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag.*;
import com.ankamagames.wakfu.common.game.item.*;

final class BagInventoryLogger implements BagInventoryListener
{
    private static final Logger m_logger;
    
    @Override
    public void bagAdded(final Bag bag) {
        BagInventoryLogger.m_logger.info((Object)("[BAG INVENTORY] Ajout du sac " + bag));
    }
    
    @Override
    public void bagRemoved(final Bag bag) {
        BagInventoryLogger.m_logger.info((Object)("[BAG INVENTORY] Retrait du sac " + bag));
    }
    
    @Override
    public void bagItemAdded(final Item item) {
        BagInventoryLogger.m_logger.info((Object)("[BAG INVENTORY] Ajout de l'item " + item));
    }
    
    @Override
    public void bagItemRemoved(final Item item) {
        BagInventoryLogger.m_logger.info((Object)("[BAG INVENTORY] Retrait de l'item " + item));
    }
    
    static {
        m_logger = Logger.getLogger((Class)BagInventoryLogger.class);
    }
}
