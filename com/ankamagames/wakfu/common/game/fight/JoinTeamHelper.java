package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.common.game.protector.*;
import org.jetbrains.annotations.*;

public final class JoinTeamHelper
{
    public static <T extends BasicCharacterInfo> boolean canJoinMonsterTeam(final FightersInformationProvider<T> fight, final T joiningCharacter, final byte teamId) {
        if (joiningCharacter.getType() == 1) {
            return true;
        }
        for (final T fighter : fight.getFighters()) {
            if (fighter.getTeamId() != teamId) {
                continue;
            }
            final byte fighterType = fighter.getType();
            if (fighterType == 1) {
                return false;
            }
        }
        return true;
    }
    
    public static <T extends BasicCharacterInfo> JoinFightResult testJoinNationFight(final BasicFightInfo<T> fight, final T joiningPlayer, final byte teamId) {
        if (fight.getModel().isAllowOnlyOneNationPerTeam()) {
            final Collection<T> fightersInTeam = fight.getFightersInTeam(teamId);
            final Nation nation = NationManager.INSTANCE.getNationById(joiningPlayer.getCitizenComportment().getNationId());
            for (final T fighter : fightersInTeam) {
                if (nation.getDiplomacyManager().getAlignment(fighter.getCitizenComportment().getNationId()) != NationAlignement.ALLIED) {
                    return JoinFightResult.PVP_JOINER_MUST_HAVE_ALLIED_NATION;
                }
            }
        }
        return JoinFightResult.OK;
    }
    
    public static <T extends BasicCharacterInfo> boolean canJoinOpposingPartyTeam(final BasicFightInfo<T> fight, final T player, final byte team) {
        if (fight.getModel().canJoinOpposingPartyTeam()) {
            return true;
        }
        if (!(player instanceof GroupUser)) {
            return true;
        }
        final GroupUser groupUser = (GroupUser)player;
        final Collection<T> fighters = fight.getFighters();
        final long groupId = groupUser.getGroupId(GroupType.PARTY);
        byte groupTeamId = -1;
        for (final T fighter : fighters) {
            if (!(fighter instanceof GroupUser)) {
                continue;
            }
            final long fighterGroupId = ((GroupUser)fighter).getGroupId(GroupType.PARTY);
            if (fighterGroupId != groupId) {
                continue;
            }
            groupTeamId = fighter.getTeamId();
        }
        return groupTeamId == -1 || team == groupTeamId;
    }
    
    public static <T extends BasicCharacterInfo> JoinFightResult canJoinProtectorFight(final BasicFightInfo<T> fight, final T fighter, final byte teamId) {
        if (!fight.getModel().isProtectorFight()) {
            return JoinFightResult.OK;
        }
        ProtectorBase protector = null;
        byte protectorTeamId = -1;
        for (final T f : fight.getFighters()) {
            protector = getProtector(f);
            if (protector != null) {
                protectorTeamId = f.getTeamId();
                break;
            }
        }
        if (protector == null) {
            return JoinFightResult.OK;
        }
        final boolean joinProtectorTeam = teamId == protectorTeamId;
        final boolean playerInAlliedNation = isProtectorAllied(protector, fighter) && protector.getCurrentNationId() != 0;
        final boolean playerInEnemyNation = isProtectorEnemy(protector, fighter) && protector.getCurrentNationId() != 0;
        if (joinProtectorTeam && playerInEnemyNation) {
            return JoinFightResult.CANNOT_DEFEND_ENEMY_PROTECTOR;
        }
        if (!joinProtectorTeam && playerInAlliedNation) {
            return JoinFightResult.CANNOT_ATTACK_ALLIED_PROTECTOR;
        }
        return JoinFightResult.OK;
    }
    
    private static <T extends BasicCharacterInfo> boolean isProtectorAllied(final ProtectorBase protector, final T fighter) {
        final NationAlignement nationAlignement = fighter.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(protector.getCurrentNationId());
        return nationAlignement == NationAlignement.ALLIED;
    }
    
    private static <T extends BasicCharacterInfo> boolean isProtectorEnemy(final ProtectorBase protector, final T fighter) {
        final NationAlignement nationAlignement = fighter.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(protector.getCurrentNationId());
        return nationAlignement == NationAlignement.ENEMY;
    }
    
    @Nullable
    private static <T extends BasicCharacterInfo> ProtectorBase getProtector(final T fighter) {
        if (fighter.getType() != 1) {
            return null;
        }
        return fighter.getProtector();
    }
}
