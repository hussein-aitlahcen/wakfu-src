package com.ankamagames.wakfu.common.game.item.referenceItem;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.gems.*;

public abstract class AbstractReferenceItemBuilder<T extends AbstractReferenceItemBuilder, I extends AbstractReferenceItem>
{
    protected final I m_item;
    
    public AbstractReferenceItemBuilder(final I item) {
        super();
        this.m_item = item;
    }
    
    public I getItem() {
        return this.m_item;
    }
    
    public T setId(final int id) {
        this.m_item.setId(id);
        return (T)this;
    }
    
    public T setSetId(final short setId) {
        this.m_item.setSetId(setId);
        return (T)this;
    }
    
    public T setItemType(final AbstractItemType type) {
        this.m_item.setItemType(type);
        return (T)this;
    }
    
    public T setGfxId(final int gfxId) {
        this.m_item.setGfxId(gfxId);
        return (T)this;
    }
    
    public T setFemaleGfxId(final int gfxId) {
        this.m_item.setFemaleGfxId(gfxId);
        return (T)this;
    }
    
    public T setFloorGfxId(final int floorGfxId) {
        this.m_item.setFloorGfxId(floorGfxId);
        return (T)this;
    }
    
    public T setLevel(final short level) {
        this.m_item.setLevel(level);
        return (T)this;
    }
    
    public T setCriteria(final Map<ActionsOnItem, SimpleCriterion> criteria) {
        this.m_item.setCriteria(criteria);
        return (T)this;
    }
    
    public T setStackMaximumHeight(final short stackMaximumHeight) {
        this.m_item.setStackMaximumHeight(stackMaximumHeight);
        return (T)this;
    }
    
    public T setUseCostAP(final byte useCostAP) {
        this.m_item.setUseCostAP(useCostAP);
        return (T)this;
    }
    
    public T setUseCostMP(final byte useCostMP) {
        this.m_item.setUseCostMP(useCostMP);
        return (T)this;
    }
    
    public T setUseCostWP(final byte useCostWP) {
        this.m_item.setUseCostWP(useCostWP);
        return (T)this;
    }
    
    public T setUseRangeMin(final int useRangeMin) {
        this.m_item.setUseRangeMin(useRangeMin);
        return (T)this;
    }
    
    public T setUseRangeMax(final int useRangeMax) {
        this.m_item.setUseRangeMax(useRangeMax);
        return (T)this;
    }
    
    public T setUseTestFreeCell(final boolean useTestFreeCell) {
        this.m_item.setUseTestFreeCell(useTestFreeCell);
        return (T)this;
    }
    
    public T setUseTestNotBorderCell(final boolean useTestNotBorderCell) {
        this.m_item.setUseTestNotBorderCell(useTestNotBorderCell);
        return (T)this;
    }
    
    public T setUseTestLOS(final boolean useTestLOS) {
        this.m_item.setUseTestLOS(useTestLOS);
        return (T)this;
    }
    
    public T setUseOnlyInLine(final boolean useOnlyInLine) {
        this.m_item.setUseOnlyInLine(useOnlyInLine);
        return (T)this;
    }
    
    public T setRarity(final ItemRarity rarity) {
        this.m_item.setRarity(rarity);
        return (T)this;
    }
    
    public T setBindType(final ItemBindType type) {
        this.m_item.setBindType(type);
        return (T)this;
    }
    
    public T setType(final ItemType type) {
        this.m_item.setType(type);
        return (T)this;
    }
    
    public T setGemType(final GemType type) {
        this.m_item.setGemType(type);
        return (T)this;
    }
    
    public T setGemsNum(final byte gemsNum) {
        this.m_item.setGemsNum(gemsNum);
        return (T)this;
    }
    
    public T setGemElementType(final GemElementType type) {
        this.m_item.setGemElementType(type);
        return (T)this;
    }
}
