package com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.graphics.*;

public class UIHideOverHeadInfosMessage extends UIMessage
{
    private final OverHeadTarget m_target;
    
    public UIHideOverHeadInfosMessage(final OverHeadTarget target) {
        super();
        this.m_target = target;
    }
    
    public OverHeadTarget getTarget() {
        return this.m_target;
    }
    
    @Override
    public int getId() {
        return 16591;
    }
}
