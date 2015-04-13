package com.ankamagames.wakfu.common.game.havenWorld.definition.entry;

public abstract class HavenWorldCatalogEntry
{
    private final short m_id;
    private final int m_categoryId;
    private final short m_maxQuantity;
    
    protected HavenWorldCatalogEntry(final short id, final int categoryId, final short maxQuantity) {
        super();
        this.m_id = id;
        this.m_categoryId = categoryId;
        this.m_maxQuantity = maxQuantity;
    }
    
    public abstract int getKamaCost();
    
    public final short getId() {
        return this.m_id;
    }
    
    public final int getCategoryId() {
        return this.m_categoryId;
    }
    
    public final short getMaxQuantity() {
        return this.m_maxQuantity;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' + "m_id=" + this.m_id + ", m_categoryId=" + this.m_categoryId + ", m_maxQuantity=" + this.m_maxQuantity + '}';
    }
}
