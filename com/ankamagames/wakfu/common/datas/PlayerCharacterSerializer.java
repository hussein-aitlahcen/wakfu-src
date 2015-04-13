package com.ankamagames.wakfu.common.datas;

public class PlayerCharacterSerializer extends CharacterSerializer
{
    private final CharacterSerializedGameServer m_gameServerPart;
    private final CharacterSerializedHp m_hpPart;
    private final CharacterSerializedXp m_xpPart;
    private final CharacterSerializedXpCharacteristics m_xpCharacteristicsPart;
    private final CharacterSerializedEquipmentInventory m_equipmentInventoryPart;
    private final CharacterSerializedSkillInventory m_skillInventoryPart;
    private final CharacterSerializedCraft m_craftPart;
    private final CharacterSerializedAptitudeInventory m_aptitudeInventoryPart;
    private final CharacterSerializedAptitudeBonusInventory m_aptitudeBonusInventoryPart;
    private final CharacterSerializedAppearance m_appearancePart;
    private final CharacterSerializedEquipmentAppearance m_equipmentAppearancePart;
    private final CharacterSerializedBreedSpecific m_breedSpecificPart;
    private final CharacterSerializedNationCitizenScore m_nationCitizenPart;
    private final CharacterSerializedSpellInventory m_spellInventoryPart;
    private final CharacterSerializedCharacteristics m_publicCharacteristicsPart;
    private final CharacterSerializedCharacteristics m_privateCharacteristicsPart;
    private final CharacterSerializedRunningEffectsForSave m_runningEffectsForSavePart;
    private final CharacterSerializedRunningEffectsForSave m_runningEffectsForGameToGamePart;
    private final CharacterSerializedAccountInformation m_accountInformationPart;
    private final CharacterSerializedRemoteAccountInformation m_remoteAccountInformationPart;
    private final CharacterSerializedDimensionalBagForSave m_dimensionalBagForSavePart;
    private final CharacterSerializedDimensionalBagForClient m_dimensionalBagForLocalClientPart;
    private final CharacterSerializedRunningEffects m_runningEffectsPart;
    private final CharacterSerializedBags m_bagsPart;
    private final CharacterSerializedShortcutInventories m_shortcutInventoriesPart;
    private final CharacterSerializedEmoteInventory m_emoteInventoryPart;
    private final CharacterSerializedLandMarkInventory m_landMarkInventoryPart;
    private final CharacterSerializedDiscoveredItemsInventory m_discoveredItemsPart;
    private final CharacterSerializedScenarioManager m_scenarioManagerPart;
    private final CharacterSerializedChallenges m_challengesPart;
    private final CharacterSerializedRespawnPoint m_respawnPointPart;
    private final CharacterSerializedCreationData m_creationDataPart;
    private final CharacterSerializedTitle m_titlePart;
    private final CharacterSerializedGroup m_groupPart;
    private final CharacterSerializedOccupation m_occupationPart;
    private final CharacterSerializedPreviousPosition m_previousPositionPart;
    private final CharacterSerializedGuild m_guildPart;
    private final CharacterSerializedGuildId m_guildIdPart;
    private final CharacterSerializedRemoteGuildInfo m_guildRemoteInfoPart;
    private final CharacterSerializedLocalGuildInfo m_guildLocalInfoPart;
    private final CharacterSerializedGuildInfoForGame m_guildInfoForGamePart;
    private final CharacterSerializedNationId m_nationIdPart;
    private final CharacterSerializedNationId m_characterListNationIdPart;
    private final CharacterSerializedNationStorage m_nationStoragePart;
    private final CharacterSerializedNationSynchro m_nationSynchroPart;
    private final CharacterSerializedNationPvp m_nationPvpPart;
    private final CharacterSerializedNationPvpMoney m_nationPvpMoneyPart;
    private final CharacterSerializedSocialStates m_socialStatesPart;
    private final CharacterSerializedPet m_petPart;
    private final CharacterSerializedAchievements m_achievementsPart;
    private final CharacterSerializedPasseportInfo m_passeportInfoPart;
    private final CharacterSerializedStatistics m_statisticsPart;
    private final CharacterSerializedDimensionalBagViewInventory m_dimensionalBagViewInventory;
    private final CharacterSerializedInventories m_inventoriesPart;
    private final CharacterSerializedLocks m_locksPart;
    private final CharacterSerializedLocksForClient m_locksForClientPart;
    private final CharacterSerializedPersonalEffects m_personalEffectsPart;
    private final CharacterSerializedAntiAddiction m_antiAddictionPart;
    private final CharacterSerializedVisibility m_visibilityPart;
    
