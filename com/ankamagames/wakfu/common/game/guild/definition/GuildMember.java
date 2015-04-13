package com.ankamagames.wakfu.common.game.guild.definition;

public interface GuildMember
{
    long getId();
    
    int getGuildPoints();
    
    long getRank();
    
    String getName();
    
    long getXp();
    
    boolean isConnected();
    
    byte getSmiley();
    
    byte getSex();
    
    short getBreedId();
    
    int getNationId();
    
    boolean addListener(GuildMemberListener p0);
    
    boolean removeListener(GuildMemberListener p0);
}
