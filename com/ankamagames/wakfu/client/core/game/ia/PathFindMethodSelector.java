package com.ankamagames.wakfu.client.core.game.ia;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;

public final class PathFindMethodSelector
{
    public static FightPathFindMethods selectPathFindMethod(final CharacterInfo player) {
        final Breed breed = player.getBreed();
        final Point3 cellUnderMouse = UIFightFrame.getLastTarget();
        if (breed == AvatarBreed.XELOR && cellUnderMouse != null && isOnPlayerDial(player, player.getPosition()) && isOnPlayerDial(player, cellUnderMouse)) {
            return FightPathFindMethods.WITH_XELORS_DIAL;
        }
        if (cellUnderMouse != null && canUseGatesToMove(player) && isOnSameTeamGate(player, player.getPosition(), cellUnderMouse)) {
            return FightPathFindMethods.WITH_GATES;
        }
        return FightPathFindMethods.WITH_STEAMER_RAILS;
    }
    
    private static boolean canUseGatesToMove(final CharacterInfo player) {
        return !player.isActiveProperty(FightPropertyType.CANNOT_USE_GATE_TO_MOVE);
    }
    
    private static boolean isOnSameTeamGate(final CharacterInfo player, final Point3 position, final Point3 cellUnderMouse) {
        final Fight fight = player.getCurrentFight();
        if (fight == null) {
            return false;
        }
        final BasicEffectAreaManager areaManager = fight.getEffectAreaManager();
        final byte gate1TeamId = getGateTeamIdOnPosition(position, areaManager);
        final byte gate2TeamId = getGateTeamIdOnPosition(cellUnderMouse, areaManager);
        return gate1TeamId != -1 && gate1TeamId == gate2TeamId;
    }
    
    private static byte getGateTeamIdOnPosition(final Point3 position, final BasicEffectAreaManager areaManager) {
        byte gate1TeamId = -1;
        final List<BasicEffectArea> areasOnPosition = areaManager.getEffectAreasListOnPosition(position);
        for (final BasicEffectArea area : areasOnPosition) {
            if (area.getType() == EffectAreaType.GATE.getTypeId()) {
                gate1TeamId = area.getTeamId();
            }
        }
        return gate1TeamId;
    }
    
    private static boolean isOnPlayerDial(final CharacterInfo player, final Point3 p) {
        final Fight fight = player.getCurrentFight();
        if (fight == null) {
            return false;
        }
        final BasicEffectAreaManager areaManager = fight.getEffectAreaManager();
        final List<BasicEffectArea> areasOnPosition = areaManager.getEffectAreasListOnPosition(p);
        for (final BasicEffectArea area : areasOnPosition) {
            if (area.getOwner() != player) {
                continue;
            }
            if (area.getType() == EffectAreaType.HOUR.getTypeId()) {
                return true;
            }
        }
        return false;
    }
}
