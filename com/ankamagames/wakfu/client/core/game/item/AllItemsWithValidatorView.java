package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

public class AllItemsWithValidatorView extends AbstractBagView
{
    private final ClientBagContainer m_bag;
    private final InventoryContentValidator<Item> m_validator;
    
    public AllItemsWithValidatorView(final ClientBagContainer bag, final InventoryContentValidator<Item> validator) {
        super();
        this.m_bag = bag;
        this.m_validator = validator;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("bagInventory")) {
            return this.buildItemList();
        }
        if (fieldName.equals("canBeSorted")) {
            return false;
        }
        return super.getFieldValue(fieldName);
    }
    
    private Collection<Item> buildItemList() {
        return this.m_bag.getAllWithValidator(this.m_validator);
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
