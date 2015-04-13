package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.client.core.*;

public class ResourceCraftHarvestElement extends AbstractCraftHarvestElement
{
    private int m_resourceId;
    
    public ResourceCraftHarvestElement(final int itemId, final int visualId, final int resourceId, final int levelMin, final int duration, final boolean multiple, final ResourceType type) {
        super(itemId, visualId, levelMin, duration, multiple, type);
        this.m_resourceId = resourceId;
    }
    
    @Override
    public String getSourceName() {
        return WakfuTranslator.getInstance().getString(12, this.m_resourceId, new Object[0]);
    }
    
    @Override
    public CraftHarvestElementType getType() {
        return CraftHarvestElementType.RESOURCE;
    }
}
