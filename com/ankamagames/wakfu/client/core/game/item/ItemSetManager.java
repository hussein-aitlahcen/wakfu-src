package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class ItemSetManager extends AbstractItemSetManager<ReferenceItem, ItemSet>
{
    private static final Logger m_logger;
    private static final ItemSetManager m_instance;
    private BinaryLoader<ItemSetBinaryData> m_loader;
    
    public static ItemSetManager getInstance() {
        return ItemSetManager.m_instance;
    }
    
    private ItemSetManager() {
        super();
        this.m_loader = new BinaryLoaderFromFile<ItemSetBinaryData>(new ItemSetBinaryData());
    }
    
    public void setLoader(@NotNull final BinaryLoader<ItemSetBinaryData> loader) {
        this.m_loader = loader;
    }
    
    @Nullable
    @Override
    public ItemSet getItemSet(final short itemSetId) {
        if (itemSetId <= 0) {
            return null;
        }
        ItemSet itemSet = (ItemSet)this.m_itemSetsById.get(itemSetId);
        if (itemSet == null && !this.m_itemSetsById.contains(itemSetId)) {
            final ItemSetBinaryData data = this.m_loader.createFromId(itemSetId);
            if (data == null) {
                return null;
            }
            itemSet = createItemSetFromBinaryForm(data);
            ((AbstractItemSetManager<ReferenceItem, ItemSet>)this).addItemSet(itemSet);
        }
        return itemSet;
    }
    
    private static ItemSet createItemSetFromBinaryForm(final ItemSetBinaryData bs) {
        final ArrayList<ReferenceItem> itemsSet = new ArrayList<ReferenceItem>();
        final int[] items = bs.getItemsId();
        for (int itemCount = (items == null) ? 0 : items.length, i = 0; i < itemCount; ++i) {
            final int itemId = items[i];
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(itemId);
            if (referenceItem == null) {
                ItemSetManager.m_logger.warn((Object)("item inconnu itemId=" + itemId + " pour la panop " + bs.getId()));
            }
            else {
                itemsSet.add(referenceItem);
            }
        }
        final ItemSet itemSet = new ItemSet(bs.getId(), bs.getLinkedItemReferenceId(), itemsSet);
        addEffects(bs, itemSet);
        itemSet.initializeItemSetCriterions();
        return itemSet;
    }
    
    public static void addEffects(final ItemSetBinaryData bs, final AbstractItemSet itemSet) {
        final TIntObjectHashMap<int[]> effectIdsByPartCount = bs.getEffectIdsByPartCount();
        if (effectIdsByPartCount != null && !effectIdsByPartCount.isEmpty()) {
            effectIdsByPartCount.forEachEntry(new TIntObjectProcedure<int[]>() {
                @Override
                public boolean execute(final int partCount, final int[] effectIds) {
                    for (final int effectId : effectIds) {
                        final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
                        if (effect != null) {
                            itemSet.addEffect(partCount, effect);
                        }
                        else {
                            ItemSetManager.m_logger.error((Object)("Probl\u00e8me lors de chargemetn de itemSEt " + itemSet.getId()));
                        }
                    }
                    return true;
                }
            });
        }
    }
    
    public TIntObjectHashMap<ItemSet> getFullList() {
        final TIntObjectHashMap<ItemSet> map = new TIntObjectHashMap<ItemSet>();
        try {
            BinaryDocumentManager.getInstance().foreach(new ItemSetBinaryData(), new LoadProcedure<ItemSetBinaryData>() {
                @Override
                public void load(final ItemSetBinaryData data) {
                    try {
                        final ItemSet itemSet = createItemSetFromBinaryForm(data);
                        map.put(itemSet.getId(), itemSet);
                    }
                    catch (Exception e) {
                        ItemSetManager.m_logger.error((Object)("Exception levee sur " + data.getId()), (Throwable)e);
                    }
                }
            });
        }
        catch (Exception e) {
            ItemSetManager.m_logger.error((Object)"", (Throwable)e);
        }
        return map;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemSetManager.class);
        m_instance = new ItemSetManager();
    }
}
