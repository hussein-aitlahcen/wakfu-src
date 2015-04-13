package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class TimelineDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.timeline";
    
    public static void displayNextFighterInTimeline(final Event event, final List list) {
        list.setOffset(list.getOffset() + 1.0f);
    }
    
    public static void displayPreviousFighterInTimeline(final Event event, final List list) {
        list.setOffset(list.getOffset() - 1.0f);
    }
    
    public static void openCloseSecondTimeline(final Event event) {
        UIMessage.send((short)18100);
    }
    
    public static void openCloseStateBar(final Event event) {
        UIMessage.send((short)18101);
    }
    
    public static void selectFighterInTimeline(final MouseEvent event, final CharacterInfo fighter) {
        if (event.getType() == Events.MOUSE_PRESSED) {
            if (event.getButton() == 1) {
                if (WakfuGameEntity.getInstance().hasFrame(UIFightPlacementFrame.getInstance())) {
                    final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                    if (fighter.isControlledByLocalPlayer()) {
                        UIFightPlacementFrame.getInstance().setSelectedCharacter(fighter);
                        UIFightPlacementFrame.getInstance().handleCompanionIconDisplay();
                    }
                    return;
                }
                if (!WakfuGameEntity.getInstance().hasFrame(UIFightTurnFrame.getInstance())) {
                    return;
                }
                final UIAbstractFightCastInteractionFrame frame = UIFightTurnFrame.getInstance().getCurrentCastFrame();
                if (!WakfuGameEntity.getInstance().hasFrame(frame)) {
                    return;
                }
                if (fighter.canBeFoundByUI()) {
                    final Point3 nearestPos = getNearestPointFromFighter(fighter);
                    UIFightFrame.getInstance();
                    UIFightFrame.setLastTarget((nearestPos != null) ? nearestPos : fighter.getPosition());
                    frame.selectTargetedPosition();
                }
            }
            else if (event.getButton() == 3) {
                UITimelineFrame.getInstance().openCloseFighterDescription(fighter);
            }
        }
    }
    
    private static Point3 getNearestPointFromFighter(final CharacterInfo fighter) {
        final byte radius = fighter.getPhysicalRadius();
        Point3 nearestPos = null;
        if (radius > 0) {
            final CharacterInfo concernedFighter = UIFightTurnFrame.getInstance().getConcernedFighter();
            if (concernedFighter != null && concernedFighter.isControlledByLocalPlayer()) {
                final Point3 concernedFighterPosition = concernedFighter.getPosition();
                for (int i = -radius; i < radius + 1; ++i) {
                    for (int j = -radius; j < radius + 1; ++j) {
                        final Point3 newPos = new Point3(fighter.getWorldCellX() + i, fighter.getWorldCellY() + j);
                        if (nearestPos == null || nearestPos.getDistance(concernedFighterPosition) >= newPos.getDistance(concernedFighterPosition)) {
                            nearestPos = newPos;
                        }
                    }
                }
            }
        }
        return nearestPos;
    }
    
    public static void highlightFighterInTimeline(final Event e, final CharacterInfo fighter) {
        if (fighter != null && fighter.getFighterFieldProvider() != null) {
            UITimelineFrame.getInstance().getTimeline().displayCurrentFighter(fighter);
            PropertiesProvider.getInstance().setPropertyValue("fight.describedFighter", fighter);
            if (WakfuGameEntity.getInstance().hasFrame(UIFightFrame.getInstance())) {
                final Point3 nearestPos = getNearestPointFromFighter(fighter);
                UIFightFrame.getInstance().setOverTimelineCharacter(true);
                fighter.getCurrentFight().getTimeline().highlightFighter(fighter, true);
                if (fighter.canBeFoundByUI()) {
                    UIFightFrame.getInstance();
                    UIFightFrame.setLastTarget((nearestPos != null) ? nearestPos : fighter.getPosition());
                }
            }
        }
    }
    
    public static void unhighlightFighterInTimeline(final Event e, final CharacterInfo fighter) {
        if (fighter != null && fighter.getFighterFieldProvider() != null) {
            PropertiesProvider.getInstance().setPropertyValue("fight.describedFighter", null);
            fighter.getCurrentFight().getTimeline().highlightFighter(fighter, false);
            UIFightFrame.getInstance().setOverTimelineCharacter(false);
        }
    }
    
    public static void onFighterClick(final ItemEvent e) {
        UITimelineFrame.getInstance().openCloseFighterDescription((CharacterInfo)e.getItemValue());
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimelineDialogActions.class);
    }
}
