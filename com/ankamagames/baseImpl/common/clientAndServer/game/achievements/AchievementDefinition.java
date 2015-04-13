package com.ankamagames.baseImpl.common.clientAndServer.game.achievements;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface AchievementDefinition
{
    int getId();
    
    int getDuration();
    
    long getCooldown();
    
    boolean isRepeatable();
    
    GameDateConst getPeriodStartDate();
    
    GameIntervalConst getPeriod();
    
    byte getRank();
    
    int getOrder();
    
    boolean canRepeatIfFailed();
}
