package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.item.data.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import org.apache.log4j.*;

public final class ItemManagerImpl extends ReferenceItemManager<ReferenceItem>
{
    private final TIntHashSet m_craftableItems;
    private final BinaryLoaderFromFile<ItemBinaryData> m_loader;
    private final ReferenceItemUniqueInstanceLoader m_binaryTransformer;
    
    public ItemManagerImpl() {
        super();
        this.m_craftableItems = new TIntHashSet();
        this.m_loader = new BinaryLoaderFromFile<ItemBinaryData>(new ItemBinaryData());
        this.m_binaryTransformer = new ReferenceItemUniqueInstanceLoader();
    }
    
    @Nullable
    @Override
    public ReferenceItem getReferenceItem(final int referenceItemId) {
        if (referenceItemId <= 0) {
            return null;
        }
        if (this.m_referenceItems.containsKey(referenceItemId)) {
            return (ReferenceItem)this.m_referenceItems.get(referenceItemId);
        }
        final ItemBinaryData data = this.m_loader.createFromId(referenceItemId);
        if (data == null) {
            return null;
        }
        final AbstractReferenceItem referenceItem = this.m_binaryTransformer.loadFromBinaryForm((BinaryData)data);
        if (!(referenceItem instanceof ReferenceItem)) {
            return null;
        }
        final ReferenceItem item = (ReferenceItem)referenceItem;
        this.addReferenceItem(item);
        return item;
    }
    
    public TIntObjectHashMap<AbstractReferenceItem> getFullList() {
        final TIntObjectHashMap<AbstractReferenceItem> map = new TIntObjectHashMap<AbstractReferenceItem>();
        try {
            BinaryDocumentManager.getInstance().foreach(new ItemBinaryData(), new LoadProcedure<ItemBinaryData>() {
                @Override
                public void load(final ItemBinaryData data) {
                    try {
                        final AbstractReferenceItem referenceItem = ItemManagerImpl.this.m_binaryTransformer.loadFromBinaryForm((BinaryData)data);
                        map.put(referenceItem.getId(), referenceItem);
                    }
                    catch (Exception e) {
                        ItemManagerImpl.m_logger.error((Object)("Exception levee sur " + data.getId()), (Throwable)e);
                    }
                }
            });
        }
        catch (Exception e) {
            ItemManagerImpl.m_logger.error((Object)"", (Throwable)e);
        }
        return map;
    }
    
    public boolean isCraftableItem(final int refId) {
        return this.m_craftableItems.contains(refId);
    }
    
    public void addItemCraftedId(final int itemId) {
        this.m_craftableItems.add(itemId);
    }
}
