package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.core.game.item.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class DimensionalBagFlea extends AbstractMerchantInventoryCollection<MerchantInventoryItem, MerchantInventory>
{
    private float m_baseBuyDuty;
    private float m_nationBuyDuty;
    
    public float getNationBuyDuty() {
        return this.m_nationBuyDuty;
    }
    
    public void setNationBuyDuty(final float nationBuyDuty) {
        this.m_nationBuyDuty = nationBuyDuty;
    }
    
    public float getBaseBuyDuty() {
        return this.m_baseBuyDuty;
    }
    
    public void setBaseBuyDuty(final float baseBuyDuty) {
        this.m_baseBuyDuty = baseBuyDuty;
    }
    
    public ArrayList<MerchantInventoryItem> getAllItems(final boolean showNull) {
        final ArrayList<MerchantInventoryItem> result = new ArrayList<MerchantInventoryItem>(showNull ? this.getMaximumSize() : this.getItemsCount());
        ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)this).forEachMerchantInventory(new TObjectProcedure<MerchantInventory>() {
            @Override
            public boolean execute(final MerchantInventory inventory) {
                final Iterator<AbstractMerchantInventoryItem> it = ((ArrayInventory<AbstractMerchantInventoryItem, R>)inventory).iterator(showNull);
                while (it.hasNext()) {
                    result.add(it.next());
                }
                return true;
            }
        });
        return result;
    }
    
    public MerchantInventory fromRawInventory(final RawMerchantItemInventory inventory) {
        final MerchantItemType requiredItemType = (inventory.requiredItemType >= 0) ? MerchantItemType.values()[inventory.requiredItemType & 0xFF] : null;
        final MerchantInventory merchantInventory = new MerchantInventory(inventory.uid, requiredItemType, inventory.nSlots, inventory.maxPackSize);
        if (merchantInventory.fromRaw(inventory)) {
            ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)this).addMerchantInventory(merchantInventory);
            return merchantInventory;
        }
        DimensionalBagFlea.m_logger.error((Object)"Erreur durant la d\u00e9s\u00e9rialisation du DimensionalBagFlea");
        return null;
    }
    
    public void releaseAllItems() {
        final ArrayList<MerchantInventoryItem> allItems = this.getAllItems(false);
        for (int i = 0; i < allItems.size(); ++i) {
            allItems.get(i).release();
        }
    }
    
    public void removeMerchantInventory(final int inventoryItemRefId) {
    }
}
