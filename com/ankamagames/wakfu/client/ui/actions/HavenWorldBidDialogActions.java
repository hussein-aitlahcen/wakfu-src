package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class HavenWorldBidDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.havenWorldBid";
    
    public static void bid(final Event event) {
        UIMessage.send((short)19362);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldBidDialogActions.class);
    }
}
