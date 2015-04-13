package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public abstract class FullGemsDefinitionManager<R extends AbstractReferenceItem>
{
    private static final Logger m_logger;
    protected TByteObjectHashMap<LootList> m_metaGemsLists;
    
    public FullGemsDefinitionManager() {
        super();
        this.m_metaGemsLists = new TByteObjectHashMap<LootList>();
    }
    
    public void setMetaGemsList(final LootList list, final byte type) {
        this.m_metaGemsLists.put(type, list);
    }
    
    public R getRandomGem(final ItemRarity rarity, final GemType type) {
        return this.getRandomGem(rarity, type, (byte)0);
    }
    
    public R getRandomGem(final ItemRarity rarity, final GemType type, final byte forcedPerfection) {
        final LootList list = this.m_metaGemsLists.get(type.getId());
        if (list == null) {
            throw new IllegalStateException("Pas de table de loot de meta gemmes pour le type " + type + ", le GemsDefinitionManager n'a pas \u00e9t\u00e9 initialis\u00e9.");
        }
        final Loot metaGemLoot = this.roll(list);
        if (metaGemLoot == null) {
            FullGemsDefinitionManager.m_logger.warn((Object)"[GemsDefinitionManager] La liste de m\u00e9ta-gemmes est vide !");
            return null;
        }
        byte perfectionIndex;
        if (forcedPerfection != 0) {
            perfectionIndex = forcedPerfection;
        }
        else {
            perfectionIndex = GemRarityHelper.rollIndex(rarity);
        }
        return this.createGem(metaGemLoot.getReferenceId(), perfectionIndex);
    }
    
    public R rerollRandomGem(final R baseGem) {
        final GemType gemType = this.getGemType(baseGem);
        final LootList lootList = this.m_metaGemsLists.get(gemType.getId());
        if (lootList == null) {
            return null;
        }
        if (lootList.size() == 1) {
            return baseGem;
        }
        Loot roll;
        do {
            roll = this.roll(lootList);
        } while (roll.getReferenceId() == baseGem.getMetaId());
        final byte perfectionIndex = this.getPerfectionIndex(baseGem);
        return this.createGem(roll.getReferenceId(), perfectionIndex);
    }
    
    public R increaseGemPerfection(final R baseGem) {
        final byte perfectionIndex = (byte)(this.getPerfectionIndex(baseGem) + 1);
        if (perfectionIndex >= 10) {
            return baseGem;
        }
        return this.createGem(baseGem.getMetaId(), perfectionIndex);
    }
    
    public R changeGem(final R baseGem, final IMetaItem item) {
        final byte perfectionIndex = this.getPerfectionIndex(baseGem);
        return this.createGem(item.getId(), perfectionIndex);
    }
    
    public GemType getGemType(final R gem) {
        if (gem.getMetaType() != ItemMetaType.SUB_META_ITEM) {
            return GemType.NONE;
        }
        final IMetaItem metaItem = MetaItemManager.INSTANCE.get(gem.getMetaId());
        if (metaItem == null) {
            return GemType.NONE;
        }
        final TByteObjectIterator<LootList> it = this.m_metaGemsLists.iterator();
        while (it.hasNext()) {
            it.advance();
            final GemType gemType = GemType.getById(it.key());
            final LootList lootList = it.value();
            for (int i = 0, size = lootList.size(); i < size; ++i) {
                final Loot loot = lootList.get(i);
                if (loot.getReferenceId() == metaItem.getId()) {
                    return gemType;
                }
            }
        }
        return GemType.NONE;
    }
    
    public byte getPerfectionIndex(final R gem) {
        if (gem.getMetaType() != ItemMetaType.SUB_META_ITEM) {
            return 0;
        }
        final IMetaItem metaItem = MetaItemManager.INSTANCE.get(gem.getMetaId());
        if (metaItem == null) {
            return 0;
        }
        final int[] subIds = metaItem.getSubIds();
        for (byte index = 0; index < subIds.length; ++index) {
            if (subIds[index] == gem.getId()) {
                return index;
            }
        }
        return 0;
    }
    
    public R getGem(final int refId, final byte perfectionIndex) {
        return this.createGem(refId, perfectionIndex);
    }
    
    protected abstract R createGem(final int p0, final byte p1);
    
    private Loot roll(final LootList list) {
        double roll = MathHelper.randomDouble();
        double accumulatedRate = 0.0;
        double totalDrop = 1.0;
        for (int i = 0, size = list.size(); i < size; ++i) {
            final Loot loot = list.get(i);
            if (loot.getCriterion() != null && !loot.getCriterion().isValid(null, null, null, null)) {
                totalDrop -= loot.getDropRate();
            }
        }
        roll *= totalDrop;
        for (int i = 0, size = list.size(); i < size; ++i) {
            final Loot loot = list.get(i);
            if (loot.getCriterion() == null || loot.getCriterion().isValid(null, null, null, null)) {
                accumulatedRate += loot.getDropRate();
                if (roll < accumulatedRate) {
                    return loot;
                }
            }
        }
        return null;
    }
    
    public boolean containsMetaGem(final int metaGemId) {
        final TByteObjectIterator<LootList> it = this.m_metaGemsLists.iterator();
        while (it.hasNext()) {
            it.advance();
            final LootList list = it.value();
            for (int i = 0, size = list.size(); i < size; ++i) {
                final Loot loot = list.get(i);
                if (loot.getReferenceId() == metaGemId) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FullGemsDefinitionManager.class);
    }
}
