package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class GemsUpgradeData
{
    private byte m_numGems;
    private ItemRarity m_rarity;
    
    public GemsUpgradeData(final int numGems, final ItemRarity rarity) {
        super();
        this.m_numGems = MathHelper.ensureByte(numGems);
        this.m_rarity = rarity;
    }
    
    public ItemRarity getRarity() {
        return this.m_rarity;
    }
    
    public byte getNumGems() {
        return this.m_numGems;
    }
    
    public boolean isRarityValid(final ItemRarity rarity) {
        return rarity.getSortOrder() >= this.m_rarity.getSortOrder();
    }
}
