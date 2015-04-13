package com.ankamagames.wakfu.common.game.ai.antlrcriteria.system;

import org.apache.log4j.*;
import org.antlr.runtime.*;

public class CritereLexer extends Lexer
{
    public static final int EOF = -1;
    public static final int AD = 4;
    public static final int AG = 5;
    public static final int AI_GET_SPELL_CAST_COUNT = 6;
    public static final int AI_HAS_CAST_SPELL = 7;
    public static final int AI_HAS_MOVED = 8;
    public static final int AND = 9;
    public static final int ASSIGN = 10;
    public static final int AT = 11;
    public static final int BARRELAMOUNT = 12;
    public static final int BD = 13;
    public static final int BEACONAMOUNT = 14;
    public static final int BG = 15;
    public static final int CANBECOMESOLDIERORMILITIAMAN = 16;
    public static final int CANCARRYTARGET = 17;
    public static final int CANRESETACHIEVEMENT = 18;
    public static final int CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER = 19;
    public static final int CELL_CONTAINS_SPECIFIC_EFFECT_AREA = 20;
    public static final int CHAR = 21;
    public static final int DIVIDE = 22;
    public static final int DOUBLE_OR_QUITS_CRITERION = 23;
    public static final int EFFECTISFROMHEAL = 24;
    public static final int ELSE = 25;
    public static final int EOL = 26;
    public static final int EQUALS = 27;
    public static final int FALSE = 28;
    public static final int FLOAT = 29;
    public static final int GETACHIEVEMENTVARIABLE = 30;
    public static final int GETALLIESCOUNTINRANGE = 31;
    public static final int GETBOOLEANVALUE = 32;
    public static final int GETCHA = 33;
    public static final int GETCHAMAX = 34;
    public static final int GETCHAPCT = 35;
    public static final int GETCHARACTERDIRECTION = 36;
    public static final int GETCHARACTERID = 37;
    public static final int GETCONTROLLERINSAMETEAMCOUNTINRANGE = 38;
    public static final int GETCRAFTLEARNINGITEM = 39;
    public static final int GETCRAFTLEVEL = 40;
    public static final int GETCRIMESCORE = 41;
    public static final int GETCURRENTPARTITIONNATIONID = 42;
    public static final int GETDATE = 43;
    public static final int GETDESTRUCTIBLECOUNTINRANGE = 44;
    public static final int GETDISTANCEBETWEENCASTERANDTARGET = 45;
    public static final int GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON = 46;
    public static final int GETEFFECTCASTER = 47;
    public static final int GETEFFECTTARGET = 48;
    public static final int GETENNEMYCOUNTINRANGE = 49;
    public static final int GETFIGHTMODEL = 50;
    public static final int GETIEPOSITION = 51;
    public static final int GETINSTANCEID = 52;
    public static final int GETKAMASCOUNT = 53;
    public static final int GETLASTINSTANCEID = 54;
    public static final int GETLEVEL = 55;
    public static final int GETLOCKINCREMENT = 56;
    public static final int GETMONST = 57;
    public static final int GETMONTH = 58;
    public static final int GETNATIONALIGNMENT = 59;
    public static final int GETNATIONID = 60;
    public static final int GETNATIONRANK = 61;
    public static final int GETNATIVENATIONID = 62;
    public static final int GETPOSITION = 63;
    public static final int GETPROTECTORNATIONID = 64;
    public static final int GETRANDOMNUMBER = 65;
    public static final int GETSATISFACTIONLEVEL = 66;
    public static final int GETSKILLLEVEL = 67;
    public static final int GETSPELLLEVEL = 68;
    public static final int GETSPELLTREELEVEL = 69;
    public static final int GETSTASISGAUGE = 70;
    public static final int GETSTATECOUNTINRANGE = 71;
    public static final int GETTEAMID = 72;
    public static final int GETTERRITORYID = 73;
    public static final int GETTERRITORYNATIONID = 74;
    public static final int GETTIME = 75;
    public static final int GETTITLE = 76;
    public static final int GETTRIGGEREREFFECTCASTER = 77;
    public static final int GETWAKFUGAUGE = 78;
    public static final int GETWALLCOUNTINRANGE = 79;
    public static final int GET_ACTIVE_SPELL_ID = 80;
    public static final int GET_ALLIES_COUNT = 81;
    public static final int GET_BOOLEAN_SYSTEM_CONFIGURATION = 82;
    public static final int GET_CHALLENGE_UNAVAILABILITY_DURATION = 83;
    public static final int GET_CURRENT_FIGHTER_ID = 84;
    public static final int GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER = 85;
    public static final int GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE = 86;
    public static final int GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS = 87;
    public static final int GET_EFFECT_AREA_COUNT_IN_RANGE = 88;
    public static final int GET_EFFECT_CASTER_ORIGINAL_CONTROLLER = 89;
    public static final int GET_EFFECT_TARGET_ORIGINAL_CONTROLLER = 90;
    public static final int GET_ENEMIES_HUMAN_COUNT_IN_RANGE = 91;
    public static final int GET_FGHT_CURRENT_TABLE_TURN = 92;
    public static final int GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE = 93;
    public static final int GET_FIGHTERS_LEVEL_DIFF = 94;
    public static final int GET_FIGHTERS_LEVEL_SUM = 95;
    public static final int GET_FIGHTERS_MAX_LEVEL = 96;
    public static final int GET_FIGHTERS_MIN_LEVEL = 97;
    public static final int GET_FIGHTERS_WITH_BREED_IN_RANGE = 98;
    public static final int GET_FIGHTER_ID = 99;
    public static final int GET_GUILD_LEVEL = 100;
    public static final int GET_GUILD_PARTNER_COUNT_IN_FIGHT = 101;
    public static final int GET_HUMAN_ALLIES_COUNT_IN_RANGE = 102;
    public static final int GET_NEXT_FIGHTER_IN_TIMELINE = 103;
    public static final int GET_OWN_ARMOR_COUNT = 104;
    public static final int GET_OWN_TEAM_STATE_COUNT_IN_RANGE = 105;
    public static final int GET_PARTITION_X = 106;
    public static final int GET_PARTITION_Y = 107;
    public static final int GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT = 108;
    public static final int GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE = 109;
    public static final int GET_STATE_LEVEL = 110;
    public static final int GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA = 111;
    public static final int GET_TARGET_COUNT_IN_BEACON_AREA = 112;
    public static final int GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE = 113;
    public static final int GET_TOTAL_HP_IN_PCT = 114;
    public static final int GET_TRIGGERING_ANCESTORS_COUNT = 115;
    public static final int GET_TRIGGERING_EFFECT_CASTER = 116;
    public static final int GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER = 117;
    public static final int GET_TRIGGERING_EFFECT_ID = 118;
    public static final int GET_TRIGGERING_EFFECT_TARGET = 119;
    public static final int GET_TRIGGERING_EFFECT_TARGET_BREED_ID = 120;
    public static final int GET_TRIGGERING_EFFECT_VALUE = 121;
    public static final int GET_X = 122;
    public static final int GET_XELOR_DIALS_COUNT = 123;
    public static final int GET_Y = 124;
    public static final int GET_Z = 125;
    public static final int HASAVAILABLECREATUREINSYMBIOT = 126;
    public static final int HASCRAFT = 127;
    public static final int HASEMOTE = 128;
    public static final int HASEQID = 129;
    public static final int HASEQTYPE = 130;
    public static final int HASFIGHTPROPERTY = 131;
    public static final int HASFREECELLINEFFECTAREA = 132;
    public static final int HASFREESURROUNDINGCELL = 133;
    public static final int HASLINEOFSIGHT = 134;
    public static final int HASNATIONJOB = 135;
    public static final int HASNTEVOLVEDSINCE = 136;
    public static final int HASPVPRANK = 137;
    public static final int HASSTATE = 138;
    public static final int HASSUMMONS = 139;
    public static final int HASSUMMONWITHBREED = 140;
    public static final int HASWORLDPROPERTY = 141;
    public static final int HAS_ANOTHER_SAME_EQUIPMENT = 142;
    public static final int HAS_BEEN_NAUGHTY = 143;
    public static final int HAS_BEEN_RAISED_BY_EFFECT = 144;
    public static final int HAS_CASTER_FECA_ARMOR = 145;
    public static final int HAS_EFFECT_WITH_ACTION_ID = 146;
    public static final int HAS_EFFECT_WITH_SPECIFIC_ID = 147;
    public static final int HAS_FECA_ARMOR = 148;
    public static final int HAS_GUILD_BONUS = 149;
    public static final int HAS_LINE_OF_SIGHT_FROM_ENEMY = 150;
    public static final int HAS_LINE_OF_SIGHT_TO_ENEMY = 151;
    public static final int HAS_LOOT = 152;
    public static final int HAS_STATE_FROM_LEVEL = 153;
    public static final int HAS_STATE_FROM_USER = 154;
    public static final int HAS_SUBSCRIPTION_LEVEL = 155;
    public static final int HAS_SURROUNDING_CELL_WITH_EFFECT_AREA = 156;
    public static final int HAS_SURROUNDING_CELL_WITH_OWN_BARREL = 157;
    public static final int HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA = 158;
    public static final int HAS_SURROUNDING_CELL_WITH_OWN_SUMMON = 159;
    public static final int HAS_UNLOCKED_COMPANION = 160;
    public static final int HAS_VALID_GATE_FOR_TP = 161;
    public static final int HAS_VALID_PATH_TO_TARGET = 162;
    public static final int HAS_WEAPON_TYPE = 163;
    public static final int HYP = 164;
    public static final int IF = 165;
    public static final int INF = 166;
    public static final int INFEQ = 167;
    public static final int INTEGER = 168;
    public static final int ISABANDONNING = 169;
    public static final int ISACCOUNTSUBSCRIBED = 170;
    public static final int ISACHIEVEMENTACTIVE = 171;
    public static final int ISACHIEVEMENTCOMPLETE = 172;
    public static final int ISACHIEVEMENTFAILED = 173;
    public static final int ISACHIEVEMENTOBJECTIVECOMPLETE = 174;
    public static final int ISACHIEVEMENTREPEATABLE = 175;
    public static final int ISACHIEVEMENTRUNNING = 176;
    public static final int ISACTIVATEDBYELEMENT = 177;
    public static final int ISACTIVATEDBYSPELL = 178;
    public static final int ISAFTER = 179;
    public static final int ISBACKSTAB = 180;
    public static final int ISBOMB = 181;
    public static final int ISBREED = 182;
    public static final int ISBREEDFAMILY = 183;
    public static final int ISBREEDID = 184;
    public static final int ISCARRIED = 185;
    public static final int ISCARRYING = 186;
    public static final int ISCHALLENGEUSER = 187;
    public static final int ISDAY = 188;
    public static final int ISDEAD = 189;
    public static final int ISDEPOSIT = 190;
    public static final int ISENNEMY = 191;
    public static final int ISEQUIPPEDWITHSET = 192;
    public static final int ISFACESTABBED = 193;
    public static final int ISFLEEING = 194;
    public static final int ISHOUR = 195;
    public static final int ISINGROUP = 196;
    public static final int ISMONSTERBREED = 197;
    public static final int ISNATIONFIRSTINDUNGEONLADDER = 198;
    public static final int ISOFFPLAY = 199;
    public static final int ISONEFFECTAREATYPE = 200;
    public static final int ISONSPECIFICEFFECTAREA = 201;
    public static final int ISONSPECIFICEFFECTAREAWITHSPECIFICSTATE = 202;
    public static final int ISOWNBEACON = 203;
    public static final int ISOWNBOMB = 204;
    public static final int ISOWNDEPOSIT = 205;
    public static final int ISOWNGLYPH = 206;
    public static final int ISOWNHOUR = 207;
    public static final int ISOWNSPECIFICAREA = 208;
    public static final int ISOWNSUMMON = 209;
    public static final int ISPASSEPORTACTIVE = 210;
    public static final int ISPROTECTORINFIGHT = 211;
    public static final int ISPVP = 212;
    public static final int ISSEASON = 213;
    public static final int ISSEX = 214;
    public static final int ISSPECIFICAREA = 215;
    public static final int ISSPECIFICAREAWITHSPECIFICSTATE = 216;
    public static final int ISTARGETCELLFREE = 217;
    public static final int ISTUNNEL = 218;
    public static final int ISUNDEAD = 219;
    public static final int ISUNDERCONTROL = 220;
    public static final int ISZONEINCHAOS = 221;
    public static final int IS_CARRYING_OWN_BARREL = 222;
    public static final int IS_CARRYING_OWN_BOMB = 223;
    public static final int IS_CASTER_FACING_FIGHTER = 224;
    public static final int IS_CELL_BEHIND_TARGET_FREE = 225;
    public static final int IS_CHALLENGER = 226;
    public static final int IS_CHARACTER = 227;
    public static final int IS_CHARACTERISTIC_FULL = 228;
    public static final int IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL = 229;
    public static final int IS_COMPANION = 230;
    public static final int IS_CONTROLLED_BY_AI = 231;
    public static final int IS_ENNEMY_NATION = 232;
    public static final int IS_FECA_GLYPH_CENTER = 233;
    public static final int IS_FREE_CELL = 234;
    public static final int IS_HERO = 235;
    public static final int IS_HOSTILE = 236;
    public static final int IS_IN_ALIGNMENT = 237;
    public static final int IS_IN_FIGHT = 238;
    public static final int IS_IN_GUILD = 239;
    public static final int IS_IN_PLAY = 240;
    public static final int IS_LOCKED = 241;
    public static final int IS_ON_BORDER_CELL = 242;
    public static final int IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA = 243;
    public static final int IS_ON_OWN_DIAL = 244;
    public static final int IS_ORIGINAL_CONTROLLER = 245;
    public static final int IS_OUT_OF_PLAY = 246;
    public static final int IS_OWN_AREA = 247;
    public static final int IS_OWN_FECA_GLYPH = 248;
    public static final int IS_PLAYER = 249;
    public static final int IS_PRELOADING = 250;
    public static final int IS_PVP_STATE_ACTIVE = 251;
    public static final int IS_SELECTED_CREATURE_AVAILABLE = 252;
    public static final int IS_SIDE_STABBED = 253;
    public static final int IS_SUMMON = 254;
    public static final int IS_SUMMON_FROM_SYMBIOT = 255;
    public static final int IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE = 256;
    public static final int IS_TARGET_ON_SAME_TEAM = 257;
    public static final int IS_TRIGGERED_BY_ZONE_EFFECT = 258;
    public static final int IS_TRIGGERING_EFFECT_CRITICAL = 259;
    public static final int ITEMQUANTITY = 260;
    public static final int LEADERSHIPFORCURRENTINVOC = 261;
    public static final int MAJ = 262;
    public static final int MIN = 263;
    public static final int MINUS = 264;
    public static final int MOD = 265;
    public static final int MULT = 266;
    public static final int NBBOMB = 267;
    public static final int NB_ALL_AREAS = 268;
    public static final int NB_AREAS_WITH_BASE_ID = 269;
    public static final int NB_FECA_GLYPH = 270;
    public static final int NB_GATES = 271;
    public static final int NB_GLYPHS = 272;
    public static final int NB_HYDRANDS = 273;
    public static final int NB_ROUBLABOT = 274;
    public static final int NB_STEAMBOTS = 275;
    public static final int NOT = 276;
    public static final int NOT_EQUALS = 277;
    public static final int OPPONENTSCONTAINSNATIONENEMY = 278;
    public static final int OR = 279;
    public static final int OWNSBEACON = 280;
    public static final int PD = 281;
    public static final int PETWITHINRANGE = 282;
    public static final int PG = 283;
    public static final int PLUS = 284;
    public static final int POINT = 285;
    public static final int PV = 286;
    public static final int SHARP = 287;
    public static final int SLOTSINBAG = 288;
    public static final int SPACEINSYMBIOT = 289;
    public static final int STRING = 290;
    public static final int SUMMONAMOUNT = 291;
    public static final int SUMMONSLEADERSHIPSCORE = 292;
    public static final int SUP = 293;
    public static final int SUPEQ = 294;
    public static final int THEN = 295;
    public static final int TRAPAMOUNT = 296;
    public static final int TRUE = 297;
    public static final int USE_GATE_EFFECT = 298;
    public static final int VARNAME = 299;
    public static final int VIRGULE = 300;
    public static final int WALLAMOUNT = 301;
    public static final int WS = 302;
    protected static final Logger m_logger;
    protected DFA32 dfa32;
    static final String DFA32_eotS = "\u0002\uffff\u0002\u001a\u0001\uffff\u0001\u001a\u0001\uffff\u0001=\u0001?\u0003\u001a\u0004\uffff\u0001D\u0001G\u0001I\u0004\uffff\u0001\u001a\u0001\uffff\u0001\u001a\u0012\uffff\u0001[\u0001]\t\uffff\u0001\u0004\u0002\u001a\u0002\u0006\u0004\uffff\u0001\u001a\u0001b\u0002\u001a\u0007\uffff\u0002\u001a\u000f\uffff\u0001]\u0001\uffff\u0001\u001a\u0001\u0004\u0002=\u0001\uffff\u0002\u001a\u0001\uffff\u0003\u001a \uffff\u0001\u00e6\u0001\u00e7\u0002\u0016\u0001\u001a\u0001\u0018Y\uffff\u0001\u0018.\uffff\u0001\u0168\u007f\uffff\u0001\u01d2.\uffff\u0001\u01f6\u0006\uffff\u0001\u00fc\u0001\uffff\u0001\u01fd#\uffff\u0001N\u0014\uffff\u0001\u0233\u001d\uffff\u0001\u024d\u0002\uffff\u0001\u0251%\uffff\u0001\u0272Y\uffff\u0001\u02c0\u0001\u024d\u0010\uffff\u0001\u02ce\r\uffff\u0001\u02d5\u0001\u02d7\u0012\uffff\u0001\u0233\u001f\uffff\u0001\u0306\u0011\uffff\u0001\u0316\u0001\u0318\f\uffff";
    static final String DFA32_eofS = "\u031d\uffff";
    static final String DFA32_minS = "\u0001\t\u0001\uffff\u0001l\u0001n\u0001\uffff\u0001r\u0001\uffff\u0002=\u0001o\u0001f\u0001h\u0004\uffff\u00010\u0002=\u0002\uffff\u0001r\u0001\uffff\u0001r\u0001\uffff\u0001a\u0001\uffff\u0002a\u0001s\u0001e\u0002\uffff\u0001b\u0001\uffff\u0001u\u0001\uffff\u0001f\u0001p\u0001e\u0001I\u0003\uffff\u0002.\t\uffff\u0001!\u0001s\u0001d\u0002!\u0004\uffff\u0001n\u0001!\u0001e\u0001u\u0006\uffff\u0002a\u0001l\u0001s\u0001\uffff\u0001n\u0001\uffff\u0001A\u0001t\u0001A\u0001m\u0005\uffff\u0001G\u0001\uffff\u0001.\u0001\uffff\u0001e\u0003!\u0001\uffff\u0001n\u0001e\u0001\uffff\u0001i\u0001s\u0001x\u0001A\u0001B\u0001\uffff\u0001n\u0001e\u0002a\u0001n\u0001\uffff\u0001a\u0001b\u0002a\u0001f\u0001a\u0001e\u0001n\u0003\uffff\u0001A\u0001t\u0001a\u0004\uffff\u0001l\u0001a\u0001m\u0001a\u0001\uffff\u0004!\u0001e\u0001!\u0001f\u0001t\u0001e\u0001i\u0001e\u0001\uffff\u0001n\u0001a\u0001\uffff\u0001a\u0001e\u0006\uffff\u0001e\u0001\uffff\u0001a\u0001e\u0001m\u0002\uffff\u0001a\u0001e\u0002\uffff\u0001d\u0001a\u0001r\u0001m\u0002\uffff\u0001c\u0005\uffff\u0001r\u0001\uffff\u0001i\u0001n\u0001\uffff\u0001B\u0003\uffff\u0001e\u0001\uffff\u0001p\u0001s\u0001\uffff\u0001A\u0001h\u0001o\u0002a\u0001o\u0001a\u0001E\u0001f\u0002a\u0001\uffff\u0001a\u0001\uffff\u0001c\u0001a\u0001o\u0001i\u0001u\u0003\uffff\u0001w\b\uffff\u0001o\u0001s\u0002\uffff\u0001!\u0001u\u0001\uffff\u0001f\u0001b\u0001a\u0001e\u0002\uffff\u0001n\u0007\uffff\u0001l\u0002e\u0004\uffff\u0001c\u0001m\u0002\uffff\u0002e\u0001l\u0001r\u0003\uffff\u0002i\u0001\uffff\u0002g\u0001A\u0001p\u0002\uffff\u0001r\u0002\uffff\u0001S\u0002\uffff\u0001r\u0003\uffff\u0001a\u0001\uffff\u0001a\u0001r\u0001n\u0001a\u0001\uffff\u0001a\u0001\uffff\u0001k\u0002\uffff\u0001m\u0001i\u0001a\u0001t\u0001r\u0003\uffff\u0001f\u0001e\u0001s\u0003\uffff\u0001o\u0001r\u0003\uffff\u0001l\u0001h\u0001t\u0001\uffff\u0001o\u0001g\u0001i\u0002n\u0001C\u0001i\u0001e\u0001m\u0001r\u0001\uffff\u0001t\u0002e\u0001i\u0001n\u0001m\u0001i\u0001o\u0001d\u0001a\u0001l\u0001a\u0001i\u0001e\u0001v\u0001e\u0001g\u0001p\u0001\uffff\u0001e\u0004\uffff\u0001e\u0006\uffff\u0001l\u0001\uffff\u0001f\u0001r\u0001s\u0001c\u0001s\u0001l\u0002\uffff\u0001g\u0001m\u0001r\u0001a\u0001g\u0001e\u0002\uffff\u0003t\u0001i\u0002\uffff\u0001i\u0001l\u0001h\u0001l\u0002A\u0002\uffff\u0001p\u0001c\u0002o\u0001e\u0001C\u0001O\u0001d\u0001N\u0001y\u0001f\u0001n\u0001F\u0002\uffff\u0001e\u0001c\u0001\uffff\u0001i\u0001v\u0001a\u0001t\u0001e\u0004\uffff\u0001c\u0001a\u0001\uffff\u0001t\u0001e\u0001\uffff\u0001l\u0002\uffff\u0001e\u0001A\u0001g\u0001E\u0001i\u0001l\u0001e\u0001c\u0001a\u0001e\u0001i\u0001e\u0001o\u0001e\u0001t\u0001d\u0003\uffff\u0001m\u0001t\u0001n\u0001u\u0001F\u0002\uffff\u0001f\u0001G\u0002\uffff\u0001N\u0001i\u0001e\u0003\uffff\u0001n\u0001F\u0001n\u0001e\u0001t\u0001C\u0001r\u0001i\u0001c\u0001L\u0001n\u0001L\u0001C\u0001\uffff\u0001e\u0002\uffff\u0001t\u0001H\u0002t\u0001n\u0001c\u0001t\u0001s\u0001n\u0001\uffff\u0001a\u0001C\u0001L\u0001e\u0002W\u0001n\u0001r\u0001\uffff\u0001S\u0003\uffff\u0001c\u0002\uffff\u0001g\u0001e\u0001\uffff\u0001g\u0001m\u0002e\u0001\uffff\u0001e\u0001f\u0001I\u0001e\u0001t\u0004\uffff\u0001r\u0001o\u0002\uffff\u0001C\u0001A\u0001c\u0001t\u0001i\u0001C\u0001A\u0001n\u0001\uffff\u0001r\u0003\uffff\u0001n\u0001i\u0001\uffff\u0001d\u0001o\u0001i\u0001A\u0001e\u0001r\u0001O\u0001e\u0001d\u0001l\u0002\uffff\u0001i\u0001e\u0003\uffff\u0001a\u0001F\u0001e\u0001r\u0002\uffff\u0002a\u0001r\u0001\uffff\u0001e\u0003o\u0003\uffff\u0001S\u0001I\u0002t\u0001i\u0001m\u0001g\u0001r\u0001\uffff\u0001U\u0001W\u0001w\u0001\uffff\u0001n\u0001B\u0001l\u0001c\u0001r\u0005\uffff\u0001n\u0001y\u0001s\u0001r\u0001e\u0001B\u0001r\u0001n\u0001u\u0002\uffff\u0001C\u0001\uffff\u0001I\u0001h\u0001n\u0001L\u0001h\u0001e\u0004\uffff\u0001n\u0001t\u0001y\u0001F\u0001E\u0001D\u0001g\u0001I\u0001t\u0001g\u0001a\u0001e\u0001C\u0001X\u0001n\u0001\uffff\u0001a\u0001e\u0003\uffff\u0001A\u0001g\u0002\uffff\u0001t\u0001a\u0001B\u0001A\u0001E\u0002\uffff\u0001f\u0001s\u0002\uffff\u0001A\u0002\uffff\u0002e\u0001C\u0001t\u0004\uffff\u0001t\u0002\uffff\u0001v\u0002\uffff\u0001C\u0001F\u0001W\u0001a\u0001\uffff\u0001e\u0005\uffff\u0001f\u0001t\u0001f\u0001\uffff\u0001r\u0001t\u0001o\u0001w\u0001I\u0002e\b\uffff\u0001e\u0001i\u0001f\u0002O\u0001u\u0001e\u0002\uffff\u0002l\u0002c\u0001e\u0004\uffff\u0001n\u0001e\u0001D\u0001l\u0001t\u0001M\u0001c\u0001t\u0001n\u0002\uffff\u0001W\u0001A\u0001t\u0001I\u0001C\u0001i\u0001r\u0001C\u0001n\u0001\uffff\u0001a\u0001t\u0001e\u0002a\u0002\uffff\u0001R\u0001r\u0001h\u0001a\u0001s\u0001r\u0001a\u0001g\u0001E\u0001W\u0001t\u0001g\u0002\uffff\u0001e\u0001w\u0003\uffff\u0002e\u0001t\u0001n\u0001r\u0001t\u0001A\u0001B\u0001I\u0001B\u0001n\u0007\uffff\u0001d\u0001E\u0002\uffff";
    static final String DFA32_maxS = "\u0001}\u0001\uffff\u0001t\u0001n\u0001\uffff\u0001u\u0001\uffff\u0001=\u0001>\u0001o\u0001f\u0001r\u0004\uffff\u00019\u0002=\u0002\uffff\u0001r\u0001\uffff\u0001r\u0001\uffff\u0001a\u0001\uffff\u0001a\u0001h\u0001s\u0001e\u0002\uffff\u0001b\u0001\uffff\u0001u\u0001\uffff\u0001m\u0001w\u0001l\u0001I\u0003\uffff\u00029\t\uffff\u0001z\u0001s\u0001d\u0002z\u0004\uffff\u0001t\u0001z\u0001e\u0001u\u0006\uffff\u0001u\u0001a\u0001u\u0001s\u0001\uffff\u0001s\u0001\uffff\u0001Z\u0001t\u0001X\u0001m\u0005\uffff\u0001H\u0001\uffff\u00019\u0001\uffff\u0001e\u0003z\u0001\uffff\u0001n\u0001e\u0001\uffff\u0001i\u0001s\u0001x\u0001n\u0001R\u0001\uffff\u0001q\u0001u\u0001e\u0001r\u0001n\u0001\uffff\u0001o\u0001f\u0001r\u0001u\u0001w\u0001v\u0001o\u0001n\u0003\uffff\u0001Z\u0001u\u0001o\u0004\uffff\u0001r\u0001l\u0001m\u0001a\u0001\uffff\u0004z\u0001e\u0001z\u0001q\u0001u\u0001r\u0002o\u0001\uffff\u0001v\u0001r\u0001\uffff\u0001a\u0001e\u0006\uffff\u0001n\u0001\uffff\u0001x\u0001e\u0001m\u0002\uffff\u0001p\u0001e\u0002\uffff\u0001d\u0001a\u0001s\u0001n\u0002\uffff\u0001t\u0005\uffff\u0001r\u0001\uffff\u0001i\u0001n\u0001\uffff\u0001S\u0003\uffff\u0001o\u0001\uffff\u0001p\u0001u\u0001\uffff\u0001P\u0001u\u0001o\u0001t\u0001a\u0001u\u0001r\u0001t\u0001n\u0001i\u0001r\u0001\uffff\u0001o\u0001\uffff\u0001l\u0001e\u0001o\u0001i\u0001u\u0003\uffff\u0001w\b\uffff\u0001o\u0001s\u0002\uffff\u0001z\u0001u\u0001\uffff\u0001f\u0001r\u0001a\u0001e\u0002\uffff\u0001n\u0007\uffff\u0001l\u0002e\u0004\uffff\u0001c\u0001m\u0002\uffff\u0002e\u0002r\u0003\uffff\u0002i\u0001\uffff\u0002g\u0001S\u0001p\u0002\uffff\u0001w\u0002\uffff\u0001S\u0002\uffff\u0001u\u0003\uffff\u0001a\u0001\uffff\u0001i\u0001r\u0001n\u0001e\u0001\uffff\u0001a\u0001\uffff\u0001l\u0002\uffff\u0001t\u0001i\u0001r\u0001t\u0001r\u0003\uffff\u0001f\u0001n\u0001s\u0003\uffff\u0001o\u0001r\u0003\uffff\u0001l\u0002t\u0001\uffff\u0001o\u0001g\u0001i\u0002n\u0001M\u0001i\u0001e\u0001m\u0001r\u0001\uffff\u0001t\u0002e\u0001i\u0001n\u0001m\u0001i\u0001o\u0001d\u0001r\u0001l\u0001a\u0001y\u0001e\u0001v\u0001e\u0001g\u0001u\u0001\uffff\u0001o\u0004\uffff\u0001e\u0006\uffff\u0001r\u0001\uffff\u0001f\u0001r\u0001t\u0001l\u0001t\u0001l\u0002\uffff\u0001g\u0001m\u0001r\u0001a\u0001g\u0001e\u0002\uffff\u0003t\u0001i\u0002\uffff\u0001i\u0001l\u0001h\u0001l\u0001T\u0001s\u0002\uffff\u0001p\u0001c\u0002o\u0001e\u0001S\u0001O\u0001d\u0001R\u0001y\u0001f\u0001n\u0001I\u0002\uffff\u0001e\u0001c\u0001\uffff\u0001i\u0001v\u0001a\u0001t\u0001e\u0004\uffff\u0001c\u0001a\u0001\uffff\u0001t\u0001e\u0001\uffff\u0001l\u0002\uffff\u0001e\u0001C\u0001g\u0001I\u0001i\u0001l\u0001e\u0001c\u0001a\u0001e\u0001i\u0001e\u0001v\u0001e\u0001t\u0001d\u0003\uffff\u0001m\u0001t\u0001n\u0001u\u0001F\u0002\uffff\u0001f\u0001P\u0002\uffff\u0001N\u0001i\u0001e\u0003\uffff\u0001n\u0001t\u0001n\u0001e\u0001t\u0001O\u0001r\u0001i\u0001c\u0001L\u0001n\u0001T\u0001L\u0001\uffff\u0001e\u0002\uffff\u0001t\u0001L\u0002t\u0001n\u0001c\u0001t\u0001s\u0001n\u0001\uffff\u0001a\u0001e\u0001P\u0001e\u0002W\u0001n\u0001r\u0001\uffff\u0001S\u0003\uffff\u0001c\u0002\uffff\u0001g\u0001e\u0001\uffff\u0001g\u0001m\u0002e\u0001\uffff\u0001i\u0001f\u0001t\u0001e\u0001t\u0004\uffff\u0001r\u0001o\u0002\uffff\u0002s\u0001c\u0001t\u0001i\u0001C\u0001R\u0001n\u0001\uffff\u0001r\u0003\uffff\u0001n\u0001i\u0001\uffff\u0001d\u0001o\u0001i\u0001A\u0001e\u0001r\u0001O\u0001e\u0001d\u0001l\u0002\uffff\u0001i\u0001e\u0003\uffff\u0001v\u0001P\u0001i\u0001r\u0002\uffff\u0002a\u0001r\u0001\uffff\u0001e\u0003o\u0003\uffff\u0001V\u0001s\u0002t\u0001i\u0001m\u0001g\u0001r\u0001\uffff\u0001r\u0001i\u0001w\u0001\uffff\u0001n\u0001B\u0001l\u0001c\u0001r\u0005\uffff\u0001n\u0001y\u0001s\u0001r\u0001e\u0001B\u0001r\u0001n\u0001u\u0002\uffff\u0001W\u0001\uffff\u0001T\u0001h\u0001n\u0001U\u0001h\u0001e\u0004\uffff\u0001n\u0001t\u0001y\u0001V\u0001E\u0001i\u0001g\u0001N\u0001t\u0001g\u0001a\u0001e\u0001N\u0001Y\u0001n\u0001\uffff\u0001i\u0001e\u0003\uffff\u0001S\u0001g\u0002\uffff\u0001t\u0001a\u0001B\u0001R\u0001S\u0002\uffff\u0001f\u0001s\u0002\uffff\u0001E\u0002\uffff\u0002e\u0001C\u0001t\u0004\uffff\u0001t\u0002\uffff\u0001v\u0002\uffff\u0001C\u0001T\u0001W\u0001o\u0001\uffff\u0001u\u0005\uffff\u0001f\u0001t\u0001f\u0001\uffff\u0001r\u0001t\u0001o\u0001w\u0001I\u0002e\b\uffff\u0001e\u0001i\u0001f\u0002O\u0001u\u0001e\u0002\uffff\u0002l\u0002c\u0001e\u0004\uffff\u0001n\u0001e\u0001S\u0001l\u0001t\u0001P\u0001c\u0001t\u0001n\u0002\uffff\u0001W\u0001A\u0001t\u0001I\u0001T\u0001i\u0001r\u0001V\u0001n\u0001\uffff\u0001a\u0001t\u0001e\u0002a\u0002\uffff\u0001R\u0001r\u0001h\u0001a\u0001s\u0001r\u0001u\u0001g\u0001O\u0001W\u0001t\u0001g\u0002\uffff\u0001e\u0001w\u0003\uffff\u0002e\u0001t\u0001n\u0001r\u0001t\u0001A\u0001S\u0001I\u0001B\u0001n\u0007\uffff\u0001d\u0001N\u0002\uffff";
    static final String DFA32_acceptS = "\u0001\uffff\u0001\u0001\u0002\uffff\u0001\u0002\u0001\uffff\u0001\u0003\u0005\uffff\u0001\t\u0001\n\u0001\u000b\u0001\f\u0003\uffff\u0001\u0014\u0001\u0015\u0001\uffff\u0001\u0016\u0001\uffff\u0001\u0017\u0001\uffff\u0001\u0018\u0004\uffff\u0001!\u0001\"\u0001\uffff\u0001$\u0001\uffff\u0001&\u0004\uffff\u0001¶\u0001\u0118\u0001\u011b\u0002\uffff\u0001\u011e\u0001\u011f\u0001\u0120\u0001\u0121\u0001\u0123\u0001\u0124\u0001\u0125\u0001\u0126\u0001\u0127\u0005\uffff\u0001\u0004\u0001\u0005\u0001\u0010\u0001\u000e\u0004\uffff\u0001\r\u0001\u011d\u0001\u0011\u0001\u000f\u0001\u0013\u0001\u0012\u0004\uffff\u0001\u001b\u0001\uffff\u0001\u0099\u0004\uffff\u0001*\u0001G\u0001+\u0001\u008b\u0001@\u0001\uffff\u0001\u0122\u0001\uffff\u0001\u011c\u0004\uffff\u0001\u0006\u0002\uffff\u0001#\u0005\uffff\u0001\u00c5\u0005\uffff\u0001J\b\uffff\u0001\u009e\u0001\u00f3\u0001\u00f8\u0003\uffff\u0001(\u0001»\u0001¿\u0001\u00cb\u0004\uffff\u0001¨\u000b\uffff\u0001N\u0002\uffff\u0001\u008c\u0002\uffff\u0001\u00f7\u0001\u0102\u0001\u010f\u0001\u001d\u0001x\u0001\u008a\u0001\uffff\u0001{\u0003\uffff\u0001\u00d5\u0001/\u0002\uffff\u0001A\u0001~\u0004\uffff\u0001\u00ef\u0001T\u0001\uffff\u0001\u009d\u0001a\u0001\u009c\u0001¾\u0001\u00e3\u0001\uffff\u0001\u0080\u0002\uffff\u0001\u0090\u0001\uffff\u0001\u00c4\u0001\u0103\u0001w\u0001\uffff\u0001\u00d8\u0002\uffff\u0001\u011a\u000b\uffff\u0001I\u0001\uffff\u0001V\u0005\uffff\u0001\u00e5\u0001\u00e6\u0001\u00e7\u0001\uffff\u0001%\u0001\u00cc\u0001'\u0001\u0085\u0001\u00d0\u0001\u00e8\u0001\u00d1\u0001\u0115\u0002\uffff\u0001\b\u0001\u0007\u0002\uffff\u0001v\u0004\uffff\u0001L\u0001½\u0001\uffff\u0001²\u0001K\u0001·\u0001m\u0001\u00f2\u0001t\u0001\u00da\u0003\uffff\u0001\u001c\u0001)\u00010\u0001F\u0002\uffff\u0001c\u0001\u009a\u0004\uffff\u0001\u0106\u0001ª\u0001\u00d9\u0002\uffff\u0001\u00c7\u0004\uffff\u0001\u0097\u0001¹\u0001\uffff\u0001\u008f\u0001\u0101\u0001\uffff\u0001|\u0001\u0113\u0001\uffff\u0001\u00e1\u0001\u0104\u0001\u0105\u0001\uffff\u0001Y\u0004\uffff\u00015\u0001\uffff\u0001d\u0001\uffff\u0001.\u0001\u00f5\u0005\uffff\u00018\u0001E\u0001H\u0003\uffff\u0001Z\u0001`\u0001D\u0002\uffff\u0001O\u0001P\u0001\u0112\u0003\uffff\u0001\u00f9\n\uffff\u0001\u010c\u0012\uffff\u0001}\u0001\uffff\u0001\u0081\u0001\u0098\u0001¸\u0001\u00cd\u0001\uffff\u0001\u00ca\u0001\u00f1\u0001\u0111\u0001\u010e\u0001\u0091\u0001\u00df\u0001\uffff\u0001b\u0006\uffff\u0001U\u0001y\u0006\uffff\u0001W\u0001°\u0004\uffff\u0001\u0094\u0001\u00c0\u0006\uffff\u0001¦\u0001§\r\uffff\u00013\u0001S\u0002\uffff\u0001k\u0005\uffff\u0001q\u0001\u0083\u0001\u007f\u0001\u0082\u0002\uffff\u0001\u00c1\u0002\uffff\u0001M\u0001\uffff\u0001\u00fb\u0001_\u0010\uffff\u0001\u0109\u0001\u010a\u0001n\u0005\uffff\u0001?\u0001h\u0002\uffff\u0001\u00e4\u0001\u0114\u0003\uffff\u00011\u0001Q\u00012\r\uffff\u0001[\u0001\uffff\u0001<\u0001\u0116\t\uffff\u0001^\b\uffff\u00014\u0001\uffff\u0001\u00e2\u0001\u0117\u0001\u0110\u0001\uffff\u0001¬\u0001«\u0002\uffff\u0001\u00c6\u0004\uffff\u0001±\u0005\uffff\u00016\u00017\u0001\u009b\u0001\u00f0\u0002\uffff\u0001o\u0001\u00ec\b\uffff\u0001\u009f\u0001\uffff\u0001\u00cf\u0001\u00de\u0001\u00e0\u0002\uffff\u0001\u0084\n\uffff\u0001\u00fa\u0001\u010d\u0002\uffff\u0001\u001f\u0001 \u0001\u001e\u0004\uffff\u0001¤\u0001©\u0003\uffff\u0001\u00e9\u0004\uffff\u0001\\\u0001]\u0001z\b\uffff\u0001,\u0003\uffff\u0001l\u0005\uffff\u0001s\u0001u\u0001f\u0001\u0119\u0001;\t\uffff\u0001e\u0001\u00fc\u0001\uffff\u0001\u0107\u0006\uffff\u0001R\u0001\u00db\u0001\u00f6\u0001´\u000f\uffff\u0001¥\u0002\uffff\u0001\u010b\u0001\u0019\u0001\u001a\u0002\uffff\u0001µ\u0001\u00f4\u0005\uffff\u0001i\u0001j\u0002\uffff\u0001C\u0001r\u0001\uffff\u0001g\u0001p\u0004\uffff\u0001º\u0001\u00ed\u0001\u00ea\u0001\u00eb\u0001\uffff\u0001\u00fd\u0001\u00fe\u0001\uffff\u0001³\u0001¼\u0004\uffff\u0001\u0086\u0001\uffff\u0001\u0088\u0001\u008d\u0001\u008e\u0001\u0092\u0001\u0093\u0003\uffff\u0001\u00d3\u0007\uffff\u0001¯\u0001\u00d7\u0001B\u0001-\u0001£\u0001\u00dc\u0001\u0087\u0001\u0089\u0007\uffff\u0001X\u0001\u00ee\u0005\uffff\u0001\u00c2\u00019\u0001\u00c3\u0001:\t\uffff\u0001\u00ff\u0001\u0100\t\uffff\u0001=\u0005\uffff\u0001\u00d4\u0001\u0108\f\uffff\u0001\u00ad\u0001®\u0002\uffff\u0001¢\u0001\u0096\u0001\u0095\u000b\uffff\u0001 \u0001¡\u0001\u00dd\u0001\u00c8\u0001\u00c9\u0001\u00d2\u0001\u00d6\u0002\uffff\u0001>\u0001\u00ce";
    static final String DFA32_specialS = "\u031d\uffff}>";
    static final String[] DFA32_transitionS;
    static final short[] DFA32_eot;
    static final short[] DFA32_eof;
    static final char[] DFA32_min;
    static final char[] DFA32_max;
    static final short[] DFA32_accept;
    static final short[] DFA32_special;
    static final short[][] DFA32_transition;
    
