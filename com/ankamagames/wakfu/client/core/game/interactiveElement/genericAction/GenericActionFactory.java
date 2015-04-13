package com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;

public class GenericActionFactory
{
    private static final Logger m_logger;
    
    @Nullable
    public static AbstractClientGenericAction newAction(final int id, final GenericActionConstants constants) {
        if (constants == null) {
            GenericActionFactory.m_logger.error((Object)"Impossible de cr\u00e9er une action de generic \u00e0 partir d'une constante 'null'", (Throwable)new IllegalArgumentException());
            return null;
        }
        switch (constants) {
            case PLAY_SCRIPT: {
                return new PlayScriptGenericAction(id);
            }
            case APPLY_STATE: {
                return new ApplyStateGenericAction(id);
            }
            case TELEPORT: {
                return new TeleportGenericAction(id);
            }
            case GIVE_ITEM: {
                return new GiveItemGenericAction(id);
            }
            case GIVE_KAMA: {
                return new GiveKamaGenericAction(id);
            }
            case RESET_ACHIEVEMENT: {
                return new ResetAchievementGenericAction(id);
            }
            case GIVE_EMOTE: {
                return new GiveEmoteGenericAction(id);
            }
            case KROSMOZ_GAME_PLAY: {
                return new KrosmozGamePlayGenericAction(id);
            }
            case RECUSTOM_CHARACTER: {
                return new RecustomCharacterGenericAction(id);
            }
            case LAUNCH_SCENARIO: {
                return new LaunchScenarioGenericAction(id);
            }
            case OPEN_MERCENARY_DIALOG: {
                return new MercenaryDialogGenericAction(id);
            }
            default: {
                return null;
            }
        }
    }
    
    public static AbstractClientGenericAction newAction(final GenericActivableParameter.Action ieAction) {
        final AbstractClientGenericAction action = newAction(ieAction.getActionId(), ieAction.getActionConstant());
        if (action != null) {
            action.setAction(ieAction);
        }
        return action;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GenericActionFactory.class);
    }
}
