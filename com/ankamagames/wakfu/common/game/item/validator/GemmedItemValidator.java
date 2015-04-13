package com.ankamagames.wakfu.common.game.item.validator;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class GemmedItemValidator implements InventoryContentValidator<Item>
{
    public static final GemmedItemValidator INSTANCE;
    
    @Override
    public boolean isValid(final Item content) {
        return content.hasGemsSlotted();
    }
    
    static {
        INSTANCE = new GemmedItemValidator();
    }
}
