package com.ankamagames.wakfu.client.ui.protocol.message.playerGift;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.gift.*;

public class UIPlayerGiftPackageMessage extends UIMessage
{
    private GiftPackage m_package;
    
    public UIPlayerGiftPackageMessage(final GiftPackage content) {
        super();
        this.m_package = content;
    }
    
    public GiftPackage getPackage() {
        return this.m_package;
    }
}
