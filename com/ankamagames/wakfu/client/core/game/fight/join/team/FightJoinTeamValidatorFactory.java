package com.ankamagames.wakfu.client.core.game.fight.join.team;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FightJoinTeamValidatorFactory
{
    public FightJoinTeamValidator getValidator(final FightInfo fightInfo, final byte teamId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final FightModel fightModel = fightInfo.getModel();
        switch (fightModel) {
            case PVP: {
                return new PvpFightJoinTeamValidator(fightInfo, localPlayer, teamId);
            }
            case RANKED_NATION_PVP: {
                return new PvpFightJoinTeamValidator(fightInfo, localPlayer, teamId);
            }
            case TUTO: {
                return new UnjoinableFightJoinTeamValidator(JoinFightResult.CANT_JOIN_TUTO_FIGHT);
            }
            case DUEL: {
                return new DuelFightJoinTeamValidator(fightInfo, localPlayer, teamId);
            }
            case PROTECTOR_ASSAULT:
            case PVE:
            case NOPLACEMENT_MOVEPLAYER:
            case STDNOPLACEMENT:
            case ARCADE_DUNGEON:
            case NO_DEFEAT_CONTINUE_WHEN_NO_PLAYERS:
            case CONTINUE_WHEN_NO_PLAYERS:
            case CONTINUE_WHEN_NO_PLAYERS_NO_COMPANION:
            case TRAINING_FIGHT_WITHOUT_PENALTIES:
            case TRAINING_FIGHT_WITH_XP_WITHOUT_REPORT:
            case COLLECT_FIGHT: {
                return new PveFightJoinTeamValidator(fightInfo, localPlayer, teamId);
            }
            default: {
                throw new IllegalArgumentException(String.format("fightModel %s non g\u00e9r\u00e9.", fightModel));
            }
        }
    }
}
