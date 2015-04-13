package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public class FightActionHelper
{
    public static void executeInFight(final Runnable runnable) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            final Fight fight = localPlayer.getCurrentOrObservedFight();
            if (fight != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fight, new GenericAction(runnable));
                FightActionGroupManager.getInstance().executePendingGroup(fight);
                return;
            }
        }
        runnable.run();
    }
}
