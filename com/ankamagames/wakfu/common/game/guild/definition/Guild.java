package com.ankamagames.wakfu.common.game.guild.definition;

import gnu.trove.*;

public interface Guild
{
    public static final int LADDER_PAGE_SIZE = 9;
    
    long getId();
    
    long getBestRank();
    
    long getWorstRank();
    
    String getName();
    
    long getBlazon();
    
    String getDescription();
    
    String getMessage();
    
    short getLevel();
    
    int getNationId();
    
    int getCurrentGuildPoints();
    
    int getTotalGuildPoints();
    
    GuildMember getMember(long p0);
    
    GuildRank getRank(long p0);
    
    GuildBonus getBonus(int p0);
    
    int memberSize();
    
    boolean forEachMember(TObjectProcedure<GuildMember> p0);
    
    int rankSize();
    
    boolean forEachRank(TObjectProcedure<GuildRank> p0);
    
    int bonusSize();
    
    boolean forEachBonus(TObjectProcedure<GuildBonus> p0);
    
    boolean addListener(GuildListener p0);
    
    boolean removeListener(GuildListener p0);
    
    GuildBonusConstantManager getConstantManager();
    
    int getWeeklyPointsLimit();
    
    int getEarnedPointsWeekly();
    
    int getLastEarningPointWeek();
    
    void setLastEarningPointWeek(int p0);
    
    void setEarnedPointsWeekly(int p0);
}
