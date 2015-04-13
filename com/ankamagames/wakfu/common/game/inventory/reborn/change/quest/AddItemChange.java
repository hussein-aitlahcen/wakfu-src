package com.ankamagames.wakfu.common.game.inventory.reborn.change.quest;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

class AddItemChange implements QuestInventoryChange
{
    private static final Logger m_logger;
    private QuestItem m_item;
    
    AddItemChange() {
        super();
    }
    
    AddItemChange(final QuestItem item) {
        super();
        this.m_item = item;
    }
    
    @Override
    public byte[] serialize() {
        final RawQuestItemInventory.RawQuestItem raw = QuestInventorySerializer.itemToRaw(this.m_item);
        final ByteBuffer bb = ByteBuffer.allocate(raw.serializedSize());
        raw.serialize(bb);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        final RawQuestItemInventory.RawQuestItem raw = new RawQuestItemInventory.RawQuestItem();
        raw.unserialize(bb);
        try {
            this.m_item = QuestInventorySerializer.itemFromRaw(raw, ReferenceItemManager.getInstance());
        }
        catch (QuestInventoryException e) {
            AddItemChange.m_logger.error((Object)("Impossible de d\u00e9-s\u00e9rialiser l'item " + raw), (Throwable)e);
        }
    }
    
    @Override
    public void compute(final QuestInventoryController controller) {
        try {
            controller.addItem(this.m_item);
        }
        catch (QuestInventoryException e) {
            AddItemChange.m_logger.error((Object)"Impossible d'ajouter l'item", (Throwable)e);
        }
    }
    
    @Override
    public QuestInventoryChangeType getType() {
        return QuestInventoryChangeType.ADD_ITEM;
    }
    
    @Override
    public String toString() {
        return "AddItemChange{m_item=" + this.m_item + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddItemChange.class);
    }
}
