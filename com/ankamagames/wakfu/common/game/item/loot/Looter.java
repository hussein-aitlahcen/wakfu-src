package com.ankamagames.wakfu.common.game.item.loot;

import com.ankamagames.wakfu.common.account.subscription.*;

public interface Looter
{
    long getId();
    
    Object getCriterionContext();
    
    int getProspection();
    
    short getInstanceId();
    
    int getBonusTries();
    
    boolean alwaysLoot();
    
    Looter getLootReceiver();
    
    float getLootRatio();
    
    SubscriptionLevel getSubscriptionLevel();
}
