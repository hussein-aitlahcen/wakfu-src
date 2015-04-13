package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.game.fight.fightEndCheck.*;

class FightModelBuilder
{
    private static final int DEFAULT_NUM_TEAMS = 2;
    private static final int DEFAULT_MAX_TEAM_SIZE = 6;
    private static final int DEFAULT_PLACEMENT_DURATION_MS = 30000;
    private static final int DEFAULT_TURN_DURATION_MS = 30000;
    private final int m_id;
    private final int m_numTeams = 2;
    private int m_maxTeamSize;
    private int m_placementDurationMillis;
    private int m_turnDurationMillis;
    private boolean m_withPlacement;
    private boolean m_withFighterTeleport;
    private boolean m_withLoot;
    private boolean m_withXp;
    private boolean m_canGiveUp;
    private boolean m_withPenalties;
    private boolean m_withOsaInvocDeath;
    private boolean m_displayTimePointBar;
    private boolean m_displayFightResult;
    private boolean m_allowNullFightersAtCreation;
    private FightEndChecker m_fightEndChecker;
    private boolean m_allowNoFightersInPlayAtEnd;
    private boolean m_noFighterLimitForRedTeam;
    private boolean m_isArcadeType;
    private boolean m_checkPlacementPositionSize;
    private PvpModelType m_pvpModelType;
    private boolean m_useTimeScoreGauge;
    private boolean m_useFightChallenges;
    private boolean m_companionsAllowed;
    private boolean m_decreaseRent;
    private boolean m_restoreBeforeFightHp;
    private boolean m_withPassiveSpells;
    private boolean m_authorizedReconnection;
    private boolean m_allowOnlyOneNationPerTeam;
    private boolean m_useInPvpRanking;
    private boolean m_applyNonAgressionStateOnDefeat;
    private boolean m_canBeRaised;
    private boolean m_canJoinOpposingPartyTeam;
    private boolean m_isProtectorFight;
    private boolean m_canInitiatorGainPvpPoints;
    private boolean m_attackerNeedPvpTag;
    private boolean m_defenderNeedPvpTag;
    private TeamTypes m_restoreHpAtFightStart;
    private boolean m_heroesAllowed;
    
