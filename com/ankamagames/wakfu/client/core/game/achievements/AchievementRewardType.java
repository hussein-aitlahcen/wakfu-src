package com.ankamagames.wakfu.client.core.game.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;

public enum AchievementRewardType implements RewardType
{
    GIVE_PET_EQUIPMENT(1, false, true), 
    GIVE_PLAYER_TITLE(2, false, true), 
    GIVE_PLAYER_EMOTE(3, false, true), 
    GIVE_PLAYER_LANDMARK(4, false, true), 
    GIVE_PLAYER_ITEM(5, false, true), 
    GIVE_PLAYER_XP(6, true, true), 
    GIVE_PLAYER_APTITUDE_POINTS(7, true, true), 
    SPELLS_RESTAT(8, false, true), 
    APPLY_STATE_EFFECTS(9, false, true), 
    LEARN_RECIPE(10, false, true), 
    GIVE_KAMAS(11, false, true), 
    GIVE_GUILD_POINTS(12, false, true), 
    GIVE_PLAYER_XP_IN_PERCENT(13, false, true), 
    VALIDATE_STEAM_ACHIEVEMENT(14, false, false), 
    REGISTER_ZAAP(15, false, false);
    
    private final int m_id;
    private final boolean m_text;
    private boolean m_display;
    private static AchievementRewardType[] m_values;
    
    private AchievementRewardType(final int id, final boolean text, final boolean display) {
        this.m_id = id;
        this.m_text = text;
        this.m_display = display;
    }
    
    public static AchievementRewardType fromId(final int id) {
        if (AchievementRewardType.m_values == null) {
            AchievementRewardType.m_values = values();
        }
        for (final AchievementRewardType value : AchievementRewardType.m_values) {
            if (value.m_id == id) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public boolean isTextReward() {
        return this.m_text;
    }
    
    @Override
    public boolean isDisplayed() {
        return this.m_display;
    }
}
