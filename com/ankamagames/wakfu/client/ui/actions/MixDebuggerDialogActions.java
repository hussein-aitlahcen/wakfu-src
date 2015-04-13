package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class MixDebuggerDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.mixDebugger";
    
    public static void pause(final SelectionChangedEvent e) {
        if (e.isSelected()) {
            UIMixDebugger.getInstance().pause();
        }
        else {
            UIMixDebugger.getInstance().unPause();
        }
    }
    
    public static void chooseSaveDirectory(final Event e) {
        UIMixDebugger.getInstance().selectSaveDirectory();
    }
    
    public static void save(final Event e) {
        UIMixDebugger.getInstance().saveLogsToFile();
    }
    
    public static void setRefreshOn(final Event event) {
        UIMixDebugger.getInstance().getMixDebugger().setRefreshCache(true);
    }
    
    public static void setRefreshOff(final Event event) {
        UIMixDebugger.getInstance().getMixDebugger().setRefreshCache(false);
    }
    
    public static void forceRefresh(final Event event) {
        UIMixDebugger.getInstance().refresh(true);
    }
    
    public static void selectSoundGroup(final Event event, final UIMixDebugger.SourceGroupDisplayer sourceGroupDisplayer) {
        final ToggleButton toggleButton = event.getCurrentTarget();
        sourceGroupDisplayer.setVisible(toggleButton.getSelected());
    }
    
    public static void selectLogSoundGroup(final Event event, final UIMixDebugger.SourceGroupDisplayer sourceGroupDisplayer) {
        final ToggleButton toggleButton = event.getCurrentTarget();
        sourceGroupDisplayer.setLogFiltered(toggleButton.getSelected());
    }
    
    static {
        m_logger = Logger.getLogger((Class)MixDebuggerDialogActions.class);
    }
}
