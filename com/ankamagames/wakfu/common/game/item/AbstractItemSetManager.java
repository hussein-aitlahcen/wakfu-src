package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class AbstractItemSetManager<ReferenceItem extends AbstractReferenceItem, ItemSet extends AbstractItemSet<ReferenceItem>>
{
    protected static final Logger m_logger;
    protected final TShortObjectHashMap<ItemSet> m_itemSetsById;
    
    public AbstractItemSetManager() {
        super();
        this.m_itemSetsById = new TShortObjectHashMap<ItemSet>();
    }
    
    public void addItemSet(final ItemSet itemSet) {
        this.m_itemSetsById.put(itemSet.getId(), itemSet);
    }
    
    public ItemSet getItemSet(final short itemSetId) {
        return this.m_itemSetsById.get(itemSetId);
    }
    
    public ItemSet getItemSet(final ReferenceItem referenceItem) {
        return this.getItemSet(referenceItem.getSetId());
    }
    
    public short[] getAllowedBreedSets(final short breedId, final byte sex) {
        return PrimitiveArrays.EMPTY_SHORT_ARRAY;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractItemSetManager.class);
    }
}
