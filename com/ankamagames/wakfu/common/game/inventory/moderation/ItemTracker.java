package com.ankamagames.wakfu.common.game.inventory.moderation;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;

public class ItemTracker
{
    public static final String DEFAULT_FORMAT = "%s,%s,%d,%d,%d,%d,%s,%s,%s,%d,%d,%d,%d,%d,%d,%d";
    public static final String COLLECTOR = "Collector";
    public static final String STORAGE_BOX = "StorageBox";
    public static final String GUILD_STORAGE_BOX = "GuildStorageBox";
    public static final String VAULT = "Vault";
    public static final String KAMA_TO_VAULT = "KamaToVault";
    public static final String KAMA_FROM_VAULT = "KamaFromVault";
    public static final String EXCHANGE = "Exchange";
    public static final String HP_KAMAS = "HpKamas";
    public static final String KAMA_SHIELD = "KamaShield";
    public static final String KAMA_STEAL = "KamaSteal";
    public static final String BUY_IN_MERCHANT_INVENTORY = "BuyInMerchantInventory";
    public static final String KAMA_FIGHT_LOOT = "KamaFightLoot";
    public static final String KAMA_ACHIEVEMENT_REWARD = "KamaAchievementReward";
    public static final String MARKET_ADD_KAMA = "MarketAddKama";
    public static final String MARKET_REMOVE_KAMA = "MarketRemoveKama";
    public static final String GUILD_CREATION_KAMA = "GuildCreationKama";
    public static final String EXCHANGE_MACHINE_KAMA = "ExchangeMachineKama";
    public static final String GENERIC_ACTIVABLE_IE_KAMA = "GenericActivableIEKama";
    public static final String TELEPORTER_KAMA = "TeleporterKama";
    public static final String GIVE_KAMA_ACTION = "GiveKamaAction";
    public static final String KAMA_FROM_FREE_COLLECTOR = "KamaFromFreeCollector";
    public static final String KAMA_TO_FREE_COLLECTOR = "KamaToFreeCollector";
    public static final String KAMA_FROM_GUILD_STORAGE = "KamaFromGuildStorage";
    public static final String KAMA_TO_GUILD_STORAGE = "KamaToGuildStorage";
    public static final String KAMA_FROM_ITEM_ACTION = "KamaFromItemAction";
    public static final String KAMA_TO_GEM_UPGRADE = "KamaToGemUpgrade";
    public static final String KAMA_TO_BOAT_TRAVEL = "KamaToBoatTravel";
    public static final String KAMA_TO_DRAGO_TRAVEL = "KamaToDragoTravel";
    public static final String KAMA_TO_ZAAP_TRAVEL = "KamaToZaapTravel";
    public static final String KAMA_FROM_ZAAP_TRAVEL = "KamaFromZaapTravel";
    public static final String KAMA_TO_CANDIDATE_FEE = "KamaToCandidateFee";
    public static final String KAMA_TO_MARKET = "KamaToMarket";
    public static final String KAMA_FROM_MARKET = "KamaFromMarket";
    public static final String KAMA_FROM_SCENARIO = "KamaFromScenario";
    public static final String KAMA_TO_SCENARIO = "KamaToScenario";
    public static final String KAMA_IN_LOOT_CHEST = "KamaInLootChest";
    public static final String TREASURE = "Treasure";
    public static final String KAMA_IN_COLLECTOR = "KamaInCollector";
    public static final String RESURRECTION = "Resurrection";
    public static final String MERGE_ITEM_TO_SET = "MergeItemToSet";
    public static final String PICK_UP_ITEM_TO_INVENTORY = "PickUpItemToInventory";
    public static final String PICK_UP_ITEM_TO_TEMPORARY_INVENTORY = "PickUpItemToTemporaryInventory";
    public static final String MERGE_GEM_FROM_INVENTORY = "MergeGemFromInventory";
    public static final String MERGE_GEM_TO_INVENTORY = "MergeGemToInventory";
    public static final String MERGE_GEM_TO_TEMPORARY_INVENTORY = "MergeGemToTemporaryInventory";
    public static final String DELETE_GEM_FROM_INVENTORY = "DeleteGemFromInventory";
    public static final String DROP_ITEM = "DropItem";
    public static final String FROM_QUEST_INVENTORY_TO_TELEPORTER = "FromQuestInventoryToTeleporter";
    public static final String FROM_INVENTORY_TO_TELEPORTER = "FromInventoryToTeleporter";
    public static final String FROM_CRAFT_TO_INVENTORY = "FromCraftToInventory";
    public static final String FROM_CRAFT_TO_QUEST_INVENTORY = "FromCraftToQuestInventory";
    public static final String FROM_CRAFT_TO_TEMPORARY_INVENTORY = "FromCraftToTemporaryInventory";
    public static final String FROM_INVENTORY_TO_CRAFT = "FromInventoryToCraft";
    public static final String FROM_QUEST_INVENTORY_TO_CRAFT = "FromQuestInventoryToCraft";
    public static final String FROM_INVENTORY_TO_COLLECT = "FromInventoryToCollect";
    public static final String FROM_INVENTORY_TO_STORAGE_BOX_UNLOCK = "FromInventoryToStorageBoxUnlock";
    public static final String FROM_INVENTORY_TO_CANNON_TRAVEL = "FromInventoryToCannonTravel";
    public static final String FROM_EXCHANGE_MACHINE_TO_INVENTORY = "FromExchangeMachineToInventory";
    public static final String FROM_EXCHANGE_MACHINE_TO_QUEST_INVENTORY = "FromExchangeMachineToQuestInventory";
    public static final String FROM_QUEST_INVENTORY_TO_EXCHANGE_MACHINE = "FromQuestInventoryToExchangeMachine";
    public static final String FROM_INVENTORY_TO_EXCHANGE_MACHINE = "FromInventoryToExchangeMachine";
    public static final String FROM_EXCHANGE_MACHINE_TO_TEMPORARY_INVENTORY = "FromExchangeMachineToTemporaryInventory";
    public static final String GIVE_ITEM_ACTION_TO_INVENTORY = "GiveItemActionToInventory";
    public static final String GIVE_ITEM_ACTION_TO_TEMPORARY_INVENTORY = "GiveItemActionToTemporaryInventory";
    public static final String FROM_INVENTORY_TO_REGENERATION = "FromInventoryToRegeneration";
    public static final String ADD_TO_INVENTORY_FROM_DUNGEON_REWARD = "AddToInventoryFromDungeonReward";
    public static final String ADD_TO_TEMPORARY_INVENTORY_FROM_DUNGEON_REWARD = "AddToTemporaryInventoryFromDungeonReward";
    public static final String ADD_TO_QUEST_INVENTORY_FROM_DUNGEON_REWARD = "AddToQuestFromDungeonReward";
    public static final String ADD_TO_INVENTORY_FROM_RANDOM_LIST = "AddToInventoryFromRandomList";
    public static final String ADD_TO_TEMPORARY_INVENTORY_FROM_RANDOM_LIST = "AddToTemporaryInventoryFromRandomList";
    public static final String ADD_TO_INVENTORY_FROM_DEPLOIED_IE = "AddToInventoryFromDeploiedIE";
    public static final String REMOVE_FROM_INVENTORY_TO_DEPLOY_IE = "RemoveFromInventoryToDeployIE";
    public static final String ADD_TO_SEED_SPREADER = "AddToSeedSpreader";
    public static final String ADD_TO_TEMPORARY_INVENTORY = "AddToTemporaryInventory";
    public static final String FROM_LOOT_CHEST_TO_INVENTORY = "FromLootChestToInventory";
    public static final String FROM_LOOT_CHEST_TO_QUEST_INVENTORY = "FromLootChestToQuestInventory";
    public static final String FROM_QUEST_INVENTORY_TO_LOOT_CHEST = "FromQuestInventoryToLootChest";
    public static final String FROM_INVENTORY_TO_LOOT_CHEST = "FromInventoryToLootChest";
    public static final String FROM_LOOT_CHEST_TO_TEMPORARY_INVENTORY = "FromLootChestToTemporaryInventory";
    public static final String FROM_INVENTORY_TO_STREET_LIGHT = "FromInventoryToStreetLight";
    public static final String FROM_INVENTORY_TO_RESOURCES_COLLECTOR = "FromInventoryToResourcesCollector";
    public static final String FROM_SPLIT_SET_TO_INVENTORY = "FromSplitSetToInventory";
    public static final String FROM_SCRIPT_ACTION_TO_QUEST_INVENTORY = "FromScriptActionToQuestInventory";
    public static final String FROM_INVENTORY_TO_SCRIPT_ACTION = "FromInventoryToScriptAction";
    public static final String FROM_SCENARIO_ACTION_TO_INVENTORY = "FromScenarioActionToInventory";
    public static final String FROM_SCENARIO_ACTION_TO_QUEST_INVENTORY = "FromScenarioActionToQuestInventory";
    public static final String FROM_SCENARIO_ACTION_TO_TEMPORARY_INVENTORY = "FromScenarioActionToTemporaryInventory";
    public static final String FROM_INVENTORY_TO_GUILD_CREATION = "FromInventoryToGuildCreation";
    public static final String FROM_INVENTORY_TO_PET = "FromInventoryToPet";
    public static final String FROM_PET_TO_INVENTORY = "FromPetToInventory";
    public static final String FROM_INVENTORY_TO_GEM_REROLL = "FromInventoryToGemReroll";
    public static final String FROM_INVENTORY_TO_GEM_UPGRADE = "FromInventoryToGemUpgrade";
    public static final String FROM_ACHIEVEMENT_REWARD_TO_INVENTORY = "FromAchievementRewardToInventory";
    public static final String FROM_INVENTORY_TO_COMPANION = "FromInventoryToCompanion";
    public static final String FROM_COMPANION_TO_INVENTORY = "FromCompanionToInventory";
    public static final String FROM_INVENTORY_TO_BOOKCASE = "FromInventoryToBookcase";
    public static final String FROM_BOOKCASE_TO_INVENTORY = "FromBookcaseToInventory";
    public static final String FROM_INVENTORY_TO_DIMENSIONAL_ROOM_MANAGEMENT = "FromInventoryToDimensionalRoomManagement";
    public static final String FROM_INVENTORY_TO_FLEA = "FromInventoryToFlea";
    public static final String FROM_FLEA_TO_INVENTORY = "FromFleaToInventory";
    public static final String FROM_MARKET_TO_INVENTORY = "FromMarketToInventory";
    public static final String FROM_MARKET_TO_TEMPORARY_INVENTORY = "FromMarketToTemporaryInventory";
    public static final String FROM_INVENTORY_TO_MARKET = "FromInventoryToMarket";
    public static final String FROM_TREASURE_TO_INVENTORY = "FromTreasureToInventory";
    public static final String FROM_TREASURE_TO_TEMPORARY_INVENTORY = "FromTreasureToTemporaryInventory";
    public static final String FROM_LOOT_TO_INVENTORY = "FromLootToInventory";
    public static final String FROM_LOOT_TO_QUEST_INVENTORY = "FromLootToQuestInventory";
    public static final String FROM_LOOT_TO_TEMPORARY_INVENTORY = "FromLootToTemporaryInventory";
    public static final String FROM_COLLECT_TO_TEMPORARY_INVENTORY = "FromCollectToTemporaryInventory";
    public static final String FROM_COLLECT_TO_QUEST_INVENTORY = "FromCollectToQuestInventory";
    public static final String FROM_COLLECT_TO_INVENTORY = "FromCollectToInventory";
    public static final String FROM_INVENTORY_TO_EQUIPMENT = "FromInventoryToEquipement";
    public static final String FROM_EQUIPMENT_TO_INVENTORY = "FromEquipmentToInventory";
    public static final String FROM_INVENTORY_TO_COLLECTOR = "FromInventoryToCollector";
    public static final String FROM_RENT_TO_INVENTORY = "FromRentToInventory";
    public static final String FROM_RENT_TO_TEMPORARY_INVENTORY = "FromRentToTemporaryInventory";
    public static final String FROM_INVENTORY_TO_HAVEN_WORLD_BUILDING = "FromInventoryToHavenWorldBuilding";
    public static final String FROM_HAVEN_WORLD_BUILDING_TO_INVENTORY = "FromHavenWorldBuildingToInventory";
    public static final String FROM_TEMPORARY_INVENTORY_TO_EQUIPMENT = "FromTemporaryInventoryToEquipment";
    public static final String FROM_TEMPORARY_INVENTORY_TO_INVENTORY = "FromTemporaryInventoryToInventory";
    public static final String REMOVE_FROM_TEMPORARY_INVENTORY = "RemoveFromTemporaryInventory";
    public static final String FROM_INVENTORY_TO_GEM_EQUIPMENT = "FromInventoryToGemEquipment";
    public static final String FROM_INVENTORY_TO_BAG_EQUIPMENT = "FromInventoryToBagEquipment";
    public static final String FROM_BAG_EQUIPEMENT_TO_INVENTORY = "FromBagEquipmentToInventory";
    public static final String DELETE_ITEM_FROM_INVENTORY = "DeleteItemFromInventory";
    public static final String DELETE_ITEM_FROM_EQUIPEMENT = "DeleteItemFromEquipment";
    public static final String PLAYER_USE_ITEM = "PlayerUseItem";
    public static final String FROM_SEED_SPREADER_TO_INVENTORY = "FromSeedSpreaderToInventory";
    public static final String FROM_REPACK_TO_INVENTORY = "FromRepackToInventory";
    public static final String FROM_DISASSEMBLE_TO_INVENTORY = "FromDisassembleToInventory";
    public static final String FROM_INVENTORY_TO_DISASSEMBLE = "FromInventoryToDisassemble";
    public static final String FROM_PROTECTOR_TO_INVENTORY = "FromProtectorToInventory";
    public static final String REMOVE_FROM_INVENTORY = "RemoveFromInventory";
    public static final String FROM_MERGE_ITEMS_TO_INVENTORY = "FromMergeItemsToInventory";
    public static final String FROM_ICE_FIGURINE_TO_INVENTORY = "FromIceFigurineToInventory";
    public static final String FROM_ICE_GIFT_TO_INVENTORY = "FromIceGiftToInventory";
    public static final String FROM_INVENTORY_TO_DUMMY = "FromInventoryToDummy";
    public static final String FROM_DUMMY_TO_INVENTORY = "FromDummyToInventory";
    public static final String FROM_DUMMY_TO_TEMPORARY_INVENTORY = "FromDummyToTemporaryInventory";
    public static final String FROM_EQUIPMENT_TO_SCENARIO = "FromEquipmentToScenario";
    public static final String FROM_QUEST_INVENTORY_TO_SCENARIO = "FromQuestInventoryToScenario";
    public static final String FROM_INVENTORY_TO_SCENARIO = "FromInventoryToScenario";
    public static final String FROM_SCENARIO_TO_INVENTORY = "FromScenarioToInventory";
    public static final String FROM_SCENARIO_TO_TEMPORARY_INVENTORY = "FromScenarioToTemporaryInventory";
    public static final String FROM_SCENARIO_TO_QUEST_INVENTORY = "FromScenarioToQuestInventory";
    public static final String KAMA_TO_FLEA_TAX = "KamaToFleaTax";
    public static final String KAMA_TO_MARKET_TAX = "KamaToMarketTax";
    public static final String SCENARIO_FROM_INVENTORY_TO_EQUIPMENT = "ScenarioFromInventoryToEquipement";
    public static final String SCENARIO_FROM_EQUIPMENT_TO_INVENTORY = "ScenarioFromEquipementToInventory";
    public static final String FROM_MERCHANT_INVENTORY_TO_INVENTORY = "FromMerchantInventoryToInventory";
    private static final Logger LOGGER;
    
