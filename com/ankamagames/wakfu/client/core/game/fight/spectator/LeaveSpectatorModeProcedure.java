package com.ankamagames.wakfu.client.core.game.fight.spectator;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public final class LeaveSpectatorModeProcedure
{
    public void execute() {
        if (!WakfuGameEntity.getInstance().getLocalPlayer().isInSpectatorMode()) {
            return;
        }
        final Fight observedFight = WakfuGameEntity.getInstance().getLocalPlayer().getObservedFight();
        WakfuGameEntity.getInstance().getLocalPlayer().setObservedFight(null);
        WakfuGameEntity.getInstance().removeFrame(NetSpectatorFightFrame.getInstance());
        NetSpectatorFightFrame.getInstance().associateFight(null);
        WakfuGameEntity.getInstance().removeFrame(UITimelineFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(NetNotInFightManagementFrame.getInstance());
        PropertiesProvider.getInstance().setPropertyValue("fight", null);
        PropertiesProvider.getInstance().setPropertyValue("fight.timeline", null);
        PropertiesProvider.getInstance().setPropertyValue("isInFightCreationOrPlacement", false);
        PropertiesProvider.getInstance().setPropertyValue("isFightSpectator", false);
        WakfuSoundManager.getInstance().exitFight();
        WakfuGameEntity.getInstance().removeFrame(UITimelineFrame.getInstance());
        final FightInfo fight = FightManager.getInstance().getFightById(FightVisibilityManager.getInstance().getParticipatingFight());
        FightVisibilityManager.getInstance().setParticipatingFight(-1);
        if (fight != null && fight.getStatus() != AbstractFight.FightStatus.DESTRUCTION) {
            FightVisibilityManager.getInstance();
            FightVisibilityManager.deactivateFightObservationView(fight);
            new ToExternalFightMigrator(observedFight).migrateFightToExternal();
        }
        if (fight != null) {
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new LeaveSpectatorModeMessage(fight.getId()));
        }
        FightVisibilityManager.getInstance().updateFightVisibility();
    }
}
