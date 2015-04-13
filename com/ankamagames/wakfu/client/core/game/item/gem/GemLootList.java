package com.ankamagames.wakfu.client.core.game.item.gem;

import java.util.*;
import com.ankamagames.wakfu.common.game.item.loot.*;

public class GemLootList implements LootList
{
    private final ArrayList<GemLoot> m_list;
    
    public GemLootList() {
        super();
        this.m_list = new ArrayList<GemLoot>();
    }
    
    public void addLoot(final GemLoot loot) {
        this.m_list.add(loot);
    }
    
    @Override
    public Loot get(final int idx) {
        return this.m_list.get(idx);
    }
    
    @Override
    public int size() {
        return this.m_list.size();
    }
}
