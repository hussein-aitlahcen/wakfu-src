package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.craft.collect.*;

public class CollectAction extends AbstractCollectAction
{
    private final int m_resourceStep;
    private final int m_collectItemId;
    private final byte m_order;
    private boolean m_displayInCraftDialog;
    private int[] m_lootList;
    
    public CollectAction(final int collectId, final int resourceStep, final int craftId, final int levelMin, final int nbPlayerMin, final int collectDuration, final int collectedItemId, final int visualId, final SimpleCriterion criterion, final int resourceNextIndex, final byte order, final ConsumableInfo consumableInfo, final boolean displayInCraftDialog, final int[] lootList) {
        super(collectId, craftId, levelMin, nbPlayerMin, collectDuration, visualId, criterion, resourceNextIndex, consumableInfo);
        this.m_resourceStep = resourceStep;
        this.m_collectItemId = collectedItemId;
        this.m_order = order;
        this.m_displayInCraftDialog = displayInCraftDialog;
        this.m_lootList = lootList;
    }
    
    public int getResourceStep() {
        return this.m_resourceStep;
    }
    
    public byte getOrder() {
        return this.m_order;
    }
    
    public int getCollectItemId() {
        return this.m_collectItemId;
    }
    
    public boolean isDisplayInCraftDialog() {
        return this.m_displayInCraftDialog;
    }
    
    public int[] getLootList() {
        return this.m_lootList;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CollectAction{");
        sb.append(super.toString());
        sb.append("m_resourceStep=").append(this.m_resourceStep);
        sb.append(", m_collectItemId=").append(this.m_collectItemId);
        sb.append('}');
        return sb.toString();
    }
}
