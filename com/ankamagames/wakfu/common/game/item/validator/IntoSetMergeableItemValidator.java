package com.ankamagames.wakfu.common.game.item.validator;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class IntoSetMergeableItemValidator implements InventoryContentValidator<Item>
{
    public static final IntoSetMergeableItemValidator INSTANCE;
    
    @Override
    public boolean isValid(final Item item) {
        return item.isMergeableIntoSet();
    }
    
    static {
        INSTANCE = new IntoSetMergeableItemValidator();
    }
}
