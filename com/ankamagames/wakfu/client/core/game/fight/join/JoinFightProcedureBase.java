package com.ankamagames.wakfu.client.core.game.fight.join;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.join.team.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.jetbrains.annotations.*;

abstract class JoinFightProcedureBase implements JoinFightProcedure
{
    private static final Logger m_logger;
    private final FightJoinTeamValidatorFactory m_joinTeamValidatorFactory;
    @NotNull
    final FightInfo m_fight;
    @NotNull
    final LocalPlayerCharacter m_player;
    Byte m_joinedTeamId;
    CharacterInfo m_joinedAlly;
    boolean m_suppressMessages;
    private boolean m_suppressQueries;
    private static final int MAX_PATH_LENGTH = 20;
    private boolean m_joinProtectorAttack;
    
    JoinFightProcedureBase(@NotNull final FightInfo fight, @NotNull final LocalPlayerCharacter player) {
        super();
        this.m_joinTeamValidatorFactory = new FightJoinTeamValidatorFactory();
        this.m_suppressMessages = false;
        this.m_fight = fight;
        this.m_player = player;
    }
    
    @Override
    public final JoinFightResult tryJoinFight() {
        final JoinFightResult canFightBeJoined = this.canFightBeJoined();
        if (canFightBeJoined != JoinFightResult.OK) {
            return canFightBeJoined;
        }
        final JoinFightResult canPlayerJoin = this.canPlayerJoin();
        if (canPlayerJoin != JoinFightResult.OK) {
            return canPlayerJoin;
        }
        return this.tryJoinFightCore();
    }
    
    @Override
    public final JoinFightResult canJoinFight() {
        this.m_suppressMessages = true;
        try {
            return this.tryJoinFight();
        }
        finally {
            this.m_suppressMessages = false;
        }
    }
    
    @Override
    public final void suppressQueries() {
        this.m_suppressQueries = true;
    }
    
    abstract JoinFightResult tryJoinFightCore();
    
    void setJoinedAlly(@NotNull final CharacterInfo joinedAlly) {
        this.m_joinedAlly = joinedAlly;
        if (this.m_joinedTeamId == null) {
            this.m_joinedTeamId = this.m_joinedAlly.getTeamId();
        }
    }
    
    JoinFightResult joinAlly() {
        final JoinFightResult verifyTeam = this.m_joinTeamValidatorFactory.getValidator(this.m_fight, this.m_joinedTeamId).canJoinTeam();
        if (verifyTeam != JoinFightResult.OK) {
            return verifyTeam;
        }
        final JoinFightResult result = this.canAllyBeJoined();
        if (result != JoinFightResult.OK) {
            return result;
        }
        if (!this.m_suppressQueries) {
            final ProtectorAssaultCheck protectorCheck = new ProtectorAssaultCheck();
            if (protectorCheck.joiningAgainstProtector()) {
                this.m_joinProtectorAttack = true;
                return JoinFightResult.OK;
            }
        }
        return this.joinVerifiedAlly();
    }
    
    JoinFightResult autoJoinFight() {
        final JoinFightResult findTeamResult = this.findTeam();
        if (findTeamResult != JoinFightResult.OK) {
            return findTeamResult;
        }
        return this.joinTeam();
    }
    
