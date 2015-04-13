package com.ankamagames.wakfu.common.game.inventory.reborn;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;

public class CosmeticsInventorySerializer
{
    private static final Logger m_logger;
    private final CosmeticsInventory m_inventory;
    private final RawCosmeticsItemInventory m_raw;
    private final BasicReferenceItemManager m_referenceItemManager;
    
    public CosmeticsInventorySerializer(final CosmeticsInventory inventory, final RawCosmeticsItemInventory raw, final BasicReferenceItemManager referenceItemManager) {
        super();
        this.m_inventory = inventory;
        this.m_raw = raw;
        this.m_referenceItemManager = referenceItemManager;
    }
    
    void toRaw() {
        this.m_inventory.forEach(new ToRawProcedure(this.m_raw));
    }
    
    public static RawCosmeticsItemInventory.RawCosmeticsItem itemToRaw(final CosmeticsItem item) {
        final RawCosmeticsItemInventory.RawCosmeticsItem raw = new RawCosmeticsItemInventory.RawCosmeticsItem();
        raw.refId = item.getRefId();
        return raw;
    }
    
    public void fromRaw() {
        final CosmeticsInventoryModel inventory = (CosmeticsInventoryModel)this.m_inventory;
        inventory.clear();
        for (int i = 0, size = this.m_raw.items.size(); i < size; ++i) {
            final RawCosmeticsItemInventory.RawCosmeticsItem raw = this.m_raw.items.get(i);
            try {
                final CosmeticsItem item = itemFromRaw(raw, this.m_referenceItemManager);
                inventory.add(item);
            }
            catch (CosmeticsInventoryException e) {
                CosmeticsInventorySerializer.m_logger.error((Object)("Probl\u00e8me \u00e0 la d\u00e9-serialisation de l'item " + raw), (Throwable)e);
            }
        }
    }
    
    public static CosmeticsItem itemFromRaw(final RawCosmeticsItemInventory.RawCosmeticsItem raw, final BasicReferenceItemManager referenceItemManager) throws CosmeticsInventoryException {
        final BasicReferenceItem refItem = referenceItemManager.getReferenceItem(raw.refId);
        if (refItem == null) {
            throw new CosmeticsInventoryException("Impossible de trouver le refItem " + raw.refId);
        }
        final CosmeticsItemModel item = new CosmeticsItemModel(refItem.getId());
        return item;
    }
    
    @Override
    public String toString() {
        return "QuestInventorySerializer{m_inventory=" + this.m_inventory + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)CosmeticsInventorySerializer.class);
    }
    
    private static class ToRawProcedure implements TObjectProcedure<CosmeticsItem>
    {
        private final RawCosmeticsItemInventory m_raw;
        
        ToRawProcedure(final RawCosmeticsItemInventory raw) {
            super();
            this.m_raw = raw;
        }
        
        @Override
        public boolean execute(final CosmeticsItem object) {
            final RawCosmeticsItemInventory.RawCosmeticsItem raw = CosmeticsInventorySerializer.itemToRaw(object);
            this.m_raw.items.add(raw);
            return true;
        }
        
        @Override
        public String toString() {
            return "BuildRawProcedure{m_raw=" + this.m_raw + '}';
        }
    }
}
