package com.ankamagames.wakfu.common.game.guild.definition;

public interface GuildListener
{
    void nameChanged();
    
    void blazonChanged();
    
    void descriptionChanged();
    
    void messageChanged();
    
    void levelChanged(short p0);
    
    void nationIdChanged(int p0);
    
    void currentGuildPointsChanged(int p0);
    
    void totalGuildPointsChanged(int p0);
    
    void rankAdded(GuildRank p0);
    
    void rankMoved(GuildRank p0);
    
    void rankRemoved(GuildRank p0);
    
    void memberAdded(GuildMember p0);
    
    void memberRemoved(GuildMember p0);
    
    void bonusAdded(GuildBonus p0);
    
    void bonusRemoved(GuildBonus p0);
    
    void rankChanged(GuildRank p0);
    
    void memberChanged(GuildMember p0);
    
    void bonusActivated(GuildBonus p0);
    
    void earnedPointsWeeklyChanged(int p0);
    
    void lastEarningPointWeekChanged(int p0);
}
