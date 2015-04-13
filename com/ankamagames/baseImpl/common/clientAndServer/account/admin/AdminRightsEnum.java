package com.ankamagames.baseImpl.common.clientAndServer.account.admin;

import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;

public enum AdminRightsEnum implements AdminRight
{
    WHO(1, new short[] { 1, 110, 114 }), 
    WHERE(2, new short[] { 2 }), 
    STATS(3, new short[] { 3 }), 
    SERVERLOCK(4, new short[] { 4, 0, 2, 1 }), 
    TELEPORT(5, new short[] { 6, 5, 22, 59 }), 
    KICK(6, new short[] { 7, 111 }), 
    GHOST_CHECK(7, new short[] { 8 }), 
    SHUTDOWN(8, new short[] { 9 }), 
    SYMBIOT(9, new short[] { 10 }), 
    MONSTER_GROUP(10, new short[] { 11, 12, 16, 17, 95, 96, 141 }), 
    RIGHTS(11, new short[] { 13 }), 
    INVENTORY(12, new short[] { 14, 115, 116, 117, 143, 159, 160, 25 }), 
    REGENERATE(13, new short[] { 15 }), 
    FIGHT_DEBUG(14, new short[] { 18, 40 }), 
    SCENARIO(15, new short[] { 19, 20, 21 }), 
    CHARACTER_MODIFICATION(16, new short[] { 23, 24, 26, 24, 33, 44, 60, 77, 78, 97, 98, 99, 100, 101, 134, 135, 162, 183, 185, 184, 144 }), 
    GOD_MODE(17, new short[] { 30 }), 
    TELEPORT_PLAYER(18, new short[] { 31, 32, 36 }), 
    SYSMSG(19, new short[] { 34, 54 }), 
    INSTANCE_USAGE(20, new short[] { 37 }), 
    DESTROY_INSTANCE(21, new short[] { 38 }), 
    BOOST_FACTORS(22, new short[] { 39, 136, 137, 163, 164, 165, 238, 138 }), 
    PLAY_ANIMATION(23, new short[] { 42 }), 
    PLAY_APS(24, new short[] { 43 }), 
    SPAWN_IE(25, new short[] { 45 }), 
    SESSIONS(26, new short[] { 46 }), 
    IDENT_PHASE(27, new short[] { 47 }), 
    CHALLENGES(28, new short[] { 48, 104 }), 
    BAN(29, new short[] { 49, 105, 113 }), 
    HELP(30, new short[0]), 
    STAFF(31, new short[] { 51 }), 
    MUTE(32, new short[] { 52, 53, 140 }), 
    DISTRIBUTE_ITEMS(33, new short[] { 55 }), 
    SEARCH(34, new short[] { 56 }), 
    QUOTA(35, new short[] { 61 }), 
    RAGNAROK(36, new short[] { 63 }), 
    REMOVE_FLOOR_ITEMS(37, new short[] { 64 }), 
    SHOW_POPULATION(38, new short[] { 65 }), 
    SHOW_MONSTER_QUOTA(39, new short[] { 66 }), 
    CANCEL_MONSTER_COLLECT_COOLDOWN(40, new short[] { 67 }), 
    GET_MY_INSTANCE_UID(41, new short[] { 68 }), 
    NATION(42, new short[] { 74 }), 
    ACHIEVEMENT(43, new short[] { 75 }), 
    DUMP_BAG(44, new short[] { 76 }), 
    ZONE_BUFF(45, new short[] { 81 }), 
    PROTECTOR(46, new short[] { 84, 88, 132, 133 }), 
    ECOSYSTEM(47, new short[] { 85, 86, 91, 92, 93 }), 
    DEBUG(48, new short[] { 87, 119, 120, 193, 215, 216, 194, 125 }), 
    FIGHT(49, new short[] { 90, 102, 103 }), 
    SET_RESPAWN_POINT(50, new short[] { 94 }), 
    ICE_STATUS(51, new short[] { 106 }), 
    CHAOS(52, new short[] { 107, 108, 118 }), 
    SUBSCRIBER(53, new short[] { 109, 176, 177, 178, 179, 180, 175 }), 
    NEW_REQUEST_SANCTIONS(54, new short[] { 112 }), 
    GUILD(55, new short[] { 121, 126, 127, 145, 149, 150, 154, 155, 239, 157 }), 
    RESTAT(56, new short[] { 123, 124 }), 
    HAVEN_WORLD(57, new short[] { 128, 129, 130, 146, 147, 148, 158, 156, 161 }), 
    ALMANACH(58, new short[] { 131 }), 
    FREE_ACCESS(59, new short[] { 139 }), 
    GEM(60, new short[] { 142 }), 
    GAMEPLAY_STATS(61, new short[0]), 
    COMPANION(62, new short[] { 166, 167, 169, 170, 172, 173, 174, 181, 230, 168 }), 
    FIGHT_CHALLENGE(63, new short[] { 171 }), 
    RESTORE_CHARACTER(64, new short[] { 182 }), 
    ITEM_TRACKER(65, new short[] { 186 }), 
    BOT(66, new short[] { 190 }), 
    AI(67, new short[] { 191, 192 }), 
    POPUP_MODERATION_MESSAGE(68, new short[] { 195 }), 
    RED_MESSAGE(69, new short[] { 196, 197 }), 
    EMOTE_TARGETABLE(70, new short[] { 198 }), 
    HAVEN_BAG_KICK(71, new short[] { 199 }), 
    JAIL_COMMANDS(72, new short[] { 200, 201, 202, 203 }), 
    LIST_LOOT(73, new short[] { 204 }), 
    REVIVE(74, new short[] { 205 }), 
    GIVE_ITEM(75, new short[] { 207 }), 
    SYS_CONF(76, new short[] { 208, 209, 233, 214, 234 }), 
    SANCTION_MESSAGE(77, new short[] { 206 }), 
    SPECIAL_GET(78, new short[] { 211, 210, 213 }), 
    INTERSERVER(79, new short[] { 212 }), 
    PVP(80, new short[] { 218, 217, 219, 220, 236, 221 }), 
    APTITUDE(81, new short[] { 231, 237, 232 }), 
    MARKET(82, new short[] { 235 }), 
    HERO(83, new short[] { 240 }), 
    TAKE_ACCOUNT_CONTROL(149, new short[0]), 
    PASS_THROUGH_COMMUNITY_CHECK(150, new short[0]), 
    PASS_THROUGH_NAME_CHECK(151, new short[0]), 
    PASS_THROUGH_SERVER_LOCK_CHECK(152, new short[0]), 
    GET_WORLD_STATS(153, new short[0]), 
    MODIFY_CALENDAR(154, new short[0]), 
    EVOLVE_HAVEN_WORLD_BUILDING(155, new short[0]), 
    ADMIN_CHAT(156, new short[0]), 
    DEBUG_WORLD_EDITOR(157, new short[0]), 
    CAN_ANSWER_TO_PLAYER_REQUEST(158, new short[0]), 
    PASS_THROUGH_BAG_LOCK(159, new short[0]), 
    PASS_THROUGH_CONNECTION_QUEUE(160, new short[0]);
    
    private final short m_id;
    private final TShortHashSet m_moderationCommandProtocolIds;
    
    private AdminRightsEnum(final int id, final short[] moderationCommandProtocolIds) {
        this.m_moderationCommandProtocolIds = new TShortHashSet();
        this.m_id = MathHelper.ensureShort(id);
        this.m_moderationCommandProtocolIds.addAll(moderationCommandProtocolIds);
    }
    
    public short getId() {
        return this.m_id;
    }
    
    @Nullable
    public static AdminRightsEnum getRightFromCommand(final short command) {
        final TShortProcedure finder = new RightFromCommandFinder(command);
        for (final AdminRightsEnum adminRightsEnum : values()) {
            if (!adminRightsEnum.m_moderationCommandProtocolIds.forEach(finder)) {
                return adminRightsEnum;
            }
        }
        return null;
    }
    
    private static class RightFromCommandFinder implements TShortProcedure
    {
        private final short m_command;
        
        RightFromCommandFinder(final short command) {
            super();
            this.m_command = command;
        }
        
        @Override
        public boolean execute(final short value) {
            return this.m_command != value;
        }
        
        @Override
        public String toString() {
            return "RightFromCommandFinder{m_command=" + this.m_command + "} " + super.toString();
        }
    }
}
