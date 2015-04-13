package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.game.pvp.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class FightModelChooserHelper
{
    @Nullable
    public static FightModel choosePvpFightModel(final PvpUser targetedFighter, final PvpUser initiatingFighter) {
        if (NationPvpHelper.testPlayersCanDoRankedNationPvp(initiatingFighter, targetedFighter) == JoinFightResult.OK) {
            return FightModel.RANKED_NATION_PVP;
        }
        if (NationPvpHelper.testPlayersCanDoRegularPvp(initiatingFighter, targetedFighter) == JoinFightResult.OK) {
            return FightModel.PVP;
        }
        return null;
    }
    
    public static FightModel getPveFightModel(final BasicCharacterInfo targetedFighter) {
        if (targetedFighter.getProtector() != null) {
            return FightModel.PROTECTOR_ASSAULT;
        }
        if (!targetedFighter.isActiveProperty(WorldPropertyType.TRAINING_MOB)) {
            return FightModel.PVE;
        }
        if (targetedFighter.isActiveProperty(WorldPropertyType.NO_SPELL_OR_SKILL_XP)) {
            return FightModel.TRAINING_FIGHT;
        }
        return FightModel.TRAINING_FIGHT_WITH_XP_WITHOUT_REPORT;
    }
}
