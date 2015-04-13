package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.client.core.*;

public class MonsterFamilyCraftHarvestElement extends AbstractCraftHarvestElement
{
    private int m_monsterFamily;
    
    public MonsterFamilyCraftHarvestElement(final int itemId, final int visualId, final int monsterFamily, final int levelMin, final int duration, final boolean multiple) {
        super(itemId, visualId, levelMin, duration, multiple, ResourceType.MOB);
        this.m_monsterFamily = monsterFamily;
    }
    
    @Override
    public String getSourceName() {
        return WakfuTranslator.getInstance().getString(38, this.m_monsterFamily, new Object[0]);
    }
    
    @Override
    public CraftHarvestElementType getType() {
        return CraftHarvestElementType.MONSTER;
    }
}
