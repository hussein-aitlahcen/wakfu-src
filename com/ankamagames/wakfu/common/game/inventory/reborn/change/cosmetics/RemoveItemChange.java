package com.ankamagames.wakfu.common.game.inventory.reborn.change.cosmetics;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;

class RemoveItemChange implements CosmeticsInventoryChange
{
    private static final Logger m_logger;
    private int m_itemId;
    
    RemoveItemChange() {
        super();
    }
    
    RemoveItemChange(final int itemId) {
        super();
        this.m_itemId = itemId;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_itemId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_itemId = bb.getInt();
    }
    
    @Override
    public void compute(final CosmeticsInventoryController controller) {
        try {
            controller.removeItem(this.m_itemId);
        }
        catch (CosmeticsInventoryException e) {
            RemoveItemChange.m_logger.error((Object)"Impossible d'ajouter l'item", (Throwable)e);
        }
    }
    
    @Override
    public CosmeticsInventoryChangeType getType() {
        return CosmeticsInventoryChangeType.REMOVE_ITEM;
    }
    
    @Override
    public String toString() {
        return "RemoveItemChange{m_itemId=" + this.m_itemId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveItemChange.class);
    }
}
