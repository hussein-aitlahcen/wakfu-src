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

public class XelorsDialFightPathFindMethod extends TackleAwareFightPathFindMethod
{
    public static final Logger m_logger;
    public static final FightPathFindMethod INSTANCE;
    
    @Override
    public ClientPathFindResult findPath(final CharacterInfo currentFighter, final CharacterActor actor, final int availableMovementPoints) {
        if (availableMovementPoints > 0) {
            final Point3 hourOkForTeleportPosition = this.getHourOkForTeleportPosition(currentFighter, actor);
            if (hourOkForTeleportPosition != null) {
                final int[][] path = new int[1][3];
                path[0][0] = hourOkForTeleportPosition.getX();
                path[0][1] = hourOkForTeleportPosition.getY();
                path[0][2] = hourOkForTeleportPosition.getZ();
                return new ClientPathFindResult(new PathFindResult(path), false);
            }
        }
        return super.findPath(currentFighter, actor, availableMovementPoints);
    }
    
    private Point3 getHourOkForTeleportPosition(final CharacterInfo currentFighter, final CharacterActor actor) {
        final Fight currentFight = currentFighter.getCurrentFight();
        if (!this.isCasterOnOwnHour(currentFight, currentFighter)) {
            return null;
        }
        return this.getHourUnderMousePosition(currentFight, currentFighter, actor);
    }
    
    private Point3 getHourUnderMousePosition(final Fight fight, final CharacterInfo currentFighter, final CharacterActor actor) {
        final Point3 lastTarget = UIFightFrame.getLastTarget();
        if (this.checkOwnHourOnPosition(fight, currentFighter, lastTarget.getX(), lastTarget.getY(), lastTarget.getZ())) {
            return new Point3(lastTarget);
        }
        return null;
    }
    
    private boolean isCasterOnOwnHour(final Fight fight, final CharacterInfo currentFighter) {
        final Point3 positionConst = currentFighter.getPositionConst();
        return this.checkOwnHourOnPosition(fight, currentFighter, positionConst.getX(), positionConst.getY(), positionConst.getZ());
    }
    
    private boolean checkOwnHourOnPosition(final Fight fight, final CharacterInfo owner, final int posX, final int posY, final short posZ) {
        final Collection<BasicEffectArea> activeEffectAreas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : activeEffectAreas) {
            if (area.getOwner() != owner) {
                continue;
            }
            if (area.getType() != EffectAreaType.HOUR.getTypeId()) {
                continue;
            }
            if (area.contains(posX, posY, posZ)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)XelorsDialFightPathFindMethod.class);
        INSTANCE = new XelorsDialFightPathFindMethod();
    }
}
