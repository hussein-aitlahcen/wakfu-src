package com.ankamagames.wakfu.common.game.item.gems;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class BasicGemsDefinitionManager
{
    public static final BasicGemsDefinitionManager INSTANCE;
    public static final int GEM_LEVEL_DELTA = 10;
    public static final short MAX_GEM_LEVEL = 200;
    public static final ItemRarity MAX_GEM_RARITY;
    private TIntIntHashMap m_gems;
    
    private BasicGemsDefinitionManager() {
        super();
        this.m_gems = new TIntIntHashMap();
    }
    
    public static short trimLevel(final short level) {
        return (short)(level - level % 10 + 10);
    }
    
    public static short getNextLevel(final short level) {
        return (short)(level + 10);
    }
    
    private static int getKey(final GemElementType type, final ItemRarity rarity, final short level) {
        return MathHelper.getIntFromTwoShort(MathHelper.getShortFromTwoBytes(type.getId(), (byte)rarity.getId()), level);
    }
    
    private static void checkLevel(final short level) {
        if (level % 10 != 0) {
            throw new IllegalArgumentException("le niveau de gemme demand\u00e9 n'est pas multiple de 10");
        }
    }
    
    public void registerGem(final GemElementType type, final ItemRarity rarity, final short level, final int refItemId) {
        checkLevel(level);
        final int key = getKey(type, rarity, level);
        this.m_gems.put(key, refItemId);
    }
    
    public AbstractReferenceItem getGem(final GemElementType type, final ItemRarity rarity, final short level) {
        checkLevel(level);
        return ReferenceItemManager.getInstance().getReferenceItem(this.m_gems.get(getKey(type, rarity, level)));
    }
    
    static {
        INSTANCE = new BasicGemsDefinitionManager();
        MAX_GEM_RARITY = ItemRarity.LEGENDARY;
    }
}