    public static void log(final Level logLevel, final long accountFrom, final long characterFrom, final long accountTo, final long characterTo, final String type, final long externalId, final int instanceId, final int itemRefId, final long itemFromUid, final long itemToUid, final long kamas) {
        log(logLevel, accountFrom, characterFrom, accountTo, characterTo, null, null, type, externalId, instanceId, itemRefId, itemFromUid, itemToUid, (short)32767, kamas);
    }
    
    public static void log(final Level logLevel, final long accountFrom, final long characterFrom, final long accountTo, final long characterTo, final String type, final long externalId, final int instanceId, final int itemRefId, final long itemFromUid, final long itemToUid, final short quantity) {
        log(logLevel, accountFrom, characterFrom, accountTo, characterTo, null, null, type, externalId, instanceId, itemRefId, itemFromUid, itemToUid, quantity, Long.MAX_VALUE);
    }
    
    public static void log(final Level logLevel, final long accountFrom, final long characterFrom, final long accountTo, final long characterTo, final String type, final long externalId, final int instanceId, final int itemRefId, final long itemFromUid, final long itemToUid, final short quantity, final long kamas) {
        log(logLevel, accountFrom, characterFrom, accountTo, characterTo, null, null, type, externalId, instanceId, itemRefId, itemFromUid, itemToUid, quantity, kamas);
    }
    
