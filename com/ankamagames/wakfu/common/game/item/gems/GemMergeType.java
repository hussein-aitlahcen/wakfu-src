package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public enum GemMergeType
{
    LEVEL_UP((byte)0, (short)2), 
    RARITY_UP((byte)1, (short)2), 
    GEM_TYPE_UP((byte)2, (short)100);
    
    private final byte m_id;
    private final short m_quantityNeeded;
    
    private GemMergeType(final byte id, final short quantityNeeded) {
        this.m_id = id;
        this.m_quantityNeeded = quantityNeeded;
    }
    
    public static GemMergeType getFromId(final byte id) {
        for (final GemMergeType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public short getQuantityNeeded() {
        return this.m_quantityNeeded;
    }
    
    public boolean canMergeItem(final AbstractReferenceItem item) {
        switch (this) {
            case LEVEL_UP: {
                return item.getGemElementType() != GemElementType.NONE && item.getLevel() < 200;
            }
            case RARITY_UP: {
                return item.getGemElementType() != GemElementType.NONE && ItemRarity.getDescendingRarityComparator().compare(item.getRarity(), BasicGemsDefinitionManager.MAX_GEM_RARITY) > 0;
            }
            case GEM_TYPE_UP: {
                return item.getGemElementType() == GemElementType.POWDER;
            }
            default: {
                return false;
            }
        }
    }
    
    public short computeLevel(final short level) {
        switch (this) {
            case LEVEL_UP: {
                return BasicGemsDefinitionManager.getNextLevel(level);
            }
            default: {
                return level;
            }
        }
    }
    
    public ItemRarity computeRarity(final ItemRarity rarity) {
        switch (this) {
            case RARITY_UP: {
                return rarity.getNextRarity();
            }
            default: {
                return rarity;
            }
        }
    }
    
    public GemElementType computeElementType(final GemElementType type) {
        switch (this) {
            case GEM_TYPE_UP: {
                if (type == GemElementType.POWDER) {
                    return GemElementType.GEM;
                }
                break;
            }
        }
        return type;
    }
}