    private JoinFightResult joinVerifiedAlly() {
        if (!this.m_suppressMessages) {
            final boolean locked = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY);
            final Message joinMessage = new FightJoinRequestMessage(this.m_joinedAlly.getId(), locked);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(joinMessage);
        }
        return JoinFightResult.OK;
    }
    
    private JoinFightResult findTeam() {
        final TByteArrayList joinableTeams = new TByteArrayList();
        for (byte teamId = 0; teamId < this.m_fight.getModel().getMaxTeam(); ++teamId) {
            if (this.m_joinTeamValidatorFactory.getValidator(this.m_fight, teamId).canJoinTeam() == JoinFightResult.OK && !joinableTeams.contains(teamId)) {
                joinableTeams.add(teamId);
            }
        }
        if (joinableTeams.size() != 1) {
            return JoinFightResult.CANNOT_AUTO_JOIN;
        }
        this.m_joinedTeamId = joinableTeams.getQuick(0);
        return JoinFightResult.OK;
    }
    
    private JoinFightResult joinTeam() {
        final Collection<CharacterInfo> fighters = this.m_fight.getFighters();
        for (final CharacterInfo fighter : fighters) {
            if (fighter.getTeamId() != this.m_joinedTeamId) {
                continue;
            }
            this.m_joinedAlly = fighter;
            final JoinFightResult joinResult = this.joinAlly();
            if (joinResult == JoinFightResult.OK) {
                return joinResult;
            }
        }
        return JoinFightResult.CANNOT_AUTO_JOIN;
    }
    
    private JoinFightResult canPlayerJoin() {
        if (this.m_player.isOnFight()) {
            return JoinFightResult.ALREADY_IN_FIGHT;
        }
        if (this.m_player.getPropertyValue(WorldPropertyType.CANT_ATTACK) > 0) {
            return JoinFightResult.JOINER_CANT_ATTACK;
        }
        if (this.m_player.isWaitingForResult()) {
            return JoinFightResult.CANT_JOIN_WHILE_ENTERING_DIM_BAG;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return JoinFightResult.CANT_JOIN_WHILE_IN_TRADE;
        }
        return JoinFightResult.OK;
    }
    
    private JoinFightResult canFightBeJoined() {
        final AbstractFight.FightStatus status = this.m_fight.getStatus();
        if (status != AbstractFight.FightStatus.PLACEMENT && status != AbstractFight.FightStatus.CREATION) {
            return JoinFightResult.FIGHT_NOT_IN_PLACEMENT_PHASE;
        }
        return JoinFightResult.OK;
    }
    
    private JoinFightResult canAllyBeJoined() {
        if (!this.m_joinedAlly.isOnFight()) {
            return JoinFightResult.TARGET_NOT_IN_FIGHT;
        }
        if (this.m_joinedAlly.getPropertyValue(WorldPropertyType.CANT_BE_JOINED) > 0) {
            return JoinFightResult.TARGET_CANT_BE_JOINED;
        }
        final int pathFindResult = this.canPlayerWalkToJoinedAlly(20);
        if (pathFindResult != 0) {
            return JoinFightResult.PATHFINDER_ERROR;
        }
        return JoinFightResult.OK;
    }
    
    private int canPlayerWalkToJoinedAlly(final int maxPathLength) {
        final CharacterActor localActor = this.m_player.getActor();
        final int fromX = localActor.getCurrentWorldX();
        final int fromY = localActor.getCurrentWorldY();
        final short fromZ = localActor.getCurrentAltitude();
        final int toX = this.m_joinedAlly.getWorldCellX();
        final int toY = this.m_joinedAlly.getWorldCellY();
        final short toZ = this.m_joinedAlly.getWorldCellAltitude();
        final PathFinderParameters parameters = new PathFinderParameters();
        parameters.m_searchLimit = maxPathLength * maxPathLength;
        parameters.m_stopBeforeEndCell = true;
        parameters.m_limitTo4Directions = false;
        parameters.m_allowGap = false;
        final TopologyMapInstanceSet mapSet = new TopologyMapInstanceSet();
        TopologyMapManager.getTopologyMapInstances(fromX, fromY, toX, toY, mapSet);
        final PathFinder pathFinder = PathFinder.checkOut();
        pathFinder.addStartCell(fromX, fromY, fromZ);
        pathFinder.setStopCell(toX, toY, toZ);
        pathFinder.setMoverCaracteristics(localActor.getHeight(), localActor.getPhysicalRadius(), localActor.getJumpCapacity());
        pathFinder.setParameters(parameters);
        pathFinder.setTopologyMapInstanceSet(mapSet);
        final int pathLength = pathFinder.findPath();
        pathFinder.release();
        if (pathLength == -1) {
            return 1026;
        }
        if (pathLength >= maxPathLength) {
            return 1042;
        }
        return 0;
    }
    
    @Override
    public boolean isJoinProtectorAttack() {
        return this.m_joinProtectorAttack;
    }
    
    static {
        m_logger = Logger.getLogger((Class)JoinFightProcedureBase.class);
    }
    
    private class ProtectorAssaultCheck
    {
        Protector m_protector;
        
        public boolean joiningAgainstProtector() {
            if (JoinFightProcedureBase.this.m_fight.getModel() != FightModel.PROTECTOR_ASSAULT) {
                return false;
            }
            this.m_protector = this.getProtector();
            if (this.m_protector == null) {
                JoinFightProcedureBase.m_logger.error((Object)String.format("Fight id=%d de type %s sans protecteur.", JoinFightProcedureBase.this.m_fight.getId(), FightModel.PROTECTOR_ASSAULT));
                return false;
            }
            return this.m_protector.getNpc().getTeamId() == 1 - JoinFightProcedureBase.this.m_joinedTeamId;
        }
        
        private Protector getProtector() {
            for (final CharacterInfo fighter : JoinFightProcedureBase.this.m_fight.getFighters()) {
                final Protector protector = this.getProtector(fighter);
                if (protector != null) {
                    return protector;
                }
            }
            return null;
        }
        
        @Nullable
        private Protector getProtector(final CharacterInfo fighter) {
            if (fighter.getType() != 1 || !(fighter instanceof NonPlayerCharacter)) {
                return null;
            }
            final NonPlayerCharacter nonPlayerCharacter = (NonPlayerCharacter)fighter;
            return nonPlayerCharacter.getProtector();
        }
    }
}
