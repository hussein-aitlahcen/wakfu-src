package com.ankamagames.wakfu.common.game.aptitudeNewVersion.dataTest;

import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;

public final class AptitudeBonusTest
{
    public static final AptitudeBonusModel DEG_MONO_BOOST;
    public static final AptitudeBonusModel DEG_BI_BOOST;
    public static final AptitudeBonusModel DEG_ALL_BOOST;
    public static final AptitudeBonusModel DEG_MONO_CLOSE_BOOST;
    public static final AptitudeBonusModel DEG_ZONE_CLOSE_BOOST;
    public static final AptitudeBonusModel DEG_MONO_RANGE_BOOST;
    public static final AptitudeBonusModel DEG_ZONE_RANGE_BOOST;
    public static final AptitudeBonusModel HP_MAX_BOOST;
    public static final AptitudeBonusModel RES_BOOST;
    public static final AptitudeBonusModel HP_REGEN_BY_TURN_BOOST;
    public static final AptitudeBonusModel RECEIVED_HEAL_BOOST;
    public static final AptitudeBonusModel LOCK_BOOST;
    public static final AptitudeBonusModel DODGE_BOOST;
    public static final AptitudeBonusModel INIT_BOOST;
    public static final AptitudeBonusModel LOCK_N_DODGE_BOOST;
    public static final AptitudeBonusModel CLOSE_LOCK_BOOST;
    public static final AptitudeBonusModel LOW_LIFE_DODGE_BOOST;
    public static final AptitudeBonusModel CRITICAL_BOOST;
    public static final AptitudeBonusModel BLOCK_BOOST;
    public static final AptitudeBonusModel CRITICAL_DAMAGES_BOOST;
    public static final AptitudeBonusModel BACKSTAB_BOOST;
    public static final AptitudeBonusModel BERZERK_DAMAGES_BOOST;
    public static final AptitudeBonusModel HEAL_BOOST;
    public static final AptitudeBonusModel NON_CRITICAL_BOOST;
    public static final AptitudeBonusModel PA_BOOST;
    public static final AptitudeBonusModel PM_BOOST;
    public static final AptitudeBonusModel PO_BOOST;
    public static final AptitudeBonusModel PW_BOOST;
    public static final AptitudeBonusModel CONTROL_BOOST;
    public static final AptitudeBonusModel KIT_SKILL_BOOST;
    
    static {
        DEG_MONO_BOOST = new AptitudeBonusModel(10, 0);
        DEG_BI_BOOST = new AptitudeBonusModel(11, 0);
        DEG_ALL_BOOST = new AptitudeBonusModel(12, 128226);
        DEG_MONO_CLOSE_BOOST = new AptitudeBonusModel(13, 128227);
        DEG_ZONE_CLOSE_BOOST = new AptitudeBonusModel(14, 128233);
        DEG_MONO_RANGE_BOOST = new AptitudeBonusModel(15, 128230);
        DEG_ZONE_RANGE_BOOST = new AptitudeBonusModel(16, 128236);
        HP_MAX_BOOST = new AptitudeBonusModel(20, 128218);
        RES_BOOST = new AptitudeBonusModel(21, 128219, 10);
        HP_REGEN_BY_TURN_BOOST = new AptitudeBonusModel(22, 128240, 5);
        RECEIVED_HEAL_BOOST = new AptitudeBonusModel(23, 0);
        LOCK_BOOST = new AptitudeBonusModel(30, 128220);
        DODGE_BOOST = new AptitudeBonusModel(31, 128221);
        INIT_BOOST = new AptitudeBonusModel(32, 128225);
        LOCK_N_DODGE_BOOST = new AptitudeBonusModel(33, 128222);
        CLOSE_LOCK_BOOST = new AptitudeBonusModel(34, 0);
        LOW_LIFE_DODGE_BOOST = new AptitudeBonusModel(35, 0);
        CRITICAL_BOOST = new AptitudeBonusModel(41, 128209, 20);
        BLOCK_BOOST = new AptitudeBonusModel(42, 128210, 20);
        CRITICAL_DAMAGES_BOOST = new AptitudeBonusModel(40, 128211);
        BACKSTAB_BOOST = new AptitudeBonusModel(43, 128212);
        BERZERK_DAMAGES_BOOST = new AptitudeBonusModel(44, 128213);
        HEAL_BOOST = new AptitudeBonusModel(45, 128214);
        NON_CRITICAL_BOOST = new AptitudeBonusModel(46, 128239);
        PA_BOOST = new AptitudeBonusModel(50, 128200, 1);
        PM_BOOST = new AptitudeBonusModel(51, 128217, 1);
        PO_BOOST = new AptitudeBonusModel(52, 128216, 1);
        PW_BOOST = new AptitudeBonusModel(53, 128205, 1);
        CONTROL_BOOST = new AptitudeBonusModel(54, 128215, 1);
        KIT_SKILL_BOOST = new AptitudeBonusModel(55, 128208, 1);
    }
}
