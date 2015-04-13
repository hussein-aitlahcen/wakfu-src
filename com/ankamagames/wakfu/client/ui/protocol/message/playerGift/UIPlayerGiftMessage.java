package com.ankamagames.wakfu.client.ui.protocol.message.playerGift;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.gift.*;

public class UIPlayerGiftMessage extends UIMessage
{
    private GiftItem m_gift;
    
    public UIPlayerGiftMessage(final GiftItem content) {
        super();
        this.m_gift = content;
    }
    
    public GiftItem getGift() {
        return this.m_gift;
    }
}
