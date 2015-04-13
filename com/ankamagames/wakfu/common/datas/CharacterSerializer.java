package com.ankamagames.wakfu.common.datas;

import org.apache.log4j.*;
import java.nio.*;
import java.util.*;

public abstract class CharacterSerializer
{
    protected static final Logger m_logger;
    private final CharacterSerializedId m_idPart;
    private final CharacterSerializedIdentity m_identityPart;
    private final CharacterSerializedName m_namePart;
    private final CharacterSerializedBreed m_breedPart;
    private final CharacterSerializedSex m_sexPart;
    private final CharacterSerializedPosition m_positionPart;
    private final CharacterSerializedFight m_fightPart;
    private final CharacterSerializedCharacteristics m_fightCharacteristicsPart;
    private final CharacterSerializedCharacteristics m_allCharacteristicsPart;
    private final CharacterSerializedProperties m_worldPropertiesPart;
    private final CharacterSerializedProperties m_fightPropertiesPart;
    private final CharacterSerializedControlledByAI m_controlledByAIPart;
    private final CharacterSerializedCurrentMovementPath m_currentMovementPathPart;
    
    public CharacterSerializer() {
        super();
        this.m_idPart = new CharacterSerializedId();
        this.m_identityPart = new CharacterSerializedIdentity();
        this.m_namePart = new CharacterSerializedName();
        this.m_breedPart = new CharacterSerializedBreed();
        this.m_sexPart = new CharacterSerializedSex();
        this.m_positionPart = new CharacterSerializedPosition();
        this.m_fightPart = new CharacterSerializedFight();
        this.m_fightCharacteristicsPart = new CharacterSerializedCharacteristics();
        this.m_allCharacteristicsPart = new CharacterSerializedCharacteristics();
        this.m_worldPropertiesPart = new CharacterSerializedProperties();
        this.m_fightPropertiesPart = new CharacterSerializedProperties();
        this.m_controlledByAIPart = new CharacterSerializedControlledByAI();
        this.m_currentMovementPathPart = new CharacterSerializedCurrentMovementPath();
    }
    
    public void clear() {
        this.m_idPart.clear();
        this.m_identityPart.clear();
        this.m_namePart.clear();
        this.m_breedPart.clear();
        this.m_sexPart.clear();
        this.m_positionPart.clear();
        this.m_fightPart.clear();
        this.m_fightCharacteristicsPart.clear();
        this.m_allCharacteristicsPart.clear();
        this.m_worldPropertiesPart.clear();
        this.m_fightPropertiesPart.clear();
        this.m_controlledByAIPart.clear();
        this.m_currentMovementPathPart.clear();
    }
    
    public final CharacterSerializedName getNamePart() {
        return this.m_namePart;
    }
    
    public final CharacterSerializedIdentity getIdentityPart() {
        return this.m_identityPart;
    }
    
    public final CharacterSerializedId getIdPart() {
        return this.m_idPart;
    }
    
    public final CharacterSerializedBreed getBreedPart() {
        return this.m_breedPart;
    }
    
    public CharacterSerializedSex getSexPart() {
        return this.m_sexPart;
    }
    
    public final CharacterSerializedPosition getPositionPart() {
        return this.m_positionPart;
    }
    
    public final CharacterSerializedFight getFightPart() {
        return this.m_fightPart;
    }
    
    public final CharacterSerializedCharacteristics getFightCharacteristicsPart() {
        return this.m_fightCharacteristicsPart;
    }
    
    public final CharacterSerializedCharacteristics getAllCharacteristicsPart() {
        return this.m_allCharacteristicsPart;
    }
    
    public final CharacterSerializedCurrentMovementPath getCurrentMovementPathPart() {
        return this.m_currentMovementPathPart;
    }
    
    public final CharacterSerializedProperties getWorldPropertiesPart() {
        return this.m_worldPropertiesPart;
    }
    
    public final CharacterSerializedProperties getFightPropertiesPart() {
        return this.m_fightPropertiesPart;
    }
    
    public final CharacterSerializedControlledByAI getControlledByAIPart() {
        return this.m_controlledByAIPart;
    }
    
