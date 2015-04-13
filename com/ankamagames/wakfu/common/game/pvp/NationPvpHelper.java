package com.ankamagames.wakfu.common.game.pvp;

import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.government.*;

public final class NationPvpHelper
{
    private static final int PVP_MAX_LEVEL_DIFFERENCE = 50;
    
    public static boolean isPvpActive(final CitizenComportment citizen) {
        return citizen.getPvpState().isActive();
    }
    
    public static boolean canTogglePvp(final PvpUser citizen, final boolean on) {
        final CitizenComportment citizenComportment = citizen.getCitizenComportment();
        if (citizenComportment.getCitizenScoreForNation(citizenComportment.getNationId()) < 0) {
            return false;
        }
        if (testPlayerCanDoRankedNationPvp(citizen) != JoinFightResult.OK) {
            return !on;
        }
        switch (citizenComportment.getPvpState()) {
            case PVP_OFF:
            case PVP_ON: {
                return true;
            }
            default: {
                final GameDateConst pvpStartDate = citizenComportment.getPvpDate();
                if (pvpStartDate.isNull()) {
                    return true;
                }
                final GameInterval timeElapsed = pvpStartDate.timeTo(WakfuGameCalendar.getInstance().getDate());
                return timeElapsed.greaterThan(NationPvpConstants.PVP_LEAVE_DURATION);
            }
        }
    }
    
    public static JoinFightResult testPlayersCanDoRegularPvp(final PvpUser initiatingPlayer, final PvpUser targetPlayer) {
        if (initiatingPlayer.getLevel() < targetPlayer.getLevel() - 50) {
            return JoinFightResult.PVP_INITIATOR_HAS_TOO_BIG_LEVEL_DIFFERENCE;
        }
        final JoinFightResult guildResult = testCantFightGuildMates(initiatingPlayer, targetPlayer);
        if (guildResult != JoinFightResult.OK) {
            return guildResult;
        }
        return testPlayerDoRankedNationPvpNow(initiatingPlayer, true);
    }
    
    public static JoinFightResult testPlayersCanDoRankedNationPvp(final PvpUser initiatingPlayer, final PvpUser targetPlayer) {
        final JoinFightResult resultInitiating = testPlayerDoRankedNationPvpNow(initiatingPlayer, true);
        if (resultInitiating != JoinFightResult.OK) {
            return resultInitiating;
        }
        final JoinFightResult resultTarget = testPlayerDoRankedNationPvpNow(targetPlayer, false);
        if (resultTarget != JoinFightResult.OK) {
            return resultTarget;
        }
        if (initiatingPlayer.getLevel() <= targetPlayer.getLevel() - 50) {
            return JoinFightResult.PVP_INITIATOR_HAS_TOO_BIG_LEVEL_DIFFERENCE;
        }
        final JoinFightResult guildResult = testCantFightGuildMates(initiatingPlayer, targetPlayer);
        if (guildResult != JoinFightResult.OK) {
            return guildResult;
        }
        final Nation initiatingPlayerNation = NationManager.INSTANCE.getNationById(initiatingPlayer.getCitizenComportment().getNationId());
        final Nation targetPlayerNation = NationManager.INSTANCE.getNationById(targetPlayer.getCitizenComportment().getNationId());
        if (initiatingPlayerNation.getDiplomacyManager().getAlignment(targetPlayerNation.getNationId()) != NationAlignement.ENEMY) {
            return JoinFightResult.PVP_JOINER_MUST_HAVE_ALLIED_NATION;
        }
        return JoinFightResult.OK;
    }
    
    public static <T extends BasicCharacterInfo> JoinFightResult testPlayerCanJoinPvpFightOf(final BasicFightInfo<T> fight, final T fighter, final byte teamId) {
        if (!fight.getModel().isPvp()) {
            return JoinFightResult.OK;
        }
        if (playerNeedsPvpTagToJoin(fight, teamId)) {
            final JoinFightResult joinResult = testPlayerCanDoRankedNationPvp(fighter);
            if (joinResult != JoinFightResult.OK) {
                return joinResult;
            }
        }
        final Nation joiningNation = NationManager.INSTANCE.getNationById(fighter.getCitizenComportment().getNationId());
        final Collection<T> fightersInTeam = fight.getFightersInTeam(teamId);
        for (final T f : fightersInTeam) {
            final Nation nation = f.getCitizenComportment().getNation();
            if (nation != Nation.VOID_NATION && joiningNation.getDiplomacyManager().getAlignment(nation.getNationId()) != NationAlignement.ALLIED) {
                return JoinFightResult.PVP_JOINER_MUST_HAVE_ALLIED_NATION;
            }
        }
        return JoinFightResult.OK;
    }
    
    public static <T extends BasicCharacterInfo> boolean playerNeedsPvpTagToJoin(final BasicFightInfo<T> fight, final byte teamId) {
        final byte initiatingTeamId = fight.getInitiatingTeamId();
        final boolean attacking = initiatingTeamId == teamId;
        return attacking ? fight.getModel().isAttackerNeedPvpTag() : fight.getModel().isDefenderNeedPvpTag();
    }
    
    private static JoinFightResult testPlayerDoRankedNationPvpNow(final PvpUser pc, final boolean attacking) {
        if (attacking) {
            final JoinFightResult result = testPlayerCanDoRankedNationPvp(pc);
            if (result != JoinFightResult.OK) {
                return result;
            }
        }
        final NationPvpState pvpState = pc.getCitizenComportment().getPvpState();
        if (pvpState == NationPvpState.PVP_OFF) {
            return JoinFightResult.PVP_TAG_NOT_ACTIVATED;
        }
        if (pvpState == NationPvpState.PVP_STARTING) {
            return attacking ? JoinFightResult.PVP_TAG_NOT_ACTIVE_YET : JoinFightResult.OK;
        }
        return JoinFightResult.OK;
    }
    
    public static JoinFightResult testPlayerCanDoRankedNationPvp(final PvpUser pc) {
        if (pc.getCitizenComportment().getNationId() == 34) {
            return JoinFightResult.OK;
        }
        if (!pc.getCitizenComportment().hasJob(NationJob.GUARD)) {
            return JoinFightResult.PVP_MUST_BE_GUARD;
        }
        if (pc.getLevel() < 38) {
            return JoinFightResult.PVP_MUST_HAVE_MIN_LEVEL;
        }
        if (pc.getGuildId() > 0L) {
            if (pc.getGuildNationId() == 0) {
                return JoinFightResult.PVP_NOT_SAME_NATION_AS_GUILD;
            }
            final Nation nation = NationManager.INSTANCE.getNationById(pc.getGuildNationId());
            if (nation.getDiplomacyManager().getAlignment(pc.getCitizenComportment().getNationId()) != NationAlignement.ALLIED) {
                return JoinFightResult.PVP_NOT_SAME_NATION_AS_GUILD;
            }
        }
        return JoinFightResult.OK;
    }
    
    public static JoinFightResult testCantFightGuildMates(final PvpUser initiating, final PvpUser target) {
        final long initiatingGuildId = initiating.getGuildId();
        final long targetGuildId = target.getGuildId();
        if (initiatingGuildId > 0L && initiatingGuildId == targetGuildId) {
            return JoinFightResult.PVP_CANT_FIGHT_GUILDMATE;
        }
        return JoinFightResult.OK;
    }
}
