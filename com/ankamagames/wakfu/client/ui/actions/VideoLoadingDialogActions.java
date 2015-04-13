package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

@XulorActionsTag
public class VideoLoadingDialogActions
{
    public static final String PACKAGE = "wakfu.videoLoading";
    
    public static void onClick(final MouseEvent e) {
        if (PropertiesProvider.getInstance().getBooleanProperty("isNewWorldReady")) {
            WakfuGameEntity.getInstance().removeFrame(UIVideoLoadingFrame.getInstance());
        }
    }
}
