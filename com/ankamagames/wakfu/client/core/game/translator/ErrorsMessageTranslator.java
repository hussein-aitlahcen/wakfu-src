package com.ankamagames.wakfu.client.core.game.translator;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class ErrorsMessageTranslator
{
    private static final ErrorsMessageTranslator m_instance;
    
    public static ErrorsMessageTranslator getInstance() {
        return ErrorsMessageTranslator.m_instance;
    }
    
    public void pushMessage(final int errorId, final int chatGameErrorPipe, final Object... args) {
        final String errorMsg = this.getMessageByErrorId(errorId, args);
        if (errorMsg == null) {
            return;
        }
        ChatManager.getInstance().pushMessage(errorMsg, chatGameErrorPipe);
    }
    
    public String getMessageByErrorId(final int id, final Object... args) {
        return WakfuTranslator.getInstance().getString(this.getErrorById(id).m_translatorKey, args);
    }
    
    public ErrorMessage getErrorById(final int id) {
        final ErrorMessage message = ErrorMessage.getById(id);
        return (message != null) ? message : ErrorMessage.UNKNOWN;
    }
    
    static {
        m_instance = new ErrorsMessageTranslator();
    }
    
    public enum ErrorMessage
    {
        NO_ERROR(0, "collect.success"), 
        UNKNOWN(1, "error.unknown"), 
        TOO_FAR(2, "collect.error.badDistance"), 
        NOT_ENOUGH_KAMAS(3, "error.notEnoughKamas"), 
        BAD_LEVEL(4, "occupation.error.levelRequired"), 
        BAD_ALTITUDE(5, "collect.error.badAltitude"), 
        CANT_WHILE_DEAD(6, "action.error.cantWhileDead"), 
        TOO_MANY_IN_PARTITION(10, "plant.error.tooManyInPartition"), 
        TOO_MANY_IN_ZONE(35, "plant.error.tooManyInZone"), 
        PLANT_NOT_ALLOWED(36, "plant.error.notAllowed"), 
        CANT_PLANT_IN_ZONE(11, "plant.error.cantPlantInZone"), 
        GROUND_TYPE_EXCLUSIVE(12, "plant.error.groundTypeExclusive"), 
        PLANT_FAILED(13, "plant.error.failed"), 
        CANT_BE_COLLECT(14, "collect.error.cantBeCollect"), 
        RESOURCE_UNAVAILABLE(15, "collect.error.resourceUnavailable"), 
        RESOURCE_PRESENT(16, "plant.error.present"), 
        RESOURCE_ACTION_IMPOSSIBLE(17, "collect.error.actionInProgress"), 
        GROUND_TYPE_ERREUR(18, "plant.error.groundTypeError"), 
        ITEM_ID_SKILL_ERROR(19, "plant.error.itemError"), 
        BDD_ERROR(20, "bdd.error"), 
        SKILL_UNKNOW(21, "craft.unknown"), 
        LEVEL_SKILL_REQUIRED(22, "occupation.error.skillRequired"), 
        RECIPE_UNKNOWN(23, "craft.error.unknowRecipe"), 
        INGREDIENTS_NOT_FOUND(24, "inventory.error.ingredientsNotFound"), 
        CANT_JOIN(25, "collect.error.cantJoin"), 
        FIGHT_IN_PROGRESS(26, "collect.fightInProgress"), 
        TO_MANY_BLOCKING_RESOURCES(27, "collect.error.blockingResources"), 
        NO_MAP_RESOURCES(28, "error.noResourceMap"), 
        BAD_POSITION(29, "too.far.to.interact"), 
        COLLECT_TOO_EARLY(30, "collect.error.tooEarly"), 
        RECIPE_ALREADY_KNOWN(31, "craft.error.recipeAlreadyKnown"), 
        BAD_LEVEL_FOR_SECRET(33, "craft.error.badLevelForSecret"), 
        RECIPE_NOT_ALLOWED(34, "craft.error.recipeNotAllowed"), 
        RESOURCE_MONSTER_ACTION_IN_PROGRESS(37, "craft.error.recipeNotAllowed"), 
        SEARCH_TREASURE_FAILED(38, "error.searchTreasureFailed"), 
        INVENTORY_ERROR(50, "inventory.error.lackItem"), 
        INVENTORY_FULL(51, "error.bagFull"), 
        WRONG_ITEM_EQUIPED(52, "action.error.wrongItemEquiped"), 
        ITEM_NOT_USABLE(53, "collect.error.ItemNotUsable"), 
        ITEM_SHOULD_NOT_EXIST(55, "inventory.error.lackItem"), 
        ERROR_ITEM_ALREADY_INBAGS(57, "item.error.alreadyInBags"), 
        IMPOSSIBLE_TO_EQUIP_ITEM(58, "item.error.impossibilityToEquip"), 
        IMPOSSIBLE_TO_USE(61, "item.error.impossibilityToUse"), 
        CANT_EQUIP_PET_ITEM(64, "cantEquipPetItem"), 
        INCOMPATIBLE_RENT_TYPE(65, "incompatibleRentTypes"), 
        INCOMPATIBLE_ITEM_TYPE(66, "incompatibleItemTypes"), 
        CANT_BE_EQUIPED(60, "item.error.cantBeEquiped"), 
        ITEM_ACTION_FAILED(59, "error.item.actionFailed"), 
        MARKET_ALREADY_BOUGHT_ENTRY(63, "market.alreadyBoughtEntry"), 
        CANT_EQUIP_UNIDENTIFIED_ITEM(67, "item.error.cantEquipAnUnidentifiedItem"), 
        LOOT_CHEST_IS_EMPTY(100, "ie.lootChestIsEmpty"), 
        IE_WELL_HAS_TOO_MANY_USERS(101, "ie.wellHasTooManyUsers"), 
        IE_ONLY_ONE_ACTION_BY_USER(102, "ie.onlyOneActionByUser"), 
        DIMENSIONNAL_BAG_FORBIDDEN_OUTSIDE_MARKET(103, "bag.forbiddenOutsideMarket"), 
        IE_WRONG_RIGHTS(104, "ie.wrongRights"), 
        IE_NOT_ENOUGH_SPACE_TO_REPACK(105, "ie.notEnoughSpaceToRepack"), 
        IE_INVALID_CELL(106, "ie.invalidCell"), 
        INVALID_TRAVEL_CRITERION(107, "chat.travel.invalidCriterion"), 
        CHALLENGE_REFUSED(201, "challenge.refused"), 
        CHALLENGE_DELAY_OUTDATED(202, "challenge.delayoutdated"), 
        CHALLENGE_MAX_USERS_LIMIT(203, "challenge.maxUsersLimit"), 
        GROUP_ERROR_USER_ALREADY_IN_PARTY(300, "group.error.alreadyInParty"), 
        GROUP_ERROR_USER_ALREADY_IN_GUILD(318, "group.error.alreadyInGuild"), 
        GROUP_ERROR_SELF_INVITATION(301, "group.error.self_invitation"), 
        GROUP_ERROR_NO_RIGHTS(302, "group.error.no_rights"), 
        GROUP_ERROR_USER_DISCONNECTED(303, "group.error.unknown_user"), 
        GROUP_ERROR_SAME_NATION_REQUIRED(315, "group.error.nation.is.enemy"), 
        GROUP_INVITATION_REFUSED(304, "group.error.invitation_refused"), 
        GROUP_ERROR_REMOTE_NOT_IN_GROUP(305, "group.error.remote_not_in_group"), 
        GROUP_ERROR_NOT_IN_GROUP(306, "group.error.not_in_group"), 
        GROUP_ERROR_FULL(307, "group.error.full"), 
        GROUP_ERROR_CANT_MERGE_TOO_MANY_MEMBERS(325, "group.error.cant.merge.too.many.members"), 
        GROUP_ERROR_COMPANION_FULL(323, "group.error.companion.full"), 
        GROUP_ERROR_BREED_UNAVAILABLE(335, "group.error.breed.unavailable"), 
        GROUP_ERROR_MUST_SUBSCRIBE_TO_GROUP_WITH_COMPANION(324, "group.error.mustSubscribeToGroupWithCompanion"), 
        GROUP_ERROR_INVITATION_PENDING(308, "group.error.invitationPending"), 
        GUILD_ERROR_BAD_NAME(309, "guild.error.name"), 
        GUILD_ERROR_NAME_ALREADY_EXIST(314, "guild.error.name.already.exist"), 
        GUILD_ERROR_BAD_BLAZON(310, "guild.error.blazon"), 
        GUILD_CREATION_FAILED(311, "guild.error.creationFailed"), 
        GUILD_ERROR_USER_ALREADY_IN_GUILD(312, "guild.error.user.already.in.guild"), 
        ERROR_GUILD_CREATION_CREATOR_ALREADY_IN_GUILD(313, "error.guild.creation.creator.already.in.guild"), 
        GROUP_ERROR_REMOTE_IN_FIGHT(317, "group.error.remoteInFight"), 
        GROUP_ERROR_GROUP_DISABLED(319, "group.error.groupDisabled"), 
        GROUP_ERROR_GROUP_WITH_OTHER_BREED_DISABLED(320, "group.error.groupWithOtherBreedDisabled"), 
        GROUP_ERROR_REMOTE_GROUP_DISABLED(321, "group.error.remoteGroupDisabled"), 
        GROUP_ERROR_REMOTE_GROUP_WITH_OTHER_BREED_DISABLED(322, "group.error.remoteGroupWithOtherBreedDisabled"), 
        MONSTER_ACTION_FAILED(500, "monster.error.monsterAction.failed"), 
        FIGHT_JOIN_ERROR_CANT_ATTACK(1020, "fight.error.join.cannotAttack"), 
        FIGHT_JOIN_ERROR_CANT_JOIN(1021, "fight.error.join.cannotBeJoined"), 
        FIGHT_JOIN_ERROR_TARGET_NOT_IN_FIGHT(1022, "fight.error.join.targetNotInFight"), 
        FIGHT_JOIN_ERROR_ALREADY_IN_FIGHT(1023, "fight.error.join.selfInFight"), 
        FIGHT_JOIN_ERROR_ONLY_DURING_PLACEMENT(1024, "fight.error.join.onlyDuringPlacement"), 
        FIGHT_JOIN_ERROR_FIGHT_LOCKED(1025, "fight.error.join.fightLocked"), 
        FIGHT_JOIN_ERROR_CANT_WALK(1026, "fight.error.join.cannotWalk"), 
        FIGHT_JOIN_ERROR_FIGHT_FULL(1027, "fight.error.join.fightFull"), 
        FIGHT_JOIN_ERROR_CANT_STAKE(1028, "fight.error.join.cantStake"), 
        FIGHT_JOIN_ERROR_JOINER_DEAD(1029, "fight.error.join.selfDead"), 
        FIGHT_JOIN_ERROR_ENTERING_DIMBAG(1030, "fight.error.join.enteringDimensionalBag"), 
        FIGHT_JOIN_ERROR_WHILE_EXCHANGING(1031, "fight.error.join.exchanging"), 
        FIGHT_JOIN_ERROR_WHILE_ROLLING_LOOT(1032, "fight.error.join.rollingLoot"), 
        FIGHT_JOIN_ERROR_JOINED_DEAD(1033, "fight.error.join.targetDead"), 
        FIGHT_JOIN_ERROR_TARGET_NOT_IN_PARTY(1034, "fight.error.join.targetNotInParty"), 
        FIGHT_JOIN_ERROR_GROUPMATE_IN_OPPOSING_TEAM(1035, "fight.error.join.groupMemberInOpposingTeam"), 
        FIGHT_JOIN_ERROR_CANNOT_DEFEND_ENEMY_PROTECTOR(1036, "fight.error.join.cannotDefendEnemyProtector"), 
        FIGHT_JOIN_ERROR_CANNOT_ATTACK_ALLIED_PROTECTOR(1037, "fight.error.join.cannotAttackAlliedProtector"), 
        FIGHT_JOIN_ERROR_CANNOT_JOIN_DUEL(1038, "fight.error.join.cannotJoinDuel"), 
        FIGHT_JOIN_ERROR_CANNOT_JOIN_TUTO(1039, "fight.error.join.cannotJoinTuto"), 
        FIGHT_JOIN_ERROR_CANNOT_JOIN_MONSTER_TEAM(1040, "fight.error.join.cannotJoinMonsters"), 
        FIGHT_JOIN_ERROR_CANNOT_AUTO_JOIN(1041, "fight.error.join.cannotSelectTeam"), 
        FIGHT_JOIN_ERROR_TARGET_TOO_FAR(1042, "fight.error.join.targetTooFar"), 
        FIGHT_JOIN_ERROR_NO_START_PLACEMENT_FOUND(1043, "fight.error.join.no.start.placement.found"), 
        FIGHT_JOIN_ERROR_IS_DEAD(1044, "fight.error.join.isDead"), 
        FIGHT_JOIN_ERROR_PVP_TAG_NOT_ACTIVATED(327, "pvp.error.tagNotActivated"), 
        FIGHT_JOIN_ERROR_PVP_TAG_NOT_ACTIVE_YET(328, "pvp.error.tagNotActiveYet"), 
        FIGHT_JOIN_ERROR_PVP_CANT_JOIN_ENEMY(326, "error.pvp.cantJoinEnemy"), 
        FIGHT_JOIN_ERROR_PVP_INITIATOR_LEVEL_DIFFERENCE_TOO_BIG(329, "error.pvp.levelDifferenceTooBig"), 
        FIGHT_JOIN_ERROR_PVP_NOT_SAME_NATION_AS_GUILD(331, "error.pvp.notSameNationAsGuild"), 
        FIGHT_JOIN_ERROR_PVP_CANT_FIGHT_GUILDMATE(334, "error.pvp.cantAgroGuildmate"), 
        FIGHT_JOIN_ERROR_PVP_MUST_BE_GUARD(332, "error.pvp.mustBeGuard"), 
        FIGHT_JOIN_ERROR_PVP_MUST_HAVE_MIN_LEVEL(333, "error.pvp.mustHaveMinLevel"), 
        NATION_NOMINATION_REFUSED(10200, "nation.error.governmentNominationRefuseChatMessage"), 
        NATION_NOMINATION_RANK_OCCUPIED(10201, "nation.error.governmentNominationRankOccupiedChatMessage"), 
        NATION_NOMINATION_USER_UNKNOWN(10202, "group.error.unknown_user"), 
        NATION_NOMINATION_BAD_NATION(10203, "nation.error.governmentNominationOtherNationChatMessage"), 
        NATION_NOMINATION_ALREADY_IN_GOVERNMENT(10204, "nation.error.governmentNominationAlreadyInGovChatMessage"), 
        NATION_NOMINATION_CRITERION_FAIL(10205, "nation.error.governmentNominationCriterionFailChatMessage"), 
        NATION_NOMINATION_NOT_ENOUGH_CITIZEN_SCORE(10207, "nation.error.governmentNominationNotEnoughCitizenScoreChatMessage"), 
        NATION_DIPLOMACY_REQUEST_TOO_EARLY(10206, "nation.error.diplomacy.requestTooEarly"), 
        NATION_DIPLOMACY_ALREADY_HAS_ALLY(10210, "nation.error.diplomacy.alreadyHasAlly"), 
        NATION_DIPLOMACY_ALREADY_HAS_ALLY_REQUEST(10211, "nation.error.diplomacy.alreadyHasAllyRequest"), 
        NATION_NOMINATION_FORBIDDEN_BY_REVOKE(10208, "nation.error.governmentNominationForbiddenByRevokeChatMessage"), 
        NATION_CANDIDATE_FORBIDDEN_BY_REVOKE(10209, "nation.error.governmentCandidateForbiddenByRevokeChatMessage"), 
        NOT_SUBSCRIBER(7, "error.playerNotSubscribed"), 
        NOT_TARGET_SUBSCRIBER(8, "error.targetNotSubscribed"), 
        COMPANION_ITEM_EQUIP_ERROR_GENERIC(10300, "companion.itemEquipErrorGeneric");
        
        private static final TIntObjectHashMap<ErrorMessage> m_messages;
        public final int m_constantId;
        public final String m_translatorKey;
        
        private ErrorMessage(final int result, final String key) {
            this.m_constantId = result;
            this.m_translatorKey = key;
        }
        
        static ErrorMessage getById(final int id) {
            return ErrorMessage.m_messages.get(id);
        }
        
        static {
            m_messages = new TIntObjectHashMap<ErrorMessage>();
            for (final ErrorMessage message : values()) {
                ErrorMessage.m_messages.put(message.m_constantId, message);
            }
        }
    }
}
