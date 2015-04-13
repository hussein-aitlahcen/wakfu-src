package com.ankamagames.wakfu.common.game.item.gems;

import org.jetbrains.annotations.*;

public class GemSlotsDefinition
{
    public static final GemSlotsDefinition EMPTY;
    private final GemSlotDefinition[] m_gemSlots;
    
    public GemSlotsDefinition(final GemSlotDefinition[] gemSlots) {
        super();
        this.m_gemSlots = gemSlots;
    }
    
    public boolean canBeAdded(final int index, final GemType gemType) {
        final GemSlotDefinition gemSlotDefinition = this.getGemSlotDefinition(index);
        return gemSlotDefinition != null && gemSlotDefinition.getGemType() == gemType;
    }
    
    @Nullable
    public GemSlotDefinition getGemSlotDefinition(final int index) {
        if (index >= this.m_gemSlots.length) {
            return null;
        }
        return this.m_gemSlots[index];
    }
    
    public GemSlotDefinition[] getGemSlots() {
        return this.m_gemSlots;
    }
    
    public int getSlotCount() {
        return this.m_gemSlots.length;
    }
    
    static {
        EMPTY = new GemSlotsDefinition(new GemSlotDefinition[0]);
    }
}
