package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.game.fight.fightEndCheck.*;

public enum FightModel
{
    PVP(id(1).withPvpModelType(PvpModelType.PVP).withAuthorizedReconnection(true).withApplyNonAggressionStateOnDefeat(true).withUseInPvpRanking(true).withAttackerNeedPvpTag(true).withCanBeRaised(false).withRestoreHpAtFightStart(TeamTypes.TARGETED_TEAM)), 
    PVE(id(2).withMaxTeamSize(8).withLoot().withXp().withUseFightChallenges(true).withCompanionsAllowed(true).withHeroesAllowed(true).withAuthorizedReconnection(true)), 
    TUTO(id(3).withoutGivingUp().withXp().withoutPenalties().withTurnLasting(600000)), 
    STDNOPLACEMENT(id(4).withoutPlacement().withXp()), 
    NOPLACEMENT_MOVEPLAYER(id(5).withAutoPlacement().withXp()), 
    PROTECTOR_ASSAULT(id(6).withPlacementLasting(15000).withPvpModelType(PvpModelType.PVP).withCanJoinOpposingPartyTeam(false).withProtectorFight(true)), 
    TRAINING_FIGHT(id(7).withMaxTeamSize(8).withDecreaseRent(false)), 
    TRAINING_FIGHT_WITH_XP_WITHOUT_REPORT(id(9).withMaxTeamSize(8).withXp().withDisplayFightResult(false).withDecreaseRent(false)), 
    OGREST_FIGHT_INTRO(id(10).withMaxTeamSize(1).withDisplayFightResult(false).withoutPenalties().withoutGivingUp().withDecreaseRent(false)), 
    ARCADE_DUNGEON(id(11).withAllowNullFightersAtCreation().withFightEndChecker(new EnoughPlayerLeftChecker()).withAllowNoFightersInPlayAtEnd(true).withNoFighterLimitForRedTeam().withDisplayFightResult(false).withArcadeType(true).withCheckPlacementPositionSize(false).withoutPenalties()), 
    NO_DEFEAT_CONTINUE_WHEN_NO_PLAYERS(id(12).withAllowNullFightersAtCreation().withAllowNoFightersInPlayAtEnd(true).withNoFighterLimitForRedTeam().withDisplayFightResult(false).withArcadeType(true).withoutPenalties().withFightEndChecker(new AtLeastOneFighterInBlueTeam()).withCheckPlacementPositionSize(false)), 
    BOUFBOWL_MATCH(id(13).withAllowNullFightersAtCreation().withAllowNoFightersInPlayAtEnd(true).withMaxTeamSize(5).withDisplayFightResult(false).withoutPenalties().withFightEndChecker(new BoufbowlEndChecker()).withCheckPlacementPositionSize(false)), 
    TRAINING_FIGHT_WITHOUT_PENALTIES(id(14).withMaxTeamSize(8).withoutPenalties().withDisplayFightResult(false).withoutOsaInvocDeath()), 
    CONTINUE_WHEN_NO_PLAYERS(id(15).withAllowNullFightersAtCreation().withAllowNoFightersInPlayAtEnd(true).withNoFighterLimitForRedTeam().withLoot().withFightEndChecker(new EnoughTeamLeftChecker()).withCheckPlacementPositionSize(false)), 
    NEW_TUTO(id(16).withoutGivingUp().withTurnLasting(600000).withoutPlacement().withUseTimeScoreGauge(false).withDisplayFightResult(false).withDecreaseRent(false)), 
    NEW_TUTO_WITH_PLACEMENT(id(17).withoutGivingUp().withTurnLasting(600000).withUseTimeScoreGauge(false).withDisplayFightResult(false).withDecreaseRent(false)), 
    PVE_WITHOUT_COMPANION(id(18).withMaxTeamSize(8).withLoot().withXp().withUseFightChallenges(true)), 
    COLLECT_FIGHT(id(19).withMaxTeamSize(1).withCheckPlacementPositionSize(false).withLoot().withXp().withUseFightChallenges(false).withoutPenalties().withRestoreBeforeFightHp(true).withUseTimeScoreGauge(false).withPassiveSpell(false)), 
    CONTINUE_WHEN_NO_PLAYERS_NO_COMPANION(id(20).withAllowNullFightersAtCreation().withAllowNoFightersInPlayAtEnd(true).withNoFighterLimitForRedTeam().withCompanionsAllowed(false).withLoot().withFightEndChecker(new EnoughTeamLeftChecker()).withCheckPlacementPositionSize(false)), 
    DUEL(id(65).withoutPenalties().withPvpModelType(PvpModelType.DUEL).withoutOsaInvocDeath().withDecreaseRent(false).withCompanionsAllowed(true).withRestoreBeforeFightHp(true).withAuthorizedReconnection(true)), 
    RANKED_NATION_PVP(id(66).withPvpModelType(PvpModelType.PVP).withAuthorizedReconnection(true).withAllowOnlyOneNationPerTeam(true).withUseInPvpRanking(true).withInitiatorGainPvpPoints(true).withAttackerNeedPvpTag(true).withDefenderNeedPvpTag(true).withCanBeRaised(false).withRestoreHpAtFightStart(TeamTypes.TARGETED_TEAM));
    
