package com.ankamagames.wakfu.common.game.inventory.reborn.change.quest;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;

class ItemQuantityChange implements QuestInventoryChange
{
    private static final Logger m_logger;
    private int m_itemId;
    private short m_quantity;
    
    ItemQuantityChange() {
        super();
    }
    
    ItemQuantityChange(final int itemId, final short quantity) {
        super();
        this.m_itemId = itemId;
        this.m_quantity = quantity;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(6);
        bb.putInt(this.m_itemId);
        bb.putShort(this.m_quantity);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_itemId = bb.getInt();
        this.m_quantity = bb.getShort();
    }
    
    @Override
    public void compute(final QuestInventoryController controller) {
        try {
            controller.setItemQuantity(this.m_itemId, this.m_quantity);
        }
        catch (QuestInventoryException e) {
            ItemQuantityChange.m_logger.error((Object)"Impossible d'ajouter l'item", (Throwable)e);
        }
    }
    
    @Override
    public QuestInventoryChangeType getType() {
        return QuestInventoryChangeType.ITEM_QUANTITY;
    }
    
    @Override
    public String toString() {
        return "ItemQuantityChange{m_itemId=" + this.m_itemId + ", m_quantity=" + this.m_quantity + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemQuantityChange.class);
    }
}
