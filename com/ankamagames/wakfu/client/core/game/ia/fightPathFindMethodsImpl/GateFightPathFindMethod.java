package com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.ia.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;

public class GateFightPathFindMethod extends TackleAwareFightPathFindMethod
{
    public static final Logger m_logger;
    public static final FightPathFindMethod INSTANCE;
    
    @Override
    public ClientPathFindResult findPath(final CharacterInfo currentFighter, final CharacterActor actor, final int availableMovementPoints) {
        if (availableMovementPoints > 0) {
            final Point3 gateOkForTeleportPosition = this.getGateOkForTeleportPosition(currentFighter, actor);
            if (gateOkForTeleportPosition != null) {
                final int[][] path = new int[1][3];
                path[0][0] = gateOkForTeleportPosition.getX();
                path[0][1] = gateOkForTeleportPosition.getY();
                path[0][2] = gateOkForTeleportPosition.getZ();
                return new ClientPathFindResult(new PathFindResult(path), false);
            }
        }
        return super.findPath(currentFighter, actor, availableMovementPoints);
    }
    
    private Point3 getGateOkForTeleportPosition(final CharacterInfo currentFighter, final CharacterActor actor) {
        final Fight currentFight = currentFighter.getCurrentFight();
        final byte startGateTeamId = this.getGateTeamIdOnPosition(currentFight, currentFighter);
        if (startGateTeamId == -1) {
            return null;
        }
        return this.getSameTeamGateUnderMousePosition(currentFight, startGateTeamId);
    }
    
    private Point3 getSameTeamGateUnderMousePosition(final Fight fight, final byte startGateTeamId) {
        final Point3 lastTarget = UIFightFrame.getLastTarget();
        if (this.getGateTeamIdOnPosition(fight, lastTarget.getX(), lastTarget.getY(), lastTarget.getZ()) == startGateTeamId) {
            return new Point3(lastTarget);
        }
        return null;
    }
    
    private byte getGateTeamIdOnPosition(final Fight fight, final CharacterInfo currentFighter) {
        final Point3 positionConst = currentFighter.getPositionConst();
        return this.getGateTeamIdOnPosition(fight, positionConst.getX(), positionConst.getY(), positionConst.getZ());
    }
    
    private byte getGateTeamIdOnPosition(final Fight fight, final int posX, final int posY, final short posZ) {
        final Collection<BasicEffectArea> activeEffectAreas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : activeEffectAreas) {
            if (area.getType() != EffectAreaType.GATE.getTypeId()) {
                continue;
            }
            if (area.contains(posX, posY, posZ)) {
                return area.getTeamId();
            }
        }
        return -1;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GateFightPathFindMethod.class);
        INSTANCE = new GateFightPathFindMethod();
    }
}
