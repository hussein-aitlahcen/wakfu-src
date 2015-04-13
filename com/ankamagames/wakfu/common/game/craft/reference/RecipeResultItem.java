package com.ankamagames.wakfu.common.game.craft.reference;

public class RecipeResultItem
{
    public static final RecipeResultItem NULL_ITEM;
    private final int m_itemId;
    private final short m_quantity;
    
    public RecipeResultItem(final int itemId, final short quantity) {
        super();
        this.m_itemId = itemId;
        this.m_quantity = quantity;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final RecipeResultItem that = (RecipeResultItem)obj;
        return this.m_itemId == that.m_itemId && this.m_quantity == that.m_quantity;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_itemId;
        result = 31 * result + this.m_quantity;
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RecipeResultItem");
        sb.append("{m_itemId=").append(this.m_itemId);
        sb.append(", m_quantity=").append(this.m_quantity);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        NULL_ITEM = new RecipeResultItem(-1, (short)0);
    }
}
