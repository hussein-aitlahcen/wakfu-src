package com.ankamagames.wakfu.common.game.havenWorld.definition.entry;

public class PartitionCatalogEntry extends HavenWorldCatalogEntry
{
    public static final int BASE_COST = 250000;
    public static final float INCREMENT_COST = 1.05f;
    private final int m_cost;
    
    public PartitionCatalogEntry(final int cost) {
        super((short)(-1), -1, (short)(-1));
        this.m_cost = cost;
    }
    
    @Override
    public int getKamaCost() {
        return this.m_cost;
    }
}
