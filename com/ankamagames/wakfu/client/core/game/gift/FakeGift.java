package com.ankamagames.wakfu.client.core.game.gift;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;

public class FakeGift extends ImmutableFieldProvider implements Gift
{
    @Override
    public String[] getFields() {
        return FakeGift.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getItemBigIconUrl(47212409);
        }
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("fakeGiftItemName") + " x" + 1;
        }
        if (fieldName.equals("quantity")) {
            return 1;
        }
        if (fieldName.equals("allowControls")) {
            return false;
        }
        return null;
    }
    
    @Override
    public boolean consume(final boolean all) {
        return false;
    }
}
