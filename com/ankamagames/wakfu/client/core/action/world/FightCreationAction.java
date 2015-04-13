package com.ankamagames.wakfu.client.core.action.world;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.actionsOperations.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public final class FightCreationAction extends TimedAction
{
    private final Fight m_concernedFight;
    private final FightCreationMessage m_msg;
    
    public FightCreationAction(final int uniqueId, final int actionType, final int actionId, final Fight concernedFight, final FightCreationMessage msg, final CreationActionSequenceOperations creationActionSequenceOperations) {
        super(uniqueId, actionType, actionId);
        this.m_concernedFight = concernedFight;
        this.m_msg = msg;
    }
    
    @Override
    protected long onRun() {
        FightCreationAction.m_logger.info((Object)"CREATION DU COMBAT");
        final FightBuilderFromMessage builder = new FightBuilderFromMessage();
        builder.buildFightFromFightCreationMessage(this.m_msg, this.m_concernedFight);
        this.setupFightCreationActions(this.m_msg);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.changeToSpellAttackIfNecessary();
        final CharacterInfo fighterFromId = this.m_concernedFight.getFighterFromId(this.m_msg.getAttackerId());
        final byte attackerTeamId = this.m_concernedFight.getTeamId(fighterFromId);
        final byte localPlayerTeamid = this.m_concernedFight.getTeamId(localPlayer);
        if (FightModel.getFromTypeId(this.m_msg.getFightType()).isPvp() && attackerTeamId != localPlayerTeamid) {
            final String message = WakfuTranslator.getInstance().getString("pvp.aggroChatMessage", fighterFromId.getName());
            ChatManager.getInstance().pushMessage(message, 4);
        }
        FightVisibilityManager.getInstance().updateFightVisibility();
        return 0L;
    }
    
    private void setupFightCreationActions(final FightCreationMessage msg) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_concernedFight.readTimelineFromBuild(msg.getTimelineSerialized());
        this.m_concernedFight.addCountdown();
        this.m_concernedFight.start();
        PropertiesProvider.getInstance().setPropertyValue("fight", this.m_concernedFight);
        PropertiesProvider.getInstance().setPropertyValue("fight.timeline", this.m_concernedFight.getTimeline());
        PropertiesProvider.getInstance().setPropertyValue("isInFightCreationOrPlacement", true);
        localPlayer.cancelCurrentOccupation(false, false);
        WakfuGameEntity.getInstance().removeFrame(UISeedInteractionFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UICraftTableFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UIManageFleaFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UIBrowseFleaFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UIExchangeMachineFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(NetNotInFightManagementFrame.getInstance());
        try {
            WakfuSoundManager.getInstance().enterFight(this.m_concernedFight);
        }
        catch (Exception e) {
            FightCreationAction.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        if (this.m_concernedFight.isLocked(localPlayer.getTeamId())) {
            final String infoMessage = WakfuTranslator.getInstance().getString("fight.locked.at.creation");
            final ChatMessage chatMessage = new ChatMessage(infoMessage);
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
    }
    
    @Override
    protected void onActionFinished() {
    }
}
