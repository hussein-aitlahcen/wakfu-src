package com.ankamagames.baseImpl.common.clientAndServer.account.admin;

import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

public enum AdminRightsGroup
{
    NONE(0, new AdminRightsEnum[0]), 
    SUPER_ADMINISTRATOR(1, AdminRightsEnum.values()), 
    ADMINISTRATOR(2, new AdminRightsEnum[] { AdminRightsEnum.WHO, AdminRightsEnum.RIGHTS, AdminRightsEnum.WHERE, AdminRightsEnum.TELEPORT, AdminRightsEnum.KICK, AdminRightsEnum.GHOST_CHECK, AdminRightsEnum.SYMBIOT, AdminRightsEnum.MONSTER_GROUP, AdminRightsEnum.INVENTORY, AdminRightsEnum.REGENERATE, AdminRightsEnum.FIGHT_DEBUG, AdminRightsEnum.SCENARIO, AdminRightsEnum.CHARACTER_MODIFICATION, AdminRightsEnum.GOD_MODE, AdminRightsEnum.TELEPORT_PLAYER, AdminRightsEnum.SYSMSG, AdminRightsEnum.INSTANCE_USAGE, AdminRightsEnum.DESTROY_INSTANCE, AdminRightsEnum.BOOST_FACTORS, AdminRightsEnum.PLAY_ANIMATION, AdminRightsEnum.PLAY_APS, AdminRightsEnum.SPAWN_IE, AdminRightsEnum.SESSIONS, AdminRightsEnum.IDENT_PHASE, AdminRightsEnum.CHALLENGES, AdminRightsEnum.BAN, AdminRightsEnum.HELP, AdminRightsEnum.STAFF, AdminRightsEnum.MUTE, AdminRightsEnum.DISTRIBUTE_ITEMS, AdminRightsEnum.SEARCH, AdminRightsEnum.QUOTA, AdminRightsEnum.RAGNAROK, AdminRightsEnum.REMOVE_FLOOR_ITEMS, AdminRightsEnum.SHOW_POPULATION, AdminRightsEnum.SHOW_MONSTER_QUOTA, AdminRightsEnum.CANCEL_MONSTER_COLLECT_COOLDOWN, AdminRightsEnum.GET_MY_INSTANCE_UID, AdminRightsEnum.NATION, AdminRightsEnum.ACHIEVEMENT, AdminRightsEnum.DUMP_BAG, AdminRightsEnum.ZONE_BUFF, AdminRightsEnum.PROTECTOR, AdminRightsEnum.ECOSYSTEM, AdminRightsEnum.DEBUG, AdminRightsEnum.FIGHT, AdminRightsEnum.SET_RESPAWN_POINT, AdminRightsEnum.CHAOS, AdminRightsEnum.SUBSCRIBER, AdminRightsEnum.NEW_REQUEST_SANCTIONS, AdminRightsEnum.GUILD, AdminRightsEnum.RESTAT, AdminRightsEnum.HAVEN_WORLD, AdminRightsEnum.ALMANACH, AdminRightsEnum.FREE_ACCESS, AdminRightsEnum.GEM, AdminRightsEnum.GAMEPLAY_STATS, AdminRightsEnum.TAKE_ACCOUNT_CONTROL, AdminRightsEnum.PASS_THROUGH_COMMUNITY_CHECK, AdminRightsEnum.PASS_THROUGH_NAME_CHECK, AdminRightsEnum.PASS_THROUGH_SERVER_LOCK_CHECK, AdminRightsEnum.GET_WORLD_STATS, AdminRightsEnum.MODIFY_CALENDAR, AdminRightsEnum.EVOLVE_HAVEN_WORLD_BUILDING, AdminRightsEnum.ADMIN_CHAT, AdminRightsEnum.DEBUG_WORLD_EDITOR, AdminRightsEnum.FIGHT_CHALLENGE, AdminRightsEnum.COMPANION, AdminRightsEnum.ITEM_TRACKER, AdminRightsEnum.BOT, AdminRightsEnum.AI, AdminRightsEnum.CAN_ANSWER_TO_PLAYER_REQUEST, AdminRightsEnum.POPUP_MODERATION_MESSAGE, AdminRightsEnum.RED_MESSAGE, AdminRightsEnum.EMOTE_TARGETABLE, AdminRightsEnum.HAVEN_BAG_KICK, AdminRightsEnum.JAIL_COMMANDS, AdminRightsEnum.LIST_LOOT, AdminRightsEnum.REVIVE, AdminRightsEnum.GIVE_ITEM, AdminRightsEnum.SANCTION_MESSAGE, AdminRightsEnum.SPECIAL_GET, AdminRightsEnum.INTERSERVER, AdminRightsEnum.PASS_THROUGH_BAG_LOCK, AdminRightsEnum.APTITUDE, AdminRightsEnum.PVP, AdminRightsEnum.PASS_THROUGH_CONNECTION_QUEUE, AdminRightsEnum.MARKET, AdminRightsEnum.ICE_STATUS, AdminRightsEnum.RESTORE_CHARACTER, AdminRightsEnum.SERVERLOCK, AdminRightsEnum.SHUTDOWN, AdminRightsEnum.HERO, AdminRightsEnum.SYS_CONF }), 
    STAFF(3, new AdminRightsEnum[] { AdminRightsEnum.WHO, AdminRightsEnum.WHERE, AdminRightsEnum.TELEPORT, AdminRightsEnum.KICK, AdminRightsEnum.MONSTER_GROUP, AdminRightsEnum.REGENERATE, AdminRightsEnum.CHARACTER_MODIFICATION, AdminRightsEnum.TELEPORT_PLAYER, AdminRightsEnum.SYSMSG, AdminRightsEnum.PLAY_ANIMATION, AdminRightsEnum.PLAY_APS, AdminRightsEnum.SPAWN_IE, AdminRightsEnum.CHALLENGES, AdminRightsEnum.BAN, AdminRightsEnum.HELP, AdminRightsEnum.STAFF, AdminRightsEnum.MUTE, AdminRightsEnum.SEARCH, AdminRightsEnum.GET_MY_INSTANCE_UID, AdminRightsEnum.NATION, AdminRightsEnum.ACHIEVEMENT, AdminRightsEnum.ZONE_BUFF, AdminRightsEnum.ECOSYSTEM, AdminRightsEnum.FIGHT, AdminRightsEnum.SET_RESPAWN_POINT, AdminRightsEnum.NEW_REQUEST_SANCTIONS, AdminRightsEnum.RESTAT, AdminRightsEnum.HAVEN_WORLD, AdminRightsEnum.ALMANACH, AdminRightsEnum.GEM, AdminRightsEnum.GAMEPLAY_STATS, AdminRightsEnum.PASS_THROUGH_COMMUNITY_CHECK, AdminRightsEnum.PASS_THROUGH_NAME_CHECK, AdminRightsEnum.PASS_THROUGH_SERVER_LOCK_CHECK, AdminRightsEnum.GET_WORLD_STATS, AdminRightsEnum.ADMIN_CHAT, AdminRightsEnum.FIGHT_CHALLENGE, AdminRightsEnum.CAN_ANSWER_TO_PLAYER_REQUEST, AdminRightsEnum.POPUP_MODERATION_MESSAGE, AdminRightsEnum.RED_MESSAGE, AdminRightsEnum.EMOTE_TARGETABLE, AdminRightsEnum.HAVEN_BAG_KICK, AdminRightsEnum.JAIL_COMMANDS, AdminRightsEnum.LIST_LOOT, AdminRightsEnum.REVIVE, AdminRightsEnum.SANCTION_MESSAGE, AdminRightsEnum.SPECIAL_GET, AdminRightsEnum.INTERSERVER, AdminRightsEnum.PASS_THROUGH_BAG_LOCK, AdminRightsEnum.APTITUDE, AdminRightsEnum.PVP, AdminRightsEnum.PASS_THROUGH_CONNECTION_QUEUE, AdminRightsEnum.MARKET }), 
    MODERATOR(4, new AdminRightsEnum[] { AdminRightsEnum.WHO, AdminRightsEnum.WHERE, AdminRightsEnum.TELEPORT, AdminRightsEnum.KICK, AdminRightsEnum.REGENERATE, AdminRightsEnum.CHARACTER_MODIFICATION, AdminRightsEnum.TELEPORT_PLAYER, AdminRightsEnum.BAN, AdminRightsEnum.HELP, AdminRightsEnum.STAFF, AdminRightsEnum.MUTE, AdminRightsEnum.SEARCH, AdminRightsEnum.GET_MY_INSTANCE_UID, AdminRightsEnum.FIGHT, AdminRightsEnum.SET_RESPAWN_POINT, AdminRightsEnum.SUBSCRIBER, AdminRightsEnum.NEW_REQUEST_SANCTIONS, AdminRightsEnum.RESTAT, AdminRightsEnum.PASS_THROUGH_COMMUNITY_CHECK, AdminRightsEnum.PASS_THROUGH_SERVER_LOCK_CHECK, AdminRightsEnum.PASS_THROUGH_NAME_CHECK, AdminRightsEnum.ADMIN_CHAT, AdminRightsEnum.CAN_ANSWER_TO_PLAYER_REQUEST, AdminRightsEnum.POPUP_MODERATION_MESSAGE, AdminRightsEnum.RED_MESSAGE, AdminRightsEnum.EMOTE_TARGETABLE, AdminRightsEnum.HAVEN_BAG_KICK, AdminRightsEnum.JAIL_COMMANDS, AdminRightsEnum.REVIVE, AdminRightsEnum.SANCTION_MESSAGE, AdminRightsEnum.SPECIAL_GET, AdminRightsEnum.INTERSERVER, AdminRightsEnum.PASS_THROUGH_BAG_LOCK, AdminRightsEnum.PASS_THROUGH_CONNECTION_QUEUE }), 
    AMBASSADOR(5, new AdminRightsEnum[] { AdminRightsEnum.ADMIN_CHAT, AdminRightsEnum.CAN_ANSWER_TO_PLAYER_REQUEST });
    
    private final int m_id;
    private final AdminRightsEnum[] m_rights;
    
    private AdminRightsGroup(final int id, final AdminRightsEnum[] rights) {
        this.m_id = id;
        this.m_rights = rights.clone();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean hasRight(final AdminRightsEnum right) {
        return ArrayUtils.contains(this.m_rights, right);
    }
    
    @Nullable
    public static AdminRightsGroup getRightsGroupFromId(final int id) {
        for (final AdminRightsGroup adminRightsGroup : values()) {
            if (adminRightsGroup.m_id == id) {
                return adminRightsGroup;
            }
        }
        return null;
    }
    
    public int[] getRights() {
        final int[] masks = new int[AdminRightHelper.getMaskArraySize()];
        for (final AdminRightsEnum adminRightsEnum : this.m_rights) {
            AdminRightHelper.addRight(masks, adminRightsEnum);
        }
        return masks;
    }
    
    public static AdminRightsGroup fromRights(final int[] rights) {
        for (final AdminRightsGroup g : values()) {
            if (AdminRightHelper.checkRights(rights, g.getRights())) {
                return g;
            }
        }
        return null;
    }
}
