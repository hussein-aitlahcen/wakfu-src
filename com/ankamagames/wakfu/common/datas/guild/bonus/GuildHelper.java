package com.ankamagames.wakfu.common.datas.guild.bonus;

import com.ankamagames.wakfu.common.datas.guild.agt_like.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;
import org.apache.commons.lang3.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;

public class GuildHelper
{
    private static GuildBonusDataAGT[] GUILD_LEVEL_BONUSES;
    
    public static boolean hasRights(final GuildInformationHandler handler, final GuildMember member, final GuildRankAuthorisation authorisation) {
        final GuildRank rank = handler.getRank(member.getRank());
        return rank != null && rank.hasAuthorisation(authorisation);
    }
    
    private static void checkLevelBonuses() {
        if (GuildHelper.GUILD_LEVEL_BONUSES == null) {
            GuildHelper.GUILD_LEVEL_BONUSES = new GuildBonusDataAGT[] { GuildBonusDataAGT.BONUS_74, GuildBonusDataAGT.BONUS_75, GuildBonusDataAGT.BONUS_76, GuildBonusDataAGT.BONUS_77, GuildBonusDataAGT.BONUS_78, GuildBonusDataAGT.BONUS_79, GuildBonusDataAGT.BONUS_80, GuildBonusDataAGT.BONUS_81, GuildBonusDataAGT.BONUS_82, GuildBonusDataAGT.BONUS_83 };
        }
    }
    
    public static GuildBonusDataAGT getLevelBonusFor(final int level) {
        checkLevelBonuses();
        for (final GuildBonusDataAGT bonus : GuildHelper.GUILD_LEVEL_BONUSES) {
            final GuildBonusDefinition definition = bonus.get();
            final UnlockGuildLevel effect = (UnlockGuildLevel)definition.getEffect();
            if (effect.getLevel().get().getId() == level) {
                return bonus;
            }
        }
        return null;
    }
    
    public static boolean isLevelBonus(final GuildBonusDataAGT bonus) {
        checkLevelBonuses();
        return ArrayUtils.contains(GuildHelper.GUILD_LEVEL_BONUSES, bonus);
    }
    
    public static boolean isBonusValid(final GuildBonusDefinition guildBonus) {
        final GuildBuffEffect effect = guildBonus.getEffect();
        switch (effect.getType()) {
            case MEMBER_EFFECT: {
                final MemberEffect memberEffect = (MemberEffect)effect;
                if (memberEffect.getEffectId() == 0) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
    
    public static int[] checkBonusListIntegrity(final int[] bonuses) {
        final TIntArrayList bonusesList = new TIntArrayList();
        for (final int bonusId : bonuses) {
            if (GuildBonusManager.INSTANCE.getBonus(bonusId) != null) {
                bonusesList.add(bonusId);
            }
        }
        return bonuses;
    }
}