    private final byte m_typeId;
    private final byte m_minTeam;
    private final byte m_maxTeam;
    private final byte m_maxFighterByTeam;
    private final int m_placementDurationInMillisecond;
    private final int m_turnDurationInMillisecond;
    private final boolean m_needPlacementStep;
    private final boolean m_moveFightersOnEnterFight;
    private final boolean m_fighterCanLoot;
    private final boolean m_fighterCanGiveUp;
    private final boolean m_hasXpManager;
    private final boolean m_deadAtEndOfFight;
    private final boolean m_displayTimePointBar;
    private final boolean m_displayFightResult;
    private final boolean m_allowNullFightersAtCreation;
    private final FightEndChecker m_fightEndChecker;
    private final boolean m_allowNoFightersInPlayAtEnd;
    private final boolean m_noFighterLimitForRedTeam;
    private final boolean m_isArcadeType;
    private final boolean m_checkPlacementPositionSize;
    private final PvpModelType m_pvpModelType;
    private final boolean m_osaInvocDeath;
    private final boolean m_useTimeScoreGauge;
    private final boolean m_useFightChallenges;
    private final boolean m_companionsAllowed;
    private final boolean m_heroesAllowed;
    private final boolean m_decreaseRent;
    private final boolean m_restoreBeforeFightHp;
    private final boolean m_withPassiveSpells;
    private final boolean m_authorizedReconnection;
    private final boolean m_allowOnlyOneNationPerTeam;
    private final boolean m_useInPvpRanking;
    private final boolean m_applyNonAggressionStateOnDefeat;
    private final boolean m_canBeRaised;
    private final boolean m_canJoinOpposingPartyTeam;
    private final boolean m_protectorFight;
    private final boolean m_canInitiatorGainPvpPoints;
    private final boolean m_attackerNeedPvpTag;
    private final boolean m_defenderNeedPvpTag;
    private final TeamTypes m_restoreHpAtFightStart;
    
    public static FightModel getFromTypeId(final int typeId) {
        for (final FightModel model : values()) {
            if (model.getTypeId() == (byte)typeId) {
                return model;
            }
        }
        return null;
    }
    
    private static FightModelBuilder id(final int id) {
        return new FightModelBuilder(id);
    }
    
    private FightModel(final FightModelBuilder builder) {
        this.m_typeId = (byte)builder.getId();
        this.m_minTeam = (byte)builder.getNumTeams();
        this.m_maxTeam = (byte)builder.getNumTeams();
        this.m_maxFighterByTeam = (byte)builder.getMaxTeamSize();
        this.m_placementDurationInMillisecond = builder.getPlacementDurationInMilliseconds();
        this.m_turnDurationInMillisecond = builder.getTurnDurationInMilliseconds();
        this.m_needPlacementStep = builder.isWithPlacement();
        this.m_moveFightersOnEnterFight = builder.isWithFighterTeleport();
        this.m_fighterCanLoot = builder.isWithLoot();
        this.m_fighterCanGiveUp = builder.isCanGiveUp();
        this.m_hasXpManager = builder.isWithXp();
        this.m_deadAtEndOfFight = builder.isWithPenalties();
        this.m_displayTimePointBar = builder.isDisplayTimePointBar();
        this.m_displayFightResult = builder.isDisplayFightResult();
        this.m_allowNullFightersAtCreation = builder.isAllowNullFightersAtCreation();
        this.m_fightEndChecker = builder.getFightEndChecker();
        this.m_allowNoFightersInPlayAtEnd = builder.isAllowNoFightersInPlayAtEnd();
        this.m_noFighterLimitForRedTeam = builder.isNoFighterLimitForRedTeam();
        this.m_isArcadeType = builder.isArcadeType();
        this.m_checkPlacementPositionSize = builder.isCheckPlacementPositionSize();
        this.m_pvpModelType = builder.getPvpModelType();
        this.m_osaInvocDeath = builder.isWithOsaInvocDeath();
        this.m_useTimeScoreGauge = builder.useTimeScoreGauge();
        this.m_useFightChallenges = builder.isUseFightChallenges();
        this.m_companionsAllowed = builder.isCompanionsAllowed();
        this.m_heroesAllowed = builder.isHeroesAllowed();
        this.m_decreaseRent = builder.decreaseRent();
        this.m_restoreBeforeFightHp = builder.isRestoreBeforeFightHp();
        this.m_withPassiveSpells = builder.isWithPassiveSpells();
        this.m_authorizedReconnection = builder.isAuthorizedReconnection();
        this.m_useInPvpRanking = builder.isUseInPvpRanking();
        this.m_allowOnlyOneNationPerTeam = builder.isAllowOnlyOneNationPerTeam();
        this.m_applyNonAggressionStateOnDefeat = builder.isApplyNonAgressionStateOnDefeat();
        this.m_canBeRaised = builder.isCanBeRaised();
        this.m_canJoinOpposingPartyTeam = builder.canJoinOpposingPartyTeam();
        this.m_protectorFight = builder.isProtectorFight();
        this.m_canInitiatorGainPvpPoints = builder.canInitiatorGainPvpPoints();
        this.m_attackerNeedPvpTag = builder.isAttackerNeedPvpTag();
        this.m_defenderNeedPvpTag = builder.isDefenderNeedPvpTag();
        this.m_restoreHpAtFightStart = builder.isRestoreHpAtFightStart();
    }
    
