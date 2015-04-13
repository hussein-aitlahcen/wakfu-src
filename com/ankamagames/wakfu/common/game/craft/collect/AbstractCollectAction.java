package com.ankamagames.wakfu.common.game.craft.collect;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class AbstractCollectAction
{
    private final int m_id;
    private final int m_resourceNextIndex;
    private final int m_craftId;
    private final int m_levelMin;
    private final int m_nbPlayerMin;
    private final int m_visualId;
    private final int m_collectDuration;
    private final ConsumableInfo m_consumableInfo;
    private final SimpleCriterion m_criterion;
    
    protected AbstractCollectAction(final int actionId, final int craftId, final int levelMin, final int nbPlayerMin, final int collectDuration, final int visualId, final SimpleCriterion criterion, final int resourceNextIndex, final ConsumableInfo consumableInfo) {
        super();
        this.m_id = actionId;
        this.m_craftId = craftId;
        this.m_levelMin = levelMin;
        this.m_nbPlayerMin = nbPlayerMin;
        this.m_visualId = visualId;
        this.m_criterion = criterion;
        this.m_collectDuration = collectDuration;
        this.m_resourceNextIndex = resourceNextIndex;
        this.m_consumableInfo = consumableInfo;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public int getNbRequiredPlayer() {
        return this.m_nbPlayerMin;
    }
    
    public int getLevelMin() {
        return this.m_levelMin;
    }
    
    public int getCraftId() {
        return this.m_craftId;
    }
    
    public int getCollectDuration() {
        return this.m_collectDuration;
    }
    
    public int getResourceNextIndex() {
        return this.m_resourceNextIndex;
    }
    
    public boolean isDestructive() {
        return this.m_resourceNextIndex == 0 || this.m_resourceNextIndex == 16;
    }
    
    public <Info extends ConsumableInfo> Info getConsumableInfo() {
        return (Info)this.m_consumableInfo;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AbstractCollectAction");
        sb.append("{m_id=").append(this.m_id);
        sb.append(", m_craftId=").append(this.m_craftId);
        sb.append(", m_levelMin=").append(this.m_levelMin);
        sb.append(", m_nbPlayerMin=").append(this.m_nbPlayerMin);
        sb.append('}');
        return sb.toString();
    }
}
