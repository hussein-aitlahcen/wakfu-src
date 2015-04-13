package com.ankamagames.wakfu.client.core.game.ressource;

public interface CollectConstants
{
    public static final String BAR_ICONPICK = "iconpick";
    public static final String BAR_ICONPHARMA = "iconpharma";
    public static final String BAR_ICONREAP = "iconreap";
    public static final String BAR_ICONAXE = "iconaxe";
    public static final String BAR_ICONPICKAXE = "iconpickaxe";
    public static final String BAR_ICONBAG = "iconbag";
    public static final String BAR_ICONHAND = "iconcollect";
    public static final String BAR_ICONFIGHT = "iconfight";
    
    public enum State
    {
        AVAILABLE, 
        INVALID_CRITERION, 
        IMPOSSIBLE, 
        WRONG_LEVEL, 
        IN_FIGHT, 
        ALREADY_COLLECTED, 
        ALREADY_COLLECTING, 
        UNUSABLE_CRAFT, 
        NO_CONSUMABLE, 
        WRONG_ITEM_EQUIPED, 
        NOT_ON_FRIEND_NATION, 
        NO_NATION, 
        TIMER_NOT_FINISHED, 
        PLAYER_NOT_SUBSCRIBED;
    }
}
