package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.lang.reflect.*;

public abstract class WakfuEffectContainerType extends EffectContainerType
{
    public static final int SPELL = 11;
    public static final int ITEM = 12;
    public static final int ITEMSETLEVEL = 14;
    public static final int SCRIPT_ACTIONS = 15;
    public static final int NATION_BUFF = 16;
    public static final int APTITUDE = 17;
    public static final int ZONE_BUFF = 18;
    public static final int PROTECTOR_BUFF = 19;
    public static final int DUMMY = 20;
    public static final int TIMELINE_BUFF_LIST = 21;
    public static final int DEBUG = 22;
    public static final int DESTRUCTIBLE_MACHINE = 23;
    public static final int BOMB_SPECIAL_ZONE = 24;
    public static final int PASSIVE_SPELL = 25;
    public static final int DEAD_STATE = 26;
    public static final int ARCADE_DUNGEON_BONUS = 27;
    public static final int HAVEN_WORLD_BUFF = 28;
    public static final int GENERIC_IE = 29;
    public static final int ACHIEVEMENT_BONUS = 30;
    public static final int BAGS = 31;
    public static final int GUILD = 32;
    public static final int PROTECTOR_GENERIC_BUFF = 33;
    public static final int ANTI_ADDICTION_BUFF = 34;
    public static final int SUBSCRIPTION_STATE_CONTAINER = 35;
    public static final int NEW_APTITUDE = 36;
    
    public static String getTypeName(final int typeId) {
        for (final Field field : WakfuEffectContainerType.class.getFields()) {
            try {
                if (field.getInt(null) == typeId) {
                    return field.getName();
                }
            }
            catch (IllegalAccessException e) {}
        }
        return "UNKNOWN_CONTAINER_TYPE (" + typeId + ")";
    }
}
