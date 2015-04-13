package com.ankamagames.wakfu.client.core.game.item;

public class AllBagsView extends AbstractBagView
{
    private final ClientBagContainer m_bag;
    
    public AllBagsView(final ClientBagContainer bag) {
        super();
        this.m_bag = bag;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("bagInventory")) {
            return this.m_bag.getAllItems();
        }
        if (fieldName.equals("canBeSorted")) {
            return false;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AllBagsView");
        sb.append("{m_bag=").append(this.m_bag);
        sb.append('}');
        return sb.toString();
    }
}
