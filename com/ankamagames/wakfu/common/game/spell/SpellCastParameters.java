package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;

public final class SpellCastParameters
{
    private static final int DEFAULT_COST_PARAMETERS_COUNT = 1;
    private final Map<FighterCharacteristicType, CastParameter> m_cost;
    private CastParameter m_rangeMax;
    private CastParameter m_rangeMin;
    private boolean m_isLosAware;
    private boolean m_onlyInLine;
    private boolean m_rangeDynamic;
    
    public SpellCastParameters() {
        super();
        this.m_cost = new HashMap<FighterCharacteristicType, CastParameter>(1);
    }
    
    public SpellCastParameters(final float actionPointsBase, final float actionPointsLevelIncrement, final float wakfuPointsBase, final float wakfuPointsLevelIncrement, final float movePointsBase, final float movePointsLevelIncrement) {
        super();
        this.m_cost = new HashMap<FighterCharacteristicType, CastParameter>(1);
        this.addCost(FighterCharacteristicType.AP, actionPointsBase, actionPointsLevelIncrement);
        this.addCost(FighterCharacteristicType.MP, movePointsBase, movePointsLevelIncrement);
        this.addCost(FighterCharacteristicType.WP, wakfuPointsBase, wakfuPointsLevelIncrement);
    }
    
    public void addCost(final FighterCharacteristicType charac, final float baseValue, final float increment) {
        if (baseValue == 0.0f && increment == 0.0f) {
            return;
        }
        this.m_cost.put(charac, new CastParameter(baseValue, increment));
    }
    
    public float getCostCharacBaseValue(final FighterCharacteristicType charac) {
        final CastParameter characCost = this.m_cost.get(charac);
        if (characCost == null) {
            return 0.0f;
        }
        return characCost.getBaseValue();
    }
    
    public float getCostCharacIncrement(final FighterCharacteristicType charac) {
        final CastParameter characCost = this.m_cost.get(charac);
        if (characCost == null) {
            return 0.0f;
        }
        return characCost.getIncrement();
    }
    
    public byte getCost(final FighterCharacteristicType charac, final int level) {
        final CastParameter characCost = this.m_cost.get(charac);
        return (byte)((characCost == null) ? 0 : characCost.getCost(level));
    }
    
    public Map<FighterCharacteristicType, CastParameter> getCost() {
        return this.m_cost;
    }
    
    public CastParameter getRangeMin() {
        return this.m_rangeMin;
    }
    
    public void setRangeMin(final float rangeBase, final float rangeInc) {
        this.m_rangeMin = new CastParameter(rangeBase, rangeInc);
    }
    
    public CastParameter getRangeMax() {
        return this.m_rangeMax;
    }
    
    public void setRangeMax(final float rangeBase, final float rangeInc) {
        this.m_rangeMax = new CastParameter(rangeBase, rangeInc);
    }
    
    public boolean isLosAware() {
        return this.m_isLosAware;
    }
    
    public void setLosAware(final boolean losAware) {
        this.m_isLosAware = losAware;
    }
    
    public boolean isOnlyInLine() {
        return this.m_onlyInLine;
    }
    
    public void setOnlyInLine(final boolean onlyInLine) {
        this.m_onlyInLine = onlyInLine;
    }
    
    public boolean isRangeDynamic() {
        return this.m_rangeDynamic;
    }
    
    public void setRangeDynamic(final boolean rangeDynamic) {
        this.m_rangeDynamic = rangeDynamic;
    }
}
