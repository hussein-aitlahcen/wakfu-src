package com.ankamagames.wakfu.client.ui.protocol.message.havenWorld;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;

public class UICatalogEntryMessage extends UIMessage
{
    private final HavenWorldCatalogEntryView m_havenWorldCatalogEntryView;
    
    public UICatalogEntryMessage(final HavenWorldCatalogEntryView havenWorldCatalogEntryView) {
        super();
        this.m_havenWorldCatalogEntryView = havenWorldCatalogEntryView;
    }
    
    public HavenWorldCatalogEntryView getHavenWorldCatalogEntryView() {
        return this.m_havenWorldCatalogEntryView;
    }
}
