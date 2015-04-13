package com.ankamagames.wakfu.client.core.game.fight.actionsOperations;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class CreationActionSequenceOperations
{
    private static final int NO_ACTION_ID = 0;
    private Fight m_concernedFight;
    private CharacterTeleportAction m_characterTeleportAction;
    private DisplayFightMessageInterface m_displayFightMessageInterface2;
    private StartPlacementAction m_startPlacementAction;
    private StartActionAction m_startActionAction;
    private NewTableTurnAction m_newTableTurnAction;
    private FighterTurnStartAction m_fighterTurnStartAction;
    
    public CreationActionSequenceOperations() {
        super();
        this.m_concernedFight = null;
    }
    
    public void execute() {
        this.addActions(DisplayFightMessageInterface.checkout(TimedAction.getNextUid(), FightActionType.DISPLAY_FIGHT_INTERFACE_MESSAGE.getId(), 0, "fight.beginning"), this.m_characterTeleportAction, PushFrameAction.checkout(TimedAction.getNextUid(), FightActionType.PUSH_FRAME.getId(), 0, getUiFrames()), this.m_displayFightMessageInterface2, this.m_startPlacementAction, this.m_startActionAction, this.m_newTableTurnAction, this.m_fighterTurnStartAction);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
    }
    
    public static ArrayList<MessageFrame> getUiFrames() {
        final ArrayList<MessageFrame> frameList = new ArrayList<MessageFrame>();
        frameList.add(UITimelineFrame.getInstance());
        frameList.add(UIFightFrame.getInstance());
        frameList.add(UIFightCameraFrame.getInstance());
        return frameList;
    }
    
    private void addActions(final Action... actions) {
        for (int i = 0; i < actions.length; ++i) {
            final Action action = actions[i];
            this.addAction(action);
        }
    }
    
    private void addAction(final Action action) {
        FightActionGroupManager.getInstance().addActionToPendingGroup(this.m_concernedFight, action);
    }
    
    public void reset() {
        this.m_characterTeleportAction = null;
        this.m_displayFightMessageInterface2 = null;
        this.m_startPlacementAction = null;
        this.m_startActionAction = null;
        this.m_newTableTurnAction = null;
        this.m_fighterTurnStartAction = null;
    }
    
    public void setConcernedFight(final Fight concernedFight) {
        this.m_concernedFight = concernedFight;
    }
    
    public void setDisplayFightMessageInterface2(final DisplayFightMessageInterface displayFightMessageInterface2) {
        this.m_displayFightMessageInterface2 = displayFightMessageInterface2;
    }
    
    public void setFighterTurnStartAction(final FighterTurnStartAction fighterTurnStartAction) {
        this.m_fighterTurnStartAction = fighterTurnStartAction;
    }
    
    public void setNewTableTurnAction(final NewTableTurnAction newTableTurnAction) {
        this.m_newTableTurnAction = newTableTurnAction;
    }
    
    public void setStartActionAction(final StartActionAction startActionAction) {
        this.m_startActionAction = startActionAction;
    }
    
    public void setStartPlacementAction(final StartPlacementAction startPlacementAction) {
        this.m_startPlacementAction = startPlacementAction;
    }
    
    public void addCharacterTeleport(final CharacterInfo info, final Point3 destination) {
        if (this.m_characterTeleportAction == null) {
            this.m_characterTeleportAction = CharacterTeleportAction.checkout(TimedAction.getNextUid(), FightActionType.TELEPORT_CHARACTER.getId(), 0, info, destination);
        }
        else {
            this.m_characterTeleportAction.addCharacterTeleport(info, destination);
        }
    }
}
