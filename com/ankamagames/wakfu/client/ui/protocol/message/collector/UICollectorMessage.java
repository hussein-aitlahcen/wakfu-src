package com.ankamagames.wakfu.client.ui.protocol.message.collector;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.collector.ui.*;

public class UICollectorMessage extends UIMessage
{
    private CollectorContentView m_collectorContentView;
    
    public UICollectorMessage(final CollectorContentView collectorContentView) {
        super();
        this.m_collectorContentView = collectorContentView;
    }
    
    public CollectorContentView getCollectorContentView() {
        return this.m_collectorContentView;
    }
}