    public byte getTypeId() {
        return this.m_typeId;
    }
    
    public byte getMinTeam() {
        return this.m_minTeam;
    }
    
    public byte getMaxTeam() {
        return this.m_maxTeam;
    }
    
    public byte getMaxFighterByTeam() {
        return this.m_maxFighterByTeam;
    }
    
    public boolean fighterCanLoot() {
        return this.m_fighterCanLoot;
    }
    
    public boolean hasXpManager() {
        return this.m_hasXpManager;
    }
    
    public int getTurnDurationInMillisecond() {
        return this.m_turnDurationInMillisecond;
    }
    
    public boolean needPlacementStep() {
        return this.m_needPlacementStep;
    }
    
    public boolean moveFightersOnEnterFight() {
        return this.m_moveFightersOnEnterFight;
    }
    
    public boolean fighterCanGiveUp() {
        return this.m_fighterCanGiveUp;
    }
    
    public boolean mustDieAtEndOfFight() {
        return this.m_deadAtEndOfFight;
    }
    
    public int getPlacementDurationInMillisecond() {
        return this.m_placementDurationInMillisecond;
    }
    
    public boolean displayFightResult() {
        return this.m_displayFightResult;
    }
    
    @Deprecated
    public boolean displayTimePointBar() {
        return this.m_displayTimePointBar;
    }
    
    public boolean isAllowNullFightersAtCreation() {
        return this.m_allowNullFightersAtCreation;
    }
    
    public FightEndChecker getFightEndChecker() {
        return this.m_fightEndChecker;
    }
    
    public boolean isAllowNoFightersInPlayAtEnd() {
        return this.m_allowNoFightersInPlayAtEnd;
    }
    
    public boolean isNoFighterLimitForRedTeam() {
        return this.m_noFighterLimitForRedTeam;
    }
    
    public boolean isArcadeType() {
        return this.m_isArcadeType;
    }
    
    public boolean isCheckPlacementPositionSize() {
        return this.m_checkPlacementPositionSize;
    }
    
    public boolean isPvp() {
        return this.m_pvpModelType.isPvp() && !this.m_pvpModelType.isFriendly();
    }
    
    public PvpModelType getPvpModelType() {
        return this.m_pvpModelType;
    }
    
    public boolean isOsaInvocDeath() {
        return this.m_osaInvocDeath;
    }
    
    public boolean isUseTimeScoreGauge() {
        return this.m_useTimeScoreGauge;
    }
    
    public boolean isUseFightChallenges() {
        return this.m_useFightChallenges;
    }
    
    public boolean isCompanionsAllowed() {
        return this.m_companionsAllowed;
    }
    
    public boolean isHeroesAllowed() {
        return this.m_heroesAllowed;
    }
    
    public boolean decreaseRent() {
        return this.m_decreaseRent;
    }
    
    public boolean isRestoreBeforeFightHp() {
        return this.m_restoreBeforeFightHp;
    }
    
    public boolean isWithPassiveSpells() {
        return this.m_withPassiveSpells;
    }
    
    public boolean isAuthorizedReconnection() {
        return this.m_authorizedReconnection;
    }
    
    public boolean isUseInPvpRanking() {
        return this.m_useInPvpRanking;
    }
    
    public boolean isAllowOnlyOneNationPerTeam() {
        return this.m_allowOnlyOneNationPerTeam;
    }
    
    public boolean isApplyNonAggressionStateOnDefeat() {
        return this.m_applyNonAggressionStateOnDefeat;
    }
    
    public boolean canBeRaised() {
        return this.m_canBeRaised;
    }
    
    public boolean canJoinOpposingPartyTeam() {
        return this.m_canJoinOpposingPartyTeam;
    }
    
    public boolean isProtectorFight() {
        return this.m_protectorFight;
    }
    
    public boolean canInitiatorGainPvpPoints() {
        return this.m_canInitiatorGainPvpPoints;
    }
    
    public boolean isAttackerNeedPvpTag() {
        return this.m_attackerNeedPvpTag;
    }
    
    public boolean isDefenderNeedPvpTag() {
        return this.m_defenderNeedPvpTag;
    }
    
    public TeamTypes isRestoreHpAtFightStart() {
        return this.m_restoreHpAtFightStart;
    }
}
