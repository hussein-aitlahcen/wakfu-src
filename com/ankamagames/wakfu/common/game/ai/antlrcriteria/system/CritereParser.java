package com.ankamagames.wakfu.common.game.ai.antlrcriteria.system;

import org.apache.log4j.*;
import org.antlr.runtime.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class CritereParser extends Parser
{
    public static final String[] tokenNames;
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
    HashMap<String, ParserObject> mem;
    protected static final Logger m_logger;
    public static final BitSet FOLLOW_statement_in_critere47;
    public static final BitSet FOLLOW_statement_in_critere56;
    public static final BitSet FOLLOW_expr_in_statement79;
    public static final BitSet FOLLOW_set_in_statement82;
    public static final BitSet FOLLOW_EOL_in_statement90;
    public static final BitSet FOLLOW_VARNAME_in_statement99;
    public static final BitSet FOLLOW_ASSIGN_in_statement101;
    public static final BitSet FOLLOW_expr_in_statement107;
    public static final BitSet FOLLOW_set_in_statement109;
    public static final BitSet FOLLOW_EOL_in_statement117;
    public static final BitSet FOLLOW_factor_in_expr147;
    public static final BitSet FOLLOW_AND_in_expr164;
    public static final BitSet FOLLOW_expr_in_expr170;
    public static final BitSet FOLLOW_OR_in_expr187;
    public static final BitSet FOLLOW_expr_in_expr191;
    public static final BitSet FOLLOW_PLUS_in_expr199;
    public static final BitSet FOLLOW_expr_in_expr205;
    public static final BitSet FOLLOW_MINUS_in_expr214;
    public static final BitSet FOLLOW_expr_in_expr220;
    public static final BitSet FOLLOW_IF_in_expr230;
    public static final BitSet FOLLOW_PG_in_expr232;
    public static final BitSet FOLLOW_expr_in_expr236;
    public static final BitSet FOLLOW_PD_in_expr238;
    public static final BitSet FOLLOW_THEN_in_expr240;
    public static final BitSet FOLLOW_expr_in_expr246;
    public static final BitSet FOLLOW_ELSE_in_expr248;
    public static final BitSet FOLLOW_expr_in_expr252;
    public static final BitSet FOLLOW_expr_in_exprlist273;
    public static final BitSet FOLLOW_VIRGULE_in_exprlist278;
    public static final BitSet FOLLOW_expr_in_exprlist282;
    public static final BitSet FOLLOW_PG_in_paramlist297;
    public static final BitSet FOLLOW_exprlist_in_paramlist301;
    public static final BitSet FOLLOW_PD_in_paramlist305;
    public static final BitSet FOLLOW_PG_in_paramlist309;
    public static final BitSet FOLLOW_PD_in_paramlist311;
    public static final BitSet FOLLOW_BG_in_numberlist325;
    public static final BitSet FOLLOW_exprlist_in_numberlist329;
    public static final BitSet FOLLOW_BD_in_numberlist331;
    public static final BitSet FOLLOW_BG_in_numberlist337;
    public static final BitSet FOLLOW_BD_in_numberlist339;
    public static final BitSet FOLLOW_atoms_in_factor357;
    public static final BitSet FOLLOW_MULT_in_factor364;
    public static final BitSet FOLLOW_factor_in_factor369;
    public static final BitSet FOLLOW_DIVIDE_in_factor375;
    public static final BitSet FOLLOW_factor_in_factor381;
    public static final BitSet FOLLOW_MOD_in_factor388;
    public static final BitSet FOLLOW_factor_in_factor394;
    public static final BitSet FOLLOW_NOT_EQUALS_in_factor401;
    public static final BitSet FOLLOW_factor_in_factor407;
    public static final BitSet FOLLOW_EQUALS_in_factor414;
    public static final BitSet FOLLOW_factor_in_factor420;
    public static final BitSet FOLLOW_INF_in_factor427;
    public static final BitSet FOLLOW_factor_in_factor433;
    public static final BitSet FOLLOW_INFEQ_in_factor440;
    public static final BitSet FOLLOW_factor_in_factor446;
    public static final BitSet FOLLOW_SUP_in_factor453;
    public static final BitSet FOLLOW_factor_in_factor459;
    public static final BitSet FOLLOW_SUPEQ_in_factor466;
    public static final BitSet FOLLOW_factor_in_factor472;
    public static final BitSet FOLLOW_POINT_in_factor479;
    public static final BitSet FOLLOW_factor_in_factor485;
    public static final BitSet FOLLOW_HASEQTYPE_in_functioncall504;
    public static final BitSet FOLLOW_paramlist_in_functioncall508;
    public static final BitSet FOLLOW_HASEQID_in_functioncall514;
    public static final BitSet FOLLOW_paramlist_in_functioncall518;
    public static final BitSet FOLLOW_HASSUMMONS_in_functioncall524;
    public static final BitSet FOLLOW_paramlist_in_functioncall528;
    public static final BitSet FOLLOW_GETCHA_in_functioncall534;
    public static final BitSet FOLLOW_paramlist_in_functioncall538;
    public static final BitSet FOLLOW_GETCHAPCT_in_functioncall544;
    public static final BitSet FOLLOW_paramlist_in_functioncall548;
    public static final BitSet FOLLOW_GETCHAMAX_in_functioncall554;
    public static final BitSet FOLLOW_paramlist_in_functioncall558;
    public static final BitSet FOLLOW_ISENNEMY_in_functioncall564;
    public static final BitSet FOLLOW_paramlist_in_functioncall568;
    public static final BitSet FOLLOW_CANCARRYTARGET_in_functioncall574;
    public static final BitSet FOLLOW_paramlist_in_functioncall578;
    public static final BitSet FOLLOW_SPACEINSYMBIOT_in_functioncall584;
    public static final BitSet FOLLOW_paramlist_in_functioncall588;
    public static final BitSet FOLLOW_TRAPAMOUNT_in_functioncall594;
    public static final BitSet FOLLOW_paramlist_in_functioncall598;
    public static final BitSet FOLLOW_WALLAMOUNT_in_functioncall604;
    public static final BitSet FOLLOW_paramlist_in_functioncall608;
    public static final BitSet FOLLOW_IS_SELECTED_CREATURE_AVAILABLE_in_functioncall614;
    public static final BitSet FOLLOW_paramlist_in_functioncall618;
    public static final BitSet FOLLOW_OWNSBEACON_in_functioncall623;
    public static final BitSet FOLLOW_paramlist_in_functioncall627;
    public static final BitSet FOLLOW_ISSPECIFICAREA_in_functioncall633;
    public static final BitSet FOLLOW_paramlist_in_functioncall637;
    public static final BitSet FOLLOW_ISSPECIFICAREAWITHSPECIFICSTATE_in_functioncall643;
    public static final BitSet FOLLOW_paramlist_in_functioncall647;
    public static final BitSet FOLLOW_GETTIME_in_functioncall653;
    public static final BitSet FOLLOW_paramlist_in_functioncall659;
    public static final BitSet FOLLOW_ISDAY_in_functioncall665;
    public static final BitSet FOLLOW_paramlist_in_functioncall671;
    public static final BitSet FOLLOW_ISBREEDID_in_functioncall677;
    public static final BitSet FOLLOW_paramlist_in_functioncall681;
    public static final BitSet FOLLOW_ISBREED_in_functioncall686;
    public static final BitSet FOLLOW_paramlist_in_functioncall690;
    public static final BitSet FOLLOW_ISSEASON_in_functioncall695;
    public static final BitSet FOLLOW_paramlist_in_functioncall701;
    public static final BitSet FOLLOW_HASSTATE_in_functioncall706;
    public static final BitSet FOLLOW_paramlist_in_functioncall710;
    public static final BitSet FOLLOW_GETSKILLLEVEL_in_functioncall715;
    public static final BitSet FOLLOW_paramlist_in_functioncall719;
    public static final BitSet FOLLOW_GETSPELLLEVEL_in_functioncall724;
    public static final BitSet FOLLOW_paramlist_in_functioncall728;
    public static final BitSet FOLLOW_GETSPELLTREELEVEL_in_functioncall733;
    public static final BitSet FOLLOW_paramlist_in_functioncall737;
    public static final BitSet FOLLOW_GETTEAMID_in_functioncall742;
    public static final BitSet FOLLOW_paramlist_in_functioncall746;
    public static final BitSet FOLLOW_GETMONST_in_functioncall751;
    public static final BitSet FOLLOW_paramlist_in_functioncall755;
    public static final BitSet FOLLOW_PETWITHINRANGE_in_functioncall761;
    public static final BitSet FOLLOW_paramlist_in_functioncall765;
    public static final BitSet FOLLOW_SUMMONAMOUNT_in_functioncall771;
    public static final BitSet FOLLOW_paramlist_in_functioncall775;
    public static final BitSet FOLLOW_BEACONAMOUNT_in_functioncall781;
    public static final BitSet FOLLOW_paramlist_in_functioncall785;
    public static final BitSet FOLLOW_BARRELAMOUNT_in_functioncall791;
    public static final BitSet FOLLOW_paramlist_in_functioncall795;
    public static final BitSet FOLLOW_GET_XELOR_DIALS_COUNT_in_functioncall801;
    public static final BitSet FOLLOW_paramlist_in_functioncall805;
    public static final BitSet FOLLOW_ISBACKSTAB_in_functioncall811;
    public static final BitSet FOLLOW_paramlist_in_functioncall815;
    public static final BitSet FOLLOW_HASLINEOFSIGHT_in_functioncall820;
    public static final BitSet FOLLOW_paramlist_in_functioncall824;
    public static final BitSet FOLLOW_GETPOSITION_in_functioncall829;
    public static final BitSet FOLLOW_paramlist_in_functioncall833;
    public static final BitSet FOLLOW_GETCHARACTERID_in_functioncall838;
    public static final BitSet FOLLOW_paramlist_in_functioncall842;
    public static final BitSet FOLLOW_GETIEPOSITION_in_functioncall847;
    public static final BitSet FOLLOW_paramlist_in_functioncall851;
    public static final BitSet FOLLOW_ISSEX_in_functioncall856;
    public static final BitSet FOLLOW_paramlist_in_functioncall862;
    public static final BitSet FOLLOW_SLOTSINBAG_in_functioncall867;
    public static final BitSet FOLLOW_paramlist_in_functioncall873;
    public static final BitSet FOLLOW_GETINSTANCEID_in_functioncall878;
    public static final BitSet FOLLOW_paramlist_in_functioncall882;
    public static final BitSet FOLLOW_GETEFFECTCASTER_in_functioncall887;
    public static final BitSet FOLLOW_paramlist_in_functioncall891;
    public static final BitSet FOLLOW_GETEFFECTTARGET_in_functioncall896;
    public static final BitSet FOLLOW_paramlist_in_functioncall900;
    public static final BitSet FOLLOW_GETTRIGGEREREFFECTCASTER_in_functioncall905;
    public static final BitSet FOLLOW_paramlist_in_functioncall909;
    public static final BitSet FOLLOW_GET_TRIGGERING_EFFECT_ID_in_functioncall914;
    public static final BitSet FOLLOW_paramlist_in_functioncall918;
    public static final BitSet FOLLOW_GET_TRIGGERING_EFFECT_VALUE_in_functioncall923;
    public static final BitSet FOLLOW_paramlist_in_functioncall927;
    public static final BitSet FOLLOW_ITEMQUANTITY_in_functioncall932;
    public static final BitSet FOLLOW_paramlist_in_functioncall936;
    public static final BitSet FOLLOW_GETKAMASCOUNT_in_functioncall941;
    public static final BitSet FOLLOW_paramlist_in_functioncall945;
    public static final BitSet FOLLOW_ISMONSTERBREED_in_functioncall950;
    public static final BitSet FOLLOW_paramlist_in_functioncall954;
    public static final BitSet FOLLOW_GETDISTANCEBETWEENCASTERANDTARGET_in_functioncall959;
    public static final BitSet FOLLOW_paramlist_in_functioncall963;
    public static final BitSet FOLLOW_GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON_in_functioncall968;
    public static final BitSet FOLLOW_paramlist_in_functioncall972;
    public static final BitSet FOLLOW_ISUNDEAD_in_functioncall977;
    public static final BitSet FOLLOW_paramlist_in_functioncall981;
    public static final BitSet FOLLOW_EFFECTISFROMHEAL_in_functioncall986;
    public static final BitSet FOLLOW_paramlist_in_functioncall990;
    public static final BitSet FOLLOW_HASWORLDPROPERTY_in_functioncall995;
    public static final BitSet FOLLOW_paramlist_in_functioncall999;
    public static final BitSet FOLLOW_HASFIGHTPROPERTY_in_functioncall1004;
    public static final BitSet FOLLOW_paramlist_in_functioncall1008;
    public static final BitSet FOLLOW_GETMONTH_in_functioncall1013;
    public static final BitSet FOLLOW_paramlist_in_functioncall1017;
    public static final BitSet FOLLOW_HASNTEVOLVEDSINCE_in_functioncall1022;
    public static final BitSet FOLLOW_paramlist_in_functioncall1026;
    public static final BitSet FOLLOW_GETLEVEL_in_functioncall1031;
    public static final BitSet FOLLOW_paramlist_in_functioncall1037;
    public static final BitSet FOLLOW_GETLOCKINCREMENT_in_functioncall1042;
    public static final BitSet FOLLOW_paramlist_in_functioncall1048;
    public static final BitSet FOLLOW_ISBREEDFAMILY_in_functioncall1053;
    public static final BitSet FOLLOW_paramlist_in_functioncall1059;
    public static final BitSet FOLLOW_ISCHALLENGEUSER_in_functioncall1065;
    public static final BitSet FOLLOW_paramlist_in_functioncall1071;
    public static final BitSet FOLLOW_ISUNDERCONTROL_in_functioncall1076;
    public static final BitSet FOLLOW_paramlist_in_functioncall1082;
    public static final BitSet FOLLOW_ISAFTER_in_functioncall1088;
    public static final BitSet FOLLOW_paramlist_in_functioncall1094;
    public static final BitSet FOLLOW_GETWAKFUGAUGE_in_functioncall1100;
    public static final BitSet FOLLOW_paramlist_in_functioncall1106;
    public static final BitSet FOLLOW_GETRANDOMNUMBER_in_functioncall1112;
    public static final BitSet FOLLOW_paramlist_in_functioncall1118;
    public static final BitSet FOLLOW_GETENNEMYCOUNTINRANGE_in_functioncall1124;
    public static final BitSet FOLLOW_paramlist_in_functioncall1130;
    public static final BitSet FOLLOW_GETALLIESCOUNTINRANGE_in_functioncall1136;
    public static final BitSet FOLLOW_paramlist_in_functioncall1142;
    public static final BitSet FOLLOW_GETCONTROLLERINSAMETEAMCOUNTINRANGE_in_functioncall1148;
    public static final BitSet FOLLOW_paramlist_in_functioncall1154;
    public static final BitSet FOLLOW_GETDESTRUCTIBLECOUNTINRANGE_in_functioncall1160;
    public static final BitSet FOLLOW_paramlist_in_functioncall1166;
    public static final BitSet FOLLOW_GETWALLCOUNTINRANGE_in_functioncall1172;
    public static final BitSet FOLLOW_paramlist_in_functioncall1178;
    public static final BitSet FOLLOW_GETNATIONID_in_functioncall1184;
    public static final BitSet FOLLOW_paramlist_in_functioncall1188;
    public static final BitSet FOLLOW_GETNATIONALIGNMENT_in_functioncall1193;
    public static final BitSet FOLLOW_paramlist_in_functioncall1197;
    public static final BitSet FOLLOW_GETNATIVENATIONID_in_functioncall1203;
    public static final BitSet FOLLOW_paramlist_in_functioncall1207;
    public static final BitSet FOLLOW_GETSTASISGAUGE_in_functioncall1212;
    public static final BitSet FOLLOW_paramlist_in_functioncall1218;
    public static final BitSet FOLLOW_GETDATE_in_functioncall1224;
    public static final BitSet FOLLOW_paramlist_in_functioncall1230;
    public static final BitSet FOLLOW_ISFACESTABBED_in_functioncall1236;
    public static final BitSet FOLLOW_paramlist_in_functioncall1242;
    public static final BitSet FOLLOW_GETCRIMESCORE_in_functioncall1248;
    public static final BitSet FOLLOW_paramlist_in_functioncall1254;
    public static final BitSet FOLLOW_ISDEAD_in_functioncall1260;
    public static final BitSet FOLLOW_paramlist_in_functioncall1266;
    public static final BitSet FOLLOW_GETSATISFACTIONLEVEL_in_functioncall1272;
    public static final BitSet FOLLOW_paramlist_in_functioncall1278;
    public static final BitSet FOLLOW_GETBOOLEANVALUE_in_functioncall1284;
    public static final BitSet FOLLOW_paramlist_in_functioncall1290;
    public static final BitSet FOLLOW_GETCURRENTPARTITIONNATIONID_in_functioncall1296;
    public static final BitSet FOLLOW_paramlist_in_functioncall1302;
    public static final BitSet FOLLOW_GETTERRITORYID_in_functioncall1308;
    public static final BitSet FOLLOW_paramlist_in_functioncall1314;
    public static final BitSet FOLLOW_GETPROTECTORNATIONID_in_functioncall1321;
    public static final BitSet FOLLOW_paramlist_in_functioncall1327;
    public static final BitSet FOLLOW_GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT_in_functioncall1334;
    public static final BitSet FOLLOW_paramlist_in_functioncall1340;
    public static final BitSet FOLLOW_HASFREESURROUNDINGCELL_in_functioncall1346;
    public static final BitSet FOLLOW_paramlist_in_functioncall1352;
    public static final BitSet FOLLOW_ISTARGETCELLFREE_in_functioncall1358;
    public static final BitSet FOLLOW_paramlist_in_functioncall1364;
    public static final BitSet FOLLOW_IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE_in_functioncall1370;
    public static final BitSet FOLLOW_paramlist_in_functioncall1376;
    public static final BitSet FOLLOW_ISCARRIED_in_functioncall1382;
    public static final BitSet FOLLOW_paramlist_in_functioncall1388;
    public static final BitSet FOLLOW_ISCARRYING_in_functioncall1394;
    public static final BitSet FOLLOW_paramlist_in_functioncall1400;
    public static final BitSet FOLLOW_HASAVAILABLECREATUREINSYMBIOT_in_functioncall1406;
    public static final BitSet FOLLOW_paramlist_in_functioncall1412;
    public static final BitSet FOLLOW_SUMMONSLEADERSHIPSCORE_in_functioncall1418;
    public static final BitSet FOLLOW_paramlist_in_functioncall1424;
    public static final BitSet FOLLOW_LEADERSHIPFORCURRENTINVOC_in_functioncall1430;
    public static final BitSet FOLLOW_paramlist_in_functioncall1436;
    public static final BitSet FOLLOW_GETTERRITORYNATIONID_in_functioncall1442;
    public static final BitSet FOLLOW_paramlist_in_functioncall1448;
    public static final BitSet FOLLOW_ISOWNSUMMON_in_functioncall1454;
    public static final BitSet FOLLOW_paramlist_in_functioncall1460;
    public static final BitSet FOLLOW_GETCHARACTERDIRECTION_in_functioncall1466;
    public static final BitSet FOLLOW_paramlist_in_functioncall1472;
    public static final BitSet FOLLOW_GETCRAFTLEARNINGITEM_in_functioncall1477;
    public static final BitSet FOLLOW_paramlist_in_functioncall1483;
    public static final BitSet FOLLOW_HASCRAFT_in_functioncall1488;
    public static final BitSet FOLLOW_paramlist_in_functioncall1494;
    public static final BitSet FOLLOW_GETCRAFTLEVEL_in_functioncall1499;
    public static final BitSet FOLLOW_paramlist_in_functioncall1505;
    public static final BitSet FOLLOW_HASEMOTE_in_functioncall1510;
    public static final BitSet FOLLOW_paramlist_in_functioncall1516;
    public static final BitSet FOLLOW_ISPASSEPORTACTIVE_in_functioncall1521;
    public static final BitSet FOLLOW_paramlist_in_functioncall1527;
    public static final BitSet FOLLOW_CANBECOMESOLDIERORMILITIAMAN_in_functioncall1532;
    public static final BitSet FOLLOW_paramlist_in_functioncall1538;
    public static final BitSet FOLLOW_GETTITLE_in_functioncall1544;
    public static final BitSet FOLLOW_paramlist_in_functioncall1550;
    public static final BitSet FOLLOW_GETNATIONRANK_in_functioncall1556;
    public static final BitSet FOLLOW_paramlist_in_functioncall1562;
    public static final BitSet FOLLOW_ISEQUIPPEDWITHSET_in_functioncall1569;
    public static final BitSet FOLLOW_paramlist_in_functioncall1575;
    public static final BitSet FOLLOW_ISHOUR_in_functioncall1581;
    public static final BitSet FOLLOW_paramlist_in_functioncall1585;
    public static final BitSet FOLLOW_ISOWNHOUR_in_functioncall1591;
    public static final BitSet FOLLOW_paramlist_in_functioncall1595;
    public static final BitSet FOLLOW_ISBOMB_in_functioncall1601;
    public static final BitSet FOLLOW_paramlist_in_functioncall1607;
    public static final BitSet FOLLOW_ISTUNNEL_in_functioncall1612;
    public static final BitSet FOLLOW_paramlist_in_functioncall1618;
    public static final BitSet FOLLOW_ISOWNBOMB_in_functioncall1623;
    public static final BitSet FOLLOW_paramlist_in_functioncall1629;
    public static final BitSet FOLLOW_ISOWNBEACON_in_functioncall1634;
    public static final BitSet FOLLOW_paramlist_in_functioncall1640;
    public static final BitSet FOLLOW_ISOWNSPECIFICAREA_in_functioncall1646;
    public static final BitSet FOLLOW_paramlist_in_functioncall1652;
    public static final BitSet FOLLOW_HASSUMMONWITHBREED_in_functioncall1658;
    public static final BitSet FOLLOW_paramlist_in_functioncall1664;
    public static final BitSet FOLLOW_NBBOMB_in_functioncall1670;
    public static final BitSet FOLLOW_paramlist_in_functioncall1676;
    public static final BitSet FOLLOW_ISACHIEVEMENTOBJECTIVECOMPLETE_in_functioncall1682;
    public static final BitSet FOLLOW_paramlist_in_functioncall1687;
    public static final BitSet FOLLOW_ISACHIEVEMENTREPEATABLE_in_functioncall1693;
    public static final BitSet FOLLOW_paramlist_in_functioncall1698;
    public static final BitSet FOLLOW_CANRESETACHIEVEMENT_in_functioncall1704;
    public static final BitSet FOLLOW_paramlist_in_functioncall1709;
    public static final BitSet FOLLOW_OPPONENTSCONTAINSNATIONENEMY_in_functioncall1715;
    public static final BitSet FOLLOW_paramlist_in_functioncall1720;
    public static final BitSet FOLLOW_HASNATIONJOB_in_functioncall1726;
    public static final BitSet FOLLOW_paramlist_in_functioncall1731;
    public static final BitSet FOLLOW_ISACHIEVEMENTCOMPLETE_in_functioncall1737;
    public static final BitSet FOLLOW_paramlist_in_functioncall1742;
    public static final BitSet FOLLOW_ISACHIEVEMENTACTIVE_in_functioncall1748;
    public static final BitSet FOLLOW_paramlist_in_functioncall1753;
    public static final BitSet FOLLOW_ISACHIEVEMENTRUNNING_in_functioncall1762;
    public static final BitSet FOLLOW_paramlist_in_functioncall1767;
    public static final BitSet FOLLOW_ISACHIEVEMENTFAILED_in_functioncall1776;
    public static final BitSet FOLLOW_paramlist_in_functioncall1781;
    public static final BitSet FOLLOW_ISPROTECTORINFIGHT_in_functioncall1790;
    public static final BitSet FOLLOW_paramlist_in_functioncall1796;
    public static final BitSet FOLLOW_ISOFFPLAY_in_functioncall1801;
    public static final BitSet FOLLOW_paramlist_in_functioncall1807;
    public static final BitSet FOLLOW_IS_IN_PLAY_in_functioncall1812;
    public static final BitSet FOLLOW_paramlist_in_functioncall1818;
    public static final BitSet FOLLOW_IS_OUT_OF_PLAY_in_functioncall1823;
    public static final BitSet FOLLOW_paramlist_in_functioncall1829;
    public static final BitSet FOLLOW_IS_IN_FIGHT_in_functioncall1834;
    public static final BitSet FOLLOW_paramlist_in_functioncall1840;
    public static final BitSet FOLLOW_ISOWNDEPOSIT_in_functioncall1845;
    public static final BitSet FOLLOW_paramlist_in_functioncall1851;
    public static final BitSet FOLLOW_ISINGROUP_in_functioncall1856;
    public static final BitSet FOLLOW_paramlist_in_functioncall1862;
    public static final BitSet FOLLOW_ISACTIVATEDBYELEMENT_in_functioncall1868;
    public static final BitSet FOLLOW_paramlist_in_functioncall1874;
    public static final BitSet FOLLOW_ISACTIVATEDBYSPELL_in_functioncall1880;
    public static final BitSet FOLLOW_paramlist_in_functioncall1886;
    public static final BitSet FOLLOW_ISONEFFECTAREATYPE_in_functioncall1892;
    public static final BitSet FOLLOW_paramlist_in_functioncall1898;
    public static final BitSet FOLLOW_ISONSPECIFICEFFECTAREA_in_functioncall1904;
    public static final BitSet FOLLOW_paramlist_in_functioncall1910;
    public static final BitSet FOLLOW_ISONSPECIFICEFFECTAREAWITHSPECIFICSTATE_in_functioncall1919;
    public static final BitSet FOLLOW_paramlist_in_functioncall1925;
    public static final BitSet FOLLOW_CELL_CONTAINS_SPECIFIC_EFFECT_AREA_in_functioncall1934;
    public static final BitSet FOLLOW_paramlist_in_functioncall1940;
    public static final BitSet FOLLOW_ISOWNGLYPH_in_functioncall1946;
    public static final BitSet FOLLOW_paramlist_in_functioncall1952;
    public static final BitSet FOLLOW_ISDEPOSIT_in_functioncall1957;
    public static final BitSet FOLLOW_paramlist_in_functioncall1963;
    public static final BitSet FOLLOW_GETSTATECOUNTINRANGE_in_functioncall1968;
    public static final BitSet FOLLOW_paramlist_in_functioncall1974;
    public static final BitSet FOLLOW_ISFLEEING_in_functioncall1979;
    public static final BitSet FOLLOW_paramlist_in_functioncall1985;
    public static final BitSet FOLLOW_ISABANDONNING_in_functioncall1990;
    public static final BitSet FOLLOW_paramlist_in_functioncall1996;
    public static final BitSet FOLLOW_ISNATIONFIRSTINDUNGEONLADDER_in_functioncall2001;
    public static final BitSet FOLLOW_paramlist_in_functioncall2007;
    public static final BitSet FOLLOW_GETFIGHTMODEL_in_functioncall2012;
    public static final BitSet FOLLOW_paramlist_in_functioncall2018;
    public static final BitSet FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_BARREL_in_functioncall2023;
    public static final BitSet FOLLOW_paramlist_in_functioncall2029;
    public static final BitSet FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA_in_functioncall2034;
    public static final BitSet FOLLOW_paramlist_in_functioncall2040;
    public static final BitSet FOLLOW_HAS_SURROUNDING_CELL_WITH_EFFECT_AREA_in_functioncall2045;
    public static final BitSet FOLLOW_paramlist_in_functioncall2051;
    public static final BitSet FOLLOW_IS_CARRYING_OWN_BARREL_in_functioncall2056;
    public static final BitSet FOLLOW_paramlist_in_functioncall2062;
    public static final BitSet FOLLOW_GET_TARGET_COUNT_IN_BEACON_AREA_in_functioncall2068;
    public static final BitSet FOLLOW_paramlist_in_functioncall2074;
    public static final BitSet FOLLOW_GET_FIGHTERS_WITH_BREED_IN_RANGE_in_functioncall2080;
    public static final BitSet FOLLOW_paramlist_in_functioncall2086;
    public static final BitSet FOLLOW_AI_HAS_CAST_SPELL_in_functioncall2092;
    public static final BitSet FOLLOW_paramlist_in_functioncall2098;
    public static final BitSet FOLLOW_AI_HAS_MOVED_in_functioncall2104;
    public static final BitSet FOLLOW_paramlist_in_functioncall2110;
    public static final BitSet FOLLOW_AI_GET_SPELL_CAST_COUNT_in_functioncall2116;
    public static final BitSet FOLLOW_paramlist_in_functioncall2122;
    public static final BitSet FOLLOW_GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA_in_functioncall2128;
    public static final BitSet FOLLOW_paramlist_in_functioncall2134;
    public static final BitSet FOLLOW_IS_SUMMON_in_functioncall2140;
    public static final BitSet FOLLOW_paramlist_in_functioncall2146;
    public static final BitSet FOLLOW_IS_SUMMON_FROM_SYMBIOT_in_functioncall2152;
    public static final BitSet FOLLOW_paramlist_in_functioncall2158;
    public static final BitSet FOLLOW_IS_CONTROLLED_BY_AI_in_functioncall2164;
    public static final BitSet FOLLOW_paramlist_in_functioncall2170;
    public static final BitSet FOLLOW_GET_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall2176;
    public static final BitSet FOLLOW_paramlist_in_functioncall2182;
    public static final BitSet FOLLOW_GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall2188;
    public static final BitSet FOLLOW_paramlist_in_functioncall2194;
    public static final BitSet FOLLOW_GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE_in_functioncall2200;
    public static final BitSet FOLLOW_paramlist_in_functioncall2206;
    public static final BitSet FOLLOW_HAS_LINE_OF_SIGHT_FROM_ENEMY_in_functioncall2212;
    public static final BitSet FOLLOW_paramlist_in_functioncall2218;
    public static final BitSet FOLLOW_GET_ENEMIES_HUMAN_COUNT_IN_RANGE_in_functioncall2224;
    public static final BitSet FOLLOW_paramlist_in_functioncall2230;
    public static final BitSet FOLLOW_IS_TARGET_ON_SAME_TEAM_in_functioncall2236;
    public static final BitSet FOLLOW_paramlist_in_functioncall2242;
    public static final BitSet FOLLOW_HAS_LOOT_in_functioncall2248;
    public static final BitSet FOLLOW_paramlist_in_functioncall2254;
    public static final BitSet FOLLOW_HAS_EFFECT_WITH_ACTION_ID_in_functioncall2260;
    public static final BitSet FOLLOW_paramlist_in_functioncall2266;
    public static final BitSet FOLLOW_IS_CHARACTER_in_functioncall2272;
    public static final BitSet FOLLOW_paramlist_in_functioncall2278;
    public static final BitSet FOLLOW_HAS_STATE_FROM_LEVEL_in_functioncall2284;
    public static final BitSet FOLLOW_paramlist_in_functioncall2290;
    public static final BitSet FOLLOW_DOUBLE_OR_QUITS_CRITERION_in_functioncall2296;
    public static final BitSet FOLLOW_paramlist_in_functioncall2302;
    public static final BitSet FOLLOW_HAS_WEAPON_TYPE_in_functioncall2308;
    public static final BitSet FOLLOW_paramlist_in_functioncall2314;
    public static final BitSet FOLLOW_IS_OWN_AREA_in_functioncall2320;
    public static final BitSet FOLLOW_paramlist_in_functioncall2326;
    public static final BitSet FOLLOW_IS_ON_BORDER_CELL_in_functioncall2332;
    public static final BitSet FOLLOW_paramlist_in_functioncall2338;
    public static final BitSet FOLLOW_NB_ROUBLABOT_in_functioncall2344;
    public static final BitSet FOLLOW_paramlist_in_functioncall2350;
    public static final BitSet FOLLOW_HAS_EFFECT_WITH_SPECIFIC_ID_in_functioncall2356;
    public static final BitSet FOLLOW_paramlist_in_functioncall2362;
    public static final BitSet FOLLOW_HAS_FECA_ARMOR_in_functioncall2368;
    public static final BitSet FOLLOW_paramlist_in_functioncall2374;
    public static final BitSet FOLLOW_IS_FECA_GLYPH_CENTER_in_functioncall2380;
    public static final BitSet FOLLOW_paramlist_in_functioncall2386;
    public static final BitSet FOLLOW_NB_FECA_GLYPH_in_functioncall2392;
    public static final BitSet FOLLOW_paramlist_in_functioncall2398;
    public static final BitSet FOLLOW_GETACHIEVEMENTVARIABLE_in_functioncall2404;
    public static final BitSet FOLLOW_paramlist_in_functioncall2409;
    public static final BitSet FOLLOW_GET_CHALLENGE_UNAVAILABILITY_DURATION_in_functioncall2415;
    public static final BitSet FOLLOW_paramlist_in_functioncall2420;
    public static final BitSet FOLLOW_GET_EFFECT_CASTER_ORIGINAL_CONTROLLER_in_functioncall2426;
    public static final BitSet FOLLOW_paramlist_in_functioncall2431;
    public static final BitSet FOLLOW_GET_EFFECT_TARGET_ORIGINAL_CONTROLLER_in_functioncall2437;
    public static final BitSet FOLLOW_paramlist_in_functioncall2442;
    public static final BitSet FOLLOW_CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER_in_functioncall2448;
    public static final BitSet FOLLOW_paramlist_in_functioncall2453;
    public static final BitSet FOLLOW_IS_ORIGINAL_CONTROLLER_in_functioncall2459;
    public static final BitSet FOLLOW_paramlist_in_functioncall2464;
    public static final BitSet FOLLOW_IS_CHARACTERISTIC_FULL_in_functioncall2470;
    public static final BitSet FOLLOW_paramlist_in_functioncall2475;
    public static final BitSet FOLLOW_ISACCOUNTSUBSCRIBED_in_functioncall2481;
    public static final BitSet FOLLOW_paramlist_in_functioncall2486;
    public static final BitSet FOLLOW_ISZONEINCHAOS_in_functioncall2492;
    public static final BitSet FOLLOW_paramlist_in_functioncall2498;
    public static final BitSet FOLLOW_GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER_in_functioncall2503;
    public static final BitSet FOLLOW_paramlist_in_functioncall2508;
    public static final BitSet FOLLOW_GET_TRIGGERING_EFFECT_CASTER_in_functioncall2514;
    public static final BitSet FOLLOW_paramlist_in_functioncall2519;
    public static final BitSet FOLLOW_GET_FIGHTER_ID_in_functioncall2525;
    public static final BitSet FOLLOW_paramlist_in_functioncall2530;
    public static final BitSet FOLLOW_IS_ON_OWN_DIAL_in_functioncall2536;
    public static final BitSet FOLLOW_paramlist_in_functioncall2542;
    public static final BitSet FOLLOW_NB_HYDRANDS_in_functioncall2548;
    public static final BitSet FOLLOW_paramlist_in_functioncall2554;
    public static final BitSet FOLLOW_NB_STEAMBOTS_in_functioncall2560;
    public static final BitSet FOLLOW_paramlist_in_functioncall2566;
    public static final BitSet FOLLOW_IS_OWN_FECA_GLYPH_in_functioncall2572;
    public static final BitSet FOLLOW_paramlist_in_functioncall2578;
    public static final BitSet FOLLOW_GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER_in_functioncall2584;
    public static final BitSet FOLLOW_paramlist_in_functioncall2590;
    public static final BitSet FOLLOW_GET_FGHT_CURRENT_TABLE_TURN_in_functioncall2596;
    public static final BitSet FOLLOW_paramlist_in_functioncall2602;
    public static final BitSet FOLLOW_NB_ALL_AREAS_in_functioncall2608;
    public static final BitSet FOLLOW_paramlist_in_functioncall2614;
    public static final BitSet FOLLOW_NB_GLYPHS_in_functioncall2620;
    public static final BitSet FOLLOW_paramlist_in_functioncall2626;
    public static final BitSet FOLLOW_GET_TRIGGERING_EFFECT_TARGET_BREED_ID_in_functioncall2633;
    public static final BitSet FOLLOW_paramlist_in_functioncall2639;
    public static final BitSet FOLLOW_GET_TRIGGERING_ANCESTORS_COUNT_in_functioncall2646;
    public static final BitSet FOLLOW_paramlist_in_functioncall2652;
    public static final BitSet FOLLOW_IS_SIDE_STABBED_in_functioncall2658;
    public static final BitSet FOLLOW_paramlist_in_functioncall2664;
    public static final BitSet FOLLOW_GET_TRIGGERING_EFFECT_TARGET_in_functioncall2670;
    public static final BitSet FOLLOW_paramlist_in_functioncall2676;
    public static final BitSet FOLLOW_HAS_LINE_OF_SIGHT_TO_ENEMY_in_functioncall2682;
    public static final BitSet FOLLOW_paramlist_in_functioncall2688;
    public static final BitSet FOLLOW_IS_PLAYER_in_functioncall2694;
    public static final BitSet FOLLOW_paramlist_in_functioncall2700;
    public static final BitSet FOLLOW_IS_COMPANION_in_functioncall2706;
    public static final BitSet FOLLOW_paramlist_in_functioncall2712;
    public static final BitSet FOLLOW_IS_CHALLENGER_in_functioncall2718;
    public static final BitSet FOLLOW_paramlist_in_functioncall2724;
    public static final BitSet FOLLOW_IS_CARRYING_OWN_BOMB_in_functioncall2730;
    public static final BitSet FOLLOW_paramlist_in_functioncall2736;
    public static final BitSet FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_SUMMON_in_functioncall2742;
    public static final BitSet FOLLOW_paramlist_in_functioncall2748;
    public static final BitSet FOLLOW_GET_GUILD_LEVEL_in_functioncall2754;
    public static final BitSet FOLLOW_paramlist_in_functioncall2760;
    public static final BitSet FOLLOW_IS_IN_GUILD_in_functioncall2766;
    public static final BitSet FOLLOW_paramlist_in_functioncall2772;
    public static final BitSet FOLLOW_GET_GUILD_PARTNER_COUNT_IN_FIGHT_in_functioncall2778;
    public static final BitSet FOLLOW_paramlist_in_functioncall2784;
    public static final BitSet FOLLOW_IS_IN_ALIGNMENT_in_functioncall2790;
    public static final BitSet FOLLOW_paramlist_in_functioncall2796;
    public static final BitSet FOLLOW_HAS_VALID_PATH_TO_TARGET_in_functioncall2802;
    public static final BitSet FOLLOW_paramlist_in_functioncall2808;
    public static final BitSet FOLLOW_IS_FREE_CELL_in_functioncall2815;
    public static final BitSet FOLLOW_paramlist_in_functioncall2821;
    public static final BitSet FOLLOW_HAS_BEEN_RAISED_BY_EFFECT_in_functioncall2827;
    public static final BitSet FOLLOW_paramlist_in_functioncall2833;
    public static final BitSet FOLLOW_GET_X_in_functioncall2839;
    public static final BitSet FOLLOW_paramlist_in_functioncall2845;
    public static final BitSet FOLLOW_GET_Y_in_functioncall2852;
    public static final BitSet FOLLOW_paramlist_in_functioncall2858;
    public static final BitSet FOLLOW_GET_Z_in_functioncall2865;
    public static final BitSet FOLLOW_paramlist_in_functioncall2871;
    public static final BitSet FOLLOW_NB_AREAS_WITH_BASE_ID_in_functioncall2878;
    public static final BitSet FOLLOW_paramlist_in_functioncall2884;
    public static final BitSet FOLLOW_GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS_in_functioncall2890;
    public static final BitSet FOLLOW_paramlist_in_functioncall2896;
    public static final BitSet FOLLOW_GET_PARTITION_X_in_functioncall2902;
    public static final BitSet FOLLOW_paramlist_in_functioncall2908;
    public static final BitSet FOLLOW_GET_PARTITION_Y_in_functioncall2915;
    public static final BitSet FOLLOW_paramlist_in_functioncall2921;
    public static final BitSet FOLLOW_GET_TOTAL_HP_IN_PCT_in_functioncall2928;
    public static final BitSet FOLLOW_paramlist_in_functioncall2934;
    public static final BitSet FOLLOW_GET_ALLIES_COUNT_in_functioncall2940;
    public static final BitSet FOLLOW_paramlist_in_functioncall2946;
    public static final BitSet FOLLOW_IS_CELL_BEHIND_TARGET_FREE_in_functioncall2952;
    public static final BitSet FOLLOW_paramlist_in_functioncall2958;
    public static final BitSet FOLLOW_GET_STATE_LEVEL_in_functioncall2964;
    public static final BitSet FOLLOW_paramlist_in_functioncall2970;
    public static final BitSet FOLLOW_IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA_in_functioncall2976;
    public static final BitSet FOLLOW_paramlist_in_functioncall2982;
    public static final BitSet FOLLOW_HAS_ANOTHER_SAME_EQUIPMENT_in_functioncall2988;
    public static final BitSet FOLLOW_paramlist_in_functioncall2994;
    public static final BitSet FOLLOW_IS_LOCKED_in_functioncall3000;
    public static final BitSet FOLLOW_paramlist_in_functioncall3006;
    public static final BitSet FOLLOW_HAS_STATE_FROM_USER_in_functioncall3012;
    public static final BitSet FOLLOW_paramlist_in_functioncall3018;
    public static final BitSet FOLLOW_GET_HUMAN_ALLIES_COUNT_IN_RANGE_in_functioncall3024;
    public static final BitSet FOLLOW_paramlist_in_functioncall3030;
    public static final BitSet FOLLOW_IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL_in_functioncall3036;
    public static final BitSet FOLLOW_paramlist_in_functioncall3042;
    public static final BitSet FOLLOW_HAS_GUILD_BONUS_in_functioncall3048;
    public static final BitSet FOLLOW_paramlist_in_functioncall3054;
    public static final BitSet FOLLOW_GET_NEXT_FIGHTER_IN_TIMELINE_in_functioncall3060;
    public static final BitSet FOLLOW_paramlist_in_functioncall3066;
    public static final BitSet FOLLOW_IS_TRIGGERED_BY_ZONE_EFFECT_in_functioncall3072;
    public static final BitSet FOLLOW_paramlist_in_functioncall3078;
    public static final BitSet FOLLOW_IS_TRIGGERING_EFFECT_CRITICAL_in_functioncall3084;
    public static final BitSet FOLLOW_paramlist_in_functioncall3090;
    public static final BitSet FOLLOW_GET_BOOLEAN_SYSTEM_CONFIGURATION_in_functioncall3096;
    public static final BitSet FOLLOW_paramlist_in_functioncall3102;
    public static final BitSet FOLLOW_GET_FIGHTERS_MIN_LEVEL_in_functioncall3108;
    public static final BitSet FOLLOW_paramlist_in_functioncall3114;
    public static final BitSet FOLLOW_GET_FIGHTERS_MAX_LEVEL_in_functioncall3120;
    public static final BitSet FOLLOW_paramlist_in_functioncall3126;
    public static final BitSet FOLLOW_GET_FIGHTERS_LEVEL_DIFF_in_functioncall3132;
    public static final BitSet FOLLOW_paramlist_in_functioncall3138;
    public static final BitSet FOLLOW_GET_FIGHTERS_LEVEL_SUM_in_functioncall3144;
    public static final BitSet FOLLOW_paramlist_in_functioncall3150;
    public static final BitSet FOLLOW_IS_PRELOADING_in_functioncall3156;
    public static final BitSet FOLLOW_paramlist_in_functioncall3162;
    public static final BitSet FOLLOW_HAS_UNLOCKED_COMPANION_in_functioncall3168;
    public static final BitSet FOLLOW_paramlist_in_functioncall3174;
    public static final BitSet FOLLOW_IS_CASTER_FACING_FIGHTER_in_functioncall3180;
    public static final BitSet FOLLOW_paramlist_in_functioncall3186;
    public static final BitSet FOLLOW_GET_OWN_ARMOR_COUNT_in_functioncall3192;
    public static final BitSet FOLLOW_paramlist_in_functioncall3198;
    public static final BitSet FOLLOW_HAS_CASTER_FECA_ARMOR_in_functioncall3204;
    public static final BitSet FOLLOW_paramlist_in_functioncall3210;
    public static final BitSet FOLLOW_GET_OWN_TEAM_STATE_COUNT_IN_RANGE_in_functioncall3216;
    public static final BitSet FOLLOW_paramlist_in_functioncall3222;
    public static final BitSet FOLLOW_GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE_in_functioncall3228;
    public static final BitSet FOLLOW_paramlist_in_functioncall3234;
    public static final BitSet FOLLOW_HAS_SUBSCRIPTION_LEVEL_in_functioncall3240;
    public static final BitSet FOLLOW_paramlist_in_functioncall3246;
    public static final BitSet FOLLOW_ISPVP_in_functioncall3252;
    public static final BitSet FOLLOW_paramlist_in_functioncall3258;
    public static final BitSet FOLLOW_HASPVPRANK_in_functioncall3264;
    public static final BitSet FOLLOW_paramlist_in_functioncall3270;
    public static final BitSet FOLLOW_IS_ENNEMY_NATION_in_functioncall3276;
    public static final BitSet FOLLOW_paramlist_in_functioncall3282;
    public static final BitSet FOLLOW_IS_PVP_STATE_ACTIVE_in_functioncall3288;
    public static final BitSet FOLLOW_paramlist_in_functioncall3294;
    public static final BitSet FOLLOW_GETLASTINSTANCEID_in_functioncall3300;
    public static final BitSet FOLLOW_paramlist_in_functioncall3305;
    public static final BitSet FOLLOW_GET_ACTIVE_SPELL_ID_in_functioncall3311;
    public static final BitSet FOLLOW_paramlist_in_functioncall3316;
    public static final BitSet FOLLOW_IS_HOSTILE_in_functioncall3322;
    public static final BitSet FOLLOW_paramlist_in_functioncall3328;
    public static final BitSet FOLLOW_HAS_BEEN_NAUGHTY_in_functioncall3334;
    public static final BitSet FOLLOW_paramlist_in_functioncall3340;
    public static final BitSet FOLLOW_NB_GATES_in_functioncall3346;
    public static final BitSet FOLLOW_paramlist_in_functioncall3352;
    public static final BitSet FOLLOW_GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall3358;
    public static final BitSet FOLLOW_paramlist_in_functioncall3364;
    public static final BitSet FOLLOW_HAS_VALID_GATE_FOR_TP_in_functioncall3370;
    public static final BitSet FOLLOW_paramlist_in_functioncall3376;
    public static final BitSet FOLLOW_USE_GATE_EFFECT_in_functioncall3382;
    public static final BitSet FOLLOW_paramlist_in_functioncall3388;
    public static final BitSet FOLLOW_GET_CURRENT_FIGHTER_ID_in_functioncall3394;
    public static final BitSet FOLLOW_paramlist_in_functioncall3400;
    public static final BitSet FOLLOW_IS_HERO_in_functioncall3406;
    public static final BitSet FOLLOW_paramlist_in_functioncall3412;
    public static final BitSet FOLLOW_constants_in_atoms3440;
    public static final BitSet FOLLOW_PG_in_atoms3455;
    public static final BitSet FOLLOW_expr_in_atoms3459;
    public static final BitSet FOLLOW_PD_in_atoms3461;
    public static final BitSet FOLLOW_SHARP_in_atoms3477;
    public static final BitSet FOLLOW_expr_in_atoms3481;
    public static final BitSet FOLLOW_SHARP_in_atoms3483;
    public static final BitSet FOLLOW_AT_in_atoms3499;
    public static final BitSet FOLLOW_PG_in_atoms3501;
    public static final BitSet FOLLOW_STRING_in_atoms3505;
    public static final BitSet FOLLOW_PD_in_atoms3507;
    public static final BitSet FOLLOW_expr_in_atoms3511;
    public static final BitSet FOLLOW_AT_in_atoms3513;
    public static final BitSet FOLLOW_NOT_in_atoms3529;
    public static final BitSet FOLLOW_atoms_in_atoms3533;
    public static final BitSet FOLLOW_MINUS_in_atoms3548;
    public static final BitSet FOLLOW_atoms_in_atoms3552;
    public static final BitSet FOLLOW_functioncall_in_atoms3568;
    public static final BitSet FOLLOW_VARNAME_in_atoms3587;
    public static final BitSet FOLLOW_numberlist_in_atoms3606;
    public static final BitSet FOLLOW_TRUE_in_constants3632;
    public static final BitSet FOLLOW_FALSE_in_constants3640;
    public static final BitSet FOLLOW_INTEGER_in_constants3652;
    public static final BitSet FOLLOW_FLOAT_in_constants3662;
    public static final BitSet FOLLOW_STRING_in_constants3671;
    public static final BitSet FOLLOW_AG_in_constants3677;
    public static final BitSet FOLLOW_INTEGER_in_constants3683;
    public static final BitSet FOLLOW_VIRGULE_in_constants3685;
    public static final BitSet FOLLOW_INTEGER_in_constants3691;
    public static final BitSet FOLLOW_VIRGULE_in_constants3693;
    public static final BitSet FOLLOW_INTEGER_in_constants3697;
    public static final BitSet FOLLOW_AD_in_constants3699;
    
    public Parser[] getDelegates() {
        return new Parser[0];
    }
    
    public CritereParser(final TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    
    public CritereParser(final TokenStream input, final RecognizerSharedState state) {
        super(input, state);
        this.mem = new HashMap<String, ParserObject>();
    }
    
    public String[] getTokenNames() {
        return CritereParser.tokenNames;
    }
    
    public String getGrammarFileName() {
        return "C:\\Users\\abarth\\Documents\\code\\trunk\\src\\maven\\wakfu-parent\\wakfu-parent-java\\wakfu-common\\src\\main\\java\\com\\ankamagames\\wakfu\\common\\game\\ai\\antlrcriteria\\system\\Critere.g";
    }
    
    public void emitErrorMessage(final String msg) {
        CritereParser.m_logger.warn((Object)msg);
    }
    
    public final ArrayList<ParserObject> critere() throws RecognitionException {
        ArrayList<ParserObject> critlist = null;
        ParserObject s1 = null;
        ParserObject s2 = null;
        Label_0357: {
            try {
                this.pushFollow(CritereParser.FOLLOW_statement_in_critere47);
                s1 = this.statement();
                final RecognizerSharedState state = this.state;
                --state._fsp;
                critlist = new ArrayList<ParserObject>();
                critlist.add(s1);
                while (true) {
                    int alt1 = 2;
                    final int LA1_0 = this.input.LA(1);
                    if ((LA1_0 >= 5 && LA1_0 <= 8) || (LA1_0 >= 11 && LA1_0 <= 12) || (LA1_0 >= 14 && LA1_0 <= 20) || (LA1_0 >= 23 && LA1_0 <= 24) || (LA1_0 >= 28 && LA1_0 <= 131) || (LA1_0 >= 133 && LA1_0 <= 163) || LA1_0 == 165 || (LA1_0 >= 168 && LA1_0 <= 261) || LA1_0 == 264 || (LA1_0 >= 267 && LA1_0 <= 276) || LA1_0 == 278 || LA1_0 == 280 || (LA1_0 >= 282 && LA1_0 <= 283) || (LA1_0 >= 287 && LA1_0 <= 292) || (LA1_0 >= 296 && LA1_0 <= 299) || LA1_0 == 301) {
                        alt1 = 1;
                    }
                    switch (alt1) {
                        case 1: {
                            this.pushFollow(CritereParser.FOLLOW_statement_in_critere56);
                            s2 = this.statement();
                            final RecognizerSharedState state2 = this.state;
                            --state2._fsp;
                            critlist.add(s2);
                            continue;
                        }
                        default: {
                            break Label_0357;
                        }
                    }
                }
            }
            catch (RecognitionException re) {
                this.reportError(re);
                this.recover((IntStream)this.input, re);
            }
        }
        return critlist;
    }
    
    public final ParserObject statement() throws RecognitionException {
        ParserObject crit = null;
        Token t = null;
        ParserObject e1 = null;
        ParserObject e2 = null;
        try {
            int alt4 = 2;
            final int LA4_0 = this.input.LA(1);
            if ((LA4_0 >= 5 && LA4_0 <= 8) || (LA4_0 >= 11 && LA4_0 <= 12) || (LA4_0 >= 14 && LA4_0 <= 20) || (LA4_0 >= 23 && LA4_0 <= 24) || (LA4_0 >= 28 && LA4_0 <= 131) || (LA4_0 >= 133 && LA4_0 <= 163) || LA4_0 == 165 || (LA4_0 >= 168 && LA4_0 <= 261) || LA4_0 == 264 || (LA4_0 >= 267 && LA4_0 <= 276) || LA4_0 == 278 || LA4_0 == 280 || (LA4_0 >= 282 && LA4_0 <= 283) || (LA4_0 >= 287 && LA4_0 <= 292) || (LA4_0 >= 296 && LA4_0 <= 298) || LA4_0 == 301) {
                alt4 = 1;
            }
            else {
                if (LA4_0 != 299) {
                    final NoViableAltException nvae = new NoViableAltException("", 4, 0, (IntStream)this.input);
                    throw nvae;
                }
                final int LA4_ = this.input.LA(2);
                if (LA4_ == 10) {
                    alt4 = 2;
                }
                else if (LA4_ == -1 || LA4_ == 9 || LA4_ == 22 || (LA4_ >= 26 && LA4_ <= 27) || (LA4_ >= 166 && LA4_ <= 167) || (LA4_ >= 264 && LA4_ <= 266) || LA4_ == 277 || LA4_ == 279 || (LA4_ >= 284 && LA4_ <= 286) || (LA4_ >= 293 && LA4_ <= 294)) {
                    alt4 = 1;
                }
                else {
                    final int nvaeMark = this.input.mark();
                    try {
                        this.input.consume();
                        final NoViableAltException nvae2 = new NoViableAltException("", 4, 2, (IntStream)this.input);
                        throw nvae2;
                    }
                    finally {
                        this.input.rewind(nvaeMark);
                    }
                }
            }
            Label_0910: {
                switch (alt4) {
                    case 1: {
                        this.pushFollow(CritereParser.FOLLOW_expr_in_statement79);
                        e1 = this.expr();
                        final RecognizerSharedState state = this.state;
                        --state._fsp;
                        if (this.input.LA(1) != -1 && this.input.LA(1) != 26 && this.input.LA(1) != 286) {
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            throw mse;
                        }
                        this.input.consume();
                        this.state.errorRecovery = false;
                        while (true) {
                            int alt2 = 2;
                            final int LA2_0 = this.input.LA(1);
                            if (LA2_0 == 26) {
                                alt2 = 1;
                            }
                            switch (alt2) {
                                case 1: {
                                    this.match((IntStream)this.input, 26, CritereParser.FOLLOW_EOL_in_statement90);
                                    continue;
                                }
                                default: {
                                    crit = e1;
                                    break Label_0910;
                                }
                            }
                        }
                        break;
                    }
                    case 2: {
                        t = (Token)this.match((IntStream)this.input, 299, CritereParser.FOLLOW_VARNAME_in_statement99);
                        this.match((IntStream)this.input, 10, CritereParser.FOLLOW_ASSIGN_in_statement101);
                        this.pushFollow(CritereParser.FOLLOW_expr_in_statement107);
                        e2 = this.expr();
                        final RecognizerSharedState state2 = this.state;
                        --state2._fsp;
                        if (this.input.LA(1) != -1 && this.input.LA(1) != 26 && this.input.LA(1) != 286) {
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            throw mse;
                        }
                        this.input.consume();
                        this.state.errorRecovery = false;
                        while (true) {
                            int alt3 = 2;
                            final int LA3_0 = this.input.LA(1);
                            if (LA3_0 == 26) {
                                alt3 = 1;
                            }
                            switch (alt3) {
                                case 1: {
                                    this.match((IntStream)this.input, 26, CritereParser.FOLLOW_EOL_in_statement117);
                                    continue;
                                }
                                default: {
                                    crit = this.mem.put((t != null) ? t.getText() : null, e2);
                                    break Label_0910;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return crit;
    }
    
    public final ParserObject expr() throws RecognitionException {
        ParserObject crit = null;
        ParserObject e1 = null;
        ParserObject e2 = null;
        ParserObject s1 = null;
        ParserObject s2 = null;
        try {
            int alt6 = 2;
            final int LA6_0 = this.input.LA(1);
            if ((LA6_0 >= 5 && LA6_0 <= 8) || (LA6_0 >= 11 && LA6_0 <= 12) || (LA6_0 >= 14 && LA6_0 <= 20) || (LA6_0 >= 23 && LA6_0 <= 24) || (LA6_0 >= 28 && LA6_0 <= 131) || (LA6_0 >= 133 && LA6_0 <= 163) || (LA6_0 >= 168 && LA6_0 <= 261) || LA6_0 == 264 || (LA6_0 >= 267 && LA6_0 <= 276) || LA6_0 == 278 || LA6_0 == 280 || (LA6_0 >= 282 && LA6_0 <= 283) || (LA6_0 >= 287 && LA6_0 <= 292) || (LA6_0 >= 296 && LA6_0 <= 299) || LA6_0 == 301) {
                alt6 = 1;
            }
            else {
                if (LA6_0 != 165) {
                    final NoViableAltException nvae = new NoViableAltException("", 6, 0, (IntStream)this.input);
                    throw nvae;
                }
                alt6 = 2;
            }
            switch (alt6) {
                case 1: {
                    this.pushFollow(CritereParser.FOLLOW_factor_in_expr147);
                    e1 = this.factor();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    crit = e1;
                    int alt2 = 5;
                    switch (this.input.LA(1)) {
                        case 9: {
                            alt2 = 1;
                            break;
                        }
                        case 279: {
                            alt2 = 2;
                            break;
                        }
                        case 284: {
                            alt2 = 3;
                            break;
                        }
                        case 264: {
                            alt2 = 4;
                            break;
                        }
                    }
                    switch (alt2) {
                        case 1: {
                            this.match((IntStream)this.input, 9, CritereParser.FOLLOW_AND_in_expr164);
                            this.pushFollow(CritereParser.FOLLOW_expr_in_expr170);
                            e2 = this.expr();
                            final RecognizerSharedState state2 = this.state;
                            --state2._fsp;
                            crit = AndCriterion.generate(e1, e2);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 279, CritereParser.FOLLOW_OR_in_expr187);
                            this.pushFollow(CritereParser.FOLLOW_expr_in_expr191);
                            e2 = this.expr();
                            final RecognizerSharedState state3 = this.state;
                            --state3._fsp;
                            crit = OrCriterion.generate(e1, e2);
                            break;
                        }
                        case 3: {
                            this.match((IntStream)this.input, 284, CritereParser.FOLLOW_PLUS_in_expr199);
                            this.pushFollow(CritereParser.FOLLOW_expr_in_expr205);
                            e2 = this.expr();
                            final RecognizerSharedState state4 = this.state;
                            --state4._fsp;
                            crit = AddValue.generate(crit, e2);
                            break;
                        }
                        case 4: {
                            this.match((IntStream)this.input, 264, CritereParser.FOLLOW_MINUS_in_expr214);
                            this.pushFollow(CritereParser.FOLLOW_expr_in_expr220);
                            e2 = this.expr();
                            final RecognizerSharedState state5 = this.state;
                            --state5._fsp;
                            crit = SubstractValue.generate(crit, e2);
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 165, CritereParser.FOLLOW_IF_in_expr230);
                    this.match((IntStream)this.input, 283, CritereParser.FOLLOW_PG_in_expr232);
                    this.pushFollow(CritereParser.FOLLOW_expr_in_expr236);
                    e1 = this.expr();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    this.match((IntStream)this.input, 281, CritereParser.FOLLOW_PD_in_expr238);
                    this.match((IntStream)this.input, 295, CritereParser.FOLLOW_THEN_in_expr240);
                    this.pushFollow(CritereParser.FOLLOW_expr_in_expr246);
                    s1 = this.expr();
                    final RecognizerSharedState state7 = this.state;
                    --state7._fsp;
                    this.match((IntStream)this.input, 25, CritereParser.FOLLOW_ELSE_in_expr248);
                    this.pushFollow(CritereParser.FOLLOW_expr_in_expr252);
                    s2 = this.expr();
                    final RecognizerSharedState state8 = this.state;
                    --state8._fsp;
                    crit = IfCriterion.generate(e1, s1, s2);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return crit;
    }
    
    public final ArrayList<ParserObject> exprlist() throws RecognitionException {
        ArrayList<ParserObject> params = null;
        ParserObject e1 = null;
        ParserObject e2 = null;
        Label_0176: {
            try {
                this.pushFollow(CritereParser.FOLLOW_expr_in_exprlist273);
                e1 = this.expr();
                final RecognizerSharedState state = this.state;
                --state._fsp;
                params = new ArrayList<ParserObject>();
                params.add(e1);
                while (true) {
                    int alt7 = 2;
                    final int LA7_0 = this.input.LA(1);
                    if (LA7_0 == 300) {
                        alt7 = 1;
                    }
                    switch (alt7) {
                        case 1: {
                            this.match((IntStream)this.input, 300, CritereParser.FOLLOW_VIRGULE_in_exprlist278);
                            this.pushFollow(CritereParser.FOLLOW_expr_in_exprlist282);
                            e2 = this.expr();
                            final RecognizerSharedState state2 = this.state;
                            --state2._fsp;
                            params.add(e2);
                            continue;
                        }
                        default: {
                            break Label_0176;
                        }
                    }
                }
            }
            catch (RecognitionException re) {
                this.reportError(re);
                this.recover((IntStream)this.input, re);
            }
        }
        return params;
    }
    
    public final ArrayList<ParserObject> paramlist() throws RecognitionException {
        ArrayList<ParserObject> params = null;
        ArrayList<ParserObject> e = null;
        try {
            int alt8 = 2;
            final int LA8_0 = this.input.LA(1);
            if (LA8_0 != 283) {
                final NoViableAltException nvae = new NoViableAltException("", 8, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA8_ = this.input.LA(2);
            if (LA8_ == 281) {
                alt8 = 2;
            }
            else if ((LA8_ >= 5 && LA8_ <= 8) || (LA8_ >= 11 && LA8_ <= 12) || (LA8_ >= 14 && LA8_ <= 20) || (LA8_ >= 23 && LA8_ <= 24) || (LA8_ >= 28 && LA8_ <= 131) || (LA8_ >= 133 && LA8_ <= 163) || LA8_ == 165 || (LA8_ >= 168 && LA8_ <= 261) || LA8_ == 264 || (LA8_ >= 267 && LA8_ <= 276) || LA8_ == 278 || LA8_ == 280 || (LA8_ >= 282 && LA8_ <= 283) || (LA8_ >= 287 && LA8_ <= 292) || (LA8_ >= 296 && LA8_ <= 299) || LA8_ == 301) {
                alt8 = 1;
            }
            else {
                final int nvaeMark = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae2 = new NoViableAltException("", 8, 1, (IntStream)this.input);
                    throw nvae2;
                }
                finally {
                    this.input.rewind(nvaeMark);
                }
            }
            switch (alt8) {
                case 1: {
                    this.match((IntStream)this.input, 283, CritereParser.FOLLOW_PG_in_paramlist297);
                    this.pushFollow(CritereParser.FOLLOW_exprlist_in_paramlist301);
                    e = this.exprlist();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    params = e;
                    this.match((IntStream)this.input, 281, CritereParser.FOLLOW_PD_in_paramlist305);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 283, CritereParser.FOLLOW_PG_in_paramlist309);
                    this.match((IntStream)this.input, 281, CritereParser.FOLLOW_PD_in_paramlist311);
                    params = new ArrayList<ParserObject>();
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return params;
    }
    
    public final ParserObject numberlist() throws RecognitionException {
        ParserObject list = null;
        ArrayList<ParserObject> e = null;
        try {
            int alt9 = 2;
            final int LA9_0 = this.input.LA(1);
            if (LA9_0 != 15) {
                final NoViableAltException nvae = new NoViableAltException("", 9, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA9_ = this.input.LA(2);
            if (LA9_ == 13) {
                alt9 = 2;
            }
            else if ((LA9_ >= 5 && LA9_ <= 8) || (LA9_ >= 11 && LA9_ <= 12) || (LA9_ >= 14 && LA9_ <= 20) || (LA9_ >= 23 && LA9_ <= 24) || (LA9_ >= 28 && LA9_ <= 131) || (LA9_ >= 133 && LA9_ <= 163) || LA9_ == 165 || (LA9_ >= 168 && LA9_ <= 261) || LA9_ == 264 || (LA9_ >= 267 && LA9_ <= 276) || LA9_ == 278 || LA9_ == 280 || (LA9_ >= 282 && LA9_ <= 283) || (LA9_ >= 287 && LA9_ <= 292) || (LA9_ >= 296 && LA9_ <= 299) || LA9_ == 301) {
                alt9 = 1;
            }
            else {
                final int nvaeMark = this.input.mark();
                try {
                    this.input.consume();
                    final NoViableAltException nvae2 = new NoViableAltException("", 9, 1, (IntStream)this.input);
                    throw nvae2;
                }
                finally {
                    this.input.rewind(nvaeMark);
                }
            }
            switch (alt9) {
                case 1: {
                    this.match((IntStream)this.input, 15, CritereParser.FOLLOW_BG_in_numberlist325);
                    this.pushFollow(CritereParser.FOLLOW_exprlist_in_numberlist329);
                    e = this.exprlist();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 13, CritereParser.FOLLOW_BD_in_numberlist331);
                    list = new ConstantNumberList(e);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 15, CritereParser.FOLLOW_BG_in_numberlist337);
                    this.match((IntStream)this.input, 13, CritereParser.FOLLOW_BD_in_numberlist339);
                    list = new ConstantNumberList(new ArrayList<ParserObject>());
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return list;
    }
    
    public final ParserObject factor() throws RecognitionException {
        ParserObject crit = null;
        ParserObject e = null;
        ParserObject e2 = null;
        try {
            this.pushFollow(CritereParser.FOLLOW_atoms_in_factor357);
            e = this.atoms();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            crit = e;
            int alt10 = 11;
            switch (this.input.LA(1)) {
                case 266: {
                    alt10 = 1;
                    break;
                }
                case 22: {
                    alt10 = 2;
                    break;
                }
                case 265: {
                    alt10 = 3;
                    break;
                }
                case 277: {
                    alt10 = 4;
                    break;
                }
                case 27: {
                    alt10 = 5;
                    break;
                }
                case 166: {
                    alt10 = 6;
                    break;
                }
                case 167: {
                    alt10 = 7;
                    break;
                }
                case 293: {
                    alt10 = 8;
                    break;
                }
                case 294: {
                    alt10 = 9;
                    break;
                }
                case 285: {
                    alt10 = 10;
                    break;
                }
            }
            switch (alt10) {
                case 1: {
                    this.match((IntStream)this.input, 266, CritereParser.FOLLOW_MULT_in_factor364);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor369);
                    e2 = this.factor();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    crit = MultValue.generate(e, e2);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 22, CritereParser.FOLLOW_DIVIDE_in_factor375);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor381);
                    e2 = this.factor();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    crit = DivideValue.generate(e, e2);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 265, CritereParser.FOLLOW_MOD_in_factor388);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor394);
                    e2 = this.factor();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    crit = ModValue.generate(e, e2);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 277, CritereParser.FOLLOW_NOT_EQUALS_in_factor401);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor407);
                    e2 = this.factor();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    crit = NotEqualCriterion.generate(e, e2);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 27, CritereParser.FOLLOW_EQUALS_in_factor414);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor420);
                    e2 = this.factor();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    crit = EqualCriterion.generate(e, e2);
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 166, CritereParser.FOLLOW_INF_in_factor427);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor433);
                    e2 = this.factor();
                    final RecognizerSharedState state7 = this.state;
                    --state7._fsp;
                    crit = InfCriterion.generate(e, e2);
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 167, CritereParser.FOLLOW_INFEQ_in_factor440);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor446);
                    e2 = this.factor();
                    final RecognizerSharedState state8 = this.state;
                    --state8._fsp;
                    crit = InfEqCriterion.generate(e, e2);
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 293, CritereParser.FOLLOW_SUP_in_factor453);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor459);
                    e2 = this.factor();
                    final RecognizerSharedState state9 = this.state;
                    --state9._fsp;
                    crit = InfCriterion.generate(e2, e);
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 294, CritereParser.FOLLOW_SUPEQ_in_factor466);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor472);
                    e2 = this.factor();
                    final RecognizerSharedState state10 = this.state;
                    --state10._fsp;
                    crit = InfEqCriterion.generate(e2, e);
                    break;
                }
                case 10: {
                    this.match((IntStream)this.input, 285, CritereParser.FOLLOW_POINT_in_factor479);
                    this.pushFollow(CritereParser.FOLLOW_factor_in_factor485);
                    e2 = this.factor();
                    final RecognizerSharedState state11 = this.state;
                    --state11._fsp;
                    crit = new ConcatenateList(e, e2);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return crit;
    }
    
    public final ParserObject functioncall() throws RecognitionException {
        ParserObject crit = null;
        ArrayList<ParserObject> p = null;
        try {
            int alt11 = 257;
            switch (this.input.LA(1)) {
                case 130: {
                    alt11 = 1;
                    break;
                }
                case 129: {
                    alt11 = 2;
                    break;
                }
                case 139: {
                    alt11 = 3;
                    break;
                }
                case 33: {
                    alt11 = 4;
                    break;
                }
                case 35: {
                    alt11 = 5;
                    break;
                }
                case 34: {
                    alt11 = 6;
                    break;
                }
                case 191: {
                    alt11 = 7;
                    break;
                }
                case 17: {
                    alt11 = 8;
                    break;
                }
                case 289: {
                    alt11 = 9;
                    break;
                }
                case 296: {
                    alt11 = 10;
                    break;
                }
                case 301: {
                    alt11 = 11;
                    break;
                }
                case 252: {
                    alt11 = 12;
                    break;
                }
                case 280: {
                    alt11 = 13;
                    break;
                }
                case 215: {
                    alt11 = 14;
                    break;
                }
                case 216: {
                    alt11 = 15;
                    break;
                }
                case 75: {
                    alt11 = 16;
                    break;
                }
                case 188: {
                    alt11 = 17;
                    break;
                }
                case 184: {
                    alt11 = 18;
                    break;
                }
                case 182: {
                    alt11 = 19;
                    break;
                }
                case 213: {
                    alt11 = 20;
                    break;
                }
                case 138: {
                    alt11 = 21;
                    break;
                }
                case 67: {
                    alt11 = 22;
                    break;
                }
                case 68: {
                    alt11 = 23;
                    break;
                }
                case 69: {
                    alt11 = 24;
                    break;
                }
                case 72: {
                    alt11 = 25;
                    break;
                }
                case 57: {
                    alt11 = 26;
                    break;
                }
                case 282: {
                    alt11 = 27;
                    break;
                }
                case 291: {
                    alt11 = 28;
                    break;
                }
                case 14: {
                    alt11 = 29;
                    break;
                }
                case 12: {
                    alt11 = 30;
                    break;
                }
                case 123: {
                    alt11 = 31;
                    break;
                }
                case 180: {
                    alt11 = 32;
                    break;
                }
                case 134: {
                    alt11 = 33;
                    break;
                }
                case 63: {
                    alt11 = 34;
                    break;
                }
                case 37: {
                    alt11 = 35;
                    break;
                }
                case 51: {
                    alt11 = 36;
                    break;
                }
                case 214: {
                    alt11 = 37;
                    break;
                }
                case 288: {
                    alt11 = 38;
                    break;
                }
                case 52: {
                    alt11 = 39;
                    break;
                }
                case 47: {
                    alt11 = 40;
                    break;
                }
                case 48: {
                    alt11 = 41;
                    break;
                }
                case 77: {
                    alt11 = 42;
                    break;
                }
                case 118: {
                    alt11 = 43;
                    break;
                }
                case 121: {
                    alt11 = 44;
                    break;
                }
                case 260: {
                    alt11 = 45;
                    break;
                }
                case 53: {
                    alt11 = 46;
                    break;
                }
                case 197: {
                    alt11 = 47;
                    break;
                }
                case 45: {
                    alt11 = 48;
                    break;
                }
                case 46: {
                    alt11 = 49;
                    break;
                }
                case 219: {
                    alt11 = 50;
                    break;
                }
                case 24: {
                    alt11 = 51;
                    break;
                }
                case 141: {
                    alt11 = 52;
                    break;
                }
                case 131: {
                    alt11 = 53;
                    break;
                }
                case 58: {
                    alt11 = 54;
                    break;
                }
                case 136: {
                    alt11 = 55;
                    break;
                }
                case 55: {
                    alt11 = 56;
                    break;
                }
                case 56: {
                    alt11 = 57;
                    break;
                }
                case 183: {
                    alt11 = 58;
                    break;
                }
                case 187: {
                    alt11 = 59;
                    break;
                }
                case 220: {
                    alt11 = 60;
                    break;
                }
                case 179: {
                    alt11 = 61;
                    break;
                }
                case 78: {
                    alt11 = 62;
                    break;
                }
                case 65: {
                    alt11 = 63;
                    break;
                }
                case 49: {
                    alt11 = 64;
                    break;
                }
                case 31: {
                    alt11 = 65;
                    break;
                }
                case 38: {
                    alt11 = 66;
                    break;
                }
                case 44: {
                    alt11 = 67;
                    break;
                }
                case 79: {
                    alt11 = 68;
                    break;
                }
                case 60: {
                    alt11 = 69;
                    break;
                }
                case 59: {
                    alt11 = 70;
                    break;
                }
                case 62: {
                    alt11 = 71;
                    break;
                }
                case 70: {
                    alt11 = 72;
                    break;
                }
                case 43: {
                    alt11 = 73;
                    break;
                }
                case 193: {
                    alt11 = 74;
                    break;
                }
                case 41: {
                    alt11 = 75;
                    break;
                }
                case 189: {
                    alt11 = 76;
                    break;
                }
                case 66: {
                    alt11 = 77;
                    break;
                }
                case 32: {
                    alt11 = 78;
                    break;
                }
                case 42: {
                    alt11 = 79;
                    break;
                }
                case 73: {
                    alt11 = 80;
                    break;
                }
                case 64: {
                    alt11 = 81;
                    break;
                }
                case 108: {
                    alt11 = 82;
                    break;
                }
                case 133: {
                    alt11 = 83;
                    break;
                }
                case 217: {
                    alt11 = 84;
                    break;
                }
                case 256: {
                    alt11 = 85;
                    break;
                }
                case 185: {
                    alt11 = 86;
                    break;
                }
                case 186: {
                    alt11 = 87;
                    break;
                }
                case 126: {
                    alt11 = 88;
                    break;
                }
                case 292: {
                    alt11 = 89;
                    break;
                }
                case 261: {
                    alt11 = 90;
                    break;
                }
                case 74: {
                    alt11 = 91;
                    break;
                }
                case 209: {
                    alt11 = 92;
                    break;
                }
                case 36: {
                    alt11 = 93;
                    break;
                }
                case 39: {
                    alt11 = 94;
                    break;
                }
                case 127: {
                    alt11 = 95;
                    break;
                }
                case 40: {
                    alt11 = 96;
                    break;
                }
                case 128: {
                    alt11 = 97;
                    break;
                }
                case 210: {
                    alt11 = 98;
                    break;
                }
                case 16: {
                    alt11 = 99;
                    break;
                }
                case 76: {
                    alt11 = 100;
                    break;
                }
                case 61: {
                    alt11 = 101;
                    break;
                }
                case 192: {
                    alt11 = 102;
                    break;
                }
                case 195: {
                    alt11 = 103;
                    break;
                }
                case 207: {
                    alt11 = 104;
                    break;
                }
                case 181: {
                    alt11 = 105;
                    break;
                }
                case 218: {
                    alt11 = 106;
                    break;
                }
                case 204: {
                    alt11 = 107;
                    break;
                }
                case 203: {
                    alt11 = 108;
                    break;
                }
                case 208: {
                    alt11 = 109;
                    break;
                }
                case 140: {
                    alt11 = 110;
                    break;
                }
                case 267: {
                    alt11 = 111;
                    break;
                }
                case 174: {
                    alt11 = 112;
                    break;
                }
                case 175: {
                    alt11 = 113;
                    break;
                }
                case 18: {
                    alt11 = 114;
                    break;
                }
                case 278: {
                    alt11 = 115;
                    break;
                }
                case 135: {
                    alt11 = 116;
                    break;
                }
                case 172: {
                    alt11 = 117;
                    break;
                }
                case 171: {
                    alt11 = 118;
                    break;
                }
                case 176: {
                    alt11 = 119;
                    break;
                }
                case 173: {
                    alt11 = 120;
                    break;
                }
                case 211: {
                    alt11 = 121;
                    break;
                }
                case 199: {
                    alt11 = 122;
                    break;
                }
                case 240: {
                    alt11 = 123;
                    break;
                }
                case 246: {
                    alt11 = 124;
                    break;
                }
                case 238: {
                    alt11 = 125;
                    break;
                }
                case 205: {
                    alt11 = 126;
                    break;
                }
                case 196: {
                    alt11 = 127;
                    break;
                }
                case 177: {
                    alt11 = 128;
                    break;
                }
                case 178: {
                    alt11 = 129;
                    break;
                }
                case 200: {
                    alt11 = 130;
                    break;
                }
                case 201: {
                    alt11 = 131;
                    break;
                }
                case 202: {
                    alt11 = 132;
                    break;
                }
                case 20: {
                    alt11 = 133;
                    break;
                }
                case 206: {
                    alt11 = 134;
                    break;
                }
                case 190: {
                    alt11 = 135;
                    break;
                }
                case 71: {
                    alt11 = 136;
                    break;
                }
                case 194: {
                    alt11 = 137;
                    break;
                }
                case 169: {
                    alt11 = 138;
                    break;
                }
                case 198: {
                    alt11 = 139;
                    break;
                }
                case 50: {
                    alt11 = 140;
                    break;
                }
                case 157: {
                    alt11 = 141;
                    break;
                }
                case 158: {
                    alt11 = 142;
                    break;
                }
                case 156: {
                    alt11 = 143;
                    break;
                }
                case 222: {
                    alt11 = 144;
                    break;
                }
                case 112: {
                    alt11 = 145;
                    break;
                }
                case 98: {
                    alt11 = 146;
                    break;
                }
                case 7: {
                    alt11 = 147;
                    break;
                }
                case 8: {
                    alt11 = 148;
                    break;
                }
                case 6: {
                    alt11 = 149;
                    break;
                }
                case 111: {
                    alt11 = 150;
                    break;
                }
                case 254: {
                    alt11 = 151;
                    break;
                }
                case 255: {
                    alt11 = 152;
                    break;
                }
                case 231: {
                    alt11 = 153;
                    break;
                }
                case 88: {
                    alt11 = 154;
                    break;
                }
                case 109: {
                    alt11 = 155;
                    break;
                }
                case 86: {
                    alt11 = 156;
                    break;
                }
                case 150: {
                    alt11 = 157;
                    break;
                }
                case 91: {
                    alt11 = 158;
                    break;
                }
                case 257: {
                    alt11 = 159;
                    break;
                }
                case 152: {
                    alt11 = 160;
                    break;
                }
                case 146: {
                    alt11 = 161;
                    break;
                }
                case 227: {
                    alt11 = 162;
                    break;
                }
                case 153: {
                    alt11 = 163;
                    break;
                }
                case 23: {
                    alt11 = 164;
                    break;
                }
                case 163: {
                    alt11 = 165;
                    break;
                }
                case 247: {
                    alt11 = 166;
                    break;
                }
                case 242: {
                    alt11 = 167;
                    break;
                }
                case 274: {
                    alt11 = 168;
                    break;
                }
                case 147: {
                    alt11 = 169;
                    break;
                }
                case 148: {
                    alt11 = 170;
                    break;
                }
                case 233: {
                    alt11 = 171;
                    break;
                }
                case 270: {
                    alt11 = 172;
                    break;
                }
                case 30: {
                    alt11 = 173;
                    break;
                }
                case 83: {
                    alt11 = 174;
                    break;
                }
                case 89: {
                    alt11 = 175;
                    break;
                }
                case 90: {
                    alt11 = 176;
                    break;
                }
                case 19: {
                    alt11 = 177;
                    break;
                }
                case 245: {
                    alt11 = 178;
                    break;
                }
                case 228: {
                    alt11 = 179;
                    break;
                }
                case 170: {
                    alt11 = 180;
                    break;
                }
                case 221: {
                    alt11 = 181;
                    break;
                }
                case 117: {
                    alt11 = 182;
                    break;
                }
                case 116: {
                    alt11 = 183;
                    break;
                }
                case 99: {
                    alt11 = 184;
                    break;
                }
                case 244: {
                    alt11 = 185;
                    break;
                }
                case 273: {
                    alt11 = 186;
                    break;
                }
                case 275: {
                    alt11 = 187;
                    break;
                }
                case 248: {
                    alt11 = 188;
                    break;
                }
                case 85: {
                    alt11 = 189;
                    break;
                }
                case 92: {
                    alt11 = 190;
                    break;
                }
                case 268: {
                    alt11 = 191;
                    break;
                }
                case 272: {
                    alt11 = 192;
                    break;
                }
                case 120: {
                    alt11 = 193;
                    break;
                }
                case 115: {
                    alt11 = 194;
                    break;
                }
                case 253: {
                    alt11 = 195;
                    break;
                }
                case 119: {
                    alt11 = 196;
                    break;
                }
                case 151: {
                    alt11 = 197;
                    break;
                }
                case 249: {
                    alt11 = 198;
                    break;
                }
                case 230: {
                    alt11 = 199;
                    break;
                }
                case 226: {
                    alt11 = 200;
                    break;
                }
                case 223: {
                    alt11 = 201;
                    break;
                }
                case 159: {
                    alt11 = 202;
                    break;
                }
                case 100: {
                    alt11 = 203;
                    break;
                }
                case 239: {
                    alt11 = 204;
                    break;
                }
                case 101: {
                    alt11 = 205;
                    break;
                }
                case 237: {
                    alt11 = 206;
                    break;
                }
                case 162: {
                    alt11 = 207;
                    break;
                }
                case 234: {
                    alt11 = 208;
                    break;
                }
                case 144: {
                    alt11 = 209;
                    break;
                }
                case 122: {
                    alt11 = 210;
                    break;
                }
                case 124: {
                    alt11 = 211;
                    break;
                }
                case 125: {
                    alt11 = 212;
                    break;
                }
                case 269: {
                    alt11 = 213;
                    break;
                }
                case 87: {
                    alt11 = 214;
                    break;
                }
                case 106: {
                    alt11 = 215;
                    break;
                }
                case 107: {
                    alt11 = 216;
                    break;
                }
                case 114: {
                    alt11 = 217;
                    break;
                }
                case 81: {
                    alt11 = 218;
                    break;
                }
                case 225: {
                    alt11 = 219;
                    break;
                }
                case 110: {
                    alt11 = 220;
                    break;
                }
                case 243: {
                    alt11 = 221;
                    break;
                }
                case 142: {
                    alt11 = 222;
                    break;
                }
                case 241: {
                    alt11 = 223;
                    break;
                }
                case 154: {
                    alt11 = 224;
                    break;
                }
                case 102: {
                    alt11 = 225;
                    break;
                }
                case 229: {
                    alt11 = 226;
                    break;
                }
                case 149: {
                    alt11 = 227;
                    break;
                }
                case 103: {
                    alt11 = 228;
                    break;
                }
                case 258: {
                    alt11 = 229;
                    break;
                }
                case 259: {
                    alt11 = 230;
                    break;
                }
                case 82: {
                    alt11 = 231;
                    break;
                }
                case 97: {
                    alt11 = 232;
                    break;
                }
                case 96: {
                    alt11 = 233;
                    break;
                }
                case 94: {
                    alt11 = 234;
                    break;
                }
                case 95: {
                    alt11 = 235;
                    break;
                }
                case 250: {
                    alt11 = 236;
                    break;
                }
                case 160: {
                    alt11 = 237;
                    break;
                }
                case 224: {
                    alt11 = 238;
                    break;
                }
                case 104: {
                    alt11 = 239;
                    break;
                }
                case 145: {
                    alt11 = 240;
                    break;
                }
                case 105: {
                    alt11 = 241;
                    break;
                }
                case 93: {
                    alt11 = 242;
                    break;
                }
                case 155: {
                    alt11 = 243;
                    break;
                }
                case 212: {
                    alt11 = 244;
                    break;
                }
                case 137: {
                    alt11 = 245;
                    break;
                }
                case 232: {
                    alt11 = 246;
                    break;
                }
                case 251: {
                    alt11 = 247;
                    break;
                }
                case 54: {
                    alt11 = 248;
                    break;
                }
                case 80: {
                    alt11 = 249;
                    break;
                }
                case 236: {
                    alt11 = 250;
                    break;
                }
                case 143: {
                    alt11 = 251;
                    break;
                }
                case 271: {
                    alt11 = 252;
                    break;
                }
                case 113: {
                    alt11 = 253;
                    break;
                }
                case 161: {
                    alt11 = 254;
                    break;
                }
                case 298: {
                    alt11 = 255;
                    break;
                }
                case 84: {
                    alt11 = 256;
                    break;
                }
                case 235: {
                    alt11 = 257;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 11, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt11) {
                case 1: {
                    this.match((IntStream)this.input, 130, CritereParser.FOLLOW_HASEQTYPE_in_functioncall504);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall508);
                    p = this.paramlist();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    crit = new HasEquipmentType(p);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 129, CritereParser.FOLLOW_HASEQID_in_functioncall514);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall518);
                    p = this.paramlist();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    crit = new HasEquipmentId(p);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 139, CritereParser.FOLLOW_HASSUMMONS_in_functioncall524);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall528);
                    p = this.paramlist();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    crit = new CharacterHasSummons(p);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 33, CritereParser.FOLLOW_GETCHA_in_functioncall534);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall538);
                    p = this.paramlist();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    crit = new GetCharacteristic(p);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 35, CritereParser.FOLLOW_GETCHAPCT_in_functioncall544);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall548);
                    p = this.paramlist();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    crit = new GetCharacteristicInPct(p);
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 34, CritereParser.FOLLOW_GETCHAMAX_in_functioncall554);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall558);
                    p = this.paramlist();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    crit = new GetCharacteristicMax(p);
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 191, CritereParser.FOLLOW_ISENNEMY_in_functioncall564);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall568);
                    p = this.paramlist();
                    final RecognizerSharedState state7 = this.state;
                    --state7._fsp;
                    crit = new IsEnemy(p);
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 17, CritereParser.FOLLOW_CANCARRYTARGET_in_functioncall574);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall578);
                    p = this.paramlist();
                    final RecognizerSharedState state8 = this.state;
                    --state8._fsp;
                    crit = new CanCarryTarget(p);
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 289, CritereParser.FOLLOW_SPACEINSYMBIOT_in_functioncall584);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall588);
                    p = this.paramlist();
                    final RecognizerSharedState state9 = this.state;
                    --state9._fsp;
                    crit = new GetSpaceInSymbiot(p);
                    break;
                }
                case 10: {
                    this.match((IntStream)this.input, 296, CritereParser.FOLLOW_TRAPAMOUNT_in_functioncall594);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall598);
                    p = this.paramlist();
                    final RecognizerSharedState state10 = this.state;
                    --state10._fsp;
                    crit = new NbTraps(p);
                    break;
                }
                case 11: {
                    this.match((IntStream)this.input, 301, CritereParser.FOLLOW_WALLAMOUNT_in_functioncall604);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall608);
                    p = this.paramlist();
                    final RecognizerSharedState state11 = this.state;
                    --state11._fsp;
                    crit = new NbWalls(p);
                    break;
                }
                case 12: {
                    this.match((IntStream)this.input, 252, CritereParser.FOLLOW_IS_SELECTED_CREATURE_AVAILABLE_in_functioncall614);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall618);
                    p = this.paramlist();
                    final RecognizerSharedState state12 = this.state;
                    --state12._fsp;
                    crit = new IsSelectedCreatureAvailable(p);
                    break;
                }
                case 13: {
                    this.match((IntStream)this.input, 280, CritereParser.FOLLOW_OWNSBEACON_in_functioncall623);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall627);
                    p = this.paramlist();
                    final RecognizerSharedState state13 = this.state;
                    --state13._fsp;
                    crit = new OwnsTargetBeacon(p);
                    break;
                }
                case 14: {
                    this.match((IntStream)this.input, 215, CritereParser.FOLLOW_ISSPECIFICAREA_in_functioncall633);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall637);
                    p = this.paramlist();
                    final RecognizerSharedState state14 = this.state;
                    --state14._fsp;
                    crit = new IsSpecificArea(p);
                    break;
                }
                case 15: {
                    this.match((IntStream)this.input, 216, CritereParser.FOLLOW_ISSPECIFICAREAWITHSPECIFICSTATE_in_functioncall643);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall647);
                    p = this.paramlist();
                    final RecognizerSharedState state15 = this.state;
                    --state15._fsp;
                    crit = new IsSpecificAreaWithSpecificState(p);
                    break;
                }
                case 16: {
                    this.match((IntStream)this.input, 75, CritereParser.FOLLOW_GETTIME_in_functioncall653);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall659);
                    p = this.paramlist();
                    final RecognizerSharedState state16 = this.state;
                    --state16._fsp;
                    crit = new GetHour(p);
                    break;
                }
                case 17: {
                    this.match((IntStream)this.input, 188, CritereParser.FOLLOW_ISDAY_in_functioncall665);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall671);
                    p = this.paramlist();
                    final RecognizerSharedState state17 = this.state;
                    --state17._fsp;
                    crit = new IsDay(p);
                    break;
                }
                case 18: {
                    this.match((IntStream)this.input, 184, CritereParser.FOLLOW_ISBREEDID_in_functioncall677);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall681);
                    p = this.paramlist();
                    final RecognizerSharedState state18 = this.state;
                    --state18._fsp;
                    crit = new IsBreedId(p);
                    break;
                }
                case 19: {
                    this.match((IntStream)this.input, 182, CritereParser.FOLLOW_ISBREED_in_functioncall686);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall690);
                    p = this.paramlist();
                    final RecognizerSharedState state19 = this.state;
                    --state19._fsp;
                    crit = new IsBreed(p);
                    break;
                }
                case 20: {
                    this.match((IntStream)this.input, 213, CritereParser.FOLLOW_ISSEASON_in_functioncall695);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall701);
                    p = this.paramlist();
                    final RecognizerSharedState state20 = this.state;
                    --state20._fsp;
                    crit = new IsSeason(p);
                    break;
                }
                case 21: {
                    this.match((IntStream)this.input, 138, CritereParser.FOLLOW_HASSTATE_in_functioncall706);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall710);
                    p = this.paramlist();
                    final RecognizerSharedState state21 = this.state;
                    --state21._fsp;
                    crit = new HasState(p);
                    break;
                }
                case 22: {
                    this.match((IntStream)this.input, 67, CritereParser.FOLLOW_GETSKILLLEVEL_in_functioncall715);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall719);
                    p = this.paramlist();
                    final RecognizerSharedState state22 = this.state;
                    --state22._fsp;
                    crit = new GetSkillLevel(p);
                    break;
                }
                case 23: {
                    this.match((IntStream)this.input, 68, CritereParser.FOLLOW_GETSPELLLEVEL_in_functioncall724);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall728);
                    p = this.paramlist();
                    final RecognizerSharedState state23 = this.state;
                    --state23._fsp;
                    crit = new GetSpellLevel(p);
                    break;
                }
                case 24: {
                    this.match((IntStream)this.input, 69, CritereParser.FOLLOW_GETSPELLTREELEVEL_in_functioncall733);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall737);
                    p = this.paramlist();
                    final RecognizerSharedState state24 = this.state;
                    --state24._fsp;
                    crit = new GetSpellTreeLevel(p);
                    break;
                }
                case 25: {
                    this.match((IntStream)this.input, 72, CritereParser.FOLLOW_GETTEAMID_in_functioncall742);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall746);
                    p = this.paramlist();
                    final RecognizerSharedState state25 = this.state;
                    --state25._fsp;
                    crit = new GetTeamId(p);
                    break;
                }
                case 26: {
                    this.match((IntStream)this.input, 57, CritereParser.FOLLOW_GETMONST_in_functioncall751);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall755);
                    p = this.paramlist();
                    final RecognizerSharedState state26 = this.state;
                    --state26._fsp;
                    crit = new GetMonstersInFight(p);
                    break;
                }
                case 27: {
                    this.match((IntStream)this.input, 282, CritereParser.FOLLOW_PETWITHINRANGE_in_functioncall761);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall765);
                    p = this.paramlist();
                    final RecognizerSharedState state27 = this.state;
                    --state27._fsp;
                    crit = new PetWithinRange(p);
                    break;
                }
                case 28: {
                    this.match((IntStream)this.input, 291, CritereParser.FOLLOW_SUMMONAMOUNT_in_functioncall771);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall775);
                    p = this.paramlist();
                    final RecognizerSharedState state28 = this.state;
                    --state28._fsp;
                    crit = new NbSummons(p);
                    break;
                }
                case 29: {
                    this.match((IntStream)this.input, 14, CritereParser.FOLLOW_BEACONAMOUNT_in_functioncall781);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall785);
                    p = this.paramlist();
                    final RecognizerSharedState state29 = this.state;
                    --state29._fsp;
                    crit = new NbBeacons(p);
                    break;
                }
                case 30: {
                    this.match((IntStream)this.input, 12, CritereParser.FOLLOW_BARRELAMOUNT_in_functioncall791);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall795);
                    p = this.paramlist();
                    final RecognizerSharedState state30 = this.state;
                    --state30._fsp;
                    crit = new NbBarrels(p);
                    break;
                }
                case 31: {
                    this.match((IntStream)this.input, 123, CritereParser.FOLLOW_GET_XELOR_DIALS_COUNT_in_functioncall801);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall805);
                    p = this.paramlist();
                    final RecognizerSharedState state31 = this.state;
                    --state31._fsp;
                    crit = new NbXelorDials(p);
                    break;
                }
                case 32: {
                    this.match((IntStream)this.input, 180, CritereParser.FOLLOW_ISBACKSTAB_in_functioncall811);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall815);
                    p = this.paramlist();
                    final RecognizerSharedState state32 = this.state;
                    --state32._fsp;
                    crit = new IsBackstabbed(p);
                    break;
                }
                case 33: {
                    this.match((IntStream)this.input, 134, CritereParser.FOLLOW_HASLINEOFSIGHT_in_functioncall820);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall824);
                    p = this.paramlist();
                    final RecognizerSharedState state33 = this.state;
                    --state33._fsp;
                    crit = new HasLineOfSight(p);
                    break;
                }
                case 34: {
                    this.match((IntStream)this.input, 63, CritereParser.FOLLOW_GETPOSITION_in_functioncall829);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall833);
                    p = this.paramlist();
                    final RecognizerSharedState state34 = this.state;
                    --state34._fsp;
                    crit = new GetPosition(p);
                    break;
                }
                case 35: {
                    this.match((IntStream)this.input, 37, CritereParser.FOLLOW_GETCHARACTERID_in_functioncall838);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall842);
                    p = this.paramlist();
                    final RecognizerSharedState state35 = this.state;
                    --state35._fsp;
                    crit = new GetCharacterId(p);
                    break;
                }
                case 36: {
                    this.match((IntStream)this.input, 51, CritereParser.FOLLOW_GETIEPOSITION_in_functioncall847);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall851);
                    p = this.paramlist();
                    final RecognizerSharedState state36 = this.state;
                    --state36._fsp;
                    crit = new GetIEPosition(p);
                    break;
                }
                case 37: {
                    this.match((IntStream)this.input, 214, CritereParser.FOLLOW_ISSEX_in_functioncall856);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall862);
                    p = this.paramlist();
                    final RecognizerSharedState state37 = this.state;
                    --state37._fsp;
                    crit = new IsSex(p);
                    break;
                }
                case 38: {
                    this.match((IntStream)this.input, 288, CritereParser.FOLLOW_SLOTSINBAG_in_functioncall867);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall873);
                    p = this.paramlist();
                    final RecognizerSharedState state38 = this.state;
                    --state38._fsp;
                    crit = new RemainingSlotsInBags(p);
                    break;
                }
                case 39: {
                    this.match((IntStream)this.input, 52, CritereParser.FOLLOW_GETINSTANCEID_in_functioncall878);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall882);
                    p = this.paramlist();
                    final RecognizerSharedState state39 = this.state;
                    --state39._fsp;
                    crit = new GetInstanceId(p);
                    break;
                }
                case 40: {
                    this.match((IntStream)this.input, 47, CritereParser.FOLLOW_GETEFFECTCASTER_in_functioncall887);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall891);
                    p = this.paramlist();
                    final RecognizerSharedState state40 = this.state;
                    --state40._fsp;
                    crit = new GetEffectCaster(p);
                    break;
                }
                case 41: {
                    this.match((IntStream)this.input, 48, CritereParser.FOLLOW_GETEFFECTTARGET_in_functioncall896);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall900);
                    p = this.paramlist();
                    final RecognizerSharedState state41 = this.state;
                    --state41._fsp;
                    crit = new GetEffectTarget(p);
                    break;
                }
                case 42: {
                    this.match((IntStream)this.input, 77, CritereParser.FOLLOW_GETTRIGGEREREFFECTCASTER_in_functioncall905);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall909);
                    p = this.paramlist();
                    final RecognizerSharedState state42 = this.state;
                    --state42._fsp;
                    crit = new GetTriggererEffectCaster(p);
                    break;
                }
                case 43: {
                    this.match((IntStream)this.input, 118, CritereParser.FOLLOW_GET_TRIGGERING_EFFECT_ID_in_functioncall914);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall918);
                    p = this.paramlist();
                    final RecognizerSharedState state43 = this.state;
                    --state43._fsp;
                    crit = new GetTriggeringEffectId(p);
                    break;
                }
                case 44: {
                    this.match((IntStream)this.input, 121, CritereParser.FOLLOW_GET_TRIGGERING_EFFECT_VALUE_in_functioncall923);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall927);
                    p = this.paramlist();
                    final RecognizerSharedState state44 = this.state;
                    --state44._fsp;
                    crit = new GetTriggeringEffectValue(p);
                    break;
                }
                case 45: {
                    this.match((IntStream)this.input, 260, CritereParser.FOLLOW_ITEMQUANTITY_in_functioncall932);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall936);
                    p = this.paramlist();
                    final RecognizerSharedState state45 = this.state;
                    --state45._fsp;
                    crit = new GetItemQuantity(p);
                    break;
                }
                case 46: {
                    this.match((IntStream)this.input, 53, CritereParser.FOLLOW_GETKAMASCOUNT_in_functioncall941);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall945);
                    p = this.paramlist();
                    final RecognizerSharedState state46 = this.state;
                    --state46._fsp;
                    crit = new GetKamasCount(p);
                    break;
                }
                case 47: {
                    this.match((IntStream)this.input, 197, CritereParser.FOLLOW_ISMONSTERBREED_in_functioncall950);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall954);
                    p = this.paramlist();
                    final RecognizerSharedState state47 = this.state;
                    --state47._fsp;
                    crit = new IsMonsterBreed(p);
                    break;
                }
                case 48: {
                    this.match((IntStream)this.input, 45, CritereParser.FOLLOW_GETDISTANCEBETWEENCASTERANDTARGET_in_functioncall959);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall963);
                    p = this.paramlist();
                    final RecognizerSharedState state48 = this.state;
                    --state48._fsp;
                    crit = new GetDistanceBetweenCasterAndTarget(p);
                    break;
                }
                case 49: {
                    this.match((IntStream)this.input, 46, CritereParser.FOLLOW_GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON_in_functioncall968);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall972);
                    p = this.paramlist();
                    final RecognizerSharedState state49 = this.state;
                    --state49._fsp;
                    crit = new GetDistanceBetweenTargetAndNearestAllyBeacon(p);
                    break;
                }
                case 50: {
                    this.match((IntStream)this.input, 219, CritereParser.FOLLOW_ISUNDEAD_in_functioncall977);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall981);
                    p = this.paramlist();
                    final RecognizerSharedState state50 = this.state;
                    --state50._fsp;
                    crit = new IsUndead(p);
                    break;
                }
                case 51: {
                    this.match((IntStream)this.input, 24, CritereParser.FOLLOW_EFFECTISFROMHEAL_in_functioncall986);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall990);
                    p = this.paramlist();
                    final RecognizerSharedState state51 = this.state;
                    --state51._fsp;
                    crit = new EffectGeneratedByHeal(p);
                    break;
                }
                case 52: {
                    this.match((IntStream)this.input, 141, CritereParser.FOLLOW_HASWORLDPROPERTY_in_functioncall995);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall999);
                    p = this.paramlist();
                    final RecognizerSharedState state52 = this.state;
                    --state52._fsp;
                    crit = new HasWorldProperty(p);
                    break;
                }
                case 53: {
                    this.match((IntStream)this.input, 131, CritereParser.FOLLOW_HASFIGHTPROPERTY_in_functioncall1004);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1008);
                    p = this.paramlist();
                    final RecognizerSharedState state53 = this.state;
                    --state53._fsp;
                    crit = new HasFightProperty(p);
                    break;
                }
                case 54: {
                    this.match((IntStream)this.input, 58, CritereParser.FOLLOW_GETMONTH_in_functioncall1013);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1017);
                    p = this.paramlist();
                    final RecognizerSharedState state54 = this.state;
                    --state54._fsp;
                    crit = new GetMonth(p);
                    break;
                }
                case 55: {
                    this.match((IntStream)this.input, 136, CritereParser.FOLLOW_HASNTEVOLVEDSINCE_in_functioncall1022);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1026);
                    p = this.paramlist();
                    final RecognizerSharedState state55 = this.state;
                    --state55._fsp;
                    crit = new HasntEvolvedSince(p);
                    break;
                }
                case 56: {
                    this.match((IntStream)this.input, 55, CritereParser.FOLLOW_GETLEVEL_in_functioncall1031);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1037);
                    p = this.paramlist();
                    final RecognizerSharedState state56 = this.state;
                    --state56._fsp;
                    crit = new GetLevel(p);
                    break;
                }
                case 57: {
                    this.match((IntStream)this.input, 56, CritereParser.FOLLOW_GETLOCKINCREMENT_in_functioncall1042);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1048);
                    p = this.paramlist();
                    final RecognizerSharedState state57 = this.state;
                    --state57._fsp;
                    crit = new GetLockIncrement(p);
                    break;
                }
                case 58: {
                    this.match((IntStream)this.input, 183, CritereParser.FOLLOW_ISBREEDFAMILY_in_functioncall1053);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1059);
                    p = this.paramlist();
                    final RecognizerSharedState state58 = this.state;
                    --state58._fsp;
                    crit = new IsBreedFamily(p);
                    break;
                }
                case 59: {
                    this.match((IntStream)this.input, 187, CritereParser.FOLLOW_ISCHALLENGEUSER_in_functioncall1065);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1071);
                    p = this.paramlist();
                    final RecognizerSharedState state59 = this.state;
                    --state59._fsp;
                    crit = new IsChallengeUser(p);
                    break;
                }
                case 60: {
                    this.match((IntStream)this.input, 220, CritereParser.FOLLOW_ISUNDERCONTROL_in_functioncall1076);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1082);
                    p = this.paramlist();
                    final RecognizerSharedState state60 = this.state;
                    --state60._fsp;
                    crit = new IsUnderControl(p);
                    break;
                }
                case 61: {
                    this.match((IntStream)this.input, 179, CritereParser.FOLLOW_ISAFTER_in_functioncall1088);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1094);
                    p = this.paramlist();
                    final RecognizerSharedState state61 = this.state;
                    --state61._fsp;
                    crit = new IsAfter(p);
                    break;
                }
                case 62: {
                    this.match((IntStream)this.input, 78, CritereParser.FOLLOW_GETWAKFUGAUGE_in_functioncall1100);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1106);
                    p = this.paramlist();
                    final RecognizerSharedState state62 = this.state;
                    --state62._fsp;
                    crit = new GetWakfuGauge(p);
                    break;
                }
                case 63: {
                    this.match((IntStream)this.input, 65, CritereParser.FOLLOW_GETRANDOMNUMBER_in_functioncall1112);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1118);
                    p = this.paramlist();
                    final RecognizerSharedState state63 = this.state;
                    --state63._fsp;
                    crit = new GetRandomNumber(p);
                    break;
                }
                case 64: {
                    this.match((IntStream)this.input, 49, CritereParser.FOLLOW_GETENNEMYCOUNTINRANGE_in_functioncall1124);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1130);
                    p = this.paramlist();
                    final RecognizerSharedState state64 = this.state;
                    --state64._fsp;
                    crit = new GetEnnemyCountInRange(p);
                    break;
                }
                case 65: {
                    this.match((IntStream)this.input, 31, CritereParser.FOLLOW_GETALLIESCOUNTINRANGE_in_functioncall1136);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1142);
                    p = this.paramlist();
                    final RecognizerSharedState state65 = this.state;
                    --state65._fsp;
                    crit = new GetAlliesCountInRange(p);
                    break;
                }
                case 66: {
                    this.match((IntStream)this.input, 38, CritereParser.FOLLOW_GETCONTROLLERINSAMETEAMCOUNTINRANGE_in_functioncall1148);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1154);
                    p = this.paramlist();
                    final RecognizerSharedState state66 = this.state;
                    --state66._fsp;
                    crit = new GetControllerInSameTeamCountInRange(p);
                    break;
                }
                case 67: {
                    this.match((IntStream)this.input, 44, CritereParser.FOLLOW_GETDESTRUCTIBLECOUNTINRANGE_in_functioncall1160);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1166);
                    p = this.paramlist();
                    final RecognizerSharedState state67 = this.state;
                    --state67._fsp;
                    crit = new GetDestructibleCountInRange(p);
                    break;
                }
                case 68: {
                    this.match((IntStream)this.input, 79, CritereParser.FOLLOW_GETWALLCOUNTINRANGE_in_functioncall1172);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1178);
                    p = this.paramlist();
                    final RecognizerSharedState state68 = this.state;
                    --state68._fsp;
                    crit = new GetWallCountInRange(p);
                    break;
                }
                case 69: {
                    this.match((IntStream)this.input, 60, CritereParser.FOLLOW_GETNATIONID_in_functioncall1184);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1188);
                    p = this.paramlist();
                    final RecognizerSharedState state69 = this.state;
                    --state69._fsp;
                    crit = new GetNationId(p);
                    break;
                }
                case 70: {
                    this.match((IntStream)this.input, 59, CritereParser.FOLLOW_GETNATIONALIGNMENT_in_functioncall1193);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1197);
                    p = this.paramlist();
                    final RecognizerSharedState state70 = this.state;
                    --state70._fsp;
                    crit = new GetNationAlignment(p);
                    break;
                }
                case 71: {
                    this.match((IntStream)this.input, 62, CritereParser.FOLLOW_GETNATIVENATIONID_in_functioncall1203);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1207);
                    p = this.paramlist();
                    final RecognizerSharedState state71 = this.state;
                    --state71._fsp;
                    crit = new GetNativeNationId(p);
                    break;
                }
                case 72: {
                    this.match((IntStream)this.input, 70, CritereParser.FOLLOW_GETSTASISGAUGE_in_functioncall1212);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1218);
                    p = this.paramlist();
                    final RecognizerSharedState state72 = this.state;
                    --state72._fsp;
                    crit = new GetStasisGauge(p);
                    break;
                }
                case 73: {
                    this.match((IntStream)this.input, 43, CritereParser.FOLLOW_GETDATE_in_functioncall1224);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1230);
                    p = this.paramlist();
                    final RecognizerSharedState state73 = this.state;
                    --state73._fsp;
                    crit = new GetDate(p);
                    break;
                }
                case 74: {
                    this.match((IntStream)this.input, 193, CritereParser.FOLLOW_ISFACESTABBED_in_functioncall1236);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1242);
                    p = this.paramlist();
                    final RecognizerSharedState state74 = this.state;
                    --state74._fsp;
                    crit = new IsFacestabbed(p);
                    break;
                }
                case 75: {
                    this.match((IntStream)this.input, 41, CritereParser.FOLLOW_GETCRIMESCORE_in_functioncall1248);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1254);
                    p = this.paramlist();
                    final RecognizerSharedState state75 = this.state;
                    --state75._fsp;
                    crit = new GetCrimeScore(p);
                    break;
                }
                case 76: {
                    this.match((IntStream)this.input, 189, CritereParser.FOLLOW_ISDEAD_in_functioncall1260);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1266);
                    p = this.paramlist();
                    final RecognizerSharedState state76 = this.state;
                    --state76._fsp;
                    crit = new IsDead(p);
                    break;
                }
                case 77: {
                    this.match((IntStream)this.input, 66, CritereParser.FOLLOW_GETSATISFACTIONLEVEL_in_functioncall1272);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1278);
                    p = this.paramlist();
                    final RecognizerSharedState state77 = this.state;
                    --state77._fsp;
                    crit = new GetSatisfactionLevel(p);
                    break;
                }
                case 78: {
                    this.match((IntStream)this.input, 32, CritereParser.FOLLOW_GETBOOLEANVALUE_in_functioncall1284);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1290);
                    p = this.paramlist();
                    final RecognizerSharedState state78 = this.state;
                    --state78._fsp;
                    crit = new GetBooleanValue(p);
                    break;
                }
                case 79: {
                    this.match((IntStream)this.input, 42, CritereParser.FOLLOW_GETCURRENTPARTITIONNATIONID_in_functioncall1296);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1302);
                    p = this.paramlist();
                    final RecognizerSharedState state79 = this.state;
                    --state79._fsp;
                    crit = new GetCurrentPartitionNationId(p);
                    break;
                }
                case 80: {
                    this.match((IntStream)this.input, 73, CritereParser.FOLLOW_GETTERRITORYID_in_functioncall1308);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1314);
                    p = this.paramlist();
                    final RecognizerSharedState state80 = this.state;
                    --state80._fsp;
                    crit = new GetTerritoryId(p);
                    break;
                }
                case 81: {
                    this.match((IntStream)this.input, 64, CritereParser.FOLLOW_GETPROTECTORNATIONID_in_functioncall1321);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1327);
                    p = this.paramlist();
                    final RecognizerSharedState state81 = this.state;
                    --state81._fsp;
                    crit = new GetProtectorNationId(p);
                    break;
                }
                case 82: {
                    this.match((IntStream)this.input, 108, CritereParser.FOLLOW_GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT_in_functioncall1334);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1340);
                    p = this.paramlist();
                    final RecognizerSharedState state82 = this.state;
                    --state82._fsp;
                    crit = new GetProtectorChallengeKamaAmount(p);
                    break;
                }
                case 83: {
                    this.match((IntStream)this.input, 133, CritereParser.FOLLOW_HASFREESURROUNDINGCELL_in_functioncall1346);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1352);
                    p = this.paramlist();
                    final RecognizerSharedState state83 = this.state;
                    --state83._fsp;
                    crit = new HasFreeSurroundingCell(p);
                    break;
                }
                case 84: {
                    this.match((IntStream)this.input, 217, CritereParser.FOLLOW_ISTARGETCELLFREE_in_functioncall1358);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1364);
                    p = this.paramlist();
                    final RecognizerSharedState state84 = this.state;
                    --state84._fsp;
                    crit = new IsTargetCellFree(p);
                    break;
                }
                case 85: {
                    this.match((IntStream)this.input, 256, CritereParser.FOLLOW_IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE_in_functioncall1370);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1376);
                    p = this.paramlist();
                    final RecognizerSharedState state85 = this.state;
                    --state85._fsp;
                    crit = new IsTargetCellValidForNewObstacle(p);
                    break;
                }
                case 86: {
                    this.match((IntStream)this.input, 185, CritereParser.FOLLOW_ISCARRIED_in_functioncall1382);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1388);
                    p = this.paramlist();
                    final RecognizerSharedState state86 = this.state;
                    --state86._fsp;
                    crit = new IsCarried(p);
                    break;
                }
                case 87: {
                    this.match((IntStream)this.input, 186, CritereParser.FOLLOW_ISCARRYING_in_functioncall1394);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1400);
                    p = this.paramlist();
                    final RecognizerSharedState state87 = this.state;
                    --state87._fsp;
                    crit = new IsCarrying(p);
                    break;
                }
                case 88: {
                    this.match((IntStream)this.input, 126, CritereParser.FOLLOW_HASAVAILABLECREATUREINSYMBIOT_in_functioncall1406);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1412);
                    p = this.paramlist();
                    final RecognizerSharedState state88 = this.state;
                    --state88._fsp;
                    crit = new HasAvailableCreatureInSymbiot(p);
                    break;
                }
                case 89: {
                    this.match((IntStream)this.input, 292, CritereParser.FOLLOW_SUMMONSLEADERSHIPSCORE_in_functioncall1418);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1424);
                    p = this.paramlist();
                    final RecognizerSharedState state89 = this.state;
                    --state89._fsp;
                    crit = new SummonsLeadershipScore(p);
                    break;
                }
                case 90: {
                    this.match((IntStream)this.input, 261, CritereParser.FOLLOW_LEADERSHIPFORCURRENTINVOC_in_functioncall1430);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1436);
                    p = this.paramlist();
                    final RecognizerSharedState state90 = this.state;
                    --state90._fsp;
                    crit = new GetTotalLeadershipNeededForCurrentCreature(p);
                    break;
                }
                case 91: {
                    this.match((IntStream)this.input, 74, CritereParser.FOLLOW_GETTERRITORYNATIONID_in_functioncall1442);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1448);
                    p = this.paramlist();
                    final RecognizerSharedState state91 = this.state;
                    --state91._fsp;
                    crit = new GetTerritoryNationId(p);
                    break;
                }
                case 92: {
                    this.match((IntStream)this.input, 209, CritereParser.FOLLOW_ISOWNSUMMON_in_functioncall1454);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1460);
                    p = this.paramlist();
                    final RecognizerSharedState state92 = this.state;
                    --state92._fsp;
                    crit = new IsOwnSummon(p);
                    break;
                }
                case 93: {
                    this.match((IntStream)this.input, 36, CritereParser.FOLLOW_GETCHARACTERDIRECTION_in_functioncall1466);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1472);
                    p = this.paramlist();
                    final RecognizerSharedState state93 = this.state;
                    --state93._fsp;
                    crit = new GetCharacterDirection(p);
                    break;
                }
                case 94: {
                    this.match((IntStream)this.input, 39, CritereParser.FOLLOW_GETCRAFTLEARNINGITEM_in_functioncall1477);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1483);
                    p = this.paramlist();
                    final RecognizerSharedState state94 = this.state;
                    --state94._fsp;
                    crit = new GetCraftLearningItem(p);
                    break;
                }
                case 95: {
                    this.match((IntStream)this.input, 127, CritereParser.FOLLOW_HASCRAFT_in_functioncall1488);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1494);
                    p = this.paramlist();
                    final RecognizerSharedState state95 = this.state;
                    --state95._fsp;
                    crit = new HasCraft(p);
                    break;
                }
                case 96: {
                    this.match((IntStream)this.input, 40, CritereParser.FOLLOW_GETCRAFTLEVEL_in_functioncall1499);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1505);
                    p = this.paramlist();
                    final RecognizerSharedState state96 = this.state;
                    --state96._fsp;
                    crit = new GetCraftLevel(p);
                    break;
                }
                case 97: {
                    this.match((IntStream)this.input, 128, CritereParser.FOLLOW_HASEMOTE_in_functioncall1510);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1516);
                    p = this.paramlist();
                    final RecognizerSharedState state97 = this.state;
                    --state97._fsp;
                    crit = new HasEmote(p);
                    break;
                }
                case 98: {
                    this.match((IntStream)this.input, 210, CritereParser.FOLLOW_ISPASSEPORTACTIVE_in_functioncall1521);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1527);
                    p = this.paramlist();
                    final RecognizerSharedState state98 = this.state;
                    --state98._fsp;
                    crit = new IsPasseportActive(p);
                    break;
                }
                case 99: {
                    this.match((IntStream)this.input, 16, CritereParser.FOLLOW_CANBECOMESOLDIERORMILITIAMAN_in_functioncall1532);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1538);
                    p = this.paramlist();
                    final RecognizerSharedState state99 = this.state;
                    --state99._fsp;
                    crit = new CanBecomeSoldierOrMilitiaman(p);
                    break;
                }
                case 100: {
                    this.match((IntStream)this.input, 76, CritereParser.FOLLOW_GETTITLE_in_functioncall1544);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1550);
                    p = this.paramlist();
                    final RecognizerSharedState state100 = this.state;
                    --state100._fsp;
                    crit = new GetTitle(p);
                    break;
                }
                case 101: {
                    this.match((IntStream)this.input, 61, CritereParser.FOLLOW_GETNATIONRANK_in_functioncall1556);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1562);
                    p = this.paramlist();
                    final RecognizerSharedState state101 = this.state;
                    --state101._fsp;
                    crit = new GetNationRank(p);
                    break;
                }
                case 102: {
                    this.match((IntStream)this.input, 192, CritereParser.FOLLOW_ISEQUIPPEDWITHSET_in_functioncall1569);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1575);
                    p = this.paramlist();
                    final RecognizerSharedState state102 = this.state;
                    --state102._fsp;
                    crit = new IsEquippedWithSet(p);
                    break;
                }
                case 103: {
                    this.match((IntStream)this.input, 195, CritereParser.FOLLOW_ISHOUR_in_functioncall1581);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1585);
                    p = this.paramlist();
                    final RecognizerSharedState state103 = this.state;
                    --state103._fsp;
                    crit = new IsHour(p);
                    break;
                }
                case 104: {
                    this.match((IntStream)this.input, 207, CritereParser.FOLLOW_ISOWNHOUR_in_functioncall1591);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1595);
                    p = this.paramlist();
                    final RecognizerSharedState state104 = this.state;
                    --state104._fsp;
                    crit = new IsOwnHour(p);
                    break;
                }
                case 105: {
                    this.match((IntStream)this.input, 181, CritereParser.FOLLOW_ISBOMB_in_functioncall1601);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1607);
                    p = this.paramlist();
                    final RecognizerSharedState state105 = this.state;
                    --state105._fsp;
                    crit = new IsBomb(p);
                    break;
                }
                case 106: {
                    this.match((IntStream)this.input, 218, CritereParser.FOLLOW_ISTUNNEL_in_functioncall1612);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1618);
                    p = this.paramlist();
                    final RecognizerSharedState state106 = this.state;
                    --state106._fsp;
                    crit = new IsTunnel(p);
                    break;
                }
                case 107: {
                    this.match((IntStream)this.input, 204, CritereParser.FOLLOW_ISOWNBOMB_in_functioncall1623);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1629);
                    p = this.paramlist();
                    final RecognizerSharedState state107 = this.state;
                    --state107._fsp;
                    crit = new IsOwnBomb(p);
                    break;
                }
                case 108: {
                    this.match((IntStream)this.input, 203, CritereParser.FOLLOW_ISOWNBEACON_in_functioncall1634);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1640);
                    p = this.paramlist();
                    final RecognizerSharedState state108 = this.state;
                    --state108._fsp;
                    crit = new IsOwnBeacon(p);
                    break;
                }
                case 109: {
                    this.match((IntStream)this.input, 208, CritereParser.FOLLOW_ISOWNSPECIFICAREA_in_functioncall1646);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1652);
                    p = this.paramlist();
                    final RecognizerSharedState state109 = this.state;
                    --state109._fsp;
                    crit = new IsOwnSpecificArea(p);
                    break;
                }
                case 110: {
                    this.match((IntStream)this.input, 140, CritereParser.FOLLOW_HASSUMMONWITHBREED_in_functioncall1658);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1664);
                    p = this.paramlist();
                    final RecognizerSharedState state110 = this.state;
                    --state110._fsp;
                    crit = new HasSummonWithBreed(p);
                    break;
                }
                case 111: {
                    this.match((IntStream)this.input, 267, CritereParser.FOLLOW_NBBOMB_in_functioncall1670);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1676);
                    p = this.paramlist();
                    final RecognizerSharedState state111 = this.state;
                    --state111._fsp;
                    crit = new NbBombs(p);
                    break;
                }
                case 112: {
                    this.match((IntStream)this.input, 174, CritereParser.FOLLOW_ISACHIEVEMENTOBJECTIVECOMPLETE_in_functioncall1682);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1687);
                    p = this.paramlist();
                    final RecognizerSharedState state112 = this.state;
                    --state112._fsp;
                    crit = new IsAchievementObjectiveComplete(p);
                    break;
                }
                case 113: {
                    this.match((IntStream)this.input, 175, CritereParser.FOLLOW_ISACHIEVEMENTREPEATABLE_in_functioncall1693);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1698);
                    p = this.paramlist();
                    final RecognizerSharedState state113 = this.state;
                    --state113._fsp;
                    crit = new IsAchievementRepeatable(p);
                    break;
                }
                case 114: {
                    this.match((IntStream)this.input, 18, CritereParser.FOLLOW_CANRESETACHIEVEMENT_in_functioncall1704);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1709);
                    p = this.paramlist();
                    final RecognizerSharedState state114 = this.state;
                    --state114._fsp;
                    crit = new CanResetAchievement(p);
                    break;
                }
                case 115: {
                    this.match((IntStream)this.input, 278, CritereParser.FOLLOW_OPPONENTSCONTAINSNATIONENEMY_in_functioncall1715);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1720);
                    p = this.paramlist();
                    final RecognizerSharedState state115 = this.state;
                    --state115._fsp;
                    crit = new OpponentsContainsNationEnemy(p);
                    break;
                }
                case 116: {
                    this.match((IntStream)this.input, 135, CritereParser.FOLLOW_HASNATIONJOB_in_functioncall1726);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1731);
                    p = this.paramlist();
                    final RecognizerSharedState state116 = this.state;
                    --state116._fsp;
                    crit = new HasNationJob(p);
                    break;
                }
                case 117: {
                    this.match((IntStream)this.input, 172, CritereParser.FOLLOW_ISACHIEVEMENTCOMPLETE_in_functioncall1737);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1742);
                    p = this.paramlist();
                    final RecognizerSharedState state117 = this.state;
                    --state117._fsp;
                    crit = new IsAchievementComplete(p);
                    break;
                }
                case 118: {
                    this.match((IntStream)this.input, 171, CritereParser.FOLLOW_ISACHIEVEMENTACTIVE_in_functioncall1748);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1753);
                    p = this.paramlist();
                    final RecognizerSharedState state118 = this.state;
                    --state118._fsp;
                    crit = new IsAchievementActive(p);
                    break;
                }
                case 119: {
                    this.match((IntStream)this.input, 176, CritereParser.FOLLOW_ISACHIEVEMENTRUNNING_in_functioncall1762);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1767);
                    p = this.paramlist();
                    final RecognizerSharedState state119 = this.state;
                    --state119._fsp;
                    crit = new IsAchievementRunning(p);
                    break;
                }
                case 120: {
                    this.match((IntStream)this.input, 173, CritereParser.FOLLOW_ISACHIEVEMENTFAILED_in_functioncall1776);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1781);
                    p = this.paramlist();
                    final RecognizerSharedState state120 = this.state;
                    --state120._fsp;
                    crit = new IsAchievementFailed(p);
                    break;
                }
                case 121: {
                    this.match((IntStream)this.input, 211, CritereParser.FOLLOW_ISPROTECTORINFIGHT_in_functioncall1790);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1796);
                    p = this.paramlist();
                    final RecognizerSharedState state121 = this.state;
                    --state121._fsp;
                    crit = new IsProtectorInFight(p);
                    break;
                }
                case 122: {
                    this.match((IntStream)this.input, 199, CritereParser.FOLLOW_ISOFFPLAY_in_functioncall1801);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1807);
                    p = this.paramlist();
                    final RecognizerSharedState state122 = this.state;
                    --state122._fsp;
                    crit = new IsOffPlay(p);
                    break;
                }
                case 123: {
                    this.match((IntStream)this.input, 240, CritereParser.FOLLOW_IS_IN_PLAY_in_functioncall1812);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1818);
                    p = this.paramlist();
                    final RecognizerSharedState state123 = this.state;
                    --state123._fsp;
                    crit = new IsInPlay(p);
                    break;
                }
                case 124: {
                    this.match((IntStream)this.input, 246, CritereParser.FOLLOW_IS_OUT_OF_PLAY_in_functioncall1823);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1829);
                    p = this.paramlist();
                    final RecognizerSharedState state124 = this.state;
                    --state124._fsp;
                    crit = new IsOutOfPlay(p);
                    break;
                }
                case 125: {
                    this.match((IntStream)this.input, 238, CritereParser.FOLLOW_IS_IN_FIGHT_in_functioncall1834);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1840);
                    p = this.paramlist();
                    final RecognizerSharedState state125 = this.state;
                    --state125._fsp;
                    crit = new IsInFight(p);
                    break;
                }
                case 126: {
                    this.match((IntStream)this.input, 205, CritereParser.FOLLOW_ISOWNDEPOSIT_in_functioncall1845);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1851);
                    p = this.paramlist();
                    final RecognizerSharedState state126 = this.state;
                    --state126._fsp;
                    crit = new IsOwnDeposit(p);
                    break;
                }
                case 127: {
                    this.match((IntStream)this.input, 196, CritereParser.FOLLOW_ISINGROUP_in_functioncall1856);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1862);
                    p = this.paramlist();
                    final RecognizerSharedState state127 = this.state;
                    --state127._fsp;
                    crit = new IsInGroup(p);
                    break;
                }
                case 128: {
                    this.match((IntStream)this.input, 177, CritereParser.FOLLOW_ISACTIVATEDBYELEMENT_in_functioncall1868);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1874);
                    p = this.paramlist();
                    final RecognizerSharedState state128 = this.state;
                    --state128._fsp;
                    crit = new IsActivatedByElement(p);
                    break;
                }
                case 129: {
                    this.match((IntStream)this.input, 178, CritereParser.FOLLOW_ISACTIVATEDBYSPELL_in_functioncall1880);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1886);
                    p = this.paramlist();
                    final RecognizerSharedState state129 = this.state;
                    --state129._fsp;
                    crit = new IsActivatedBySpell(p);
                    break;
                }
                case 130: {
                    this.match((IntStream)this.input, 200, CritereParser.FOLLOW_ISONEFFECTAREATYPE_in_functioncall1892);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1898);
                    p = this.paramlist();
                    final RecognizerSharedState state130 = this.state;
                    --state130._fsp;
                    crit = new IsOnEffectAreaType(p);
                    break;
                }
                case 131: {
                    this.match((IntStream)this.input, 201, CritereParser.FOLLOW_ISONSPECIFICEFFECTAREA_in_functioncall1904);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1910);
                    p = this.paramlist();
                    final RecognizerSharedState state131 = this.state;
                    --state131._fsp;
                    crit = new IsOnSpecificEffectArea(p);
                    break;
                }
                case 132: {
                    this.match((IntStream)this.input, 202, CritereParser.FOLLOW_ISONSPECIFICEFFECTAREAWITHSPECIFICSTATE_in_functioncall1919);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1925);
                    p = this.paramlist();
                    final RecognizerSharedState state132 = this.state;
                    --state132._fsp;
                    crit = new IsOnSpecificEffectAreaWithSpecificState(p);
                    break;
                }
                case 133: {
                    this.match((IntStream)this.input, 20, CritereParser.FOLLOW_CELL_CONTAINS_SPECIFIC_EFFECT_AREA_in_functioncall1934);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1940);
                    p = this.paramlist();
                    final RecognizerSharedState state133 = this.state;
                    --state133._fsp;
                    crit = new CellContainsSpecificEffectArea(p);
                    break;
                }
                case 134: {
                    this.match((IntStream)this.input, 206, CritereParser.FOLLOW_ISOWNGLYPH_in_functioncall1946);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1952);
                    p = this.paramlist();
                    final RecognizerSharedState state134 = this.state;
                    --state134._fsp;
                    crit = new IsOwnGlyph(p);
                    break;
                }
                case 135: {
                    this.match((IntStream)this.input, 190, CritereParser.FOLLOW_ISDEPOSIT_in_functioncall1957);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1963);
                    p = this.paramlist();
                    final RecognizerSharedState state135 = this.state;
                    --state135._fsp;
                    crit = new IsDeposit(p);
                    break;
                }
                case 136: {
                    this.match((IntStream)this.input, 71, CritereParser.FOLLOW_GETSTATECOUNTINRANGE_in_functioncall1968);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1974);
                    p = this.paramlist();
                    final RecognizerSharedState state136 = this.state;
                    --state136._fsp;
                    crit = new GetStateCountInRange((List<ParserObject>)p);
                    break;
                }
                case 137: {
                    this.match((IntStream)this.input, 194, CritereParser.FOLLOW_ISFLEEING_in_functioncall1979);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1985);
                    p = this.paramlist();
                    final RecognizerSharedState state137 = this.state;
                    --state137._fsp;
                    crit = new IsFleeing(p);
                    break;
                }
                case 138: {
                    this.match((IntStream)this.input, 169, CritereParser.FOLLOW_ISABANDONNING_in_functioncall1990);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall1996);
                    p = this.paramlist();
                    final RecognizerSharedState state138 = this.state;
                    --state138._fsp;
                    crit = new IsAbandonning(p);
                    break;
                }
                case 139: {
                    this.match((IntStream)this.input, 198, CritereParser.FOLLOW_ISNATIONFIRSTINDUNGEONLADDER_in_functioncall2001);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2007);
                    p = this.paramlist();
                    final RecognizerSharedState state139 = this.state;
                    --state139._fsp;
                    crit = new IsNationFirstInDungeonLadder(p);
                    break;
                }
                case 140: {
                    this.match((IntStream)this.input, 50, CritereParser.FOLLOW_GETFIGHTMODEL_in_functioncall2012);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2018);
                    p = this.paramlist();
                    final RecognizerSharedState state140 = this.state;
                    --state140._fsp;
                    crit = new GetFightModel(p);
                    break;
                }
                case 141: {
                    this.match((IntStream)this.input, 157, CritereParser.FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_BARREL_in_functioncall2023);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2029);
                    p = this.paramlist();
                    final RecognizerSharedState state141 = this.state;
                    --state141._fsp;
                    crit = new HasSurroundingCellWithOwnBarrel(p);
                    break;
                }
                case 142: {
                    this.match((IntStream)this.input, 158, CritereParser.FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA_in_functioncall2034);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2040);
                    p = this.paramlist();
                    final RecognizerSharedState state142 = this.state;
                    --state142._fsp;
                    crit = new HasSurroundingCellWithOwnEffectArea(p);
                    break;
                }
                case 143: {
                    this.match((IntStream)this.input, 156, CritereParser.FOLLOW_HAS_SURROUNDING_CELL_WITH_EFFECT_AREA_in_functioncall2045);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2051);
                    p = this.paramlist();
                    final RecognizerSharedState state143 = this.state;
                    --state143._fsp;
                    crit = new HasSurroundingCellWithEffectArea(p);
                    break;
                }
                case 144: {
                    this.match((IntStream)this.input, 222, CritereParser.FOLLOW_IS_CARRYING_OWN_BARREL_in_functioncall2056);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2062);
                    p = this.paramlist();
                    final RecognizerSharedState state144 = this.state;
                    --state144._fsp;
                    crit = new IsCarryingOwnBarrel(p);
                    break;
                }
                case 145: {
                    this.match((IntStream)this.input, 112, CritereParser.FOLLOW_GET_TARGET_COUNT_IN_BEACON_AREA_in_functioncall2068);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2074);
                    p = this.paramlist();
                    final RecognizerSharedState state145 = this.state;
                    --state145._fsp;
                    crit = new GetTargetCountInBeaconArea(p);
                    break;
                }
                case 146: {
                    this.match((IntStream)this.input, 98, CritereParser.FOLLOW_GET_FIGHTERS_WITH_BREED_IN_RANGE_in_functioncall2080);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2086);
                    p = this.paramlist();
                    final RecognizerSharedState state146 = this.state;
                    --state146._fsp;
                    crit = new GetFightersWithBreedInRange(p);
                    break;
                }
                case 147: {
                    this.match((IntStream)this.input, 7, CritereParser.FOLLOW_AI_HAS_CAST_SPELL_in_functioncall2092);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2098);
                    p = this.paramlist();
                    final RecognizerSharedState state147 = this.state;
                    --state147._fsp;
                    crit = new AIHasCastSpell(p);
                    break;
                }
                case 148: {
                    this.match((IntStream)this.input, 8, CritereParser.FOLLOW_AI_HAS_MOVED_in_functioncall2104);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2110);
                    p = this.paramlist();
                    final RecognizerSharedState state148 = this.state;
                    --state148._fsp;
                    crit = new AIHasMoved(p);
                    break;
                }
                case 149: {
                    this.match((IntStream)this.input, 6, CritereParser.FOLLOW_AI_GET_SPELL_CAST_COUNT_in_functioncall2116);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2122);
                    p = this.paramlist();
                    final RecognizerSharedState state149 = this.state;
                    --state149._fsp;
                    crit = new AIGetSpellCastCount(p);
                    break;
                }
                case 150: {
                    this.match((IntStream)this.input, 111, CritereParser.FOLLOW_GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA_in_functioncall2128);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2134);
                    p = this.paramlist();
                    final RecognizerSharedState state150 = this.state;
                    --state150._fsp;
                    crit = new GetTargetsCountInEffectZoneArea(p);
                    break;
                }
                case 151: {
                    this.match((IntStream)this.input, 254, CritereParser.FOLLOW_IS_SUMMON_in_functioncall2140);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2146);
                    p = this.paramlist();
                    final RecognizerSharedState state151 = this.state;
                    --state151._fsp;
                    crit = new IsSummon(p);
                    break;
                }
                case 152: {
                    this.match((IntStream)this.input, 255, CritereParser.FOLLOW_IS_SUMMON_FROM_SYMBIOT_in_functioncall2152);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2158);
                    p = this.paramlist();
                    final RecognizerSharedState state152 = this.state;
                    --state152._fsp;
                    crit = new IsSummonedFromSymbiot(p);
                    break;
                }
                case 153: {
                    this.match((IntStream)this.input, 231, CritereParser.FOLLOW_IS_CONTROLLED_BY_AI_in_functioncall2164);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2170);
                    p = this.paramlist();
                    final RecognizerSharedState state153 = this.state;
                    --state153._fsp;
                    crit = new IsControlledByAI(p);
                    break;
                }
                case 154: {
                    this.match((IntStream)this.input, 88, CritereParser.FOLLOW_GET_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall2176);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2182);
                    p = this.paramlist();
                    final RecognizerSharedState state154 = this.state;
                    --state154._fsp;
                    crit = new GetEffectAreaCountInRange(p);
                    break;
                }
                case 155: {
                    this.match((IntStream)this.input, 109, CritereParser.FOLLOW_GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall2188);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2194);
                    p = this.paramlist();
                    final RecognizerSharedState state155 = this.state;
                    --state155._fsp;
                    crit = new GetSpecificEffectAreaCountInRange(p);
                    break;
                }
                case 156: {
                    this.match((IntStream)this.input, 86, CritereParser.FOLLOW_GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE_in_functioncall2200);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2206);
                    p = this.paramlist();
                    final RecognizerSharedState state156 = this.state;
                    --state156._fsp;
                    crit = new GetEffectAreaCountInRunningEffectAOE(p);
                    break;
                }
                case 157: {
                    this.match((IntStream)this.input, 150, CritereParser.FOLLOW_HAS_LINE_OF_SIGHT_FROM_ENEMY_in_functioncall2212);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2218);
                    p = this.paramlist();
                    final RecognizerSharedState state157 = this.state;
                    --state157._fsp;
                    crit = new HasLineOfSightFromEnemy(p);
                    break;
                }
                case 158: {
                    this.match((IntStream)this.input, 91, CritereParser.FOLLOW_GET_ENEMIES_HUMAN_COUNT_IN_RANGE_in_functioncall2224);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2230);
                    p = this.paramlist();
                    final RecognizerSharedState state158 = this.state;
                    --state158._fsp;
                    crit = new GetEnemiesHumanCountInRange(p);
                    break;
                }
                case 159: {
                    this.match((IntStream)this.input, 257, CritereParser.FOLLOW_IS_TARGET_ON_SAME_TEAM_in_functioncall2236);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2242);
                    p = this.paramlist();
                    final RecognizerSharedState state159 = this.state;
                    --state159._fsp;
                    crit = new IsTargetOnSameTeam(p);
                    break;
                }
                case 160: {
                    this.match((IntStream)this.input, 152, CritereParser.FOLLOW_HAS_LOOT_in_functioncall2248);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2254);
                    p = this.paramlist();
                    final RecognizerSharedState state160 = this.state;
                    --state160._fsp;
                    crit = new HasLoot(p);
                    break;
                }
                case 161: {
                    this.match((IntStream)this.input, 146, CritereParser.FOLLOW_HAS_EFFECT_WITH_ACTION_ID_in_functioncall2260);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2266);
                    p = this.paramlist();
                    final RecognizerSharedState state161 = this.state;
                    --state161._fsp;
                    crit = new HasEffectWithActionId(p);
                    break;
                }
                case 162: {
                    this.match((IntStream)this.input, 227, CritereParser.FOLLOW_IS_CHARACTER_in_functioncall2272);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2278);
                    p = this.paramlist();
                    final RecognizerSharedState state162 = this.state;
                    --state162._fsp;
                    crit = new IsCharacter(p);
                    break;
                }
                case 163: {
                    this.match((IntStream)this.input, 153, CritereParser.FOLLOW_HAS_STATE_FROM_LEVEL_in_functioncall2284);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2290);
                    p = this.paramlist();
                    final RecognizerSharedState state163 = this.state;
                    --state163._fsp;
                    crit = new HasStateFromLevel(p);
                    break;
                }
                case 164: {
                    this.match((IntStream)this.input, 23, CritereParser.FOLLOW_DOUBLE_OR_QUITS_CRITERION_in_functioncall2296);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2302);
                    p = this.paramlist();
                    final RecognizerSharedState state164 = this.state;
                    --state164._fsp;
                    crit = new DoubleOrQuitsCriterion(p);
                    break;
                }
                case 165: {
                    this.match((IntStream)this.input, 163, CritereParser.FOLLOW_HAS_WEAPON_TYPE_in_functioncall2308);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2314);
                    p = this.paramlist();
                    final RecognizerSharedState state165 = this.state;
                    --state165._fsp;
                    crit = new HasWeaponType(p);
                    break;
                }
                case 166: {
                    this.match((IntStream)this.input, 247, CritereParser.FOLLOW_IS_OWN_AREA_in_functioncall2320);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2326);
                    p = this.paramlist();
                    final RecognizerSharedState state166 = this.state;
                    --state166._fsp;
                    crit = new IsOwnArea(p);
                    break;
                }
                case 167: {
                    this.match((IntStream)this.input, 242, CritereParser.FOLLOW_IS_ON_BORDER_CELL_in_functioncall2332);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2338);
                    p = this.paramlist();
                    final RecognizerSharedState state167 = this.state;
                    --state167._fsp;
                    crit = new IsOnBorderCell(p);
                    break;
                }
                case 168: {
                    this.match((IntStream)this.input, 274, CritereParser.FOLLOW_NB_ROUBLABOT_in_functioncall2344);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2350);
                    p = this.paramlist();
                    final RecognizerSharedState state168 = this.state;
                    --state168._fsp;
                    crit = new NbRoublabot(p);
                    break;
                }
                case 169: {
                    this.match((IntStream)this.input, 147, CritereParser.FOLLOW_HAS_EFFECT_WITH_SPECIFIC_ID_in_functioncall2356);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2362);
                    p = this.paramlist();
                    final RecognizerSharedState state169 = this.state;
                    --state169._fsp;
                    crit = new HasEffectWithSpecificId(p);
                    break;
                }
                case 170: {
                    this.match((IntStream)this.input, 148, CritereParser.FOLLOW_HAS_FECA_ARMOR_in_functioncall2368);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2374);
                    p = this.paramlist();
                    final RecognizerSharedState state170 = this.state;
                    --state170._fsp;
                    crit = new HasFecaArmor(p);
                    break;
                }
                case 171: {
                    this.match((IntStream)this.input, 233, CritereParser.FOLLOW_IS_FECA_GLYPH_CENTER_in_functioncall2380);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2386);
                    p = this.paramlist();
                    final RecognizerSharedState state171 = this.state;
                    --state171._fsp;
                    crit = new IsFecaGlyphCenter(p);
                    break;
                }
                case 172: {
                    this.match((IntStream)this.input, 270, CritereParser.FOLLOW_NB_FECA_GLYPH_in_functioncall2392);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2398);
                    p = this.paramlist();
                    final RecognizerSharedState state172 = this.state;
                    --state172._fsp;
                    crit = new NbFecaGlyph(p);
                    break;
                }
                case 173: {
                    this.match((IntStream)this.input, 30, CritereParser.FOLLOW_GETACHIEVEMENTVARIABLE_in_functioncall2404);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2409);
                    p = this.paramlist();
                    final RecognizerSharedState state173 = this.state;
                    --state173._fsp;
                    crit = new GetAchievementVariable(p);
                    break;
                }
                case 174: {
                    this.match((IntStream)this.input, 83, CritereParser.FOLLOW_GET_CHALLENGE_UNAVAILABILITY_DURATION_in_functioncall2415);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2420);
                    p = this.paramlist();
                    final RecognizerSharedState state174 = this.state;
                    --state174._fsp;
                    crit = new GetChallengeUnavailabilityDuration();
                    break;
                }
                case 175: {
                    this.match((IntStream)this.input, 89, CritereParser.FOLLOW_GET_EFFECT_CASTER_ORIGINAL_CONTROLLER_in_functioncall2426);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2431);
                    p = this.paramlist();
                    final RecognizerSharedState state175 = this.state;
                    --state175._fsp;
                    crit = new GetEffectCasterOriginalController(p);
                    break;
                }
                case 176: {
                    this.match((IntStream)this.input, 90, CritereParser.FOLLOW_GET_EFFECT_TARGET_ORIGINAL_CONTROLLER_in_functioncall2437);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2442);
                    p = this.paramlist();
                    final RecognizerSharedState state176 = this.state;
                    --state176._fsp;
                    crit = new GetEffectTargetOriginalController(p);
                    break;
                }
                case 177: {
                    this.match((IntStream)this.input, 19, CritereParser.FOLLOW_CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER_in_functioncall2448);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2453);
                    p = this.paramlist();
                    final RecognizerSharedState state177 = this.state;
                    --state177._fsp;
                    crit = new CasterAndTargetHaveSameOriginalController(p);
                    break;
                }
                case 178: {
                    this.match((IntStream)this.input, 245, CritereParser.FOLLOW_IS_ORIGINAL_CONTROLLER_in_functioncall2459);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2464);
                    p = this.paramlist();
                    final RecognizerSharedState state178 = this.state;
                    --state178._fsp;
                    crit = new IsOriginalController(p);
                    break;
                }
                case 179: {
                    this.match((IntStream)this.input, 228, CritereParser.FOLLOW_IS_CHARACTERISTIC_FULL_in_functioncall2470);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2475);
                    p = this.paramlist();
                    final RecognizerSharedState state179 = this.state;
                    --state179._fsp;
                    crit = new IsCharacteristicFull(p);
                    break;
                }
                case 180: {
                    this.match((IntStream)this.input, 170, CritereParser.FOLLOW_ISACCOUNTSUBSCRIBED_in_functioncall2481);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2486);
                    p = this.paramlist();
                    final RecognizerSharedState state180 = this.state;
                    --state180._fsp;
                    crit = new IsAccountSubscribed(p);
                    break;
                }
                case 181: {
                    this.match((IntStream)this.input, 221, CritereParser.FOLLOW_ISZONEINCHAOS_in_functioncall2492);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2498);
                    p = this.paramlist();
                    final RecognizerSharedState state181 = this.state;
                    --state181._fsp;
                    crit = new IsZoneInChaos(p);
                    break;
                }
                case 182: {
                    this.match((IntStream)this.input, 117, CritereParser.FOLLOW_GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER_in_functioncall2503);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2508);
                    p = this.paramlist();
                    final RecognizerSharedState state182 = this.state;
                    --state182._fsp;
                    crit = new GetTriggeringEffectCasterIsSameAsCaster(p);
                    break;
                }
                case 183: {
                    this.match((IntStream)this.input, 116, CritereParser.FOLLOW_GET_TRIGGERING_EFFECT_CASTER_in_functioncall2514);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2519);
                    p = this.paramlist();
                    final RecognizerSharedState state183 = this.state;
                    --state183._fsp;
                    crit = new GetFighterId("triggeringCaster");
                    break;
                }
                case 184: {
                    this.match((IntStream)this.input, 99, CritereParser.FOLLOW_GET_FIGHTER_ID_in_functioncall2525);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2530);
                    p = this.paramlist();
                    final RecognizerSharedState state184 = this.state;
                    --state184._fsp;
                    crit = new GetFighterId(p);
                    break;
                }
                case 185: {
                    this.match((IntStream)this.input, 244, CritereParser.FOLLOW_IS_ON_OWN_DIAL_in_functioncall2536);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2542);
                    p = this.paramlist();
                    final RecognizerSharedState state185 = this.state;
                    --state185._fsp;
                    crit = new IsOnOwnDial();
                    break;
                }
                case 186: {
                    this.match((IntStream)this.input, 273, CritereParser.FOLLOW_NB_HYDRANDS_in_functioncall2548);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2554);
                    p = this.paramlist();
                    final RecognizerSharedState state186 = this.state;
                    --state186._fsp;
                    crit = new NbHydrands(p);
                    break;
                }
                case 187: {
                    this.match((IntStream)this.input, 275, CritereParser.FOLLOW_NB_STEAMBOTS_in_functioncall2560);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2566);
                    p = this.paramlist();
                    final RecognizerSharedState state187 = this.state;
                    --state187._fsp;
                    crit = new NbSteamBots(p);
                    break;
                }
                case 188: {
                    this.match((IntStream)this.input, 248, CritereParser.FOLLOW_IS_OWN_FECA_GLYPH_in_functioncall2572);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2578);
                    p = this.paramlist();
                    final RecognizerSharedState state188 = this.state;
                    --state188._fsp;
                    crit = new IsOwnFecaGlyph(p);
                    break;
                }
                case 189: {
                    this.match((IntStream)this.input, 85, CritereParser.FOLLOW_GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER_in_functioncall2584);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2590);
                    p = this.paramlist();
                    final RecognizerSharedState state189 = this.state;
                    --state189._fsp;
                    crit = new GetDistanceBetweenTargetAndEffectBearer(p);
                    break;
                }
                case 190: {
                    this.match((IntStream)this.input, 92, CritereParser.FOLLOW_GET_FGHT_CURRENT_TABLE_TURN_in_functioncall2596);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2602);
                    p = this.paramlist();
                    final RecognizerSharedState state190 = this.state;
                    --state190._fsp;
                    crit = new GetFightCurrentTableTurn(p);
                    break;
                }
                case 191: {
                    this.match((IntStream)this.input, 268, CritereParser.FOLLOW_NB_ALL_AREAS_in_functioncall2608);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2614);
                    p = this.paramlist();
                    final RecognizerSharedState state191 = this.state;
                    --state191._fsp;
                    crit = new NbAllAreas(p);
                    break;
                }
                case 192: {
                    this.match((IntStream)this.input, 272, CritereParser.FOLLOW_NB_GLYPHS_in_functioncall2620);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2626);
                    p = this.paramlist();
                    final RecognizerSharedState state192 = this.state;
                    --state192._fsp;
                    crit = new NbGlyphs(p);
                    break;
                }
                case 193: {
                    this.match((IntStream)this.input, 120, CritereParser.FOLLOW_GET_TRIGGERING_EFFECT_TARGET_BREED_ID_in_functioncall2633);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2639);
                    p = this.paramlist();
                    final RecognizerSharedState state193 = this.state;
                    --state193._fsp;
                    crit = new GetTriggeringEffectTargetBreedId(p);
                    break;
                }
                case 194: {
                    this.match((IntStream)this.input, 115, CritereParser.FOLLOW_GET_TRIGGERING_ANCESTORS_COUNT_in_functioncall2646);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2652);
                    p = this.paramlist();
                    final RecognizerSharedState state194 = this.state;
                    --state194._fsp;
                    crit = new GetTriggeringAncestorsCount(p);
                    break;
                }
                case 195: {
                    this.match((IntStream)this.input, 253, CritereParser.FOLLOW_IS_SIDE_STABBED_in_functioncall2658);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2664);
                    p = this.paramlist();
                    final RecognizerSharedState state195 = this.state;
                    --state195._fsp;
                    crit = new IsSidestabbed(p);
                    break;
                }
                case 196: {
                    this.match((IntStream)this.input, 119, CritereParser.FOLLOW_GET_TRIGGERING_EFFECT_TARGET_in_functioncall2670);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2676);
                    p = this.paramlist();
                    final RecognizerSharedState state196 = this.state;
                    --state196._fsp;
                    crit = new GetTriggeringEffectTarget(p);
                    break;
                }
                case 197: {
                    this.match((IntStream)this.input, 151, CritereParser.FOLLOW_HAS_LINE_OF_SIGHT_TO_ENEMY_in_functioncall2682);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2688);
                    p = this.paramlist();
                    final RecognizerSharedState state197 = this.state;
                    --state197._fsp;
                    crit = new HasLineOfSightToEnemy(p);
                    break;
                }
                case 198: {
                    this.match((IntStream)this.input, 249, CritereParser.FOLLOW_IS_PLAYER_in_functioncall2694);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2700);
                    p = this.paramlist();
                    final RecognizerSharedState state198 = this.state;
                    --state198._fsp;
                    crit = new IsPlayer(p);
                    break;
                }
                case 199: {
                    this.match((IntStream)this.input, 230, CritereParser.FOLLOW_IS_COMPANION_in_functioncall2706);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2712);
                    p = this.paramlist();
                    final RecognizerSharedState state199 = this.state;
                    --state199._fsp;
                    crit = new IsCompanion(p);
                    break;
                }
                case 200: {
                    this.match((IntStream)this.input, 226, CritereParser.FOLLOW_IS_CHALLENGER_in_functioncall2718);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2724);
                    p = this.paramlist();
                    final RecognizerSharedState state200 = this.state;
                    --state200._fsp;
                    crit = new IsChallenger(p);
                    break;
                }
                case 201: {
                    this.match((IntStream)this.input, 223, CritereParser.FOLLOW_IS_CARRYING_OWN_BOMB_in_functioncall2730);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2736);
                    p = this.paramlist();
                    final RecognizerSharedState state201 = this.state;
                    --state201._fsp;
                    crit = new IsCarryingOwnBomb(p);
                    break;
                }
                case 202: {
                    this.match((IntStream)this.input, 159, CritereParser.FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_SUMMON_in_functioncall2742);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2748);
                    p = this.paramlist();
                    final RecognizerSharedState state202 = this.state;
                    --state202._fsp;
                    crit = new HasSurroundingCellWithOwnSummon(p);
                    break;
                }
                case 203: {
                    this.match((IntStream)this.input, 100, CritereParser.FOLLOW_GET_GUILD_LEVEL_in_functioncall2754);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2760);
                    p = this.paramlist();
                    final RecognizerSharedState state203 = this.state;
                    --state203._fsp;
                    crit = new GetGuildLevel(p);
                    break;
                }
                case 204: {
                    this.match((IntStream)this.input, 239, CritereParser.FOLLOW_IS_IN_GUILD_in_functioncall2766);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2772);
                    p = this.paramlist();
                    final RecognizerSharedState state204 = this.state;
                    --state204._fsp;
                    crit = new IsInGuild(p);
                    break;
                }
                case 205: {
                    this.match((IntStream)this.input, 101, CritereParser.FOLLOW_GET_GUILD_PARTNER_COUNT_IN_FIGHT_in_functioncall2778);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2784);
                    p = this.paramlist();
                    final RecognizerSharedState state205 = this.state;
                    --state205._fsp;
                    crit = new GetGuildPartnerCountInFight(p);
                    break;
                }
                case 206: {
                    this.match((IntStream)this.input, 237, CritereParser.FOLLOW_IS_IN_ALIGNMENT_in_functioncall2790);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2796);
                    p = this.paramlist();
                    final RecognizerSharedState state206 = this.state;
                    --state206._fsp;
                    crit = new IsInAlignment(p);
                    break;
                }
                case 207: {
                    this.match((IntStream)this.input, 162, CritereParser.FOLLOW_HAS_VALID_PATH_TO_TARGET_in_functioncall2802);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2808);
                    p = this.paramlist();
                    final RecognizerSharedState state207 = this.state;
                    --state207._fsp;
                    crit = new HasValidPathToTarget(p);
                    break;
                }
                case 208: {
                    this.match((IntStream)this.input, 234, CritereParser.FOLLOW_IS_FREE_CELL_in_functioncall2815);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2821);
                    p = this.paramlist();
                    final RecognizerSharedState state208 = this.state;
                    --state208._fsp;
                    crit = new IsFreeCell(p);
                    break;
                }
                case 209: {
                    this.match((IntStream)this.input, 144, CritereParser.FOLLOW_HAS_BEEN_RAISED_BY_EFFECT_in_functioncall2827);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2833);
                    p = this.paramlist();
                    final RecognizerSharedState state209 = this.state;
                    --state209._fsp;
                    crit = new HasBeenRaisedByEffect(p);
                    break;
                }
                case 210: {
                    this.match((IntStream)this.input, 122, CritereParser.FOLLOW_GET_X_in_functioncall2839);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2845);
                    p = this.paramlist();
                    final RecognizerSharedState state210 = this.state;
                    --state210._fsp;
                    crit = new GetX(p);
                    break;
                }
                case 211: {
                    this.match((IntStream)this.input, 124, CritereParser.FOLLOW_GET_Y_in_functioncall2852);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2858);
                    p = this.paramlist();
                    final RecognizerSharedState state211 = this.state;
                    --state211._fsp;
                    crit = new GetY(p);
                    break;
                }
                case 212: {
                    this.match((IntStream)this.input, 125, CritereParser.FOLLOW_GET_Z_in_functioncall2865);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2871);
                    p = this.paramlist();
                    final RecognizerSharedState state212 = this.state;
                    --state212._fsp;
                    crit = new GetZ(p);
                    break;
                }
                case 213: {
                    this.match((IntStream)this.input, 269, CritereParser.FOLLOW_NB_AREAS_WITH_BASE_ID_in_functioncall2878);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2884);
                    p = this.paramlist();
                    final RecognizerSharedState state213 = this.state;
                    --state213._fsp;
                    crit = new NbAreasWithBaseId(p);
                    break;
                }
                case 214: {
                    this.match((IntStream)this.input, 87, CritereParser.FOLLOW_GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS_in_functioncall2890);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2896);
                    p = this.paramlist();
                    final RecognizerSharedState state214 = this.state;
                    --state214._fsp;
                    crit = new GetEffectsCountWithSpecificIds(p);
                    break;
                }
                case 215: {
                    this.match((IntStream)this.input, 106, CritereParser.FOLLOW_GET_PARTITION_X_in_functioncall2902);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2908);
                    p = this.paramlist();
                    final RecognizerSharedState state215 = this.state;
                    --state215._fsp;
                    crit = new GetPartitionX(p);
                    break;
                }
                case 216: {
                    this.match((IntStream)this.input, 107, CritereParser.FOLLOW_GET_PARTITION_Y_in_functioncall2915);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2921);
                    p = this.paramlist();
                    final RecognizerSharedState state216 = this.state;
                    --state216._fsp;
                    crit = new GetPartitionY(p);
                    break;
                }
                case 217: {
                    this.match((IntStream)this.input, 114, CritereParser.FOLLOW_GET_TOTAL_HP_IN_PCT_in_functioncall2928);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2934);
                    p = this.paramlist();
                    final RecognizerSharedState state217 = this.state;
                    --state217._fsp;
                    crit = new GetTotalHpInPercent(p);
                    break;
                }
                case 218: {
                    this.match((IntStream)this.input, 81, CritereParser.FOLLOW_GET_ALLIES_COUNT_in_functioncall2940);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2946);
                    p = this.paramlist();
                    final RecognizerSharedState state218 = this.state;
                    --state218._fsp;
                    crit = new GetAlliesCount(p);
                    break;
                }
                case 219: {
                    this.match((IntStream)this.input, 225, CritereParser.FOLLOW_IS_CELL_BEHIND_TARGET_FREE_in_functioncall2952);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2958);
                    p = this.paramlist();
                    final RecognizerSharedState state219 = this.state;
                    --state219._fsp;
                    crit = new IsCellBehindTargetFree(p);
                    break;
                }
                case 220: {
                    this.match((IntStream)this.input, 110, CritereParser.FOLLOW_GET_STATE_LEVEL_in_functioncall2964);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2970);
                    p = this.paramlist();
                    final RecognizerSharedState state220 = this.state;
                    --state220._fsp;
                    crit = new GetStateLevel(p);
                    break;
                }
                case 221: {
                    this.match((IntStream)this.input, 243, CritereParser.FOLLOW_IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA_in_functioncall2976);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2982);
                    p = this.paramlist();
                    final RecognizerSharedState state221 = this.state;
                    --state221._fsp;
                    crit = new IsOnOriginalControllerSpecificArea(p);
                    break;
                }
                case 222: {
                    this.match((IntStream)this.input, 142, CritereParser.FOLLOW_HAS_ANOTHER_SAME_EQUIPMENT_in_functioncall2988);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall2994);
                    p = this.paramlist();
                    final RecognizerSharedState state222 = this.state;
                    --state222._fsp;
                    crit = new HasAnotherSameEquipment(p);
                    break;
                }
                case 223: {
                    this.match((IntStream)this.input, 241, CritereParser.FOLLOW_IS_LOCKED_in_functioncall3000);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3006);
                    p = this.paramlist();
                    final RecognizerSharedState state223 = this.state;
                    --state223._fsp;
                    crit = new IsLocked(p);
                    break;
                }
                case 224: {
                    this.match((IntStream)this.input, 154, CritereParser.FOLLOW_HAS_STATE_FROM_USER_in_functioncall3012);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3018);
                    p = this.paramlist();
                    final RecognizerSharedState state224 = this.state;
                    --state224._fsp;
                    crit = new HasStateFromUser(p);
                    break;
                }
                case 225: {
                    this.match((IntStream)this.input, 102, CritereParser.FOLLOW_GET_HUMAN_ALLIES_COUNT_IN_RANGE_in_functioncall3024);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3030);
                    p = this.paramlist();
                    final RecognizerSharedState state225 = this.state;
                    --state225._fsp;
                    crit = new GetHumanAlliesCountInRange(p);
                    break;
                }
                case 226: {
                    this.match((IntStream)this.input, 229, CritereParser.FOLLOW_IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL_in_functioncall3036);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3042);
                    p = this.paramlist();
                    final RecognizerSharedState state226 = this.state;
                    --state226._fsp;
                    crit = new IsCharacterWithHighestStateLevel(p);
                    break;
                }
                case 227: {
                    this.match((IntStream)this.input, 149, CritereParser.FOLLOW_HAS_GUILD_BONUS_in_functioncall3048);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3054);
                    p = this.paramlist();
                    final RecognizerSharedState state227 = this.state;
                    --state227._fsp;
                    crit = new HasGuildBonus(p);
                    break;
                }
                case 228: {
                    this.match((IntStream)this.input, 103, CritereParser.FOLLOW_GET_NEXT_FIGHTER_IN_TIMELINE_in_functioncall3060);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3066);
                    p = this.paramlist();
                    final RecognizerSharedState state228 = this.state;
                    --state228._fsp;
                    crit = new GetNextFighterInTimeline(p);
                    break;
                }
                case 229: {
                    this.match((IntStream)this.input, 258, CritereParser.FOLLOW_IS_TRIGGERED_BY_ZONE_EFFECT_in_functioncall3072);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3078);
                    p = this.paramlist();
                    final RecognizerSharedState state229 = this.state;
                    --state229._fsp;
                    crit = new IsTriggeredByZoneEffect(p);
                    break;
                }
                case 230: {
                    this.match((IntStream)this.input, 259, CritereParser.FOLLOW_IS_TRIGGERING_EFFECT_CRITICAL_in_functioncall3084);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3090);
                    p = this.paramlist();
                    final RecognizerSharedState state230 = this.state;
                    --state230._fsp;
                    crit = new IsTriggeringEffectCritical(p);
                    break;
                }
                case 231: {
                    this.match((IntStream)this.input, 82, CritereParser.FOLLOW_GET_BOOLEAN_SYSTEM_CONFIGURATION_in_functioncall3096);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3102);
                    p = this.paramlist();
                    final RecognizerSharedState state231 = this.state;
                    --state231._fsp;
                    crit = new GetBooleanSystemConfiguration(p);
                    break;
                }
                case 232: {
                    this.match((IntStream)this.input, 97, CritereParser.FOLLOW_GET_FIGHTERS_MIN_LEVEL_in_functioncall3108);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3114);
                    p = this.paramlist();
                    final RecognizerSharedState state232 = this.state;
                    --state232._fsp;
                    crit = new GetFightersMinLevel(p);
                    break;
                }
                case 233: {
                    this.match((IntStream)this.input, 96, CritereParser.FOLLOW_GET_FIGHTERS_MAX_LEVEL_in_functioncall3120);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3126);
                    p = this.paramlist();
                    final RecognizerSharedState state233 = this.state;
                    --state233._fsp;
                    crit = new GetFightersMaxLevel(p);
                    break;
                }
                case 234: {
                    this.match((IntStream)this.input, 94, CritereParser.FOLLOW_GET_FIGHTERS_LEVEL_DIFF_in_functioncall3132);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3138);
                    p = this.paramlist();
                    final RecognizerSharedState state234 = this.state;
                    --state234._fsp;
                    crit = new GetFightersLevelDiff(p);
                    break;
                }
                case 235: {
                    this.match((IntStream)this.input, 95, CritereParser.FOLLOW_GET_FIGHTERS_LEVEL_SUM_in_functioncall3144);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3150);
                    p = this.paramlist();
                    final RecognizerSharedState state235 = this.state;
                    --state235._fsp;
                    crit = new GetFightersLevelSum(p);
                    break;
                }
                case 236: {
                    this.match((IntStream)this.input, 250, CritereParser.FOLLOW_IS_PRELOADING_in_functioncall3156);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3162);
                    p = this.paramlist();
                    final RecognizerSharedState state236 = this.state;
                    --state236._fsp;
                    crit = new IsPreloading(p);
                    break;
                }
                case 237: {
                    this.match((IntStream)this.input, 160, CritereParser.FOLLOW_HAS_UNLOCKED_COMPANION_in_functioncall3168);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3174);
                    p = this.paramlist();
                    final RecognizerSharedState state237 = this.state;
                    --state237._fsp;
                    crit = new HasUnlockedCompanion(p);
                    break;
                }
                case 238: {
                    this.match((IntStream)this.input, 224, CritereParser.FOLLOW_IS_CASTER_FACING_FIGHTER_in_functioncall3180);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3186);
                    p = this.paramlist();
                    final RecognizerSharedState state238 = this.state;
                    --state238._fsp;
                    crit = new IsCasterFacingFighter(p);
                    break;
                }
                case 239: {
                    this.match((IntStream)this.input, 104, CritereParser.FOLLOW_GET_OWN_ARMOR_COUNT_in_functioncall3192);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3198);
                    p = this.paramlist();
                    final RecognizerSharedState state239 = this.state;
                    --state239._fsp;
                    crit = new GetOwnArmorCount(p);
                    break;
                }
                case 240: {
                    this.match((IntStream)this.input, 145, CritereParser.FOLLOW_HAS_CASTER_FECA_ARMOR_in_functioncall3204);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3210);
                    p = this.paramlist();
                    final RecognizerSharedState state240 = this.state;
                    --state240._fsp;
                    crit = new HasCasterFecaArmor(p);
                    break;
                }
                case 241: {
                    this.match((IntStream)this.input, 105, CritereParser.FOLLOW_GET_OWN_TEAM_STATE_COUNT_IN_RANGE_in_functioncall3216);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3222);
                    p = this.paramlist();
                    final RecognizerSharedState state241 = this.state;
                    --state241._fsp;
                    crit = new GetOwnTeamStateCountInRange((List<ParserObject>)p);
                    break;
                }
                case 242: {
                    this.match((IntStream)this.input, 93, CritereParser.FOLLOW_GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE_in_functioncall3228);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3234);
                    p = this.paramlist();
                    final RecognizerSharedState state242 = this.state;
                    --state242._fsp;
                    crit = new GetFightersCharacteristicMaxValue(p);
                    break;
                }
                case 243: {
                    this.match((IntStream)this.input, 155, CritereParser.FOLLOW_HAS_SUBSCRIPTION_LEVEL_in_functioncall3240);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3246);
                    p = this.paramlist();
                    final RecognizerSharedState state243 = this.state;
                    --state243._fsp;
                    crit = new HasSubscriptionLevel(p);
                    break;
                }
                case 244: {
                    this.match((IntStream)this.input, 212, CritereParser.FOLLOW_ISPVP_in_functioncall3252);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3258);
                    p = this.paramlist();
                    final RecognizerSharedState state244 = this.state;
                    --state244._fsp;
                    crit = new IsPvp(p);
                    break;
                }
                case 245: {
                    this.match((IntStream)this.input, 137, CritereParser.FOLLOW_HASPVPRANK_in_functioncall3264);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3270);
                    p = this.paramlist();
                    final RecognizerSharedState state245 = this.state;
                    --state245._fsp;
                    crit = new HasPvpRank(p);
                    break;
                }
                case 246: {
                    this.match((IntStream)this.input, 232, CritereParser.FOLLOW_IS_ENNEMY_NATION_in_functioncall3276);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3282);
                    p = this.paramlist();
                    final RecognizerSharedState state246 = this.state;
                    --state246._fsp;
                    crit = new IsEnnemyNation(p);
                    break;
                }
                case 247: {
                    this.match((IntStream)this.input, 251, CritereParser.FOLLOW_IS_PVP_STATE_ACTIVE_in_functioncall3288);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3294);
                    p = this.paramlist();
                    final RecognizerSharedState state247 = this.state;
                    --state247._fsp;
                    crit = new IsPvpStateActive(p);
                    break;
                }
                case 248: {
                    this.match((IntStream)this.input, 54, CritereParser.FOLLOW_GETLASTINSTANCEID_in_functioncall3300);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3305);
                    p = this.paramlist();
                    final RecognizerSharedState state248 = this.state;
                    --state248._fsp;
                    crit = new GetCommonLastInstanceId(p);
                    break;
                }
                case 249: {
                    this.match((IntStream)this.input, 80, CritereParser.FOLLOW_GET_ACTIVE_SPELL_ID_in_functioncall3311);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3316);
                    p = this.paramlist();
                    final RecognizerSharedState state249 = this.state;
                    --state249._fsp;
                    crit = new GetActiveSpellId(p);
                    break;
                }
                case 250: {
                    this.match((IntStream)this.input, 236, CritereParser.FOLLOW_IS_HOSTILE_in_functioncall3322);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3328);
                    p = this.paramlist();
                    final RecognizerSharedState state250 = this.state;
                    --state250._fsp;
                    crit = new IsHostile(p);
                    break;
                }
                case 251: {
                    this.match((IntStream)this.input, 143, CritereParser.FOLLOW_HAS_BEEN_NAUGHTY_in_functioncall3334);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3340);
                    p = this.paramlist();
                    final RecognizerSharedState state251 = this.state;
                    --state251._fsp;
                    crit = new HasBeenNaughty(p);
                    break;
                }
                case 252: {
                    this.match((IntStream)this.input, 271, CritereParser.FOLLOW_NB_GATES_in_functioncall3346);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3352);
                    p = this.paramlist();
                    final RecognizerSharedState state252 = this.state;
                    --state252._fsp;
                    crit = new NbGates(p);
                    break;
                }
                case 253: {
                    this.match((IntStream)this.input, 113, CritereParser.FOLLOW_GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall3358);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3364);
                    p = this.paramlist();
                    final RecognizerSharedState state253 = this.state;
                    --state253._fsp;
                    crit = new GetTeamEffectAreaCountInRange(p);
                    break;
                }
                case 254: {
                    this.match((IntStream)this.input, 161, CritereParser.FOLLOW_HAS_VALID_GATE_FOR_TP_in_functioncall3370);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3376);
                    p = this.paramlist();
                    final RecognizerSharedState state254 = this.state;
                    --state254._fsp;
                    crit = new HasValidGateForTp(p);
                    break;
                }
                case 255: {
                    this.match((IntStream)this.input, 298, CritereParser.FOLLOW_USE_GATE_EFFECT_in_functioncall3382);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3388);
                    p = this.paramlist();
                    final RecognizerSharedState state255 = this.state;
                    --state255._fsp;
                    crit = new UseGateEffect(p);
                    break;
                }
                case 256: {
                    this.match((IntStream)this.input, 84, CritereParser.FOLLOW_GET_CURRENT_FIGHTER_ID_in_functioncall3394);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3400);
                    p = this.paramlist();
                    final RecognizerSharedState state256 = this.state;
                    --state256._fsp;
                    crit = new GetCurrentFighterId(p);
                    break;
                }
                case 257: {
                    this.match((IntStream)this.input, 235, CritereParser.FOLLOW_IS_HERO_in_functioncall3406);
                    this.pushFollow(CritereParser.FOLLOW_paramlist_in_functioncall3412);
                    p = this.paramlist();
                    final RecognizerSharedState state257 = this.state;
                    --state257._fsp;
                    crit = new IsHero(p);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return crit;
    }
    
    public final ParserObject atoms() throws RecognitionException {
        ParserObject crit = null;
        Token t = null;
        Token e = null;
        ParserObject e2 = null;
        ParserObject e3 = null;
        ParserObject e4 = null;
        ParserObject f = null;
        ParserObject l = null;
        try {
            int alt12 = 9;
            switch (this.input.LA(1)) {
                case 5:
                case 28:
                case 29:
                case 168:
                case 290:
                case 297: {
                    alt12 = 1;
                    break;
                }
                case 283: {
                    alt12 = 2;
                    break;
                }
                case 287: {
                    alt12 = 3;
                    break;
                }
                case 11: {
                    alt12 = 4;
                    break;
                }
                case 276: {
                    alt12 = 5;
                    break;
                }
                case 264: {
                    alt12 = 6;
                    break;
                }
                case 6:
                case 7:
                case 8:
                case 12:
                case 14:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 23:
                case 24:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 131:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 169:
                case 170:
                case 171:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 185:
                case 186:
                case 187:
                case 188:
                case 189:
                case 190:
                case 191:
                case 192:
                case 193:
                case 194:
                case 195:
                case 196:
                case 197:
                case 198:
                case 199:
                case 200:
                case 201:
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                case 209:
                case 210:
                case 211:
                case 212:
                case 213:
                case 214:
                case 215:
                case 216:
                case 217:
                case 218:
                case 219:
                case 220:
                case 221:
                case 222:
                case 223:
                case 224:
                case 225:
                case 226:
                case 227:
                case 228:
                case 229:
                case 230:
                case 231:
                case 232:
                case 233:
                case 234:
                case 235:
                case 236:
                case 237:
                case 238:
                case 239:
                case 240:
                case 241:
                case 242:
                case 243:
                case 244:
                case 245:
                case 246:
                case 247:
                case 248:
                case 249:
                case 250:
                case 251:
                case 252:
                case 253:
                case 254:
                case 255:
                case 256:
                case 257:
                case 258:
                case 259:
                case 260:
                case 261:
                case 267:
                case 268:
                case 269:
                case 270:
                case 271:
                case 272:
                case 273:
                case 274:
                case 275:
                case 278:
                case 280:
                case 282:
                case 288:
                case 289:
                case 291:
                case 292:
                case 296:
                case 298:
                case 301: {
                    alt12 = 7;
                    break;
                }
                case 299: {
                    alt12 = 8;
                    break;
                }
                case 15: {
                    alt12 = 9;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 12, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt12) {
                case 1: {
                    this.pushFollow(CritereParser.FOLLOW_constants_in_atoms3440);
                    e2 = this.constants();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    crit = e2;
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 283, CritereParser.FOLLOW_PG_in_atoms3455);
                    this.pushFollow(CritereParser.FOLLOW_expr_in_atoms3459);
                    e3 = this.expr();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    this.match((IntStream)this.input, 281, CritereParser.FOLLOW_PD_in_atoms3461);
                    crit = e3;
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 287, CritereParser.FOLLOW_SHARP_in_atoms3477);
                    this.pushFollow(CritereParser.FOLLOW_expr_in_atoms3481);
                    e3 = this.expr();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    this.match((IntStream)this.input, 287, CritereParser.FOLLOW_SHARP_in_atoms3483);
                    crit = e3;
                    crit.setDisplayable(false);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 11, CritereParser.FOLLOW_AT_in_atoms3499);
                    this.match((IntStream)this.input, 283, CritereParser.FOLLOW_PG_in_atoms3501);
                    t = (Token)this.match((IntStream)this.input, 290, CritereParser.FOLLOW_STRING_in_atoms3505);
                    this.match((IntStream)this.input, 281, CritereParser.FOLLOW_PD_in_atoms3507);
                    this.pushFollow(CritereParser.FOLLOW_expr_in_atoms3511);
                    e3 = this.expr();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    this.match((IntStream)this.input, 11, CritereParser.FOLLOW_AT_in_atoms3513);
                    crit = e3;
                    crit.setInvalidCriterionTradKey((t != null) ? t.getText() : null);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 276, CritereParser.FOLLOW_NOT_in_atoms3529);
                    this.pushFollow(CritereParser.FOLLOW_atoms_in_atoms3533);
                    e2 = this.atoms();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    crit = ((SimpleCriterion)e2).setNegate();
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 264, CritereParser.FOLLOW_MINUS_in_atoms3548);
                    this.pushFollow(CritereParser.FOLLOW_atoms_in_atoms3552);
                    e4 = this.atoms();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    ((NumericalValue)e4).setOpposite();
                    crit = e4;
                    break;
                }
                case 7: {
                    this.pushFollow(CritereParser.FOLLOW_functioncall_in_atoms3568);
                    f = this.functioncall();
                    final RecognizerSharedState state7 = this.state;
                    --state7._fsp;
                    crit = f;
                    break;
                }
                case 8: {
                    e = (Token)this.match((IntStream)this.input, 299, CritereParser.FOLLOW_VARNAME_in_atoms3587);
                    crit = this.mem.get((e != null) ? e.getText() : null);
                    if (crit == null) {
                        throw new ParseException("la variable " + ((e != null) ? e.getText() : null) + " n'est pas definie. \n Les constantes et les noms de fonction DOIVENT commencer par une majuscule.");
                    }
                    break;
                }
                case 9: {
                    this.pushFollow(CritereParser.FOLLOW_numberlist_in_atoms3606);
                    l = this.numberlist();
                    final RecognizerSharedState state8 = this.state;
                    --state8._fsp;
                    crit = l;
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return crit;
    }
    
    public final ParserObject constants() throws RecognitionException {
        ParserObject crit = null;
        Token e = null;
        Token t = null;
        Token x = null;
        Token y = null;
        Token z = null;
        try {
            int alt13 = 6;
            switch (this.input.LA(1)) {
                case 297: {
                    alt13 = 1;
                    break;
                }
                case 28: {
                    alt13 = 2;
                    break;
                }
                case 168: {
                    alt13 = 3;
                    break;
                }
                case 29: {
                    alt13 = 4;
                    break;
                }
                case 290: {
                    alt13 = 5;
                    break;
                }
                case 5: {
                    alt13 = 6;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 13, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt13) {
                case 1: {
                    this.match((IntStream)this.input, 297, CritereParser.FOLLOW_TRUE_in_constants3632);
                    crit = new ConstantBooleanCriterion(true);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 28, CritereParser.FOLLOW_FALSE_in_constants3640);
                    crit = new ConstantBooleanCriterion(false);
                    break;
                }
                case 3: {
                    e = (Token)this.match((IntStream)this.input, 168, CritereParser.FOLLOW_INTEGER_in_constants3652);
                    crit = new ConstantIntegerValue(Long.parseLong((e != null) ? e.getText() : null));
                    break;
                }
                case 4: {
                    e = (Token)this.match((IntStream)this.input, 29, CritereParser.FOLLOW_FLOAT_in_constants3662);
                    crit = new ConstantDoubleValue(Double.parseDouble((e != null) ? e.getText() : null));
                    break;
                }
                case 5: {
                    t = (Token)this.match((IntStream)this.input, 290, CritereParser.FOLLOW_STRING_in_constants3671);
                    crit = new StringObject((t != null) ? t.getText() : null);
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 5, CritereParser.FOLLOW_AG_in_constants3677);
                    x = (Token)this.match((IntStream)this.input, 168, CritereParser.FOLLOW_INTEGER_in_constants3683);
                    this.match((IntStream)this.input, 300, CritereParser.FOLLOW_VIRGULE_in_constants3685);
                    y = (Token)this.match((IntStream)this.input, 168, CritereParser.FOLLOW_INTEGER_in_constants3691);
                    this.match((IntStream)this.input, 300, CritereParser.FOLLOW_VIRGULE_in_constants3693);
                    z = (Token)this.match((IntStream)this.input, 168, CritereParser.FOLLOW_INTEGER_in_constants3697);
                    this.match((IntStream)this.input, 4, CritereParser.FOLLOW_AD_in_constants3699);
                    crit = new ConstantPosition(Integer.parseInt((x != null) ? x.getText() : null), Integer.parseInt((y != null) ? y.getText() : null), Short.parseShort((z != null) ? z.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return crit;
    }
    
    static {
        tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AD", "AG", "AI_GET_SPELL_CAST_COUNT", "AI_HAS_CAST_SPELL", "AI_HAS_MOVED", "AND", "ASSIGN", "AT", "BARRELAMOUNT", "BD", "BEACONAMOUNT", "BG", "CANBECOMESOLDIERORMILITIAMAN", "CANCARRYTARGET", "CANRESETACHIEVEMENT", "CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER", "CELL_CONTAINS_SPECIFIC_EFFECT_AREA", "CHAR", "DIVIDE", "DOUBLE_OR_QUITS_CRITERION", "EFFECTISFROMHEAL", "ELSE", "EOL", "EQUALS", "FALSE", "FLOAT", "GETACHIEVEMENTVARIABLE", "GETALLIESCOUNTINRANGE", "GETBOOLEANVALUE", "GETCHA", "GETCHAMAX", "GETCHAPCT", "GETCHARACTERDIRECTION", "GETCHARACTERID", "GETCONTROLLERINSAMETEAMCOUNTINRANGE", "GETCRAFTLEARNINGITEM", "GETCRAFTLEVEL", "GETCRIMESCORE", "GETCURRENTPARTITIONNATIONID", "GETDATE", "GETDESTRUCTIBLECOUNTINRANGE", "GETDISTANCEBETWEENCASTERANDTARGET", "GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON", "GETEFFECTCASTER", "GETEFFECTTARGET", "GETENNEMYCOUNTINRANGE", "GETFIGHTMODEL", "GETIEPOSITION", "GETINSTANCEID", "GETKAMASCOUNT", "GETLASTINSTANCEID", "GETLEVEL", "GETLOCKINCREMENT", "GETMONST", "GETMONTH", "GETNATIONALIGNMENT", "GETNATIONID", "GETNATIONRANK", "GETNATIVENATIONID", "GETPOSITION", "GETPROTECTORNATIONID", "GETRANDOMNUMBER", "GETSATISFACTIONLEVEL", "GETSKILLLEVEL", "GETSPELLLEVEL", "GETSPELLTREELEVEL", "GETSTASISGAUGE", "GETSTATECOUNTINRANGE", "GETTEAMID", "GETTERRITORYID", "GETTERRITORYNATIONID", "GETTIME", "GETTITLE", "GETTRIGGEREREFFECTCASTER", "GETWAKFUGAUGE", "GETWALLCOUNTINRANGE", "GET_ACTIVE_SPELL_ID", "GET_ALLIES_COUNT", "GET_BOOLEAN_SYSTEM_CONFIGURATION", "GET_CHALLENGE_UNAVAILABILITY_DURATION", "GET_CURRENT_FIGHTER_ID", "GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER", "GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE", "GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS", "GET_EFFECT_AREA_COUNT_IN_RANGE", "GET_EFFECT_CASTER_ORIGINAL_CONTROLLER", "GET_EFFECT_TARGET_ORIGINAL_CONTROLLER", "GET_ENEMIES_HUMAN_COUNT_IN_RANGE", "GET_FGHT_CURRENT_TABLE_TURN", "GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE", "GET_FIGHTERS_LEVEL_DIFF", "GET_FIGHTERS_LEVEL_SUM", "GET_FIGHTERS_MAX_LEVEL", "GET_FIGHTERS_MIN_LEVEL", "GET_FIGHTERS_WITH_BREED_IN_RANGE", "GET_FIGHTER_ID", "GET_GUILD_LEVEL", "GET_GUILD_PARTNER_COUNT_IN_FIGHT", "GET_HUMAN_ALLIES_COUNT_IN_RANGE", "GET_NEXT_FIGHTER_IN_TIMELINE", "GET_OWN_ARMOR_COUNT", "GET_OWN_TEAM_STATE_COUNT_IN_RANGE", "GET_PARTITION_X", "GET_PARTITION_Y", "GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT", "GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE", "GET_STATE_LEVEL", "GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA", "GET_TARGET_COUNT_IN_BEACON_AREA", "GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE", "GET_TOTAL_HP_IN_PCT", "GET_TRIGGERING_ANCESTORS_COUNT", "GET_TRIGGERING_EFFECT_CASTER", "GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER", "GET_TRIGGERING_EFFECT_ID", "GET_TRIGGERING_EFFECT_TARGET", "GET_TRIGGERING_EFFECT_TARGET_BREED_ID", "GET_TRIGGERING_EFFECT_VALUE", "GET_X", "GET_XELOR_DIALS_COUNT", "GET_Y", "GET_Z", "HASAVAILABLECREATUREINSYMBIOT", "HASCRAFT", "HASEMOTE", "HASEQID", "HASEQTYPE", "HASFIGHTPROPERTY", "HASFREECELLINEFFECTAREA", "HASFREESURROUNDINGCELL", "HASLINEOFSIGHT", "HASNATIONJOB", "HASNTEVOLVEDSINCE", "HASPVPRANK", "HASSTATE", "HASSUMMONS", "HASSUMMONWITHBREED", "HASWORLDPROPERTY", "HAS_ANOTHER_SAME_EQUIPMENT", "HAS_BEEN_NAUGHTY", "HAS_BEEN_RAISED_BY_EFFECT", "HAS_CASTER_FECA_ARMOR", "HAS_EFFECT_WITH_ACTION_ID", "HAS_EFFECT_WITH_SPECIFIC_ID", "HAS_FECA_ARMOR", "HAS_GUILD_BONUS", "HAS_LINE_OF_SIGHT_FROM_ENEMY", "HAS_LINE_OF_SIGHT_TO_ENEMY", "HAS_LOOT", "HAS_STATE_FROM_LEVEL", "HAS_STATE_FROM_USER", "HAS_SUBSCRIPTION_LEVEL", "HAS_SURROUNDING_CELL_WITH_EFFECT_AREA", "HAS_SURROUNDING_CELL_WITH_OWN_BARREL", "HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA", "HAS_SURROUNDING_CELL_WITH_OWN_SUMMON", "HAS_UNLOCKED_COMPANION", "HAS_VALID_GATE_FOR_TP", "HAS_VALID_PATH_TO_TARGET", "HAS_WEAPON_TYPE", "HYP", "IF", "INF", "INFEQ", "INTEGER", "ISABANDONNING", "ISACCOUNTSUBSCRIBED", "ISACHIEVEMENTACTIVE", "ISACHIEVEMENTCOMPLETE", "ISACHIEVEMENTFAILED", "ISACHIEVEMENTOBJECTIVECOMPLETE", "ISACHIEVEMENTREPEATABLE", "ISACHIEVEMENTRUNNING", "ISACTIVATEDBYELEMENT", "ISACTIVATEDBYSPELL", "ISAFTER", "ISBACKSTAB", "ISBOMB", "ISBREED", "ISBREEDFAMILY", "ISBREEDID", "ISCARRIED", "ISCARRYING", "ISCHALLENGEUSER", "ISDAY", "ISDEAD", "ISDEPOSIT", "ISENNEMY", "ISEQUIPPEDWITHSET", "ISFACESTABBED", "ISFLEEING", "ISHOUR", "ISINGROUP", "ISMONSTERBREED", "ISNATIONFIRSTINDUNGEONLADDER", "ISOFFPLAY", "ISONEFFECTAREATYPE", "ISONSPECIFICEFFECTAREA", "ISONSPECIFICEFFECTAREAWITHSPECIFICSTATE", "ISOWNBEACON", "ISOWNBOMB", "ISOWNDEPOSIT", "ISOWNGLYPH", "ISOWNHOUR", "ISOWNSPECIFICAREA", "ISOWNSUMMON", "ISPASSEPORTACTIVE", "ISPROTECTORINFIGHT", "ISPVP", "ISSEASON", "ISSEX", "ISSPECIFICAREA", "ISSPECIFICAREAWITHSPECIFICSTATE", "ISTARGETCELLFREE", "ISTUNNEL", "ISUNDEAD", "ISUNDERCONTROL", "ISZONEINCHAOS", "IS_CARRYING_OWN_BARREL", "IS_CARRYING_OWN_BOMB", "IS_CASTER_FACING_FIGHTER", "IS_CELL_BEHIND_TARGET_FREE", "IS_CHALLENGER", "IS_CHARACTER", "IS_CHARACTERISTIC_FULL", "IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL", "IS_COMPANION", "IS_CONTROLLED_BY_AI", "IS_ENNEMY_NATION", "IS_FECA_GLYPH_CENTER", "IS_FREE_CELL", "IS_HERO", "IS_HOSTILE", "IS_IN_ALIGNMENT", "IS_IN_FIGHT", "IS_IN_GUILD", "IS_IN_PLAY", "IS_LOCKED", "IS_ON_BORDER_CELL", "IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA", "IS_ON_OWN_DIAL", "IS_ORIGINAL_CONTROLLER", "IS_OUT_OF_PLAY", "IS_OWN_AREA", "IS_OWN_FECA_GLYPH", "IS_PLAYER", "IS_PRELOADING", "IS_PVP_STATE_ACTIVE", "IS_SELECTED_CREATURE_AVAILABLE", "IS_SIDE_STABBED", "IS_SUMMON", "IS_SUMMON_FROM_SYMBIOT", "IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE", "IS_TARGET_ON_SAME_TEAM", "IS_TRIGGERED_BY_ZONE_EFFECT", "IS_TRIGGERING_EFFECT_CRITICAL", "ITEMQUANTITY", "LEADERSHIPFORCURRENTINVOC", "MAJ", "MIN", "MINUS", "MOD", "MULT", "NBBOMB", "NB_ALL_AREAS", "NB_AREAS_WITH_BASE_ID", "NB_FECA_GLYPH", "NB_GATES", "NB_GLYPHS", "NB_HYDRANDS", "NB_ROUBLABOT", "NB_STEAMBOTS", "NOT", "NOT_EQUALS", "OPPONENTSCONTAINSNATIONENEMY", "OR", "OWNSBEACON", "PD", "PETWITHINRANGE", "PG", "PLUS", "POINT", "PV", "SHARP", "SLOTSINBAG", "SPACEINSYMBIOT", "STRING", "SUMMONAMOUNT", "SUMMONSLEADERSHIPSCORE", "SUP", "SUPEQ", "THEN", "TRAPAMOUNT", "TRUE", "USE_GATE_EFFECT", "VARNAME", "VIRGULE", "WALLAMOUNT", "WS" };
        m_logger = Logger.getLogger((Class)CritereParser.class);
        FOLLOW_statement_in_critere47 = new BitSet(new long[] { -241182238L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_statement_in_critere56 = new BitSet(new long[] { -241182238L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_statement79 = new BitSet(new long[] { 67108864L, 0L, 0L, 0L, 1073741824L });
        FOLLOW_set_in_statement82 = new BitSet(new long[] { 67108866L });
        FOLLOW_EOL_in_statement90 = new BitSet(new long[] { 67108866L });
        FOLLOW_VARNAME_in_statement99 = new BitSet(new long[] { 1024L });
        FOLLOW_ASSIGN_in_statement101 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_statement107 = new BitSet(new long[] { 67108864L, 0L, 0L, 0L, 1073741824L });
        FOLLOW_set_in_statement109 = new BitSet(new long[] { 67108866L });
        FOLLOW_EOL_in_statement117 = new BitSet(new long[] { 67108866L });
        FOLLOW_factor_in_expr147 = new BitSet(new long[] { 514L, 0L, 0L, 0L, 276824320L });
        FOLLOW_AND_in_expr164 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr170 = new BitSet(new long[] { 2L });
        FOLLOW_OR_in_expr187 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr191 = new BitSet(new long[] { 2L });
        FOLLOW_PLUS_in_expr199 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr205 = new BitSet(new long[] { 2L });
        FOLLOW_MINUS_in_expr214 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr220 = new BitSet(new long[] { 2L });
        FOLLOW_IF_in_expr230 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_PG_in_expr232 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr236 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 33554432L });
        FOLLOW_PD_in_expr238 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 549755813888L });
        FOLLOW_THEN_in_expr240 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr246 = new BitSet(new long[] { 33554432L });
        FOLLOW_ELSE_in_expr248 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_expr252 = new BitSet(new long[] { 2L });
        FOLLOW_expr_in_exprlist273 = new BitSet(new long[] { 2L, 0L, 0L, 0L, 17592186044416L });
        FOLLOW_VIRGULE_in_exprlist278 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_exprlist282 = new BitSet(new long[] { 2L, 0L, 0L, 0L, 17592186044416L });
        FOLLOW_PG_in_paramlist297 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_exprlist_in_paramlist301 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 33554432L });
        FOLLOW_PD_in_paramlist305 = new BitSet(new long[] { 2L });
        FOLLOW_PG_in_paramlist309 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 33554432L });
        FOLLOW_PD_in_paramlist311 = new BitSet(new long[] { 2L });
        FOLLOW_BG_in_numberlist325 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_exprlist_in_numberlist329 = new BitSet(new long[] { 8192L });
        FOLLOW_BD_in_numberlist331 = new BitSet(new long[] { 2L });
        FOLLOW_BG_in_numberlist337 = new BitSet(new long[] { 8192L });
        FOLLOW_BD_in_numberlist339 = new BitSet(new long[] { 2L });
        FOLLOW_atoms_in_factor357 = new BitSet(new long[] { 138412034L, 0L, 824633720832L, 0L, 412855830016L });
        FOLLOW_MULT_in_factor364 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor369 = new BitSet(new long[] { 2L });
        FOLLOW_DIVIDE_in_factor375 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor381 = new BitSet(new long[] { 2L });
        FOLLOW_MOD_in_factor388 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor394 = new BitSet(new long[] { 2L });
        FOLLOW_NOT_EQUALS_in_factor401 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor407 = new BitSet(new long[] { 2L });
        FOLLOW_EQUALS_in_factor414 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor420 = new BitSet(new long[] { 2L });
        FOLLOW_INF_in_factor427 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor433 = new BitSet(new long[] { 2L });
        FOLLOW_INFEQ_in_factor440 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor446 = new BitSet(new long[] { 2L });
        FOLLOW_SUP_in_factor453 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor459 = new BitSet(new long[] { 2L });
        FOLLOW_SUPEQ_in_factor466 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor472 = new BitSet(new long[] { 2L });
        FOLLOW_POINT_in_factor479 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_factor_in_factor485 = new BitSet(new long[] { 2L });
        FOLLOW_HASEQTYPE_in_functioncall504 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall508 = new BitSet(new long[] { 2L });
        FOLLOW_HASEQID_in_functioncall514 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall518 = new BitSet(new long[] { 2L });
        FOLLOW_HASSUMMONS_in_functioncall524 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall528 = new BitSet(new long[] { 2L });
        FOLLOW_GETCHA_in_functioncall534 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall538 = new BitSet(new long[] { 2L });
        FOLLOW_GETCHAPCT_in_functioncall544 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall548 = new BitSet(new long[] { 2L });
        FOLLOW_GETCHAMAX_in_functioncall554 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall558 = new BitSet(new long[] { 2L });
        FOLLOW_ISENNEMY_in_functioncall564 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall568 = new BitSet(new long[] { 2L });
        FOLLOW_CANCARRYTARGET_in_functioncall574 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall578 = new BitSet(new long[] { 2L });
        FOLLOW_SPACEINSYMBIOT_in_functioncall584 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall588 = new BitSet(new long[] { 2L });
        FOLLOW_TRAPAMOUNT_in_functioncall594 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall598 = new BitSet(new long[] { 2L });
        FOLLOW_WALLAMOUNT_in_functioncall604 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall608 = new BitSet(new long[] { 2L });
        FOLLOW_IS_SELECTED_CREATURE_AVAILABLE_in_functioncall614 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall618 = new BitSet(new long[] { 2L });
        FOLLOW_OWNSBEACON_in_functioncall623 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall627 = new BitSet(new long[] { 2L });
        FOLLOW_ISSPECIFICAREA_in_functioncall633 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall637 = new BitSet(new long[] { 2L });
        FOLLOW_ISSPECIFICAREAWITHSPECIFICSTATE_in_functioncall643 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall647 = new BitSet(new long[] { 2L });
        FOLLOW_GETTIME_in_functioncall653 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall659 = new BitSet(new long[] { 2L });
        FOLLOW_ISDAY_in_functioncall665 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall671 = new BitSet(new long[] { 2L });
        FOLLOW_ISBREEDID_in_functioncall677 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall681 = new BitSet(new long[] { 2L });
        FOLLOW_ISBREED_in_functioncall686 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall690 = new BitSet(new long[] { 2L });
        FOLLOW_ISSEASON_in_functioncall695 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall701 = new BitSet(new long[] { 2L });
        FOLLOW_HASSTATE_in_functioncall706 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall710 = new BitSet(new long[] { 2L });
        FOLLOW_GETSKILLLEVEL_in_functioncall715 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall719 = new BitSet(new long[] { 2L });
        FOLLOW_GETSPELLLEVEL_in_functioncall724 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall728 = new BitSet(new long[] { 2L });
        FOLLOW_GETSPELLTREELEVEL_in_functioncall733 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall737 = new BitSet(new long[] { 2L });
        FOLLOW_GETTEAMID_in_functioncall742 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall746 = new BitSet(new long[] { 2L });
        FOLLOW_GETMONST_in_functioncall751 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall755 = new BitSet(new long[] { 2L });
        FOLLOW_PETWITHINRANGE_in_functioncall761 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall765 = new BitSet(new long[] { 2L });
        FOLLOW_SUMMONAMOUNT_in_functioncall771 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall775 = new BitSet(new long[] { 2L });
        FOLLOW_BEACONAMOUNT_in_functioncall781 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall785 = new BitSet(new long[] { 2L });
        FOLLOW_BARRELAMOUNT_in_functioncall791 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall795 = new BitSet(new long[] { 2L });
        FOLLOW_GET_XELOR_DIALS_COUNT_in_functioncall801 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall805 = new BitSet(new long[] { 2L });
        FOLLOW_ISBACKSTAB_in_functioncall811 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall815 = new BitSet(new long[] { 2L });
        FOLLOW_HASLINEOFSIGHT_in_functioncall820 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall824 = new BitSet(new long[] { 2L });
        FOLLOW_GETPOSITION_in_functioncall829 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall833 = new BitSet(new long[] { 2L });
        FOLLOW_GETCHARACTERID_in_functioncall838 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall842 = new BitSet(new long[] { 2L });
        FOLLOW_GETIEPOSITION_in_functioncall847 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall851 = new BitSet(new long[] { 2L });
        FOLLOW_ISSEX_in_functioncall856 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall862 = new BitSet(new long[] { 2L });
        FOLLOW_SLOTSINBAG_in_functioncall867 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall873 = new BitSet(new long[] { 2L });
        FOLLOW_GETINSTANCEID_in_functioncall878 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall882 = new BitSet(new long[] { 2L });
        FOLLOW_GETEFFECTCASTER_in_functioncall887 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall891 = new BitSet(new long[] { 2L });
        FOLLOW_GETEFFECTTARGET_in_functioncall896 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall900 = new BitSet(new long[] { 2L });
        FOLLOW_GETTRIGGEREREFFECTCASTER_in_functioncall905 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall909 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_EFFECT_ID_in_functioncall914 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall918 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_EFFECT_VALUE_in_functioncall923 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall927 = new BitSet(new long[] { 2L });
        FOLLOW_ITEMQUANTITY_in_functioncall932 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall936 = new BitSet(new long[] { 2L });
        FOLLOW_GETKAMASCOUNT_in_functioncall941 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall945 = new BitSet(new long[] { 2L });
        FOLLOW_ISMONSTERBREED_in_functioncall950 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall954 = new BitSet(new long[] { 2L });
        FOLLOW_GETDISTANCEBETWEENCASTERANDTARGET_in_functioncall959 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall963 = new BitSet(new long[] { 2L });
        FOLLOW_GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON_in_functioncall968 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall972 = new BitSet(new long[] { 2L });
        FOLLOW_ISUNDEAD_in_functioncall977 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall981 = new BitSet(new long[] { 2L });
        FOLLOW_EFFECTISFROMHEAL_in_functioncall986 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall990 = new BitSet(new long[] { 2L });
        FOLLOW_HASWORLDPROPERTY_in_functioncall995 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall999 = new BitSet(new long[] { 2L });
        FOLLOW_HASFIGHTPROPERTY_in_functioncall1004 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1008 = new BitSet(new long[] { 2L });
        FOLLOW_GETMONTH_in_functioncall1013 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1017 = new BitSet(new long[] { 2L });
        FOLLOW_HASNTEVOLVEDSINCE_in_functioncall1022 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1026 = new BitSet(new long[] { 2L });
        FOLLOW_GETLEVEL_in_functioncall1031 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1037 = new BitSet(new long[] { 2L });
        FOLLOW_GETLOCKINCREMENT_in_functioncall1042 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1048 = new BitSet(new long[] { 2L });
        FOLLOW_ISBREEDFAMILY_in_functioncall1053 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1059 = new BitSet(new long[] { 2L });
        FOLLOW_ISCHALLENGEUSER_in_functioncall1065 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1071 = new BitSet(new long[] { 2L });
        FOLLOW_ISUNDERCONTROL_in_functioncall1076 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1082 = new BitSet(new long[] { 2L });
        FOLLOW_ISAFTER_in_functioncall1088 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1094 = new BitSet(new long[] { 2L });
        FOLLOW_GETWAKFUGAUGE_in_functioncall1100 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1106 = new BitSet(new long[] { 2L });
        FOLLOW_GETRANDOMNUMBER_in_functioncall1112 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1118 = new BitSet(new long[] { 2L });
        FOLLOW_GETENNEMYCOUNTINRANGE_in_functioncall1124 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1130 = new BitSet(new long[] { 2L });
        FOLLOW_GETALLIESCOUNTINRANGE_in_functioncall1136 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1142 = new BitSet(new long[] { 2L });
        FOLLOW_GETCONTROLLERINSAMETEAMCOUNTINRANGE_in_functioncall1148 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1154 = new BitSet(new long[] { 2L });
        FOLLOW_GETDESTRUCTIBLECOUNTINRANGE_in_functioncall1160 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1166 = new BitSet(new long[] { 2L });
        FOLLOW_GETWALLCOUNTINRANGE_in_functioncall1172 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1178 = new BitSet(new long[] { 2L });
        FOLLOW_GETNATIONID_in_functioncall1184 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1188 = new BitSet(new long[] { 2L });
        FOLLOW_GETNATIONALIGNMENT_in_functioncall1193 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1197 = new BitSet(new long[] { 2L });
        FOLLOW_GETNATIVENATIONID_in_functioncall1203 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1207 = new BitSet(new long[] { 2L });
        FOLLOW_GETSTASISGAUGE_in_functioncall1212 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1218 = new BitSet(new long[] { 2L });
        FOLLOW_GETDATE_in_functioncall1224 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1230 = new BitSet(new long[] { 2L });
        FOLLOW_ISFACESTABBED_in_functioncall1236 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1242 = new BitSet(new long[] { 2L });
        FOLLOW_GETCRIMESCORE_in_functioncall1248 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1254 = new BitSet(new long[] { 2L });
        FOLLOW_ISDEAD_in_functioncall1260 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1266 = new BitSet(new long[] { 2L });
        FOLLOW_GETSATISFACTIONLEVEL_in_functioncall1272 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1278 = new BitSet(new long[] { 2L });
        FOLLOW_GETBOOLEANVALUE_in_functioncall1284 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1290 = new BitSet(new long[] { 2L });
        FOLLOW_GETCURRENTPARTITIONNATIONID_in_functioncall1296 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1302 = new BitSet(new long[] { 2L });
        FOLLOW_GETTERRITORYID_in_functioncall1308 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1314 = new BitSet(new long[] { 2L });
        FOLLOW_GETPROTECTORNATIONID_in_functioncall1321 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1327 = new BitSet(new long[] { 2L });
        FOLLOW_GET_PROTECTOR_CHALLENGE_KAMA_AMOUNT_in_functioncall1334 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1340 = new BitSet(new long[] { 2L });
        FOLLOW_HASFREESURROUNDINGCELL_in_functioncall1346 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1352 = new BitSet(new long[] { 2L });
        FOLLOW_ISTARGETCELLFREE_in_functioncall1358 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1364 = new BitSet(new long[] { 2L });
        FOLLOW_IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE_in_functioncall1370 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1376 = new BitSet(new long[] { 2L });
        FOLLOW_ISCARRIED_in_functioncall1382 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1388 = new BitSet(new long[] { 2L });
        FOLLOW_ISCARRYING_in_functioncall1394 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1400 = new BitSet(new long[] { 2L });
        FOLLOW_HASAVAILABLECREATUREINSYMBIOT_in_functioncall1406 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1412 = new BitSet(new long[] { 2L });
        FOLLOW_SUMMONSLEADERSHIPSCORE_in_functioncall1418 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1424 = new BitSet(new long[] { 2L });
        FOLLOW_LEADERSHIPFORCURRENTINVOC_in_functioncall1430 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1436 = new BitSet(new long[] { 2L });
        FOLLOW_GETTERRITORYNATIONID_in_functioncall1442 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1448 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNSUMMON_in_functioncall1454 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1460 = new BitSet(new long[] { 2L });
        FOLLOW_GETCHARACTERDIRECTION_in_functioncall1466 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1472 = new BitSet(new long[] { 2L });
        FOLLOW_GETCRAFTLEARNINGITEM_in_functioncall1477 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1483 = new BitSet(new long[] { 2L });
        FOLLOW_HASCRAFT_in_functioncall1488 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1494 = new BitSet(new long[] { 2L });
        FOLLOW_GETCRAFTLEVEL_in_functioncall1499 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1505 = new BitSet(new long[] { 2L });
        FOLLOW_HASEMOTE_in_functioncall1510 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1516 = new BitSet(new long[] { 2L });
        FOLLOW_ISPASSEPORTACTIVE_in_functioncall1521 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1527 = new BitSet(new long[] { 2L });
        FOLLOW_CANBECOMESOLDIERORMILITIAMAN_in_functioncall1532 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1538 = new BitSet(new long[] { 2L });
        FOLLOW_GETTITLE_in_functioncall1544 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1550 = new BitSet(new long[] { 2L });
        FOLLOW_GETNATIONRANK_in_functioncall1556 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1562 = new BitSet(new long[] { 2L });
        FOLLOW_ISEQUIPPEDWITHSET_in_functioncall1569 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1575 = new BitSet(new long[] { 2L });
        FOLLOW_ISHOUR_in_functioncall1581 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1585 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNHOUR_in_functioncall1591 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1595 = new BitSet(new long[] { 2L });
        FOLLOW_ISBOMB_in_functioncall1601 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1607 = new BitSet(new long[] { 2L });
        FOLLOW_ISTUNNEL_in_functioncall1612 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1618 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNBOMB_in_functioncall1623 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1629 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNBEACON_in_functioncall1634 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1640 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNSPECIFICAREA_in_functioncall1646 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1652 = new BitSet(new long[] { 2L });
        FOLLOW_HASSUMMONWITHBREED_in_functioncall1658 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1664 = new BitSet(new long[] { 2L });
        FOLLOW_NBBOMB_in_functioncall1670 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1676 = new BitSet(new long[] { 2L });
        FOLLOW_ISACHIEVEMENTOBJECTIVECOMPLETE_in_functioncall1682 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1687 = new BitSet(new long[] { 2L });
        FOLLOW_ISACHIEVEMENTREPEATABLE_in_functioncall1693 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1698 = new BitSet(new long[] { 2L });
        FOLLOW_CANRESETACHIEVEMENT_in_functioncall1704 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1709 = new BitSet(new long[] { 2L });
        FOLLOW_OPPONENTSCONTAINSNATIONENEMY_in_functioncall1715 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1720 = new BitSet(new long[] { 2L });
        FOLLOW_HASNATIONJOB_in_functioncall1726 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1731 = new BitSet(new long[] { 2L });
        FOLLOW_ISACHIEVEMENTCOMPLETE_in_functioncall1737 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1742 = new BitSet(new long[] { 2L });
        FOLLOW_ISACHIEVEMENTACTIVE_in_functioncall1748 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1753 = new BitSet(new long[] { 2L });
        FOLLOW_ISACHIEVEMENTRUNNING_in_functioncall1762 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1767 = new BitSet(new long[] { 2L });
        FOLLOW_ISACHIEVEMENTFAILED_in_functioncall1776 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1781 = new BitSet(new long[] { 2L });
        FOLLOW_ISPROTECTORINFIGHT_in_functioncall1790 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1796 = new BitSet(new long[] { 2L });
        FOLLOW_ISOFFPLAY_in_functioncall1801 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1807 = new BitSet(new long[] { 2L });
        FOLLOW_IS_IN_PLAY_in_functioncall1812 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1818 = new BitSet(new long[] { 2L });
        FOLLOW_IS_OUT_OF_PLAY_in_functioncall1823 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1829 = new BitSet(new long[] { 2L });
        FOLLOW_IS_IN_FIGHT_in_functioncall1834 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1840 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNDEPOSIT_in_functioncall1845 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1851 = new BitSet(new long[] { 2L });
        FOLLOW_ISINGROUP_in_functioncall1856 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1862 = new BitSet(new long[] { 2L });
        FOLLOW_ISACTIVATEDBYELEMENT_in_functioncall1868 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1874 = new BitSet(new long[] { 2L });
        FOLLOW_ISACTIVATEDBYSPELL_in_functioncall1880 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1886 = new BitSet(new long[] { 2L });
        FOLLOW_ISONEFFECTAREATYPE_in_functioncall1892 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1898 = new BitSet(new long[] { 2L });
        FOLLOW_ISONSPECIFICEFFECTAREA_in_functioncall1904 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1910 = new BitSet(new long[] { 2L });
        FOLLOW_ISONSPECIFICEFFECTAREAWITHSPECIFICSTATE_in_functioncall1919 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1925 = new BitSet(new long[] { 2L });
        FOLLOW_CELL_CONTAINS_SPECIFIC_EFFECT_AREA_in_functioncall1934 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1940 = new BitSet(new long[] { 2L });
        FOLLOW_ISOWNGLYPH_in_functioncall1946 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1952 = new BitSet(new long[] { 2L });
        FOLLOW_ISDEPOSIT_in_functioncall1957 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1963 = new BitSet(new long[] { 2L });
        FOLLOW_GETSTATECOUNTINRANGE_in_functioncall1968 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1974 = new BitSet(new long[] { 2L });
        FOLLOW_ISFLEEING_in_functioncall1979 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1985 = new BitSet(new long[] { 2L });
        FOLLOW_ISABANDONNING_in_functioncall1990 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall1996 = new BitSet(new long[] { 2L });
        FOLLOW_ISNATIONFIRSTINDUNGEONLADDER_in_functioncall2001 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2007 = new BitSet(new long[] { 2L });
        FOLLOW_GETFIGHTMODEL_in_functioncall2012 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2018 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_BARREL_in_functioncall2023 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2029 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_EFFECT_AREA_in_functioncall2034 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2040 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_SURROUNDING_CELL_WITH_EFFECT_AREA_in_functioncall2045 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2051 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CARRYING_OWN_BARREL_in_functioncall2056 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2062 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TARGET_COUNT_IN_BEACON_AREA_in_functioncall2068 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2074 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTERS_WITH_BREED_IN_RANGE_in_functioncall2080 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2086 = new BitSet(new long[] { 2L });
        FOLLOW_AI_HAS_CAST_SPELL_in_functioncall2092 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2098 = new BitSet(new long[] { 2L });
        FOLLOW_AI_HAS_MOVED_in_functioncall2104 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2110 = new BitSet(new long[] { 2L });
        FOLLOW_AI_GET_SPELL_CAST_COUNT_in_functioncall2116 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2122 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TARGETS_COUNT_IN_EFFECT_ZONE_AREA_in_functioncall2128 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2134 = new BitSet(new long[] { 2L });
        FOLLOW_IS_SUMMON_in_functioncall2140 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2146 = new BitSet(new long[] { 2L });
        FOLLOW_IS_SUMMON_FROM_SYMBIOT_in_functioncall2152 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2158 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CONTROLLED_BY_AI_in_functioncall2164 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2170 = new BitSet(new long[] { 2L });
        FOLLOW_GET_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall2176 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2182 = new BitSet(new long[] { 2L });
        FOLLOW_GET_SPECIFIC_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall2188 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2194 = new BitSet(new long[] { 2L });
        FOLLOW_GET_EFFECTAREA_COUNT_IN_RUNNINGEFFECT_AOE_in_functioncall2200 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2206 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_LINE_OF_SIGHT_FROM_ENEMY_in_functioncall2212 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2218 = new BitSet(new long[] { 2L });
        FOLLOW_GET_ENEMIES_HUMAN_COUNT_IN_RANGE_in_functioncall2224 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2230 = new BitSet(new long[] { 2L });
        FOLLOW_IS_TARGET_ON_SAME_TEAM_in_functioncall2236 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2242 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_LOOT_in_functioncall2248 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2254 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_EFFECT_WITH_ACTION_ID_in_functioncall2260 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2266 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CHARACTER_in_functioncall2272 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2278 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_STATE_FROM_LEVEL_in_functioncall2284 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2290 = new BitSet(new long[] { 2L });
        FOLLOW_DOUBLE_OR_QUITS_CRITERION_in_functioncall2296 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2302 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_WEAPON_TYPE_in_functioncall2308 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2314 = new BitSet(new long[] { 2L });
        FOLLOW_IS_OWN_AREA_in_functioncall2320 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2326 = new BitSet(new long[] { 2L });
        FOLLOW_IS_ON_BORDER_CELL_in_functioncall2332 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2338 = new BitSet(new long[] { 2L });
        FOLLOW_NB_ROUBLABOT_in_functioncall2344 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2350 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_EFFECT_WITH_SPECIFIC_ID_in_functioncall2356 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2362 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_FECA_ARMOR_in_functioncall2368 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2374 = new BitSet(new long[] { 2L });
        FOLLOW_IS_FECA_GLYPH_CENTER_in_functioncall2380 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2386 = new BitSet(new long[] { 2L });
        FOLLOW_NB_FECA_GLYPH_in_functioncall2392 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2398 = new BitSet(new long[] { 2L });
        FOLLOW_GETACHIEVEMENTVARIABLE_in_functioncall2404 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2409 = new BitSet(new long[] { 2L });
        FOLLOW_GET_CHALLENGE_UNAVAILABILITY_DURATION_in_functioncall2415 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2420 = new BitSet(new long[] { 2L });
        FOLLOW_GET_EFFECT_CASTER_ORIGINAL_CONTROLLER_in_functioncall2426 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2431 = new BitSet(new long[] { 2L });
        FOLLOW_GET_EFFECT_TARGET_ORIGINAL_CONTROLLER_in_functioncall2437 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2442 = new BitSet(new long[] { 2L });
        FOLLOW_CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER_in_functioncall2448 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2453 = new BitSet(new long[] { 2L });
        FOLLOW_IS_ORIGINAL_CONTROLLER_in_functioncall2459 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2464 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CHARACTERISTIC_FULL_in_functioncall2470 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2475 = new BitSet(new long[] { 2L });
        FOLLOW_ISACCOUNTSUBSCRIBED_in_functioncall2481 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2486 = new BitSet(new long[] { 2L });
        FOLLOW_ISZONEINCHAOS_in_functioncall2492 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2498 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_EFFECT_CASTER_IS_SAME_AS_CASTER_in_functioncall2503 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2508 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_EFFECT_CASTER_in_functioncall2514 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2519 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTER_ID_in_functioncall2525 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2530 = new BitSet(new long[] { 2L });
        FOLLOW_IS_ON_OWN_DIAL_in_functioncall2536 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2542 = new BitSet(new long[] { 2L });
        FOLLOW_NB_HYDRANDS_in_functioncall2548 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2554 = new BitSet(new long[] { 2L });
        FOLLOW_NB_STEAMBOTS_in_functioncall2560 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2566 = new BitSet(new long[] { 2L });
        FOLLOW_IS_OWN_FECA_GLYPH_in_functioncall2572 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2578 = new BitSet(new long[] { 2L });
        FOLLOW_GET_DISTANCE_BETWEEN_TARGET_AND_EFFECT_BEARER_in_functioncall2584 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2590 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FGHT_CURRENT_TABLE_TURN_in_functioncall2596 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2602 = new BitSet(new long[] { 2L });
        FOLLOW_NB_ALL_AREAS_in_functioncall2608 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2614 = new BitSet(new long[] { 2L });
        FOLLOW_NB_GLYPHS_in_functioncall2620 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2626 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_EFFECT_TARGET_BREED_ID_in_functioncall2633 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2639 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_ANCESTORS_COUNT_in_functioncall2646 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2652 = new BitSet(new long[] { 2L });
        FOLLOW_IS_SIDE_STABBED_in_functioncall2658 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2664 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TRIGGERING_EFFECT_TARGET_in_functioncall2670 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2676 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_LINE_OF_SIGHT_TO_ENEMY_in_functioncall2682 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2688 = new BitSet(new long[] { 2L });
        FOLLOW_IS_PLAYER_in_functioncall2694 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2700 = new BitSet(new long[] { 2L });
        FOLLOW_IS_COMPANION_in_functioncall2706 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2712 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CHALLENGER_in_functioncall2718 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2724 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CARRYING_OWN_BOMB_in_functioncall2730 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2736 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_SURROUNDING_CELL_WITH_OWN_SUMMON_in_functioncall2742 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2748 = new BitSet(new long[] { 2L });
        FOLLOW_GET_GUILD_LEVEL_in_functioncall2754 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2760 = new BitSet(new long[] { 2L });
        FOLLOW_IS_IN_GUILD_in_functioncall2766 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2772 = new BitSet(new long[] { 2L });
        FOLLOW_GET_GUILD_PARTNER_COUNT_IN_FIGHT_in_functioncall2778 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2784 = new BitSet(new long[] { 2L });
        FOLLOW_IS_IN_ALIGNMENT_in_functioncall2790 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2796 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_VALID_PATH_TO_TARGET_in_functioncall2802 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2808 = new BitSet(new long[] { 2L });
        FOLLOW_IS_FREE_CELL_in_functioncall2815 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2821 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_BEEN_RAISED_BY_EFFECT_in_functioncall2827 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2833 = new BitSet(new long[] { 2L });
        FOLLOW_GET_X_in_functioncall2839 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2845 = new BitSet(new long[] { 2L });
        FOLLOW_GET_Y_in_functioncall2852 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2858 = new BitSet(new long[] { 2L });
        FOLLOW_GET_Z_in_functioncall2865 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2871 = new BitSet(new long[] { 2L });
        FOLLOW_NB_AREAS_WITH_BASE_ID_in_functioncall2878 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2884 = new BitSet(new long[] { 2L });
        FOLLOW_GET_EFFECTS_COUNT_WITH_SPECIFIC_IDS_in_functioncall2890 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2896 = new BitSet(new long[] { 2L });
        FOLLOW_GET_PARTITION_X_in_functioncall2902 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2908 = new BitSet(new long[] { 2L });
        FOLLOW_GET_PARTITION_Y_in_functioncall2915 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2921 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TOTAL_HP_IN_PCT_in_functioncall2928 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2934 = new BitSet(new long[] { 2L });
        FOLLOW_GET_ALLIES_COUNT_in_functioncall2940 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2946 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CELL_BEHIND_TARGET_FREE_in_functioncall2952 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2958 = new BitSet(new long[] { 2L });
        FOLLOW_GET_STATE_LEVEL_in_functioncall2964 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2970 = new BitSet(new long[] { 2L });
        FOLLOW_IS_ON_ORIGINAL_CONTROLLER_SPECIFIC_EFFECT_AREA_in_functioncall2976 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2982 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_ANOTHER_SAME_EQUIPMENT_in_functioncall2988 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall2994 = new BitSet(new long[] { 2L });
        FOLLOW_IS_LOCKED_in_functioncall3000 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3006 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_STATE_FROM_USER_in_functioncall3012 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3018 = new BitSet(new long[] { 2L });
        FOLLOW_GET_HUMAN_ALLIES_COUNT_IN_RANGE_in_functioncall3024 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3030 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL_in_functioncall3036 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3042 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_GUILD_BONUS_in_functioncall3048 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3054 = new BitSet(new long[] { 2L });
        FOLLOW_GET_NEXT_FIGHTER_IN_TIMELINE_in_functioncall3060 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3066 = new BitSet(new long[] { 2L });
        FOLLOW_IS_TRIGGERED_BY_ZONE_EFFECT_in_functioncall3072 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3078 = new BitSet(new long[] { 2L });
        FOLLOW_IS_TRIGGERING_EFFECT_CRITICAL_in_functioncall3084 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3090 = new BitSet(new long[] { 2L });
        FOLLOW_GET_BOOLEAN_SYSTEM_CONFIGURATION_in_functioncall3096 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3102 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTERS_MIN_LEVEL_in_functioncall3108 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3114 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTERS_MAX_LEVEL_in_functioncall3120 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3126 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTERS_LEVEL_DIFF_in_functioncall3132 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3138 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTERS_LEVEL_SUM_in_functioncall3144 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3150 = new BitSet(new long[] { 2L });
        FOLLOW_IS_PRELOADING_in_functioncall3156 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3162 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_UNLOCKED_COMPANION_in_functioncall3168 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3174 = new BitSet(new long[] { 2L });
        FOLLOW_IS_CASTER_FACING_FIGHTER_in_functioncall3180 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3186 = new BitSet(new long[] { 2L });
        FOLLOW_GET_OWN_ARMOR_COUNT_in_functioncall3192 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3198 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_CASTER_FECA_ARMOR_in_functioncall3204 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3210 = new BitSet(new long[] { 2L });
        FOLLOW_GET_OWN_TEAM_STATE_COUNT_IN_RANGE_in_functioncall3216 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3222 = new BitSet(new long[] { 2L });
        FOLLOW_GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE_in_functioncall3228 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3234 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_SUBSCRIPTION_LEVEL_in_functioncall3240 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3246 = new BitSet(new long[] { 2L });
        FOLLOW_ISPVP_in_functioncall3252 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3258 = new BitSet(new long[] { 2L });
        FOLLOW_HASPVPRANK_in_functioncall3264 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3270 = new BitSet(new long[] { 2L });
        FOLLOW_IS_ENNEMY_NATION_in_functioncall3276 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3282 = new BitSet(new long[] { 2L });
        FOLLOW_IS_PVP_STATE_ACTIVE_in_functioncall3288 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3294 = new BitSet(new long[] { 2L });
        FOLLOW_GETLASTINSTANCEID_in_functioncall3300 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3305 = new BitSet(new long[] { 2L });
        FOLLOW_GET_ACTIVE_SPELL_ID_in_functioncall3311 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3316 = new BitSet(new long[] { 2L });
        FOLLOW_IS_HOSTILE_in_functioncall3322 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3328 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_BEEN_NAUGHTY_in_functioncall3334 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3340 = new BitSet(new long[] { 2L });
        FOLLOW_NB_GATES_in_functioncall3346 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3352 = new BitSet(new long[] { 2L });
        FOLLOW_GET_TEAM_EFFECT_AREA_COUNT_IN_RANGE_in_functioncall3358 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3364 = new BitSet(new long[] { 2L });
        FOLLOW_HAS_VALID_GATE_FOR_TP_in_functioncall3370 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3376 = new BitSet(new long[] { 2L });
        FOLLOW_USE_GATE_EFFECT_in_functioncall3382 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3388 = new BitSet(new long[] { 2L });
        FOLLOW_GET_CURRENT_FIGHTER_ID_in_functioncall3394 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3400 = new BitSet(new long[] { 2L });
        FOLLOW_IS_HERO_in_functioncall3406 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_paramlist_in_functioncall3412 = new BitSet(new long[] { 2L });
        FOLLOW_constants_in_atoms3440 = new BitSet(new long[] { 2L });
        FOLLOW_PG_in_atoms3455 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_atoms3459 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 33554432L });
        FOLLOW_PD_in_atoms3461 = new BitSet(new long[] { 2L });
        FOLLOW_SHARP_in_atoms3477 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_atoms3481 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 2147483648L });
        FOLLOW_SHARP_in_atoms3483 = new BitSet(new long[] { 2L });
        FOLLOW_AT_in_atoms3499 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_PG_in_atoms3501 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 17179869184L });
        FOLLOW_STRING_in_atoms3505 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 33554432L });
        FOLLOW_PD_in_atoms3507 = new BitSet(new long[] { -241182240L, -1L, -893353197585L, -1L, 51812562368831L });
        FOLLOW_expr_in_atoms3511 = new BitSet(new long[] { 2048L });
        FOLLOW_AT_in_atoms3513 = new BitSet(new long[] { 2L });
        FOLLOW_NOT_in_atoms3529 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_atoms_in_atoms3533 = new BitSet(new long[] { 2L });
        FOLLOW_MINUS_in_atoms3548 = new BitSet(new long[] { -241182240L, -1L, -1030792151057L, -1L, 51812562368831L });
        FOLLOW_atoms_in_atoms3552 = new BitSet(new long[] { 2L });
        FOLLOW_functioncall_in_atoms3568 = new BitSet(new long[] { 2L });
        FOLLOW_VARNAME_in_atoms3587 = new BitSet(new long[] { 2L });
        FOLLOW_numberlist_in_atoms3606 = new BitSet(new long[] { 2L });
        FOLLOW_TRUE_in_constants3632 = new BitSet(new long[] { 2L });
        FOLLOW_FALSE_in_constants3640 = new BitSet(new long[] { 2L });
        FOLLOW_INTEGER_in_constants3652 = new BitSet(new long[] { 2L });
        FOLLOW_FLOAT_in_constants3662 = new BitSet(new long[] { 2L });
        FOLLOW_STRING_in_constants3671 = new BitSet(new long[] { 2L });
        FOLLOW_AG_in_constants3677 = new BitSet(new long[] { 0L, 0L, 1099511627776L });
        FOLLOW_INTEGER_in_constants3683 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 17592186044416L });
        FOLLOW_VIRGULE_in_constants3685 = new BitSet(new long[] { 0L, 0L, 1099511627776L });
        FOLLOW_INTEGER_in_constants3691 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 17592186044416L });
        FOLLOW_VIRGULE_in_constants3693 = new BitSet(new long[] { 0L, 0L, 1099511627776L });
        FOLLOW_INTEGER_in_constants3697 = new BitSet(new long[] { 16L });
        FOLLOW_AD_in_constants3699 = new BitSet(new long[] { 2L });
    }
}
