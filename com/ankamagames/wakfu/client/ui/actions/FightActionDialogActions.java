package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class FightActionDialogActions
{
    public static final String PACKAGE = "wakfu.fightAction";
    private static boolean m_callingHelp;
    
    public static void endPlacement(final Event event) {
        UIMessage.send((short)18007);
    }
    
    public static void fighterEndsTurn(final Event event) {
        if (PropertiesProvider.getInstance().getBooleanProperty("isInFightPlacement")) {
            UIMessage.send((short)18007);
        }
        else {
            UIMessage.send((short)18001);
        }
    }
    
    public static void giveUpFight(final Event event) {
        UIMessage.send((short)18000);
    }
    
    public static void fighterSetSouthEastDirection(final Event event) {
        UIMessage.send((short)18004);
    }
    
    public static void fighterSetSouthWestDirection(final Event event) {
        UIMessage.send((short)18005);
    }
    
    public static void fighterSetNorthWestDirection(final Event event) {
        UIMessage.send((short)18003);
    }
    
    public static void fighterSetNorthEastDirection(final Event event) {
        UIMessage.send((short)18006);
    }
    
    public static void callHelp(final Event event) {
        final Window window = UIControlCenterContainerFrame.getInstance().getWindow();
        if (window != null) {
            callHelp(event, (Button)window.getElementMap().getElement("callHelpBtn"));
        }
    }
    
    public static void callHelp(final Event event, final Button b) {
        if (FightActionDialogActions.m_callingHelp) {
            return;
        }
        UIMessage.send((short)18011);
        b.setEnabled(false);
        FightActionDialogActions.m_callingHelp = true;
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                FightActionDialogActions.m_callingHelp = false;
                if (WakfuGameEntity.getInstance().hasFrame(UIFightPlacementFrame.getInstance())) {
                    b.setEnabled(true);
                }
            }
        }, 5000L, 1);
    }
    
    public static void reportCell(final Event event) {
        final Window window = UIControlCenterContainerFrame.getInstance().getWindow();
        if (window != null) {
            reportCell(event, (Button)window.getElementMap().getElement("reportCellBtn"));
        }
    }
    
    public static void reportCell(final Event event, final Button b) {
        UIMessage.send((short)18013);
        b.setEnabled(false);
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                b.setEnabled(true);
            }
        }, 5000L, 1);
    }
    
    public static void hideFighters(final MouseEvent event) {
        UIMessage.send((short)16713);
    }
    
    public static void openSpellsPage(final Event event) {
        UISpellsPageFrame.getInstance().loadUnloadSpellsPage();
    }
}
