package com.ankamagames.wakfu.client.ui.protocol.message.havenWorld;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;

public class UIHavenWorldModification extends UIMessage
{
    private static final Logger m_logger;
    private final Modification m_modification;
    
    public UIHavenWorldModification(final Modification modification) {
        super();
        this.m_modification = modification;
    }
    
    public Modification getModification() {
        return this.m_modification;
    }
    
    @Override
    public int getId() {
        return 17803;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIHavenWorldModification.class);
    }
}
