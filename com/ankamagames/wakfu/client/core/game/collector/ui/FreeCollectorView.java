package com.ankamagames.wakfu.client.core.game.collector.ui;

import com.ankamagames.wakfu.client.core.game.collector.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.free.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class FreeCollectorView extends AbstractCollectorView
{
    public FreeCollectorView(final CollectorOccupationProvider Collector) {
        super(Collector);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("content")) {
            final ArrayList<Item> items = new ArrayList<Item>();
            final Iterator<Item> it = ((ClientCollectorInventoryFree)this.getCollector().getInventory()).getContent().iterator();
            while (it.hasNext()) {
                items.add(it.next());
            }
            return items;
        }
        if (fieldName.equals("kama")) {
            return this.getKamaQuantity();
        }
        if (fieldName.equals("freeBagSlotsSize")) {
            return this.getFreeBagSlotsSize();
        }
        return super.getFieldValue(fieldName);
    }
    
    private int getFreeBagSlotsSize() {
        final Inventory<Item> content = ((ClientCollectorInventoryFree)this.getCollector().getInventory()).getContent();
        return content.getMaximumSize();
    }
    
    @Override
    protected boolean isValid() {
        return false;
    }
    
    @Override
    public void validForm() {
    }
    
    @Override
    public int getKamaQuantity() {
        return ((ClientCollectorInventoryFree)this.getCollector().getInventory()).getAmountOfCash();
    }
}
