package com.ankamagames.wakfu.client.ui.protocol.message.item;

import com.ankamagames.wakfu.common.game.item.*;

public class UISocketGemMessage extends AbstractUIDetailMessage<Item>
{
    private Item m_gem;
    
    public Item getGem() {
        return this.m_gem;
    }
    
    public void setGem(final Item gem) {
        this.m_gem = gem;
    }
    
    @Override
    public int getId() {
        return 16870;
    }
}
