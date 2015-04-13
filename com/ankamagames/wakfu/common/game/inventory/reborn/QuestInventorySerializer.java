package com.ankamagames.wakfu.common.game.inventory.reborn;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;

public class QuestInventorySerializer
{
    private static final Logger m_logger;
    private final QuestInventory m_inventory;
    private final RawQuestItemInventory m_raw;
    private final BasicReferenceItemManager m_referenceItemManager;
    
    public QuestInventorySerializer(final QuestInventory inventory, final RawQuestItemInventory raw, final BasicReferenceItemManager referenceItemManager) {
        super();
        this.m_inventory = inventory;
        this.m_raw = raw;
        this.m_referenceItemManager = referenceItemManager;
    }
    
    void toRaw() {
        this.m_inventory.forEach(new ToRawProcedure(this.m_raw));
    }
    
    public static RawQuestItemInventory.RawQuestItem itemToRaw(final QuestItem item) {
        final RawQuestItemInventory.RawQuestItem raw = new RawQuestItemInventory.RawQuestItem();
        raw.refId = item.getRefId();
        raw.quantity = item.getQuantity();
        return raw;
    }
    
    public void fromRaw() {
        final QuestInventoryModel inventory = (QuestInventoryModel)this.m_inventory;
        inventory.clear();
        for (int i = 0, size = this.m_raw.items.size(); i < size; ++i) {
            final RawQuestItemInventory.RawQuestItem raw = this.m_raw.items.get(i);
            try {
                final QuestItem item = itemFromRaw(raw, this.m_referenceItemManager);
                inventory.add(item);
            }
            catch (QuestInventoryException e) {
                QuestInventorySerializer.m_logger.error((Object)("Probl\u00e8me \u00e0 la d\u00e9-serialisation de l'item " + raw), (Throwable)e);
            }
        }
    }
    
    public static QuestItem itemFromRaw(final RawQuestItemInventory.RawQuestItem raw, final BasicReferenceItemManager referenceItemManager) throws QuestInventoryException {
        final BasicReferenceItem refItem = referenceItemManager.getReferenceItem(raw.refId);
        if (refItem == null) {
            throw new QuestInventoryException("Impossible de trouver le refItem " + raw.refId);
        }
        final QuestItemModel item = new QuestItemModel(refItem);
        item.setQuantity(MathHelper.minShort(refItem.getStackMaximumHeight(), raw.quantity));
        return item;
    }
    
    @Override
    public String toString() {
        return "QuestInventorySerializer{m_inventory=" + this.m_inventory + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)QuestInventorySerializer.class);
    }
    
    private static class ToRawProcedure implements TObjectProcedure<QuestItem>
    {
        private final RawQuestItemInventory m_raw;
        
        ToRawProcedure(final RawQuestItemInventory raw) {
            super();
            this.m_raw = raw;
        }
        
        @Override
        public boolean execute(final QuestItem object) {
            final RawQuestItemInventory.RawQuestItem raw = QuestInventorySerializer.itemToRaw(object);
            this.m_raw.items.add(raw);
            return true;
        }
        
        @Override
        public String toString() {
            return "BuildRawProcedure{m_raw=" + this.m_raw + '}';
        }
    }
}