    public PlayerCharacterSerializer() {
        super();
        this.m_gameServerPart = new CharacterSerializedGameServer();
        this.m_hpPart = new CharacterSerializedHp();
        this.m_xpPart = new CharacterSerializedXp();
        this.m_xpCharacteristicsPart = new CharacterSerializedXpCharacteristics();
        this.m_equipmentInventoryPart = new CharacterSerializedEquipmentInventory();
        this.m_skillInventoryPart = new CharacterSerializedSkillInventory();
        this.m_craftPart = new CharacterSerializedCraft();
        this.m_aptitudeInventoryPart = new CharacterSerializedAptitudeInventory();
        this.m_aptitudeBonusInventoryPart = new CharacterSerializedAptitudeBonusInventory();
        this.m_appearancePart = new CharacterSerializedAppearance();
        this.m_equipmentAppearancePart = new CharacterSerializedEquipmentAppearance();
        this.m_breedSpecificPart = new CharacterSerializedBreedSpecific();
        this.m_nationCitizenPart = new CharacterSerializedNationCitizenScore();
        this.m_spellInventoryPart = new CharacterSerializedSpellInventory();
        this.m_publicCharacteristicsPart = new CharacterSerializedCharacteristics();
        this.m_privateCharacteristicsPart = new CharacterSerializedCharacteristics();
        this.m_runningEffectsForSavePart = new CharacterSerializedRunningEffectsForSave();
        this.m_runningEffectsForGameToGamePart = new CharacterSerializedRunningEffectsForSave();
        this.m_accountInformationPart = new CharacterSerializedAccountInformation();
        this.m_remoteAccountInformationPart = new CharacterSerializedRemoteAccountInformation();
        this.m_dimensionalBagForSavePart = new CharacterSerializedDimensionalBagForSave();
        this.m_dimensionalBagForLocalClientPart = new CharacterSerializedDimensionalBagForClient();
        this.m_runningEffectsPart = new CharacterSerializedRunningEffects();
        this.m_bagsPart = new CharacterSerializedBags();
        this.m_shortcutInventoriesPart = new CharacterSerializedShortcutInventories();
        this.m_emoteInventoryPart = new CharacterSerializedEmoteInventory();
        this.m_landMarkInventoryPart = new CharacterSerializedLandMarkInventory();
        this.m_discoveredItemsPart = new CharacterSerializedDiscoveredItemsInventory();
        this.m_scenarioManagerPart = new CharacterSerializedScenarioManager();
        this.m_challengesPart = new CharacterSerializedChallenges();
        this.m_respawnPointPart = new CharacterSerializedRespawnPoint();
        this.m_creationDataPart = new CharacterSerializedCreationData();
        this.m_titlePart = new CharacterSerializedTitle();
        this.m_groupPart = new CharacterSerializedGroup();
        this.m_occupationPart = new CharacterSerializedOccupation();
        this.m_previousPositionPart = new CharacterSerializedPreviousPosition();
        this.m_guildPart = new CharacterSerializedGuild();
        this.m_guildIdPart = new CharacterSerializedGuildId();
        this.m_guildRemoteInfoPart = new CharacterSerializedRemoteGuildInfo();
        this.m_guildLocalInfoPart = new CharacterSerializedLocalGuildInfo();
        this.m_guildInfoForGamePart = new CharacterSerializedGuildInfoForGame();
        this.m_nationIdPart = new CharacterSerializedNationId();
        this.m_characterListNationIdPart = new CharacterSerializedNationId();
        this.m_nationStoragePart = new CharacterSerializedNationStorage();
        this.m_nationSynchroPart = new CharacterSerializedNationSynchro();
        this.m_nationPvpPart = new CharacterSerializedNationPvp();
        this.m_nationPvpMoneyPart = new CharacterSerializedNationPvpMoney();
        this.m_socialStatesPart = new CharacterSerializedSocialStates();
        this.m_petPart = new CharacterSerializedPet();
        this.m_achievementsPart = new CharacterSerializedAchievements();
        this.m_passeportInfoPart = new CharacterSerializedPasseportInfo();
        this.m_statisticsPart = new CharacterSerializedStatistics();
        this.m_dimensionalBagViewInventory = new CharacterSerializedDimensionalBagViewInventory();
        this.m_inventoriesPart = new CharacterSerializedInventories();
        this.m_locksPart = new CharacterSerializedLocks();
        this.m_locksForClientPart = new CharacterSerializedLocksForClient();
        this.m_personalEffectsPart = new CharacterSerializedPersonalEffects();
        this.m_antiAddictionPart = new CharacterSerializedAntiAddiction();
        this.m_visibilityPart = new CharacterSerializedVisibility();
    }
    