    FightModelBuilder(final int id) {
        super();
        this.m_maxTeamSize = 6;
        this.m_placementDurationMillis = 30000;
        this.m_turnDurationMillis = 30000;
        this.m_withPlacement = true;
        this.m_withFighterTeleport = true;
        this.m_withLoot = false;
        this.m_withXp = false;
        this.m_canGiveUp = true;
        this.m_withPenalties = true;
        this.m_withOsaInvocDeath = true;
        this.m_displayTimePointBar = true;
        this.m_displayFightResult = true;
        this.m_allowNullFightersAtCreation = false;
        this.m_fightEndChecker = new EnoughTeamWithPlayersOrCompanionLeftChecker();
        this.m_allowNoFightersInPlayAtEnd = false;
        this.m_noFighterLimitForRedTeam = false;
        this.m_isArcadeType = false;
        this.m_checkPlacementPositionSize = true;
        this.m_pvpModelType = PvpModelType.NOT_PVP;
        this.m_useTimeScoreGauge = true;
        this.m_useFightChallenges = false;
        this.m_companionsAllowed = false;
        this.m_decreaseRent = true;
        this.m_restoreBeforeFightHp = false;
        this.m_withPassiveSpells = true;
        this.m_authorizedReconnection = false;
        this.m_allowOnlyOneNationPerTeam = false;
        this.m_useInPvpRanking = false;
        this.m_applyNonAgressionStateOnDefeat = false;
        this.m_canBeRaised = true;
        this.m_canJoinOpposingPartyTeam = true;
        this.m_isProtectorFight = false;
        this.m_canInitiatorGainPvpPoints = false;
        this.m_attackerNeedPvpTag = false;
        this.m_defenderNeedPvpTag = false;
        this.m_restoreHpAtFightStart = TeamTypes.NO_TEAM;
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getNumTeams() {
        return 2;
    }
    
    public int getMaxTeamSize() {
        return this.m_maxTeamSize;
    }
    
    public int getPlacementDurationInMilliseconds() {
        return this.m_placementDurationMillis;
    }
    
    public int getTurnDurationInMilliseconds() {
        return this.m_turnDurationMillis;
    }
    
    public boolean isWithPlacement() {
        return this.m_withPlacement;
    }
    
    public boolean isWithFighterTeleport() {
        return this.m_withFighterTeleport;
    }
    
    public boolean isWithLoot() {
        return this.m_withLoot;
    }
    
    public boolean isWithXp() {
        return this.m_withXp;
    }
    
    public boolean isCanGiveUp() {
        return this.m_canGiveUp;
    }
    
    public boolean isWithPenalties() {
        return this.m_withPenalties;
    }
    
    public boolean isDisplayTimePointBar() {
        return this.m_displayTimePointBar;
    }
    
    public boolean isDisplayFightResult() {
        return this.m_displayFightResult;
    }
    
    public boolean isAllowNullFightersAtCreation() {
        return this.m_allowNullFightersAtCreation;
    }
    
    public boolean isAllowNoFightersInPlayAtEnd() {
        return this.m_allowNoFightersInPlayAtEnd;
    }
    
    public boolean isNoFighterLimitForRedTeam() {
        return this.m_noFighterLimitForRedTeam;
    }
    
    public FightEndChecker getFightEndChecker() {
        return this.m_fightEndChecker;
    }
    
    public boolean isArcadeType() {
        return this.m_isArcadeType;
    }
    
    public boolean isCheckPlacementPositionSize() {
        return this.m_checkPlacementPositionSize;
    }
    
    public PvpModelType getPvpModelType() {
        return this.m_pvpModelType;
    }
    
    public boolean isWithOsaInvocDeath() {
        return this.m_withOsaInvocDeath;
    }
    
    public boolean useTimeScoreGauge() {
        return this.m_useTimeScoreGauge;
    }
    
    boolean isUseFightChallenges() {
        return this.m_useFightChallenges;
    }
    
    boolean isCompanionsAllowed() {
        return this.m_companionsAllowed;
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
    
    public boolean isAllowOnlyOneNationPerTeam() {
        return this.m_allowOnlyOneNationPerTeam;
    }
    
    public boolean isUseInPvpRanking() {
        return this.m_useInPvpRanking;
    }
    
    public boolean isApplyNonAgressionStateOnDefeat() {
        return this.m_applyNonAgressionStateOnDefeat;
    }
    
    public boolean canJoinOpposingPartyTeam() {
        return this.m_canJoinOpposingPartyTeam;
    }
    
    public boolean isProtectorFight() {
        return this.m_isProtectorFight;
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
    
    public FightModelBuilder withDisplayFightResult(final boolean displayFightResult) {
        this.m_displayFightResult = displayFightResult;
        return this;
    }
    
    public FightModelBuilder withDisplayTimePointBar(final boolean displayTimePointBar) {
        this.m_displayTimePointBar = displayTimePointBar;
        return this;
    }
    
    public FightModelBuilder withMaxTeamSize(final int maxTeamSize) {
        this.m_maxTeamSize = maxTeamSize;
        return this;
    }
    
    public FightModelBuilder withPlacementLasting(final int placementDurationMillis) {
        this.m_placementDurationMillis = placementDurationMillis;
        return this;
    }
    
    public FightModelBuilder withTurnLasting(final int turnDurationMillis) {
        this.m_turnDurationMillis = turnDurationMillis;
        return this;
    }
    
    public FightModelBuilder withoutPlacement() {
        this.m_withPlacement = false;
        this.m_withFighterTeleport = false;
        return this;
    }
    
    public FightModelBuilder withAutoPlacement() {
        this.m_withPlacement = false;
        return this;
    }
    
    public FightModelBuilder withLoot() {
        this.m_withLoot = true;
        return this;
    }
    
    public FightModelBuilder withXp() {
        this.m_withXp = true;
        return this;
    }
    
    public FightModelBuilder withoutGivingUp() {
        this.m_canGiveUp = false;
        return this;
    }
    
    public FightModelBuilder withoutPenalties() {
        this.m_withPenalties = false;
        return this;
    }
    
    public FightModelBuilder withoutOsaInvocDeath() {
        this.m_withOsaInvocDeath = false;
        return this;
    }
    
    public FightModelBuilder withAllowNullFightersAtCreation() {
        this.m_allowNullFightersAtCreation = true;
        return this;
    }
    
    public FightModelBuilder withFightEndChecker(final FightEndChecker checker) {
        this.m_fightEndChecker = checker;
        return this;
    }
    
    public FightModelBuilder withAllowNoFightersInPlayAtEnd(final boolean allowNoFightersInPlayAtEnd) {
        this.m_allowNoFightersInPlayAtEnd = allowNoFightersInPlayAtEnd;
        return this;
    }
    
    public FightModelBuilder withNoFighterLimitForRedTeam() {
        this.m_noFighterLimitForRedTeam = true;
        return this;
    }
    
    public FightModelBuilder withArcadeType(final boolean isArcadeType) {
        this.m_isArcadeType = isArcadeType;
        return this;
    }
    
    public FightModelBuilder withCheckPlacementPositionSize(final boolean checkPlacementPositionSize) {
        this.m_checkPlacementPositionSize = checkPlacementPositionSize;
        return this;
    }
    
    public FightModelBuilder withPvpModelType(final PvpModelType type) {
        this.m_pvpModelType = type;
        return this;
    }
    
    public FightModelBuilder withUseTimeScoreGauge(final boolean useTimeScoreGauge) {
        this.m_useTimeScoreGauge = useTimeScoreGauge;
        return this;
    }
    
    public FightModelBuilder withUseFightChallenges(final boolean useFightChallenges) {
        this.m_useFightChallenges = useFightChallenges;
        return this;
    }
    
    public FightModelBuilder withCompanionsAllowed(final boolean companionsAllowed) {
        this.m_companionsAllowed = companionsAllowed;
        return this;
    }
    
    public FightModelBuilder withDecreaseRent(final boolean decreaseRent) {
        this.m_decreaseRent = decreaseRent;
        return this;
    }
    
    public FightModelBuilder withRestoreBeforeFightHp(final boolean restoreBeforeFightHp) {
        this.m_restoreBeforeFightHp = restoreBeforeFightHp;
        return this;
    }
    
    public FightModelBuilder withPassiveSpell(final boolean withPassiveSpells) {
        this.m_withPassiveSpells = withPassiveSpells;
        return this;
    }
    
    public FightModelBuilder withAuthorizedReconnection(final boolean authorizedReconnection) {
        this.m_authorizedReconnection = authorizedReconnection;
        return this;
    }
    
    public FightModelBuilder withAllowOnlyOneNationPerTeam(final boolean allowOnlyOneNationPerTeam) {
        this.m_allowOnlyOneNationPerTeam = allowOnlyOneNationPerTeam;
        return this;
    }
    
    public FightModelBuilder withUseInPvpRanking(final boolean useInPvpRanking) {
        this.m_useInPvpRanking = useInPvpRanking;
        return this;
    }
    
    public FightModelBuilder withApplyNonAggressionStateOnDefeat(final boolean applyNonAggressionStateOnDefeat) {
        this.m_applyNonAgressionStateOnDefeat = applyNonAggressionStateOnDefeat;
        return this;
    }
    
    public FightModelBuilder withCanBeRaised(final boolean canBeRaised) {
        this.m_canBeRaised = canBeRaised;
        return this;
    }
    
    public FightModelBuilder withCanJoinOpposingPartyTeam(final boolean canJoinOpposingPartyTeam) {
        this.m_canJoinOpposingPartyTeam = canJoinOpposingPartyTeam;
        return this;
    }
    
    public FightModelBuilder withProtectorFight(final boolean isProtectorFight) {
        this.m_isProtectorFight = isProtectorFight;
        return this;
    }
    
    public FightModelBuilder withInitiatorGainPvpPoints(final boolean canInitiatorGainPvpPoints) {
        this.m_canInitiatorGainPvpPoints = canInitiatorGainPvpPoints;
        return this;
    }
    
    public FightModelBuilder withAttackerNeedPvpTag(final boolean attackerNeedPvpTag) {
        this.m_attackerNeedPvpTag = attackerNeedPvpTag;
        return this;
    }
    
    public FightModelBuilder withDefenderNeedPvpTag(final boolean defenderNeedPvpTag) {
        this.m_defenderNeedPvpTag = defenderNeedPvpTag;
        return this;
    }
    
    public FightModelBuilder withRestoreHpAtFightStart(final TeamTypes restoreHpAtFightStart) {
        this.m_restoreHpAtFightStart = restoreHpAtFightStart;
        return this;
    }
    
    public FightModelBuilder withHeroesAllowed(final boolean heroesAllowed) {
        this.m_heroesAllowed = heroesAllowed;
        return this;
    }
    
    public boolean isAuthorizedReconnection() {
        return this.m_authorizedReconnection;
    }
    
    public boolean isCanBeRaised() {
        return this.m_canBeRaised;
    }
    
    public boolean isHeroesAllowed() {
        return this.m_heroesAllowed;
    }
}
