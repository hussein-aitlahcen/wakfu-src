package com.ankamagames.wakfu.client.core.game.fight.spectator;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public final class CreateFightForSpectatorProcedure
{
    public static void createFight(final FightCreationForSpectatorMessage msg) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.cancelCurrentOccupation(false, false);
        FightManager.getInstance().destroyFight(msg.getFightId());
        final Fight fight = buildFight(msg);
        localPlayer.setObservedFight(fight);
        FightVisibilityManager.getInstance().setParticipatingFight(fight.getId());
        FightVisibilityManager.getInstance().updateFightVisibility();
        removeFrames();
        fight.addCountdown();
        fight.start();
        fillXulorProperties(fight);
        NetSpectatorFightFrame.getInstance().associateFight(fight);
        addFrames();
        WakfuSoundManager.getInstance().enterFight(fight);
        WakfuSoundManager.getInstance().enterAction();
    }
    
    private static Fight buildFight(final FightCreationForSpectatorMessage msg) {
        final FightMap fightMap = new WakfuClientFightMap();
        new FightMapSerializer(fightMap).unserialize(ByteBuffer.wrap(msg.getSerializedMap()));
        final Fight fight = FightManager.getInstance().createFight(msg.getFightType(), msg.getFightId(), fightMap, msg.getLockedTeams(), msg.getFightStatus());
        final FightBuilderFromMessage builder = new FightBuilderFromMessage();
        builder.buildFightFromFightCreationMessage(msg, fight);
        return fight;
    }
    
    private static void addFrames() {
        final List<MessageFrame> framesToAdd = getFramesToAddAtCreationAndRemoveAtLeave();
        for (int i = 0; i < framesToAdd.size(); ++i) {
            final MessageFrame messageFrame = framesToAdd.get(i);
            WakfuGameEntity.getInstance().pushFrame(messageFrame);
        }
    }
    
    private static void removeFrames() {
        WakfuGameEntity.getInstance().removeFrame(UISeedInteractionFrame.getInstance());
        final List<MessageFrame> framesToRemove = getFramesToRemoveAtCreationAndAddAtLeave();
        for (int i = 0; i < framesToRemove.size(); ++i) {
            final MessageFrame frame = framesToRemove.get(i);
            WakfuGameEntity.getInstance().removeFrame(frame);
        }
    }
    
    private static void fillXulorProperties(final Fight fight) {
        PropertiesProvider.getInstance().setPropertyValue("fight", fight);
        PropertiesProvider.getInstance().setPropertyValue("fight.timeline", fight.getTimeline());
        PropertiesProvider.getInstance().setPropertyValue("isInFightCreationOrPlacement", true);
        PropertiesProvider.getInstance().setPropertyValue("isFightSpectator", true);
    }
    
    public static List<MessageFrame> getFramesToRemoveAtCreationAndAddAtLeave() {
        final List<MessageFrame> res = new ArrayList<MessageFrame>();
        res.add(NetNotInFightManagementFrame.getInstance());
        return res;
    }
    
    public static List<MessageFrame> getFramesToAddAtCreationAndRemoveAtLeave() {
        final List<MessageFrame> res = new ArrayList<MessageFrame>();
        res.add(UITimelineFrame.getInstance());
        res.add(NetSpectatorFightFrame.getInstance());
        return res;
    }
}
