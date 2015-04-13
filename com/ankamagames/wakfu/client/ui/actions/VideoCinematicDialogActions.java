package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.messagebox.*;

@XulorActionsTag
public class VideoCinematicDialogActions
{
    private static final Logger m_logger;
    public static final String PACKAGE = "wakfu.videoCinematic";
    
    public static void onClick(final MouseEvent e) {
        final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("question.quitCinematic"), 6L);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type != 2) {
                    return;
                }
                UICinematicVideoFrame.INSTANCE.stopVideo();
                WakfuGameEntity.getInstance().removeFrame(UICinematicVideoFrame.INSTANCE);
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)VideoCinematicDialogActions.class);
    }
}
