package com.ankamagames.wakfu.client.console.command.admin.commands.antlr;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.wakfu.client.console.command.admin.commands.*;
import org.antlr.runtime.*;

public class ModerationCommandParser extends Parser
{
    public static final String[] tokenNames;
    public static final int EOF = -1;
    public static final int T__173 = 173;
    public static final int T__174 = 174;
    public static final int T__175 = 175;
    public static final int T__176 = 176;
    public static final int T__177 = 177;
    public static final int T__178 = 178;
    public static final int T__179 = 179;
    public static final int T__180 = 180;
    public static final int T__181 = 181;
    public static final int T__182 = 182;
    public static final int T__183 = 183;
    public static final int T__184 = 184;
    public static final int T__185 = 185;
    public static final int T__186 = 186;
    public static final int T__187 = 187;
    public static final int T__188 = 188;
    public static final int T__189 = 189;
    public static final int T__190 = 190;
    public static final int T__191 = 191;
    public static final int T__192 = 192;
    public static final int T__193 = 193;
    public static final int T__194 = 194;
    public static final int T__195 = 195;
    public static final int T__196 = 196;
    public static final int T__197 = 197;
    public static final int T__198 = 198;
    public static final int T__199 = 199;
    public static final int T__200 = 200;
    public static final int T__201 = 201;
    public static final int T__202 = 202;
    public static final int T__203 = 203;
    public static final int T__204 = 204;
    public static final int T__205 = 205;
    public static final int T__206 = 206;
    public static final int T__207 = 207;
    public static final int T__208 = 208;
    public static final int T__209 = 209;
    public static final int T__210 = 210;
    public static final int T__211 = 211;
    public static final int T__212 = 212;
    public static final int T__213 = 213;
    public static final int T__214 = 214;
    public static final int T__215 = 215;
    public static final int T__216 = 216;
    public static final int T__217 = 217;
    public static final int T__218 = 218;
    public static final int T__219 = 219;
    public static final int T__220 = 220;
    public static final int T__221 = 221;
    public static final int T__222 = 222;
    public static final int T__223 = 223;
    public static final int T__224 = 224;
    public static final int T__225 = 225;
    public static final int T__226 = 226;
    public static final int T__227 = 227;
    public static final int T__228 = 228;
    public static final int T__229 = 229;
    public static final int T__230 = 230;
    public static final int T__231 = 231;
    public static final int T__232 = 232;
    public static final int T__233 = 233;
    public static final int T__234 = 234;
    public static final int T__235 = 235;
    public static final int T__236 = 236;
    public static final int T__237 = 237;
    public static final int T__238 = 238;
    public static final int T__239 = 239;
    public static final int T__240 = 240;
    public static final int T__241 = 241;
    public static final int T__242 = 242;
    public static final int T__243 = 243;
    public static final int T__244 = 244;
    public static final int T__245 = 245;
    public static final int T__246 = 246;
    public static final int T__247 = 247;
    public static final int T__248 = 248;
    public static final int T__249 = 249;
    public static final int T__250 = 250;
    public static final int T__251 = 251;
    public static final int T__252 = 252;
    public static final int T__253 = 253;
    public static final int T__254 = 254;
    public static final int T__255 = 255;
    public static final int T__256 = 256;
    public static final int T__257 = 257;
    public static final int T__258 = 258;
    public static final int T__259 = 259;
    public static final int T__260 = 260;
    public static final int T__261 = 261;
    public static final int T__262 = 262;
    public static final int T__263 = 263;
    public static final int T__264 = 264;
    public static final int T__265 = 265;
    public static final int T__266 = 266;
    public static final int T__267 = 267;
    public static final int T__268 = 268;
    public static final int T__269 = 269;
    public static final int T__270 = 270;
    public static final int T__271 = 271;
    public static final int T__272 = 272;
    public static final int T__273 = 273;
    public static final int T__274 = 274;
    public static final int T__275 = 275;
    public static final int T__276 = 276;
    public static final int T__277 = 277;
    public static final int T__278 = 278;
    public static final int T__279 = 279;
    public static final int T__280 = 280;
    public static final int T__281 = 281;
    public static final int T__282 = 282;
    public static final int T__283 = 283;
    public static final int T__284 = 284;
    public static final int T__285 = 285;
    public static final int T__286 = 286;
    public static final int T__287 = 287;
    public static final int T__288 = 288;
    public static final int T__289 = 289;
    public static final int T__290 = 290;
    public static final int T__291 = 291;
    public static final int T__292 = 292;
    public static final int T__293 = 293;
    public static final int T__294 = 294;
    public static final int T__295 = 295;
    public static final int T__296 = 296;
    public static final int T__297 = 297;
    public static final int T__298 = 298;
    public static final int T__299 = 299;
    public static final int T__300 = 300;
    public static final int T__301 = 301;
    public static final int T__302 = 302;
    public static final int T__303 = 303;
    public static final int T__304 = 304;
    public static final int T__305 = 305;
    public static final int T__306 = 306;
    public static final int T__307 = 307;
    public static final int T__308 = 308;
    public static final int T__309 = 309;
    public static final int T__310 = 310;
    public static final int T__311 = 311;
    public static final int T__312 = 312;
    public static final int T__313 = 313;
    public static final int T__314 = 314;
    public static final int T__315 = 315;
    public static final int T__316 = 316;
    public static final int T__317 = 317;
    public static final int T__318 = 318;
    public static final int T__319 = 319;
    public static final int T__320 = 320;
    public static final int T__321 = 321;
    public static final int T__322 = 322;
    public static final int T__323 = 323;
    public static final int T__324 = 324;
    public static final int T__325 = 325;
    public static final int T__326 = 326;
    public static final int T__327 = 327;
    public static final int T__328 = 328;
    public static final int T__329 = 329;
    public static final int T__330 = 330;
    public static final int T__331 = 331;
    public static final int T__332 = 332;
    public static final int T__333 = 333;
    public static final int T__334 = 334;
    public static final int T__335 = 335;
    public static final int T__336 = 336;
    public static final int T__337 = 337;
    public static final int T__338 = 338;
    public static final int T__339 = 339;
    public static final int T__340 = 340;
    public static final int T__341 = 341;
    public static final int T__342 = 342;
    public static final int T__343 = 343;
    public static final int T__344 = 344;
    public static final int T__345 = 345;
    public static final int T__346 = 346;
    public static final int T__347 = 347;
    public static final int T__348 = 348;
    public static final int T__349 = 349;
    public static final int T__350 = 350;
    public static final int T__351 = 351;
    public static final int T__352 = 352;
    public static final int T__353 = 353;
    public static final int T__354 = 354;
    public static final int T__355 = 355;
    public static final int T__356 = 356;
    public static final int T__357 = 357;
    public static final int T__358 = 358;
    public static final int T__359 = 359;
    public static final int T__360 = 360;
    public static final int T__361 = 361;
    public static final int T__362 = 362;
    public static final int T__363 = 363;
    public static final int T__364 = 364;
    public static final int T__365 = 365;
    public static final int T__366 = 366;
    public static final int T__367 = 367;
    public static final int T__368 = 368;
    public static final int T__369 = 369;
    public static final int T__370 = 370;
    public static final int T__371 = 371;
    public static final int T__372 = 372;
    public static final int T__373 = 373;
    public static final int T__374 = 374;
    public static final int T__375 = 375;
    public static final int T__376 = 376;
    public static final int T__377 = 377;
    public static final int T__378 = 378;
    public static final int T__379 = 379;
    public static final int T__380 = 380;
    public static final int T__381 = 381;
    public static final int T__382 = 382;
    public static final int T__383 = 383;
    public static final int T__384 = 384;
    public static final int T__385 = 385;
    public static final int T__386 = 386;
    public static final int T__387 = 387;
    public static final int T__388 = 388;
    public static final int T__389 = 389;
    public static final int T__390 = 390;
    public static final int T__391 = 391;
    public static final int T__392 = 392;
    public static final int T__393 = 393;
    public static final int T__394 = 394;
    public static final int T__395 = 395;
    public static final int T__396 = 396;
    public static final int T__397 = 397;
    public static final int T__398 = 398;
    public static final int T__399 = 399;
    public static final int T__400 = 400;
    public static final int T__401 = 401;
    public static final int T__402 = 402;
    public static final int T__403 = 403;
    public static final int T__404 = 404;
    public static final int T__405 = 405;
    public static final int T__406 = 406;
    public static final int T__407 = 407;
    public static final int T__408 = 408;
    public static final int T__409 = 409;
    public static final int T__410 = 410;
    public static final int T__411 = 411;
    public static final int T__412 = 412;
    public static final int T__413 = 413;
    public static final int T__414 = 414;
    public static final int T__415 = 415;
    public static final int ACCOUNT_PATTERN = 4;
    public static final int ACHIEVEMENT = 5;
    public static final int ADD = 6;
    public static final int ADD_ITEM_XP = 7;
    public static final int ADD_MONEY = 8;
    public static final int ADD_SKILLXP = 9;
    public static final int ADD_SPELLXP = 10;
    public static final int ADD_TO_GROUP = 11;
    public static final int ADD_XP = 12;
    public static final int AI = 13;
    public static final int ALIGNMENT = 14;
    public static final int ALMANACH = 15;
    public static final int APTITUDE = 16;
    public static final int BAN = 17;
    public static final int BAN_REQUEST = 18;
    public static final int BEGIN = 19;
    public static final int BOOLEAN = 20;
    public static final int BOT = 21;
    public static final int BUFF = 22;
    public static final int CALENDAR_CMD = 23;
    public static final int CANCEL_COLLECT_COOLDOWN = 24;
    public static final int CHAOS = 25;
    public static final int CHARACTER_CMD = 26;
    public static final int CHARNAME_PATTERN = 27;
    public static final int CHARNAME_USABLECHAR = 28;
    public static final int CHECK_CMD = 29;
    public static final int CITIZEN_POINTS = 30;
    public static final int CLIENT_GAME_EVENT_CMD = 31;
    public static final int COMPANION = 32;
    public static final int COMPLETE = 33;
    public static final int COMPLETE_OBJECTIVE = 34;
    public static final int COORDS_SEPARATOR = 35;
    public static final int CRAFT_CMD = 36;
    public static final int CREATE_FULL_GROUP = 37;
    public static final int CREATE_GROUP = 38;
    public static final int CREATE_ITEM = 39;
    public static final int CREATE_SET = 40;
    public static final int DATE = 41;
    public static final int DELETE_ITEM = 42;
    public static final int DESTROY_INSTANCE = 43;
    public static final int DESTROY_MONSTERS = 44;
    public static final int DESTROY_RESOURCES = 45;
    public static final int DISTRIBUTE_ITEMS = 46;
    public static final int DUMP = 47;
    public static final int DUMP_BAG = 48;
    public static final int EMOTE_TARGETABLE = 49;
    public static final int EMPTY_CHAR = 50;
    public static final int ENABLE = 51;
    public static final int END = 52;
    public static final int ENDLINE = 53;
    public static final int ENDSCENARIO = 54;
    public static final int ESCAPE = 55;
    public static final int ESCAPED_STRING = 56;
    public static final int FIGHT_CHALLENGE = 57;
    public static final int FIGHT_CMD = 58;
    public static final int FINISHCHALLENGE = 59;
    public static final int FLOAT = 60;
    public static final int FREEDOM = 61;
    public static final int FREE_ACCESS = 62;
    public static final int GEM_CMD = 63;
    public static final int GET = 64;
    public static final int GET_INSTANCE_UID = 65;
    public static final int GHOSTCHECK = 66;
    public static final int GIVE_ITEM = 67;
    public static final int GOD_MODE = 68;
    public static final int GUILD = 69;
    public static final int HAVEN_BAG_KICK = 70;
    public static final int HAVEN_WORLD = 71;
    public static final int HELP = 72;
    public static final int HERO = 73;
    public static final int ICE_STATUS = 74;
    public static final int IDENT_PHASE = 75;
    public static final int INFO = 76;
    public static final int INSTANCE_USAGE = 77;
    public static final int INVENTORY = 78;
    public static final int KICK = 79;
    public static final int LEARN_EMOTE = 80;
    public static final int LIST_LOOT = 81;
    public static final int MONSTER_GROUP = 82;
    public static final int MSGALL = 83;
    public static final int MUTE = 84;
    public static final int MUTE_PARTITIONS = 85;
    public static final int NATION = 86;
    public static final int NUMBER = 87;
    public static final int NUMERAL = 88;
    public static final int OFF = 89;
    public static final int ON = 90;
    public static final int PANEL = 91;
    public static final int PAUSE = 92;
    public static final int PET = 93;
    public static final int PING = 94;
    public static final int PLANT_RESOURCES = 95;
    public static final int PLAY_ANIMATION = 96;
    public static final int PLAY_APS = 97;
    public static final int POPUP_MESSAGE = 98;
    public static final int PROTECTOR_CMD = 99;
    public static final int PROXIMITY_PATTERN = 100;
    public static final int PVP = 101;
    public static final int QUOTA = 102;
    public static final int RAGNAROK = 103;
    public static final int RECOMPUTE_POINTS = 104;
    public static final int RED_MESSAGE = 105;
    public static final int RED_MESSAGE_TO_PLAYER = 106;
    public static final int REGENERATE = 107;
    public static final int REGENERATE_WITH_ITEM = 108;
    public static final int RELOADSCENARIOS = 109;
    public static final int REMOVE = 110;
    public static final int REMOVE_FLOOR_ITEMS = 111;
    public static final int RENT_ITEM_CMD = 112;
    public static final int RESET = 113;
    public static final int RESET_ACCOUNT_MARKET_ENTRIES = 114;
    public static final int RESET_REGRESSION = 115;
    public static final int RESTART_CHAOS = 116;
    public static final int RESTORE_CHARACTER_CMD = 117;
    public static final int RESUME = 118;
    public static final int REVIVE = 119;
    public static final int RIGHTS = 120;
    public static final int RUNACTION = 121;
    public static final int SCENARIO_COMMAND = 122;
    public static final int SEARCH = 123;
    public static final int SERVERLOCK = 124;
    public static final int SESSIONS = 125;
    public static final int SET = 126;
    public static final int SETNEXTCHALLENGE = 127;
    public static final int SET_BONUS_FACTOR = 128;
    public static final int SET_ITEM_TRACKER_LOG_LEVEL_CMD = 129;
    public static final int SET_LEVEL = 130;
    public static final int SET_PLAYER_TITLE = 131;
    public static final int SET_RANK = 132;
    public static final int SET_RESOURCE_SPEED_FACTOR = 133;
    public static final int SET_RESPAWN_CMD = 134;
    public static final int SET_SKILL_LEVEL = 135;
    public static final int SET_SPELLLEVEL = 136;
    public static final int SET_WAKFU_GAUGE = 137;
    public static final int SHOW_AGGRO_LIST = 138;
    public static final int SHOW_MONSTER_QUOTA = 139;
    public static final int SHOW_POPULATION = 140;
    public static final int SHUTDOWN = 141;
    public static final int SPAWN_INTERACTIVE_ELEMENT = 142;
    public static final int SPELL_CMD = 143;
    public static final int STAFF = 144;
    public static final int START = 145;
    public static final int START_DATE = 146;
    public static final int STATE_CMD = 147;
    public static final int STATS = 148;
    public static final int STATUS = 149;
    public static final int STOP = 150;
    public static final int SUBSCRIBER = 151;
    public static final int SYMBIOT = 152;
    public static final int SYSMSG = 153;
    public static final int SYSTEM_CONFIGURATION = 154;
    public static final int TELEPORT = 155;
    public static final int TELEPORT_TO_MONSTER = 156;
    public static final int TELEPORT_USER = 157;
    public static final int TEMP = 158;
    public static final int TIME = 159;
    public static final int TP_TO_JAIL = 160;
    public static final int TURN_DURATION = 161;
    public static final int UNBAN = 162;
    public static final int UNMUTE = 163;
    public static final int UNMUTE_PARTITIONS = 164;
    public static final int VAR = 165;
    public static final int VERSION = 166;
    public static final int VOTE = 167;
    public static final int WEB_BROWSER = 168;
    public static final int WHERE = 169;
    public static final int WHITESPACE = 170;
    public static final int WHO = 171;
    public static final int ZONE_BUFF = 172;
    protected static final Logger m_logger;
    protected DFA92 dfa92;
    static final String DFA92_eotS = "\u0091\uffff";
    static final String DFA92_eofS = "\u0091\uffff";
    static final String DFA92_minS = "\u0001\u0005\u0007\uffff\u0002\u0004\n\uffff\u0001!m\uffff\u0001#\u00035\u0001\uffff\u0001W\u00015\u0001\uffff\u0001#\u0002\uffff\u0001W\u00015\u0002\uffff";
    static final String DFA92_maxS = "\u0001\u0193\u0007\uffff\u0001W\u00018\n\uffff\u0001¥m\uffff\u0004W\u0001\uffff\u0002W\u0001\uffff\u0001W\u0002\uffff\u0002W\u0002\uffff";
    static final String DFA92_acceptS = "\u0001\uffff\u0001\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0001\u0005\u0001\u0006\u0001\u0007\u0002\uffff\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\uffff\u0001\u0018\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0001\"\u0001#\u0001$\u0001%\u0001&\u0001'\u0001(\u0001)\u0001*\u0001+\u0001,\u0001-\u0001.\u0001/\u00010\u00011\u00012\u00013\u00014\u00015\u00016\u00017\u00018\u00019\u0001:\u0001;\u0001<\u0001=\u0001>\u0001?\u0001@\u0001A\u0001B\u0001C\u0001D\u0001E\u0001F\u0001G\u0001H\u0001I\u0001J\u0001K\u0001L\u0001M\u0001N\u0001O\u0001P\u0001Q\u0001R\u0001S\u0001T\u0001U\u0001V\u0001W\u0001X\u0001Y\u0001Z\u0001[\u0001\\\u0001]\u0001^\u0001_\u0001`\u0001a\u0001b\u0001c\u0001d\u0001e\u0001f\u0001g\u0001h\u0001i\u0001j\u0001k\u0001l\u0001m\u0001n\u0001o\u0001p\u0001q\u0001r\u0001s\u0001t\u0001u\u0001v\u0001w\u0001x\u0001y\u0001z\u0001{\u0001|\u0001}\u0001~\u0001\u007f\u0001\u0080\u0001\u0081\u0001\u0082\u0001\u0083\u0001\u0084\u0001\b\u0004\uffff\u0001\u0019\u0002\uffff\u0001\u000b\u0001\uffff\u0001\t\u0001\n\u0002\uffff\u0001\f\u0001\r";
    static final String DFA92_specialS = "\u0091\uffff}>";
    static final String[] DFA92_transitionS;
    static final short[] DFA92_eot;
    static final short[] DFA92_eof;
    static final char[] DFA92_min;
    static final char[] DFA92_max;
    static final short[] DFA92_accept;
    static final short[] DFA92_special;
    static final short[][] DFA92_transition;
    public static final BitSet FOLLOW_NUMBER_in_coords1984;
    public static final BitSet FOLLOW_COORDS_SEPARATOR_in_coords1986;
    public static final BitSet FOLLOW_NUMBER_in_coords1991;
    public static final BitSet FOLLOW_CHARNAME_PATTERN_in_character_pattern2008;
    public static final BitSet FOLLOW_ESCAPED_STRING_in_character_pattern2017;
    public static final BitSet FOLLOW_ACCOUNT_PATTERN_in_character_pattern2026;
    public static final BitSet FOLLOW_ESCAPED_STRING_in_message2046;
    public static final BitSet FOLLOW_PROXIMITY_PATTERN_in_proximity_pattern2065;
    public static final BitSet FOLLOW_NUMBER_in_id_list_pattern2087;
    public static final BitSet FOLLOW_BOT_in_bot_cmd2108;
    public static final BitSet FOLLOW_PING_in_bot_cmd2110;
    public static final BitSet FOLLOW_NUMBER_in_bot_cmd2114;
    public static final BitSet FOLLOW_ENDLINE_in_bot_cmd2116;
    public static final BitSet FOLLOW_STATS_in_stats_cmd2131;
    public static final BitSet FOLLOW_ENDLINE_in_stats_cmd2133;
    public static final BitSet FOLLOW_PANEL_in_panel_cmd2148;
    public static final BitSet FOLLOW_ENDLINE_in_panel_cmd2150;
    public static final BitSet FOLLOW_PING_in_ping_cmd2165;
    public static final BitSet FOLLOW_ENDLINE_in_ping_cmd2167;
    public static final BitSet FOLLOW_PING_in_ping_cmd2174;
    public static final BitSet FOLLOW_START_in_ping_cmd2176;
    public static final BitSet FOLLOW_ENDLINE_in_ping_cmd2178;
    public static final BitSet FOLLOW_PING_in_ping_cmd2185;
    public static final BitSet FOLLOW_STOP_in_ping_cmd2187;
    public static final BitSet FOLLOW_ENDLINE_in_ping_cmd2189;
    public static final BitSet FOLLOW_TIME_in_time_cmd2210;
    public static final BitSet FOLLOW_ENDLINE_in_time_cmd2212;
    public static final BitSet FOLLOW_WHO_in_who_cmd2233;
    public static final BitSet FOLLOW_character_pattern_in_who_cmd2237;
    public static final BitSet FOLLOW_ENDLINE_in_who_cmd2239;
    public static final BitSet FOLLOW_WHERE_in_where_cmd2254;
    public static final BitSet FOLLOW_character_pattern_in_where_cmd2258;
    public static final BitSet FOLLOW_ENDLINE_in_where_cmd2260;
    public static final BitSet FOLLOW_TELEPORT_in_teleport_to_player_cmd2275;
    public static final BitSet FOLLOW_character_pattern_in_teleport_to_player_cmd2279;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_to_player_cmd2281;
    public static final BitSet FOLLOW_TELEPORT_USER_in_teleport_player_to_me_cmd2296;
    public static final BitSet FOLLOW_character_pattern_in_teleport_player_to_me_cmd2300;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_player_to_me_cmd2302;
    public static final BitSet FOLLOW_TELEPORT_USER_in_teleport_player_to_coords_cmd2317;
    public static final BitSet FOLLOW_character_pattern_in_teleport_player_to_coords_cmd2321;
    public static final BitSet FOLLOW_coords_in_teleport_player_to_coords_cmd2325;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_player_to_coords_cmd2327;
    public static final BitSet FOLLOW_TELEPORT_USER_in_teleport_player_to_instance_cmd2342;
    public static final BitSet FOLLOW_character_pattern_in_teleport_player_to_instance_cmd2346;
    public static final BitSet FOLLOW_coords_in_teleport_player_to_instance_cmd2350;
    public static final BitSet FOLLOW_NUMBER_in_teleport_player_to_instance_cmd2354;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_player_to_instance_cmd2356;
    public static final BitSet FOLLOW_TELEPORT_in_teleport_to_coords_cmd2372;
    public static final BitSet FOLLOW_coords_in_teleport_to_coords_cmd2376;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_to_coords_cmd2378;
    public static final BitSet FOLLOW_TELEPORT_in_teleport_to_inst_cmd2393;
    public static final BitSet FOLLOW_coords_in_teleport_to_inst_cmd2397;
    public static final BitSet FOLLOW_NUMBER_in_teleport_to_inst_cmd2401;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_to_inst_cmd2403;
    public static final BitSet FOLLOW_SERVERLOCK_in_serverlock_cmd2418;
    public static final BitSet FOLLOW_ON_in_serverlock_cmd2421;
    public static final BitSet FOLLOW_OFF_in_serverlock_cmd2427;
    public static final BitSet FOLLOW_ENDLINE_in_serverlock_cmd2439;
    public static final BitSet FOLLOW_KICK_in_kick_cmd2452;
    public static final BitSet FOLLOW_character_pattern_in_kick_cmd2456;
    public static final BitSet FOLLOW_ENDLINE_in_kick_cmd2458;
    public static final BitSet FOLLOW_KICK_in_kick_cmd2465;
    public static final BitSet FOLLOW_character_pattern_in_kick_cmd2469;
    public static final BitSet FOLLOW_message_in_kick_cmd2473;
    public static final BitSet FOLLOW_ENDLINE_in_kick_cmd2475;
    public static final BitSet FOLLOW_BAN_in_ban_cmd2492;
    public static final BitSet FOLLOW_NUMBER_in_ban_cmd2496;
    public static final BitSet FOLLOW_NUMBER_in_ban_cmd2500;
    public static final BitSet FOLLOW_ENDLINE_in_ban_cmd2502;
    public static final BitSet FOLLOW_BAN_in_ban_cmd2509;
    public static final BitSet FOLLOW_NUMBER_in_ban_cmd2513;
    public static final BitSet FOLLOW_ENDLINE_in_ban_cmd2515;
    public static final BitSet FOLLOW_UNBAN_in_ban_cmd2522;
    public static final BitSet FOLLOW_NUMBER_in_ban_cmd2526;
    public static final BitSet FOLLOW_ENDLINE_in_ban_cmd2528;
    public static final BitSet FOLLOW_BAN_REQUEST_in_ban_request_cmd2547;
    public static final BitSet FOLLOW_NUMBER_in_ban_request_cmd2551;
    public static final BitSet FOLLOW_NUMBER_in_ban_request_cmd2555;
    public static final BitSet FOLLOW_message_in_ban_request_cmd2559;
    public static final BitSet FOLLOW_ENDLINE_in_ban_request_cmd2561;
    public static final BitSet FOLLOW_GHOSTCHECK_in_ghostcheck_cmd2577;
    public static final BitSet FOLLOW_ENDLINE_in_ghostcheck_cmd2579;
    public static final BitSet FOLLOW_IDENT_PHASE_in_identphase_cmd2594;
    public static final BitSet FOLLOW_NUMBER_in_identphase_cmd2598;
    public static final BitSet FOLLOW_ENDLINE_in_identphase_cmd2600;
    public static final BitSet FOLLOW_SHUTDOWN_in_shutdown_cmd2615;
    public static final BitSet FOLLOW_NUMBER_in_shutdown_cmd2619;
    public static final BitSet FOLLOW_ENDLINE_in_shutdown_cmd2621;
    public static final BitSet FOLLOW_SHUTDOWN_in_shutdown_cmd2629;
    public static final BitSet FOLLOW_ENDLINE_in_shutdown_cmd2631;
    public static final BitSet FOLLOW_SHUTDOWN_in_shutdown_cmd2639;
    public static final BitSet FOLLOW_STOP_in_shutdown_cmd2641;
    public static final BitSet FOLLOW_ENDLINE_in_shutdown_cmd2643;
    public static final BitSet FOLLOW_SYSMSG_in_sysmsg_cmd2663;
    public static final BitSet FOLLOW_message_in_sysmsg_cmd2667;
    public static final BitSet FOLLOW_ENDLINE_in_sysmsg_cmd2669;
    public static final BitSet FOLLOW_SYSMSG_in_sysmsg_cmd2676;
    public static final BitSet FOLLOW_character_pattern_in_sysmsg_cmd2680;
    public static final BitSet FOLLOW_message_in_sysmsg_cmd2684;
    public static final BitSet FOLLOW_SYSMSG_in_sysmsg_cmd2691;
    public static final BitSet FOLLOW_proximity_pattern_in_sysmsg_cmd2695;
    public static final BitSet FOLLOW_message_in_sysmsg_cmd2699;
    public static final BitSet FOLLOW_ENDLINE_in_sysmsg_cmd2701;
    public static final BitSet FOLLOW_MSGALL_in_msgall_cmd2719;
    public static final BitSet FOLLOW_message_in_msgall_cmd2723;
    public static final BitSet FOLLOW_ENDLINE_in_msgall_cmd2725;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2741;
    public static final BitSet FOLLOW_180_in_symbiot_cmd2743;
    public static final BitSet FOLLOW_NUMBER_in_symbiot_cmd2747;
    public static final BitSet FOLLOW_ENDLINE_in_symbiot_cmd2749;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2757;
    public static final BitSet FOLLOW_180_in_symbiot_cmd2759;
    public static final BitSet FOLLOW_NUMBER_in_symbiot_cmd2763;
    public static final BitSet FOLLOW_NUMBER_in_symbiot_cmd2767;
    public static final BitSet FOLLOW_ENDLINE_in_symbiot_cmd2769;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2777;
    public static final BitSet FOLLOW_233_in_symbiot_cmd2779;
    public static final BitSet FOLLOW_NUMBER_in_symbiot_cmd2783;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2789;
    public static final BitSet FOLLOW_228_in_symbiot_cmd2791;
    public static final BitSet FOLLOW_NUMBER_in_symbiot_cmd2795;
    public static final BitSet FOLLOW_message_in_symbiot_cmd2799;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2806;
    public static final BitSet FOLLOW_212_in_symbiot_cmd2808;
    public static final BitSet FOLLOW_NUMBER_in_symbiot_cmd2812;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2818;
    public static final BitSet FOLLOW_210_in_symbiot_cmd2820;
    public static final BitSet FOLLOW_SYMBIOT_in_symbiot_cmd2827;
    public static final BitSet FOLLOW_set_in_symbiot_cmd2829;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2851;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2853;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2860;
    public static final BitSet FOLLOW_SET_in_nation_cmd2862;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd2866;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2868;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2875;
    public static final BitSet FOLLOW_VOTE_in_nation_cmd2877;
    public static final BitSet FOLLOW_INFO_in_nation_cmd2879;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2881;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2888;
    public static final BitSet FOLLOW_VOTE_in_nation_cmd2890;
    public static final BitSet FOLLOW_INFO_in_nation_cmd2892;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd2896;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2898;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2905;
    public static final BitSet FOLLOW_VOTE_in_nation_cmd2907;
    public static final BitSet FOLLOW_START_in_nation_cmd2909;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd2913;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2915;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2922;
    public static final BitSet FOLLOW_VOTE_in_nation_cmd2924;
    public static final BitSet FOLLOW_START_in_nation_cmd2926;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2928;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2935;
    public static final BitSet FOLLOW_VOTE_in_nation_cmd2937;
    public static final BitSet FOLLOW_END_in_nation_cmd2939;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd2943;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2945;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2952;
    public static final BitSet FOLLOW_VOTE_in_nation_cmd2954;
    public static final BitSet FOLLOW_END_in_nation_cmd2956;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2958;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2965;
    public static final BitSet FOLLOW_CITIZEN_POINTS_in_nation_cmd2967;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd2971;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2973;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2980;
    public static final BitSet FOLLOW_SET_RANK_in_nation_cmd2982;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd2986;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd2988;
    public static final BitSet FOLLOW_NATION_in_nation_cmd2995;
    public static final BitSet FOLLOW_ALIGNMENT_in_nation_cmd2997;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd3001;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd3005;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3007;
    public static final BitSet FOLLOW_NATION_in_nation_cmd3013;
    public static final BitSet FOLLOW_set_in_nation_cmd3015;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd3025;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3027;
    public static final BitSet FOLLOW_NATION_in_nation_cmd3034;
    public static final BitSet FOLLOW_set_in_nation_cmd3036;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd3047;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3049;
    public static final BitSet FOLLOW_NATION_in_nation_cmd3057;
    public static final BitSet FOLLOW_set_in_nation_cmd3059;
    public static final BitSet FOLLOW_NUMBER_in_nation_cmd3070;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3072;
    public static final BitSet FOLLOW_NATION_in_nation_cmd3080;
    public static final BitSet FOLLOW_set_in_nation_cmd3082;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3090;
    public static final BitSet FOLLOW_NATION_in_nation_cmd3097;
    public static final BitSet FOLLOW_set_in_nation_cmd3099;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3107;
    public static final BitSet FOLLOW_NATION_in_nation_cmd3114;
    public static final BitSet FOLLOW_390_in_nation_cmd3117;
    public static final BitSet FOLLOW_ENDLINE_in_nation_cmd3120;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_cmd3139;
    public static final BitSet FOLLOW_RESET_in_achievement_cmd3141;
    public static final BitSet FOLLOW_283_in_achievement_cmd3146;
    public static final BitSet FOLLOW_NUMBER_in_achievement_cmd3151;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_cmd3153;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_cmd3161;
    public static final BitSet FOLLOW_RESET_in_achievement_cmd3163;
    public static final BitSet FOLLOW_280_in_achievement_cmd3165;
    public static final BitSet FOLLOW_284_in_achievement_cmd3170;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_cmd3173;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_cmd3179;
    public static final BitSet FOLLOW_COMPLETE_in_achievement_cmd3181;
    public static final BitSet FOLLOW_NUMBER_in_achievement_cmd3185;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_cmd3187;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_cmd3193;
    public static final BitSet FOLLOW_COMPLETE_OBJECTIVE_in_achievement_cmd3195;
    public static final BitSet FOLLOW_NUMBER_in_achievement_cmd3199;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_cmd3201;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_cmd3207;
    public static final BitSet FOLLOW_VAR_in_achievement_cmd3209;
    public static final BitSet FOLLOW_GET_in_achievement_cmd3211;
    public static final BitSet FOLLOW_message_in_achievement_cmd3215;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_cmd3217;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_cmd3223;
    public static final BitSet FOLLOW_VAR_in_achievement_cmd3225;
    public static final BitSet FOLLOW_SET_in_achievement_cmd3227;
    public static final BitSet FOLLOW_message_in_achievement_cmd3231;
    public static final BitSet FOLLOW_NUMBER_in_achievement_cmd3235;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_cmd3237;
    public static final BitSet FOLLOW_ACHIEVEMENT_in_achievement_date_cmd3256;
    public static final BitSet FOLLOW_START_DATE_in_achievement_date_cmd3258;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3262;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3266;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3270;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3274;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3278;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3282;
    public static final BitSet FOLLOW_NUMBER_in_achievement_date_cmd3286;
    public static final BitSet FOLLOW_ENDLINE_in_achievement_date_cmd3288;
    public static final BitSet FOLLOW_ZONE_BUFF_in_zone_buff_cmd3306;
    public static final BitSet FOLLOW_ENDLINE_in_zone_buff_cmd3308;
    public static final BitSet FOLLOW_ZONE_BUFF_in_zone_buff_cmd3315;
    public static final BitSet FOLLOW_ADD_in_zone_buff_cmd3317;
    public static final BitSet FOLLOW_NUMBER_in_zone_buff_cmd3321;
    public static final BitSet FOLLOW_ENDLINE_in_zone_buff_cmd3323;
    public static final BitSet FOLLOW_ZONE_BUFF_in_zone_buff_cmd3330;
    public static final BitSet FOLLOW_REMOVE_in_zone_buff_cmd3332;
    public static final BitSet FOLLOW_NUMBER_in_zone_buff_cmd3336;
    public static final BitSet FOLLOW_ENDLINE_in_zone_buff_cmd3338;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3357;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3361;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3363;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3370;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3374;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3378;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3380;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3387;
    public static final BitSet FOLLOW_255_in_create_group_cmd3389;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3393;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3395;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3402;
    public static final BitSet FOLLOW_255_in_create_group_cmd3404;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3408;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3412;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3414;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3422;
    public static final BitSet FOLLOW_262_in_create_group_cmd3424;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3428;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3430;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3437;
    public static final BitSet FOLLOW_262_in_create_group_cmd3439;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3443;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3447;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3449;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3458;
    public static final BitSet FOLLOW_257_in_create_group_cmd3460;
    public static final BitSet FOLLOW_NUMBER_in_create_group_cmd3464;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3466;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3473;
    public static final BitSet FOLLOW_257_in_create_group_cmd3475;
    public static final BitSet FOLLOW_296_in_create_group_cmd3477;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3479;
    public static final BitSet FOLLOW_CREATE_GROUP_in_create_group_cmd3486;
    public static final BitSet FOLLOW_set_in_create_group_cmd3488;
    public static final BitSet FOLLOW_ENDLINE_in_create_group_cmd3496;
    public static final BitSet FOLLOW_CREATE_FULL_GROUP_in_create_full_group_cmd3517;
    public static final BitSet FOLLOW_id_list_pattern_in_create_full_group_cmd3523;
    public static final BitSet FOLLOW_ENDLINE_in_create_full_group_cmd3525;
    public static final BitSet FOLLOW_DESTROY_MONSTERS_in_destroy_monsters_cmd3547;
    public static final BitSet FOLLOW_NUMBER_in_destroy_monsters_cmd3551;
    public static final BitSet FOLLOW_ENDLINE_in_destroy_monsters_cmd3553;
    public static final BitSet FOLLOW_DESTROY_MONSTERS_in_destroy_monsters_cmd3560;
    public static final BitSet FOLLOW_NUMBER_in_destroy_monsters_cmd3564;
    public static final BitSet FOLLOW_NUMBER_in_destroy_monsters_cmd3568;
    public static final BitSet FOLLOW_ENDLINE_in_destroy_monsters_cmd3570;
    public static final BitSet FOLLOW_PLANT_RESOURCES_in_plant_resources_cmd3590;
    public static final BitSet FOLLOW_NUMBER_in_plant_resources_cmd3594;
    public static final BitSet FOLLOW_ENDLINE_in_plant_resources_cmd3596;
    public static final BitSet FOLLOW_PLANT_RESOURCES_in_plant_resources_cmd3604;
    public static final BitSet FOLLOW_NUMBER_in_plant_resources_cmd3608;
    public static final BitSet FOLLOW_NUMBER_in_plant_resources_cmd3612;
    public static final BitSet FOLLOW_ENDLINE_in_plant_resources_cmd3614;
    public static final BitSet FOLLOW_DESTROY_RESOURCES_in_destroy_resources_cmd3633;
    public static final BitSet FOLLOW_NUMBER_in_destroy_resources_cmd3637;
    public static final BitSet FOLLOW_ENDLINE_in_destroy_resources_cmd3639;
    public static final BitSet FOLLOW_DESTROY_RESOURCES_in_destroy_resources_cmd3647;
    public static final BitSet FOLLOW_NUMBER_in_destroy_resources_cmd3651;
    public static final BitSet FOLLOW_NUMBER_in_destroy_resources_cmd3655;
    public static final BitSet FOLLOW_ENDLINE_in_destroy_resources_cmd3657;
    public static final BitSet FOLLOW_ADD_TO_GROUP_in_add_to_group_cmd3674;
    public static final BitSet FOLLOW_NUMBER_in_add_to_group_cmd3678;
    public static final BitSet FOLLOW_NUMBER_in_add_to_group_cmd3682;
    public static final BitSet FOLLOW_NUMBER_in_add_to_group_cmd3686;
    public static final BitSet FOLLOW_ENDLINE_in_add_to_group_cmd3688;
    public static final BitSet FOLLOW_PLAY_ANIMATION_in_play_animation_cmd3703;
    public static final BitSet FOLLOW_NUMBER_in_play_animation_cmd3707;
    public static final BitSet FOLLOW_message_in_play_animation_cmd3711;
    public static final BitSet FOLLOW_ENDLINE_in_play_animation_cmd3713;
    public static final BitSet FOLLOW_PLAY_APS_in_play_aps_cmd3728;
    public static final BitSet FOLLOW_NUMBER_in_play_aps_cmd3732;
    public static final BitSet FOLLOW_NUMBER_in_play_aps_cmd3736;
    public static final BitSet FOLLOW_NUMBER_in_play_aps_cmd3740;
    public static final BitSet FOLLOW_BOOLEAN_in_play_aps_cmd3744;
    public static final BitSet FOLLOW_ENDLINE_in_play_aps_cmd3746;
    public static final BitSet FOLLOW_PLAY_APS_in_play_aps_cmd3753;
    public static final BitSet FOLLOW_NUMBER_in_play_aps_cmd3757;
    public static final BitSet FOLLOW_NUMBER_in_play_aps_cmd3761;
    public static final BitSet FOLLOW_NUMBER_in_play_aps_cmd3765;
    public static final BitSet FOLLOW_ENDLINE_in_play_aps_cmd3767;
    public static final BitSet FOLLOW_RIGHTS_in_rights_cmd3784;
    public static final BitSet FOLLOW_ENDLINE_in_rights_cmd3786;
    public static final BitSet FOLLOW_CHAOS_in_chaos_cmd3801;
    public static final BitSet FOLLOW_START_in_chaos_cmd3803;
    public static final BitSet FOLLOW_NUMBER_in_chaos_cmd3807;
    public static final BitSet FOLLOW_ENDLINE_in_chaos_cmd3809;
    public static final BitSet FOLLOW_CHAOS_in_chaos_cmd3815;
    public static final BitSet FOLLOW_START_in_chaos_cmd3817;
    public static final BitSet FOLLOW_NUMBER_in_chaos_cmd3821;
    public static final BitSet FOLLOW_NUMBER_in_chaos_cmd3825;
    public static final BitSet FOLLOW_ENDLINE_in_chaos_cmd3827;
    public static final BitSet FOLLOW_CHAOS_in_chaos_cmd3833;
    public static final BitSet FOLLOW_STOP_in_chaos_cmd3835;
    public static final BitSet FOLLOW_NUMBER_in_chaos_cmd3839;
    public static final BitSet FOLLOW_ENDLINE_in_chaos_cmd3841;
    public static final BitSet FOLLOW_RESTART_CHAOS_in_restart_chaos_cmd3858;
    public static final BitSet FOLLOW_NUMBER_in_restart_chaos_cmd3862;
    public static final BitSet FOLLOW_ENDLINE_in_restart_chaos_cmd3865;
    public static final BitSet FOLLOW_CREATE_ITEM_in_create_item_cmd3881;
    public static final BitSet FOLLOW_NUMBER_in_create_item_cmd3885;
    public static final BitSet FOLLOW_NUMBER_in_create_item_cmd3889;
    public static final BitSet FOLLOW_ENDLINE_in_create_item_cmd3892;
    public static final BitSet FOLLOW_CREATE_SET_in_create_set_cmd3907;
    public static final BitSet FOLLOW_NUMBER_in_create_set_cmd3911;
    public static final BitSet FOLLOW_ENDLINE_in_create_set_cmd3913;
    public static final BitSet FOLLOW_DELETE_ITEM_in_delete_item_cmd3928;
    public static final BitSet FOLLOW_NUMBER_in_delete_item_cmd3932;
    public static final BitSet FOLLOW_NUMBER_in_delete_item_cmd3936;
    public static final BitSet FOLLOW_ENDLINE_in_delete_item_cmd3939;
    public static final BitSet FOLLOW_REGENERATE_in_regenerate_cmd3954;
    public static final BitSet FOLLOW_ENDLINE_in_regenerate_cmd3956;
    public static final BitSet FOLLOW_REGENERATE_WITH_ITEM_in_regenerate_with_item_cmd3971;
    public static final BitSet FOLLOW_NUMBER_in_regenerate_with_item_cmd3975;
    public static final BitSet FOLLOW_ENDLINE_in_regenerate_with_item_cmd3977;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd3993;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd3995;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd4002;
    public static final BitSet FOLLOW_set_in_god_mode_cmd4004;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd4012;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd4019;
    public static final BitSet FOLLOW_set_in_god_mode_cmd4021;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd4029;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd4036;
    public static final BitSet FOLLOW_set_in_god_mode_cmd4038;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd4046;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd4054;
    public static final BitSet FOLLOW_set_in_god_mode_cmd4056;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd4064;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd4072;
    public static final BitSet FOLLOW_set_in_god_mode_cmd4074;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd4082;
    public static final BitSet FOLLOW_GOD_MODE_in_god_mode_cmd4090;
    public static final BitSet FOLLOW_set_in_god_mode_cmd4092;
    public static final BitSet FOLLOW_ENDLINE_in_god_mode_cmd4100;
    public static final BitSet FOLLOW_BUFF_in_buff_character_cmd4118;
    public static final BitSet FOLLOW_BUFF_in_buff_character_cmd4124;
    public static final BitSet FOLLOW_NUMBER_in_buff_character_cmd4128;
    public static final BitSet FOLLOW_BUFF_in_buff_character_cmd4134;
    public static final BitSet FOLLOW_199_in_buff_character_cmd4136;
    public static final BitSet FOLLOW_NUMBER_in_buff_character_cmd4140;
    public static final BitSet FOLLOW_NUMBER_in_buff_character_cmd4144;
    public static final BitSet FOLLOW_ENDLINE_in_buff_character_cmd4146;
    public static final BitSet FOLLOW_BUFF_in_buff_character_cmd4152;
    public static final BitSet FOLLOW_238_in_buff_character_cmd4154;
    public static final BitSet FOLLOW_NUMBER_in_buff_character_cmd4158;
    public static final BitSet FOLLOW_NUMBER_in_buff_character_cmd4162;
    public static final BitSet FOLLOW_ENDLINE_in_buff_character_cmd4164;
    public static final BitSet FOLLOW_TURN_DURATION_in_turn_duration_cmd4182;
    public static final BitSet FOLLOW_NUMBER_in_turn_duration_cmd4186;
    public static final BitSet FOLLOW_ENDLINE_in_turn_duration_cmd4188;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4207;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4209;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4220;
    public static final BitSet FOLLOW_set_in_pvp_cmd4222;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4230;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4237;
    public static final BitSet FOLLOW_SET_in_pvp_cmd4239;
    public static final BitSet FOLLOW_NUMBER_in_pvp_cmd4243;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4245;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4252;
    public static final BitSet FOLLOW_ADD_in_pvp_cmd4254;
    public static final BitSet FOLLOW_NUMBER_in_pvp_cmd4258;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4260;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4267;
    public static final BitSet FOLLOW_set_in_pvp_cmd4269;
    public static final BitSet FOLLOW_NUMBER_in_pvp_cmd4277;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4279;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4286;
    public static final BitSet FOLLOW_ENABLE_in_pvp_cmd4288;
    public static final BitSet FOLLOW_set_in_pvp_cmd4292;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4298;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4305;
    public static final BitSet FOLLOW_RECOMPUTE_POINTS_in_pvp_cmd4307;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4309;
    public static final BitSet FOLLOW_PVP_in_pvp_cmd4316;
    public static final BitSet FOLLOW_RESET_REGRESSION_in_pvp_cmd4318;
    public static final BitSet FOLLOW_ENDLINE_in_pvp_cmd4320;
    public static final BitSet FOLLOW_RUNACTION_in_run_action_cmd4337;
    public static final BitSet FOLLOW_NUMBER_in_run_action_cmd4341;
    public static final BitSet FOLLOW_NUMBER_in_run_action_cmd4345;
    public static final BitSet FOLLOW_ENDLINE_in_run_action_cmd4347;
    public static final BitSet FOLLOW_ENDSCENARIO_in_end_scenario_cmd4361;
    public static final BitSet FOLLOW_NUMBER_in_end_scenario_cmd4365;
    public static final BitSet FOLLOW_ENDLINE_in_end_scenario_cmd4367;
    public static final BitSet FOLLOW_SCENARIO_COMMAND_in_scenario_cmd4382;
    public static final BitSet FOLLOW_255_in_scenario_cmd4384;
    public static final BitSet FOLLOW_NUMBER_in_scenario_cmd4388;
    public static final BitSet FOLLOW_ENDLINE_in_scenario_cmd4390;
    public static final BitSet FOLLOW_RELOADSCENARIOS_in_reload_scenarios_cmd4407;
    public static final BitSet FOLLOW_ENDLINE_in_reload_scenarios_cmd4409;
    public static final BitSet FOLLOW_ADD_SPELLXP_in_add_spellxp_cmd4423;
    public static final BitSet FOLLOW_NUMBER_in_add_spellxp_cmd4427;
    public static final BitSet FOLLOW_NUMBER_in_add_spellxp_cmd4431;
    public static final BitSet FOLLOW_ENDLINE_in_add_spellxp_cmd4433;
    public static final BitSet FOLLOW_SET_SPELLLEVEL_in_set_spelllevel_cmd4448;
    public static final BitSet FOLLOW_NUMBER_in_set_spelllevel_cmd4452;
    public static final BitSet FOLLOW_NUMBER_in_set_spelllevel_cmd4456;
    public static final BitSet FOLLOW_ENDLINE_in_set_spelllevel_cmd4458;
    public static final BitSet FOLLOW_ADD_SKILLXP_in_add_skillxp_cmd4473;
    public static final BitSet FOLLOW_NUMBER_in_add_skillxp_cmd4477;
    public static final BitSet FOLLOW_NUMBER_in_add_skillxp_cmd4481;
    public static final BitSet FOLLOW_ENDLINE_in_add_skillxp_cmd4483;
    public static final BitSet FOLLOW_ADD_ITEM_XP_in_add_itemxp_cmd4499;
    public static final BitSet FOLLOW_NUMBER_in_add_itemxp_cmd4503;
    public static final BitSet FOLLOW_ENDLINE_in_add_itemxp_cmd4505;
    public static final BitSet FOLLOW_RENT_ITEM_CMD_in_rent_item_cmd4520;
    public static final BitSet FOLLOW_NUMBER_in_rent_item_cmd4524;
    public static final BitSet FOLLOW_NUMBER_in_rent_item_cmd4528;
    public static final BitSet FOLLOW_NUMBER_in_rent_item_cmd4532;
    public static final BitSet FOLLOW_ENDLINE_in_rent_item_cmd4534;
    public static final BitSet FOLLOW_CHARACTER_CMD_in_character_cmd4549;
    public static final BitSet FOLLOW_380_in_character_cmd4551;
    public static final BitSet FOLLOW_NUMBER_in_character_cmd4555;
    public static final BitSet FOLLOW_ENDLINE_in_character_cmd4557;
    public static final BitSet FOLLOW_CHARACTER_CMD_in_character_cmd4564;
    public static final BitSet FOLLOW_set_in_character_cmd4566;
    public static final BitSet FOLLOW_ENDLINE_in_character_cmd4572;
    public static final BitSet FOLLOW_CHARACTER_CMD_in_character_cmd4579;
    public static final BitSet FOLLOW_INFO_in_character_cmd4582;
    public static final BitSet FOLLOW_ENDLINE_in_character_cmd4585;
    public static final BitSet FOLLOW_CHARACTER_CMD_in_character_cmd4592;
    public static final BitSet FOLLOW_382_in_character_cmd4594;
    public static final BitSet FOLLOW_NUMBER_in_character_cmd4598;
    public static final BitSet FOLLOW_ENDLINE_in_character_cmd4600;
    public static final BitSet FOLLOW_CHARACTER_CMD_in_character_cmd4607;
    public static final BitSet FOLLOW_set_in_character_cmd4609;
    public static final BitSet FOLLOW_NUMBER_in_character_cmd4617;
    public static final BitSet FOLLOW_ENDLINE_in_character_cmd4619;
    public static final BitSet FOLLOW_RESTORE_CHARACTER_CMD_in_restore_character_cmd4636;
    public static final BitSet FOLLOW_NUMBER_in_restore_character_cmd4640;
    public static final BitSet FOLLOW_ENDLINE_in_restore_character_cmd4642;
    public static final BitSet FOLLOW_SET_ITEM_TRACKER_LOG_LEVEL_CMD_in_set_item_tracker_log_level_cmd4661;
    public static final BitSet FOLLOW_NUMBER_in_set_item_tracker_log_level_cmd4665;
    public static final BitSet FOLLOW_ENDLINE_in_set_item_tracker_log_level_cmd4667;
    public static final BitSet FOLLOW_SET_ITEM_TRACKER_LOG_LEVEL_CMD_in_set_item_tracker_log_level_cmd4674;
    public static final BitSet FOLLOW_set_in_set_item_tracker_log_level_cmd4676;
    public static final BitSet FOLLOW_ENDLINE_in_set_item_tracker_log_level_cmd4682;
    public static final BitSet FOLLOW_SET_SKILL_LEVEL_in_set_skill_level_cmd4700;
    public static final BitSet FOLLOW_NUMBER_in_set_skill_level_cmd4704;
    public static final BitSet FOLLOW_NUMBER_in_set_skill_level_cmd4708;
    public static final BitSet FOLLOW_ENDLINE_in_set_skill_level_cmd4710;
    public static final BitSet FOLLOW_MONSTER_GROUP_in_monster_group4725;
    public static final BitSet FOLLOW_set_in_monster_group4727;
    public static final BitSet FOLLOW_NUMBER_in_monster_group4738;
    public static final BitSet FOLLOW_ENDLINE_in_monster_group4740;
    public static final BitSet FOLLOW_SET_RESOURCE_SPEED_FACTOR_in_set_resource_speed_factor4758;
    public static final BitSet FOLLOW_FLOAT_in_set_resource_speed_factor4762;
    public static final BitSet FOLLOW_ENDLINE_in_set_resource_speed_factor4764;
    public static final BitSet FOLLOW_ADD_XP_in_add_xp_cmd4779;
    public static final BitSet FOLLOW_NUMBER_in_add_xp_cmd4783;
    public static final BitSet FOLLOW_ENDLINE_in_add_xp_cmd4785;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4801;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4803;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4810;
    public static final BitSet FOLLOW_set_in_set_bonus_factor_cmd4812;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4820;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4827;
    public static final BitSet FOLLOW_309_in_set_bonus_factor_cmd4830;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd4835;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4841;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4846;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4852;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4855;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4862;
    public static final BitSet FOLLOW_299_in_set_bonus_factor_cmd4865;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd4870;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4876;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4881;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4887;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4891;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4898;
    public static final BitSet FOLLOW_344_in_set_bonus_factor_cmd4901;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd4906;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4912;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4917;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4923;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4927;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4934;
    public static final BitSet FOLLOW_321_in_set_bonus_factor_cmd4937;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd4942;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4948;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4953;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4959;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4963;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4970;
    public static final BitSet FOLLOW_324_in_set_bonus_factor_cmd4973;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd4978;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4984;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd4989;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4995;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd4999;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd5006;
    public static final BitSet FOLLOW_310_in_set_bonus_factor_cmd5009;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd5014;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd5020;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd5025;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd5031;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd5035;
    public static final BitSet FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd5042;
    public static final BitSet FOLLOW_PVP_in_set_bonus_factor_cmd5045;
    public static final BitSet FOLLOW_FLOAT_in_set_bonus_factor_cmd5050;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd5056;
    public static final BitSet FOLLOW_DATE_in_set_bonus_factor_cmd5061;
    public static final BitSet FOLLOW_id_list_pattern_in_set_bonus_factor_cmd5067;
    public static final BitSet FOLLOW_ENDLINE_in_set_bonus_factor_cmd5071;
    public static final BitSet FOLLOW_ADD_MONEY_in_add_money_cmd5088;
    public static final BitSet FOLLOW_NUMBER_in_add_money_cmd5092;
    public static final BitSet FOLLOW_HELP_in_help_cmd5107;
    public static final BitSet FOLLOW_character_pattern_in_help_cmd5111;
    public static final BitSet FOLLOW_ENDLINE_in_help_cmd5113;
    public static final BitSet FOLLOW_HELP_in_help_cmd5120;
    public static final BitSet FOLLOW_character_pattern_in_help_cmd5124;
    public static final BitSet FOLLOW_set_in_help_cmd5126;
    public static final BitSet FOLLOW_ENDLINE_in_help_cmd5134;
    public static final BitSet FOLLOW_HELP_in_help_cmd5142;
    public static final BitSet FOLLOW_ENDLINE_in_help_cmd5144;
    public static final BitSet FOLLOW_HELP_in_help_cmd5152;
    public static final BitSet FOLLOW_set_in_help_cmd5154;
    public static final BitSet FOLLOW_ENDLINE_in_help_cmd5162;
    public static final BitSet FOLLOW_INSTANCE_USAGE_in_instance_usage_cmd5181;
    public static final BitSet FOLLOW_NUMBER_in_instance_usage_cmd5185;
    public static final BitSet FOLLOW_ENDLINE_in_instance_usage_cmd5187;
    public static final BitSet FOLLOW_DESTROY_INSTANCE_in_destroy_instance_cmd5203;
    public static final BitSet FOLLOW_ENDLINE_in_destroy_instance_cmd5205;
    public static final BitSet FOLLOW_SHOW_AGGRO_LIST_in_show_aggro_list_cmd5222;
    public static final BitSet FOLLOW_ENDLINE_in_show_aggro_list_cmd5224;
    public static final BitSet FOLLOW_SET_LEVEL_in_set_level_cmd5240;
    public static final BitSet FOLLOW_NUMBER_in_set_level_cmd5244;
    public static final BitSet FOLLOW_ENDLINE_in_set_level_cmd5246;
    public static final BitSet FOLLOW_SPAWN_INTERACTIVE_ELEMENT_in_spawn_ie_cmd5259;
    public static final BitSet FOLLOW_NUMBER_in_spawn_ie_cmd5265;
    public static final BitSet FOLLOW_NUMBER_in_spawn_ie_cmd5271;
    public static final BitSet FOLLOW_ENDLINE_in_spawn_ie_cmd5273;
    public static final BitSet FOLLOW_SESSIONS_in_sessions_cmd5288;
    public static final BitSet FOLLOW_ENDLINE_in_sessions_cmd5290;
    public static final BitSet FOLLOW_SETNEXTCHALLENGE_in_set_next_challenge_cmd5306;
    public static final BitSet FOLLOW_NUMBER_in_set_next_challenge_cmd5312;
    public static final BitSet FOLLOW_ENDLINE_in_set_next_challenge_cmd5314;
    public static final BitSet FOLLOW_FINISHCHALLENGE_in_finish_challenge_cmd5330;
    public static final BitSet FOLLOW_ENDLINE_in_finish_challenge_cmd5332;
    public static final BitSet FOLLOW_STAFF_in_staff_cmd5348;
    public static final BitSet FOLLOW_ON_in_staff_cmd5350;
    public static final BitSet FOLLOW_ENDLINE_in_staff_cmd5352;
    public static final BitSet FOLLOW_STAFF_in_staff_cmd5359;
    public static final BitSet FOLLOW_OFF_in_staff_cmd5361;
    public static final BitSet FOLLOW_ENDLINE_in_staff_cmd5363;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5383;
    public static final BitSet FOLLOW_ON_in_subscriber_cmd5385;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5387;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5394;
    public static final BitSet FOLLOW_OFF_in_subscriber_cmd5396;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5398;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5405;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5409;
    public static final BitSet FOLLOW_ON_in_subscriber_cmd5411;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5413;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5420;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5424;
    public static final BitSet FOLLOW_OFF_in_subscriber_cmd5426;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5428;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5435;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5437;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5445;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5452;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5454;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5462;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5469;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5471;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5479;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5486;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5488;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5496;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5503;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5505;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5515;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5517;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5524;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5526;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5536;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5538;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5545;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5547;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5557;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5559;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5566;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5568;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5578;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5580;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5587;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5589;
    public static final BitSet FOLLOW_NUMBER_in_subscriber_cmd5599;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5601;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5608;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5610;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5618;
    public static final BitSet FOLLOW_SUBSCRIBER_in_subscriber_cmd5625;
    public static final BitSet FOLLOW_set_in_subscriber_cmd5627;
    public static final BitSet FOLLOW_ENDLINE_in_subscriber_cmd5635;
    public static final BitSet FOLLOW_FREE_ACCESS_in_free_access_cmd5653;
    public static final BitSet FOLLOW_ON_in_free_access_cmd5655;
    public static final BitSet FOLLOW_ENDLINE_in_free_access_cmd5657;
    public static final BitSet FOLLOW_FREE_ACCESS_in_free_access_cmd5664;
    public static final BitSet FOLLOW_OFF_in_free_access_cmd5666;
    public static final BitSet FOLLOW_ENDLINE_in_free_access_cmd5668;
    public static final BitSet FOLLOW_FREE_ACCESS_in_free_access_cmd5676;
    public static final BitSet FOLLOW_ENDLINE_in_free_access_cmd5678;
    public static final BitSet FOLLOW_MUTE_PARTITIONS_in_mute_partitions_cmd5699;
    public static final BitSet FOLLOW_proximity_pattern_in_mute_partitions_cmd5703;
    public static final BitSet FOLLOW_ENDLINE_in_mute_partitions_cmd5705;
    public static final BitSet FOLLOW_MUTE_PARTITIONS_in_mute_partitions_cmd5712;
    public static final BitSet FOLLOW_ENDLINE_in_mute_partitions_cmd5714;
    public static final BitSet FOLLOW_UNMUTE_PARTITIONS_in_unmute_partitions_cmd5731;
    public static final BitSet FOLLOW_ENDLINE_in_unmute_partitions_cmd5733;
    public static final BitSet FOLLOW_MUTE_in_mute_cmd5759;
    public static final BitSet FOLLOW_NUMBER_in_mute_cmd5764;
    public static final BitSet FOLLOW_NUMBER_in_mute_cmd5768;
    public static final BitSet FOLLOW_ENDLINE_in_mute_cmd5772;
    public static final BitSet FOLLOW_MUTE_in_mute_cmd5779;
    public static final BitSet FOLLOW_INFO_in_mute_cmd5782;
    public static final BitSet FOLLOW_ENDLINE_in_mute_cmd5785;
    public static final BitSet FOLLOW_UNMUTE_in_unmute_cmd5818;
    public static final BitSet FOLLOW_NUMBER_in_unmute_cmd5822;
    public static final BitSet FOLLOW_ENDLINE_in_unmute_cmd5824;
    public static final BitSet FOLLOW_DISTRIBUTE_ITEMS_in_distribute_items_cmd5842;
    public static final BitSet FOLLOW_proximity_pattern_in_distribute_items_cmd5846;
    public static final BitSet FOLLOW_NUMBER_in_distribute_items_cmd5850;
    public static final BitSet FOLLOW_NUMBER_in_distribute_items_cmd5854;
    public static final BitSet FOLLOW_ENDLINE_in_distribute_items_cmd5856;
    public static final BitSet FOLLOW_DISTRIBUTE_ITEMS_in_distribute_items_cmd5863;
    public static final BitSet FOLLOW_proximity_pattern_in_distribute_items_cmd5867;
    public static final BitSet FOLLOW_NUMBER_in_distribute_items_cmd5871;
    public static final BitSet FOLLOW_ENDLINE_in_distribute_items_cmd5873;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd5891;
    public static final BitSet FOLLOW_361_in_search_cmd5893;
    public static final BitSet FOLLOW_NUMBER_in_search_cmd5897;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd5899;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd5907;
    public static final BitSet FOLLOW_328_in_search_cmd5909;
    public static final BitSet FOLLOW_397_in_search_cmd5913;
    public static final BitSet FOLLOW_NUMBER_in_search_cmd5918;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd5920;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd5927;
    public static final BitSet FOLLOW_319_in_search_cmd5929;
    public static final BitSet FOLLOW_NUMBER_in_search_cmd5933;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd5935;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd5943;
    public static final BitSet FOLLOW_358_in_search_cmd5945;
    public static final BitSet FOLLOW_NUMBER_in_search_cmd5949;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd5951;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd5961;
    public static final BitSet FOLLOW_320_in_search_cmd5963;
    public static final BitSet FOLLOW_394_in_search_cmd5968;
    public static final BitSet FOLLOW_message_in_search_cmd5973;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd5975;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd5985;
    public static final BitSet FOLLOW_STATE_CMD_in_search_cmd5987;
    public static final BitSet FOLLOW_403_in_search_cmd5992;
    public static final BitSet FOLLOW_message_in_search_cmd5997;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd5999;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd6009;
    public static final BitSet FOLLOW_329_in_search_cmd6011;
    public static final BitSet FOLLOW_398_in_search_cmd6016;
    public static final BitSet FOLLOW_message_in_search_cmd6021;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd6023;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd6030;
    public static final BitSet FOLLOW_SET_in_search_cmd6032;
    public static final BitSet FOLLOW_message_in_search_cmd6036;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd6038;
    public static final BitSet FOLLOW_SEARCH_in_search_cmd6045;
    public static final BitSet FOLLOW_349_in_search_cmd6047;
    public static final BitSet FOLLOW_message_in_search_cmd6051;
    public static final BitSet FOLLOW_ENDLINE_in_search_cmd6053;
    public static final BitSet FOLLOW_TELEPORT_TO_MONSTER_in_teleport_to_breed_mob_cmd6070;
    public static final BitSet FOLLOW_NUMBER_in_teleport_to_breed_mob_cmd6074;
    public static final BitSet FOLLOW_ENDLINE_in_teleport_to_breed_mob_cmd6076;
    public static final BitSet FOLLOW_QUOTA_in_quota_cmd6094;
    public static final BitSet FOLLOW_ENDLINE_in_quota_cmd6096;
    public static final BitSet FOLLOW_QUOTA_in_quota_cmd6106;
    public static final BitSet FOLLOW_set_in_quota_cmd6108;
    public static final BitSet FOLLOW_348_in_quota_cmd6119;
    public static final BitSet FOLLOW_BOOLEAN_in_quota_cmd6124;
    public static final BitSet FOLLOW_ENDLINE_in_quota_cmd6126;
    public static final BitSet FOLLOW_QUOTA_in_quota_cmd6135;
    public static final BitSet FOLLOW_set_in_quota_cmd6137;
    public static final BitSet FOLLOW_347_in_quota_cmd6148;
    public static final BitSet FOLLOW_NUMBER_in_quota_cmd6153;
    public static final BitSet FOLLOW_ENDLINE_in_quota_cmd6155;
    public static final BitSet FOLLOW_RAGNAROK_in_ragnarok_cmd6173;
    public static final BitSet FOLLOW_message_in_ragnarok_cmd6177;
    public static final BitSet FOLLOW_ENDLINE_in_ragnarok_cmd6179;
    public static final BitSet FOLLOW_REMOVE_FLOOR_ITEMS_in_remove_floor_items_cmd6196;
    public static final BitSet FOLLOW_ENDLINE_in_remove_floor_items_cmd6198;
    public static final BitSet FOLLOW_SHOW_POPULATION_in_show_population_cmd6217;
    public static final BitSet FOLLOW_NUMBER_in_show_population_cmd6221;
    public static final BitSet FOLLOW_ENDLINE_in_show_population_cmd6223;
    public static final BitSet FOLLOW_SHOW_MONSTER_QUOTA_in_show_monster_quota_cmd6241;
    public static final BitSet FOLLOW_CANCEL_COLLECT_COOLDOWN_in_cancel_collect_cooldown_cmd6259;
    public static final BitSet FOLLOW_SET_WAKFU_GAUGE_in_set_wakfu_gauge_cmd6277;
    public static final BitSet FOLLOW_FLOAT_in_set_wakfu_gauge_cmd6281;
    public static final BitSet FOLLOW_ENDLINE_in_set_wakfu_gauge_cmd6283;
    public static final BitSet FOLLOW_GET_INSTANCE_UID_in_get_instance_uid_cmd6301;
    public static final BitSet FOLLOW_DUMP_BAG_in_dump_bag_cmd6319;
    public static final BitSet FOLLOW_TEMP_in_temp_cmd6337;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6354;
    public static final BitSet FOLLOW_180_in_calendar_cmd6356;
    public static final BitSet FOLLOW_message_in_calendar_cmd6360;
    public static final BitSet FOLLOW_message_in_calendar_cmd6364;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6368;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6372;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6376;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6380;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6382;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6388;
    public static final BitSet FOLLOW_180_in_calendar_cmd6390;
    public static final BitSet FOLLOW_message_in_calendar_cmd6394;
    public static final BitSet FOLLOW_message_in_calendar_cmd6398;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6402;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6406;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6410;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6414;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6418;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6422;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6424;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6431;
    public static final BitSet FOLLOW_234_in_calendar_cmd6433;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6437;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6439;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6446;
    public static final BitSet FOLLOW_193_in_calendar_cmd6448;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6450;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6457;
    public static final BitSet FOLLOW_261_in_calendar_cmd6459;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6463;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6465;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6472;
    public static final BitSet FOLLOW_261_in_calendar_cmd6474;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6476;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6483;
    public static final BitSet FOLLOW_225_in_calendar_cmd6485;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6489;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6491;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6497;
    public static final BitSet FOLLOW_259_in_calendar_cmd6499;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6503;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6505;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6511;
    public static final BitSet FOLLOW_263_in_calendar_cmd6513;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6517;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6521;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6523;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6529;
    public static final BitSet FOLLOW_260_in_calendar_cmd6531;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6535;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6539;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6541;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6547;
    public static final BitSet FOLLOW_222_in_calendar_cmd6549;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6553;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6557;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6561;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6563;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6569;
    public static final BitSet FOLLOW_213_in_calendar_cmd6571;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6575;
    public static final BitSet FOLLOW_message_in_calendar_cmd6579;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6581;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6588;
    public static final BitSet FOLLOW_246_in_calendar_cmd6590;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6594;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6598;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6602;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6606;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6610;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6614;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6616;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6623;
    public static final BitSet FOLLOW_186_in_calendar_cmd6625;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6629;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6636;
    public static final BitSet FOLLOW_245_in_calendar_cmd6638;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6642;
    public static final BitSet FOLLOW_message_in_calendar_cmd6646;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6653;
    public static final BitSet FOLLOW_248_in_calendar_cmd6655;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6659;
    public static final BitSet FOLLOW_message_in_calendar_cmd6663;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6670;
    public static final BitSet FOLLOW_247_in_calendar_cmd6672;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6676;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6680;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6684;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6688;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6692;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6696;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6698;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6705;
    public static final BitSet FOLLOW_247_in_calendar_cmd6707;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6711;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6715;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6719;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6723;
    public static final BitSet FOLLOW_ENDLINE_in_calendar_cmd6725;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6732;
    public static final BitSet FOLLOW_247_in_calendar_cmd6734;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6738;
    public static final BitSet FOLLOW_message_in_calendar_cmd6742;
    public static final BitSet FOLLOW_CALENDAR_CMD_in_calendar_cmd6749;
    public static final BitSet FOLLOW_208_in_calendar_cmd6751;
    public static final BitSet FOLLOW_NUMBER_in_calendar_cmd6755;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6774;
    public static final BitSet FOLLOW_set_in_fight_cmd6776;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6784;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6791;
    public static final BitSet FOLLOW_set_in_fight_cmd6793;
    public static final BitSet FOLLOW_NUMBER_in_fight_cmd6803;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6806;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6813;
    public static final BitSet FOLLOW_set_in_fight_cmd6815;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6828;
    public static final BitSet FOLLOW_264_in_fight_cmd6830;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6832;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6839;
    public static final BitSet FOLLOW_207_in_fight_cmd6841;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6843;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6850;
    public static final BitSet FOLLOW_set_in_fight_cmd6852;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6860;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6867;
    public static final BitSet FOLLOW_set_in_fight_cmd6869;
    public static final BitSet FOLLOW_NUMBER_in_fight_cmd6879;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6881;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6888;
    public static final BitSet FOLLOW_set_in_fight_cmd6890;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6898;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6905;
    public static final BitSet FOLLOW_set_in_fight_cmd6907;
    public static final BitSet FOLLOW_NUMBER_in_fight_cmd6917;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6919;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6926;
    public static final BitSet FOLLOW_set_in_fight_cmd6928;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6936;
    public static final BitSet FOLLOW_FIGHT_CMD_in_fight_cmd6943;
    public static final BitSet FOLLOW_set_in_fight_cmd6945;
    public static final BitSet FOLLOW_NUMBER_in_fight_cmd6955;
    public static final BitSet FOLLOW_ENDLINE_in_fight_cmd6957;
    public static final BitSet FOLLOW_PROTECTOR_CMD_in_protector_command6975;
    public static final BitSet FOLLOW_set_in_protector_command6977;
    public static final BitSet FOLLOW_NUMBER_in_protector_command6987;
    public static final BitSet FOLLOW_NUMBER_in_protector_command6991;
    public static final BitSet FOLLOW_ENDLINE_in_protector_command6993;
    public static final BitSet FOLLOW_PROTECTOR_CMD_in_protector_command7004;
    public static final BitSet FOLLOW_set_in_protector_command7006;
    public static final BitSet FOLLOW_NUMBER_in_protector_command7016;
    public static final BitSet FOLLOW_NUMBER_in_protector_command7020;
    public static final BitSet FOLLOW_ENDLINE_in_protector_command7022;
    public static final BitSet FOLLOW_PROTECTOR_CMD_in_protector_command7033;
    public static final BitSet FOLLOW_set_in_protector_command7035;
    public static final BitSet FOLLOW_NUMBER_in_protector_command7045;
    public static final BitSet FOLLOW_ENDLINE_in_protector_command7047;
    public static final BitSet FOLLOW_PROTECTOR_CMD_in_protector_command7057;
    public static final BitSet FOLLOW_set_in_protector_command7059;
    public static final BitSet FOLLOW_NUMBER_in_protector_command7069;
    public static final BitSet FOLLOW_ENDLINE_in_protector_command7071;
    public static final BitSet FOLLOW_PROTECTOR_CMD_in_protector_command7081;
    public static final BitSet FOLLOW_set_in_protector_command7083;
    public static final BitSet FOLLOW_ENDLINE_in_protector_command7091;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7108;
    public static final BitSet FOLLOW_set_in_state_command7110;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7118;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7125;
    public static final BitSet FOLLOW_set_in_state_command7127;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7135;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7142;
    public static final BitSet FOLLOW_set_in_state_command7144;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7152;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7159;
    public static final BitSet FOLLOW_set_in_state_command7161;
    public static final BitSet FOLLOW_NUMBER_in_state_command7171;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7173;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7180;
    public static final BitSet FOLLOW_set_in_state_command7182;
    public static final BitSet FOLLOW_NUMBER_in_state_command7192;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7194;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7201;
    public static final BitSet FOLLOW_set_in_state_command7203;
    public static final BitSet FOLLOW_NUMBER_in_state_command7213;
    public static final BitSet FOLLOW_NUMBER_in_state_command7217;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7219;
    public static final BitSet FOLLOW_STATE_CMD_in_state_command7226;
    public static final BitSet FOLLOW_set_in_state_command7228;
    public static final BitSet FOLLOW_NUMBER_in_state_command7238;
    public static final BitSet FOLLOW_ENDLINE_in_state_command7240;
    public static final BitSet FOLLOW_SPELL_CMD_in_spell_command7258;
    public static final BitSet FOLLOW_231_in_spell_command7261;
    public static final BitSet FOLLOW_ENDLINE_in_spell_command7264;
    public static final BitSet FOLLOW_CLIENT_GAME_EVENT_CMD_in_client_game_event_command7285;
    public static final BitSet FOLLOW_set_in_client_game_event_command7287;
    public static final BitSet FOLLOW_NUMBER_in_client_game_event_command7297;
    public static final BitSet FOLLOW_ENDLINE_in_client_game_event_command7299;
    public static final BitSet FOLLOW_CLIENT_GAME_EVENT_CMD_in_client_game_event_command7310;
    public static final BitSet FOLLOW_set_in_client_game_event_command7312;
    public static final BitSet FOLLOW_NUMBER_in_client_game_event_command7322;
    public static final BitSet FOLLOW_ENDLINE_in_client_game_event_command7324;
    public static final BitSet FOLLOW_GEM_CMD_in_gem_command7345;
    public static final BitSet FOLLOW_323_in_gem_command7348;
    public static final BitSet FOLLOW_ENDLINE_in_gem_command7351;
    public static final BitSet FOLLOW_GEM_CMD_in_gem_command7358;
    public static final BitSet FOLLOW_CREATE_ITEM_in_gem_command7361;
    public static final BitSet FOLLOW_NUMBER_in_gem_command7366;
    public static final BitSet FOLLOW_NUMBER_in_gem_command7370;
    public static final BitSet FOLLOW_NUMBER_in_gem_command7374;
    public static final BitSet FOLLOW_ENDLINE_in_gem_command7376;
    public static final BitSet FOLLOW_GEM_CMD_in_gem_command7387;
    public static final BitSet FOLLOW_CREATE_ITEM_in_gem_command7390;
    public static final BitSet FOLLOW_NUMBER_in_gem_command7395;
    public static final BitSet FOLLOW_NUMBER_in_gem_command7399;
    public static final BitSet FOLLOW_ENDLINE_in_gem_command7401;
    public static final BitSet FOLLOW_GEM_CMD_in_gem_command7412;
    public static final BitSet FOLLOW_CREATE_ITEM_in_gem_command7415;
    public static final BitSet FOLLOW_NUMBER_in_gem_command7420;
    public static final BitSet FOLLOW_ENDLINE_in_gem_command7422;
    public static final BitSet FOLLOW_GEM_CMD_in_gem_command7433;
    public static final BitSet FOLLOW_set_in_gem_command7435;
    public static final BitSet FOLLOW_ENDLINE_in_gem_command7443;
    public static final BitSet FOLLOW_GEM_CMD_in_gem_command7454;
    public static final BitSet FOLLOW_ENDLINE_in_gem_command7456;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7478;
    public static final BitSet FOLLOW_set_in_aptitude_command7480;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7486;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7493;
    public static final BitSet FOLLOW_set_in_aptitude_command7495;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7501;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7508;
    public static final BitSet FOLLOW_set_in_aptitude_command7510;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7516;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7523;
    public static final BitSet FOLLOW_set_in_aptitude_command7525;
    public static final BitSet FOLLOW_NUMBER_in_aptitude_command7533;
    public static final BitSet FOLLOW_NUMBER_in_aptitude_command7537;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7539;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7546;
    public static final BitSet FOLLOW_set_in_aptitude_command7548;
    public static final BitSet FOLLOW_NUMBER_in_aptitude_command7556;
    public static final BitSet FOLLOW_NUMBER_in_aptitude_command7560;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7562;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7569;
    public static final BitSet FOLLOW_set_in_aptitude_command7571;
    public static final BitSet FOLLOW_NUMBER_in_aptitude_command7579;
    public static final BitSet FOLLOW_NUMBER_in_aptitude_command7583;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7585;
    public static final BitSet FOLLOW_APTITUDE_in_aptitude_command7592;
    public static final BitSet FOLLOW_359_in_aptitude_command7595;
    public static final BitSet FOLLOW_ENDLINE_in_aptitude_command7598;
    public static final BitSet FOLLOW_VERSION_in_version_cmd7616;
    public static final BitSet FOLLOW_ENDLINE_in_version_cmd7618;
    public static final BitSet FOLLOW_SET_RESPAWN_CMD_in_set_respawn_cmd7645;
    public static final BitSet FOLLOW_ENDLINE_in_set_respawn_cmd7647;
    public static final BitSet FOLLOW_CHECK_CMD_in_check_cmd7662;
    public static final BitSet FOLLOW_set_in_check_cmd7664;
    public static final BitSet FOLLOW_NUMBER_in_check_cmd7674;
    public static final BitSet FOLLOW_ENDLINE_in_check_cmd7676;
    public static final BitSet FOLLOW_CHECK_CMD_in_check_cmd7687;
    public static final BitSet FOLLOW_set_in_check_cmd7689;
    public static final BitSet FOLLOW_ENDLINE_in_check_cmd7697;
    public static final BitSet FOLLOW_CHECK_CMD_in_check_cmd7708;
    public static final BitSet FOLLOW_set_in_check_cmd7710;
    public static final BitSet FOLLOW_ENDLINE_in_check_cmd7718;
    public static final BitSet FOLLOW_CRAFT_CMD_in_craft_cmd7737;
    public static final BitSet FOLLOW_set_in_craft_cmd7739;
    public static final BitSet FOLLOW_NUMBER_in_craft_cmd7749;
    public static final BitSet FOLLOW_ENDLINE_in_craft_cmd7751;
    public static final BitSet FOLLOW_CRAFT_CMD_in_craft_cmd7762;
    public static final BitSet FOLLOW_set_in_craft_cmd7764;
    public static final BitSet FOLLOW_NUMBER_in_craft_cmd7774;
    public static final BitSet FOLLOW_ENDLINE_in_craft_cmd7776;
    public static final BitSet FOLLOW_CRAFT_CMD_in_craft_cmd7787;
    public static final BitSet FOLLOW_set_in_craft_cmd7788;
    public static final BitSet FOLLOW_NUMBER_in_craft_cmd7798;
    public static final BitSet FOLLOW_NUMBER_in_craft_cmd7802;
    public static final BitSet FOLLOW_ENDLINE_in_craft_cmd7804;
    public static final BitSet FOLLOW_CRAFT_CMD_in_craft_cmd7815;
    public static final BitSet FOLLOW_set_in_craft_cmd7817;
    public static final BitSet FOLLOW_ENDLINE_in_craft_cmd7825;
    public static final BitSet FOLLOW_ICE_STATUS_in_ice_status_cmd7845;
    public static final BitSet FOLLOW_ENDLINE_in_ice_status_cmd7847;
    public static final BitSet FOLLOW_PET_in_pet_cmd7863;
    public static final BitSet FOLLOW_414_in_pet_cmd7866;
    public static final BitSet FOLLOW_NUMBER_in_pet_cmd7871;
    public static final BitSet FOLLOW_ENDLINE_in_pet_cmd7873;
    public static final BitSet FOLLOW_PET_in_pet_cmd7885;
    public static final BitSet FOLLOW_set_in_pet_cmd7887;
    public static final BitSet FOLLOW_ENDLINE_in_pet_cmd7893;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd7915;
    public static final BitSet FOLLOW_set_in_guild_cmd7917;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd7931;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd7933;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd7941;
    public static final BitSet FOLLOW_327_in_guild_cmd7944;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd7949;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd7951;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd7959;
    public static final BitSet FOLLOW_STATS_in_guild_cmd7962;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd7965;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd7973;
    public static final BitSet FOLLOW_set_in_guild_cmd7975;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd7985;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd7987;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd7995;
    public static final BitSet FOLLOW_set_in_guild_cmd7997;
    public static final BitSet FOLLOW_FLOAT_in_guild_cmd8007;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8009;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8017;
    public static final BitSet FOLLOW_set_in_guild_cmd8019;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd8029;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8031;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8039;
    public static final BitSet FOLLOW_set_in_guild_cmd8041;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8049;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8057;
    public static final BitSet FOLLOW_set_in_guild_cmd8059;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd8069;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8071;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8079;
    public static final BitSet FOLLOW_set_in_guild_cmd8081;
    public static final BitSet FOLLOW_FLOAT_in_guild_cmd8091;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8093;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8101;
    public static final BitSet FOLLOW_INFO_in_guild_cmd8104;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8107;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8115;
    public static final BitSet FOLLOW_317_in_guild_cmd8118;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8121;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8129;
    public static final BitSet FOLLOW_set_in_guild_cmd8131;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd8141;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8143;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8151;
    public static final BitSet FOLLOW_set_in_guild_cmd8153;
    public static final BitSet FOLLOW_NUMBER_in_guild_cmd8163;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8165;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8173;
    public static final BitSet FOLLOW_set_in_guild_cmd8175;
    public static final BitSet FOLLOW_message_in_guild_cmd8185;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8187;
    public static final BitSet FOLLOW_GUILD_in_guild_cmd8195;
    public static final BitSet FOLLOW_set_in_guild_cmd8197;
    public static final BitSet FOLLOW_message_in_guild_cmd8207;
    public static final BitSet FOLLOW_ENDLINE_in_guild_cmd8209;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8231;
    public static final BitSet FOLLOW_set_in_companion_cmd8233;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8241;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8251;
    public static final BitSet FOLLOW_set_in_companion_cmd8253;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8261;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8271;
    public static final BitSet FOLLOW_set_in_companion_cmd8273;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8283;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8285;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8295;
    public static final BitSet FOLLOW_set_in_companion_cmd8297;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8305;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8315;
    public static final BitSet FOLLOW_set_in_companion_cmd8317;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8325;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8335;
    public static final BitSet FOLLOW_set_in_companion_cmd8337;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8347;
    public static final BitSet FOLLOW_message_in_companion_cmd8351;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8353;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8363;
    public static final BitSet FOLLOW_set_in_companion_cmd8365;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8375;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8377;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8387;
    public static final BitSet FOLLOW_set_in_companion_cmd8389;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8399;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8401;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8411;
    public static final BitSet FOLLOW_set_in_companion_cmd8413;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8423;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8427;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8429;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8439;
    public static final BitSet FOLLOW_set_in_companion_cmd8441;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8451;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8453;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8463;
    public static final BitSet FOLLOW_set_in_companion_cmd8465;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8473;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8483;
    public static final BitSet FOLLOW_set_in_companion_cmd8485;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8497;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8507;
    public static final BitSet FOLLOW_333_in_companion_cmd8510;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8513;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8523;
    public static final BitSet FOLLOW_set_in_companion_cmd8525;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8533;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8543;
    public static final BitSet FOLLOW_set_in_companion_cmd8545;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8555;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8559;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8563;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8567;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8569;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8584;
    public static final BitSet FOLLOW_set_in_companion_cmd8586;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8596;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8600;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8605;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8607;
    public static final BitSet FOLLOW_COMPANION_in_companion_cmd8625;
    public static final BitSet FOLLOW_set_in_companion_cmd8627;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8637;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8642;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8646;
    public static final BitSet FOLLOW_NUMBER_in_companion_cmd8650;
    public static final BitSet FOLLOW_ENDLINE_in_companion_cmd8653;
    public static final BitSet FOLLOW_HERO_in_hero_cmd8681;
    public static final BitSet FOLLOW_set_in_hero_cmd8683;
    public static final BitSet FOLLOW_ENDLINE_in_hero_cmd8691;
    public static final BitSet FOLLOW_HERO_in_hero_cmd8698;
    public static final BitSet FOLLOW_set_in_hero_cmd8700;
    public static final BitSet FOLLOW_NUMBER_in_hero_cmd8710;
    public static final BitSet FOLLOW_NUMBER_in_hero_cmd8714;
    public static final BitSet FOLLOW_ENDLINE_in_hero_cmd8716;
    public static final BitSet FOLLOW_HERO_in_hero_cmd8723;
    public static final BitSet FOLLOW_set_in_hero_cmd8725;
    public static final BitSet FOLLOW_ENDLINE_in_hero_cmd8733;
    public static final BitSet FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8752;
    public static final BitSet FOLLOW_set_in_system_configuration_cmd8755;
    public static final BitSet FOLLOW_ENDLINE_in_system_configuration_cmd8763;
    public static final BitSet FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8772;
    public static final BitSet FOLLOW_386_in_system_configuration_cmd8776;
    public static final BitSet FOLLOW_message_in_system_configuration_cmd8781;
    public static final BitSet FOLLOW_message_in_system_configuration_cmd8785;
    public static final BitSet FOLLOW_ENDLINE_in_system_configuration_cmd8787;
    public static final BitSet FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8796;
    public static final BitSet FOLLOW_352_in_system_configuration_cmd8800;
    public static final BitSet FOLLOW_message_in_system_configuration_cmd8805;
    public static final BitSet FOLLOW_ENDLINE_in_system_configuration_cmd8807;
    public static final BitSet FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8816;
    public static final BitSet FOLLOW_set_in_system_configuration_cmd8819;
    public static final BitSet FOLLOW_message_in_system_configuration_cmd8827;
    public static final BitSet FOLLOW_ENDLINE_in_system_configuration_cmd8829;
    public static final BitSet FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8838;
    public static final BitSet FOLLOW_415_in_system_configuration_cmd8842;
    public static final BitSet FOLLOW_message_in_system_configuration_cmd8847;
    public static final BitSet FOLLOW_DATE_in_system_configuration_cmd8852;
    public static final BitSet FOLLOW_DATE_in_system_configuration_cmd8857;
    public static final BitSet FOLLOW_ENDLINE_in_system_configuration_cmd8861;
    public static final BitSet FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8870;
    public static final BitSet FOLLOW_341_in_system_configuration_cmd8874;
    public static final BitSet FOLLOW_message_in_system_configuration_cmd8879;
    public static final BitSet FOLLOW_ENDLINE_in_system_configuration_cmd8881;
    public static final BitSet FOLLOW_AI_in_ai_cmd8905;
    public static final BitSet FOLLOW_set_in_ai_cmd8907;
    public static final BitSet FOLLOW_ENDLINE_in_ai_cmd8915;
    public static final BitSet FOLLOW_AI_in_ai_cmd8926;
    public static final BitSet FOLLOW_ON_in_ai_cmd8928;
    public static final BitSet FOLLOW_message_in_ai_cmd8932;
    public static final BitSet FOLLOW_ENDLINE_in_ai_cmd8934;
    public static final BitSet FOLLOW_AI_in_ai_cmd8945;
    public static final BitSet FOLLOW_OFF_in_ai_cmd8947;
    public static final BitSet FOLLOW_message_in_ai_cmd8951;
    public static final BitSet FOLLOW_ENDLINE_in_ai_cmd8953;
    public static final BitSet FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd8978;
    public static final BitSet FOLLOW_set_in_fightchallenge_cmd8980;
    public static final BitSet FOLLOW_ENDLINE_in_fightchallenge_cmd8988;
    public static final BitSet FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd8999;
    public static final BitSet FOLLOW_set_in_fightchallenge_cmd9001;
    public static final BitSet FOLLOW_ENDLINE_in_fightchallenge_cmd9009;
    public static final BitSet FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd9020;
    public static final BitSet FOLLOW_set_in_fightchallenge_cmd9022;
    public static final BitSet FOLLOW_NUMBER_in_fightchallenge_cmd9032;
    public static final BitSet FOLLOW_ENDLINE_in_fightchallenge_cmd9034;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9055;
    public static final BitSet FOLLOW_START_in_havenworld_cmd9057;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9059;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9067;
    public static final BitSet FOLLOW_STOP_in_havenworld_cmd9069;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9071;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9079;
    public static final BitSet FOLLOW_411_in_havenworld_cmd9081;
    public static final BitSet FOLLOW_298_in_havenworld_cmd9083;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9087;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9091;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9093;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9101;
    public static final BitSet FOLLOW_411_in_havenworld_cmd9103;
    public static final BitSet FOLLOW_413_in_havenworld_cmd9105;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9109;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9113;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9117;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9119;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9127;
    public static final BitSet FOLLOW_288_in_havenworld_cmd9129;
    public static final BitSet FOLLOW_298_in_havenworld_cmd9131;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9135;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9139;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9143;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9145;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9153;
    public static final BitSet FOLLOW_288_in_havenworld_cmd9155;
    public static final BitSet FOLLOW_300_in_havenworld_cmd9157;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9161;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9163;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9171;
    public static final BitSet FOLLOW_297_in_havenworld_cmd9173;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9175;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9183;
    public static final BitSet FOLLOW_287_in_havenworld_cmd9185;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9189;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9193;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9195;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9210;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9212;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9222;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9224;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9232;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9234;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9244;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9246;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9258;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9260;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9270;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9272;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9288;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9290;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9300;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9304;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9306;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9322;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9324;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9334;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9338;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9340;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9356;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9358;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9368;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9372;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9376;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9380;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9384;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9388;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9392;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9394;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9413;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9415;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9425;
    public static final BitSet FOLLOW_NUMBER_in_havenworld_cmd9429;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9431;
    public static final BitSet FOLLOW_HAVEN_WORLD_in_havenworld_cmd9450;
    public static final BitSet FOLLOW_set_in_havenworld_cmd9452;
    public static final BitSet FOLLOW_ENDLINE_in_havenworld_cmd9460;
    public static final BitSet FOLLOW_ALMANACH_in_almanach_start_cmd9484;
    public static final BitSet FOLLOW_START_in_almanach_start_cmd9486;
    public static final BitSet FOLLOW_NUMBER_in_almanach_start_cmd9490;
    public static final BitSet FOLLOW_ENDLINE_in_almanach_start_cmd9492;
    public static final BitSet FOLLOW_LEARN_EMOTE_in_learn_emote_cmd9511;
    public static final BitSet FOLLOW_NUMBER_in_learn_emote_cmd9515;
    public static final BitSet FOLLOW_ENDLINE_in_learn_emote_cmd9517;
    public static final BitSet FOLLOW_SET_PLAYER_TITLE_in_set_player_title_cmd9540;
    public static final BitSet FOLLOW_NUMBER_in_set_player_title_cmd9544;
    public static final BitSet FOLLOW_character_pattern_in_set_player_title_cmd9548;
    public static final BitSet FOLLOW_ENDLINE_in_set_player_title_cmd9550;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9573;
    public static final BitSet FOLLOW_set_in_inventory_cmd9575;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9583;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9593;
    public static final BitSet FOLLOW_set_in_inventory_cmd9595;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9603;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9613;
    public static final BitSet FOLLOW_set_in_inventory_cmd9615;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9623;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9633;
    public static final BitSet FOLLOW_set_in_inventory_cmd9635;
    public static final BitSet FOLLOW_id_list_pattern_in_inventory_cmd9645;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9648;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9658;
    public static final BitSet FOLLOW_set_in_inventory_cmd9660;
    public static final BitSet FOLLOW_NUMBER_in_inventory_cmd9670;
    public static final BitSet FOLLOW_id_list_pattern_in_inventory_cmd9674;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9677;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9687;
    public static final BitSet FOLLOW_set_in_inventory_cmd9689;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9697;
    public static final BitSet FOLLOW_INVENTORY_in_inventory_cmd9707;
    public static final BitSet FOLLOW_set_in_inventory_cmd9709;
    public static final BitSet FOLLOW_NUMBER_in_inventory_cmd9719;
    public static final BitSet FOLLOW_NUMBER_in_inventory_cmd9723;
    public static final BitSet FOLLOW_ENDLINE_in_inventory_cmd9725;
    public static final BitSet FOLLOW_EMPTY_CHAR_in_empty_char_cmd9749;
    public static final BitSet FOLLOW_ENDLINE_in_empty_char_cmd9751;
    public static final BitSet FOLLOW_POPUP_MESSAGE_in_popup_message_cmd9779;
    public static final BitSet FOLLOW_character_pattern_in_popup_message_cmd9783;
    public static final BitSet FOLLOW_ESCAPED_STRING_in_popup_message_cmd9787;
    public static final BitSet FOLLOW_ENDLINE_in_popup_message_cmd9789;
    public static final BitSet FOLLOW_RED_MESSAGE_in_red_message_cmd9817;
    public static final BitSet FOLLOW_ESCAPED_STRING_in_red_message_cmd9821;
    public static final BitSet FOLLOW_ENDLINE_in_red_message_cmd9823;
    public static final BitSet FOLLOW_RED_MESSAGE_TO_PLAYER_in_red_message_to_player_cmd9851;
    public static final BitSet FOLLOW_character_pattern_in_red_message_to_player_cmd9855;
    public static final BitSet FOLLOW_ESCAPED_STRING_in_red_message_to_player_cmd9859;
    public static final BitSet FOLLOW_ENDLINE_in_red_message_to_player_cmd9861;
    public static final BitSet FOLLOW_EMOTE_TARGETABLE_in_emote_targetable_cmd9889;
    public static final BitSet FOLLOW_NUMBER_in_emote_targetable_cmd9893;
    public static final BitSet FOLLOW_ENDLINE_in_emote_targetable_cmd9895;
    public static final BitSet FOLLOW_HAVEN_BAG_KICK_in_haven_bag_kick_cmd9923;
    public static final BitSet FOLLOW_NUMBER_in_haven_bag_kick_cmd9927;
    public static final BitSet FOLLOW_ENDLINE_in_haven_bag_kick_cmd9929;
    public static final BitSet FOLLOW_HAVEN_BAG_KICK_in_haven_bag_kick_cmd9939;
    public static final BitSet FOLLOW_character_pattern_in_haven_bag_kick_cmd9943;
    public static final BitSet FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd9967;
    public static final BitSet FOLLOW_character_pattern_in_tp_to_jail_cmd9971;
    public static final BitSet FOLLOW_ENDLINE_in_tp_to_jail_cmd9973;
    public static final BitSet FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd9983;
    public static final BitSet FOLLOW_character_pattern_in_tp_to_jail_cmd9987;
    public static final BitSet FOLLOW_NUMBER_in_tp_to_jail_cmd9991;
    public static final BitSet FOLLOW_265_in_tp_to_jail_cmd9993;
    public static final BitSet FOLLOW_ENDLINE_in_tp_to_jail_cmd9995;
    public static final BitSet FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd10005;
    public static final BitSet FOLLOW_character_pattern_in_tp_to_jail_cmd10009;
    public static final BitSet FOLLOW_NUMBER_in_tp_to_jail_cmd10013;
    public static final BitSet FOLLOW_266_in_tp_to_jail_cmd10015;
    public static final BitSet FOLLOW_ENDLINE_in_tp_to_jail_cmd10017;
    public static final BitSet FOLLOW_FREEDOM_in_freedom_cmd10045;
    public static final BitSet FOLLOW_character_pattern_in_freedom_cmd10049;
    public static final BitSet FOLLOW_WEB_BROWSER_in_web_browser_cmd10077;
    public static final BitSet FOLLOW_LIST_LOOT_in_listloot_cmd10105;
    public static final BitSet FOLLOW_NUMBER_in_listloot_cmd10109;
    public static final BitSet FOLLOW_ENDLINE_in_listloot_cmd10111;
    public static final BitSet FOLLOW_REVIVE_in_revive_cmd10139;
    public static final BitSet FOLLOW_character_pattern_in_revive_cmd10143;
    public static final BitSet FOLLOW_ENDLINE_in_revive_cmd10145;
    public static final BitSet FOLLOW_GIVE_ITEM_in_give_item_cmd10165;
    public static final BitSet FOLLOW_character_pattern_in_give_item_cmd10169;
    public static final BitSet FOLLOW_NUMBER_in_give_item_cmd10173;
    public static final BitSet FOLLOW_NUMBER_in_give_item_cmd10177;
    public static final BitSet FOLLOW_ENDLINE_in_give_item_cmd10179;
    public static final BitSet FOLLOW_GIVE_ITEM_in_give_item_cmd10185;
    public static final BitSet FOLLOW_character_pattern_in_give_item_cmd10189;
    public static final BitSet FOLLOW_NUMBER_in_give_item_cmd10193;
    public static final BitSet FOLLOW_ENDLINE_in_give_item_cmd10195;
    public static final BitSet FOLLOW_RESET_ACCOUNT_MARKET_ENTRIES_in_reset_account_market_entries_cmd10212;
    public static final BitSet FOLLOW_NUMBER_in_reset_account_market_entries_cmd10216;
    public static final BitSet FOLLOW_ENDLINE_in_reset_account_market_entries_cmd10218;
    public static final BitSet FOLLOW_stats_cmd_in_cmd10235;
    public static final BitSet FOLLOW_bot_cmd_in_cmd10266;
    public static final BitSet FOLLOW_panel_cmd_in_cmd10278;
    public static final BitSet FOLLOW_ping_cmd_in_cmd10290;
    public static final BitSet FOLLOW_time_cmd_in_cmd10302;
    public static final BitSet FOLLOW_who_cmd_in_cmd10314;
    public static final BitSet FOLLOW_where_cmd_in_cmd10326;
    public static final BitSet FOLLOW_teleport_to_player_cmd_in_cmd10358;
    public static final BitSet FOLLOW_teleport_to_coords_cmd_in_cmd10377;
    public static final BitSet FOLLOW_teleport_to_inst_cmd_in_cmd10395;
    public static final BitSet FOLLOW_teleport_player_to_me_cmd_in_cmd10416;
    public static final BitSet FOLLOW_teleport_player_to_coords_cmd_in_cmd10431;
    public static final BitSet FOLLOW_teleport_player_to_instance_cmd_in_cmd10443;
    public static final BitSet FOLLOW_serverlock_cmd_in_cmd10453;
    public static final BitSet FOLLOW_kick_cmd_in_cmd10480;
    public static final BitSet FOLLOW_ban_cmd_in_cmd10512;
    public static final BitSet FOLLOW_ghostcheck_cmd_in_cmd10545;
    public static final BitSet FOLLOW_identphase_cmd_in_cmd10556;
    public static final BitSet FOLLOW_shutdown_cmd_in_cmd10567;
    public static final BitSet FOLLOW_sysmsg_cmd_in_cmd10578;
    public static final BitSet FOLLOW_msgall_cmd_in_cmd10590;
    public static final BitSet FOLLOW_symbiot_cmd_in_cmd10602;
    public static final BitSet FOLLOW_nation_cmd_in_cmd10614;
    public static final BitSet FOLLOW_achievement_cmd_in_cmd10626;
    public static final BitSet FOLLOW_achievement_date_cmd_in_cmd10637;
    public static final BitSet FOLLOW_zone_buff_cmd_in_cmd10647;
    public static final BitSet FOLLOW_create_group_cmd_in_cmd10658;
    public static final BitSet FOLLOW_add_to_group_cmd_in_cmd10669;
    public static final BitSet FOLLOW_rights_cmd_in_cmd10680;
    public static final BitSet FOLLOW_chaos_cmd_in_cmd10692;
    public static final BitSet FOLLOW_restart_chaos_cmd_in_cmd10704;
    public static final BitSet FOLLOW_create_item_cmd_in_cmd10715;
    public static final BitSet FOLLOW_create_set_cmd_in_cmd10726;
    public static final BitSet FOLLOW_delete_item_cmd_in_cmd10737;
    public static final BitSet FOLLOW_regenerate_cmd_in_cmd10748;
    public static final BitSet FOLLOW_regenerate_with_item_cmd_in_cmd10759;
    public static final BitSet FOLLOW_turn_duration_cmd_in_cmd10769;
    public static final BitSet FOLLOW_pvp_cmd_in_cmd10780;
    public static final BitSet FOLLOW_run_action_cmd_in_cmd10792;
    public static final BitSet FOLLOW_end_scenario_cmd_in_cmd10803;
    public static final BitSet FOLLOW_reload_scenarios_cmd_in_cmd10814;
    public static final BitSet FOLLOW_add_spellxp_cmd_in_cmd10824;
    public static final BitSet FOLLOW_set_spelllevel_cmd_in_cmd10835;
    public static final BitSet FOLLOW_add_skillxp_cmd_in_cmd10846;
    public static final BitSet FOLLOW_set_skill_level_cmd_in_cmd10857;
    public static final BitSet FOLLOW_add_xp_cmd_in_cmd10868;
    public static final BitSet FOLLOW_set_bonus_factor_cmd_in_cmd10880;
    public static final BitSet FOLLOW_add_money_cmd_in_cmd10890;
    public static final BitSet FOLLOW_help_cmd_in_cmd10901;
    public static final BitSet FOLLOW_god_mode_cmd_in_cmd10913;
    public static final BitSet FOLLOW_instance_usage_cmd_in_cmd10924;
    public static final BitSet FOLLOW_destroy_instance_cmd_in_cmd10935;
    public static final BitSet FOLLOW_show_aggro_list_cmd_in_cmd10945;
    public static final BitSet FOLLOW_play_animation_cmd_in_cmd10956;
    public static final BitSet FOLLOW_play_aps_cmd_in_cmd10967;
    public static final BitSet FOLLOW_set_level_cmd_in_cmd10978;
    public static final BitSet FOLLOW_spawn_ie_cmd_in_cmd10989;
    public static final BitSet FOLLOW_sessions_cmd_in_cmd11000;
    public static final BitSet FOLLOW_set_next_challenge_cmd_in_cmd11011;
    public static final BitSet FOLLOW_finish_challenge_cmd_in_cmd11021;
    public static final BitSet FOLLOW_staff_cmd_in_cmd11031;
    public static final BitSet FOLLOW_subscriber_cmd_in_cmd11043;
    public static final BitSet FOLLOW_mute_partitions_cmd_in_cmd11054;
    public static final BitSet FOLLOW_unmute_partitions_cmd_in_cmd11065;
    public static final BitSet FOLLOW_mute_cmd_in_cmd11075;
    public static final BitSet FOLLOW_unmute_cmd_in_cmd11087;
    public static final BitSet FOLLOW_distribute_items_cmd_in_cmd11099;
    public static final BitSet FOLLOW_search_cmd_in_cmd11109;
    public static final BitSet FOLLOW_teleport_to_breed_mob_cmd_in_cmd11121;
    public static final BitSet FOLLOW_buff_character_cmd_in_cmd11131;
    public static final BitSet FOLLOW_restore_character_cmd_in_cmd11142;
    public static final BitSet FOLLOW_set_item_tracker_log_level_cmd_in_cmd11152;
    public static final BitSet FOLLOW_quota_cmd_in_cmd11161;
    public static final BitSet FOLLOW_ragnarok_cmd_in_cmd11174;
    public static final BitSet FOLLOW_remove_floor_items_cmd_in_cmd11185;
    public static final BitSet FOLLOW_show_population_cmd_in_cmd11195;
    public static final BitSet FOLLOW_show_monster_quota_cmd_in_cmd11206;
    public static final BitSet FOLLOW_cancel_collect_cooldown_cmd_in_cmd11216;
    public static final BitSet FOLLOW_get_instance_uid_cmd_in_cmd11226;
    public static final BitSet FOLLOW_dump_bag_cmd_in_cmd11236;
    public static final BitSet FOLLOW_set_wakfu_gauge_cmd_in_cmd11247;
    public static final BitSet FOLLOW_temp_cmd_in_cmd11258;
    public static final BitSet FOLLOW_calendar_cmd_in_cmd11270;
    public static final BitSet FOLLOW_fight_cmd_in_cmd11281;
    public static final BitSet FOLLOW_protector_command_in_cmd11293;
    public static final BitSet FOLLOW_monster_group_in_cmd11304;
    public static final BitSet FOLLOW_set_resource_speed_factor_in_cmd11315;
    public static final BitSet FOLLOW_state_command_in_cmd11325;
    public static final BitSet FOLLOW_scenario_cmd_in_cmd11336;
    public static final BitSet FOLLOW_version_cmd_in_cmd11347;
    public static final BitSet FOLLOW_plant_resources_cmd_in_cmd11359;
    public static final BitSet FOLLOW_destroy_resources_cmd_in_cmd11370;
    public static final BitSet FOLLOW_destroy_monsters_cmd_in_cmd11380;
    public static final BitSet FOLLOW_set_respawn_cmd_in_cmd11390;
    public static final BitSet FOLLOW_check_cmd_in_cmd11401;
    public static final BitSet FOLLOW_craft_cmd_in_cmd11413;
    public static final BitSet FOLLOW_ban_request_cmd_in_cmd11425;
    public static final BitSet FOLLOW_ice_status_cmd_in_cmd11436;
    public static final BitSet FOLLOW_pet_cmd_in_cmd11447;
    public static final BitSet FOLLOW_add_itemxp_cmd_in_cmd11460;
    public static final BitSet FOLLOW_guild_cmd_in_cmd11471;
    public static final BitSet FOLLOW_companion_cmd_in_cmd11483;
    public static final BitSet FOLLOW_system_configuration_cmd_in_cmd11494;
    public static final BitSet FOLLOW_ai_cmd_in_cmd11504;
    public static final BitSet FOLLOW_fightchallenge_cmd_in_cmd11516;
    public static final BitSet FOLLOW_spell_command_in_cmd11527;
    public static final BitSet FOLLOW_gem_command_in_cmd11538;
    public static final BitSet FOLLOW_aptitude_command_in_cmd11550;
    public static final BitSet FOLLOW_havenworld_cmd_in_cmd11561;
    public static final BitSet FOLLOW_almanach_start_cmd_in_cmd11572;
    public static final BitSet FOLLOW_learn_emote_cmd_in_cmd11583;
    public static final BitSet FOLLOW_set_player_title_cmd_in_cmd11598;
    public static final BitSet FOLLOW_free_access_cmd_in_cmd11613;
    public static final BitSet FOLLOW_create_full_group_cmd_in_cmd11629;
    public static final BitSet FOLLOW_inventory_cmd_in_cmd11644;
    public static final BitSet FOLLOW_empty_char_cmd_in_cmd11665;
    public static final BitSet FOLLOW_client_game_event_command_in_cmd11686;
    public static final BitSet FOLLOW_rent_item_cmd_in_cmd11705;
    public static final BitSet FOLLOW_character_cmd_in_cmd11724;
    public static final BitSet FOLLOW_popup_message_cmd_in_cmd11745;
    public static final BitSet FOLLOW_red_message_cmd_in_cmd11756;
    public static final BitSet FOLLOW_red_message_to_player_cmd_in_cmd11767;
    public static final BitSet FOLLOW_emote_targetable_cmd_in_cmd11777;
    public static final BitSet FOLLOW_haven_bag_kick_cmd_in_cmd11787;
    public static final BitSet FOLLOW_tp_to_jail_cmd_in_cmd11798;
    public static final BitSet FOLLOW_freedom_cmd_in_cmd11809;
    public static final BitSet FOLLOW_web_browser_cmd_in_cmd11821;
    public static final BitSet FOLLOW_give_item_cmd_in_cmd11832;
    public static final BitSet FOLLOW_listloot_cmd_in_cmd11843;
    public static final BitSet FOLLOW_revive_cmd_in_cmd11854;
    public static final BitSet FOLLOW_reset_account_market_entries_cmd_in_cmd11866;
    public static final BitSet FOLLOW_hero_cmd_in_cmd11875;
    
    public Parser[] getDelegates() {
        return new Parser[0];
    }
    
    public ModerationCommandParser(final TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    
    public ModerationCommandParser(final TokenStream input, final RecognizerSharedState state) {
        super(input, state);
        this.dfa92 = new DFA92((BaseRecognizer)this);
    }
    
    public String[] getTokenNames() {
        return ModerationCommandParser.tokenNames;
    }
    
    public String getGrammarFileName() {
        return "F:\\work\\CODE_TRUNK\\src\\maven\\wakfu-parent\\wakfu-parent-java\\wakfu-client\\src\\main\\java\\com\\ankamagames\\wakfu\\client\\console\\command\\admin\\commands\\antlr\\ModerationCommand.g";
    }
    
    public void emitErrorMessage(final String msg) {
        ConsoleManager.getInstance().err(msg);
        ModerationCommandParser.m_logger.error((Object)msg);
    }
    
    public final ObjectPair coords() throws RecognitionException {
        ObjectPair coords = null;
        Token x = null;
        Token y = null;
        try {
            x = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_coords1984);
            int alt1 = 2;
            final int LA1_0 = this.input.LA(1);
            if (LA1_0 == 35) {
                alt1 = 1;
            }
            switch (alt1) {
                case 1: {
                    this.match((IntStream)this.input, 35, ModerationCommandParser.FOLLOW_COORDS_SEPARATOR_in_coords1986);
                    break;
                }
            }
            y = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_coords1991);
            coords = new ObjectPair((F)Integer.parseInt((x != null) ? x.getText() : null), (S)Integer.parseInt((y != null) ? y.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return coords;
    }
    
    public final character_pattern_return character_pattern() throws RecognitionException {
        final character_pattern_return retval = new character_pattern_return();
        retval.start = this.input.LT(1);
        Token sp = null;
        Token ep = null;
        Token ap = null;
        try {
            int alt2 = 3;
            switch (this.input.LA(1)) {
                case 27: {
                    alt2 = 1;
                    break;
                }
                case 56: {
                    alt2 = 2;
                    break;
                }
                case 4: {
                    alt2 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 2, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt2) {
                case 1: {
                    sp = (Token)this.match((IntStream)this.input, 27, ModerationCommandParser.FOLLOW_CHARNAME_PATTERN_in_character_pattern2008);
                    retval.pattern = ((sp != null) ? sp.getText() : null);
                    break;
                }
                case 2: {
                    ep = (Token)this.match((IntStream)this.input, 56, ModerationCommandParser.FOLLOW_ESCAPED_STRING_in_character_pattern2017);
                    retval.pattern = ((ep != null) ? ep.getText() : null).substring(1, ((ep != null) ? ep.getText() : null).length() - 1);
                    break;
                }
                case 3: {
                    ap = (Token)this.match((IntStream)this.input, 4, ModerationCommandParser.FOLLOW_ACCOUNT_PATTERN_in_character_pattern2026);
                    retval.pattern = ((ap != null) ? ap.getText() : null);
                    break;
                }
            }
            retval.stop = this.input.LT(-1);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return retval;
    }
    
    public final message_return message() throws RecognitionException {
        final message_return retval = new message_return();
        retval.start = this.input.LT(1);
        Token es = null;
        try {
            es = (Token)this.match((IntStream)this.input, 56, ModerationCommandParser.FOLLOW_ESCAPED_STRING_in_message2046);
            retval.txt = ((es != null) ? es.getText() : null).substring(1, ((es != null) ? es.getText() : null).length() - 1);
            retval.stop = this.input.LT(-1);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return retval;
    }
    
    public final int proximity_pattern() throws RecognitionException {
        int radius = 0;
        Token pp = null;
        try {
            pp = (Token)this.match((IntStream)this.input, 100, ModerationCommandParser.FOLLOW_PROXIMITY_PATTERN_in_proximity_pattern2065);
            radius = Integer.parseInt(((pp != null) ? pp.getText() : null).substring(1));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return radius;
    }
    
    public final List<Integer> id_list_pattern() throws RecognitionException {
        List<Integer> idList = null;
        Token NUMBER1 = null;
        idList = new ArrayList<Integer>();
        Label_0164: {
            try {
                int cnt3 = 0;
                while (true) {
                    int alt3 = 2;
                    final int LA3_0 = this.input.LA(1);
                    if (LA3_0 == 87) {
                        alt3 = 1;
                    }
                    switch (alt3) {
                        case 1: {
                            NUMBER1 = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_id_list_pattern2087);
                            idList.add(Integer.parseInt((NUMBER1 != null) ? NUMBER1.getText() : null));
                            ++cnt3;
                            continue;
                        }
                        default: {
                            if (cnt3 >= 1) {
                                break Label_0164;
                            }
                            final EarlyExitException eee = new EarlyExitException(3, (IntStream)this.input);
                            throw eee;
                        }
                    }
                }
            }
            catch (RecognitionException re) {
                this.reportError(re);
                this.recover((IntStream)this.input, re);
            }
        }
        return idList;
    }
    
    public final ModerationCommand bot_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token accountId = null;
        try {
            this.match((IntStream)this.input, 21, ModerationCommandParser.FOLLOW_BOT_in_bot_cmd2108);
            this.match((IntStream)this.input, 94, ModerationCommandParser.FOLLOW_PING_in_bot_cmd2110);
            accountId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_bot_cmd2114);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_bot_cmd2116);
            cmd = new BotCommand((byte)1, Long.parseLong((accountId != null) ? accountId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand stats_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 148, ModerationCommandParser.FOLLOW_STATS_in_stats_cmd2131);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_stats_cmd2133);
            cmd = new StatsCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand panel_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 91, ModerationCommandParser.FOLLOW_PANEL_in_panel_cmd2148);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_panel_cmd2150);
            cmd = new AdminPanelCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ping_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            int alt4 = 3;
            final int LA4_0 = this.input.LA(1);
            if (LA4_0 != 94) {
                final NoViableAltException nvae = new NoViableAltException("", 4, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt4 = 1;
                    break;
                }
                case 145: {
                    alt4 = 2;
                    break;
                }
                case 150: {
                    alt4 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 4, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt4) {
                case 1: {
                    this.match((IntStream)this.input, 94, ModerationCommandParser.FOLLOW_PING_in_ping_cmd2165);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ping_cmd2167);
                    cmd = new PingCommand();
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 94, ModerationCommandParser.FOLLOW_PING_in_ping_cmd2174);
                    this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_ping_cmd2176);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ping_cmd2178);
                    cmd = new PingCommand(true);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 94, ModerationCommandParser.FOLLOW_PING_in_ping_cmd2185);
                    this.match((IntStream)this.input, 150, ModerationCommandParser.FOLLOW_STOP_in_ping_cmd2187);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ping_cmd2189);
                    cmd = new PingCommand(false);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand time_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 159, ModerationCommandParser.FOLLOW_TIME_in_time_cmd2210);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_time_cmd2212);
            cmd = new TimeCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand who_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 171, ModerationCommandParser.FOLLOW_WHO_in_who_cmd2233);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_who_cmd2237);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_who_cmd2239);
            cmd = new WhoCommand((name != null) ? name.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand where_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 169, ModerationCommandParser.FOLLOW_WHERE_in_where_cmd2254);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_where_cmd2258);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_where_cmd2260);
            cmd = new WhereCommand((name != null) ? name.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_to_player_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 155, ModerationCommandParser.FOLLOW_TELEPORT_in_teleport_to_player_cmd2275);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_teleport_to_player_cmd2279);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_to_player_cmd2281);
            cmd = new TeleportToPlayerCommand((name != null) ? name.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_player_to_me_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 157, ModerationCommandParser.FOLLOW_TELEPORT_USER_in_teleport_player_to_me_cmd2296);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_teleport_player_to_me_cmd2300);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_player_to_me_cmd2302);
            cmd = new TeleportPlayerToMeCommand((name != null) ? name.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_player_to_coords_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        ObjectPair c = null;
        try {
            this.match((IntStream)this.input, 157, ModerationCommandParser.FOLLOW_TELEPORT_USER_in_teleport_player_to_coords_cmd2317);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_teleport_player_to_coords_cmd2321);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.pushFollow(ModerationCommandParser.FOLLOW_coords_in_teleport_player_to_coords_cmd2325);
            c = this.coords();
            final RecognizerSharedState state2 = this.state;
            --state2._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_player_to_coords_cmd2327);
            cmd = new TeleportPlayerToCoordsCommand((name != null) ? name.pattern : null, c);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_player_to_instance_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token i = null;
        character_pattern_return name = null;
        ObjectPair c = null;
        try {
            this.match((IntStream)this.input, 157, ModerationCommandParser.FOLLOW_TELEPORT_USER_in_teleport_player_to_instance_cmd2342);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_teleport_player_to_instance_cmd2346);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.pushFollow(ModerationCommandParser.FOLLOW_coords_in_teleport_player_to_instance_cmd2350);
            c = this.coords();
            final RecognizerSharedState state2 = this.state;
            --state2._fsp;
            i = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_teleport_player_to_instance_cmd2354);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_player_to_instance_cmd2356);
            cmd = new TeleportPlayerToInstanceCommand((name != null) ? name.pattern : null, c, Short.parseShort((i != null) ? i.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_to_coords_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        ObjectPair c = null;
        try {
            this.match((IntStream)this.input, 155, ModerationCommandParser.FOLLOW_TELEPORT_in_teleport_to_coords_cmd2372);
            this.pushFollow(ModerationCommandParser.FOLLOW_coords_in_teleport_to_coords_cmd2376);
            c = this.coords();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_to_coords_cmd2378);
            cmd = new TeleportToCoordsCommand(c);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_to_inst_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token i = null;
        ObjectPair c = null;
        try {
            this.match((IntStream)this.input, 155, ModerationCommandParser.FOLLOW_TELEPORT_in_teleport_to_inst_cmd2393);
            this.pushFollow(ModerationCommandParser.FOLLOW_coords_in_teleport_to_inst_cmd2397);
            c = this.coords();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            i = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_teleport_to_inst_cmd2401);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_to_inst_cmd2403);
            cmd = new TeleportToInstanceCommand(c, Short.parseShort((i != null) ? i.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand serverlock_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 124, ModerationCommandParser.FOLLOW_SERVERLOCK_in_serverlock_cmd2418);
            int alt5 = 3;
            switch (this.input.LA(1)) {
                case 90: {
                    alt5 = 1;
                    break;
                }
                case 89: {
                    alt5 = 2;
                    break;
                }
                case 53: {
                    alt5 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 5, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt5) {
                case 1: {
                    this.match((IntStream)this.input, 90, ModerationCommandParser.FOLLOW_ON_in_serverlock_cmd2421);
                    cmd = new ServerlockCommand((byte)1);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 89, ModerationCommandParser.FOLLOW_OFF_in_serverlock_cmd2427);
                    cmd = new ServerlockCommand((byte)2);
                    break;
                }
                case 3: {
                    cmd = new ServerlockCommand((byte)0);
                    break;
                }
            }
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_serverlock_cmd2439);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand kick_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        message_return reason = null;
        try {
            int alt6 = 2;
            final int LA6_0 = this.input.LA(1);
            if (LA6_0 != 79) {
                final NoViableAltException nvae = new NoViableAltException("", 6, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 27: {
                    final int LA6_ = this.input.LA(3);
                    if (LA6_ == 53) {
                        alt6 = 1;
                    }
                    else {
                        if (LA6_ != 56) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 6, 2, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt6 = 2;
                    }
                    break;
                }
                case 56: {
                    final int LA6_2 = this.input.LA(3);
                    if (LA6_2 == 53) {
                        alt6 = 1;
                    }
                    else {
                        if (LA6_2 != 56) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 6, 3, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt6 = 2;
                    }
                    break;
                }
                case 4: {
                    final int LA6_3 = this.input.LA(3);
                    if (LA6_3 == 53) {
                        alt6 = 1;
                    }
                    else {
                        if (LA6_3 != 56) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 6, 4, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt6 = 2;
                    }
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 6, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt6) {
                case 1: {
                    this.match((IntStream)this.input, 79, ModerationCommandParser.FOLLOW_KICK_in_kick_cmd2452);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_kick_cmd2456);
                    name = this.character_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_kick_cmd2458);
                    cmd = new KickCommand((name != null) ? name.pattern : null);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 79, ModerationCommandParser.FOLLOW_KICK_in_kick_cmd2465);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_kick_cmd2469);
                    name = this.character_pattern();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_kick_cmd2473);
                    reason = this.message();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_kick_cmd2475);
                    cmd = new KickCommand((name != null) ? name.pattern : null, (reason != null) ? reason.txt : null);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ban_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token clientId = null;
        Token minutes = null;
        try {
            int alt7 = 3;
            final int LA7_0 = this.input.LA(1);
            if (LA7_0 == 17) {
                final int LA7_ = this.input.LA(2);
                if (LA7_ != 87) {
                    final NoViableAltException nvae = new NoViableAltException("", 7, 1, (IntStream)this.input);
                    throw nvae;
                }
                final int LA7_2 = this.input.LA(3);
                if (LA7_2 == 87) {
                    alt7 = 1;
                }
                else {
                    if (LA7_2 != 53) {
                        final NoViableAltException nvae2 = new NoViableAltException("", 7, 3, (IntStream)this.input);
                        throw nvae2;
                    }
                    alt7 = 2;
                }
            }
            else {
                if (LA7_0 != 162) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 7, 0, (IntStream)this.input);
                    throw nvae3;
                }
                alt7 = 3;
            }
            switch (alt7) {
                case 1: {
                    this.match((IntStream)this.input, 17, ModerationCommandParser.FOLLOW_BAN_in_ban_cmd2492);
                    clientId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_ban_cmd2496);
                    minutes = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_ban_cmd2500);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ban_cmd2502);
                    cmd = new BanCommand(Long.parseLong((clientId != null) ? clientId.getText() : null), true, Integer.parseInt((minutes != null) ? minutes.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 17, ModerationCommandParser.FOLLOW_BAN_in_ban_cmd2509);
                    clientId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_ban_cmd2513);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ban_cmd2515);
                    cmd = new BanCommand(Long.parseLong((clientId != null) ? clientId.getText() : null), true, 5);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 162, ModerationCommandParser.FOLLOW_UNBAN_in_ban_cmd2522);
                    clientId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_ban_cmd2526);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ban_cmd2528);
                    cmd = new BanCommand(Long.parseLong((clientId != null) ? clientId.getText() : null), false, 0);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ban_request_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token clientId = null;
        Token characterId = null;
        message_return reason = null;
        try {
            this.match((IntStream)this.input, 18, ModerationCommandParser.FOLLOW_BAN_REQUEST_in_ban_request_cmd2547);
            clientId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_ban_request_cmd2551);
            characterId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_ban_request_cmd2555);
            this.pushFollow(ModerationCommandParser.FOLLOW_message_in_ban_request_cmd2559);
            reason = this.message();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ban_request_cmd2561);
            cmd = new BanRequestCommand(Long.parseLong((clientId != null) ? clientId.getText() : null), Long.parseLong((characterId != null) ? characterId.getText() : null), (reason != null) ? this.input.toString(reason.start, reason.stop) : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ghostcheck_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 66, ModerationCommandParser.FOLLOW_GHOSTCHECK_in_ghostcheck_cmd2577);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ghostcheck_cmd2579);
            cmd = new GhostCheckCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand identphase_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            this.match((IntStream)this.input, 75, ModerationCommandParser.FOLLOW_IDENT_PHASE_in_identphase_cmd2594);
            id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_identphase_cmd2598);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_identphase_cmd2600);
            cmd = new IdentPhaseCommand(Long.parseLong((id != null) ? id.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand shutdown_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token delay = null;
        try {
            int alt8 = 3;
            final int LA8_0 = this.input.LA(1);
            if (LA8_0 != 141) {
                final NoViableAltException nvae = new NoViableAltException("", 8, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 87: {
                    alt8 = 1;
                    break;
                }
                case 53: {
                    alt8 = 2;
                    break;
                }
                case 150: {
                    alt8 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 8, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt8) {
                case 1: {
                    this.match((IntStream)this.input, 141, ModerationCommandParser.FOLLOW_SHUTDOWN_in_shutdown_cmd2615);
                    delay = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_shutdown_cmd2619);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_shutdown_cmd2621);
                    cmd = new ShutdownCommand(Integer.parseInt((delay != null) ? delay.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 141, ModerationCommandParser.FOLLOW_SHUTDOWN_in_shutdown_cmd2629);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_shutdown_cmd2631);
                    cmd = new ShutdownCommand(-1);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 141, ModerationCommandParser.FOLLOW_SHUTDOWN_in_shutdown_cmd2639);
                    this.match((IntStream)this.input, 150, ModerationCommandParser.FOLLOW_STOP_in_shutdown_cmd2641);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_shutdown_cmd2643);
                    cmd = new ShutdownCommand(0);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand sysmsg_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        message_return msg = null;
        character_pattern_return name = null;
        int radius = 0;
        try {
            int alt9 = 3;
            final int LA9_0 = this.input.LA(1);
            if (LA9_0 != 153) {
                final NoViableAltException nvae = new NoViableAltException("", 9, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 56: {
                    final int LA9_ = this.input.LA(3);
                    if (LA9_ == 53) {
                        alt9 = 1;
                    }
                    else {
                        if (LA9_ != 56) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 9, 2, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt9 = 2;
                    }
                    break;
                }
                case 4:
                case 27: {
                    alt9 = 2;
                    break;
                }
                case 100: {
                    alt9 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 9, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt9) {
                case 1: {
                    this.match((IntStream)this.input, 153, ModerationCommandParser.FOLLOW_SYSMSG_in_sysmsg_cmd2663);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_sysmsg_cmd2667);
                    msg = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_sysmsg_cmd2669);
                    cmd = new SysmsgCommand((msg != null) ? msg.txt : null);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 153, ModerationCommandParser.FOLLOW_SYSMSG_in_sysmsg_cmd2676);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_sysmsg_cmd2680);
                    name = this.character_pattern();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_sysmsg_cmd2684);
                    msg = this.message();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    cmd = new SysmsgCommand((name != null) ? name.pattern : null, (msg != null) ? msg.txt : null);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 153, ModerationCommandParser.FOLLOW_SYSMSG_in_sysmsg_cmd2691);
                    this.pushFollow(ModerationCommandParser.FOLLOW_proximity_pattern_in_sysmsg_cmd2695);
                    radius = this.proximity_pattern();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_sysmsg_cmd2699);
                    msg = this.message();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_sysmsg_cmd2701);
                    cmd = new ProximitySysmsgCommand(radius, (msg != null) ? msg.txt : null);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand msgall_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        message_return msg = null;
        try {
            this.match((IntStream)this.input, 83, ModerationCommandParser.FOLLOW_MSGALL_in_msgall_cmd2719);
            this.pushFollow(ModerationCommandParser.FOLLOW_message_in_msgall_cmd2723);
            msg = this.message();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_msgall_cmd2725);
            cmd = new MsgAllCommand((msg != null) ? msg.txt : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand symbiot_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        Token qty = null;
        message_return name = null;
        try {
            int alt10 = 7;
            final int LA10_0 = this.input.LA(1);
            if (LA10_0 != 152) {
                final NoViableAltException nvae = new NoViableAltException("", 10, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 180: {
                    final int LA10_ = this.input.LA(3);
                    if (LA10_ == 87) {
                        final int LA10_2 = this.input.LA(4);
                        if (LA10_2 == 53) {
                            alt10 = 1;
                        }
                        else {
                            if (LA10_2 != 87) {
                                final NoViableAltException nvae2 = new NoViableAltException("", 10, 8, (IntStream)this.input);
                                throw nvae2;
                            }
                            alt10 = 2;
                        }
                        break;
                    }
                    final NoViableAltException nvae3 = new NoViableAltException("", 10, 2, (IntStream)this.input);
                    throw nvae3;
                }
                case 233: {
                    alt10 = 3;
                    break;
                }
                case 228: {
                    alt10 = 4;
                    break;
                }
                case 212: {
                    alt10 = 5;
                    break;
                }
                case 210: {
                    alt10 = 6;
                    break;
                }
                case 316:
                case 317: {
                    alt10 = 7;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 10, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt10) {
                case 1: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2741);
                    this.match((IntStream)this.input, 180, ModerationCommandParser.FOLLOW_180_in_symbiot_cmd2743);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_symbiot_cmd2747);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_symbiot_cmd2749);
                    cmd = new SymbiotCommand((byte)0, new String[] { (id != null) ? id.getText() : null });
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2757);
                    this.match((IntStream)this.input, 180, ModerationCommandParser.FOLLOW_180_in_symbiot_cmd2759);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_symbiot_cmd2763);
                    qty = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_symbiot_cmd2767);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_symbiot_cmd2769);
                    cmd = new SymbiotCommand((byte)0, new String[] { (id != null) ? id.getText() : null, (qty != null) ? qty.getText() : null });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2777);
                    this.match((IntStream)this.input, 233, ModerationCommandParser.FOLLOW_233_in_symbiot_cmd2779);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_symbiot_cmd2783);
                    cmd = new SymbiotCommand((byte)1, new String[] { (id != null) ? id.getText() : null });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2789);
                    this.match((IntStream)this.input, 228, ModerationCommandParser.FOLLOW_228_in_symbiot_cmd2791);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_symbiot_cmd2795);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_symbiot_cmd2799);
                    name = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    cmd = new SymbiotCommand((byte)2, new String[] { (id != null) ? id.getText() : null, (name != null) ? name.txt : null });
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2806);
                    this.match((IntStream)this.input, 212, ModerationCommandParser.FOLLOW_212_in_symbiot_cmd2808);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_symbiot_cmd2812);
                    cmd = new SymbiotCommand((byte)3, new String[] { (id != null) ? id.getText() : null });
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2818);
                    this.match((IntStream)this.input, 210, ModerationCommandParser.FOLLOW_210_in_symbiot_cmd2820);
                    cmd = new SymbiotCommand((byte)4, new String[0]);
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 152, ModerationCommandParser.FOLLOW_SYMBIOT_in_symbiot_cmd2827);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        cmd = new SymbiotCommand((byte)5, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand nation_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        Token amount = null;
        Token rank = null;
        Token nationId = null;
        Token alignement = null;
        try {
            int alt11 = 17;
            final int LA11_0 = this.input.LA(1);
            if (LA11_0 != 86) {
                final NoViableAltException nvae = new NoViableAltException("", 11, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt11 = 1;
                    break;
                }
                case 126: {
                    alt11 = 2;
                    break;
                }
                case 167: {
                    switch (this.input.LA(3)) {
                        case 76: {
                            final int LA11_ = this.input.LA(4);
                            if (LA11_ == 53) {
                                alt11 = 3;
                            }
                            else {
                                if (LA11_ != 87) {
                                    final NoViableAltException nvae2 = new NoViableAltException("", 11, 14, (IntStream)this.input);
                                    throw nvae2;
                                }
                                alt11 = 4;
                            }
                            break;
                        }
                        case 145: {
                            final int LA11_2 = this.input.LA(4);
                            if (LA11_2 == 87) {
                                alt11 = 5;
                            }
                            else {
                                if (LA11_2 != 53) {
                                    final NoViableAltException nvae2 = new NoViableAltException("", 11, 15, (IntStream)this.input);
                                    throw nvae2;
                                }
                                alt11 = 6;
                            }
                            break;
                        }
                        case 52: {
                            final int LA11_3 = this.input.LA(4);
                            if (LA11_3 == 87) {
                                alt11 = 7;
                            }
                            else {
                                if (LA11_3 != 53) {
                                    final NoViableAltException nvae2 = new NoViableAltException("", 11, 16, (IntStream)this.input);
                                    throw nvae2;
                                }
                                alt11 = 8;
                            }
                            break;
                        }
                        default: {
                            final NoViableAltException nvae = new NoViableAltException("", 11, 4, (IntStream)this.input);
                            throw nvae;
                        }
                    }
                    break;
                }
                case 30: {
                    alt11 = 9;
                    break;
                }
                case 132: {
                    alt11 = 10;
                    break;
                }
                case 14: {
                    alt11 = 11;
                    break;
                }
                case 313:
                case 314: {
                    alt11 = 12;
                    break;
                }
                case 336:
                case 337: {
                    alt11 = 13;
                    break;
                }
                case 334:
                case 335: {
                    alt11 = 14;
                    break;
                }
                case 401:
                case 408: {
                    alt11 = 15;
                    break;
                }
                case 316:
                case 317: {
                    alt11 = 16;
                    break;
                }
                case 390: {
                    alt11 = 17;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 11, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt11) {
                case 1: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2851);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2853);
                    cmd = new NationCommand((byte)2);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2860);
                    this.match((IntStream)this.input, 126, ModerationCommandParser.FOLLOW_SET_in_nation_cmd2862);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd2866);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2868);
                    cmd = new NationCommand(Integer.parseInt((id != null) ? id.getText() : null), (byte)1);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2875);
                    this.match((IntStream)this.input, 167, ModerationCommandParser.FOLLOW_VOTE_in_nation_cmd2877);
                    this.match((IntStream)this.input, 76, ModerationCommandParser.FOLLOW_INFO_in_nation_cmd2879);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2881);
                    cmd = new NationCommand(-1, (byte)5);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2888);
                    this.match((IntStream)this.input, 167, ModerationCommandParser.FOLLOW_VOTE_in_nation_cmd2890);
                    this.match((IntStream)this.input, 76, ModerationCommandParser.FOLLOW_INFO_in_nation_cmd2892);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd2896);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2898);
                    cmd = new NationCommand(Integer.parseInt((id != null) ? id.getText() : null), (byte)5);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2905);
                    this.match((IntStream)this.input, 167, ModerationCommandParser.FOLLOW_VOTE_in_nation_cmd2907);
                    this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_nation_cmd2909);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd2913);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2915);
                    cmd = new NationCommand(Integer.parseInt((id != null) ? id.getText() : null), (byte)3);
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2922);
                    this.match((IntStream)this.input, 167, ModerationCommandParser.FOLLOW_VOTE_in_nation_cmd2924);
                    this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_nation_cmd2926);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2928);
                    cmd = new NationCommand(-1, (byte)3);
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2935);
                    this.match((IntStream)this.input, 167, ModerationCommandParser.FOLLOW_VOTE_in_nation_cmd2937);
                    this.match((IntStream)this.input, 52, ModerationCommandParser.FOLLOW_END_in_nation_cmd2939);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd2943);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2945);
                    cmd = new NationCommand(Integer.parseInt((id != null) ? id.getText() : null), (byte)4);
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2952);
                    this.match((IntStream)this.input, 167, ModerationCommandParser.FOLLOW_VOTE_in_nation_cmd2954);
                    this.match((IntStream)this.input, 52, ModerationCommandParser.FOLLOW_END_in_nation_cmd2956);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2958);
                    cmd = new NationCommand(-1, (byte)4);
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2965);
                    this.match((IntStream)this.input, 30, ModerationCommandParser.FOLLOW_CITIZEN_POINTS_in_nation_cmd2967);
                    amount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd2971);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2973);
                    cmd = new NationCommand(-1, (byte)9, Integer.parseInt((amount != null) ? amount.getText() : null));
                    break;
                }
                case 10: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2980);
                    this.match((IntStream)this.input, 132, ModerationCommandParser.FOLLOW_SET_RANK_in_nation_cmd2982);
                    rank = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd2986);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd2988);
                    cmd = new NationCommand(-1, (byte)16, Integer.parseInt((rank != null) ? rank.getText() : null));
                    break;
                }
                case 11: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd2995);
                    this.match((IntStream)this.input, 14, ModerationCommandParser.FOLLOW_ALIGNMENT_in_nation_cmd2997);
                    nationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd3001);
                    alignement = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd3005);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3007);
                    cmd = new NationCommand(Integer.parseInt((nationId != null) ? nationId.getText() : null), (byte)10, Integer.parseInt((alignement != null) ? alignement.getText() : null));
                    break;
                }
                case 12: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd3013);
                    if (this.input.LA(1) >= 313 && this.input.LA(1) <= 314) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        nationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd3025);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3027);
                        cmd = new NationCommand(-1, (byte)11, Integer.parseInt((nationId != null) ? nationId.getText() : null));
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 13: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd3034);
                    if (this.input.LA(1) >= 336 && this.input.LA(1) <= 337) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        nationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd3047);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3049);
                        cmd = new NationCommand(Integer.parseInt((nationId != null) ? nationId.getText() : null), (byte)12);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 14: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd3057);
                    if (this.input.LA(1) >= 334 && this.input.LA(1) <= 335) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        nationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_nation_cmd3070);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3072);
                        cmd = new NationCommand(Integer.parseInt((nationId != null) ? nationId.getText() : null), (byte)13);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 15: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd3080);
                    if (this.input.LA(1) == 401 || this.input.LA(1) == 408) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3090);
                        cmd = new NationCommand(-1, (byte)14);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 16: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd3097);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3107);
                        cmd = new NationCommand(-1, (byte)15);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 17: {
                    this.match((IntStream)this.input, 86, ModerationCommandParser.FOLLOW_NATION_in_nation_cmd3114);
                    this.match((IntStream)this.input, 390, ModerationCommandParser.FOLLOW_390_in_nation_cmd3117);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_nation_cmd3120);
                    cmd = new NationCommand(-1, (byte)17);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand achievement_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token value = null;
        message_return name = null;
        try {
            int alt14 = 6;
            switch (this.input.LA(1)) {
                case 5: {
                    switch (this.input.LA(2)) {
                        case 113: {
                            final int LA14_4 = this.input.LA(3);
                            if (LA14_4 == 280) {
                                alt14 = 2;
                            }
                            else {
                                if (LA14_4 != 87) {
                                    final NoViableAltException nvae = new NoViableAltException("", 14, 4, (IntStream)this.input);
                                    throw nvae;
                                }
                                alt14 = 1;
                            }
                            break;
                        }
                        case 33: {
                            alt14 = 3;
                            break;
                        }
                        case 34: {
                            alt14 = 4;
                            break;
                        }
                        case 165: {
                            final int LA14_2 = this.input.LA(3);
                            if (LA14_2 == 64) {
                                alt14 = 5;
                            }
                            else {
                                if (LA14_2 != 126) {
                                    final NoViableAltException nvae = new NoViableAltException("", 14, 7, (IntStream)this.input);
                                    throw nvae;
                                }
                                alt14 = 6;
                            }
                            break;
                        }
                        default: {
                            final NoViableAltException nvae2 = new NoViableAltException("", 14, 1, (IntStream)this.input);
                            throw nvae2;
                        }
                    }
                    break;
                }
                case 283: {
                    alt14 = 1;
                    break;
                }
                case 284: {
                    alt14 = 2;
                    break;
                }
                default: {
                    final NoViableAltException nvae2 = new NoViableAltException("", 14, 0, (IntStream)this.input);
                    throw nvae2;
                }
            }
            switch (alt14) {
                case 1: {
                    int alt2 = 2;
                    final int LA12_0 = this.input.LA(1);
                    if (LA12_0 == 5) {
                        alt2 = 1;
                    }
                    else {
                        if (LA12_0 != 283) {
                            final NoViableAltException nvae3 = new NoViableAltException("", 12, 0, (IntStream)this.input);
                            throw nvae3;
                        }
                        alt2 = 2;
                    }
                    switch (alt2) {
                        case 1: {
                            this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_cmd3139);
                            this.match((IntStream)this.input, 113, ModerationCommandParser.FOLLOW_RESET_in_achievement_cmd3141);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 283, ModerationCommandParser.FOLLOW_283_in_achievement_cmd3146);
                            break;
                        }
                    }
                    value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_cmd3151);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_cmd3153);
                    cmd = new AchievementCommand((byte)4, Integer.parseInt((value != null) ? value.getText() : null));
                    break;
                }
                case 2: {
                    int alt3 = 2;
                    final int LA13_0 = this.input.LA(1);
                    if (LA13_0 == 5) {
                        alt3 = 1;
                    }
                    else {
                        if (LA13_0 != 284) {
                            final NoViableAltException nvae3 = new NoViableAltException("", 13, 0, (IntStream)this.input);
                            throw nvae3;
                        }
                        alt3 = 2;
                    }
                    switch (alt3) {
                        case 1: {
                            this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_cmd3161);
                            this.match((IntStream)this.input, 113, ModerationCommandParser.FOLLOW_RESET_in_achievement_cmd3163);
                            this.match((IntStream)this.input, 280, ModerationCommandParser.FOLLOW_280_in_achievement_cmd3165);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 284, ModerationCommandParser.FOLLOW_284_in_achievement_cmd3170);
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_cmd3173);
                    cmd = new AchievementCommand((byte)7);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_cmd3179);
                    this.match((IntStream)this.input, 33, ModerationCommandParser.FOLLOW_COMPLETE_in_achievement_cmd3181);
                    value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_cmd3185);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_cmd3187);
                    cmd = new AchievementCommand((byte)3, Integer.parseInt((value != null) ? value.getText() : null));
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_cmd3193);
                    this.match((IntStream)this.input, 34, ModerationCommandParser.FOLLOW_COMPLETE_OBJECTIVE_in_achievement_cmd3195);
                    value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_cmd3199);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_cmd3201);
                    cmd = new AchievementCommand((byte)5, Integer.parseInt((value != null) ? value.getText() : null));
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_cmd3207);
                    this.match((IntStream)this.input, 165, ModerationCommandParser.FOLLOW_VAR_in_achievement_cmd3209);
                    this.match((IntStream)this.input, 64, ModerationCommandParser.FOLLOW_GET_in_achievement_cmd3211);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_achievement_cmd3215);
                    name = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_cmd3217);
                    cmd = new AchievementCommand((byte)1, (name != null) ? name.txt : null);
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_cmd3223);
                    this.match((IntStream)this.input, 165, ModerationCommandParser.FOLLOW_VAR_in_achievement_cmd3225);
                    this.match((IntStream)this.input, 126, ModerationCommandParser.FOLLOW_SET_in_achievement_cmd3227);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_achievement_cmd3231);
                    name = this.message();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_cmd3235);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_cmd3237);
                    cmd = new AchievementCommand((byte)2, (name != null) ? name.txt : null, Integer.parseInt((value != null) ? value.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand achievement_date_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token value = null;
        Token seconds = null;
        Token minutes = null;
        Token hours = null;
        Token day = null;
        Token month = null;
        Token year = null;
        try {
            this.match((IntStream)this.input, 5, ModerationCommandParser.FOLLOW_ACHIEVEMENT_in_achievement_date_cmd3256);
            this.match((IntStream)this.input, 146, ModerationCommandParser.FOLLOW_START_DATE_in_achievement_date_cmd3258);
            value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3262);
            seconds = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3266);
            minutes = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3270);
            hours = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3274);
            day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3278);
            month = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3282);
            year = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_achievement_date_cmd3286);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_achievement_date_cmd3288);
            cmd = new AchievementDateCommand((byte)6, Integer.parseInt((value != null) ? value.getText() : null), Integer.parseInt((seconds != null) ? seconds.getText() : null), Integer.parseInt((minutes != null) ? minutes.getText() : null), Integer.parseInt((hours != null) ? hours.getText() : null), Integer.parseInt((day != null) ? day.getText() : null), Integer.parseInt((month != null) ? month.getText() : null), Integer.parseInt((year != null) ? year.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand zone_buff_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            int alt15 = 3;
            final int LA15_0 = this.input.LA(1);
            if (LA15_0 != 172) {
                final NoViableAltException nvae = new NoViableAltException("", 15, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt15 = 1;
                    break;
                }
                case 6: {
                    alt15 = 2;
                    break;
                }
                case 110: {
                    alt15 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 15, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt15) {
                case 1: {
                    this.match((IntStream)this.input, 172, ModerationCommandParser.FOLLOW_ZONE_BUFF_in_zone_buff_cmd3306);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_zone_buff_cmd3308);
                    cmd = new ZoneBuffCommand((byte)1);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 172, ModerationCommandParser.FOLLOW_ZONE_BUFF_in_zone_buff_cmd3315);
                    this.match((IntStream)this.input, 6, ModerationCommandParser.FOLLOW_ADD_in_zone_buff_cmd3317);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_zone_buff_cmd3321);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_zone_buff_cmd3323);
                    cmd = new ZoneBuffCommand((byte)2, Integer.parseInt((id != null) ? id.getText() : null));
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 172, ModerationCommandParser.FOLLOW_ZONE_BUFF_in_zone_buff_cmd3330);
                    this.match((IntStream)this.input, 110, ModerationCommandParser.FOLLOW_REMOVE_in_zone_buff_cmd3332);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_zone_buff_cmd3336);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_zone_buff_cmd3338);
                    cmd = new ZoneBuffCommand((byte)3, Integer.parseInt((id != null) ? id.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand create_group_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idCharacter = null;
        Token groupNumber = null;
        Token idTemplate = null;
        Token userGroupId = null;
        try {
            int alt16 = 9;
            final int LA16_0 = this.input.LA(1);
            if (LA16_0 != 38) {
                final NoViableAltException nvae = new NoViableAltException("", 16, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 87: {
                    final int LA16_ = this.input.LA(3);
                    if (LA16_ == 53) {
                        alt16 = 1;
                    }
                    else {
                        if (LA16_ != 87) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 16, 2, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt16 = 2;
                    }
                    break;
                }
                case 255: {
                    final int LA16_2 = this.input.LA(3);
                    if (LA16_2 == 87) {
                        final int LA16_3 = this.input.LA(4);
                        if (LA16_3 == 53) {
                            alt16 = 3;
                        }
                        else {
                            if (LA16_3 != 87) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 16, 9, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt16 = 4;
                        }
                        break;
                    }
                    final NoViableAltException nvae2 = new NoViableAltException("", 16, 3, (IntStream)this.input);
                    throw nvae2;
                }
                case 262: {
                    final int LA16_4 = this.input.LA(3);
                    if (LA16_4 == 87) {
                        final int LA16_5 = this.input.LA(4);
                        if (LA16_5 == 53) {
                            alt16 = 5;
                        }
                        else {
                            if (LA16_5 != 87) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 16, 10, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt16 = 6;
                        }
                        break;
                    }
                    final NoViableAltException nvae2 = new NoViableAltException("", 16, 4, (IntStream)this.input);
                    throw nvae2;
                }
                case 257: {
                    final int LA16_6 = this.input.LA(3);
                    if (LA16_6 == 87) {
                        alt16 = 7;
                    }
                    else {
                        if (LA16_6 != 296) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 16, 5, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt16 = 8;
                    }
                    break;
                }
                case 316:
                case 317: {
                    alt16 = 9;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 16, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt16) {
                case 1: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3357);
                    idCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3361);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3363);
                    cmd = new CreateGroupCommand((byte)1, Integer.parseInt((idCharacter != null) ? idCharacter.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3370);
                    idCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3374);
                    groupNumber = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3378);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3380);
                    cmd = new CreateGroupCommand((byte)1, Integer.parseInt((idCharacter != null) ? idCharacter.getText() : null), Integer.parseInt((groupNumber != null) ? groupNumber.getText() : null));
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3387);
                    this.match((IntStream)this.input, 255, ModerationCommandParser.FOLLOW_255_in_create_group_cmd3389);
                    idTemplate = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3393);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3395);
                    cmd = new CreateGroupCommand((byte)2, Integer.parseInt((idTemplate != null) ? idTemplate.getText() : null));
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3402);
                    this.match((IntStream)this.input, 255, ModerationCommandParser.FOLLOW_255_in_create_group_cmd3404);
                    idTemplate = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3408);
                    groupNumber = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3412);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3414);
                    cmd = new CreateGroupCommand((byte)2, Integer.parseInt((idTemplate != null) ? idTemplate.getText() : null), Integer.parseInt((groupNumber != null) ? groupNumber.getText() : null));
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3422);
                    this.match((IntStream)this.input, 262, ModerationCommandParser.FOLLOW_262_in_create_group_cmd3424);
                    idTemplate = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3428);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3430);
                    cmd = new CreateGroupCommand((byte)3, Integer.parseInt((idTemplate != null) ? idTemplate.getText() : null));
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3437);
                    this.match((IntStream)this.input, 262, ModerationCommandParser.FOLLOW_262_in_create_group_cmd3439);
                    idTemplate = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3443);
                    groupNumber = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3447);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3449);
                    cmd = new CreateGroupCommand((byte)3, Integer.parseInt((idTemplate != null) ? idTemplate.getText() : null), Integer.parseInt((groupNumber != null) ? groupNumber.getText() : null));
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3458);
                    this.match((IntStream)this.input, 257, ModerationCommandParser.FOLLOW_257_in_create_group_cmd3460);
                    userGroupId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_group_cmd3464);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3466);
                    cmd = new CreateGroupCommand((byte)4, Integer.parseInt((userGroupId != null) ? userGroupId.getText() : null));
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3473);
                    this.match((IntStream)this.input, 257, ModerationCommandParser.FOLLOW_257_in_create_group_cmd3475);
                    this.match((IntStream)this.input, 296, ModerationCommandParser.FOLLOW_296_in_create_group_cmd3477);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3479);
                    cmd = new CreateGroupCommand((byte)4, 15496);
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 38, ModerationCommandParser.FOLLOW_CREATE_GROUP_in_create_group_cmd3486);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_group_cmd3496);
                        cmd = new CreateGroupCommand((byte)0, 0);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand create_full_group_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        List<Integer> breedIds = null;
        try {
            this.match((IntStream)this.input, 37, ModerationCommandParser.FOLLOW_CREATE_FULL_GROUP_in_create_full_group_cmd3517);
            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_create_full_group_cmd3523);
            breedIds = this.id_list_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_full_group_cmd3525);
            cmd = new CreateFullGroupCommand(breedIds);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand destroy_monsters_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token breedId = null;
        Token monsterCount = null;
        try {
            int alt17 = 2;
            final int LA17_0 = this.input.LA(1);
            if (LA17_0 != 44) {
                final NoViableAltException nvae = new NoViableAltException("", 17, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA17_ = this.input.LA(2);
            if (LA17_ != 87) {
                final NoViableAltException nvae2 = new NoViableAltException("", 17, 1, (IntStream)this.input);
                throw nvae2;
            }
            final int LA17_2 = this.input.LA(3);
            if (LA17_2 == 53) {
                alt17 = 1;
            }
            else {
                if (LA17_2 != 87) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 17, 2, (IntStream)this.input);
                    throw nvae3;
                }
                alt17 = 2;
            }
            switch (alt17) {
                case 1: {
                    this.match((IntStream)this.input, 44, ModerationCommandParser.FOLLOW_DESTROY_MONSTERS_in_destroy_monsters_cmd3547);
                    breedId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_destroy_monsters_cmd3551);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_destroy_monsters_cmd3553);
                    cmd = new DestroyMonstersCommand(Integer.parseInt((breedId != null) ? breedId.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 44, ModerationCommandParser.FOLLOW_DESTROY_MONSTERS_in_destroy_monsters_cmd3560);
                    breedId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_destroy_monsters_cmd3564);
                    monsterCount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_destroy_monsters_cmd3568);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_destroy_monsters_cmd3570);
                    cmd = new DestroyMonstersCommand(Integer.parseInt((breedId != null) ? breedId.getText() : null), Integer.parseInt((monsterCount != null) ? monsterCount.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand plant_resources_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token resourceId = null;
        Token resourceCount = null;
        try {
            int alt18 = 2;
            final int LA18_0 = this.input.LA(1);
            if (LA18_0 != 95) {
                final NoViableAltException nvae = new NoViableAltException("", 18, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA18_ = this.input.LA(2);
            if (LA18_ != 87) {
                final NoViableAltException nvae2 = new NoViableAltException("", 18, 1, (IntStream)this.input);
                throw nvae2;
            }
            final int LA18_2 = this.input.LA(3);
            if (LA18_2 == 53) {
                alt18 = 1;
            }
            else {
                if (LA18_2 != 87) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 18, 2, (IntStream)this.input);
                    throw nvae3;
                }
                alt18 = 2;
            }
            switch (alt18) {
                case 1: {
                    this.match((IntStream)this.input, 95, ModerationCommandParser.FOLLOW_PLANT_RESOURCES_in_plant_resources_cmd3590);
                    resourceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_plant_resources_cmd3594);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_plant_resources_cmd3596);
                    cmd = new PlantResourcesCommand(Integer.parseInt((resourceId != null) ? resourceId.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 95, ModerationCommandParser.FOLLOW_PLANT_RESOURCES_in_plant_resources_cmd3604);
                    resourceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_plant_resources_cmd3608);
                    resourceCount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_plant_resources_cmd3612);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_plant_resources_cmd3614);
                    cmd = new PlantResourcesCommand(Integer.parseInt((resourceId != null) ? resourceId.getText() : null), Integer.parseInt((resourceCount != null) ? resourceCount.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand destroy_resources_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token resourceId = null;
        Token resourceCount = null;
        try {
            int alt19 = 2;
            final int LA19_0 = this.input.LA(1);
            if (LA19_0 != 45) {
                final NoViableAltException nvae = new NoViableAltException("", 19, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA19_ = this.input.LA(2);
            if (LA19_ != 87) {
                final NoViableAltException nvae2 = new NoViableAltException("", 19, 1, (IntStream)this.input);
                throw nvae2;
            }
            final int LA19_2 = this.input.LA(3);
            if (LA19_2 == 53) {
                alt19 = 1;
            }
            else {
                if (LA19_2 != 87) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 19, 2, (IntStream)this.input);
                    throw nvae3;
                }
                alt19 = 2;
            }
            switch (alt19) {
                case 1: {
                    this.match((IntStream)this.input, 45, ModerationCommandParser.FOLLOW_DESTROY_RESOURCES_in_destroy_resources_cmd3633);
                    resourceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_destroy_resources_cmd3637);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_destroy_resources_cmd3639);
                    cmd = new DestroyResourcesCommand(Integer.parseInt((resourceId != null) ? resourceId.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 45, ModerationCommandParser.FOLLOW_DESTROY_RESOURCES_in_destroy_resources_cmd3647);
                    resourceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_destroy_resources_cmd3651);
                    resourceCount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_destroy_resources_cmd3655);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_destroy_resources_cmd3657);
                    cmd = new DestroyResourcesCommand(Integer.parseInt((resourceId != null) ? resourceId.getText() : null), Integer.parseInt((resourceCount != null) ? resourceCount.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand add_to_group_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idGroup = null;
        Token idCharacter = null;
        Token nbCharacter = null;
        try {
            this.match((IntStream)this.input, 11, ModerationCommandParser.FOLLOW_ADD_TO_GROUP_in_add_to_group_cmd3674);
            idGroup = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_to_group_cmd3678);
            idCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_to_group_cmd3682);
            nbCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_to_group_cmd3686);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_add_to_group_cmd3688);
            cmd = new AddToGroupCommand(Long.parseLong((idGroup != null) ? idGroup.getText() : null), Integer.parseInt((idCharacter != null) ? idCharacter.getText() : null), Integer.parseInt((nbCharacter != null) ? nbCharacter.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand play_animation_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idCharacter = null;
        message_return linkAnimation = null;
        try {
            this.match((IntStream)this.input, 96, ModerationCommandParser.FOLLOW_PLAY_ANIMATION_in_play_animation_cmd3703);
            idCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_animation_cmd3707);
            this.pushFollow(ModerationCommandParser.FOLLOW_message_in_play_animation_cmd3711);
            linkAnimation = this.message();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_play_animation_cmd3713);
            cmd = new PlayAnimationCommand(Long.parseLong((idCharacter != null) ? idCharacter.getText() : null), (linkAnimation != null) ? linkAnimation.txt : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand play_aps_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idCharacter = null;
        Token idAps = null;
        Token duree = null;
        Token follow = null;
        try {
            int alt20 = 2;
            final int LA20_0 = this.input.LA(1);
            if (LA20_0 != 97) {
                final NoViableAltException nvae = new NoViableAltException("", 20, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA20_ = this.input.LA(2);
            if (LA20_ != 87) {
                final NoViableAltException nvae2 = new NoViableAltException("", 20, 1, (IntStream)this.input);
                throw nvae2;
            }
            final int LA20_2 = this.input.LA(3);
            if (LA20_2 != 87) {
                final NoViableAltException nvae3 = new NoViableAltException("", 20, 2, (IntStream)this.input);
                throw nvae3;
            }
            final int LA20_3 = this.input.LA(4);
            if (LA20_3 != 87) {
                final NoViableAltException nvae4 = new NoViableAltException("", 20, 3, (IntStream)this.input);
                throw nvae4;
            }
            final int LA20_4 = this.input.LA(5);
            if (LA20_4 == 20) {
                alt20 = 1;
            }
            else {
                if (LA20_4 != 53) {
                    final NoViableAltException nvae5 = new NoViableAltException("", 20, 4, (IntStream)this.input);
                    throw nvae5;
                }
                alt20 = 2;
            }
            switch (alt20) {
                case 1: {
                    this.match((IntStream)this.input, 97, ModerationCommandParser.FOLLOW_PLAY_APS_in_play_aps_cmd3728);
                    idCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_aps_cmd3732);
                    idAps = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_aps_cmd3736);
                    duree = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_aps_cmd3740);
                    follow = (Token)this.match((IntStream)this.input, 20, ModerationCommandParser.FOLLOW_BOOLEAN_in_play_aps_cmd3744);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_play_aps_cmd3746);
                    cmd = new PlayApsCommand(Long.parseLong((idCharacter != null) ? idCharacter.getText() : null), Integer.parseInt((idAps != null) ? idAps.getText() : null), Integer.parseInt((duree != null) ? duree.getText() : null), Boolean.parseBoolean((follow != null) ? follow.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 97, ModerationCommandParser.FOLLOW_PLAY_APS_in_play_aps_cmd3753);
                    idCharacter = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_aps_cmd3757);
                    idAps = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_aps_cmd3761);
                    duree = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_play_aps_cmd3765);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_play_aps_cmd3767);
                    cmd = new PlayApsCommand(Long.parseLong((idCharacter != null) ? idCharacter.getText() : null), Integer.parseInt((idAps != null) ? idAps.getText() : null), Integer.parseInt((duree != null) ? duree.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand rights_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 120, ModerationCommandParser.FOLLOW_RIGHTS_in_rights_cmd3784);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_rights_cmd3786);
            cmd = new RightsCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand chaos_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token chaos_id = null;
        Token zoneId = null;
        Token zone_id = null;
        try {
            int alt21 = 3;
            final int LA21_0 = this.input.LA(1);
            if (LA21_0 != 25) {
                final NoViableAltException nvae = new NoViableAltException("", 21, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA21_ = this.input.LA(2);
            if (LA21_ == 145) {
                final int LA21_2 = this.input.LA(3);
                if (LA21_2 != 87) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 21, 2, (IntStream)this.input);
                    throw nvae2;
                }
                final int LA21_3 = this.input.LA(4);
                if (LA21_3 == 53) {
                    alt21 = 1;
                }
                else {
                    if (LA21_3 != 87) {
                        final NoViableAltException nvae3 = new NoViableAltException("", 21, 4, (IntStream)this.input);
                        throw nvae3;
                    }
                    alt21 = 2;
                }
            }
            else {
                if (LA21_ != 150) {
                    final NoViableAltException nvae4 = new NoViableAltException("", 21, 1, (IntStream)this.input);
                    throw nvae4;
                }
                alt21 = 3;
            }
            switch (alt21) {
                case 1: {
                    this.match((IntStream)this.input, 25, ModerationCommandParser.FOLLOW_CHAOS_in_chaos_cmd3801);
                    this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_chaos_cmd3803);
                    chaos_id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_chaos_cmd3807);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_chaos_cmd3809);
                    cmd = new ChaosCommand((byte)1, new String[] { (chaos_id != null) ? chaos_id.getText() : null });
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 25, ModerationCommandParser.FOLLOW_CHAOS_in_chaos_cmd3815);
                    this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_chaos_cmd3817);
                    chaos_id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_chaos_cmd3821);
                    zoneId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_chaos_cmd3825);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_chaos_cmd3827);
                    cmd = new ChaosCommand((byte)1, new String[] { (chaos_id != null) ? chaos_id.getText() : null, (zoneId != null) ? zoneId.getText() : null });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 25, ModerationCommandParser.FOLLOW_CHAOS_in_chaos_cmd3833);
                    this.match((IntStream)this.input, 150, ModerationCommandParser.FOLLOW_STOP_in_chaos_cmd3835);
                    zone_id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_chaos_cmd3839);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_chaos_cmd3841);
                    cmd = new ChaosCommand((byte)2, new String[] { (zone_id != null) ? zone_id.getText() : null });
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand restart_chaos_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token period = null;
        try {
            this.match((IntStream)this.input, 116, ModerationCommandParser.FOLLOW_RESTART_CHAOS_in_restart_chaos_cmd3858);
            int alt22 = 2;
            final int LA22_0 = this.input.LA(1);
            if (LA22_0 == 87) {
                alt22 = 1;
            }
            switch (alt22) {
                case 1: {
                    period = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_restart_chaos_cmd3862);
                    break;
                }
            }
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_restart_chaos_cmd3865);
            cmd = new RestartChaosCommand((period != null) ? Integer.parseInt((period != null) ? period.getText() : null) : -1);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand create_item_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token referenceId = null;
        Token qty = null;
        try {
            this.match((IntStream)this.input, 39, ModerationCommandParser.FOLLOW_CREATE_ITEM_in_create_item_cmd3881);
            referenceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_item_cmd3885);
            int alt23 = 2;
            final int LA23_0 = this.input.LA(1);
            if (LA23_0 == 87) {
                alt23 = 1;
            }
            switch (alt23) {
                case 1: {
                    qty = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_item_cmd3889);
                    break;
                }
            }
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_item_cmd3892);
            cmd = new CreateItemCommand(Integer.parseInt((referenceId != null) ? referenceId.getText() : null), (short)((qty != null) ? Short.parseShort((qty != null) ? qty.getText() : null) : 1));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand create_set_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token setId = null;
        try {
            this.match((IntStream)this.input, 40, ModerationCommandParser.FOLLOW_CREATE_SET_in_create_set_cmd3907);
            setId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_create_set_cmd3911);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_create_set_cmd3913);
            cmd = new CreateSetCommand(Short.parseShort((setId != null) ? setId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand delete_item_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token referenceId = null;
        Token qty = null;
        try {
            this.match((IntStream)this.input, 42, ModerationCommandParser.FOLLOW_DELETE_ITEM_in_delete_item_cmd3928);
            referenceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_delete_item_cmd3932);
            int alt24 = 2;
            final int LA24_0 = this.input.LA(1);
            if (LA24_0 == 87) {
                alt24 = 1;
            }
            switch (alt24) {
                case 1: {
                    qty = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_delete_item_cmd3936);
                    break;
                }
            }
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_delete_item_cmd3939);
            cmd = new DeleteItemCommand(Integer.parseInt((referenceId != null) ? referenceId.getText() : null), (short)((qty != null) ? Short.parseShort((qty != null) ? qty.getText() : null) : 1));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand regenerate_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 107, ModerationCommandParser.FOLLOW_REGENERATE_in_regenerate_cmd3954);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_regenerate_cmd3956);
            cmd = new RegenerateCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand regenerate_with_item_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token referenceId = null;
        try {
            this.match((IntStream)this.input, 108, ModerationCommandParser.FOLLOW_REGENERATE_WITH_ITEM_in_regenerate_with_item_cmd3971);
            referenceId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_regenerate_with_item_cmd3975);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_regenerate_with_item_cmd3977);
            cmd = new RegenerateWithItemCommand(Integer.parseInt((referenceId != null) ? referenceId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand god_mode_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            int alt25 = 7;
            final int LA25_0 = this.input.LA(1);
            if (LA25_0 != 68) {
                final NoViableAltException nvae = new NoViableAltException("", 25, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt25 = 1;
                    break;
                }
                case 178:
                case 182: {
                    alt25 = 2;
                    break;
                }
                case 316:
                case 317: {
                    alt25 = 3;
                    break;
                }
                case 214:
                case 217: {
                    alt25 = 4;
                    break;
                }
                case 205:
                case 206: {
                    alt25 = 5;
                    break;
                }
                case 203:
                case 204: {
                    alt25 = 6;
                    break;
                }
                case 200:
                case 201: {
                    alt25 = 7;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 25, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt25) {
                case 1: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd3993);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd3995);
                    cmd = new GodModeCommand();
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd4002);
                    if (this.input.LA(1) == 178 || this.input.LA(1) == 182) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd4012);
                        cmd = new GodModeCommand();
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd4019);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd4029);
                        cmd = new GodModeCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd4036);
                    if (this.input.LA(1) == 214 || this.input.LA(1) == 217) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd4046);
                        cmd = new GodModeCommand(3, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd4054);
                    if (this.input.LA(1) >= 205 && this.input.LA(1) <= 206) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd4064);
                        cmd = new GodModeCommand(2, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd4072);
                    if (this.input.LA(1) >= 203 && this.input.LA(1) <= 204) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd4082);
                        cmd = new GodModeCommand(4, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 68, ModerationCommandParser.FOLLOW_GOD_MODE_in_god_mode_cmd4090);
                    if (this.input.LA(1) >= 200 && this.input.LA(1) <= 201) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_god_mode_cmd4100);
                        cmd = new GodModeCommand(5, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand buff_character_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token level = null;
        Token element = null;
        Token spellId = null;
        try {
            int alt26 = 4;
            final int LA26_0 = this.input.LA(1);
            if (LA26_0 != 22) {
                final NoViableAltException nvae = new NoViableAltException("", 26, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 87: {
                    alt26 = 2;
                    break;
                }
                case 199: {
                    alt26 = 3;
                    break;
                }
                case 238: {
                    alt26 = 4;
                    break;
                }
                case -1: {
                    alt26 = 1;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 26, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt26) {
                case 1: {
                    this.match((IntStream)this.input, 22, ModerationCommandParser.FOLLOW_BUFF_in_buff_character_cmd4118);
                    cmd = new BuffCharacterCommand();
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 22, ModerationCommandParser.FOLLOW_BUFF_in_buff_character_cmd4124);
                    level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_buff_character_cmd4128);
                    cmd = new BuffCharacterCommand(Short.valueOf((level != null) ? level.getText() : null));
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 22, ModerationCommandParser.FOLLOW_BUFF_in_buff_character_cmd4134);
                    this.match((IntStream)this.input, 199, ModerationCommandParser.FOLLOW_199_in_buff_character_cmd4136);
                    level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_buff_character_cmd4140);
                    element = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_buff_character_cmd4144);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_buff_character_cmd4146);
                    cmd = new BuffCharacterCommand(Short.valueOf((level != null) ? level.getText() : null), Byte.valueOf((element != null) ? element.getText() : null));
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 22, ModerationCommandParser.FOLLOW_BUFF_in_buff_character_cmd4152);
                    this.match((IntStream)this.input, 238, ModerationCommandParser.FOLLOW_238_in_buff_character_cmd4154);
                    level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_buff_character_cmd4158);
                    spellId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_buff_character_cmd4162);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_buff_character_cmd4164);
                    cmd = new BuffCharacterCommand(Short.valueOf((level != null) ? level.getText() : null), Integer.valueOf((spellId != null) ? spellId.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand turn_duration_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token duration = null;
        try {
            this.match((IntStream)this.input, 161, ModerationCommandParser.FOLLOW_TURN_DURATION_in_turn_duration_cmd4182);
            duration = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_turn_duration_cmd4186);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_turn_duration_cmd4188);
            cmd = new TurnDurationCommand(Integer.parseInt((duration != null) ? duration.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand pvp_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token amount = null;
        Token value = null;
        try {
            int alt27 = 8;
            final int LA27_0 = this.input.LA(1);
            if (LA27_0 != 101) {
                final NoViableAltException nvae = new NoViableAltException("", 27, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt27 = 1;
                    break;
                }
                case 316:
                case 317: {
                    alt27 = 2;
                    break;
                }
                case 126: {
                    alt27 = 3;
                    break;
                }
                case 6: {
                    alt27 = 4;
                    break;
                }
                case 273:
                case 281: {
                    alt27 = 5;
                    break;
                }
                case 51: {
                    alt27 = 6;
                    break;
                }
                case 104: {
                    alt27 = 7;
                    break;
                }
                case 115: {
                    alt27 = 8;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 27, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt27) {
                case 1: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4207);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4209);
                    cmd = new PvpCommand((byte)0, new String[0]);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4220);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4230);
                        cmd = new PvpCommand((byte)0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4237);
                    this.match((IntStream)this.input, 126, ModerationCommandParser.FOLLOW_SET_in_pvp_cmd4239);
                    amount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_pvp_cmd4243);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4245);
                    cmd = new PvpCommand((byte)1, new String[] { (amount != null) ? amount.getText() : null });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4252);
                    this.match((IntStream)this.input, 6, ModerationCommandParser.FOLLOW_ADD_in_pvp_cmd4254);
                    amount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_pvp_cmd4258);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4260);
                    cmd = new PvpCommand((byte)2, new String[] { (amount != null) ? amount.getText() : null });
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4267);
                    if (this.input.LA(1) == 273 || this.input.LA(1) == 281) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        amount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_pvp_cmd4277);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4279);
                        cmd = new PvpCommand((byte)6, new String[] { (amount != null) ? amount.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4286);
                    this.match((IntStream)this.input, 51, ModerationCommandParser.FOLLOW_ENABLE_in_pvp_cmd4288);
                    value = this.input.LT(1);
                    if (this.input.LA(1) >= 89 && this.input.LA(1) <= 90) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4298);
                        cmd = new PvpCommand((byte)3, new String[] { (value != null) ? value.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4305);
                    this.match((IntStream)this.input, 104, ModerationCommandParser.FOLLOW_RECOMPUTE_POINTS_in_pvp_cmd4307);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4309);
                    cmd = new PvpCommand((byte)4, new String[0]);
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_pvp_cmd4316);
                    this.match((IntStream)this.input, 115, ModerationCommandParser.FOLLOW_RESET_REGRESSION_in_pvp_cmd4318);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pvp_cmd4320);
                    cmd = new PvpCommand((byte)5, new String[0]);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand run_action_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token actionID = null;
        Token scenarioID = null;
        try {
            this.match((IntStream)this.input, 121, ModerationCommandParser.FOLLOW_RUNACTION_in_run_action_cmd4337);
            actionID = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_run_action_cmd4341);
            scenarioID = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_run_action_cmd4345);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_run_action_cmd4347);
            cmd = new RunActionCommand(Integer.parseInt((actionID != null) ? actionID.getText() : null), Integer.parseInt((scenarioID != null) ? scenarioID.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand end_scenario_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token scenarioID = null;
        try {
            this.match((IntStream)this.input, 54, ModerationCommandParser.FOLLOW_ENDSCENARIO_in_end_scenario_cmd4361);
            scenarioID = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_end_scenario_cmd4365);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_end_scenario_cmd4367);
            cmd = new EndScenarioCommand(Integer.parseInt((scenarioID != null) ? scenarioID.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand scenario_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token eventId = null;
        try {
            this.match((IntStream)this.input, 122, ModerationCommandParser.FOLLOW_SCENARIO_COMMAND_in_scenario_cmd4382);
            this.match((IntStream)this.input, 255, ModerationCommandParser.FOLLOW_255_in_scenario_cmd4384);
            eventId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_scenario_cmd4388);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_scenario_cmd4390);
            cmd = new ScenarioCommand(new String[] { (eventId != null) ? eventId.getText() : null });
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand reload_scenarios_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 109, ModerationCommandParser.FOLLOW_RELOADSCENARIOS_in_reload_scenarios_cmd4407);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_reload_scenarios_cmd4409);
            cmd = new ReloadScenariosCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand add_spellxp_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idSpell = null;
        Token quantity = null;
        try {
            this.match((IntStream)this.input, 10, ModerationCommandParser.FOLLOW_ADD_SPELLXP_in_add_spellxp_cmd4423);
            idSpell = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_spellxp_cmd4427);
            quantity = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_spellxp_cmd4431);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_add_spellxp_cmd4433);
            cmd = new AddSpellXPCommand(Integer.parseInt((idSpell != null) ? idSpell.getText() : null), Short.parseShort((quantity != null) ? quantity.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_spelllevel_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idSpell = null;
        Token level = null;
        try {
            this.match((IntStream)this.input, 136, ModerationCommandParser.FOLLOW_SET_SPELLLEVEL_in_set_spelllevel_cmd4448);
            idSpell = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_spelllevel_cmd4452);
            level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_spelllevel_cmd4456);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_spelllevel_cmd4458);
            cmd = new SetSpellLevelCommand(Integer.parseInt((idSpell != null) ? idSpell.getText() : null), Short.parseShort((level != null) ? level.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand add_skillxp_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idSkill = null;
        Token quantity = null;
        try {
            this.match((IntStream)this.input, 9, ModerationCommandParser.FOLLOW_ADD_SKILLXP_in_add_skillxp_cmd4473);
            idSkill = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_skillxp_cmd4477);
            quantity = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_skillxp_cmd4481);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_add_skillxp_cmd4483);
            cmd = new AddSkillXPCommand(Integer.parseInt((idSkill != null) ? idSkill.getText() : null), Long.parseLong((quantity != null) ? quantity.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand add_itemxp_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token quantity = null;
        try {
            this.match((IntStream)this.input, 7, ModerationCommandParser.FOLLOW_ADD_ITEM_XP_in_add_itemxp_cmd4499);
            quantity = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_itemxp_cmd4503);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_add_itemxp_cmd4505);
            cmd = new AddItemXpCommand(Long.parseLong((quantity != null) ? quantity.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand rent_item_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token refItem = null;
        Token rentType = null;
        Token rentDuration = null;
        try {
            this.match((IntStream)this.input, 112, ModerationCommandParser.FOLLOW_RENT_ITEM_CMD_in_rent_item_cmd4520);
            refItem = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_rent_item_cmd4524);
            rentType = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_rent_item_cmd4528);
            rentDuration = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_rent_item_cmd4532);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_rent_item_cmd4534);
            cmd = new RentItemCommand(Integer.parseInt((refItem != null) ? refItem.getText() : null), Integer.parseInt((rentType != null) ? rentType.getText() : null), Long.parseLong((rentDuration != null) ? rentDuration.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand character_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token sexId = null;
        Token visibilityState = null;
        Token dndState = null;
        try {
            int alt28 = 5;
            final int LA28_0 = this.input.LA(1);
            if (LA28_0 != 26) {
                final NoViableAltException nvae = new NoViableAltException("", 28, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 380: {
                    alt28 = 1;
                    break;
                }
                case 316:
                case 317: {
                    alt28 = 2;
                    break;
                }
                case 382: {
                    alt28 = 4;
                    break;
                }
                case 301:
                case 302: {
                    alt28 = 5;
                    break;
                }
                case 76: {
                    alt28 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 28, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt28) {
                case 1: {
                    this.match((IntStream)this.input, 26, ModerationCommandParser.FOLLOW_CHARACTER_CMD_in_character_cmd4549);
                    this.match((IntStream)this.input, 380, ModerationCommandParser.FOLLOW_380_in_character_cmd4551);
                    sexId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_character_cmd4555);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_character_cmd4557);
                    cmd = new CharacterCommand(1, new String[] { (sexId != null) ? sexId.getText() : null });
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 26, ModerationCommandParser.FOLLOW_CHARACTER_CMD_in_character_cmd4564);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_character_cmd4572);
                        cmd = new CharacterCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 26, ModerationCommandParser.FOLLOW_CHARACTER_CMD_in_character_cmd4579);
                    this.match((IntStream)this.input, 76, ModerationCommandParser.FOLLOW_INFO_in_character_cmd4582);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_character_cmd4585);
                    cmd = new CharacterCommand(4, new String[0]);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 26, ModerationCommandParser.FOLLOW_CHARACTER_CMD_in_character_cmd4592);
                    this.match((IntStream)this.input, 382, ModerationCommandParser.FOLLOW_382_in_character_cmd4594);
                    visibilityState = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_character_cmd4598);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_character_cmd4600);
                    cmd = new CharacterCommand(2, new String[] { (visibilityState != null) ? visibilityState.getText() : null });
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 26, ModerationCommandParser.FOLLOW_CHARACTER_CMD_in_character_cmd4607);
                    if (this.input.LA(1) >= 301 && this.input.LA(1) <= 302) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        dndState = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_character_cmd4617);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_character_cmd4619);
                        cmd = new CharacterCommand(3, new String[] { (dndState != null) ? dndState.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand restore_character_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token characterId = null;
        try {
            this.match((IntStream)this.input, 117, ModerationCommandParser.FOLLOW_RESTORE_CHARACTER_CMD_in_restore_character_cmd4636);
            characterId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_restore_character_cmd4640);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_restore_character_cmd4642);
            cmd = new RestoreCharacterCommand(Long.parseLong((characterId != null) ? characterId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_item_tracker_log_level_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token levelId = null;
        try {
            int alt29 = 2;
            final int LA29_0 = this.input.LA(1);
            if (LA29_0 != 129) {
                final NoViableAltException nvae = new NoViableAltException("", 29, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA29_ = this.input.LA(2);
            if (LA29_ == 87) {
                alt29 = 1;
            }
            else {
                if (LA29_ < 316 || LA29_ > 317) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 29, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt29 = 2;
            }
            switch (alt29) {
                case 1: {
                    this.match((IntStream)this.input, 129, ModerationCommandParser.FOLLOW_SET_ITEM_TRACKER_LOG_LEVEL_CMD_in_set_item_tracker_log_level_cmd4661);
                    levelId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_item_tracker_log_level_cmd4665);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_item_tracker_log_level_cmd4667);
                    cmd = new SetItemTrackerLogLevelCommand(Integer.parseInt((levelId != null) ? levelId.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 129, ModerationCommandParser.FOLLOW_SET_ITEM_TRACKER_LOG_LEVEL_CMD_in_set_item_tracker_log_level_cmd4674);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_item_tracker_log_level_cmd4682);
                        cmd = new SetItemTrackerLogLevelCommand();
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_skill_level_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token idSkill = null;
        Token level = null;
        try {
            this.match((IntStream)this.input, 135, ModerationCommandParser.FOLLOW_SET_SKILL_LEVEL_in_set_skill_level_cmd4700);
            idSkill = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_skill_level_cmd4704);
            level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_skill_level_cmd4708);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_skill_level_cmd4710);
            cmd = new SetSkillLevelCommand(Integer.parseInt((idSkill != null) ? idSkill.getText() : null), Short.parseShort((level != null) ? level.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand monster_group() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            this.match((IntStream)this.input, 82, ModerationCommandParser.FOLLOW_MONSTER_GROUP_in_monster_group4725);
            if (this.input.LA(1) != 267 && this.input.LA(1) != 269) {
                final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                throw mse;
            }
            this.input.consume();
            this.state.errorRecovery = false;
            id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_monster_group4738);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_monster_group4740);
            cmd = MonsterGroupCommand.createActivate(Long.parseLong((id != null) ? id.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_resource_speed_factor() throws RecognitionException {
        ModerationCommand cmd = null;
        Token speed = null;
        try {
            this.match((IntStream)this.input, 133, ModerationCommandParser.FOLLOW_SET_RESOURCE_SPEED_FACTOR_in_set_resource_speed_factor4758);
            speed = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_resource_speed_factor4762);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_resource_speed_factor4764);
            cmd = new SetResourceSpeedFactorCommand(Float.parseFloat((speed != null) ? speed.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand add_xp_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token quantity = null;
        try {
            this.match((IntStream)this.input, 12, ModerationCommandParser.FOLLOW_ADD_XP_in_add_xp_cmd4779);
            quantity = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_xp_cmd4783);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_add_xp_cmd4785);
            cmd = new AddXpCommand(Long.parseLong((quantity != null) ? quantity.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_bonus_factor_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token factor = null;
        Token startDate = null;
        Token endDate = null;
        List<Integer> instanceIds = null;
        try {
            int alt51 = 9;
            final int LA51_0 = this.input.LA(1);
            if (LA51_0 != 128) {
                final NoViableAltException nvae = new NoViableAltException("", 51, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt51 = 1;
                    break;
                }
                case 316:
                case 317: {
                    alt51 = 2;
                    break;
                }
                case 309: {
                    alt51 = 3;
                    break;
                }
                case 299: {
                    alt51 = 4;
                    break;
                }
                case 344: {
                    alt51 = 5;
                    break;
                }
                case 321: {
                    alt51 = 6;
                    break;
                }
                case 324: {
                    alt51 = 7;
                    break;
                }
                case 310: {
                    alt51 = 8;
                    break;
                }
                case 101: {
                    alt51 = 9;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 51, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt51) {
                case 1: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4801);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4803);
                    cmd = new SetServerBonusModificatorCommand();
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4810);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4820);
                        cmd = new SetServerBonusModificatorCommand(4);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4827);
                    this.match((IntStream)this.input, 309, ModerationCommandParser.FOLLOW_309_in_set_bonus_factor_cmd4830);
                    int alt2 = 2;
                    final int LA30_0 = this.input.LA(1);
                    if (LA30_0 == 60) {
                        alt2 = 1;
                    }
                    switch (alt2) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd4835);
                            break;
                        }
                    }
                    int alt3 = 2;
                    final int LA31_0 = this.input.LA(1);
                    if (LA31_0 == 41) {
                        alt3 = 1;
                    }
                    switch (alt3) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4841);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4846);
                            break;
                        }
                    }
                    int alt4 = 2;
                    final int LA32_0 = this.input.LA(1);
                    if (LA32_0 == 87) {
                        alt4 = 1;
                    }
                    switch (alt4) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4852);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state = this.state;
                            --state._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4855);
                    cmd = new SetServerBonusModificatorCommand(0, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4862);
                    this.match((IntStream)this.input, 299, ModerationCommandParser.FOLLOW_299_in_set_bonus_factor_cmd4865);
                    int alt5 = 2;
                    final int LA33_0 = this.input.LA(1);
                    if (LA33_0 == 60) {
                        alt5 = 1;
                    }
                    switch (alt5) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd4870);
                            break;
                        }
                    }
                    int alt6 = 2;
                    final int LA34_0 = this.input.LA(1);
                    if (LA34_0 == 41) {
                        alt6 = 1;
                    }
                    switch (alt6) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4876);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4881);
                            break;
                        }
                    }
                    int alt7 = 2;
                    final int LA35_0 = this.input.LA(1);
                    if (LA35_0 == 87) {
                        alt7 = 1;
                    }
                    switch (alt7) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4887);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state2 = this.state;
                            --state2._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4891);
                    cmd = new SetServerBonusModificatorCommand(1, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4898);
                    this.match((IntStream)this.input, 344, ModerationCommandParser.FOLLOW_344_in_set_bonus_factor_cmd4901);
                    int alt8 = 2;
                    final int LA36_0 = this.input.LA(1);
                    if (LA36_0 == 60) {
                        alt8 = 1;
                    }
                    switch (alt8) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd4906);
                            break;
                        }
                    }
                    int alt9 = 2;
                    final int LA37_0 = this.input.LA(1);
                    if (LA37_0 == 41) {
                        alt9 = 1;
                    }
                    switch (alt9) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4912);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4917);
                            break;
                        }
                    }
                    int alt10 = 2;
                    final int LA38_0 = this.input.LA(1);
                    if (LA38_0 == 87) {
                        alt10 = 1;
                    }
                    switch (alt10) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4923);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state3 = this.state;
                            --state3._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4927);
                    cmd = new SetServerBonusModificatorCommand(2, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4934);
                    this.match((IntStream)this.input, 321, ModerationCommandParser.FOLLOW_321_in_set_bonus_factor_cmd4937);
                    int alt11 = 2;
                    final int LA39_0 = this.input.LA(1);
                    if (LA39_0 == 60) {
                        alt11 = 1;
                    }
                    switch (alt11) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd4942);
                            break;
                        }
                    }
                    int alt12 = 2;
                    final int LA40_0 = this.input.LA(1);
                    if (LA40_0 == 41) {
                        alt12 = 1;
                    }
                    switch (alt12) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4948);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4953);
                            break;
                        }
                    }
                    int alt13 = 2;
                    final int LA41_0 = this.input.LA(1);
                    if (LA41_0 == 87) {
                        alt13 = 1;
                    }
                    switch (alt13) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4959);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state4 = this.state;
                            --state4._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4963);
                    cmd = new SetServerBonusModificatorCommand(5, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4970);
                    this.match((IntStream)this.input, 324, ModerationCommandParser.FOLLOW_324_in_set_bonus_factor_cmd4973);
                    int alt14 = 2;
                    final int LA42_0 = this.input.LA(1);
                    if (LA42_0 == 60) {
                        alt14 = 1;
                    }
                    switch (alt14) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd4978);
                            break;
                        }
                    }
                    int alt15 = 2;
                    final int LA43_0 = this.input.LA(1);
                    if (LA43_0 == 41) {
                        alt15 = 1;
                    }
                    switch (alt15) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4984);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd4989);
                            break;
                        }
                    }
                    int alt16 = 2;
                    final int LA44_0 = this.input.LA(1);
                    if (LA44_0 == 87) {
                        alt16 = 1;
                    }
                    switch (alt16) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4995);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state5 = this.state;
                            --state5._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd4999);
                    cmd = new SetServerBonusModificatorCommand(6, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd5006);
                    this.match((IntStream)this.input, 310, ModerationCommandParser.FOLLOW_310_in_set_bonus_factor_cmd5009);
                    int alt17 = 2;
                    final int LA45_0 = this.input.LA(1);
                    if (LA45_0 == 60) {
                        alt17 = 1;
                    }
                    switch (alt17) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd5014);
                            break;
                        }
                    }
                    int alt18 = 2;
                    final int LA46_0 = this.input.LA(1);
                    if (LA46_0 == 41) {
                        alt18 = 1;
                    }
                    switch (alt18) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd5020);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd5025);
                            break;
                        }
                    }
                    int alt19 = 2;
                    final int LA47_0 = this.input.LA(1);
                    if (LA47_0 == 87) {
                        alt19 = 1;
                    }
                    switch (alt19) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd5031);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state6 = this.state;
                            --state6._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd5035);
                    cmd = new SetServerBonusModificatorCommand(7, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 128, ModerationCommandParser.FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd5042);
                    this.match((IntStream)this.input, 101, ModerationCommandParser.FOLLOW_PVP_in_set_bonus_factor_cmd5045);
                    int alt20 = 2;
                    final int LA48_0 = this.input.LA(1);
                    if (LA48_0 == 60) {
                        alt20 = 1;
                    }
                    switch (alt20) {
                        case 1: {
                            factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_bonus_factor_cmd5050);
                            break;
                        }
                    }
                    int alt21 = 2;
                    final int LA49_0 = this.input.LA(1);
                    if (LA49_0 == 41) {
                        alt21 = 1;
                    }
                    switch (alt21) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd5056);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_set_bonus_factor_cmd5061);
                            break;
                        }
                    }
                    int alt22 = 2;
                    final int LA50_0 = this.input.LA(1);
                    if (LA50_0 == 87) {
                        alt22 = 1;
                    }
                    switch (alt22) {
                        case 1: {
                            this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_set_bonus_factor_cmd5067);
                            instanceIds = this.id_list_pattern();
                            final RecognizerSharedState state7 = this.state;
                            --state7._fsp;
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_bonus_factor_cmd5071);
                    cmd = new SetServerBonusModificatorCommand(8, (factor == null) ? 0.0f : Float.parseFloat((factor != null) ? factor.getText() : null), instanceIds, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand add_money_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token amountOfCash = null;
        try {
            this.match((IntStream)this.input, 8, ModerationCommandParser.FOLLOW_ADD_MONEY_in_add_money_cmd5088);
            amountOfCash = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_add_money_cmd5092);
            cmd = new AddMoneyCommand(Integer.parseInt((amountOfCash != null) ? amountOfCash.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand help_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return command = null;
        try {
            int alt52 = 4;
            final int LA52_0 = this.input.LA(1);
            if (LA52_0 != 72) {
                final NoViableAltException nvae = new NoViableAltException("", 52, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 53: {
                    alt52 = 3;
                    break;
                }
                case 224:
                case 232: {
                    alt52 = 4;
                    break;
                }
                case 27: {
                    final int LA52_ = this.input.LA(3);
                    if (LA52_ == 53) {
                        alt52 = 1;
                    }
                    else {
                        if (LA52_ != 224 && LA52_ != 232) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 52, 4, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt52 = 2;
                    }
                    break;
                }
                case 56: {
                    final int LA52_2 = this.input.LA(3);
                    if (LA52_2 == 53) {
                        alt52 = 1;
                    }
                    else {
                        if (LA52_2 != 224 && LA52_2 != 232) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 52, 5, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt52 = 2;
                    }
                    break;
                }
                case 4: {
                    final int LA52_3 = this.input.LA(3);
                    if (LA52_3 == 53) {
                        alt52 = 1;
                    }
                    else {
                        if (LA52_3 != 224 && LA52_3 != 232) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 52, 6, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt52 = 2;
                    }
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 52, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt52) {
                case 1: {
                    this.match((IntStream)this.input, 72, ModerationCommandParser.FOLLOW_HELP_in_help_cmd5107);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_help_cmd5111);
                    command = this.character_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_help_cmd5113);
                    cmd = new HelpCommand((command != null) ? this.input.toString(command.start, command.stop) : null);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 72, ModerationCommandParser.FOLLOW_HELP_in_help_cmd5120);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_help_cmd5124);
                    command = this.character_pattern();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    if (this.input.LA(1) == 224 || this.input.LA(1) == 232) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_help_cmd5134);
                        cmd = new HelpCommand((command != null) ? this.input.toString(command.start, command.stop) : null, true);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 72, ModerationCommandParser.FOLLOW_HELP_in_help_cmd5142);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_help_cmd5144);
                    cmd = new HelpCommand();
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 72, ModerationCommandParser.FOLLOW_HELP_in_help_cmd5152);
                    if (this.input.LA(1) == 224 || this.input.LA(1) == 232) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_help_cmd5162);
                        cmd = new HelpCommand(true);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand instance_usage_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            this.match((IntStream)this.input, 77, ModerationCommandParser.FOLLOW_INSTANCE_USAGE_in_instance_usage_cmd5181);
            id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_instance_usage_cmd5185);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_instance_usage_cmd5187);
            cmd = new InstanceUsageCommand(Short.parseShort((id != null) ? id.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand destroy_instance_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 43, ModerationCommandParser.FOLLOW_DESTROY_INSTANCE_in_destroy_instance_cmd5203);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_destroy_instance_cmd5205);
            cmd = new DestroyInstanceCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand show_aggro_list_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 138, ModerationCommandParser.FOLLOW_SHOW_AGGRO_LIST_in_show_aggro_list_cmd5222);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_show_aggro_list_cmd5224);
            cmd = new ShowAggroListCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_level_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token level = null;
        try {
            this.match((IntStream)this.input, 130, ModerationCommandParser.FOLLOW_SET_LEVEL_in_set_level_cmd5240);
            level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_level_cmd5244);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_level_cmd5246);
            cmd = new SetLevelCommand(Short.parseShort((level != null) ? level.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand spawn_ie_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token templateId = null;
        Token nb = null;
        try {
            this.match((IntStream)this.input, 142, ModerationCommandParser.FOLLOW_SPAWN_INTERACTIVE_ELEMENT_in_spawn_ie_cmd5259);
            templateId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_spawn_ie_cmd5265);
            nb = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_spawn_ie_cmd5271);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_spawn_ie_cmd5273);
            cmd = new SpawnIECommand(Integer.parseInt((templateId != null) ? templateId.getText() : null), Short.parseShort((nb != null) ? nb.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand sessions_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 125, ModerationCommandParser.FOLLOW_SESSIONS_in_sessions_cmd5288);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_sessions_cmd5290);
            cmd = new SessionsCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_next_challenge_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token challengeId = null;
        try {
            this.match((IntStream)this.input, 127, ModerationCommandParser.FOLLOW_SETNEXTCHALLENGE_in_set_next_challenge_cmd5306);
            challengeId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_next_challenge_cmd5312);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_next_challenge_cmd5314);
            cmd = new SetNextChallengeCommand(Integer.parseInt((challengeId != null) ? challengeId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand finish_challenge_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 59, ModerationCommandParser.FOLLOW_FINISHCHALLENGE_in_finish_challenge_cmd5330);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_finish_challenge_cmd5332);
            cmd = new FinishChallengeCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand staff_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            int alt53 = 2;
            final int LA53_0 = this.input.LA(1);
            if (LA53_0 != 144) {
                final NoViableAltException nvae = new NoViableAltException("", 53, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA53_ = this.input.LA(2);
            if (LA53_ == 90) {
                alt53 = 1;
            }
            else {
                if (LA53_ != 89) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 53, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt53 = 2;
            }
            switch (alt53) {
                case 1: {
                    this.match((IntStream)this.input, 144, ModerationCommandParser.FOLLOW_STAFF_in_staff_cmd5348);
                    this.match((IntStream)this.input, 90, ModerationCommandParser.FOLLOW_ON_in_staff_cmd5350);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_staff_cmd5352);
                    cmd = new StaffCommand(true);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 144, ModerationCommandParser.FOLLOW_STAFF_in_staff_cmd5359);
                    this.match((IntStream)this.input, 89, ModerationCommandParser.FOLLOW_OFF_in_staff_cmd5361);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_staff_cmd5363);
                    cmd = new StaffCommand(false);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand subscriber_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token accountId = null;
        Token rightId = null;
        Token levelId = null;
        try {
            int alt54 = 15;
            final int LA54_0 = this.input.LA(1);
            if (LA54_0 != 151) {
                final NoViableAltException nvae = new NoViableAltException("", 54, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 90: {
                    alt54 = 1;
                    break;
                }
                case 89: {
                    alt54 = 2;
                    break;
                }
                case 87: {
                    final int LA54_ = this.input.LA(3);
                    if (LA54_ == 90) {
                        alt54 = 3;
                    }
                    else {
                        if (LA54_ != 89) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 54, 4, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt54 = 4;
                    }
                    break;
                }
                case 369:
                case 403: {
                    alt54 = 5;
                    break;
                }
                case 316:
                case 317: {
                    alt54 = 6;
                    break;
                }
                case 389:
                case 395: {
                    alt54 = 7;
                    break;
                }
                case 391:
                case 402: {
                    alt54 = 8;
                    break;
                }
                case 276:
                case 283: {
                    alt54 = 9;
                    break;
                }
                case 277:
                case 285: {
                    alt54 = 10;
                    break;
                }
                case 115:
                case 355: {
                    alt54 = 11;
                    break;
                }
                case 381:
                case 405: {
                    alt54 = 12;
                    break;
                }
                case 393:
                case 406: {
                    alt54 = 13;
                    break;
                }
                case 367:
                case 387: {
                    alt54 = 14;
                    break;
                }
                case 353:
                case 362: {
                    alt54 = 15;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 54, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt54) {
                case 1: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5383);
                    this.match((IntStream)this.input, 90, ModerationCommandParser.FOLLOW_ON_in_subscriber_cmd5385);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5387);
                    cmd = new SubscriptionCommand(2, new String[0]);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5394);
                    this.match((IntStream)this.input, 89, ModerationCommandParser.FOLLOW_OFF_in_subscriber_cmd5396);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5398);
                    cmd = new SubscriptionCommand(3, new String[0]);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5405);
                    accountId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5409);
                    this.match((IntStream)this.input, 90, ModerationCommandParser.FOLLOW_ON_in_subscriber_cmd5411);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5413);
                    cmd = new SubscriptionCommand(2, new String[] { (accountId != null) ? accountId.getText() : null });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5420);
                    accountId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5424);
                    this.match((IntStream)this.input, 89, ModerationCommandParser.FOLLOW_OFF_in_subscriber_cmd5426);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5428);
                    cmd = new SubscriptionCommand(3, new String[] { (accountId != null) ? accountId.getText() : null });
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5435);
                    if (this.input.LA(1) == 369 || this.input.LA(1) == 403) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5445);
                        cmd = new SubscriptionCommand(1, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5452);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5462);
                        cmd = new SubscriptionCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5469);
                    if (this.input.LA(1) == 389 || this.input.LA(1) == 395) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5479);
                        cmd = new SubscriptionCommand(4, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 8: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5486);
                    if (this.input.LA(1) == 391 || this.input.LA(1) == 402) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5496);
                        cmd = new SubscriptionCommand(8, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 9: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5503);
                    if (this.input.LA(1) == 276 || this.input.LA(1) == 283) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        rightId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5515);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5517);
                        cmd = new SubscriptionCommand(5, new String[] { (rightId != null) ? rightId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 10: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5524);
                    if (this.input.LA(1) == 277 || this.input.LA(1) == 285) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        rightId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5536);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5538);
                        cmd = new SubscriptionCommand(10, new String[] { (rightId != null) ? rightId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 11: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5545);
                    if (this.input.LA(1) == 115 || this.input.LA(1) == 355) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        rightId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5557);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5559);
                        cmd = new SubscriptionCommand(6, new String[] { (rightId != null) ? rightId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 12: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5566);
                    if (this.input.LA(1) == 381 || this.input.LA(1) == 405) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        levelId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5578);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5580);
                        cmd = new SubscriptionCommand(9, new String[] { (levelId != null) ? levelId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 13: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5587);
                    if (this.input.LA(1) == 393 || this.input.LA(1) == 406) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        levelId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_subscriber_cmd5599);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5601);
                        cmd = new SubscriptionCommand(11, new String[] { (levelId != null) ? levelId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 14: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5608);
                    if (this.input.LA(1) == 367 || this.input.LA(1) == 387) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5618);
                        cmd = new SubscriptionCommand(7, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 15: {
                    this.match((IntStream)this.input, 151, ModerationCommandParser.FOLLOW_SUBSCRIBER_in_subscriber_cmd5625);
                    if (this.input.LA(1) == 353 || this.input.LA(1) == 362) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_subscriber_cmd5635);
                        cmd = new SubscriptionCommand(12, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand free_access_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            int alt55 = 3;
            final int LA55_0 = this.input.LA(1);
            if (LA55_0 != 62) {
                final NoViableAltException nvae = new NoViableAltException("", 55, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 90: {
                    alt55 = 1;
                    break;
                }
                case 89: {
                    alt55 = 2;
                    break;
                }
                case 53: {
                    alt55 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 55, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt55) {
                case 1: {
                    this.match((IntStream)this.input, 62, ModerationCommandParser.FOLLOW_FREE_ACCESS_in_free_access_cmd5653);
                    this.match((IntStream)this.input, 90, ModerationCommandParser.FOLLOW_ON_in_free_access_cmd5655);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_free_access_cmd5657);
                    cmd = new FreeAccess(true);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 62, ModerationCommandParser.FOLLOW_FREE_ACCESS_in_free_access_cmd5664);
                    this.match((IntStream)this.input, 89, ModerationCommandParser.FOLLOW_OFF_in_free_access_cmd5666);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_free_access_cmd5668);
                    cmd = new FreeAccess(false);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 62, ModerationCommandParser.FOLLOW_FREE_ACCESS_in_free_access_cmd5676);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_free_access_cmd5678);
                    cmd = new FreeAccess();
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand mute_partitions_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        int radius = 0;
        try {
            int alt56 = 2;
            final int LA56_0 = this.input.LA(1);
            if (LA56_0 != 85) {
                final NoViableAltException nvae = new NoViableAltException("", 56, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA56_ = this.input.LA(2);
            if (LA56_ == 53) {
                alt56 = 2;
            }
            else {
                if (LA56_ != 100) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 56, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt56 = 1;
            }
            switch (alt56) {
                case 1: {
                    this.match((IntStream)this.input, 85, ModerationCommandParser.FOLLOW_MUTE_PARTITIONS_in_mute_partitions_cmd5699);
                    this.pushFollow(ModerationCommandParser.FOLLOW_proximity_pattern_in_mute_partitions_cmd5703);
                    radius = this.proximity_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_mute_partitions_cmd5705);
                    cmd = new MutePartitionsCommand(radius);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 85, ModerationCommandParser.FOLLOW_MUTE_PARTITIONS_in_mute_partitions_cmd5712);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_mute_partitions_cmd5714);
                    cmd = new MutePartitionsCommand(1);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand unmute_partitions_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 164, ModerationCommandParser.FOLLOW_UNMUTE_PARTITIONS_in_unmute_partitions_cmd5731);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_unmute_partitions_cmd5733);
            cmd = new UnmutePartitionsCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand mute_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token clientId = null;
        Token duration = null;
        try {
            int alt58 = 2;
            final int LA58_0 = this.input.LA(1);
            if (LA58_0 != 84) {
                final NoViableAltException nvae = new NoViableAltException("", 58, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA58_ = this.input.LA(2);
            if (LA58_ == 53 || LA58_ == 87) {
                alt58 = 1;
            }
            else {
                if (LA58_ != 76) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 58, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt58 = 2;
            }
            switch (alt58) {
                case 1: {
                    this.match((IntStream)this.input, 84, ModerationCommandParser.FOLLOW_MUTE_in_mute_cmd5759);
                    int alt2 = 2;
                    final int LA57_0 = this.input.LA(1);
                    if (LA57_0 == 87) {
                        alt2 = 1;
                    }
                    switch (alt2) {
                        case 1: {
                            clientId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_mute_cmd5764);
                            duration = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_mute_cmd5768);
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_mute_cmd5772);
                    cmd = new MuteCommand(0, (clientId == null) ? -1L : Long.parseLong((clientId != null) ? clientId.getText() : null), (duration == null) ? -1 : Integer.parseInt((duration != null) ? duration.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 84, ModerationCommandParser.FOLLOW_MUTE_in_mute_cmd5779);
                    this.match((IntStream)this.input, 76, ModerationCommandParser.FOLLOW_INFO_in_mute_cmd5782);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_mute_cmd5785);
                    cmd = new MuteCommand(2);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand unmute_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token clientId = null;
        try {
            this.match((IntStream)this.input, 163, ModerationCommandParser.FOLLOW_UNMUTE_in_unmute_cmd5818);
            clientId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_unmute_cmd5822);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_unmute_cmd5824);
            cmd = new MuteCommand(1, (clientId == null) ? -1L : Long.parseLong((clientId != null) ? clientId.getText() : null), -1);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand distribute_items_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token itemId = null;
        Token stackSize = null;
        int radius = 0;
        try {
            int alt59 = 2;
            final int LA59_0 = this.input.LA(1);
            if (LA59_0 != 46) {
                final NoViableAltException nvae = new NoViableAltException("", 59, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA59_ = this.input.LA(2);
            if (LA59_ != 100) {
                final NoViableAltException nvae2 = new NoViableAltException("", 59, 1, (IntStream)this.input);
                throw nvae2;
            }
            final int LA59_2 = this.input.LA(3);
            if (LA59_2 != 87) {
                final NoViableAltException nvae3 = new NoViableAltException("", 59, 2, (IntStream)this.input);
                throw nvae3;
            }
            final int LA59_3 = this.input.LA(4);
            if (LA59_3 == 87) {
                alt59 = 1;
            }
            else {
                if (LA59_3 != 53) {
                    final NoViableAltException nvae4 = new NoViableAltException("", 59, 3, (IntStream)this.input);
                    throw nvae4;
                }
                alt59 = 2;
            }
            switch (alt59) {
                case 1: {
                    this.match((IntStream)this.input, 46, ModerationCommandParser.FOLLOW_DISTRIBUTE_ITEMS_in_distribute_items_cmd5842);
                    this.pushFollow(ModerationCommandParser.FOLLOW_proximity_pattern_in_distribute_items_cmd5846);
                    radius = this.proximity_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_distribute_items_cmd5850);
                    stackSize = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_distribute_items_cmd5854);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_distribute_items_cmd5856);
                    cmd = new DistributeItemsCommand(radius, Integer.parseInt((itemId != null) ? itemId.getText() : null), Short.parseShort((stackSize != null) ? stackSize.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 46, ModerationCommandParser.FOLLOW_DISTRIBUTE_ITEMS_in_distribute_items_cmd5863);
                    this.pushFollow(ModerationCommandParser.FOLLOW_proximity_pattern_in_distribute_items_cmd5867);
                    radius = this.proximity_pattern();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_distribute_items_cmd5871);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_distribute_items_cmd5873);
                    cmd = new DistributeItemsCommand(radius, Integer.parseInt((itemId != null) ? itemId.getText() : null), (short)1);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand search_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        message_return text = null;
        try {
            int alt64 = 9;
            switch (this.input.LA(1)) {
                case 123: {
                    switch (this.input.LA(2)) {
                        case 361: {
                            alt64 = 1;
                            break;
                        }
                        case 328: {
                            alt64 = 2;
                            break;
                        }
                        case 319: {
                            alt64 = 3;
                            break;
                        }
                        case 358: {
                            alt64 = 4;
                            break;
                        }
                        case 320: {
                            alt64 = 5;
                            break;
                        }
                        case 147: {
                            alt64 = 6;
                            break;
                        }
                        case 329: {
                            alt64 = 7;
                            break;
                        }
                        case 126: {
                            alt64 = 8;
                            break;
                        }
                        case 349: {
                            alt64 = 9;
                            break;
                        }
                        default: {
                            final NoViableAltException nvae = new NoViableAltException("", 64, 1, (IntStream)this.input);
                            throw nvae;
                        }
                    }
                    break;
                }
                case 397: {
                    alt64 = 2;
                    break;
                }
                case 394: {
                    alt64 = 5;
                    break;
                }
                case 403: {
                    alt64 = 6;
                    break;
                }
                case 398: {
                    alt64 = 7;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 64, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt64) {
                case 1: {
                    this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd5891);
                    this.match((IntStream)this.input, 361, ModerationCommandParser.FOLLOW_361_in_search_cmd5893);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_search_cmd5897);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd5899);
                    cmd = new SearchCommand((byte)1, new Object[] { Integer.parseInt((id != null) ? id.getText() : null) });
                    break;
                }
                case 2: {
                    int alt2 = 2;
                    final int LA60_0 = this.input.LA(1);
                    if (LA60_0 == 123) {
                        alt2 = 1;
                    }
                    else {
                        if (LA60_0 != 397) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 60, 0, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt2 = 2;
                    }
                    switch (alt2) {
                        case 1: {
                            this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd5907);
                            this.match((IntStream)this.input, 328, ModerationCommandParser.FOLLOW_328_in_search_cmd5909);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 397, ModerationCommandParser.FOLLOW_397_in_search_cmd5913);
                            break;
                        }
                    }
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_search_cmd5918);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd5920);
                    cmd = new SearchCommand((byte)2, new Object[] { Integer.parseInt((id != null) ? id.getText() : null) });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd5927);
                    this.match((IntStream)this.input, 319, ModerationCommandParser.FOLLOW_319_in_search_cmd5929);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_search_cmd5933);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd5935);
                    cmd = new SearchCommand((byte)6, new Object[] { Integer.parseInt((id != null) ? id.getText() : null) });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd5943);
                    this.match((IntStream)this.input, 358, ModerationCommandParser.FOLLOW_358_in_search_cmd5945);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_search_cmd5949);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd5951);
                    cmd = new SearchCommand((byte)3, new Object[] { Integer.parseInt((id != null) ? id.getText() : null) });
                    break;
                }
                case 5: {
                    int alt3 = 2;
                    final int LA61_0 = this.input.LA(1);
                    if (LA61_0 == 123) {
                        alt3 = 1;
                    }
                    else {
                        if (LA61_0 != 394) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 61, 0, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt3 = 2;
                    }
                    switch (alt3) {
                        case 1: {
                            this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd5961);
                            this.match((IntStream)this.input, 320, ModerationCommandParser.FOLLOW_320_in_search_cmd5963);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 394, ModerationCommandParser.FOLLOW_394_in_search_cmd5968);
                            break;
                        }
                    }
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_search_cmd5973);
                    text = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd5975);
                    cmd = new SearchCommand((byte)4, new Object[] { (text != null) ? text.txt : null });
                    break;
                }
                case 6: {
                    int alt4 = 2;
                    final int LA62_0 = this.input.LA(1);
                    if (LA62_0 == 123) {
                        alt4 = 1;
                    }
                    else {
                        if (LA62_0 != 403) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 62, 0, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt4 = 2;
                    }
                    switch (alt4) {
                        case 1: {
                            this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd5985);
                            this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_search_cmd5987);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 403, ModerationCommandParser.FOLLOW_403_in_search_cmd5992);
                            break;
                        }
                    }
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_search_cmd5997);
                    text = this.message();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd5999);
                    cmd = new SearchCommand((byte)7, new Object[] { (text != null) ? text.txt : null });
                    break;
                }
                case 7: {
                    int alt5 = 2;
                    final int LA63_0 = this.input.LA(1);
                    if (LA63_0 == 123) {
                        alt5 = 1;
                    }
                    else {
                        if (LA63_0 != 398) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 63, 0, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt5 = 2;
                    }
                    switch (alt5) {
                        case 1: {
                            this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd6009);
                            this.match((IntStream)this.input, 329, ModerationCommandParser.FOLLOW_329_in_search_cmd6011);
                            break;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 398, ModerationCommandParser.FOLLOW_398_in_search_cmd6016);
                            break;
                        }
                    }
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_search_cmd6021);
                    text = this.message();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd6023);
                    cmd = new SearchCommand((byte)5, new Object[] { (text != null) ? text.txt : null });
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd6030);
                    this.match((IntStream)this.input, 126, ModerationCommandParser.FOLLOW_SET_in_search_cmd6032);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_search_cmd6036);
                    text = this.message();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd6038);
                    cmd = new SearchCommand((byte)8, new Object[] { (text != null) ? text.txt : null });
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 123, ModerationCommandParser.FOLLOW_SEARCH_in_search_cmd6045);
                    this.match((IntStream)this.input, 349, ModerationCommandParser.FOLLOW_349_in_search_cmd6047);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_search_cmd6051);
                    text = this.message();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_search_cmd6053);
                    cmd = new SearchCommand((byte)9, new Object[] { (text != null) ? text.txt : null });
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand teleport_to_breed_mob_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token breedId = null;
        try {
            this.match((IntStream)this.input, 156, ModerationCommandParser.FOLLOW_TELEPORT_TO_MONSTER_in_teleport_to_breed_mob_cmd6070);
            breedId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_teleport_to_breed_mob_cmd6074);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_teleport_to_breed_mob_cmd6076);
            cmd = new TeleportToBreedMobCommand(Short.parseShort((breedId != null) ? breedId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand quota_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token queue = null;
        Token player = null;
        try {
            int alt67 = 3;
            switch (this.input.LA(1)) {
                case 102: {
                    switch (this.input.LA(2)) {
                        case 53: {
                            alt67 = 1;
                            break;
                        }
                        case 346:
                        case 350: {
                            alt67 = 2;
                            break;
                        }
                        case 338:
                        case 340: {
                            alt67 = 3;
                            break;
                        }
                        default: {
                            final NoViableAltException nvae = new NoViableAltException("", 67, 1, (IntStream)this.input);
                            throw nvae;
                        }
                    }
                    break;
                }
                case 348: {
                    alt67 = 2;
                    break;
                }
                case 347: {
                    alt67 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 67, 0, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt67) {
                case 1: {
                    this.match((IntStream)this.input, 102, ModerationCommandParser.FOLLOW_QUOTA_in_quota_cmd6094);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_quota_cmd6096);
                    cmd = new QuotaCommand();
                    break;
                }
                case 2: {
                    int alt2 = 2;
                    final int LA65_0 = this.input.LA(1);
                    if (LA65_0 == 102) {
                        alt2 = 1;
                    }
                    else {
                        if (LA65_0 != 348) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 65, 0, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt2 = 2;
                    }
                    switch (alt2) {
                        case 1: {
                            this.match((IntStream)this.input, 102, ModerationCommandParser.FOLLOW_QUOTA_in_quota_cmd6106);
                            if (this.input.LA(1) == 346 || this.input.LA(1) == 350) {
                                this.input.consume();
                                this.state.errorRecovery = false;
                                break;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            throw mse;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 348, ModerationCommandParser.FOLLOW_348_in_quota_cmd6119);
                            break;
                        }
                    }
                    queue = (Token)this.match((IntStream)this.input, 20, ModerationCommandParser.FOLLOW_BOOLEAN_in_quota_cmd6124);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_quota_cmd6126);
                    cmd = new QuotaCommand(Boolean.parseBoolean((queue != null) ? queue.getText() : null));
                    break;
                }
                case 3: {
                    int alt3 = 2;
                    final int LA66_0 = this.input.LA(1);
                    if (LA66_0 == 102) {
                        alt3 = 1;
                    }
                    else {
                        if (LA66_0 != 347) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 66, 0, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt3 = 2;
                    }
                    switch (alt3) {
                        case 1: {
                            this.match((IntStream)this.input, 102, ModerationCommandParser.FOLLOW_QUOTA_in_quota_cmd6135);
                            if (this.input.LA(1) == 338 || this.input.LA(1) == 340) {
                                this.input.consume();
                                this.state.errorRecovery = false;
                                break;
                            }
                            final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                            throw mse;
                        }
                        case 2: {
                            this.match((IntStream)this.input, 347, ModerationCommandParser.FOLLOW_347_in_quota_cmd6148);
                            break;
                        }
                    }
                    player = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_quota_cmd6153);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_quota_cmd6155);
                    cmd = new QuotaCommand(Short.parseShort((player != null) ? player.getText() : null));
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ragnarok_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        message_return pass = null;
        try {
            this.match((IntStream)this.input, 103, ModerationCommandParser.FOLLOW_RAGNAROK_in_ragnarok_cmd6173);
            this.pushFollow(ModerationCommandParser.FOLLOW_message_in_ragnarok_cmd6177);
            pass = this.message();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ragnarok_cmd6179);
            cmd = new RagnarokCommand((pass != null) ? pass.txt : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand remove_floor_items_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 111, ModerationCommandParser.FOLLOW_REMOVE_FLOOR_ITEMS_in_remove_floor_items_cmd6196);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_remove_floor_items_cmd6198);
            cmd = new RemoveFloorItemsCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand show_population_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token nb = null;
        try {
            this.match((IntStream)this.input, 140, ModerationCommandParser.FOLLOW_SHOW_POPULATION_in_show_population_cmd6217);
            nb = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_show_population_cmd6221);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_show_population_cmd6223);
            cmd = new ShowPopulationCommand(Integer.parseInt((nb != null) ? nb.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand show_monster_quota_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 139, ModerationCommandParser.FOLLOW_SHOW_MONSTER_QUOTA_in_show_monster_quota_cmd6241);
            cmd = new ShowMonsterQuota();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand cancel_collect_cooldown_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 24, ModerationCommandParser.FOLLOW_CANCEL_COLLECT_COOLDOWN_in_cancel_collect_cooldown_cmd6259);
            cmd = new CancelCollectCooldownCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_wakfu_gauge_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token nb = null;
        try {
            this.match((IntStream)this.input, 137, ModerationCommandParser.FOLLOW_SET_WAKFU_GAUGE_in_set_wakfu_gauge_cmd6277);
            nb = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_set_wakfu_gauge_cmd6281);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_wakfu_gauge_cmd6283);
            cmd = new SetWakfuGaugeCommand(Float.parseFloat((nb != null) ? nb.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand get_instance_uid_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 65, ModerationCommandParser.FOLLOW_GET_INSTANCE_UID_in_get_instance_uid_cmd6301);
            cmd = new GetMyInstanceUIDCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand dump_bag_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 48, ModerationCommandParser.FOLLOW_DUMP_BAG_in_dump_bag_cmd6319);
            cmd = new DumpBagCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand temp_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 158, ModerationCommandParser.FOLLOW_TEMP_in_temp_cmd6337);
            cmd = new TempCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand calendar_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token type = null;
        Token day = null;
        Token month = null;
        Token year = null;
        Token hour = null;
        Token minute = null;
        Token eventUid = null;
        Token characterID = null;
        Token newMax = null;
        Token nbEvents = null;
        message_return title = null;
        message_return desc = null;
        message_return invitedName = null;
        try {
            int alt68 = 20;
            final int LA68_0 = this.input.LA(1);
            if (LA68_0 != 23) {
                final NoViableAltException nvae = new NoViableAltException("", 68, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 180: {
                    final int LA68_ = this.input.LA(3);
                    if (LA68_ != 56) {
                        final NoViableAltException nvae2 = new NoViableAltException("", 68, 2, (IntStream)this.input);
                        throw nvae2;
                    }
                    final int LA68_2 = this.input.LA(4);
                    if (LA68_2 != 56) {
                        final NoViableAltException nvae3 = new NoViableAltException("", 68, 18, (IntStream)this.input);
                        throw nvae3;
                    }
                    final int LA68_3 = this.input.LA(5);
                    if (LA68_3 != 87) {
                        final NoViableAltException nvae4 = new NoViableAltException("", 68, 22, (IntStream)this.input);
                        throw nvae4;
                    }
                    final int LA68_4 = this.input.LA(6);
                    if (LA68_4 != 87) {
                        final NoViableAltException nvae5 = new NoViableAltException("", 68, 25, (IntStream)this.input);
                        throw nvae5;
                    }
                    final int LA68_5 = this.input.LA(7);
                    if (LA68_5 != 87) {
                        final NoViableAltException nvae6 = new NoViableAltException("", 68, 27, (IntStream)this.input);
                        throw nvae6;
                    }
                    final int LA68_6 = this.input.LA(8);
                    if (LA68_6 == 87) {
                        final int LA68_7 = this.input.LA(9);
                        if (LA68_7 == 53) {
                            alt68 = 1;
                        }
                        else {
                            if (LA68_7 != 87) {
                                final NoViableAltException nvae7 = new NoViableAltException("", 68, 32, (IntStream)this.input);
                                throw nvae7;
                            }
                            alt68 = 2;
                        }
                        break;
                    }
                    final NoViableAltException nvae8 = new NoViableAltException("", 68, 29, (IntStream)this.input);
                    throw nvae8;
                }
                case 234: {
                    alt68 = 3;
                    break;
                }
                case 193: {
                    alt68 = 4;
                    break;
                }
                case 261: {
                    final int LA68_8 = this.input.LA(3);
                    if (LA68_8 == 87) {
                        alt68 = 5;
                    }
                    else {
                        if (LA68_8 != 53) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 68, 5, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt68 = 6;
                    }
                    break;
                }
                case 225: {
                    alt68 = 7;
                    break;
                }
                case 259: {
                    alt68 = 8;
                    break;
                }
                case 263: {
                    alt68 = 9;
                    break;
                }
                case 260: {
                    alt68 = 10;
                    break;
                }
                case 222: {
                    alt68 = 11;
                    break;
                }
                case 213: {
                    alt68 = 12;
                    break;
                }
                case 246: {
                    alt68 = 13;
                    break;
                }
                case 186: {
                    alt68 = 14;
                    break;
                }
                case 245: {
                    alt68 = 15;
                    break;
                }
                case 248: {
                    alt68 = 16;
                    break;
                }
                case 247: {
                    final int LA68_9 = this.input.LA(3);
                    if (LA68_9 == 87) {
                        final int LA68_10 = this.input.LA(4);
                        if (LA68_10 == 87) {
                            final int LA68_11 = this.input.LA(5);
                            if (LA68_11 != 87) {
                                final NoViableAltException nvae4 = new NoViableAltException("", 68, 23, (IntStream)this.input);
                                throw nvae4;
                            }
                            final int LA68_12 = this.input.LA(6);
                            if (LA68_12 != 87) {
                                final NoViableAltException nvae5 = new NoViableAltException("", 68, 26, (IntStream)this.input);
                                throw nvae5;
                            }
                            final int LA68_13 = this.input.LA(7);
                            if (LA68_13 == 87) {
                                alt68 = 17;
                            }
                            else {
                                if (LA68_13 != 53) {
                                    final NoViableAltException nvae6 = new NoViableAltException("", 68, 28, (IntStream)this.input);
                                    throw nvae6;
                                }
                                alt68 = 18;
                            }
                        }
                        else {
                            if (LA68_10 != 56) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 68, 21, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt68 = 19;
                        }
                        break;
                    }
                    final NoViableAltException nvae2 = new NoViableAltException("", 68, 16, (IntStream)this.input);
                    throw nvae2;
                }
                case 208: {
                    alt68 = 20;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 68, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt68) {
                case 1: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6354);
                    this.match((IntStream)this.input, 180, ModerationCommandParser.FOLLOW_180_in_calendar_cmd6356);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6360);
                    title = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6364);
                    desc = this.message();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    type = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6368);
                    day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6372);
                    month = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6376);
                    year = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6380);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6382);
                    cmd = new CalendarCommand(4, new String[] { (title != null) ? title.txt : null, (desc != null) ? desc.txt : null, (type != null) ? type.getText() : null, (day != null) ? day.getText() : null, (month != null) ? month.getText() : null, (year != null) ? year.getText() : null });
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6388);
                    this.match((IntStream)this.input, 180, ModerationCommandParser.FOLLOW_180_in_calendar_cmd6390);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6394);
                    title = this.message();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6398);
                    desc = this.message();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    type = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6402);
                    day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6406);
                    month = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6410);
                    year = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6414);
                    hour = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6418);
                    minute = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6422);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6424);
                    cmd = new CalendarCommand(4, new String[] { (title != null) ? title.txt : null, (desc != null) ? desc.txt : null, (type != null) ? type.getText() : null, (day != null) ? day.getText() : null, (month != null) ? month.getText() : null, (year != null) ? year.getText() : null, (hour != null) ? hour.getText() : null, (minute != null) ? minute.getText() : null });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6431);
                    this.match((IntStream)this.input, 234, ModerationCommandParser.FOLLOW_234_in_calendar_cmd6433);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6437);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6439);
                    cmd = new CalendarCommand(3, new String[] { (eventUid != null) ? eventUid.getText() : null });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6446);
                    this.match((IntStream)this.input, 193, ModerationCommandParser.FOLLOW_193_in_calendar_cmd6448);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6450);
                    cmd = new CalendarCommand(2, new String[0]);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6457);
                    this.match((IntStream)this.input, 261, ModerationCommandParser.FOLLOW_261_in_calendar_cmd6459);
                    type = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6463);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6465);
                    cmd = new CalendarCommand(10, new String[] { (type != null) ? type.getText() : null });
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6472);
                    this.match((IntStream)this.input, 261, ModerationCommandParser.FOLLOW_261_in_calendar_cmd6474);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6476);
                    cmd = new CalendarCommand(10, new String[] { "0" });
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6483);
                    this.match((IntStream)this.input, 225, ModerationCommandParser.FOLLOW_225_in_calendar_cmd6485);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6489);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6491);
                    cmd = new CalendarCommand(1, new String[] { (eventUid != null) ? eventUid.getText() : null });
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6497);
                    this.match((IntStream)this.input, 259, ModerationCommandParser.FOLLOW_259_in_calendar_cmd6499);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6503);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6505);
                    cmd = new CalendarCommand(5, new String[] { (eventUid != null) ? eventUid.getText() : null });
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6511);
                    this.match((IntStream)this.input, 263, ModerationCommandParser.FOLLOW_263_in_calendar_cmd6513);
                    characterID = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6517);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6521);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6523);
                    cmd = new CalendarCommand(6, new String[] { (characterID != null) ? characterID.getText() : null, (eventUid != null) ? eventUid.getText() : null });
                    break;
                }
                case 10: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6529);
                    this.match((IntStream)this.input, 260, ModerationCommandParser.FOLLOW_260_in_calendar_cmd6531);
                    characterID = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6535);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6539);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6541);
                    cmd = new CalendarCommand(7, new String[] { (characterID != null) ? characterID.getText() : null, (eventUid != null) ? eventUid.getText() : null });
                    break;
                }
                case 11: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6547);
                    this.match((IntStream)this.input, 222, ModerationCommandParser.FOLLOW_222_in_calendar_cmd6549);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6553);
                    newMax = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6557);
                    type = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6561);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6563);
                    cmd = new CalendarCommand(8, new String[] { (eventUid != null) ? eventUid.getText() : null, (newMax != null) ? newMax.getText() : null, (type != null) ? type.getText() : null });
                    break;
                }
                case 12: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6569);
                    this.match((IntStream)this.input, 213, ModerationCommandParser.FOLLOW_213_in_calendar_cmd6571);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6575);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6579);
                    invitedName = this.message();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6581);
                    cmd = new CalendarCommand(9, new String[] { (eventUid != null) ? eventUid.getText() : null, (invitedName != null) ? invitedName.txt : null });
                    break;
                }
                case 13: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6588);
                    this.match((IntStream)this.input, 246, ModerationCommandParser.FOLLOW_246_in_calendar_cmd6590);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6594);
                    day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6598);
                    month = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6602);
                    year = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6606);
                    hour = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6610);
                    minute = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6614);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6616);
                    cmd = new CalendarCommand(11, new String[] { (eventUid != null) ? eventUid.getText() : null, (day != null) ? day.getText() : null, (month != null) ? month.getText() : null, (year != null) ? year.getText() : null, (hour != null) ? hour.getText() : null, (minute != null) ? minute.getText() : null });
                    break;
                }
                case 14: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6623);
                    this.match((IntStream)this.input, 186, ModerationCommandParser.FOLLOW_186_in_calendar_cmd6625);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6629);
                    cmd = new CalendarCommand(11, new String[] { (eventUid != null) ? eventUid.getText() : null, "-1" });
                    break;
                }
                case 15: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6636);
                    this.match((IntStream)this.input, 245, ModerationCommandParser.FOLLOW_245_in_calendar_cmd6638);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6642);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6646);
                    desc = this.message();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    cmd = new CalendarCommand(12, new String[] { (eventUid != null) ? eventUid.getText() : null, (desc != null) ? desc.txt : null });
                    break;
                }
                case 16: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6653);
                    this.match((IntStream)this.input, 248, ModerationCommandParser.FOLLOW_248_in_calendar_cmd6655);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6659);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6663);
                    title = this.message();
                    final RecognizerSharedState state7 = this.state;
                    --state7._fsp;
                    cmd = new CalendarCommand(13, new String[] { (eventUid != null) ? eventUid.getText() : null, (title != null) ? title.txt : null });
                    break;
                }
                case 17: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6670);
                    this.match((IntStream)this.input, 247, ModerationCommandParser.FOLLOW_247_in_calendar_cmd6672);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6676);
                    day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6680);
                    month = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6684);
                    year = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6688);
                    hour = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6692);
                    minute = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6696);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6698);
                    cmd = new CalendarCommand(14, new String[] { (eventUid != null) ? eventUid.getText() : null, (day != null) ? day.getText() : null, (month != null) ? month.getText() : null, (year != null) ? year.getText() : null, (hour != null) ? hour.getText() : null, (minute != null) ? minute.getText() : null });
                    break;
                }
                case 18: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6705);
                    this.match((IntStream)this.input, 247, ModerationCommandParser.FOLLOW_247_in_calendar_cmd6707);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6711);
                    day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6715);
                    month = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6719);
                    year = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6723);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_calendar_cmd6725);
                    cmd = new CalendarCommand(14, new String[] { (eventUid != null) ? eventUid.getText() : null, (day != null) ? day.getText() : null, (month != null) ? month.getText() : null, (year != null) ? year.getText() : null });
                    break;
                }
                case 19: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6732);
                    this.match((IntStream)this.input, 247, ModerationCommandParser.FOLLOW_247_in_calendar_cmd6734);
                    eventUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6738);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_calendar_cmd6742);
                    title = this.message();
                    final RecognizerSharedState state8 = this.state;
                    --state8._fsp;
                    cmd = new CalendarCommand(13, new String[] { (eventUid != null) ? eventUid.getText() : null, (title != null) ? title.txt : null });
                    break;
                }
                case 20: {
                    this.match((IntStream)this.input, 23, ModerationCommandParser.FOLLOW_CALENDAR_CMD_in_calendar_cmd6749);
                    this.match((IntStream)this.input, 208, ModerationCommandParser.FOLLOW_208_in_calendar_cmd6751);
                    nbEvents = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_calendar_cmd6755);
                    cmd = new CalendarCommand(15, new String[] { (nbEvents != null) ? nbEvents.getText() : null });
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand fight_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        Token value = null;
        try {
            int alt69 = 11;
            final int LA69_0 = this.input.LA(1);
            if (LA69_0 != 58) {
                final NoViableAltException nvae = new NoViableAltException("", 69, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 242:
                case 250: {
                    alt69 = 1;
                    break;
                }
                case 241:
                case 243: {
                    alt69 = 2;
                    break;
                }
                case 316:
                case 317: {
                    alt69 = 3;
                    break;
                }
                case 264: {
                    alt69 = 4;
                    break;
                }
                case 207: {
                    alt69 = 5;
                    break;
                }
                case 221:
                case 223: {
                    alt69 = 6;
                    break;
                }
                case 185:
                case 194: {
                    alt69 = 7;
                    break;
                }
                case 187:
                case 195: {
                    alt69 = 8;
                    break;
                }
                case 189:
                case 192: {
                    alt69 = 9;
                    break;
                }
                case 188:
                case 196: {
                    alt69 = 10;
                    break;
                }
                case 244:
                case 254: {
                    alt69 = 11;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 69, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt69) {
                case 1: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6774);
                    if (this.input.LA(1) == 242 || this.input.LA(1) == 250) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6784);
                        cmd = new FightCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6791);
                    if (this.input.LA(1) == 241 || this.input.LA(1) == 243) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_fight_cmd6803);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6806);
                        cmd = new FightCommand(1, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6813);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        cmd = new FightCommand(2, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6828);
                    this.match((IntStream)this.input, 264, ModerationCommandParser.FOLLOW_264_in_fight_cmd6830);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6832);
                    cmd = new FightCommand(3, new String[0]);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6839);
                    this.match((IntStream)this.input, 207, ModerationCommandParser.FOLLOW_207_in_fight_cmd6841);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6843);
                    cmd = new FightCommand(4, new String[0]);
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6850);
                    if (this.input.LA(1) == 221 || this.input.LA(1) == 223) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6860);
                        cmd = new FightCommand(5, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6867);
                    if (this.input.LA(1) == 185 || this.input.LA(1) == 194) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_fight_cmd6879);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6881);
                        cmd = new FightCommand(6, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 8: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6888);
                    if (this.input.LA(1) == 187 || this.input.LA(1) == 195) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6898);
                        cmd = new FightCommand(7, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 9: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6905);
                    if (this.input.LA(1) == 189 || this.input.LA(1) == 192) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_fight_cmd6917);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6919);
                        cmd = new FightCommand(8, new String[] { (value != null) ? value.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 10: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6926);
                    if (this.input.LA(1) == 188 || this.input.LA(1) == 196) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6936);
                        cmd = new FightCommand(9, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 11: {
                    this.match((IntStream)this.input, 58, ModerationCommandParser.FOLLOW_FIGHT_CMD_in_fight_cmd6943);
                    if (this.input.LA(1) == 244 || this.input.LA(1) == 254) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        value = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_fight_cmd6955);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fight_cmd6957);
                        cmd = new FightCommand(10, new String[] { (value != null) ? value.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand protector_command() throws RecognitionException {
        ModerationCommand cmd = null;
        Token protectorId = null;
        Token newNationId = null;
        Token second = null;
        Token newRatio = null;
        try {
            int alt70 = 5;
            final int LA70_0 = this.input.LA(1);
            if (LA70_0 != 99) {
                final NoViableAltException nvae = new NoViableAltException("", 70, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 190:
                case 191: {
                    alt70 = 1;
                    break;
                }
                case 181:
                case 183: {
                    alt70 = 2;
                    break;
                }
                case 215:
                case 218: {
                    alt70 = 3;
                    break;
                }
                case 216:
                case 219: {
                    alt70 = 4;
                    break;
                }
                case 316:
                case 317: {
                    alt70 = 5;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 70, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt70) {
                case 1: {
                    this.match((IntStream)this.input, 99, ModerationCommandParser.FOLLOW_PROTECTOR_CMD_in_protector_command6975);
                    if (this.input.LA(1) >= 190 && this.input.LA(1) <= 191) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        protectorId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_protector_command6987);
                        newNationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_protector_command6991);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_protector_command6993);
                        cmd = new ProtectorCommand(2, new String[] { (protectorId != null) ? protectorId.getText() : null, (newNationId != null) ? newNationId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 99, ModerationCommandParser.FOLLOW_PROTECTOR_CMD_in_protector_command7004);
                    if (this.input.LA(1) == 181 || this.input.LA(1) == 183) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        protectorId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_protector_command7016);
                        newNationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_protector_command7020);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_protector_command7022);
                        cmd = new ProtectorCommand(3, new String[] { (protectorId != null) ? protectorId.getText() : null, (newNationId != null) ? newNationId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 99, ModerationCommandParser.FOLLOW_PROTECTOR_CMD_in_protector_command7033);
                    if (this.input.LA(1) == 215 || this.input.LA(1) == 218) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        second = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_protector_command7045);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_protector_command7047);
                        cmd = new ProtectorCommand(4, new String[] { (second != null) ? second.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 99, ModerationCommandParser.FOLLOW_PROTECTOR_CMD_in_protector_command7057);
                    if (this.input.LA(1) == 216 || this.input.LA(1) == 219) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        newRatio = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_protector_command7069);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_protector_command7071);
                        cmd = new ProtectorCommand(5, new String[] { (newRatio != null) ? newRatio.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 99, ModerationCommandParser.FOLLOW_PROTECTOR_CMD_in_protector_command7081);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_protector_command7091);
                        cmd = new ProtectorCommand(1, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand state_command() throws RecognitionException {
        ModerationCommand cmd = null;
        Token containerType = null;
        Token stateId = null;
        Token stateLevel = null;
        try {
            int alt71 = 7;
            final int LA71_0 = this.input.LA(1);
            if (LA71_0 != 147) {
                final NoViableAltException nvae = new NoViableAltException("", 71, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt71 = 1;
                    break;
                }
                case 238:
                case 249: {
                    alt71 = 2;
                    break;
                }
                case 239:
                case 251: {
                    alt71 = 3;
                    break;
                }
                case 240:
                case 252: {
                    alt71 = 4;
                    break;
                }
                case 178:
                case 180: {
                    final int LA71_ = this.input.LA(3);
                    if (LA71_ == 87) {
                        final int LA71_2 = this.input.LA(4);
                        if (LA71_2 == 53) {
                            alt71 = 5;
                        }
                        else {
                            if (LA71_2 != 87) {
                                final NoViableAltException nvae2 = new NoViableAltException("", 71, 8, (IntStream)this.input);
                                throw nvae2;
                            }
                            alt71 = 6;
                        }
                        break;
                    }
                    final NoViableAltException nvae3 = new NoViableAltException("", 71, 6, (IntStream)this.input);
                    throw nvae3;
                }
                case 224:
                case 226: {
                    alt71 = 7;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 71, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt71) {
                case 1: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7108);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7118);
                        cmd = new StateCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7125);
                    if (this.input.LA(1) == 238 || this.input.LA(1) == 249) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7135);
                        cmd = new StateCommand(1, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7142);
                    if (this.input.LA(1) == 239 || this.input.LA(1) == 251) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7152);
                        cmd = new StateCommand(4, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7159);
                    if (this.input.LA(1) == 240 || this.input.LA(1) == 252) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        containerType = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_state_command7171);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7173);
                        cmd = new StateCommand(5, new String[] { (containerType != null) ? containerType.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7180);
                    if (this.input.LA(1) == 178 || this.input.LA(1) == 180) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        stateId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_state_command7192);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7194);
                        cmd = new StateCommand(2, new String[] { (stateId != null) ? stateId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7201);
                    if (this.input.LA(1) == 178 || this.input.LA(1) == 180) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        stateId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_state_command7213);
                        stateLevel = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_state_command7217);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7219);
                        cmd = new StateCommand(2, new String[] { (stateId != null) ? stateId.getText() : null, (stateLevel != null) ? stateLevel.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 147, ModerationCommandParser.FOLLOW_STATE_CMD_in_state_command7226);
                    if (this.input.LA(1) == 224 || this.input.LA(1) == 226) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        stateId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_state_command7238);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_state_command7240);
                        cmd = new StateCommand(3, new String[] { (stateId != null) ? stateId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand spell_command() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 143, ModerationCommandParser.FOLLOW_SPELL_CMD_in_spell_command7258);
            this.match((IntStream)this.input, 231, ModerationCommandParser.FOLLOW_231_in_spell_command7261);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_spell_command7264);
            cmd = new SpellCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand client_game_event_command() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            int alt72 = 2;
            final int LA72_0 = this.input.LA(1);
            if (LA72_0 != 31) {
                final NoViableAltException nvae = new NoViableAltException("", 72, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA72_ = this.input.LA(2);
            if (LA72_ >= 178 && LA72_ <= 179) {
                alt72 = 1;
            }
            else {
                if (LA72_ < 210 || LA72_ > 211) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 72, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt72 = 2;
            }
            switch (alt72) {
                case 1: {
                    this.match((IntStream)this.input, 31, ModerationCommandParser.FOLLOW_CLIENT_GAME_EVENT_CMD_in_client_game_event_command7285);
                    if (this.input.LA(1) >= 178 && this.input.LA(1) <= 179) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_client_game_event_command7297);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_client_game_event_command7299);
                        cmd = new ClientGameEventCommand(1, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 31, ModerationCommandParser.FOLLOW_CLIENT_GAME_EVENT_CMD_in_client_game_event_command7310);
                    if (this.input.LA(1) >= 210 && this.input.LA(1) <= 211) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_client_game_event_command7322);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_client_game_event_command7324);
                        cmd = new ClientGameEventCommand(2, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand gem_command() throws RecognitionException {
        ModerationCommand cmd = null;
        Token itemId = null;
        Token gemId = null;
        Token index = null;
        try {
            int alt73 = 6;
            final int LA73_0 = this.input.LA(1);
            if (LA73_0 != 63) {
                final NoViableAltException nvae = new NoViableAltException("", 73, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt73 = 5;
                    break;
                }
                case 53: {
                    alt73 = 6;
                    break;
                }
                case 323: {
                    alt73 = 1;
                    break;
                }
                case 39: {
                    final int LA73_ = this.input.LA(3);
                    if (LA73_ == 87) {
                        final int LA73_2 = this.input.LA(4);
                        if (LA73_2 == 87) {
                            final int LA73_3 = this.input.LA(5);
                            if (LA73_3 == 87) {
                                alt73 = 2;
                            }
                            else {
                                if (LA73_3 != 53) {
                                    final NoViableAltException nvae2 = new NoViableAltException("", 73, 7, (IntStream)this.input);
                                    throw nvae2;
                                }
                                alt73 = 3;
                            }
                        }
                        else {
                            if (LA73_2 != 53) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 73, 6, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt73 = 4;
                        }
                        break;
                    }
                    final NoViableAltException nvae4 = new NoViableAltException("", 73, 5, (IntStream)this.input);
                    throw nvae4;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 73, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt73) {
                case 1: {
                    this.match((IntStream)this.input, 63, ModerationCommandParser.FOLLOW_GEM_CMD_in_gem_command7345);
                    this.match((IntStream)this.input, 323, ModerationCommandParser.FOLLOW_323_in_gem_command7348);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_gem_command7351);
                    cmd = new GemCommand(0);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 63, ModerationCommandParser.FOLLOW_GEM_CMD_in_gem_command7358);
                    this.match((IntStream)this.input, 39, ModerationCommandParser.FOLLOW_CREATE_ITEM_in_gem_command7361);
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_gem_command7366);
                    gemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_gem_command7370);
                    index = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_gem_command7374);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_gem_command7376);
                    cmd = new GemCommand(1, Integer.parseInt((itemId != null) ? itemId.getText() : null), Integer.parseInt((gemId != null) ? gemId.getText() : null), Integer.parseInt((index != null) ? index.getText() : null));
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 63, ModerationCommandParser.FOLLOW_GEM_CMD_in_gem_command7387);
                    this.match((IntStream)this.input, 39, ModerationCommandParser.FOLLOW_CREATE_ITEM_in_gem_command7390);
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_gem_command7395);
                    gemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_gem_command7399);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_gem_command7401);
                    cmd = new GemCommand(1, Integer.parseInt((itemId != null) ? itemId.getText() : null), Integer.parseInt((gemId != null) ? gemId.getText() : null), -1);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 63, ModerationCommandParser.FOLLOW_GEM_CMD_in_gem_command7412);
                    this.match((IntStream)this.input, 39, ModerationCommandParser.FOLLOW_CREATE_ITEM_in_gem_command7415);
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_gem_command7420);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_gem_command7422);
                    cmd = new GemCommand(1, Integer.parseInt((itemId != null) ? itemId.getText() : null), -1, -1);
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 63, ModerationCommandParser.FOLLOW_GEM_CMD_in_gem_command7433);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_gem_command7443);
                        cmd = new GemCommand(2);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 63, ModerationCommandParser.FOLLOW_GEM_CMD_in_gem_command7454);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_gem_command7456);
                    cmd = new GemCommand(2);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand aptitude_command() throws RecognitionException {
        ModerationCommand cmd = null;
        Token bonusId = null;
        Token level = null;
        Token aptId = null;
        try {
            int alt74 = 7;
            final int LA74_0 = this.input.LA(1);
            if (LA74_0 != 16) {
                final NoViableAltException nvae = new NoViableAltException("", 74, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt74 = 1;
                    break;
                }
                case 364:
                case 386: {
                    alt74 = 2;
                    break;
                }
                case 392:
                case 404: {
                    alt74 = 3;
                    break;
                }
                case 377:
                case 395: {
                    alt74 = 4;
                    break;
                }
                case 272:
                case 280: {
                    alt74 = 5;
                    break;
                }
                case 274:
                case 282: {
                    alt74 = 6;
                    break;
                }
                case 359: {
                    alt74 = 7;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 74, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt74) {
                case 1: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7478);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7486);
                        cmd = new NewAptitudeCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7493);
                    if (this.input.LA(1) == 364 || this.input.LA(1) == 386) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7501);
                        cmd = new NewAptitudeCommand(1, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7508);
                    if (this.input.LA(1) == 392 || this.input.LA(1) == 404) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7516);
                        cmd = new NewAptitudeCommand(2, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7523);
                    if (this.input.LA(1) == 377 || this.input.LA(1) == 395) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        bonusId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_aptitude_command7533);
                        level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_aptitude_command7537);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7539);
                        cmd = new NewAptitudeCommand(3, new String[] { (bonusId != null) ? bonusId.getText() : null, (level != null) ? level.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7546);
                    if (this.input.LA(1) == 272 || this.input.LA(1) == 280) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        bonusId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_aptitude_command7556);
                        level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_aptitude_command7560);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7562);
                        cmd = new NewAptitudeCommand(4, new String[] { (bonusId != null) ? bonusId.getText() : null, (level != null) ? level.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7569);
                    if (this.input.LA(1) == 274 || this.input.LA(1) == 282) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        aptId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_aptitude_command7579);
                        level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_aptitude_command7583);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7585);
                        cmd = new NewAptitudeCommand(7, new String[] { (aptId != null) ? aptId.getText() : null, (level != null) ? level.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 16, ModerationCommandParser.FOLLOW_APTITUDE_in_aptitude_command7592);
                    this.match((IntStream)this.input, 359, ModerationCommandParser.FOLLOW_359_in_aptitude_command7595);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_aptitude_command7598);
                    cmd = new NewAptitudeCommand(5, new String[0]);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand version_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 166, ModerationCommandParser.FOLLOW_VERSION_in_version_cmd7616);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_version_cmd7618);
            cmd = new VersionCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_respawn_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 134, ModerationCommandParser.FOLLOW_SET_RESPAWN_CMD_in_set_respawn_cmd7645);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_respawn_cmd7647);
            cmd = new SetRespawnPointCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand check_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            int alt75 = 3;
            final int LA75_0 = this.input.LA(1);
            if (LA75_0 != 29) {
                final NoViableAltException nvae = new NoViableAltException("", 75, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA75_ = this.input.LA(2);
            if (LA75_ == 177 || LA75_ == 256) {
                final int LA75_2 = this.input.LA(3);
                if (LA75_2 == 87) {
                    alt75 = 1;
                }
                else {
                    if (LA75_2 != 53) {
                        final NoViableAltException nvae2 = new NoViableAltException("", 75, 2, (IntStream)this.input);
                        throw nvae2;
                    }
                    alt75 = 2;
                }
            }
            else {
                if (LA75_ != 174 && LA75_ != 209) {
                    final NoViableAltException nvae3 = new NoViableAltException("", 75, 1, (IntStream)this.input);
                    throw nvae3;
                }
                alt75 = 3;
            }
            switch (alt75) {
                case 1: {
                    this.match((IntStream)this.input, 29, ModerationCommandParser.FOLLOW_CHECK_CMD_in_check_cmd7662);
                    if (this.input.LA(1) == 177 || this.input.LA(1) == 256) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_check_cmd7674);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_check_cmd7676);
                        cmd = new CheckCommand(1, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 29, ModerationCommandParser.FOLLOW_CHECK_CMD_in_check_cmd7687);
                    if (this.input.LA(1) == 177 || this.input.LA(1) == 256) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_check_cmd7697);
                        cmd = new CheckCommand(2, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 29, ModerationCommandParser.FOLLOW_CHECK_CMD_in_check_cmd7708);
                    if (this.input.LA(1) == 174 || this.input.LA(1) == 209) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_check_cmd7718);
                        cmd = new CheckCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand craft_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        Token xp = null;
        try {
            int alt76 = 4;
            final int LA76_0 = this.input.LA(1);
            if (LA76_0 != 36) {
                final NoViableAltException nvae = new NoViableAltException("", 76, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 175:
                case 220: {
                    alt76 = 1;
                    break;
                }
                case 176:
                case 258: {
                    alt76 = 2;
                    break;
                }
                case 173:
                case 184: {
                    alt76 = 3;
                    break;
                }
                case 174:
                case 209: {
                    alt76 = 4;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 76, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt76) {
                case 1: {
                    this.match((IntStream)this.input, 36, ModerationCommandParser.FOLLOW_CRAFT_CMD_in_craft_cmd7737);
                    if (this.input.LA(1) == 175 || this.input.LA(1) == 220) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_craft_cmd7749);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_craft_cmd7751);
                        cmd = new CraftCommand(1, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 36, ModerationCommandParser.FOLLOW_CRAFT_CMD_in_craft_cmd7762);
                    if (this.input.LA(1) == 176 || this.input.LA(1) == 258) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_craft_cmd7774);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_craft_cmd7776);
                        cmd = new CraftCommand(2, new String[] { (id != null) ? id.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 36, ModerationCommandParser.FOLLOW_CRAFT_CMD_in_craft_cmd7787);
                    if (this.input.LA(1) == 173 || this.input.LA(1) == 184) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_craft_cmd7798);
                        xp = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_craft_cmd7802);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_craft_cmd7804);
                        cmd = new CraftCommand(3, new String[] { (id != null) ? id.getText() : null, (xp != null) ? xp.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 36, ModerationCommandParser.FOLLOW_CRAFT_CMD_in_craft_cmd7815);
                    if (this.input.LA(1) == 174 || this.input.LA(1) == 209) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_craft_cmd7825);
                        cmd = new CraftCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ice_status_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 74, ModerationCommandParser.FOLLOW_ICE_STATUS_in_ice_status_cmd7845);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ice_status_cmd7847);
            cmd = new ICEStatusCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand pet_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token delta = null;
        try {
            int alt77 = 2;
            final int LA77_0 = this.input.LA(1);
            if (LA77_0 != 93) {
                final NoViableAltException nvae = new NoViableAltException("", 77, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA77_ = this.input.LA(2);
            if (LA77_ >= 306 && LA77_ <= 307) {
                alt77 = 2;
            }
            else {
                if (LA77_ != 414) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 77, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt77 = 1;
            }
            switch (alt77) {
                case 1: {
                    this.match((IntStream)this.input, 93, ModerationCommandParser.FOLLOW_PET_in_pet_cmd7863);
                    this.match((IntStream)this.input, 414, ModerationCommandParser.FOLLOW_414_in_pet_cmd7866);
                    delta = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_pet_cmd7871);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pet_cmd7873);
                    cmd = new PetCommand(0, new String[] { (delta != null) ? delta.getText() : null });
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 93, ModerationCommandParser.FOLLOW_PET_in_pet_cmd7885);
                    if (this.input.LA(1) >= 306 && this.input.LA(1) <= 307) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_pet_cmd7893);
                        cmd = new PetCommand(1, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand guild_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token delta = null;
        Token bonusId = null;
        Token factor = null;
        Token max = null;
        Token maxPerDay = null;
        Token level = null;
        Token nationId = null;
        message_return desc = null;
        try {
            int alt78 = 15;
            final int LA78_0 = this.input.LA(1);
            if (LA78_0 != 69) {
                final NoViableAltException nvae = new NoViableAltException("", 78, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 338:
                case 342:
                case 345: {
                    alt78 = 1;
                    break;
                }
                case 268:
                case 270: {
                    alt78 = 4;
                    break;
                }
                case 376:
                case 396: {
                    alt78 = 5;
                    break;
                }
                case 378:
                case 399: {
                    alt78 = 6;
                    break;
                }
                case 311:
                case 318: {
                    alt78 = 7;
                    break;
                }
                case 325:
                case 330: {
                    alt78 = 8;
                    break;
                }
                case 339:
                case 343: {
                    alt78 = 9;
                    break;
                }
                case 377:
                case 395: {
                    alt78 = 12;
                    break;
                }
                case 379:
                case 400: {
                    alt78 = 13;
                    break;
                }
                case 290:
                case 291: {
                    alt78 = 14;
                    break;
                }
                case 292:
                case 295: {
                    alt78 = 15;
                    break;
                }
                case 327: {
                    alt78 = 2;
                    break;
                }
                case 148: {
                    alt78 = 3;
                    break;
                }
                case 76: {
                    alt78 = 10;
                    break;
                }
                case 317: {
                    alt78 = 11;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 78, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt78) {
                case 1: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd7915);
                    if (this.input.LA(1) == 338 || this.input.LA(1) == 342 || this.input.LA(1) == 345) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        delta = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd7931);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd7933);
                        cmd = new GuildCommand(0, new String[] { (delta != null) ? delta.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd7941);
                    this.match((IntStream)this.input, 327, ModerationCommandParser.FOLLOW_327_in_guild_cmd7944);
                    delta = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd7949);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd7951);
                    cmd = new GuildCommand(2, new String[] { (delta != null) ? delta.getText() : null });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd7959);
                    this.match((IntStream)this.input, 148, ModerationCommandParser.FOLLOW_STATS_in_guild_cmd7962);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd7965);
                    cmd = new GuildCommand(3, new String[0]);
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd7973);
                    if (this.input.LA(1) == 268 || this.input.LA(1) == 270) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        bonusId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd7985);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd7987);
                        cmd = new GuildCommand(4, new String[] { (bonusId != null) ? bonusId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd7995);
                    if (this.input.LA(1) == 376 || this.input.LA(1) == 396) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_guild_cmd8007);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8009);
                        cmd = new GuildCommand(5, new String[] { (factor != null) ? factor.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8017);
                    if (this.input.LA(1) == 378 || this.input.LA(1) == 399) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        max = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd8029);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8031);
                        cmd = new GuildCommand(6, new String[] { (max != null) ? max.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8039);
                    if (this.input.LA(1) == 311 || this.input.LA(1) == 318) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8049);
                        cmd = new GuildCommand(7, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 8: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8057);
                    if (this.input.LA(1) == 325 || this.input.LA(1) == 330) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        maxPerDay = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd8069);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8071);
                        cmd = new GuildCommand(8, new String[] { (maxPerDay != null) ? maxPerDay.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 9: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8079);
                    if (this.input.LA(1) == 339 || this.input.LA(1) == 343) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        factor = (Token)this.match((IntStream)this.input, 60, ModerationCommandParser.FOLLOW_FLOAT_in_guild_cmd8091);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8093);
                        cmd = new GuildCommand(10, new String[] { (factor != null) ? factor.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 10: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8101);
                    this.match((IntStream)this.input, 76, ModerationCommandParser.FOLLOW_INFO_in_guild_cmd8104);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8107);
                    cmd = new GuildCommand(9, new String[0]);
                    break;
                }
                case 11: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8115);
                    this.match((IntStream)this.input, 317, ModerationCommandParser.FOLLOW_317_in_guild_cmd8118);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8121);
                    cmd = new GuildCommand(12, new String[0]);
                    break;
                }
                case 12: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8129);
                    if (this.input.LA(1) == 377 || this.input.LA(1) == 395) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        level = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd8141);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8143);
                        cmd = new GuildCommand(11, new String[] { (level != null) ? level.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 13: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8151);
                    if (this.input.LA(1) == 379 || this.input.LA(1) == 400) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        nationId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_guild_cmd8163);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8165);
                        cmd = new GuildCommand(15, new String[] { (nationId != null) ? nationId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 14: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8173);
                    if (this.input.LA(1) >= 290 && this.input.LA(1) <= 291) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.pushFollow(ModerationCommandParser.FOLLOW_message_in_guild_cmd8185);
                        desc = this.message();
                        final RecognizerSharedState state = this.state;
                        --state._fsp;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8187);
                        cmd = new GuildCommand(14, new String[] { (desc != null) ? desc.txt : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 15: {
                    this.match((IntStream)this.input, 69, ModerationCommandParser.FOLLOW_GUILD_in_guild_cmd8195);
                    if (this.input.LA(1) == 292 || this.input.LA(1) == 295) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.pushFollow(ModerationCommandParser.FOLLOW_message_in_guild_cmd8207);
                        desc = this.message();
                        final RecognizerSharedState state2 = this.state;
                        --state2._fsp;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_guild_cmd8209);
                        cmd = new GuildCommand(13, new String[] { (desc != null) ? desc.txt : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand companion_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token breedId = null;
        Token companionId = null;
        Token xp = null;
        Token companionBreedId = null;
        Token second = null;
        Token minute = null;
        Token hour = null;
        Token day = null;
        Token pos = null;
        Token itemUid = null;
        Token bagId = null;
        message_return name = null;
        try {
            int alt79 = 17;
            final int LA79_0 = this.input.LA(1);
            if (LA79_0 != 32) {
                final NoViableAltException nvae = new NoViableAltException("", 79, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt79 = 1;
                    break;
                }
                case 322:
                case 323: {
                    alt79 = 2;
                    break;
                }
                case 6:
                case 267: {
                    alt79 = 3;
                    break;
                }
                case 289:
                case 294: {
                    alt79 = 4;
                    break;
                }
                case 412:
                case 413: {
                    alt79 = 5;
                    break;
                }
                case 351:
                case 356: {
                    alt79 = 6;
                    break;
                }
                case 409:
                case 410: {
                    alt79 = 7;
                    break;
                }
                case 278:
                case 286: {
                    alt79 = 8;
                    break;
                }
                case 279:
                case 414: {
                    alt79 = 9;
                    break;
                }
                case 308:
                case 373: {
                    alt79 = 10;
                    break;
                }
                case 384:
                case 388: {
                    alt79 = 11;
                    break;
                }
                case 293:
                case 312:
                case 315: {
                    alt79 = 12;
                    break;
                }
                case 326:
                case 371: {
                    alt79 = 14;
                    break;
                }
                case 374:
                case 383: {
                    alt79 = 15;
                    break;
                }
                case 271:
                case 305: {
                    alt79 = 16;
                    break;
                }
                case 354:
                case 363: {
                    alt79 = 17;
                    break;
                }
                case 333: {
                    alt79 = 13;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 79, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt79) {
                case 1: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8231);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8241);
                        cmd = new CompanionCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8251);
                    if (this.input.LA(1) >= 322 && this.input.LA(1) <= 323) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8261);
                        cmd = new CompanionCommand(3, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8271);
                    if (this.input.LA(1) == 6 || this.input.LA(1) == 267) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        breedId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8283);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8285);
                        cmd = new CompanionCommand(1, new String[] { (breedId != null) ? breedId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8295);
                    if (this.input.LA(1) == 289 || this.input.LA(1) == 294) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8305);
                        cmd = new CompanionCommand(4, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8315);
                    if (this.input.LA(1) >= 412 && this.input.LA(1) <= 413) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8325);
                        cmd = new CompanionCommand(6, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8335);
                    if (this.input.LA(1) == 351 || this.input.LA(1) == 356) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8347);
                        this.pushFollow(ModerationCommandParser.FOLLOW_message_in_companion_cmd8351);
                        name = this.message();
                        final RecognizerSharedState state = this.state;
                        --state._fsp;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8353);
                        cmd = new CompanionCommand(5, new String[] { (companionId != null) ? companionId.getText() : null, (name != null) ? name.txt : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8363);
                    if (this.input.LA(1) >= 409 && this.input.LA(1) <= 410) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8375);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8377);
                        cmd = new CompanionCommand(7, new String[] { (companionId != null) ? companionId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 8: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8387);
                    if (this.input.LA(1) == 278 || this.input.LA(1) == 286) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8399);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8401);
                        cmd = new CompanionCommand(9, new String[] { (companionId != null) ? companionId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 9: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8411);
                    if (this.input.LA(1) == 279 || this.input.LA(1) == 414) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8423);
                        xp = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8427);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8429);
                        cmd = new CompanionCommand(8, new String[] { (companionId != null) ? companionId.getText() : null, (xp != null) ? xp.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 10: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8439);
                    if (this.input.LA(1) == 308 || this.input.LA(1) == 373) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionBreedId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8451);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8453);
                        cmd = new CompanionCommand(12, new String[] { (companionBreedId != null) ? companionBreedId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 11: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8463);
                    if (this.input.LA(1) == 384 || this.input.LA(1) == 388) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8473);
                        cmd = new CompanionCommand(13, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 12: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8483);
                    if (this.input.LA(1) == 293 || this.input.LA(1) == 312 || this.input.LA(1) == 315) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8497);
                        cmd = new CompanionCommand(16, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 13: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8507);
                    this.match((IntStream)this.input, 333, ModerationCommandParser.FOLLOW_333_in_companion_cmd8510);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8513);
                    cmd = new CompanionCommand(14, new String[0]);
                    break;
                }
                case 14: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8523);
                    if (this.input.LA(1) == 326 || this.input.LA(1) == 371) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8533);
                        cmd = new CompanionCommand(15, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 15: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8543);
                    if (this.input.LA(1) == 374 || this.input.LA(1) == 383) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        second = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8555);
                        minute = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8559);
                        hour = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8563);
                        day = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8567);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8569);
                        cmd = new CompanionCommand(17, new String[] { (second != null) ? second.getText() : null, (minute != null) ? minute.getText() : null, (hour != null) ? hour.getText() : null, (day != null) ? day.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 16: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8584);
                    if (this.input.LA(1) == 271 || this.input.LA(1) == 305) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8596);
                        pos = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8600);
                        itemUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8605);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8607);
                        cmd = new CompanionCommand(10, new String[] { (companionId != null) ? companionId.getText() : null, (pos != null) ? pos.getText() : null, (itemUid != null) ? itemUid.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 17: {
                    this.match((IntStream)this.input, 32, ModerationCommandParser.FOLLOW_COMPANION_in_companion_cmd8625);
                    if (this.input.LA(1) == 354 || this.input.LA(1) == 363) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        companionId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8637);
                        itemUid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8642);
                        bagId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8646);
                        pos = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_companion_cmd8650);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_companion_cmd8653);
                        cmd = new CompanionCommand(11, new String[] { (companionId != null) ? companionId.getText() : null, (itemUid != null) ? itemUid.getText() : null, (bagId != null) ? bagId.getText() : null, (pos != null) ? pos.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand hero_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token heroId = null;
        Token xp = null;
        try {
            int alt80 = 3;
            final int LA80_0 = this.input.LA(1);
            if (LA80_0 != 73) {
                final NoViableAltException nvae = new NoViableAltException("", 80, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt80 = 1;
                    break;
                }
                case 279:
                case 414: {
                    alt80 = 2;
                    break;
                }
                case 322:
                case 323: {
                    alt80 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 80, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt80) {
                case 1: {
                    this.match((IntStream)this.input, 73, ModerationCommandParser.FOLLOW_HERO_in_hero_cmd8681);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_hero_cmd8691);
                        cmd = new HeroCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 73, ModerationCommandParser.FOLLOW_HERO_in_hero_cmd8698);
                    if (this.input.LA(1) == 279 || this.input.LA(1) == 414) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        heroId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_hero_cmd8710);
                        xp = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_hero_cmd8714);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_hero_cmd8716);
                        cmd = new HeroCommand(1, new String[] { (heroId != null) ? heroId.getText() : null, (xp != null) ? xp.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 73, ModerationCommandParser.FOLLOW_HERO_in_hero_cmd8723);
                    if (this.input.LA(1) >= 322 && this.input.LA(1) <= 323) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_hero_cmd8733);
                        cmd = new HeroCommand(2, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand system_configuration_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token startDate = null;
        Token endDate = null;
        message_return target = null;
        message_return text = null;
        message_return value = null;
        try {
            int alt82 = 6;
            final int LA82_0 = this.input.LA(1);
            if (LA82_0 != 154) {
                final NoViableAltException nvae = new NoViableAltException("", 82, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt82 = 1;
                    break;
                }
                case 331:
                case 332: {
                    alt82 = 4;
                    break;
                }
                case 386: {
                    alt82 = 2;
                    break;
                }
                case 352: {
                    alt82 = 3;
                    break;
                }
                case 415: {
                    alt82 = 5;
                    break;
                }
                case 341: {
                    alt82 = 6;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 82, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt82) {
                case 1: {
                    this.match((IntStream)this.input, 154, ModerationCommandParser.FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8752);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_system_configuration_cmd8763);
                        cmd = new SystemConfigurationCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 154, ModerationCommandParser.FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8772);
                    this.match((IntStream)this.input, 386, ModerationCommandParser.FOLLOW_386_in_system_configuration_cmd8776);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_system_configuration_cmd8781);
                    target = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_system_configuration_cmd8785);
                    text = this.message();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_system_configuration_cmd8787);
                    cmd = new SystemConfigurationCommand(1, new String[] { (target != null) ? target.txt : null, (text != null) ? text.txt : null });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 154, ModerationCommandParser.FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8796);
                    this.match((IntStream)this.input, 352, ModerationCommandParser.FOLLOW_352_in_system_configuration_cmd8800);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_system_configuration_cmd8805);
                    value = this.message();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_system_configuration_cmd8807);
                    cmd = new SystemConfigurationCommand(2, new String[] { (value != null) ? value.txt : null });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 154, ModerationCommandParser.FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8816);
                    if (this.input.LA(1) >= 331 && this.input.LA(1) <= 332) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.pushFollow(ModerationCommandParser.FOLLOW_message_in_system_configuration_cmd8827);
                        value = this.message();
                        final RecognizerSharedState state4 = this.state;
                        --state4._fsp;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_system_configuration_cmd8829);
                        cmd = new SystemConfigurationCommand(4, new String[] { (value != null) ? value.txt : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 154, ModerationCommandParser.FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8838);
                    this.match((IntStream)this.input, 415, ModerationCommandParser.FOLLOW_415_in_system_configuration_cmd8842);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_system_configuration_cmd8847);
                    value = this.message();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    int alt2 = 2;
                    final int LA81_0 = this.input.LA(1);
                    if (LA81_0 == 41) {
                        alt2 = 1;
                    }
                    switch (alt2) {
                        case 1: {
                            startDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_system_configuration_cmd8852);
                            endDate = (Token)this.match((IntStream)this.input, 41, ModerationCommandParser.FOLLOW_DATE_in_system_configuration_cmd8857);
                            break;
                        }
                    }
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_system_configuration_cmd8861);
                    cmd = new SystemConfigurationCommand(3, new String[] { (value != null) ? value.txt : null, (startDate == null) ? null : ((startDate != null) ? startDate.getText() : null), (endDate == null) ? null : ((endDate != null) ? endDate.getText() : null) });
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 154, ModerationCommandParser.FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8870);
                    this.match((IntStream)this.input, 341, ModerationCommandParser.FOLLOW_341_in_system_configuration_cmd8874);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_system_configuration_cmd8879);
                    value = this.message();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_system_configuration_cmd8881);
                    cmd = new SystemConfigurationCommand(5, new String[] { (value != null) ? value.txt : null });
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand ai_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        message_return serverId = null;
        try {
            int alt83 = 3;
            final int LA83_0 = this.input.LA(1);
            if (LA83_0 != 13) {
                final NoViableAltException nvae = new NoViableAltException("", 83, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt83 = 1;
                    break;
                }
                case 90: {
                    alt83 = 2;
                    break;
                }
                case 89: {
                    alt83 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 83, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt83) {
                case 1: {
                    this.match((IntStream)this.input, 13, ModerationCommandParser.FOLLOW_AI_in_ai_cmd8905);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ai_cmd8915);
                        cmd = new AICommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 13, ModerationCommandParser.FOLLOW_AI_in_ai_cmd8926);
                    this.match((IntStream)this.input, 90, ModerationCommandParser.FOLLOW_ON_in_ai_cmd8928);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_ai_cmd8932);
                    serverId = this.message();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ai_cmd8934);
                    cmd = new AICommand(1, new String[] { (serverId != null) ? serverId.txt : null });
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 13, ModerationCommandParser.FOLLOW_AI_in_ai_cmd8945);
                    this.match((IntStream)this.input, 89, ModerationCommandParser.FOLLOW_OFF_in_ai_cmd8947);
                    this.pushFollow(ModerationCommandParser.FOLLOW_message_in_ai_cmd8951);
                    serverId = this.message();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_ai_cmd8953);
                    cmd = new AICommand(2, new String[] { (serverId != null) ? serverId.txt : null });
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand fightchallenge_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token challengeid = null;
        try {
            int alt84 = 3;
            final int LA84_0 = this.input.LA(1);
            if (LA84_0 != 57) {
                final NoViableAltException nvae = new NoViableAltException("", 84, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 316:
                case 317: {
                    alt84 = 1;
                    break;
                }
                case 322:
                case 323: {
                    alt84 = 2;
                    break;
                }
                case 145:
                case 364: {
                    alt84 = 3;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 84, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt84) {
                case 1: {
                    this.match((IntStream)this.input, 57, ModerationCommandParser.FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd8978);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fightchallenge_cmd8988);
                        cmd = new FightChallengeCommand(0, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 57, ModerationCommandParser.FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd8999);
                    if (this.input.LA(1) >= 322 && this.input.LA(1) <= 323) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fightchallenge_cmd9009);
                        cmd = new FightChallengeCommand(1, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 57, ModerationCommandParser.FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd9020);
                    if (this.input.LA(1) == 145 || this.input.LA(1) == 364) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        challengeid = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_fightchallenge_cmd9032);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_fightchallenge_cmd9034);
                        cmd = new FightChallengeCommand(2, new String[] { (challengeid != null) ? challengeid.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand havenworld_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token x = null;
        Token y = null;
        Token id = null;
        Token havenWorldId = null;
        Token bidValue = null;
        Token guildId = null;
        Token amount = null;
        Token s = null;
        Token min = null;
        Token h = null;
        Token d = null;
        Token m = null;
        Token factor = null;
        try {
            int alt85 = 16;
            final int LA85_0 = this.input.LA(1);
            if (LA85_0 != 71) {
                final NoViableAltException nvae = new NoViableAltException("", 85, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 145: {
                    alt85 = 1;
                    break;
                }
                case 150: {
                    alt85 = 2;
                    break;
                }
                case 411: {
                    final int LA85_ = this.input.LA(3);
                    if (LA85_ == 298) {
                        alt85 = 3;
                    }
                    else {
                        if (LA85_ != 413) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 85, 4, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt85 = 4;
                    }
                    break;
                }
                case 288: {
                    final int LA85_2 = this.input.LA(3);
                    if (LA85_2 == 298) {
                        alt85 = 5;
                    }
                    else {
                        if (LA85_2 != 300) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 85, 5, (IntStream)this.input);
                            throw nvae2;
                        }
                        alt85 = 6;
                    }
                    break;
                }
                case 297: {
                    alt85 = 7;
                    break;
                }
                case 287: {
                    alt85 = 8;
                    break;
                }
                case 365:
                case 407: {
                    alt85 = 9;
                    break;
                }
                case 303:
                case 304: {
                    alt85 = 10;
                    break;
                }
                case 357:
                case 360: {
                    alt85 = 11;
                    break;
                }
                case 375:
                case 385: {
                    alt85 = 12;
                    break;
                }
                case 275:
                case 283: {
                    alt85 = 13;
                    break;
                }
                case 368:
                case 372: {
                    alt85 = 14;
                    break;
                }
                case 366:
                case 370: {
                    alt85 = 15;
                    break;
                }
                case 316:
                case 317: {
                    alt85 = 16;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 85, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt85) {
                case 1: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9055);
                    this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_havenworld_cmd9057);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9059);
                    cmd = new HavenWorldCommand(0, new String[0]);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9067);
                    this.match((IntStream)this.input, 150, ModerationCommandParser.FOLLOW_STOP_in_havenworld_cmd9069);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9071);
                    cmd = new HavenWorldCommand(1, new String[0]);
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9079);
                    this.match((IntStream)this.input, 411, ModerationCommandParser.FOLLOW_411_in_havenworld_cmd9081);
                    this.match((IntStream)this.input, 298, ModerationCommandParser.FOLLOW_298_in_havenworld_cmd9083);
                    x = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9087);
                    y = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9091);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9093);
                    cmd = new HavenWorldCommand(4, new String[] { (x != null) ? x.getText() : null, (y != null) ? y.getText() : null });
                    break;
                }
                case 4: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9101);
                    this.match((IntStream)this.input, 411, ModerationCommandParser.FOLLOW_411_in_havenworld_cmd9103);
                    this.match((IntStream)this.input, 413, ModerationCommandParser.FOLLOW_413_in_havenworld_cmd9105);
                    x = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9109);
                    y = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9113);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9117);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9119);
                    cmd = new HavenWorldCommand(5, new String[] { (x != null) ? x.getText() : null, (y != null) ? y.getText() : null, (id != null) ? id.getText() : null });
                    break;
                }
                case 5: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9127);
                    this.match((IntStream)this.input, 288, ModerationCommandParser.FOLLOW_288_in_havenworld_cmd9129);
                    this.match((IntStream)this.input, 298, ModerationCommandParser.FOLLOW_298_in_havenworld_cmd9131);
                    x = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9135);
                    y = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9139);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9143);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9145);
                    cmd = new HavenWorldCommand(6, new String[] { (x != null) ? x.getText() : null, (y != null) ? y.getText() : null, (id != null) ? id.getText() : null });
                    break;
                }
                case 6: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9153);
                    this.match((IntStream)this.input, 288, ModerationCommandParser.FOLLOW_288_in_havenworld_cmd9155);
                    this.match((IntStream)this.input, 300, ModerationCommandParser.FOLLOW_300_in_havenworld_cmd9157);
                    id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9161);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9163);
                    cmd = new HavenWorldCommand(7, new String[] { (id != null) ? id.getText() : null });
                    break;
                }
                case 7: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9171);
                    this.match((IntStream)this.input, 297, ModerationCommandParser.FOLLOW_297_in_havenworld_cmd9173);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9175);
                    cmd = new HavenWorldCommand(2, new String[0]);
                    break;
                }
                case 8: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9183);
                    this.match((IntStream)this.input, 287, ModerationCommandParser.FOLLOW_287_in_havenworld_cmd9185);
                    havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9189);
                    bidValue = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9193);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9195);
                    cmd = new HavenWorldCommand(8, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null, (bidValue != null) ? bidValue.getText() : null });
                    break;
                }
                case 9: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9210);
                    if (this.input.LA(1) == 365 || this.input.LA(1) == 407) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9222);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9224);
                        cmd = new HavenWorldCommand(9, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 10: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9232);
                    if (this.input.LA(1) >= 303 && this.input.LA(1) <= 304) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9244);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9246);
                        cmd = new HavenWorldCommand(10, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 11: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9258);
                    if (this.input.LA(1) == 357 || this.input.LA(1) == 360) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9270);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9272);
                        cmd = new HavenWorldCommand(11, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 12: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9288);
                    if (this.input.LA(1) == 375 || this.input.LA(1) == 385) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9300);
                        guildId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9304);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9306);
                        cmd = new HavenWorldCommand(12, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null, (guildId != null) ? guildId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 13: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9322);
                    if (this.input.LA(1) == 275 || this.input.LA(1) == 283) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9334);
                        amount = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9338);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9340);
                        cmd = new HavenWorldCommand(15, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null, (amount != null) ? amount.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 14: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9356);
                    if (this.input.LA(1) == 368 || this.input.LA(1) == 372) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9368);
                        s = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9372);
                        min = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9376);
                        h = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9380);
                        d = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9384);
                        m = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9388);
                        y = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9392);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9394);
                        cmd = new HavenWorldCommand(13, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null, (s != null) ? s.getText() : null, (min != null) ? min.getText() : null, (h != null) ? h.getText() : null, (d != null) ? d.getText() : null, (m != null) ? m.getText() : null, (y != null) ? y.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 15: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9413);
                    if (this.input.LA(1) == 366 || this.input.LA(1) == 370) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        havenWorldId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9425);
                        factor = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_havenworld_cmd9429);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9431);
                        cmd = new HavenWorldCommand(16, new String[] { (havenWorldId != null) ? havenWorldId.getText() : null, (factor != null) ? factor.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 16: {
                    this.match((IntStream)this.input, 71, ModerationCommandParser.FOLLOW_HAVEN_WORLD_in_havenworld_cmd9450);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_havenworld_cmd9460);
                        cmd = new HavenWorldCommand(14, new String[0]);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand almanach_start_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token entryId = null;
        try {
            this.match((IntStream)this.input, 15, ModerationCommandParser.FOLLOW_ALMANACH_in_almanach_start_cmd9484);
            this.match((IntStream)this.input, 145, ModerationCommandParser.FOLLOW_START_in_almanach_start_cmd9486);
            entryId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_almanach_start_cmd9490);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_almanach_start_cmd9492);
            cmd = new AlmanachStartEventCommand(Integer.parseInt((entryId != null) ? entryId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand learn_emote_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token emote_id = null;
        try {
            this.match((IntStream)this.input, 80, ModerationCommandParser.FOLLOW_LEARN_EMOTE_in_learn_emote_cmd9511);
            emote_id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_learn_emote_cmd9515);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_learn_emote_cmd9517);
            cmd = new LearnEmoteCommand(Integer.parseInt((emote_id != null) ? emote_id.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand set_player_title_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token title_id = null;
        character_pattern_return player = null;
        try {
            this.match((IntStream)this.input, 131, ModerationCommandParser.FOLLOW_SET_PLAYER_TITLE_in_set_player_title_cmd9540);
            title_id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_set_player_title_cmd9544);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_set_player_title_cmd9548);
            player = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_set_player_title_cmd9550);
            cmd = new SetPlayerTitleCommand(Integer.parseInt((title_id != null) ? title_id.getText() : null), (player != null) ? player.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand inventory_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token bagId = null;
        Token refId = null;
        Token qty = null;
        List<Integer> priorities = null;
        try {
            int alt88 = 7;
            final int LA88_0 = this.input.LA(1);
            if (LA88_0 != 78) {
                final NoViableAltException nvae = new NoViableAltException("", 88, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 199:
                case 202: {
                    alt88 = 1;
                    break;
                }
                case 316:
                case 317: {
                    alt88 = 2;
                    break;
                }
                case 238:
                case 253: {
                    alt88 = 3;
                    break;
                }
                case 229:
                case 235: {
                    alt88 = 4;
                    break;
                }
                case 230:
                case 236: {
                    alt88 = 5;
                    break;
                }
                case 197:
                case 198: {
                    alt88 = 6;
                    break;
                }
                case 227:
                case 237: {
                    alt88 = 7;
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 88, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt88) {
                case 1: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9573);
                    if (this.input.LA(1) == 199 || this.input.LA(1) == 202) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9583);
                        cmd = new InventoryCommand(1);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 2: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9593);
                    if (this.input.LA(1) >= 316 && this.input.LA(1) <= 317) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9603);
                        cmd = new InventoryCommand(0);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 3: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9613);
                    if (this.input.LA(1) == 238 || this.input.LA(1) == 253) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9623);
                        cmd = new InventoryCommand(3);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 4: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9633);
                    if (this.input.LA(1) == 229 || this.input.LA(1) == 235) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        int alt2 = 2;
                        final int LA86_0 = this.input.LA(1);
                        if (LA86_0 == 87) {
                            alt2 = 1;
                        }
                        switch (alt2) {
                            case 1: {
                                this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_inventory_cmd9645);
                                priorities = this.id_list_pattern();
                                final RecognizerSharedState state = this.state;
                                --state._fsp;
                                break;
                            }
                        }
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9648);
                        cmd = new InventoryCommand(2, priorities);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 5: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9658);
                    if (this.input.LA(1) == 230 || this.input.LA(1) == 236) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        bagId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_inventory_cmd9670);
                        int alt3 = 2;
                        final int LA87_0 = this.input.LA(1);
                        if (LA87_0 == 87) {
                            alt3 = 1;
                        }
                        switch (alt3) {
                            case 1: {
                                this.pushFollow(ModerationCommandParser.FOLLOW_id_list_pattern_in_inventory_cmd9674);
                                priorities = this.id_list_pattern();
                                final RecognizerSharedState state2 = this.state;
                                --state2._fsp;
                                break;
                            }
                        }
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9677);
                        cmd = new InventoryCommand(2, priorities, new String[] { (bagId != null) ? bagId.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 6: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9687);
                    if (this.input.LA(1) >= 197 && this.input.LA(1) <= 198) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9697);
                        cmd = new InventoryCommand(4);
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
                case 7: {
                    this.match((IntStream)this.input, 78, ModerationCommandParser.FOLLOW_INVENTORY_in_inventory_cmd9707);
                    if (this.input.LA(1) == 227 || this.input.LA(1) == 237) {
                        this.input.consume();
                        this.state.errorRecovery = false;
                        refId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_inventory_cmd9719);
                        qty = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_inventory_cmd9723);
                        this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_inventory_cmd9725);
                        cmd = new InventoryCommand(5, new String[] { (refId != null) ? refId.getText() : null, (qty != null) ? qty.getText() : null });
                        break;
                    }
                    final MismatchedSetException mse = new MismatchedSetException((BitSet)null, (IntStream)this.input);
                    throw mse;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand empty_char_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 50, ModerationCommandParser.FOLLOW_EMPTY_CHAR_in_empty_char_cmd9749);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_empty_char_cmd9751);
            cmd = new GenericCommand((short)144);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand popup_message_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token msg = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 98, ModerationCommandParser.FOLLOW_POPUP_MESSAGE_in_popup_message_cmd9779);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_popup_message_cmd9783);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            msg = (Token)this.match((IntStream)this.input, 56, ModerationCommandParser.FOLLOW_ESCAPED_STRING_in_popup_message_cmd9787);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_popup_message_cmd9789);
            cmd = new PopupMessageCommand((name != null) ? name.pattern : null, (msg != null) ? msg.getText() : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand red_message_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token msg = null;
        try {
            this.match((IntStream)this.input, 105, ModerationCommandParser.FOLLOW_RED_MESSAGE_in_red_message_cmd9817);
            msg = (Token)this.match((IntStream)this.input, 56, ModerationCommandParser.FOLLOW_ESCAPED_STRING_in_red_message_cmd9821);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_red_message_cmd9823);
            cmd = new RedMessageCommand((msg != null) ? msg.getText() : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand red_message_to_player_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token msg = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 106, ModerationCommandParser.FOLLOW_RED_MESSAGE_TO_PLAYER_in_red_message_to_player_cmd9851);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_red_message_to_player_cmd9855);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            msg = (Token)this.match((IntStream)this.input, 56, ModerationCommandParser.FOLLOW_ESCAPED_STRING_in_red_message_to_player_cmd9859);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_red_message_to_player_cmd9861);
            cmd = new RedMessageToPlayerCommand((name != null) ? name.pattern : null, (msg != null) ? msg.getText() : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand emote_targetable_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token targetable = null;
        try {
            this.match((IntStream)this.input, 49, ModerationCommandParser.FOLLOW_EMOTE_TARGETABLE_in_emote_targetable_cmd9889);
            targetable = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_emote_targetable_cmd9893);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_emote_targetable_cmd9895);
            cmd = new EmoteTargetableCommand(Integer.parseInt((targetable != null) ? targetable.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand haven_bag_kick_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token characterId = null;
        character_pattern_return name = null;
        try {
            int alt89 = 2;
            final int LA89_0 = this.input.LA(1);
            if (LA89_0 != 70) {
                final NoViableAltException nvae = new NoViableAltException("", 89, 0, (IntStream)this.input);
                throw nvae;
            }
            final int LA89_ = this.input.LA(2);
            if (LA89_ == 87) {
                alt89 = 1;
            }
            else {
                if (LA89_ != 4 && LA89_ != 27 && LA89_ != 56) {
                    final NoViableAltException nvae2 = new NoViableAltException("", 89, 1, (IntStream)this.input);
                    throw nvae2;
                }
                alt89 = 2;
            }
            switch (alt89) {
                case 1: {
                    this.match((IntStream)this.input, 70, ModerationCommandParser.FOLLOW_HAVEN_BAG_KICK_in_haven_bag_kick_cmd9923);
                    characterId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_haven_bag_kick_cmd9927);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_haven_bag_kick_cmd9929);
                    cmd = new HavenBagKickCommand(Long.parseLong((characterId != null) ? characterId.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 70, ModerationCommandParser.FOLLOW_HAVEN_BAG_KICK_in_haven_bag_kick_cmd9939);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_haven_bag_kick_cmd9943);
                    name = this.character_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    cmd = new HavenBagKickCommand((name != null) ? name.pattern : null);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand tp_to_jail_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token time = null;
        character_pattern_return name = null;
        try {
            int alt90 = 3;
            final int LA90_0 = this.input.LA(1);
            if (LA90_0 != 160) {
                final NoViableAltException nvae = new NoViableAltException("", 90, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 27: {
                    final int LA90_ = this.input.LA(3);
                    if (LA90_ == 53) {
                        alt90 = 1;
                    }
                    else {
                        if (LA90_ != 87) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 90, 2, (IntStream)this.input);
                            throw nvae2;
                        }
                        final int LA90_2 = this.input.LA(4);
                        if (LA90_2 == 265) {
                            alt90 = 2;
                        }
                        else {
                            if (LA90_2 != 266) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 90, 6, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt90 = 3;
                        }
                    }
                    break;
                }
                case 56: {
                    final int LA90_3 = this.input.LA(3);
                    if (LA90_3 == 53) {
                        alt90 = 1;
                    }
                    else {
                        if (LA90_3 != 87) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 90, 3, (IntStream)this.input);
                            throw nvae2;
                        }
                        final int LA90_2 = this.input.LA(4);
                        if (LA90_2 == 265) {
                            alt90 = 2;
                        }
                        else {
                            if (LA90_2 != 266) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 90, 6, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt90 = 3;
                        }
                    }
                    break;
                }
                case 4: {
                    final int LA90_4 = this.input.LA(3);
                    if (LA90_4 == 53) {
                        alt90 = 1;
                    }
                    else {
                        if (LA90_4 != 87) {
                            final NoViableAltException nvae2 = new NoViableAltException("", 90, 4, (IntStream)this.input);
                            throw nvae2;
                        }
                        final int LA90_2 = this.input.LA(4);
                        if (LA90_2 == 265) {
                            alt90 = 2;
                        }
                        else {
                            if (LA90_2 != 266) {
                                final NoViableAltException nvae3 = new NoViableAltException("", 90, 6, (IntStream)this.input);
                                throw nvae3;
                            }
                            alt90 = 3;
                        }
                    }
                    break;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 90, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt90) {
                case 1: {
                    this.match((IntStream)this.input, 160, ModerationCommandParser.FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd9967);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_tp_to_jail_cmd9971);
                    name = this.character_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_tp_to_jail_cmd9973);
                    cmd = new TpToJailCommand((name != null) ? name.pattern : null);
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 160, ModerationCommandParser.FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd9983);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_tp_to_jail_cmd9987);
                    name = this.character_pattern();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    time = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_tp_to_jail_cmd9991);
                    this.match((IntStream)this.input, 265, ModerationCommandParser.FOLLOW_265_in_tp_to_jail_cmd9993);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_tp_to_jail_cmd9995);
                    cmd = new TpToJailCommand((name != null) ? name.pattern : null, Integer.parseInt((time != null) ? time.getText() : null), "IG");
                    break;
                }
                case 3: {
                    this.match((IntStream)this.input, 160, ModerationCommandParser.FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd10005);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_tp_to_jail_cmd10009);
                    name = this.character_pattern();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    time = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_tp_to_jail_cmd10013);
                    this.match((IntStream)this.input, 266, ModerationCommandParser.FOLLOW_266_in_tp_to_jail_cmd10015);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_tp_to_jail_cmd10017);
                    cmd = new TpToJailCommand((name != null) ? name.pattern : null, Integer.parseInt((time != null) ? time.getText() : null), "IRL");
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand freedom_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 61, ModerationCommandParser.FOLLOW_FREEDOM_in_freedom_cmd10045);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_freedom_cmd10049);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            cmd = new FreedomCommand((name != null) ? name.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand web_browser_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        try {
            this.match((IntStream)this.input, 168, ModerationCommandParser.FOLLOW_WEB_BROWSER_in_web_browser_cmd10077);
            cmd = new WebBrowserCommand();
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand listloot_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token id = null;
        try {
            this.match((IntStream)this.input, 81, ModerationCommandParser.FOLLOW_LIST_LOOT_in_listloot_cmd10105);
            id = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_listloot_cmd10109);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_listloot_cmd10111);
            cmd = new ListLootCommand(Short.parseShort((id != null) ? id.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand revive_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        character_pattern_return name = null;
        try {
            this.match((IntStream)this.input, 119, ModerationCommandParser.FOLLOW_REVIVE_in_revive_cmd10139);
            this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_revive_cmd10143);
            name = this.character_pattern();
            final RecognizerSharedState state = this.state;
            --state._fsp;
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_revive_cmd10145);
            cmd = new ReviveCommand((name != null) ? name.pattern : null);
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand give_item_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token itemId = null;
        Token quantity = null;
        character_pattern_return name = null;
        try {
            int alt91 = 2;
            final int LA91_0 = this.input.LA(1);
            if (LA91_0 != 67) {
                final NoViableAltException nvae = new NoViableAltException("", 91, 0, (IntStream)this.input);
                throw nvae;
            }
            switch (this.input.LA(2)) {
                case 27: {
                    final int LA91_ = this.input.LA(3);
                    if (LA91_ == 87) {
                        final int LA91_2 = this.input.LA(4);
                        if (LA91_2 == 87) {
                            alt91 = 1;
                        }
                        else {
                            if (LA91_2 != 53) {
                                final NoViableAltException nvae2 = new NoViableAltException("", 91, 5, (IntStream)this.input);
                                throw nvae2;
                            }
                            alt91 = 2;
                        }
                        break;
                    }
                    final NoViableAltException nvae3 = new NoViableAltException("", 91, 2, (IntStream)this.input);
                    throw nvae3;
                }
                case 56: {
                    final int LA91_3 = this.input.LA(3);
                    if (LA91_3 == 87) {
                        final int LA91_2 = this.input.LA(4);
                        if (LA91_2 == 87) {
                            alt91 = 1;
                        }
                        else {
                            if (LA91_2 != 53) {
                                final NoViableAltException nvae2 = new NoViableAltException("", 91, 5, (IntStream)this.input);
                                throw nvae2;
                            }
                            alt91 = 2;
                        }
                        break;
                    }
                    final NoViableAltException nvae3 = new NoViableAltException("", 91, 3, (IntStream)this.input);
                    throw nvae3;
                }
                case 4: {
                    final int LA91_4 = this.input.LA(3);
                    if (LA91_4 == 87) {
                        final int LA91_2 = this.input.LA(4);
                        if (LA91_2 == 87) {
                            alt91 = 1;
                        }
                        else {
                            if (LA91_2 != 53) {
                                final NoViableAltException nvae2 = new NoViableAltException("", 91, 5, (IntStream)this.input);
                                throw nvae2;
                            }
                            alt91 = 2;
                        }
                        break;
                    }
                    final NoViableAltException nvae3 = new NoViableAltException("", 91, 4, (IntStream)this.input);
                    throw nvae3;
                }
                default: {
                    final NoViableAltException nvae = new NoViableAltException("", 91, 1, (IntStream)this.input);
                    throw nvae;
                }
            }
            switch (alt91) {
                case 1: {
                    this.match((IntStream)this.input, 67, ModerationCommandParser.FOLLOW_GIVE_ITEM_in_give_item_cmd10165);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_give_item_cmd10169);
                    name = this.character_pattern();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_give_item_cmd10173);
                    quantity = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_give_item_cmd10177);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_give_item_cmd10179);
                    cmd = new GiveItemCommand((name != null) ? name.pattern : null, Integer.parseInt((itemId != null) ? itemId.getText() : null), Short.parseShort((quantity != null) ? quantity.getText() : null));
                    break;
                }
                case 2: {
                    this.match((IntStream)this.input, 67, ModerationCommandParser.FOLLOW_GIVE_ITEM_in_give_item_cmd10185);
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_pattern_in_give_item_cmd10189);
                    name = this.character_pattern();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    itemId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_give_item_cmd10193);
                    this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_give_item_cmd10195);
                    cmd = new GiveItemCommand((name != null) ? name.pattern : null, Integer.parseInt((itemId != null) ? itemId.getText() : null), (short)1);
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand reset_account_market_entries_cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        Token accountId = null;
        try {
            this.match((IntStream)this.input, 114, ModerationCommandParser.FOLLOW_RESET_ACCOUNT_MARKET_ENTRIES_in_reset_account_market_entries_cmd10212);
            accountId = (Token)this.match((IntStream)this.input, 87, ModerationCommandParser.FOLLOW_NUMBER_in_reset_account_market_entries_cmd10216);
            this.match((IntStream)this.input, 53, ModerationCommandParser.FOLLOW_ENDLINE_in_reset_account_market_entries_cmd10218);
            cmd = new ResetAccountMarketEntriesCommand(Long.parseLong((accountId != null) ? accountId.getText() : null));
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    public final ModerationCommand cmd() throws RecognitionException {
        ModerationCommand cmd = null;
        ModerationCommand c = null;
        try {
            int alt92 = 132;
            alt92 = this.dfa92.predict((IntStream)this.input);
            switch (alt92) {
                case 1: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_stats_cmd_in_cmd10235);
                    c = this.stats_cmd();
                    final RecognizerSharedState state = this.state;
                    --state._fsp;
                    cmd = c;
                    break;
                }
                case 2: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_bot_cmd_in_cmd10266);
                    c = this.bot_cmd();
                    final RecognizerSharedState state2 = this.state;
                    --state2._fsp;
                    cmd = c;
                    break;
                }
                case 3: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_panel_cmd_in_cmd10278);
                    c = this.panel_cmd();
                    final RecognizerSharedState state3 = this.state;
                    --state3._fsp;
                    cmd = c;
                    break;
                }
                case 4: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ping_cmd_in_cmd10290);
                    c = this.ping_cmd();
                    final RecognizerSharedState state4 = this.state;
                    --state4._fsp;
                    cmd = c;
                    break;
                }
                case 5: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_time_cmd_in_cmd10302);
                    c = this.time_cmd();
                    final RecognizerSharedState state5 = this.state;
                    --state5._fsp;
                    cmd = c;
                    break;
                }
                case 6: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_who_cmd_in_cmd10314);
                    c = this.who_cmd();
                    final RecognizerSharedState state6 = this.state;
                    --state6._fsp;
                    cmd = c;
                    break;
                }
                case 7: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_where_cmd_in_cmd10326);
                    c = this.where_cmd();
                    final RecognizerSharedState state7 = this.state;
                    --state7._fsp;
                    cmd = c;
                    break;
                }
                case 8: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_to_player_cmd_in_cmd10358);
                    c = this.teleport_to_player_cmd();
                    final RecognizerSharedState state8 = this.state;
                    --state8._fsp;
                    cmd = c;
                    break;
                }
                case 9: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_to_coords_cmd_in_cmd10377);
                    c = this.teleport_to_coords_cmd();
                    final RecognizerSharedState state9 = this.state;
                    --state9._fsp;
                    cmd = c;
                    break;
                }
                case 10: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_to_inst_cmd_in_cmd10395);
                    c = this.teleport_to_inst_cmd();
                    final RecognizerSharedState state10 = this.state;
                    --state10._fsp;
                    cmd = c;
                    break;
                }
                case 11: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_player_to_me_cmd_in_cmd10416);
                    c = this.teleport_player_to_me_cmd();
                    final RecognizerSharedState state11 = this.state;
                    --state11._fsp;
                    cmd = c;
                    break;
                }
                case 12: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_player_to_coords_cmd_in_cmd10431);
                    c = this.teleport_player_to_coords_cmd();
                    final RecognizerSharedState state12 = this.state;
                    --state12._fsp;
                    cmd = c;
                    break;
                }
                case 13: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_player_to_instance_cmd_in_cmd10443);
                    c = this.teleport_player_to_instance_cmd();
                    final RecognizerSharedState state13 = this.state;
                    --state13._fsp;
                    cmd = c;
                    break;
                }
                case 14: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_serverlock_cmd_in_cmd10453);
                    c = this.serverlock_cmd();
                    final RecognizerSharedState state14 = this.state;
                    --state14._fsp;
                    cmd = c;
                    break;
                }
                case 15: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_kick_cmd_in_cmd10480);
                    c = this.kick_cmd();
                    final RecognizerSharedState state15 = this.state;
                    --state15._fsp;
                    cmd = c;
                    break;
                }
                case 16: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ban_cmd_in_cmd10512);
                    c = this.ban_cmd();
                    final RecognizerSharedState state16 = this.state;
                    --state16._fsp;
                    cmd = c;
                    break;
                }
                case 17: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ghostcheck_cmd_in_cmd10545);
                    c = this.ghostcheck_cmd();
                    final RecognizerSharedState state17 = this.state;
                    --state17._fsp;
                    cmd = c;
                    break;
                }
                case 18: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_identphase_cmd_in_cmd10556);
                    c = this.identphase_cmd();
                    final RecognizerSharedState state18 = this.state;
                    --state18._fsp;
                    cmd = c;
                    break;
                }
                case 19: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_shutdown_cmd_in_cmd10567);
                    c = this.shutdown_cmd();
                    final RecognizerSharedState state19 = this.state;
                    --state19._fsp;
                    cmd = c;
                    break;
                }
                case 20: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_sysmsg_cmd_in_cmd10578);
                    c = this.sysmsg_cmd();
                    final RecognizerSharedState state20 = this.state;
                    --state20._fsp;
                    cmd = c;
                    break;
                }
                case 21: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_msgall_cmd_in_cmd10590);
                    c = this.msgall_cmd();
                    final RecognizerSharedState state21 = this.state;
                    --state21._fsp;
                    cmd = c;
                    break;
                }
                case 22: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_symbiot_cmd_in_cmd10602);
                    c = this.symbiot_cmd();
                    final RecognizerSharedState state22 = this.state;
                    --state22._fsp;
                    cmd = c;
                    break;
                }
                case 23: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_nation_cmd_in_cmd10614);
                    c = this.nation_cmd();
                    final RecognizerSharedState state23 = this.state;
                    --state23._fsp;
                    cmd = c;
                    break;
                }
                case 24: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_achievement_cmd_in_cmd10626);
                    c = this.achievement_cmd();
                    final RecognizerSharedState state24 = this.state;
                    --state24._fsp;
                    cmd = c;
                    break;
                }
                case 25: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_achievement_date_cmd_in_cmd10637);
                    c = this.achievement_date_cmd();
                    final RecognizerSharedState state25 = this.state;
                    --state25._fsp;
                    cmd = c;
                    break;
                }
                case 26: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_zone_buff_cmd_in_cmd10647);
                    c = this.zone_buff_cmd();
                    final RecognizerSharedState state26 = this.state;
                    --state26._fsp;
                    cmd = c;
                    break;
                }
                case 27: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_create_group_cmd_in_cmd10658);
                    c = this.create_group_cmd();
                    final RecognizerSharedState state27 = this.state;
                    --state27._fsp;
                    cmd = c;
                    break;
                }
                case 28: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_add_to_group_cmd_in_cmd10669);
                    c = this.add_to_group_cmd();
                    final RecognizerSharedState state28 = this.state;
                    --state28._fsp;
                    cmd = c;
                    break;
                }
                case 29: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_rights_cmd_in_cmd10680);
                    c = this.rights_cmd();
                    final RecognizerSharedState state29 = this.state;
                    --state29._fsp;
                    cmd = c;
                    break;
                }
                case 30: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_chaos_cmd_in_cmd10692);
                    c = this.chaos_cmd();
                    final RecognizerSharedState state30 = this.state;
                    --state30._fsp;
                    cmd = c;
                    break;
                }
                case 31: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_restart_chaos_cmd_in_cmd10704);
                    c = this.restart_chaos_cmd();
                    final RecognizerSharedState state31 = this.state;
                    --state31._fsp;
                    cmd = c;
                    break;
                }
                case 32: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_create_item_cmd_in_cmd10715);
                    c = this.create_item_cmd();
                    final RecognizerSharedState state32 = this.state;
                    --state32._fsp;
                    cmd = c;
                    break;
                }
                case 33: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_create_set_cmd_in_cmd10726);
                    c = this.create_set_cmd();
                    final RecognizerSharedState state33 = this.state;
                    --state33._fsp;
                    cmd = c;
                    break;
                }
                case 34: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_delete_item_cmd_in_cmd10737);
                    c = this.delete_item_cmd();
                    final RecognizerSharedState state34 = this.state;
                    --state34._fsp;
                    cmd = c;
                    break;
                }
                case 35: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_regenerate_cmd_in_cmd10748);
                    c = this.regenerate_cmd();
                    final RecognizerSharedState state35 = this.state;
                    --state35._fsp;
                    cmd = c;
                    break;
                }
                case 36: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_regenerate_with_item_cmd_in_cmd10759);
                    c = this.regenerate_with_item_cmd();
                    final RecognizerSharedState state36 = this.state;
                    --state36._fsp;
                    cmd = c;
                    break;
                }
                case 37: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_turn_duration_cmd_in_cmd10769);
                    c = this.turn_duration_cmd();
                    final RecognizerSharedState state37 = this.state;
                    --state37._fsp;
                    cmd = c;
                    break;
                }
                case 38: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_pvp_cmd_in_cmd10780);
                    c = this.pvp_cmd();
                    final RecognizerSharedState state38 = this.state;
                    --state38._fsp;
                    cmd = c;
                    break;
                }
                case 39: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_run_action_cmd_in_cmd10792);
                    c = this.run_action_cmd();
                    final RecognizerSharedState state39 = this.state;
                    --state39._fsp;
                    cmd = c;
                    break;
                }
                case 40: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_end_scenario_cmd_in_cmd10803);
                    c = this.end_scenario_cmd();
                    final RecognizerSharedState state40 = this.state;
                    --state40._fsp;
                    cmd = c;
                    break;
                }
                case 41: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_reload_scenarios_cmd_in_cmd10814);
                    c = this.reload_scenarios_cmd();
                    final RecognizerSharedState state41 = this.state;
                    --state41._fsp;
                    cmd = c;
                    break;
                }
                case 42: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_add_spellxp_cmd_in_cmd10824);
                    c = this.add_spellxp_cmd();
                    final RecognizerSharedState state42 = this.state;
                    --state42._fsp;
                    cmd = c;
                    break;
                }
                case 43: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_spelllevel_cmd_in_cmd10835);
                    c = this.set_spelllevel_cmd();
                    final RecognizerSharedState state43 = this.state;
                    --state43._fsp;
                    cmd = c;
                    break;
                }
                case 44: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_add_skillxp_cmd_in_cmd10846);
                    c = this.add_skillxp_cmd();
                    final RecognizerSharedState state44 = this.state;
                    --state44._fsp;
                    cmd = c;
                    break;
                }
                case 45: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_skill_level_cmd_in_cmd10857);
                    c = this.set_skill_level_cmd();
                    final RecognizerSharedState state45 = this.state;
                    --state45._fsp;
                    cmd = c;
                    break;
                }
                case 46: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_add_xp_cmd_in_cmd10868);
                    c = this.add_xp_cmd();
                    final RecognizerSharedState state46 = this.state;
                    --state46._fsp;
                    cmd = c;
                    break;
                }
                case 47: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_bonus_factor_cmd_in_cmd10880);
                    c = this.set_bonus_factor_cmd();
                    final RecognizerSharedState state47 = this.state;
                    --state47._fsp;
                    cmd = c;
                    break;
                }
                case 48: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_add_money_cmd_in_cmd10890);
                    c = this.add_money_cmd();
                    final RecognizerSharedState state48 = this.state;
                    --state48._fsp;
                    cmd = c;
                    break;
                }
                case 49: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_help_cmd_in_cmd10901);
                    c = this.help_cmd();
                    final RecognizerSharedState state49 = this.state;
                    --state49._fsp;
                    cmd = c;
                    break;
                }
                case 50: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_god_mode_cmd_in_cmd10913);
                    c = this.god_mode_cmd();
                    final RecognizerSharedState state50 = this.state;
                    --state50._fsp;
                    cmd = c;
                    break;
                }
                case 51: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_instance_usage_cmd_in_cmd10924);
                    c = this.instance_usage_cmd();
                    final RecognizerSharedState state51 = this.state;
                    --state51._fsp;
                    cmd = c;
                    break;
                }
                case 52: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_destroy_instance_cmd_in_cmd10935);
                    c = this.destroy_instance_cmd();
                    final RecognizerSharedState state52 = this.state;
                    --state52._fsp;
                    cmd = c;
                    break;
                }
                case 53: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_show_aggro_list_cmd_in_cmd10945);
                    c = this.show_aggro_list_cmd();
                    final RecognizerSharedState state53 = this.state;
                    --state53._fsp;
                    cmd = c;
                    break;
                }
                case 54: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_play_animation_cmd_in_cmd10956);
                    c = this.play_animation_cmd();
                    final RecognizerSharedState state54 = this.state;
                    --state54._fsp;
                    cmd = c;
                    break;
                }
                case 55: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_play_aps_cmd_in_cmd10967);
                    c = this.play_aps_cmd();
                    final RecognizerSharedState state55 = this.state;
                    --state55._fsp;
                    cmd = c;
                    break;
                }
                case 56: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_level_cmd_in_cmd10978);
                    c = this.set_level_cmd();
                    final RecognizerSharedState state56 = this.state;
                    --state56._fsp;
                    cmd = c;
                    break;
                }
                case 57: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_spawn_ie_cmd_in_cmd10989);
                    c = this.spawn_ie_cmd();
                    final RecognizerSharedState state57 = this.state;
                    --state57._fsp;
                    cmd = c;
                    break;
                }
                case 58: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_sessions_cmd_in_cmd11000);
                    c = this.sessions_cmd();
                    final RecognizerSharedState state58 = this.state;
                    --state58._fsp;
                    cmd = c;
                    break;
                }
                case 59: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_next_challenge_cmd_in_cmd11011);
                    c = this.set_next_challenge_cmd();
                    final RecognizerSharedState state59 = this.state;
                    --state59._fsp;
                    cmd = c;
                    break;
                }
                case 60: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_finish_challenge_cmd_in_cmd11021);
                    c = this.finish_challenge_cmd();
                    final RecognizerSharedState state60 = this.state;
                    --state60._fsp;
                    cmd = c;
                    break;
                }
                case 61: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_staff_cmd_in_cmd11031);
                    c = this.staff_cmd();
                    final RecognizerSharedState state61 = this.state;
                    --state61._fsp;
                    cmd = c;
                    break;
                }
                case 62: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_subscriber_cmd_in_cmd11043);
                    c = this.subscriber_cmd();
                    final RecognizerSharedState state62 = this.state;
                    --state62._fsp;
                    cmd = c;
                    break;
                }
                case 63: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_mute_partitions_cmd_in_cmd11054);
                    c = this.mute_partitions_cmd();
                    final RecognizerSharedState state63 = this.state;
                    --state63._fsp;
                    cmd = c;
                    break;
                }
                case 64: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_unmute_partitions_cmd_in_cmd11065);
                    c = this.unmute_partitions_cmd();
                    final RecognizerSharedState state64 = this.state;
                    --state64._fsp;
                    cmd = c;
                    break;
                }
                case 65: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_mute_cmd_in_cmd11075);
                    c = this.mute_cmd();
                    final RecognizerSharedState state65 = this.state;
                    --state65._fsp;
                    cmd = c;
                    break;
                }
                case 66: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_unmute_cmd_in_cmd11087);
                    c = this.unmute_cmd();
                    final RecognizerSharedState state66 = this.state;
                    --state66._fsp;
                    cmd = c;
                    break;
                }
                case 67: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_distribute_items_cmd_in_cmd11099);
                    c = this.distribute_items_cmd();
                    final RecognizerSharedState state67 = this.state;
                    --state67._fsp;
                    cmd = c;
                    break;
                }
                case 68: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_search_cmd_in_cmd11109);
                    c = this.search_cmd();
                    final RecognizerSharedState state68 = this.state;
                    --state68._fsp;
                    cmd = c;
                    break;
                }
                case 69: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_teleport_to_breed_mob_cmd_in_cmd11121);
                    c = this.teleport_to_breed_mob_cmd();
                    final RecognizerSharedState state69 = this.state;
                    --state69._fsp;
                    cmd = c;
                    break;
                }
                case 70: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_buff_character_cmd_in_cmd11131);
                    c = this.buff_character_cmd();
                    final RecognizerSharedState state70 = this.state;
                    --state70._fsp;
                    cmd = c;
                    break;
                }
                case 71: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_restore_character_cmd_in_cmd11142);
                    c = this.restore_character_cmd();
                    final RecognizerSharedState state71 = this.state;
                    --state71._fsp;
                    cmd = c;
                    break;
                }
                case 72: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_item_tracker_log_level_cmd_in_cmd11152);
                    c = this.set_item_tracker_log_level_cmd();
                    final RecognizerSharedState state72 = this.state;
                    --state72._fsp;
                    cmd = c;
                    break;
                }
                case 73: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_quota_cmd_in_cmd11161);
                    c = this.quota_cmd();
                    final RecognizerSharedState state73 = this.state;
                    --state73._fsp;
                    cmd = c;
                    break;
                }
                case 74: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ragnarok_cmd_in_cmd11174);
                    c = this.ragnarok_cmd();
                    final RecognizerSharedState state74 = this.state;
                    --state74._fsp;
                    cmd = c;
                    break;
                }
                case 75: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_remove_floor_items_cmd_in_cmd11185);
                    c = this.remove_floor_items_cmd();
                    final RecognizerSharedState state75 = this.state;
                    --state75._fsp;
                    cmd = c;
                    break;
                }
                case 76: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_show_population_cmd_in_cmd11195);
                    c = this.show_population_cmd();
                    final RecognizerSharedState state76 = this.state;
                    --state76._fsp;
                    cmd = c;
                    break;
                }
                case 77: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_show_monster_quota_cmd_in_cmd11206);
                    c = this.show_monster_quota_cmd();
                    final RecognizerSharedState state77 = this.state;
                    --state77._fsp;
                    cmd = c;
                    break;
                }
                case 78: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_cancel_collect_cooldown_cmd_in_cmd11216);
                    c = this.cancel_collect_cooldown_cmd();
                    final RecognizerSharedState state78 = this.state;
                    --state78._fsp;
                    cmd = c;
                    break;
                }
                case 79: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_get_instance_uid_cmd_in_cmd11226);
                    c = this.get_instance_uid_cmd();
                    final RecognizerSharedState state79 = this.state;
                    --state79._fsp;
                    cmd = c;
                    break;
                }
                case 80: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_dump_bag_cmd_in_cmd11236);
                    c = this.dump_bag_cmd();
                    final RecognizerSharedState state80 = this.state;
                    --state80._fsp;
                    cmd = c;
                    break;
                }
                case 81: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_wakfu_gauge_cmd_in_cmd11247);
                    c = this.set_wakfu_gauge_cmd();
                    final RecognizerSharedState state81 = this.state;
                    --state81._fsp;
                    cmd = c;
                    break;
                }
                case 82: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_temp_cmd_in_cmd11258);
                    c = this.temp_cmd();
                    final RecognizerSharedState state82 = this.state;
                    --state82._fsp;
                    cmd = c;
                    break;
                }
                case 83: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_calendar_cmd_in_cmd11270);
                    c = this.calendar_cmd();
                    final RecognizerSharedState state83 = this.state;
                    --state83._fsp;
                    cmd = c;
                    break;
                }
                case 84: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_fight_cmd_in_cmd11281);
                    c = this.fight_cmd();
                    final RecognizerSharedState state84 = this.state;
                    --state84._fsp;
                    cmd = c;
                    break;
                }
                case 85: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_protector_command_in_cmd11293);
                    c = this.protector_command();
                    final RecognizerSharedState state85 = this.state;
                    --state85._fsp;
                    cmd = c;
                    break;
                }
                case 86: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_monster_group_in_cmd11304);
                    c = this.monster_group();
                    final RecognizerSharedState state86 = this.state;
                    --state86._fsp;
                    cmd = c;
                    break;
                }
                case 87: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_resource_speed_factor_in_cmd11315);
                    c = this.set_resource_speed_factor();
                    final RecognizerSharedState state87 = this.state;
                    --state87._fsp;
                    cmd = c;
                    break;
                }
                case 88: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_state_command_in_cmd11325);
                    c = this.state_command();
                    final RecognizerSharedState state88 = this.state;
                    --state88._fsp;
                    cmd = c;
                    break;
                }
                case 89: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_scenario_cmd_in_cmd11336);
                    c = this.scenario_cmd();
                    final RecognizerSharedState state89 = this.state;
                    --state89._fsp;
                    cmd = c;
                    break;
                }
                case 90: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_version_cmd_in_cmd11347);
                    c = this.version_cmd();
                    final RecognizerSharedState state90 = this.state;
                    --state90._fsp;
                    cmd = c;
                    break;
                }
                case 91: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_plant_resources_cmd_in_cmd11359);
                    c = this.plant_resources_cmd();
                    final RecognizerSharedState state91 = this.state;
                    --state91._fsp;
                    cmd = c;
                    break;
                }
                case 92: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_destroy_resources_cmd_in_cmd11370);
                    c = this.destroy_resources_cmd();
                    final RecognizerSharedState state92 = this.state;
                    --state92._fsp;
                    cmd = c;
                    break;
                }
                case 93: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_destroy_monsters_cmd_in_cmd11380);
                    c = this.destroy_monsters_cmd();
                    final RecognizerSharedState state93 = this.state;
                    --state93._fsp;
                    cmd = c;
                    break;
                }
                case 94: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_respawn_cmd_in_cmd11390);
                    c = this.set_respawn_cmd();
                    final RecognizerSharedState state94 = this.state;
                    --state94._fsp;
                    cmd = c;
                    break;
                }
                case 95: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_check_cmd_in_cmd11401);
                    c = this.check_cmd();
                    final RecognizerSharedState state95 = this.state;
                    --state95._fsp;
                    cmd = c;
                    break;
                }
                case 96: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_craft_cmd_in_cmd11413);
                    c = this.craft_cmd();
                    final RecognizerSharedState state96 = this.state;
                    --state96._fsp;
                    cmd = c;
                    break;
                }
                case 97: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ban_request_cmd_in_cmd11425);
                    c = this.ban_request_cmd();
                    final RecognizerSharedState state97 = this.state;
                    --state97._fsp;
                    cmd = c;
                    break;
                }
                case 98: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ice_status_cmd_in_cmd11436);
                    c = this.ice_status_cmd();
                    final RecognizerSharedState state98 = this.state;
                    --state98._fsp;
                    cmd = c;
                    break;
                }
                case 99: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_pet_cmd_in_cmd11447);
                    c = this.pet_cmd();
                    final RecognizerSharedState state99 = this.state;
                    --state99._fsp;
                    cmd = c;
                    break;
                }
                case 100: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_add_itemxp_cmd_in_cmd11460);
                    c = this.add_itemxp_cmd();
                    final RecognizerSharedState state100 = this.state;
                    --state100._fsp;
                    cmd = c;
                    break;
                }
                case 101: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_guild_cmd_in_cmd11471);
                    c = this.guild_cmd();
                    final RecognizerSharedState state101 = this.state;
                    --state101._fsp;
                    cmd = c;
                    break;
                }
                case 102: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_companion_cmd_in_cmd11483);
                    c = this.companion_cmd();
                    final RecognizerSharedState state102 = this.state;
                    --state102._fsp;
                    cmd = c;
                    break;
                }
                case 103: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_system_configuration_cmd_in_cmd11494);
                    c = this.system_configuration_cmd();
                    final RecognizerSharedState state103 = this.state;
                    --state103._fsp;
                    cmd = c;
                    break;
                }
                case 104: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_ai_cmd_in_cmd11504);
                    c = this.ai_cmd();
                    final RecognizerSharedState state104 = this.state;
                    --state104._fsp;
                    cmd = c;
                    break;
                }
                case 105: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_fightchallenge_cmd_in_cmd11516);
                    c = this.fightchallenge_cmd();
                    final RecognizerSharedState state105 = this.state;
                    --state105._fsp;
                    cmd = c;
                    break;
                }
                case 106: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_spell_command_in_cmd11527);
                    c = this.spell_command();
                    final RecognizerSharedState state106 = this.state;
                    --state106._fsp;
                    cmd = c;
                    break;
                }
                case 107: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_gem_command_in_cmd11538);
                    c = this.gem_command();
                    final RecognizerSharedState state107 = this.state;
                    --state107._fsp;
                    cmd = c;
                    break;
                }
                case 108: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_aptitude_command_in_cmd11550);
                    c = this.aptitude_command();
                    final RecognizerSharedState state108 = this.state;
                    --state108._fsp;
                    cmd = c;
                    break;
                }
                case 109: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_havenworld_cmd_in_cmd11561);
                    c = this.havenworld_cmd();
                    final RecognizerSharedState state109 = this.state;
                    --state109._fsp;
                    cmd = c;
                    break;
                }
                case 110: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_almanach_start_cmd_in_cmd11572);
                    c = this.almanach_start_cmd();
                    final RecognizerSharedState state110 = this.state;
                    --state110._fsp;
                    cmd = c;
                    break;
                }
                case 111: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_learn_emote_cmd_in_cmd11583);
                    c = this.learn_emote_cmd();
                    final RecognizerSharedState state111 = this.state;
                    --state111._fsp;
                    cmd = c;
                    break;
                }
                case 112: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_set_player_title_cmd_in_cmd11598);
                    c = this.set_player_title_cmd();
                    final RecognizerSharedState state112 = this.state;
                    --state112._fsp;
                    cmd = c;
                    break;
                }
                case 113: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_free_access_cmd_in_cmd11613);
                    c = this.free_access_cmd();
                    final RecognizerSharedState state113 = this.state;
                    --state113._fsp;
                    cmd = c;
                    break;
                }
                case 114: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_create_full_group_cmd_in_cmd11629);
                    c = this.create_full_group_cmd();
                    final RecognizerSharedState state114 = this.state;
                    --state114._fsp;
                    cmd = c;
                    break;
                }
                case 115: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_inventory_cmd_in_cmd11644);
                    c = this.inventory_cmd();
                    final RecognizerSharedState state115 = this.state;
                    --state115._fsp;
                    cmd = c;
                    break;
                }
                case 116: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_empty_char_cmd_in_cmd11665);
                    c = this.empty_char_cmd();
                    final RecognizerSharedState state116 = this.state;
                    --state116._fsp;
                    cmd = c;
                    break;
                }
                case 117: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_client_game_event_command_in_cmd11686);
                    c = this.client_game_event_command();
                    final RecognizerSharedState state117 = this.state;
                    --state117._fsp;
                    cmd = c;
                    break;
                }
                case 118: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_rent_item_cmd_in_cmd11705);
                    c = this.rent_item_cmd();
                    final RecognizerSharedState state118 = this.state;
                    --state118._fsp;
                    cmd = c;
                    break;
                }
                case 119: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_character_cmd_in_cmd11724);
                    c = this.character_cmd();
                    final RecognizerSharedState state119 = this.state;
                    --state119._fsp;
                    cmd = c;
                    break;
                }
                case 120: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_popup_message_cmd_in_cmd11745);
                    c = this.popup_message_cmd();
                    final RecognizerSharedState state120 = this.state;
                    --state120._fsp;
                    cmd = c;
                    break;
                }
                case 121: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_red_message_cmd_in_cmd11756);
                    c = this.red_message_cmd();
                    final RecognizerSharedState state121 = this.state;
                    --state121._fsp;
                    cmd = c;
                    break;
                }
                case 122: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_red_message_to_player_cmd_in_cmd11767);
                    c = this.red_message_to_player_cmd();
                    final RecognizerSharedState state122 = this.state;
                    --state122._fsp;
                    cmd = c;
                    break;
                }
                case 123: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_emote_targetable_cmd_in_cmd11777);
                    c = this.emote_targetable_cmd();
                    final RecognizerSharedState state123 = this.state;
                    --state123._fsp;
                    cmd = c;
                    break;
                }
                case 124: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_haven_bag_kick_cmd_in_cmd11787);
                    c = this.haven_bag_kick_cmd();
                    final RecognizerSharedState state124 = this.state;
                    --state124._fsp;
                    cmd = c;
                    break;
                }
                case 125: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_tp_to_jail_cmd_in_cmd11798);
                    c = this.tp_to_jail_cmd();
                    final RecognizerSharedState state125 = this.state;
                    --state125._fsp;
                    cmd = c;
                    break;
                }
                case 126: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_freedom_cmd_in_cmd11809);
                    c = this.freedom_cmd();
                    final RecognizerSharedState state126 = this.state;
                    --state126._fsp;
                    cmd = c;
                    break;
                }
                case 127: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_web_browser_cmd_in_cmd11821);
                    c = this.web_browser_cmd();
                    final RecognizerSharedState state127 = this.state;
                    --state127._fsp;
                    cmd = c;
                    break;
                }
                case 128: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_give_item_cmd_in_cmd11832);
                    c = this.give_item_cmd();
                    final RecognizerSharedState state128 = this.state;
                    --state128._fsp;
                    cmd = c;
                    break;
                }
                case 129: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_listloot_cmd_in_cmd11843);
                    c = this.listloot_cmd();
                    final RecognizerSharedState state129 = this.state;
                    --state129._fsp;
                    cmd = c;
                    break;
                }
                case 130: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_revive_cmd_in_cmd11854);
                    c = this.revive_cmd();
                    final RecognizerSharedState state130 = this.state;
                    --state130._fsp;
                    cmd = c;
                    break;
                }
                case 131: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_reset_account_market_entries_cmd_in_cmd11866);
                    c = this.reset_account_market_entries_cmd();
                    final RecognizerSharedState state131 = this.state;
                    --state131._fsp;
                    cmd = c;
                    break;
                }
                case 132: {
                    this.pushFollow(ModerationCommandParser.FOLLOW_hero_cmd_in_cmd11875);
                    c = this.hero_cmd();
                    final RecognizerSharedState state132 = this.state;
                    --state132._fsp;
                    cmd = c;
                    break;
                }
            }
        }
        catch (RecognitionException re) {
            this.reportError(re);
            this.recover((IntStream)this.input, re);
        }
        return cmd;
    }
    
    static {
        tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ACCOUNT_PATTERN", "ACHIEVEMENT", "ADD", "ADD_ITEM_XP", "ADD_MONEY", "ADD_SKILLXP", "ADD_SPELLXP", "ADD_TO_GROUP", "ADD_XP", "AI", "ALIGNMENT", "ALMANACH", "APTITUDE", "BAN", "BAN_REQUEST", "BEGIN", "BOOLEAN", "BOT", "BUFF", "CALENDAR_CMD", "CANCEL_COLLECT_COOLDOWN", "CHAOS", "CHARACTER_CMD", "CHARNAME_PATTERN", "CHARNAME_USABLECHAR", "CHECK_CMD", "CITIZEN_POINTS", "CLIENT_GAME_EVENT_CMD", "COMPANION", "COMPLETE", "COMPLETE_OBJECTIVE", "COORDS_SEPARATOR", "CRAFT_CMD", "CREATE_FULL_GROUP", "CREATE_GROUP", "CREATE_ITEM", "CREATE_SET", "DATE", "DELETE_ITEM", "DESTROY_INSTANCE", "DESTROY_MONSTERS", "DESTROY_RESOURCES", "DISTRIBUTE_ITEMS", "DUMP", "DUMP_BAG", "EMOTE_TARGETABLE", "EMPTY_CHAR", "ENABLE", "END", "ENDLINE", "ENDSCENARIO", "ESCAPE", "ESCAPED_STRING", "FIGHT_CHALLENGE", "FIGHT_CMD", "FINISHCHALLENGE", "FLOAT", "FREEDOM", "FREE_ACCESS", "GEM_CMD", "GET", "GET_INSTANCE_UID", "GHOSTCHECK", "GIVE_ITEM", "GOD_MODE", "GUILD", "HAVEN_BAG_KICK", "HAVEN_WORLD", "HELP", "HERO", "ICE_STATUS", "IDENT_PHASE", "INFO", "INSTANCE_USAGE", "INVENTORY", "KICK", "LEARN_EMOTE", "LIST_LOOT", "MONSTER_GROUP", "MSGALL", "MUTE", "MUTE_PARTITIONS", "NATION", "NUMBER", "NUMERAL", "OFF", "ON", "PANEL", "PAUSE", "PET", "PING", "PLANT_RESOURCES", "PLAY_ANIMATION", "PLAY_APS", "POPUP_MESSAGE", "PROTECTOR_CMD", "PROXIMITY_PATTERN", "PVP", "QUOTA", "RAGNAROK", "RECOMPUTE_POINTS", "RED_MESSAGE", "RED_MESSAGE_TO_PLAYER", "REGENERATE", "REGENERATE_WITH_ITEM", "RELOADSCENARIOS", "REMOVE", "REMOVE_FLOOR_ITEMS", "RENT_ITEM_CMD", "RESET", "RESET_ACCOUNT_MARKET_ENTRIES", "RESET_REGRESSION", "RESTART_CHAOS", "RESTORE_CHARACTER_CMD", "RESUME", "REVIVE", "RIGHTS", "RUNACTION", "SCENARIO_COMMAND", "SEARCH", "SERVERLOCK", "SESSIONS", "SET", "SETNEXTCHALLENGE", "SET_BONUS_FACTOR", "SET_ITEM_TRACKER_LOG_LEVEL_CMD", "SET_LEVEL", "SET_PLAYER_TITLE", "SET_RANK", "SET_RESOURCE_SPEED_FACTOR", "SET_RESPAWN_CMD", "SET_SKILL_LEVEL", "SET_SPELLLEVEL", "SET_WAKFU_GAUGE", "SHOW_AGGRO_LIST", "SHOW_MONSTER_QUOTA", "SHOW_POPULATION", "SHUTDOWN", "SPAWN_INTERACTIVE_ELEMENT", "SPELL_CMD", "STAFF", "START", "START_DATE", "STATE_CMD", "STATS", "STATUS", "STOP", "SUBSCRIBER", "SYMBIOT", "SYSMSG", "SYSTEM_CONFIGURATION", "TELEPORT", "TELEPORT_TO_MONSTER", "TELEPORT_USER", "TEMP", "TIME", "TP_TO_JAIL", "TURN_DURATION", "UNBAN", "UNMUTE", "UNMUTE_PARTITIONS", "VAR", "VERSION", "VOTE", "WEB_BROWSER", "WHERE", "WHITESPACE", "WHO", "ZONE_BUFF", "'--addXp'", "'--help'", "'--learn'", "'--unlearn'", "'--userGroup'", "'-a'", "'-activate'", "'-add'", "'-addMoney'", "'-all'", "'-am'", "'-ax'", "'-ca'", "'-cancelEnd'", "'-cb'", "'-ccf'", "'-cfrv'", "'-changenation'", "'-cn'", "'-collectFightRandomValue'", "'-consult'", "'-createArcade'", "'-createBoufbowl'", "'-createCollectFight'", "'-desc'", "'-describe'", "'-e'", "'-em'", "'-emotes'", "'-empty'", "'-ex'", "'-exploit'", "'-f'", "'-fight'", "'-flee'", "'-floodCalendar'", "'-h'", "'-i'", "'-inactivate'", "'-index'", "'-invit'", "'-k'", "'-kamaQuestCd'", "'-kamaQuestRatio'", "'-kamas'", "'-kqcd'", "'-kqr'", "'-l'", "'-makeFlee'", "'-max'", "'-mf'", "'-r'", "'-reg'", "'-remove'", "'-removeRefItem'", "'-rename'", "'-repack'", "'-repackBag'", "'-restat'", "'-rights'", "'-rm'", "'-rmv'", "'-rp'", "'-rpb'", "'-rri'", "'-s'", "'-sa'", "'-sao'", "'-sb'", "'-sbps'", "'-selectBonus'", "'-setReconnectionTurnTimeout'", "'-setdesc'", "'-setend'", "'-setstart'", "'-settitle'", "'-show'", "'-showBonusPointSelectbable'", "'-showall'", "'-showallof'", "'-shuffle'", "'-srtt'", "'-t'", "'-u'", "'-ugi'", "'-ul'", "'-unreg'", "'-unvalid'", "'-update'", "'-ut'", "'-valid'", "'-win'", "'IG'", "'IRL'", "'a'", "'ab'", "'activate'", "'addBonus'", "'addEquipment'", "'addLevelLegit'", "'addMoney'", "'addOldAptitudeLevel'", "'addResources'", "'addRight'", "'addRightInClient'", "'addToGroup'", "'addXp'", "'all'", "'am'", "'aoal'", "'ar'", "'ara'", "'arc'", "'atg'", "'bid'", "'building'", "'c'", "'cd'", "'changeDescription'", "'changeMessage'", "'changeUnlockGroupLimit'", "'clear'", "'cm'", "'cn'", "'commit'", "'create'", "'cxp'", "'delete'", "'dnd'", "'doNotDisturb'", "'ea'", "'endAuction'", "'equip'", "'ff'", "'forcefeed'", "'free'", "'fxp'", "'gblf'", "'getId'", "'gl'", "'goInPrison'", "'gp'", "'groupLimit'", "'h'", "'help'", "'id'", "'ie'", "'item'", "'k'", "'l'", "'list'", "'loot'", "'maxPerWeek'", "'maxxp'", "'money'", "'monster'", "'monsterId'", "'mpw'", "'na'", "'newAptitude'", "'nextFree'", "'oa'", "'offenseAdd'", "'offenseRem'", "'or'", "'p'", "'pef'", "'player'", "'playerLevelCap'", "'point'", "'pointEarnedFactor'", "'pp'", "'pt'", "'q'", "'qp'", "'qq'", "'quest'", "'queue'", "'re'", "'recoInFight'", "'refreshSubscription'", "'removeEquipment'", "'removeRight'", "'rename'", "'resetGuild'", "'resource'", "'restat'", "'rg'", "'right'", "'rs'", "'rvequip'", "'s'", "'sa'", "'sabf'", "'sci'", "'sed'", "'serverStatus'", "'setAdminBuildingFactor'", "'setCompanionToMaxXp'", "'setEndDate'", "'setFreeCompanion'", "'setFreeCompanionCycle'", "'setGuild'", "'setLearningFactor'", "'setLevel'", "'setMaxSimultaneous'", "'setNation'", "'setSex'", "'setSubscriptionLevel'", "'setVisibility'", "'sfcc'", "'sfree'", "'sg'", "'show'", "'showClientInfo'", "'showFree'", "'showLevels'", "'showRanks'", "'showRights'", "'showServerInfo'", "'showSubscriptionRightsSet'", "'si'", "'sl'", "'slf'", "'sm'", "'smi'", "'sms'", "'sn'", "'sp'", "'sr'", "'ss'", "'ssi'", "'ssl'", "'ssrs'", "'startAuction'", "'switchPasseport'", "'ti'", "'toItem'", "'topology'", "'u'", "'update'", "'xp'", "'zaapFree'" };
        m_logger = Logger.getLogger((Class)ModerationCommandParser.class);
        DFA92_transitionS = new String[] { "\u0001\u0014\u0001\uffff\u0001`\u0001,\u0001(\u0001&\u0001\u0018\u0001*\u0001d\u0001\uffff\u0001j\u0001h\u0001\f\u0001]\u0002\uffff\u0001\u0002\u0001B\u0001O\u0001J\u0001\u001a\u0001s\u0002\uffff\u0001[\u0001\uffff\u0001q\u0001b\u0003\uffff\u0001\\\u0001n\u0001\u0017\u0001\u001c\u0001\u001d\u0001\uffff\u0001\u001e\u00010\u0001Y\u0001X\u0001?\u0001\uffff\u0001L\u0001w\u0001p\u0003\uffff\u0001$\u0002\uffff\u0001e\u0001P\u00018\u0001\uffff\u0001z\u0001m\u0001g\u0001\uffff\u0001K\u0001\r\u0001|\u0001.\u0001a\u0001x\u0001i\u0001-\u0001\u0080\u0001^\u0001\u000e\u0001\uffff\u0001/\u0001o\u0001\u000b\u0001k\u0001}\u0001R\u0001\u0011\u0001=\u0001;\u0001\u0013\u0004\uffff\u0001\u0003\u0001\uffff\u0001_\u0001\u0004\u0001W\u00012\u00013\u0001t\u0001Q\u0001\uffff\u0001\"\u0001E\u0001F\u0001\uffff\u0001u\u0001v\u0001\u001f\u0001 \u0001%\u0001\uffff\u0001G\u0001r\u0001\uffff\u0001\u007f\u0001\uffff\u0001\u001b\u0001C\u0001\uffff\u0001~\u0001\u0019\u0001#\u0001U\u0001@\u0001\n\u00016\u0001\uffff\u00017\u0001+\u0001D\u00014\u0001l\u0001\uffff\u0001S\u0001Z\u0001)\u0001'\u0001M\u00011\u0001I\u0001H\u0001\u000f\u00015\u0001f\u00019\u0002\uffff\u0001T\u0001\u0001\u0002\uffff\u0001:\u0001\u0012\u0001\u0010\u0001c\u0001\b\u0001A\u0001\t\u0001N\u0001\u0005\u0001y\u0001!\u0001\f\u0001>\u0001<\u0001\uffff\u0001V\u0001\uffff\u0001{\u0001\u0007\u0001\uffff\u0001\u0006\u0001\u0016n\uffff\u0002\u0015>\uffff\u0002E-\uffff\u0001@\u0002\uffff\u0002@\u0004\uffff\u0001@", "", "", "", "", "", "", "", "\u0001\u0081\u0016\uffff\u0001\u0081\u001c\uffff\u0001\u0081\u001e\uffff\u0001\u0082", "\u0001\u0085\u0016\uffff\u0001\u0083\u001c\uffff\u0001\u0084", "", "", "", "", "", "", "", "", "", "", "\u0002\u0015N\uffff\u0001\u0015 \uffff\u0001\u0086\u0012\uffff\u0001\u0015", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "\u0001\u00873\uffff\u0001\u0088", "\u0001\u0089!\uffff\u0001\u008a", "\u0001\u0089!\uffff\u0001\u008a", "\u0001\u0089!\uffff\u0001\u008a", "", "\u0001\u0088", "\u0001\u008b!\uffff\u0001\u008c", "", "\u0001\u008d3\uffff\u0001\u008e", "", "", "\u0001\u008e", "\u0001\u008f!\uffff\u0001\u0090", "", "" };
        DFA92_eot = DFA.unpackEncodedString("\u0091\uffff");
        DFA92_eof = DFA.unpackEncodedString("\u0091\uffff");
        DFA92_min = DFA.unpackEncodedStringToUnsignedChars("\u0001\u0005\u0007\uffff\u0002\u0004\n\uffff\u0001!m\uffff\u0001#\u00035\u0001\uffff\u0001W\u00015\u0001\uffff\u0001#\u0002\uffff\u0001W\u00015\u0002\uffff");
        DFA92_max = DFA.unpackEncodedStringToUnsignedChars("\u0001\u0193\u0007\uffff\u0001W\u00018\n\uffff\u0001¥m\uffff\u0004W\u0001\uffff\u0002W\u0001\uffff\u0001W\u0002\uffff\u0002W\u0002\uffff");
        DFA92_accept = DFA.unpackEncodedString("\u0001\uffff\u0001\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0001\u0005\u0001\u0006\u0001\u0007\u0002\uffff\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\uffff\u0001\u0018\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0001\"\u0001#\u0001$\u0001%\u0001&\u0001'\u0001(\u0001)\u0001*\u0001+\u0001,\u0001-\u0001.\u0001/\u00010\u00011\u00012\u00013\u00014\u00015\u00016\u00017\u00018\u00019\u0001:\u0001;\u0001<\u0001=\u0001>\u0001?\u0001@\u0001A\u0001B\u0001C\u0001D\u0001E\u0001F\u0001G\u0001H\u0001I\u0001J\u0001K\u0001L\u0001M\u0001N\u0001O\u0001P\u0001Q\u0001R\u0001S\u0001T\u0001U\u0001V\u0001W\u0001X\u0001Y\u0001Z\u0001[\u0001\\\u0001]\u0001^\u0001_\u0001`\u0001a\u0001b\u0001c\u0001d\u0001e\u0001f\u0001g\u0001h\u0001i\u0001j\u0001k\u0001l\u0001m\u0001n\u0001o\u0001p\u0001q\u0001r\u0001s\u0001t\u0001u\u0001v\u0001w\u0001x\u0001y\u0001z\u0001{\u0001|\u0001}\u0001~\u0001\u007f\u0001\u0080\u0001\u0081\u0001\u0082\u0001\u0083\u0001\u0084\u0001\b\u0004\uffff\u0001\u0019\u0002\uffff\u0001\u000b\u0001\uffff\u0001\t\u0001\n\u0002\uffff\u0001\f\u0001\r");
        DFA92_special = DFA.unpackEncodedString("\u0091\uffff}>");
        final int numStates = ModerationCommandParser.DFA92_transitionS.length;
        DFA92_transition = new short[numStates][];
        for (int i = 0; i < numStates; ++i) {
            ModerationCommandParser.DFA92_transition[i] = DFA.unpackEncodedString(ModerationCommandParser.DFA92_transitionS[i]);
        }
        FOLLOW_NUMBER_in_coords1984 = new BitSet(new long[] { 34359738368L, 8388608L });
        FOLLOW_COORDS_SEPARATOR_in_coords1986 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_coords1991 = new BitSet(new long[] { 2L });
        FOLLOW_CHARNAME_PATTERN_in_character_pattern2008 = new BitSet(new long[] { 2L });
        FOLLOW_ESCAPED_STRING_in_character_pattern2017 = new BitSet(new long[] { 2L });
        FOLLOW_ACCOUNT_PATTERN_in_character_pattern2026 = new BitSet(new long[] { 2L });
        FOLLOW_ESCAPED_STRING_in_message2046 = new BitSet(new long[] { 2L });
        FOLLOW_PROXIMITY_PATTERN_in_proximity_pattern2065 = new BitSet(new long[] { 2L });
        FOLLOW_NUMBER_in_id_list_pattern2087 = new BitSet(new long[] { 2L, 8388608L });
        FOLLOW_BOT_in_bot_cmd2108 = new BitSet(new long[] { 0L, 1073741824L });
        FOLLOW_PING_in_bot_cmd2110 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_bot_cmd2114 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_bot_cmd2116 = new BitSet(new long[] { 2L });
        FOLLOW_STATS_in_stats_cmd2131 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_stats_cmd2133 = new BitSet(new long[] { 2L });
        FOLLOW_PANEL_in_panel_cmd2148 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_panel_cmd2150 = new BitSet(new long[] { 2L });
        FOLLOW_PING_in_ping_cmd2165 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ping_cmd2167 = new BitSet(new long[] { 2L });
        FOLLOW_PING_in_ping_cmd2174 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_ping_cmd2176 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ping_cmd2178 = new BitSet(new long[] { 2L });
        FOLLOW_PING_in_ping_cmd2185 = new BitSet(new long[] { 0L, 0L, 4194304L });
        FOLLOW_STOP_in_ping_cmd2187 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ping_cmd2189 = new BitSet(new long[] { 2L });
        FOLLOW_TIME_in_time_cmd2210 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_time_cmd2212 = new BitSet(new long[] { 2L });
        FOLLOW_WHO_in_who_cmd2233 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_who_cmd2237 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_who_cmd2239 = new BitSet(new long[] { 2L });
        FOLLOW_WHERE_in_where_cmd2254 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_where_cmd2258 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_where_cmd2260 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_in_teleport_to_player_cmd2275 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_teleport_to_player_cmd2279 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_to_player_cmd2281 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_USER_in_teleport_player_to_me_cmd2296 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_teleport_player_to_me_cmd2300 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_player_to_me_cmd2302 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_USER_in_teleport_player_to_coords_cmd2317 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_teleport_player_to_coords_cmd2321 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_coords_in_teleport_player_to_coords_cmd2325 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_player_to_coords_cmd2327 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_USER_in_teleport_player_to_instance_cmd2342 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_teleport_player_to_instance_cmd2346 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_coords_in_teleport_player_to_instance_cmd2350 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_teleport_player_to_instance_cmd2354 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_player_to_instance_cmd2356 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_in_teleport_to_coords_cmd2372 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_coords_in_teleport_to_coords_cmd2376 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_to_coords_cmd2378 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_in_teleport_to_inst_cmd2393 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_coords_in_teleport_to_inst_cmd2397 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_teleport_to_inst_cmd2401 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_to_inst_cmd2403 = new BitSet(new long[] { 2L });
        FOLLOW_SERVERLOCK_in_serverlock_cmd2418 = new BitSet(new long[] { 9007199254740992L, 100663296L });
        FOLLOW_ON_in_serverlock_cmd2421 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_OFF_in_serverlock_cmd2427 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_serverlock_cmd2439 = new BitSet(new long[] { 2L });
        FOLLOW_KICK_in_kick_cmd2452 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_kick_cmd2456 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_kick_cmd2458 = new BitSet(new long[] { 2L });
        FOLLOW_KICK_in_kick_cmd2465 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_kick_cmd2469 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_kick_cmd2473 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_kick_cmd2475 = new BitSet(new long[] { 2L });
        FOLLOW_BAN_in_ban_cmd2492 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_ban_cmd2496 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_ban_cmd2500 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ban_cmd2502 = new BitSet(new long[] { 2L });
        FOLLOW_BAN_in_ban_cmd2509 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_ban_cmd2513 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ban_cmd2515 = new BitSet(new long[] { 2L });
        FOLLOW_UNBAN_in_ban_cmd2522 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_ban_cmd2526 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ban_cmd2528 = new BitSet(new long[] { 2L });
        FOLLOW_BAN_REQUEST_in_ban_request_cmd2547 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_ban_request_cmd2551 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_ban_request_cmd2555 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_ban_request_cmd2559 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ban_request_cmd2561 = new BitSet(new long[] { 2L });
        FOLLOW_GHOSTCHECK_in_ghostcheck_cmd2577 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ghostcheck_cmd2579 = new BitSet(new long[] { 2L });
        FOLLOW_IDENT_PHASE_in_identphase_cmd2594 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_identphase_cmd2598 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_identphase_cmd2600 = new BitSet(new long[] { 2L });
        FOLLOW_SHUTDOWN_in_shutdown_cmd2615 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_shutdown_cmd2619 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_shutdown_cmd2621 = new BitSet(new long[] { 2L });
        FOLLOW_SHUTDOWN_in_shutdown_cmd2629 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_shutdown_cmd2631 = new BitSet(new long[] { 2L });
        FOLLOW_SHUTDOWN_in_shutdown_cmd2639 = new BitSet(new long[] { 0L, 0L, 4194304L });
        FOLLOW_STOP_in_shutdown_cmd2641 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_shutdown_cmd2643 = new BitSet(new long[] { 2L });
        FOLLOW_SYSMSG_in_sysmsg_cmd2663 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_sysmsg_cmd2667 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_sysmsg_cmd2669 = new BitSet(new long[] { 2L });
        FOLLOW_SYSMSG_in_sysmsg_cmd2676 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_sysmsg_cmd2680 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_sysmsg_cmd2684 = new BitSet(new long[] { 2L });
        FOLLOW_SYSMSG_in_sysmsg_cmd2691 = new BitSet(new long[] { 0L, 68719476736L });
        FOLLOW_proximity_pattern_in_sysmsg_cmd2695 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_sysmsg_cmd2699 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_sysmsg_cmd2701 = new BitSet(new long[] { 2L });
        FOLLOW_MSGALL_in_msgall_cmd2719 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_msgall_cmd2723 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_msgall_cmd2725 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2741 = new BitSet(new long[] { 0L, 0L, 4503599627370496L });
        FOLLOW_180_in_symbiot_cmd2743 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_symbiot_cmd2747 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_symbiot_cmd2749 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2757 = new BitSet(new long[] { 0L, 0L, 4503599627370496L });
        FOLLOW_180_in_symbiot_cmd2759 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_symbiot_cmd2763 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_symbiot_cmd2767 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_symbiot_cmd2769 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2777 = new BitSet(new long[] { 0L, 0L, 0L, 2199023255552L });
        FOLLOW_233_in_symbiot_cmd2779 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_symbiot_cmd2783 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2789 = new BitSet(new long[] { 0L, 0L, 0L, 68719476736L });
        FOLLOW_228_in_symbiot_cmd2791 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_symbiot_cmd2795 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_symbiot_cmd2799 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2806 = new BitSet(new long[] { 0L, 0L, 0L, 1048576L });
        FOLLOW_212_in_symbiot_cmd2808 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_symbiot_cmd2812 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2818 = new BitSet(new long[] { 0L, 0L, 0L, 262144L });
        FOLLOW_210_in_symbiot_cmd2820 = new BitSet(new long[] { 2L });
        FOLLOW_SYMBIOT_in_symbiot_cmd2827 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_symbiot_cmd2829 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2851 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2853 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2860 = new BitSet(new long[] { 0L, 4611686018427387904L });
        FOLLOW_SET_in_nation_cmd2862 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd2866 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2868 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2875 = new BitSet(new long[] { 0L, 0L, 549755813888L });
        FOLLOW_VOTE_in_nation_cmd2877 = new BitSet(new long[] { 0L, 4096L });
        FOLLOW_INFO_in_nation_cmd2879 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2881 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2888 = new BitSet(new long[] { 0L, 0L, 549755813888L });
        FOLLOW_VOTE_in_nation_cmd2890 = new BitSet(new long[] { 0L, 4096L });
        FOLLOW_INFO_in_nation_cmd2892 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd2896 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2898 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2905 = new BitSet(new long[] { 0L, 0L, 549755813888L });
        FOLLOW_VOTE_in_nation_cmd2907 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_nation_cmd2909 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd2913 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2915 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2922 = new BitSet(new long[] { 0L, 0L, 549755813888L });
        FOLLOW_VOTE_in_nation_cmd2924 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_nation_cmd2926 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2928 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2935 = new BitSet(new long[] { 0L, 0L, 549755813888L });
        FOLLOW_VOTE_in_nation_cmd2937 = new BitSet(new long[] { 4503599627370496L });
        FOLLOW_END_in_nation_cmd2939 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd2943 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2945 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2952 = new BitSet(new long[] { 0L, 0L, 549755813888L });
        FOLLOW_VOTE_in_nation_cmd2954 = new BitSet(new long[] { 4503599627370496L });
        FOLLOW_END_in_nation_cmd2956 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2958 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2965 = new BitSet(new long[] { 1073741824L });
        FOLLOW_CITIZEN_POINTS_in_nation_cmd2967 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd2971 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2973 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2980 = new BitSet(new long[] { 0L, 0L, 16L });
        FOLLOW_SET_RANK_in_nation_cmd2982 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd2986 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd2988 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd2995 = new BitSet(new long[] { 16384L });
        FOLLOW_ALIGNMENT_in_nation_cmd2997 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd3001 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd3005 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3007 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd3013 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 432345564227567616L });
        FOLLOW_set_in_nation_cmd3015 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd3025 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3027 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd3034 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 196608L });
        FOLLOW_set_in_nation_cmd3036 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd3047 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3049 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd3057 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 49152L });
        FOLLOW_set_in_nation_cmd3059 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_nation_cmd3070 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3072 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd3080 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 16908288L });
        FOLLOW_set_in_nation_cmd3082 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3090 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd3097 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_nation_cmd3099 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3107 = new BitSet(new long[] { 2L });
        FOLLOW_NATION_in_nation_cmd3114 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 64L });
        FOLLOW_390_in_nation_cmd3117 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_nation_cmd3120 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_cmd3139 = new BitSet(new long[] { 0L, 562949953421312L });
        FOLLOW_RESET_in_achievement_cmd3141 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_283_in_achievement_cmd3146 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_cmd3151 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_cmd3153 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_cmd3161 = new BitSet(new long[] { 0L, 562949953421312L });
        FOLLOW_RESET_in_achievement_cmd3163 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 16777216L });
        FOLLOW_280_in_achievement_cmd3165 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_284_in_achievement_cmd3170 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_cmd3173 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_cmd3179 = new BitSet(new long[] { 8589934592L });
        FOLLOW_COMPLETE_in_achievement_cmd3181 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_cmd3185 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_cmd3187 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_cmd3193 = new BitSet(new long[] { 17179869184L });
        FOLLOW_COMPLETE_OBJECTIVE_in_achievement_cmd3195 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_cmd3199 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_cmd3201 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_cmd3207 = new BitSet(new long[] { 0L, 0L, 137438953472L });
        FOLLOW_VAR_in_achievement_cmd3209 = new BitSet(new long[] { 0L, 1L });
        FOLLOW_GET_in_achievement_cmd3211 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_achievement_cmd3215 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_cmd3217 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_cmd3223 = new BitSet(new long[] { 0L, 0L, 137438953472L });
        FOLLOW_VAR_in_achievement_cmd3225 = new BitSet(new long[] { 0L, 4611686018427387904L });
        FOLLOW_SET_in_achievement_cmd3227 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_achievement_cmd3231 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_cmd3235 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_cmd3237 = new BitSet(new long[] { 2L });
        FOLLOW_ACHIEVEMENT_in_achievement_date_cmd3256 = new BitSet(new long[] { 0L, 0L, 262144L });
        FOLLOW_START_DATE_in_achievement_date_cmd3258 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3262 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3266 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3270 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3274 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3278 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3282 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_achievement_date_cmd3286 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_achievement_date_cmd3288 = new BitSet(new long[] { 2L });
        FOLLOW_ZONE_BUFF_in_zone_buff_cmd3306 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_zone_buff_cmd3308 = new BitSet(new long[] { 2L });
        FOLLOW_ZONE_BUFF_in_zone_buff_cmd3315 = new BitSet(new long[] { 64L });
        FOLLOW_ADD_in_zone_buff_cmd3317 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_zone_buff_cmd3321 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_zone_buff_cmd3323 = new BitSet(new long[] { 2L });
        FOLLOW_ZONE_BUFF_in_zone_buff_cmd3330 = new BitSet(new long[] { 0L, 70368744177664L });
        FOLLOW_REMOVE_in_zone_buff_cmd3332 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_zone_buff_cmd3336 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_zone_buff_cmd3338 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3357 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3361 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3363 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3370 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3374 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3378 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3380 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3387 = new BitSet(new long[] { 0L, 0L, 0L, Long.MIN_VALUE });
        FOLLOW_255_in_create_group_cmd3389 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3393 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3395 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3402 = new BitSet(new long[] { 0L, 0L, 0L, Long.MIN_VALUE });
        FOLLOW_255_in_create_group_cmd3404 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3408 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3412 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3414 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3422 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 64L });
        FOLLOW_262_in_create_group_cmd3424 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3428 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3430 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3437 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 64L });
        FOLLOW_262_in_create_group_cmd3439 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3443 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3447 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3449 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3458 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 2L });
        FOLLOW_257_in_create_group_cmd3460 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_group_cmd3464 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3466 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3473 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 2L });
        FOLLOW_257_in_create_group_cmd3475 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 1099511627776L });
        FOLLOW_296_in_create_group_cmd3477 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3479 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_GROUP_in_create_group_cmd3486 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_create_group_cmd3488 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_group_cmd3496 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_FULL_GROUP_in_create_full_group_cmd3517 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_id_list_pattern_in_create_full_group_cmd3523 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_full_group_cmd3525 = new BitSet(new long[] { 2L });
        FOLLOW_DESTROY_MONSTERS_in_destroy_monsters_cmd3547 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_destroy_monsters_cmd3551 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_destroy_monsters_cmd3553 = new BitSet(new long[] { 2L });
        FOLLOW_DESTROY_MONSTERS_in_destroy_monsters_cmd3560 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_destroy_monsters_cmd3564 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_destroy_monsters_cmd3568 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_destroy_monsters_cmd3570 = new BitSet(new long[] { 2L });
        FOLLOW_PLANT_RESOURCES_in_plant_resources_cmd3590 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_plant_resources_cmd3594 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_plant_resources_cmd3596 = new BitSet(new long[] { 2L });
        FOLLOW_PLANT_RESOURCES_in_plant_resources_cmd3604 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_plant_resources_cmd3608 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_plant_resources_cmd3612 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_plant_resources_cmd3614 = new BitSet(new long[] { 2L });
        FOLLOW_DESTROY_RESOURCES_in_destroy_resources_cmd3633 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_destroy_resources_cmd3637 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_destroy_resources_cmd3639 = new BitSet(new long[] { 2L });
        FOLLOW_DESTROY_RESOURCES_in_destroy_resources_cmd3647 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_destroy_resources_cmd3651 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_destroy_resources_cmd3655 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_destroy_resources_cmd3657 = new BitSet(new long[] { 2L });
        FOLLOW_ADD_TO_GROUP_in_add_to_group_cmd3674 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_to_group_cmd3678 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_to_group_cmd3682 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_to_group_cmd3686 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_add_to_group_cmd3688 = new BitSet(new long[] { 2L });
        FOLLOW_PLAY_ANIMATION_in_play_animation_cmd3703 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_animation_cmd3707 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_play_animation_cmd3711 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_play_animation_cmd3713 = new BitSet(new long[] { 2L });
        FOLLOW_PLAY_APS_in_play_aps_cmd3728 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_aps_cmd3732 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_aps_cmd3736 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_aps_cmd3740 = new BitSet(new long[] { 1048576L });
        FOLLOW_BOOLEAN_in_play_aps_cmd3744 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_play_aps_cmd3746 = new BitSet(new long[] { 2L });
        FOLLOW_PLAY_APS_in_play_aps_cmd3753 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_aps_cmd3757 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_aps_cmd3761 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_play_aps_cmd3765 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_play_aps_cmd3767 = new BitSet(new long[] { 2L });
        FOLLOW_RIGHTS_in_rights_cmd3784 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_rights_cmd3786 = new BitSet(new long[] { 2L });
        FOLLOW_CHAOS_in_chaos_cmd3801 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_chaos_cmd3803 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_chaos_cmd3807 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_chaos_cmd3809 = new BitSet(new long[] { 2L });
        FOLLOW_CHAOS_in_chaos_cmd3815 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_chaos_cmd3817 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_chaos_cmd3821 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_chaos_cmd3825 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_chaos_cmd3827 = new BitSet(new long[] { 2L });
        FOLLOW_CHAOS_in_chaos_cmd3833 = new BitSet(new long[] { 0L, 0L, 4194304L });
        FOLLOW_STOP_in_chaos_cmd3835 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_chaos_cmd3839 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_chaos_cmd3841 = new BitSet(new long[] { 2L });
        FOLLOW_RESTART_CHAOS_in_restart_chaos_cmd3858 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_NUMBER_in_restart_chaos_cmd3862 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_restart_chaos_cmd3865 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_ITEM_in_create_item_cmd3881 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_item_cmd3885 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_NUMBER_in_create_item_cmd3889 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_item_cmd3892 = new BitSet(new long[] { 2L });
        FOLLOW_CREATE_SET_in_create_set_cmd3907 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_create_set_cmd3911 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_create_set_cmd3913 = new BitSet(new long[] { 2L });
        FOLLOW_DELETE_ITEM_in_delete_item_cmd3928 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_delete_item_cmd3932 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_NUMBER_in_delete_item_cmd3936 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_delete_item_cmd3939 = new BitSet(new long[] { 2L });
        FOLLOW_REGENERATE_in_regenerate_cmd3954 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_regenerate_cmd3956 = new BitSet(new long[] { 2L });
        FOLLOW_REGENERATE_WITH_ITEM_in_regenerate_with_item_cmd3971 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_regenerate_with_item_cmd3975 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_regenerate_with_item_cmd3977 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd3993 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd3995 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd4002 = new BitSet(new long[] { 0L, 0L, 19140298416324608L });
        FOLLOW_set_in_god_mode_cmd4004 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd4012 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd4019 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_god_mode_cmd4021 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd4029 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd4036 = new BitSet(new long[] { 0L, 0L, 0L, 37748736L });
        FOLLOW_set_in_god_mode_cmd4038 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd4046 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd4054 = new BitSet(new long[] { 0L, 0L, 0L, 24576L });
        FOLLOW_set_in_god_mode_cmd4056 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd4064 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd4072 = new BitSet(new long[] { 0L, 0L, 0L, 6144L });
        FOLLOW_set_in_god_mode_cmd4074 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd4082 = new BitSet(new long[] { 2L });
        FOLLOW_GOD_MODE_in_god_mode_cmd4090 = new BitSet(new long[] { 0L, 0L, 0L, 768L });
        FOLLOW_set_in_god_mode_cmd4092 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_god_mode_cmd4100 = new BitSet(new long[] { 2L });
        FOLLOW_BUFF_in_buff_character_cmd4118 = new BitSet(new long[] { 2L });
        FOLLOW_BUFF_in_buff_character_cmd4124 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_buff_character_cmd4128 = new BitSet(new long[] { 2L });
        FOLLOW_BUFF_in_buff_character_cmd4134 = new BitSet(new long[] { 0L, 0L, 0L, 128L });
        FOLLOW_199_in_buff_character_cmd4136 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_buff_character_cmd4140 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_buff_character_cmd4144 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_buff_character_cmd4146 = new BitSet(new long[] { 2L });
        FOLLOW_BUFF_in_buff_character_cmd4152 = new BitSet(new long[] { 0L, 0L, 0L, 70368744177664L });
        FOLLOW_238_in_buff_character_cmd4154 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_buff_character_cmd4158 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_buff_character_cmd4162 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_buff_character_cmd4164 = new BitSet(new long[] { 2L });
        FOLLOW_TURN_DURATION_in_turn_duration_cmd4182 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_turn_duration_cmd4186 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_turn_duration_cmd4188 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4207 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4209 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4220 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_pvp_cmd4222 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4230 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4237 = new BitSet(new long[] { 0L, 4611686018427387904L });
        FOLLOW_SET_in_pvp_cmd4239 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_pvp_cmd4243 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4245 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4252 = new BitSet(new long[] { 64L });
        FOLLOW_ADD_in_pvp_cmd4254 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_pvp_cmd4258 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4260 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4267 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 33685504L });
        FOLLOW_set_in_pvp_cmd4269 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_pvp_cmd4277 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4279 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4286 = new BitSet(new long[] { 2251799813685248L });
        FOLLOW_ENABLE_in_pvp_cmd4288 = new BitSet(new long[] { 0L, 100663296L });
        FOLLOW_set_in_pvp_cmd4292 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4298 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4305 = new BitSet(new long[] { 0L, 1099511627776L });
        FOLLOW_RECOMPUTE_POINTS_in_pvp_cmd4307 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4309 = new BitSet(new long[] { 2L });
        FOLLOW_PVP_in_pvp_cmd4316 = new BitSet(new long[] { 0L, 2251799813685248L });
        FOLLOW_RESET_REGRESSION_in_pvp_cmd4318 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pvp_cmd4320 = new BitSet(new long[] { 2L });
        FOLLOW_RUNACTION_in_run_action_cmd4337 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_run_action_cmd4341 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_run_action_cmd4345 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_run_action_cmd4347 = new BitSet(new long[] { 2L });
        FOLLOW_ENDSCENARIO_in_end_scenario_cmd4361 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_end_scenario_cmd4365 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_end_scenario_cmd4367 = new BitSet(new long[] { 2L });
        FOLLOW_SCENARIO_COMMAND_in_scenario_cmd4382 = new BitSet(new long[] { 0L, 0L, 0L, Long.MIN_VALUE });
        FOLLOW_255_in_scenario_cmd4384 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_scenario_cmd4388 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_scenario_cmd4390 = new BitSet(new long[] { 2L });
        FOLLOW_RELOADSCENARIOS_in_reload_scenarios_cmd4407 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_reload_scenarios_cmd4409 = new BitSet(new long[] { 2L });
        FOLLOW_ADD_SPELLXP_in_add_spellxp_cmd4423 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_spellxp_cmd4427 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_spellxp_cmd4431 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_add_spellxp_cmd4433 = new BitSet(new long[] { 2L });
        FOLLOW_SET_SPELLLEVEL_in_set_spelllevel_cmd4448 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_spelllevel_cmd4452 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_spelllevel_cmd4456 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_spelllevel_cmd4458 = new BitSet(new long[] { 2L });
        FOLLOW_ADD_SKILLXP_in_add_skillxp_cmd4473 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_skillxp_cmd4477 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_skillxp_cmd4481 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_add_skillxp_cmd4483 = new BitSet(new long[] { 2L });
        FOLLOW_ADD_ITEM_XP_in_add_itemxp_cmd4499 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_itemxp_cmd4503 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_add_itemxp_cmd4505 = new BitSet(new long[] { 2L });
        FOLLOW_RENT_ITEM_CMD_in_rent_item_cmd4520 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_rent_item_cmd4524 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_rent_item_cmd4528 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_rent_item_cmd4532 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_rent_item_cmd4534 = new BitSet(new long[] { 2L });
        FOLLOW_CHARACTER_CMD_in_character_cmd4549 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1152921504606846976L });
        FOLLOW_380_in_character_cmd4551 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_character_cmd4555 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_character_cmd4557 = new BitSet(new long[] { 2L });
        FOLLOW_CHARACTER_CMD_in_character_cmd4564 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_character_cmd4566 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_character_cmd4572 = new BitSet(new long[] { 2L });
        FOLLOW_CHARACTER_CMD_in_character_cmd4579 = new BitSet(new long[] { 0L, 4096L });
        FOLLOW_INFO_in_character_cmd4582 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_character_cmd4585 = new BitSet(new long[] { 2L });
        FOLLOW_CHARACTER_CMD_in_character_cmd4592 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 4611686018427387904L });
        FOLLOW_382_in_character_cmd4594 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_character_cmd4598 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_character_cmd4600 = new BitSet(new long[] { 2L });
        FOLLOW_CHARACTER_CMD_in_character_cmd4607 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 105553116266496L });
        FOLLOW_set_in_character_cmd4609 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_character_cmd4617 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_character_cmd4619 = new BitSet(new long[] { 2L });
        FOLLOW_RESTORE_CHARACTER_CMD_in_restore_character_cmd4636 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_restore_character_cmd4640 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_restore_character_cmd4642 = new BitSet(new long[] { 2L });
        FOLLOW_SET_ITEM_TRACKER_LOG_LEVEL_CMD_in_set_item_tracker_log_level_cmd4661 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_item_tracker_log_level_cmd4665 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_item_tracker_log_level_cmd4667 = new BitSet(new long[] { 2L });
        FOLLOW_SET_ITEM_TRACKER_LOG_LEVEL_CMD_in_set_item_tracker_log_level_cmd4674 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_set_item_tracker_log_level_cmd4676 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_item_tracker_log_level_cmd4682 = new BitSet(new long[] { 2L });
        FOLLOW_SET_SKILL_LEVEL_in_set_skill_level_cmd4700 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_skill_level_cmd4704 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_skill_level_cmd4708 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_skill_level_cmd4710 = new BitSet(new long[] { 2L });
        FOLLOW_MONSTER_GROUP_in_monster_group4725 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 10240L });
        FOLLOW_set_in_monster_group4727 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_monster_group4738 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_monster_group4740 = new BitSet(new long[] { 2L });
        FOLLOW_SET_RESOURCE_SPEED_FACTOR_in_set_resource_speed_factor4758 = new BitSet(new long[] { 1152921504606846976L });
        FOLLOW_FLOAT_in_set_resource_speed_factor4762 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_resource_speed_factor4764 = new BitSet(new long[] { 2L });
        FOLLOW_ADD_XP_in_add_xp_cmd4779 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_xp_cmd4783 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_add_xp_cmd4785 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4801 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4803 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4810 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_set_bonus_factor_cmd4812 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4820 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4827 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 9007199254740992L });
        FOLLOW_309_in_set_bonus_factor_cmd4830 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd4835 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4841 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4846 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4852 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4855 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4862 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 8796093022208L });
        FOLLOW_299_in_set_bonus_factor_cmd4865 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd4870 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4876 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4881 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4887 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4891 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4898 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 16777216L });
        FOLLOW_344_in_set_bonus_factor_cmd4901 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd4906 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4912 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4917 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4923 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4927 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4934 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 2L });
        FOLLOW_321_in_set_bonus_factor_cmd4937 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd4942 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4948 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4953 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4959 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4963 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd4970 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 16L });
        FOLLOW_324_in_set_bonus_factor_cmd4973 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd4978 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4984 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd4989 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd4995 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd4999 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd5006 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 18014398509481984L });
        FOLLOW_310_in_set_bonus_factor_cmd5009 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd5014 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd5020 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd5025 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd5031 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd5035 = new BitSet(new long[] { 2L });
        FOLLOW_SET_BONUS_FACTOR_in_set_bonus_factor_cmd5042 = new BitSet(new long[] { 0L, 137438953472L });
        FOLLOW_PVP_in_set_bonus_factor_cmd5045 = new BitSet(new long[] { 1161930902884843520L, 8388608L });
        FOLLOW_FLOAT_in_set_bonus_factor_cmd5050 = new BitSet(new long[] { 9009398277996544L, 8388608L });
        FOLLOW_DATE_in_set_bonus_factor_cmd5056 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_set_bonus_factor_cmd5061 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_set_bonus_factor_cmd5067 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_bonus_factor_cmd5071 = new BitSet(new long[] { 2L });
        FOLLOW_ADD_MONEY_in_add_money_cmd5088 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_add_money_cmd5092 = new BitSet(new long[] { 2L });
        FOLLOW_HELP_in_help_cmd5107 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_help_cmd5111 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_help_cmd5113 = new BitSet(new long[] { 2L });
        FOLLOW_HELP_in_help_cmd5120 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_help_cmd5124 = new BitSet(new long[] { 0L, 0L, 0L, 1103806595072L });
        FOLLOW_set_in_help_cmd5126 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_help_cmd5134 = new BitSet(new long[] { 2L });
        FOLLOW_HELP_in_help_cmd5142 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_help_cmd5144 = new BitSet(new long[] { 2L });
        FOLLOW_HELP_in_help_cmd5152 = new BitSet(new long[] { 0L, 0L, 0L, 1103806595072L });
        FOLLOW_set_in_help_cmd5154 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_help_cmd5162 = new BitSet(new long[] { 2L });
        FOLLOW_INSTANCE_USAGE_in_instance_usage_cmd5181 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_instance_usage_cmd5185 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_instance_usage_cmd5187 = new BitSet(new long[] { 2L });
        FOLLOW_DESTROY_INSTANCE_in_destroy_instance_cmd5203 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_destroy_instance_cmd5205 = new BitSet(new long[] { 2L });
        FOLLOW_SHOW_AGGRO_LIST_in_show_aggro_list_cmd5222 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_show_aggro_list_cmd5224 = new BitSet(new long[] { 2L });
        FOLLOW_SET_LEVEL_in_set_level_cmd5240 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_level_cmd5244 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_level_cmd5246 = new BitSet(new long[] { 2L });
        FOLLOW_SPAWN_INTERACTIVE_ELEMENT_in_spawn_ie_cmd5259 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_spawn_ie_cmd5265 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_spawn_ie_cmd5271 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_spawn_ie_cmd5273 = new BitSet(new long[] { 2L });
        FOLLOW_SESSIONS_in_sessions_cmd5288 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_sessions_cmd5290 = new BitSet(new long[] { 2L });
        FOLLOW_SETNEXTCHALLENGE_in_set_next_challenge_cmd5306 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_next_challenge_cmd5312 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_next_challenge_cmd5314 = new BitSet(new long[] { 2L });
        FOLLOW_FINISHCHALLENGE_in_finish_challenge_cmd5330 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_finish_challenge_cmd5332 = new BitSet(new long[] { 2L });
        FOLLOW_STAFF_in_staff_cmd5348 = new BitSet(new long[] { 0L, 67108864L });
        FOLLOW_ON_in_staff_cmd5350 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_staff_cmd5352 = new BitSet(new long[] { 2L });
        FOLLOW_STAFF_in_staff_cmd5359 = new BitSet(new long[] { 0L, 33554432L });
        FOLLOW_OFF_in_staff_cmd5361 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_staff_cmd5363 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5383 = new BitSet(new long[] { 0L, 67108864L });
        FOLLOW_ON_in_subscriber_cmd5385 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5387 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5394 = new BitSet(new long[] { 0L, 33554432L });
        FOLLOW_OFF_in_subscriber_cmd5396 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5398 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5405 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5409 = new BitSet(new long[] { 0L, 67108864L });
        FOLLOW_ON_in_subscriber_cmd5411 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5413 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5420 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5424 = new BitSet(new long[] { 0L, 33554432L });
        FOLLOW_OFF_in_subscriber_cmd5426 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5428 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5435 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 562949953421312L, 524288L });
        FOLLOW_set_in_subscriber_cmd5437 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5445 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5452 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_subscriber_cmd5454 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5462 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5469 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 2080L });
        FOLLOW_set_in_subscriber_cmd5471 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5479 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5486 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 262272L });
        FOLLOW_set_in_subscriber_cmd5488 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5496 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5503 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 135266304L });
        FOLLOW_set_in_subscriber_cmd5505 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5515 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5517 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5524 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 538968064L });
        FOLLOW_set_in_subscriber_cmd5526 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5536 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5538 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5545 = new BitSet(new long[] { 0L, 2251799813685248L, 0L, 0L, 0L, 34359738368L });
        FOLLOW_set_in_subscriber_cmd5547 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5557 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5559 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5566 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 2305843009213693952L, 2097152L });
        FOLLOW_set_in_subscriber_cmd5568 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5578 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5580 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5587 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 4194816L });
        FOLLOW_set_in_subscriber_cmd5589 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_subscriber_cmd5599 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5601 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5608 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 140737488355328L, 8L });
        FOLLOW_set_in_subscriber_cmd5610 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5618 = new BitSet(new long[] { 2L });
        FOLLOW_SUBSCRIBER_in_subscriber_cmd5625 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 4406636445696L });
        FOLLOW_set_in_subscriber_cmd5627 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_subscriber_cmd5635 = new BitSet(new long[] { 2L });
        FOLLOW_FREE_ACCESS_in_free_access_cmd5653 = new BitSet(new long[] { 0L, 67108864L });
        FOLLOW_ON_in_free_access_cmd5655 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_free_access_cmd5657 = new BitSet(new long[] { 2L });
        FOLLOW_FREE_ACCESS_in_free_access_cmd5664 = new BitSet(new long[] { 0L, 33554432L });
        FOLLOW_OFF_in_free_access_cmd5666 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_free_access_cmd5668 = new BitSet(new long[] { 2L });
        FOLLOW_FREE_ACCESS_in_free_access_cmd5676 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_free_access_cmd5678 = new BitSet(new long[] { 2L });
        FOLLOW_MUTE_PARTITIONS_in_mute_partitions_cmd5699 = new BitSet(new long[] { 0L, 68719476736L });
        FOLLOW_proximity_pattern_in_mute_partitions_cmd5703 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_mute_partitions_cmd5705 = new BitSet(new long[] { 2L });
        FOLLOW_MUTE_PARTITIONS_in_mute_partitions_cmd5712 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_mute_partitions_cmd5714 = new BitSet(new long[] { 2L });
        FOLLOW_UNMUTE_PARTITIONS_in_unmute_partitions_cmd5731 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_unmute_partitions_cmd5733 = new BitSet(new long[] { 2L });
        FOLLOW_MUTE_in_mute_cmd5759 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_NUMBER_in_mute_cmd5764 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_mute_cmd5768 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_mute_cmd5772 = new BitSet(new long[] { 2L });
        FOLLOW_MUTE_in_mute_cmd5779 = new BitSet(new long[] { 0L, 4096L });
        FOLLOW_INFO_in_mute_cmd5782 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_mute_cmd5785 = new BitSet(new long[] { 2L });
        FOLLOW_UNMUTE_in_unmute_cmd5818 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_unmute_cmd5822 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_unmute_cmd5824 = new BitSet(new long[] { 2L });
        FOLLOW_DISTRIBUTE_ITEMS_in_distribute_items_cmd5842 = new BitSet(new long[] { 0L, 68719476736L });
        FOLLOW_proximity_pattern_in_distribute_items_cmd5846 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_distribute_items_cmd5850 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_distribute_items_cmd5854 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_distribute_items_cmd5856 = new BitSet(new long[] { 2L });
        FOLLOW_DISTRIBUTE_ITEMS_in_distribute_items_cmd5863 = new BitSet(new long[] { 0L, 68719476736L });
        FOLLOW_proximity_pattern_in_distribute_items_cmd5867 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_distribute_items_cmd5871 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_distribute_items_cmd5873 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd5891 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 2199023255552L });
        FOLLOW_361_in_search_cmd5893 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_search_cmd5897 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd5899 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd5907 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 256L });
        FOLLOW_328_in_search_cmd5909 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_397_in_search_cmd5913 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_search_cmd5918 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd5920 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd5927 = new BitSet(new long[] { 0L, 0L, 0L, 0L, Long.MIN_VALUE });
        FOLLOW_319_in_search_cmd5929 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_search_cmd5933 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd5935 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd5943 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 274877906944L });
        FOLLOW_358_in_search_cmd5945 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_search_cmd5949 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd5951 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd5961 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1L });
        FOLLOW_320_in_search_cmd5963 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_394_in_search_cmd5968 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_search_cmd5973 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd5975 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd5985 = new BitSet(new long[] { 0L, 0L, 524288L });
        FOLLOW_STATE_CMD_in_search_cmd5987 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_403_in_search_cmd5992 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_search_cmd5997 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd5999 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd6009 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 512L });
        FOLLOW_329_in_search_cmd6011 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_398_in_search_cmd6016 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_search_cmd6021 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd6023 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd6030 = new BitSet(new long[] { 0L, 4611686018427387904L });
        FOLLOW_SET_in_search_cmd6032 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_search_cmd6036 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd6038 = new BitSet(new long[] { 2L });
        FOLLOW_SEARCH_in_search_cmd6045 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 536870912L });
        FOLLOW_349_in_search_cmd6047 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_search_cmd6051 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_search_cmd6053 = new BitSet(new long[] { 2L });
        FOLLOW_TELEPORT_TO_MONSTER_in_teleport_to_breed_mob_cmd6070 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_teleport_to_breed_mob_cmd6074 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_teleport_to_breed_mob_cmd6076 = new BitSet(new long[] { 2L });
        FOLLOW_QUOTA_in_quota_cmd6094 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_quota_cmd6096 = new BitSet(new long[] { 2L });
        FOLLOW_QUOTA_in_quota_cmd6106 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1140850688L });
        FOLLOW_set_in_quota_cmd6108 = new BitSet(new long[] { 1048576L });
        FOLLOW_348_in_quota_cmd6119 = new BitSet(new long[] { 1048576L });
        FOLLOW_BOOLEAN_in_quota_cmd6124 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_quota_cmd6126 = new BitSet(new long[] { 2L });
        FOLLOW_QUOTA_in_quota_cmd6135 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1310720L });
        FOLLOW_set_in_quota_cmd6137 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_347_in_quota_cmd6148 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_quota_cmd6153 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_quota_cmd6155 = new BitSet(new long[] { 2L });
        FOLLOW_RAGNAROK_in_ragnarok_cmd6173 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_ragnarok_cmd6177 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ragnarok_cmd6179 = new BitSet(new long[] { 2L });
        FOLLOW_REMOVE_FLOOR_ITEMS_in_remove_floor_items_cmd6196 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_remove_floor_items_cmd6198 = new BitSet(new long[] { 2L });
        FOLLOW_SHOW_POPULATION_in_show_population_cmd6217 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_show_population_cmd6221 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_show_population_cmd6223 = new BitSet(new long[] { 2L });
        FOLLOW_SHOW_MONSTER_QUOTA_in_show_monster_quota_cmd6241 = new BitSet(new long[] { 2L });
        FOLLOW_CANCEL_COLLECT_COOLDOWN_in_cancel_collect_cooldown_cmd6259 = new BitSet(new long[] { 2L });
        FOLLOW_SET_WAKFU_GAUGE_in_set_wakfu_gauge_cmd6277 = new BitSet(new long[] { 1152921504606846976L });
        FOLLOW_FLOAT_in_set_wakfu_gauge_cmd6281 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_wakfu_gauge_cmd6283 = new BitSet(new long[] { 2L });
        FOLLOW_GET_INSTANCE_UID_in_get_instance_uid_cmd6301 = new BitSet(new long[] { 2L });
        FOLLOW_DUMP_BAG_in_dump_bag_cmd6319 = new BitSet(new long[] { 2L });
        FOLLOW_TEMP_in_temp_cmd6337 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6354 = new BitSet(new long[] { 0L, 0L, 4503599627370496L });
        FOLLOW_180_in_calendar_cmd6356 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6360 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6364 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6368 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6372 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6376 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6380 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6382 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6388 = new BitSet(new long[] { 0L, 0L, 4503599627370496L });
        FOLLOW_180_in_calendar_cmd6390 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6394 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6398 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6402 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6406 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6410 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6414 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6418 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6422 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6424 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6431 = new BitSet(new long[] { 0L, 0L, 0L, 4398046511104L });
        FOLLOW_234_in_calendar_cmd6433 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6437 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6439 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6446 = new BitSet(new long[] { 0L, 0L, 0L, 2L });
        FOLLOW_193_in_calendar_cmd6448 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6450 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6457 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 32L });
        FOLLOW_261_in_calendar_cmd6459 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6463 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6465 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6472 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 32L });
        FOLLOW_261_in_calendar_cmd6474 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6476 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6483 = new BitSet(new long[] { 0L, 0L, 0L, 8589934592L });
        FOLLOW_225_in_calendar_cmd6485 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6489 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6491 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6497 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 8L });
        FOLLOW_259_in_calendar_cmd6499 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6503 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6505 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6511 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 128L });
        FOLLOW_263_in_calendar_cmd6513 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6517 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6521 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6523 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6529 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 16L });
        FOLLOW_260_in_calendar_cmd6531 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6535 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6539 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6541 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6547 = new BitSet(new long[] { 0L, 0L, 0L, 1073741824L });
        FOLLOW_222_in_calendar_cmd6549 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6553 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6557 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6561 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6563 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6569 = new BitSet(new long[] { 0L, 0L, 0L, 2097152L });
        FOLLOW_213_in_calendar_cmd6571 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6575 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6579 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6581 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6588 = new BitSet(new long[] { 0L, 0L, 0L, 18014398509481984L });
        FOLLOW_246_in_calendar_cmd6590 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6594 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6598 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6602 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6606 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6610 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6614 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6616 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6623 = new BitSet(new long[] { 0L, 0L, 288230376151711744L });
        FOLLOW_186_in_calendar_cmd6625 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6629 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6636 = new BitSet(new long[] { 0L, 0L, 0L, 9007199254740992L });
        FOLLOW_245_in_calendar_cmd6638 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6642 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6646 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6653 = new BitSet(new long[] { 0L, 0L, 0L, 72057594037927936L });
        FOLLOW_248_in_calendar_cmd6655 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6659 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6663 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6670 = new BitSet(new long[] { 0L, 0L, 0L, 36028797018963968L });
        FOLLOW_247_in_calendar_cmd6672 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6676 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6680 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6684 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6688 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6692 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6696 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6698 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6705 = new BitSet(new long[] { 0L, 0L, 0L, 36028797018963968L });
        FOLLOW_247_in_calendar_cmd6707 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6711 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6715 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6719 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6723 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_calendar_cmd6725 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6732 = new BitSet(new long[] { 0L, 0L, 0L, 36028797018963968L });
        FOLLOW_247_in_calendar_cmd6734 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6738 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_calendar_cmd6742 = new BitSet(new long[] { 2L });
        FOLLOW_CALENDAR_CMD_in_calendar_cmd6749 = new BitSet(new long[] { 0L, 0L, 0L, 65536L });
        FOLLOW_208_in_calendar_cmd6751 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_calendar_cmd6755 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6774 = new BitSet(new long[] { 0L, 0L, 0L, 289356276058554368L });
        FOLLOW_set_in_fight_cmd6776 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6784 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6791 = new BitSet(new long[] { 0L, 0L, 0L, 2814749767106560L });
        FOLLOW_set_in_fight_cmd6793 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_fight_cmd6803 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6806 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6813 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_fight_cmd6815 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6828 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 256L });
        FOLLOW_264_in_fight_cmd6830 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6832 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6839 = new BitSet(new long[] { 0L, 0L, 0L, 32768L });
        FOLLOW_207_in_fight_cmd6841 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6843 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6850 = new BitSet(new long[] { 0L, 0L, 0L, 2684354560L });
        FOLLOW_set_in_fight_cmd6852 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6860 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6867 = new BitSet(new long[] { 0L, 0L, 144115188075855872L, 4L });
        FOLLOW_set_in_fight_cmd6869 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_fight_cmd6879 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6881 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6888 = new BitSet(new long[] { 0L, 0L, 576460752303423488L, 8L });
        FOLLOW_set_in_fight_cmd6890 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6898 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6905 = new BitSet(new long[] { 0L, 0L, 2305843009213693952L, 1L });
        FOLLOW_set_in_fight_cmd6907 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_fight_cmd6917 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6919 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6926 = new BitSet(new long[] { 0L, 0L, 1152921504606846976L, 16L });
        FOLLOW_set_in_fight_cmd6928 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6936 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CMD_in_fight_cmd6943 = new BitSet(new long[] { 0L, 0L, 0L, 4616189618054758400L });
        FOLLOW_set_in_fight_cmd6945 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_fight_cmd6955 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fight_cmd6957 = new BitSet(new long[] { 2L });
        FOLLOW_PROTECTOR_CMD_in_protector_command6975 = new BitSet(new long[] { 0L, 0L, -4611686018427387904L });
        FOLLOW_set_in_protector_command6977 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_protector_command6987 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_protector_command6991 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_protector_command6993 = new BitSet(new long[] { 2L });
        FOLLOW_PROTECTOR_CMD_in_protector_command7004 = new BitSet(new long[] { 0L, 0L, 45035996273704960L });
        FOLLOW_set_in_protector_command7006 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_protector_command7016 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_protector_command7020 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_protector_command7022 = new BitSet(new long[] { 2L });
        FOLLOW_PROTECTOR_CMD_in_protector_command7033 = new BitSet(new long[] { 0L, 0L, 0L, 75497472L });
        FOLLOW_set_in_protector_command7035 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_protector_command7045 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_protector_command7047 = new BitSet(new long[] { 2L });
        FOLLOW_PROTECTOR_CMD_in_protector_command7057 = new BitSet(new long[] { 0L, 0L, 0L, 150994944L });
        FOLLOW_set_in_protector_command7059 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_protector_command7069 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_protector_command7071 = new BitSet(new long[] { 2L });
        FOLLOW_PROTECTOR_CMD_in_protector_command7081 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_protector_command7083 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_protector_command7091 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7108 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_state_command7110 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7118 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7125 = new BitSet(new long[] { 0L, 0L, 0L, 144185556820033536L });
        FOLLOW_set_in_state_command7127 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7135 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7142 = new BitSet(new long[] { 0L, 0L, 0L, 576601489791778816L });
        FOLLOW_set_in_state_command7144 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7152 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7159 = new BitSet(new long[] { 0L, 0L, 0L, 1153202979583557632L });
        FOLLOW_set_in_state_command7161 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_state_command7171 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7173 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7180 = new BitSet(new long[] { 0L, 0L, 5629499534213120L });
        FOLLOW_set_in_state_command7182 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_state_command7192 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7194 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7201 = new BitSet(new long[] { 0L, 0L, 5629499534213120L });
        FOLLOW_set_in_state_command7203 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_state_command7213 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_state_command7217 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7219 = new BitSet(new long[] { 2L });
        FOLLOW_STATE_CMD_in_state_command7226 = new BitSet(new long[] { 0L, 0L, 0L, 21474836480L });
        FOLLOW_set_in_state_command7228 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_state_command7238 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_state_command7240 = new BitSet(new long[] { 2L });
        FOLLOW_SPELL_CMD_in_spell_command7258 = new BitSet(new long[] { 0L, 0L, 0L, 549755813888L });
        FOLLOW_231_in_spell_command7261 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_spell_command7264 = new BitSet(new long[] { 2L });
        FOLLOW_CLIENT_GAME_EVENT_CMD_in_client_game_event_command7285 = new BitSet(new long[] { 0L, 0L, 3377699720527872L });
        FOLLOW_set_in_client_game_event_command7287 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_client_game_event_command7297 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_client_game_event_command7299 = new BitSet(new long[] { 2L });
        FOLLOW_CLIENT_GAME_EVENT_CMD_in_client_game_event_command7310 = new BitSet(new long[] { 0L, 0L, 0L, 786432L });
        FOLLOW_set_in_client_game_event_command7312 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_client_game_event_command7322 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_client_game_event_command7324 = new BitSet(new long[] { 2L });
        FOLLOW_GEM_CMD_in_gem_command7345 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 8L });
        FOLLOW_323_in_gem_command7348 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_gem_command7351 = new BitSet(new long[] { 2L });
        FOLLOW_GEM_CMD_in_gem_command7358 = new BitSet(new long[] { 549755813888L });
        FOLLOW_CREATE_ITEM_in_gem_command7361 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_gem_command7366 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_gem_command7370 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_gem_command7374 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_gem_command7376 = new BitSet(new long[] { 2L });
        FOLLOW_GEM_CMD_in_gem_command7387 = new BitSet(new long[] { 549755813888L });
        FOLLOW_CREATE_ITEM_in_gem_command7390 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_gem_command7395 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_gem_command7399 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_gem_command7401 = new BitSet(new long[] { 2L });
        FOLLOW_GEM_CMD_in_gem_command7412 = new BitSet(new long[] { 549755813888L });
        FOLLOW_CREATE_ITEM_in_gem_command7415 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_gem_command7420 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_gem_command7422 = new BitSet(new long[] { 2L });
        FOLLOW_GEM_CMD_in_gem_command7433 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_gem_command7435 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_gem_command7443 = new BitSet(new long[] { 2L });
        FOLLOW_GEM_CMD_in_gem_command7454 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_gem_command7456 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7478 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_aptitude_command7480 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7486 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7493 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 17592186044416L, 4L });
        FOLLOW_set_in_aptitude_command7495 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7501 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7508 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 1048832L });
        FOLLOW_set_in_aptitude_command7510 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7516 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7523 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 144115188075855872L, 2048L });
        FOLLOW_set_in_aptitude_command7525 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_aptitude_command7533 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_aptitude_command7537 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7539 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7546 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 16842752L });
        FOLLOW_set_in_aptitude_command7548 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_aptitude_command7556 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_aptitude_command7560 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7562 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7569 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 67371008L });
        FOLLOW_set_in_aptitude_command7571 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_aptitude_command7579 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_aptitude_command7583 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7585 = new BitSet(new long[] { 2L });
        FOLLOW_APTITUDE_in_aptitude_command7592 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 549755813888L });
        FOLLOW_359_in_aptitude_command7595 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_aptitude_command7598 = new BitSet(new long[] { 2L });
        FOLLOW_VERSION_in_version_cmd7616 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_version_cmd7618 = new BitSet(new long[] { 2L });
        FOLLOW_SET_RESPAWN_CMD_in_set_respawn_cmd7645 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_respawn_cmd7647 = new BitSet(new long[] { 2L });
        FOLLOW_CHECK_CMD_in_check_cmd7662 = new BitSet(new long[] { 0L, 0L, 562949953421312L, 0L, 1L });
        FOLLOW_set_in_check_cmd7664 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_check_cmd7674 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_check_cmd7676 = new BitSet(new long[] { 2L });
        FOLLOW_CHECK_CMD_in_check_cmd7687 = new BitSet(new long[] { 0L, 0L, 562949953421312L, 0L, 1L });
        FOLLOW_set_in_check_cmd7689 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_check_cmd7697 = new BitSet(new long[] { 2L });
        FOLLOW_CHECK_CMD_in_check_cmd7708 = new BitSet(new long[] { 0L, 0L, 70368744177664L, 131072L });
        FOLLOW_set_in_check_cmd7710 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_check_cmd7718 = new BitSet(new long[] { 2L });
        FOLLOW_CRAFT_CMD_in_craft_cmd7737 = new BitSet(new long[] { 0L, 0L, 140737488355328L, 268435456L });
        FOLLOW_set_in_craft_cmd7739 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_craft_cmd7749 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_craft_cmd7751 = new BitSet(new long[] { 2L });
        FOLLOW_CRAFT_CMD_in_craft_cmd7762 = new BitSet(new long[] { 0L, 0L, 281474976710656L, 0L, 4L });
        FOLLOW_set_in_craft_cmd7764 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_craft_cmd7774 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_craft_cmd7776 = new BitSet(new long[] { 2L });
        FOLLOW_CRAFT_CMD_in_craft_cmd7787 = new BitSet(new long[] { 0L, 0L, 72092778410016768L });
        FOLLOW_set_in_craft_cmd7788 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_craft_cmd7798 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_craft_cmd7802 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_craft_cmd7804 = new BitSet(new long[] { 2L });
        FOLLOW_CRAFT_CMD_in_craft_cmd7815 = new BitSet(new long[] { 0L, 0L, 70368744177664L, 131072L });
        FOLLOW_set_in_craft_cmd7817 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_craft_cmd7825 = new BitSet(new long[] { 2L });
        FOLLOW_ICE_STATUS_in_ice_status_cmd7845 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ice_status_cmd7847 = new BitSet(new long[] { 2L });
        FOLLOW_PET_in_pet_cmd7863 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 1073741824L });
        FOLLOW_414_in_pet_cmd7866 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_pet_cmd7871 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pet_cmd7873 = new BitSet(new long[] { 2L });
        FOLLOW_PET_in_pet_cmd7885 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3377699720527872L });
        FOLLOW_set_in_pet_cmd7887 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_pet_cmd7893 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd7915 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 38010880L });
        FOLLOW_set_in_guild_cmd7917 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd7931 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd7933 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd7941 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 128L });
        FOLLOW_327_in_guild_cmd7944 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd7949 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd7951 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd7959 = new BitSet(new long[] { 0L, 0L, 1048576L });
        FOLLOW_STATS_in_guild_cmd7962 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd7965 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd7973 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 20480L });
        FOLLOW_set_in_guild_cmd7975 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd7985 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd7987 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd7995 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 72057594037927936L, 4096L });
        FOLLOW_set_in_guild_cmd7997 = new BitSet(new long[] { 1152921504606846976L });
        FOLLOW_FLOAT_in_guild_cmd8007 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8009 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8017 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 288230376151711744L, 32768L });
        FOLLOW_set_in_guild_cmd8019 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd8029 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8031 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8039 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 4647714815446351872L });
        FOLLOW_set_in_guild_cmd8041 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8049 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8057 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1056L });
        FOLLOW_set_in_guild_cmd8059 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd8069 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8071 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8079 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 8912896L });
        FOLLOW_set_in_guild_cmd8081 = new BitSet(new long[] { 1152921504606846976L });
        FOLLOW_FLOAT_in_guild_cmd8091 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8093 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8101 = new BitSet(new long[] { 0L, 4096L });
        FOLLOW_INFO_in_guild_cmd8104 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8107 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8115 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 2305843009213693952L });
        FOLLOW_317_in_guild_cmd8118 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8121 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8129 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 144115188075855872L, 2048L });
        FOLLOW_set_in_guild_cmd8131 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd8141 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8143 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8151 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 576460752303423488L, 65536L });
        FOLLOW_set_in_guild_cmd8153 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_guild_cmd8163 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8165 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8173 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 51539607552L });
        FOLLOW_set_in_guild_cmd8175 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_guild_cmd8185 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8187 = new BitSet(new long[] { 2L });
        FOLLOW_GUILD_in_guild_cmd8195 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 618475290624L });
        FOLLOW_set_in_guild_cmd8197 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_guild_cmd8207 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_guild_cmd8209 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8231 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_companion_cmd8233 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8241 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8251 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 12L });
        FOLLOW_set_in_companion_cmd8253 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8261 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8271 = new BitSet(new long[] { 64L, 0L, 0L, 0L, 2048L });
        FOLLOW_set_in_companion_cmd8273 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8283 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8285 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8295 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 283467841536L });
        FOLLOW_set_in_companion_cmd8297 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8305 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8315 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 805306368L });
        FOLLOW_set_in_companion_cmd8317 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8325 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8335 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 70866960384L });
        FOLLOW_set_in_companion_cmd8337 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8347 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_companion_cmd8351 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8353 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8363 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 100663296L });
        FOLLOW_set_in_companion_cmd8365 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8375 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8377 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8387 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 1077936128L });
        FOLLOW_set_in_companion_cmd8389 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8399 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8401 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8411 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 8388608L, 0L, 1073741824L });
        FOLLOW_set_in_companion_cmd8413 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8423 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8427 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8429 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8439 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 4503599627370496L, 9007199254740992L });
        FOLLOW_set_in_companion_cmd8441 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8451 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8453 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8463 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 17L });
        FOLLOW_set_in_companion_cmd8465 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8473 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8483 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 648518483780304896L });
        FOLLOW_set_in_companion_cmd8485 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8497 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8507 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 8192L });
        FOLLOW_333_in_companion_cmd8510 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8513 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8523 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 2251799813685312L });
        FOLLOW_set_in_companion_cmd8525 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8533 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8543 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, -9205357638345293824L });
        FOLLOW_set_in_companion_cmd8545 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8555 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8559 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8563 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8567 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8569 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8584 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 562949953454080L });
        FOLLOW_set_in_companion_cmd8586 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8596 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8600 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8605 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8607 = new BitSet(new long[] { 2L });
        FOLLOW_COMPANION_in_companion_cmd8625 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 8813272891392L });
        FOLLOW_set_in_companion_cmd8627 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8637 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8642 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8646 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_companion_cmd8650 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_companion_cmd8653 = new BitSet(new long[] { 2L });
        FOLLOW_HERO_in_hero_cmd8681 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_hero_cmd8683 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_hero_cmd8691 = new BitSet(new long[] { 2L });
        FOLLOW_HERO_in_hero_cmd8698 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 8388608L, 0L, 1073741824L });
        FOLLOW_set_in_hero_cmd8700 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_hero_cmd8710 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_hero_cmd8714 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_hero_cmd8716 = new BitSet(new long[] { 2L });
        FOLLOW_HERO_in_hero_cmd8723 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 12L });
        FOLLOW_set_in_hero_cmd8725 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_hero_cmd8733 = new BitSet(new long[] { 2L });
        FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8752 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_system_configuration_cmd8755 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_system_configuration_cmd8763 = new BitSet(new long[] { 2L });
        FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8772 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 4L });
        FOLLOW_386_in_system_configuration_cmd8776 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_system_configuration_cmd8781 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_system_configuration_cmd8785 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_system_configuration_cmd8787 = new BitSet(new long[] { 2L });
        FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8796 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 4294967296L });
        FOLLOW_352_in_system_configuration_cmd8800 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_system_configuration_cmd8805 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_system_configuration_cmd8807 = new BitSet(new long[] { 2L });
        FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8816 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 6144L });
        FOLLOW_set_in_system_configuration_cmd8819 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_system_configuration_cmd8827 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_system_configuration_cmd8829 = new BitSet(new long[] { 2L });
        FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8838 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 2147483648L });
        FOLLOW_415_in_system_configuration_cmd8842 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_system_configuration_cmd8847 = new BitSet(new long[] { 9009398277996544L });
        FOLLOW_DATE_in_system_configuration_cmd8852 = new BitSet(new long[] { 2199023255552L });
        FOLLOW_DATE_in_system_configuration_cmd8857 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_system_configuration_cmd8861 = new BitSet(new long[] { 2L });
        FOLLOW_SYSTEM_CONFIGURATION_in_system_configuration_cmd8870 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 2097152L });
        FOLLOW_341_in_system_configuration_cmd8874 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_system_configuration_cmd8879 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_system_configuration_cmd8881 = new BitSet(new long[] { 2L });
        FOLLOW_AI_in_ai_cmd8905 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_ai_cmd8907 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ai_cmd8915 = new BitSet(new long[] { 2L });
        FOLLOW_AI_in_ai_cmd8926 = new BitSet(new long[] { 0L, 67108864L });
        FOLLOW_ON_in_ai_cmd8928 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_ai_cmd8932 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ai_cmd8934 = new BitSet(new long[] { 2L });
        FOLLOW_AI_in_ai_cmd8945 = new BitSet(new long[] { 0L, 33554432L });
        FOLLOW_OFF_in_ai_cmd8947 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_message_in_ai_cmd8951 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_ai_cmd8953 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd8978 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_fightchallenge_cmd8980 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fightchallenge_cmd8988 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd8999 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 12L });
        FOLLOW_set_in_fightchallenge_cmd9001 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fightchallenge_cmd9009 = new BitSet(new long[] { 2L });
        FOLLOW_FIGHT_CHALLENGE_in_fightchallenge_cmd9020 = new BitSet(new long[] { 0L, 0L, 131072L, 0L, 0L, 17592186044416L });
        FOLLOW_set_in_fightchallenge_cmd9022 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_fightchallenge_cmd9032 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_fightchallenge_cmd9034 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9055 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_havenworld_cmd9057 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9059 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9067 = new BitSet(new long[] { 0L, 0L, 4194304L });
        FOLLOW_STOP_in_havenworld_cmd9069 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9071 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9079 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_411_in_havenworld_cmd9081 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 4398046511104L });
        FOLLOW_298_in_havenworld_cmd9083 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9087 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9091 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9093 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9101 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 134217728L });
        FOLLOW_411_in_havenworld_cmd9103 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 0L, 536870912L });
        FOLLOW_413_in_havenworld_cmd9105 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9109 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9113 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9117 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9119 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9127 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 4294967296L });
        FOLLOW_288_in_havenworld_cmd9129 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 4398046511104L });
        FOLLOW_298_in_havenworld_cmd9131 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9135 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9139 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9143 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9145 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9153 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 4294967296L });
        FOLLOW_288_in_havenworld_cmd9155 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 17592186044416L });
        FOLLOW_300_in_havenworld_cmd9157 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9161 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9163 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9171 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 2199023255552L });
        FOLLOW_297_in_havenworld_cmd9173 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9175 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9183 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 2147483648L });
        FOLLOW_287_in_havenworld_cmd9185 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9189 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9193 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9195 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9210 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 35184372088832L, 8388608L });
        FOLLOW_set_in_havenworld_cmd9212 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9222 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9224 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9232 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 422212465065984L });
        FOLLOW_set_in_havenworld_cmd9234 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9244 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9246 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9258 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1236950581248L });
        FOLLOW_set_in_havenworld_cmd9260 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9270 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9272 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9288 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 36028797018963968L, 2L });
        FOLLOW_set_in_havenworld_cmd9290 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9300 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9304 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9306 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9322 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 134742016L });
        FOLLOW_set_in_havenworld_cmd9324 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9334 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9338 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9340 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9356 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 4785074604081152L });
        FOLLOW_set_in_havenworld_cmd9358 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9368 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9372 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9376 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9380 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9384 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9388 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9392 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9394 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9413 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 0L, 1196268651020288L });
        FOLLOW_set_in_havenworld_cmd9415 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9425 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_havenworld_cmd9429 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9431 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_WORLD_in_havenworld_cmd9450 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_havenworld_cmd9452 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_havenworld_cmd9460 = new BitSet(new long[] { 2L });
        FOLLOW_ALMANACH_in_almanach_start_cmd9484 = new BitSet(new long[] { 0L, 0L, 131072L });
        FOLLOW_START_in_almanach_start_cmd9486 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_almanach_start_cmd9490 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_almanach_start_cmd9492 = new BitSet(new long[] { 2L });
        FOLLOW_LEARN_EMOTE_in_learn_emote_cmd9511 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_learn_emote_cmd9515 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_learn_emote_cmd9517 = new BitSet(new long[] { 2L });
        FOLLOW_SET_PLAYER_TITLE_in_set_player_title_cmd9540 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_set_player_title_cmd9544 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_set_player_title_cmd9548 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_set_player_title_cmd9550 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9573 = new BitSet(new long[] { 0L, 0L, 0L, 1152L });
        FOLLOW_set_in_inventory_cmd9575 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9583 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9593 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 3458764513820540928L });
        FOLLOW_set_in_inventory_cmd9595 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9603 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9613 = new BitSet(new long[] { 0L, 0L, 0L, 2305913377957871616L });
        FOLLOW_set_in_inventory_cmd9615 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9623 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9633 = new BitSet(new long[] { 0L, 0L, 0L, 8933531975680L });
        FOLLOW_set_in_inventory_cmd9635 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_inventory_cmd9645 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9648 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9658 = new BitSet(new long[] { 0L, 0L, 0L, 17867063951360L });
        FOLLOW_set_in_inventory_cmd9660 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_inventory_cmd9670 = new BitSet(new long[] { 9007199254740992L, 8388608L });
        FOLLOW_id_list_pattern_in_inventory_cmd9674 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9677 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9687 = new BitSet(new long[] { 0L, 0L, 0L, 96L });
        FOLLOW_set_in_inventory_cmd9689 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9697 = new BitSet(new long[] { 2L });
        FOLLOW_INVENTORY_in_inventory_cmd9707 = new BitSet(new long[] { 0L, 0L, 0L, 35218731827200L });
        FOLLOW_set_in_inventory_cmd9709 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_inventory_cmd9719 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_inventory_cmd9723 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_inventory_cmd9725 = new BitSet(new long[] { 2L });
        FOLLOW_EMPTY_CHAR_in_empty_char_cmd9749 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_empty_char_cmd9751 = new BitSet(new long[] { 2L });
        FOLLOW_POPUP_MESSAGE_in_popup_message_cmd9779 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_popup_message_cmd9783 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_ESCAPED_STRING_in_popup_message_cmd9787 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_popup_message_cmd9789 = new BitSet(new long[] { 2L });
        FOLLOW_RED_MESSAGE_in_red_message_cmd9817 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_ESCAPED_STRING_in_red_message_cmd9821 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_red_message_cmd9823 = new BitSet(new long[] { 2L });
        FOLLOW_RED_MESSAGE_TO_PLAYER_in_red_message_to_player_cmd9851 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_red_message_to_player_cmd9855 = new BitSet(new long[] { 72057594037927936L });
        FOLLOW_ESCAPED_STRING_in_red_message_to_player_cmd9859 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_red_message_to_player_cmd9861 = new BitSet(new long[] { 2L });
        FOLLOW_EMOTE_TARGETABLE_in_emote_targetable_cmd9889 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_emote_targetable_cmd9893 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_emote_targetable_cmd9895 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_BAG_KICK_in_haven_bag_kick_cmd9923 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_haven_bag_kick_cmd9927 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_haven_bag_kick_cmd9929 = new BitSet(new long[] { 2L });
        FOLLOW_HAVEN_BAG_KICK_in_haven_bag_kick_cmd9939 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_haven_bag_kick_cmd9943 = new BitSet(new long[] { 2L });
        FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd9967 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_tp_to_jail_cmd9971 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_tp_to_jail_cmd9973 = new BitSet(new long[] { 2L });
        FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd9983 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_tp_to_jail_cmd9987 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_tp_to_jail_cmd9991 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 512L });
        FOLLOW_265_in_tp_to_jail_cmd9993 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_tp_to_jail_cmd9995 = new BitSet(new long[] { 2L });
        FOLLOW_TP_TO_JAIL_in_tp_to_jail_cmd10005 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_tp_to_jail_cmd10009 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_tp_to_jail_cmd10013 = new BitSet(new long[] { 0L, 0L, 0L, 0L, 1024L });
        FOLLOW_266_in_tp_to_jail_cmd10015 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_tp_to_jail_cmd10017 = new BitSet(new long[] { 2L });
        FOLLOW_FREEDOM_in_freedom_cmd10045 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_freedom_cmd10049 = new BitSet(new long[] { 2L });
        FOLLOW_WEB_BROWSER_in_web_browser_cmd10077 = new BitSet(new long[] { 2L });
        FOLLOW_LIST_LOOT_in_listloot_cmd10105 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_listloot_cmd10109 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_listloot_cmd10111 = new BitSet(new long[] { 2L });
        FOLLOW_REVIVE_in_revive_cmd10139 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_revive_cmd10143 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_revive_cmd10145 = new BitSet(new long[] { 2L });
        FOLLOW_GIVE_ITEM_in_give_item_cmd10165 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_give_item_cmd10169 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_give_item_cmd10173 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_give_item_cmd10177 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_give_item_cmd10179 = new BitSet(new long[] { 2L });
        FOLLOW_GIVE_ITEM_in_give_item_cmd10185 = new BitSet(new long[] { 72057594172145680L });
        FOLLOW_character_pattern_in_give_item_cmd10189 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_give_item_cmd10193 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_give_item_cmd10195 = new BitSet(new long[] { 2L });
        FOLLOW_RESET_ACCOUNT_MARKET_ENTRIES_in_reset_account_market_entries_cmd10212 = new BitSet(new long[] { 0L, 8388608L });
        FOLLOW_NUMBER_in_reset_account_market_entries_cmd10216 = new BitSet(new long[] { 9007199254740992L });
        FOLLOW_ENDLINE_in_reset_account_market_entries_cmd10218 = new BitSet(new long[] { 2L });
        FOLLOW_stats_cmd_in_cmd10235 = new BitSet(new long[] { 2L });
        FOLLOW_bot_cmd_in_cmd10266 = new BitSet(new long[] { 2L });
        FOLLOW_panel_cmd_in_cmd10278 = new BitSet(new long[] { 2L });
        FOLLOW_ping_cmd_in_cmd10290 = new BitSet(new long[] { 2L });
        FOLLOW_time_cmd_in_cmd10302 = new BitSet(new long[] { 2L });
        FOLLOW_who_cmd_in_cmd10314 = new BitSet(new long[] { 2L });
        FOLLOW_where_cmd_in_cmd10326 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_to_player_cmd_in_cmd10358 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_to_coords_cmd_in_cmd10377 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_to_inst_cmd_in_cmd10395 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_player_to_me_cmd_in_cmd10416 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_player_to_coords_cmd_in_cmd10431 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_player_to_instance_cmd_in_cmd10443 = new BitSet(new long[] { 2L });
        FOLLOW_serverlock_cmd_in_cmd10453 = new BitSet(new long[] { 2L });
        FOLLOW_kick_cmd_in_cmd10480 = new BitSet(new long[] { 2L });
        FOLLOW_ban_cmd_in_cmd10512 = new BitSet(new long[] { 2L });
        FOLLOW_ghostcheck_cmd_in_cmd10545 = new BitSet(new long[] { 2L });
        FOLLOW_identphase_cmd_in_cmd10556 = new BitSet(new long[] { 2L });
        FOLLOW_shutdown_cmd_in_cmd10567 = new BitSet(new long[] { 2L });
        FOLLOW_sysmsg_cmd_in_cmd10578 = new BitSet(new long[] { 2L });
        FOLLOW_msgall_cmd_in_cmd10590 = new BitSet(new long[] { 2L });
        FOLLOW_symbiot_cmd_in_cmd10602 = new BitSet(new long[] { 2L });
        FOLLOW_nation_cmd_in_cmd10614 = new BitSet(new long[] { 2L });
        FOLLOW_achievement_cmd_in_cmd10626 = new BitSet(new long[] { 2L });
        FOLLOW_achievement_date_cmd_in_cmd10637 = new BitSet(new long[] { 2L });
        FOLLOW_zone_buff_cmd_in_cmd10647 = new BitSet(new long[] { 2L });
        FOLLOW_create_group_cmd_in_cmd10658 = new BitSet(new long[] { 2L });
        FOLLOW_add_to_group_cmd_in_cmd10669 = new BitSet(new long[] { 2L });
        FOLLOW_rights_cmd_in_cmd10680 = new BitSet(new long[] { 2L });
        FOLLOW_chaos_cmd_in_cmd10692 = new BitSet(new long[] { 2L });
        FOLLOW_restart_chaos_cmd_in_cmd10704 = new BitSet(new long[] { 2L });
        FOLLOW_create_item_cmd_in_cmd10715 = new BitSet(new long[] { 2L });
        FOLLOW_create_set_cmd_in_cmd10726 = new BitSet(new long[] { 2L });
        FOLLOW_delete_item_cmd_in_cmd10737 = new BitSet(new long[] { 2L });
        FOLLOW_regenerate_cmd_in_cmd10748 = new BitSet(new long[] { 2L });
        FOLLOW_regenerate_with_item_cmd_in_cmd10759 = new BitSet(new long[] { 2L });
        FOLLOW_turn_duration_cmd_in_cmd10769 = new BitSet(new long[] { 2L });
        FOLLOW_pvp_cmd_in_cmd10780 = new BitSet(new long[] { 2L });
        FOLLOW_run_action_cmd_in_cmd10792 = new BitSet(new long[] { 2L });
        FOLLOW_end_scenario_cmd_in_cmd10803 = new BitSet(new long[] { 2L });
        FOLLOW_reload_scenarios_cmd_in_cmd10814 = new BitSet(new long[] { 2L });
        FOLLOW_add_spellxp_cmd_in_cmd10824 = new BitSet(new long[] { 2L });
        FOLLOW_set_spelllevel_cmd_in_cmd10835 = new BitSet(new long[] { 2L });
        FOLLOW_add_skillxp_cmd_in_cmd10846 = new BitSet(new long[] { 2L });
        FOLLOW_set_skill_level_cmd_in_cmd10857 = new BitSet(new long[] { 2L });
        FOLLOW_add_xp_cmd_in_cmd10868 = new BitSet(new long[] { 2L });
        FOLLOW_set_bonus_factor_cmd_in_cmd10880 = new BitSet(new long[] { 2L });
        FOLLOW_add_money_cmd_in_cmd10890 = new BitSet(new long[] { 2L });
        FOLLOW_help_cmd_in_cmd10901 = new BitSet(new long[] { 2L });
        FOLLOW_god_mode_cmd_in_cmd10913 = new BitSet(new long[] { 2L });
        FOLLOW_instance_usage_cmd_in_cmd10924 = new BitSet(new long[] { 2L });
        FOLLOW_destroy_instance_cmd_in_cmd10935 = new BitSet(new long[] { 2L });
        FOLLOW_show_aggro_list_cmd_in_cmd10945 = new BitSet(new long[] { 2L });
        FOLLOW_play_animation_cmd_in_cmd10956 = new BitSet(new long[] { 2L });
        FOLLOW_play_aps_cmd_in_cmd10967 = new BitSet(new long[] { 2L });
        FOLLOW_set_level_cmd_in_cmd10978 = new BitSet(new long[] { 2L });
        FOLLOW_spawn_ie_cmd_in_cmd10989 = new BitSet(new long[] { 2L });
        FOLLOW_sessions_cmd_in_cmd11000 = new BitSet(new long[] { 2L });
        FOLLOW_set_next_challenge_cmd_in_cmd11011 = new BitSet(new long[] { 2L });
        FOLLOW_finish_challenge_cmd_in_cmd11021 = new BitSet(new long[] { 2L });
        FOLLOW_staff_cmd_in_cmd11031 = new BitSet(new long[] { 2L });
        FOLLOW_subscriber_cmd_in_cmd11043 = new BitSet(new long[] { 2L });
        FOLLOW_mute_partitions_cmd_in_cmd11054 = new BitSet(new long[] { 2L });
        FOLLOW_unmute_partitions_cmd_in_cmd11065 = new BitSet(new long[] { 2L });
        FOLLOW_mute_cmd_in_cmd11075 = new BitSet(new long[] { 2L });
        FOLLOW_unmute_cmd_in_cmd11087 = new BitSet(new long[] { 2L });
        FOLLOW_distribute_items_cmd_in_cmd11099 = new BitSet(new long[] { 2L });
        FOLLOW_search_cmd_in_cmd11109 = new BitSet(new long[] { 2L });
        FOLLOW_teleport_to_breed_mob_cmd_in_cmd11121 = new BitSet(new long[] { 2L });
        FOLLOW_buff_character_cmd_in_cmd11131 = new BitSet(new long[] { 2L });
        FOLLOW_restore_character_cmd_in_cmd11142 = new BitSet(new long[] { 2L });
        FOLLOW_set_item_tracker_log_level_cmd_in_cmd11152 = new BitSet(new long[] { 2L });
        FOLLOW_quota_cmd_in_cmd11161 = new BitSet(new long[] { 2L });
        FOLLOW_ragnarok_cmd_in_cmd11174 = new BitSet(new long[] { 2L });
        FOLLOW_remove_floor_items_cmd_in_cmd11185 = new BitSet(new long[] { 2L });
        FOLLOW_show_population_cmd_in_cmd11195 = new BitSet(new long[] { 2L });
        FOLLOW_show_monster_quota_cmd_in_cmd11206 = new BitSet(new long[] { 2L });
        FOLLOW_cancel_collect_cooldown_cmd_in_cmd11216 = new BitSet(new long[] { 2L });
        FOLLOW_get_instance_uid_cmd_in_cmd11226 = new BitSet(new long[] { 2L });
        FOLLOW_dump_bag_cmd_in_cmd11236 = new BitSet(new long[] { 2L });
        FOLLOW_set_wakfu_gauge_cmd_in_cmd11247 = new BitSet(new long[] { 2L });
        FOLLOW_temp_cmd_in_cmd11258 = new BitSet(new long[] { 2L });
        FOLLOW_calendar_cmd_in_cmd11270 = new BitSet(new long[] { 2L });
        FOLLOW_fight_cmd_in_cmd11281 = new BitSet(new long[] { 2L });
        FOLLOW_protector_command_in_cmd11293 = new BitSet(new long[] { 2L });
        FOLLOW_monster_group_in_cmd11304 = new BitSet(new long[] { 2L });
        FOLLOW_set_resource_speed_factor_in_cmd11315 = new BitSet(new long[] { 2L });
        FOLLOW_state_command_in_cmd11325 = new BitSet(new long[] { 2L });
        FOLLOW_scenario_cmd_in_cmd11336 = new BitSet(new long[] { 2L });
        FOLLOW_version_cmd_in_cmd11347 = new BitSet(new long[] { 2L });
        FOLLOW_plant_resources_cmd_in_cmd11359 = new BitSet(new long[] { 2L });
        FOLLOW_destroy_resources_cmd_in_cmd11370 = new BitSet(new long[] { 2L });
        FOLLOW_destroy_monsters_cmd_in_cmd11380 = new BitSet(new long[] { 2L });
        FOLLOW_set_respawn_cmd_in_cmd11390 = new BitSet(new long[] { 2L });
        FOLLOW_check_cmd_in_cmd11401 = new BitSet(new long[] { 2L });
        FOLLOW_craft_cmd_in_cmd11413 = new BitSet(new long[] { 2L });
        FOLLOW_ban_request_cmd_in_cmd11425 = new BitSet(new long[] { 2L });
        FOLLOW_ice_status_cmd_in_cmd11436 = new BitSet(new long[] { 2L });
        FOLLOW_pet_cmd_in_cmd11447 = new BitSet(new long[] { 2L });
        FOLLOW_add_itemxp_cmd_in_cmd11460 = new BitSet(new long[] { 2L });
        FOLLOW_guild_cmd_in_cmd11471 = new BitSet(new long[] { 2L });
        FOLLOW_companion_cmd_in_cmd11483 = new BitSet(new long[] { 2L });
        FOLLOW_system_configuration_cmd_in_cmd11494 = new BitSet(new long[] { 2L });
        FOLLOW_ai_cmd_in_cmd11504 = new BitSet(new long[] { 2L });
        FOLLOW_fightchallenge_cmd_in_cmd11516 = new BitSet(new long[] { 2L });
        FOLLOW_spell_command_in_cmd11527 = new BitSet(new long[] { 2L });
        FOLLOW_gem_command_in_cmd11538 = new BitSet(new long[] { 2L });
        FOLLOW_aptitude_command_in_cmd11550 = new BitSet(new long[] { 2L });
        FOLLOW_havenworld_cmd_in_cmd11561 = new BitSet(new long[] { 2L });
        FOLLOW_almanach_start_cmd_in_cmd11572 = new BitSet(new long[] { 2L });
        FOLLOW_learn_emote_cmd_in_cmd11583 = new BitSet(new long[] { 2L });
        FOLLOW_set_player_title_cmd_in_cmd11598 = new BitSet(new long[] { 2L });
        FOLLOW_free_access_cmd_in_cmd11613 = new BitSet(new long[] { 2L });
        FOLLOW_create_full_group_cmd_in_cmd11629 = new BitSet(new long[] { 2L });
        FOLLOW_inventory_cmd_in_cmd11644 = new BitSet(new long[] { 2L });
        FOLLOW_empty_char_cmd_in_cmd11665 = new BitSet(new long[] { 2L });
        FOLLOW_client_game_event_command_in_cmd11686 = new BitSet(new long[] { 2L });
        FOLLOW_rent_item_cmd_in_cmd11705 = new BitSet(new long[] { 2L });
        FOLLOW_character_cmd_in_cmd11724 = new BitSet(new long[] { 2L });
        FOLLOW_popup_message_cmd_in_cmd11745 = new BitSet(new long[] { 2L });
        FOLLOW_red_message_cmd_in_cmd11756 = new BitSet(new long[] { 2L });
        FOLLOW_red_message_to_player_cmd_in_cmd11767 = new BitSet(new long[] { 2L });
        FOLLOW_emote_targetable_cmd_in_cmd11777 = new BitSet(new long[] { 2L });
        FOLLOW_haven_bag_kick_cmd_in_cmd11787 = new BitSet(new long[] { 2L });
        FOLLOW_tp_to_jail_cmd_in_cmd11798 = new BitSet(new long[] { 2L });
        FOLLOW_freedom_cmd_in_cmd11809 = new BitSet(new long[] { 2L });
        FOLLOW_web_browser_cmd_in_cmd11821 = new BitSet(new long[] { 2L });
        FOLLOW_give_item_cmd_in_cmd11832 = new BitSet(new long[] { 2L });
        FOLLOW_listloot_cmd_in_cmd11843 = new BitSet(new long[] { 2L });
        FOLLOW_revive_cmd_in_cmd11854 = new BitSet(new long[] { 2L });
        FOLLOW_reset_account_market_entries_cmd_in_cmd11866 = new BitSet(new long[] { 2L });
        FOLLOW_hero_cmd_in_cmd11875 = new BitSet(new long[] { 2L });
    }
    
    public static class character_pattern_return extends ParserRuleReturnScope
    {
        public String pattern;
    }
    
    public static class message_return extends ParserRuleReturnScope
    {
        public String txt;
    }
    
    class DFA92 extends DFA
    {
        public DFA92(final BaseRecognizer recognizer) {
            super();
            this.recognizer = recognizer;
            this.decisionNumber = 92;
            this.eot = ModerationCommandParser.DFA92_eot;
            this.eof = ModerationCommandParser.DFA92_eof;
            this.min = ModerationCommandParser.DFA92_min;
            this.max = ModerationCommandParser.DFA92_max;
            this.accept = ModerationCommandParser.DFA92_accept;
            this.special = ModerationCommandParser.DFA92_special;
            this.transition = ModerationCommandParser.DFA92_transition;
        }
        
        public String getDescription() {
            return "1017:1: cmd returns [ModerationCommand cmd] : (c= stats_cmd |c= bot_cmd |c= panel_cmd |c= ping_cmd |c= time_cmd |c= who_cmd |c= where_cmd |c= teleport_to_player_cmd |c= teleport_to_coords_cmd |c= teleport_to_inst_cmd |c= teleport_player_to_me_cmd |c= teleport_player_to_coords_cmd |c= teleport_player_to_instance_cmd |c= serverlock_cmd |c= kick_cmd |c= ban_cmd |c= ghostcheck_cmd |c= identphase_cmd |c= shutdown_cmd |c= sysmsg_cmd |c= msgall_cmd |c= symbiot_cmd |c= nation_cmd |c= achievement_cmd |c= achievement_date_cmd |c= zone_buff_cmd |c= create_group_cmd |c= add_to_group_cmd |c= rights_cmd |c= chaos_cmd |c= restart_chaos_cmd |c= create_item_cmd |c= create_set_cmd |c= delete_item_cmd |c= regenerate_cmd |c= regenerate_with_item_cmd |c= turn_duration_cmd |c= pvp_cmd |c= run_action_cmd |c= end_scenario_cmd |c= reload_scenarios_cmd |c= add_spellxp_cmd |c= set_spelllevel_cmd |c= add_skillxp_cmd |c= set_skill_level_cmd |c= add_xp_cmd |c= set_bonus_factor_cmd |c= add_money_cmd |c= help_cmd |c= god_mode_cmd |c= instance_usage_cmd |c= destroy_instance_cmd |c= show_aggro_list_cmd |c= play_animation_cmd |c= play_aps_cmd |c= set_level_cmd |c= spawn_ie_cmd |c= sessions_cmd |c= set_next_challenge_cmd |c= finish_challenge_cmd |c= staff_cmd |c= subscriber_cmd |c= mute_partitions_cmd |c= unmute_partitions_cmd |c= mute_cmd |c= unmute_cmd |c= distribute_items_cmd |c= search_cmd |c= teleport_to_breed_mob_cmd |c= buff_character_cmd |c= restore_character_cmd |c= set_item_tracker_log_level_cmd |c= quota_cmd |c= ragnarok_cmd |c= remove_floor_items_cmd |c= show_population_cmd |c= show_monster_quota_cmd |c= cancel_collect_cooldown_cmd |c= get_instance_uid_cmd |c= dump_bag_cmd |c= set_wakfu_gauge_cmd |c= temp_cmd |c= calendar_cmd |c= fight_cmd |c= protector_command |c= monster_group |c= set_resource_speed_factor |c= state_command |c= scenario_cmd |c= version_cmd |c= plant_resources_cmd |c= destroy_resources_cmd |c= destroy_monsters_cmd |c= set_respawn_cmd |c= check_cmd |c= craft_cmd |c= ban_request_cmd |c= ice_status_cmd |c= pet_cmd |c= add_itemxp_cmd |c= guild_cmd |c= companion_cmd |c= system_configuration_cmd |c= ai_cmd |c= fightchallenge_cmd |c= spell_command |c= gem_command |c= aptitude_command |c= havenworld_cmd |c= almanach_start_cmd |c= learn_emote_cmd |c= set_player_title_cmd |c= free_access_cmd |c= create_full_group_cmd |c= inventory_cmd |c= empty_char_cmd |c= client_game_event_command |c= rent_item_cmd |c= character_cmd |c= popup_message_cmd |c= red_message_cmd |c= red_message_to_player_cmd |c= emote_targetable_cmd |c= haven_bag_kick_cmd |c= tp_to_jail_cmd |c= freedom_cmd |c= web_browser_cmd |c= give_item_cmd |c= listloot_cmd |c= revive_cmd |c= reset_account_market_entries_cmd |c= hero_cmd );";
        }
    }
}
