package com.ankamagames.wakfu.common.datas.guild.agt_like;

import com.ankamagames.wakfu.common.game.guild.bonus.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;

public enum GuildBonusDataAGT
{
    BONUS_1(new GuildBonusDefinition(1, 7200000L, -1L, 200, new MemberEffect(93608), BonusType.MEMBER_BONUS)), 
    BONUS_2(new GuildBonusDefinition(2, 7200000L, -1L, 400, new MemberEffect(93960), BonusType.MEMBER_BONUS)), 
    BONUS_3(new GuildBonusDefinition(3, 14400000L, -1L, 500, new MemberEffect(93962), BonusType.MEMBER_BONUS)), 
    BONUS_4(new GuildBonusDefinition(4, 14400000L, -1L, 500, new MemberEffect(93963), BonusType.MEMBER_BONUS)), 
    BONUS_5(new GuildBonusDefinition(5, 36000000L, -1L, 1000, new MemberEffect(93967), BonusType.MEMBER_BONUS)), 
    BONUS_6(new GuildBonusDefinition(6, 36000000L, -1L, 1000, new MemberEffect(93966), BonusType.MEMBER_BONUS)), 
    BONUS_7(new GuildBonusDefinition(7, 36000000L, -1L, 1000, new MemberEffect(93964), BonusType.MEMBER_BONUS)), 
    BONUS_8(new GuildBonusDefinition(8, 36000000L, -1L, 1000, new MemberEffect(93965), BonusType.MEMBER_BONUS)), 
    BONUS_9(new GuildBonusDefinition(9, 21600000L, -1L, 800, new MemberEffect(93968), BonusType.MEMBER_BONUS)), 
    BONUS_10(new GuildBonusDefinition(10, 43200000L, -1L, 1500, new CriterionBonus(), BonusType.CRITERION_BONUS)), 
    BONUS_11(new GuildBonusDefinition(11, 57600000L, -1L, 3000, new UnlockGuildStorageCompartment(GuildStorageCompartmentType.GUILD_1), BonusType.GUILD_BONUS)), 
    BONUS_12(new GuildBonusDefinition(12, 43200000L, -1L, 1500, new MemberEffect(93589), BonusType.MEMBER_BONUS)), 
    BONUS_13(new GuildBonusDefinition(13, 43200000L, -1L, 1500, new MemberEffect(93598), BonusType.MEMBER_BONUS)), 
    BONUS_14(new GuildBonusDefinition(14, 43200000L, -1L, 1500, new MemberEffect(93969), BonusType.MEMBER_BONUS)), 
    BONUS_15(new GuildBonusDefinition(15, 43200000L, -1L, 1500, new MemberEffect(93970), BonusType.MEMBER_BONUS)), 
    BONUS_16(new GuildBonusDefinition(16, 14400000L, -1L, 800, new MemberEffect(93971), BonusType.MEMBER_BONUS)), 
    BONUS_17(new GuildBonusDefinition(17, 21600000L, -1L, 1000, new MemberEffect(93972), BonusType.MEMBER_BONUS)), 
    BONUS_18(new GuildBonusDefinition(18, 54000000L, -1L, 1500, new MemberEffect(93976), BonusType.MEMBER_BONUS)), 
    BONUS_19(new GuildBonusDefinition(19, 54000000L, -1L, 1500, new MemberEffect(93975), BonusType.MEMBER_BONUS)), 
    BONUS_20(new GuildBonusDefinition(20, 54000000L, -1L, 1500, new MemberEffect(93973), BonusType.MEMBER_BONUS)), 
    BONUS_21(new GuildBonusDefinition(21, 54000000L, -1L, 1500, new MemberEffect(93974), BonusType.MEMBER_BONUS)), 
    BONUS_22(new GuildBonusDefinition(22, 86400000L, -1L, 5000, new MemberEffect(93610), BonusType.MEMBER_BONUS)), 
    BONUS_23(new GuildBonusDefinition(23, 43200000L, -1L, 2000, new CriterionBonus(), BonusType.CRITERION_BONUS)), 
    BONUS_24(new GuildBonusDefinition(24, 86400000L, -1L, 1500, new CriterionBonus(), BonusType.CRITERION_BONUS)), 
    BONUS_25(new GuildBonusDefinition(25, 259200000L, -1L, 14000, new SetPointsEarnedFactor(1.1f), BonusType.MEMBER_BONUS)), 
    BONUS_26(new GuildBonusDefinition(26, 64800000L, -1L, 3000, new MemberEffect(93977), BonusType.MEMBER_BONUS)), 
    BONUS_27(new GuildBonusDefinition(27, 64800000L, -1L, 3000, new MemberEffect(93978), BonusType.MEMBER_BONUS)), 
    BONUS_28(new GuildBonusDefinition(28, 64800000L, -1L, 3000, new MemberEffect(93979), BonusType.MEMBER_BONUS)), 
    BONUS_29(new GuildBonusDefinition(29, 64800000L, -1L, 3000, new MemberEffect(93980), BonusType.MEMBER_BONUS)), 
    BONUS_30(new GuildBonusDefinition(30, 28800000L, -1L, 1500, new MemberEffect(93981), BonusType.MEMBER_BONUS)), 
    BONUS_31(new GuildBonusDefinition(31, 28800000L, -1L, 1500, new MemberEffect(93982), BonusType.MEMBER_BONUS)), 
    BONUS_32(new GuildBonusDefinition(32, 115200000L, -1L, 7500, new MemberEffect(93983), BonusType.MEMBER_BONUS)), 
    BONUS_33(new GuildBonusDefinition(33, 172800000L, -1L, 5000, new UnlockGuildStorageCompartment(GuildStorageCompartmentType.GUILD_2), BonusType.GUILD_BONUS)), 
    BONUS_34(new GuildBonusDefinition(34, 345600000L, -1L, 20000, new ReduceLearningDuration(), BonusType.GUILD_BONUS)), 
    BONUS_35(new GuildBonusDefinition(35, 43200000L, -1L, 1500, new MemberEffect(93984), BonusType.MEMBER_BONUS)), 
    BONUS_36(new GuildBonusDefinition(36, 86400000L, -1L, 3000, new MemberEffect(93988), BonusType.MEMBER_BONUS)), 
    BONUS_37(new GuildBonusDefinition(37, 86400000L, -1L, 3000, new MemberEffect(93987), BonusType.MEMBER_BONUS)), 
    BONUS_38(new GuildBonusDefinition(38, 86400000L, -1L, 3000, new MemberEffect(93985), BonusType.MEMBER_BONUS)), 
    BONUS_39(new GuildBonusDefinition(39, 86400000L, -1L, 3000, new MemberEffect(93986), BonusType.MEMBER_BONUS)), 
    BONUS_40(new GuildBonusDefinition(40, 172800000L, -1L, 3000, new CriterionBonus(), BonusType.CRITERION_BONUS)), 
    BONUS_41(new GuildBonusDefinition(41, 259200000L, -1L, 15000, new MemberEffect(93989), BonusType.MEMBER_BONUS)), 
    BONUS_42(new GuildBonusDefinition(42, 345600000L, -1L, 20000, new SetWeeklyPointsLimit(9000), BonusType.GUILD_BONUS)), 
    BONUS_43(new GuildBonusDefinition(43, 64800000L, -1L, 2000, new MemberEffect(93990), BonusType.MEMBER_BONUS)), 
    BONUS_44(new GuildBonusDefinition(44, 64800000L, -1L, 2000, new MemberEffect(93991), BonusType.MEMBER_BONUS)), 
    BONUS_45(new GuildBonusDefinition(45, 64800000L, -1L, 2000, new MemberEffect(93993), BonusType.MEMBER_BONUS)), 
    BONUS_46(new GuildBonusDefinition(46, 115200000L, -1L, 4500, new MemberEffect(93997), BonusType.MEMBER_BONUS)), 
    BONUS_47(new GuildBonusDefinition(47, 115200000L, -1L, 4500, new MemberEffect(93998), BonusType.MEMBER_BONUS)), 
    BONUS_48(new GuildBonusDefinition(48, 115200000L, -1L, 4500, new MemberEffect(93999), BonusType.MEMBER_BONUS)), 
    BONUS_49(new GuildBonusDefinition(49, 115200000L, -1L, 4500, new MemberEffect(94000), BonusType.MEMBER_BONUS)), 
    BONUS_50(new GuildBonusDefinition(50, 345600000L, -1L, 10000, new UnlockGuildStorageCompartment(GuildStorageCompartmentType.GUILD_3), BonusType.GUILD_BONUS)), 
    BONUS_51(new GuildBonusDefinition(51, 345600000L, -1L, 20000, new IncreaseMaxAuthorizedEvolution(), BonusType.GUILD_BONUS)), 
    BONUS_52(new GuildBonusDefinition(52, 86400000L, -1L, 2500, new MemberEffect(94005), BonusType.MEMBER_BONUS)), 
    BONUS_53(new GuildBonusDefinition(53, 172800000L, -1L, 4500, new MemberEffect(94004), BonusType.MEMBER_BONUS)), 
    BONUS_54(new GuildBonusDefinition(54, 172800000L, -1L, 4500, new MemberEffect(94003), BonusType.MEMBER_BONUS)), 
    BONUS_55(new GuildBonusDefinition(55, 172800000L, -1L, 4500, new MemberEffect(94001), BonusType.MEMBER_BONUS)), 
    BONUS_56(new GuildBonusDefinition(56, 172800000L, -1L, 4500, new MemberEffect(94002), BonusType.MEMBER_BONUS)), 
    BONUS_57(new GuildBonusDefinition(57, 345600000L, -1L, 10000, new MemberEffect(94007), BonusType.MEMBER_BONUS)), 
    BONUS_58(new GuildBonusDefinition(58, 345600000L, -1L, 10000, new MemberEffect(94006), BonusType.MEMBER_BONUS)), 
    BONUS_59(new GuildBonusDefinition(59, 345600000L, -1L, 7500, new CriterionBonus(), BonusType.CRITERION_BONUS)), 
    BONUS_60(new GuildBonusDefinition(60, 115200000L, -1L, 4000, new MemberEffect(94008), BonusType.MEMBER_BONUS)), 
    BONUS_61(new GuildBonusDefinition(61, 259200000L, -1L, 6000, new MemberEffect(94009), BonusType.MEMBER_BONUS)), 
    BONUS_62(new GuildBonusDefinition(62, 259200000L, -1L, 6000, new MemberEffect(94010), BonusType.MEMBER_BONUS)), 
    BONUS_63(new GuildBonusDefinition(63, 259200000L, -1L, 6000, new MemberEffect(94011), BonusType.MEMBER_BONUS)), 
    BONUS_64(new GuildBonusDefinition(64, 259200000L, -1L, 6000, new MemberEffect(94012), BonusType.MEMBER_BONUS)), 
    BONUS_65(new GuildBonusDefinition(65, 86400000L, -1L, 4000, new MemberEffect(94013), BonusType.MEMBER_BONUS)), 
    BONUS_66(new GuildBonusDefinition(66, 86400000L, -1L, 4000, new MemberEffect(94014), BonusType.MEMBER_BONUS)), 
    BONUS_67(new GuildBonusDefinition(67, 604800000L, -1L, 20000, new MemberEffect(94015), BonusType.MEMBER_BONUS)), 
    BONUS_68(new GuildBonusDefinition(68, 604800000L, -1L, 20000, new CriterionBonus(), BonusType.CRITERION_BONUS)), 
    BONUS_69(new GuildBonusDefinition(69, 604800000L, -1L, 20000, new UnlockGuildStorageCompartment(GuildStorageCompartmentType.GUILD_4), BonusType.GUILD_BONUS)), 
    BONUS_70(new GuildBonusDefinition(70, 43200000L, -1L, 3000, new MemberEffect(95924), BonusType.MEMBER_BONUS)), 
    BONUS_71(new GuildBonusDefinition(71, 115200000L, -1L, 5000, new MemberEffect(95925), BonusType.MEMBER_BONUS)), 
    BONUS_72(new GuildBonusDefinition(72, 86400000L, 21600000L, 4500, new MemberEffect(95926), BonusType.MEMBER_BONUS)), 
    BONUS_73(new GuildBonusDefinition(73, 86400000L, 21600000L, 4500, new MemberEffect(95927), BonusType.MEMBER_BONUS)), 
    BONUS_74(new GuildBonusDefinition(74, 1800000L, -1L, 200, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_1), BonusType.GUILD_BONUS)), 
    BONUS_75(new GuildBonusDefinition(75, 7200000L, -1L, 400, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_2), BonusType.GUILD_BONUS)), 
    BONUS_76(new GuildBonusDefinition(76, 14400000L, -1L, 700, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_3), BonusType.GUILD_BONUS)), 
    BONUS_77(new GuildBonusDefinition(77, 28800000L, -1L, 1000, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_4), BonusType.GUILD_BONUS)), 
    BONUS_78(new GuildBonusDefinition(78, 57600000L, -1L, 1500, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_5), BonusType.GUILD_BONUS)), 
    BONUS_79(new GuildBonusDefinition(79, 86400000L, -1L, 4000, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_6), BonusType.GUILD_BONUS)), 
    BONUS_80(new GuildBonusDefinition(80, 172800000L, -1L, 7500, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_7), BonusType.GUILD_BONUS)), 
    BONUS_81(new GuildBonusDefinition(81, 259200000L, -1L, 10000, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_8), BonusType.GUILD_BONUS)), 
    BONUS_82(new GuildBonusDefinition(82, 345600000L, -1L, 15000, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_9), BonusType.GUILD_BONUS)), 
    BONUS_83(new GuildBonusDefinition(83, 604800000L, -1L, 20000, new UnlockGuildLevel(GuildLevelDataAGT.LEVEL_10), BonusType.GUILD_BONUS)), 
    BONUS_84(new GuildBonusDefinition(84, 86400000L, -1L, 40000, new ChangeNationBonus(), BonusType.GUILD_BONUS));
    
    private final GuildBonusDefinition m_data;
    
    private GuildBonusDataAGT(final GuildBonusDefinition data) {
        this.m_data = data;
    }
    
    public GuildBonusDefinition get() {
        return this.m_data;
    }
    
    public static GuildBonusDataAGT getFromId(final int id) {
        for (final GuildBonusDataAGT bonus : values()) {
            if (bonus.get().getId() == id) {
                return bonus;
            }
        }
        return null;
    }
}
