package com.ankamagames.wakfu.client.core.game.fight.join;

import com.ankamagames.wakfu.client.core.game.fight.handler.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class SummonForceCharacDisplayFightRunnable implements FightEventRunnable
{
    @Override
    public void runSpellCastEvent(final BasicCharacterInfo caster, final long spellElement) {
    }
    
    @Override
    public void runFighterEvent(final BasicCharacterInfo fighter) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (fighter != localPlayer && fighter.isControlledByAI() && fighter.isActiveProperty(FightPropertyType.SUMMON_FORCE_CHARACTERISTIC_DISPLAY)) {
            UIFightTurnFrame.prepareDisplayedFighter((CharacterInfo)fighter);
        }
    }
    
    @Override
    public void runFightEvent() {
    }
}
