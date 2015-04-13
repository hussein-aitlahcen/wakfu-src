package com.ankamagames.wakfu.client.ui.protocol.message.craft;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.component.*;

public class UIStartCraftMessage extends UIMessage
{
    private ProgressBar m_bar;
    private ToggleButton m_button;
    
    public UIStartCraftMessage(final ProgressBar bar, final ToggleButton button) {
        super();
        this.m_bar = bar;
        this.m_button = button;
    }
    
    public ProgressBar getBar() {
        return this.m_bar;
    }
    
    public ToggleButton getButton() {
        return this.m_button;
    }
    
    @Override
    public int getId() {
        return 16840;
    }
}
