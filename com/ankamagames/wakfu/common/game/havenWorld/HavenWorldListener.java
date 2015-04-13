package com.ankamagames.wakfu.common.game.havenWorld;

public interface HavenWorldListener
{
    void guildChanged(GuildInfo p0);
    
    void buildingAdded(Building p0);
    
    void buildingRemoved(Building p0);
    
    void partitionAdded(Partition p0);
    
    void partitionChanged(Partition p0);
    
    void resourcesChanged(int p0);
}