    @Override
    public CharacterSerializedGameServer getGameServerPart() {
        return this.m_gameServerPart;
    }
    
    @Override
    public CharacterSerializedHp getHpPart() {
        return this.m_hpPart;
    }
    
    @Override
    public CharacterSerializedXp getXpPart() {
        return this.m_xpPart;
    }
    
    @Override
    public CharacterSerializedXpCharacteristics getXpCharacteristicsPart() {
        return this.m_xpCharacteristicsPart;
    }
    
    @Override
    public CharacterSerializedEquipmentInventory getEquipmentInventoryPart() {
        return this.m_equipmentInventoryPart;
    }
    
    @Override
    public CharacterSerializedSkillInventory getSkillInventoryPart() {
        return this.m_skillInventoryPart;
    }
    
    @Override
    public CharacterSerializedCraft getCraftPart() {
        return this.m_craftPart;
    }
    
    @Override
    public CharacterSerializedAptitudeInventory getAptitudeInventoryPart() {
        return this.m_aptitudeInventoryPart;
    }
    
    @Override
    public CharacterSerializedAptitudeBonusInventory getAptitudeBonusInventoryPart() {
        return this.m_aptitudeBonusInventoryPart;
    }
    
    @Override
    public CharacterSerializedAppearance getAppearancePart() {
        return this.m_appearancePart;
    }
    
    @Override
    public CharacterSerializedBreedSpecific getBreedSpecificPart() {
        return this.m_breedSpecificPart;
    }
    
    @Override
    public CharacterSerializedNationCitizenScore getCitizenPart() {
        return this.m_nationCitizenPart;
    }
    
    @Override
    public CharacterSerializedPasseportInfo getPasseportInfoPart() {
        return this.m_passeportInfoPart;
    }
    
    @Override
    public CharacterSerializedCharacteristics getPublicCharacteristicsPart() {
        return this.m_publicCharacteristicsPart;
    }
    
    @Override
    public CharacterSerializedCharacteristics getPrivateCharacteristicsPart() {
        return this.m_privateCharacteristicsPart;
    }
    
    @Override
    public CharacterSerializedRunningEffectsForSave getRunningEffectsForSavePart() {
        return this.m_runningEffectsForSavePart;
    }
    
    @Override
    public CharacterSerializedRunningEffectsForSave getRunningEffectsForGameToGamePart() {
        return this.m_runningEffectsForGameToGamePart;
    }
    
