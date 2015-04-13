package com.ankamagames.wakfu.common.game.pet.definition;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;

public class PetDefinitionMeal
{
    private final IntBooleanLightWeightMap m_mealItemRefIds;
    private final GameIntervalConst m_mealMinInterval;
    private final GameIntervalConst m_mealMaxInterval;
    private final byte m_xpPerMeal;
    
    public PetDefinitionMeal(final long mealMinInterval, final long mealMaxInterval, final byte xpPerMeal) {
        super();
        this.m_mealItemRefIds = new IntBooleanLightWeightMap();
        this.m_mealMinInterval = GameInterval.fromSeconds(mealMinInterval);
        this.m_mealMaxInterval = GameInterval.fromSeconds(mealMaxInterval);
        this.m_xpPerMeal = xpPerMeal;
    }
    
    public GameIntervalConst getMealMinInterval() {
        return this.m_mealMinInterval;
    }
    
    public GameIntervalConst getMealMaxInterval() {
        return this.m_mealMaxInterval;
    }
    
    public byte getXpPerMeal() {
        return this.m_xpPerMeal;
    }
    
    public void addMeal(final int itemRefId, final boolean visible) {
        this.m_mealItemRefIds.put(itemRefId, visible);
    }
    
    public boolean containsMeal(final int itemRefId) {
        return this.m_mealItemRefIds.contains(itemRefId);
    }
    
    public boolean foreachVisibleMeal(final TIntProcedure procedure) {
        for (int i = 0, size = this.m_mealItemRefIds.size(); i < size; ++i) {
            if (this.m_mealItemRefIds.getQuickValue(i)) {
                if (!procedure.execute(this.m_mealItemRefIds.getQuickKey(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "PetDefinitionMeal{m_mealItemRefIds=" + this.m_mealItemRefIds.size() + ", m_mealMinInterval=" + this.m_mealMinInterval + ", m_mealMaxInterval=" + this.m_mealMaxInterval + ", m_xpPerMeal=" + this.m_xpPerMeal + '}';
    }
}
