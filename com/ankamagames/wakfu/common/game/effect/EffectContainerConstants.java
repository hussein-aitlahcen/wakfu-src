package com.ankamagames.wakfu.common.game.effect;

public class EffectContainerConstants
{
    public static final int HAVEN_WORLD_CONTAINER_ID = 1;
    public static final int ACHIEVEMENT_CONTAINER_ID = 1;
    public static final int GUILD_CONTAINER_ID = 1;
    public static final int ANTI_ADDICTION_CONTAINER_ID = 1;
    public static final int SUBSCRIPTION_STATE_CONTAINER_ID = 1;
    public static final WakfuEffectContainer HAVEN_WORLD_CONTAINER;
    public static final WakfuEffectContainer ACHIEVEMENT_BONUS_CONTAINER;
    public static final WakfuEffectContainer GUILD_CONTAINER;
    public static final WakfuEffectContainer ANTI_ADDICTION_CONTAINER;
    public static final WakfuEffectContainer SUBSCRIPTION_STATE_CONTAINER;
    
    static {
        HAVEN_WORLD_CONTAINER = new WakfuEffectContainerBuilder().setContainerId(1L).setContainerType(28).build();
        ACHIEVEMENT_BONUS_CONTAINER = new WakfuEffectContainerBuilder().setContainerId(1L).setContainerType(30).build();
        GUILD_CONTAINER = new WakfuEffectContainerBuilder().setContainerId(1L).setContainerType(32).build();
        ANTI_ADDICTION_CONTAINER = new WakfuEffectContainerBuilder().setContainerId(1L).setContainerType(34).build();
        SUBSCRIPTION_STATE_CONTAINER = new WakfuEffectContainerBuilder().setContainerId(1L).setContainerType(35).build();
    }
}