    public static void log(final Level logLevel, final long accountFrom, final long characterFrom, final long accountTo, final long characterTo, final String ipFrom, final String ipTo, final String type, final long externalId, final int instanceId, final int itemRefId, final long itemFromUid, final long itemToUid, final short quantity, final long kamas) {
        if (ItemTracker.LOGGER == null) {
            return;
        }
        final ISO8601DateFormat simpleDateFormat = new ISO8601DateFormat(TimeZone.getTimeZone("UTC"));
        final GameDate date = new GameDate(WakfuGameCalendar.getInstance().getDate());
        if (date == null) {
            return;
        }
        final String timeZoneString = SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.CALENDAR_TZ);
        final long deltaTime = SystemConfiguration.INSTANCE.getLongValue(SystemConfigurationType.CALENDAR_DELTA);
        final TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        date.sub(new GameInterval(deltaTime / 1000L));
        date.sub(new GameInterval(timeZone.getRawOffset() / 1000));
        String iceServerId = SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SERVER_ID);
        if (iceServerId != null && iceServerId.length() == 0) {
            iceServerId = null;
        }
        ItemTracker.LOGGER.log((Priority)logLevel, (Object)String.format("%s,%s,%d,%d,%d,%d,%s,%s,%s,%d,%d,%d,%d,%d,%d,%d", iceServerId, simpleDateFormat.format(date.toJavaDate()), (accountFrom == -1L) ? null : accountFrom, (characterFrom == -1L) ? null : characterFrom, (accountTo == -1L) ? null : accountTo, (characterTo == -1L) ? null : characterTo, ipFrom, ipTo, type, (externalId == -1L) ? null : externalId, (instanceId == -1) ? null : instanceId, (itemRefId == -1) ? null : itemRefId, (itemFromUid == -1L) ? null : itemFromUid, (itemToUid == -1L) ? null : itemToUid, (quantity == 32767) ? null : quantity, (kamas == Long.MAX_VALUE) ? null : kamas));
    }
    
    public static void logInventoryAction(final InventoryAction inventoryAction, final String inventoryActionHandler, final long handlerId, final BasicCharacterInfo character) {
        switch (inventoryAction.getType()) {
            case ADD_ITEM: {
                final InventoryAddItemAction inventoryAddItemAction = (InventoryAddItemAction)inventoryAction;
                log(Level.INFO, (character == null) ? null : character.getOwnerId(), (character == null) ? null : character.getId(), (character == null) ? null : character.getOwnerId(), (character == null) ? null : character.getId(), "AddTo" + inventoryActionHandler, handlerId, (short)((character == null) ? null : character.getInstanceId()), -1, inventoryAddItemAction.getItemId(), inventoryAddItemAction.getItemId(), (short)(-inventoryAddItemAction.getQuantity()));
                break;
            }
            case GAME_ADD_ITEM: {}
            case MOVE_ITEM: {
                final InventoryMoveItemAction inventoryMoveItemAction = (InventoryMoveItemAction)inventoryAction;
                break;
            }
            case REMOVE_ITEM: {
                final InventoryRemoveItemAction inventoryRemoveItemAction = (InventoryRemoveItemAction)inventoryAction;
                log(Level.INFO, (character == null) ? null : character.getOwnerId(), (character == null) ? null : character.getId(), (character == null) ? null : character.getOwnerId(), (character == null) ? null : character.getId(), "RemoveFrom" + inventoryActionHandler, handlerId, (short)((character == null) ? null : character.getInstanceId()), -1, inventoryRemoveItemAction.getItemId(), inventoryRemoveItemAction.getItemId(), inventoryRemoveItemAction.getQuantity());
                break;
            }
        }
    }
    
    public static void activateLogLevel(final int levelValue) {
        final Level level = Level.toLevel(levelValue);
        if (level == null) {
            return;
        }
        ItemTracker.LOGGER.setLevel(level);
    }
    
    public static void addSwingAppender(final String name) {
        if (ItemTracker.LOGGER == null) {
            return;
        }
        final String fullName = ItemTrackerViewer.getFullName(name);
        final Appender appender = ItemTracker.LOGGER.getAppender(fullName);
        if (appender != null) {
            appender.close();
            ItemTracker.LOGGER.removeAppender(fullName);
        }
        else {
            ItemTracker.LOGGER.addAppender((Appender)new ItemTrackerViewer(name));
        }
    }
    
    static {
        LOGGER = Logger.getLogger("itemTracker");
    }
}
