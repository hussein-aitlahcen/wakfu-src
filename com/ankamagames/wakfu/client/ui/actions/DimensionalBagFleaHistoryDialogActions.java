package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class DimensionalBagFleaHistoryDialogActions
{
    public static final String PACKAGE = "wakfu.dimensionalBagFleaHistory";
    private static final Logger m_logger;
    
    public static void closeDialog(final Event e) {
        WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagSaleHistoryFrame.getInstance());
    }
    
    public static void previousPage(final Event e) {
        UIMessage.send((short)17010);
    }
    
    public static void nextPage(final Event e) {
        UIMessage.send((short)17011);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagFleaHistoryDialogActions.class);
    }
}
