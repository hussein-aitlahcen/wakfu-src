package com.ankamagames.wakfu.common.game.item.gems;

public class GemSlotDefinition
{
    public static final GemSlotDefinition ATTACK;
    private final GemType m_gemType;
    
    public GemSlotDefinition(final GemType gemType) {
        super();
        this.m_gemType = gemType;
    }
    
    public GemType getGemType() {
        return this.m_gemType;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GemSlotDefinition");
        sb.append("{m_gemType=").append(this.m_gemType);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        ATTACK = new GemSlotDefinition(GemType.ATTACK);
    }
}
