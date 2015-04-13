package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

public interface RewardType
{
    boolean isTextReward();
    
    int getId();
    
    boolean isDisplayed();
}
