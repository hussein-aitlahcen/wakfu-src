package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public class IELootChestParameter extends IEParameter
{
    public static final short STATE_USE = 3;
    public static final short STATE_OPENED = 2;
    public static final short STATE_CLOSED = 1;
    private final long m_coolDown;
    private final int m_cost;
    private final int m_itemIdCost;
    private final int m_itemQuantityCost;
    private final boolean m_consumeItem;
    private final int m_nbActivation;
    private final long m_distributionDuration;
    private final SimpleCriterion m_criterion;
    
    public IELootChestParameter(final int paramId, final int visualId, final long coolDown, final int cost, final int itemIdCost, final int itemQuantityCost, final boolean consumeItem, final int nbActivation, final long distributionDuration, final SimpleCriterion criterion, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_coolDown = coolDown;
        this.m_cost = cost;
        this.m_itemIdCost = itemIdCost;
        this.m_itemQuantityCost = itemQuantityCost;
        this.m_consumeItem = consumeItem;
        this.m_nbActivation = nbActivation;
        this.m_distributionDuration = distributionDuration;
        this.m_criterion = criterion;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public long getCoolDown() {
        return this.m_coolDown;
    }
    
    public int getCost() {
        return this.m_cost;
    }
    
    public int getItemIdCost() {
        return this.m_itemIdCost;
    }
    
    public int getItemQuantityCost() {
        return this.m_itemQuantityCost;
    }
    
    public boolean isConsumeItem() {
        return this.m_consumeItem;
    }
    
    public boolean hasItemCost() {
        return this.getItemIdCost() > 0;
    }
    
    public int getNbActivation() {
        return this.m_nbActivation;
    }
    
    public long getDistributionDuration() {
        return this.m_distributionDuration;
    }
    
    @Override
    public String toString() {
        return "IELootChestParameter{m_coolDown=" + this.m_coolDown + ", m_cost=" + this.m_cost + ", m_itemIdCost=" + this.m_itemIdCost + ", m_itemQuantityCost=" + this.m_itemQuantityCost + ", m_consumeItem=" + this.m_consumeItem + ", m_nbActivation=" + this.m_nbActivation + ", m_distributionDuration=" + this.m_distributionDuration + ", m_criterion=" + this.m_criterion + '}';
    }
}
