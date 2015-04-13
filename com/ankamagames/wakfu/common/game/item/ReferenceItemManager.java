package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;

public class ReferenceItemManager<R extends AbstractReferenceItem> implements InventoryContentProvider<Item, RawInventoryItem>, ItemProvider, RawInventoryItemProvider, BasicReferenceItemManager
{
    protected static final Logger m_logger;
    protected final TIntObjectHashMap<R> m_referenceItems;
    protected final TIntObjectHashMap<Item> m_defaultItems;
    private static ReferenceItemManager m_uniqueInstance;
    
    public static ReferenceItemManager getInstance() {
        return ReferenceItemManager.m_uniqueInstance;
    }
    
    public static void setUniqueInstance(final ReferenceItemManager uniqueInstance) {
        ReferenceItemManager.m_uniqueInstance = uniqueInstance;
    }
    
    protected ReferenceItemManager() {
        super();
        this.m_referenceItems = new TIntObjectHashMap<R>();
        this.m_defaultItems = new TIntObjectHashMap<Item>();
    }
    
    public void addReferenceItem(final R item) {
        this.m_referenceItems.put(item.getId(), item);
    }
    
    @Override
    public R getReferenceItem(final int referenceItemId) {
        return this.m_referenceItems.get(referenceItemId);
    }
    
    public Item getDefaultItem(final int referenceId) {
        Item item = this.m_defaultItems.get(referenceId);
        if (item == null) {
            final R referenceItem = this.getReferenceItem(referenceId);
            if (referenceItem == null) {
                return null;
            }
            item = Item.newInstance(referenceItem);
            this.m_defaultItems.put(referenceId, item);
        }
        return item;
    }
    
    @Override
    public Item unSerializeContent(final RawInventoryItem rawItem) {
        final Item item = this.createItem();
        if (item.fromRaw(rawItem)) {
            return item;
        }
        return null;
    }
    
    @Override
    public Item createItem() {
        return new Item();
    }
    
    @Override
    public RawInventoryItem createRawInventoryItem() {
        return new RawInventoryItem();
    }
    
    public TIntObjectIterator<R> iterator() {
        return this.m_referenceItems.iterator();
    }
    
    public List<AbstractReferenceItem> asList() {
        return (List<AbstractReferenceItem>)Arrays.asList((AbstractReferenceItem[])this.m_referenceItems.getValues((T[])new AbstractReferenceItem[this.m_referenceItems.size()]));
    }
    
    public ItemRarity getItemRarity(final int refItemId) {
        final R referenceItem = this.m_referenceItems.get(refItemId);
        if (referenceItem == null) {
            return null;
        }
        return referenceItem.getRarity();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceItemManager.class);
        ReferenceItemManager.m_uniqueInstance = new ReferenceItemManager();
    }
}
