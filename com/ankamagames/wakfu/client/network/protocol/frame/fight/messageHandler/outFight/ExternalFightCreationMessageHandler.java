package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class ExternalFightCreationMessageHandler extends UsingFightMessageHandler<ExternalFightCreationMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    private static final boolean DEBUG = false;
    
    @Override
    public boolean onMessage(final ExternalFightCreationMessage msg) {
        final FightModel fightModel = FightModel.getFromTypeId(msg.getFightType());
        if (fightModel == null) {
            ExternalFightCreationMessageHandler.m_logger.error((Object)String.format("FightModel (typeId=%d) inconnu dans la cr\u00e9ation de l'external fight id=%d", msg.getFightType(), msg.getFightId()));
            return false;
        }
        final ExternalFightInfo externalFightInfo = new ExternalFightInfo(fightModel);
        final FightMap fightMap = new WakfuClientFightMap();
        new FightMapSerializer(fightMap).unserialize(ByteBuffer.wrap(msg.getSerializedFightMap()));
        externalFightInfo.setId(msg.getFightId());
        externalFightInfo.setStatus(msg.getFightStatus());
        externalFightInfo.setPartition(msg.getPartition());
        externalFightInfo.setFightMap(fightMap);
        fightMap.blockFightingGroundInTopology(true, false);
        final boolean displayEffect = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() == null;
        for (int i = 0; i < msg.getFightersCount(); ++i) {
            final byte teamId = msg.getTeamId(i);
            final long fighterId = msg.getFighterId(i);
            externalFightInfo.addFighterToTeamById(teamId, fighterId);
        }
        ExternalFightCreationActions.INSTANCE.m_fightIsInFightCreation.add(msg.getFightId());
        externalFightInfo.setAttackerCreatorId(msg.getCreators().get(0));
        externalFightInfo.setDefenderCreatorId(msg.getCreators().get(1));
        final CharacterInfo attackerCreator = externalFightInfo.getFighterFromId((long)msg.getCreators().get(0));
        final CharacterInfo defenderCreator = externalFightInfo.getFighterFromId((long)msg.getCreators().get(1));
        if (attackerCreator != null && defenderCreator != null) {
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions1.put(msg.getFightId(), LookAtTheFoeAction.checkout(1, FightActionType.LOOK_AT_THE_FOE.getId(), 0, attackerCreator, defenderCreator));
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions2.put(msg.getFightId(), LookAtTheFoeAction.checkout(2, FightActionType.LOOK_AT_THE_FOE.getId(), 0, defenderCreator, attackerCreator));
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions3.put(msg.getFightId(), LookAtTheFoeAction.checkout(3, FightActionType.LOOK_AT_THE_FOE.getId(), 0, externalFightInfo.getFightersInTeam(attackerCreator.getTeamId()), defenderCreator));
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions4.put(msg.getFightId(), LookAtTheFoeAction.checkout(4, FightActionType.LOOK_AT_THE_FOE.getId(), 0, externalFightInfo.getFightersInTeam(defenderCreator.getTeamId()), attackerCreator));
            ExternalFightCreationActions.INSTANCE.m_playAnimationAction2.put(msg.getFightId(), PlayAnimationAction.checkout(6, FightActionType.PLAY_ANIMATION.getId(), 0, defenderCreator, "AnimHit", 250));
            if (displayEffect) {
                ExternalFightCreationActions.INSTANCE.m_tauntAction1.put(msg.getFightId(), TauntAction.checkout(10, FightActionType.TAUNT.getId(), 0, defenderCreator));
                final Collection<CharacterInfo> fightersWhoTaunt = new HashSet<CharacterInfo>(externalFightInfo.getFightersInTeam(defenderCreator.getTeamId()));
                fightersWhoTaunt.remove(defenderCreator);
                ExternalFightCreationActions.INSTANCE.m_tauntAction2.put(msg.getFightId(), TauntAction.checkout(10, FightActionType.TAUNT.getId(), 0, fightersWhoTaunt));
            }
            externalFightInfo.setAttackerCreator(attackerCreator);
            externalFightInfo.setDefenderCreator(defenderCreator);
        }
        if (displayEffect) {
            final AbstractBattlegroundBorderEffectArea baseArea = StaticEffectAreaManager.getInstance().getBorderCellArea(msg.getBattlegroundBorderEffectAreaBaseId());
            if (baseArea != null) {
                final AbstractBattlegroundBorderEffectArea area = (AbstractBattlegroundBorderEffectArea)baseArea.instanceAnother(new EffectAreaParameters(msg.getBattlegroundBorderEffectAreaUID(), fightMap.getMinX(), fightMap.getMinY(), fightMap.getMinZ(), externalFightInfo.getContext(), null, (short)0, Direction8.NONE));
                area.initialize(fightMap);
                externalFightInfo.getEffectAreaManager().addEffectArea(area);
            }
            else {
                ExternalFightCreationMessageHandler.m_logger.error((Object)("Impossible de cr\u00e9er la bulle de combat : la zone d'effet d'index " + msg.getBattlegroundBorderEffectAreaBaseId() + " n'existe pas"));
            }
            ((ExternalFightCellLightModifier)externalFightInfo.getCellLightModifier()).setFightMap(fightMap);
        }
        FightManager.getInstance().addFight(externalFightInfo);
        LocalPartitionManager.getInstance().addExternalFight(externalFightInfo);
        if (attackerCreator != null && defenderCreator != null) {
            FightVisibilityManager.getInstance().onExternalFightCreation(externalFightInfo);
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExternalFightCreationMessageHandler.class);
    }
}
