package com.ankamagames.wakfu.client.ui.protocol.message.item;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIFakeItemMessage extends UIMessage
{
    private final FakeItem m_fakeItem;
    
    public UIFakeItemMessage(final FakeItem fakeItem) {
        super();
        this.m_fakeItem = fakeItem;
    }
    
    public FakeItem getFakeItem() {
        return this.m_fakeItem;
    }
}
