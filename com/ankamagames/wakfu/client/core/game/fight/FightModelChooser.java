package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class FightModelChooser
{
    public static FightModel chooseFightModel(@NotNull final BasicCharacterInfo targetedFighter, @NotNull final PvpUser initiatingFighter) {
        if (targetedFighter instanceof PlayerCharacter && initiatingFighter instanceof PlayerCharacter) {
            return FightModelChooserHelper.choosePvpFightModel(targetedFighter, initiatingFighter);
        }
        if (!(targetedFighter instanceof NonPlayerCharacter) && initiatingFighter instanceof PlayerCharacter) {
            return FightModel.PVE;
        }
        return FightModelChooserHelper.getPveFightModel(targetedFighter);
    }
}