    public void emitErrorMessage(final String msg) {
        CritereLexer.m_logger.warn((Object)msg);
    }
    
    public Lexer[] getDelegates() {
        return new Lexer[0];
    }
    
    public CritereLexer() {
        super();
        this.dfa32 = new DFA32((BaseRecognizer)this);
    }
    
    public CritereLexer(final CharStream input) {
        this(input, new RecognizerSharedState());
    }
    
    public CritereLexer(final CharStream input, final RecognizerSharedState state) {
        super(input, state);
        this.dfa32 = new DFA32((BaseRecognizer)this);
    }
    
    public String getGrammarFileName() {
        return "C:\\Users\\abarth\\Documents\\code\\trunk\\src\\maven\\wakfu-parent\\wakfu-parent-java\\wakfu-common\\src\\main\\java\\com\\ankamagames\\wakfu\\common\\game\\ai\\antlrcriteria\\system\\Critere.g";
    }
    
    public final void mPD() throws RecognitionException {
        try {
            final int _type = 281;
            final int _channel = 0;
            this.match(41);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAND() throws RecognitionException {
        try {
            final int _type = 9;
            final int _channel = 0;
            int alt1 = 3;
            switch (this.input.LA(1)) {
                case 101: {
                    alt1 = 1;
                    break;
                }
                case 97: {
                    alt1 = 2;
                    break;
                }
                case 38: {
                    alt1 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 1, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt1) {
                case 1: {
                    this.match("et");
                    break;
                }
                case 2: {
                    this.match("and");
                    break;
                }
                case 3: {
                    this.match("&&");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mOR() throws RecognitionException {
        try {
            final int _type = 279;
            final int _channel = 0;
            int alt2 = 3;
            final int LA2_0 = this.input.LA(1);
            if (LA2_0 == 111) {
                final int LA2_ = this.input.LA(2);
                if (LA2_ == 117) {
                    alt2 = 1;
                }
                else if (LA2_ == 114) {
                    alt2 = 2;
                }
                else {
                    final int nvaeMark = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae = new NoViableAltException("", 2, 1, (IntStream)this.input);
                        throw nvae;
                    }
                    finally {
                        this.input.rewind(nvaeMark);
                    }
                }
            }
            else {
                if (LA2_0 != 124) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 2, 0, (IntStream)this.input);
                    throw nvae2;
                }
                alt2 = 3;
            }
            switch (alt2) {
                case 1: {
                    this.match("ou");
                    break;
                }
                case 2: {
                    this.match("or");
                    break;
                }
                case 3: {
                    this.match("||");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNOT_EQUALS() throws RecognitionException {
        try {
            final int _type = 277;
            final int _channel = 0;
            int alt3 = 2;
            final int LA3_0 = this.input.LA(1);
            if (LA3_0 == 33) {
                alt3 = 1;
            }
            else {
                if (LA3_0 != 60) {
                    final NoViableAltException nvae = new NoViableAltException("", 3, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt3 = 2;
            }
            switch (alt3) {
                case 1: {
                    this.match("!=");
                    break;
                }
                case 2: {
                    this.match("<>");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNOT() throws RecognitionException {
        try {
            final int _type = 276;
            final int _channel = 0;
            int alt4 = 3;
            final int LA4_0 = this.input.LA(1);
            if (LA4_0 == 110) {
                final int LA4_ = this.input.LA(2);
                if (LA4_ == 111) {
                    final int LA4_2 = this.input.LA(3);
                    if (LA4_2 == 110) {
                        alt4 = 1;
                    }
                    else if (LA4_2 == 116) {
                        alt4 = 2;
                    }
                    else {
                        final int nvaeMark = this.input.mark();
                        try {
                            for (int nvaeConsume = 0; nvaeConsume < 2; ++nvaeConsume) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae = new NoViableAltException("", 4, 3, (IntStream)this.input);
                            throw nvae;
                        }
                        finally {
                            this.input.rewind(nvaeMark);
                        }
                    }
                }
                else {
                    final int nvaeMark2 = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae2 = new NoViableAltException("", 4, 1, (IntStream)this.input);
                        throw nvae2;
                    }
                    finally {
                        this.input.rewind(nvaeMark2);
                    }
                }
            }
            else {
                if (LA4_0 != 33) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 4, 0, (IntStream)this.input);
                    throw nvae3;
                }
                alt4 = 3;
            }
            switch (alt4) {
                case 1: {
                    this.match("non");
                    break;
                }
                case 2: {
                    this.match("not");
                    break;
                }
                case 3: {
                    this.match(33);
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIF() throws RecognitionException {
        try {
            final int _type = 165;
            final int _channel = 0;
            this.match("if");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mTHEN() throws RecognitionException {
        try {
            final int _type = 295;
            final int _channel = 0;
            this.match("then");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mELSE() throws RecognitionException {
        try {
            final int _type = 25;
            final int _channel = 0;
            this.match("else");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAG() throws RecognitionException {
        try {
            final int _type = 5;
            final int _channel = 0;
            this.match(123);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAD() throws RecognitionException {
        try {
            final int _type = 4;
            final int _channel = 0;
            this.match(125);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mBG() throws RecognitionException {
        try {
            final int _type = 15;
            final int _channel = 0;
            this.match(91);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mBD() throws RecognitionException {
        try {
            final int _type = 13;
            final int _channel = 0;
            this.match(93);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mPOINT() throws RecognitionException {
        try {
            final int _type = 285;
            final int _channel = 0;
            this.match(46);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHYP() throws RecognitionException {
        try {
            this.match(34);
        }
        finally {}
    }
    
    public final void mINF() throws RecognitionException {
        try {
            final int _type = 166;
            final int _channel = 0;
            this.match(60);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSUP() throws RecognitionException {
        try {
            final int _type = 293;
            final int _channel = 0;
            this.match(62);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mINFEQ() throws RecognitionException {
        try {
            final int _type = 167;
            final int _channel = 0;
            this.match("<=");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSUPEQ() throws RecognitionException {
        try {
            final int _type = 294;
            final int _channel = 0;
            this.match(">=");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mASSIGN() throws RecognitionException {
        try {
            final int _type = 10;
            final int _channel = 0;
            this.match(61);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mEQUALS() throws RecognitionException {
        try {
            final int _type = 27;
            final int _channel = 0;
            this.match("==");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mPG() throws RecognitionException {
        try {
            final int _type = 283;
            final int _channel = 0;
            this.match(40);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSTRING() throws RecognitionException {
        Label_0410: {
            try {
                final int _type = 290;
                final int _channel = 0;
                this.mHYP();
                int cnt5 = 0;
                while (true) {
                    int alt5 = 2;
                    final int LA5_0 = this.input.LA(1);
                    if ((LA5_0 >= 32 && LA5_0 <= 33) || LA5_0 == 39 || LA5_0 == 46 || (LA5_0 >= 48 && LA5_0 <= 58) || LA5_0 == 63 || (LA5_0 >= 65 && LA5_0 <= 90) || LA5_0 == 95 || (LA5_0 >= 97 && LA5_0 <= 122)) {
                        alt5 = 1;
                    }
                    switch (alt5) {
                        case 1: {
                            if ((this.input.LA(1) >= 32 && this.input.LA(1) <= 33) || this.input.LA(1) == 39 || this.input.LA(1) == 46 || (this.input.LA(1) >= 48 && this.input.LA(1) <= 58) || this.input.LA(1) == 63 || (this.input.LA(1) >= 65 && this.input.LA(1) <= 90) || this.input.LA(1) == 95 || (this.input.LA(1) >= 97 && this.input.LA(1) <= 122)) {
                                this.input.consume();
                                ++cnt5;
                                continue;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            this.recover((RecognitionException)mse);
                            throw mse;
                        }
                        default: {
                            if (cnt5 >= 1) {
                                this.mHYP();
                                this.state.type = _type;
                                this.state.channel = _channel;
                                break Label_0410;
                            }
                            final EarlyExitException eee = new EarlyExitException(5, (IntStream)this.input);
                            throw eee;
                        }
                    }
                }
            }
            finally {}
        }
    }
    
    public final void mTRUE() throws RecognitionException {
        try {
            final int _type = 297;
            final int _channel = 0;
            int alt6 = 4;
            switch (this.input.LA(1)) {
                case 84: {
                    alt6 = 1;
                    break;
                }
                case 86: {
                    alt6 = 2;
                    break;
                }
                case 116: {
                    alt6 = 3;
                    break;
                }
                case 118: {
                    alt6 = 4;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 6, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt6) {
                case 1: {
                    this.match("True");
                    break;
                }
                case 2: {
                    this.match("Vrai");
                    break;
                }
                case 3: {
                    this.match("true");
                    break;
                }
                case 4: {
                    this.match("vrai");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mFALSE() throws RecognitionException {
        try {
            final int _type = 28;
            final int _channel = 0;
            int alt7 = 4;
            final int LA7_0 = this.input.LA(1);
            if (LA7_0 == 70) {
                final int LA7_ = this.input.LA(2);
                if (LA7_ == 97) {
                    final int LA7_2 = this.input.LA(3);
                    if (LA7_2 == 108) {
                        alt7 = 1;
                    }
                    else if (LA7_2 == 117) {
                        alt7 = 2;
                    }
                    else {
                        final int nvaeMark = this.input.mark();
                        try {
                            for (int nvaeConsume = 0; nvaeConsume < 2; ++nvaeConsume) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae = new NoViableAltException("", 7, 3, (IntStream)this.input);
                            throw nvae;
                        }
                        finally {
                            this.input.rewind(nvaeMark);
                        }
                    }
                }
                else {
                    final int nvaeMark2 = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae2 = new NoViableAltException("", 7, 1, (IntStream)this.input);
                        throw nvae2;
                    }
                    finally {
                        this.input.rewind(nvaeMark2);
                    }
                }
            }
            else {
                if (LA7_0 != 102) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 7, 0, (IntStream)this.input);
                    throw nvae3;
                }
                final int LA7_3 = this.input.LA(2);
                if (LA7_3 == 97) {
                    final int LA7_4 = this.input.LA(3);
                    if (LA7_4 == 108) {
                        alt7 = 3;
                    }
                    else if (LA7_4 == 117) {
                        alt7 = 4;
                    }
                    else {
                        final int nvaeMark = this.input.mark();
                        try {
                            for (int nvaeConsume = 0; nvaeConsume < 2; ++nvaeConsume) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae = new NoViableAltException("", 7, 4, (IntStream)this.input);
                            throw nvae;
                        }
                        finally {
                            this.input.rewind(nvaeMark);
                        }
                    }
                }
                else {
                    final int nvaeMark2 = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae2 = new NoViableAltException("", 7, 2, (IntStream)this.input);
                        throw nvae2;
                    }
                    finally {
                        this.input.rewind(nvaeMark2);
                    }
                }
            }
            switch (alt7) {
                case 1: {
                    this.match("False");
                    break;
                }
                case 2: {
                    this.match("Faux");
                    break;
                }
                case 3: {
                    this.match("false");
                    break;
                }
                case 4: {
                    this.match("faux");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mMIN() throws RecognitionException {
        try {
            if (this.input.LA(1) < 97 || this.input.LA(1) > 122) {
                final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                this.recover((RecognitionException)mse);
                throw mse;
            }
            this.input.consume();
        }
        finally {}
    }
    
    public final void mMAJ() throws RecognitionException {
        try {
            if (this.input.LA(1) < 65 || this.input.LA(1) > 90) {
                final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                this.recover((RecognitionException)mse);
                throw mse;
            }
            this.input.consume();
        }
        finally {}
    }
    
    public final void mCHAR() throws RecognitionException {
        try {
            if (this.input.LA(1) != 33 && this.input.LA(1) != 39 && this.input.LA(1) != 46 && this.input.LA(1) != 58 && this.input.LA(1) != 63 && this.input.LA(1) != 95) {
                final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                this.recover((RecognitionException)mse);
                throw mse;
            }
            this.input.consume();
        }
        finally {}
    }
    
    public final void mVARNAME() throws RecognitionException {
        Label_0354: {
            try {
                final int _type = 299;
                final int _channel = 0;
                this.mMIN();
                while (true) {
                    int alt8 = 2;
                    final int LA8_0 = this.input.LA(1);
                    if (LA8_0 == 33 || LA8_0 == 39 || LA8_0 == 46 || (LA8_0 >= 48 && LA8_0 <= 58) || LA8_0 == 63 || (LA8_0 >= 65 && LA8_0 <= 90) || LA8_0 == 95 || (LA8_0 >= 97 && LA8_0 <= 122)) {
                        alt8 = 1;
                    }
                    switch (alt8) {
                        case 1: {
                            if (this.input.LA(1) == 33 || this.input.LA(1) == 39 || this.input.LA(1) == 46 || (this.input.LA(1) >= 48 && this.input.LA(1) <= 58) || this.input.LA(1) == 63 || (this.input.LA(1) >= 65 && this.input.LA(1) <= 90) || this.input.LA(1) == 95 || (this.input.LA(1) >= 97 && this.input.LA(1) <= 122)) {
                                this.input.consume();
                                continue;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            this.recover((RecognitionException)mse);
                            throw mse;
                        }
                        default: {
                            this.state.type = _type;
                            this.state.channel = _channel;
                            break Label_0354;
                        }
                    }
                }
            }
            finally {}
        }
    }
    
    public final void mHASEQTYPE() throws RecognitionException {
        try {
            final int _type = 130;
            final int _channel = 0;
            this.match("HasEquipmentType");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASEQID() throws RecognitionException {
        try {
            final int _type = 129;
            final int _channel = 0;
            this.match("HasEquipmentId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASSUMMONS() throws RecognitionException {
        try {
            final int _type = 139;
            final int _channel = 0;
            int alt9 = 4;
            final int LA9_0 = this.input.LA(1);
            if (LA9_0 == 67) {
                final int LA9_ = this.input.LA(2);
                if (LA9_ == 104) {
                    final int LA9_2 = this.input.LA(3);
                    if (LA9_2 == 97) {
                        final int LA9_3 = this.input.LA(4);
                        if (LA9_3 == 114) {
                            final int LA9_4 = this.input.LA(5);
                            if (LA9_4 == 97) {
                                final int LA9_5 = this.input.LA(6);
                                if (LA9_5 == 99) {
                                    final int LA9_6 = this.input.LA(7);
                                    if (LA9_6 == 116) {
                                        final int LA9_7 = this.input.LA(8);
                                        if (LA9_7 == 101) {
                                            final int LA9_8 = this.input.LA(9);
                                            if (LA9_8 == 114) {
                                                final int LA9_9 = this.input.LA(10);
                                                if (LA9_9 == 72) {
                                                    final int LA9_10 = this.input.LA(11);
                                                    if (LA9_10 == 97) {
                                                        final int LA9_11 = this.input.LA(12);
                                                        if (LA9_11 == 115) {
                                                            final int LA9_12 = this.input.LA(13);
                                                            if (LA9_12 == 83) {
                                                                final int LA9_13 = this.input.LA(14);
                                                                if (LA9_13 == 117) {
                                                                    final int LA9_14 = this.input.LA(15);
                                                                    if (LA9_14 == 109) {
                                                                        final int LA9_15 = this.input.LA(16);
                                                                        if (LA9_15 == 109) {
                                                                            final int LA9_16 = this.input.LA(17);
                                                                            if (LA9_16 == 111) {
                                                                                final int LA9_17 = this.input.LA(18);
                                                                                if (LA9_17 == 110) {
                                                                                    final int LA9_18 = this.input.LA(19);
                                                                                    if (LA9_18 == 115) {
                                                                                        alt9 = 1;
                                                                                    }
                                                                                    else {
                                                                                        alt9 = 3;
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    final int nvaeMark = this.input.mark();
                                                                                    try {
                                                                                        for (int nvaeConsume = 0; nvaeConsume < 17; ++nvaeConsume) {
                                                                                            this.input.consume();
                                                                                        }
                                                                                        final NoViableAltException nvae = new NoViableAltException("", 9, 28, (IntStream)this.input);
                                                                                        throw nvae;
                                                                                    }
                                                                                    finally {
                                                                                        this.input.rewind(nvaeMark);
                                                                                    }
                                                                                }
                                                                            }
                                                                            else {
                                                                                final int nvaeMark2 = this.input.mark();
                                                                                try {
                                                                                    for (int nvaeConsume2 = 0; nvaeConsume2 < 16; ++nvaeConsume2) {
                                                                                        this.input.consume();
                                                                                    }
                                                                                    final NoViableAltException nvae2 = new NoViableAltException("", 9, 27, (IntStream)this.input);
                                                                                    throw nvae2;
                                                                                }
                                                                                finally {
                                                                                    this.input.rewind(nvaeMark2);
                                                                                }
                                                                            }
                                                                        }
                                                                        else {
                                                                            final int nvaeMark3 = this.input.mark();
                                                                            try {
                                                                                for (int nvaeConsume3 = 0; nvaeConsume3 < 15; ++nvaeConsume3) {
                                                                                    this.input.consume();
                                                                                }
                                                                                final NoViableAltException nvae3 = new NoViableAltException("", 9, 26, (IntStream)this.input);
                                                                                throw nvae3;
                                                                            }
                                                                            finally {
                                                                                this.input.rewind(nvaeMark3);
                                                                            }
                                                                        }
                                                                    }
                                                                    else {
                                                                        final int nvaeMark4 = this.input.mark();
                                                                        try {
                                                                            for (int nvaeConsume4 = 0; nvaeConsume4 < 14; ++nvaeConsume4) {
                                                                                this.input.consume();
                                                                            }
                                                                            final NoViableAltException nvae4 = new NoViableAltException("", 9, 25, (IntStream)this.input);
                                                                            throw nvae4;
                                                                        }
                                                                        finally {
                                                                            this.input.rewind(nvaeMark4);
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    final int nvaeMark5 = this.input.mark();
                                                                    try {
                                                                        for (int nvaeConsume5 = 0; nvaeConsume5 < 13; ++nvaeConsume5) {
                                                                            this.input.consume();
                                                                        }
                                                                        final NoViableAltException nvae5 = new NoViableAltException("", 9, 24, (IntStream)this.input);
                                                                        throw nvae5;
                                                                    }
                                                                    finally {
                                                                        this.input.rewind(nvaeMark5);
                                                                    }
                                                                }
                                                            }
                                                            else {
                                                                final int nvaeMark6 = this.input.mark();
                                                                try {
                                                                    for (int nvaeConsume6 = 0; nvaeConsume6 < 12; ++nvaeConsume6) {
                                                                        this.input.consume();
                                                                    }
                                                                    final NoViableAltException nvae6 = new NoViableAltException("", 9, 23, (IntStream)this.input);
                                                                    throw nvae6;
                                                                }
                                                                finally {
                                                                    this.input.rewind(nvaeMark6);
                                                                }
                                                            }
                                                        }
                                                        else {
                                                            final int nvaeMark7 = this.input.mark();
                                                            try {
                                                                for (int nvaeConsume7 = 0; nvaeConsume7 < 11; ++nvaeConsume7) {
                                                                    this.input.consume();
                                                                }
                                                                final NoViableAltException nvae7 = new NoViableAltException("", 9, 22, (IntStream)this.input);
                                                                throw nvae7;
                                                            }
                                                            finally {
                                                                this.input.rewind(nvaeMark7);
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        final int nvaeMark8 = this.input.mark();
                                                        try {
                                                            for (int nvaeConsume8 = 0; nvaeConsume8 < 10; ++nvaeConsume8) {
                                                                this.input.consume();
                                                            }
                                                            final NoViableAltException nvae8 = new NoViableAltException("", 9, 19, (IntStream)this.input);
                                                            throw nvae8;
                                                        }
                                                        finally {
                                                            this.input.rewind(nvaeMark8);
                                                        }
                                                    }
                                                }
                                                else {
                                                    final int nvaeMark9 = this.input.mark();
                                                    try {
                                                        for (int nvaeConsume9 = 0; nvaeConsume9 < 9; ++nvaeConsume9) {
                                                            this.input.consume();
                                                        }
                                                        final NoViableAltException nvae9 = new NoViableAltException("", 9, 17, (IntStream)this.input);
                                                        throw nvae9;
                                                    }
                                                    finally {
                                                        this.input.rewind(nvaeMark9);
                                                    }
                                                }
                                            }
                                            else {
                                                final int nvaeMark10 = this.input.mark();
                                                try {
                                                    for (int nvaeConsume10 = 0; nvaeConsume10 < 8; ++nvaeConsume10) {
                                                        this.input.consume();
                                                    }
                                                    final NoViableAltException nvae10 = new NoViableAltException("", 9, 15, (IntStream)this.input);
                                                    throw nvae10;
                                                }
                                                finally {
                                                    this.input.rewind(nvaeMark10);
                                                }
                                            }
                                        }
                                        else {
                                            final int nvaeMark11 = this.input.mark();
                                            try {
                                                for (int nvaeConsume11 = 0; nvaeConsume11 < 7; ++nvaeConsume11) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae11 = new NoViableAltException("", 9, 13, (IntStream)this.input);
                                                throw nvae11;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark11);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark12 = this.input.mark();
                                        try {
                                            for (int nvaeConsume12 = 0; nvaeConsume12 < 6; ++nvaeConsume12) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae12 = new NoViableAltException("", 9, 11, (IntStream)this.input);
                                            throw nvae12;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark12);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark13 = this.input.mark();
                                    try {
                                        for (int nvaeConsume13 = 0; nvaeConsume13 < 5; ++nvaeConsume13) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae13 = new NoViableAltException("", 9, 9, (IntStream)this.input);
                                        throw nvae13;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark13);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark14 = this.input.mark();
                                try {
                                    for (int nvaeConsume14 = 0; nvaeConsume14 < 4; ++nvaeConsume14) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae14 = new NoViableAltException("", 9, 7, (IntStream)this.input);
                                    throw nvae14;
                                }
                                finally {
                                    this.input.rewind(nvaeMark14);
                                }
                            }
                        }
                        else {
                            final int nvaeMark15 = this.input.mark();
                            try {
                                for (int nvaeConsume15 = 0; nvaeConsume15 < 3; ++nvaeConsume15) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae15 = new NoViableAltException("", 9, 5, (IntStream)this.input);
                                throw nvae15;
                            }
                            finally {
                                this.input.rewind(nvaeMark15);
                            }
                        }
                    }
                    else {
                        final int nvaeMark16 = this.input.mark();
                        try {
                            for (int nvaeConsume16 = 0; nvaeConsume16 < 2; ++nvaeConsume16) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae16 = new NoViableAltException("", 9, 3, (IntStream)this.input);
                            throw nvae16;
                        }
                        finally {
                            this.input.rewind(nvaeMark16);
                        }
                    }
                }
                else {
                    final int nvaeMark17 = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae17 = new NoViableAltException("", 9, 1, (IntStream)this.input);
                        throw nvae17;
                    }
                    finally {
                        this.input.rewind(nvaeMark17);
                    }
                }
            }
            else {
                if (LA9_0 != 72) {
                    final NoViableAltException nvae18 = new NoViableAltException("", 9, 0, (IntStream)this.input);
                    throw nvae18;
                }
                final int LA9_19 = this.input.LA(2);
                if (LA9_19 == 97) {
                    final int LA9_20 = this.input.LA(3);
                    if (LA9_20 == 115) {
                        final int LA9_21 = this.input.LA(4);
                        if (LA9_21 == 83) {
                            final int LA9_22 = this.input.LA(5);
                            if (LA9_22 == 117) {
                                final int LA9_23 = this.input.LA(6);
                                if (LA9_23 == 109) {
                                    final int LA9_24 = this.input.LA(7);
                                    if (LA9_24 == 109) {
                                        final int LA9_25 = this.input.LA(8);
                                        if (LA9_25 == 111) {
                                            final int LA9_26 = this.input.LA(9);
                                            if (LA9_26 == 110) {
                                                final int LA9_27 = this.input.LA(10);
                                                if (LA9_27 == 115) {
                                                    alt9 = 2;
                                                }
                                                else {
                                                    alt9 = 4;
                                                }
                                            }
                                            else {
                                                final int nvaeMark10 = this.input.mark();
                                                try {
                                                    for (int nvaeConsume10 = 0; nvaeConsume10 < 8; ++nvaeConsume10) {
                                                        this.input.consume();
                                                    }
                                                    final NoViableAltException nvae10 = new NoViableAltException("", 9, 16, (IntStream)this.input);
                                                    throw nvae10;
                                                }
                                                finally {
                                                    this.input.rewind(nvaeMark10);
                                                }
                                            }
                                        }
                                        else {
                                            final int nvaeMark11 = this.input.mark();
                                            try {
                                                for (int nvaeConsume11 = 0; nvaeConsume11 < 7; ++nvaeConsume11) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae11 = new NoViableAltException("", 9, 14, (IntStream)this.input);
                                                throw nvae11;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark11);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark12 = this.input.mark();
                                        try {
                                            for (int nvaeConsume12 = 0; nvaeConsume12 < 6; ++nvaeConsume12) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae12 = new NoViableAltException("", 9, 12, (IntStream)this.input);
                                            throw nvae12;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark12);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark13 = this.input.mark();
                                    try {
                                        for (int nvaeConsume13 = 0; nvaeConsume13 < 5; ++nvaeConsume13) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae13 = new NoViableAltException("", 9, 10, (IntStream)this.input);
                                        throw nvae13;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark13);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark14 = this.input.mark();
                                try {
                                    for (int nvaeConsume14 = 0; nvaeConsume14 < 4; ++nvaeConsume14) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae14 = new NoViableAltException("", 9, 8, (IntStream)this.input);
                                    throw nvae14;
                                }
                                finally {
                                    this.input.rewind(nvaeMark14);
                                }
                            }
                        }
                        else {
                            final int nvaeMark15 = this.input.mark();
                            try {
                                for (int nvaeConsume15 = 0; nvaeConsume15 < 3; ++nvaeConsume15) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae15 = new NoViableAltException("", 9, 6, (IntStream)this.input);
                                throw nvae15;
                            }
                            finally {
                                this.input.rewind(nvaeMark15);
                            }
                        }
                    }
                    else {
                        final int nvaeMark16 = this.input.mark();
                        try {
                            for (int nvaeConsume16 = 0; nvaeConsume16 < 2; ++nvaeConsume16) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae16 = new NoViableAltException("", 9, 4, (IntStream)this.input);
                            throw nvae16;
                        }
                        finally {
                            this.input.rewind(nvaeMark16);
                        }
                    }
                }
                else {
                    final int nvaeMark17 = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae17 = new NoViableAltException("", 9, 2, (IntStream)this.input);
                        throw nvae17;
                    }
                    finally {
                        this.input.rewind(nvaeMark17);
                    }
                }
            }
            switch (alt9) {
                case 1: {
                    this.match("CharacterHasSummons");
                    break;
                }
                case 2: {
                    this.match("HasSummons");
                    break;
                }
                case 3: {
                    this.match("CharacterHasSummon");
                    break;
                }
                case 4: {
                    this.match("HasSummon");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISENNEMY() throws RecognitionException {
        try {
            final int _type = 191;
            final int _channel = 0;
            int alt10 = 2;
            final int LA10_0 = this.input.LA(1);
            if (LA10_0 != 73) {
                final NoViableAltException nvae = new NoViableAltException("", 10, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA10_ = this.input.LA(2);
            if (LA10_ == 115) {
                final int LA10_2 = this.input.LA(3);
                if (LA10_2 == 69) {
                    final int LA10_3 = this.input.LA(4);
                    if (LA10_3 == 110) {
                        final int LA10_4 = this.input.LA(5);
                        if (LA10_4 == 110) {
                            alt10 = 1;
                        }
                        else if (LA10_4 == 101) {
                            alt10 = 2;
                        }
                        else {
                            final int nvaeMark = this.input.mark();
                            try {
                                for (int nvaeConsume = 0; nvaeConsume < 4; ++nvaeConsume) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae2 = new NoViableAltException("", 10, 4, (IntStream)this.input);
                                throw nvae2;
                            }
                            finally {
                                this.input.rewind(nvaeMark);
                            }
                        }
                    }
                    else {
                        final int nvaeMark2 = this.input.mark();
                        try {
                            for (int nvaeConsume2 = 0; nvaeConsume2 < 3; ++nvaeConsume2) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae3 = new NoViableAltException("", 10, 3, (IntStream)this.input);
                            throw nvae3;
                        }
                        finally {
                            this.input.rewind(nvaeMark2);
                        }
                    }
                }
                else {
                    final int nvaeMark3 = this.input.mark();
                    try {
                        for (int nvaeConsume3 = 0; nvaeConsume3 < 2; ++nvaeConsume3) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae4 = new NoViableAltException("", 10, 2, (IntStream)this.input);
                        throw nvae4;
                    }
                    finally {
                        this.input.rewind(nvaeMark3);
                    }
                }
            }
            else {
                final int nvaeMark4 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae5 = new NoViableAltException("", 10, 1, (IntStream)this.input);
                    throw nvae5;
                }
                finally {
                    this.input.rewind(nvaeMark4);
                }
            }
            switch (alt10) {
                case 1: {
                    this.match("IsEnnemy");
                    break;
                }
                case 2: {
                    this.match("IsEnemy");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mCANCARRYTARGET() throws RecognitionException {
        try {
            final int _type = 17;
            final int _channel = 0;
            this.match("CanCarryTarget");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCHA() throws RecognitionException {
        try {
            final int _type = 33;
            final int _channel = 0;
            int alt11 = 2;
            final int LA11_0 = this.input.LA(1);
            if (LA11_0 != 71) {
                final NoViableAltException nvae = new NoViableAltException("", 11, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA11_ = this.input.LA(2);
            if (LA11_ == 101) {
                final int LA11_2 = this.input.LA(3);
                if (LA11_2 == 116) {
                    final int LA11_3 = this.input.LA(4);
                    if (LA11_3 == 67) {
                        final int LA11_4 = this.input.LA(5);
                        if (LA11_4 == 104) {
                            final int LA11_5 = this.input.LA(6);
                            if (LA11_5 == 97) {
                                final int LA11_6 = this.input.LA(7);
                                if (LA11_6 == 114) {
                                    final int LA11_7 = this.input.LA(8);
                                    if (LA11_7 == 97) {
                                        final int LA11_8 = this.input.LA(9);
                                        if (LA11_8 == 99) {
                                            final int LA11_9 = this.input.LA(10);
                                            if (LA11_9 == 116) {
                                                alt11 = 2;
                                            }
                                            else {
                                                alt11 = 1;
                                            }
                                        }
                                        else {
                                            final int nvaeMark = this.input.mark();
                                            try {
                                                for (int nvaeConsume = 0; nvaeConsume < 8; ++nvaeConsume) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae2 = new NoViableAltException("", 11, 8, (IntStream)this.input);
                                                throw nvae2;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark2 = this.input.mark();
                                        try {
                                            for (int nvaeConsume2 = 0; nvaeConsume2 < 7; ++nvaeConsume2) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae3 = new NoViableAltException("", 11, 7, (IntStream)this.input);
                                            throw nvae3;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark2);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark3 = this.input.mark();
                                    try {
                                        for (int nvaeConsume3 = 0; nvaeConsume3 < 6; ++nvaeConsume3) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae4 = new NoViableAltException("", 11, 6, (IntStream)this.input);
                                        throw nvae4;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark3);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark4 = this.input.mark();
                                try {
                                    for (int nvaeConsume4 = 0; nvaeConsume4 < 5; ++nvaeConsume4) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae5 = new NoViableAltException("", 11, 5, (IntStream)this.input);
                                    throw nvae5;
                                }
                                finally {
                                    this.input.rewind(nvaeMark4);
                                }
                            }
                        }
                        else {
                            final int nvaeMark5 = this.input.mark();
                            try {
                                for (int nvaeConsume5 = 0; nvaeConsume5 < 4; ++nvaeConsume5) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae6 = new NoViableAltException("", 11, 4, (IntStream)this.input);
                                throw nvae6;
                            }
                            finally {
                                this.input.rewind(nvaeMark5);
                            }
                        }
                    }
                    else {
                        final int nvaeMark6 = this.input.mark();
                        try {
                            for (int nvaeConsume6 = 0; nvaeConsume6 < 3; ++nvaeConsume6) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae7 = new NoViableAltException("", 11, 3, (IntStream)this.input);
                            throw nvae7;
                        }
                        finally {
                            this.input.rewind(nvaeMark6);
                        }
                    }
                }
                else {
                    final int nvaeMark7 = this.input.mark();
                    try {
                        for (int nvaeConsume7 = 0; nvaeConsume7 < 2; ++nvaeConsume7) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae8 = new NoViableAltException("", 11, 2, (IntStream)this.input);
                        throw nvae8;
                    }
                    finally {
                        this.input.rewind(nvaeMark7);
                    }
                }
            }
            else {
                final int nvaeMark8 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae9 = new NoViableAltException("", 11, 1, (IntStream)this.input);
                    throw nvae9;
                }
                finally {
                    this.input.rewind(nvaeMark8);
                }
            }
            switch (alt11) {
                case 1: {
                    this.match("GetCharac");
                    break;
                }
                case 2: {
                    this.match("GetCharacteristic");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCHAPCT() throws RecognitionException {
        try {
            final int _type = 35;
            final int _channel = 0;
            int alt12 = 3;
            final int LA12_0 = this.input.LA(1);
            if (LA12_0 != 71) {
                final NoViableAltException nvae = new NoViableAltException("", 12, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA12_ = this.input.LA(2);
            if (LA12_ == 101) {
                final int LA12_2 = this.input.LA(3);
                if (LA12_2 == 116) {
                    final int LA12_3 = this.input.LA(4);
                    if (LA12_3 == 67) {
                        final int LA12_4 = this.input.LA(5);
                        if (LA12_4 == 104) {
                            final int LA12_5 = this.input.LA(6);
                            if (LA12_5 == 97) {
                                final int LA12_6 = this.input.LA(7);
                                if (LA12_6 == 114) {
                                    final int LA12_7 = this.input.LA(8);
                                    if (LA12_7 == 97) {
                                        final int LA12_8 = this.input.LA(9);
                                        if (LA12_8 == 99) {
                                            switch (this.input.LA(10)) {
                                                case 73: {
                                                    alt12 = 1;
                                                    break;
                                                }
                                                case 80: {
                                                    alt12 = 2;
                                                    break;
                                                }
                                                case 116: {
                                                    alt12 = 3;
                                                    break;
                                                }
                                                default: {
                                                    final int nvaeMark = this.input.mark();
                                                    try {
                                                        for (int nvaeConsume = 0; nvaeConsume < 9; ++nvaeConsume) {
                                                            this.input.consume();
                                                        }
                                                        final NoViableAltException nvae2 = new NoViableAltException("", 12, 9, (IntStream)this.input);
                                                        throw nvae2;
                                                    }
                                                    finally {
                                                        this.input.rewind(nvaeMark);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        else {
                                            final int nvaeMark = this.input.mark();
                                            try {
                                                for (int nvaeConsume = 0; nvaeConsume < 8; ++nvaeConsume) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae2 = new NoViableAltException("", 12, 8, (IntStream)this.input);
                                                throw nvae2;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark2 = this.input.mark();
                                        try {
                                            for (int nvaeConsume2 = 0; nvaeConsume2 < 7; ++nvaeConsume2) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae3 = new NoViableAltException("", 12, 7, (IntStream)this.input);
                                            throw nvae3;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark2);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark3 = this.input.mark();
                                    try {
                                        for (int nvaeConsume3 = 0; nvaeConsume3 < 6; ++nvaeConsume3) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae4 = new NoViableAltException("", 12, 6, (IntStream)this.input);
                                        throw nvae4;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark3);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark4 = this.input.mark();
                                try {
                                    for (int nvaeConsume4 = 0; nvaeConsume4 < 5; ++nvaeConsume4) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae5 = new NoViableAltException("", 12, 5, (IntStream)this.input);
                                    throw nvae5;
                                }
                                finally {
                                    this.input.rewind(nvaeMark4);
                                }
                            }
                        }
                        else {
                            final int nvaeMark5 = this.input.mark();
                            try {
                                for (int nvaeConsume5 = 0; nvaeConsume5 < 4; ++nvaeConsume5) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae6 = new NoViableAltException("", 12, 4, (IntStream)this.input);
                                throw nvae6;
                            }
                            finally {
                                this.input.rewind(nvaeMark5);
                            }
                        }
                    }
                    else {
                        final int nvaeMark6 = this.input.mark();
                        try {
                            for (int nvaeConsume6 = 0; nvaeConsume6 < 3; ++nvaeConsume6) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae7 = new NoViableAltException("", 12, 3, (IntStream)this.input);
                            throw nvae7;
                        }
                        finally {
                            this.input.rewind(nvaeMark6);
                        }
                    }
                }
                else {
                    final int nvaeMark7 = this.input.mark();
                    try {
                        for (int nvaeConsume7 = 0; nvaeConsume7 < 2; ++nvaeConsume7) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae8 = new NoViableAltException("", 12, 2, (IntStream)this.input);
                        throw nvae8;
                    }
                    finally {
                        this.input.rewind(nvaeMark7);
                    }
                }
            }
            else {
                final int nvaeMark8 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae9 = new NoViableAltException("", 12, 1, (IntStream)this.input);
                    throw nvae9;
                }
                finally {
                    this.input.rewind(nvaeMark8);
                }
            }
            switch (alt12) {
                case 1: {
                    this.match("GetCharacInPct");
                    break;
                }
                case 2: {
                    this.match("GetCharacPct");
                    break;
                }
                case 3: {
                    this.match("GetCharacteristicPct");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCHAMAX() throws RecognitionException {
        try {
            final int _type = 34;
            final int _channel = 0;
            int alt13 = 2;
            final int LA13_0 = this.input.LA(1);
            if (LA13_0 != 71) {
                final NoViableAltException nvae = new NoViableAltException("", 13, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA13_ = this.input.LA(2);
            if (LA13_ == 101) {
                final int LA13_2 = this.input.LA(3);
                if (LA13_2 == 116) {
                    final int LA13_3 = this.input.LA(4);
                    if (LA13_3 == 67) {
                        final int LA13_4 = this.input.LA(5);
                        if (LA13_4 == 104) {
                            final int LA13_5 = this.input.LA(6);
                            if (LA13_5 == 97) {
                                final int LA13_6 = this.input.LA(7);
                                if (LA13_6 == 114) {
                                    final int LA13_7 = this.input.LA(8);
                                    if (LA13_7 == 97) {
                                        final int LA13_8 = this.input.LA(9);
                                        if (LA13_8 == 99) {
                                            final int LA13_9 = this.input.LA(10);
                                            if (LA13_9 == 77) {
                                                alt13 = 1;
                                            }
                                            else if (LA13_9 == 116) {
                                                alt13 = 2;
                                            }
                                            else {
                                                final int nvaeMark = this.input.mark();
                                                try {
                                                    for (int nvaeConsume = 0; nvaeConsume < 9; ++nvaeConsume) {
                                                        this.input.consume();
                                                    }
                                                    final NoViableAltException nvae2 = new NoViableAltException("", 13, 9, (IntStream)this.input);
                                                    throw nvae2;
                                                }
                                                finally {
                                                    this.input.rewind(nvaeMark);
                                                }
                                            }
                                        }
                                        else {
                                            final int nvaeMark2 = this.input.mark();
                                            try {
                                                for (int nvaeConsume2 = 0; nvaeConsume2 < 8; ++nvaeConsume2) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae3 = new NoViableAltException("", 13, 8, (IntStream)this.input);
                                                throw nvae3;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark2);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark3 = this.input.mark();
                                        try {
                                            for (int nvaeConsume3 = 0; nvaeConsume3 < 7; ++nvaeConsume3) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae4 = new NoViableAltException("", 13, 7, (IntStream)this.input);
                                            throw nvae4;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark3);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark4 = this.input.mark();
                                    try {
                                        for (int nvaeConsume4 = 0; nvaeConsume4 < 6; ++nvaeConsume4) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae5 = new NoViableAltException("", 13, 6, (IntStream)this.input);
                                        throw nvae5;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark4);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark5 = this.input.mark();
                                try {
                                    for (int nvaeConsume5 = 0; nvaeConsume5 < 5; ++nvaeConsume5) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae6 = new NoViableAltException("", 13, 5, (IntStream)this.input);
                                    throw nvae6;
                                }
                                finally {
                                    this.input.rewind(nvaeMark5);
                                }
                            }
                        }
                        else {
                            final int nvaeMark6 = this.input.mark();
                            try {
                                for (int nvaeConsume6 = 0; nvaeConsume6 < 4; ++nvaeConsume6) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae7 = new NoViableAltException("", 13, 4, (IntStream)this.input);
                                throw nvae7;
                            }
                            finally {
                                this.input.rewind(nvaeMark6);
                            }
                        }
                    }
                    else {
                        final int nvaeMark7 = this.input.mark();
                        try {
                            for (int nvaeConsume7 = 0; nvaeConsume7 < 3; ++nvaeConsume7) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae8 = new NoViableAltException("", 13, 3, (IntStream)this.input);
                            throw nvae8;
                        }
                        finally {
                            this.input.rewind(nvaeMark7);
                        }
                    }
                }
                else {
                    final int nvaeMark8 = this.input.mark();
                    try {
                        for (int nvaeConsume8 = 0; nvaeConsume8 < 2; ++nvaeConsume8) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae9 = new NoViableAltException("", 13, 2, (IntStream)this.input);
                        throw nvae9;
                    }
                    finally {
                        this.input.rewind(nvaeMark8);
                    }
                }
            }
            else {
                final int nvaeMark9 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae10 = new NoViableAltException("", 13, 1, (IntStream)this.input);
                    throw nvae10;
                }
                finally {
                    this.input.rewind(nvaeMark9);
                }
            }
            switch (alt13) {
                case 1: {
                    this.match("GetCharacMax");
                    break;
                }
                case 2: {
                    this.match("GetCharacteristicMax");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETMONST() throws RecognitionException {
        try {
            final int _type = 57;
            final int _channel = 0;
            int alt14 = 3;
            final int LA14_0 = this.input.LA(1);
            if (LA14_0 == 71) {
                alt14 = 1;
            }
            else {
                if (LA14_0 != 77) {
                    final NoViableAltException nvae = new NoViableAltException("", 14, 0, (IntStream)this.input);
                    throw nvae;
                }
                final int LA14_ = this.input.LA(2);
                if (LA14_ == 111) {
                    final int LA14_2 = this.input.LA(3);
                    if (LA14_2 == 110) {
                        final int LA14_3 = this.input.LA(4);
                        if (LA14_3 == 115) {
                            final int LA14_4 = this.input.LA(5);
                            if (LA14_4 == 116) {
                                final int LA14_5 = this.input.LA(6);
                                if (LA14_5 == 101) {
                                    final int LA14_6 = this.input.LA(7);
                                    if (LA14_6 == 114) {
                                        final int LA14_7 = this.input.LA(8);
                                        if (LA14_7 == 67) {
                                            alt14 = 2;
                                        }
                                        else if (LA14_7 == 78) {
                                            alt14 = 3;
                                        }
                                        else {
                                            final int nvaeMark = this.input.mark();
                                            try {
                                                for (int nvaeConsume = 0; nvaeConsume < 7; ++nvaeConsume) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae2 = new NoViableAltException("", 14, 8, (IntStream)this.input);
                                                throw nvae2;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark2 = this.input.mark();
                                        try {
                                            for (int nvaeConsume2 = 0; nvaeConsume2 < 6; ++nvaeConsume2) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae3 = new NoViableAltException("", 14, 7, (IntStream)this.input);
                                            throw nvae3;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark2);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark3 = this.input.mark();
                                    try {
                                        for (int nvaeConsume3 = 0; nvaeConsume3 < 5; ++nvaeConsume3) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae4 = new NoViableAltException("", 14, 6, (IntStream)this.input);
                                        throw nvae4;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark3);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark4 = this.input.mark();
                                try {
                                    for (int nvaeConsume4 = 0; nvaeConsume4 < 4; ++nvaeConsume4) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae5 = new NoViableAltException("", 14, 5, (IntStream)this.input);
                                    throw nvae5;
                                }
                                finally {
                                    this.input.rewind(nvaeMark4);
                                }
                            }
                        }
                        else {
                            final int nvaeMark5 = this.input.mark();
                            try {
                                for (int nvaeConsume5 = 0; nvaeConsume5 < 3; ++nvaeConsume5) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae6 = new NoViableAltException("", 14, 4, (IntStream)this.input);
                                throw nvae6;
                            }
                            finally {
                                this.input.rewind(nvaeMark5);
                            }
                        }
                    }
                    else {
                        final int nvaeMark6 = this.input.mark();
                        try {
                            for (int nvaeConsume6 = 0; nvaeConsume6 < 2; ++nvaeConsume6) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae7 = new NoViableAltException("", 14, 3, (IntStream)this.input);
                            throw nvae7;
                        }
                        finally {
                            this.input.rewind(nvaeMark6);
                        }
                    }
                }
                else {
                    final int nvaeMark7 = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae8 = new NoViableAltException("", 14, 2, (IntStream)this.input);
                        throw nvae8;
                    }
                    finally {
                        this.input.rewind(nvaeMark7);
                    }
                }
            }
            switch (alt14) {
                case 1: {
                    this.match("GetMonstersInFight");
                    break;
                }
                case 2: {
                    this.match("MonsterCountInFight");
                    break;
                }
                case 3: {
                    this.match("MonsterNbInFight");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSPACEINSYMBIOT() throws RecognitionException {
        try {
            final int _type = 289;
            final int _channel = 0;
            int alt15 = 2;
            final int LA15_0 = this.input.LA(1);
            if (LA15_0 == 71) {
                alt15 = 1;
            }
            else {
                if (LA15_0 != 82) {
                    final NoViableAltException nvae = new NoViableAltException("", 15, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt15 = 2;
            }
            switch (alt15) {
                case 1: {
                    this.match("GetSpaceInSymbiot");
                    break;
                }
                case 2: {
                    this.match("RemainingSymbiotSpace");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mTRAPAMOUNT() throws RecognitionException {
        try {
            final int _type = 296;
            final int _channel = 0;
            int alt16 = 2;
            final int LA16_0 = this.input.LA(1);
            if (LA16_0 == 84) {
                alt16 = 1;
            }
            else {
                if (LA16_0 != 78) {
                    final NoViableAltException nvae = new NoViableAltException("", 16, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt16 = 2;
            }
            switch (alt16) {
                case 1: {
                    this.match("TrapAmount");
                    break;
                }
                case 2: {
                    this.match("NbTraps");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mWALLAMOUNT() throws RecognitionException {
        try {
            final int _type = 301;
            final int _channel = 0;
            int alt17 = 3;
            switch (this.input.LA(1)) {
                case 87: {
                    alt17 = 1;
                    break;
                }
                case 78: {
                    alt17 = 2;
                    break;
                }
                case 71: {
                    alt17 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 17, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt17) {
                case 1: {
                    this.match("WallAmount");
                    break;
                }
                case 2: {
                    this.match("NbWalls");
                    break;
                }
                case 3: {
                    this.match("GetWallAmount");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSUMMONAMOUNT() throws RecognitionException {
        try {
            final int _type = 291;
            final int _channel = 0;
            int alt18 = 2;
            final int LA18_0 = this.input.LA(1);
            if (LA18_0 == 83) {
                alt18 = 1;
            }
            else {
                if (LA18_0 != 78) {
                    final NoViableAltException nvae = new NoViableAltException("", 18, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt18 = 2;
            }
            switch (alt18) {
                case 1: {
                    this.match("SummonAmount");
                    break;
                }
                case 2: {
                    this.match("NbSummons");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mBEACONAMOUNT() throws RecognitionException {
        try {
            final int _type = 14;
            final int _channel = 0;
            int alt19 = 2;
            final int LA19_0 = this.input.LA(1);
            if (LA19_0 == 66) {
                alt19 = 1;
            }
            else {
                if (LA19_0 != 78) {
                    final NoViableAltException nvae = new NoViableAltException("", 19, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt19 = 2;
            }
            switch (alt19) {
                case 1: {
                    this.match("BeaconAmount");
                    break;
                }
                case 2: {
                    this.match("NbBeacons");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mBARRELAMOUNT() throws RecognitionException {
        try {
            final int _type = 12;
            final int _channel = 0;
            this.match("NbBarrels");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_XELOR_DIALS_COUNT() throws RecognitionException {
        try {
            final int _type = 123;
            final int _channel = 0;
            this.match("NbXelorDials");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_SELECTED_CREATURE_AVAILABLE() throws RecognitionException {
        try {
            final int _type = 252;
            final int _channel = 0;
            this.match("IsSelectedCreatureAvailable");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mEFFECTISFROMHEAL() throws RecognitionException {
        try {
            final int _type = 24;
            final int _channel = 0;
            this.match("EffectGeneratedByHeal");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mOWNSBEACON() throws RecognitionException {
        try {
            final int _type = 280;
            final int _channel = 0;
            this.match("OwnsBeacon");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISSPECIFICAREA() throws RecognitionException {
        try {
            final int _type = 215;
            final int _channel = 0;
            int alt20 = 2;
            final int LA20_0 = this.input.LA(1);
            if (LA20_0 != 73) {
                final NoViableAltException nvae = new NoViableAltException("", 20, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA20_ = this.input.LA(2);
            if (LA20_ == 115) {
                final int LA20_2 = this.input.LA(3);
                if (LA20_2 == 83) {
                    final int LA20_3 = this.input.LA(4);
                    if (LA20_3 == 112) {
                        final int LA20_4 = this.input.LA(5);
                        if (LA20_4 == 101) {
                            final int LA20_5 = this.input.LA(6);
                            if (LA20_5 == 99) {
                                final int LA20_6 = this.input.LA(7);
                                if (LA20_6 == 105) {
                                    final int LA20_7 = this.input.LA(8);
                                    if (LA20_7 == 102) {
                                        final int LA20_8 = this.input.LA(9);
                                        if (LA20_8 == 105) {
                                            final int LA20_9 = this.input.LA(10);
                                            if (LA20_9 == 99) {
                                                final int LA20_10 = this.input.LA(11);
                                                if (LA20_10 == 65) {
                                                    alt20 = 1;
                                                }
                                                else {
                                                    alt20 = 2;
                                                }
                                            }
                                            else {
                                                final int nvaeMark = this.input.mark();
                                                try {
                                                    for (int nvaeConsume = 0; nvaeConsume < 9; ++nvaeConsume) {
                                                        this.input.consume();
                                                    }
                                                    final NoViableAltException nvae2 = new NoViableAltException("", 20, 9, (IntStream)this.input);
                                                    throw nvae2;
                                                }
                                                finally {
                                                    this.input.rewind(nvaeMark);
                                                }
                                            }
                                        }
                                        else {
                                            final int nvaeMark2 = this.input.mark();
                                            try {
                                                for (int nvaeConsume2 = 0; nvaeConsume2 < 8; ++nvaeConsume2) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae3 = new NoViableAltException("", 20, 8, (IntStream)this.input);
                                                throw nvae3;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark2);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark3 = this.input.mark();
                                        try {
                                            for (int nvaeConsume3 = 0; nvaeConsume3 < 7; ++nvaeConsume3) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae4 = new NoViableAltException("", 20, 7, (IntStream)this.input);
                                            throw nvae4;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark3);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark4 = this.input.mark();
                                    try {
                                        for (int nvaeConsume4 = 0; nvaeConsume4 < 6; ++nvaeConsume4) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae5 = new NoViableAltException("", 20, 6, (IntStream)this.input);
                                        throw nvae5;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark4);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark5 = this.input.mark();
                                try {
                                    for (int nvaeConsume5 = 0; nvaeConsume5 < 5; ++nvaeConsume5) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae6 = new NoViableAltException("", 20, 5, (IntStream)this.input);
                                    throw nvae6;
                                }
                                finally {
                                    this.input.rewind(nvaeMark5);
                                }
                            }
                        }
                        else {
                            final int nvaeMark6 = this.input.mark();
                            try {
                                for (int nvaeConsume6 = 0; nvaeConsume6 < 4; ++nvaeConsume6) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae7 = new NoViableAltException("", 20, 4, (IntStream)this.input);
                                throw nvae7;
                            }
                            finally {
                                this.input.rewind(nvaeMark6);
                            }
                        }
                    }
                    else {
                        final int nvaeMark7 = this.input.mark();
                        try {
                            for (int nvaeConsume7 = 0; nvaeConsume7 < 3; ++nvaeConsume7) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae8 = new NoViableAltException("", 20, 3, (IntStream)this.input);
                            throw nvae8;
                        }
                        finally {
                            this.input.rewind(nvaeMark7);
                        }
                    }
                }
                else {
                    final int nvaeMark8 = this.input.mark();
                    try {
                        for (int nvaeConsume8 = 0; nvaeConsume8 < 2; ++nvaeConsume8) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae9 = new NoViableAltException("", 20, 2, (IntStream)this.input);
                        throw nvae9;
                    }
                    finally {
                        this.input.rewind(nvaeMark8);
                    }
                }
            }
            else {
                final int nvaeMark9 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae10 = new NoViableAltException("", 20, 1, (IntStream)this.input);
                    throw nvae10;
                }
                finally {
                    this.input.rewind(nvaeMark9);
                }
            }
            switch (alt20) {
                case 1: {
                    this.match("IsSpecificArea");
                    break;
                }
                case 2: {
                    this.match("IsSpecific");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISSPECIFICAREAWITHSPECIFICSTATE() throws RecognitionException {
        try {
            final int _type = 216;
            final int _channel = 0;
            this.match("IsSpecificAreaWithSpecificState");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETTIME() throws RecognitionException {
        try {
            final int _type = 75;
            final int _channel = 0;
            int alt21 = 2;
            final int LA21_0 = this.input.LA(1);
            if (LA21_0 != 71) {
                final NoViableAltException nvae = new NoViableAltException("", 21, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA21_ = this.input.LA(2);
            if (LA21_ == 101) {
                final int LA21_2 = this.input.LA(3);
                if (LA21_2 == 116) {
                    final int LA21_3 = this.input.LA(4);
                    if (LA21_3 == 72) {
                        alt21 = 1;
                    }
                    else if (LA21_3 == 84) {
                        alt21 = 2;
                    }
                    else {
                        final int nvaeMark = this.input.mark();
                        try {
                            for (int nvaeConsume = 0; nvaeConsume < 3; ++nvaeConsume) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae2 = new NoViableAltException("", 21, 3, (IntStream)this.input);
                            throw nvae2;
                        }
                        finally {
                            this.input.rewind(nvaeMark);
                        }
                    }
                }
                else {
                    final int nvaeMark2 = this.input.mark();
                    try {
                        for (int nvaeConsume2 = 0; nvaeConsume2 < 2; ++nvaeConsume2) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae3 = new NoViableAltException("", 21, 2, (IntStream)this.input);
                        throw nvae3;
                    }
                    finally {
                        this.input.rewind(nvaeMark2);
                    }
                }
            }
            else {
                final int nvaeMark3 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae4 = new NoViableAltException("", 21, 1, (IntStream)this.input);
                    throw nvae4;
                }
                finally {
                    this.input.rewind(nvaeMark3);
                }
            }
            switch (alt21) {
                case 1: {
                    this.match("GetHour");
                    break;
                }
                case 2: {
                    this.match("GetTime");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISDAY() throws RecognitionException {
        try {
            final int _type = 188;
            final int _channel = 0;
            this.match("IsDay");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISSEASON() throws RecognitionException {
        try {
            final int _type = 213;
            final int _channel = 0;
            this.match("IsSeason");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISBREEDID() throws RecognitionException {
        try {
            final int _type = 184;
            final int _channel = 0;
            this.match("IsBreedId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISBREED() throws RecognitionException {
        try {
            final int _type = 182;
            final int _channel = 0;
            this.match("IsBreed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISUNDEAD() throws RecognitionException {
        try {
            final int _type = 219;
            final int _channel = 0;
            this.match("IsUndead");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASSTATE() throws RecognitionException {
        try {
            final int _type = 138;
            final int _channel = 0;
            this.match("HasState");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETSKILLLEVEL() throws RecognitionException {
        try {
            final int _type = 67;
            final int _channel = 0;
            this.match("GetSkillLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETSPELLLEVEL() throws RecognitionException {
        try {
            final int _type = 68;
            final int _channel = 0;
            this.match("GetSpellLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETSPELLTREELEVEL() throws RecognitionException {
        try {
            final int _type = 69;
            final int _channel = 0;
            this.match("GetSpellTreeLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETINSTANCEID() throws RecognitionException {
        try {
            final int _type = 52;
            final int _channel = 0;
            this.match("GetInstanceId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETEFFECTCASTER() throws RecognitionException {
        try {
            final int _type = 47;
            final int _channel = 0;
            this.match("GetEffectCaster");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETEFFECTTARGET() throws RecognitionException {
        try {
            final int _type = 48;
            final int _channel = 0;
            this.match("GetEffectTarget");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETTRIGGEREREFFECTCASTER() throws RecognitionException {
        try {
            final int _type = 77;
            final int _channel = 0;
            this.match("GetTriggererEffectCaster");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETTEAMID() throws RecognitionException {
        try {
            final int _type = 72;
            final int _channel = 0;
            this.match("GetTeamId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETDISTANCEBETWEENCASTERANDTARGET() throws RecognitionException {
        try {
            final int _type = 45;
            final int _channel = 0;
            this.match("GetDistanceBetweenCasterAndTarget");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON() throws RecognitionException {
        try {
            final int _type = 46;
            final int _channel = 0;
            this.match("GetDistanceBetweenTargetAndNearestAllyBeacon");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASFREECELLINEFFECTAREA() throws RecognitionException {
        try {
            final int _type = 132;
            final int _channel = 0;
            this.match("HasFreeCellInEffectArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mPETWITHINRANGE() throws RecognitionException {
        try {
            final int _type = 282;
            final int _channel = 0;
            this.match("PetInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISBACKSTAB() throws RecognitionException {
        try {
            final int _type = 180;
            final int _channel = 0;
            this.match("IsBackstabbed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASLINEOFSIGHT() throws RecognitionException {
        try {
            final int _type = 134;
            final int _channel = 0;
            this.match("HasLineOfSight");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCHARACTERID() throws RecognitionException {
        try {
            final int _type = 37;
            final int _channel = 0;
            this.match("GetCharacterId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETPOSITION() throws RecognitionException {
        try {
            final int _type = 63;
            final int _channel = 0;
            this.match("GetPosition");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETIEPOSITION() throws RecognitionException {
        try {
            final int _type = 51;
            final int _channel = 0;
            this.match("GetIEPosition");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISSEX() throws RecognitionException {
        try {
            final int _type = 214;
            final int _channel = 0;
            this.match("IsSex");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSLOTSINBAG() throws RecognitionException {
        try {
            final int _type = 288;
            final int _channel = 0;
            int alt22 = 2;
            final int LA22_0 = this.input.LA(1);
            if (LA22_0 == 69) {
                alt22 = 1;
            }
            else {
                if (LA22_0 != 80) {
                    final NoViableAltException nvae = new NoViableAltException("", 22, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt22 = 2;
            }
            switch (alt22) {
                case 1: {
                    this.match("EmptyBagSlotsCount");
                    break;
                }
                case 2: {
                    this.match("PlacesSac");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mITEMQUANTITY() throws RecognitionException {
        try {
            final int _type = 260;
            final int _channel = 0;
            this.match("GetItemQuantity");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETKAMASCOUNT() throws RecognitionException {
        try {
            final int _type = 53;
            final int _channel = 0;
            this.match("GetKamasCount");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISMONSTERBREED() throws RecognitionException {
        try {
            final int _type = 197;
            final int _channel = 0;
            this.match("IsMonsterBreed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASWORLDPROPERTY() throws RecognitionException {
        try {
            final int _type = 141;
            final int _channel = 0;
            this.match("HasWorldProperty");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASFIGHTPROPERTY() throws RecognitionException {
        try {
            final int _type = 131;
            final int _channel = 0;
            this.match("HasFightProperty");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETMONTH() throws RecognitionException {
        try {
            final int _type = 58;
            final int _channel = 0;
            this.match("GetMonth");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASNTEVOLVEDSINCE() throws RecognitionException {
        try {
            final int _type = 136;
            final int _channel = 0;
            this.match("HasntEvolvedSince");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETLEVEL() throws RecognitionException {
        try {
            final int _type = 55;
            final int _channel = 0;
            this.match("GetLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETLOCKINCREMENT() throws RecognitionException {
        try {
            final int _type = 56;
            final int _channel = 0;
            this.match("GetLockIncrement");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISBREEDFAMILY() throws RecognitionException {
        try {
            final int _type = 183;
            final int _channel = 0;
            this.match("IsBreedFamily");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISCHALLENGEUSER() throws RecognitionException {
        try {
            final int _type = 187;
            final int _channel = 0;
            this.match("IsChallengeUser");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISUNDERCONTROL() throws RecognitionException {
        try {
            final int _type = 220;
            final int _channel = 0;
            this.match("IsUnderControl");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISAFTER() throws RecognitionException {
        try {
            final int _type = 179;
            final int _channel = 0;
            this.match("IsAfter");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETWAKFUGAUGE() throws RecognitionException {
        try {
            final int _type = 78;
            final int _channel = 0;
            this.match("GetWakfuGauge");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETRANDOMNUMBER() throws RecognitionException {
        try {
            final int _type = 65;
            final int _channel = 0;
            this.match("GetRandomNumber");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETENNEMYCOUNTINRANGE() throws RecognitionException {
        try {
            final int _type = 49;
            final int _channel = 0;
            this.match("GetEnnemyCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETALLIESCOUNTINRANGE() throws RecognitionException {
        try {
            final int _type = 31;
            final int _channel = 0;
            this.match("GetAlliesCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCONTROLLERINSAMETEAMCOUNTINRANGE() throws RecognitionException {
        try {
            final int _type = 38;
            final int _channel = 0;
            this.match("GetControllerInSameTeamCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETDESTRUCTIBLECOUNTINRANGE() throws RecognitionException {
        try {
            final int _type = 44;
            final int _channel = 0;
            this.match("GetDestructibleCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETWALLCOUNTINRANGE() throws RecognitionException {
        try {
            final int _type = 79;
            final int _channel = 0;
            this.match("GetWallCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETNATIONID() throws RecognitionException {
        try {
            final int _type = 60;
            final int _channel = 0;
            this.match("GetNationId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETNATIONALIGNMENT() throws RecognitionException {
        try {
            final int _type = 59;
            final int _channel = 0;
            this.match("GetNationAlignment");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETNATIVENATIONID() throws RecognitionException {
        try {
            final int _type = 62;
            final int _channel = 0;
            this.match("GetNativeNationId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETSTASISGAUGE() throws RecognitionException {
        try {
            final int _type = 70;
            final int _channel = 0;
            this.match("GetStasisGauge");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETDATE() throws RecognitionException {
        try {
            final int _type = 43;
            final int _channel = 0;
            this.match("GetDate");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISFACESTABBED() throws RecognitionException {
        try {
            final int _type = 193;
            final int _channel = 0;
            this.match("IsFacestabbed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCRIMESCORE() throws RecognitionException {
        try {
            final int _type = 41;
            final int _channel = 0;
            this.match("GetCrimeScore");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISDEAD() throws RecognitionException {
        try {
            final int _type = 189;
            final int _channel = 0;
            this.match("IsDead");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETSATISFACTIONLEVEL() throws RecognitionException {
        try {
            final int _type = 66;
            final int _channel = 0;
            this.match("GetSatisfactionLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETBOOLEANVALUE() throws RecognitionException {
        try {
            final int _type = 32;
            final int _channel = 0;
            this.match("GetBooleanValue");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCURRENTPARTITIONNATIONID() throws RecognitionException {
        try {
            final int _type = 42;
            final int _channel = 0;
            this.match("GetCurrentPartitionNationId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETTERRITORYID() throws RecognitionException {
        try {
            final int _type = 73;
            final int _channel = 0;
            this.match("GetTerritoryId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASFREESURROUNDINGCELL() throws RecognitionException {
        try {
            final int _type = 133;
            final int _channel = 0;
            this.match("HasFreeSurroundingCell");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISTARGETCELLFREE() throws RecognitionException {
        try {
            final int _type = 217;
            final int _channel = 0;
            this.match("IsTargetCellFree");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE() throws RecognitionException {
        try {
            final int _type = 256;
            final int _channel = 0;
            this.match("IsTargetCellValidForNewObstacle");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISCARRIED() throws RecognitionException {
        try {
            final int _type = 185;
            final int _channel = 0;
            this.match("IsCarried");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISCARRYING() throws RecognitionException {
        try {
            final int _type = 186;
            final int _channel = 0;
            this.match("IsCarrying");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASAVAILABLECREATUREINSYMBIOT() throws RecognitionException {
        try {
            final int _type = 126;
            final int _channel = 0;
            this.match("HasAvailableCreatureInSymbiot");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSUMMONSLEADERSHIPSCORE() throws RecognitionException {
        try {
            final int _type = 292;
            final int _channel = 0;
            this.match("SummonsLeadershipScore");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mLEADERSHIPFORCURRENTINVOC() throws RecognitionException {
        try {
            final int _type = 261;
            final int _channel = 0;
            this.match("GetTotalLeadershipNeededForCurrentCreature");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETTERRITORYNATIONID() throws RecognitionException {
        try {
            final int _type = 74;
            final int _channel = 0;
            this.match("GetTerritoryNationId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNSUMMON() throws RecognitionException {
        try {
            final int _type = 209;
            final int _channel = 0;
            this.match("IsOwnSummon");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCHARACTERDIRECTION() throws RecognitionException {
        try {
            final int _type = 36;
            final int _channel = 0;
            this.match("GetCharacterDirection");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCRAFTLEARNINGITEM() throws RecognitionException {
        try {
            final int _type = 39;
            final int _channel = 0;
            this.match("GetCraftLearningItem");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASCRAFT() throws RecognitionException {
        try {
            final int _type = 127;
            final int _channel = 0;
            this.match("HasCraft");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETCRAFTLEVEL() throws RecognitionException {
        try {
            final int _type = 40;
            final int _channel = 0;
            this.match("GetCraftLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASEMOTE() throws RecognitionException {
        try {
            final int _type = 128;
            final int _channel = 0;
            this.match("HasEmote");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISPASSEPORTACTIVE() throws RecognitionException {
        try {
            final int _type = 210;
            final int _channel = 0;
            this.match("IsPasseportActive");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mCANBECOMESOLDIERORMILITIAMAN() throws RecognitionException {
        try {
            final int _type = 16;
            final int _channel = 0;
            this.match("CanBecomeSoldierOrMilitiaman");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETTITLE() throws RecognitionException {
        try {
            final int _type = 76;
            final int _channel = 0;
            this.match("GetTitle");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETNATIONRANK() throws RecognitionException {
        try {
            final int _type = 61;
            final int _channel = 0;
            this.match("GetNationRank");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISEQUIPPEDWITHSET() throws RecognitionException {
        try {
            final int _type = 192;
            final int _channel = 0;
            this.match("IsEquippedWithSet");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISHOUR() throws RecognitionException {
        try {
            final int _type = 195;
            final int _channel = 0;
            int alt23 = 2;
            final int LA23_0 = this.input.LA(1);
            if (LA23_0 != 73) {
                final NoViableAltException nvae = new NoViableAltException("", 23, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA23_ = this.input.LA(2);
            if (LA23_ == 115) {
                final int LA23_2 = this.input.LA(3);
                if (LA23_2 == 72) {
                    alt23 = 1;
                }
                else if (LA23_2 == 84) {
                    alt23 = 2;
                }
                else {
                    final int nvaeMark = this.input.mark();
                    try {
                        for (int nvaeConsume = 0; nvaeConsume < 2; ++nvaeConsume) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae2 = new NoViableAltException("", 23, 2, (IntStream)this.input);
                        throw nvae2;
                    }
                    finally {
                        this.input.rewind(nvaeMark);
                    }
                }
            }
            else {
                final int nvaeMark2 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae3 = new NoViableAltException("", 23, 1, (IntStream)this.input);
                    throw nvae3;
                }
                finally {
                    this.input.rewind(nvaeMark2);
                }
            }
            switch (alt23) {
                case 1: {
                    this.match("IsHour");
                    break;
                }
                case 2: {
                    this.match("IsTargetHour");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNHOUR() throws RecognitionException {
        try {
            final int _type = 207;
            final int _channel = 0;
            this.match("IsOwnHour");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISBOMB() throws RecognitionException {
        try {
            final int _type = 181;
            final int _channel = 0;
            this.match("IsBomb");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNBOMB() throws RecognitionException {
        try {
            final int _type = 204;
            final int _channel = 0;
            this.match("IsOwnBomb");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISTUNNEL() throws RecognitionException {
        try {
            final int _type = 218;
            final int _channel = 0;
            this.match("IsTunnel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNDEPOSIT() throws RecognitionException {
        try {
            final int _type = 205;
            final int _channel = 0;
            this.match("IsOwnDeposit");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNBEACON() throws RecognitionException {
        try {
            final int _type = 203;
            final int _channel = 0;
            this.match("IsOwnBeacon");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNSPECIFICAREA() throws RecognitionException {
        try {
            final int _type = 208;
            final int _channel = 0;
            this.match("IsOwnSpecificArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASSUMMONWITHBREED() throws RecognitionException {
        try {
            final int _type = 140;
            final int _channel = 0;
            this.match("HasSummonWithBreed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNBBOMB() throws RecognitionException {
        try {
            final int _type = 267;
            final int _channel = 0;
            this.match("NbBombs");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACHIEVEMENTFAILED() throws RecognitionException {
        try {
            final int _type = 173;
            final int _channel = 0;
            this.match("IsAchievementFailed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACHIEVEMENTRUNNING() throws RecognitionException {
        try {
            final int _type = 176;
            final int _channel = 0;
            this.match("IsAchievementRunning");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACHIEVEMENTOBJECTIVECOMPLETE() throws RecognitionException {
        try {
            final int _type = 174;
            final int _channel = 0;
            this.match("IsAchievementObjectiveComplete");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACHIEVEMENTREPEATABLE() throws RecognitionException {
        try {
            final int _type = 175;
            final int _channel = 0;
            this.match("IsAchievementRepeatable");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mCANRESETACHIEVEMENT() throws RecognitionException {
        try {
            final int _type = 18;
            final int _channel = 0;
            this.match("CanResetAchievement");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mOPPONENTSCONTAINSNATIONENEMY() throws RecognitionException {
        try {
            final int _type = 278;
            final int _channel = 0;
            this.match("OpponentsContainsNationEnemy");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASNATIONJOB() throws RecognitionException {
        try {
            final int _type = 135;
            final int _channel = 0;
            this.match("HasNationJob");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACHIEVEMENTCOMPLETE() throws RecognitionException {
        try {
            final int _type = 172;
            final int _channel = 0;
            this.match("IsAchievementComplete");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACHIEVEMENTACTIVE() throws RecognitionException {
        try {
            final int _type = 171;
            final int _channel = 0;
            this.match("IsAchievementActive");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISPROTECTORINFIGHT() throws RecognitionException {
        try {
            final int _type = 211;
            final int _channel = 0;
            this.match("IsProtectorInFight");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOFFPLAY() throws RecognitionException {
        try {
            final int _type = 199;
            final int _channel = 0;
            this.match("IsOffPlay");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISINGROUP() throws RecognitionException {
        try {
            final int _type = 196;
            final int _channel = 0;
            this.match("IsInGroup");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACTIVATEDBYELEMENT() throws RecognitionException {
        try {
            final int _type = 177;
            final int _channel = 0;
            this.match("IsActivatedByElement");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACTIVATEDBYSPELL() throws RecognitionException {
        try {
            final int _type = 178;
            final int _channel = 0;
            this.match("IsActivatedBySpell");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_ACTIVE_SPELL_ID() throws RecognitionException {
        try {
            final int _type = 80;
            final int _channel = 0;
            this.match("GetActiveSpellId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISONSPECIFICEFFECTAREA() throws RecognitionException {
        try {
            final int _type = 201;
            final int _channel = 0;
            this.match("IsOnSpecificEffectArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISONSPECIFICEFFECTAREAWITHSPECIFICSTATE() throws RecognitionException {
        try {
            final int _type = 202;
            final int _channel = 0;
            this.match("IsOnSpecificEffectAreaWithSpecificState");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISONEFFECTAREATYPE() throws RecognitionException {
        try {
            final int _type = 200;
            final int _channel = 0;
            this.match("IsOnEffectAreaType");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISOWNGLYPH() throws RecognitionException {
        try {
            final int _type = 206;
            final int _channel = 0;
            this.match("IsOwnGlyph");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mCELL_CONTAINS_SPECIFIC_EFFECT_AREA() throws RecognitionException {
        try {
            final int _type = 20;
            final int _channel = 0;
            this.match("CellContainsSpecificEffectArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISDEPOSIT() throws RecognitionException {
        try {
            final int _type = 190;
            final int _channel = 0;
            this.match("IsDeposit");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETSTATECOUNTINRANGE() throws RecognitionException {
        try {
            final int _type = 71;
            final int _channel = 0;
            this.match("GetStateCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISFLEEING() throws RecognitionException {
        try {
            final int _type = 194;
            final int _channel = 0;
            this.match("IsFleeing");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISABANDONNING() throws RecognitionException {
        try {
            final int _type = 169;
            final int _channel = 0;
            this.match("IsAbandonning");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISNATIONFIRSTINDUNGEONLADDER() throws RecognitionException {
        try {
            final int _type = 198;
            final int _channel = 0;
            this.match("IsNationFirstInDungeonLadder");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETFIGHTMODEL() throws RecognitionException {
        try {
            final int _type = 50;
            final int _channel = 0;
            this.match("GetFightModel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_SURROUNDING_CELL_WITH_OWN_BARREL() throws RecognitionException {
        try {
            final int _type = 157;
            final int _channel = 0;
            this.match("HasSurroundingCellWithOwnBarrel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA() throws RecognitionException {
        try {
            final int _type = 158;
            final int _channel = 0;
            this.match("HasSurroundingCellWithOwnEffectArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_SURROUNDING_CELL_WITH_EFFECT_AREA() throws RecognitionException {
        try {
            final int _type = 156;
            final int _channel = 0;
            this.match("HasSurroundingCellWithEffectArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CARRYING_OWN_BARREL() throws RecognitionException {
        try {
            final int _type = 222;
            final int _channel = 0;
            this.match("IsCarryingOwnBarrel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TARGET_COUNT_IN_BEACON_AREA() throws RecognitionException {
        try {
            final int _type = 112;
            final int _channel = 0;
            this.match("GetTargetCountInBeaconArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTERS_WITH_BREED_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 98;
            final int _channel = 0;
            this.match("GetFightersWithBreedInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAI_HAS_CAST_SPELL() throws RecognitionException {
        try {
            final int _type = 7;
            final int _channel = 0;
            this.match("AIHasCastSpell");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAI_HAS_MOVED() throws RecognitionException {
        try {
            final int _type = 8;
            final int _channel = 0;
            this.match("AIHasMoved");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAI_GET_SPELL_CAST_COUNT() throws RecognitionException {
        try {
            final int _type = 6;
            final int _channel = 0;
            this.match("AIGetSpellCastCount");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA() throws RecognitionException {
        try {
            final int _type = 111;
            final int _channel = 0;
            this.match("GetTargetsCountInEffectZoneArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CONTROLLED_BY_AI() throws RecognitionException {
        try {
            final int _type = 231;
            final int _channel = 0;
            this.match("IsControlledByAI");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_SUMMON() throws RecognitionException {
        try {
            final int _type = 254;
            final int _channel = 0;
            this.match("IsSummon");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_SUMMON_FROM_SYMBIOT() throws RecognitionException {
        try {
            final int _type = 255;
            final int _channel = 0;
            this.match("IsSummonedFromSymbiot");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_EFFECT_AREA_COUNT_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 88;
            final int _channel = 0;
            this.match("GetEffectAreaCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE() throws RecognitionException {
        try {
            final int _type = 86;
            final int _channel = 0;
            this.match("GetEffectAreaCountInRunningEffectAOE");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_LINE_OF_SIGHT_FROM_ENEMY() throws RecognitionException {
        try {
            final int _type = 150;
            final int _channel = 0;
            this.match("HasLineOfSightFromEnemy");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_ENEMIES_HUMAN_COUNT_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 91;
            final int _channel = 0;
            this.match("GetEnemiesHumanCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_TARGET_ON_SAME_TEAM() throws RecognitionException {
        try {
            final int _type = 257;
            final int _channel = 0;
            this.match("IsTargetOnSameTeam");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_LOOT() throws RecognitionException {
        try {
            final int _type = 152;
            final int _channel = 0;
            this.match("HasLoot");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_EFFECT_WITH_ACTION_ID() throws RecognitionException {
        try {
            final int _type = 146;
            final int _channel = 0;
            this.match("HasEffectWithActionId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CHARACTER() throws RecognitionException {
        try {
            final int _type = 227;
            final int _channel = 0;
            this.match("IsCharacter");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_STATE_FROM_LEVEL() throws RecognitionException {
        try {
            final int _type = 153;
            final int _channel = 0;
            this.match("HasStateFromLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mDOUBLE_OR_QUITS_CRITERION() throws RecognitionException {
        try {
            final int _type = 23;
            final int _channel = 0;
            this.match("DoubleOrQuitsCriterion");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_WEAPON_TYPE() throws RecognitionException {
        try {
            final int _type = 163;
            final int _channel = 0;
            this.match("HasWeaponType");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_OWN_AREA() throws RecognitionException {
        try {
            final int _type = 247;
            final int _channel = 0;
            this.match("IsOwnArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_ON_BORDER_CELL() throws RecognitionException {
        try {
            final int _type = 242;
            final int _channel = 0;
            this.match("IsOnBorderCell");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETPROTECTORNATIONID() throws RecognitionException {
        try {
            final int _type = 64;
            final int _channel = 0;
            this.match("GetProtectorNationId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_ROUBLABOT() throws RecognitionException {
        try {
            final int _type = 274;
            final int _channel = 0;
            this.match("NbRoublabot");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_EFFECT_WITH_SPECIFIC_ID() throws RecognitionException {
        try {
            final int _type = 147;
            final int _channel = 0;
            this.match("HasEffectWithSpecificId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_FECA_ARMOR() throws RecognitionException {
        try {
            final int _type = 148;
            final int _channel = 0;
            this.match("HasFecaArmor");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_FECA_GLYPH_CENTER() throws RecognitionException {
        try {
            final int _type = 233;
            final int _channel = 0;
            this.match("IsFecaGlyphCenter");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_FECA_GLYPH() throws RecognitionException {
        try {
            final int _type = 270;
            final int _channel = 0;
            this.match("NbFecaGlyph");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETACHIEVEMENTVARIABLE() throws RecognitionException {
        try {
            final int _type = 30;
            final int _channel = 0;
            this.match("GetAchievementVariable");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_CHALLENGE_UNAVAILABILITY_DURATION() throws RecognitionException {
        try {
            final int _type = 83;
            final int _channel = 0;
            this.match("GetChallengeUnavailabilityDuration");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_EFFECT_CASTER_ORIGINAL_CONTROLLER() throws RecognitionException {
        try {
            final int _type = 89;
            final int _channel = 0;
            this.match("GetEffectCasterOriginalController");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_EFFECT_TARGET_ORIGINAL_CONTROLLER() throws RecognitionException {
        try {
            final int _type = 90;
            final int _channel = 0;
            this.match("GetEffectTargetOriginalController");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_ORIGINAL_CONTROLLER() throws RecognitionException {
        try {
            final int _type = 245;
            final int _channel = 0;
            this.match("IsOriginalController");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mCASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER() throws RecognitionException {
        try {
            final int _type = 19;
            final int _channel = 0;
            this.match("CasterAndTargetHaveSameOriginalController");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CHARACTERISTIC_FULL() throws RecognitionException {
        try {
            final int _type = 228;
            final int _channel = 0;
            int alt24 = 2;
            final int LA24_0 = this.input.LA(1);
            if (LA24_0 != 73) {
                final NoViableAltException nvae = new NoViableAltException("", 24, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA24_ = this.input.LA(2);
            if (LA24_ == 115) {
                final int LA24_2 = this.input.LA(3);
                if (LA24_2 == 67) {
                    final int LA24_3 = this.input.LA(4);
                    if (LA24_3 == 104) {
                        final int LA24_4 = this.input.LA(5);
                        if (LA24_4 == 97) {
                            final int LA24_5 = this.input.LA(6);
                            if (LA24_5 == 114) {
                                final int LA24_6 = this.input.LA(7);
                                if (LA24_6 == 97) {
                                    final int LA24_7 = this.input.LA(8);
                                    if (LA24_7 == 99) {
                                        final int LA24_8 = this.input.LA(9);
                                        if (LA24_8 == 116) {
                                            alt24 = 1;
                                        }
                                        else if (LA24_8 == 70) {
                                            alt24 = 2;
                                        }
                                        else {
                                            final int nvaeMark = this.input.mark();
                                            try {
                                                for (int nvaeConsume = 0; nvaeConsume < 8; ++nvaeConsume) {
                                                    this.input.consume();
                                                }
                                                final NoViableAltException nvae2 = new NoViableAltException("", 24, 8, (IntStream)this.input);
                                                throw nvae2;
                                            }
                                            finally {
                                                this.input.rewind(nvaeMark);
                                            }
                                        }
                                    }
                                    else {
                                        final int nvaeMark2 = this.input.mark();
                                        try {
                                            for (int nvaeConsume2 = 0; nvaeConsume2 < 7; ++nvaeConsume2) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae3 = new NoViableAltException("", 24, 7, (IntStream)this.input);
                                            throw nvae3;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark2);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark3 = this.input.mark();
                                    try {
                                        for (int nvaeConsume3 = 0; nvaeConsume3 < 6; ++nvaeConsume3) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae4 = new NoViableAltException("", 24, 6, (IntStream)this.input);
                                        throw nvae4;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark3);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark4 = this.input.mark();
                                try {
                                    for (int nvaeConsume4 = 0; nvaeConsume4 < 5; ++nvaeConsume4) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae5 = new NoViableAltException("", 24, 5, (IntStream)this.input);
                                    throw nvae5;
                                }
                                finally {
                                    this.input.rewind(nvaeMark4);
                                }
                            }
                        }
                        else {
                            final int nvaeMark5 = this.input.mark();
                            try {
                                for (int nvaeConsume5 = 0; nvaeConsume5 < 4; ++nvaeConsume5) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae6 = new NoViableAltException("", 24, 4, (IntStream)this.input);
                                throw nvae6;
                            }
                            finally {
                                this.input.rewind(nvaeMark5);
                            }
                        }
                    }
                    else {
                        final int nvaeMark6 = this.input.mark();
                        try {
                            for (int nvaeConsume6 = 0; nvaeConsume6 < 3; ++nvaeConsume6) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae7 = new NoViableAltException("", 24, 3, (IntStream)this.input);
                            throw nvae7;
                        }
                        finally {
                            this.input.rewind(nvaeMark6);
                        }
                    }
                }
                else {
                    final int nvaeMark7 = this.input.mark();
                    try {
                        for (int nvaeConsume7 = 0; nvaeConsume7 < 2; ++nvaeConsume7) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae8 = new NoViableAltException("", 24, 2, (IntStream)this.input);
                        throw nvae8;
                    }
                    finally {
                        this.input.rewind(nvaeMark7);
                    }
                }
            }
            else {
                final int nvaeMark8 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae9 = new NoViableAltException("", 24, 1, (IntStream)this.input);
                    throw nvae9;
                }
                finally {
                    this.input.rewind(nvaeMark8);
                }
            }
            switch (alt24) {
                case 1: {
                    this.match("IsCharacteristicFull");
                    break;
                }
                case 2: {
                    this.match("IsCharacFull");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISACCOUNTSUBSCRIBED() throws RecognitionException {
        try {
            final int _type = 170;
            final int _channel = 0;
            this.match("IsAccountSubscribed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER() throws RecognitionException {
        try {
            final int _type = 117;
            final int _channel = 0;
            this.match("GetTriggeringEffectCasterIsSameAsCaster");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_EFFECT_CASTER() throws RecognitionException {
        try {
            final int _type = 116;
            final int _channel = 0;
            this.match("GetTriggeringEffectCaster");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_ON_OWN_DIAL() throws RecognitionException {
        try {
            final int _type = 244;
            final int _channel = 0;
            this.match("IsOnOwnDial");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_HYDRANDS() throws RecognitionException {
        try {
            final int _type = 273;
            final int _channel = 0;
            this.match("NbHydrands");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_STEAMBOTS() throws RecognitionException {
        try {
            final int _type = 275;
            final int _channel = 0;
            int alt25 = 2;
            final int LA25_0 = this.input.LA(1);
            if (LA25_0 != 78) {
                final NoViableAltException nvae = new NoViableAltException("", 25, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA25_ = this.input.LA(2);
            if (LA25_ == 98) {
                final int LA25_2 = this.input.LA(3);
                if (LA25_2 == 83) {
                    final int LA25_3 = this.input.LA(4);
                    if (LA25_3 == 116) {
                        final int LA25_4 = this.input.LA(5);
                        if (LA25_4 == 101) {
                            final int LA25_5 = this.input.LA(6);
                            if (LA25_5 == 97) {
                                final int LA25_6 = this.input.LA(7);
                                if (LA25_6 == 109) {
                                    final int LA25_7 = this.input.LA(8);
                                    if (LA25_7 == 66) {
                                        alt25 = 1;
                                    }
                                    else if (LA25_7 == 98) {
                                        alt25 = 2;
                                    }
                                    else {
                                        final int nvaeMark = this.input.mark();
                                        try {
                                            for (int nvaeConsume = 0; nvaeConsume < 7; ++nvaeConsume) {
                                                this.input.consume();
                                            }
                                            final NoViableAltException nvae2 = new NoViableAltException("", 25, 7, (IntStream)this.input);
                                            throw nvae2;
                                        }
                                        finally {
                                            this.input.rewind(nvaeMark);
                                        }
                                    }
                                }
                                else {
                                    final int nvaeMark2 = this.input.mark();
                                    try {
                                        for (int nvaeConsume2 = 0; nvaeConsume2 < 6; ++nvaeConsume2) {
                                            this.input.consume();
                                        }
                                        final NoViableAltException nvae3 = new NoViableAltException("", 25, 6, (IntStream)this.input);
                                        throw nvae3;
                                    }
                                    finally {
                                        this.input.rewind(nvaeMark2);
                                    }
                                }
                            }
                            else {
                                final int nvaeMark3 = this.input.mark();
                                try {
                                    for (int nvaeConsume3 = 0; nvaeConsume3 < 5; ++nvaeConsume3) {
                                        this.input.consume();
                                    }
                                    final NoViableAltException nvae4 = new NoViableAltException("", 25, 5, (IntStream)this.input);
                                    throw nvae4;
                                }
                                finally {
                                    this.input.rewind(nvaeMark3);
                                }
                            }
                        }
                        else {
                            final int nvaeMark4 = this.input.mark();
                            try {
                                for (int nvaeConsume4 = 0; nvaeConsume4 < 4; ++nvaeConsume4) {
                                    this.input.consume();
                                }
                                final NoViableAltException nvae5 = new NoViableAltException("", 25, 4, (IntStream)this.input);
                                throw nvae5;
                            }
                            finally {
                                this.input.rewind(nvaeMark4);
                            }
                        }
                    }
                    else {
                        final int nvaeMark5 = this.input.mark();
                        try {
                            for (int nvaeConsume5 = 0; nvaeConsume5 < 3; ++nvaeConsume5) {
                                this.input.consume();
                            }
                            final NoViableAltException nvae6 = new NoViableAltException("", 25, 3, (IntStream)this.input);
                            throw nvae6;
                        }
                        finally {
                            this.input.rewind(nvaeMark5);
                        }
                    }
                }
                else {
                    final int nvaeMark6 = this.input.mark();
                    try {
                        for (int nvaeConsume6 = 0; nvaeConsume6 < 2; ++nvaeConsume6) {
                            this.input.consume();
                        }
                        final NoViableAltException nvae7 = new NoViableAltException("", 25, 2, (IntStream)this.input);
                        throw nvae7;
                    }
                    finally {
                        this.input.rewind(nvaeMark6);
                    }
                }
            }
            else {
                final int nvaeMark7 = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae8 = new NoViableAltException("", 25, 1, (IntStream)this.input);
                    throw nvae8;
                }
                finally {
                    this.input.rewind(nvaeMark7);
                }
            }
            switch (alt25) {
                case 1: {
                    this.match("NbSteamBots");
                    break;
                }
                case 2: {
                    this.match("NbSteambots");
                    break;
                }
            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_OWN_FECA_GLYPH() throws RecognitionException {
        try {
            final int _type = 248;
            final int _channel = 0;
            this.match("IsOwnFecaGlyph");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER() throws RecognitionException {
        try {
            final int _type = 85;
            final int _channel = 0;
            this.match("GetDistanceBetweenTargetAndEffectBearer");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FGHT_CURRENT_TABLE_TURN() throws RecognitionException {
        try {
            final int _type = 92;
            final int _channel = 0;
            this.match("GetFightCurrentTableTurn");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_ALL_AREAS() throws RecognitionException {
        try {
            final int _type = 268;
            final int _channel = 0;
            this.match("NbAllAreas");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_GLYPHS() throws RecognitionException {
        try {
            final int _type = 272;
            final int _channel = 0;
            this.match("NbGlyphs");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_EFFECT_TARGET_BREED_ID() throws RecognitionException {
        try {
            final int _type = 120;
            final int _channel = 0;
            this.match("GetTriggeringEffectTargetBreedId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_ANCESTORS_COUNT() throws RecognitionException {
        try {
            final int _type = 115;
            final int _channel = 0;
            this.match("GetTriggeringAncestorsCount");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_EFFECT_ID() throws RecognitionException {
        try {
            final int _type = 118;
            final int _channel = 0;
            this.match("GetTriggeringEffectId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_SIDE_STABBED() throws RecognitionException {
        try {
            final int _type = 253;
            final int _channel = 0;
            this.match("IsSidestabbed");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_EFFECT_TARGET() throws RecognitionException {
        try {
            final int _type = 119;
            final int _channel = 0;
            this.match("GetTriggeringEffectTarget");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_LINE_OF_SIGHT_TO_ENEMY() throws RecognitionException {
        try {
            final int _type = 151;
            final int _channel = 0;
            this.match("HasLineOfSightToEnemy");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_PLAYER() throws RecognitionException {
        try {
            final int _type = 249;
            final int _channel = 0;
            this.match("IsPlayer");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_COMPANION() throws RecognitionException {
        try {
            final int _type = 230;
            final int _channel = 0;
            this.match("IsCompanion");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_CASTER_FECA_ARMOR() throws RecognitionException {
        try {
            final int _type = 145;
            final int _channel = 0;
            this.match("HasCasterFecaArmor");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CHALLENGER() throws RecognitionException {
        try {
            final int _type = 226;
            final int _channel = 0;
            this.match("IsChallenger");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CARRYING_OWN_BOMB() throws RecognitionException {
        try {
            final int _type = 223;
            final int _channel = 0;
            this.match("IsCarryingOwnBomb");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_SURROUNDING_CELL_WITH_OWN_SUMMON() throws RecognitionException {
        try {
            final int _type = 159;
            final int _channel = 0;
            this.match("HasSurroundingCellWithOwnSummon");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_GUILD_LEVEL() throws RecognitionException {
        try {
            final int _type = 100;
            final int _channel = 0;
            this.match("GetGuildLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_IN_GUILD() throws RecognitionException {
        try {
            final int _type = 239;
            final int _channel = 0;
            this.match("IsInGuild");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_GUILD_PARTNER_COUNT_IN_FIGHT() throws RecognitionException {
        try {
            final int _type = 101;
            final int _channel = 0;
            this.match("GetGuildPartnerCountInFight");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_IN_ALIGNMENT() throws RecognitionException {
        try {
            final int _type = 237;
            final int _channel = 0;
            this.match("IsInAlignment");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_VALID_PATH_TO_TARGET() throws RecognitionException {
        try {
            final int _type = 162;
            final int _channel = 0;
            this.match("HasValidPathToTarget");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_FREE_CELL() throws RecognitionException {
        try {
            final int _type = 234;
            final int _channel = 0;
            this.match("IsFreeCell");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_BEEN_RAISED_BY_EFFECT() throws RecognitionException {
        try {
            final int _type = 144;
            final int _channel = 0;
            this.match("HasBeenRaisedByEffect");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_X() throws RecognitionException {
        try {
            final int _type = 122;
            final int _channel = 0;
            this.match("GetX");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_Y() throws RecognitionException {
        try {
            final int _type = 124;
            final int _channel = 0;
            this.match("GetY");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_Z() throws RecognitionException {
        try {
            final int _type = 125;
            final int _channel = 0;
            this.match("GetZ");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_AREAS_WITH_BASE_ID() throws RecognitionException {
        try {
            final int _type = 269;
            final int _channel = 0;
            this.match("NbAreasWithBaseId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_EFFECTS_COUNT_WITH_SPECIFIC_IDS() throws RecognitionException {
        try {
            final int _type = 87;
            final int _channel = 0;
            this.match("GetEffectsCountWithSpecificIds");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_PARTITION_X() throws RecognitionException {
        try {
            final int _type = 106;
            final int _channel = 0;
            this.match("GetPartitionX");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_PARTITION_Y() throws RecognitionException {
        try {
            final int _type = 107;
            final int _channel = 0;
            this.match("GetPartitionY");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TOTAL_HP_IN_PCT() throws RecognitionException {
        try {
            final int _type = 114;
            final int _channel = 0;
            this.match("GetTotalHpInPercent");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_PROTECTOR_CHALLENGE_KAMA_AMOUNT() throws RecognitionException {
        try {
            final int _type = 108;
            final int _channel = 0;
            this.match("GetProtectorChallengeKamaAmount");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_ALLIES_COUNT() throws RecognitionException {
        try {
            final int _type = 81;
            final int _channel = 0;
            this.match("GetAlliesCount");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CELL_BEHIND_TARGET_FREE() throws RecognitionException {
        try {
            final int _type = 225;
            final int _channel = 0;
            this.match("IsCellBehindTargetFree");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_STATE_LEVEL() throws RecognitionException {
        try {
            final int _type = 110;
            final int _channel = 0;
            this.match("GetStateLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA() throws RecognitionException {
        try {
            final int _type = 243;
            final int _channel = 0;
            this.match("IsOnOriginalControllerSpecificArea");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_ANOTHER_SAME_EQUIPMENT() throws RecognitionException {
        try {
            final int _type = 142;
            final int _channel = 0;
            this.match("HasAnotherSameEquipment");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_LOCKED() throws RecognitionException {
        try {
            final int _type = 241;
            final int _channel = 0;
            this.match("IsLocked");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_STATE_FROM_USER() throws RecognitionException {
        try {
            final int _type = 154;
            final int _channel = 0;
            this.match("HasStateFromUser");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_HUMAN_ALLIES_COUNT_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 102;
            final int _channel = 0;
            this.match("GetHumanAlliesCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CHARACTER_WITH_HIGHEST_STATE_LEVEL() throws RecognitionException {
        try {
            final int _type = 229;
            final int _channel = 0;
            this.match("IsCharacterWithHighestStateLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_GUILD_BONUS() throws RecognitionException {
        try {
            final int _type = 149;
            final int _channel = 0;
            this.match("HasGuildBonus");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISZONEINCHAOS() throws RecognitionException {
        try {
            final int _type = 221;
            final int _channel = 0;
            this.match("IsZoneInChaos");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_NEXT_FIGHTER_IN_TIMELINE() throws RecognitionException {
        try {
            final int _type = 103;
            final int _channel = 0;
            this.match("GetNextFighterInTimeline");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_TRIGGERED_BY_ZONE_EFFECT() throws RecognitionException {
        try {
            final int _type = 258;
            final int _channel = 0;
            this.match("IsTriggeredByZoneEffect");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 109;
            final int _channel = 0;
            this.match("GetSpecificEffectAreaCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_BOOLEAN_SYSTEM_CONFIGURATION() throws RecognitionException {
        try {
            final int _type = 82;
            final int _channel = 0;
            this.match("GetBooleanSystemConfiguration");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTERS_MAX_LEVEL() throws RecognitionException {
        try {
            final int _type = 96;
            final int _channel = 0;
            this.match("GetFightersMaxLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTERS_MIN_LEVEL() throws RecognitionException {
        try {
            final int _type = 97;
            final int _channel = 0;
            this.match("GetFightersMinLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTERS_LEVEL_DIFF() throws RecognitionException {
        try {
            final int _type = 94;
            final int _channel = 0;
            this.match("GetFightersLevelDiff");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTERS_LEVEL_SUM() throws RecognitionException {
        try {
            final int _type = 95;
            final int _channel = 0;
            this.match("GetFightersLevelSum");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_PRELOADING() throws RecognitionException {
        try {
            final int _type = 250;
            final int _channel = 0;
            this.match("IsPreloading");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_UNLOCKED_COMPANION() throws RecognitionException {
        try {
            final int _type = 160;
            final int _channel = 0;
            this.match("HasUnlockedCompanion");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_OUT_OF_PLAY() throws RecognitionException {
        try {
            final int _type = 246;
            final int _channel = 0;
            this.match("IsOutOfPlay");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_IN_FIGHT() throws RecognitionException {
        try {
            final int _type = 238;
            final int _channel = 0;
            this.match("IsInFight");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_IN_PLAY() throws RecognitionException {
        try {
            final int _type = 240;
            final int _channel = 0;
            this.match("IsInPlay");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_CASTER_FACING_FIGHTER() throws RecognitionException {
        try {
            final int _type = 224;
            final int _channel = 0;
            this.match("IsCasterFacingFighter");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTER_ID() throws RecognitionException {
        try {
            final int _type = 99;
            final int _channel = 0;
            this.match("GetFighterId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TRIGGERING_EFFECT_VALUE() throws RecognitionException {
        try {
            final int _type = 121;
            final int _channel = 0;
            this.match("GetTriggeringEffectValue");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_OWN_ARMOR_COUNT() throws RecognitionException {
        try {
            final int _type = 104;
            final int _channel = 0;
            this.match("GetOwnArmorCount");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_OWN_TEAM_STATE_COUNT_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 105;
            final int _channel = 0;
            this.match("GetOwnTeamStateCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_FIGHTERS_CHARACTERISTIC_MAX_VALUE() throws RecognitionException {
        try {
            final int _type = 93;
            final int _channel = 0;
            this.match("GetFightersCharacteristicMaxValue");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_SUBSCRIPTION_LEVEL() throws RecognitionException {
        try {
            final int _type = 155;
            final int _channel = 0;
            this.match("HasSubscriptionLevel");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_TRIGGERING_EFFECT_CRITICAL() throws RecognitionException {
        try {
            final int _type = 259;
            final int _channel = 0;
            this.match("IsTriggeringEffectCritical");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mISPVP() throws RecognitionException {
        try {
            final int _type = 212;
            final int _channel = 0;
            this.match("IsPvp");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHASPVPRANK() throws RecognitionException {
        try {
            final int _type = 137;
            final int _channel = 0;
            this.match("HasPvpRank");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_ENNEMY_NATION() throws RecognitionException {
        try {
            final int _type = 232;
            final int _channel = 0;
            this.match("IsEnnemyNation");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_PVP_STATE_ACTIVE() throws RecognitionException {
        try {
            final int _type = 251;
            final int _channel = 0;
            this.match("IsPvpStateActive");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGETLASTINSTANCEID() throws RecognitionException {
        try {
            final int _type = 54;
            final int _channel = 0;
            this.match("GetLastInstanceId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_HOSTILE() throws RecognitionException {
        try {
            final int _type = 236;
            final int _channel = 0;
            this.match("IsHostile");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_BEEN_NAUGHTY() throws RecognitionException {
        try {
            final int _type = 143;
            final int _channel = 0;
            this.match("HasBeenNaughty");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mNB_GATES() throws RecognitionException {
        try {
            final int _type = 271;
            final int _channel = 0;
            this.match("NbGates");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_TEAM_EFFECT_AREA_COUNT_IN_RANGE() throws RecognitionException {
        try {
            final int _type = 113;
            final int _channel = 0;
            this.match("GetTeamEffectAreaCountInRange");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mHAS_VALID_GATE_FOR_TP() throws RecognitionException {
        try {
            final int _type = 161;
            final int _channel = 0;
            this.match("HasValidGateForTp");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mUSE_GATE_EFFECT() throws RecognitionException {
        try {
            final int _type = 298;
            final int _channel = 0;
            this.match("UseGateEffect");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mGET_CURRENT_FIGHTER_ID() throws RecognitionException {
        try {
            final int _type = 84;
            final int _channel = 0;
            this.match("GetCurrentFighterId");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mIS_HERO() throws RecognitionException {
        try {
            final int _type = 235;
            final int _channel = 0;
            this.match("IsHero");
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mVIRGULE() throws RecognitionException {
        try {
            final int _type = 300;
            final int _channel = 0;
            this.match(44);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mINTEGER() throws RecognitionException {
        Label_0211: {
            try {
                final int _type = 168;
                final int _channel = 0;
                int alt26 = 2;
                final int LA26_0 = this.input.LA(1);
                if (LA26_0 == 45) {
                    alt26 = 1;
                }
                switch (alt26) {
                    case 1: {
                        this.match(45);
                        break;
                    }
                }
                this.matchRange(48, 57);
                while (true) {
                    int alt2 = 2;
                    final int LA27_0 = this.input.LA(1);
                    if (LA27_0 >= 48 && LA27_0 <= 57) {
                        alt2 = 1;
                    }
                    switch (alt2) {
                        case 1: {
                            if (this.input.LA(1) >= 48 && this.input.LA(1) <= 57) {
                                this.input.consume();
                                continue;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            this.recover((RecognitionException)mse);
                            throw mse;
                        }
                        default: {
                            this.state.type = _type;
                            this.state.channel = _channel;
                            break Label_0211;
                        }
                    }
                }
            }
            finally {}
        }
    }
    
    public final void mFLOAT() throws RecognitionException {
        Label_0362: {
            try {
                final int _type = 29;
                final int _channel = 0;
                int alt28 = 2;
                final int LA28_0 = this.input.LA(1);
                if (LA28_0 == 45) {
                    alt28 = 1;
                }
                switch (alt28) {
                    case 1: {
                        this.match(45);
                        break;
                    }
                }
                while (true) {
                    int alt2 = 2;
                    final int LA29_0 = this.input.LA(1);
                    if (LA29_0 >= 48 && LA29_0 <= 57) {
                        alt2 = 1;
                    }
                    switch (alt2) {
                        case 1: {
                            if (this.input.LA(1) >= 48 && this.input.LA(1) <= 57) {
                                this.input.consume();
                                continue;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            this.recover((RecognitionException)mse);
                            throw mse;
                        }
                        default: {
                            this.match(46);
                            int cnt30 = 0;
                            while (true) {
                                int alt3 = 2;
                                final int LA30_0 = this.input.LA(1);
                                if (LA30_0 >= 48 && LA30_0 <= 57) {
                                    alt3 = 1;
                                }
                                switch (alt3) {
                                    case 1: {
                                        if (this.input.LA(1) >= 48 && this.input.LA(1) <= 57) {
                                            this.input.consume();
                                            ++cnt30;
                                            continue;
                                        }
                                        final MismatchedSetException mse2 = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                                        this.recover((RecognitionException)mse2);
                                        throw mse2;
                                    }
                                    default: {
                                        if (cnt30 >= 1) {
                                            this.state.type = _type;
                                            this.state.channel = _channel;
                                            break Label_0362;
                                        }
                                        final EarlyExitException eee = new EarlyExitException(30, (IntStream)this.input);
                                        throw eee;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            finally {}
        }
    }
    
    public final void mDIVIDE() throws RecognitionException {
        try {
            final int _type = 22;
            final int _channel = 0;
            this.match(47);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mMOD() throws RecognitionException {
        try {
            final int _type = 265;
            final int _channel = 0;
            this.match(37);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mMULT() throws RecognitionException {
        try {
            final int _type = 266;
            final int _channel = 0;
            this.match(42);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mPLUS() throws RecognitionException {
        try {
            final int _type = 284;
            final int _channel = 0;
            this.match(43);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mMINUS() throws RecognitionException {
        try {
            final int _type = 264;
            final int _channel = 0;
            this.match(45);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mEOL() throws RecognitionException {
        try {
            final int _type = 26;
            final int _channel = 0;
            if (this.input.LA(1) != 10 && this.input.LA(1) != 13) {
                final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                this.recover((RecognitionException)mse);
                throw mse;
            }
            this.input.consume();
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mWS() throws RecognitionException {
        Label_0185: {
            try {
                final int _type = 302;
                final int _channel = 0;
                int cnt31 = 0;
                while (true) {
                    int alt31 = 2;
                    final int LA31_0 = this.input.LA(1);
                    if (LA31_0 == 9 || LA31_0 == 32) {
                        alt31 = 1;
                    }
                    switch (alt31) {
                        case 1: {
                            if (this.input.LA(1) == 9 || this.input.LA(1) == 32) {
                                this.input.consume();
                                ++cnt31;
                                continue;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            this.recover((RecognitionException)mse);
                            throw mse;
                        }
                        default: {
                            if (cnt31 >= 1) {
                                this.skip();
                                this.state.type = _type;
                                this.state.channel = _channel;
                                break Label_0185;
                            }
                            final EarlyExitException eee = new EarlyExitException(31, (IntStream)this.input);
                            throw eee;
                        }
                    }
                }
            }
            finally {}
        }
    }
    
    public final void mPV() throws RecognitionException {
        try {
            final int _type = 286;
            final int _channel = 0;
            this.match(59);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mSHARP() throws RecognitionException {
        try {
            final int _type = 287;
            final int _channel = 0;
            this.match(35);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public final void mAT() throws RecognitionException {
        try {
            final int _type = 11;
            final int _channel = 0;
            this.match(64);
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {}
    }
    
    public void mTokens() throws RecognitionException {
        int alt32 = 295;
        alt32 = this.dfa32.predict((IntStream)this.input);
        switch (alt32) {
            case 1: {
                this.mPD();
                break;
            }
            case 2: {
                this.mAND();
                break;
            }
            case 3: {
                this.mOR();
                break;
            }
            case 4: {
                this.mNOT_EQUALS();
                break;
            }
            case 5: {
                this.mNOT();
                break;
            }
            case 6: {
                this.mIF();
                break;
            }
            case 7: {
                this.mTHEN();
                break;
            }
            case 8: {
                this.mELSE();
                break;
            }
            case 9: {
                this.mAG();
                break;
            }
            case 10: {
                this.mAD();
                break;
            }
            case 11: {
                this.mBG();
                break;
            }
            case 12: {
                this.mBD();
                break;
            }
            case 13: {
                this.mPOINT();
                break;
            }
            case 14: {
                this.mINF();
                break;
            }
            case 15: {
                this.mSUP();
                break;
            }
            case 16: {
                this.mINFEQ();
                break;
            }
            case 17: {
                this.mSUPEQ();
                break;
            }
            case 18: {
                this.mASSIGN();
                break;
            }
            case 19: {
                this.mEQUALS();
                break;
            }
            case 20: {
                this.mPG();
                break;
            }
            case 21: {
                this.mSTRING();
                break;
            }
            case 22: {
                this.mTRUE();
                break;
            }
            case 23: {
                this.mFALSE();
                break;
            }
            case 24: {
                this.mVARNAME();
                break;
            }
            case 25: {
                this.mHASEQTYPE();
                break;
            }
            case 26: {
                this.mHASEQID();
                break;
            }
            case 27: {
                this.mHASSUMMONS();
                break;
            }
            case 28: {
                this.mISENNEMY();
                break;
            }
            case 29: {
                this.mCANCARRYTARGET();
                break;
            }
            case 30: {
                this.mGETCHA();
                break;
            }
            case 31: {
                this.mGETCHAPCT();
                break;
            }
            case 32: {
                this.mGETCHAMAX();
                break;
            }
            case 33: {
                this.mGETMONST();
                break;
            }
            case 34: {
                this.mSPACEINSYMBIOT();
                break;
            }
            case 35: {
                this.mTRAPAMOUNT();
                break;
            }
            case 36: {
                this.mWALLAMOUNT();
                break;
            }
            case 37: {
                this.mSUMMONAMOUNT();
                break;
            }
            case 38: {
                this.mBEACONAMOUNT();
                break;
            }
            case 39: {
                this.mBARRELAMOUNT();
                break;
            }
            case 40: {
                this.mGET_XELOR_DIALS_COUNT();
                break;
            }
            case 41: {
                this.mIS_SELECTED_CREATURE_AVAILABLE();
                break;
            }
            case 42: {
                this.mEFFECTISFROMHEAL();
                break;
            }
            case 43: {
                this.mOWNSBEACON();
                break;
            }
            case 44: {
                this.mISSPECIFICAREA();
                break;
            }
            case 45: {
                this.mISSPECIFICAREAWITHSPECIFICSTATE();
                break;
            }
            case 46: {
                this.mGETTIME();
                break;
            }
            case 47: {
                this.mISDAY();
                break;
            }
            case 48: {
                this.mISSEASON();
                break;
            }
            case 49: {
                this.mISBREEDID();
                break;
            }
            case 50: {
                this.mISBREED();
                break;
            }
            case 51: {
                this.mISUNDEAD();
                break;
            }
            case 52: {
                this.mHASSTATE();
                break;
            }
            case 53: {
                this.mGETSKILLLEVEL();
                break;
            }
            case 54: {
                this.mGETSPELLLEVEL();
                break;
            }
            case 55: {
                this.mGETSPELLTREELEVEL();
                break;
            }
            case 56: {
                this.mGETINSTANCEID();
                break;
            }
            case 57: {
                this.mGETEFFECTCASTER();
                break;
            }
            case 58: {
                this.mGETEFFECTTARGET();
                break;
            }
            case 59: {
                this.mGETTRIGGEREREFFECTCASTER();
                break;
            }
            case 60: {
                this.mGETTEAMID();
                break;
            }
            case 61: {
                this.mGETDISTANCEBETWEENCASTERANDTARGET();
                break;
            }
            case 62: {
                this.mGETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON();
                break;
            }
            case 63: {
                this.mHASFREECELLINEFFECTAREA();
                break;
            }
            case 64: {
                this.mPETWITHINRANGE();
                break;
            }
            case 65: {
                this.mISBACKSTAB();
                break;
            }
            case 66: {
                this.mHASLINEOFSIGHT();
                break;
            }
            case 67: {
                this.mGETCHARACTERID();
                break;
            }
            case 68: {
                this.mGETPOSITION();
                break;
            }
            case 69: {
                this.mGETIEPOSITION();
                break;
            }
            case 70: {
                this.mISSEX();
                break;
            }
            case 71: {
                this.mSLOTSINBAG();
                break;
            }
            case 72: {
                this.mITEMQUANTITY();
                break;
            }
            case 73: {
                this.mGETKAMASCOUNT();
                break;
            }
            case 74: {
                this.mISMONSTERBREED();
                break;
            }
            case 75: {
                this.mHASWORLDPROPERTY();
                break;
            }
            case 76: {
                this.mHASFIGHTPROPERTY();
                break;
            }
            case 77: {
                this.mGETMONTH();
                break;
            }
            case 78: {
                this.mHASNTEVOLVEDSINCE();
                break;
            }
            case 79: {
                this.mGETLEVEL();
                break;
            }
            case 80: {
                this.mGETLOCKINCREMENT();
                break;
            }
            case 81: {
                this.mISBREEDFAMILY();
                break;
            }
            case 82: {
                this.mISCHALLENGEUSER();
                break;
            }
            case 83: {
                this.mISUNDERCONTROL();
                break;
            }
            case 84: {
                this.mISAFTER();
                break;
            }
            case 85: {
                this.mGETWAKFUGAUGE();
                break;
            }
            case 86: {
                this.mGETRANDOMNUMBER();
                break;
            }
            case 87: {
                this.mGETENNEMYCOUNTINRANGE();
                break;
            }
            case 88: {
                this.mGETALLIESCOUNTINRANGE();
                break;
            }
            case 89: {
                this.mGETCONTROLLERINSAMETEAMCOUNTINRANGE();
                break;
            }
            case 90: {
                this.mGETDESTRUCTIBLECOUNTINRANGE();
                break;
            }
            case 91: {
                this.mGETWALLCOUNTINRANGE();
                break;
            }
            case 92: {
                this.mGETNATIONID();
                break;
            }
            case 93: {
                this.mGETNATIONALIGNMENT();
                break;
            }
            case 94: {
                this.mGETNATIVENATIONID();
                break;
            }
            case 95: {
                this.mGETSTASISGAUGE();
                break;
            }
            case 96: {
                this.mGETDATE();
                break;
            }
            case 97: {
                this.mISFACESTABBED();
                break;
            }
            case 98: {
                this.mGETCRIMESCORE();
                break;
            }
            case 99: {
                this.mISDEAD();
                break;
            }
            case 100: {
                this.mGETSATISFACTIONLEVEL();
                break;
            }
            case 101: {
                this.mGETBOOLEANVALUE();
                break;
            }
            case 102: {
                this.mGETCURRENTPARTITIONNATIONID();
                break;
            }
            case 103: {
                this.mGETTERRITORYID();
                break;
            }
            case 104: {
                this.mHASFREESURROUNDINGCELL();
                break;
            }
            case 105: {
                this.mISTARGETCELLFREE();
                break;
            }
            case 106: {
                this.mIS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE();
                break;
            }
            case 107: {
                this.mISCARRIED();
                break;
            }
            case 108: {
                this.mISCARRYING();
                break;
            }
            case 109: {
                this.mHASAVAILABLECREATUREINSYMBIOT();
                break;
            }
            case 110: {
                this.mSUMMONSLEADERSHIPSCORE();
                break;
            }
            case 111: {
                this.mLEADERSHIPFORCURRENTINVOC();
                break;
            }
            case 112: {
                this.mGETTERRITORYNATIONID();
                break;
            }
            case 113: {
                this.mISOWNSUMMON();
                break;
            }
            case 114: {
                this.mGETCHARACTERDIRECTION();
                break;
            }
            case 115: {
                this.mGETCRAFTLEARNINGITEM();
                break;
            }
            case 116: {
                this.mHASCRAFT();
                break;
            }
            case 117: {
                this.mGETCRAFTLEVEL();
                break;
            }
            case 118: {
                this.mHASEMOTE();
                break;
            }
            case 119: {
                this.mISPASSEPORTACTIVE();
                break;
            }
            case 120: {
                this.mCANBECOMESOLDIERORMILITIAMAN();
                break;
            }
            case 121: {
                this.mGETTITLE();
                break;
            }
            case 122: {
                this.mGETNATIONRANK();
                break;
            }
            case 123: {
                this.mISEQUIPPEDWITHSET();
                break;
            }
            case 124: {
                this.mISHOUR();
                break;
            }
            case 125: {
                this.mISOWNHOUR();
                break;
            }
            case 126: {
                this.mISBOMB();
                break;
            }
            case 127: {
                this.mISOWNBOMB();
                break;
            }
            case 128: {
                this.mISTUNNEL();
                break;
            }
            case 129: {
                this.mISOWNDEPOSIT();
                break;
            }
            case 130: {
                this.mISOWNBEACON();
                break;
            }
            case 131: {
                this.mISOWNSPECIFICAREA();
                break;
            }
            case 132: {
                this.mHASSUMMONWITHBREED();
                break;
            }
            case 133: {
                this.mNBBOMB();
                break;
            }
            case 134: {
                this.mISACHIEVEMENTFAILED();
                break;
            }
            case 135: {
                this.mISACHIEVEMENTRUNNING();
                break;
            }
            case 136: {
                this.mISACHIEVEMENTOBJECTIVECOMPLETE();
                break;
            }
            case 137: {
                this.mISACHIEVEMENTREPEATABLE();
                break;
            }
            case 138: {
                this.mCANRESETACHIEVEMENT();
                break;
            }
            case 139: {
                this.mOPPONENTSCONTAINSNATIONENEMY();
                break;
            }
            case 140: {
                this.mHASNATIONJOB();
                break;
            }
            case 141: {
                this.mISACHIEVEMENTCOMPLETE();
                break;
            }
            case 142: {
                this.mISACHIEVEMENTACTIVE();
                break;
            }
            case 143: {
                this.mISPROTECTORINFIGHT();
                break;
            }
            case 144: {
                this.mISOFFPLAY();
                break;
            }
            case 145: {
                this.mISINGROUP();
                break;
            }
            case 146: {
                this.mISACTIVATEDBYELEMENT();
                break;
            }
            case 147: {
                this.mISACTIVATEDBYSPELL();
                break;
            }
            case 148: {
                this.mGET_ACTIVE_SPELL_ID();
                break;
            }
            case 149: {
                this.mISONSPECIFICEFFECTAREA();
                break;
            }
            case 150: {
                this.mISONSPECIFICEFFECTAREAWITHSPECIFICSTATE();
                break;
            }
            case 151: {
                this.mISONEFFECTAREATYPE();
                break;
            }
            case 152: {
                this.mISOWNGLYPH();
                break;
            }
            case 153: {
                this.mCELL_CONTAINS_SPECIFIC_EFFECT_AREA();
                break;
            }
            case 154: {
                this.mISDEPOSIT();
                break;
            }
            case 155: {
                this.mGETSTATECOUNTINRANGE();
                break;
            }
            case 156: {
                this.mISFLEEING();
                break;
            }
            case 157: {
                this.mISABANDONNING();
                break;
            }
            case 158: {
                this.mISNATIONFIRSTINDUNGEONLADDER();
                break;
            }
            case 159: {
                this.mGETFIGHTMODEL();
                break;
            }
            case 160: {
                this.mHAS_SURROUNDING_CELL_WITH_OWN_BARREL();
                break;
            }
            case 161: {
                this.mHAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA();
                break;
            }
            case 162: {
                this.mHAS_SURROUNDING_CELL_WITH_EFFECT_AREA();
                break;
            }
            case 163: {
                this.mIS_CARRYING_OWN_BARREL();
                break;
            }
            case 164: {
                this.mGET_TARGET_COUNT_IN_BEACON_AREA();
                break;
            }
            case 165: {
                this.mGET_FIGHTERS_WITH_BREED_IN_RANGE();
                break;
            }
            case 166: {
                this.mAI_HAS_CAST_SPELL();
                break;
            }
            case 167: {
                this.mAI_HAS_MOVED();
                break;
            }
            case 168: {
                this.mAI_GET_SPELL_CAST_COUNT();
                break;
            }
            case 169: {
                this.mGET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA();
                break;
            }
            case 170: {
                this.mIS_CONTROLLED_BY_AI();
                break;
            }
            case 171: {
                this.mIS_SUMMON();
                break;
            }
            case 172: {
                this.mIS_SUMMON_FROM_SYMBIOT();
                break;
            }
            case 173: {
                this.mGET_EFFECT_AREA_COUNT_IN_RANGE();
                break;
            }
            case 174: {
                this.mGET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE();
                break;
            }
            case 175: {
                this.mHAS_LINE_OF_SIGHT_FROM_ENEMY();
                break;
            }
            case 176: {
                this.mGET_ENEMIES_HUMAN_COUNT_IN_RANGE();
                break;
            }
            case 177: {
                this.mIS_TARGET_ON_SAME_TEAM();
                break;
            }
            case 178: {
                this.mHAS_LOOT();
                break;
            }
            case 179: {
                this.mHAS_EFFECT_WITH_ACTION_ID();
                break;
            }
            case 180: {
                this.mIS_CHARACTER();
                break;
            }
            case 181: {
                this.mHAS_STATE_FROM_LEVEL();
                break;
            }
            case 182: {
                this.mDOUBLE_OR_QUITS_CRITERION();
                break;
            }
            case 183: {
                this.mHAS_WEAPON_TYPE();
                break;
            }
            case 184: {
                this.mIS_OWN_AREA();
                break;
            }
            case 185: {
                this.mIS_ON_BORDER_CELL();
                break;
            }
            case 186: {
                this.mGETPROTECTORNATIONID();
                break;
            }
            case 187: {
                this.mNB_ROUBLABOT();
                break;
            }
            case 188: {
                this.mHAS_EFFECT_WITH_SPECIFIC_ID();
                break;
            }
            case 189: {
                this.mHAS_FECA_ARMOR();
                break;
            }
            case 190: {
                this.mIS_FECA_GLYPH_CENTER();
                break;
            }
            case 191: {
                this.mNB_FECA_GLYPH();
                break;
            }
            case 192: {
                this.mGETACHIEVEMENTVARIABLE();
                break;
            }
            case 193: {
                this.mGET_CHALLENGE_UNAVAILABILITY_DURATION();
                break;
            }
            case 194: {
                this.mGET_EFFECT_CASTER_ORIGINAL_CONTROLLER();
                break;
            }
            case 195: {
                this.mGET_EFFECT_TARGET_ORIGINAL_CONTROLLER();
                break;
            }
            case 196: {
                this.mIS_ORIGINAL_CONTROLLER();
                break;
            }
            case 197: {
                this.mCASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER();
                break;
            }
            case 198: {
                this.mIS_CHARACTERISTIC_FULL();
                break;
            }
            case 199: {
                this.mISACCOUNTSUBSCRIBED();
                break;
            }
            case 200: {
                this.mGET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER();
                break;
            }
            case 201: {
                this.mGET_TRIGGERING_EFFECT_CASTER();
                break;
            }
            case 202: {
                this.mIS_ON_OWN_DIAL();
                break;
            }
            case 203: {
                this.mNB_HYDRANDS();
                break;
            }
            case 204: {
                this.mNB_STEAMBOTS();
                break;
            }
            case 205: {
                this.mIS_OWN_FECA_GLYPH();
                break;
            }
            case 206: {
                this.mGET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER();
                break;
            }
            case 207: {
                this.mGET_FGHT_CURRENT_TABLE_TURN();
                break;
            }
            case 208: {
                this.mNB_ALL_AREAS();
                break;
            }
            case 209: {
                this.mNB_GLYPHS();
                break;
            }
            case 210: {
                this.mGET_TRIGGERING_EFFECT_TARGET_BREED_ID();
                break;
            }
            case 211: {
                this.mGET_TRIGGERING_ANCESTORS_COUNT();
                break;
            }
            case 212: {
                this.mGET_TRIGGERING_EFFECT_ID();
                break;
            }
            case 213: {
                this.mIS_SIDE_STABBED();
                break;
            }
            case 214: {
                this.mGET_TRIGGERING_EFFECT_TARGET();
                break;
            }
            case 215: {
                this.mHAS_LINE_OF_SIGHT_TO_ENEMY();
                break;
            }
            case 216: {
                this.mIS_PLAYER();
                break;
            }
            case 217: {
                this.mIS_COMPANION();
                break;
            }
            case 218: {
                this.mHAS_CASTER_FECA_ARMOR();
                break;
            }
            case 219: {
                this.mIS_CHALLENGER();
                break;
            }
            case 220: {
                this.mIS_CARRYING_OWN_BOMB();
                break;
            }
            case 221: {
                this.mHAS_SURROUNDING_CELL_WITH_OWN_SUMMON();
                break;
            }
            case 222: {
                this.mGET_GUILD_LEVEL();
                break;
            }
            case 223: {
                this.mIS_IN_GUILD();
                break;
            }
            case 224: {
                this.mGET_GUILD_PARTNER_COUNT_IN_FIGHT();
                break;
            }
            case 225: {
                this.mIS_IN_ALIGNMENT();
                break;
            }
            case 226: {
                this.mHAS_VALID_PATH_TO_TARGET();
                break;
            }
            case 227: {
                this.mIS_FREE_CELL();
                break;
            }
            case 228: {
                this.mHAS_BEEN_RAISED_BY_EFFECT();
                break;
            }
            case 229: {
                this.mGET_X();
                break;
            }
            case 230: {
                this.mGET_Y();
                break;
            }
            case 231: {
                this.mGET_Z();
                break;
            }
            case 232: {
                this.mNB_AREAS_WITH_BASE_ID();
                break;
            }
            case 233: {
                this.mGET_EFFECTS_COUNT_WITH_SPECIFIC_IDS();
                break;
            }
            case 234: {
                this.mGET_PARTITION_X();
                break;
            }
            case 235: {
                this.mGET_PARTITION_Y();
                break;
            }
            case 236: {
                this.mGET_TOTAL_HP_IN_PCT();
                break;
            }
            case 237: {
                this.mGET_PROTECTOR_CHALLENGE_KAMA_AMOUNT();
                break;
            }
            case 238: {
                this.mGET_ALLIES_COUNT();
                break;
            }
            case 239: {
                this.mIS_CELL_BEHIND_TARGET_FREE();
                break;
            }
            case 240: {
                this.mGET_STATE_LEVEL();
                break;
            }
            case 241: {
                this.mIS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA();
                break;
            }
            case 242: {
                this.mHAS_ANOTHER_SAME_EQUIPMENT();
                break;
            }
            case 243: {
                this.mIS_LOCKED();
                break;
            }
            case 244: {
                this.mHAS_STATE_FROM_USER();
                break;
            }
            case 245: {
                this.mGET_HUMAN_ALLIES_COUNT_IN_RANGE();
                break;
            }
            case 246: {
                this.mIS_CHARACTER_WITH_HIGHEST_STATE_LEVEL();
                break;
            }
            case 247: {
                this.mHAS_GUILD_BONUS();
                break;
            }
            case 248: {
                this.mISZONEINCHAOS();
                break;
            }
            case 249: {
                this.mGET_NEXT_FIGHTER_IN_TIMELINE();
                break;
            }
            case 250: {
                this.mIS_TRIGGERED_BY_ZONE_EFFECT();
                break;
            }
            case 251: {
                this.mGET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE();
                break;
            }
            case 252: {
                this.mGET_BOOLEAN_SYSTEM_CONFIGURATION();
                break;
            }
            case 253: {
                this.mGET_FIGHTERS_MAX_LEVEL();
                break;
            }
            case 254: {
                this.mGET_FIGHTERS_MIN_LEVEL();
                break;
            }
            case 255: {
                this.mGET_FIGHTERS_LEVEL_DIFF();
                break;
            }
            case 256: {
                this.mGET_FIGHTERS_LEVEL_SUM();
                break;
            }
            case 257: {
                this.mIS_PRELOADING();
                break;
            }
            case 258: {
                this.mHAS_UNLOCKED_COMPANION();
                break;
            }
            case 259: {
                this.mIS_OUT_OF_PLAY();
                break;
            }
            case 260: {
                this.mIS_IN_FIGHT();
                break;
            }
            case 261: {
                this.mIS_IN_PLAY();
                break;
            }
            case 262: {
                this.mIS_CASTER_FACING_FIGHTER();
                break;
            }
            case 263: {
                this.mGET_FIGHTER_ID();
                break;
            }
            case 264: {
                this.mGET_TRIGGERING_EFFECT_VALUE();
                break;
            }
            case 265: {
                this.mGET_OWN_ARMOR_COUNT();
                break;
            }
            case 266: {
                this.mGET_OWN_TEAM_STATE_COUNT_IN_RANGE();
                break;
            }
            case 267: {
                this.mGET_FIGHTERS_CHARACTERISTIC_MAX_VALUE();
                break;
            }
            case 268: {
                this.mHAS_SUBSCRIPTION_LEVEL();
                break;
            }
            case 269: {
                this.mIS_TRIGGERING_EFFECT_CRITICAL();
                break;
            }
            case 270: {
                this.mISPVP();
                break;
            }
            case 271: {
                this.mHASPVPRANK();
                break;
            }
            case 272: {
                this.mIS_ENNEMY_NATION();
                break;
            }
            case 273: {
                this.mIS_PVP_STATE_ACTIVE();
                break;
            }
            case 274: {
                this.mGETLASTINSTANCEID();
                break;
            }
            case 275: {
                this.mIS_HOSTILE();
                break;
            }
            case 276: {
                this.mHAS_BEEN_NAUGHTY();
                break;
            }
            case 277: {
                this.mNB_GATES();
                break;
            }
            case 278: {
                this.mGET_TEAM_EFFECT_AREA_COUNT_IN_RANGE();
                break;
            }
            case 279: {
                this.mHAS_VALID_GATE_FOR_TP();
                break;
            }
            case 280: {
                this.mUSE_GATE_EFFECT();
                break;
            }
            case 281: {
                this.mGET_CURRENT_FIGHTER_ID();
                break;
            }
            case 282: {
                this.mIS_HERO();
                break;
            }
            case 283: {
                this.mVIRGULE();
                break;
            }
            case 284: {
                this.mINTEGER();
                break;
            }
            case 285: {
                this.mFLOAT();
                break;
            }
            case 286: {
                this.mDIVIDE();
                break;
            }
            case 287: {
                this.mMOD();
                break;
            }
            case 288: {
                this.mMULT();
                break;
            }
            case 289: {
                this.mPLUS();
                break;
            }
            case 290: {
                this.mMINUS();
                break;
            }
            case 291: {
                this.mEOL();
                break;
            }
            case 292: {
                this.mWS();
                break;
            }
            case 293: {
                this.mPV();
                break;
            }
            case 294: {
                this.mSHARP();
                break;
            }
            case 295: {
                this.mAT();
                break;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)CritereLexer.class);
        DFA32_transitionS = new String[] { "\u00013\u00012\u0002\uffff\u00012\u0012\uffff\u00013\u0001\u0007\u0001\u0014\u00015\u0001\uffff\u0001/\u0001\u0004\u0001\uffff\u0001\u0013\u0001\u0001\u00010\u00011\u0001+\u0001,\u0001\u0010\u0001.\n-\u0001\uffff\u00014\u0001\b\u0001\u0012\u0001\u0011\u0001\uffff\u00016\u0001(\u0001$\u0001\u001c\u0001)\u0001%\u0001\u0018\u0001\u001e\u0001\u001b\u0001\u001d\u0003\uffff\u0001\u001f\u0001!\u0001&\u0001'\u0001\uffff\u0001 \u0001#\u0001\u0015\u0001*\u0001\u0016\u0001\"\u0003\uffff\u0001\u000e\u0001\uffff\u0001\u000f\u0003\uffff\u0001\u0003\u0003\u001a\u0001\u0002\u0001\u0019\u0002\u001a\u0001\n\u0004\u001a\u0001\t\u0001\u0005\u0004\u001a\u0001\u000b\u0001\u001a\u0001\u0017\u0004\u001a\u0001\f\u0001\u0006\u0001\r", "", "\u00018\u0007\uffff\u00017", "\u00019", "", "\u0001;\u0002\uffff\u0001:", "", "\u0001<", "\u0001>\u0001<", "\u0001@", "\u0001A", "\u0001B\t\uffff\u0001C", "", "", "", "", "\nE", "\u0001F", "\u0001H", "", "", "\u0001J", "", "\u0001K", "", "\u0001L", "", "\u0001M", "\u0001O\u0003\uffff\u0001P\u0002\uffff\u0001N", "\u0001Q", "\u0001R", "", "", "\u0001S", "", "\u0001T", "", "\u0001U\u0006\uffff\u0001V", "\u0001X\u0006\uffff\u0001W", "\u0001Y\u0006\uffff\u0001V", "\u0001Z", "", "", "", "\u0001E\u0001\uffff\n-", "\u0001E\u0001\uffff\n\\", "", "", "", "", "", "", "", "", "", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001^", "\u0001_", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "", "", "", "", "\u0001`\u0005\uffff\u0001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001c", "\u0001d", "", "", "", "", "", "", "\u0001e\u0013\uffff\u0001\u0016", "\u0001f", "\u0001g\b\uffff\u0001h", "\u0001i", "", "\u0001j\u0004\uffff\u0001k", "", "\u0001s\u0001o\u0001r\u0001n\u0001l\u0001t\u0001\uffff\u0001x\u0001y\u0002\uffff\u0001{\u0001q\u0001z\u0001v\u0001w\u0002\uffff\u0001m\u0001u\u0001p\u0004\uffff\u0001|", "\u0001}", "\u0001\u0084\u0001\u007f\u0003\uffff\u0001\u0082\u0001\u0085\u0001\u0083\t\uffff\u0001\u0081\u0001~\u0001e\u0002\uffff\u0001\"\u0001\u0080", "\u0001\u0086", "", "", "", "", "", "\u0001\u0088\u0001\u0087", "", "\u0001E\u0001\uffff\n\\", "", "\u0001\u0089", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "", "\u0001\u008a", "\u0001\u008b", "", "\u0001\u008c", "\u0001\u008d", "\u0001\u008e", "\u0001\u0095\u0001\u0099\u0001\u0096\u0001\uffff\u0001\u008f\u0001\u0091\u0001\u009a\u0004\uffff\u0001\u0092\u0001\uffff\u0001\u0097\u0001\uffff\u0001\u009c\u0002\uffff\u0001\u0090\u0001\uffff\u0001\u009b\u0001\u0098\u0001\u0093\u0016\uffff\u0001\u0094", "\u0001\u009e\u0001\u009d\u000e\uffff\u0001\u009f", "", "\u0001 \u0002\uffff\u0001¡", "\u0001¢\u0003\uffff\u0001¥\u0006\uffff\u0001£\u0004\uffff\u0001¤", "\u0001¦\u0003\uffff\u0001§", "\u0001©\r\uffff\u0001ª\u0002\uffff\u0001¨", "\u0001«", "", "\u0001\u00ad\u0003\uffff\u0001¯\u0002\uffff\u0001¬\u0006\uffff\u0001®", "\u0001²\u0001±\u0002\uffff\u0001°", "\u0001³\u0003\uffff\u0001µ\u0006\uffff\u0001´\u0005\uffff\u0001¶", "\u0001·\u0010\uffff\u0001¹\u0002\uffff\u0001¸", "\u0001»\u0007\uffff\u0001¼\u0003\uffff\u0001½\u0002\uffff\u0001¾\u0001\uffff\u0001º", "\u0001¿\n\uffff\u0001\u00c1\u0005\uffff\u0001\u00c0\u0003\uffff\u0001\u00c2", "\u0001\u00c4\t\uffff\u0001\u00c3", "\u0001\u00c5", "", "", "", "\u0001\u00d3\u0001\u00d5\u0001\u00c6\u0001\u00ce\u0001\u00cd\u0001\u00d6\u0001\u00d7\u0001\u00ca\u0001\u00cc\u0001\uffff\u0001\u00d0\u0001\u00d1\u0001\u00c7\u0001\u00d4\u0001\u00db\u0001\u00cf\u0001\uffff\u0001\u00d2\u0001\u00c8\u0001\u00cb\u0002\uffff\u0001\u00c9\u0001\u00d8\u0001\u00d9\u0001\u00da", "\u0001\u00dd\u0001\u00dc", "\u0001\u00de\u0003\uffff\u0001$\t\uffff\u0001\u00df", "", "", "", "", "\u0001\u00e0\u0005\uffff\u0001\u00e1", "\u0001\u00e3\n\uffff\u0001\u00e2", "\u0001\u00e4", "\u0001\u00e5", "", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u00e8", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u00eb\u0006\uffff\u0001\u00ea\u0003\uffff\u0001\u00e9", "\u0001\u00ed\u0001\u00ec", "\u0001\u00f0\u0003\uffff\u0001\u00ef\b\uffff\u0001\u00ee", "\u0001\u00f1\u0005\uffff\u0001\u00f2", "\u0001\u00f4\t\uffff\u0001\u00f3", "", "\u0001\u00f6\u0007\uffff\u0001\u00f5", "\u0001\u00f8\u0010\uffff\u0001\u00f7", "", "\u0001\u00f9", "\u0001\u00fa", "", "", "", "", "", "", "\u0001\u00fc\b\uffff\u0001\u00fb", "", "\u0001\u00fe\n\uffff\u0001\u00fd\u000b\uffff\u0001\u00ff", "\u0001\u0100", "\u0001\u0101", "", "", "\u0001\u0102\u000e\uffff\u0001\u0103", "\u0001\u0104", "", "", "\u0001\u0105", "\u0001\u0106", "\u0001\u0107\u0001\u0108", "\u0001\u010a\u0001\u0109", "", "", "\u0001\u010d\u0004\uffff\u0001\u010b\u000b\uffff\u0001\u010c", "", "", "", "", "", "\u0001\u010e", "", "\u0001\u010f", "\u0001\u0110", "", "\u0001\u0113\u0002\uffff\u0001\u0112\t\uffff\u0001\u0114\u0003\uffff\u0001\u0111", "", "", "", "\u0001\u0116\t\uffff\u0001\u0115", "", "\u0001\u0117", "\u0001\u0119\u0001\uffff\u0001\u0118", "", "\u0001\u011b\u0004\uffff\u0001\u011c\u0001\u011a\b\uffff\u0001\u011d", "\u0001\u011e\u0006\uffff\u0001\u011f\u0002\uffff\u0001\u0120\u0002\uffff\u0001\u0121", "\u0001\u0122", "\u0001\u0126\t\uffff\u0001\u0124\u0004\uffff\u0001\u0123\u0003\uffff\u0001\u0125", "\u0001\u0127", "\u0001\u0128\u0005\uffff\u0001\u0129", "\u0001\u012e\u0003\uffff\u0001\u012c\u0003\uffff\u0001\u012a\u0005\uffff\u0001\u012d\u0002\uffff\u0001\u012b", "\u0001\u0130(\uffff\u0001\u012f\u0005\uffff\u0001\u0131", "\u0001\u0132\u0007\uffff\u0001\u0133", "\u0001\u0136\u0003\uffff\u0001\u0135\u0003\uffff\u0001\u0134", "\u0001\u0139\r\uffff\u0001\u0137\u0002\uffff\u0001\u0138", "", "\u0001\u013c\u0003\uffff\u0001\u013a\t\uffff\u0001\u013b", "", "\u0001\u013e\b\uffff\u0001\u013d", "\u0001\u013f\u0003\uffff\u0001\u0140", "\u0001\u0141", "\u0001\u0142", "\u0001\u0143", "", "", "", "\u0001\u0144", "", "", "", "", "", "", "", "", "\u0001\u0145", "\u0001\u0146", "", "", "\u0001\u001a\u0005\uffff\u0001\u001a\u0006\uffff\u0001\u001a\u0001\uffff\u000b\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a\u0004\uffff\u0001\u001a\u0001\uffff\u001a\u001a", "\u0001\u0147", "", "\u0001\u0148", "\u0001\u014b\n\uffff\u0001\u0149\u0004\uffff\u0001\u014a", "\u0001\u014c", "\u0001\u014d", "", "", "\u0001\u014e", "", "", "", "", "", "", "", "\u0001\u014f", "\u0001\u0150", "\u0001\u0151", "", "", "", "", "\u0001\u0152", "\u0001\u0153", "", "", "\u0001\u0154", "\u0001\u0155", "\u0001\u0156\u0005\uffff\u0001\u0157", "\u0001\u0158", "", "", "", "\u0001\u0159", "\u0001\u015a", "", "\u0001\u015b", "\u0001\u015c", "\u0001\u0162\u0001\u015f\u0001\uffff\u0001\u0160\u0001\uffff\u0001\u0163\u0001\u0161\u0001\u015e\n\uffff\u0001\u015d", "\u0001\u0164", "", "", "\u0001\u0166\u0004\uffff\u0001\u0165", "", "", "\u0001\u0167", "", "", "\u0001\u0169\u0002\uffff\u0001\u016a", "", "", "", "\u0001\u016b", "", "\u0001\u016d\u0007\uffff\u0001\u016c", "\u0001\u016e", "\u0001\u016f", "\u0001 \u0003\uffff\u0001\u0170", "", "\u0001\u0171", "", "\u0001\u0173\u0001\u0172", "", "", "\u0001\u0128\u0006\uffff\u0001\u0174", "\u0001\u0175", "\u0001\u0176\u0010\uffff\u0001\u0177", "\u0001\u0178", "\u0001\u0179", "", "", "", "\u0001\u017a", "\u0001\u017c\b\uffff\u0001\u017b", "\u0001\u017d", "", "", "", "\u0001\u017e", "\u0001\u017f", "", "", "", "\u0001\u0180", "\u0001\u0182\u000b\uffff\u0001\u0181", "\u0001\u0183", "", "\u0001\u0184", "\u0001\u0185", "\u0001\u0186", "\u0001\u0187", "\u0001\u0188", "\u0001\u0189\t\uffff\u0001\u018a", "\u0001\u018b", "\u0001\u018c", "\u0001\u018d", "\u0001\u018e", "", "\u0001\u018f", "\u0001\u0190", "\u0001\u0191", "\u0001\u0192", "\u0001\u0193", "\u0001\u0194", "\u0001\u0195", "\u0001\u0196", "\u0001\u0197", "\u0001\u0198\u0010\uffff\u0001\u0199", "\u0001\u019a", "\u0001\u019b", "\u0001\u019c\u000f\uffff\u0001\u019d", "\u0001\u019e", "\u0001\u019f", "\u0001\u01a0", "\u0001\u01a1", "\u0001\u01a3\u0004\uffff\u0001\u01a2", "", "\u0001\u01a5\t\uffff\u0001\u01a4", "", "", "", "", "\u0001\u01a6", "", "", "", "", "", "", "\u0001\u01a8\u0005\uffff\u0001\u01a7", "", "\u0001\u01a9", "\u0001\u01aa", "\u0001\u001f\u0001\u01ab", "\u0001\u01ad\b\uffff\u0001\u01ac", "\u0001\u01ae\u0001\u01af", "\u0001\u01b0", "", "", "\u0001\u01b1", "\u0001\u01b2", "\u0001\u01b3", "\u0001\u01b4", "\u0001\u01b5", "\u0001\u01b6", "", "", "\u0001\u01b7", "\u0001\u01b8", "\u0001\u01b9", "\u0001\u01ba", "", "", "\u0001\u01bb", "\u0001\u01bc", "\u0001\u01bd", "\u0001\u01be", "\u0001\u01bf\u0012\uffff\u0001\u01c0", "\u0001\u00dc1\uffff\u0001\u01c1", "", "", "\u0001\u01c2", "\u0001\u01c3", "\u0001\u01c4", "\u0001\u01c5", "\u0001\u01c6", "\u0001\u01c7\u000f\uffff\u0001\u01c8", "\u0001\u01c9", "\u0001\u01ca", "\u0001\u01cc\u0003\uffff\u0001\u01cb", "\u0001\u01cd", "\u0001\u01ce", "\u0001\u01cf", "\u0001\u01d1\u0002\uffff\u0001\u01d0", "", "", "\u0001\u01d3", "\u0001\u01d4", "", "\u0001\u01d5", "\u0001\u01d6", "\u0001\u01d7", "\u0001\u01d8", "\u0001\u01d9", "", "", "", "", "\u0001\u01da", "\u0001\u01db", "", "\u0001\u01dc", "\u0001\u01dd", "", "\u0001\u01de", "", "", "\u0001\u01df", "\u0001\"\u0001\uffff\u0001\u01e0", "\u0001\u01e1", "\u0001\u01e3\u0003\uffff\u0001\u01e2", "\u0001\u01e4", "\u0001\u01e5", "\u0001\u01e6", "\u0001\u01e7", "\u0001\u01e8", "\u0001\u01e9", "\u0001\u01ea", "\u0001\u01eb", "\u0001\u01ec\u0006\uffff\u0001\u01ed", "\u0001\u01ee", "\u0001\u01ef", "\u0001\u01f0", "", "", "", "\u0001\u01f1", "\u0001\u01f2", "\u0001\u01f3", "\u0001\u01f4", "\u0001\u01f5", "", "", "\u0001\u01f7", "\u0001\u01f9\b\uffff\u0001\u01f8", "", "", "\u0001\u01fa", "\u0001\u01fb", "\u0001\u01fc", "", "", "", "\u0001\u01fe", "\u0001\u0200-\uffff\u0001\u01ff", "\u0001\u0201", "\u0001\u0202", "\u0001\u0203", "\u0001\u0204\u0004\uffff\u0001\u0118\u0006\uffff\u0001\u0205", "\u0001\u0206", "\u0001\u0207", "\u0001\u0208", "\u0001\u0209", "\u0001\u020a", "\u0001\u020b\u0007\uffff\u0001\u020c", "\u0001\u020d\b\uffff\u0001\u020e", "", "\u0001\u020f", "", "", "\u0001\u0210", "\u0001\u0212\u0003\uffff\u0001\u0211", "\u0001\u0213", "\u0001\u0214", "\u0001\u0215", "\u0001\u0216", "\u0001\u0217", "\u0001\u0218", "\u0001\u0219", "", "\u0001\u021a", "\u0001\u021d\t\uffff\u0001\u021b\u0017\uffff\u0001\u021c", "\u0001\u021e\u0003\uffff\u0001\u021f", "\u0001\u0220", "\u0001\u0221", "\u0001\u0222", "\u0001\u0223", "\u0001\u0224", "", "\u0001\u0225", "", "", "", "\u0001\u0226", "", "", "\u0001\u0227", "\u0001\u0228", "", "\u0001\u0229", "\u0001\u022a", "\u0001\u022b", "\u0001\u022c", "", "\u0001\u022d\u0003\uffff\u0001\u022e", "\u0001\u022f", "\u0001\u0231\u0003\uffff\u0001\u0232\u0002\uffff\u0001\u0231#\uffff\u0001\u0230", "\u0001\u0234", "\u0001\u0235", "", "", "", "", "\u0001\u0236", "\u0001\u0237", "", "", "\u0001\u0238/\uffff\u0001\u0239", "\u0001\u023c\u0001\uffff\u0001\u023a\u0010\uffff\u0001\u023b\u001e\uffff\u0001\u023d", "\u0001\u023e", "\u0001\u023f", "\u0001\u0240", "\u0001\u0241", "\u0001\u0243\u0007\uffff\u0001\u0242\b\uffff\u0001\u0244", "\u0001\u0245", "", "\u0001\u0246", "", "", "", "\u0001\u0247", "\u0001\u0248", "", "\u0001\u0249", "\u0001\u024a", "\u0001\u024b", "\u0001\u024c", "\u0001\u024e", "\u0001\u024f", "\u0001\u0250", "\u0001\u0252", "\u0001\u0253", "\u0001\u0254", "", "", "\u0001\u0255", "\u0001\u0256", "", "", "", "\u0001\u0257\u0014\uffff\u0001\u0258", "\u0001\u025a\t\uffff\u0001\u0259", "\u0001\u025b\u0003\uffff\u0001\u025c", "\u0001\u025d", "", "", "\u0001\u025e", "\u0001\u025f", "\u0001\u0260", "", "\u0001\u0261", "\u0001\u0262", "\u0001\u0263", "\u0001\u0264", "", "", "", "\u0001\u0266\u0002\uffff\u0001\u0265", "\u0001\u0268)\uffff\u0001\u0267", "\u0001\u0269", "\u0001\u026a", "\u0001\u026b", "\u0001\u026c", "\u0001\u026d", "\u0001\u026e", "", "\u0001\u026f\u001c\uffff\u0001\u0270", "\u0001\u0271\u0011\uffff\u0001\u0200", "\u0001\u0273", "", "\u0001\u0274", "\u0001\u0275", "\u0001\u0276", "\u0001\u0277", "\u0001\u0278", "", "", "", "", "", "\u0001\u0279", "\u0001\u027a", "\u0001\u027b", "\u0001\u027c", "\u0001\u027d", "\u0001\u027e", "\u0001\u027f", "\u0001\u0280", "\u0001\u0281", "", "", "\u0001\u0285\b\uffff\u0001\u0284\u0001\u0283\t\uffff\u0001\u0282", "", "\u0001\u0287\n\uffff\u0001\u0286", "\u0001\u0288", "\u0001\u0289", "\u0001\u028a\b\uffff\u0001\u028b", "\u0001\u028c", "\u0001\u028d", "", "", "", "", "\u0001\u028e", "\u0001\u028f", "\u0001\u0290", "\u0001\u0291\u000f\uffff\u0001\u0292", "\u0001\u0293", "\u0001\u0296\u0004\uffff\u0001\u0295\u001f\uffff\u0001\u0294", "\u0001\u0297", "\u0001\u0298\u0004\uffff\u0001\u0299", "\u0001\u029a", "\u0001\u029b", "\u0001\u029c", "\u0001\u029d", "\u0001\u029f\n\uffff\u0001\u029e", "\u0001\u02a0\u0001\u02a1", "\u0001\u02a2", "", "\u0001\u02a3\u0007\uffff\u0001\u02a4", "\u0001\u02a5", "", "", "", "\u0001\u02a6\u0011\uffff\u0001\u02a7", "\u0001\u02a8", "", "", "\u0001\u02a9", "\u0001\u02aa", "\u0001\u02ab", "\u0001\u02b0\u0001\uffff\u0001\u02af\u0002\uffff\u0001\u02ac\b\uffff\u0001\u02ae\u0002\uffff\u0001\u02ad", "\u0001\u02b1\r\uffff\u0001\u02b2", "", "", "\u0001\u02b3", "\u0001\u02b4", "", "", "\u0001\u02b6\u0003\uffff\u0001\u02b5", "", "", "\u0001\u02b7", "\u0001\u02b8", "\u0001\u02b9", "\u0001\u02ba", "", "", "", "", "\u0001\u02bb", "", "", "\u0001\u02bc", "", "", "\u0001\u02bd", "\u0001\u02be\r\uffff\u0001\u02bf", "\u0001\u02c1", "\u0001\u02c2\r\uffff\u0001\u02c3", "", "\u0001\u02c5\u000f\uffff\u0001\u02c4", "", "", "", "", "", "\u0001\u02c6", "\u0001\u02c7", "\u0001\u02c8", "", "\u0001\u02c9", "\u0001\u02ca", "\u0001\u02cb", "\u0001\u02cc", "\u0001\u02cd", "\u0001\u02cf", "\u0001\u02d0", "", "", "", "", "", "", "", "", "\u0001\u02d1", "\u0001\u02d2", "\u0001\u02d3", "\u0001\u02d4", "\u0001\u02d6", "\u0001\u02d8", "\u0001\u02d9", "", "", "\u0001\u02da", "\u0001\u02db", "\u0001\u02dc", "\u0001\u02dd", "\u0001\u02de", "", "", "", "", "\u0001\u02df", "\u0001\u02e0", "\u0001\u02e1\u000e\uffff\u0001\u02e2", "\u0001\u02e3", "\u0001\u02e4", "\u0001\u0232\u0002\uffff\u0001\u0231", "\u0001\u02e5", "\u0001\u02e6", "\u0001\u02e7", "", "", "\u0001\u02e8", "\u0001\u02e9", "\u0001\u02ea", "\u0001\u02eb", "\u0001\u02ec\u0010\uffff\u0001\u02ed", "\u0001\u02ee", "\u0001\u02ef", "\u0001\u02f0\u0005\uffff\u0001\u02f2\n\uffff\u0001\u02f1\u0001\uffff\u0001\u02f3", "\u0001\u02f4", "", "\u0001\u02f5", "\u0001\u02f6", "\u0001\u02f7", "\u0001\u02f8", "\u0001\u02f9", "", "", "\u0001\u02fa", "\u0001\u02fb", "\u0001\u02fc", "\u0001\u02fd", "\u0001\u02fe", "\u0001\u02ff", "\u0001\u0300\u0013\uffff\u0001\u0301", "\u0001\u0302", "\u0001\u0304\t\uffff\u0001\u0303", "\u0001\u0305", "\u0001\u0307", "\u0001\u0308", "", "", "\u0001\u0309", "\u0001\u030a", "", "", "", "\u0001\u030b", "\u0001\u030c", "\u0001\u030d", "\u0001\u030e", "\u0001\u030f", "\u0001\u0310", "\u0001\u0311", "\u0001\u0312\u0002\uffff\u0001\u0313\r\uffff\u0001\u0314", "\u0001\u0315", "\u0001\u0317", "\u0001\u0319", "", "", "", "", "", "", "", "\u0001\u031a", "\u0001\u031c\b\uffff\u0001\u031b", "", "" };
        DFA32_eot = DFA.unpackEncodedString("\u0002\uffff\u0002\u001a\u0001\uffff\u0001\u001a\u0001\uffff\u0001=\u0001?\u0003\u001a\u0004\uffff\u0001D\u0001G\u0001I\u0004\uffff\u0001\u001a\u0001\uffff\u0001\u001a\u0012\uffff\u0001[\u0001]\t\uffff\u0001\u0004\u0002\u001a\u0002\u0006\u0004\uffff\u0001\u001a\u0001b\u0002\u001a\u0007\uffff\u0002\u001a\u000f\uffff\u0001]\u0001\uffff\u0001\u001a\u0001\u0004\u0002=\u0001\uffff\u0002\u001a\u0001\uffff\u0003\u001a \uffff\u0001\u00e6\u0001\u00e7\u0002\u0016\u0001\u001a\u0001\u0018Y\uffff\u0001\u0018.\uffff\u0001\u0168\u007f\uffff\u0001\u01d2.\uffff\u0001\u01f6\u0006\uffff\u0001\u00fc\u0001\uffff\u0001\u01fd#\uffff\u0001N\u0014\uffff\u0001\u0233\u001d\uffff\u0001\u024d\u0002\uffff\u0001\u0251%\uffff\u0001\u0272Y\uffff\u0001\u02c0\u0001\u024d\u0010\uffff\u0001\u02ce\r\uffff\u0001\u02d5\u0001\u02d7\u0012\uffff\u0001\u0233\u001f\uffff\u0001\u0306\u0011\uffff\u0001\u0316\u0001\u0318\f\uffff");
        DFA32_eof = DFA.unpackEncodedString("\u031d\uffff");
        DFA32_min = DFA.unpackEncodedStringToUnsignedChars("\u0001\t\u0001\uffff\u0001l\u0001n\u0001\uffff\u0001r\u0001\uffff\u0002=\u0001o\u0001f\u0001h\u0004\uffff\u00010\u0002=\u0002\uffff\u0001r\u0001\uffff\u0001r\u0001\uffff\u0001a\u0001\uffff\u0002a\u0001s\u0001e\u0002\uffff\u0001b\u0001\uffff\u0001u\u0001\uffff\u0001f\u0001p\u0001e\u0001I\u0003\uffff\u0002.\t\uffff\u0001!\u0001s\u0001d\u0002!\u0004\uffff\u0001n\u0001!\u0001e\u0001u\u0006\uffff\u0002a\u0001l\u0001s\u0001\uffff\u0001n\u0001\uffff\u0001A\u0001t\u0001A\u0001m\u0005\uffff\u0001G\u0001\uffff\u0001.\u0001\uffff\u0001e\u0003!\u0001\uffff\u0001n\u0001e\u0001\uffff\u0001i\u0001s\u0001x\u0001A\u0001B\u0001\uffff\u0001n\u0001e\u0002a\u0001n\u0001\uffff\u0001a\u0001b\u0002a\u0001f\u0001a\u0001e\u0001n\u0003\uffff\u0001A\u0001t\u0001a\u0004\uffff\u0001l\u0001a\u0001m\u0001a\u0001\uffff\u0004!\u0001e\u0001!\u0001f\u0001t\u0001e\u0001i\u0001e\u0001\uffff\u0001n\u0001a\u0001\uffff\u0001a\u0001e\u0006\uffff\u0001e\u0001\uffff\u0001a\u0001e\u0001m\u0002\uffff\u0001a\u0001e\u0002\uffff\u0001d\u0001a\u0001r\u0001m\u0002\uffff\u0001c\u0005\uffff\u0001r\u0001\uffff\u0001i\u0001n\u0001\uffff\u0001B\u0003\uffff\u0001e\u0001\uffff\u0001p\u0001s\u0001\uffff\u0001A\u0001h\u0001o\u0002a\u0001o\u0001a\u0001E\u0001f\u0002a\u0001\uffff\u0001a\u0001\uffff\u0001c\u0001a\u0001o\u0001i\u0001u\u0003\uffff\u0001w\b\uffff\u0001o\u0001s\u0002\uffff\u0001!\u0001u\u0001\uffff\u0001f\u0001b\u0001a\u0001e\u0002\uffff\u0001n\u0007\uffff\u0001l\u0002e\u0004\uffff\u0001c\u0001m\u0002\uffff\u0002e\u0001l\u0001r\u0003\uffff\u0002i\u0001\uffff\u0002g\u0001A\u0001p\u0002\uffff\u0001r\u0002\uffff\u0001S\u0002\uffff\u0001r\u0003\uffff\u0001a\u0001\uffff\u0001a\u0001r\u0001n\u0001a\u0001\uffff\u0001a\u0001\uffff\u0001k\u0002\uffff\u0001m\u0001i\u0001a\u0001t\u0001r\u0003\uffff\u0001f\u0001e\u0001s\u0003\uffff\u0001o\u0001r\u0003\uffff\u0001l\u0001h\u0001t\u0001\uffff\u0001o\u0001g\u0001i\u0002n\u0001C\u0001i\u0001e\u0001m\u0001r\u0001\uffff\u0001t\u0002e\u0001i\u0001n\u0001m\u0001i\u0001o\u0001d\u0001a\u0001l\u0001a\u0001i\u0001e\u0001v\u0001e\u0001g\u0001p\u0001\uffff\u0001e\u0004\uffff\u0001e\u0006\uffff\u0001l\u0001\uffff\u0001f\u0001r\u0001s\u0001c\u0001s\u0001l\u0002\uffff\u0001g\u0001m\u0001r\u0001a\u0001g\u0001e\u0002\uffff\u0003t\u0001i\u0002\uffff\u0001i\u0001l\u0001h\u0001l\u0002A\u0002\uffff\u0001p\u0001c\u0002o\u0001e\u0001C\u0001O\u0001d\u0001N\u0001y\u0001f\u0001n\u0001F\u0002\uffff\u0001e\u0001c\u0001\uffff\u0001i\u0001v\u0001a\u0001t\u0001e\u0004\uffff\u0001c\u0001a\u0001\uffff\u0001t\u0001e\u0001\uffff\u0001l\u0002\uffff\u0001e\u0001A\u0001g\u0001E\u0001i\u0001l\u0001e\u0001c\u0001a\u0001e\u0001i\u0001e\u0001o\u0001e\u0001t\u0001d\u0003\uffff\u0001m\u0001t\u0001n\u0001u\u0001F\u0002\uffff\u0001f\u0001G\u0002\uffff\u0001N\u0001i\u0001e\u0003\uffff\u0001n\u0001F\u0001n\u0001e\u0001t\u0001C\u0001r\u0001i\u0001c\u0001L\u0001n\u0001L\u0001C\u0001\uffff\u0001e\u0002\uffff\u0001t\u0001H\u0002t\u0001n\u0001c\u0001t\u0001s\u0001n\u0001\uffff\u0001a\u0001C\u0001L\u0001e\u0002W\u0001n\u0001r\u0001\uffff\u0001S\u0003\uffff\u0001c\u0002\uffff\u0001g\u0001e\u0001\uffff\u0001g\u0001m\u0002e\u0001\uffff\u0001e\u0001f\u0001I\u0001e\u0001t\u0004\uffff\u0001r\u0001o\u0002\uffff\u0001C\u0001A\u0001c\u0001t\u0001i\u0001C\u0001A\u0001n\u0001\uffff\u0001r\u0003\uffff\u0001n\u0001i\u0001\uffff\u0001d\u0001o\u0001i\u0001A\u0001e\u0001r\u0001O\u0001e\u0001d\u0001l\u0002\uffff\u0001i\u0001e\u0003\uffff\u0001a\u0001F\u0001e\u0001r\u0002\uffff\u0002a\u0001r\u0001\uffff\u0001e\u0003o\u0003\uffff\u0001S\u0001I\u0002t\u0001i\u0001m\u0001g\u0001r\u0001\uffff\u0001U\u0001W\u0001w\u0001\uffff\u0001n\u0001B\u0001l\u0001c\u0001r\u0005\uffff\u0001n\u0001y\u0001s\u0001r\u0001e\u0001B\u0001r\u0001n\u0001u\u0002\uffff\u0001C\u0001\uffff\u0001I\u0001h\u0001n\u0001L\u0001h\u0001e\u0004\uffff\u0001n\u0001t\u0001y\u0001F\u0001E\u0001D\u0001g\u0001I\u0001t\u0001g\u0001a\u0001e\u0001C\u0001X\u0001n\u0001\uffff\u0001a\u0001e\u0003\uffff\u0001A\u0001g\u0002\uffff\u0001t\u0001a\u0001B\u0001A\u0001E\u0002\uffff\u0001f\u0001s\u0002\uffff\u0001A\u0002\uffff\u0002e\u0001C\u0001t\u0004\uffff\u0001t\u0002\uffff\u0001v\u0002\uffff\u0001C\u0001F\u0001W\u0001a\u0001\uffff\u0001e\u0005\uffff\u0001f\u0001t\u0001f\u0001\uffff\u0001r\u0001t\u0001o\u0001w\u0001I\u0002e\b\uffff\u0001e\u0001i\u0001f\u0002O\u0001u\u0001e\u0002\uffff\u0002l\u0002c\u0001e\u0004\uffff\u0001n\u0001e\u0001D\u0001l\u0001t\u0001M\u0001c\u0001t\u0001n\u0002\uffff\u0001W\u0001A\u0001t\u0001I\u0001C\u0001i\u0001r\u0001C\u0001n\u0001\uffff\u0001a\u0001t\u0001e\u0002a\u0002\uffff\u0001R\u0001r\u0001h\u0001a\u0001s\u0001r\u0001a\u0001g\u0001E\u0001W\u0001t\u0001g\u0002\uffff\u0001e\u0001w\u0003\uffff\u0002e\u0001t\u0001n\u0001r\u0001t\u0001A\u0001B\u0001I\u0001B\u0001n\u0007\uffff\u0001d\u0001E\u0002\uffff");
        DFA32_max = DFA.unpackEncodedStringToUnsignedChars("\u0001}\u0001\uffff\u0001t\u0001n\u0001\uffff\u0001u\u0001\uffff\u0001=\u0001>\u0001o\u0001f\u0001r\u0004\uffff\u00019\u0002=\u0002\uffff\u0001r\u0001\uffff\u0001r\u0001\uffff\u0001a\u0001\uffff\u0001a\u0001h\u0001s\u0001e\u0002\uffff\u0001b\u0001\uffff\u0001u\u0001\uffff\u0001m\u0001w\u0001l\u0001I\u0003\uffff\u00029\t\uffff\u0001z\u0001s\u0001d\u0002z\u0004\uffff\u0001t\u0001z\u0001e\u0001u\u0006\uffff\u0001u\u0001a\u0001u\u0001s\u0001\uffff\u0001s\u0001\uffff\u0001Z\u0001t\u0001X\u0001m\u0005\uffff\u0001H\u0001\uffff\u00019\u0001\uffff\u0001e\u0003z\u0001\uffff\u0001n\u0001e\u0001\uffff\u0001i\u0001s\u0001x\u0001n\u0001R\u0001\uffff\u0001q\u0001u\u0001e\u0001r\u0001n\u0001\uffff\u0001o\u0001f\u0001r\u0001u\u0001w\u0001v\u0001o\u0001n\u0003\uffff\u0001Z\u0001u\u0001o\u0004\uffff\u0001r\u0001l\u0001m\u0001a\u0001\uffff\u0004z\u0001e\u0001z\u0001q\u0001u\u0001r\u0002o\u0001\uffff\u0001v\u0001r\u0001\uffff\u0001a\u0001e\u0006\uffff\u0001n\u0001\uffff\u0001x\u0001e\u0001m\u0002\uffff\u0001p\u0001e\u0002\uffff\u0001d\u0001a\u0001s\u0001n\u0002\uffff\u0001t\u0005\uffff\u0001r\u0001\uffff\u0001i\u0001n\u0001\uffff\u0001S\u0003\uffff\u0001o\u0001\uffff\u0001p\u0001u\u0001\uffff\u0001P\u0001u\u0001o\u0001t\u0001a\u0001u\u0001r\u0001t\u0001n\u0001i\u0001r\u0001\uffff\u0001o\u0001\uffff\u0001l\u0001e\u0001o\u0001i\u0001u\u0003\uffff\u0001w\b\uffff\u0001o\u0001s\u0002\uffff\u0001z\u0001u\u0001\uffff\u0001f\u0001r\u0001a\u0001e\u0002\uffff\u0001n\u0007\uffff\u0001l\u0002e\u0004\uffff\u0001c\u0001m\u0002\uffff\u0002e\u0002r\u0003\uffff\u0002i\u0001\uffff\u0002g\u0001S\u0001p\u0002\uffff\u0001w\u0002\uffff\u0001S\u0002\uffff\u0001u\u0003\uffff\u0001a\u0001\uffff\u0001i\u0001r\u0001n\u0001e\u0001\uffff\u0001a\u0001\uffff\u0001l\u0002\uffff\u0001t\u0001i\u0001r\u0001t\u0001r\u0003\uffff\u0001f\u0001n\u0001s\u0003\uffff\u0001o\u0001r\u0003\uffff\u0001l\u0002t\u0001\uffff\u0001o\u0001g\u0001i\u0002n\u0001M\u0001i\u0001e\u0001m\u0001r\u0001\uffff\u0001t\u0002e\u0001i\u0001n\u0001m\u0001i\u0001o\u0001d\u0001r\u0001l\u0001a\u0001y\u0001e\u0001v\u0001e\u0001g\u0001u\u0001\uffff\u0001o\u0004\uffff\u0001e\u0006\uffff\u0001r\u0001\uffff\u0001f\u0001r\u0001t\u0001l\u0001t\u0001l\u0002\uffff\u0001g\u0001m\u0001r\u0001a\u0001g\u0001e\u0002\uffff\u0003t\u0001i\u0002\uffff\u0001i\u0001l\u0001h\u0001l\u0001T\u0001s\u0002\uffff\u0001p\u0001c\u0002o\u0001e\u0001S\u0001O\u0001d\u0001R\u0001y\u0001f\u0001n\u0001I\u0002\uffff\u0001e\u0001c\u0001\uffff\u0001i\u0001v\u0001a\u0001t\u0001e\u0004\uffff\u0001c\u0001a\u0001\uffff\u0001t\u0001e\u0001\uffff\u0001l\u0002\uffff\u0001e\u0001C\u0001g\u0001I\u0001i\u0001l\u0001e\u0001c\u0001a\u0001e\u0001i\u0001e\u0001v\u0001e\u0001t\u0001d\u0003\uffff\u0001m\u0001t\u0001n\u0001u\u0001F\u0002\uffff\u0001f\u0001P\u0002\uffff\u0001N\u0001i\u0001e\u0003\uffff\u0001n\u0001t\u0001n\u0001e\u0001t\u0001O\u0001r\u0001i\u0001c\u0001L\u0001n\u0001T\u0001L\u0001\uffff\u0001e\u0002\uffff\u0001t\u0001L\u0002t\u0001n\u0001c\u0001t\u0001s\u0001n\u0001\uffff\u0001a\u0001e\u0001P\u0001e\u0002W\u0001n\u0001r\u0001\uffff\u0001S\u0003\uffff\u0001c\u0002\uffff\u0001g\u0001e\u0001\uffff\u0001g\u0001m\u0002e\u0001\uffff\u0001i\u0001f\u0001t\u0001e\u0001t\u0004\uffff\u0001r\u0001o\u0002\uffff\u0002s\u0001c\u0001t\u0001i\u0001C\u0001R\u0001n\u0001\uffff\u0001r\u0003\uffff\u0001n\u0001i\u0001\uffff\u0001d\u0001o\u0001i\u0001A\u0001e\u0001r\u0001O\u0001e\u0001d\u0001l\u0002\uffff\u0001i\u0001e\u0003\uffff\u0001v\u0001P\u0001i\u0001r\u0002\uffff\u0002a\u0001r\u0001\uffff\u0001e\u0003o\u0003\uffff\u0001V\u0001s\u0002t\u0001i\u0001m\u0001g\u0001r\u0001\uffff\u0001r\u0001i\u0001w\u0001\uffff\u0001n\u0001B\u0001l\u0001c\u0001r\u0005\uffff\u0001n\u0001y\u0001s\u0001r\u0001e\u0001B\u0001r\u0001n\u0001u\u0002\uffff\u0001W\u0001\uffff\u0001T\u0001h\u0001n\u0001U\u0001h\u0001e\u0004\uffff\u0001n\u0001t\u0001y\u0001V\u0001E\u0001i\u0001g\u0001N\u0001t\u0001g\u0001a\u0001e\u0001N\u0001Y\u0001n\u0001\uffff\u0001i\u0001e\u0003\uffff\u0001S\u0001g\u0002\uffff\u0001t\u0001a\u0001B\u0001R\u0001S\u0002\uffff\u0001f\u0001s\u0002\uffff\u0001E\u0002\uffff\u0002e\u0001C\u0001t\u0004\uffff\u0001t\u0002\uffff\u0001v\u0002\uffff\u0001C\u0001T\u0001W\u0001o\u0001\uffff\u0001u\u0005\uffff\u0001f\u0001t\u0001f\u0001\uffff\u0001r\u0001t\u0001o\u0001w\u0001I\u0002e\b\uffff\u0001e\u0001i\u0001f\u0002O\u0001u\u0001e\u0002\uffff\u0002l\u0002c\u0001e\u0004\uffff\u0001n\u0001e\u0001S\u0001l\u0001t\u0001P\u0001c\u0001t\u0001n\u0002\uffff\u0001W\u0001A\u0001t\u0001I\u0001T\u0001i\u0001r\u0001V\u0001n\u0001\uffff\u0001a\u0001t\u0001e\u0002a\u0002\uffff\u0001R\u0001r\u0001h\u0001a\u0001s\u0001r\u0001u\u0001g\u0001O\u0001W\u0001t\u0001g\u0002\uffff\u0001e\u0001w\u0003\uffff\u0002e\u0001t\u0001n\u0001r\u0001t\u0001A\u0001S\u0001I\u0001B\u0001n\u0007\uffff\u0001d\u0001N\u0002\uffff");
        DFA32_accept = DFA.unpackEncodedString("\u0001\uffff\u0001\u0001\u0002\uffff\u0001\u0002\u0001\uffff\u0001\u0003\u0005\uffff\u0001\t\u0001\n\u0001\u000b\u0001\f\u0003\uffff\u0001\u0014\u0001\u0015\u0001\uffff\u0001\u0016\u0001\uffff\u0001\u0017\u0001\uffff\u0001\u0018\u0004\uffff\u0001!\u0001\"\u0001\uffff\u0001$\u0001\uffff\u0001&\u0004\uffff\u0001¶\u0001\u0118\u0001\u011b\u0002\uffff\u0001\u011e\u0001\u011f\u0001\u0120\u0001\u0121\u0001\u0123\u0001\u0124\u0001\u0125\u0001\u0126\u0001\u0127\u0005\uffff\u0001\u0004\u0001\u0005\u0001\u0010\u0001\u000e\u0004\uffff\u0001\r\u0001\u011d\u0001\u0011\u0001\u000f\u0001\u0013\u0001\u0012\u0004\uffff\u0001\u001b\u0001\uffff\u0001\u0099\u0004\uffff\u0001*\u0001G\u0001+\u0001\u008b\u0001@\u0001\uffff\u0001\u0122\u0001\uffff\u0001\u011c\u0004\uffff\u0001\u0006\u0002\uffff\u0001#\u0005\uffff\u0001\u00c5\u0005\uffff\u0001J\b\uffff\u0001\u009e\u0001\u00f3\u0001\u00f8\u0003\uffff\u0001(\u0001»\u0001¿\u0001\u00cb\u0004\uffff\u0001¨\u000b\uffff\u0001N\u0002\uffff\u0001\u008c\u0002\uffff\u0001\u00f7\u0001\u0102\u0001\u010f\u0001\u001d\u0001x\u0001\u008a\u0001\uffff\u0001{\u0003\uffff\u0001\u00d5\u0001/\u0002\uffff\u0001A\u0001~\u0004\uffff\u0001\u00ef\u0001T\u0001\uffff\u0001\u009d\u0001a\u0001\u009c\u0001¾\u0001\u00e3\u0001\uffff\u0001\u0080\u0002\uffff\u0001\u0090\u0001\uffff\u0001\u00c4\u0001\u0103\u0001w\u0001\uffff\u0001\u00d8\u0002\uffff\u0001\u011a\u000b\uffff\u0001I\u0001\uffff\u0001V\u0005\uffff\u0001\u00e5\u0001\u00e6\u0001\u00e7\u0001\uffff\u0001%\u0001\u00cc\u0001'\u0001\u0085\u0001\u00d0\u0001\u00e8\u0001\u00d1\u0001\u0115\u0002\uffff\u0001\b\u0001\u0007\u0002\uffff\u0001v\u0004\uffff\u0001L\u0001½\u0001\uffff\u0001²\u0001K\u0001·\u0001m\u0001\u00f2\u0001t\u0001\u00da\u0003\uffff\u0001\u001c\u0001)\u00010\u0001F\u0002\uffff\u0001c\u0001\u009a\u0004\uffff\u0001\u0106\u0001ª\u0001\u00d9\u0002\uffff\u0001\u00c7\u0004\uffff\u0001\u0097\u0001¹\u0001\uffff\u0001\u008f\u0001\u0101\u0001\uffff\u0001|\u0001\u0113\u0001\uffff\u0001\u00e1\u0001\u0104\u0001\u0105\u0001\uffff\u0001Y\u0004\uffff\u00015\u0001\uffff\u0001d\u0001\uffff\u0001.\u0001\u00f5\u0005\uffff\u00018\u0001E\u0001H\u0003\uffff\u0001Z\u0001`\u0001D\u0002\uffff\u0001O\u0001P\u0001\u0112\u0003\uffff\u0001\u00f9\n\uffff\u0001\u010c\u0012\uffff\u0001}\u0001\uffff\u0001\u0081\u0001\u0098\u0001¸\u0001\u00cd\u0001\uffff\u0001\u00ca\u0001\u00f1\u0001\u0111\u0001\u010e\u0001\u0091\u0001\u00df\u0001\uffff\u0001b\u0006\uffff\u0001U\u0001y\u0006\uffff\u0001W\u0001°\u0004\uffff\u0001\u0094\u0001\u00c0\u0006\uffff\u0001¦\u0001§\r\uffff\u00013\u0001S\u0002\uffff\u0001k\u0005\uffff\u0001q\u0001\u0083\u0001\u007f\u0001\u0082\u0002\uffff\u0001\u00c1\u0002\uffff\u0001M\u0001\uffff\u0001\u00fb\u0001_\u0010\uffff\u0001\u0109\u0001\u010a\u0001n\u0005\uffff\u0001?\u0001h\u0002\uffff\u0001\u00e4\u0001\u0114\u0003\uffff\u00011\u0001Q\u00012\r\uffff\u0001[\u0001\uffff\u0001<\u0001\u0116\t\uffff\u0001^\b\uffff\u00014\u0001\uffff\u0001\u00e2\u0001\u0117\u0001\u0110\u0001\uffff\u0001¬\u0001«\u0002\uffff\u0001\u00c6\u0004\uffff\u0001±\u0005\uffff\u00016\u00017\u0001\u009b\u0001\u00f0\u0002\uffff\u0001o\u0001\u00ec\b\uffff\u0001\u009f\u0001\uffff\u0001\u00cf\u0001\u00de\u0001\u00e0\u0002\uffff\u0001\u0084\n\uffff\u0001\u00fa\u0001\u010d\u0002\uffff\u0001\u001f\u0001 \u0001\u001e\u0004\uffff\u0001¤\u0001©\u0003\uffff\u0001\u00e9\u0004\uffff\u0001\\\u0001]\u0001z\b\uffff\u0001,\u0003\uffff\u0001l\u0005\uffff\u0001s\u0001u\u0001f\u0001\u0119\u0001;\t\uffff\u0001e\u0001\u00fc\u0001\uffff\u0001\u0107\u0006\uffff\u0001R\u0001\u00db\u0001\u00f6\u0001´\u000f\uffff\u0001¥\u0002\uffff\u0001\u010b\u0001\u0019\u0001\u001a\u0002\uffff\u0001µ\u0001\u00f4\u0005\uffff\u0001i\u0001j\u0002\uffff\u0001C\u0001r\u0001\uffff\u0001g\u0001p\u0004\uffff\u0001º\u0001\u00ed\u0001\u00ea\u0001\u00eb\u0001\uffff\u0001\u00fd\u0001\u00fe\u0001\uffff\u0001³\u0001¼\u0004\uffff\u0001\u0086\u0001\uffff\u0001\u0088\u0001\u008d\u0001\u008e\u0001\u0092\u0001\u0093\u0003\uffff\u0001\u00d3\u0007\uffff\u0001¯\u0001\u00d7\u0001B\u0001-\u0001£\u0001\u00dc\u0001\u0087\u0001\u0089\u0007\uffff\u0001X\u0001\u00ee\u0005\uffff\u0001\u00c2\u00019\u0001\u00c3\u0001:\t\uffff\u0001\u00ff\u0001\u0100\t\uffff\u0001=\u0005\uffff\u0001\u00d4\u0001\u0108\f\uffff\u0001\u00ad\u0001®\u0002\uffff\u0001¢\u0001\u0096\u0001\u0095\u000b\uffff\u0001 \u0001¡\u0001\u00dd\u0001\u00c8\u0001\u00c9\u0001\u00d2\u0001\u00d6\u0002\uffff\u0001>\u0001\u00ce");
        DFA32_special = DFA.unpackEncodedString("\u031d\uffff}>");
        final int numStates = CritereLexer.DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i = 0; i < numStates; ++i) {
            CritereLexer.DFA32_transition[i] = DFA.unpackEncodedString(CritereLexer.DFA32_transitionS[i]);
        }
    }
    
    protected class DFA32 extends DFA
    {
        public DFA32(final BaseRecognizer recognizer) {
            super();
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = CritereLexer.DFA32_eot;
            this.eof = CritereLexer.DFA32_eof;
            this.min = CritereLexer.DFA32_min;
            this.max = CritereLexer.DFA32_max;
            this.accept = CritereLexer.DFA32_accept;
            this.special = CritereLexer.DFA32_special;
            this.transition = CritereLexer.DFA32_transition;
        }
        
        public String getDescription() {
            return "1:1: Tokens : ( PD | AND | OR | NOT_EQUALS | NOT | IF | THEN | ELSE | AG | AD | BG | BD | POINT | INF | SUP | INFEQ | SUPEQ | ASSIGN | EQUALS | PG | STRING | TRUE | FALSE | VARNAME | HASEQTYPE | HASEQID | HASSUMMONS | ISENNEMY | CANCARRYTARGET | GETCHA | GETCHAPCT | GETCHAMAX | GETMONST | SPACEINSYMBIOT | TRAPAMOUNT | WALLAMOUNT | SUMMONAMOUNT | BEACONAMOUNT | BARRELAMOUNT | GET_XELOR_DIALS_COUNT | IS_SELECTED_CREATURE_AVAILABLE | EFFECTISFROMHEAL | OWNSBEACON | ISSPECIFICAREA | ISSPECIFICAREAWITHSPECIFICSTATE | GETTIME | ISDAY | ISSEASON | ISBREEDID | ISBREED | ISUNDEAD | HASSTATE | GETSKILLLEVEL | GETSPELLLEVEL | GETSPELLTREELEVEL | GETINSTANCEID | GETEFFECTCASTER | GETEFFECTTARGET | GETTRIGGEREREFFECTCASTER | GETTEAMID | GETDISTANCEBETWEENCASTERANDTARGET | GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON | HASFREECELLINEFFECTAREA | PETWITHINRANGE | ISBACKSTAB | HASLINEOFSIGHT | GETCHARACTERID | GETPOSITION | GETIEPOSITION | ISSEX | SLOTSINBAG | ITEMQUANTITY | GETKAMASCOUNT | ISMONSTERBREED | HASWORLDPROPERTY | HASFIGHTPROPERTY | GETMONTH | HASNTEVOLVEDSINCE | GETLEVEL | GETLOCKINCREMENT | ISBREEDFAMILY | ISCHALLENGEUSER | ISUNDERCONTROL | ISAFTER | GETWAKFUGAUGE | GETRANDOMNUMBER | GETENNEMYCOUNTINRANGE | GETALLIESCOUNTINRANGE | GETCONTROLLERINSAMETEAMCOUNTINRANGE | GETDESTRUCTIBLECOUNTINRANGE | GETWALLCOUNTINRANGE | GETNATIONID | GETNATIONALIGNMENT | GETNATIVENATIONID | GETSTASISGAUGE | GETDATE | ISFACESTABBED | GETCRIMESCORE | ISDEAD | GETSATISFACTIONLEVEL | GETBOOLEANVALUE | GETCURRENTPARTITIONNATIONID | GETTERRITORYID | HASFREESURROUNDINGCELL | ISTARGETCELLFREE | IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE | ISCARRIED | ISCARRYING | HASAVAILABLECREATUREINSYMBIOT | SUMMONSLEADERSHIPSCORE | LEADERSHIPFORCURRENTINVOC | GETTERRITORYNATIONID | ISOWNSUMMON | GETCHARACTERDIRECTION | GETCRAFTLEARNINGITEM | HASCRAFT | GETCRAFTLEVEL | HASEMOTE | ISPASSEPORTACTIVE | CANBECOMESOLDIERORMILITIAMAN | GETTITLE | GETNATIONRANK | ISEQUIPPEDWITHSET | ISHOUR | ISOWNHOUR | ISBOMB | ISOWNBOMB | ISTUNNEL | ISOWNDEPOSIT | ISOWNBEACON | ISOWNSPECIFICAREA | HASSUMMONWITHBREED | NBBOMB | ISACHIEVEMENTFAILED | ISACHIEVEMENTRUNNING | ISACHIEVEMENTOBJECTIVECOMPLETE | ISACHIEVEMENTREPEATABLE | CANRESETACHIEVEMENT | OPPONENTSCONTAINSNATIONENEMY | HASNATIONJOB | ISACHIEVEMENTCOMPLETE | ISACHIEVEMENTACTIVE | ISPROTECTORINFIGHT | ISOFFPLAY | ISINGROUP | ISACTIVATEDBYELEMENT | ISACTIVATEDBYSPELL | GET_ACTIVE_SPELL_ID | ISONSPECIFICEFFECTAREA | ISONSPECIFICEFFECTAREAWITHSPECIFICSTATE | ISONEFFECTAREATYPE | ISOWNGLYPH | CELL_CONTAINS_SPECIFIC_EFFECT_AREA | ISDEPOSIT | GETSTATECOUNTINRANGE | ISFLEEING | ISABANDONNING | ISNATIONFIRSTINDUNGEONLADDER | GETFIGHTMODEL | HAS_SURROUNDING_CELL_WITH_OWN_BARREL | HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA | HAS_SURROUNDING_CELL_WITH_EFFECT_AREA | IS_CARRYING_OWN_BARREL | GET_TARGET_COUNT_IN_BEACON_AREA | GET_FIGHTERS_WITH_BREED_IN_RANGE | AI_HAS_CAST_SPELL | AI_HAS_MOVED | AI_GET_SPELL_CAST_COUNT | GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA | IS_CONTROLLED_BY_AI | IS_SUMMON | IS_SUMMON_FROM_SYMBIOT | GET_EFFECT_AREA_COUNT_IN_RANGE | GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE | HAS_LINE_OF_SIGHT_FROM_ENEMY | GET_ENEMIES_HUMAN_COUNT_IN_RANGE | IS_TARGET_ON_SAME_TEAM | HAS_LOOT | HAS_EFFECT_WITH_ACTION_ID | IS_CHARACTER | HAS_STATE_FROM_LEVEL | DOUBLE_OR_QUITS_CRITERION | HAS_WEAPON_TYPE | IS_OWN_AREA | IS_ON_BORDER_CELL | GETPROTECTORNATIONID | NB_ROUBLABOT | HAS_EFFECT_WITH_SPECIFIC_ID | HAS_FECA_ARMOR | IS_FECA_GLYPH_CENTER | NB_FECA_GLYPH | GETACHIEVEMENTVARIABLE | GET_CHALLENGE_UNAVAILABILITY_DURATION | GET_EFFECT_CASTER_ORIGINAL_CONTROLLER | GET_EFFECT_TARGET_ORIGINAL_CONTROLLER | IS_ORIGINAL_CONTROLLER | CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER | IS_CHARACTERISTIC_FULL | ISACCOUNTSUBSCRIBED | GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER | GET_TRIGGERING_EFFECT_CASTER | IS_ON_OWN_DIAL | NB_HYDRANDS | NB_STEAMBOTS | IS_OWN_FECA_GLYPH | GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER | GET_FGHT_CURRENT_TABLE_TURN | NB_ALL_AREAS | NB_GLYPHS | GET_TRIGGERING_EFFECT_TARGET_BREED_ID | GET_TRIGGERING_ANCESTORS_COUNT | GET_TRIGGERING_EFFECT_ID | IS_SIDE_STABBED | GET_TRIGGERING_EFFECT_TARGET | HAS_LINE_OF_SIGHT_TO_ENEMY | IS_PLAYER | IS_COMPANION | HAS_CASTER_FECA_ARMOR | IS_CHALLENGER | IS_CARRYING_OWN_BOMB | HAS_SURROUNDING_CELL_WITH_OWN_SUMMON | GET_GUILD_LEVEL | IS_IN_GUILD | GET_GUILD_PARTNER_COUNT_IN_FIGHT | IS_IN_ALIGNMENT | HAS_VALID_PATH_TO_TARGET | IS_FREE_CELL | HAS_BEEN_RAISED_BY_EFFECT | GET_X | GET_Y | GET_Z | NB_AREAS_WITH_BASE_ID | GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS | GET_PARTITION_X | GET_PARTITION_Y | GET_TOTAL_HP_IN_PCT | GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT | GET_ALLIES_COUNT | IS_CELL_BEHIND_TARGET_FREE | GET_STATE_LEVEL | IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA | HAS_ANOTHER_SAME_EQUIPMENT | IS_LOCKED | HAS_STATE_FROM_USER | GET_HUMAN_ALLIES_COUNT_IN_RANGE | IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL | HAS_GUILD_BONUS | ISZONEINCHAOS | GET_NEXT_FIGHTER_IN_TIMELINE | IS_TRIGGERED_BY_ZONE_EFFECT | GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE | GET_BOOLEAN_SYSTEM_CONFIGURATION | GET_FIGHTERS_MAX_LEVEL | GET_FIGHTERS_MIN_LEVEL | GET_FIGHTERS_LEVEL_DIFF | GET_FIGHTERS_LEVEL_SUM | IS_PRELOADING | HAS_UNLOCKED_COMPANION | IS_OUT_OF_PLAY | IS_IN_FIGHT | IS_IN_PLAY | IS_CASTER_FACING_FIGHTER | GET_FIGHTER_ID | GET_TRIGGERING_EFFECT_VALUE | GET_OWN_ARMOR_COUNT | GET_OWN_TEAM_STATE_COUNT_IN_RANGE | GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE | HAS_SUBSCRIPTION_LEVEL | IS_TRIGGERING_EFFECT_CRITICAL | ISPVP | HASPVPRANK | IS_ENNEMY_NATION | IS_PVP_STATE_ACTIVE | GETLASTINSTANCEID | IS_HOSTILE | HAS_BEEN_NAUGHTY | NB_GATES | GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE | HAS_VALID_GATE_FOR_TP | USE_GATE_EFFECT | GET_CURRENT_FIGHTER_ID | IS_HERO | VIRGULE | INTEGER | FLOAT | DIVIDE | MOD | MULT | PLUS | MINUS | EOL | WS | PV | SHARP | AT );";
        }
    }
}
