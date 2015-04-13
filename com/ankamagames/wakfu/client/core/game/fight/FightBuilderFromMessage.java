package com.ankamagames.wakfu.client.core.game.fight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.reconnection.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import gnu.trove.*;
import java.util.*;

public final class FightBuilderFromMessage
{
    private static Logger m_logger;
    private Fight m_concernedFight;
    
    public Fight buildFightFromFightCreationMessage(final FightCreationMessage msg, final Fight fight) {
        this.m_concernedFight = fight;
        final FightCreationMessage.EffectUserID[] effectUserIDs = msg.getAdditionnalEffectUsers();
        for (int i = 0; i < effectUserIDs.length; ++i) {
            if (effectUserIDs[i].m_typeId == 10) {
                final ClientMapInteractiveElement interactiveElement = LocalPartitionManager.getInstance().getInteractiveElement(effectUserIDs[i].m_id);
                if (interactiveElement == null) {
                    FightBuilderFromMessage.m_logger.error((Object)("IE non trouv\u00e9 " + effectUserIDs[i].m_id));
                }
                else {
                    this.m_concernedFight.tryPutAdditionalTarget((FightObstacle)interactiveElement);
                }
            }
        }
        final AbstractBattlegroundBorderEffectArea baseArea = StaticEffectAreaManager.getInstance().getBorderCellArea(msg.getBattlegroundBorderEffectAreaBaseId());
        assert baseArea != null : "Impossible de cr\u00e9er la bulle de combat : la zone d'effet d'index " + msg.getBattlegroundBorderEffectAreaBaseId() + " n'existe pas";
        final FightMap fightMap = this.m_concernedFight.getFightMap();
        final AbstractBattlegroundBorderEffectArea area = (AbstractBattlegroundBorderEffectArea)baseArea.instanceAnother(new EffectAreaParameters(msg.getBattlegroundBorderEffectAreaUID(), fightMap.getMinX(), fightMap.getMinY(), fightMap.getMinZ(), this.m_concernedFight.getContext(), null, (short)0, Direction8.NONE));
        area.initialize(fightMap);
        this.m_concernedFight.setBattlegroundBorderEffectArea(area);
        final TeamSetup teamSetup = this.readTeamSetup(msg);
        this.readAndAddFighters(msg, teamSetup);
        this.m_concernedFight.readTimelineFromBuild(msg.getTimelineSerialized());
        recoverEffectAreas(msg.getSerializedEffectArea(), this.m_concernedFight);
        return this.m_concernedFight;
    }
    
    public static void recoverEffectAreas(final List<byte[]> serializedEffectArea, final Fight concernedFight) {
        for (final byte[] bytes : serializedEffectArea) {
            try {
                final ByteBuffer bb = ByteBuffer.wrap(bytes);
                final long baseId = bb.getLong();
                final AbstractEffectArea area = StaticEffectAreaManager.getInstance().getAreaFromId(baseId);
                if (area == null) {
                    FightManager.m_logger.error((Object)("Impossible de recr\u00e9er la zone d'id " + baseId));
                }
                else {
                    final EffectAreaParameters parameters = EffectAreaSerializerForReconnection.createBaseEffectAreaParameters(bb, concernedFight);
                    AbstractEffectArea effectArea = (AbstractEffectArea)concernedFight.getEffectAreaManager().getActiveEffectAreaWithId(parameters.getId());
                    if (effectArea == null) {
                        effectArea = area.instanceAnother(parameters);
                    }
                    effectArea.initialize();
                    EffectAreaSerializerForReconnection.unserializeInfoForAfterInit(effectArea, bb);
                    concernedFight.getEffectAreaManager().addEffectArea(effectArea);
                    StaticEffectAreaDisplayer.getInstance().update();
                }
            }
            catch (Exception e) {
                FightManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private TeamSetup readTeamSetup(final FightCreationMessage msg) {
        final TeamSetup teamSetup = new TeamSetup();
        for (int i = 0; i < msg.getFightersCount(); ++i) {
            final byte teamId = msg.getTeamId(i);
            final long fighterId = msg.getFighterId(i);
            final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(fighterId);
            if (fighter == null) {
                FightBuilderFromMessage.m_logger.error((Object)("Impossible d'ajouter le joueur " + fighterId + " au combat " + msg.getFightId() + " : ce fighter n'existe pas"));
            }
            else if (fighterId == msg.getAttackerId() || fighterId == msg.getDefenderId()) {
                teamSetup.addFighterToTeamListAsLeader(fighter, teamId);
            }
            else {
                teamSetup.addFighterToTeamList(fighter, teamId);
            }
        }
        return teamSetup;
    }
    
    private void readAndAddFighters(final FightCreationMessage msg, final TeamSetup teamSetup) {
        for (final byte teamId : new byte[] { 0, 1 }) {
            if (!this.m_concernedFight.addFightersTeam(teamId, teamSetup.getTeamList(teamId), false)) {
                FightBuilderFromMessage.m_logger.error((Object)"Impossible d'ajouter une team au combat, client d\u00e9synchronis\u00e9 avec la demande serveur");
            }
        }
        for (int i = 0; i < msg.getFightersCount(); ++i) {
            final CharacterInfo fighter = this.m_concernedFight.getFighterFromId(msg.getFighterId(i));
            final byte[] serializedFighterDatas = msg.getSerializedFighterDatas(i);
            final byte[] serializedEffectUsersDatas = msg.getSerializedEffectUsersDatas(i);
            if (fighter != null) {
                fighter.setSpawnInMyFight(true);
                if (!msg.isForReconnection()) {
                    fighter.reloadCharacterForFight(this.m_concernedFight, serializedFighterDatas, serializedEffectUsersDatas);
                }
            }
            final byte playStateId = msg.getPlayStateId(i);
            if (FighterPlayState.fromId(playStateId) == FighterPlayState.OFF_PLAY) {
                this.m_concernedFight.putFighterOffPlay(fighter);
            }
            else if (FighterPlayState.fromId(playStateId) == FighterPlayState.OUT_OF_PLAY) {
                this.m_concernedFight.putFighterOffPlay(fighter);
                this.m_concernedFight.putFighterOutOfPlay(fighter);
            }
        }
    }
    
    static {
        FightBuilderFromMessage.m_logger = Logger.getLogger((Class)FightBuilderFromMessage.class);
    }
    
    private static class TeamSetup
    {
        private final TByteObjectHashMap<List<CharacterInfo>> m_teamLists;
        
        private TeamSetup() {
            super();
            this.m_teamLists = new TByteObjectHashMap<List<CharacterInfo>>();
            for (byte b = 0; b < 2; ++b) {
                this.m_teamLists.put(b, new ArrayList<CharacterInfo>());
            }
        }
        
        List<CharacterInfo> getTeamList(final byte teamId) {
            return this.m_teamLists.get(teamId);
        }
        
        void addFighterToTeamList(final CharacterInfo fighter, final byte teamId) {
            this.m_teamLists.get(teamId).add(fighter);
        }
        
        void addFighterToTeamListAsLeader(final CharacterInfo fighter, final byte teamId) {
            this.m_teamLists.get(teamId).add(0, fighter);
        }
    }
}