    @Override
    public CharacterSerializedAccountInformation getAccountInformationPart() {
        return this.m_accountInformationPart;
    }
    
    @Override
    public CharacterSerializedRemoteAccountInformation getRemoteAccountInformationPart() {
        return this.m_remoteAccountInformationPart;
    }
    
    @Override
    public CharacterSerializedDimensionalBagForSave getDimensionalBagForSavePart() {
        return this.m_dimensionalBagForSavePart;
    }
    
    @Override
    public CharacterSerializedDimensionalBagForClient getDimensionalBagForLocalClientPart() {
        return this.m_dimensionalBagForLocalClientPart;
    }
    
    @Override
    public CharacterSerializedRunningEffects getRunningEffectsPart() {
        return this.m_runningEffectsPart;
    }
    
    @Override
    public CharacterSerializedBags getBagsPart() {
        return this.m_bagsPart;
    }
    
    @Override
    public CharacterSerializedEquipmentAppearance getEquipmentAppearancePart() {
        return this.m_equipmentAppearancePart;
    }
    
    @Override
    public CharacterSerializedShortcutInventories getShortcutInventoriesPart() {
        return this.m_shortcutInventoriesPart;
    }
    
    @Override
    public CharacterSerializedEmoteInventory getEmoteInventoryPart() {
        return this.m_emoteInventoryPart;
    }
    
    @Override
    public CharacterSerializedLandMarkInventory getLandMarkInventoryPart() {
        return this.m_landMarkInventoryPart;
    }
    
    @Override
    public CharacterSerializedDiscoveredItemsInventory getDiscoveredItemsPart() {
        return this.m_discoveredItemsPart;
    }
    
    @Override
    public CharacterSerializedScenarioManager getScenarioManagerPart() {
        return this.m_scenarioManagerPart;
    }
    
    @Override
    public CharacterSerializedChallenges getChallengesPart() {
        return this.m_challengesPart;
    }
    
    @Override
    public CharacterSerializedRespawnPoint getRespawnPointPart() {
        return this.m_respawnPointPart;
    }
    
    @Override
    public CharacterSerializedCreationData getCreationDataPart() {
        return this.m_creationDataPart;
    }
    
    @Override
    public CharacterSerializedSpellInventory getSpellInventoryPart() {
        return this.m_spellInventoryPart;
    }
    
    @Override
    public CharacterSerializedTitle getTitlePart() {
        return this.m_titlePart;
    }
    
    @Override
    public CharacterSerializedGroup getGroupPart() {
        return this.m_groupPart;
    }
    
    @Override
    public CharacterSerializedOccupation getOccupationPart() {
        return this.m_occupationPart;
    }
    
    @Override
    public CharacterSerializedPreviousPosition getPreviousPositionPart() {
        return this.m_previousPositionPart;
    }
    
    @Override
    public CharacterSerializedGuild getGuildPart() {
        return this.m_guildPart;
    }
    
    @Override
    public CharacterSerializedGuildId getGuildIdPart() {
        return this.m_guildIdPart;
    }
    
    @Override
    public CharacterSerializedRemoteGuildInfo getGuildRemoteInfoPart() {
        return this.m_guildRemoteInfoPart;
    }
    
    @Override
    public CharacterSerializedLocalGuildInfo getGuildLocalInfoPart() {
        return this.m_guildLocalInfoPart;
    }
    
    @Override
    public CharacterSerializedGuildInfoForGame getGuildInfoForGamePart() {
        return this.m_guildInfoForGamePart;
    }
    
    @Override
    public CharacterSerializedNationId getNationIdPart() {
        return this.m_nationIdPart;
    }
    
    @Override
    public CharacterSerializedNationId getCharacterListNationIdPart() {
        return this.m_characterListNationIdPart;
    }
    
    @Override
    public CharacterSerializedNationStorage getNationStoragePart() {
        return this.m_nationStoragePart;
    }
    
