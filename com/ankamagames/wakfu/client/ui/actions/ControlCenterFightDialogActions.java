package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class ControlCenterFightDialogActions
{
    public static final String PACKAGE = "wakfu.controlCenterFight";
    private static final Logger m_logger;
    
    public static void openCloseStateBar(final Event event) {
        ControlCenterDialogActions.openCloseStateBar(event);
    }
    
    public static void dropCommand(final Event event, final String index) {
        ControlCenterDialogActions.dropCommand(event, index);
    }
    
    public static void fightSetDirection(final Fight fight, final short fightMessageId) {
        if (!PropertiesProvider.getInstance().getBooleanProperty("isInFightPlacement")) {
            if (fight.getTimeline().getCurrentFighter() == WakfuGameEntity.getInstance().getLocalPlayer() || fight.getTimeline().getCurrentFighter().isControlledByLocalPlayer()) {
                UIMessage.send(fightMessageId);
            }
        }
        else if (!fight.getTimeline().hasCurrentFighter()) {
            ControlCenterFightDialogActions.m_logger.info((Object)"Changement d'orientation inutile en phase de placement. Aucune action effecut\u00e9e");
            UIMessage.send(fightMessageId);
        }
    }
    
    private static void setDirection(final short messageId, final short fightMessageId) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
        if (fight != null) {
            fightSetDirection(fight, fightMessageId);
        }
        else {
            UIMessage.send(messageId);
        }
    }
    
    public static void setSouthEastDirection(final Event event) {
        setDirection((short)16708, (short)18004);
    }
    
    public static void setSouthWestDirection(final Event event) {
        setDirection((short)16709, (short)18005);
    }
    
    public static void setNorthWestDirection(final Event event) {
        setDirection((short)16707, (short)18003);
    }
    
    public static void setNorthEastDirection(final Event event) {
        setDirection((short)16710, (short)18006);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ControlCenterFightDialogActions.class);
    }
}
