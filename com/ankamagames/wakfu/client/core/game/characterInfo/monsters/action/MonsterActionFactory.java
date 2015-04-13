package com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.characterInfo.action.*;
import org.jetbrains.annotations.*;

public class MonsterActionFactory
{
    private static final Logger m_logger;
    
    @Nullable
    public static AbstractClientMonsterAction newAction(final int actionId, final byte actionTypeId, final SimpleCriterion criterion, final boolean criteriaOnNpc, final ActionVisual visual, final int actionScriptId, final long duration, final boolean needProgressBar, final boolean movePlayer) {
        final MonsterActionConstants constants = MonsterActionConstants.getFromId(actionTypeId);
        if (constants == null) {
            MonsterActionFactory.m_logger.error((Object)("Impossible de trouver l'action de monstre " + actionTypeId));
            return null;
        }
        switch (constants) {
            case DO_NOTHING: {
                return new MonsterActionDoNothing(actionId, actionTypeId, criterion, criteriaOnNpc, visual, actionScriptId, duration, needProgressBar, movePlayer, constants.isCanBeTriggeredWhenBusy());
            }
            case SET_COMPORTMENT: {
                return new MonsterActionSetComportment(actionId, actionTypeId, criterion, criteriaOnNpc, visual, actionScriptId, duration, needProgressBar, movePlayer, constants.isCanBeTriggeredWhenBusy());
            }
            case DESTROY: {
                return new MonsterActionDestroy(actionId, actionTypeId, criterion, criteriaOnNpc, visual, actionScriptId, duration, needProgressBar, movePlayer, constants.isCanBeTriggeredWhenBusy());
            }
            case START_DIALOG: {
                return new MonsterActionDialog(actionId, actionTypeId, criterion, criteriaOnNpc, visual, actionScriptId, duration, needProgressBar, movePlayer, constants.isCanBeTriggeredWhenBusy());
            }
            case MANAGE_HAVEN_WORLD: {
                return new MonsterActionManageHavenWorld(actionId, actionTypeId, criterion, criteriaOnNpc, visual, actionScriptId, duration, needProgressBar, movePlayer, constants.isCanBeTriggeredWhenBusy());
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonsterActionFactory.class);
    }
}