    final CharacterSerializedPart getPart(final Part part) {
        switch (part) {
            case ACCOUNT_INFORMATION: {
                return this.getAccountInformationPart();
            }
            case ACCOUNT_INFORMATION_REMOTE: {
                return this.getRemoteAccountInformationPart();
            }
            case APPEARANCE: {
                return this.getAppearancePart();
            }
            case BAGS: {
                return this.getBagsPart();
            }
            case BREED: {
                return this.getBreedPart();
            }
            case SEX: {
                return this.getSexPart();
            }
            case BREED_SPECIFIC: {
                return this.getBreedSpecificPart();
            }
            case CHALLENGES: {
                return this.getChallengesPart();
            }
            case CITIZEN_POINT: {
                return this.getCitizenPart();
            }
            case PASSEPORT_INFO: {
                return this.getPasseportInfoPart();
            }
            case CONTROLLED_BY_AI: {
                return this.getControlledByAIPart();
            }
            case CRAFT: {
                return this.getCraftPart();
            }
            case CREATION_DATA: {
                return this.getCreationDataPart();
            }
            case CURRENT_MOVEMENT_PATH: {
                return this.getCurrentMovementPathPart();
            }
            case DIMENSIONAL_BAG_FOR_SAVE: {
                return this.getDimensionalBagForSavePart();
            }
            case DIMENSIONAL_BAG_FOR_LOCAL_CLIENT: {
                return this.getDimensionalBagForLocalClientPart();
            }
            case EQUIPMENT_APPEARANCE: {
                return this.getEquipmentAppearancePart();
            }
            case EQUIPMENT_INVENTORY: {
                return this.getEquipmentInventoryPart();
            }
            case FIGHT: {
                return this.getFightPart();
            }
            case FIGHT_PROPERTIES: {
                return this.getFightPropertiesPart();
            }
            case FIGHT_CHARACTERISTICS: {
                return this.getFightCharacteristicsPart();
            }
            case ALL_CHARACTERISTICS: {
                return this.getAllCharacteristicsPart();
            }
            case GAME_SERVER: {
                return this.getGameServerPart();
            }
            case GIFT_TOKEN_ACCOUNT_INVENTORY: {
                return this.getGiftTokenAccountInventoryPart();
            }
            case GROUP: {
                return this.getGroupPart();
            }
            case GUILD_ID: {
                return this.getGuildIdPart();
            }
            case GUILD_REMOTE_INFO: {
                return this.getGuildRemoteInfoPart();
            }
            case GUILD_LOCAL_INFO: {
                return this.getGuildLocalInfoPart();
            }
            case GUILD_INFO_FOR_GAME: {
                return this.getGuildInfoForGamePart();
            }
            case NATION_STORAGE: {
                return this.getNationStoragePart();
            }
            case NATION_SYNCHRO: {
                return this.getNationSynchroPart();
            }
            case NATION_ID: {
                return this.getNationIdPart();
            }
            case NATION_PVP: {
                return this.getNationPvpPart();
            }
            case NATION_PVP_MONEY: {
                return this.getNationPvpMoneyPart();
            }
            case CHARACTER_LIST_NATION_ID: {
                return this.getCharacterListNationIdPart();
            }
            case SOCIAL_STATES: {
                return this.getSocialStatesPart();
            }
            case TEMPLATE: {
                return this.getTemplatePart();
            }
            case COLLECT: {
                return this.getCollectPart();
            }
            case HP: {
                return this.getHpPart();
            }
            case ID: {
                return this.getIdPart();
            }
            case IDENTITY: {
                return this.getIdentityPart();
            }
            case NAME: {
                return this.getNamePart();
            }
            case OCCUPATION: {
                return this.getOccupationPart();
            }
            case POSITION: {
                return this.getPositionPart();
            }
            case PREVIOUS_POSITION: {
                return this.getPreviousPositionPart();
            }
            case PRIVATE_CHARACTERISTICS: {
                return this.getPrivateCharacteristicsPart();
            }
            case PUBLIC_CHARACTERISTICS: {
                return this.getPublicCharacteristicsPart();
            }
            case RESPAWN_POINT: {
                return this.getRespawnPointPart();
            }
            case RUNNING_EFFECTS: {
                return this.getRunningEffectsPart();
            }
            case RUNNING_EFFECTS_FOR_SAVE: {
                return this.getRunningEffectsForSavePart();
            }
            case RUNNING_EFFECTS_FOR_GAME_TO_GAME: {
                return this.getRunningEffectsForGameToGamePart();
            }
            case SCENARIO_MANAGER: {
                return this.getScenarioManagerPart();
            }
            case SHORTCUT_INVENTORIES: {
                return this.getShortcutInventoriesPart();
            }
            case EMOTE_INVENTORY: {
                return this.getEmoteInventoryPart();
            }
            case LANDMARK_INVENTORY: {
                return this.getLandMarkInventoryPart();
            }
            case DISCOVERED_ITEMS: {
                return this.getDiscoveredItemsPart();
            }
            case SKILL_INVENTORY: {
                return this.getSkillInventoryPart();
            }
            case APTITUDE_INVENTORY: {
                return this.getAptitudeInventoryPart();
            }
            case APTITUDE_BONUS_INVENTORY: {
                return this.getAptitudeBonusInventoryPart();
            }
            case SPELL_INVENTORY: {
                return this.getSpellInventoryPart();
            }
            case TITLES: {
                return this.getTitlePart();
            }
            case WORLD_PROPERTIES: {
                return this.getWorldPropertiesPart();
            }
            case XP: {
                return this.getXpPart();
            }
            case XP_CHARACTERISTICS: {
                return this.getXpCharacteristicsPart();
            }
            case PET: {
                return this.getPetPart();
            }
            case ACHIEVEMENTS: {
                return this.getAchievementsPart();
            }
            case STATISTICS_FOR_WORLD_SERVER: {
                return this.getStatisticsPart();
            }
            case DIMENSIONAL_BAG_VIEWS_INVENTORY: {
                return this.getDimensionalBagViewInventoryPart();
            }
            case INVENTORIES: {
                return this.getInventoriesPart();
            }
            case LOCK_STORAGE: {
                return this.getLockStoragePart();
            }
            case LOCK_TO_CLIENT: {
                return this.getLockClientPart();
            }
            case PERSONAL_EFFECTS: {
                return this.getPersonalEffectsPart();
            }
            case ANTI_ADDICTION: {
                return this.getAntiAddictionPart();
            }
            case COMPANION_CONTROLLER_ID: {
                return this.getCompanionControllerIdPart();
            }
            case VISIBILITY: {
                return this.getVisibilityPart();
            }
            default: {
                return null;
            }
        }
    }
    
