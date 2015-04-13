package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public interface CriterionConstants
{
    public static final String USE_TARGET = "target";
    public static final String USE_CASTER = "caster";
    public static final String USE_TARGET_TEAM = "targetTeam";
    public static final String USE_CASTER_TEAM = "casterTeam";
    public static final String USE_TRIGGERING_TARGET = "triggeringTarget";
    public static final String USE_TRIGGERING_CASTER = "triggeringCaster";
    public static final String USE_CASTER_CONTROLLER = "casterController";
    public static final String EVENT_TARGET = "eventTarget";
    public static final ParserType[] EMPTY_SIGNATURE = new ParserType[0];
    public static final ParserType[] ONE_STRING_SIGNATURE = { ParserType.STRING };
    public static final ParserType[] ONE_BOOLEAN_SIGNATURE = { ParserType.BOOLEAN };
    public static final ParserType[] ONE_NUMBER_SIGNATURE = { ParserType.NUMBER };
}
