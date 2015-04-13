package com.ankamagames.wakfu.client.ui.protocol.message.market;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIMarketAddItem extends UIMessage
{
    private Item m_item;
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    @Override
    public int getId() {
        return 19258;
    }
}