    @Override
    public CharacterSerializedNationSynchro getNationSynchroPart() {
        return this.m_nationSynchroPart;
    }
    
    @Override
    public CharacterSerializedNationPvp getNationPvpPart() {
        return this.m_nationPvpPart;
    }
    
    @Override
    public CharacterSerializedNationPvpMoney getNationPvpMoneyPart() {
        return this.m_nationPvpMoneyPart;
    }
    
    @Override
    public CharacterSerializedSocialStates getSocialStatesPart() {
        return this.m_socialStatesPart;
    }
    
    @Override
    public CharacterSerializedPet getPetPart() {
        return this.m_petPart;
    }
    
    @Override
    public CharacterSerializedAchievements getAchievementsPart() {
        return this.m_achievementsPart;
    }
    
    @Override
    public CharacterSerializedLocksForClient getLockClientPart() {
        return this.m_locksForClientPart;
    }
    
    @Override
    public CharacterSerializedPersonalEffects getPersonalEffectsPart() {
        return this.m_personalEffectsPart;
    }
    
    @Override
    public CharacterSerializedLocks getLockStoragePart() {
        return this.m_locksPart;
    }
    
    @Override
    public CharacterSerializedStatistics getStatisticsPart() {
        return this.m_statisticsPart;
    }
    
    @Override
    public CharacterSerializedDimensionalBagViewInventory getDimensionalBagViewInventoryPart() {
        return this.m_dimensionalBagViewInventory;
    }
    
    @Override
    public CharacterSerializedInventories getInventoriesPart() {
        return this.m_inventoriesPart;
    }
    
    @Override
    public CharacterSerializedAntiAddiction getAntiAddictionPart() {
        return this.m_antiAddictionPart;
    }
    
    @Override
    public CharacterSerializedVisibility getVisibilityPart() {
        return this.m_visibilityPart;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_breedSpecificPart.clear();
        this.m_gameServerPart.clear();
        this.m_hpPart.clear();
        this.m_xpCharacteristicsPart.clear();
        this.m_equipmentInventoryPart.clear();
        this.m_skillInventoryPart.clear();
        this.m_craftPart.clear();
        this.m_aptitudeInventoryPart.clear();
        this.m_appearancePart.clear();
        this.m_equipmentAppearancePart.clear();
        this.m_publicCharacteristicsPart.clear();
        this.m_privateCharacteristicsPart.clear();
        this.m_runningEffectsForSavePart.clear();
        this.m_spellInventoryPart.clear();
        this.m_accountInformationPart.clear();
        this.m_remoteAccountInformationPart.clear();
        this.m_dimensionalBagForSavePart.clear();
        this.m_dimensionalBagForLocalClientPart.clear();
        this.m_runningEffectsPart.clear();
        this.m_bagsPart.clear();
        this.m_shortcutInventoriesPart.clear();
        this.m_scenarioManagerPart.clear();
        this.m_challengesPart.clear();
        this.m_respawnPointPart.clear();
        this.m_creationDataPart.clear();
        this.m_titlePart.clear();
        this.m_groupPart.clear();
        this.m_occupationPart.clear();
        this.m_guildPart.clear();
        this.m_guildIdPart.clear();
        this.m_nationIdPart.clear();
        this.m_nationStoragePart.clear();
        this.m_nationSynchroPart.clear();
        this.m_nationCitizenPart.clear();
        this.m_petPart.clear();
        this.m_achievementsPart.clear();
        this.m_socialStatesPart.clear();
        this.m_emoteInventoryPart.clear();
        this.m_landMarkInventoryPart.clear();
        this.m_discoveredItemsPart.clear();
        this.m_characterListNationIdPart.clear();
        this.m_passeportInfoPart.clear();
        this.m_statisticsPart.clear();
        this.m_dimensionalBagViewInventory.clear();
        this.m_inventoriesPart.clear();
        this.m_locksPart.clear();
        this.m_antiAddictionPart.clear();
        this.m_visibilityPart.clear();
    }
}