    public final byte[] build(final SerializationType serializationType) {
        int totalSize = 1;
        for (final Part part : serializationType.getParts()) {
            final CharacterSerializedPart characterPart = this.getPart(part);
            if (characterPart != null) {
                try {
                    characterPart.getBinarPart().grabDataFromSource();
                }
                catch (Exception e) {
                    CharacterSerializer.m_logger.error((Object)("Exception lors du callback de s\u00e9rialisation de la part " + part + " : "), (Throwable)e);
                }
                totalSize += characterPart.serializedSize();
            }
            else {
                CharacterSerializer.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration de la CharacterSerializedPart correspondant \u00e0 " + part + " de la forme " + serializationType));
            }
        }
        final ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.put((byte)serializationType.ordinal());
        for (final Part part2 : serializationType.getParts()) {
            final CharacterSerializedPart characterPart2 = this.getPart(part2);
            if (characterPart2 != null && !characterPart2.serialize(buffer)) {
                CharacterSerializer.m_logger.error((Object)("Erreur lors de la s\u00e9rialisation de la part " + part2 + " de la forme " + serializationType));
            }
        }
        return buffer.array();
    }
    
    public final void fromBuild(final byte[] data) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        final int serializationNumber = buffer.get() & 0xFF;
        if (serializationNumber < 0 || serializationNumber >= SerializationType.values().length) {
            CharacterSerializer.m_logger.error((Object)("Num\u00e9ro de part invalide : " + serializationNumber));
            return;
        }
        final SerializationType serializationType = SerializationType.values()[serializationNumber];
        for (final Part part : serializationType.getParts()) {
            final CharacterSerializedPart characterPart = this.getPart(part);
            if (characterPart != null) {
                characterPart.unserialize(buffer);
                try {
                    characterPart.getBinarPart().notifyListener();
                }
                catch (Exception e) {
                    CharacterSerializer.m_logger.error((Object)("Exception lors du callback de d\u00e9s\u00e9rialisation de la part " + part + ": "), (Throwable)e);
                }
            }
            else {
                CharacterSerializer.m_logger.error((Object)("Impossible de trouver la CharacterPart correspondant \u00e0 " + part));
            }
        }
    }
    
    public final void extractPartFromBuild(final CharacterSerializedPart selectedPart, final byte[] data) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        final int serializationNumber = buffer.get() & 0xFF;
        if (serializationNumber < 0 || serializationNumber >= SerializationType.values().length) {
            CharacterSerializer.m_logger.error((Object)("Num\u00e9ro de part invalide : " + serializationNumber));
            return;
        }
        final SerializationType serializationType = SerializationType.values()[serializationNumber];
        try {
            for (final Part part : serializationType.getParts()) {
                CharacterSerializedPart characterPart = this.getPart(part);
                Label_0210: {
                    if (characterPart != null) {
                        if (characterPart == selectedPart) {
                            characterPart.unserialize(buffer);
                            try {
                                characterPart.getBinarPart().notifyListener();
                                return;
                            }
                            catch (Exception e) {
                                CharacterSerializer.m_logger.error((Object)("Exception lors du callback de d\u00e9s\u00e9rialisation de la part " + part), (Throwable)e);
                                break Label_0210;
                            }
                        }
                        characterPart = (CharacterSerializedPart)characterPart.getClass().newInstance();
                        characterPart.unserialize(buffer);
                    }
                    else {
                        CharacterSerializer.m_logger.error((Object)("Impossible de trouver la CharacterPart correspondant \u00e0 " + part));
                    }
                }
            }
        }
        catch (InstantiationException e2) {
            CharacterSerializer.m_logger.error((Object)e2);
        }
        catch (IllegalAccessException e3) {
            CharacterSerializer.m_logger.error((Object)e3);
        }
    }
    
    public final void extractPartsFromBuild(final Set<CharacterSerializedPart> selectedParts, final byte[] data) {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        final int serializationNumber = buffer.get() & 0xFF;
        if (serializationNumber < 0 || serializationNumber >= SerializationType.values().length) {
            CharacterSerializer.m_logger.error((Object)("Num\u00e9ro de part invalide : " + serializationNumber));
            return;
        }
        final SerializationType serializationType = SerializationType.values()[serializationNumber];
        try {
            for (final Part part : serializationType.getParts()) {
                CharacterSerializedPart characterPart = this.getPart(part);
                if (characterPart != null) {
                    if (selectedParts.remove(characterPart)) {
                        characterPart.unserialize(buffer);
                        try {
                            characterPart.getBinarPart().notifyListener();
                            if (selectedParts.isEmpty()) {
                                return;
                            }
                        }
                        catch (Exception e) {
                            CharacterSerializer.m_logger.error((Object)("Exception lors du callback de d\u00e9s\u00e9rialisation de la part " + part), (Throwable)e);
                        }
                    }
                    else {
                        characterPart = (CharacterSerializedPart)characterPart.getClass().newInstance();
                        characterPart.unserialize(buffer);
                    }
                }
                else {
                    CharacterSerializer.m_logger.error((Object)("Impossible de trouver la CharacterPart correspondant \u00e0 " + part));
                }
            }
        }
        catch (InstantiationException e2) {
            CharacterSerializer.m_logger.error((Object)e2);
        }
        catch (IllegalAccessException e3) {
            CharacterSerializer.m_logger.error((Object)e3);
        }
    }
    
    public CharacterSerializedPart getAppearancePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getEquipmentInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getPublicCharacteristicsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getPrivateCharacteristicsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getScenarioManagerPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getSkillInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getAptitudeInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getAptitudeBonusInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getBreedSpecificPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getCitizenPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getPasseportInfoPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getBagsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getRespawnPointPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getEmoteInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getLandMarkInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getDiscoveredItemsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getShortcutInventoriesPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getDimensionalBagForSavePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getDimensionalBagForLocalClientPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getRunningEffectsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getCraftPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getRunningEffectsForSavePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getRunningEffectsForGameToGamePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getSpellInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getHpPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getXpPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getXpCharacteristicsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getEquipmentAppearancePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getAccountInformationPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getRemoteAccountInformationPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGameServerPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getChallengesPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getCreationDataPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGroupPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getTemplatePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getCollectPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getCompanionControllerIdPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getVisibilityPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getTitlePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getOccupationPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getPreviousPositionPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGiftTokenAccountInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGuildPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGuildIdPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGuildRemoteInfoPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGuildLocalInfoPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getGuildInfoForGamePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getNationStoragePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getNationSynchroPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getNationIdPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getNationPvpPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getNationPvpMoneyPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getCharacterListNationIdPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getSocialStatesPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getPetPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getAchievementsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getLockStoragePart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getLockClientPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getPersonalEffectsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getStatisticsPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getDimensionalBagViewInventoryPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getInventoriesPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public CharacterSerializedPart getAntiAddictionPart() {
        return CharacterSerializedPart.EMPTY;
    }
    
    public byte[] serializeForPropertiesUpdate(final boolean withFightPart) {
        return withFightPart ? this.build(SerializationType.FOR_PROPERTIES_UPDATE_WITH_FIGHT) : this.build(SerializationType.FOR_PROPERTIES_UPDATE_WITHOUT_FIGHT);
    }
    
    public byte[] serializeForCharacterPositionInformation() {
        return this.build(SerializationType.FOR_CHARACTER_POSITION_INFORMATION);
    }
    
    public byte[] serializeForRemoteCharacterInformation() {
        return this.build(SerializationType.FOR_REMOTE_CHARACTER_INFORMATION);
    }
    
    public byte[] serializeForRemoteUpdateCharacterInformation() {
        return this.build(SerializationType.FOR_REMOTE_UPDATE_CHARACTER_INFORMATION);
    }
    
    public byte[] serializeForCharacterCreationInformation() {
        return this.build(SerializationType.FOR_CHARACTER_CREATION_INFORMATION);
    }
    
    public byte[] serialize() {
        return this.build(SerializationType.TOTAL);
    }
    
    public byte[] serializeFighterDatas() {
        return this.build(SerializationType.FIGHTER_DATAS);
    }
    
    public byte[] serializeFighterDataForReconnection() {
        return this.build(SerializationType.FIGHTER_DATA_FOR_RECONNECTION);
    }
    
    public byte[] serializePublicCharacteristics() {
        return this.build(SerializationType.PUBLIC_CHARACTERISTICS);
    }
    
    public byte[] serializeAllCharacteristics() {
        return this.build(SerializationType.ALL_CHARACTERISTICS);
    }
    
    public final byte[] serializeInventories() {
        return this.build(SerializationType.INVENTORIES);
    }
    
    public final byte[] serializeForAccountInformationUpdate() {
        return this.build(SerializationType.FOR_ACCOUNT_INFORMATION_UPDATE);
    }
    
    public final byte[] serializeForAntiAddictionUpdate() {
        return this.build(SerializationType.FOR_ANTI_ADDICTION);
    }
    
    public final byte[] serializeForAptitudeBonusInventory() {
        return this.build(SerializationType.APTITUDE_BONUS_INVENTORY);
    }
    
    public final byte[] serializeForRemoteAccountInformationUpdate() {
        return this.build(SerializationType.FOR_REMOTE_ACCOUNT_INFORMATION_UPDATE);
    }
    
    public final byte[] serializeForRemotePetUpdate() {
        return this.build(SerializationType.FOR_REMOTE_PET_UPDATE);
    }
    
    public final byte[] serializeForDiscoveredItemsUpdate() {
        return this.build(SerializationType.FOR_DISCOVERED_ITEMS_UPDATE);
    }
    
    public byte[] serializeForCharacterList() {
        return this.build(SerializationType.FOR_CHARACTER_LIST);
    }
    
    public byte[] serializeForLocalCharacterInformation() {
        return this.build(SerializationType.FOR_LOCAL_CHARACTER_INFORMATION);
    }
    
    public byte[] serializeForHeroLoad() {
        return this.build(SerializationType.FOR_HERO_CHARACTER_LOAD);
    }
    
    public byte[] serializeForGameToGameServerExchange() {
        return this.build(SerializationType.FOR_GAME_TO_GAME_SERVER_EXCHANGE);
    }
    
    public byte[] serializeForGameToGameServerHeroExchange() {
        return this.build(SerializationType.FOR_GAME_TO_GAME_SERVER_HERO_EXCHANGE);
    }
    
    public byte[] serializeForWorldToGameServerExchange() {
        return this.build(SerializationType.FOR_WORLD_TO_GAME_SERVER_EXCHANGE);
    }
    
    public byte[] serializeForWorldToGameServerHero() {
        return this.build(SerializationType.FOR_WORLD_TO_GAME_SERVER_HERO);
    }
    
    public byte[] serializeForWorldToGlobalExchange() {
        return this.build(SerializationType.FOR_WORLD_TO_GLOBAL_EXCHANGE);
    }
    
    public byte[] serializeForGlobalToGameServerExchange() {
        return this.build(SerializationType.FOR_GLOBAL_TO_GAME_SERVER_EXCHANGE);
    }
    
    public byte[] serializeForNationPvp() {
        return this.build(SerializationType.FOR_NATION_PVP_UPDATE);
    }
    
    public byte[] serializeForGlobalToLocalClient() {
        return this.build(SerializationType.FOR_GLOBAL_TO_LOCAL_CHARACTER);
    }
    
    public byte[] serializeForGlobalToWorldExchange() {
        return this.build(SerializationType.FOR_GLOBAL_TO_WORLD_EXCHANGE);
    }
    
    public byte[] serializeForAIServerInformation() {
        return this.build(SerializationType.FOR_AI_SERVER_INFORMATION);
    }
    
    public byte[] serializeForPersonalEffects() {
        return this.build(SerializationType.FOR_PERSONAL_EFFECTS);
    }
    
    public String getSerializationRepresentation(final byte[] encoded) {
        final StringBuilder result = new StringBuilder("[ ");
        result.append(this.getClass().getSimpleName()).append(" serialization=").append(encoded.length).append(" byte(s) in ");
        final ByteBuffer buffer = ByteBuffer.wrap(encoded);
        final int serializationNumber = buffer.get() & 0xFF;
        if (serializationNumber < 0 || serializationNumber >= SerializationType.values().length) {
            return "[ERROR: invalid serialization type: " + serializationNumber + "]";
        }
        final SerializationType serializationType = SerializationType.values()[serializationNumber];
        result.append(serializationType).append(", parts=");
        for (final Part part : serializationType.getParts()) {
            final CharacterSerializedPart characterPart = this.getPart(part);
            if (characterPart == null) {
                return "[ERROR: no character part for " + part + "]";
            }
            result.append("{").append(part).append(" ");
            final int start = buffer.position();
            characterPart.unserialize(buffer);
            for (int i = start; i < buffer.position(); ++i) {
                result.append(String.format("%02x", encoded[i]));
                if ((i - start) % 4 == 3 && i < buffer.position() - 1) {
                    result.append(".");
                }
            }
            result.append("} ");
        }
        result.append("] ").append(buffer.remaining()).append(" byte(s) left.");
        return result.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterSerializer.class);
    }
    
    public enum SerializationType
    {
        TOTAL(Part.values()), 
        FOR_CHARACTER_CREATION_INFORMATION(new Part[] { Part.IDENTITY, Part.NAME, Part.BREED, Part.APPEARANCE, Part.CREATION_DATA }), 
        FIGHTER_DATAS(new Part[] { Part.POSITION, Part.FIGHT, Part.FIGHT_CHARACTERISTICS, Part.FIGHT_PROPERTIES, Part.BREED, Part.CONTROLLED_BY_AI, Part.PUBLIC_CHARACTERISTICS, Part.ID, Part.IDENTITY }), 
        INVENTORIES(new Part[] { Part.EQUIPMENT_INVENTORY, Part.BAGS }), 
        FOR_CHARACTER_LIST(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.APPEARANCE, Part.EQUIPMENT_APPEARANCE, Part.CREATION_DATA, Part.XP, Part.CHARACTER_LIST_NATION_ID }), 
        FOR_GAME_TO_GAME_SERVER_EXCHANGE(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.POSITION, Part.PREVIOUS_POSITION, Part.APPEARANCE, Part.SPELL_INVENTORY, Part.SHORTCUT_INVENTORIES, Part.EMOTE_INVENTORY, Part.LANDMARK_INVENTORY, Part.DISCOVERED_ITEMS, Part.INVENTORIES, Part.BAGS, Part.EQUIPMENT_INVENTORY, Part.BREED_SPECIFIC, Part.SKILL_INVENTORY, Part.CRAFT, Part.APTITUDE_INVENTORY, Part.DIMENSIONAL_BAG_FOR_SAVE, Part.RUNNING_EFFECTS_FOR_SAVE, Part.RUNNING_EFFECTS_FOR_GAME_TO_GAME, Part.HP, Part.GAME_SERVER, Part.SCENARIO_MANAGER, Part.ACCOUNT_INFORMATION, Part.RESPAWN_POINT, Part.XP, Part.XP_CHARACTERISTICS, Part.TITLES, Part.GROUP, Part.TEMPLATE, Part.GIFT_TOKEN_ACCOUNT_INVENTORY, Part.GIFT_TOKEN_ACCOUNT_INVENTORY, Part.CITIZEN_POINT, Part.PASSEPORT_INFO, Part.SOCIAL_STATES, Part.STATISTICS_FOR_WORLD_SERVER, Part.ACHIEVEMENTS, Part.LOCK_STORAGE, Part.DIMENSIONAL_BAG_VIEWS_INVENTORY, Part.NATION_ID, Part.ANTI_ADDICTION, Part.VISIBILITY, Part.OCCUPATION, Part.APTITUDE_BONUS_INVENTORY, Part.CREATION_DATA }), 
        FOR_GAME_TO_GAME_SERVER_HERO_EXCHANGE(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.POSITION, Part.PREVIOUS_POSITION, Part.APPEARANCE, Part.SPELL_INVENTORY, Part.SHORTCUT_INVENTORIES, Part.EMOTE_INVENTORY, Part.LANDMARK_INVENTORY, Part.DISCOVERED_ITEMS, Part.INVENTORIES, Part.BAGS, Part.EQUIPMENT_INVENTORY, Part.BREED_SPECIFIC, Part.SKILL_INVENTORY, Part.CRAFT, Part.APTITUDE_INVENTORY, Part.DIMENSIONAL_BAG_FOR_SAVE, Part.RUNNING_EFFECTS_FOR_SAVE, Part.RUNNING_EFFECTS_FOR_GAME_TO_GAME, Part.HP, Part.GAME_SERVER, Part.SCENARIO_MANAGER, Part.RESPAWN_POINT, Part.XP, Part.XP_CHARACTERISTICS, Part.TITLES, Part.GROUP, Part.GIFT_TOKEN_ACCOUNT_INVENTORY, Part.GIFT_TOKEN_ACCOUNT_INVENTORY, Part.CITIZEN_POINT, Part.PASSEPORT_INFO, Part.SOCIAL_STATES, Part.STATISTICS_FOR_WORLD_SERVER, Part.ACHIEVEMENTS, Part.LOCK_STORAGE, Part.DIMENSIONAL_BAG_VIEWS_INVENTORY, Part.NATION_ID, Part.ANTI_ADDICTION, Part.VISIBILITY, Part.OCCUPATION, Part.APTITUDE_BONUS_INVENTORY, Part.CREATION_DATA }), 
        FOR_LOCAL_CHARACTER_INFORMATION(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.HP, Part.POSITION, Part.APPEARANCE, Part.SHORTCUT_INVENTORIES, Part.EMOTE_INVENTORY, Part.LANDMARK_INVENTORY, Part.DISCOVERED_ITEMS, Part.SPELL_INVENTORY, Part.INVENTORIES, Part.EQUIPMENT_INVENTORY, Part.BAGS, Part.BREED_SPECIFIC, Part.SKILL_INVENTORY, Part.CRAFT, Part.APTITUDE_INVENTORY, Part.RUNNING_EFFECTS, Part.DIMENSIONAL_BAG_FOR_LOCAL_CLIENT, Part.CHALLENGES, Part.XP, Part.XP_CHARACTERISTICS, Part.TITLES, Part.CITIZEN_POINT, Part.PASSEPORT_INFO, Part.SOCIAL_STATES, Part.PET, Part.ACHIEVEMENTS, Part.ACCOUNT_INFORMATION, Part.LOCK_TO_CLIENT, Part.DIMENSIONAL_BAG_VIEWS_INVENTORY, Part.PERSONAL_EFFECTS, Part.ANTI_ADDICTION, Part.WORLD_PROPERTIES, Part.VISIBILITY, Part.OCCUPATION, Part.APTITUDE_BONUS_INVENTORY }), 
        FOR_REMOTE_CHARACTER_INFORMATION(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.POSITION, Part.APPEARANCE, Part.PUBLIC_CHARACTERISTICS, Part.FIGHT_PROPERTIES, Part.FIGHT, Part.EQUIPMENT_APPEARANCE, Part.RUNNING_EFFECTS, Part.CURRENT_MOVEMENT_PATH, Part.WORLD_PROPERTIES, Part.GROUP, Part.TEMPLATE, Part.COLLECT, Part.PET, Part.OCCUPATION, Part.XP, Part.XP_CHARACTERISTICS, Part.CITIZEN_POINT, Part.GUILD_REMOTE_INFO, Part.NATION_ID, Part.NATION_SYNCHRO, Part.SOCIAL_STATES, Part.ACCOUNT_INFORMATION_REMOTE, Part.COMPANION_CONTROLLER_ID, Part.VISIBILITY }), 
        FOR_REMOTE_UPDATE_CHARACTER_INFORMATION(new Part[] { Part.NATION_ID, Part.NATION_SYNCHRO, Part.GUILD_REMOTE_INFO }), 
        FOR_HERO_CHARACTER_LOAD(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.HP, Part.APPEARANCE, Part.SHORTCUT_INVENTORIES, Part.EMOTE_INVENTORY, Part.LANDMARK_INVENTORY, Part.DISCOVERED_ITEMS, Part.SPELL_INVENTORY, Part.INVENTORIES, Part.EQUIPMENT_INVENTORY, Part.BAGS, Part.BREED_SPECIFIC, Part.SKILL_INVENTORY, Part.CRAFT, Part.APTITUDE_INVENTORY, Part.RUNNING_EFFECTS, Part.DIMENSIONAL_BAG_FOR_LOCAL_CLIENT, Part.CHALLENGES, Part.XP, Part.XP_CHARACTERISTICS, Part.TITLES, Part.CITIZEN_POINT, Part.PASSEPORT_INFO, Part.SOCIAL_STATES, Part.PET, Part.ACHIEVEMENTS, Part.LOCK_TO_CLIENT, Part.DIMENSIONAL_BAG_VIEWS_INVENTORY, Part.PERSONAL_EFFECTS, Part.ANTI_ADDICTION, Part.WORLD_PROPERTIES, Part.VISIBILITY, Part.OCCUPATION, Part.APTITUDE_BONUS_INVENTORY }), 
        FOR_PASSEPORT_INFO_UPDATE(new Part[] { Part.PASSEPORT_INFO }), 
        FOR_APTITUDE_INVENTORY_UPDATE(new Part[] { Part.APTITUDE_INVENTORY }), 
        FOR_CITIZEN_SCORE_UPDATE(new Part[] { Part.CITIZEN_POINT }), 
        FOR_NATION_PVP_UPDATE(new Part[] { Part.NATION_PVP }), 
        FOR_WORLD_TO_GAME_SERVER_EXCHANGE(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.POSITION, Part.APPEARANCE, Part.SPELL_INVENTORY, Part.SHORTCUT_INVENTORIES, Part.EMOTE_INVENTORY, Part.LANDMARK_INVENTORY, Part.DISCOVERED_ITEMS, Part.INVENTORIES, Part.EQUIPMENT_INVENTORY, Part.BAGS, Part.BREED_SPECIFIC, Part.SKILL_INVENTORY, Part.CRAFT, Part.APTITUDE_INVENTORY, Part.DIMENSIONAL_BAG_FOR_SAVE, Part.RUNNING_EFFECTS_FOR_SAVE, Part.HP, Part.GAME_SERVER, Part.SCENARIO_MANAGER, Part.ACCOUNT_INFORMATION, Part.RESPAWN_POINT, Part.XP, Part.XP_CHARACTERISTICS, Part.TITLES, Part.GROUP, Part.GIFT_TOKEN_ACCOUNT_INVENTORY, Part.NATION_ID, Part.CITIZEN_POINT, Part.PASSEPORT_INFO, Part.ACHIEVEMENTS, Part.LOCK_STORAGE, Part.DIMENSIONAL_BAG_VIEWS_INVENTORY, Part.ANTI_ADDICTION, Part.APTITUDE_BONUS_INVENTORY, Part.CREATION_DATA }), 
        FOR_WORLD_TO_GAME_SERVER_HERO(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.POSITION, Part.APPEARANCE, Part.SPELL_INVENTORY, Part.SHORTCUT_INVENTORIES, Part.EMOTE_INVENTORY, Part.LANDMARK_INVENTORY, Part.DISCOVERED_ITEMS, Part.INVENTORIES, Part.EQUIPMENT_INVENTORY, Part.BAGS, Part.BREED_SPECIFIC, Part.SKILL_INVENTORY, Part.CRAFT, Part.APTITUDE_INVENTORY, Part.DIMENSIONAL_BAG_FOR_SAVE, Part.RUNNING_EFFECTS_FOR_SAVE, Part.HP, Part.GAME_SERVER, Part.SCENARIO_MANAGER, Part.RESPAWN_POINT, Part.XP, Part.XP_CHARACTERISTICS, Part.TITLES, Part.GIFT_TOKEN_ACCOUNT_INVENTORY, Part.NATION_ID, Part.CITIZEN_POINT, Part.ACHIEVEMENTS, Part.LOCK_STORAGE, Part.DIMENSIONAL_BAG_VIEWS_INVENTORY, Part.APTITUDE_BONUS_INVENTORY, Part.CREATION_DATA }), 
        FOR_WORLD_TO_GLOBAL_EXCHANGE(new Part[] { Part.ID, Part.IDENTITY, Part.NAME, Part.BREED, Part.SEX, Part.POSITION, Part.HP, Part.GAME_SERVER, Part.ACCOUNT_INFORMATION, Part.RESPAWN_POINT, Part.XP, Part.GROUP, Part.TEMPLATE, Part.NATION_ID, Part.NATION_STORAGE, Part.CITIZEN_POINT, Part.GUILD_ID, Part.CREATION_DATA }), 
        FOR_GLOBAL_TO_GAME_SERVER_EXCHANGE(new Part[] { Part.NATION_ID, Part.NATION_SYNCHRO, Part.GUILD_REMOTE_INFO, Part.GUILD_INFO_FOR_GAME, Part.NATION_PVP_MONEY }), 
        FOR_GLOBAL_TO_LOCAL_CHARACTER(new Part[] { Part.NATION_ID, Part.NATION_SYNCHRO, Part.GUILD_LOCAL_INFO, Part.NATION_PVP_MONEY }), 
        FOR_GLOBAL_TO_WORLD_EXCHANGE(new Part[] { Part.ID, Part.NATION_ID, Part.NATION_STORAGE, Part.GUILD_ID }), 
        FOR_AI_SERVER_INFORMATION(new Part[] { Part.ID, Part.IDENTITY, Part.BREED, Part.PUBLIC_CHARACTERISTICS, Part.CONTROLLED_BY_AI, Part.XP, Part.XP_CHARACTERISTICS }), 
        FOR_ACCOUNT_INFORMATION_UPDATE(new Part[] { Part.ACCOUNT_INFORMATION }), 
        FOR_REMOTE_ACCOUNT_INFORMATION_UPDATE(new Part[] { Part.ACCOUNT_INFORMATION_REMOTE }), 
        FOR_PROPERTIES_UPDATE_WITH_FIGHT(new Part[] { Part.WORLD_PROPERTIES, Part.FIGHT_PROPERTIES }), 
        FOR_PROPERTIES_UPDATE_WITHOUT_FIGHT(new Part[] { Part.WORLD_PROPERTIES }), 
        FOR_CHARACTER_POSITION_INFORMATION(new Part[] { Part.ID, Part.IDENTITY, Part.POSITION }), 
        FOR_REMOTE_PET_UPDATE(new Part[] { Part.ID, Part.PET }), 
        FOR_DISCOVERED_ITEMS_UPDATE(new Part[] { Part.ID, Part.DISCOVERED_ITEMS }), 
        BREED_SPECIFIC(new Part[] { Part.BREED_SPECIFIC }), 
        FOR_PERSONAL_EFFECTS(new Part[] { Part.PERSONAL_EFFECTS }), 
        FOR_ANTI_ADDICTION(new Part[] { Part.ANTI_ADDICTION }), 
        SEX(new Part[] { Part.SEX }), 
        BAGS(new Part[] { Part.BAGS }), 
        VISIBILITY(new Part[] { Part.VISIBILITY }), 
        APTITUDE_BONUS_INVENTORY(new Part[] { Part.APTITUDE_BONUS_INVENTORY }), 
        NATION_PVP_MONEY(new Part[] { Part.NATION_PVP_MONEY }), 
        FIGHTER_DATA_FOR_RECONNECTION(new Part[] { Part.POSITION, Part.FIGHT, Part.ALL_CHARACTERISTICS, Part.FIGHT_PROPERTIES, Part.WORLD_PROPERTIES, Part.BREED, Part.CONTROLLED_BY_AI, Part.ID, Part.IDENTITY }), 
        PUBLIC_CHARACTERISTICS(new Part[] { Part.PUBLIC_CHARACTERISTICS }), 
        ALL_CHARACTERISTICS(new Part[] { Part.ALL_CHARACTERISTICS });
        
        private Part[] m_parts;
        
        private SerializationType(final Part[] parts) {
            this.m_parts = parts;
        }
        
        public Part[] getParts() {
            return this.m_parts;
        }
    }
    
    public enum Part
    {
        ID, 
        IDENTITY, 
        NAME, 
        BREED, 
        SEX, 
        APPEARANCE, 
        POSITION, 
        PREVIOUS_POSITION, 
        HP, 
        XP, 
        XP_CHARACTERISTICS, 
        CREATION_DATA, 
        EQUIPMENT_INVENTORY, 
        BAGS, 
        DIMENSIONAL_BAG_FOR_SAVE, 
        SPELL_INVENTORY, 
        SKILL_INVENTORY, 
        CRAFT, 
        APTITUDE_INVENTORY, 
        APTITUDE_BONUS_INVENTORY, 
        SHORTCUT_INVENTORIES, 
        EMOTE_INVENTORY, 
        LANDMARK_INVENTORY, 
        DISCOVERED_ITEMS, 
        RUNNING_EFFECTS_FOR_SAVE, 
        SCENARIO_MANAGER, 
        BREED_SPECIFIC, 
        RESPAWN_POINT, 
        TITLES, 
        CITIZEN_POINT, 
        PASSEPORT_INFO, 
        GUILD_ID, 
        GUILD_REMOTE_INFO, 
        GUILD_LOCAL_INFO, 
        GUILD_INFO_FOR_GAME, 
        NATION_STORAGE, 
        NATION_SYNCHRO, 
        NATION_ID, 
        CHARACTER_LIST_NATION_ID, 
        SOCIAL_STATES, 
        PET, 
        ACHIEVEMENTS, 
        DIMENSIONAL_BAG_VIEWS_INVENTORY, 
        INVENTORIES, 
        LOCK_STORAGE, 
        ANTI_ADDICTION, 
        EQUIPMENT_APPEARANCE, 
        GAME_SERVER, 
        ACCOUNT_INFORMATION, 
        ACCOUNT_INFORMATION_REMOTE, 
        FIGHT, 
        WORLD_PROPERTIES, 
        FIGHT_PROPERTIES, 
        PUBLIC_CHARACTERISTICS, 
        PRIVATE_CHARACTERISTICS, 
        FIGHT_CHARACTERISTICS, 
        RUNNING_EFFECTS, 
        CONTROLLED_BY_AI, 
        CURRENT_MOVEMENT_PATH, 
        CHALLENGES, 
        GROUP, 
        OCCUPATION, 
        GIFT_TOKEN_ACCOUNT_INVENTORY, 
        TEMPLATE, 
        COLLECT, 
        DIMENSIONAL_BAG_FOR_LOCAL_CLIENT, 
        STATISTICS_FOR_WORLD_SERVER, 
        RUNNING_EFFECTS_FOR_GAME_TO_GAME, 
        LOCK_TO_CLIENT, 
        PERSONAL_EFFECTS, 
        COMPANION_CONTROLLER_ID, 
        VISIBILITY, 
        NATION_PVP, 
        NATION_PVP_MONEY, 
        ALL_CHARACTERISTICS;
    }
}
