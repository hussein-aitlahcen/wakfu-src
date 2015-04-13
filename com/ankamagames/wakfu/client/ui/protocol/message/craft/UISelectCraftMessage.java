package com.ankamagames.wakfu.client.ui.protocol.message.craft;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.craft.*;

public class UISelectCraftMessage extends UIMessage
{
    private final CraftView m_craftView;
    
    public UISelectCraftMessage(final CraftView craftView) {
        super();
        this.m_craftView = craftView;
    }
    
    @Override
    public int getId() {
        return 16830;
    }
    
    public CraftView getCraftView() {
        return this.m_craftView;
    }
}
